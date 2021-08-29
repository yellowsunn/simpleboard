# simpleforum

## 프로젝트 소개
기존에 제작했던 간단한 게시판 프로젝트의 문제점을 개선하여 다시 제작한 프로젝트이다.   
(기존 프로젝트: https://github.com/yellowsunn/security-project)<br/>   

개선된 프로젝트는 유효성 검사, xss, xsrf 에 초점을 맞춰 제작되었다. 

## 기존 프로젝트의 문제점
### 1. 서버에서 유효성 검사 부재
기존 프로젝트는 프론트엔드에서만 유효성 검사를 수행한다.   
따라서 브라우저의 개발자도구에서 값을 변조하거나, 직접 백엔드 서버로 api 요청을 하면 유효하지 않은 input이 들어올 수 있는 문제를 가지고 있다.

### 2. 적절하지 못한 HTTP 상태코드
클라이언트에서 잘못된 input을 주는 경우에도 서버에서 500에러 상태코드를 출력하는 등의 예외처리가 부족해 상황에 맞지않는 상태코드를 보여주는 문제가 있다.

### 3. XSS에 취약
게시판의 게시글은 html 태그를 사용할 수 있으므로 스크립트 코드가 삽입되면 게시글을 조회하는 순간 스크립트가 실행되는 문제점이 있으므로 XSS공격이 가능하다.   
기존 프로젝트는 클라이언트의 에디터가 적절하게 태그를 필터링해주나 서버에는 전혀 필터링을 하지 않는다.   
악의적인 사용자가 우회해서 태그를 삽입해 요청하면 속수무책으로 당하고 만다.

### 4. XSRF 문제점   
* CSRF 토큰
<img src="/readme_file/csrf_token.png" width="35%">

```java
protected void configure(HttpSecurity http) throws Exception {
	http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
}
```
기존 프로젝트는 XSRF를 방지하기 위해 다음과 같은 스프링 시큐리티의 CSRF 토큰 기능을 사용했었다.   
서버는 세션별로 UUID 값을 가진 토큰을 쿠키로 발급해주고, 클라이언트에서는 요청에 X-XSRF-TOKEN 헤더에 토큰으로 받은 UUID값을 전달해야만 서버가 요청을 받아들인다.
<br><br>

* 문제점
<img src="/readme_file/csrf_failed.png" width="90%">
<img src="/readme_file/csrf_success.png" width="90%">
문제점은 CSRF 토큰을 쿠키 방식으로 사용하는데 있다.<br>

해당 쿠키는 '**HttpOnly**' 속성이 없으므로 같은 도메인 내에서는 스크립트 코드에서 읽을 수 있다.   
따라서 위의 예시처럼 순진한 사용자가 _http://hacker.com_ 에 접근하면 쿠키의 값을 읽어오지 못해 요청이 무시되지만,  
같은 도메인인 _https://yellowsunn.com/posts/10_ 에 접근하면 쿠키의 값을 읽어 CSRF 공격에 성공하게 된다.

