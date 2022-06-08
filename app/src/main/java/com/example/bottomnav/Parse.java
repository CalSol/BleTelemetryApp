package com.example.bottomnav;

import android.os.Build;

import androidx.annotation.RequiresApi;

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
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parse {
    // Const name -> ConstContents
    public HashMap<String, ConstContents> constRepo = new HashMap<>();
    // Struct name -> StructContents
    public HashMap<String, ArrayList<StructContents>> structRepo = new HashMap<>();
    // CAN ID Name -> CAN Struct
    public HashMap<String, String> canStructRepo = new HashMap<>();
    // CAN ID -> CAN ID Name
    public HashMap<Integer, String> canIdRepo = new HashMap<>();
    // Variable name or CAN ID -> decoded value
    public HashMap payloadMap = new HashMap();

    public static Parse parseTextFile(String fileName) throws Exception {
        char[] code = Parse.OpenTextFile(fileName);
        return new Parse(code);
    }

    public Parse(char[] code) throws Exception {
        IASTTranslationUnit translationUnit = getIASTTranslationUnit(code);
        IASTNode[] comments = translationUnit.getComments();
        IASTNode[] children = translationUnit.getChildren();

        for (IASTNode child : children) {
            if (child instanceof CPPASTSimpleDeclaration) {
                CPPASTSimpleDeclaration declaration = (CPPASTSimpleDeclaration) child;
                if (declaration.getDeclSpecifier() instanceof CPPASTCompositeTypeSpecifier) {
                    CPPASTCompositeTypeSpecifier compSpec =
                            (CPPASTCompositeTypeSpecifier) declaration.getDeclSpecifier();
                    CPPASTName name = (CPPASTName) compSpec.getName();
                    storeStuct(compSpec.getDeclarations(false),
                            name.getRawSignature());
                } else {
                    storeConst(declaration);
                }
            }
        }
        parseComments(comments);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void decode(int canID, byte[] payload) {
        String payloadDataType = getConst(canID).payLoadDataType;
        switch (payloadDataType){
            case "single":
                payloadMap.put(canID,
                        ByteBuffer.wrap(payload).order(ByteOrder.LITTLE_ENDIAN).getFloat());
                break;
            case "struct":
                ArrayList<StructContents> structContents = getStruct(canID);
                int incr = payload.length / structContents.size();
                for (int i = 0; i < structContents.size(); i++) {
                    byte[] byteArray = getBytes(payload, i * incr, incr);
                    if (incr == 1) {
                        payloadMap.put(structContents.get(i).name, (int) byteArray[0]);
                    } else {
                        payloadMap.put(structContents.get(i).name,
                                ByteBuffer.wrap(byteArray).order(ByteOrder.LITTLE_ENDIAN).getFloat());
                    }
                }
                break;
        }
    }

    private byte[] getBytes(byte[] payload, int start, int num) {
        byte[] bytes = new byte[num];
        for (int i = 0; i < num; i++) {
            bytes[0] = payload[start + i];
        }
        return bytes;
    }

    private void storeConst(CPPASTSimpleDeclaration declaration) throws Exception {
        Optional<Components> data = Components.Deconstruct(declaration);
        if (data.isPresent() && data.get().init.isPresent()) {
            Components comp = data.get();
            CPPASTLiteralExpression value =
                    (CPPASTLiteralExpression) comp.init.get().getInitializerClause();
            ConstContents contents = new ConstContents(comp.name, value, comp.typeQualifier,
                    comp.type);
            Pattern pattern = Pattern.compile("\\dx(\\d+)");
            Matcher matcher = pattern.matcher(contents.value);
            if (matcher.find()) {
                canIdRepo.put(Integer.parseInt(matcher.group(1), 16), contents.name);
            }
            constRepo.put(contents.name, contents);
        }
    }

    private void storeStuct(IASTDeclaration[] declarations, String name) throws Exception {
        ArrayList<StructContents> struct = new ArrayList();
        for (IASTDeclaration element : declarations) {
            CPPASTSimpleDeclaration declaration = (CPPASTSimpleDeclaration) element;
            Optional<Components> data = Components.Deconstruct(declaration);
            if (data.isPresent() && !data.get().init.isPresent()) {
                Components comp = data.get();
                StructContents contents = new StructContents(comp.name, comp.type);
                struct.add(contents);
            }
        }
        structRepo.put(name, struct);
    }

    private void parseComments(IASTNode[] comments) {
        Pattern pattern1 = Pattern.compile("@canPayloadStruct\\s+(\\S+)\\s*=\\s*(\\S+)");
        Pattern pattern2 = Pattern.compile("@payloadDataType\\s+(\\S+)\\s+=\\s*(\\S+)");
        for (IASTNode comment : comments) {
            Matcher matcher1 = pattern1.matcher(comment.getRawSignature());
            Matcher matcher2 = pattern2.matcher(comment.getRawSignature());
            if (matcher1.find()) { // associate ID to struct
                canStructRepo.put(matcher1.group(1), matcher1.group(2));
            } else if (matcher2.find()) { // assoicate payload data type
                getConst(matcher2.group(1)).payLoadDataType = matcher2.group(2);
            }
        }
    }

    // Given a CAN ID name, retrieves its associated struct contents
    public ArrayList<StructContents> getCanStruct(String idName) {
        return getStruct(canStructRepo.get(idName));
    }

    // Given const name, returns its contents
    public ConstContents getConst(String key) {
        return constRepo.get(key);
    }

    public ConstContents getConst(int canId) {
        return getConst(canIdRepo.get(canId));
    }

    // Given id number, return struct contents
    public ArrayList<StructContents> getStruct(int canId) {
        return getCanStruct(canIdRepo.get(canId));
    }

    // Given id name. return struct contents
    public ArrayList<StructContents> getStruct(String canIDName) {
        return structRepo.get(canIDName);
    }

    private static char[] OpenTextFile(String fileName) throws IOException {
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

    public static IASTTranslationUnit getIASTTranslationUnit(char[] code) throws Exception {
        FileContent fc = FileContent.create("TestFile", code);
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
