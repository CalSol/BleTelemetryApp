package com.company;
import java.util.HashMap;
import org.eclipse.cdt.core.dom.ast.*;

/**Goals of making multiple Parse objects and storing them, keeping track of them*/
public class Parser {

    public HashMap<Integer, Parse> repo; //Store the translation unit with set ID
    private String sourceCode; //current source code
    private IASTTranslationUnit translationUnit; //current translation unit node
    public int ID;

    //We need to somehow store the nodes for each specified translationUNit

    /**Instatiates the Parser class*/
    public Parser() {
        this.ID = 0; //next time use SHA-1's ?
        this.repo = new HashMap<>();
    }

    /**Stores contents*/
    public void parse(String code) throws Exception {
        //methods to store the children and their values
        Parse parsedContents = new Parse(code);
        repo.put(ID, parsedContents);
        ID += 1;
    }
}
