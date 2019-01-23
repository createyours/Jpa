package org.leadingsoft.golf.api.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.leadingsoft.golf.api.code.ApovStatusCode;
import org.leadingsoft.golf.api.code.CollectStateCode;
import org.leadingsoft.golf.api.code.MatchingCode;
import org.leadingsoft.golf.api.code.ResultCode;
import org.leadingsoft.golf.api.code.SexCode;
import org.leadingsoft.golf.api.entity.Applychat;
import org.leadingsoft.golf.api.entity.Applyinfo;
import org.leadingsoft.golf.api.entity.MemberInfo;
import org.leadingsoft.golf.api.model.ApplyInfo;
import org.leadingsoft.golf.api.model.DataResult;
import org.leadingsoft.golf.api.repository.ApplyInfoRepository;
import org.leadingsoft.golf.api.repository.ApplychatRepository;
import org.leadingsoft.golf.api.repository.MemberInfoRepository;
import org.leadingsoft.golf.api.repository.RecruitInfoRepository;
import org.leadingsoft.golf.api.util.DateLogicUtils;
import org.leadingsoft.golf.api.util.RandomUtils;
import org.leadingsoft.golf.api.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * 応募情報サービス
 *
 * <pre>
 *  登録・検索
 * </pre>
 */
@Service
public class SubscriberService {
  private final static Logger logger = LoggerFactory.getLogger(SubscriberService.class);
  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private DateLogicUtils dateLogicUtils;
  
  @Autowired
  private ApplyInfoRepository applyInfoRepository;
  
  @Autowired
  private MemberInfoRepository memberInfoRepository;
  
  @Autowired
  private ApplychatRepository applychatRepository;

  @Autowired 
  RecruitInfoRepository recruitInfoRepository;
  /**
   * 応募情報を保存する
   *
   * @param collect 募集情報
   * @return 保存結果
   */
  
