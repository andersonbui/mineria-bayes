/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algoritmosAgrupamiento;

import java.io.Serializable;
import weka.core.Attribute;

/**
 *
 * @author debian
 */
public class Modelo implements Serializable {
    private double[] probVarClase;
    private Attribute varClase;
    private double[] vectorPrecision;
    private MatProbabilidad[] vectorMatProbCondicionales;
    private Attribute[] atributos;

    public Modelo(double[] probVarClase, Attribute varClase, double[] vectorPrecision, MatProbabilidad[] vectorMatProbCondicionales, Attribute[] atributos) {
        this.probVarClase = probVarClase;
        this.varClase = varClase;
        this.vectorPrecision = vectorPrecision;
        this.vectorMatProbCondicionales = vectorMatProbCondicionales;
        this.atributos = atributos;
    }

    public double[] getProbVarClase() {
        return probVarClase;
    }

    public void setProbVarClase(double[] probVarClase) {
        this.probVarClase = probVarClase;
    }

    public Attribute getVarClase() {
        return varClase;
    }

    public void setVarClase(Attribute varClase) {
        this.varClase = varClase;
    }

    public double[] getVectorPrecision() {
        return vectorPrecision;
    }

    public void setVectorPrecision(double[] vectorPrecision) {
        this.vectorPrecision = vectorPrecision;
    }

    public MatProbabilidad[] getVectorMatProbCondicionales() {
        return vectorMatProbCondicionales;
    }

    public void setVectorMatProbCondicionales(MatProbabilidad[] vectorMatProbCondicionales) {
        this.vectorMatProbCondicionales = vectorMatProbCondicionales;
    }

    public Attribute[] getAtributos() {
        return atributos;
    }

    public void setAtributos(Attribute[] atributos) {
        this.atributos = atributos;
    }
    
    
}
