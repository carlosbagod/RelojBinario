package com.mycompany.relojbinario;

public class Calendario {

    public static boolean esBisiesto(int anio){
        return (anio % 4 == 0 && anio % 100 != 0) || anio % 400 == 0;
    }

    public static int diasDelMes(int mes, int anio){
        int dias[] = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if (mes == 2 && esBisiesto(anio)) {
            return 29;
        }
        return dias[mes - 1];
    }

    public static FechaHora calcularFecha(FechaHora inicial, int segundosTotales){
        int dias = segundosTotales / 86400;
        int resto = segundosTotales % 86400;
        if (resto < 0) {
            resto = resto + 86400;
            dias--;
        }
        int hora = resto / 3600;
        int minuto = (resto % 3600) / 60;
        int segundo = resto % 60;

        int dia = inicial.getDia();
        int mes = inicial.getMes();
        int anio = inicial.getAnio();
        while (dias > 0) {
            dia++;
            if (dia > diasDelMes(mes, anio)) {
                dia = 1;
                mes++;
                if (mes > 12) {
                    mes = 1;
                    anio++;
                }
            }
            dias--;
        }
        while (dias < 0) {
            dia--;
            if (dia < 1) {
                mes--;
                if (mes < 1) {
                    mes = 12;
                    anio--;
                }
                dia = diasDelMes(mes, anio);
            }
            dias++;
        }
        return new FechaHora(dia, mes, anio, hora, minuto, segundo);
    }
}
