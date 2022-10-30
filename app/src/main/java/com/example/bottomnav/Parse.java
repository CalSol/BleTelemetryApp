package com.example.bottomnav;

import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.dom.ast.gnu.cpp.GPPLanguage;
import org.eclipse.cdt.core.index.IIndex;
import org.eclipse.cdt.core.parser.*;

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
    private HashMap<String, DecoderData> decoderRepo = new HashMap<>();
    private HashMap<Integer, String> idToName = new HashMap<>();

    // Static function parseTextFile converts a file into array of char and creates Parse object
    public static Parse parseTextFile(String fileName) throws Exception {
        char[] code = Parse.OpenTextFile(fileName);
        return new Parse(code);
    }

    // Initialization function Parse looks for Constants and Structures, stores accordingly.
    // If conditional statements are not met, it simply ignores invalid objects.
    public Parse(char[] code) throws Exception {
        IASTTranslationUnit translationUnit = getIASTTranslationUnit(code);
        IASTNode[] comments = translationUnit.getComments();
        IASTNode[] children = translationUnit.getChildren();
        for (IASTNode child : children) {
            Optional<Declaration> declaration = Declaration.create(child);
            if (declaration.isPresent()) {
                if (declaration.get() instanceof DeclarationStruct) {
                    storeStructDecoder((DeclarationStruct) declaration.get());
                 }
                if (declaration.get() instanceof DeclarationVariable) {
                    storeConstDecoder((DeclarationVariable) declaration.get());
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
     * Given a set of declaration, iterates through all members and checks if member is another struct
     * or primitive. If its a struct, it'll recursively call parseMembers to create a new decoder.
     * Otherwise, it'll parse the statement to create a primitive decoder for the member.
     * */
    private Optional<DecoderData> parseMembers(ArrayList<Optional<Declaration>> declarations) {
        ArrayList<DecoderData> members = new ArrayList<>();
        int totalStructByteSize = 0;  // Need to track the total size of the structure in making
        for (Optional<Declaration> declaration : declarations) {
            Optional<DecoderData> decoder = Optional.empty();
            if (declaration.isPresent()) {
                if (declaration.get() instanceof DeclarationStruct) {
                    decoder = parseMembers(((DeclarationStruct) declaration.get()).getDeclarations());
                }
                if (declaration.get() instanceof DeclarationVariable) {
                    decoder = DecoderPrimitive.create((
                            (DeclarationVariable) declaration.get()).getStatement().getContents());
                }
                if (decoder.isPresent()) {
                    members.add(decoder.get());
                    totalStructByteSize += decoder.get().getTypeSize();
                }
            }
        }
        return DecoderStruct.create(declarations.size(), totalStructByteSize, members);
    }

    // Creates and stores a Primitive decoder if declaration is a Const. Precondition for declaration
    // in parse function
    private void storeConstDecoder(DeclarationVariable declaration) {
        VariableContents contents = declaration.getStatement().getContents();
        Optional<DecoderData> decoder = DecoderPrimitive.create(contents);
        if (decoder.isPresent()){
            decoderRepo.put(contents.name, decoder.get());
            idToName.put(Integer.decode(contents.value), contents.name);
        }
    }

     // Creates and stores a DecoderStruct. It's name is used as key for decoderRepo. Names of nested
     // structs are not used for decodedRepo. Precondition for declaration in parse function.
    private void storeStructDecoder(DeclarationStruct declaration) {
        Optional<DecoderData> decoderStruct = parseMembers(declaration.getDeclarations());
        if (decoderStruct.isPresent()) {
            decoderRepo.put(declaration.getName(), decoderStruct.get());
        }
    }

    public Optional<DecodedData> decode(String name, byte[] payload) {
        if (decoderRepo.containsKey(name)) {
            return decoderRepo.get(name).decodeToData(payload);
        }
        return Optional.empty();
    }

    public Optional<DecodedData> decode(Integer canId, byte[] payload) {
        return decode(idToName.get(canId), payload);
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
