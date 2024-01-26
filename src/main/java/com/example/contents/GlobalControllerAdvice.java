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

// Controller는 아니고 예외처리를 위한 ExceptionHandler를 모아놓기 위한
// 컨트롤러에게 제안을 하는 Bean이다. (component의 일종으로 Bean이다.)
@RestControllerAdvice // component 어노테이션이 있으므로 Bean으로 관리 됨
public class GlobalControllerAdvice {
  // 예외가 발생했을 때 사용자에게 에러 응답을 보내기 위한 메서드다.
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

  // UsernameExistsException 예외를 처리하는 예외 처리 메서드
  // ( @ExceptionHandler(Status400Exception.class)으로 바꿈! )
/*
  @ExceptionHandler(UsernameExistsException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorDto handleUsernameExists(
    final UsernameExistsException exception
  ) {
    ErrorDto dto = new ErrorDto();
    dto.setMessage(exception.getMessage());
    return dto;
  }
*/

  // ExceptionHandler를 상속한 부모 예외를 기준으로 받아줄 수 있다.
  // 상속 관계를 활용해서 서로 다른 400 예외에 대해서 같은 방식으로 예외 처리할 수 있다.
  @ExceptionHandler(Status400Exception.class) // Status400Exception을 기준으로 한 예외 처리기
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