package com.example.elegituvacuna.Estructuras;

public class EstructuraToken {

    private String exito;
    private String token;
    private String tokenRefresh;
    private long reciboToken;

    public EstructuraToken() {
    }



    public void setExito(String exito) {
        this.exito = exito;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenRefresh() {
        return tokenRefresh;
    }

    public void setTokenRefresh(String tokenRefresh) {
        this.tokenRefresh = tokenRefresh;
    }

    public long getReciboToken() {
        return reciboToken;
    }

    public void setReciboToken(long reciboToken) {
        this.reciboToken = reciboToken;
    }
}
