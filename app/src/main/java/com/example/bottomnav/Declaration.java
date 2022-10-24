package com.example.bottomnav;

import org.eclipse.cdt.core.dom.ast.ExpansionOverlapsBoundaryException;
import org.eclipse.cdt.core.dom.ast.IASTDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTCompositeTypeSpecifier;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTName;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTSimpleDeclaration;

import java.util.Optional;

// Declaration object makes sure all components are correctly formatted.
public class Declaration {

    // Choke-point: create static function filters out any malformed declaration such that
    // DeclarationVariables objects are guaranteed to have non null variable contents and
    // DeclarationStruct are guaranteed to have non null declarations.
    public static Optional<Declaration> create(IASTDeclaration dec) throws ExpansionOverlapsBoundaryException {
        if (dec instanceof CPPASTSimpleDeclaration) {
            CPPASTSimpleDeclaration declaration = (CPPASTSimpleDeclaration) dec;
            if (declaration.getDeclSpecifier() instanceof CPPASTCompositeTypeSpecifier) {
                CPPASTCompositeTypeSpecifier compSpec = (CPPASTCompositeTypeSpecifier) declaration.getDeclSpecifier();
                CPPASTName name = (CPPASTName) compSpec.getName();
                return Optional.of(new DeclarationStruct(compSpec.getDeclarations(false), name.getRawSignature()));
            } else {
                Optional<Statement> statementConst = StatementConst.create(declaration);
                Optional<Statement> statementMember = StatementMember.create(declaration);
                if (!statementConst.isPresent() && !statementMember.isPresent() ) {
                    return Optional.empty();
                }
                if (statementConst.get().areContentsPresent()) {
                    return Optional.of(new DeclarationVariable(statementConst.get()));
                }
                if (statementMember.get().areContentsPresent()) {
                    return Optional.of(new DeclarationVariable(statementMember.get()));
                }
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    public static Optional<Declaration> create(IASTNode dec) throws ExpansionOverlapsBoundaryException {
        return create((IASTDeclaration) dec);
    }
}
