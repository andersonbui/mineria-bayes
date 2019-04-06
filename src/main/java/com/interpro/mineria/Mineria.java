/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interpro.mineria;

import algoritmosAgrupamiento.Modelo;
import algoritmosAgrupamiento.NaiveBayesSolitario;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

/**
 *
 * @author andersonbui
 */
public class Mineria {

    /**
     * 
     */
    public Mineria() {
    }

    /**
     *
     * @param nombreModelo ubicacion completa del nombre del archivo que
     * almacenara el modelo generado
     * @param archivoInstancias de tipo *.arff
     * @param atributoClase nombre del atributo de clase
     * @throws IOException
     */
    public void entrenar(String nombreModelo, String archivoInstancias, String atributoClase) throws IOException {
        File file;
        file = new File(archivoInstancias);
        ArffLoader arrfloader = new ArffLoader();
        arrfloader.setFile(file);
        Instances instancias;
        instancias = arrfloader.getDataSet();

        instancias.setClass(instancias.attribute(atributoClase));
        NaiveBayesSolitario nv = new NaiveBayesSolitario();
        Modelo modelo = nv.crearModelo(instancias);
        almacenamiento.Almacenamiento.guardarObjeto(modelo, nombreModelo);
    }

    /**
     *
     * @param nombreInstancia nombre del archivo que contiene el modelo ya
     * entrenado
     * @param archivoInstancias nombre del archivo que contiene la instancia a
     * predecir
     * @throws IOException
     */
    public void predecir(String nombreInstancia, String archivoInstancias) throws IOException {
        ArffLoader arrfloader = new ArffLoader();
        File file;
        file = new File(archivoInstancias);
        arrfloader.setFile(file);
        Modelo modelo = (Modelo) almacenamiento.Almacenamiento.obtenerObjeto(nombreInstancia);
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
