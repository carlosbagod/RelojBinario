/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.relojbinario;

/**
 *
 * @author carlosbarrios
 */
//aqui estan las conversiones de base y las operaciones con binarios
//no se usa ninguna funcion de java que cambie de base, solo / % + y ciclos
public class Conversiones {
    //todos los binarios de las operaciones tienen 32 bits
    static final int BITS = 32;

    //DECIMAL A BASE 2, 8 O 16
    //se divide entre la base y cada residuo se pega a la izquierda (como en estructuraslogicas)
    public static String convertirBase(int numero, int base){
        String digitos = "0123456789ABCDEF"; //del 10 al 15 se usan las letras
        if (numero == 0) {
            return "0";
        }
        String resultado = "";
        int temporal = numero;
        while (temporal > 0) {
            int residuo = temporal % base;
            resultado = digitos.charAt(residuo) + resultado;
            temporal = temporal / base;
        }
        return resultado;
    }

    //BINARIO A DECIMAL
    //se recorre de derecha a izquierda y la potencia se duplica (como en BinarioaDecimal)
    public static int binarioADecimal(String binario){
        int decimal = 0;
        int potencia = 1;
        for (int i = binario.length() - 1; i >= 0; i--) {
            if (binario.charAt(i) == '1') {
                decimal = decimal + potencia;
            }
            if (i > 0) {
                potencia = potencia + potencia;
            }
        }
        return decimal;
    }

    //pone ceros a la izquierda hasta tener 32 bits, el valor no cambia
    public static String completarBits(String binario){
        while (binario.length() < BITS) {
            binario = "0" + binario;
        }
        return binario;
    }

    //SUMA BINARIA bit por bit de derecha a izquierda con acarreo
    //el acarreo que sobra al final se descarta (por eso funciona el complemento a 2)
    public static String sumarBinarios(String a, String b){
        String resultado = "";
        int acarreo = 0;
        for (int i = BITS - 1; i >= 0; i--) {
            int suma = acarreo;
            if (a.charAt(i) == '1') {
                suma++;
            }
            if (b.charAt(i) == '1') {
                suma++;
            }
            resultado = (suma % 2) + resultado;
            acarreo = suma / 2;
        }
        return resultado;
    }

    //COMPLEMENTO A 2: se invierten los bits y se le suma 1
    public static String complementoADos(String binario){
        String invertido = "";
        for (int i = 0; i < binario.length(); i++) {
            if (binario.charAt(i) == '0') {
                invertido = invertido + "1";
            } else {
                invertido = invertido + "0";
            }
        }
        String uno = completarBits("1");
        return sumarBinarios(invertido, uno);
    }
}
