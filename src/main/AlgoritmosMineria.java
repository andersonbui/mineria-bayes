/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import algoritmosAgrupamiento.NaiveBayes;
import algoritmosAgrupamiento.Evaluacion;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import weka.core.Instance;
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
//        File file = new File("/home/debian/Documentos/unicauca/actualsemestre/mineria/dataset/drug1n.arff");
        File file = new File("/home/debian/Documentos/unicauca/actualsemestre/mineria/dataset/clasificacion-drug.arff");
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
            NaiveBayes nv = new NaiveBayes();
            nv.crearModelo(instancias);

//            nv.evaluarInstancias(instancias);
            Instance instancia = (Instance) instancias.instance(4).copy();
            instancia.setValue(0, 49);
            instancia.setValue(1, 1);
            instancia.setValue(2, 2);
            instancia.setValue(3, 0);
            instancia.setValue(4, .789637);
            instancia.setValue(5, .048518);
            
//            instancia.setValue(0, 60);
//            instancia.setValue(1, 1);
//            instancia.setValue(2, 2);
//            instancia.setValue(3, 0);
//            instancia.setValue(4, .777205);
//            instancia.setValue(5, .05123);
//            instancia 
            double[] probabilidades = nv.evaluarInstancia(instancia);
            System.out.println(nv);
                System.out.println("EVALUACION DE INSTANCIA");
            for (int i = 0; i < probabilidades.length; i++) {
                System.out.println(instancias.classAttribute().value(i) + ": " + String.format("%.2f", probabilidades[i]));
            }
            Evaluacion eva = new Evaluacion(instancias);
            int[][] matProb = eva.crearMatrizDeConfucion(nv, instancias);
            System.out.println("\nrecall: \n" + eva.recall_string());
            System.out.println("fMeasure: \n" + eva.fMeasure_string());
            System.out.println("precision: \n" + eva.precision_string());
                System.out.println(eva.evaluarConConjuntoDeDatos(nv, instancias));
            System.out.println("--");
            String result = eva.evaluarConValidacionCruzada(instancias, 10);
            System.out.println("recall: \n" + eva.recall_string());
            System.out.println("fMeasure: \n" + eva.fMeasure_string());
            System.out.println(result);
            
        }

    }

}
