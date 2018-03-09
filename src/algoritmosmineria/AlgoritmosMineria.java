/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algoritmosmineria;

import algoritmosAgrupamiento.Naivayes;
import algoritmosAgrupamiento.Evaluacion;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
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
        File file = new File("/home/debian/Documentos/unicauca/actualsemestre/mineria/dataset/drug1n.arff");
//        File file = new File("/home/debian/Documentos/unicauca/actualsemestre/mineria/dataset/clasificacion-drug.arff");
//        File file = new File("/home/debian/Documentos/unicauca/actualsemestre/mineria/dataset/weather.arff");
//        File file = new File("/home/debian/Documentos/unicauca/actualsemestre/mineria/dataset/titanic.arff");
        if (file != null) {
            System.out.println("nombre archivo:" + file.getAbsolutePath());
            ArffLoader arrfloader = new ArffLoader();
            arrfloader.setFile(file);
            Instances instancias = arrfloader.getDataSet();
//            instancias.setClass(instancias.attribute("Sobrevivio"));
            instancias.setClass(instancias.attribute("Drug"));
//            instancias.setClass(instancias.attribute("play"));
            Naivayes nv = new Naivayes();
            nv.crearModelo(instancias);

//            nv.evaluarInstancias(instancias);
            double[] probabilidades = nv.evaluarInstancia(instancias.instance(4));
            for (int i = 0; i < probabilidades.length; i++) {
                System.out.println("val: " + instancias.classAttribute().value(i) + ": " + String.format("%.2f", probabilidades[i]));
            }
            Evaluacion eva = new Evaluacion(instancias);
            int[][] matProb = eva.crearMatrizDeConfucion(nv, instancias);
            System.out.println("recall: \n"+eva.recall_string());
            System.out.println("fMeasure: \n"+eva.fMeasure_string());
            for (int[] ds : matProb) {
                System.out.println("[" + Arrays.toString(ds));
            }
            System.out.println("--");
            matProb = eva.crearMatrizDeConfucionValidacionCruzada(instancias, 3);
            System.out.println("recall: \n"+eva.recall_string());
            System.out.println("fMeasure: \n"+eva.fMeasure_string());
            for (int[] ds : matProb) {
                System.out.println("[" + Arrays.toString(ds));
            }
        }

    }

}
