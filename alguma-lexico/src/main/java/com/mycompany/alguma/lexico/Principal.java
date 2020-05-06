
package com.mycompany.alguma.lexico;

import java.io.IOException;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;

public class Principal {

    public static void main(String[] args) {
         try {
            CharStream cs = CharStreams.fromFileName(args[0]);
            algumalexer lex = new algumalexer(cs);
            Token t = null;
            while ((t = lex.nextToken()).getType() != Token.EOF) {
                System.out.println("<" + algumalexer.VOCABULARY.getDisplayName(t.getType()) + "," + t.getText() + ">");
            }

        } catch (IOException ex) {
        }
    }
}