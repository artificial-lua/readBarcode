# ReadBarcode
정확한 설명은 Documents 디렉토리를 참고하도록 합시다.  
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
1. 생활적 부분 지원을 위한, Custom QRcode 생성
    - Application : 원하는 String을 QRcode로 제작  
    String은 몇가지 부분으로 나뉘며, 전반부에 Custom QRcode인지 판단하는 Header정보 포함

# Application
## Workflow
![Workflow](./Documents/Application_Workflow.png)


# Server
서버는 이후 확장성을 위해 Node.js기반으로 작성합니다.


# DBMS
DBMS는 MariaDB를 사용합니다.