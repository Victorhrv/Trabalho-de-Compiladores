
package algumalex;

public class AlgumaLexico {
    LeitorDeArquivosTexto ldat;

    public AlgumaLexico(String arquivo) {
        ldat = new LeitorDeArquivosTexto(arquivo);
    }
    public token proximoToken() {
        token proximo = null;
        espacosEComentarios();
        ldat.confirmar();
        proximo = fim();
        if (proximo == null) {
            ldat.zerar();
        } else {
            ldat.confirmar();
            return proximo;
        }
        proximo = palavrasChave();
        if (proximo == null) {
            ldat.zerar();
        } else {
            ldat.confirmar();
            return proximo;
        }
        proximo = variavel();
        if (proximo == null) {
            ldat.zerar();
        } else {
            ldat.confirmar();
            return proximo;
        }
        proximo = numeros();
        if (proximo == null) {
            ldat.zerar();
        } else {
            ldat.confirmar();
            return proximo;
        }
        proximo = operadorAritmetico();
        if (proximo == null) {
            ldat.zerar();
        } else {
            ldat.confirmar();
            return proximo;
        }
        proximo = operadorRelacional();
        if (proximo == null) {
            ldat.zerar();
        } else {
            ldat.confirmar();
            return proximo;
        }
        proximo = delimitador();
        if (proximo == null) {
            ldat.zerar();
        } else {
            ldat.confirmar();
            return proximo;
        }
        proximo = parenteses();
        if (proximo == null) {
            ldat.zerar();
        } else {
            ldat.confirmar();
            return proximo;
        }
        proximo = cadeia();
        if (proximo == null) {
            ldat.zerar();
        } else {
            ldat.confirmar();
            return proximo;
        }
        System.err.println("Erro léxico!");
        System.err.println(ldat.toString());
        return null;
    }
    
    private token operadorAritmetico() {
        int caractereLido = ldat.lerProximoCaractere();
        char c = (char) caractereLido;
        if (c == '*') {
            return new token(TipoToken.OpAritMult, ldat.getLexema());
        } else if (c == '/') {
            return new token(TipoToken.OpAritDiv, ldat.getLexema());
        } else if (c == '+') {
            return new token(TipoToken.OpAritSoma, ldat.getLexema());
        } else if (c == '-') {
            return new token(TipoToken.OpAritSub, ldat.getLexema());
        } else {
            return null;
        }
    }
    private token delimitador() {
        int caractereLido = ldat.lerProximoCaractere();
        char c = (char) caractereLido;
        if (c == ':') {
            return new token(TipoToken.Delim, ldat.getLexema());
        } else {
            return null;
        }
    }
    private token parenteses() {
        int caractereLido = ldat.lerProximoCaractere();
        char c = (char) caractereLido;
        if (c == '(') {
            return new token(TipoToken.AbrePar, ldat.getLexema());
        } else if (c == ')') {
            return new token(TipoToken.FechaPar, ldat.getLexema());
        } else {
            return null;
        }
    }
    private token operadorRelacional() {
        int caractereLido = ldat.lerProximoCaractere();
        char c = (char) caractereLido;
        if (c == '<') {
            c = (char) ldat.lerProximoCaractere();
            if (c == '>') {
                return new token(TipoToken.OpRelDif, ldat.getLexema());
            } else if (c == '=') {
                return new token(TipoToken.OpRelMenorIgual, ldat.getLexema());
            } else {
                ldat.retroceder();
                return new token(TipoToken.OpRelMenor, ldat.getLexema());
            }
        } else if (c == '=') {
            return new token(TipoToken.OpRelIgual, ldat.getLexema());
        } else if (c == '>') {
            c = (char) ldat.lerProximoCaractere();
            if (c == '=') {
                return new token(TipoToken.OpRelMaiorIgual, ldat.getLexema());
            } else {
                ldat.retroceder();
                return new token(TipoToken.OpRelMaior, ldat.getLexema());
            }
        }
        return null;
    }
    private token numeros() {
        int estado = 1;
        while (true) {
            char c = (char) ldat.lerProximoCaractere();
            if (estado == 1) {
                if (Character.isDigit(c)) {
                    estado = 2;
                } else {
                    return null;
                }
            } else if (estado == 2) {
                if (c == '.') {
                    c = (char) ldat.lerProximoCaractere();
                    if (Character.isDigit(c)) {
                        estado = 3;
                    } else {
                        return null;
                    }
                } else if (!Character.isDigit(c)) {
                    ldat.retroceder();
                    return new token(TipoToken.NumInt, ldat.getLexema());
                }
            } else if (estado == 3) {
                if (!Character.isDigit(c)) {
                    ldat.retroceder();
                    return new token(TipoToken.NumReal, ldat.getLexema());
                }
            }
        }
    }
    
