package com.cesarschool.barbearia.dominio.compartilhado.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Level{
    public static String INFO = "INFO";
    public static String SUCCESS = "SUCCESS";
    public static String ERROR = "ERROR";
    public static String WARN = "WARN";
}

public class LoggerSingleton {
    
    private static LoggerSingleton instance;
    private static final Logger slf4jLogger = LoggerFactory.getLogger(LoggerSingleton.class);

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
    
    public void error(String message, Throwable throwable){
        if(message == null) message = "";
        slf4jLogger.error(message, throwable);
    }
    
    public void warn(String message){
        getInstance().log(message, Level.WARN);
    }

    public void log(String message, String level){
        if(message == null) message = "";

        switch(level) {
            case "INFO":
                slf4jLogger.info(message);
                break;
            case "SUCCESS":
                slf4jLogger.info("✓ {}", message);
                break;
            case "ERROR":
                slf4jLogger.error(message);
                break;
            case "WARN":
                slf4jLogger.warn(message);
                break;
            default:
                slf4jLogger.info(message);
        }
    }
}
