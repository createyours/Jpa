package org.leadingsoft.golf.api.service;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.leadingsoft.golf.api.code.ResultCode;
import org.leadingsoft.golf.api.code.SexCode;
import org.leadingsoft.golf.api.entity.MemberInfo;
import org.leadingsoft.golf.api.entity.SNSInfo;
import org.leadingsoft.golf.api.entity.Temporary;
import org.leadingsoft.golf.api.model.DataResult;
import org.leadingsoft.golf.api.model.Profile;
import org.leadingsoft.golf.api.repository.MemberInfoRepository;
import org.leadingsoft.golf.api.repository.SNSinfoRepository;
import org.leadingsoft.golf.api.repository.TemporaryRepository;
import org.leadingsoft.golf.api.util.DateLogicUtils;
import org.leadingsoft.golf.api.util.IdGenerationLogic;
import org.leadingsoft.golf.api.util.PasswordUtils;
import org.leadingsoft.golf.api.util.RandomUtils;
import org.leadingsoft.golf.api.util.SendMailUtils;
import org.leadingsoft.golf.api.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
  private final static Logger logger = LoggerFactory.getLogger(LoginService.class);
  @Autowired
  private JdbcTemplate jdbcTemplate;
  @Autowired
  private SendMailUtils sendMailUtils;
  @Autowired
  private DateLogicUtils dateLogicUtils;
  @Autowired
  private MemberInfoRepository memberInfoRepository;
  @Autowired
  private SNSinfoRepository snsInfoRepository;
  @Autowired
  private TemporaryRepository temporaryRepository;
  
  /**
   * ログイン情報を取得する
   *
   * @param nameOrEmail ユーザ名前 又はメースID
   * @param password パスワード
   * @return
   */

  public List<MemberInfo> selectMemberInfoByName(String nameOrEmail){
	  return memberInfoRepository.findByNameOrEmail(nameOrEmail,nameOrEmail);
  }

  /* public List<Map<String, Object>> selectMemberInfoByName(String nameOrEmail) {//List Map 对应数据库中的行列值
  String sql = "select * from MemberInfo where (Name='" + nameOrEmail + "' or Email = '"
      + nameOrEmail + "')";
  logger.debug("loginservice selectMemberInfoByName  sql:==" + sql);
  return jdbcTemplate.queryForList(sql);
}*/

  /**
   * ログイン情報をチェックする
   *
   * @param userid
   * @return
   */

  public String selectMemberInfoByGuestId(String id) {  	
	  	SNSInfo snsInfo = snsInfoRepository.findBySNSID(id);
	  	String memberId;
	  	if(snsInfo !=null){
	  		return snsInfo.getMemberID();
	  	}else{
	  		try {
				memberId =IdGenerationLogic.getId();
				snsInfo = new SNSInfo();
				snsInfo.setMemberID(getDataForInsert(memberId));
				snsInfo.setSNSID(getDataForInsert(id));
				snsInfo.setSNSType(0);
				snsInfo.setAuthFlag(1);
		  		snsInfoRepository.save(snsInfo);
		  		return memberId;
	  		} catch (Exception e) {	
	  			logger.error(e.getMessage());
	  	        return null;
			}
	  	}
	  }

  /*  public String selectMemberInfoByGuestId(String id) {
  String sql = "select * from SNSInfo where SNSID=" + getDataForInsert(id);
  logger.debug("loginservice selectMemberInfoByGuestId select  SNSInfo sql:==" + sql);
  List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
  String memberId;
  if (list != null && list.size() > 0) {
    Map<String, Object> map = list.get(0);
    return StringUtils.toStr(map.get("MemberID"));
  } else {
    try {
      memberId = IdGenerationLogic.getId();
      // ユーザ登録する
      sql = "INSERT INTO SNSInfo (MemberID, SNSID, SNSType, AuthFlag) VALUES (%s,%s,%s,%s)";
      sql = String.format(sql, getDataForInsert(memberId), getDataForInsert(id), 0, 1);
      logger.debug("loginservice selectMemberInfoByGuestId  insert SNSInfo sql:==" + sql);
      jdbcTemplate.execute(sql);
      return memberId;
    } catch (Exception e) {
      logger.error(e.getMessage());
      return null;
    }
  }
}*/

  /**
   * 会員登録処理
   * tab:判断temporary 是否存在dummyMemberId，既要确认该认证码是否存在，和一系列判定
   *   判断memberinfo中email是否匹配，插入数据
   * @param email
   * @param name
   * @param password
   * @return
   */
  
  public DataResult<String> insertUserData(String email, String name, String password,
	      String authCode, String dummyMemberId) {
	  Temporary temporary = temporaryRepository.findByID(dummyMemberId);
	  
	  if(temporary == null){
		  return new DataResult<String>("E001", "認証情報がない、再度発行お願いします");
	  }
	    String expire = StringUtils.toStr(temporary.getExpire());
	    String dbAuthCode = StringUtils.toStr(temporary.getTemporaryValue());
	    String dbEmail = StringUtils.toStr(temporary.getTemporaryName());
	    if (dateLogicUtils.getCurrentTimeString().compareTo(expire) > 0) {
	      // 認証情報の期間切れ
	      return new DataResult<String>("E002", "認証期限きれ、再度発行お願いします");
	    }
	    // 認証コード一致しない
	    if (!authCode.equals(dbAuthCode)) {
	      // 認証コード違う
	      return new DataResult<String>("E003", "認証コードが違う、再度発行お願いします");
	    }
	    // 認証コードのメール情報が一致しない
	    if (!email.equals(dbEmail)) {
	      // メール情報違う
	      return new DataResult<String>("E004", "メール情報が違う、再度発行お願いします");
	    }
	  //查询邮箱是否已经有用户注册
	    List<MemberInfo> memberInfoList = memberInfoRepository.findByEmail(dbEmail);
	    if(memberInfoList != null && memberInfoList.size() > 0 ){
	    	return new DataResult<String>("E004", "データが存在しました。別のメールを利用お願いします");
	    }
	      try {
			String memberId = IdGenerationLogic.getId();
			MemberInfo memberInfo1 = new MemberInfo();
			
			memberInfo1.setMemberID(memberId);
			memberInfo1.setEmail(getDataForInsert(email));
			memberInfo1.setPassword(PasswordUtils.getEncoderPassword(password));
			memberInfo1.setNickname(getDataForInsert(name));
			memberInfoRepository.save(memberInfo1);
			return new DataResult<String>(memberId);
		} catch (Exception e) {
		      logger.error(e.getMessage());
		      return new DataResult<String>("E999", "データ登録が失敗しました。");	
		}  
  }
  
  
