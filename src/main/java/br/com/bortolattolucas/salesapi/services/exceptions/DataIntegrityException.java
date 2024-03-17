package br.com.bortolattolucas.salesapi.services.exceptions;

import lombok.Getter;

import java.util.Map;

@Getter
public class DataIntegrityException extends RuntimeException {

    private final Map<String, String> fieldErrors;

    public DataIntegrityException(String msg, Map<String, String> fieldErrors){
        super(msg);
        this.fieldErrors = fieldErrors;
    }
}
