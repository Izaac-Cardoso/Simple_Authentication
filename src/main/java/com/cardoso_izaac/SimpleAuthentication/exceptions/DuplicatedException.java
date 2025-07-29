package com.cardoso_izaac.SimpleAuthentication.exceptions;

public class DuplicatedException extends RuntimeException {

   public DuplicatedException(String message) {
        super(message);
    }
}
