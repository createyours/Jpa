SET SESSION FOREIGN_KEY_CHECKS=0;

/* Drop Tables */
SET GROUP_CONCAT_MAX_LEN=32768;
SET @tables = NULL;
SELECT GROUP_CONCAT('`', table_name, '`') INTO @tables
  FROM information_schema.tables
  WHERE table_schema = (SELECT DATABASE());
SELECT IFNULL(@tables,'dummy') INTO @tables;

SET @tables = CONCAT('DROP TABLE IF EXISTS ', @tables);
PREPARE stmt FROM @tables;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET FOREIGN_KEY_CHECKS = 1;
-- Project Name : No Golf No Life
-- Date/Time    : 2018-10-20 14:37:34
-- Author       : Leading
-- RDBMS Type   : MySQL
-- Application  : A5:SQL Mk-2

/*
  BackupToTempTable, RestoreFromTempTable疑似命令が付加されています。
  これにより、drop table, create table 後もデータが残ります。
  この機能は一時的に $$TableName のような一時テーブルを作成します。
*/

-- コードマスタ
-- * RestoreFromTempTable
create table CodeMaster (
  CodeType VARCHAR(20) not null comment 'コードタイプ'
  , CodeTypeName VARCHAR(256) not null comment 'コードタイプ名称'
  , Code CHAR(20) not null comment 'コード'
  , CodeName VARCHAR(256) not null comment 'コード名称'
  , DelFlg CHAR(1) not null comment '削除フラグ:0：未削除 1：削除済'
  , DispOrder INT comment '表示順'
  , constraint CodeMaster_PKC primary key (CodeType,Code)
) comment 'コードマスタ:コードマスタテーブル' ;

-- 一時テーブル
-- * RestoreFromTempTable
create table Temporary (
  ID CHAR(20) not null comment 'ID'
  , type VARCHAR(20) not null comment 'タイプ'
  , TemporaryName VARCHAR(50) not null comment '一時名'
  , TemporaryValue VARCHAR(256) comment '一時値'
  , InsTstmp CHAR(17) not null comment '作成日時'
  , expire CHAR(17) not null comment '有効期限'
  , constraint Temporary_PKC primary key (ID,type)
) comment '一時テーブル' ;

create unique index IAI_TEMPORARY_TOKEN_UKC
  on Temporary(TemporaryName);

-- 会員付属情報
-- * RestoreFromTempTable
create table MemberOptionInfo (
  MemberID CHAR(20) not null comment '会員ID'
  , Type VARCHAR(50) not null comment 'タイプ:0001:好みのゴルフ場'
  , seq INT not null comment '順番'
  , Club VARCHAR(256) comment 'ゴルフ場１'
  , constraint MemberOptionInfo_PKC primary key (MemberID,Type,seq)
) comment '会員付属情報:予定用テーブル' ;

-- 会員基本情報
-- * RestoreFromTempTable
create table MemberBaseInfo (
  MemberID CHAR(20) not null comment '会員ID'
  , EfctYmd CHAR(8) comment '入会日'
  , ExprYmd CHAR(8) comment '退会日'
  , InsTstmp CHAR(17) comment '作成日時'
  , constraint MemberBaseInfo_PKC primary key (MemberID)
) comment '会員基本情報' ;

-- 応募情報
-- * RestoreFromTempTable
create table ApplyInfo (
  RoundSerialNo INT not null comment '募集ID'
  , MemberID CHAR(10) not null comment '応募者ID'
  , RegNo INT not null comment '受付通番'
  , Comments VARCHAR(256) comment '主催者へのコメント'
  , CancelFlag CHAR(1) comment 'キャンセルフラグ'
  , WaitFlag CHAR(1) comment 'キャンセル待ちフラグ'
  , ChatContents VARCHAR(256) comment 'チャット内容:応募者と主催者の間でチャットする内容（チャットを提供してから使う予備フィールド）'
  , ApovStatus CHAR(1) comment '承認ステータス'
  , constraint ApplyInfo_PKC primary key (RoundSerialNo,RegNo)
) comment '応募情報' ;

-- 募集情報
-- * RestoreFromTempTable
create table RecruitInfo (
  RoundSerialNo INT not null comment '募集ID:通番'
  , MemberID CHAR(10) not null comment '主催者ID'
  , CourseID CHAR(10) comment 'コースID'
  , CourseName VARCHAR(256) not null comment 'コース名'
  , StartCourse INT not null comment 'スタートコース:Out/In'
  , PlayDate CHAR(8) not null comment 'プレイ日'
  , StartTime CHAR(17) not null comment 'スタート時間:セルフ／キャディ付き'
  , PlayStyle VARCHAR(256) not null comment 'プレースタイル'
  , PlayFee INT not null comment 'プレー料金:総額'
  , LunchFlag INT not null comment 'ランチ含む'
  , RoundDetails VARCHAR(256) comment '予約詳細'
  , RecruitNum INT not null comment '募集人数:1-3'
  , RecruitRange INT default 1 comment '募集範囲:1：一般、2：友人、3：親友'
  , Coments VARCHAR(256) comment '参加者へのコメント'
  , CloseDate CHAR(17) comment '締切日時'
  , PushFlag CHAR(1) comment '通知希望フラグ:募集範囲が2、3の場合のみ適用'
  , Status INT comment '募集進行状況:0-3：残り人数
-1：主催者がキャンセルした
-2：募集締切（時間になったか、主催者が手動で）'
  , RegDate CHAR(17) comment '登録日時'
  , constraint RecruitInfo_PKC primary key (RoundSerialNo)
) comment '募集情報' ;

