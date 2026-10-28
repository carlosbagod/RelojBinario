/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.relojbinario;

import java.time.LocalDateTime;

/**
 *
 * @author carlosbarrios
 */
public class principalReloj extends javax.swing.JFrame {
    private boolean formato12 = false; //empieza en formato de 24 horas
    //se guarda la ultima operacion para volver a mostrarla si cambia el formato
    private FechaHora fechaInicial = null;
    private FechaHora fechaResultado = null;
    private String procedimiento = "";

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(principalReloj.class.getName());

    /**
     * Creates new form principalReloj
     */
    public principalReloj() {
        initComponents();
        setTitle("Estructuras Lógicas II - Reloj y aritmética binaria");
        this.jLabel1.setText("Fecha y hora actual del equipo (Día/Mes/Año   Hora:Minuto:Segundo):");
        this.jButton1.setText("Formato 24 horas");
        this.jButton2.setText("Formato 12 horas");
        this.jLabel2.setText("Horas:");
        this.jLabel3.setText("Minutos:");
        this.jLabel4.setText("Segundos:");
        this.jTextField1.setText("0");
        this.jTextField2.setText("0");
        this.jTextField3.setText("0");
        this.jButton3.setText("Sumar");
        this.jButton4.setText("Restar");
        this.jLabel5.setText("Resultado de la operación:");
        this.jTextArea1.setEditable(false);
        this.jTextArea2.setEditable(false);
        this.jTextArea2.setText("Escriba cuánto tiempo quiere sumar o restar y presione Sumar o Restar.");
        mostrarReloj();

        //el timer actualiza el reloj solo (igual que en Cuenta1A10)
        //cada medio segundo para que no se salte ningun segundo
        javax.swing.Timer timer = new javax.swing.Timer(500, null);
        timer.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                mostrarReloj();
            }
        });
        timer.start();
        pack(); //ajusta la ventana a los textos nuevos
        setLocationRelativeTo(null);
    }

    //lee la fecha y hora del equipo
    private FechaHora leerFechaActual(){
        LocalDateTime ahora = LocalDateTime.now();
        return new FechaHora(ahora.getDayOfMonth(), ahora.getMonthValue(), ahora.getYear(),
                ahora.getHour(), ahora.getMinute(), ahora.getSecond());
    }

    //una linea por cada base: 10, 2, 8 y 16
    private String textoCuatroBases(FechaHora fecha){
        StringBuilder texto = new StringBuilder();
        texto.append("Base 10:  " + fecha.textoEnBase(10, formato12) + "\n");
        texto.append("Base 2:    " + fecha.textoEnBase(2, formato12) + "\n");
        texto.append("Base 8:    " + fecha.textoEnBase(8, formato12) + "\n");
        texto.append("Base 16:  " + fecha.textoEnBase(16, formato12) + "\n");
        return texto.toString();
    }

    private void mostrarReloj(){
        jTextArea1.setText(textoCuatroBases(leerFechaActual()));
    }

    //SUMAR O RESTAR TIEMPO
    //A = segundos que van del dia, B = segundos que escribio el usuario
    //sumar: A + B    restar: A + complemento a 2 de B
    private void operar(boolean restar){
        int horas = 0;
        int minutos = 0;
        int segundos = 0;
        try {
            horas = Integer.parseInt(this.jTextField1.getText().trim());
            minutos = Integer.parseInt(this.jTextField2.getText().trim());
            segundos = Integer.parseInt(this.jTextField3.getText().trim());
        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Escriba solo números enteros en horas, minutos y segundos (puede poner 0).");
            return;
        }
        //validacion
        if (horas < 0 || minutos < 0 || segundos < 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "No se permiten números negativos, use el botón Restar.");
            return;
        }
        //con este limite el resultado siempre cabe en los 32 bits
        if (horas > 100000 || minutos > 100000 || segundos > 100000) {
            javax.swing.JOptionPane.showMessageDialog(this, "Cada cantidad puede ser como máximo 100000.");
            return;
        }

        fechaInicial = leerFechaActual(); //se toma la hora en el momento de presionar el boton
        int a = fechaInicial.segundosDelDia();
        int b = horas * 3600 + minutos * 60 + segundos;

        String binarioA = Conversiones.completarBits(Conversiones.convertirBase(a, 2));
        String binarioB = Conversiones.completarBits(Conversiones.convertirBase(b, 2));
        String operando = binarioB;
        if (restar) {
            operando = Conversiones.complementoADos(binarioB);
        }
        String binarioResultado = Conversiones.sumarBinarios(binarioA, operando);

        //si el primer bit es 1 el resultado es negativo (la resta se paso al dia anterior)
        //su valor se saca con el complemento a 2 del resultado
        int totalBinario = 0;
        if (binarioResultado.charAt(0) == '1') {
            totalBinario = -Conversiones.binarioADecimal(Conversiones.complementoADos(binarioResultado));
        } else {
            totalBinario = Conversiones.binarioADecimal(binarioResultado);
        }
        fechaResultado = Calendario.calcularFecha(fechaInicial, totalBinario);

        //comprobacion: la misma operacion pero en decimal normal
        int totalDecimal = 0;
        if (restar) {
            totalDecimal = a - b;
        } else {
            totalDecimal = a + b;
        }
        FechaHora fechaDecimal = Calendario.calcularFecha(fechaInicial, totalDecimal);

        StringBuilder texto = new StringBuilder();
        texto.append("PROCEDIMIENTO EN BINARIO (" + Conversiones.BITS + " bits)\n");
        texto.append("A (segundos del día) = " + a + "\n");
        texto.append("A = " + binarioA + "\n");
        texto.append("B (tiempo escrito) = " + horas + "h " + minutos + "m " + segundos + "s = " + b + " segundos\n");
        texto.append("B = " + binarioB + "\n");
        if (restar) {
            texto.append("A2 de B = " + operando + "  (se invierten los bits y se suma 1)\n");
            texto.append("A - B = A + A2(B), el acarreo que sobra se descarta\n");
        } else {
            texto.append("A + B, bit por bit con acarreo\n");
        }
        texto.append("Resultado = " + binarioResultado + "\n");
        if (binarioResultado.charAt(0) == '1') {
            texto.append("El primer bit es 1, entonces es negativo: " + totalBinario + " segundos (se regresa al día anterior)\n");
        } else {
            texto.append("Resultado en decimal = " + totalBinario + " segundos\n");
        }
        texto.append("\nCOMPROBACIÓN EN DECIMAL\n");
        if (restar) {
            texto.append(a + " - " + b + " = " + totalDecimal + "\n");
        } else {
            texto.append(a + " + " + b + " = " + totalDecimal + "\n");
        }
        texto.append("Fecha con el cálculo decimal: " + fechaDecimal.textoEnBase(10, false) + "\n");
        if (totalBinario == totalDecimal && fechaResultado.textoEnBase(10, false).equals(fechaDecimal.textoEnBase(10, false))) {
            texto.append("El resultado binario y el decimal COINCIDEN.");
        } else {
            texto.append("El resultado binario y el decimal NO coinciden.");
        }
        procedimiento = texto.toString();
        mostrarResultado();
    }

    private void mostrarResultado(){
        if (fechaInicial == null) {
            return; //todavia no se ha hecho ninguna operacion
        }
        StringBuilder texto = new StringBuilder();
        texto.append("FECHA Y HORA AL PRESIONAR EL BOTÓN\n");
        texto.append(textoCuatroBases(fechaInicial));
        texto.append("\nNUEVA FECHA Y HORA\n");
        texto.append(textoCuatroBases(fechaResultado));
        texto.append("\n" + procedimiento);
        jTextArea2.setText(texto.toString());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("jLabel1");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jButton1.setText("jButton1");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        jButton2.setText("jButton2");
        jButton2.addActionListener(this::jButton2ActionPerformed);

        jLabel2.setText("jLabel2");

        jTextField1.setText("jTextField1");

        jLabel3.setText("jLabel3");

        jTextField2.setText("jTextField2");

        jLabel4.setText("jLabel4");

        jTextField3.setText("jTextField3");

        jButton3.setText("jButton3");
        jButton3.addActionListener(this::jButton3ActionPerformed);

        jButton4.setText("jButton4");
        jButton4.addActionListener(this::jButton4ActionPerformed);

        jLabel5.setText("jLabel5");

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jScrollPane2.setViewportView(jTextArea2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 640, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4))
                    .addComponent(jLabel5)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 640, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jButton4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        formato12 = false;
        mostrarReloj();
        mostrarResultado();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        formato12 = true;
        mostrarReloj();
        mostrarResultado();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        operar(false);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        operar(true);
    }//GEN-LAST:event_jButton4ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
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
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new principalReloj().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables
}
