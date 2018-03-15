/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algoritmosAgrupamiento;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author debian
 */
public class Evaluacion {

    int[][] matrizConfusion;
    int[] sumaReales;
    int[] sumaPredichos;
    private final Instances instancias;
    Naivayes nv;

    /**
     *
     * @param instancias
     */
    public Evaluacion(Instances instancias) {
        this.instancias = instancias;
    }

    /**
     *
     * @param modelo
     * @param instancias
     * @return
     */
    public int[][] crearMatrizDeConfucion(Naivayes modelo, Instances instancias) {
        double[][] resultado = modelo.evaluarInstancias(instancias);
        Attribute varClase = instancias.classAttribute();
        matrizConfusion = new int[varClase.numValues()][varClase.numValues()];
        sumaPredichos = new int[varClase.numValues()];
        sumaReales = new int[varClase.numValues()];

        double valMayor;
        int valReal;
        int posMayor = 0;

        for (int i = 0; i < instancias.numInstances(); i++) {
            Instance instancia = instancias.instance(i);
            valMayor = Double.NEGATIVE_INFINITY;
            //obtener mayor porcentaje
            for (int j = 0; j < resultado[i].length; j++) {
                if (valMayor < resultado[i][j]) {
                    posMayor = j;
                    valMayor = resultado[i][j];
                }
            }
            valReal = (int) instancia.value(varClase);
            matrizConfusion[valReal][posMayor]++;
            sumaReales[valReal]++;
            sumaPredichos[posMayor]++;
        }
        return matrizConfusion;
    }

    public int[][] crearMatrizDeConfucionValidacionCruzada(final Instances instancias, int numCarpetas) {
        nv = new Naivayes();
        Instances[] vectInstancias;
        Cruzados cruzados = datosCruzados(instancias, numCarpetas);
        int[][] matConfucionTotal = null;
        for (int i = 0; i < cruzados.tamanio(); i++) {
            vectInstancias = cruzados.get(i);
            nv.crearModelo(vectInstancias[0]);
            int[][] result = crearMatrizDeConfucion(nv, vectInstancias[1]);
            if (matConfucionTotal == null) {
                matConfucionTotal = result;
            } else {
                matConfucionTotal = sumar(matConfucionTotal, result);
            }
        }

        this.matrizConfusion = matConfucionTotal;
        return matConfucionTotal;
    }

    public String matrizConfucion_string() {
        Attribute varClase = instancias.classAttribute();
        String cadena = "";
        for (int i = 0; i < varClase.numValues(); i++) {
            cadena += "[" + i + "]" + Arrays.toString(matrizConfusion[i]) + "->" + varClase.value(i) + "\n";
        }
        return cadena;

    }

    /**
     * calcula los verdaderos positivos con respecto a una clase en particular
     * (indice clase)
     * <pre>
     * correctly classified positives
     * ------------------------------
     *       total positives
     * </pre>
     *
     * @param indice
     * @return
     */
    public double recall(int indice) {
        double correctos = 0;
        double sumaTotal = 0;
        for (int k = 0; k < matrizConfusion[0].length; k++) {
            if (k == indice) {
                correctos += matrizConfusion[indice][k];
            }
            sumaTotal += matrizConfusion[indice][k];

        }
        return correctos / sumaTotal;
    }

    public String fMeasure_string() {
        Attribute varClase = instancias.classAttribute();
        String cadena = "";
        double sumatoria = 0;
        double totalReales = 0;
        double precision;
        for (int i = 0; i < varClase.numValues(); i++) {
            precision = fMeasure(i);
            cadena += varClase.value(i) + ": " + precision + "\n";
            sumatoria += sumaReales[i] * precision;
            totalReales += sumaReales[i];
        }
        cadena += "Weighted Avg: " + (sumatoria / totalReales) + "\n";
        return cadena;
    }

