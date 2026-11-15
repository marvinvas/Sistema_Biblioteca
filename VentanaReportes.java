import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Ventana de los reportes, parte 4
public class VentanaReportes extends JFrame implements ActionListener {

    // Declaramos la tabla y los botones
    JTable tabla;
    JButton btn1, btn2, btn3, btn4;
    DefaultTableModel modelo;

    public VentanaReportes() {
        setTitle("Reportes Biblioteca");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel de arriba con los botones
        JPanel panel = new JPanel();
        btn1 = new JButton("Ver Libros");
        btn2 = new JButton("Ver Socios");
        btn3 = new JButton("Ver Prestamos");
        btn4 = new JButton("Reporte 4");

        // Le ponemos el listener a cada boton
        btn1.addActionListener(this);
        btn2.addActionListener(this);
        btn3.addActionListener(this);
        btn4.addActionListener(this);

        panel.add(btn1);
        panel.add(btn2);
        panel.add(btn3);
        panel.add(btn4);
        add(panel, BorderLayout.NORTH);

        // La tabla donde van a salir los datos
        tabla = new JTable();
        add(new JScrollPane(tabla), BorderLayout.CENTER);
    }

    // Esto se ejecuta cuando le damos clic a un boton
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btn1) {
            mostrarDatos("SELECT * FROM libro");
        }
        if (e.getSource() == btn2) {
            mostrarDatos("SELECT * FROM socio");
        }
        if (e.getSource() == btn3) {
            mostrarDatos("SELECT * FROM prestamo");
        }
        if (e.getSource() == btn4) {
            // Este es el reporte con JOIN
            String sql = "SELECT p.idPrestamo, s.nombre, l.titulo, p.fechaRetiro, p.fechaEntrega "
                    + "FROM prestamo p, socio s, libro l "
                    + "WHERE p.idSocio = s.idSocio AND p.idLibro = l.idLibro";
            mostrarDatos(sql);
        }
    }

    // Metodo para llenar la tabla
    public void mostrarDatos(String sql) {
        try {
            Connection con = Conexion.conectar();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            modelo = new DefaultTableModel();
            // Ponemos las columnas
            int columnas = rs.getMetaData().getColumnCount();
            for (int i = 1; i <= columnas; i++) {
                modelo.addColumn(rs.getMetaData().getColumnName(i));
            }
            // Llenamos las filas
            while (rs.next()) {
                Object[] fila = new Object[columnas];
                for (int i = 1; i <= columnas; i++) {
                    fila[i - 1] = rs.getObject(i);
                }
                modelo.addRow(fila);
            }
            tabla.setModel(modelo);
            con.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex);
        }
    }

    public static void main(String[] args) {
        VentanaReportes v = new VentanaReportes();
        v.setVisible(true);
    }
}
