package com.example.bottomnav;

import org.eclipse.cdt.core.dom.ast.ExpansionOverlapsBoundaryException;
import org.eclipse.cdt.core.dom.ast.IASTDeclaration;

import java.util.ArrayList;
import java.util.Optional;

public class DeclarationStruct extends Declaration{
    private ArrayList<Optional<Declaration>> declarations;
    private String name;

    public DeclarationStruct(IASTDeclaration[] dec, String nameStruct) throws ExpansionOverlapsBoundaryException {
        ArrayList<Optional<Declaration>> declarationsList = new ArrayList<>();
        for (IASTDeclaration declaration : dec) {
            declarationsList.add(Declaration.create(declaration));
        }
        declarations = declarationsList;
        name = nameStruct;
    }

    public ArrayList<Optional<Declaration>> getDeclarations() {
        return declarations;
    }

    public String getName() {
        return name;
    }
}
