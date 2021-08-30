# DB 작성에 대한 스크립트

DB에서 저장해야 할 내용은 아래와 같습니다.  
## User Information  
└ User Hash ( 유저 개인에게 배정되는 Hash값입니다. 이를 어디서 얻어올지는 미정이나, 구글 계정에 연동하는것이 괜찮겠다 생각합니다. )  
└ Language ( 유저가 사용하는 언어 코드입니다. )  
└ Edit log ( 유저가 직접 작성한 바코드 정보에 대한 로그입니다. 이는 유저 신뢰성 검증을 위해 필요합니다. )  
└ Sigh up date ( 유저가 처음 등록된 날짜입니다. )  
## Barcode Rawdata  
└ Code ( 바코드 Rawdata입니다. )  
└ Location ( 바코드 중복 검증을 위한 위치 정보입니다. 혹시 바코드가 같더라도, 위치정보 검증을 통해 상황에 맞는 데이터를 가져올 수 있을것입니다. )  
└ User Hash ( 등록한 유저의 Hash정보입니다. 악의성 검증을 위해 필요합니다. )  
└ Date Time ( 등록한 시간입니다. 어디서든 동일한 시간을 기준으로 작성하여야 하며, 개인별 시간 차는 클라이언트 단에서 보정해야 합니다. )  
└ Data title( TTS로 읽어줄 주 타겟입니다. )  
└ Data Descript ( 데이터에 대한 설명입니다. )  
└ Thumbup ( 검증받은 횟수입니다. )  
## Barcode DB
└ Code ( 바코드 Rawdata 입니다. )  
└ title ( TTS로 읽어줄 주 타겟입니다. )  
└ Descript ( 설명입니다. )  