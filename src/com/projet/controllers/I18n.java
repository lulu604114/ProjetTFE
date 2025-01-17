package com.projet.controllers;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Locale;


/**
 * =================================================================
 * Created by Intellij IDEA.
 *
 * @author lucas
 * @project JSF-TFE
 * Date: 06/12/2019
 * Time: 11:04
 * =================================================================
 */
@Named("i18n")
@SessionScoped
public class I18n implements Serializable {

    private static final long serialVersionUID = 1L;

    private Locale locale = new Locale("fr");

    public String getLanguage() {
        return locale.getLanguage();
    }

    public void changeLanguage(String language) {
        this.locale = new Locale(language);
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
    }

    public Locale getLocale() {
        return this.locale;
    }
}