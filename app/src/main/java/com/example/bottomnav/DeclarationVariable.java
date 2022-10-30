package com.example.bottomnav;

public class DeclarationVariable extends Declaration{
    private Statement statement;

    public DeclarationVariable(Statement  givenStatement) {
        statement = givenStatement;
    }

    public Statement getStatement() {
        return statement;
    }
}
