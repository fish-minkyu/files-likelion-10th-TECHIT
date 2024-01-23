package com.example.contents.exceptions;

// 사용자 이름이 중복일 때 발생하는 예외
public class UsernameExistsException extends Status400Exception {
  public UsernameExistsException() {
    // super은 부모 객체를 의미하며
    // 메소드의 이름이 주어지지 않는 경우 생성자를 호출할 때 사용한다.
    super("username exists");
  }
}
