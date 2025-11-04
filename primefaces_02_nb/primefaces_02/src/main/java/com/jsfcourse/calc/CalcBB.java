package com.jsfcourse.calc;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

@Named
@RequestScoped
public class CalcBB {

    private String kwota;
    private String lata;
    private String oprocentowanie;
    private Double result;

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

    public String calc() {
        try {
            double p = Double.parseDouble(this.kwota);
            double years = Double.parseDouble(this.lata);
            double rate = Double.parseDouble(this.oprocentowanie);

            double r = rate / 100 / 12; // miesięczna stopa procentowa
            double n = years * 12;      // liczba rat

            // wzór na ratę równą
            result = p * (r * Math.pow(1 + r, n)) / (Math.pow(1 + r, n) - 1);

            FacesContext ctx = FacesContext.getCurrentInstance();

            // przekazanie danych do flash scope
            ctx.getExternalContext().getFlash().put("result", result);

            ctx.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Rata obliczona poprawnie", null));

            // redirect = nowy request, dane dostępne przez flash
            return "showresult?faces-redirect=true";

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Błąd: nieprawidłowe dane wejściowe", null));
            return null;
        }
    }

    public String info() {
        return "info";
    }
}
