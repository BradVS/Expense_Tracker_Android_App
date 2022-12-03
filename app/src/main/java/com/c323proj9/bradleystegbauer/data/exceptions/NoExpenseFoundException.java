package com.c323proj9.bradleystegbauer.data.exceptions;

public class NoExpenseFoundException extends Exception{
    public NoExpenseFoundException() {
    }

    public NoExpenseFoundException(String message) {
        super(message);
    }
}