-- コース情報
-- * RestoreFromTempTable
create table CourseInfo (
  ID VARCHAR(20) not null comment 'コースID'
  , Name VARCHAR(256) comment 'コース名'
  , `Name-k` VARCHAR(256) comment 'かな名'
  , `Name-e` VARCHAR(256) comment '英語名'
  , State VARCHAR(256) comment '都道府県'
  , Address VARCHAR(256) comment '住所'
  , Tel VARCHAR(256) comment '電話番号'
  , Longitude FLOAT comment '経度'
  , Latitude FLOAT comment '緯度'
  , Caption VARCHAR(256) comment '説明'
  , Type INT comment 'コースタイプ:山岳、丘陵、林間など'
  , Point1 FLOAT comment '評価１:楽天評価'
  , Point2 FLOAT comment '評価２:GDO評価'
  , Point3 FLOAT comment '評価３:その他評価'
  , Other VARCHAR(256) comment 'その他'
  , constraint CourseInfo_PKC primary key (ID)
) comment 'コース情報:ゴルフ場基本情報' ;

-- 友人情報
-- * RestoreFromTempTable
create table FrendInfo (
  MemberID CHAR(10) not null comment '会員ID'
  , FriendID CHAR(10) not null comment '友人ID'
  , seq INT not null comment '順番'
  , Level INT not null comment '友人レベル'
  , StartDate CHAR(8) comment '年月日'
  , DelFlag INT comment '削除フラグ'
  , constraint FrendInfo_PKC primary key (MemberID,FriendID,seq)
) comment '友人情報' ;

-- SNS情報
-- * RestoreFromTempTable
create table SNSInfo (
  MemberID CHAR(20) not null comment '会員ID'
  , SNSID VARCHAR(100) not null comment 'SNSID'
  , SNSType INT not null comment 'タイプ:FB, LINE'
  , AuthFlag INT comment '認証フラグ'
  , constraint SNSInfo_PKC primary key (MemberID,SNSID,SNSType)
) comment 'SNS情報' ;

-- 会員情報
-- * RestoreFromTempTable
create table MemberInfo (
  MemberID CHAR(20) not null comment '会員ID'
  , Email VARCHAR(256) not null comment 'メールアドレス:メールアドレス認証してからレコードを生成する'
  , Password VARCHAR(256) not null comment 'パスワード:ログインに利用するパスワードを暗号化して格納する'
  , Nickname VARCHAR(256) not null comment '表示名'
  , Name VARCHAR(256) comment '氏名'
  , Kana VARCHAR(256) comment 'カナ名'
  , Sex CHAR(1) comment '性別'
  , Birthday VARCHAR(8) comment '生年月日'
  , PlayYears INT comment 'ゴルフ年数'
  , AverageScore INT comment '平均スコア'
  , PlayStyle INT comment 'スタイル:エンジョイなど'
  , Notes VARCHAR(500) comment '自己紹介'
  , TelNo VARCHAR(40) comment '携帯番号'
  , Country VARCHAR(256) comment '国'
  , State VARCHAR(256) comment '都道府県'
  , Address VARCHAR(256) comment '住所:予備'
  , Gear1 VARCHAR(256) comment '使用するドライバー:予備'
  , Gear2 VARCHAR(256) comment '使用するアイアン:予備'
  , Gear3 VARCHAR(256) comment '使用するパター:予備'
  , Area1 VARCHAR(256) comment 'よく行くゴルフ場の県１'
  , Area2 VARCHAR(256) comment 'よく行くゴルフ場の県２'
  , Club1 VARCHAR(256) comment '好みのゴルフ場１'
  , Club2 VARCHAR(256) comment '好みのゴルフ場２'
  , Club3 VARCHAR(256) comment '好みのゴルフ場３'
  , Club4 VARCHAR(256) comment '好みのゴルフ場４'
  , Club5 VARCHAR(256) comment '好みのゴルフ場５'
  , Day1 VARCHAR(256) comment 'ゴルフできる曜日１'
  , Day2 VARCHAR(256) comment 'ゴルフできる曜日２'
  , Day3 VARCHAR(256) comment 'ゴルフできる曜日３'
  , PushFlag CHAR(1) default 1 comment '通知フラグ:0：OFF、1：ON'
  , constraint MemberInfo_PKC primary key (MemberID)
) comment '会員情報' ;
