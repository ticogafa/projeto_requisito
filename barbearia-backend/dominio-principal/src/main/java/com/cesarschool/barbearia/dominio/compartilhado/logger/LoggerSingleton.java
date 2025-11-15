package com.cesarschool.barbearia.dominio.compartilhado.logger;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

class Level{
    public static String INFO = "INFO";
    public static String SUCCESS = "SUCCESS";
    public static String ERROR = "ERROR";
    public static String WARN = "WARN";
}

public class LoggerSingleton {
    
    private static LoggerSingleton instance;

    private LoggerSingleton(){}

    public static LoggerSingleton getInstance(){
        if(instance == null){
            instance = new LoggerSingleton();
        }
        return instance;
        }
    
    public void info(String message){
        getInstance().log(message, Level.INFO);
    }

    public void success(String message){
        getInstance().log(message, Level.SUCCESS);
    }
    
    public void error(String message){
        getInstance().log(message, Level.ERROR);
    }
    
    public void warn(String message){
        getInstance().log(message, Level.WARN);
    }

    public void log(String message, String level){
    if(message == null) message = "";

    String timestamp = ZonedDateTime
        .now()
        .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);


    System.out.printf("%s [%s] - %s%n", timestamp, level, message);
    }
}