   private token variavel() {
        int estado = 1;
        while (true) {
            char c = (char) ldat.lerProximoCaractere();
            if (estado == 1) {
                if (Character.isLetter(c)) {
                    estado = 2;
                } else {
                    return null;
                }
            } else if (estado == 2) {
                if (!Character.isLetterOrDigit(c)) {
                    ldat.retroceder();
                    return new token(TipoToken.Var, ldat.getLexema());
                }
            }
        }
    }
   private token cadeia() {
        int estado = 1;
        while (true) {
            char c = (char) ldat.lerProximoCaractere();
            if (estado == 1) {
                if (c == '\'') {
                    estado = 2;
                } else {
                    return null;
                }
            } else if (estado == 2) {
                if (c == '\n') {
                    return null;
                }
                if (c == '\'') {
                    return new token(TipoToken.Cadeia, ldat.getLexema());
                } else if (c == '\\') {
                    estado = 3;
                }
            } else if (estado == 3) {
                if (c == '\n') {
                    return null;
                } else {
                    estado = 2;
                }
            }
        }
    }
   private void espacosEComentarios() {
        int estado = 1;
        while (true) {
            char c = (char) ldat.lerProximoCaractere();
            if (estado == 1) {
                if (Character.isWhitespace(c) || c == ' ') {
                    estado = 2;
                } else if (c == '%') {
                    estado = 3;
                } else {
                    ldat.retroceder();
                    return;
                }
            } else if (estado == 2) {
                if (c == '%') {
                    estado = 3;
                } else if (!(Character.isWhitespace(c) || c == ' ')) {
                    ldat.retroceder();
                    return;
                }
            } else if (estado == 3) {
                if (c == '\n') {
                    return;
                }
            }
        }
    }
   private token palavrasChave() {
        while (true) {
            char c = (char) ldat.lerProximoCaractere();
            if (!Character.isLetter(c)) {
                ldat.retroceder();
                String lexema = ldat.getLexema();
                if (lexema.equals("DECLARACOES")) {
                    return new token(TipoToken.PCDeclaracoes, lexema);
                } else if (lexema.equals("ALGORITMO")) {
                    return new token(TipoToken.PCAlgoritmo, lexema);
                } else if (lexema.equals("INTEIRO")) {
                    return new token(TipoToken.PCInteiro, lexema);
                } else if (lexema.equals("REAL")) {
                    return new token(TipoToken.PCReal, lexema);
                } else if (lexema.equals("ATRIBUIR")) {
                    return new token(TipoToken.PCAtribuir, lexema);
                } else if (lexema.equals("A")) {
                    return new token(TipoToken.PCA, lexema);
                } else if (lexema.equals("LER")) {
                    return new token(TipoToken.PCLer, lexema);
                } else if (lexema.equals("IMPRIMIR")) {
                    return new token(TipoToken.PCImprimir, lexema);
                } else if (lexema.equals("SE")) {
                    return new token(TipoToken.PCSe, lexema);
                } else if (lexema.equals("ENTAO")) {
                    return new token(TipoToken.PCEntao, lexema);
                } else if (lexema.equals("SENAO")) {
                    return new token(TipoToken.PCSenao, lexema);
                }else if (lexema.equals("ENQUANTO")) {
                    return new token(TipoToken.PCEnquanto, lexema);
                } else if (lexema.equals("INICIO")) {
                    return new token(TipoToken.PCInicio, lexema);
                } else if (lexema.equals("FIM")) {
                    return new token(TipoToken.PCFim, lexema);
                } else if (lexema.equals("E")) {
                    return new token(TipoToken.OpBoolE, lexema);
                } else if (lexema.equals("OU")) {
                    return new token(TipoToken.OpBoolOu, lexema);
                } else {
                    return null;
                }
            }
        }
    }
    private token fim() {
        int caractereLido = ldat.lerProximoCaractere();
        if (caractereLido == -1) {
            return new token(TipoToken.Fim, "Fim");
        }
        return null;
    }
}
    
