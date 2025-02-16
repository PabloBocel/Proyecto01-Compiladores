
import javax.swing.SwingUtilities;

public class App {

    public static void main(String[] args) {
        // Ejecutar la interfaz gráfica en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> {
            InterfazGrafica interfaz = new InterfazGrafica();
            interfaz.mostrar();
        });
    }
}