/*  public DataResult<String> insertUserData(String email, String name, String password,
      String authCode, String dummyMemberId) {
    String sql = "SELECT * FROM  Temporary  WHERE ID=" + getDataForInsert(dummyMemberId);
    logger.debug("loginservice insertUserData  select Temporary sql:==" + sql);
    List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
    if (list == null || list.size() == 0) {
      // 認証情報が存在しない
      return new DataResult<String>("E001", "認証情報がない、再度発行お願いします");
    }
    Map<String, Object> resultData = list.get(0);
    String expire = StringUtils.toStr(resultData.get("expire"));
    String dbAuthCode = StringUtils.toStr(resultData.get("TemporaryValue"));
    String dbEmail = StringUtils.toStr(resultData.get("TemporaryName"));
    if (dateLogicUtils.getCurrentTimeString().compareTo(expire) > 0) {
      // 認証情報の期間切れ
      return new DataResult<String>("E002", "認証期限きれ、再度発行お願いします");
    }
    // 認証コード一致しない
    if (!authCode.equals(dbAuthCode)) {
      // 認証コード違う
      return new DataResult<String>("E003", "認証コードが違う、再度発行お願いします");
    }
    // 認証コードのメール情報が一致しない
    if (!email.equals(dbEmail)) {
      // メール情報違う
      return new DataResult<String>("E004", "メール情報が違う、再度発行お願いします");
    }
    sql = "select 1 from MemberInfo where Email = '" + email + "'";
    logger.debug("loginservice insertUserData  select MemberInfo sql:==" + sql);
    list = jdbcTemplate.queryForList(sql);
    if (list != null && list.size() > 0) {
      return new DataResult<String>("E004", "データが存在しました。別のメールを利用お願いします");
    }
    // データ登録
    try {
      String memberId = IdGenerationLogic.getId();
      // ユーザ登録する
      sql = "INSERT INTO MemberInfo (MemberID, Email, Password, Nickname) VALUES (%s,%s,%s,%s)";
      sql = String.format(sql, getDataForInsert(memberId), getDataForInsert(email),
          getDataForInsert(PasswordUtils.getEncoderPassword(password)), getDataForInsert(name));
      logger.debug("loginservice insertUserData  INSERT MemberInfo sql:==" + sql);
      jdbcTemplate.execute(sql);
      return new DataResult<String>(memberId);
    } catch (Exception e) {
      logger.error(e.getMessage());
      return new DataResult<String>("E999", "データ登録が失敗しました。");
    }
  }*/

  /***
   * 認証コードをメール送信する
   * tab：
   *
   * @param email
   * @param name
   * @param password
   * @return dummyMemberId
   */
  public DataResult<String> sendAuthCode(String email) {
	    try {
		   List<MemberInfo> memberInfoList = memberInfoRepository.findByEmail(email);
		   if(memberInfoList != null && memberInfoList.size() > 0){
			   return new DataResult<String>(ResultCode.NG.code(), "データが存在しました。別のメールを利用お願いします");
		   }
	      String dummyMemberId = "_dummy" + IdGenerationLogic.getId(14);
	      String authCode = String.valueOf(RandomUtils.makeAuthCode());
	      Calendar cal = dateLogicUtils.getCurrent();
	      String InsTstmp = dateLogicUtils.getCurrentTimeString(cal);
	      cal.add(Calendar.MINUTE, 60);
	      String expireTstmp = dateLogicUtils.getCurrentTimeString(cal);
	      
	      Temporary temporary = new Temporary();
	      temporary.setID(dummyMemberId);
	      temporary.setType("00");
	      temporary.setTemporaryName(email);
	      temporary.setTemporaryValue(authCode);
	      temporary.setInsTstmp(InsTstmp);
	      temporary.setExpire(expireTstmp);
	      temporaryRepository.save(temporary);
	      
	      return new DataResult<String>("authoCode : " + authCode + " , " + "dummyMemberId : " + dummyMemberId);
	    } catch (Exception e) {
	      logger.error(e.getMessage());
	      return new DataResult<String>(ResultCode.NG.code(), "メール送信失敗しました。");
	    }
	  }
  
