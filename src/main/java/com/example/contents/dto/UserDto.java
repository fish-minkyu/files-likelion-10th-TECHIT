package com.example.contents.dto;

import com.example.contents.entity.User;
import lombok.*;

// Dto는 실제 사용 데이터와 사용자에게 반환해주는 데이터를 분리하기 위해 사용
// Entity를 그대로 반환만 안해주면 된다.
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
  private Long id;
  private String username;
  private String email;
  @Setter
  private String phone;
  @Setter
  private String bio;
  @Setter
  private String avatar;



  // static factory method
  public static UserDto fromEntity(User entity) {
    return new UserDto(
      entity.getId(),
      entity.getUsername(),
      entity.getEmail(),
      entity.getPhone(),
      entity.getBio(),
      entity.getAvatar()
    );
  }
}
