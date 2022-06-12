package com.example.bottomnav;

import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTSimpleDeclSpecifier;
import java.util.Optional;

public class DeconstructionSimple implements Deconstruction<CPPASTSimpleDeclSpecifier> {

    @Override
    public Optional<Components> deconstruct(CPPASTSimpleDeclSpecifier specifier) {
        return Optional.empty();
    }
}
