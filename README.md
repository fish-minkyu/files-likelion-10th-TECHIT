# File Handling

- 2024.01.22 `10주차`

이미지 업로드를 연습해보는 프로젝트다.  
`import java.nio.file.Path;`과 `import org.springframework.web.multipart.MultipartFile;`을  
import해서 사용되었다.

## 스택

- Spring Boot 3.2.1
- Spring Web
- Spring Boot Data JPA
- SQLite

## Key Point

### 이미지 업로드 & 반환 3단계

1. application.yaml 설정
- 정적 파일 요청 URL 경로 설정
- 정적 파일 응답 폴더 설정
[이미지 설정](/src/main/resources/application.yml)

2. 사용자가 파일 저장 요청 시, 저장하는 경로 지정 & 3. 사용자에게 저장된 이미지 응답
[이미지 저장 & 이미지 응답](/src/main/java/com/example/contents/MultipartController.java)

```java
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
    // TODO 폴더가 없을 때 만들기, 폴더가 없을 때 발생하는 500 에러 방지

    // 파일을 저장할 경로 + 파일명 지정
    // Path란 객체가 실행했을 때의 디렉토리를 기준으로 상대 경로로 위치를 지정한다.
    Path downloadPath =Path.of("media/" + multipartFile.getOriginalFilename());

    // 저장한다.
    multipartFile.transferTo(downloadPath); // IOException이 발생할 위험 존재하므로 에러 처리가 필요하다.

    // 3번. 어떻게 해당 파일을 다시 받아갈 수 있는지 응답해주기
    return "http://localhost:8080/static/" + multipartFile.getOriginalFilename();
  }
```

## 복습
~~2024.01.22 복습 완료~~
