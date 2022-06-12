package com.example.bottomnav;

import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTNamedTypeSpecifier;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTSimpleDeclSpecifier;

import java.util.Optional;

public class DeconstructionComplex implements Deconstruction<CPPASTNamedTypeSpecifier> {
    @Override
    public Optional<Components> deconstruct(CPPASTNamedTypeSpecifier specifier) {
        return Optional.empty();
    }
}
