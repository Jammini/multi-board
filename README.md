# multi-board

요구사항이 점진적으로 추가되는 고전 게시판 프로젝트

## 🚩 사용기술 및 개발환경
- Spring Boot 3.3.5
- Java 17
- JPA
- Gradle
- JUnit5

## 🚩 프로젝트 주제 및 일정

1. 스프린트 1 (2024.11.17 ~ 2024.11.30)
   - GitHub Repository 생성
     - 컨트리뷰트 지정 (@Antop-dev, @imdiana)
     - master 브랜치 룰 설정
   - 게시글 CRUD 개발
2. 스프린트 2 (2024.12.01 ~ 2024.12.14)
   - 페이징
     - 총 개수
     - offset 방식
     - cursor 방식
   - 키워드 검색


## Git Branch Strategy

Github-Flow 전략을 사용한다.

![image](https://github.com/f-lab-edu/auctionHub/assets/59176149/0a15e53e-7896-4b26-848a-60e4e71b0288)


## Commit Message Convention

커밋 메세지는 다음과 같은 형식으로 작성한다.

[TYPE] COMMIT_MESSAGE

- [TYPE]
    - feature : 새로운 기능 추가
    - fix : 버그 수정
    - docs : 문서 업데이트
    - refactor : 코드의 리팩토링
    - test : 테스트코드 업데이트
    - env : 환경 구축
- COMMIT_MESSAGE
    - TYPE을 기입했다면, 그 이후에는 COMMIT_MESSAGE 부분에 해당 커밋에 대한 내용을 간략하게 기술


## Coding convention
- [google java convention](https://google.github.io/styleguide/javaguide.html) 사용.
- indent size 2 -> 4 변경.
- tab width 2 -> 4 변경.
