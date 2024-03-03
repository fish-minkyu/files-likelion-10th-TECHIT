package com.example.contents;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

// multipart/form-data를 받기 위한 연습
@Slf4j
@RestController
public class MultipartController {
  @PostMapping(
    value = "/multipart",
    consumes = MediaType.MULTIPART_FORM_DATA_VALUE
  )
  public String multipart(
    // 파일을 받을 때는 HTML의 form의 형태로 받을 것이며
    // 인자 하나 하나가 분리되어서 서버로 들어올 것이다.
    // Body 자체를 한번에 해석해서 서버로 도달하지 않는다.
    // @RequestBody보단 @RequestParam을 써야한다는 결론이 도출된다.
    @RequestParam("name")
    String name,
    // 받아주는 자료형을 MultipartFile
    @RequestParam("file")
    MultipartFile multipartFile
  ) throws IOException {
    // 2번. 사용자가 이미지 저장 요청 시, 정해진 경로에 저장해둔다.
    // 업로드한 파일명을 그대로 사용하고 싶을 때
    log.info(multipartFile.getOriginalFilename());
    // TODO 폴더가 없을 때 만들기, 폴더가 없을 때 발생하는 500 에러 방지 (UserService - updateUserAvatar 메소드 참고)

    // 파일을 저장할 경로 + 파일명 지정
    // Path란 객체가 실행했을 때의 디렉토리를 기준으로 상대 경로로 위치를 지정한다.
    Path downloadPath = Path.of("media/" + multipartFile.getOriginalFilename());

    // 저장한다.
    multipartFile.transferTo(downloadPath); // IOException이 발생할 위험 존재하므로 에러 처리가 필요하다.

    /*
    // 사용자가 업로드한 파일이 이미지 형식인지 확인하고 싶을 때를 구현해보기(그 외 다양한 방법이 있음)
    // 저장할 파일 이름
    File file = new File("./media/" + multipartFile.getOriginalFilename());
    try(OutputStream outputStream = new FileOutputStream(file)) {
      // byte[] 형태로 파일을 받는다.
      byte[] fileBytes = multipartFile.getBytes();

      // 이미지 파일을 구성하는 byte[]가 맞는지 확인
      // System.out.println(new String(fileBytes, StandardCharsets.UTF_8)); // 요청한 파일 확장자가 출력됨
      outputStream.write(fileBytes); // 파일을 저장
    }
    */

    // 3번. 어떻게 해당 파일을 다시 받아갈 수 있는지 응답해주기
    return "http://localhost:8080/static/" + multipartFile.getOriginalFilename();
  }
}
