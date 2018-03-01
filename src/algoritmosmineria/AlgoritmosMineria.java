/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algoritmosmineria;

import algoritmosAgrupamiento.Naivayes;
import java.io.File;
import java.io.IOException;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

/**
 *
 * @author debian
 */
public class AlgoritmosMineria {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        BuscarArchivo ba = new BuscarArchivo();

//        File file = ba.buscar();
        File file = new File("/home/debian/Documentos/unicauca/actualsemestre/mineria/dataset/weather.arff");
        if (file != null) {
            System.out.println("nombre archivo:" + file.getAbsolutePath());
            ArffLoader arrfloader = new ArffLoader();
            arrfloader.setFile(file);
            Naivayes nv = new Naivayes();
            Instances instancias = arrfloader.getDataSet();
            nv.ejecutar(instancias, instancias.attribute("play").index());

        }

    }

}
