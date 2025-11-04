package com.jsfcourse.calc;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@RequestScoped
public class CalcBB {

    private String kwota;
    private String lata;
    private String oprocentowanie;
    private Double result;

    @Inject
    FacesContext ctx;

    public String getKwota() {
        return kwota;
    }

    public void setKwota(String kwota) {
        this.kwota = kwota;
    }

    public String getLata() {
        return lata;
    }

    public void setLata(String lata) {
        this.lata = lata;
    }

    public String getOprocentowanie() {
        return oprocentowanie;
    }

    public void setOprocentowanie(String oprocentowanie) {
        this.oprocentowanie = oprocentowanie;
    }

    public Double getResult() {
        return result;
    }

    public void setResult(Double result) {
        this.result = result;
    }

    private boolean doTheMath() {
        try {
            double p = Double.parseDouble(this.kwota);
            double years = Double.parseDouble(this.lata);
            double rate = Double.parseDouble(this.oprocentowanie);

            double r = rate / 100 / 12; // miesięczna stopa procentowa
            double n = years * 12;      // liczba rat

            result = p * (r * Math.pow(1 + r, n)) / (Math.pow(1 + r, n) - 1);

            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Rata obliczona poprawnie", null));
            return true;
        } catch (Exception e) {
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Błąd danych wejściowych", null));
            return false;
        }
    }

    public String calc() {
        if (doTheMath()) {
            return "showresult";
        }
        return null;
    }

    public String calc_AJAX() {
        if (doTheMath()) {
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Rata: " + String.format("%.2f zł", result), null));
        }
        return null;
    }

    public String info() {
        return "info";
    }
}
