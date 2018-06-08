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
     * @param d
     * @param k
     * @param t
     * @return
     */
    public String clasificar(Instances d, int k, Instance t) {
        int c;
        clase = d.classAttribute();
        List<Instance> lista = Util.listaInstancias(d);
        // terner en cuenta la instancia a clasificar para la normalización
        lista.add(0, t);
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
            if (listaResultado.size() > k) {
                listaResultado.remove(k);
            }
        }
//        int posMayor = mayorFrecuencia(k, listaResultado);
        int posMayor = busquedaPor(k, listaResultado);
        return clase.value(posMayor);
    }

    /**
     *
     * @param k
     * @param listaResultado
     * @return
     */
    public int busquedaPor(int k, List listaResultado) {
        double[] contadorClases = new double[clase.numValues()];
        // inicializacion sumadores
        for (int i = 0; i < contadorClases.length; i++) {
            contadorClases[i] = Double.POSITIVE_INFINITY;
        }
        InstanciaDistancia idAux;
        // suma de cuadrado de clases
        for (int i = 0; i < k; i++) {
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
     * @param k
     * @param listaResultado
     * @return
     */
    public int mayorFrecuencia(int k, List listaResultado) {
        int[] contadorClases = new int[clase.numValues()];
        // conteo de clases
        for (int i = 0; i < k; i++) {
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
     * @param vec1
     * @param vec2
     * @return
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
     * @param lista
     * @return
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
