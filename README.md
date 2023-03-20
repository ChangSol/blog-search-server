# Blog Search API SERVER

### Default Environment
- PORT : 8080<br>
- swagger url : http://localhost:8080/swagger-ui
- code style : chang_sol_code_style.xml 참고

### Tech Stack
- JAVA 16
- SpringBoot 2.6.6
- JPA (Spring Data JPA), Hibernate 5.6.5(like query issue 로 인한 down)
- H2 DB
- SpringDoc를 통한 OpenAPI(3.0) 진행 (Swagger3)

### External Lib
- Lombok 1.18.20 (보일러플레이트 처리)
- MapStruct 1.5.2 (Data Mapper로 사용)

### Module Structure
- module-core : 도메인 모듈
- module-external : 외부 라이브러리 모듈
- module-search : 블로그 검색 APP 모듈
