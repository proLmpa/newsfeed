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
