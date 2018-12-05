/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import algoritmosAgrupamiento.NaiveBayes;
import algoritmosAgrupamiento.Evaluacion;
import algoritmosAgrupamiento.KVecinos;
import algoritmosAgrupamiento.Modelo;
import algoritmosAgrupamiento.NaiveBayesSolitario;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

import javax.xml.stream.util.StreamReaderDelegate;

import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

/**
 *
 * @author debian
 */
public class AlgoritmosMineria {

    /**
     */
    public static Instances instancias;
    public static String archivoEntrenamiento;
    public static String varClase;

    public static void main(String[] args) throws IOException {
        BuscarArchivo ba = new BuscarArchivo();
        System.out.println("Parametros" + Arrays.toString(args));
        File file;
        if ("-e".equals(args[0])) { //entrenar
            archivoEntrenamiento = args[1];
            varClase = args[2];
            entrenar();
        }

        if (args.length > 10) {
            file = new File("src/" + args[0]);
            System.out.println("arg2: " + args[1]);
//            file = new File("src/drug1n.arff");
        } else {
//            file = ba.buscar();
//            file = new File("./clasificacion.arff");
            file = new File("src/areasdeconocimiento_INGENIERIA_1y0.arff");
        }

//        File file = new File("src/clasificacion-drug.arff");
//        File file = new File("src/weather.arff");
//        File file = new File("src/titanic.arff");
        if (file != null) {
            System.out.println("nombre archivo:" + file.getAbsolutePath());
            ArffLoader arrfloader = new ArffLoader();
            arrfloader.setFile(file);
            instancias = arrfloader.getDataSet();

//            instancias.setClass(instancias.attribute("Sobrevivio"));
//            instancias.setClass(instancias.attribute("Drug"));
            instancias.setClass(instancias.attribute("ingenieria"));
//            instancias.setClass(instancias.attribute("play"));
//            knn();
//            naivayes();

        }
    }

    static void knn() {

        KVecinos kvencinos = new KVecinos();
        Instance instancia = (Instance) instancias.instance(2).copy();

//        instancia.setValue(0, 49);
//        instancia.setValue(1, 1);
//        instancia.setValue(2, 1);
//        instancia.setValue(3, 0);
//        instancia.setValue(4, .789637);
//        instancia.setValue(5, .048518);
//            instancia.setValue(0, 60);
//            instancia.setValue(1, 1);
//            instancia.setValue(2, 2);
//            instancia.setValue(3, 0);
//            instancia.setValue(4, .777205);
//            instancia.setValue(5, .05123);
        System.out.println("prueba: " + instancia);
        System.out.println("knn: " + kvencinos.clasificar(instancias, 3, instancia));
        System.out.println("knn: " + kvencinos.clasificar(instancias, 1, instancia));
//        System.out.println("" + kvencinos.clasificar(instancias, 15, instancia));
    }

    static void entrenar() throws IOException {
        File file;
//        file = new File("src/"+archivoEntrenamiento);
//        file = new File("src/areasdeconocimiento-INGENIERIA.arff");
        file = new File("src/areasdeconocimiento-INGENIERIA.arff");
//        file = new File("src/drug1n.arff");
        ArffLoader arrfloader = new ArffLoader();
        arrfloader.setFile(file);
        instancias = arrfloader.getDataSet();
//        instancias.setClass(instancias.attribute(varClase));
        instancias.setClass(instancias.attribute("ingenieria"));
        NaiveBayesSolitario nv = new NaiveBayesSolitario();
        Modelo modelo  = nv.crearModelo(instancias);
        almacenamiento.Almacenamiento.guardarObjeto(modelo, "modelo");
    }

    static void naivayes() {

        NaiveBayes nv = new NaiveBayes();
        nv.crearModelo(instancias);

//            nv.evaluarInstancias(instancias);
        Instance instancia = (Instance) instancias.instance(4).copy();

        instancia.setValue(0, 49);
        instancia.setValue(1, 1);
        instancia.setValue(2, 1);
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
//        System.out.println(nv);
        System.out.println("EVALUACION DE INSTANCIA");
        for (int i = 0; i < probabilidades.length; i++) {
            System.out.println(instancias.classAttribute().value(i) + ": " + String.format("%.2f", probabilidades[i]));
        }
//        Evaluacion eva = new Evaluacion(instancias);
//        int[][] matProb = eva.crearMatrizDeConfucion(nv, instancias);
//        System.out.println("\nrecall: \n" + eva.recall_string());
//        System.out.println("fMeasure: \n" + eva.fMeasure_string());
//        System.out.println("precision: \n" + eva.precision_string());
//        System.out.println(eva.evaluarConConjuntoDeDatos(nv, instancias));
//        System.out.println("--");
//        String result = eva.evaluarConValidacionCruzada(instancias, 10);
//        System.out.println("recall: \n" + eva.recall_string());
//        System.out.println("fMeasure: \n" + eva.fMeasure_string());
//        System.out.println(result);

    }

}
