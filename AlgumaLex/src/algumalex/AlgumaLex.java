
package algumalex;
public class AlgumaLex {
    public static void main(String[] args) {
        AlgumaLexico lex = new AlgumaLexico(args[0]);
        token t = null;
        while((t = lex.proximoToken()) != null) {
            System.out.print(t);
        }
     
    }
    
}
