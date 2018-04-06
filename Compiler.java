import java.util.*;

class TokenLex { 

    String Token = "";
    String Lex = "";
    TokenLex TL = new TokenLex();

    public TokenLex(){
        // contructor vazio
        this.Token = token;
        this.Lex = lex;
    }

    public TokenLex(String token, String lexico) {
        this.Token = token;
        this.Lex = lex;
    }

    //Getter & Setters
    public String getToken(){
        return this.Token;
    }

    public String getLex(){
        return this.Lex;
    }

    public void setToken(String token){
        this.Token = token;
    }
    
    public void setLex(String lex){
        this.Lex = lex;
    }
    
}

public class Compiler {
    
    // Objects
    private TokenLex TL = new TokenLex();
    private Scanner sc = new Scanner(System.in); 

    // Global variables  
    // used to get positions on the symbol table
    private int index = 0;
    // used to travel navigate on the string
    private int interator = 0;
    public boolean error = false;


    // Conferir os simbolos abaixo com nossa tabela de simbolos
    private static char[] letter = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','Y','V','W','X','Y','Z'};
    private static char[] digit = {'0','1','2','3','4','5','6','7','8','9'};																			                                          
    private static char[] hex 	= {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};													                                          
    private static char[] symbols 	= { '=','(',')',',','+','-','*',';','{','}','[',']','%',':','&','^','@','!','?','>','<','\''};
    
    // Creation of symbol table 
    private static Map<Integer,TokLex> reservado = new HashMap<Integer,TokLex>();	 																		
    
    static String[] r = {"const", "var", "integer",	"char",	"for", "if","else",	"and", "or", "not", "=", "to", "(",")","<",">","<>",">=","<=",",","+","-","*","/",";","{","}","then","readln","step", "write", "writeln",	"%", "[", "]", "do"	};      

        
    // TODO base on each state of the automat
    public static TokenLex analisadorLexico(){
        TokenLex token;
        int state;
        char c = token.charAt();
        switch (state) {
            // Create every case equals of the automato que vc criou fdp fazendo os if e etc pra cada um e separando  (sendo cada bolinha um case)
            case 0:
            default:
                System.out.println("FUDEUUUUUUUUUUUUUUUUU");
        }
        return token;
    }
    
    //TODO
    public static void casaToken(String token_expected) {
        // toke esperado avalia e pega proximo
        // senao error
        //GG
    }

    // Program Principal -- chama tudo ai dentro
    public static void main (String [] args) {
        
    }
}


