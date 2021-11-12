package com.company;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.cdt.core.dom.ast.*;
import org.eclipse.cdt.core.dom.ast.gnu.cpp.GPPLanguage;
import org.eclipse.cdt.core.index.IIndex;
import org.eclipse.cdt.core.parser.DefaultLogService;
import org.eclipse.cdt.core.parser.FileContent;
import org.eclipse.cdt.core.parser.IParserLogService;
import org.eclipse.cdt.core.parser.IScannerInfo;
import org.eclipse.cdt.core.parser.IncludeFileContentProvider;
import org.eclipse.cdt.core.parser.ScannerInfo;

public class _CDTParser {
    String sourceCode;
    IASTTranslationUnit translationUnit;
    String result;
    HashMap<Integer, String> ids;
    HashMap<Integer, String> value;
    int index1;
    int index2;

    public _CDTParser(String Code) throws Exception {
        this.sourceCode = Code;
        this.result = "";
        this.ids = new HashMap<>();
        this.value = new HashMap<>();
        index1 = 0;
        index2 = 0;
        this.translationUnit = getIASTTranslationUnit(this.sourceCode.toCharArray());



        IASTPreprocessorIncludeStatement[] includes = translationUnit.getIncludeDirectives(); //wtf does this do?
        for (IASTPreprocessorIncludeStatement include : includes) {
            System.out.println("include - " + include.getName());
        }



        //ok
        ASTVisitor visitor = makeNewASTVisitor();

        visitor.shouldVisitNames = true;
        visitor.shouldVisitInitializers = true;

        this.translationUnit.accept(visitor);
    }

    public ASTVisitor makeNewASTVisitor() {
        return new ASTVisitor() {
            public int visit(IASTInitializer initializer) {
                String rawSignature = initializer.getRawSignature();
                ids.put(index1, rawSignature);
                result += rawSignature + " ";
                index1 += 1;
                return 3;
            }
            public int visit(IASTName name) {
                String rawSignature = name.getRawSignature();
                value.put(index2, rawSignature);
                result += rawSignature + " ";
                index2 += 1;
                return 3;
            }
        };
    }


    //Setup
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

