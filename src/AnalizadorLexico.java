
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnalizadorLexico {

    private List<Token> tokens;
    private List<Simbolo> tablaSimbolos;
    private List<ErrorLexico> errores;
    private int lineaActual = 1;
    private int columnaActual = 1;

    // Expresiones regulares para los tokens
    private final Pattern patronComentarioLinea = Pattern.compile("//.*");
    private final Pattern patronComentarioBloque = Pattern.compile("/\\*.*?\\*/", Pattern.DOTALL);
    private final Pattern patronCadena = Pattern.compile("\"[^\"]*\"");
    private final Pattern patronCaracter = Pattern.compile("'[^']'");
    private final Pattern patronNumeroReal = Pattern.compile("-?\\d+\\.\\d+");
    private final Pattern patronNumeroEntero = Pattern.compile("-?\\d+");
    private final Pattern patronIdentificador = Pattern.compile("[a-zA-Z_][a-zA-Z0-9_]*");
    private final Pattern patronOperadores = Pattern.compile("[+\\-*/^#=<>!&|]|>=|<=|==|!=|&&|\\|\\|");
    private final Pattern patronSignos = Pattern.compile("[();{},]");

    public AnalizadorLexico() {
        tokens = new ArrayList<>();
        tablaSimbolos = new ArrayList<>();
        errores = new ArrayList<>();
    }

    public void analizar(String codigo) {
        String[] lineas = codigo.split("\\r?\\n");
        for (String linea : lineas) {
            procesarLinea(linea);
            lineaActual++;
            columnaActual = 1;
        }
    }

    private void procesarLinea(String linea) {
        String texto = linea.trim();
        while (!texto.isEmpty()) {
            // Ignorar comentarios
            Matcher matcherComentarioLinea = patronComentarioLinea.matcher(texto);
            if (matcherComentarioLinea.lookingAt()) {
                break;
            }

            Matcher matcherComentarioBloque = patronComentarioBloque.matcher(texto);
            if (matcherComentarioBloque.lookingAt()) {
                texto = texto.substring(matcherComentarioBloque.end()).trim();
                continue;
            }

            // Procesar tokens
            boolean encontrado = false;

            // Cadenas
            Matcher matcherCadena = patronCadena.matcher(texto);
            if (matcherCadena.lookingAt()) {
                agregarToken("CADENA", matcherCadena.group(), lineaActual, columnaActual);
                texto = texto.substring(matcherCadena.end()).trim();
                columnaActual += matcherCadena.end();
                encontrado = true;
                continue;
            }

            // Caracteres
            Matcher matcherCaracter = patronCaracter.matcher(texto);
            if (matcherCaracter.lookingAt()) {
                agregarToken("CARACTER", matcherCaracter.group(), lineaActual, columnaActual);
                texto = texto.substring(matcherCaracter.end()).trim();
                columnaActual += matcherCaracter.end();
                encontrado = true;
                continue;
            }

            // Números reales
            Matcher matcherReal = patronNumeroReal.matcher(texto);
            if (matcherReal.lookingAt()) {
                agregarToken("REAL", matcherReal.group(), lineaActual, columnaActual);
                texto = texto.substring(matcherReal.end()).trim();
                columnaActual += matcherReal.end();
                encontrado = true;
                continue;
            }

            // Números enteros
            Matcher matcherEntero = patronNumeroEntero.matcher(texto);
            if (matcherEntero.lookingAt()) {
                agregarToken("ENTERO", matcherEntero.group(), lineaActual, columnaActual);
                texto = texto.substring(matcherEntero.end()).trim();
                columnaActual += matcherEntero.end();
                encontrado = true;
                continue;
            }

            // Operadores y signos
            Matcher matcherOperadores = patronOperadores.matcher(texto);
            if (matcherOperadores.lookingAt()) {
                String operador = matcherOperadores.group();
                agregarToken("OPERADOR", operador, lineaActual, columnaActual);
                texto = texto.substring(operador.length()).trim();
                columnaActual += operador.length();
                encontrado = true;
                continue;
            }

            Matcher matcherSignos = patronSignos.matcher(texto);
            if (matcherSignos.lookingAt()) {
                String signo = matcherSignos.group();
                agregarToken("SIGNO", signo, lineaActual, columnaActual);
                texto = texto.substring(signo.length()).trim();
                columnaActual += signo.length();
                encontrado = true;
                continue;
            }

            // Identificadores y palabras reservadas
            Matcher matcherIdentificador = patronIdentificador.matcher(texto);
            if (matcherIdentificador.lookingAt()) {
                String identificador = matcherIdentificador.group().toLowerCase();
                if (esPalabraReservada(identificador)) {
                    agregarToken("PALABRA_RESERVADA", identificador, lineaActual, columnaActual);
                } else {
                    agregarToken("IDENTIFICADOR", identificador, lineaActual, columnaActual);
                    agregarSimbolo(identificador, "VARIABLE", lineaActual, columnaActual);
                }
                texto = texto.substring(identificador.length()).trim();
                columnaActual += identificador.length();
                encontrado = true;
                continue;
            }

            // Si no se encontró ningún token válido, registrar error
            if (!encontrado) {
                String caracterInvalido = texto.substring(0, 1);
                errores.add(new ErrorLexico(lineaActual, columnaActual, "Carácter inválido: " + caracterInvalido));
                texto = texto.substring(1).trim();
                columnaActual++;
            }
        }
    }

    private boolean esPalabraReservada(String token) {
        String[] palabrasReservadas = {
            "entero", "real", "booleano", "caracter", "cadena",
            "si", "sino", "para", "mientras", "hacer", "escribirlinea",
            "escribir", "longitud", "acadena"
        };
        for (String palabra : palabrasReservadas) {
            if (palabra.equalsIgnoreCase(token)) {
                return true;
            }
        }
        return false;
    }

    private void agregarToken(String tipo, String valor, int linea, int columna) {
        tokens.add(new Token(tipo, valor, linea, columna));
    }

    private void agregarSimbolo(String nombre, String tipo, int linea, int columna) {
        tablaSimbolos.add(new Simbolo(nombre, tipo, linea, columna));
    }

    // Clases auxiliares
    public static class Token {

        String tipo;
        String valor;
        int linea;
        int columna;

        public Token(String tipo, String valor, int linea, int columna) {
            this.tipo = tipo;
            this.valor = valor;
            this.linea = linea;
            this.columna = columna;
        }

        @Override
        public String toString() {
            return String.format("[%s] %s (Línea %d, Col %d)", tipo, valor, linea, columna);
        }
    }

    public static class Simbolo {

        String nombre;
        String tipo;
        int linea;
        int columna;

        public Simbolo(String nombre, String tipo, int linea, int columna) {
            this.nombre = nombre;
            this.tipo = tipo;
            this.linea = linea;
            this.columna = columna;
        }

        @Override
        public String toString() {
            return String.format("%s - %s (Línea %d)", nombre, tipo, linea);
        }
    }

    public static class ErrorLexico {

        int linea;
        int columna;
        String mensaje;

        public ErrorLexico(int linea, int columna, String mensaje) {
            this.linea = linea;
            this.columna = columna;
            this.mensaje = mensaje;
        }

        @Override
        public String toString() {
            return String.format("Error en L%d:%d - %s", linea, columna, mensaje);
        }
    }

    // Getters para la interfaz gráfica
    public List<Token> getTokens() {
        return tokens;
    }

    public List<Simbolo> getTablaSimbolos() {
        return tablaSimbolos;
    }

    public List<ErrorLexico> getErrores() {
        return errores;
    }
}
