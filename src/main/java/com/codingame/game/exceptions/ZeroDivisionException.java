package com.codingame.game.exceptions;

public class ZeroDivisionException extends ArithmeticException {
    public ZeroDivisionException(){

    }

    public ZeroDivisionException(String message){
        super(message);
    }
}
