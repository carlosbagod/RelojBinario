package com.mycompany.relojbinario;

public class FechaHora {
    private int dia;
    private int mes;
    private int anio;
    private int hora;
    private int minuto;
    private int segundo;

    public FechaHora(int dia, int mes, int anio, int hora, int minuto, int segundo){
        this.dia = dia;
        this.mes = mes;
        this.anio = anio;
        this.hora = hora;
        this.minuto = minuto;
        this.segundo = segundo;
    }

    public int getDia(){
        return dia;
    }
    public int getMes(){
        return mes;
    }
    public int getAnio(){
        return anio;
    }
    public int getHora(){
        return hora;
    }
    public int getMinuto(){
        return minuto;
    }
    public int getSegundo(){
        return segundo;
    }

    public int segundosDelDia(){
        return hora * 3600 + minuto * 60 + segundo;
    }

    public String textoEnBase(int base, boolean formato12){
        int horaMostrar = hora;
        String periodo = "";
        if (formato12) {
            if (hora < 12) {
                periodo = " AM";
            } else {
                periodo = " PM";
            }
            horaMostrar = hora % 12;
            if (horaMostrar == 0) {
                horaMostrar = 12;
            }
        }
        return Conversiones.convertirBase(dia, base) + "/" + Conversiones.convertirBase(mes, base) + "/"
                + Conversiones.convertirBase(anio, base) + "   " + Conversiones.convertirBase(horaMostrar, base) + ":"
                + Conversiones.convertirBase(minuto, base) + ":" + Conversiones.convertirBase(segundo, base) + periodo;
    }
}
