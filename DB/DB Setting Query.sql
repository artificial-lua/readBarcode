# 전처리
DROP DATABASE ReadBarcode;

# DB생성 및 사용
CREATE DATABASE ReadBarcode;
use ReadBarcode;

# 유저그룹 생성
CREATE TABLE user_group
(
    `num`   INT            NOT NULL    AUTO_INCREMENT COMMENT 'User_Group', 
    `name`  VARCHAR(45)    NULL        COMMENT 'Group_Name', 
    CONSTRAINT PK_user_group PRIMARY KEY (num)
);

ALTER TABLE user_group COMMENT 'User Group';

# 유저 그룹 1은 Admin
INSERT INTO user_group
    (name)
VALUES
    ('Admin');


# 유저 그룹 2는 일반 유저
INSERT INTO user_group
    (name)
VALUES
    ('User');


# 유저 그룹 3은 익명 유저
INSERT INTO user_group
    (name)
VALUES
    ('Unknown');




# 유저 정보 테이블 생성
CREATE TABLE user_information
(
    `num`     INT            NOT NULL    AUTO_INCREMENT COMMENT 'User_Number', 
    `id`      VARCHAR(45)    NOT NULL    COMMENT 'User_ID', 
    `signup`  TIMESTAMP      NOT NULL    COMMENT 'Signup_date', 
    `group`   INT            NULL        COMMENT 'User_Group', 
    `hash`    VARCHAR(256)    NULL        COMMENT 'User_Hash', 
    CONSTRAINT PK_user_information PRIMARY KEY (num),
    FOREIGN KEY (`group`) REFERENCES `user_group` (`num`)
);

ALTER TABLE user_information COMMENT 'User Information';

CREATE UNIQUE INDEX UQ_information_1
    ON user_information(id);



# 바코드 등록된 기본 데이터 테이블 생성
CREATE TABLE barcode_rawdata
(
    `id`        INT            NOT NULL    AUTO_INCREMENT COMMENT 'Barcode_ID', 
    `raw`       VARCHAR(45)    NULL        COMMENT 'Barcode_raw', 
    `user_num`  INT            NULL        COMMENT 'User_Number', 
    `time`      TIMESTAMP      NULL        COMMENT 'Date_Time', 
    `title`     VARCHAR(45)    NULL        COMMENT 'Barcode_title', 
    `des`       VARCHAR(45)    NULL        COMMENT 'Descript', 
    CONSTRAINT PK_barcode_rawdata PRIMARY KEY (id),
    FOREIGN KEY (`user_num`) REFERENCES `user_information` (`num`)
);

ALTER TABLE barcode_rawdata COMMENT 'Barcode Rawdata';



# rawdate에 대한 평가 테이블
CREATE TABLE barcode_rating
(
    `num`       INT          NOT NULL    AUTO_INCREMENT COMMENT 'rating_Number', 
    `raw_id`    INT          NULL        COMMENT 'Raw_ID', 
    `user_num`  INT          NULL        COMMENT 'User_Number', 
    `rating`      BIT          NULL        COMMENT 'Raw_rating', 
    `time`      TIMESTAMP    NULL        COMMENT 'Date_Time', 
    CONSTRAINT PK_barcode_rating PRIMARY KEY (num),
    FOREIGN KEY (`raw_id`) REFERENCES `barcode_rawdata` (`id`),
    FOREIGN KEY (`user_num`) REFERENCES `user_information` (`num`)
);

ALTER TABLE barcode_rating COMMENT 'Barcode rating';



# 처리된 데이터에 대한 테이블
CREATE TABLE processed_data
(
    `id`        INT            NOT NULL    AUTO_INCREMENT COMMENT 'Barcode_ID', 
    `raw_id`    INT            NULL        COMMENT 'Raw_ID', 
    `user_num`  INT            NULL        COMMENT 'User_Number', 
    `time`      TIMESTAMP      NULL        COMMENT 'Date_Time', 
    `title`     VARCHAR(45)    NULL        COMMENT 'Barcode_title', 
    `des`       VARCHAR(45)    NULL        COMMENT 'Descript', 
    `good`      INT            NULL        COMMENT 'rating_Good', 
    `bad`       INT            NULL        COMMENT 'rating_Bad', 
    CONSTRAINT PK_processed_data PRIMARY KEY (id)
);

ALTER TABLE processed_data COMMENT 'Processed Barcode Data';

ALTER TABLE processed_data
    ADD CONSTRAINT FK_processed_data_raw_id_barcode_rawdata_id FOREIGN KEY (raw_id)
        REFERENCES barcode_rawdata (id) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE processed_data
    ADD CONSTRAINT FK_processed_data_user_num_user_information_num FOREIGN KEY (user_num)
        REFERENCES user_information (num) ON DELETE RESTRICT ON UPDATE RESTRICT;



# 처리된 데이터에 대한 평가
CREATE TABLE processed_rating
(
    `num`         INT          NOT NULL    AUTO_INCREMENT COMMENT 'rating_Number', 
    `barcode_id`  INT          NULL        COMMENT 'Barcode_ID', 
    `user_num`    INT          NULL        COMMENT 'User_Number', 
    `rating`        BIT          NULL        COMMENT 'rating', 
    `time`        TIMESTAMP    NULL        COMMENT 'Date_Time', 
    CONSTRAINT PK_processed_rating PRIMARY KEY (num)
);

ALTER TABLE processed_rating COMMENT 'Processed Barcode rating';

ALTER TABLE processed_rating
    ADD CONSTRAINT FK_processed_rating_barcode_id_processed_data_id FOREIGN KEY (barcode_id)
        REFERENCES processed_data (id) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE processed_rating
    ADD CONSTRAINT FK_processed_rating_user_num_user_information_num FOREIGN KEY (user_num)
        REFERENCES user_information (num) ON DELETE RESTRICT ON UPDATE RESTRICT;
