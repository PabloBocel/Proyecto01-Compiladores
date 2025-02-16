
import java.util.ArrayList;
import java.util.List;

public class AnalizadorLexico {

    private List<String> tokens;
    private List<String> tablaSimbolos;
    private List<String> errores;

    public AnalizadorLexico() {
        tokens = new ArrayList<>();
        tablaSimbolos = new ArrayList<>();
        errores = new ArrayList<>();
    }

    public void analizar(String codigo) {
        // Aquí implementarás la lógica del análisis léxico
        // Por ahora, solo simulamos algunos tokens y errores
        tokens.add("Token1");
        tokens.add("Token2");
        tablaSimbolos.add("Variable1");
        tablaSimbolos.add("Variable2");
        errores.add("Error léxico en línea 1: Carácter inválido");
    }

    public List<String> getTokens() {
        return tokens;
    }

    public List<String> getTablaSimbolos() {
        return tablaSimbolos;
    }

    public List<String> getErrores() {
        return errores;
    }
}
