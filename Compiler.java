import java.util.*;

class TokenLex { 

    String Token = "";
    String Lex = "";

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
    private static String lexema = readFile();
    // used to get positions on the symbol table
    private static int index = 0;
    // used to travel navigate on the string
    private static int interator = 0;
    // global token string
    private static String token;
    public static boolean error = false;

    // Conferir os simbolos abaixo com nossa tabela de simbolos
    private static char[] letter = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','Y','V','W','X','Y','Z'};
    private static char[] digit = {'0','1','2','3','4','5','6','7','8','9'};																			                                          
    private static char[] hex 	= {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};													                                          
    private static char[] symbols 	= { '=','(',')',',','+','-','*',';','{','}','[',']','%',':','&','^','@','!','?','>','<','\''};
    
    // Creation of symbol table 
    private static Map<Integer,TokenLex> alphabet = new HashMap<Integer,TokenLex>();	 																		
    static String[] reservedWords = {"final", "int", "char", "for", "if", "else", "and", "or", "not", "to", "begin", "end", "then", "readln", "step", "write", "writeln", "do", "<-", "(", ")", "<", ">", "<>", ">=", "=<", ",", "+", "-", "*", "/", ";", "%", "[", "]"};
    
    public static String readFile() {
        String result = "";
        //Scanner sc = new Scanner(System.in);
        while (sc.hasNext()){
            result = result + sc.nextLine().toLowerCase()+" ";
        }
        System.out.println(result);
        return result;
    }
    
    public static boolean contains(char [] arr, char c){
        boolean result = false;
        for(int i=0; i<arr.length; i++){
            if(arr[i] == c){
                result = true;
            }
        }
        return result;
    }

    public static String returnIt(String l, int i){
        return lexema.substring(i, l.length());
    }

    public static boolean containsTok(String s, Map<Integer,TokenLex> m) {
        boolean result = false;
        for (Map.Entry<Integer, TokenLex> entry : m.entrySet()){
            if(s.equals(entry.getValue().getLex())){
                result = true;
            }
        }
        return result;
    }

    public static String getToken(String s, Map<Integer,TokenLex> m){
        String result = null;
        for (Map.Entry<Integer, TokenLex> entry : m.entrySet()){
            if(s.equals(entry.getValue().getLex())){
                result = entry.getValue().getToken();
            }
        }
        return result;
    }

