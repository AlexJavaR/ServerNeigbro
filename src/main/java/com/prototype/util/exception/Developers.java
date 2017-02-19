package com.prototype.util.exception;

public enum Developers {

    ANDREI("ahaytukam@gmail.com"),
    ALEX_MELESHKO("alemeleshko@gmail.com"),
    VLAD_DORFMAN("dorfman.vlad@gmail.com"),
    VLAD_PAPAVA("uniqalien69@gmail.com"),
    DENIS_NETOSIN("dennetesin@gmail.com"),
    MARI("reineke.fuchs@gmail.com");

    private String email;

    Developers(String email) {
        this.email = email;
    }

    public String email() {
        return email;
    }
}
