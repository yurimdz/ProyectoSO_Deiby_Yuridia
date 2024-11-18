package org.example;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.ArrayList;

public class GUI extends JFrame {

    private JTable JTableTabla;
    private EstructuraTablaProcesos ModeloTabla;
    public volatile BloqueMemoria[] bloquesMemoria;

    public GUI(String tituloVentana, ArrayList<ProcesoSistema> procesos) {

        setTitle(tituloVentana);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(0, 0, 1800, 800);
        setLayout(new BorderLayout());

        // Estilo de la tabla y encabezado
        ModeloTabla = new EstructuraTablaProcesos(procesos);
        JTableTabla = new JTable(ModeloTabla);
        JTableTabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        JTableTabla.getTableHeader().setForeground(Color.WHITE); 
        JTableTabla.getTableHeader().setBackground(new Color(250, 223, 245));
        ((DefaultTableCellRenderer) JTableTabla.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

        // Estilo del encabezado de la tabla
        JTableHeader header = JTableTabla.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 12));
        header.setBackground(new Color(176, 184, 241));
        header.setForeground(Color.DARK_GRAY);
        header.setReorderingAllowed(false);

        // Renderizador centrado para el contenido de la tabla
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < JTableTabla.getColumnCount(); i++) {
            JTableTabla.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane jScrollPane = new JScrollPane(JTableTabla);
        jScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        getContentPane().add(jScrollPane, BorderLayout.NORTH);

        // Panel de memoria con celdas personalizadas
        JPanel conMemoria = new JPanel();
        conMemoria.setLayout(new GridLayout(4, 8, 5, 5));
        conMemoria.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bloquesMemoria = new BloqueMemoria[32];

        for (int i = 0; i < 32; i++) {
            JLabel casilla = new JLabel("Disponible " + i, SwingConstants.CENTER);
            casilla.setFont(new Font("Roboto", Font.BOLD, 14));
            casilla.setOpaque(true);
            casilla.setBackground(Color.WHITE);
            casilla.setForeground(Color.DARK_GRAY);
            casilla.setBorder(BorderFactory.createLineBorder(new Color(160, 160, 160), 3));
            bloquesMemoria[i] = new BloqueMemoria(i, false, casilla);
            conMemoria.add(bloquesMemoria[i].getLabelBloque());
        }
        getContentPane().add(conMemoria, BorderLayout.CENTER);

        setVisible(true);
    }

    public void actualizar() {
        this.revalidate();
        this.repaint();
        ModeloTabla.updateTable();
    }
}
