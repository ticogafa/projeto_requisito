package com.cesarschool.barbearia_backend.common.exceptions;

public class DuplicateException extends RuntimeException{
    public DuplicateException(String msg){
        super(msg);
    }
}
