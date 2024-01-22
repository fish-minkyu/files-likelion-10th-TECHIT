package com.example.contents;

import com.example.contents.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/user")
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
    @RequestParam("file") MultipartFile image
    ) throws IOException {

    return service.updateUserAvatar(userId, image);
  }
}
