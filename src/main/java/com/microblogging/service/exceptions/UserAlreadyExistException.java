package com.microblogging.service.exceptions;

public class UserAlreadyExistException extends RuntimeException {

  public UserAlreadyExistException(String message) {
    super(message);
  }

}
