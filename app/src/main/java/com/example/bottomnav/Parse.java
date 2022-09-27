package com.example.bottomnav;

import org.eclipse.cdt.core.dom.ast.IASTDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.dom.ast.gnu.cpp.GPPLanguage;
import org.eclipse.cdt.core.index.IIndex;
import org.eclipse.cdt.core.parser.*;
import org.eclipse.cdt.internal.core.dom.parser.cpp.*;

import java.io.*;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parse {
    private HashMap<String, Optional<DataDecoder>> decoderRepo= new HashMap<>();
    private HashMap<Integer, String> IdToName = new HashMap<>();

    public static Parse parseTextFile(String fileName) throws Exception {
        char[] code = Parse.OpenTextFile(fileName);
        return new Parse(code);
    }

    public Parse(char[] code) throws Exception {
        IASTTranslationUnit translationUnit = getIASTTranslationUnit(code);
        IASTNode[] comments = translationUnit.getComments();
        IASTNode[] children = translationUnit.getChildren();
        HashMap<String, Integer> NameToId = new HashMap<>();

        for (IASTNode child : children) {
            if (child instanceof CPPASTSimpleDeclaration) {
                CPPASTSimpleDeclaration declaration = (CPPASTSimpleDeclaration) child;
                if (declaration.getDeclSpecifier() instanceof CPPASTCompositeTypeSpecifier) {
                    CPPASTCompositeTypeSpecifier compSpec =
                            (CPPASTCompositeTypeSpecifier) declaration.getDeclSpecifier();
                    CPPASTName name = (CPPASTName) compSpec.getName();
                    storeStructDecoder(compSpec.getDeclarations(false),
                            name.getRawSignature());
                } else {
                    storeConstDecoder(declaration);
                }
            }
        }
        parseComments(comments);
    }

    private void parseComments(IASTNode[] comments) {
        Pattern pattern = Pattern.compile("@canPayloadStruct\\s+(\\S+)\\s*=\\s*(\\S+)");
        for (IASTNode comment : comments) {
            Matcher matcher = pattern.matcher(comment.getRawSignature());
            if (matcher.find()) { // associate ID to struct
                String canName = matcher.group(1);
                String structName = matcher.group(2);
                decoderRepo.put(canName, decoderRepo.get(structName));
            }
        }
    }

    /**
     * Creates and stores a Primitive decoder if declaration is a Const
     * @param declaration
     * @throws Exception
     */
    private void storeConstDecoder(CPPASTSimpleDeclaration declaration) throws Exception {
        Optional<VariableContents> data = VariableContents.getContents(declaration);
        if (data.isPresent() && data.get().value.isPresent()) {
            VariableContents variableContents = data.get();
            decoderRepo.put(variableContents.name, PrimitiveDecoder.create(variableContents));
            IdToName.put(Integer.decode(variableContents.value.get()), variableContents.name);
        }
    }

    /**
     * Iterates over declarations of struct, makes array variableContents, then creates and stores
     * struct decoder with array of variableContents
     */
    private void storeStructDecoder(IASTDeclaration[] declarations, String name) throws Exception {
        ArrayList<VariableContents> variables = new ArrayList<>();
        for (IASTDeclaration element : declarations) {
            CPPASTSimpleDeclaration declaration = (CPPASTSimpleDeclaration) element;
            Optional<VariableContents> data = VariableContents.getContents(declaration);
            if (data.isPresent() && !data.get().value.isPresent()) {
                variables.add(data.get());
            }
        }
        decoderRepo.put(name, StructDecoder.create(variables));
    }

    public Optional<DataDecoder> getDecoder(String name) {
        if (!decoderRepo.containsKey(name)) {
            return Optional.empty();
        } return decoderRepo.get(name);
    }

    public Optional<DataDecoder> getDecoder(Integer canId) {
        return getDecoder(IdToName.get(canId));
    }

    /**
     * The decoders return a string format of 'NAME: Value, NAME: Value, ...'
     * Therefore, parseStringResults returns an array of Decoded objects that allows user to iterate
     * over and use functions like getName and getValue in for each Decoded object
     *
     * @return
     */
    public ArrayList<DecodedContents> parseDecodedString(String resultOfDecoder) {
        ArrayList<DecodedContents> nameValues = new ArrayList<>();
        String[] splitString = resultOfDecoder.split(",");
        Pattern pattern = Pattern.compile("(\\S+):\\s(\\S+)");
        for (String decoded : splitString) {
            Matcher matcher = pattern.matcher(decoded);
            if (matcher.find()) {
                String name = matcher.group(1);
                String value = matcher.group(2);
                nameValues.add(new DecodedContents(name, value));
            }
        } return nameValues;
    }

    static char[] OpenTextFile(String fileName) throws IOException {
        //Source: https://www.oreilly.com/content/how-to-convert-an-inputstream-to-a-string/
        InputStream is = Parse.class.getClassLoader().getResourceAsStream(fileName);
        if (is != null) {
            ByteArrayOutputStream barOutStream = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int length;
            while ((length = is.read(buf)) != -1) {
                barOutStream.write(buf, 0, length);
            }
            return barOutStream.toString().toCharArray();
        } else {
            throw new FileNotFoundException();
        }
    }

    private static IASTTranslationUnit getIASTTranslationUnit(char[] code) throws Exception {
        FileContent fc = FileContent.create("ParseFile", code);
        Map<String, String> macroDefinitions = new HashMap();
        String[] includeSearchPaths = new String[0];
        IScannerInfo si = new ScannerInfo(macroDefinitions, includeSearchPaths);
        IncludeFileContentProvider ifcp = IncludeFileContentProvider.getEmptyFilesProvider();
        IIndex idx = null;
        int options = 8;
        IParserLogService log = new DefaultLogService();
        return GPPLanguage.getDefault().getASTTranslationUnit(fc, si, ifcp, (IIndex)idx, options, log);
    }
}
