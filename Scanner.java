import java.util.*;
/*FALTANTES:
* Tokens de un carácter (símbolos de la última tabla de "Criterios de evaluación.pdf")
* Atributo "lineal" de Token para conversión de tipo de número (int, long, float, etc.) **Preguntar
* Archivo en path
*
* */
public class Scanner {
    private static int lin=0;
    private static final Map<String, TipoToken> palabrasReservadas;

    static {
        palabrasReservadas = new HashMap<>();
        palabrasReservadas.put("and",    TipoToken.AND);
        palabrasReservadas.put("else",   TipoToken.ELSE);
        palabrasReservadas.put("false",  TipoToken.FALSE);
        palabrasReservadas.put("for",    TipoToken.FOR);
        palabrasReservadas.put("fun",    TipoToken.FUN);
        palabrasReservadas.put("if",     TipoToken.IF);
        palabrasReservadas.put("null",   TipoToken.NULL);
        palabrasReservadas.put("or",     TipoToken.OR);
        palabrasReservadas.put("print",  TipoToken.PRINT);
        palabrasReservadas.put("return", TipoToken.RETURN);
        palabrasReservadas.put("true",   TipoToken.TRUE);
        palabrasReservadas.put("var",    TipoToken.VAR);
        palabrasReservadas.put("while",  TipoToken.WHILE);
    }

    private final String source;

    private final List<Token> tokens = new ArrayList<>();
    
    public Scanner(String source){
        this.source = source + " ";
    }

