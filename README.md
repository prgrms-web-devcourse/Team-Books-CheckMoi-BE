# 📖 CheckMoi-BE
### 도서 기반 스터디 관리 플랫폼

<p align="center">
<img src="https://user-images.githubusercontent.com/41179265/184524017-beed4f4a-27a2-4545-8a0e-f59b19853b0f.png" alt="checkmoi"/>
</p>


## 🧑‍🤝‍🧑 팀구성

### 🔹 BE 백둥이
<table>
  <tr>
    <td align="center"><b>kuber/쿠버</b></td>
    <td align="center"><b>kimyo/김요</b></td>
    <td align="center"><b>noolee/누리</b></td>
    <td align="center"><b>noah/노아</b></td>
    <td align="center"><b>R/멘토님</b></td>
  </tr>
  <tr>
    <td>
        <a href="https://github.com/zxcv9203">
            <img src="https://avatars.githubusercontent.com/u/41960243?v=4" width="100px" />
        </a>
    </td>
    <td>
        <a href="https://github.com/kimziou77">
            <img src="https://avatars.githubusercontent.com/u/41179265?s=96&v=4" width="100px" />
        </a>
    </td>
    <td>
        <a href="https://github.com/ynoolee">
            <img src="https://avatars.githubusercontent.com/u/53856184?v=4" width="100px" />
        </a>
    </td>
    <td>
        <a href="https://github.com/ICCHOI">
            <img src="https://avatars.githubusercontent.com/u/48702370?v=4" width="100px" />
        </a>
    </td>
    <td>
        <a href="https://github.com/SeokRae">
            <img src="https://avatars.githubusercontent.com/u/17922700?v=4" width="100px" />
        </a>
    </td>
  </tr>
  <tr>
    <td align="center"><a href="https://github.com/zxcv9203">김용철</a></td>
    <td align="center"><a href="https://github.com/kimziou77">김수빈</a></td>
    <td align="center"><a href="https://github.com/ynoolee">이연우</a></td>
    <td align="center"><a href="https://github.com/ICCHOI">최인창</a></td>
    <td align="center"><a href="https://github.com/SeokRae">R</a></td>
  </tr>
</table>

## 기획
**꾸준하게 증가하고 있는 책을 이용한 스터디 수요**

현재 온라인에서 스터디를 진행하는 것이 계속해서 증가하고 있습니다.

스터디를 하게 되면 스터디에 특화된 서비스가 아닌 , 주로 Zoom 과 같은 온라인 협업툴을 통해 진행합니다.
이런 툴들은 스터디를 진행할 공간은 제공해주지만 스터디 운영을 지원해 주는 툴은 아닙니다

책모이는 "스터디 운영과 소통을 한 곳에서 지원해 주는 곳이 있으면 좋겠다"는 기획에서 시작되었습니다

스터디를 운영하는 것
  - 스터디를 개설하고, 비동기식 소통의 공간을 제공

스터디를 관리하는 것 
  - 출석체크, 예치금과 같은 강제성을 부여

스터디를 모집하는 것 
  - 원하는 책, 원하는 시간에, 본인과 맞는 사람들을 찾을 수 있도록 지원

하지만 개발기간상 모든 종류의 스터디를 지원하기는 힘들다고 판단하여,
**책을 중심으로 모이는 스터디를 중점적으로 지원해주는 서비스**를 기획하였습니다.


## 💻 기술 스택

카테고리 | 사용 툴
--- | ---
환경 | Ubuntu 18.04
언어 | Java JDK 17
빌드 | Gradle 7.5
프레임워크 | Spring Boot 2.7.2
DB | MySQL8
ORM | Data JPA + QueryDsl
문서화 | RestDocs + Swagger
테스트 | Junit5, Mockito, Jacoco, SonarQube
보안 | Spring Security + OAuth2
CI | Github Actions
CD | Jenkins
Infra | AWS EC2, S3, RDS, Docker, Nginx

## 🏗️ Architecture
<img width="1068" alt="image" src="https://user-images.githubusercontent.com/41960243/184530706-07d26d1d-9fe3-45e4-bf5c-6d717268effd.png">

## 📏 ERD
<img width="1015" alt="image" src="https://user-images.githubusercontent.com/41960243/184529686-2355dbd1-f5df-45c5-9c70-96057ad870e7.png">

## Git 브랜치 전략
<img width="706" alt="image" src="https://user-images.githubusercontent.com/41960243/184529886-5e033677-8b32-4737-ac1c-f900ab0462f6.png">

- main 브랜치 : 운영 서버에 배포될때 사용하는 브랜치입니다. 스프린트 단위로 배포가 됩니다.
- develop 브랜치 : 개발 서버에 배포될때 사용하는 브랜치입니다. 코드를 작성할 때마다 develop 브랜치에 해당 내용을 merge합니다.
- feat 브랜치 : 특정 기능을 구현할때 사용하는 브랜치입니다. 예를들어 게시글 작성같은 경우 feat 브랜치에서 작업한 후 develop에 merge하게 됩니다.

## 📜 API 문서
- [**책모이 API 문서**](https://checkmoi.ga/docs/index.html)

## 회고