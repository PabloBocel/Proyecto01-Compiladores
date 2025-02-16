
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InterfazGrafica extends JFrame {

    private JTextArea codigoFuente;
    private JButton analizarButton;
    private JTextArea resultadoTokens;
    private JTextArea resultadoSimbolos;
    private JTextArea resultadoErrores;

    public InterfazGrafica() {
        // Configurar la ventana principal
        setTitle("Analizador Léxico");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Crear componentes
        codigoFuente = new JTextArea();
        analizarButton = new JButton("Analizar");
        resultadoTokens = new JTextArea();
        resultadoSimbolos = new JTextArea();
        resultadoErrores = new JTextArea();

        // Configurar el layout
        setLayout(new BorderLayout());

        // Panel superior: Área de texto para el código fuente
        JPanel panelCodigo = new JPanel();
        panelCodigo.setLayout(new BorderLayout());
        panelCodigo.add(new JLabel("Código fuente:"), BorderLayout.NORTH);
        panelCodigo.add(new JScrollPane(codigoFuente), BorderLayout.CENTER);

        // Panel central: Botón de análisis
        JPanel panelBotones = new JPanel();
        panelBotones.add(analizarButton);

        // Panel inferior: Resultados (tokens, tabla de símbolos, errores)
        JPanel panelResultados = new JPanel();
        panelResultados.setLayout(new GridLayout(1, 3));
        panelResultados.add(new JScrollPane(resultadoTokens));
        panelResultados.add(new JScrollPane(resultadoSimbolos));
        panelResultados.add(new JScrollPane(resultadoErrores));

        // Agregar paneles a la ventana principal
        add(panelCodigo, BorderLayout.NORTH);
        add(panelBotones, BorderLayout.CENTER);
        add(panelResultados, BorderLayout.SOUTH);

        // Acción del botón "Analizar"
        analizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String codigo = codigoFuente.getText();
                // Llamar al analizador léxico
                AnalizadorLexico analizador = new AnalizadorLexico();
                analizador.analizar(codigo);

                // Mostrar resultados en las áreas de texto
                resultadoTokens.setText(analizador.getTokens().toString());
                resultadoSimbolos.setText(analizador.getTablaSimbolos().toString());
                resultadoErrores.setText(analizador.getErrores().toString());
            }
        });
    }

    public void mostrar() {
        setVisible(true);
    }
}
