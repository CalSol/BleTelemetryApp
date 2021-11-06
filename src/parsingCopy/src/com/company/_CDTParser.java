package com.company;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.cdt.core.dom.ast.ASTVisitor;
import org.eclipse.cdt.core.dom.ast.IASTDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
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

    public _CDTParser(String Code) throws Exception {
        this.sourceCode = Code;
        this.translationUnit = getIASTTranslationUnit(this.sourceCode.toCharArray());
        ASTVisitor visitor = makeNewASTVisitor();
        visitor.shouldVisitDeclarations = true;
        this.translationUnit.accept(visitor);
    }

    public static ASTVisitor makeNewASTVisitor() {
        return new ASTVisitor() {
            public int visit(IASTDeclaration declaration) {
                System.out.println("Found a declaration: " + declaration.getRawSignature());
                return 3;
            }
        };
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

