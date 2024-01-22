package com.example.contents;

import com.example.contents.dto.UserDto;
import com.example.contents.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

// throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED); // NOT_IMPLEMENTED: 서버에서 아직 구현되지 않았다. (500번대 에러)
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository repository;

  // CREATE USER
  // 회원가입
  public UserDto create(UserDto dto) {
    // 회원 가입
    User newUser = new User(
      dto.getUsername(),
      dto.getEmail(),
      dto.getPhone(),
      dto.getBio()
    );

    return UserDto.fromEntity(repository.save(newUser));
  }

  // READ USER BY USERNAME
  public UserDto readUserByUsername(String username) {
    User user = repository.findUserByUsername(username);

    return UserDto.fromEntity(user);
  }

  // UPDATE USER AVATAR
  // 회원 프로필 아이콘 업데이트
  public UserDto updateUserAvatar(Long id, MultipartFile image) throws IOException {
    // id에 맞는 User 객체 불러오기
    Optional<User> optionalUser = repository.findById(id);

    // 해당 id의 Optional<User>이 없다면 에러 반환
    if (optionalUser.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    User user = optionalUser.get();

    // 어플리케이션 폴더에 저장
    Path downloadPath = Path.of("media/" + image.getOriginalFilename());
    image.transferTo(downloadPath);

    // 프로필 아이콘 업데이트
    user.setAvatar(downloadPath.toString());

    // 저장 후 UserDto로 변환해서 응답
    return UserDto.fromEntity(repository.save(user));
  }
}
