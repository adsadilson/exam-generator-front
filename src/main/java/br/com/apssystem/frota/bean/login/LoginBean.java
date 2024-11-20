package br.com.apssystem.frota.bean.login;


import br.com.apssystem.frota.bean.login.custom.CustomURLEncoderDecoder;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;


@Named
@ViewScoped
public class LoginBean implements Serializable {
    private final LoginDAO loginDAO;
    private final ExternalContext externalContext;
    private String username;
    private String password;
    private String token;

    @Inject
    public LoginBean(LoginDAO loginDAO, ExternalContext externalContext) {
        this.loginDAO = loginDAO;
        this.externalContext = externalContext;
    }

    public String login() {
        try {
            Token jwtToken = loginDAO.loginReturningToken(username, password);
            token = jwtToken.getAccessToken();
            if (token == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Token inválido."));
                return null;
            }
            return "/index.xhtml?faces-redirect=true";
        } catch (Exception e) {
            // Qualquer outro erro
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro desconhecido",  e.getMessage()));
        }
        return "";
    }

    public String logout() {
        removeTokenAndExpirationTimeFromCookies();
        return "login.xhtml?faces-redirect=true";
    }

    private void addTokenAndExpirationTimeToCookies(String token, String expirationTime) {
        externalContext.addResponseCookie("token", CustomURLEncoderDecoder.encodeUTF8(token), null);
        externalContext.addResponseCookie("expirationTime", expirationTime, null);
    }

    private void removeTokenAndExpirationTimeFromCookies() {
        addTokenAndExpirationTimeToCookies(null, null);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
