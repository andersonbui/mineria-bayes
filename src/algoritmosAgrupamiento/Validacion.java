/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algoritmosAgrupamiento;

import java.util.ArrayList;
import java.util.List;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author debian
 */
public class Validacion {

    private Instances instancias;
//    Naivayes nv;

    /**
     *
     */
    public Validacion() {
//        nv = new Naivayes();
    }

//    public double[][] crearMatrizDeConfucionNormal(Instances instancias) {
//        nv.crearModelo(instancias);
//        return crearMatrizDeConfucion2(instancias);
//    }
    public double[][] crearMatrizDeConfucion(Naivayes modelo, Instances instancias) {
        double[][] resultado = modelo.evaluarInstancias(instancias);
        Attribute varClase = instancias.classAttribute();
        double[][] matConfusion = new double[varClase.numValues()][varClase.numValues()];

        double valMayor;
        int valReal;
        int posMayor = 0;

        for (int i = 0; i < instancias.numInstances(); i++) {
            Instance instancia = instancias.instance(i);
            valMayor = Double.NEGATIVE_INFINITY;
            for (int j = 0; j < resultado[i].length; j++) {
                if (valMayor < resultado[i][j]) {
                    posMayor = j;
                    valMayor = resultado[i][j];
                }
            }
            valReal = (int) instancia.value(varClase);
            matConfusion[valReal][posMayor]++;
        }
        return matConfusion;
    }

    public double[][] crearMatrizDeConfucionValidacionCruzada(Instances instancias, int numCarpetas) {
        Naivayes nv = new Naivayes();
        Instances[] vectInstancias;
        Cruzados cruzados = datosCruzados(instancias, numCarpetas);
        double[][] matConfucionTotal = null;
        for (int i = 0; i < cruzados.tamanio(); i++) {
            vectInstancias = cruzados.get(i);
            nv.crearModelo(vectInstancias[0]);
            double[][] result = crearMatrizDeConfucion(nv, vectInstancias[1]);
            if (matConfucionTotal == null) {
                matConfucionTotal = result;
            } else {
                matConfucionTotal = sumar(matConfucionTotal, result);
            }
        }

        return matConfucionTotal;
    }

    public Cruzados datosCruzados(Instances instancias, int numcarpetas) {
        Attribute varClase = instancias.classAttribute();
        int tamInstancias = instancias.numInstances() / numcarpetas;
        List<Instance>[] listaInstancias = new List[varClase.numValues()];
        for (int i = 0; i < listaInstancias.length; i++) {
            listaInstancias[i] = new ArrayList();
        }
        for (int i = 0; i < instancias.numInstances(); i++) {
            Instance instancia = instancias.instance(i);
            int valor = (int) instancia.value(varClase);
            listaInstancias[valor].add(instancia);
        }
        Instances InsTemporal;
        boolean seAgrego = true;
        Cruzados cruzados = new Cruzados();
        Instances[] vectCarpetaInstancias = new Instances[numcarpetas];
        //alojar espacio para las carpetas
        for (int i = 0; i < vectCarpetaInstancias.length; i++) {
            vectCarpetaInstancias[i] = new Instances(instancias, 0, 0);
            cruzados.add(vectCarpetaInstancias[i]);
        }

        int carpeta = 0;
//        instancias.sort(varClase);
        for (List<Instance> listaInstancia : listaInstancias) {
            for (int i = 0; i < listaInstancia.size(); i++) {

                Instance instanciaActual = listaInstancia.get(i);
                seAgrego = true;
                vectCarpetaInstancias[carpeta % vectCarpetaInstancias.length].add(instanciaActual);
                carpeta++;
            }
        }
        return cruzados;
    }

    private double[][] sumar(double[][] matConfucionTotal, double[][] result) {
        double[][] suma = new double[result.length][result[0].length];
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[0].length; j++) {
                suma[i][j] = matConfucionTotal[i][j] + result[i][j];
            }
        }
        return suma;
    }

    public class Cruzados {

        List<Instances> listaInstancias;

        public Cruzados() {
            listaInstancias = new ArrayList();
        }

        public void add(Instances instancias) {
            listaInstancias.add(instancias);
        }

        public int tamanio() {
            return listaInstancias.size();
        }

        public Instances[] get(int i) {
            Instances instancias1 = null;
            for (int j = 0; j < listaInstancias.size(); j++) {
                if (j != i) {// conjunto de instancias de entrenamiento
                    if (instancias1 == null) {
                        instancias1 = new Instances(listaInstancias.get(j));
                    } else {
                        Instances insTemp = listaInstancias.get(j);
                        for (int k = 0; k < insTemp.numInstances(); k++) {
                            instancias1.add(insTemp.instance(k));
                        }
                    }
                }
            }
            return new Instances[]{instancias1, listaInstancias.get(i)};
        }

    }
}
