# File Handling & Validation(Error Handling)

- 2024.01.22 ~ 01.23`10주차`
- 01.22 File Handling: 이미지 업로드 및 출력
- 01.23 Validation

1월 22일, 이미지 업로드를 연습해보았다.  
`java.nio.file.Path;`과 `org.springframework.web.multipart.MultipartFile;`을   
import해서 파일 입출력 코드를 구현하였다.

1월 23일, Validation을 설정했다.

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
```yaml
spring:
  # 1번. 정적파일들을 어느 URL 경로에서 요청이 오면 어떤 폴더에서 찾아 답변을 줄지 설정
  mvc:
    # 어떤 경로에 대한 요청의 응답으로 정적 파일 응답을 할지를 결정하는 설정
    # 즉, static-path-pattern애서 설정한 경로로 요청이 오면
    # static 폴더 안에 있는 정적파일들을 찾아 응답을 하는 것이다.
    # Ex) static-path-pattern: /copyright
    # 요청: /copyright/assets/images/shark.png
    # 응답: copyright 경로에 요청이 왔으므로 static 경로에서 해당 요청 경로로 파일을 찾아주자
    # /**: 복수의 경로를 타는 요청을 의미
    static-path-pattern: /static/** # 정적 파일들을 어디에서 응답할지 설정
  web:
    resources:
      # 어떤 폴더의 파일을 정적 응답으로 전달할지를 설정
      # file:media/  : /static이란 요청을 받았을 때, 해당 폴더에 있는 파일을 찾아줘
      # classpath:/static  : build를 했을 때, classpath를 기준으로 static 폴더 안에 있는 내용을 정적으로 제공해줘
      static-locations: file:media/, classpath:/static
```

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
