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
                    else if(c=='"'){
                        estado=24;
                        lexema+=c;
                    }
                    else if(c=='/'){
                        estado=26;
                        lexema+=c;
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
                    else if(!Character.isLetter(c)){
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
                        Token t = new Token(TipoToken.NUMBER, lexema);
                        tokens.add(t);

                        estado = 0;
                        lexema = "";
                        i--;
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
                        Token t = new Token(TipoToken.NUMBER, lexema);
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
                        Token t = new Token(TipoToken.NUMBER, lexema);
                        tokens.add(t);

                        estado = 0;
                        lexema = "";
                        i--;
                    }
                    break;

                case 19, 20:
                    if(Character.isDigit(c)){
                        estado = 20;
                        lexema += c;
                    }
                    else{
                        Token t = new Token(TipoToken.NUMBER, lexema);
                        tokens.add(t);

                        estado = 0;
                        lexema = "";
                        i--;
                    }

                    break;

                //CADENAS
                case 24:
                    if(c=='"'){
                        Token t = new Token(TipoToken.STRING, lexema);
                        tokens.add(t);

                        estado = 0;
                        lexema = "";
                        i--;
                    }else if(c=='\n'){ //Detectar salto de linea
                        Interprete.error(lin, "error de salto");
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


            //TOKEN DE UN CARACTER




            }

        }


        return tokens;
    }
}
