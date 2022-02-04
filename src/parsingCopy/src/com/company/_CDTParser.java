package com.company;

import java.awt.*;
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
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTTranslationUnit;
import java.util.Iterator;

public class _CDTParser {
    String sourceCode;
    IASTTranslationUnit translationUnit;
    HashMap<String, Integer> repo;

    public _CDTParser(String Code) throws Exception {
        this.sourceCode = Code;
        this.repo = new HashMap<>();
        this.translationUnit = getIASTTranslationUnit(this.sourceCode.toCharArray());
        visitSetUp();
    }

    //Sets up what needs to be visited
    public void visitSetUp() throws Exception{
        ASTVisitor visitor = makeNewASTVisitor();
        visitor.shouldVisitNames = true;
        visitor.shouldVisitExpressions = true;
        printTreeSave(translationUnit, 1, this);
        this.translationUnit.accept(visitor);
    }

    //iterate over mapping and find the ID given the name's location
    public String iterate(int location) {
        Iterator iter = repo.keySet().iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            if (repo.get(key) == location) {
                return key;
            }
        }
        return "";
    }

    public String getID(String name) {
        int location = getLocation(name);
        String ID = iterate(location);
        return ID;
    }

    //Returns the offset location of name
    public int getLocation(String name) {
        return repo.get(name) + name.length() + 3;
    }

    //Identifies the IDs and its values
    public ASTVisitor makeNewASTVisitor() {
        return new ASTVisitor() {
            public int visit(IASTName a) {
                String rawSignature = a.getRawSignature();
                return 3;
            }
            public int visit(IASTExpression a) {
                String rawSignature = a.getRawSignature();
                return 3;
            }
        };
    }

    //Initializes the setup
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

    //Visual Representation of Tree and saves contents

    private static void printTreeSave(IASTNode node, int index, _CDTParser current) {
        IASTNode[] children = node.getChildren();

        boolean printContents = true;


        if ((node instanceof CPPASTTranslationUnit)) {
            printContents = false;
        }

        String offset = "";
        int loc = 0;
        try {
            int location = node.getFileLocation().getNodeOffset();
            int nodeLength = node.getFileLocation().getNodeLength();
            offset = node.getSyntax() != null ? " (offset: " + location + "," + nodeLength + ")" : "";
            printContents = node.getFileLocation().getNodeLength() < 30;
            loc = location;
        } catch (ExpansionOverlapsBoundaryException e) {
            e.printStackTrace();
        } catch (UnsupportedOperationException e) {
            offset = "UnsupportedOperationException";
        }

        System.out.println(String.format(new StringBuilder("%1$").append(index * 2).append("s").toString(),
                new Object[] { "-" }) + node.getClass().getSimpleName() + offset + " -> " +
                (printContents ? node.getRawSignature().replaceAll("\n", " \\ ")
                        : node.getRawSignature().subSequence(0, 5)));

        current.repo.put("" + (printContents ? node.getRawSignature().replaceAll("\n", " \\ ")
                : node.getRawSignature().subSequence(0, 5)), loc);

        for (IASTNode iastNode : children) {
            printTreeSave(iastNode, index + 1, current);
        }
    }
}

