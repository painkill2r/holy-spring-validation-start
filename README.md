#### 최초 작성일 : 2021.07.13(화)

# Spring Validation 기초

Spring Boot를 이용한 Validation 기초 학습

## 학습 환경

1. OS : MacOS
2. JDK : OpenJDK 11.0.5
3. Framework : Spring Boot 2.4.4
    - [Spring Initializer 링크 : https://start.spring.io](https://start.spring.io)
    - 패키징 : jar
    - 의존설정(Dependencies)
        - Spring Web
        - Thymeleaf
        - Lombok
4. Build Tools : Gradle
5. Database : H2

## 타임리프 특징

1. 서버 사이드 HTML 렌더링(SSR)
    - 타임리프는 백엔드 서버에서 HTML을 동적으로 렌더링 하는 용도로 사용된다.
2. 네츄럴 템플릿(Natural Templates)
    - 타임리프는 순수 HTML를 최대한 유지하는 특징이 있다.
    - 타임리프로 작성한 파일은 HTML을 유지하기 때문에 웹 브라우저에서 파일을 직접 열어도 내용을 확인할 수 있고, 서버를 통해 뷰 템플릿을 거치면 동적으로 변경된 결과를 확인할 수 있다.
    - JSP를 포함한 다른 뷰 템플릿들은 해당 파일을 열면 JSP 소스코드와 HTML이 뒤죽박죽 섞여서 웹 브라우저에서 정상적인 HTML 결과를 확인할 수 없다. 오직 서버를 통해서 JSP가 HTML로
      렌더링되어야 화면을 확인할 수 있다. 반면에 타임리프로 작성된 파일은 해당 파일을 그대로 웹 브라우저에서 열어도 정상적인 HTML 결과를 확인할 수 있다.
    - 이렇게` 순수 HTML을 그대로 유지하면서 뷰 템플릿도 사용할 수 있는 타임리프의 특징을 네츄럴 템플릿`이라 한다.
3. 스프링 통합 지원
    - 타임리프는 스프링과 자연스럽게 통합되고, 스프링의 다양한 기능을 편리하게 사용할 수 있게 지원한다.

## Java 8 날짜

1. 타임리프에서 자바8 날짜인 `LocalDate` , `LocalDateTime` , `Instant`를 사용하려면 추가 라이브러리가 필요하다. 스프링 부트 타임리프를 사용하면 해당 라이브러리가 자동으로 추가되고
   통합된다.
    - 타임리프 자바8 날짜 지원 라이브러리: `thymeleaf-extras-java8time`(스프링 부트 사용시 자동으로 추가되어 있음.)
    - Java 8 날짜용 유틸리티 객체: `#temporals`

## 참고 링크

1. 타임리프 공식 사이트: [https://www.thymeleaf.org/](https://www.thymeleaf.org/)
2. 타임리프 메뉴얼 기본
   기능: [https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html](https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html)
3. 타임리프 메뉴얼 - 스프링
   통합 : [https://www.thymeleaf.org/doc/tutorials/3.0/thymeleafspring.html](https://www.thymeleaf.org/doc/tutorials/3.0/thymeleafspring.html)
4. 타임리프 유틸리티
   객체: [https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#expression-utilityobjects](https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#expression-utilityobjects)
5. 타임리프 유틸리티 객체
   예시: [https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#appendix-b-expressionutility-objects](https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#appendix-b-expressionutility-objects)