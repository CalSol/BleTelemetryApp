package com.example.bottomnav;

import java.util.Optional;

public interface Deconstruction <SpecifierType>{
    Optional<Components> deconstruct(SpecifierType specifier);
}
