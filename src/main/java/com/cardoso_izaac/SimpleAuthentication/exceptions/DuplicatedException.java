package com.cardoso_izaac.SimpleAuthentication.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicatedException extends RuntimeException {

   public DuplicatedException(String message) {
        super(message);
    }
}
