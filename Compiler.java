import java.util.*;
 
class TokenLex {
 
    String Token = "";
    String Lex = "";
    String Type = "";
    String Classe = "";
    String Size = "";
    String Value = "";
	String Address = "";
 
    public TokenLex(){
        // contructor vazio
        this.Token = "";
        this.Lex = "";
        this.Type = "";
		this.Classe = "";
        this.Size = "";
		this.Value = "";
		this.Address = "";
    }
 
    public TokenLex(String token, String lex, String type, String classe, String size, String  value, String address) {
        this.Token = token;
        this.Lex = lex;
        this.Type = type;
        this.Classe = classe;
		this.Size = size;
        this.Value = value;
		this.Address = address;
    }
 
    //Getter & Setters
    public String getToken(){
        return this.Token;
    }
 
    public String getLex(){
        return this.Lex;
    }
	
	public String getType(){
        return this.Type;
    }
 
    public String getClasse(){
        return this.Classe;
    }
	
	public String getSize(){
        return this.Size;
    }
	
	public String getValue(){
        return this.Value;
    }
	
	public String getAddress(){
        return this.Address;
    }
 
    public void setToken(String token){
        this.Token = token;
    }
   
    public void setLex(String lex){
        this.Lex = lex;
    }
	
	public void setType(String type){
        this.Type = type;
    }
   
    public void setClasse(String classe){
        this.Classe = classe;
    }
	
	public void setSize(String size){
        this.Size = size;
	}
	
	public void setValue(String value){
        this.Value = value;
	}
	
	public void setAddress(String address){
        this.Address = address;
	}
   
}

class Code {
    
    
    
    public static int CalculateSize (){
        int size = 0;

        switch(type){
            case "int":
			    size = 2;
			    break;
		    case REGISTER_TYPE_BYTE:
			    size = 1;
			    break;
		    case "string":
			    if (value != "") {
				    size = value.length() -1;
			    } else	{
				    size = 256;
			    }
			    break;
        }
        return size;
    }
}

 
public class Compiler {
   
    // Objects
    private static TokenLex TL = new TokenLex();
    private static Scanner sc = new Scanner(System.in);
    private static TokenLex tempTok = new TokenLex();   
    private static TokenLex tempTok2 = new TokenLex();
	private static String MASM = "";
 
    // Global variables  
    private static String lexema = readFile();
	private static String machineCode = "";
	private static String memdata = "";
    // used to get positions on the symbol table
    private static int index = 0;
    // used to travel navigate on the string
    private static int interator = 0;
    // global token string
    private static String token;
    public static boolean error = false;
 