    public String recall_string() {
        Attribute varClase = instancias.classAttribute();
        String cadena = "";
        double sumatoria = 0;
        double totalReales = 0;
        double precision;
        for (int i = 0; i < varClase.numValues(); i++) {
            precision = recall(i);
            cadena += varClase.value(i) + ": " + precision + "\n";
            sumatoria += sumaReales[i] * precision;
            totalReales += sumaReales[i];
        }
        cadena += "Weighted Avg: " + (sumatoria / totalReales) + "\n";
        return cadena;
    }

    public String precision_string() {
        Attribute varClase = instancias.classAttribute();
        String cadena = "";
        double sumatoria = 0;
        double totalReales = 0;
        double precision;
        for (int i = 0; i < varClase.numValues(); i++) {
            precision = precision(i);
            cadena += varClase.value(i) + ": " + precision + "\n";
            sumatoria += sumaReales[i] * precision;
            totalReales += sumaReales[i];
        }
        cadena += "Weighted Avg: " + (sumatoria / totalReales) + "\n";
        return cadena;
    }

    private String to_string() {
        Attribute varClase = instancias.classAttribute();
        String cadena = "";
        for (int i = 0; i < varClase.numValues(); i++) {
            cadena += varClase.value(i) + ": " + recall(i) + "\n";
        }
        return cadena;
    }

    /**
     *
     * @param indiceClase
     * @return
     */
    public double fMeasure(int indiceClase) {
        double recall = recall(indiceClase);
        double precision = precision(indiceClase);
        if (recall + precision == 0) {
            return 0;
        }
        return 2 * recall * precision / (recall + precision);
    }

    /**
     * Calcula la precision con respecto a una clase en particular (indice
     * clase)
     * <pre>
     * correctly classified positives
     * ------------------------------
     *  total predicted as positive
     * </pre>
     *
     * @param indice
     * @return
     */
    public double precision(int indice) {
        double correctos = 0;
        double sumaTotal = 0;
        for (int k = 0; k < matrizConfusion.length; k++) {
            if (k == indice) {
                correctos += matrizConfusion[k][indice];
            }
            sumaTotal += matrizConfusion[k][indice];

        }
        return correctos / sumaTotal;
    }

    /**
     * obtine el conjunto de <numcarpetas> carpetas para una validacion cruzada
     *
     * @param instancias
     * @param numcarpetas numero de carpetas dentro del conjunto resultado
     * @return
     */
    public Cruzados datosCruzados(final Instances instancias, int numcarpetas) {
        Attribute varClase = instancias.classAttribute();
        Cruzados cruzados = new Cruzados();
        Instances[] vectCarpetaInstancias = new Instances[numcarpetas];
        //alojar espacio para las carpetas
        for (int i = 0; i < vectCarpetaInstancias.length; i++) {
            vectCarpetaInstancias[i] = new Instances(instancias, 0);
            cruzados.add(vectCarpetaInstancias[i]);
        }

        instancias.sort(varClase);
        for (int i = 0; i < instancias.numInstances(); i++) {
            Instance instanciaActual = instancias.instance(i);
            vectCarpetaInstancias[i % vectCarpetaInstancias.length].add(instanciaActual);
        }

        return cruzados;
    }

    /**
     * retorna el resultado de sumar las dos matrices pasadas por parametro.
     *
     * @param matConfucionTotal
     * @param result
     * @return
     */
    private int[][] sumar(int[][] matConfucionTotal, int[][] result) {
        int[][] suma = new int[result.length][result[0].length];
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

        /**
         * obtiene un vector de tamaño 2 de instancias, de las cuales, la
         * segunda contiene todas las intancias de la carpeta en la posicion
         * indice, y la primera instancia, contiene las instancias de todas las
         * demas carpetas juntas.
         *
         * @param indice
         * @return
         */
        public Instances[] get(int indice) {
            Instances instancias1 = null;
            for (int j = 0; j < listaInstancias.size(); j++) {
                if (j != indice) {// conjunto de instancias de entrenamiento
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
            return new Instances[]{instancias1, listaInstancias.get(indice)};
        }
    }

//    private cl
    
    public Naivayes getNaivayes() {
        return nv;
    }

}
