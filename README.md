# ERD 설계
![스크린샷(203)](https://github.com/proLmpa/newsfeed/assets/52267654/e758bf8a-64bf-4880-a69b-e5a9a7b9ce74)

# API 설계

1. User & Profile API

|API 명|Method|URL|Request Cookie|Request|Response|Response Cookie|
|:---:|:---:|:---:|:---:|:---:|:---:|:---:|
|회원가입|POST|api/user/signup||{"id": "part", "password": "partial123”, “email”: “user@email.com”}|{"msg": "SUCCESS_SIGN_UP", "statusCode": 200}||
|로그인|POST|api/user/login||{"id": "part", "password": "partial123”}|{"msg": "SUCCESS_LOGIN", "statusCode": 200}|Cookie : {"Authorization" : "Bearer... "}|
|Email 인증|POST|api/user/code||{@RequestParam "config": `인증번호 6자리}|"인증되었습니다"||
|프로필 보기|GET|api/user/profile|Cookie : {"Authorization" : "Bearer... "}||{ “id”: “part”, “username”: “KHY”, “introduction”: “hello”, “email”: “user@email.com }||
|프로필 수정|PUT|api/user/profile|Cookie : {"Authorization" : "Bearer... "}|{"username": "KHY", "introduction": "Hi~ :D"}|{"msg": "SUCCESS_PROFILE_EDIT", "statusCode": 200}||
|비밀번호 수정|PUT|api/user/password|Cookie : {"Authorization" : "Bearer... "}|{ “password”: “partial123”,“newPassword1”: “partial!@#”, ”newPassword2”: “partial!@#” }|{"msg": "SUCCESS_PASSWORD_EDIT", "statusCode": 200}||


2. Post API

|API 명|Method|URL|Request Cookie|Request|Response|Response Cookie|
|:---:|:---:|:---:|:---:|:---:|:---:|:---:|
|전체 게시글 조회|GET|api/post|||[ {"title": "title from part", "username": "part", "contents": "content from part", "createdAt": "2023-06-28T10:00:04.032185", "modifiedAt": "2023-06-28T10:00:04.032185", "commentList": [] },  {"title": "good", "username": "part", "contents": "no, bad", "createdAt": "2023-06-28T09:59:48.081672", "modifiedAt": "2023-06-28T10:00:56.025748", "commentList": [] } ]||
|선택 게시글 조회|GET|api/post/{id}|||{"title": "title from part", "username": "part", "contents": "content from part", "createdAt": "2023-06-28T10:00:04.032185", "modifiedAt": "2023-06-28T10:00:04.032185", "commentList": [] }||
|게시글 생성|POST|api/post|Cookie : {"Authorization" : "Bearer... "}|{"title": "title from part", "contents": "content from part"}|{"title": "title from part", "username": "part", "contents": "content from part", "createdAt": "2023-06-28T10:00:04.0321854", "modifiedAt": "2023-06-28T10:00:04.0321854", "commentList": [] }||
|게시글 수정|PUT|api/post/{id}|Cookie : {"Authorization" : "Bearer... "}|{"title": "good", "contents": "no, bad" }|{"title": "good", "username": "part", "contents": "no, bad", "createdAt": "2023-06-28T09:59:48.081672", "modifiedAt": "2023-06-28T10:00:56.0170722", "commentList": [] }||
|게시글 삭제|DELETE|api/post/{id}|Cookie : {"Authorization" : "Bearer... "}||{"msg": "SUCCESS_DELETE_POST", "statusCode": 200}||
|게시글 좋아요 등록|POST|api/post/{id}/like|Cookie : {"Authorization" : "Bearer... "}||{"msg": "SUCCESS_REGISTER_LIKE_IN_POST", "statusCode": 200}||
|게시글 좋아요 해제|DELETE|api/post/{id}/like|Cookie : {"Authorization" : "Bearer... "}||{{"msg": "SUCCESS_CANCEL_LIKE_IN_POST", "statusCode": 200}

3. Comment API

|API 명|Method|URL|Request Cookie|Request|Response|Response Cookie|
|:---:|:---:|:---:|:---:|:---:|:---:|:---:|
|댓글 생성|POST|api/comment|Cookie : {"Authorization" : "Bearer... "}|{"postId": 2, "content": "nice post"}|{"commentId": 1, "username": "part", "content": "nice post"}||
|댓글 수정|PUT|api/comment/{id}|Cookie : {"Authorization" : "Bearer... "}|{"content": "YKW? it was actually bad.."}|{"commentId": 1, "username": "part", "content": "YKW? it was actually bad.."}||
|댓글 삭제|DELETE|api/comment/{id}|Cookie : {"Authorization" : "Bearer... "}||{"msg": "SUCCESS_DELETE_COMMENT", "statusCode": 200}||
|댓글 좋아요 등록|POST|api/comment/{id}/like|Cookie : {"Authorization" : "Bearer... "}||{"msg": "SUCCESS_REGISTER_LIKE_IN_COMMENT", "statusCode": 200}||
|댓글 좋아요 해제|DELETE|api/comment/{id}/like|Cookie : {"Authorization" : "Bearer... "}||{{"msg": "SUCCESS_CANCEL_LIKE_IN_COMMENT", "statusCode": 200}

4. Follow API

|API 명|Method|URL|Request Cookie|Request|Response|Response Cookie|
|:---:|:---:|:---:|:---:|:---:|:---:|:---:|
|팔로우하기|POST|api/follow/{followingId}|Cookie : {"Authorization" : "Bearer... "}||{”msg”: “SUCCESS_FOLLOW_USER”, “statusCode”: 200}||
|언팔로우하기|POST|api/follow/{followingId}|Cookie : {"Authorization" : "Bearer... "}||{”msg”: “SUCCESS_UNFOLLOW_USER”, “statusCode”: 200}||
|팔로워 게시글 보기|GET|api/follow/{followingId}/post|Cookie : {"Authorization" : "Bearer... "}||{ "title": "title from part", "id": "part", "contents": "content from part", "createdAt": "2023-06-28T10:00:04.032185", "modifiedAt": "2023-06-28T10:00:04.032185", "commentList": [] }||
