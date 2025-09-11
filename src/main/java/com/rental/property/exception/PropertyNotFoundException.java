package com.rental.property.exception;
import org.springframework.web.bind.annotation.RestControllerAdvice;
public class PropertyNotFoundException  extends RuntimeException{
    public  PropertyNotFoundException(String msg){
        super(msg);
    }
}
