package algumalex;
import algumalex.TipoToken;
public class token {
    public TipoToken nome;
    public String lexema;

    public token(TipoToken nome, String lexema) {
        this.nome = nome;
        this.lexema = lexema;
    }
  
    @Override
    public String toString() {
        return "<"+nome+","+lexema+">";
    }
}
    
    
