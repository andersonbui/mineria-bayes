/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algoritmosAgrupamiento;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import utilidades.Util;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Datos siempre normalizados
 *
 * @author debian
 */
public class KVecinos {

    public class InstanciaDistancia implements Comparable<InstanciaDistancia> {

        Instance instancia;
        double similitud;

        public InstanciaDistancia(Instance instancia, double similitud) {
            this.instancia = instancia;
            this.similitud = similitud;
        }

        @Override
        public int compareTo(InstanciaDistancia o) {
            Double sim = similitud;
            return sim.compareTo(o.similitud);
        }
    }

    public Attribute clase;

    /**
     *
     * @param instancias instancias usadas para clasificar
     * @param numInstancias numero de instancias
     * @param instancia instancia a clasificar
     * @return clase a la cualpertenece la instancia
     */
    public String clasificar(Instances instancias, int numInstancias, Instance instancia) {
        int c;
        clase = instancias.classAttribute();
        List<Instance> lista = Util.listaInstancias(instancias);
        // terner en cuenta la instancia a clasificar para la normalización
        lista.add(0, instancia);
        // normalizar lista
        lista = normalizar(lista);
        // recuperar instancia a clasificar pero normalizada
        Instance t_normalizada = lista.remove(0);
        // lista de semejantes que almacenará las primeras k elementos mas semejantes
        List<InstanciaDistancia> listaResultado = new ArrayList(lista.size());
        // calculo de la distancia auclidiana para cada elemento
        for (Instance instance : lista) {
            double distancia = distanciaEuclidiana(instance, t_normalizada);
            // almacenar instancia + distancia
            InstanciaDistancia insDis = new InstanciaDistancia(instance, distancia);
            // posición del elemento actual dentro de la lista de semejantes
            int posicion = Util.indiceOrdenadamente(listaResultado, insDis, true);
            listaResultado.add(posicion, insDis);
            if (listaResultado.size() > numInstancias) {
                listaResultado.remove(numInstancias);
            }
        }
//        int posMayor = mayorFrecuencia(k, listaResultado);
        int posMayor = busquedaPor(numInstancias, listaResultado);
        return clase.value(posMayor);
    }

    /**
     *
     * @param numInstancias numero de instancias
     * @param listaResultado lista donde buscar
     * @return indice de la instancia encontrada
     */
    public int busquedaPor(int numInstancias, List listaResultado) {
        double[] contadorClases = new double[clase.numValues()];
        // inicializacion sumadores
        for (int i = 0; i < contadorClases.length; i++) {
            contadorClases[i] = Double.POSITIVE_INFINITY;
        }
        InstanciaDistancia idAux;
        // suma de cuadrado de clases
        for (int i = 0; i < numInstancias; i++) {
            idAux = (InstanciaDistancia) listaResultado.get(i);
            int pos = (int) idAux.instancia.value(clase);
            if (contadorClases[pos] == Double.POSITIVE_INFINITY) {
                contadorClases[pos] = idAux.similitud * idAux.similitud;
            } else {
                contadorClases[pos] += idAux.similitud * idAux.similitud;
            }
        }
        // calculo de inversos
        for (int i = 0; i < contadorClases.length; i++) {
            contadorClases[i] = 1 / contadorClases[i];
        }

        int posMayor = Integer.MIN_VALUE;
        double valorMayor = Integer.MIN_VALUE;
        // busqueda de la clase mayoritaria
        for (int i = 0; i < contadorClases.length; i++) {
            if (contadorClases[i] > valorMayor) {
                valorMayor = contadorClases[i];
                posMayor = i;
            }
        }
        return posMayor;
    }

    /**
     *
     * @param numInstancias numero de instancias
     * @param listaResultado lista donde buscar
     * @return indice de la instancia encontrada
     */
    public int mayorFrecuencia(int numInstancias, List listaResultado) {
        int[] contadorClases = new int[clase.numValues()];
        // conteo de clases
        for (int i = 0; i < numInstancias; i++) {
            int pos = (int) ((InstanciaDistancia) listaResultado.get(i)).instancia.value(clase);
            contadorClases[pos]++;
        }
        int posMayor = Integer.MIN_VALUE;
        int valorMayor = Integer.MIN_VALUE;
        // busqueda de la clase mayoritaria
        for (int i = 0; i < contadorClases.length; i++) {
            if (contadorClases[i] > valorMayor) {
                valorMayor = contadorClases[i];
                posMayor = i;
            }
        }
        return posMayor;
    }

    /**
     *
     * @param vec1 vector uno para el calculo
     * @param vec2 vector dos para el calculo
     * @return distancia euclidiana
     */
    public double distanciaEuclidiana(Instance vec1, Instance vec2) {
        double suma = 0;
        for (int i = 0; i < vec1.numAttributes(); i++) {
            Attribute att = vec1.attribute(i);
            if (clase.index() != i) {
                if (att.isNominal()) {
                    suma += vec1.value(i) == vec2.value(i) ? 0 : 1;
                } else {
                    suma += Math.pow(vec1.value(i) - vec2.value(i), 2);
                }
            }
        }
        return Math.sqrt(suma);
    }

    /**
     *
     * @param lista lista de instancias a normalizar
     * @return lista de instancias normalizadas
     */
    private List<Instance> normalizar(List<Instance> lista) {
        Instance instanciaMayores = (Instance) lista.get(0).copy();
        Instance instanciaMenores = (Instance) lista.get(0).copy();
        // busqueda de minimos y maximos
        for (int i = 1; i < lista.size(); i++) {
            Instance instancia = lista.get(i);
            for (int k = 0; k < instancia.numAttributes(); k++) {
                Attribute attr = instancia.attribute(k);
                if (attr.isNumeric()) {
                    if (instancia.value(k) > instanciaMayores.value(k)) {
                        instanciaMayores.setValue(k, instancia.value(k));
                    }
                    if (instancia.value(k) < instanciaMenores.value(k)) {
                        instanciaMenores.setValue(k, instancia.value(k));
                    }
                }
            }
        }
        List<Instance> listaNormalizados = new ArrayList();
        Double valorAux;
        // normalizo
        for (int i = 0; i < lista.size(); i++) {
            Instance instancia = (Instance) lista.get(i).copy();
            for (int k = 0; k < instancia.numAttributes(); k++) {
                Attribute attr = instancia.attribute(k);
                if (attr.isNumeric()) {
                    valorAux = (instancia.value(k) - instanciaMenores.value(k)) / (instanciaMayores.value(k) - instanciaMenores.value(k));
                    instancia.setValue(k, valorAux);
                }
            }
            listaNormalizados.add(i, instancia);
        }
        return listaNormalizados;
    }
}
