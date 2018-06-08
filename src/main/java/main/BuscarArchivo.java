/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.File;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author debian
 */
public class BuscarArchivo {

    private int seleccion;
    private JFileChooser buscar;
    private FileNameExtensionFilter filter;

    public BuscarArchivo() {
        buscar = new JFileChooser();
        filter = new FileNameExtensionFilter("arff", "arff");
//        filter = new FileNameExtensionFilter("JPG & GIF", "arff", "gif");
    }

    public File buscar() {
        try {
            buscar.setFileFilter(filter);
            buscar.setMultiSelectionEnabled(false);
            seleccion = buscar.showOpenDialog(buscar);
            
            if (seleccion == JFileChooser.APPROVE_OPTION) {
                File fichero = buscar.getSelectedFile();
                return fichero;
            } else {
                buscar.cancelSelection();
            }
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        }
        return null;
    }

}
