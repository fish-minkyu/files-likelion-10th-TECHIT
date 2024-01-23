package com.example.contents;

import com.example.contents.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username); // null이 나올 수 없음

  User findUserByUsername(String username); // null이 나올 수 있음
}