/*  public DataResult<String> sendAuthCode(String email) {
    try {
      String sql = "select 1 from MemberInfo where Email = " + getDataForInsert(email);
      select 1 from table;与select anycol(目的表集合中的任意一行） from table;与select * from table
             从作用上来说是没有差别的，都是查看是否有记录，一般是作条件查询用的。select 1 from 中的1是一常量（可以为任
             意数值），查到的所有行的值都是它，但从效率上来说，1>anycol>*，因为不用查字典表
      logger.debug("loginservice sendAuthCode  select MemberInfo sql:==" + sql);
      List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
      if (list != null && list.size() > 0) {
        return new DataResult<String>(ResultCode.NG.code(), "データが存在しました。別のメールを利用お願いします");
      }
      String dummyMemberId = "_dummy" + IdGenerationLogic.getId(14);
      sql = "INSERT INTO Temporary (ID, type, TemporaryName, TemporaryValue, InsTstmp, expire) VALUES ('%s', '%s', '%s', '%s', '%s', '%s')";
      String authoCode = String.valueOf(RandomUtils.makeAuthCode());
      Calendar cal = dateLogicUtils.getCurrent();
      String InsTstmp = dateLogicUtils.getCurrentTimeString(cal);
      cal.add(Calendar.MINUTE, 60);
      String expireTstmp = dateLogicUtils.getCurrentTimeString(cal);
      sql = String.format(sql, dummyMemberId, "00", email, authoCode, InsTstmp, expireTstmp);
      logger.debug("loginservice sendAuthCode  INSERT Temporary sql:==" + sql);
      jdbcTemplate.execute(sql);
      return new DataResult<String>("authoCode : " + authoCode + " , " + "dummyMemberId : " + dummyMemberId);
    } catch (Exception e) {
      logger.error(e.getMessage());
      return new DataResult<String>(ResultCode.NG.code(), "メール送信失敗しました。");
    }
  }*/


  /**
   * ユーザのプロフィール情報を設定する
   *
   * @param profile プロフィール情報
   * @return 設定結果
   */
  public DataResult<List> setProfileInfo(Profile profile) {
	  try{
	  
	  List<MemberInfo> memberInfoList = memberInfoRepository.findByMemberID(profile.getMemberid());
	  
	  if(memberInfoList == null || memberInfoList.size() == 0){
		  return new DataResult<List>(ResultCode.NG.code(), "会員が存在しないので、登録したい会員IDを確認お願いします。");
	  }
	  
      String playYears = profile.getPlayYears();
      if (StringUtils.isNotEmpty(playYears)) {
        playYears = playYears.replace("代", "");
        profile.setPlayYears(playYears);
      }
      // 性別を設定する
      String strSex = profile.getSex();
      if (StringUtils.isNotEmpty(strSex)) {
        strSex = strSex.equals(SexCode.BOY.codeNm()) ? SexCode.BOY.code() : SexCode.GIRL.code();
        profile.setSex(strSex);
      }
      String averageScore = profile.getAverageScore();
      if (StringUtils.isNotEmpty(averageScore)) {
        averageScore = averageScore.replace("台", "");
        profile.setAverageScore(averageScore);
      }

      String playStyle = profile.getPlayStyle();
      if (StringUtils.isNotEmpty(playStyle)) {
        profile.setPlayStyle("エンジョイ".equals(playStyle) ? "0" : "1");
      }
      if ("true".equals(profile.getPushFlag())) {
        profile.setPushFlag("1");
      } else {
        profile.setPushFlag("0");
      }
      
      MemberInfo memberInfo = memberInfoList.get(0);
      memberInfo.setNickname(getDataForInsert(profile.getNickname()));
      memberInfo.setName(getDataForInsert(profile.getName()));
      memberInfo.setKana(getDataForInsert(profile.getKana()));
      memberInfo.setSex(getDataForInsert(profile.getSex()));
      memberInfo.setPlayYears(getDataForInsert(profile.getPlayYears()));
      memberInfo.setAverageScore(getDataForInsert(profile.getAverageScore()));
      memberInfo.setPlayStyle(getDataForInsert(profile.getPlayStyle()));
      memberInfo.setNotes(getDataForInsert(profile.getNotes()));
      memberInfo.setTelNo(getDataForInsert(profile.getTelNo()));
      memberInfo.setState(getDataForInsert(profile.getState()));
      memberInfo.setClub1(getDataForInsert(profile.getClub1()));
      memberInfo.setClub2(getDataForInsert(profile.getClub2()));
      memberInfo.setClub3(getDataForInsert(profile.getClub3()));
      memberInfo.setClub4(getDataForInsert(profile.getClub4()));
      memberInfo.setPushFlag(getDataForInsert(profile.getPushFlag()));
      
      memberInfoRepository.save(memberInfo);
      
      
      List<MemberInfo> memberInfoList1 = memberInfoRepository.findByMemberID(profile.getMemberid());
      
      if(memberInfoList1 == null || memberInfoList1.size() == 0){
          return new DataResult<List>(ResultCode.NG.code(), "会員が存在しないので、登録したい会員IDを確認お願いします。");
      }
      return new DataResult<List>(memberInfoList1);
      } catch (Exception e) {
        logger.error(e.getMessage());
        return new DataResult<List>(ResultCode.NG.code(), "プロフィール情報が登録失敗しました。");
      }
    }
      

