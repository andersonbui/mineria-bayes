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
public class Consola {

    /**
     */
    public static Instances instancias;
    public static String archivoEntrenamiento;
    public static String atributoClase;

    public static void main(String[] args) throws IOException {

        if (args.length > 0) {
            String modelo = "modelo";
            if ("-e".equals(args[0])) { //entrenar
                archivoEntrenamiento = args[1];
                atributoClase = args[2];
                entrenar(modelo, archivoEntrenamiento, atributoClase);
            } else if ("-p".equals(args[0])) {
                String archivoInstancia = args[1];
                modelo = args[2];
                predecir("modelo", archivoInstancia);
            } else{
                System.out.println("Ayuda");
            }
        } else {
            System.out.println("Ayuda");
        }

    }

    /**
     *
     * @param nombreModelo ubicacion completa del nombre del archivo que
     * almacenara el modelo generado
     * @param archivoInstancias de tipo *.arff
     * @param atributoClase nombre del atributo de clase
     * @throws IOException
     */
    static void entrenar(String nombreModelo, String archivoInstancias, String atributoClase) throws IOException {
        File file;
        file = new File(archivoInstancias);
        ArffLoader arrfloader = new ArffLoader();
        arrfloader.setFile(file);
        instancias = arrfloader.getDataSet();

        instancias.setClass(instancias.attribute(atributoClase));
        NaiveBayesSolitario nv = new NaiveBayesSolitario();
        Modelo modelo = nv.crearModelo(instancias);
        almacenamiento.Almacenamiento.guardarObjeto(modelo, nombreModelo);
    }

    /**
     *
     * @param archivoModelo nombre del archivo que contiene el modelo ya
     * entrenado
     * @param archivoInstancia nombre del archivo que contiene la instancia a
     * predecir
     * @throws IOException
     */
    static void predecir(String archivoModelo, String archivoInstancia) throws IOException {
        ArffLoader arrfloader = new ArffLoader();
        File file;
        file = new File(archivoInstancia);
        arrfloader.setFile(file);
        Modelo modelo = (Modelo) almacenamiento.Almacenamiento.obtenerObjeto(archivoModelo);
        Instances instancias = arrfloader.getDataSet();
        Instance instanciaActual = instancias.firstInstance();
        int indiceClase = modelo.getVarClase().index();

        NaiveBayesSolitario nv = new NaiveBayesSolitario();
        double[] result = nv.evaluarInstancia(instanciaActual, modelo);
//        String[] respuestas = new String[result.length];
        int mayor = 0;
        for (int i = 1; i < result.length; i++) {
            if (result[mayor] < result[i]) {
                mayor = i;
            }
        }
        System.out.println("instancia: " + instanciaActual.toString());
        System.out.println("resultado: " + Arrays.toString(result));
        System.out.println("resultado: " + instancias.attribute(indiceClase).value(mayor));
    }

}
