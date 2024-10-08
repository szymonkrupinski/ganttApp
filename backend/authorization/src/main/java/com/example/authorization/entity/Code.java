package com.example.authorization.entity;

public enum Code {
    SUCCESS("Operacja zakończona powodzeniem"),
    PERMIT("Przyznano dostep"),
    A1("Nie udało się zalogować"),
    A2("Użytkownik o wskazanej nazwie nie istnieje"),
    A3("Token stracił ważnosć"),
    A4("Użytkownik o podanej nazwie już istnieje"),
    A5("Użytkownik o podanym emailu już istnieje");

    public final String label;

    private Code(String label){
        this.label=label;
    }
}
