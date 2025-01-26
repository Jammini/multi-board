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
3. 스프린트 3 (2024.12.15 ~ 2024.12.28)
   - 조회수 처리
   - 조회수 증가
   - 무한 조회수 증가 방지
     - IP기반, 쿠키, 세션(Redis?), 디비, fingerprint
   - 해시태그 추가
   - 해시태그 검색
4. 스프린트 4 (2024.12.29 ~ 2025.01.12)
   - 파일 첨부
5. 스프린트 5 (2025.01.13 ~ 2025.01.26)
   - 파일 첨부
   - Github Actions

## 기술적 issue 해결 과정

- [Github Actions으로 Sonar Cloud와 Slack 알림 설정하기](https://systemdata.tistory.com/115)
- [운영환경에서 안전하게 테이블 칼럼을 추가하려면 어떤 방법이 좋을까?](https://systemdata.tistory.com/114)
- [DTO는 어느 레이어까지 사용하는 것이 좋을까?](https://systemdata.tistory.com/112)
- [Dependency Analyzer & Dependency Diagram 의존성 확인하기](https://systemdata.tistory.com/111)
- [파일마다 EOL(EndOfLine) 넣어야 하는 이유는 무엇일까?](https://systemdata.tistory.com/110)
- [자바 구글 코딩 컨벤션 적용하기](https://systemdata.tistory.com/109)
- [Github 저장소에 Repository Rule 제약하기](https://systemdata.tistory.com/108)

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