    // TODO base on each state of the automat
    public static String analisadorLexico(){
        
        TokenLex tl = new TokenLex();
        String tmpLexema = "";
        int state = 0;
        
        for (;interator < lexema.length();) {
            char c = lexema.charAt(interator);
            switch (state) {
                // Create every case equals of the automato que vc criou fdp fazendo os if e etc pra cada um e separando  (sendo cada bolinha um case)
                case 0:
                    if (c == '_') {
                        interator++;   
                        tmpLexema = tmpLexema+c;
                        state = 2;
                        tl.setToken("id");
                    } else if ( contains(letter,c)) {
                        System.out.println(" >> " + c + " << " + " -- " + state);
                        interator++;
                        tmpLexema = tmpLexema+c;
                        state = 1;
                        tl.setToken("id");
                    } else if ( c == '0') {
                        System.out.println(" >> " + c + " << " + " -- " + state);
                        // pode entrar em conflito com digito
                        interator++;
                        tmpLexema = tmpLexema+c;
                        state = 10;
                    } else if ( contains(digit, c)) {
                        System.out.println(" >> " + c + " << " + " -- " + state);
                        interator++;
                        tmpLexema = tmpLexema+c;
                        state = 3;
                        tl.setToken("val");
                    } else if ( c == '<') {
                        System.out.println(" >> " + c + " << " + " -- " + state);
                        interator++;
                        tmpLexema = tmpLexema+c;
                        state = 4;
                    } else if ( c == '>') {
                        System.out.println(" >> " + c + " << " + " -- " + state);
                        interator++;
                        tmpLexema = tmpLexema+c;
                        state = 5;
                    } else if ( c == '/') {
                        System.out.println(" >> " + c + " << " + " -- " + state);
                        interator++;
                        // duvida pro be
                        tmpLexema = tmpLexema+c;
                        state = 7;
                    } else if ( contains(symbols, c)) {
                        System.out.println(" >> " + c + " << " + " -- " + state);
                        interator++;
                        tmpLexema = tmpLexema+c;
                        state = 99;
                    } else if ( c == ' ') {
                        System.out.println(" >> " + c + " << " + " -- " + state);
                        interator++;
                        state = 0;
                    } else if(c == '\"'){
                        System.out.println(" >> " + c + " << " + " -- " + state);
                        interator++;
                        state = 13;
                    } else if (c == '\''){
                        System.out.println(" >> " + c + " << " + " -- " + state);
                        interator++;
                        state = 6;
                    } else {
                        System.out.println(" >> " + c + " << " + " -- " + state);
                        // Error
                        state = 666;
                    }
                    break;
                case 1:
                    if (contains(letter, c) || contains(digit, c) || c == '_'){
                        System.out.println(" >> " + c + " << " + " -- " + state);
                        interator++;
                        tmpLexema = tmpLexema+c;
                        state = 1;
                    } else {
                        System.out.println(" >> " + c + " << " + " -- " + state);
                        System.out.println("--------------------" + tmpLexema +"---------------");
                        // novo lexama e formado
                        lexema = returnIt(lexema, interator);
                        // reseta posição
                        interator = 0;
                        // aceitação
                        state = 99;
                        System.out.println("--------------------" + tmpLexema +"---------------");
                    }
                    break;
                case 2:
                    if(contains(letter, c) || contains(digit, c)){
                        System.out.println(" >> " + c + " << " + " -- " + state);
                        interator++;
                        tmpLexema = tmpLexema+c;
                        state = 1;
                    }else if(c == '_'){
                        System.out.println(" >> " + c + " << " + " -- " + state);
                        interator++;
                        tmpLexema = tmpLexema+c;
                        state = 2;
                    } else {
                        System.out.println(" >> " + c + " << " + " -- " + state);
                        // aceitação
                        lexema = returnIt(lexema, interator);
                        interator = 0;
                        state = 99;
                    }
                    break;
                case 3:
                    if(contains(digit, c)) {
                        System.out.println(" >> " + c + " << " + " -- " + state);
                        interator++;
                        tmpLexema = tmpLexema+c;
                        state = 3;
                    } else {
                        System.out.println(" >> " + c + " << " + " -- " + state);
                        // aceitação
                        lexema = returnIt(lexema, interator);
                        interator = 0;
                        state = 99;
                    }
                    break;
                case 4:
                    if( c == '='){
                        System.out.println(" >> " + c + " << " + " -- " + state);
                        tmpLexema = tmpLexema+c;
                        interator++;
                        state = 99;
                    }else if( c == '>') {
                        System.out.println(" >> " + c + " << " + " -- " + state);
                        tmpLexema = tmpLexema+c;
                        interator++;
                        state = 99;
                    } else if( c == '-'){
                        System.out.println(" >> " + c + " << " + " -- " + state);
                        tmpLexema = tmpLexema+c;
                        interator++;
                        state = 99;
                    } else {
                        System.out.println(" >> " + c + " << " + " -- " + state);
                        // aceitação
                        lexema = returnIt(lexema, interator);
                        interator = 0;
                        state = 99;    
                    }
                    break;
                case 5:
                    if(c == '=') {
                        System.out.println(" >> " + c + " << " + " -- " + state);
                        tmpLexema = tmpLexema+c;
                        interator++;
                        state = 99;
                    } else {
                        System.out.println(" >> " + c + " << " + " -- " + state);
                        //aceitação
                        lexema = returnIt(lexema, interator);
                        interator = 0;
                        state = 99;
                    }
                    break;
                case 6:
                    if (contains(symbols, c) || contains(letter, c) || contains(digit, c)) {
                        System.out.println(" >> " + c + " << " + " -- " + state);
                        tmpLexema = tmpLexema+c;
                        interator++;
                        state = 11;
                    }
                    break;
                case 7:
                // comentario ou dividir
                    if (c == '*') {
                        System.out.println(" >> " + c + " << " + " -- " + state);
                        interator++;
                        state = 8;
                    } else {
                        System.out.println(" >> " + c + " << " + " -- " + state);
                        // criar dividir
                        lexema = returnIt(lexema, index);
                        interator = 0;
                        state = 99;
                    }
                    break;
                case 8:
                    // comentario
                    if (c == '*') {
                        System.out.println(" >> " + c + " << " + " -- " + state);
                        interator++;
                        state = 9;
                    } else {
                        System.out.println(" >> " + c + " << " + " -- " + state);
                        interator++;
                        state = 8;
                    } 
                    break;
                case 9:
                    // comentario finish
                    if(c == '/'){
                        System.out.println(" >> " + c + " << " + " -- " + state);
                        interator++;
                        tmpLexema = "";
                        state = 0;
                    } else {
                        System.out.println(" >> " + c + " << " + " -- " + state);
                        interator++;
                        state = 8;
                    }
                    break;
                case 10:
                    if (contains(hex, c)){
                        System.out.println(" >> " + c + " << " + " -- " + state);
                        tmpLexema = tmpLexema+c;
                        interator++;
                        state = 12;
                    } else if (c == 'h'){
                        System.out.println(" >> " + c + " << " + " -- " + state);
                        tmpLexema = tmpLexema+c;
                        // duvida
                        interator++;
                        state = 99;
                    }
                    break;
                case 11:
                    // Finishing char
                    if(c == '\''){
                       interator++;
                       state = 99; 
                    }
                    break;
                case 12:
                    if (contains(hex, c)){
                        System.out.println(" >> " + c + " << " + " -- " + state);
                        tmpLexema = tmpLexema+c;
                        interator++;
                        state = 14;
                    }
                    break;
                case 13:
                    if (contains(letter, c) || contains(digit, c) || contains(symbols, c) || c == ' ') {
                        System.out.println(" >> " + c + " << " + " -- " + state);
                        tmpLexema = tmpLexema+c;
                        interator++;
                        state = 13;
                    } else if (c == '\"' || c == '$' || c == '\n') {
                        System.out.println(" >> " + c + " << " + " -- " + state);
                        tmpLexema = tmpLexema+"$";
                        interator++;
                        state = 99;
                    }
                    break;
                case 14:
                    if (c == 'h'){
                        System.out.println(" >> " + c + " << " + " -- " + state);
                        tmpLexema = tmpLexema+c;
                        // duvida
                        interator++;
                        state = 99;
                    } else {
                        tmpLexema = tmpLexema+c;
                        interator++;
                        state = 3;
                    }
                    break;
                case 99:
                    if(!(containsTok(tmpLexema,alphabet))){
                        //if(!alphabet.containsKey(tmpLexema)){
                        System.out.println(" >> " + tmpLexema + " << " + " -- " + state);
                        tl.setLex((tmpLexema));
                        alphabet.put(index,tl);
                        index++;
                    } else {
                        System.out.println(" >> " + tmpLexema + " << " + " -- " + state);
                        tl.setToken(getToken(tmpLexema, alphabet));
                    }
                    return tl.getToken();
                case 666:
                    System.out.println(" >> " + c + " << " + " -- " + state);
                    interator = lexema.length();
                    System.out.println("Caractere inválido " + "( " + c + " )" );
                    break;
            }
        }
        if(state != 0 && state != 99 && state != 666 ){
            System.out.println("Fim de arquivo não esperado");
            error = true;
        }
        return tl.getToken();
    }
    
    //TODO
    // Lembrar de usar a variavel error aqui para parar o programa caso fim de arquivo não esperado
    public static void casaToken(String token_expected) {
        // toke esperado avalia e pega proximo
        // senao error
        //GG
    }

    // Program Principal -- chama tudo ai dentro
    public static void main (String [] args) {

        for(String word: reservedWords){
            TokenLex tl = new TokenLex(word, word);
            alphabet.put(index, tl);
            index++;
        }
        int x = 0;
        while (x < 200){
            token = analisadorLexico();
            x++;
        }
        // Daqui pra baixo e putaria do David e do leo
        //S();
        if(interator < lexema.length()){
            System.out.println("Fim de arquivo não esperado!");
        }
        
    }

}