package com.example.contents;

import com.example.contents.dto.ErrorDto;
import com.example.contents.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
  private final UserService service;

  // Create
  @PostMapping
  public UserDto create(
    @RequestBody UserDto dto
  ) {
    return service.create(dto);
  }

  // Read
  @GetMapping("/{username}")
  public UserDto read(
    @PathVariable("username")
    String username
  ) {
    return service.readUserByUsername(username);
  }

  // Update
  @PutMapping("/{userId}/avatar")
  public UserDto avatar(
    @PathVariable("userId") Long userId,
    @RequestParam("image") MultipartFile imageFile
    ) {
    return service.updateUserAvatar(userId, imageFile);
  }

  // 컨트롤러 단위에서 예외처리를 하고 싶은 경우
  // UserController에서 에러를 잡아서 컨트롤을 해준다.
/*  @ExceptionHandler(IllegalArgumentException.class) // 발생한 에러를 인자로
  public String handleIllegalArgument( // 메서드를 실행한다.
    final IllegalArgumentException exception
  ) {
    log.warn(exception.getMessage());
    return "illegalArgumentException"; // RequestMapping과 동일하게 사용자에게 응답으로 전달한다.
  }*/


  //IllegalArgumentException <- 이 예외가 발생한 경우
/*  @ExceptionHandler(IllegalArgumentException.class) // 예외가 발생했을 때 반응해서 응답한다.
  @ResponseStatus(code = HttpStatus.BAD_REQUEST) // 오로지 예외가 발생했을 때 어떻게 동작할지 정의하는 메서드다.
  public ErrorDto handleIllegalArgument(
    final IllegalArgumentException exception
  ) {
    log.warn(exception.getMessage());
    ErrorDto dto = new ErrorDto();
    dto.setMessage(exception.getMessage());
    return dto;
  }*/
}
