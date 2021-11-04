# ReadBarcode
## <strong>주 기능</strong>
1. 바코드를 스캔하고, 해당 내용을 DB에서 검색하여 TTS로 읽는 동작
    - Apllication : 바코드를 스캔  
    해당 내용을 서버에 전송  
    Result String을 TTS로 읽음
    - Server
1. 바코드의 내용이 다를 경우, 해당 정보를 수정하는 기능 동작
    - Application : 정보가 사실과 다를 경우, 수정하는 내용을 포함하여 서버에 전송
    - Server : 수정되는 정보의 카테고리 등으로 중복 바코드 등을 체크  
    빅 데이터로 Client의 신뢰성 검증


# Server
서버는 이후 확장성을 위해 Node.js기반으로 작성하였습니다.
main.js, mariadb-parser.js, mariadb-connector.js 세 부분으로 이루어져 있습니다.
main.js에서는 express 모듈을 사용한 REST 서버 실행을 합니다.
접근 url에 따라 알맞은 동작을 실행합니다.
알맞은 동작이란, mariadb-parser.js에 작성된 모듈을 사용함을 의미합니다. 
mariadb-parser.js는 mariadb-connector.js를 통해 접속된 db에 대한 질의와 그 해석 및 가공을 담당하고있습니다.
마지막으로, 서버에 사용되는 설정은 config.json 파일을 통해 저장되게 됩니다.
포트, url, db 호스트, id, password등을 담고 있습니다.



# DBMS
DBMS는 MariaDB를 사용합니다.
DBMS의 구조는 이러합니다.

## 테이블
 - user group
    유저 그룹에 관한 테이블
    1. admin
    2. 등록된 유저
    3. 비등록 유저
 - user information
    유저 정보에 관한 테이블
 - barcode rawdata
    가공되지 않은 바코드 정보
 - barcode rating
    가공되지 않은 바코드에 대한 유저 평가 정보
 - processed data
    가공된 바코드 정보
 - processed rating
    가공된 바코드에 대한 유저 평가 정보