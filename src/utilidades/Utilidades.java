/*
 * Copyright (C) 2017 debian
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package utilidades;

import java.util.List;

/**
 *
 * @author debian
 */
public class Utilidades {

    static public String eliminarEspaciosRepetidos(String texto) {
        java.util.StringTokenizer tokens = new java.util.StringTokenizer(texto);
        texto = "";
        while (tokens.hasMoreTokens()) {
            texto += " " + tokens.nextToken();
        }
        texto = texto.trim();
        return texto;
    }

    public static int indiceOrdenadamente(List<Object> lista, Comparable punto, boolean ascendente) {
        if (lista.isEmpty()) {
            return 0;
        }
        int pos;
        int sup = lista.size() - 1;
        int inf = 0;
        Object puntoPos;
        int comparacion;
        while (true) {
            pos = inf + (sup - inf) / 2;
            puntoPos = lista.get(pos);
            comparacion = punto.compareTo(puntoPos) * (ascendente ? 1 : -1);
            if (comparacion >= 0) {
                inf = pos + (ascendente ? 1 : 1);
            } else if (comparacion < 0) {

                sup = pos + (ascendente ? -1 : -1);
            }
            if (sup < inf || comparacion == 0) {
                return inf;
            }
        }
    }

    public static double[] invertirArray(double[] array) {
        double[] ar_i = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            ar_i[array.length - i - 1] = array[i];
        }
        return ar_i;
    }

    public static int comparaToBinario(double[] a, double[] b) {
        Double v_a;
        Double v_b;
        for (int i = 0;; i++) {
            v_a = a.length > i ? a[i] : 0;
            v_b = b.length > i ? b[i] : 0;
            if (v_b == 0 && v_a == 0) {
                if (!(a.length > i && b.length > i)) {
                    return 1;
                } else {
                    continue;
                }
            }
            if (v_b == 0) {
                return 1;
            }
            if (v_a == 0) {
                return -1;
            }

        }
    }
    
    public static double probabilidad(double valor, double media, double desvEstandar) { //funcion de densidad de probabilidad normal
        double exponente = Math.exp(-Math.pow(valor - media, 2) / (2 * Math.pow(desvEstandar, 2)));
        return exponente / (Math.sqrt(2 * Math.PI) * desvEstandar);
    }

}
