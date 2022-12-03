package com.c323proj9.bradleystegbauer.controller.exceptions;

public class InvalidIDException extends Exception{
    public InvalidIDException() {
        super();
    }

    public InvalidIDException(String message) {
        super(message);
    }
}
