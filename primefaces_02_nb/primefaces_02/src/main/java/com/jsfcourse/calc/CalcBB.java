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
        FacesContext ctx = FacesContext.getCurrentInstance();
        boolean error = false;

        // 🔸 Walidacja pustych pól
        if (kwota == null || kwota.trim().isEmpty()) {
            ctx.addMessage("creditForm:kwota",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Podaj kwotę kredytu", null));
            error = true;
        }

        if (lata == null || lata.trim().isEmpty()) {
            ctx.addMessage("creditForm:lata",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Podaj liczbę lat", null));
            error = true;
        }

        if (oprocentowanie == null || oprocentowanie.trim().isEmpty()) {
            ctx.addMessage("creditForm:oprocentowanie",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Podaj oprocentowanie", null));
            error = true;
        }

        if (error) {
            return null; // zatrzymaj przetwarzanie
        }

        try {
            double p = Double.parseDouble(kwota);
            double years = Double.parseDouble(lata);
            double rate = Double.parseDouble(oprocentowanie);

            // 🔸 Walidacja wartości logicznych
            if (p <= 0) {
                ctx.addMessage("creditForm:kwota",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Kwota musi być większa od 0", null));
                error = true;
            }

            if (years <= 0) {
                ctx.addMessage("creditForm:lata",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Liczba lat musi być większa od 0", null));
                error = true;
            }

            if (rate <= 0) {
                ctx.addMessage("creditForm:oprocentowanie",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Oprocentowanie musi być dodatnie", null));
                error = true;
            }

            if (rate > 100) {
                ctx.addMessage("creditForm:oprocentowanie",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Oprocentowanie nie może przekraczać 100%", null));
                error = true;
            }

            if (error) {
                return null;
            }

            // 🔹 Obliczanie raty
            double r = rate / 100 / 12; // miesięczna stopa procentowa
            double n = years * 12;      // liczba rat

            result = p * (r * Math.pow(1 + r, n)) / (Math.pow(1 + r, n) - 1);

            ctx.getExternalContext().getFlash().put("result", result);
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Rata obliczona poprawnie", null));

            return "showresult?faces-redirect=true";

        } catch (NumberFormatException e) {
            ctx.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wprowadź poprawne dane liczbowe", null));
            return null;
        }
    }

    public String info() {
        return "info";
    }
}