    public List<Token> scan() throws Exception {
        String lexema = "";
        int estado = 0;
        char c;
        lin++;

        for(int i=0; i<source.length(); i++){
            c = source.charAt(i);

            switch (estado){
                case 0:
                    if(Character.isLetter(c)){
                        estado = 13;
                        lexema += c;
                    }
                    else if(Character.isDigit(c)){
                        estado = 15;
                        lexema += c;
                    }
                    else if(c==34){
                        estado=24;
                        //lexema+=c;
                    }
                    else if(c=='/'){
                        estado=26;
                        lexema+=c;
                    }
                    else if(c=='>'){
                        estado=1;
                        lexema+=c;
                    }
                    else if(c=='<'){
                        estado=4;
                        lexema+=c;
                    }
                    else if(c=='='){
                        estado=7;
                        lexema+=c;
                    }
                    else if (c=='!') {
                        estado=10;
                        lexema+=c;
                    }
                    else if(c==','){
                        estado=33;
                        lexema+=c;
                    }
                    else if (c=='.') {
                        estado=34;
                        lexema+=c;
                    }
                    else if (c==';') {
                        estado=35;
                        lexema+=c;
                    }
                    else if (c=='+') {
                        estado=36;
                        lexema+=c;
                    }
                    else if (c=='-') {
                        estado=37;
                        lexema+=c;
                    }
                    else if (c=='*') {
                        estado=38;
                        lexema+=c;
                    }
                    else if (c=='(') {
                        estado=39;
                        lexema+=c;
                    }
                    else if (c==')') {
                        estado=40;
                        lexema+=c;
                    }
                    else if (c=='{') {
                        estado=41;
                        lexema+=c;
                    }
                    else if (c=='}') {
                        estado=42;
                        lexema+=c;
                    }
                    else if (c=='\n'){
                        estado=43;
                        lexema+="salto";
                    }
                    else if(c==' '){
                        estado=0;
                    }
                    else if(c == '\t'){
                        estado=0;
                    }
                    else if(c == '\r'){
                        estado=0;
                    }
                    else{
                        lexema += c;
                        Interprete.error(lin, "El caracter: "+lexema+" no pertenece");
                        estado=-1;
                    }
                    break;
                case 1:
                    //estado 2
                    if(c=='='){
                        Token t = new Token(TipoToken.GREATER_EQUAL, lexema);
                        tokens.add(t);
                        estado=0;
                        lexema="";
                    }
                    //estado 3
                    else{
                        Token t = new Token(TipoToken.GREATER, lexema);
                        tokens.add(t);
                        estado=0;
                        lexema="";
                        i--;
                    }
                    break;
                case 4:
                    //estado 5
                    if(c=='='){
                        Token t = new Token(TipoToken.LESS_EQUAL, lexema);
                        tokens.add(t);
                        estado=0;
                        lexema="";
                    }
                    //estado 6
                    else{
                        Token t = new Token(TipoToken.LESS, lexema);
                        tokens.add(t);
                        estado=0;
                        lexema="";
                        i--;
                    }
                    break;
                case 7:
                    //estado 8
                    if(c=='='){
                        Token t = new Token(TipoToken.EQUAL_EQUAL, lexema);
                        tokens.add(t);
                        estado=0;
                        lexema="";
                    }
                    //estado 9
                    else{
                        Token t = new Token(TipoToken.EQUAL, lexema);
                        tokens.add(t);
                        estado=0;
                        lexema="";
                        i--;
                    }
                    break;
                case 10:
                    //estado 11
                    if(c=='='){
                        Token t = new Token(TipoToken.BANG_EQUAL, lexema);
                        tokens.add(t);
                        estado=0;
                        lexema="";
                    }
                    //estado 12
                    else{
                        Token t = new Token(TipoToken.BANG, lexema);
                        tokens.add(t);
                        estado=0;
                        lexema="";
                        i--;
                    }
                    break;

            //IDENTIFICADORES Y PALABRAS RESERVADAS
                case 13:
                    if(Character.isLetter(c) || Character.isDigit(c)){
                        lexema += c;
                    }
                    else{
                        // Vamos a crear el Token de identificador o palabra reservada
                        TipoToken tt = palabrasReservadas.get(lexema);

                        if(tt == null){
                            Token t = new Token(TipoToken.IDENTIFIER, lexema);
                            tokens.add(t);
                        }
                        else{
                            Token t = new Token(tt, lexema);
                            tokens.add(t);
                        }

                        estado = 0;
                        lexema = "";
                        i--;
                    }
                    break;

            //NÚMEROS
                case 15:
                    if(Character.isDigit(c)){
                        lexema += c;
                    }
                    else if(c == '.'){
                        estado=16;
                        lexema +=c;
                    }
                    else if (c=='E'){
                            estado = 18;
                            lexema += c;
                    }
                    else{
                        Token t = new Token(TipoToken.NUMBER, lexema, Integer.valueOf(lexema));
                        tokens.add(t);

                        estado = 0;
                        lexema = "";
                        i--;
                    }
                    break;

                case 16:
                    if(Character.isDigit(c)) {
                        estado = 17;
                        lexema += c;
                    }else{
                        Interprete.error(lin, "error de secuencia en el numero: " + lexema);
                        estado=-1;
                    }
                    break;
                case 17:
                    if(Character.isDigit(c)){
                        lexema += c;
                    }
                    else if (c=='E'){
                            estado = 18;
                            lexema += c;
                    }
                    else{
                        Token t = new Token(TipoToken.NUMBER, lexema, Float.valueOf(lexema));
                        tokens.add(t);

                        estado = 0;
                        lexema = "";
                        i--;
                    }
                    break;
                case 18:
                    if(Character.isDigit(c)){
                        estado = 20;
                        lexema += c;
                    }
                    else if(c=='+'||c=='-'){
                        estado=19;
                        lexema+=c;
                    }
                    else{
                        Interprete.error(lin, "error de secuencia en el numero: " + lexema);
                        estado=-1;
                    }
                    break;

                case 19:
                    if (Character.isDigit(c)){
                        estado=20;
                        lexema+=c;
                    }
                    else{
                        Interprete.error(lin, "error de secuencia en el numero: " + lexema);
                        estado=-1;
                    }
                    break;
                    
                case 20:
                    if(Character.isDigit(c)){
                        estado = 20;
                        lexema += c;
                    }
                    else{
                        Token t = new Token(TipoToken.NUMBER, lexema, Float.valueOf(lexema));
                        tokens.add(t);

                        estado = 0;
                        lexema = "";
                        i--;
                    }

                    break;

                //CADENAS
                case 24:
                    if(c=='"'){
                        Token t = new Token(TipoToken.STRING, lexema, lexema);
                        tokens.add(t);

                        estado = 0;
                        lexema = "";
                        //i--;
                    }else if(c=='\n'){ //Detectar salto de linea
                        Interprete.error(lin, "error de salto en la cadena " + lexema);
                        estado=-1;
                    }else{
                        lexema+=c;
                    }
                    break;

            //COMENTARIOS
                case 26:
                    if(c=='*'){
                        estado=27;
                    }else if(c=='/'){
                        estado=30;
                    }else{
                        estado=32;
                        lexema+=c;
                    }
                    break;

                case 27:
                    if(c=='*') {
                        estado = 28;
                    }
                    break;

                case 28:
                    if(c=='*'){
                        lexema+=c;
                    }else if(c=='/'){
                        estado=29;
                    }else{
                        estado=27;
                        lexema+=c;
                    }
                    break;

                case 29, 31:
                    System.out.println("<comentario>");
                    estado=0;
                    lexema="";
                    i--;
                    break;

                case 30:
                  if(c=='\n'){
                        estado=31;
                  }
                  //estado=31;
                    break;

                case 32:
                    Token t = new Token(TipoToken.SLASH, lexema);
                    tokens.add(t);

                    estado = 0;
                    lexema = "";
                    i--;

                    break;
                case 33:
                    Token tc = new Token(TipoToken.COMMA, lexema);
                    tokens.add(tc);

                    estado = 0;
                    lexema = "";
                    i--;
                    break;
                case 34:
                    Token td = new Token(TipoToken.DOT, lexema);
                    tokens.add(td);

                    estado = 0;
                    lexema = "";
                    i--;
                    break;
                case 35:
                    Token ts = new Token(TipoToken.SEMICOLON, lexema);
                    tokens.add(ts);

                    estado = 0;
                    lexema = "";
                    i--;
                    break;
                case 36:
                    Token tp = new Token(TipoToken.PLUS, lexema);
                    tokens.add(tp);

                    estado = 0;
                    lexema = "";
                    i--;
                    break;
                case 37:
                    Token tm = new Token(TipoToken.MINUS, lexema);
                    tokens.add(tm);

                    estado = 0;
                    lexema = "";
                    i--;
                    break;
                case 38:
                    Token te = new Token(TipoToken.STAR, lexema);
                    tokens.add(te);

                    estado = 0;
                    lexema = "";
                    i--;
                    break;
                case 39:
                    Token tlp = new Token(TipoToken.LEFT_PAREN, lexema);
                    tokens.add(tlp);

                    estado = 0;
                    lexema = "";
                    i--;
                    break;
                case 40:
                    Token trp = new Token(TipoToken.RIGHT_PAREN, lexema);
                    tokens.add(trp);

                    estado = 0;
                    lexema = "";
                    i--;
                    break;
                case 41:
                    Token tlb = new Token(TipoToken.LEFT_BRACE, lexema);
                    tokens.add(tlb);

                    estado = 0;
                    lexema = "";
                    i--;
                    break;
                case 42:
                    Token trb = new Token(TipoToken.RIGHT_BRACE, lexema);
                    tokens.add(trb);

                    estado = 0;
                    lexema = "";
                    i--;
                    break;
                case 43:
                    Token tef = new Token(TipoToken.EOF, lexema);
                    tokens.add(tef);

                    estado = 0;
                    lexema = "";
                    i--;
                    lin++;
                    break;
            

            }

        }


        return tokens;
    }
}
