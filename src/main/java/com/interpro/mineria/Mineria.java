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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
     * @param archivo_modelo nombre del archivo que contiene el modelo ya
     * entrenado
     * @param archivoInstancias nombre del archivo que contiene la instancia a
     * predecir
     * @return repuesta de prediccion
     * @throws IOException
     */
    public Resultado predecir(String archivo_modelo, String archivoInstancias) throws IOException {
        ArffLoader arrfloader = new ArffLoader();
        File file;
        file = new File(archivoInstancias);
        arrfloader.setFile(file);
        Modelo modelo = (Modelo) almacenamiento.Almacenamiento.obtenerObjeto(archivo_modelo);
        Instances instancias = arrfloader.getDataSet();
        Instance instanciaActual = instancias.firstInstance();
        int indiceClase = modelo.getVarClase().index();

        NaiveBayesSolitario nv = new NaiveBayesSolitario();
        double[] result = nv.evaluarInstancia(instanciaActual, modelo);
//        String[] respuestas = new String[result.length];
        int mayor = 0;
        List<String[]> listaResultados = new ArrayList();
        for (int i = 0; i < result.length; i++) {
            if (result[mayor] < result[i]) {
                mayor = i;
            }
            listaResultados.add(new String[]{instancias.attribute(indiceClase).value(mayor), String.valueOf(result[i])});
        }
        System.out.println("instancia: " + instanciaActual.toString());
        for (int i = 0; i < listaResultados.size(); i++) {
            System.out.println(listaResultados.get(i)[0] + " : " + listaResultados.get(i)[1]);
        }

        System.out.println("mayor: " + instancias.attribute(indiceClase).value(mayor));
        return new Resultado(listaResultados, mayor);
    }
}