  public DataResult<String> apply(ApplyInfo applyInfo) {
	    try {
	    	
	    	Specification<Applyinfo> specification = new Specification<Applyinfo>(){

				private static final long serialVersionUID = 1L;

				@Override
				public Predicate toPredicate(Root<Applyinfo> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {
					
					List<Predicate> predicateList = new ArrayList<Predicate>();	
					
					Path<String> exp1 = root.get("memberid");
					Path<Integer> exp2 = root.get("roundserialno");
					Path<String> exp3 = root.get("apovstatus");
					
					CriteriaBuilder.In<String> in =  criteriaBuilder.in(exp3);
					in.value(nullToString(ApovStatusCode.UNApov.code(), true));
					in.value(nullToString(ApovStatusCode.ApovOK.code(), true));
					predicateList.add(criteriaBuilder.equal(exp1.as(String.class),nullToString(applyInfo.getMemberID(), true)));
					predicateList.add(criteriaBuilder.equal(exp2.as(Integer.class),nullToString(applyInfo.getRoundSerialNo(), false)));
					predicateList.add(in);
					
					Predicate[] p = new Predicate[predicateList.size()];
					return criteriaBuilder.and(predicateList.toArray(p));
				}
	    	};
	    	List<Applyinfo> list = applyInfoRepository.findAll(specification);
	    	
	    	System.out.println(list.size()+"----------------------");
	    	Iterator<Applyinfo> it = list.iterator();
	    	while(it.hasNext()){
	    		Applyinfo ai = it.next();
	    		
	    		System.out.println(ai.getApovstatus()+ai.getCancelflag()+ai.getMemberid()+"---------");
	    	}
	    	System.out.println("----------------------");
	    	
	    	
	      if (list != null && list.size() > 0) {
	        return new DataResult<String>(ResultCode.NG.code(), "該当する募集を応募しました。");
	      }

	      // 受付通番IDを生成する
	      String regNo;
	      while (true) {
	        regNo = RandomUtils.getRandomNumber(8);
	        
	        List<Applyinfo> applyinfoList = applyInfoRepository.findByRegno(Integer.parseInt(regNo));
	        
	        if (applyinfoList == null || applyinfoList.size() == 0) {
	          break;
	        }
	      }
	      
	      Applyinfo applyinfo = new Applyinfo();
	      applyinfo.setRoundserialno(Integer.parseInt(nullToString(applyInfo.getRoundSerialNo(), false)));
	      applyinfo.setMemberid(nullToString(applyInfo.getMemberID(), true));
	      applyinfo.setRegno(Integer.parseInt(nullToString(regNo, false)));
	      applyinfo.setComments(nullToString(applyInfo.getComments(), true));
	      applyinfo.setApovstatus(nullToString(ApovStatusCode.UNApov.code(), true));
	      //存入的数据中regno，和roundserialno数值有变
	      applyInfoRepository.save(applyinfo);

	      // プロファイル情報を更新する
	      String playYears = applyInfo.getPlayYears();
	      if (StringUtils.isNotEmpty(playYears)) {
	        playYears = playYears.replace("代", "");
	        applyInfo.setPlayYears(playYears);
	      }
	      // 性別を設定する
	      String strSex = applyInfo.getSex();
	      if (StringUtils.isNotEmpty(strSex)) {
	        strSex = strSex.equals(SexCode.BOY.codeNm()) ? SexCode.BOY.code() : SexCode.GIRL.code();
	        applyInfo.setSex(strSex);
	      }
	      
	      List<MemberInfo> memberInfoList = memberInfoRepository.findByMemberID(nullToString(applyInfo.getMemberID(), true));
	      MemberInfo memberInfo = memberInfoList.get(0);
	      memberInfo.setName(nullToString(applyInfo.getName(), true));
	      memberInfo.setKana(nullToString(applyInfo.getKana(), true));
	      memberInfo.setSex(nullToString(applyInfo.getSex(), true));
	      memberInfo.setPlayYears(nullToString(applyInfo.getPlayYears(),false));
	      memberInfo.setTelNo(nullToString(applyInfo.getTelNo(), true));
	      memberInfo.setState(nullToString(applyInfo.getState(), true));
	      memberInfoRepository.save(memberInfo);
	      
	      return new DataResult<String>();
	    } catch (Exception e) {
	      logger.error(e.getMessage());
	      return new DataResult<String>(ResultCode.NG.code(), "募集情報が登録失敗しました。");
	    }
	  }
  
 /* public DataResult<String> apply(ApplyInfo applyInfo) {
    try {
      String sql = null;
      List<Map<String, Object>> list = null;
      sql = "select 1 from ApplyInfo where MemberID = %s  AND RoundSerialNo= %s  AND ApovStatus in(%s,%s) ";
      sql = String.format(sql, nullToString(applyInfo.getMemberID(), true),
          nullToString(applyInfo.getRoundSerialNo(), false),
          nullToString(ApovStatusCode.UNApov.code(), true),
          nullToString(ApovStatusCode.ApovOK.code(), true));
      logger.debug("select ApplyInfo sql:==" + sql);
      // この応募は該当するユーザを承認済ですか？
      list = jdbcTemplate.queryForList(sql);
      if (list != null && list.size() > 0) {
        return new DataResult<String>(ResultCode.NG.code(), "該当する募集を応募しました。");
      }

      // 受付通番IDを生成する
      String regNo;
      while (true) {
        regNo = RandomUtils.getRandomNumber(8);
        sql = "select 1 from ApplyInfo where RegNo = " + regNo;
        logger.debug("select ApplyInfo2 sql:==" + sql);
        list = jdbcTemplate.queryForList(sql);
        if (list == null || list.size() == 0) {
          break;
        }
      }
      sql = "INSERT INTO ApplyInfo (RoundSerialNo, MemberID, RegNo, Comments, ApovStatus) VALUES (%s, %s, %s, %s, %s)";
      sql = String.format(sql, nullToString(applyInfo.getRoundSerialNo(), false),
          nullToString(applyInfo.getMemberID(), true), nullToString(regNo, false),
          nullToString(applyInfo.getComments(), true),
          nullToString(ApovStatusCode.UNApov.code(), true));
      logger.debug("insert ApplyInfo sql:==" + sql);
      jdbcTemplate.execute(sql);

      // プロファイル情報を更新する
      sql = "UPDATE MemberInfo SET  Name=%s, Kana=%s, Sex=%s,"
          + "  PlayYears=%s,TelNo=%s, State=%s " + "WHERE MemberID=%s";

      String playYears = applyInfo.getPlayYears();
      if (StringUtils.isNotEmpty(playYears)) {
        playYears = playYears.replace("代", "");
        applyInfo.setPlayYears(playYears);
      }
      // 性別を設定する
      String strSex = applyInfo.getSex();
      if (StringUtils.isNotEmpty(strSex)) {
        strSex = strSex.equals(SexCode.BOY.codeNm()) ? SexCode.BOY.code() : SexCode.GIRL.code();
        applyInfo.setSex(strSex);
      }
      sql = String.format(sql, nullToString(applyInfo.getName(), true),
          nullToString(applyInfo.getKana(), true), nullToString(applyInfo.getSex(), true),
          nullToString(applyInfo.getPlayYears(), false), nullToString(applyInfo.getTelNo(), true),
          nullToString(applyInfo.getState(), true), nullToString(applyInfo.getMemberID(), true));
      logger.debug("update MemberInfo sql:==" + sql);
      jdbcTemplate.execute(sql);
      return new DataResult<String>();
    } catch (Exception e) {
      logger.error(e.getMessage());
      return new DataResult<String>(ResultCode.NG.code(), "募集情報が登録失敗しました。");
    }
  }*/

  /**
   * 応募情報を検索する
   *
   * @param roundSerialNo 募集ID
   * @return
   */
  public DataResult<List> search(String roundSerialNo) {
	    try {
		      // 該当する未承認の応募情報を検索する
	    	Specification<Applyinfo> specification = new Specification<Applyinfo>(){

				private static final long serialVersionUID = 1L;

				@Override
				public Predicate toPredicate(Root<Applyinfo> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {
					
					List<Predicate> predicateList = new ArrayList<Predicate>();	
					
					Path<String> exp1 = root.get("roundserialno");
					Path<Integer> exp2 = root.get("apovstatus");
					
					predicateList.add(criteriaBuilder.equal(exp1.as(Integer.class), Integer.parseInt(nullToString(roundSerialNo, false))));
					predicateList.add(criteriaBuilder.or(criteriaBuilder.isNull(exp2),criteriaBuilder.notEqual(exp2, ApovStatusCode.ApovNG.code())));
					
					Predicate[] p = new Predicate[predicateList.size()];
					return criteriaBuilder.and(predicateList.toArray(p));
				}
	    	};
	    	List<Applyinfo> applyinfoList = applyInfoRepository.findAll(specification);
	    	
	      if (applyinfoList == null || applyinfoList.size() == 0) {
	        return new DataResult<List>(ResultCode.NG.code(), "応募情報がありません");
	      }
	      return new DataResult<List>(applyinfoList);
	    } catch (Exception e) {
	      logger.error(e.getMessage());
	      return new DataResult<List>(ResultCode.NG.code(), "応募情報が取得失敗しました。");
	    }
	  }
  
  
/*  public DataResult<List> search(String roundSerialNo) {
    try {

    	
      String sql = null;
      List<Map<String, Object>> list = null;
      // 該当する未承認の応募情報を検索する
      sql = "select MemberInfo.*, ApplyInfo.RoundSerialNo,ApplyInfo.RegNo,ApplyInfo.ApovStatus,ApplyInfo.Comments from ApplyInfo inner join MemberInfo on MemberInfo.MemberID = ApplyInfo.MemberID  where RoundSerialNo= %s  AND (ApovStatus is null or ApovStatus != '"
          + ApovStatusCode.ApovNG.code() + "') ";
      sql = String.format(sql, nullToString(roundSerialNo, false));
      logger.debug("search ApplyInfo sql:==" + sql);
      list = jdbcTemplate.queryForList(sql);
      if (list == null || list.size() == 0) {
        return new DataResult<List>(ResultCode.NG.code(), "応募情報がありません");
      }
      return new DataResult<List>(list);
    } catch (Exception e) {
      logger.error(e.getMessage());
      return new DataResult<List>(ResultCode.NG.code(), "応募情報が取得失敗しました。");
    }
  }*/

  /**
   * 応募情報を承認する
   *
   * @param roundSerialNo 募集ID
   * @param regNo 受付通番
   * @return
   */
  
  public DataResult<String> agree(String roundSerialNo, String regNo) {
	    try {
	      // 該当する未承認の応募情報を検索する
	    	Specification<Applyinfo> specification = new Specification<Applyinfo>(){

				private static final long serialVersionUID = 1L;
				
				@Override
				public Predicate toPredicate(Root<Applyinfo> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {
					
					List<Predicate> predicateList = new ArrayList<Predicate>();	
					
					Path<Integer> exp1 = root.get("roundserialno");
					Path<Integer> exp2 = root.get("regno");
					Path<String> exp3 = root.get("apovstatus");
					
					predicateList.add(criteriaBuilder.equal(exp1.as(Integer.class), nullToString(roundSerialNo, false)));
					predicateList.add(criteriaBuilder.equal(exp2.as(Integer.class), nullToString(regNo, false)));
					predicateList.add(criteriaBuilder.equal(exp3,ApovStatusCode.UNApov.code()));
					
					Predicate[] p = new Predicate[predicateList.size()];
					return criteriaBuilder.and(predicateList.toArray(p));
				}
	    	};
	    	
	    	List<Applyinfo> applyInfoList = applyInfoRepository.findAll(specification);
	    	
	      if (applyInfoList == null || applyInfoList.size() == 0) {
	        return new DataResult<String>(ResultCode.NG.code(), "該当する応募情報が承認できません");
	      }
	      
	      String collectcount = null;
	      List<Integer> list =new ArrayList();
	      list = recruitInfoRepository.getCountByRoundSerialNo(Integer.valueOf(roundSerialNo));

	      
	      if (list == null || list.size() == 0) {
	        return new DataResult<String>(ResultCode.NG.code(), "該当する応募情報が承認できません");
	      } else {
	        collectcount = StringUtils.toStr(list.get(0));
	        if (StringUtils.isEmpty(collectcount)
	            || MatchingCode.UNMATCHED.code().equals(collectcount)) {
	          // 募集が完了
	          return new DataResult<String>(ResultCode.NG.code(), "この募集が募集完了しました。");
	        }
	      }
	      
	      // 応募情報を承認ずみへ変更する
	      int updateNumber = applyInfoRepository.updateApplyInfo(ApovStatusCode.ApovOK.code(),Integer.valueOf(roundSerialNo), Integer.valueOf(regNo));
	      // 全部承認完了したら、この募集状態を更新する
	      if (MatchingCode.MATCHED.code().equals(collectcount)) {

	    	  recruitInfoRepository.updateRecruitInfo(Integer.valueOf(CollectStateCode.COMPLETE.code()), Integer.valueOf(roundSerialNo));
	      }
	      return new DataResult<String>();
	    } catch (Exception e) {
	      logger.error(e.getMessage());
	      return new DataResult<String>(ResultCode.NG.code(), "応募情報が承認失敗しました。");
	    }
	  }
  
 /* public DataResult<String> agree(String roundSerialNo, String regNo) {
    try {
      String sql = null;
      List<Map<String, Object>> list = null;
      // 該当する未承認の応募情報を検索する
      sql = "select 1 from ApplyInfo where RoundSerialNo= %s  AND RegNo = %s AND ApovStatus = '"
          + ApovStatusCode.UNApov.code() + "' ";
      sql = String.format(sql, nullToString(roundSerialNo, false), nullToString(regNo, false));
      logger.debug("select check ApplyInfo sql:==" + sql);
      list = jdbcTemplate.queryForList(sql);
      if (list == null || list.size() == 0) {
        return new DataResult<String>(ResultCode.NG.code(), "該当する応募情報が承認できません");
      }

      // 応募情報数をチェックする
      String collectcount = null;
      sql = "select (RecruitInfo.RecruitNum - IFNULL(Apply.ApovCnt, 0)) AS count "
          + " from RecruitInfo" + " left join  "
          + "      (select count(0) as ApovCnt, RoundSerialNo from ApplyInfo"
          + "       where ApovStatus = '1' group by RoundSerialNo) Apply "
          + "      ON Apply.RoundSerialNo = RecruitInfo.RoundSerialNo"
          + "      where RecruitInfo.RoundSerialNo = " + roundSerialNo;
      logger.debug("check ApplyInfo sql:==" + sql);
      list = jdbcTemplate.queryForList(sql);
      if (list == null || list.size() == 0) {
        return new DataResult<String>(ResultCode.NG.code(), "該当する応募情報が承認できません");
      } else {
        collectcount = StringUtils.toStr(list.get(0).get("count"));
        if (StringUtils.isEmpty(collectcount)
            || MatchingCode.UNMATCHED.code().equals(collectcount)) {
          // 募集が完了
          return new DataResult<String>(ResultCode.NG.code(), "この募集が募集完了しました。");
        }
      }

      // 応募情報を承認ずみへ変更する
      sql = "update ApplyInfo set ApovStatus = '" + ApovStatusCode.ApovOK.code()
          + "'  where RoundSerialNo= %s  AND RegNo = %s ";
      sql = String.format(sql, nullToString(roundSerialNo, false), nullToString(regNo, false));
      logger.debug("update ApplyInfo sql:==" + sql);
      jdbcTemplate.execute(sql);
      // 全部承認完了したら、この募集状態を更新する
      if (MatchingCode.MATCHED.code().equals(collectcount)) {
        sql = "update RecruitInfo set Status = " + CollectStateCode.COMPLETE.code()
            + " where RoundSerialNo= %s";
        sql = String.format(sql, nullToString(roundSerialNo, false));
        logger.debug("update RecruitInfo sql:==" + sql);
        jdbcTemplate.execute(sql);
      }
      return new DataResult<String>();
    } catch (Exception e) {
      logger.error(e.getMessage());
      return new DataResult<String>(ResultCode.NG.code(), "応募情報が承認失敗しました。");
    }
  }*/

  /**
   * チャット情報登録
   *
   * @param roundSerialNo
   * @param memberID
   * @param chatContents
   * @return
   */
  public DataResult<String> insertChat(String roundSerialNo, String memberID, String chatContents) {
	    int sortOrder = 0;
	    int sunNum = applychatRepository.countByRoundserialno(Integer.parseInt(roundSerialNo));//是否有类型转换
	    sortOrder = sunNum + 1;

	    Calendar cal = dateLogicUtils.getCurrent();
	    String insTstmp = dateLogicUtils.getCurrentTimeString(cal);
	    
	    Applychat applychat = new Applychat();
	    applychat.setRoundserialno(Integer.parseInt(nullToString(roundSerialNo, false)));
	    applychat.setMemberid(nullToString(memberID, true));
	    applychat.setChatcontents(nullToString(chatContents, true));
	    applychat.setSortorder(Integer.parseInt(nullToString(String.valueOf(sortOrder), false)));
	    applychat.setRegdate(nullToString(insTstmp, true));
	    applychatRepository.save(applychat);
	    
	    return new DataResult<String>(ResultCode.OK.code(), "チャット情報が登録しました。");
	  }
  
  /*public DataResult<String> insertChat(String roundSerialNo, String memberID, String chatContents) {
    int sortOrder = 0;
    String sql = "SELECT count(*) as sumNum from ApplyChat where RoundSerialNo = %s";
    sql = String.format(sql, nullToString(roundSerialNo, false));
    List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
    int sunNum = Integer.parseInt(list.get(0).get("sumNum").toString());
    sortOrder = sunNum + 1;

    Calendar cal = dateLogicUtils.getCurrent();
    String insTstmp = dateLogicUtils.getCurrentTimeString(cal);
    sql = "INSERT ApplyChat(RoundSerialNo, MemberID, ChatContents, SortOrder, RegDate) VALUES (%s, %s, %s, %s, %s)";
    sql = String.format(sql, nullToString(roundSerialNo, false), nullToString(memberID, true),
        nullToString(chatContents, true), nullToString(String.valueOf(sortOrder), false),
        nullToString(insTstmp, true));
    jdbcTemplate.execute(sql);
    return new DataResult<String>(ResultCode.OK.code(), "チャット情報が登録しました。");
  }*/

  /**
   * チャット情報取得
   *
   * @param roundSerialNo
   * @return
   */
  
  public DataResult<List> searchChatInformation(String roundSerialNo) {
	    try {
	    	
	    	Specification<Applychat> specification = new Specification<Applychat>(){

				private static final long serialVersionUID = 1L;

				@Override
				public Predicate toPredicate(Root<Applychat> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {

					Join<Applychat,MemberInfo> joinMemberInfo = root.join("memberInfo",JoinType.INNER);
					
					Predicate p1 = criteriaBuilder.equal(root.get("roundserialno"),Integer.parseInt(nullToString(roundSerialNo, false)));
					query.where(p1);
					query.orderBy(criteriaBuilder.desc(root.get("sortorder").as(Integer.class)));
					return query.getRestriction();
				}
	    	};
	    	
	    	List<Applychat> applychatList = applychatRepository.findAll(specification);
	    	
	      return new DataResult<List>(applychatList);
	    } catch (Exception e) {
	      logger.error(e.getMessage());
	      return new DataResult<List>(ResultCode.NG.code(), "チャット情報が取得しません。");
	    }
	  }
  
/*  public DataResult<List> searchChatInformation(String roundSerialNo) {
    try {
      String sql = null;
      List<Map<String, Object>> list = null;
      sql = "select ApplyChat.*, MemberInfo.NickName from ApplyChat inner join MemberInfo on MemberInfo.MemberID = ApplyChat.MemberID  where RoundSerialNo= %s order by ApplyChat.SortOrder";
      sql = String.format(sql, nullToString(roundSerialNo, false));
      logger.debug("search ApplyChat sql:==" + sql);
      list = jdbcTemplate.queryForList(sql);
      return new DataResult<List>(list);
    } catch (Exception e) {
      logger.error(e.getMessage());
      return new DataResult<List>(ResultCode.NG.code(), "チャット情報が取得しません。");
    }
  }*/

  private static String nullToString(String target, boolean isString) {
    if (StringUtils.isEmpty(target)) {
      return null;
    }
    //return isString ? "'" + target + "'" : target;
    return target;
  }

}
