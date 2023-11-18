package com.savemyroaming;

import org.springframework.http.ResponseEntity;

public class MyHttpResponse<T> implements MyResponse<T> {
  private ResponseEntity<T> response;

  public MyHttpResponse(ResponseEntity<T> response) {
    this.response = response;
  }

  @Override
  public int getStatusCodeValue() {
    return response.getStatusCodeValue();
  }

  @Override
  public T getBody() {
    return response.getBody();
  }
}
