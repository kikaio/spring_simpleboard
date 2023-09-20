# spring_simpleboard
spring boot로 만드는 간단한 게시판.

================================================================
개요
 . 기본적인 SpringBoot, Spring Security, Spring Data JPA 숙달
 . 전반적인 게시판의 기본 구성 개발
 . 1차 포트폴리오 프로젝트
 . 개발 인원 : 1명

================================================================
개발 환경
 . 사용 언어 : JAVA
 . spring boot 
 . spring Data JPA
 . spring Security
 . visual studio code
 . git
 . H2
 . view template : mustache

================================================================
주요 기능
 . 회원
  -> sign up
   -> 일반 security
   -> OAuth
   -> 기타 로그인 [추가 목표]
  -> sign in
   -> 일반 security
   -> OAuth
   -> 기타 로그인 [추가 목표]
  -> sign out
   -> 일반 security
   -> OAuth
   -> 기타 로그인 [추가 목표]
  -> quit 
   -> 일반 security
   -> OAuth
   -> 기타 로그인 [추가 목표]
 . 게시판
 . board
  -> 기본적인 CRUD
 . post
  -> 기본적인 CRUD
  -> paging
 . comment
  -> 기본적인 CRUD
  -> paging

================================================================  
기능별 상세 : 회원 인증 관련 [authentication]
. 회원 인증의 경우 가입, 로그인, 로그아웃, 탈퇴으로 이루어진다.
세부적인 방법은 기본적인 security로 인증처리를 진행하는 것과 OAuth을 사용하여 진행하는 것으로 구성된다.
 . sign up
  -> spring security
  : email, password 를 사용하여 member 정보 저장, sign up 
  -> OAuth
  : 구글 계정을 사용하여 sign up
  
 . sign in
  -> spring security
  : email, password를 사용하여 member 정보 확인, sign in
  -> OAuth
  : 구글 계정을 사용하여 sign in
  
 . sign out
  -> spring security
  : 단순 sign out 처리
  -> OAuth
  : 구글 계정 사용하여 sign out
 . quit
  -> 회원 탈퇴 시 password 일치여부 확인 및 지정된 문자열 입력 후 회원탈퇴 처리 진행.
 
 . member 정보
  -> 계정 : email 로 구분
  -> password : BCrypt 활용하며 password 암호화 하여 저장.
  -> 추가적인 프로필 정보[추가 목표]


기능별 상세 : 게시판
. 게시판은 게시판 명을 지니는 항목이며 여러개의 게시글을 포함한다.
게시판의 이름은 게시판을 생성할 때 지정하며 게시판 수정 기능에서도 게시판 이름을 변경할 수 있다.
게시판의 생성 및 변경은 관리자 만 가능하며 일반 회원 및 비회원은 일절 관여할 수 없다.
게시판을 삭제하는 경우 해당 게시판에 속한 게시글, 그 게시글에 속한 댓글들은 모두 삭제된다.
 . 게시판 생성
  -> 게시글들을 포함할 수 있는 게시판을 생성
   -> 게시판 생성의 경우 ROLE_ADMIN 만 가능.
  -> 게시판 생성 시 필요 정보
  : 게시판 명
 . 게시판 수정
  -> 게시판의 정보를 수정
   -> 게시판 수정의 경우 ROLE_ADMIN 만 가능.
  : 게시판 명
 . 게시판 제거
  -> 게시판과 관련된 모든 정보 게저
   -> 게시판 제거의 경우 ROLE_ADMIN 만 가능.
  : 해당 게시판 제거
  : 게시판 속 모든 게시글 제거
  : 해당 게시글들에 대한 모든 댓글 제거
 . 특정 등급만 작성할 수 있도록 제한 추가 [추가 목표]
  
