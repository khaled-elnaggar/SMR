package com.savemyroaming;

public interface MyResponse<T> {
  int getStatusCodeValue();
  T getBody();
}