    // Conferir os simbolos abaixo com nossa tabela de simbolos
	private static String[] classe = {"var", "const"};
	private static String[] type = {"int", "char"};
	private static String[] size = {};
	private static String[] address = {};
    private static char[] letter = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','Y','V','W','X','Y','Z'};
    private static char[] digit = {'0','1','2','3','4','5','6','7','8','9'};                                                                                                                     
    private static char[] hex   = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};                                                                                             
    private static char[] symbols   = { '=','(',')',',','+','-','*',';','{','}','[',']','%',':','&','^','@','!','?','>','<','\''};
   
    // Creation of symbol table
    private static Map<Integer,TokenLex> alphabet = new HashMap<Integer,TokenLex>();                                                                           
    static String[] reservedWords = {"final", "int", "char", "for", "if", "else", "and", "or", "not", "to", "begin", "end", "then", "readln", "step", "write", "writeln", "do", "<-", "(", ")", "<", ">", "<>", ">=", "=<", ",", "+", "-", "*", "/", ";", "%", "[", "]", "="};
   
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
        String result = "";
        for (Map.Entry<Integer, TokenLex> entry : m.entrySet()){
            if(s.equals(entry.getValue().getToken())){
                result = entry.getValue().getToken();
            }
        }
        return result;
    }
	
    public static TokenLex searchToken(String s, Map<Integer,TokenLex> m){
        TokenLex result = new TokenLex();
        for (Map.Entry<Integer, TokenLex> entry : m.entrySet()){
            if(s.equals(entry.getValue().getLex())){
                result = entry.getValue();
            }
        }
        return result;
    }
	
    public static boolean isNumeric(String str) {  
		try{  
			double d = Double.parseDouble(str);  
		}  
		catch(NumberFormatException nfe){  
			return false;  
		}  
		return true;  
    }
	
	public static void printAlphabet(){
        for(Object objname:alphabet.keySet()) {
            System.out.println(objname);
            System.out.println("Token -> " + alphabet.get(objname).getToken() + "Lexema ->  " + (alphabet.get(objname).getLex()));
          }
    }
	

    // TODO base on each state of the automat
    public static TokenLex analisadorLexico(){
       
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
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        interator++;
                        tmpLexema = tmpLexema+c;
                        state = 1;
                        tl.setToken("id");
                    } else if ( c == '0') {
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        // pode entrar em conflito com digito
                        interator++;
                        tmpLexema = tmpLexema+c;
                        state = 10;
                        tl.setToken("const");
                    } else if ( contains(digit, c)) {
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        interator++;
                        tmpLexema = tmpLexema+c;
                        state = 3;
                        tl.setToken("const");
                    } else if ( c == '<') {
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        interator++;
                        tmpLexema = tmpLexema+c;
                        state = 4;
                    } else if ( c == '>') {
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        interator++;
                        tmpLexema = tmpLexema+c;
                        state = 5;
                    } else if ( c == '/') {
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        interator++;
                        // duvida pro be
                        tmpLexema = tmpLexema+c;
                        state = 7;
                    } else if ( contains(symbols, c)) {
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        interator++;
                        tmpLexema = tmpLexema+c;
                        state = 99;
                        tl.setToken(tmpLexema);
                    } else if ( c == ' ') {
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        interator++;
                        state = 0;
                    } else if(c == '\"'){
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        interator++;
                        state = 13;
                        tl.setToken("const");
                    } else if (c == '\''){
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        interator++;
                        state = 6;
                        tl.setToken("const");
                    } else {
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        // Error
                        state = 666;
                    }
                    break;
                case 1:
                    if (contains(letter, c) || contains(digit, c) || c == '_'){
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        interator++;
                        tmpLexema = tmpLexema+c;
                        state = 1;
                    } else {
                       // System.out.println(" >> " + c + " << " + " -- " + state);
                        //System.out.println("--------------------" + tmpLexema +"---------------");
                        // novo lexama e formado
                        lexema = returnIt(lexema, interator);
                        // reseta posição
                        interator = 0;
                        // aceitação
                        state = 99;
                        //System.out.println("--------------------" + tmpLexema +"---------------");
                    }
                    break;
                case 2:
                    if(contains(letter, c) || contains(digit, c)){
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        interator++;
                        tmpLexema = tmpLexema+c;
                        state = 1;
                    }else if(c == '_'){
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        interator++;
                        tmpLexema = tmpLexema+c;
                        state = 2;
                    } else {
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        // aceitação
                        lexema = returnIt(lexema, interator);
                        interator = 0;
                        state = 99;
                    }
                    break;
                case 3:
                    if(contains(digit, c)) {
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        interator++;
                        tmpLexema = tmpLexema+c;
                        state = 3;
                    } else {
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        // aceitação
                        lexema = returnIt(lexema, interator);
                        interator = 0;
                        state = 99;
                    }
                    break;
                case 4:
                    if( c == '='){
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        tmpLexema = tmpLexema+c;
                        interator++;
                        state = 99;
                    }else if( c == '>') {
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        tmpLexema = tmpLexema+c;
                        interator++;
                        state = 99;
                    } else if( c == '-'){
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        tmpLexema = tmpLexema+c;
                        interator++;
                        state = 99;
                    } else {
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        // aceitação
                        lexema = returnIt(lexema, interator);
                        interator = 0;
                        state = 99;    
                    }
                    break;
                case 5:
                    if(c == '=') {
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        tmpLexema = tmpLexema+c;
                        interator++;
                        state = 99;
                    } else {
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        //aceitação
                        lexema = returnIt(lexema, interator);
                        interator = 0;
                        state = 99;
                    }
                    break;
                case 6:
                    if (contains(symbols, c) || contains(letter, c) || contains(digit, c)) {
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        tmpLexema = tmpLexema+c;
                        interator++;
                        state = 11;
                    }
                    break;
                case 7:
                // comentario ou dividir
                    if (c == '*') {
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        interator++;
                        state = 8;
                    } else {
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        // criar dividir
                        lexema = returnIt(lexema, interator);
                        interator = 0;
                        state = 99;
                    }
                    break;
                case 8:
                    // comentario
                    if (c == '*') {
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        interator++;
                        state = 9;
                    } else {
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        interator++;
                        state = 8;
                    }
                    break;
                case 9:
                    // comentario finish
                    if(c == '/'){
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        interator++;
                        tmpLexema = "";
                        state = 0;
                    } else {
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        interator++;
                        state = 8;
                    }
                    break;
                case 10:
                    if (contains(hex, c)){
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        tmpLexema = tmpLexema+c;
                        interator++;
                        state = 12;
                    }else{
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        lexema = returnIt(lexema, interator);
                        interator = 0;
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
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        tmpLexema = tmpLexema+c;
                        interator++;
                        state = 14;
                    }
                    break;
                case 13:
                    if (contains(letter, c) || contains(digit, c) || contains(symbols, c) || c == ' ') {
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        tmpLexema = tmpLexema+c;
                        interator++;
                        state = 13;
                    } else if (c == '\"' || c == '$' || c == '\n') {
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        tmpLexema = tmpLexema+"$";
                        interator++;
                        state = 99;
                    }
                    break;
                case 14:
                    if (c == 'h'){
                        //System.out.println(" >> " + c + " << " + " -- " + state);
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
                       
                        tl.setLex((tmpLexema));
                        alphabet.put(index,tl);
                        //System.out.println(" >> " + tl.getLex() + " << " + " -- " + tl.getToken());
                        index++;
                    } else {
                        //System.out.println(" >> " + tmpLexema + " << " + " -- " + state);
                        tl = searchToken(tmpLexema, alphabet);
                    }
                    return tl;
                case 666:
                    //System.out.println(" >> " + c + " << " + " -- " + state);
                    interator = lexema.length();
                    System.out.println("Caractere inválido " + "( " + c + " )" );
                    break;
            }
        }
        if(state != 0 && state != 99 && state != 666 ){
            System.out.println("Fim de arquivo não esperado");
            error = true;
        }
        return tl;
    }
   
    //TODO
    // Lembrar de usar a variavel error aqui para parar o programa caso fim de arquivo não esperado
    public static void casaToken(String token_expected) {
        // toke esperado avalia e pega proximo
        //System.out.println(TL.getLex() + "  ----lex------:");
        //System.out.println(">"+TL.getToken()+"<  -----token----:");
        //System.out.println(token_expected);
        if(TL.getToken().equals(token_expected)) {
            TL = analisadorLexico();
            //System.out.println("TL:"+TL.getLex() + "----lex------"+TL.getToken() + "-----token----");
			//System.out.println("TL:"+TL.getLex() + "<-lex\n"+TL.getToken() + "<-token\n"+TL.getClasse()+"<-Classe\n"+TL.getType()+"<-Type\n");
            if(error == true){
                //System.out.println("fudeu!!");
                System.exit(0);
            }
        }else{	
            System.out.println("ERRO: Token nao esperado ("+TL.getToken()+")");
            System.exit(0);
        }
 
    }
 
    public static void S (){
        while(TL.getToken().equals("int") || TL.getToken().equals("char") || TL.getToken().equals("final")){
            D();
            //System.out.println("TL:"+TL.getLex() + "<-lex\n"+TL.getToken() + "<-token\n"+TL.getClasse()+"<-Classe\n"+TL.getType()+"<-Type");
            //printAlphabet();
        }while(interator < lexema.length()){
            //printAlphabet();
            //System.out.println("TL:"+TL.getLex() + "<-lex\n"+TL.getToken() + "<-token\n"+TL.getClasse()+"<-Classe\n"+TL.getType()+"<-Type");
            
        //while(TL.getToken().equals("id") || TL.getToken().equals("For") || TL.getToken().equals("if") || TL.getToken().equals(";") || TL.getToken().equals("readln") || TL.getToken().equals("write") || TL.getToken().equals("writeln")){
            C();
            //System.out.println("Fim de compila��o");
        }
 
    }
 
    public static void D(){
        if(TL.getToken().equals("int")){
            casaToken("int");
            tempTok = searchToken(TL.getLex(), alphabet);
            tempTok.setClasse("var");
            tempTok.setType("int");
            casaToken("id");			
            Y();
            while((TL.getToken()).equals(",")){
                casaToken(",");
		tempTok = searchToken(TL.getLex(), alphabet);
                tempTok.setClasse("var");
		tempTok.setType("int");
                //System.out.println("TL:"+TL.getLex() + "<-lex\n"+TL.getToken() + "<-token\n"+TL.getClasse()+"<-Classe\n"+TL.getType()+"<-Type");
                casaToken("id");		
                Y();
            }
            casaToken(";");		
        }
        else if(TL.getToken().equals("char")){
            casaToken("char");
			tempTok = searchToken(TL.getLex(), alphabet);
			tempTok.setClasse("var");
			tempTok.setType("char");
            casaToken("id");
			
            Y();
            while(TL.getToken().equals(",")){
                casaToken(",");
				tempTok = searchToken(TL.getLex(), alphabet);
				tempTok.setClasse("var");
				tempTok.setType("char");
                casaToken("id");
				
                Y();
            }
            casaToken(";");
            //System.out.println("TL:"+TL.getLex() + "<-lex\n"+TL.getToken() + "<-token\n"+TL.getClasse()+"<-Classe\n"+TL.getType()+"<-Type");
        }
        else{
            casaToken("final");
			tempTok = searchToken(TL.getLex(), alphabet);
			tempTok.setClasse("const");
			tempTok.setType("char");
            casaToken("id");
            casaToken("=");
            if(TL.getToken().equals("-")){
                casaToken("-");
            }
			TL.setClasse("const");
            casaToken("const");
            casaToken(";");
			//System.out.println("TL:"+TL.getLex() + "<-lex\n"+TL.getToken() + "<-token\n"+TL.getClasse()+"<-Classe\n"+TL.getType()+"<-Type");
        }
    }
 
    public static void Y(){
        if(TL.getToken().equals("<-")){
            casaToken("<-");
        
            if(TL.getToken().equals("-")){
                casaToken("-");
            }
            if(isNumeric(TL.getLex())){
                TL.setType("int");
            }else if(TL.getLex().length() == 1){
                TL.setType("char");
            }else{
                TL.setType("String");
            }
            tempTok2 = searchToken(TL.getLex(), alphabet);
            if(tempTok.getType().equals(tempTok2.getType())){
                tempTok.setValue(tempTok2.getLex());
            }
	    casaToken("const");
        }else if(TL.getToken().equals("const")){
            if(TL.getToken().equals("-")){
                casaToken("-");
            }
            if(isNumeric(TL.getLex())){
                TL.setType("int");
            }else if(TL.getLex().length() == 1){
                TL.setType("char");
            }else{
                TL.setType("String");
            }
            tempTok2 = searchToken(TL.getLex(), alphabet);
            if(tempTok.getType().equals(tempTok2.getType())){
                tempTok.setValue(tempTok2.getLex());
            }
	    casaToken("const");
        }else if(TL.getToken().equals("[")){
            casaToken("[");
            tempTok.setSize(TL.getLex());
            if(isNumeric(TL.getLex())){
		tempTok.setSize(TL.getLex());
            }else{
		System.out.println("Error : Tipos incompatíveis.");
            }
            casaToken("const");
            casaToken("]");
        }
        //printAlphabet();
    }

    public static void C(){
        if(TL.getToken().equals("id")){
            //System.out.println("entrou C");
            tempTok = searchToken("p", alphabet);
            //System.out.println("TL:"+tempTok.getLex() + "<-lex\n"+tempTok.getToken() + "<-token\n"+tempTok.getClasse()+"<-Classe\n"+tempTok.getType()+"<-Type");  
            //System.out.println("TL:"+TL.getLex() + "<-lex\n"+TL.getToken() + "<-token\n"+TL.getClasse()+"<-Classe\n"+TL.getType()+"<-Type");
            casaToken("id");	
            //System.out.println("Loop ?");
            if ((tempTok.getClasse().equals("var")) || tempTok.getClasse().equals("const")){
            //System.out.println("Loop ?");
            casaToken("<-");
            String cid = Exp();
                if((tempTok.getType().equals(cid)) && (tempTok.getClasse().equals("var"))){
					//System.out.println("TL:"+TL.getLex() + "<-lex\n"+TL.getToken() + "<-token\n"+TL.getClasse()+"<-Classe\n"+TL.getType()+"<-Type");
					tempTok.setValue(tempTok2.getLex());
					//fazer atribuicao
                   // id = Exp(); Assembly
                }else{
                    System.out.println("Error : Tipos incompatíveis.  C id");
                }
            }else{
                System.out.println( "Error : Identificador não declarado [" + tempTok.getToken() + "]");
            }
            casaToken(";");
        }
        else if(TL.getToken().equals("for")){
            casaToken("for");
            tempTok = searchToken("p", alphabet);
            //System.out.println("TL:"+tempTok.getLex() + "<-lex\n"+tempTok.getToken() + "<-token\n"+tempTok.getClasse()+"<-Classe\n"+tempTok.getType()+"<-Type");  
            //casaToken("");
               //System.out.println("TL:"+TL.getLex() + "<-lex\n"+TL.getToken() + "<-token\n"+TL.getClasse()+"<-Classe\n"+TL.getType()+"<-Type");
               
               casaToken("id");
               
               //System.out.println("TL:"+TL.getLex() + "<-lex\n"+TL.getToken() + "<-token\n"+TL.getClasse()+"<-Classe\n"+TL.getType()+"<-Type");
            if(tempTok.getClasse() != ""){
			
            casaToken("<-");
			
            String cfor1 = Exp();
			//verificacao tipo, id e exp 
                        
                        System.out.println(tempTok.getType());
                        System.out.println(cfor1);
			if(tempTok.getType().equals(cfor1)){
				tempTok.setValue(tempTok2.getLex());
			}else{
				System.out.println("Error : Tipos incompatíveis. no for");
			}
            //String expT = TL.getType();
            casaToken("to");
            String cfor2 = Exp();
			
			if(tempTok.getType().equals(cfor2)){
				//geracao de codigo 
			}else{
				System.out.println("Error : Tipos incompatíveis. no for");	
			}
            //regra 24 FIM
			//geração de codigo, comparar se exp > exp1 se não erro
          }else{
                System.out.println("Error : Identificador não declarado [" + TL.getToken() + "]");
          }
          //step = 1;  - PAROU AQUI PORRA
            if(TL.getToken().equals("step")){
                casaToken("step");
                casaToken("const");
            }
            casaToken("do");
            if(TL.getToken().equals("begin")){
                casaToken("begin");
                while(!(TL.getToken().equals("end"))){
                    C();
                }
                casaToken("end");
            }
            else{
                while(TL.getToken().equals("id") || TL.getToken().equals("for") || TL.getToken().equals("if") || TL.getToken().equals(";") || TL.getToken().equals("readln") || TL.getToken().equals("write") || TL.getToken().equals("writeln")){
                    C();
                }
            }
        }
		//if geracao
        else if (TL.getToken().equals("if")){
            casaToken("if");
            //System.out.println(Exp());
            String cif = Exp();
            //System.out.println(cif);
            //voltar pra boolean 1 
            if(cif.equals("int")){
                System.out.println("if primeiro");
                //gera��o de codigo entra if
            }else if (cif.equals("0")){
                System.out.println("if second");
                //gera��o de codigo pula if
            }else{
               System.out.println("Error : Tipos Incompat�veis. if");
            }
            casaToken("then");
            W();
            casaToken("else");
            W();
        }
        else if (TL.getToken().equals(";")){
            casaToken(";");
        }
        else if (TL.getToken().equals("readln")){
            casaToken("readln");
            casaToken("(");
            tempTok = searchToken(TL.getLex(), alphabet);
            //System.out.println("TO AQUI PORRA    "+tempTok.getLex() + "TL.GETLEX" + TL.getLex());
            System.out.println("Entrei");
            casaToken("id");
            System.out.println("Casei o id");
            if(tempTok.getClasse() != ""){
             if(TL.getToken().equals("[")){
                casaToken("[");
                Exp();
                casaToken("]");
		//geracao de codigo
		//manipulacao vetor elemento a elemento(INT)	
             }
             casaToken(")");
             casaToken(";");
            }else{
                System.out.println("Error : Identificador não declarado [" + tempTok.getToken() + "]");
            } 
        }
        else if (TL.getToken().equals("write")){
            casaToken("write");
            casaToken("(");
            Exp();
            while(TL.getToken().equals(",")){
                casaToken(",");
                Exp();
            }
            casaToken(")");
            casaToken(";");
        }
        else if (TL.getToken().equals("writeln")){
            casaToken("writeln");
            casaToken("(");
            Exp();
            while(TL.getToken().equals(",")){
                casaToken(",");
                Exp();
            }
            casaToken(")");
            casaToken(";");
        }
    }
 
    public static void W(){
        if(TL.getToken().equals("begin")){
            casaToken("begin");
            while(!(TL.getToken().equals("end"))){
                C();
            }
            casaToken("end");
        }
        else{
            C();
        }
 
    }
 
    public static String Exp(){
        //System.out.println("Exp");
        String typeExp = ExpS();
        String typeExp2;
                
        while(TL.getToken().equals("==") || TL.getToken().equals("<>") || TL.getToken().equals("<") || TL.getToken().equals(">") || TL.getToken().equals("<=") || TL.getToken().equals(">=")){
            if(typeExp.equals("int") && TL.getToken().equals("<")){
                casaToken("<");
                // gera��o de codigo
            }
            else if(typeExp.equals("int") && TL.getToken().equals(">")){
                casaToken(">");
                //gera��o de codigo
                //typeExp = "1";
            }
            else if(typeExp.equals("int") && TL.getToken().equals("<=")){
                casaToken("<=");
                //gera��o de codigo
            }
            else if(typeExp.equals("int") && TL.getToken().equals(">=")){
                casaToken(">=");
                //gera��o de codigo
            } 
            else if(TL.getToken().equals("==")){
                casaToken("==");
                //gera��o de codigo
            } else if(TL.getToken().equals("<>")){
                casaToken("<>");
                //gera��o de codigo
            }
            typeExp2 = ExpS();
            //System.out.println("exp1 = "+typeExp + " exp2 = " + typeExp2);
            if(typeExp.equals(typeExp2)){
                //gera��o de c�digo
            }else{
                System.out.println("Erro: Tipos Incompat�veis.");
            }    
        }
        return typeExp;
    }
    
    public static String ExpS(){
        String typeExps, typeExps2;
        if(TL.getToken().equals("+")){
            casaToken("+");
        } else if (TL.getToken().equals("-")){
            casaToken("-");
        }          
           //System.out.println("ExpS");
        typeExps = T();  
            while(TL.getToken().equals("+") || TL.getToken().equals("-") || TL.getToken().equals("or")){
                // verificar se sting aceita concatena�ao
                if((typeExps.equals("int") || typeExps.equals("string")) && TL.getToken().equals("+")){
                    casaToken("+");
                }
                else if(typeExps.equals("int") && TL.getToken().equals("-")){
                    casaToken("-");
                }
                else if((typeExps.equals("0") || typeExps.equals("1")) && TL.getToken().equals("or")){
                    casaToken("or");
                }
                // verifica�ao
                typeExps2 = T();
                
                System.out.println("exp1 = "+typeExps + " exp2 = " + typeExps2);
                if(typeExps.equals(typeExps2)){
                  // gera�ao de codigo  verificar se existe mesmo essa gera�ao
                } else {
                    System.out.println("Error: tipos incopativeis expS");
                }
            }
            return typeExps;
    }
    
    public static String T(){
        //System.out.println("T");
        String typeT = F();
        while(TL.getToken().equals("*") || TL.getToken().equals("/") || TL.getToken().equals("and") || TL.getToken().equals("%")){
          if(typeT.equals("int")){   
            if(TL.getToken().equals("*")){
                casaToken("*");
            }else if (TL.getToken().equals("/")){
                casaToken("/");
            }else if (TL.getToken().equals("%")){
                casaToken("%");
            }    
          }else if (typeT.equals("1") || typeT.equals("0")){
                if(TL.getToken().equals("and")){
                    casaToken("and");
                    //gera��o de codigo AND
                }
          }else{
              System.out.println ("Erro : Tipo incompat�vel.");
          }     
          String typeT1 = F();
          if(typeT.equals(typeT1)){
              //gera��o de codigo
          }else{
              System.out.println("Erro : Tipos Incompat�veis.");
          }       
        }
        return typeT;
    }
 
    public static String F(){
       String result = "";
        if(TL.getToken().equals("(")){
            casaToken("(");
            // add retorno na expressao verificar se expressao vai retornar algo mesmo
            result = Exp(); 
            casaToken(")");
        }else if(TL.getToken().equals("not")){
            if(type.equals("1") || type.equals("0")){
                casaToken("not");
                result = F();
                //gerea�ao de codigo
            }else{
                System.out.println("Erro : Tipos Incompat�veis.");
            } 
        }else if(TL.getToken().equals("const")){
			//geração de codigo?
            if(isNumeric(TL.getLex())){
                TL.setType("int");             
            }else if(TL.getLex().length() == 1){
                TL.setType("char");
            }else{
                TL.setType("String");
            }
            tempTok2 = searchToken(TL.getLex(), alphabet);
            //System.out.println("type = "+TL.getType() + " lex = " + TL.getLex());
            casaToken("const");
            result = tempTok2.getType();
            //System.out.println(result);
        }else if(TL.getToken().equals("id")){
            //System.out.println("akika rai");
            result = TL.getType();
            casaToken("id");
            if(TL.getToken().equals("[")){
                casaToken("[");
                // exp ta retornando algo??
                Exp();
                //gera��o de c�digo
                casaToken("]");
            }
        }else{
            casaToken("not");//tem que melhorar
	}
        return result; 
    }
	
	/*public static ExtractType(Token Token){
		
	}*/
 
    // Program Principal -- chama tudo ai dentro
    public static void main (String [] args) {
 
		//alterar essa budega para token com type, classe e syze direito depois.
        for(String word: reservedWords){
            TokenLex tl = new TokenLex(word, word, "", "", "", "","");
            alphabet.put(index, tl);
            index++;
        }
        /*int x = 0;
        while (x < 200){
            token = analisadorLexico();
            x++;
        }*/
		
 
        TL = analisadorLexico();
		
        S();
        
        
        /*for(Map.Entry<Integer, TokenLex> entry : alphabet.entrySet()){   //print keys and values
            System.out.println(entry.getKey() + " : " +entry.getValue().getToken()+", "+entry.getValue().getLex());
        }*/
        if(interator < lexema.length()){
            System.out.println("Fim de arquivo não esperado!");
        }
		
    }
}