기능별 상세 : 게시글
. 특정 게시판에 속한 게시글을은 모든 유저들이 "조회"할 수 있다.
조회 시 server에서 지정한 paging 갯수에 따른 page로 제공된다.
모든 ROLE_USER는 게시글을 "작성"할 수 있는 "권한"을 지닌다.
해당 권한을 지닌 유저는 게시글을 작성할 수 있고 자신이 작성한 게시글에 대해서만 "수정"을 할 수 있다.
ADMIN을 제외하면 본인이 작성한 게시글에 대해서만 "삭제"를 할 수 있다.
각 게시글은 조회수를 지니며 cdate와 mdate를 노출한다.(혹은 둘중 하나만)
 . 게시글 조회
  -> 게시글의 조회에는 별도 제한을 두지 않는다.
   -> 비밀 게시글[추가 목표]
 . 게시글 생성
  -> 해당 게시글이 게시될 게시판을 선택한다.
  -> 해당 게시판에 게시글을 작성한다.
   -> 생성의 경우 ROLE_ADMIN, ROLE_USER 만 가능하다.
  -> 작성, 수정, 삭제는 ADMIN, USER 만 가능하다.
   -> 수정 및 삭제의 경우 ROLE_ADMIN 혹은 ROLE_USER 등급의 게시글 작성자만 가능하다.
 . 게시글 수정
  -> 해당 게시글을 선택하여 수정
   -> 제목 및 내용 등 수정후에 저장.
  -> 해당 게시글의 수정은 ROLE_ADMIN, ROLE_USER 작성자만 가능하다.
 . 게시글 삭제
  -> 해당 게시글을 삭제
   -> 게시글에 속한 댓글들도 삭제된다.
   -> 게시글의 삭제는 ROLE_ADMIN, ROLE_USER 작성자만 가능하다.

기능별 상세 : 댓글
. 댓글은 특정 게시판에 종속되는 항목이다.
모든 댓글은 모든 회원에게 "조회" 될 수 있다.
댓글은 게시글이 지워질 때 같이 "삭제"된다.
각 댓글의 작성은 댓글 작성 "권한"을 지닌 모든 유저가 가능하다.
댓글의 "수정", "삭제"는 작성한 본인만 가능하다.
 . 댓글 조회
  -> 댓글의 조회에는 별도 제한을 두지 않는다.
 . 댓글 생성
  -> 댓글의 작성은 ROLE_ADMIN, ROLE_USER 만 가능하다.
 . 댓글 수정
  -> 댓글의 수정은 ROLE_ADMIN, ROLE_USER 작성자만 가능하다.
 . 댓글 삭제
  -> 댓글의 삭제는 ROLE_ADMIN, ROLE_USER 작성자만 가능하다.
 . 대댓글[추가 목표]
  -> 댓글에 대댓글을 달 수 있다. [단 depth는 1로 제한을 한다.]
 . 댓글 좋아요 [추가 목표]
  -> 각 댓글은 좋아요를 받을 수 있고 댓글 옆에 좋아요 수가 노출된다.
  -> 댓글의 작성자가 본인의 댓글에 좋아요를 하는 것은 제한 된다.  
  -> 좋아요를 했던 댓글에 좋아요를 다시 취소할 수 있다.
기능별 상세 : 각 Role
. 역할은 유저의 역할에 따른 대분류를 나타내며 한 계정은 복수의 Role을 지닐 수 있다.
 . ADMIN
  -> 관리자 역할
 . USER
  -> service 사용자 역할
  
기능별 상세 : 각 Role 별 Authority
. 각 권한은 특정 Role에 종속되며 어떤 역할인지에 따라 구성이 달라 질 수 있다.
각 권한은 여러 Role에 동시에 포함될 수 있다.
 . ADMIN의 Authorities
  -> BAORD 관련
    -> BOARD_CREATE : 게시판 생성 권한
    -> BOARD_READ : 게시판 조회 권한
    -> BOARD_UPDATE : 게시판 수정 권한
    -> BOARD_DELETE : 게시판 삭제 권한
 . USER의 Authorities
  -> BOARD 관련
    -> BOARD_READ : 게시판 조회 권한
  -> POST 관련
    -> POST_CREATE : 게시글 생성 권한
    -> POST_READ : 게시글 조회 권한
    -> POST_UPDATE : 게시글 수정 권한
    -> POST_DELETE : 게시글 삭제 권한
  -> COMMENT 관련
    -> COMMENT_CREATE : 댓글 생성 권한
    -> COMMENT_READ : 댓글 조회 권한
    -> COMMENT_UPDATE : 댓글 수정 권한
    -> COMMENT_DELETE : 댓글 삭제 권한

=============================================
각 Entity 설계

member : 회원 정보
 . id
 . email
 . password
 . mdate
 . cdate
 . list - role
 . list - authority
 
board : 게시판
 . id
 . name
 
post : 게시글
 . id
 . board
 . member
 . title
 . content
 . cdate
 . mdate

comment : 댓글
 . id
 . member
 . post
 . comment
 . cdate
 . mdate
 
role : 역할
 . id
 . name
 . list - authority
 
authority : 권한
 . id
 . name
 . list - role

=============================================
각 Entity 별 상관관계 정의