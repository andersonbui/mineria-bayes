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
import java.util.logging.Level;
import java.util.logging.Logger;
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
        almacenamiento.Almacenamiento.guardarObjeto(modelo, nombreModelo, true);
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
    public Resultado predecir(String archivo_modelo, String archivoInstancias) {
        ArffLoader arrfloader = new ArffLoader();
        File file = new File(archivoInstancias);
        System.out.println("existe modelo archivo: " + (new File(archivo_modelo)).exists());
        Modelo modelo = (Modelo) almacenamiento.Almacenamiento.obtenerObjeto(archivo_modelo);
        System.out.println("archivo_modelo: " + archivo_modelo);
        System.out.println("modelo: " + modelo);
        if (file.exists() && modelo != null) {
            try {
                arrfloader.setFile(file);
                Instances instancias = arrfloader.getDataSet();
                Instance instanciaActual = instancias.firstInstance();
                System.out.println("instanciaActual: " + instanciaActual);
                int indiceClase = modelo.getVarClase().index();

                NaiveBayesSolitario nv = new NaiveBayesSolitario();
                System.out.println("instanciaActual: " + instanciaActual);
                double[] result = nv.evaluarInstancia(instanciaActual, modelo);
                System.out.println("result0: " + result[0]);
                System.out.println("result1: " + result[1]);
//        String[] respuestas = new String[result.length];
                int mayor = 0;
                List<String[]> listaResultados = new ArrayList();
                for (int i = 0; i < result.length; i++) {
                    String cad = "" + result[i];
                    System.out.println("result[i]: " + cad);
                    if (result[mayor] < result[i]) {
                        mayor = i;
                    }
                    listaResultados.add(new String[]{instancias.attribute(indiceClase).value(i), cad});
                }
                System.out.println("instancia: " + instanciaActual.toString());
                for (int i = 0; i < listaResultados.size(); i++) {
                    System.out.println(listaResultados.get(i)[0] + " : " + listaResultados.get(i)[1]);
                }

                System.out.println("mayor: " + instancias.attribute(indiceClase).value(mayor));
                Resultado resultado = new Resultado(listaResultados, mayor);
                System.out.println("resultado: ->" + resultado);
                return resultado;
            } catch (IOException ex) {
                Logger.getLogger(Mineria.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
}
