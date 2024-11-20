package br.com.apssystem.frota.bean.login;

import java.io.Serializable;


public class Token implements Serializable {
    private String accessToken; // Nome deve coincidir com o JSON retornado
    private String tokenType;

    // Getters e setters
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }


}
