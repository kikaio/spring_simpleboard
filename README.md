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
   -> OAuth [추가 목표]
   -> 기타 로그인 [추가 목표]
  -> sign in
   -> 일반 security
   -> OAuth [추가 목표]
   -> 기타 로그인 [추가 목표]
  -> sign out
   -> 일반 security
   -> OAuth [추가 목표]
   -> 기타 로그인 [추가 목표]
  -> quit 
   -> 일반 security
   -> OAuth [추가 목표]
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
  -> OAuth [추가 목표]
  : 구글 계정을 사용하여 sign up
  
 . sign in
  -> spring security
  : email, password를 사용하여 member 정보 확인, sign in
  -> OAuth [추가 목표]
  : 구글 계정을 사용하여 sign in
  
 . sign out
  -> spring security
  : 단순 sign out 처리
  -> OAuth [추가 목표]
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
. 모든 DateTime 은 UTC 기준으로 삽입 할 예정.
 -> 
 
member : 회원 정보
 . id : Long
 . email : String
 . password : String
 . mdate : LocalDateTime ?
 . cdate : LocalDateTime ?
 . roles [list - role] : Long [column name : role_id]
 
board : 게시판
 . id : Long
 . name : String
 
post : 게시글
 . id : Long
 . board : Long [column name : board_id]
 . member : Long [column name : member_id]
 . title : String
 . content : String
  => @Lob 사용
 . cdate : LocalDateTime ?
 . mdate : LocalDateTime ?

comment : 댓글
 . id : Long
 . member : Long [column name : member_id]
 . post : Long [column name : post_id]
 . comment : String
 . cdate : LocalDateTime ? 
 . mdate : LocalDateTime ? 
 
role : 역할
 . id : Long 
 . name : String
 . authorities [list - authority] : Long [column name : authority_id]
 
authority : 권한
 . id : Long
 . name : String
 . roles [list - role] : Long [column name : role_id]

=============================================
각 Entity 별 상관관계 정의

각 Role은 여러 Authority를 보유할 수 있다.
한 개의 Authority는 여러 role에 포함 될 수 있다.
 -> role과 athority : oneToMany
 -> authority와 role : oneToMany
 => ManyToMany ?
 관계 주체 : role로 지정

member는 여러 Role을 지닐 수 있다.
한 개의 Role은 여러 User에 포함될 수 있다.
 -> member와 role : oneToMany
 -> role과 member : oneToMany
 => ManyToMany ?
 관계 주체 : member로 지정

member 는 여러 post를 작성할 수 있다
한 개의 post는 단 하나의 member만 지닌다.
 -> member와 post : oneToMany
 -> post와 member : ManyToOne
 => ManyToOne : post가 Many
 관계 주체 : member로 지정
 
member는 여러 comment를 작성 할 수 있다.
한개의 comment는 하나의 member만 지닌다.
 -> comment와 member : ManyToOne
 -> member와 comment : OneToMany
 => ManyToOne : comment가 Many
 관계 주체 : member로 지정
 
post는 여러 comment를 보유할 수 있다.
한개의 comment는 하나의 post만 지닐 수 있다.
 -> post와 comment : oneToMany
 -> comment와 Post : ManyToOne
 => ManyToOne : comment가 Many
 관계 주체 : post로 지정.
 
================================================
service 개요 : member
 - member의 조회, 가입, 로그인, 로그아웃, 탈퇴를 담당한다.
 - 각 member의 role에 대한 조회, 생성, 수정, 삭제를 담당한다.
 - 각 role에 속한 authority에 대해서 조회, 생성, 수정, 삭제를 담당한다.
 
service 개요 : board
 - board의 조회, 생성, 수정, 삭제을 담당한다.
 
service 개요 : post
 - post에 대한 조회, 생성, 수정, 삭제를 담당한다.
 - post에 속할 수 있는 comment에 대해서 조회, 생성, 수정, 삭제를 담당한다.

===============================================
각 [member]service에 따른 url 과 method 구조

[member]
 - member 목록 조회  : GET , ~/members
 - member 조회 : GET , ~/members/{id}
 - member 생성 : POST ,  ~/members/create
 - member 수정 : POST ,  ~/members/{id}
 - member 삭제 : DELETE , ~members/{id}
 - sign up : member create 와 동일
 - sign in : POST , ~/members/sign-in
 - sign in process : GET , ~/members/sign-in-process
 - sign out : POST , ~/members/sign-out

[Role]
 - role 목록 조회 : GET , ~/roles
 - role 상세 조회 : GET , ~/roles/{id}
 - role 생성 : POST , ~/roles/create
 - role 수정 : POST , ~/roles/{id}
 - role 삭제 : DELETE , ~/roles/{id}

[Authority]
 - authority 목록 조회 : GET , ~/authorities
 - authority 상세 조회 : GET , ~/authorities/{id}
 - authority 생성 : POST , ~/authorities/create
 - authority 수정 : POST , ~/authorities/{id}
 - authority 삭제 : DELETE , ~/authorities/{id}
 
===================================================
각 [baord] service에 따른 url 과 method 구조
[board]
 - board 목록 조회 : GET , ~/boards
 - board 상세 조회 : GET , ~/boards/{id}
  -> 해당 board 상세 조회 시 게시판 명과 해당 board에 속한 post 목록을 전달해줘야함.
   -> board, page, 필요 시 검색 키워드 적용[추가 목표]
 - board 생성 : POST , ~/boards/create
 - board 수정 : POST , ~/boards/{id}
 - board 삭제 : DELETE , ~/boards/{id}

===================================================
각 [post]service 에 따른 url 과 method 구조
[post]
 - post 상세 조회 : GET , ~/posts/{id}
 - post 생성 : POST , ~/posts/create
 - post 수정 : POST , ~/posts/{id}
 - post 삭제 : DELETE , ~/posts/{id}
 - post like 처리[추가 목표] : POST , ~/posts/{id}/like
 - post like 취소[추가 목표] : DELETE , ~/posts/{id}/like
 
[comment]
 - comment 생성 : POST , ~/comments/create
 - comment 수정 : POST , ~/comments/{id}
 - comment 삭제 :  DELETE , ~/comments/{id}
 - comment like 처리[추가 목표] : POST , ~/comments/{id}/like
 - comment like 취소[추가 목표] : DELETE , ~/comments/{id}/like
 
===================================================
추가 목표 중 우선순위
. OAuth 적용
. 추가적인 member profile 기능
 -> profile 이미지 지정 및 인삿말 정도?
. Board 검색 기능
. Post like 기능
. Comment 에 Comment 달기 기능.
. Comment like 기능
 -> 회원 등급 분류 및 지정 기능.
. 게시판에 특정 등급만 작성 가능하도록 제한 기능
. 게시판에 주딱 파딱 지정 기능
 -> board manager 지정
. 익명 게시판 기능.

. 기타 서비스 인증 로그인.