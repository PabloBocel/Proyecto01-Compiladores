
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class InterfazGrafica extends JFrame {

    private JTextArea codigoFuente;
    private JButton analizarButton;
    private JTabbedPane panelResultados;
    private JTextArea resultadoTokens;
    private JTextArea resultadoSimbolos;
    private JTextArea resultadoErrores;

    public InterfazGrafica() {
        // Configurar la ventana principal
        setTitle("Analizador Léxico");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Panel superior: Área de código fuente
        JPanel panelCodigo = new JPanel(new BorderLayout());
        panelCodigo.setBorder(BorderFactory.createTitledBorder("Código Fuente"));
        panelCodigo.setPreferredSize(new Dimension(1000, 450));
        codigoFuente = new JTextArea();
        codigoFuente.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollCodigo = new JScrollPane(codigoFuente);
        scrollCodigo.setPreferredSize(new Dimension(900, 300));
        panelCodigo.add(scrollCodigo, BorderLayout.CENTER);

        // Botón de análisis
        analizarButton = new JButton("Analizar Código");
        analizarButton.setFont(new Font("Arial", Font.BOLD, 14));
        JPanel panelBoton = new JPanel();
        panelBoton.setPreferredSize(new Dimension(200, 400));
        panelBoton.add(analizarButton, BorderLayout.CENTER);

        // Panel inferior: Resultados con pestañas y barras de desplazamiento
        panelResultados = new JTabbedPane();
        panelResultados.setPreferredSize(new Dimension(750, 400));

        //Crear tabla para almacenar símbolos
        String[] columnas = {"Identificador", "Tipo", "Ámbito", "Valor"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

        JTable tablaSimbolos = new JTable(modelo);

        resultadoTokens = crearAreaTextoConScroll();
        JScrollPane resultadoSimbolos = new JScrollPane(tablaSimbolos);
        resultadoErrores = crearAreaTextoConScroll();

        panelResultados.addTab("Tokens", new JScrollPane(resultadoTokens));
        panelResultados.addTab("Símbolos", new JScrollPane(resultadoSimbolos));
        panelResultados.addTab("Errores", new JScrollPane(resultadoErrores));

        // Organizar componentes
        add(panelCodigo, BorderLayout.NORTH);
        add(panelBoton, BorderLayout.EAST);
        add(panelResultados, BorderLayout.WEST);

        // Acción del botón (versión mejorada)
        analizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String codigo = codigoFuente.getText();
                if (codigo.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Ingrese un código fuente primero.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Limpiar resultados anteriores
                resultadoTokens.setText("");
                resultadoErrores.setText("");

                // Ejecutar análisis
                try {
                    AnalizadorLexico analizador = new AnalizadorLexico(codigo);
                    analizador.analizar(codigo);
                    mostrarResultados(analizador);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error durante el análisis: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private JTextArea crearAreaTextoConScroll() {
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        return area;
    }

    private void mostrarResultados(AnalizadorLexico analizador) {
        resultadoTokens.append("=== TOKENS ===\n");
        analizador.getTokens().forEach(token -> resultadoTokens.append(token + "\n"));

        //resultadoSimbolos.append("=== TABLA DE SÍMBOLOS ===\n");
        //analizador.getTablaSimbolos().forEach(simbolo -> resultadoSimbolos.append(simbolo + "\n"));

        resultadoErrores.append("=== ERRORES ===\n");
        analizador.getErrores().forEach(error -> resultadoErrores.append(error + "\n"));
    }

    public void mostrar() {
        setVisible(true);
        setLocationRelativeTo(null); // Centrar ventana
    }
}
