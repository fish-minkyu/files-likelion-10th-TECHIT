package com.example.contents.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String username;
  private String email;
  @Setter
  private String phone;
  @Setter
  private String bio; // 한줄 소개
  @Setter
  private String avatar; // 프로필 이미지 링크

  public User(String username, String email, String phone, String bio) {
    this.username = username;
    this.email = email;
    this.phone = phone;
    this.bio = bio;
  }
}
