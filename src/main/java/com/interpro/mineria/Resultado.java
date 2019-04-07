/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interpro.mineria;

import java.util.List;

/**
 *
 * @author debian
 */
public class Resultado {

    private List<String[]> resultados;
    private int indiceMayor;

    public Resultado(List<String[]> resultados, int indiceMayor) {
        this.resultados = resultados;
        this.indiceMayor = indiceMayor;
    }

    public List<String[]> getResultados() {
        return resultados;
    }

    public void setResultados(List<String[]> resultados) {
        this.resultados = resultados;
    }

    public int getIndiceMayor() {
        return indiceMayor;
    }

    public void setIndiceMayor(int indiceMayor) {
        this.indiceMayor = indiceMayor;
    }
    
}
