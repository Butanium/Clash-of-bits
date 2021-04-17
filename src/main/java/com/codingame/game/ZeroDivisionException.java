package com.codingame.game;

public class ZeroDivisionException extends ArithmeticException {
    public ZeroDivisionException(){

    }

    public ZeroDivisionException(String message){
        super(message);
    }
}