/*public DataResult<List> setProfileInfo(Profile profile) {
    try {
      String sql = "select 1 from MemberInfo where MemberID = "
          + getDataForInsert(profile.getMemberid());
      logger.debug("loginservice setProfileInfo  select MemberInfo sql:==" + sql);
      List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
      if (list == null || list.size() == 0) {
        return new DataResult<List>(ResultCode.NG.code(), "会員が存在しないので、登録したい会員IDを確認お願いします。");
      }
      sql = "UPDATE MemberInfo SET Nickname=%s, Name=%s, Kana=%s, Sex=%s,"
          + "  PlayYears=%s, AverageScore=%s, PlayStyle=%s, Notes=%s, TelNo=%s, State=%s,"
          + " Club1=%s, Club2=%s, Club3=%s, Club4=%s, PushFlag=%s " + "WHERE MemberID=%s";

      String playYears = profile.getPlayYears();
      if (StringUtils.isNotEmpty(playYears)) {
        playYears = playYears.replace("代", "");
        profile.setPlayYears(playYears);
      }
      // 性別を設定する
      String strSex = profile.getSex();
      if (StringUtils.isNotEmpty(strSex)) {
        strSex = strSex.equals(SexCode.BOY.codeNm()) ? SexCode.BOY.code() : SexCode.GIRL.code();
        profile.setSex(strSex);
      }
      String averageScore = profile.getAverageScore();
      if (StringUtils.isNotEmpty(averageScore)) {
        averageScore = averageScore.replace("台", "");
        profile.setAverageScore(averageScore);
      }

      String playStyle = profile.getPlayStyle();
      if (StringUtils.isNotEmpty(playStyle)) {
        profile.setPlayStyle("エンジョイ".equals(playStyle) ? "0" : "1");
      }
      if ("true".equals(profile.getPushFlag())) {
        profile.setPushFlag("1");
      } else {
        profile.setPushFlag("0");
      }

      sql = String.format(sql, getDataForInsert(profile.getNickname()),
          getDataForInsert(profile.getName()), getDataForInsert(profile.getKana()),
          getDataForInsert(profile.getSex()), getDataForInsert(profile.getPlayYears()),
          getDataForInsert(profile.getAverageScore()), getDataForInsert(profile.getPlayStyle()),
          getDataForInsert(profile.getNotes()), getDataForInsert(profile.getTelNo()),
          getDataForInsert(profile.getState()), getDataForInsert(profile.getClub1()),
          getDataForInsert(profile.getClub2()), getDataForInsert(profile.getClub3()),
          getDataForInsert(profile.getClub4()), getDataForInsert(profile.getPushFlag()),
          getDataForInsert(profile.getMemberid()));
      logger.debug("loginservice setProfileInfo  UPDATE MemberInfo sql:==" + sql);
      jdbcTemplate.execute(sql);

      sql = "select * from MemberInfo where MemberID = '" + profile.getMemberid() + "'";
      logger.debug("loginservice setProfileInfo  select MemberInfo2 sql:==" + sql);
      list = jdbcTemplate.queryForList(sql);
      if (list == null || list.size() == 0) {
        return new DataResult<List>(ResultCode.NG.code(), "会員が存在しないので、登録したい会員IDを確認お願いします。");
      }
      return new DataResult<List>(list);
    } catch (Exception e) {
      logger.error(e.getMessage());
      return new DataResult<List>(ResultCode.NG.code(), "プロフィール情報が登録失敗しました。");
    }
  }*/

  private String getDataForInsert(String target) {//如果不为空返回当前字符串，为空返回null
    if (StringUtils.isNotEmpty(target)) {
      return "'" + target + "'";
    }
    return null;
  }
}
