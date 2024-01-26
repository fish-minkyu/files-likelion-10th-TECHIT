# File Handling & Validation(Exception Handling)

- 2024.01.22 ~ 01.23`10주차`
- 01.22 File Handling: 이미지 업로드 및 출력
- 01.23 Validation

`1월 22일`, 이미지 업로드를 연습해보았다.  
`java.nio.file.Path;`과 `org.springframework.web.multipart.MultipartFile;`을   
import해서 파일 입출력 코드를 구현하였다. 


파일 입출력에서 크게 3단계로 구분할 수 있다.  
1. application.yaml 설정  
2. 사용자가 파일 저장 요청 시, 저장하는 경로 지정
3. 사용자에게 저장된 이미지 응답

\
`1월 23일`, Validation 중에서 `예외 처리(Exception Handling)`을 학습했다.  
예외 처리를 할 수 있는 방법 총 4가지를 하였다.  
1. `thorow new ResponseStatusException();`  
가장 처음으로 배운 예외 처리로 HttpStatus를 통해 Spring이 미리 설정해둔 상태 코드를 응답으로 반환할 수 있다.
2. `@ExceptionHandler`  
Controller 내부 예외 처리로서 컨트롤러 단위에서 예외 처리를 하고 싶은 경우 사용한다.  
서로 다른 서비스에서 발생한 에러들은 이를 호출한 컨트롤러에서 처리할 수 있게 된다.  
ResponseStatusException()보다 더 정교하게 예외를 처리할 수 있다.
3. `@ControllerAdvice & @RestControllerAdvice`  
프로젝트 전역 예외 처리를 할 수 있다.  
즉, 전체 어플리케이션에서 발생하는 모든 예외처리를 할 수 있다.  
@ExceptionHandler()을 모아두기 위한 Component의 일종이다.
4. `커스텀 예외 활용하기`  
프로젝트가 커짐에 따라 일관성 있는 예외처리를 위해 예외를 직접 정의할 수 있다.  
이름에 상황에 대한 정보를 바로 표현할 수 있다.  
상속 관계를 활용해 하나의 ExceptionHandler에서 동시에 예외처리가 가능해진다.

## 스택

- Spring Boot 3.2.2
- Spring Web
- Spring Boot Data JPA
- SQLite

## Key Point
`01/22`
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

---
`01/23`

1. `thorow new ResponseStatusException();`  
```java
throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
throw new ResponseStatusException(HttpStatus.NOT_FOUND);
throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
```
2. `@ExceptionHandler`  
[UserController](/src/main/java/com/example/contents/UserController.java)
```java
 @ExceptionHandler(IllegalArgumentException.class) // 예외가 발생했을 때 반응해서 응답한다.
  @ResponseStatus(code = HttpStatus.BAD_REQUEST) // 오로지 예외가 발생했을 때 어떻게 동작할지 정의하는 메서드다.
  public ErrorDto handleIllegalArgument(
    final IllegalArgumentException exception
  ) {
    log.warn(exception.getMessage());
    ErrorDto dto = new ErrorDto();
    dto.setMessage(exception.getMessage());
    return dto;
```
3. `@ControllerAdvice & @RestControllerAdvice`  
   [GlobalControllerAdvice](/src/main/java/com/example/contents/GlobalControllerAdvice.java)
```java
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
```
4. `커스텀 예외 활용하기`  
[UserService](/src/main/java/com/example/contents/UserService.java)
```java
  public UserDto create(UserDto dto){
  // 사용자 생성 전 계정 이름 겹침 확인 후
  // 확인했을 때 겹칠 경우 400
  if(repository.existsByUsername(dto.getUsername())){
  // 예외처리 방법 4가지
  // way1. ResponseStatusException();
  // throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

  // way2. @ExceptionHandler & way3. @ControllerAdvice
  // throw new IllegalArgumentException("duplicate username");

  // way4. 커스텀 예외
  throw new UsernameExistsException();
  }
}
```
[Status400Exception](/src/main/java/com/example/contents/exceptions/Status400Exception.java)
```java
// 400 오류를 발생시키는 모든 예외의 부모로 활용
public class Status400Exception extends RuntimeException {
  public Status400Exception(String message) {
    super(message);
  }
}
```
[UsernameExistException](/src/main/java/com/example/contents/exceptions/UsernameExistsException.java)
```java
// 사용자 이름이 중복일 때 발생하는 예외
public class UsernameExistsException extends Status400Exception {
  public UsernameExistsException() {
    // super은 부모 객체를 의미하며
    // 메소드의 이름이 주어지지 않는 경우 생성자를 호출할 때 사용한다.
    super("username exists");
  }
}
```
[GlobalControllerAdvice](/src/main/java/com/example/contents/GlobalControllerAdvice.java)
```java
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
```


## 복습
~~2024.01.22 File Handling 복습 완료~~  
~~2024.01.23 ~ 25 Validation(Exception Handling) 일부 복습 완료~~  
~~2024.01.26 Validation(Exception Handling) 복습 완료~~