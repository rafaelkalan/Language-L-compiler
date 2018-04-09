import java.util.*;

class TokenLex { 

    String Token = "";
    String Lex = "";
    TokenLex TL = new TokenLex();

    public TokenLex(){
        // contructor vazio
        this.Token = "";
        this.Lex = "";
    }

    public TokenLex(String token, String lex) {
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
    private static TokenLex TL = new TokenLex();
    private static Scanner sc = new Scanner(System.in); 

    // Global variables  
    private static String lexema = "";
    // used to get positions on the symbol table
    private static int index = 0;
    // used to travel navigate on the string
    private static int interator = 0;
    public static boolean error = false;


    // Conferir os simbolos abaixo com nossa tabela de simbolos
    private static char[] letter = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','Y','V','W','X','Y','Z'};
    private static char[] digit = {'0','1','2','3','4','5','6','7','8','9'};																			                                          
    private static char[] hex 	= {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};													                                          
    private static char[] symbols 	= { '=','(',')',',','+','-','*',';','{','}','[',']','%',':','&','^','@','!','?','>','<','\''};
    
    // Creation of symbol table 
    private static Map<Integer,TokenLex> alphabet = new HashMap<Integer,TokenLex>();	 																		
    static String[] A = {"final", "int", "char", "for", "if", "else", "and", "or", "not", "to", "begin", "end", "then", "readln", "step", "write", "writeln", "do", "<-", "(", ")", "<", ">", "<>", ">=", "=<", ",", "+", "-", "*", "/", ";", "%", "[", "]"};
    
    public static boolean contains(char [] arr, char c){
        boolean result = false;
        for(int i=0; i<=arr.length; i++){
            if(arr[i] == c){
                result = true;
            }
            return result;
        }
    }

    public static String returnIt(String lexema, int index){
        return lexema.substring(index, lexema.length());
    }

    // TODO base on each state of the automat
    public static String analisadorLexico(){
        
        TokenLex tl = new TokenLex();
        String tmpLexema = "";
        int state = 0;
        
        for (;interator < lexema.length();){
            char c = lexema.charAt(interator);
            switch (state) {
                // Create every case equals of the automato que vc criou fdp fazendo os if e etc pra cada um e separando  (sendo cada bolinha um case)
                case 0:
                    if (c == '_') {
                        interator++;   
                        tmpLexema = tmpLexema+c;
                        state = 2;
                    } else if ( contains(letter,c)) {
                        interator++;
                        tmpLexema = tmpLexema+c;
                        state = 1;
                    } else if ( contains(digit, c)) {
                        interator++;
                        tmpLexema = tmpLexema+c;
                        state = 3;
                    } else if ( c == '<') {
                        interator++;
                        tmpLexema = tmpLexema+c;
                        state = 4;
                    } else if ( c == '>') {
                        interator++;
                        tmpLexema = tmpLexema+c;
                        state = 5;
                    } else if ( c == '/') {
                        interator++;
                        // duvida pro be
                        tmpLexema = tmpLexema+c;
                        state = 7;
                    } else if ( contains(symbols, c)) {
                        // duvida pro be
                        state = 6;
                    } else if ( c == '0') {
                        // pode entrar em conflito com digito
                        interator++;
                        tmpLexema = tmpLexema+c;
                        state = 10;
                    } else if ( c == ' ' || c == 'n') {
                        interator++;
                        state = 0;
                    } else {
                        // Error
                        state = 666;
                    }
                    break;
                case 1:
                    if (contains(letter, c) || contains(digit, c) || c == '_'){
                        interator++;
                        tmpLexema = tmpLexema+c;
                        state = 1;
                    } else if( c == '$'){
                        interator++;
                        tmpLexema = lexema+c;
                        state = 99;
                    }else {
                        // novo lexama e formado
                        lexema = returnIt(lexema, index);
                        // reseta posição
                        interator = 0;
                        // aceitação
                        state = 99;
                    }
                    break;
                case 2:
                    if(contains(letter, c) || contains(digit, c)){
                        interator++;
                        tmpLexema = tmpLexema+c;
                        state = 1;
                    }else if(c == '_'){
                        interator++;
                        tmpLexema = tmpLexema+c;
                        state = 2;
                    } else {
                        // aceitação
                        interator = 0;
                        lexema = returnIt(lexema, index);
                        state = 99;
                    }
                    break;
                case 3:
                    if(contains(digit, c)) {
                        interator++;
                        tmpLexema = tmpLexema+c;
                        state = 3;
                    } else {
                        // aceitação
                        interator = 0;
                        lexema = returnIt(lexema, index);
                        state = 99;
                    }
                    break;
                case 4:
                    if( c == '='){
                        tmpLexema = tmpLexema+c;
                        interator++;
                        state = 99;
                    }else if( c == '>') {
                        tmpLexema = tmpLexema+c;
                        interator++;
                        state = 99;
                    } else if( c == '-'){
                        tmpLexema = tmpLexema+c;
                        interator++;
                        state = 99;
                    } else {
                        // aceitação
                        interator = 0;
                        lexema = returnIt(lexema, index);
                        state = 99;    
                    }
                    break;
                case 5:
                    if(c == '=') {
                        tmpLexema = tmpLexema+c;
                        interator++;
                        state = 99;
                    } else {
                        //aceitação
                        interator = 0;
                        lexema = returnIt(lexema, index);
                        state = 99;
                    }
                    break;
                case 6:
                    if (contains(symbols, c)) {
                        tmpLexema = tmpLexema+c;
                        interator++;
                        state = 6;
                    } else {
                        //aceitação
                        interator = 0;
                        lexema = returnIt(lexema, index);
                        state = 99;
                    }
                    break;
                case 7:
                    // comentario ou dividir
                    if (c == '*') {
                        interator++;
                        state = 8;
                    } else if ( c != '*'){
                        // criar dividir
                        interator++;
                        lexema = returnIt(lexema, index);
                        state = 99;
                    }
                    break;
                case 8:
                    // comentario
                    if (c == '*') {
                        interator++;
                        state = 9;
                    } else {
                        interator++;
                        state = 8;
                    } 
                    break;
                case 9:
                    // comentario finish
                    if(c == '/'){
                        interator++;
                        state = 0;
                    } else {
                        interator++;
                        state = 8;
                    }
                    break;
                case 10:
                    if (contains(hex, c)){
                        tmpLexema = tmpLexema+c;
                        interator++;
                        state = 10;
                    } else if (c == 'h'){
                        tmpLexema = tmpLexema+c;
                        // duvida
                        interator++;
                        state = 99;
                    } else {

                    }
                    break;
                case 11:
                    break;
                case 12:
                    break;
                case 99:
                    if(!alphabet.containsKey(tmpLexema)){
                        tl.setLex((tmpLexema));
                        alphabet.put(index,tl);
                        index++;
                    } else {
                        // check this
                        tl.setToken((String)alphabet.get(tmpLexema));
                    }
                    break;
                default:
                    System.out.println("FUDEUUUUUUUUUUUUUUUUU");
            }
            return tmpLexema;
        }
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



