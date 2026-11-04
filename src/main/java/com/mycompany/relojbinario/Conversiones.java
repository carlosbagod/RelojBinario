package com.mycompany.relojbinario;

public class Conversiones {
    static final int BITS = 32;

    public static String convertirBase(int numero, int base){
        String digitos = "0123456789ABCDEF";
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

    public static String completarBits(String binario){
        while (binario.length() < BITS) {
            binario = "0" + binario;
        }
        return binario;
    }

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
