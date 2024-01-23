package com.example.contents;

import com.example.contents.dto.ErrorDto;
import com.example.contents.exceptions.Status400Exception;
import com.example.contents.exceptions.UsernameExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 전체 application에서 발생하는 모든 에러들을 핸들링할 수 있다.

// Controller는 아니고 예외처리를 위한 ExceptionHandler를 모아놓기 위한 용도로 사용
@RestControllerAdvice // component 어노테이션이 있으므로 Bean으로 관리 됨
public class GlobalControllerAdvice {
  @ExceptionHandler(IllegalArgumentException.class) // 예외의 일종이면 작동한다.
  public ResponseEntity<ErrorDto> handleIllegalArgument(
    final IllegalArgumentException exception
  ) {
    ErrorDto dto = new ErrorDto();
    dto.setMessage(exception.getMessage());
    return ResponseEntity
      .badRequest()
      .body(dto);
  }

  @ExceptionHandler(UsernameExistsException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorDto handleUsernameExists(
    final UsernameExistsException exception
  ) {
    ErrorDto dto = new ErrorDto();
    dto.setMessage(exception.getMessage());
    return dto;
  }

  // 11:36분 수업 놓침
  // 11:38 상속 관계 ~~ 제일 큰 장점이다.
  @ExceptionHandler(Status400Exception.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorDto handle400(
    final UsernameExistsException exception
  ) {
    ErrorDto dto = new ErrorDto();
    dto.setMessage(exception.getMessage());
    return dto;
  }
}

// ControllerAdvice는 view를 반환하는 목적으로도 사용할 수 있다.