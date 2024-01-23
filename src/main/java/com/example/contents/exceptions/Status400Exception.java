package com.example.contents.exceptions;

// 400 오류를 발생시키는 모든 예외의 부모로 활용
public class Status400Exception extends RuntimeException {
  public Status400Exception(String message) {
    super(message);
  }
}
