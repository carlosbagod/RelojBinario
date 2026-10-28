/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.relojbinario;

/**
 *
 * @author carlosbarrios
 */
//reglas del calendario para pasar de dia, mes y año despues de sumar o restar
public class Calendario {

    //bisiesto: divisible entre 4 pero no entre 100, o divisible entre 400
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

    //recibe la fecha inicial y los segundos del dia ya sumados o restados
    //si pasan de 86400 (un dia) se avanzan dias, si quedan negativos se retroceden
    public static FechaHora calcularFecha(FechaHora inicial, int segundosTotales){
        int dias = segundosTotales / 86400;
        int resto = segundosTotales % 86400;
        if (resto < 0) {
            //en java el residuo de un negativo sale negativo, se pide prestado un dia
            resto = resto + 86400;
            dias--;
        }
        int hora = resto / 3600;
        int minuto = (resto % 3600) / 60;
        int segundo = resto % 60;

        int dia = inicial.getDia();
        int mes = inicial.getMes();
        int anio = inicial.getAnio();
        //avanzar dia por dia
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
        //retroceder dia por dia
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
