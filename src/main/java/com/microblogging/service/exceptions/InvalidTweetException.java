package com.microblogging.service.exceptions;

public class InvalidTweetException extends RuntimeException {

  public InvalidTweetException(String message) {
    super(message);
  }

}

