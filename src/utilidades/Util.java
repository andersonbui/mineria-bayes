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

import java.util.ArrayList;
import java.util.List;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author debian
 */
public class Util {

    public static final int NUM_DECIMALES = 4;
    public static String formato = "%." + NUM_DECIMALES + "f";

    public static double probabilidad(double valor, double media, double desvEstandar) { //funcion de densidad de probabilidad normal
        double exponente = Math.exp(-Math.pow(valor - media, 2) / (2 * Math.pow(desvEstandar, 2)));
        return exponente / (Math.sqrt(2 * Math.PI) * desvEstandar);
    }

    public static String formatearDouble(double d) {
        return String.format(formato, d);
    }

    public static String imprimirVectorDouble(double[] probVarClase) {
        StringBuilder cadena = new StringBuilder();
        for (double d : probVarClase) {
            cadena.append(String.format(formato + " ", d));
        }
        return cadena.toString();
    }
    
    /**
     * retorna lista deatributos
     *
     * @return
     */
    public static List<Attribute> listaAtributos(Instances instancias) {
        List listAtributos = new ArrayList();
        for (int i = 0; i < instancias.numAttributes(); i++) {
            listAtributos.add(instancias.attribute(i));
        }
        return listAtributos;
    }

    public static List<Instance> listaInstancias(Instances instancias) {
        List listAtributos = new ArrayList();
        for (int i = 0; i < instancias.numInstances(); i++) {
            listAtributos.add(instancias.instance(i));
        }
        return listAtributos;
    }

}
