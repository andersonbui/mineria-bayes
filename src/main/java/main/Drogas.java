/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import algoritmosAgrupamiento.Evaluacion;
import algoritmosAgrupamiento.NaiveBayes;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

/**
 *
 * @author debian
 */
public class Drogas extends javax.swing.JFrame {

    /**
     * Creates new form Drogas
     */
    List<JComponent> listaComponentes = new ArrayList();
    NaiveBayes nv;
    Instances instancias;

    public Drogas() {
        initComponents();
        txtNumCarpetas.setEnabled(false);
//        btnEvaluar.setEnabled(false);
        rbtnConjuntoReferencia.setSelected(true);
        BuscarArchivo ba = new BuscarArchivo();

//        File file = ba.buscar();
        File file = new File("./drug1n.arff");
//        File file = new File("./clasificacion-drug.arff");
//        File file = new File("./weather.arff");
//        File file = new File("./titanic.arff");
        if (file != null) {
            try {
                System.out.println("nombre archivo:" + file.getAbsolutePath());
                ArffLoader arrfloader = new ArffLoader();
                arrfloader.setFile(file);
                instancias = arrfloader.getDataSet();
//            instancias.setClass(instancias.attribute("Sobrevivio"));
                instancias.setClass(instancias.attribute("Drug"));
//                instancias.setClass(instancias.attribute("play"));

                // ==== definir listas seleccionables ====
                Attribute atributoDeClase = instancias.classAttribute();
                for (int i = 0; i < instancias.numAttributes(); i++) {
                    Attribute attActual = instancias.attribute(i);
                    String titulo = attActual.name();

                    if (attActual.index() != atributoDeClase.index()) {
                        if (attActual.isNominal()) {
                            String[] vectorValAtt = new String[attActual.numValues()];
                            for (int j = 0; j < attActual.numValues(); j++) {
                                vectorValAtt[j] = attActual.value(j);
                            }
                            addComboText(vectorValAtt, titulo);
                        } else {
                            addCampoText(titulo);
                        }
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(Drogas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void addCampoText(String titulo) {
        jPanel6.add(new javax.swing.JLabel(titulo));
        JTextField jtf = new JTextField();
        listaComponentes.add(jtf);
        jtf.setMaximumSize(new java.awt.Dimension(3000, 24));
        jPanel6.add(jtf);

    }

    public void addComboText(String[] items, String titulo) {
        jPanel6.add(new javax.swing.JLabel(titulo));
        JComboBox cb = new javax.swing.JComboBox<>(items);
        listaComponentes.add(cb);
        cb.setMaximumSize(new java.awt.Dimension(3000, 24));
        jPanel6.add(cb);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btngPrueba = new javax.swing.ButtonGroup();
        jPanel5 = new javax.swing.JPanel();
        btnEvaluar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtPanelPrincipal = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel6 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        rbtnConjuntoReferencia = new javax.swing.JRadioButton();
        rbtnValidacionCruzada = new javax.swing.JRadioButton();
        txtNumCarpetas = new javax.swing.JTextField();
        btn_entrenar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnEvaluar.setText("Evaluar");
        btnEvaluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEvaluarActionPerformed(evt);
            }
        });
        jPanel5.add(btnEvaluar);

        txtPanelPrincipal.setColumns(20);
        txtPanelPrincipal.setRows(1);
        jScrollPane1.setViewportView(txtPanelPrincipal);

        jPanel6.setLayout(new javax.swing.BoxLayout(jPanel6, javax.swing.BoxLayout.PAGE_AXIS));
        jScrollPane2.setViewportView(jPanel6);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "prueba", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP));

        btngPrueba.add(rbtnConjuntoReferencia);
        rbtnConjuntoReferencia.setText("conjunto de entrenamiento");
        rbtnConjuntoReferencia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnConjuntoReferenciaActionPerformed(evt);
            }
        });

        btngPrueba.add(rbtnValidacionCruzada);
        rbtnValidacionCruzada.setText("validacion cruzada");
        rbtnValidacionCruzada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnValidacionCruzadaActionPerformed(evt);
            }
        });

        btn_entrenar.setText("Entrenar");
        btn_entrenar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_entrenarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(rbtnConjuntoReferencia)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(rbtnValidacionCruzada)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtNumCarpetas, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(109, 109, 109)
                .addComponent(btn_entrenar)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(rbtnConjuntoReferencia)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbtnValidacionCruzada)
                    .addComponent(txtNumCarpetas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_entrenar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 366, Short.MAX_VALUE))
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 391, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEvaluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEvaluarActionPerformed
        try {
            nv = new NaiveBayes();
            Attribute atributoDeClase = instancias.classAttribute();
            String resultado = "";
            double valorTemp;
//            int[][] matProb = eva.crearMatrizDeConfucion(nv, instancias);
            nv.crearModelo(instancias);
            // simep -siigo - aranda 

            Instance instancia = (Instance) instancias.instance(0).copy();
            for (int i = 0; i < instancias.numAttributes(); i++) {
                Attribute attActual = instancias.attribute(i);
                if (i != atributoDeClase.index()) {
                    if (attActual.isNominal()) {
                        JComboBox jcb = (JComboBox) listaComponentes.get(i);
                        instancia.setValue(i, (String) jcb.getSelectedItem());
                    } else {
                        JTextField jtext = (JTextField) listaComponentes.get(i);
                        valorTemp = Double.parseDouble(jtext.getText());
                        instancia.setValue(i, valorTemp);
                    }
                }
            }
            System.out.println("instancia: " + instancia);
            double[] probabilidades = nv.evaluarInstancia(instancia);
            resultado += "EVALUACION DE LA INSTANCIA";
            for (int i = 0; i < probabilidades.length; i++) {
                resultado += "\nclase: " + instancias.classAttribute().value(i) + ": " + String.format("%.2f", probabilidades[i]);
            }
            txtPanelPrincipal.setText(resultado);
        } catch (java.lang.NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error. Los valores de los campos deben ser numéricos.");
        }
    }//GEN-LAST:event_btnEvaluarActionPerformed

    private void rbtnValidacionCruzadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnValidacionCruzadaActionPerformed
        txtNumCarpetas.setEnabled(true);
    }//GEN-LAST:event_rbtnValidacionCruzadaActionPerformed

    private void rbtnConjuntoReferenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnConjuntoReferenciaActionPerformed
        txtNumCarpetas.setEnabled(false);
    }//GEN-LAST:event_rbtnConjuntoReferenciaActionPerformed

    private void btn_entrenarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_entrenarActionPerformed
        try {
            nv = new NaiveBayes();
            Attribute atributoDeClase = instancias.classAttribute();
            String resultado = "";
            Evaluacion eva = new Evaluacion(instancias);
//            int[][] matProb = eva.crearMatrizDeConfucion(nv, instancias);
            int numCarpetas;
            boolean pruebaValCruzada = rbtnValidacionCruzada.isSelected();
            String cadena;
            if (pruebaValCruzada) {
                numCarpetas = Integer.parseInt(txtNumCarpetas.getText());
                cadena = eva.evaluarConValidacionCruzada(instancias, numCarpetas);
                nv = eva.getNaivayes();
            } else {
                nv.crearModelo(instancias);
                // simep -siigo - aranda 
                Instance instancia = (Instance) instancias.instance(0).copy();
                double[] probabilidades = nv.evaluarInstancia(instancia);
                for (int i = 0; i < probabilidades.length; i++) {
                    System.out.println("val: " + instancias.classAttribute().value(i) + ": " + String.format("%.2f", probabilidades[i]));
                }
                cadena = eva.evaluarConConjuntoDeDatos(nv, instancias);
                System.out.println(nv);
            }

            resultado += "Matriz de confucion: \n" + cadena;
            resultado += "\n\nrecall: \n" + eva.recall_string();
            resultado += "fMeasure: \n" + eva.fMeasure_string();
            resultado += "precision: \n" + eva.precision_string();

            txtPanelPrincipal.setText(resultado);
            btnEvaluar.setEnabled(true);
        } catch (java.lang.NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error. Los valores de los campos deben ser numéricos.");
        }
    }//GEN-LAST:event_btn_entrenarActionPerformed

    /**
     * @param args the command line arguments
     */
    public  void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Drogas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Drogas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Drogas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Drogas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Drogas().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEvaluar;
    private javax.swing.JButton btn_entrenar;
    private javax.swing.ButtonGroup btngPrueba;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JRadioButton rbtnConjuntoReferencia;
    private javax.swing.JRadioButton rbtnValidacionCruzada;
    private javax.swing.JTextField txtNumCarpetas;
    private javax.swing.JTextArea txtPanelPrincipal;
    // End of variables declaration//GEN-END:variables
}
