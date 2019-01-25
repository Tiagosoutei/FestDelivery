package com.festdelivery.festdelivery.Api.Model;

public class Notificacao {

    private String mobileToken;

    public Notificacao(String mobileToken) {
        this.mobileToken = mobileToken;
    }

    public String getMobileToken() {
        return mobileToken;
    }

    public void setMobileToken(String mobileToken) {
        this.mobileToken = mobileToken;
    }
}
