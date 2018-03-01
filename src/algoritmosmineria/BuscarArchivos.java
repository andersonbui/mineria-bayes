/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algoritmosmineria;

import java.io.File;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author debian
 */
public class BuscarArchivos {
    
    private int seleccion;
    private JFileChooser buscar;
    private FileNameExtensionFilter filter;
    private DefaultTableModel model;
    public static ArrayList direcciones;

    public BuscarArchivos() {
        buscar = new JFileChooser();
        filter = new FileNameExtensionFilter("arff", "arff");
//        filter = new FileNameExtensionFilter("JPG & GIF", "arff", "gif");
        nuevoModelo();
        direcciones = new ArrayList();
    }

    public void buscar() {
//    public void buscar(JTable tabla) {
        try {
            nuevoModelo();
        buscar.setFileFilter(filter);
        buscar.setMultiSelectionEnabled(true);
        seleccion = buscar.showOpenDialog(buscar);

        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File[] fichero = buscar.getSelectedFiles();

            for (int i = 0; i < fichero.length; i++) {
                String nombre[] = {fichero[i].getName(), fichero[i].getAbsolutePath()};
                direcciones.add(fichero[i].getAbsoluteFile());
                model.addRow(nombre);
            }

        }else{
            buscar.cancelSelection();
        }
//        tabla.setModel(model);
        } catch (Exception e) {
            System.out.println("Error:"+e.getMessage());
        }

    }

    public void nuevoModelo(){
        model = new DefaultTableModel();
        model.addColumn("Nombre");
        model.addColumn("Dirección");
    }
}
