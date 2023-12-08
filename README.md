
# 톡Talk💫
<br>

### 📑 프로젝트 개요
- Java Swing 및 Socket을 활용하여 구현된 멀티 채팅 프로그램
- 여러 사용자 간의 실시간 채팅을 지원하며 간단한 GUI를 통해 사용자들이 편리하게 대화할 수 있다.
<br>

### 💻 프로젝트 기능
- postgreSQL을 이용하여 사용자, 채팅 내역, 공지 사항, 이모티콘 정보를 관리한다.
- 회원가입
    - 아이디, 비밀번호, 닉네임을 입력하여 회원가입을 진행한다.
    - 아이디와 닉네임은 중복된 값을 허용하지 않는다.
- 로그인
    - 아이디와 비밀번호를 입력하여 로그인을 진행한다.
    - 관리자의 경우 ID : admin / PW : admin1234로 로그인한다.
    - 사용자의 경우 가입된 ID, PW로 로그인을 한다.
- 닉네임 설정
    - 채팅방에서는 아이디가 아닌 닉네임을 사용한다.
- 이모티콘
    - 이모티콘 목록에서 이모티콘을 선택하면 메시지 입력 창에 출력된다.
- Clear
    - 채팅방의 모든 내용을 지운다.
- 공지사항
    - 공지 메시지를 입력하여 공지사항을 등록한다.
    - 등록된 공지사항은 모두가 볼 수 있고 공지사항란에 이름 : 공지 메시지를 출력한다.
- 사용자 목록 확인
    - 현재 채팅방에 접속되어 있는 사용자 목록을 출력한다.
- 귓속말
    - 대상을 선택한 후 그 사람한테 메시지를 전송한다.
- 강제 퇴장
    - 관리자한테만 제공되는 기능이다.
    - 선택한 사람을 채팅방에서 강제 퇴장 시킨다.
- 가입된 모든 사용자 목록 확인
    - 관리자한테만 제공되는 기능이다.
    - 회원가입을 완료한 사용자의 목록을 출력한다.
    - 사용자마다 ID, 닉네임, 강퇴 횟수, 마지막 접속 일을 확인할 수 있다,
    - ID와 닉네임을 검색하여 해당하는 사람만 조회가 가능하다.
- 채팅 내역
    - 관리자한테만 제공되는 기능이다.
    - 채팅방의 내용을 확인할 수 있고 닉네임을 검색해 그 사람만의 기록 조회가 가능하다.

<br>

### 🌐 프로젝트 구성
- 패키지 구조<br><br>

![패키지구조_1](https://github.com/y-00jin/TalkTalk/assets/81798918/ef24ec0b-10d6-4ccd-88bd-9e62ae05528a)
<br>

![패키지구조_2](https://github.com/y-00jin/TalkTalk/assets/81798918/d93a11cc-7623-43f0-a0fd-9e454c05cfb9)


- 데이터베이스 설계 <br><br>
![DB구조](https://github.com/y-00jin/TalkTalk/assets/81798918/0ff62fc6-704b-4d3f-a194-15eeac7b89f0)
<br><br>

### ⚙️ 실행 방법
- StartServer 실행
    - 첫 실행 시 : DB.createTable() 주석 해제 후 실행 → 데이터베이스 테이블 생성
    - 테이블 준비가 완료된 경우 : DB.createTable() 주석 후 실행
- StartClient 실행
    - 관리자 ) ID : admin / PW : admin1234로 로그인 (관리자 계정은 테이블 생성 시 insert됨)
    - 사용자 ) 회원가입 후 로그인

<br>

### 👀 Language & Tools
<img src="https://img.shields.io/badge/Java-007396?style=flat&logo=java&logoColor=white">   <!-- 자바 -->
<img src="https://img.shields.io/badge/PostgreSQL-4169E1?style=flat&logo=postgresql&logoColor=ffffff"/>          <!-- PostgreSQL-->



<br>

### 🍀 Result
- 로그인<br><br>
![로그인](https://github.com/y-00jin/TalkTalk/assets/81798918/46d8bc5a-e899-4f5a-bfe1-ed50bb4adee3)
<br>

- 회원가입<br><br>
![회원가입](https://github.com/y-00jin/TalkTalk/assets/81798918/18914c7d-7bd7-487e-b9bc-27e7ba5accb2)
<br>

- 채팅방<br><br>
![채팅방](https://github.com/y-00jin/TalkTalk/assets/81798918/75d701b5-8728-4d3e-9af0-1ea05c232e9f)
<br>

- 이모티콘<br><br>
![이모티콘](https://github.com/y-00jin/TalkTalk/assets/81798918/fd6e07da-197f-46fc-8b70-bb2206c07afe)
<br>

- 접속 사용자 목록<br><br>
![사용자목록](https://github.com/y-00jin/TalkTalk/assets/81798918/ea8b18a9-5738-4c66-b220-dfb23fd693cd)
<br>

- 공지<br><br>
![공지입력창](https://github.com/y-00jin/TalkTalk/assets/81798918/0d123cf9-1097-4893-82a7-af44f42d376a)

![공지사항](https://github.com/y-00jin/TalkTalk/assets/81798918/3d12ef3e-cf67-4333-a1b4-20caf7b1dd1e)
<br>

- 관리자 메뉴<br><br>
![관리자메뉴](https://github.com/y-00jin/TalkTalk/assets/81798918/32b7cfb7-9697-4830-a48f-1e166f244424)
<br>

- 관리자 메뉴 - 사용자 목록<br><br>
![관리자메뉴_사용자목록](https://github.com/y-00jin/TalkTalk/assets/81798918/16501a0a-a268-4cbf-a5d6-c3155f41a2a9)
<br>

- 관리자 메뉴 - 채팅 기록<br><br>
![관리자메뉴_채팅기록](https://github.com/y-00jin/TalkTalk/assets/81798918/84d4a003-881b-4e25-9a82-b5c9588fd59a)
<br>


- 시연 영상<br><br>

https://github.com/y-00jin/TalkTalk/assets/81798918/01304a16-b019-42fd-b998-7b7abfea78ec









