package org.leadingsoft.golf.api.service;

import java.util.ArrayList;
import java.util.Calendar;
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
import org.leadingsoft.golf.api.entity.Applyinfo;
import org.leadingsoft.golf.api.entity.CourseInfo;
import org.leadingsoft.golf.api.entity.RakutenGolfCourseInfo;
import org.leadingsoft.golf.api.entity.RecruitInfo;
import org.leadingsoft.golf.api.model.Collect;
import org.leadingsoft.golf.api.model.CollectSearchResult;
import org.leadingsoft.golf.api.model.DataResult;
import org.leadingsoft.golf.api.repository.ApplyInfoRepository;
import org.leadingsoft.golf.api.repository.CourseInfoRepository;
import org.leadingsoft.golf.api.repository.RakutenGolfCourseInfoRepository;
import org.leadingsoft.golf.api.repository.RecruitInfoRepository;
import org.leadingsoft.golf.api.util.DateLogicUtils;
import org.leadingsoft.golf.api.util.IdGenerationLogic;
import org.leadingsoft.golf.api.util.RandomUtils;
import org.leadingsoft.golf.api.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


/**
 * 募集情報サービス
 *
 * <pre>
 *  登録・検索
 * </pre>
 */
@Service
public class CollectService {
  private final static Logger logger = LoggerFactory.getLogger(CollectService.class);
  @Autowired
  private JdbcTemplate jdbcTemplate;
  @Autowired
  private DateLogicUtils dateLogicUtils;
  @Autowired
  private RecruitInfoRepository recruitInfoRepository;
  @Autowired
  private CourseInfoRepository courseInfoRepository;
  @Autowired
  private ApplyInfoRepository applyInfoRepository;
  @Autowired
  private RakutenGolfCourseInfoRepository  rakutenGolfCourseInfoRepository;

  final static String PRE_DATE = "0";
  final static String CUR_DATE = "1";
  final static String NEXT_DATE = "2";

  /**
   * 募集情報を保存する
   *
   * @param collect 募集情報
   * @return 保存結果
   */
  
  public DataResult<String> save(Collect collect) {
			try {
				Calendar cal = dateLogicUtils.getCurrent();
				String InsTstmp = dateLogicUtils.getCurrentTimeString(cal);
				while (true) {
					// 募集IDを生成する
					String roundSerialNo = RandomUtils.getRandomNumber(8);
					boolean existsFlg = recruitInfoRepository.existsByRoundSerialNo(Integer.parseInt(roundSerialNo));
					if (!existsFlg) {
						collect.setRoundSerialNo(roundSerialNo);
						break;
					}
				}

				// 募集情報登録処理を行う
				String courseId = IdGenerationLogic.getId(10);
				collect.setCourseID(courseId);
				collect.setRegDate(InsTstmp);
				String playStyle = collect.getPlayStyle();
				if (StringUtils.isNotEmpty(playStyle)) {
					collect.setPlayStyle("エンジョイ".equals(playStyle) ? "0" : "1");
				}
				String playDate = collect.getPlayDate();
				if (StringUtils.isNotEmpty(playDate)) {
					playDate = playDate.replaceAll("/", "");
					collect.setPlayDate(playDate);
				}
				String startTime = collect.getStartTime();
				if (StringUtils.isNotEmpty(startTime) && !"###".equals(startTime)) {
					String[] timelist = startTime.split("###");
					
					for(String s:timelist){
						System.out.print(s);
					}
					if (timelist.length == 2) {
						String startdate = timelist[0].replaceAll("/", "");
						String startminu = timelist[1].replaceAll(":", "");
						collect.setStartTime(startdate + startminu);
					} else {
						collect.setStartTime(null);
					}
				} else {
					collect.setStartTime(null);
				}

				String recruitNum = collect.getRecruitNum();
				if (StringUtils.isNotEmpty(recruitNum)) {
					recruitNum = recruitNum.replace("人", "");
					collect.setRecruitNum(recruitNum);
				}

				String closeTime = collect.getCloseDate();
				if (StringUtils.isNotEmpty(closeTime) && !"###".equals(closeTime)) {
					String[] timelist = closeTime.split("###");
					if (timelist.length == 2) {
						String closedate = timelist[0].replaceAll("/", "");
						String closeminu = timelist[1].replaceAll(":", "");
						collect.setCloseDate(closedate + closeminu);
					} else {
						collect.setCloseDate(null);
					}
				} else {
					collect.setCloseDate(null);
				}
				collect.setStatus(CollectStateCode.INIT.code());
				// 募集情報更新用エンティティを作成する
				RecruitInfo updateRecruitInfo = new RecruitInfo();
				// 募集ID
				updateRecruitInfo.setRoundSerialNo(Integer.valueOf(collect.getRoundSerialNo()));
				// 主催者ID
				updateRecruitInfo.setMemberID(collect.getMemberID());
				// コースID
				updateRecruitInfo.setCourseID(collect.getCourseID());
				// スタートコース
				updateRecruitInfo.setStartCourse(1);
				// プレイ日
				updateRecruitInfo.setPlayDate(collect.getPlayDate());
				// スタート時間
				updateRecruitInfo.setStartTime(collect.getStartTime());
				// プレースタイル
				updateRecruitInfo.setPlayStyle(collect.getPlayStyle());
				// プレー料金
				updateRecruitInfo
						.setPlayFee(StringUtils.isEmpty(collect.getPlayFee()) ? 0 : Integer.parseInt(collect.getPlayFee()));
				// ランチ含む
				updateRecruitInfo.setLunchFlag(
						StringUtils.isEmpty(collect.getLunchFlag()) ? 0 : Integer.valueOf(collect.getLunchFlag()));
				// 予約詳細
				updateRecruitInfo.setRoundDetails(collect.getRoundDetails());
				// 募集人数
				updateRecruitInfo.setRecruitNum(
						StringUtils.isEmpty(collect.getRecruitNum()) ? 0 : Integer.valueOf(collect.getRecruitNum()));
				// 募集範囲
				updateRecruitInfo.setRecruitRange(
						StringUtils.isEmpty(collect.getRecruitRange()) ? 1 : Integer.parseInt(collect.getRecruitRange()));
				// 参加者へのコメント
				updateRecruitInfo.setComents(collect.getComents());
				// 締切日時
				updateRecruitInfo.setCloseDate(collect.getCloseDate());
				// 通知希望フラグ
				updateRecruitInfo.setPushFlag(collect.getPushFlag());
				// 募集進行状況
				updateRecruitInfo
						.setStatus(StringUtils.isEmpty(collect.getStatus()) ? null : Integer.valueOf(collect.getStatus()));
				// 登録日時
				updateRecruitInfo.setRegDate(collect.getRegDate());
				recruitInfoRepository.save(updateRecruitInfo);

				// コース情報を登録する
				CourseInfo courseInfo = new CourseInfo();
				// ID
				courseInfo.setId(collect.getCourseID());
				// コース名
				courseInfo.setName(collect.getCourseName());
				// ゴルフコース名
				courseInfo.setGolfCourseName(collect.getGolfCourseName());
				// 登録する
				courseInfoRepository.save(courseInfo);//保存数据时报错 Unknown column 'e' in 'field list'
				return new DataResult<String>();
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.toString());
				return new DataResult<String>(ResultCode.NG.code(), "募集情報が登録失敗しました。");
			}
		}  /*public DataResult<String> save(Collect collect) {
    try {
      Calendar cal = dateLogicUtils.getCurrent();
      String InsTstmp = dateLogicUtils.getCurrentTimeString(cal);
      // 募集IDを生成する
      String roundSerialNo;
      String sql = null;
      while (true) {
        roundSerialNo = RandomUtils.getRandomNumber(8);
        sql = "select 1 from RecruitInfo where RoundSerialNo = " + roundSerialNo;
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if (list == null || list.size() == 0) {
          collect.setRoundSerialNo(roundSerialNo);
          break;
        }
      }
      sql = "INSERT INTO RecruitInfo (RoundSerialNo, MemberID, CourseID, StartCourse,"
          + " PlayDate, StartTime, PlayStyle, PlayFee, LunchFlag, RoundDetails, RecruitNum, RecruitRange,"
          + " Coments, CloseDate, PushFlag, Status, RegDate)"
          + " VALUES (%s, %s, %s, %s, %s, %s, %s,  %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)";

      String courseId = IdGenerationLogic.getId(10);
      collect.setCourseID(courseId);
      collect.setRegDate(InsTstmp);
      String playStyle = collect.getPlayStyle();
      if (StringUtils.isNotEmpty(playStyle)) {
        collect.setPlayStyle("エンジョイ".equals(playStyle) ? "0" : "1");
      }
      String playDate = collect.getPlayDate();
      if (StringUtils.isNotEmpty(playDate)) {
        playDate = playDate.replaceAll("/", "");
        collect.setPlayDate(playDate);
      }
      String startTime = collect.getStartTime();
      if (StringUtils.isNotEmpty(startTime) && !"###".equals(startTime)) {
        String[] timelist = startTime.split("###");
        if (timelist.length == 2) {
          String startdate = timelist[0].replaceAll("/", "");
          String startminu = timelist[1].replaceAll(":", "");
          collect.setStartTime(startdate + startminu);
        } else {
          collect.setStartTime(null);
        }
      } else {
        collect.setStartTime(null);
      }

      String recruitNum = collect.getRecruitNum();
      if (StringUtils.isNotEmpty(recruitNum)) {
        recruitNum = recruitNum.replace("人", "");
        collect.setRecruitNum(recruitNum);
      }

      String closeTime = collect.getCloseDate();
      if (StringUtils.isNotEmpty(closeTime) && !"###".equals(closeTime)) {
        String[] timelist = closeTime.split("###");
        if (timelist.length == 2) {
          String closedate = timelist[0].replaceAll("/", "");
          String closeminu = timelist[1].replaceAll(":", "");
          collect.setCloseDate(closedate + closeminu);
        } else {
          collect.setCloseDate(null);
        }
      } else {
        collect.setCloseDate(null);
      }
      collect.setStatus(CollectStateCode.INIT.code());
      sql = String.format(sql, nullToString(collect.getRoundSerialNo(), false),
          nullToString(collect.getMemberID(), true), nullToString(collect.getCourseID(), true),
          nullToString("1", false), nullToString(collect.getPlayDate(), true),
          nullToString(collect.getStartTime(), true), nullToString(collect.getPlayStyle(), true),
          nullToString(collect.getPlayFee(), false), nullToString(collect.getLunchFlag(), false),
          nullToString(collect.getRoundDetails(), true),
          nullToString(collect.getRecruitNum(), false),
          nullToString(collect.getRecruitRange(), false), nullToString(collect.getComents(), true),
          nullToString(collect.getCloseDate(), true), nullToString(collect.getPushFlag(), true),
          nullToString(collect.getStatus(), false), nullToString(collect.getRegDate(), true));

      logger.debug("insert RecruitInfo sql:==" + sql);
      jdbcTemplate.execute(sql);

      sql = "INSERT INTO CourseInfo (ID, Name, GolfCourseName)  VALUES (%s, %s, %s)";
      sql = String.format(sql, nullToString(collect.getCourseID(), true),
          nullToString(collect.getCourseName(), true),
          nullToString(collect.getGolfCourseName(), true));
      logger.debug("insert CourseInfo sql:==" + sql);
      jdbcTemplate.execute(sql);

      return new DataResult<String>();
    } catch (Exception e) {
      logger.error(e.getMessage());
      return new DataResult<String>(ResultCode.NG.code(), "募集情報が登録失敗しました。");
    }
  }*/

  /**
   * @param playDate プレー日
   * @param area エリア
   * @param golfBarName ゴルフ場名
   * @param pageMode 0:前頁 1:次頁
   * @param memberId ログイン中のユーザID
   * @param roundSerialNo募集ID
   * @return 募集情報
   */
  
  //TODO 子查询未检测
  @SuppressWarnings("rawtypes")
  public DataResult<CollectSearchResult> search(String playDate, String area, String golfBarName,
      String pageMode, String memberId, String roundSerialNo) {
    // String sql = "select PlayDate from RecruitInfo";
    Calendar curCal = dateLogicUtils.getCurrent();
    if (StringUtils.isNotEmpty(playDate)) {
      playDate = playDate.replaceAll("/|-", "");
    } else {
      // playDate = dateLogicUtils.getDateString(curCal); //
      // プレー日を指定しない場合、システム年月日を設定する
    }

    if (StringUtils.isNotEmpty(pageMode)) { // 前頁 次頁を指定すれあば
      if (MatchingCode.UNMATCHED.code().equals(pageMode)) { // 前頁を指定
        playDate = hasDatePage(playDate, memberId, PRE_DATE);
      } else { // 次頁を指定
        playDate = hasDatePage(playDate, memberId, NEXT_DATE);
      }
    } else {
      // プレー日を指定しない場合、最近の日付を取得する。
      if (StringUtils.isEmpty(playDate)) {
        // システム日付
        String tmpDate = dateLogicUtils.getDateString(curCal);
        playDate = hasDatePage(tmpDate, memberId, CUR_DATE);
        if (StringUtils.isEmpty(playDate)) { //
          playDate = hasDatePage(tmpDate, memberId, NEXT_DATE); // システム日付の最近日付を取得する
          if (StringUtils.isEmpty(playDate)) { //
            playDate = hasDatePage(tmpDate, memberId, PRE_DATE); // システム日付の最近日付を取得する
          }
        } else {
          playDate = tmpDate;
        }
      }
    }
    if (StringUtils.isEmpty(playDate)) {
      // 情報が存在しない
      return new DataResult<CollectSearchResult>("E001", "募集情報がありません");
    }

    Specification<RecruitInfo> specification = new Specification<RecruitInfo>(){

    	//tmp data
    	String playDate = "20190120";
    	
		private static final long serialVersionUID = -3777344393747644910L;
		List<Predicate> predicateList = new ArrayList<Predicate>();
		
		@Override
		public Predicate toPredicate(Root<RecruitInfo> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
			
			CriteriaQuery<RecruitInfo> recruitInfoQuery = criteriaBuilder.createQuery(RecruitInfo.class);
			Root<Applyinfo> applyInfoRoot = query.from(Applyinfo.class);
			Join<CourseInfo,RecruitInfo> courseInfoJoin = root.join("courseInfo", JoinType.INNER);
			Join<Applyinfo,RecruitInfo>  applyInfoJoin = root.join("applyinfo",JoinType.LEFT);
			applyInfoJoin.on(criteriaBuilder.equal(applyInfoRoot.get("memberId"),memberId ));
			//子查询join未写
			/*Subquery<RecruitInfo> subquery = RecruitInfoQuery.subquery(RecruitInfo.class);
			Root<Applyinfo> applyinfoRoot = subquery.from(Applyinfo.class);
			Subquery subqueryWhere = subquery.where(criteriaBuilder.equal(applyinfoRoot.get("apovstatus"), ApovStatusCode.ApovOK.code())).groupBy(applyinfoRoot.get("roundserialno"));*/
			
			predicateList.add(criteriaBuilder.equal(root.get("delFlg"), 0)); 
			predicateList.add(criteriaBuilder.equal(root.get("playDate"), playDate));
			
			if (StringUtils.isNotEmpty(roundSerialNo)) {
				predicateList.add(criteriaBuilder.equal(root.get("roundSerialNo"), roundSerialNo));
			    }
			
		    if (StringUtils.isNotEmpty(memberId)) {
		    	predicateList.add(criteriaBuilder.equal(root.get("memberID"), memberId));
		      }
					
		    predicateList.add(criteriaBuilder.or(criteriaBuilder.isNull(root.get("status")),criteriaBuilder.notEqual(root.get("status"), CollectStateCode.DELETE.code())));
			
			return recruitInfoQuery.select(courseInfoJoin).select(applyInfoJoin).where(
					criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]))).getRestriction();
		}};

	  try{
      List<RecruitInfo> list = recruitInfoRepository.findAll(specification);
      if (list == null || list.size() == 0) {
        // 情報が存在しない
        return new DataResult<CollectSearchResult>("E001", "募集情報がありません");
      }
      CollectSearchResult resultList = new CollectSearchResult();
      resultList.setHasPrePage(hasDatePage(playDate, memberId, PRE_DATE) != null);
      resultList.setHasNextPage(hasDatePage(playDate, memberId, NEXT_DATE) != null);
      //resultList.setSearchResultList(list);
      resultList.setSearchPlayDate(playDate);
      return new DataResult<CollectSearchResult>(resultList);
    } catch (Exception e) {
      logger.error(e.getMessage());
      return new DataResult<CollectSearchResult>(ResultCode.NG.code(), "募集情報が取得失敗しました。");
    }
  }
  
/*  @SuppressWarnings("rawtypes")
  public DataResult<CollectSearchResult> search(String playDate, String area, String golfBarName,
      String pageMode, String memberId, String roundSerialNo) {
    // String sql = "select PlayDate from RecruitInfo";
    Calendar curCal = dateLogicUtils.getCurrent();
    if (StringUtils.isNotEmpty(playDate)) {
      playDate = playDate.replaceAll("/|-", "");
    } else {
      // playDate = dateLogicUtils.getDateString(curCal); //
      // プレー日を指定しない場合、システム年月日を設定する
    }

    if (StringUtils.isNotEmpty(pageMode)) { // 前頁 次頁を指定すれあば
      if (MatchingCode.UNMATCHED.code().equals(pageMode)) { // 前頁を指定
        playDate = hasDatePage(playDate, memberId, PRE_DATE);
      } else { // 次頁を指定
        playDate = hasDatePage(playDate, memberId, NEXT_DATE);
      }
    } else {
      // プレー日を指定しない場合、最近の日付を取得する。
      if (StringUtils.isEmpty(playDate)) {
        // システム日付
        String tmpDate = dateLogicUtils.getDateString(curCal);
        playDate = hasDatePage(tmpDate, memberId, CUR_DATE);
        if (StringUtils.isEmpty(playDate)) { //
          playDate = hasDatePage(tmpDate, memberId, NEXT_DATE); // システム日付の最近日付を取得する
          if (StringUtils.isEmpty(playDate)) { //
            playDate = hasDatePage(tmpDate, memberId, PRE_DATE); // システム日付の最近日付を取得する
          }
        } else {
          playDate = tmpDate;
        }
      }
    }

    if (StringUtils.isEmpty(playDate)) {
      // 情報が存在しない
      return new DataResult<CollectSearchResult>("E001", "募集情報がありません");
    }
    StringBuffer sbWhere = new StringBuffer();
    String sql = "select RecruitInfo.*,CourseInfo.GolfCourseName, CourseInfo.`Name` AS CourseName , ApplyInfo.ApovStatus , APOVCnt.ApovCnt "
        + " from RecruitInfo INNER JOIN CourseInfo ON CourseInfo.ID = RecruitInfo.CourseID"
        + " LEFT JOIN ApplyInfo ON ApplyInfo.RoundSerialNo =RecruitInfo.RoundSerialNo AND  ApplyInfo.MemberID = '"
        + memberId + "'"
        + " LEFT JOIN (select count(0) as ApovCnt, RoundSerialNo from ApplyInfo where ApovStatus = '"
        + ApovStatusCode.ApovOK.code() + "' group by RoundSerialNo) APOVCnt"
        + " ON APOVCnt.RoundSerialNo =RecruitInfo.RoundSerialNo ";

    sbWhere.append("RecruitInfo.DelFlg = '0' and ");
    sbWhere.append("PlayDate= '" + playDate + "' and ");
    if (StringUtils.isNotEmpty(roundSerialNo)) {
      sbWhere.append(" RecruitInfo.RoundSerialNo= '" + roundSerialNo + "' and ");
    }
    if (StringUtils.isNotEmpty(memberId)) {
      sbWhere.append(" RecruitInfo.MemberID != '" + memberId + "' and ");
    }
    sbWhere.append(" (RecruitInfo.Status  is null OR  RecruitInfo.Status  != "
        + CollectStateCode.DELETE.code() + ")");

    sql = sql + " where " + sbWhere.toString();
    logger.debug("search sql:==" + sql);
    try {
      List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
      if (list == null || list.size() == 0) {
        // 情報が存在しない
        return new DataResult<CollectSearchResult>("E001", "募集情報がありません");
      }
      CollectSearchResult resultList = new CollectSearchResult();
      resultList.setHasPrePage(hasDatePage(playDate, memberId, PRE_DATE) != null);
      resultList.setHasNextPage(hasDatePage(playDate, memberId, NEXT_DATE) != null);
      resultList.setSearchResultList(list);
      resultList.setSearchPlayDate(playDate);
      return new DataResult<CollectSearchResult>(resultList);
    } catch (Exception e) {
      logger.error(e.getMessage());
      return new DataResult<CollectSearchResult>(ResultCode.NG.code(), "募集情報が取得失敗しました。");
    }
  }*/

  /**
   * 自分の応募・募集情報を検索する
   *
   * @param memberId
   * @param collectFlg
   * @return
   */
  
  @SuppressWarnings("rawtypes")
  public DataResult<List> searchSelfData(String memberId, String collectFlg) {
	  
	  List<Map<String,Object>> list = null;
	  //Calendar curCal = dateLogicUtils.getCurrent();
	  //String playDate = dateLogicUtils.getDateString(curCal);
	  String playDate = "20181220";
	  
    if (StringUtils.isEmpty(memberId)) {
      return new DataResult<List>("E001", "ユーザ情報がありません");
    }
    // collectFlg=0 募集情報
    if (MatchingCode.UNMATCHED.code().equals(collectFlg)) {
    	
    	list = recruitInfoRepository.findRecruitInfoAtCollectFlgEqualUNMATCHED(ApovStatusCode.ApovOK.code(), memberId, playDate, Integer.valueOf(CollectStateCode.DELETE.code()));
      
    } else {
    	
    	list = recruitInfoRepository.findRecruitInfoAtCollectFlgNOTEqualUNMATCHED(ApovStatusCode.ApovOK.code(), memberId, playDate, Integer.valueOf(CollectStateCode.DELETE.code()));
    	
    }
    try {
      // 情報が存在しない
      return new DataResult<List>(list);
    } catch (Exception e) {
      logger.error(e.getMessage());
      return new DataResult<List>(ResultCode.NG.code(), "募集情報が取得失敗しました。");
    }
  }
  
  
 /* @SuppressWarnings("rawtypes")
  public DataResult<List> searchSelfData(String memberId, String collectFlg) {
    if (StringUtils.isEmpty(memberId)) {
      return new DataResult<List>("E001", "ユーザ情報がありません");
    }
    String sql = null;
    StringBuffer sbWhere = new StringBuffer();
    // collectFlg=0 募集情報
    if (MatchingCode.UNMATCHED.code().equals(collectFlg)) {
      sql = "select RecruitInfo.*,CourseInfo.GolfCourseName, CourseInfo.`Name` AS CourseName , '' as ApovStatus , APOVCnt.ApovCnt "
          + " from RecruitInfo INNER JOIN CourseInfo ON CourseInfo.ID = RecruitInfo.CourseID"
          + " LEFT JOIN (select count(0) as ApovCnt, RoundSerialNo from ApplyInfo where ApovStatus = '"
          + ApovStatusCode.ApovOK.code() + "' group by RoundSerialNo) APOVCnt"
          + " ON APOVCnt.RoundSerialNo =RecruitInfo.RoundSerialNo ";
      sbWhere.append(" RecruitInfo.MemberID = '" + memberId + "' and ");
    } else {
      sql = "select RecruitInfo.*,CourseInfo.GolfCourseName, CourseInfo.`Name` AS CourseName , ApplyInfo.ApovStatus , APOVCnt.ApovCnt "
          + " from RecruitInfo INNER JOIN CourseInfo ON CourseInfo.ID = RecruitInfo.CourseID"
          + " INNER JOIN ApplyInfo ON ApplyInfo.RoundSerialNo =RecruitInfo.RoundSerialNo AND  ApplyInfo.MemberID = '"
          + memberId + "'"
          + " LEFT JOIN (select count(0) as ApovCnt, RoundSerialNo from ApplyInfo where ApovStatus = '"
          + ApovStatusCode.ApovOK.code() + "' group by RoundSerialNo) APOVCnt"
          + " ON APOVCnt.RoundSerialNo =RecruitInfo.RoundSerialNo ";
    }
    sbWhere.append("RecruitInfo.DelFlg = '0' and ");
    Calendar curCal = dateLogicUtils.getCurrent();
    String playDate = dateLogicUtils.getDateString(curCal);
    // プレー日が未来日だけ
    sbWhere.append(" RecruitInfo.PlayDate >= '" + playDate + "' and ");
    sbWhere.append(" (RecruitInfo.Status  is null OR  RecruitInfo.Status  != "
        + CollectStateCode.DELETE.code() + ")");

    sql = sql + " where " + sbWhere.toString();
    logger.debug("search RecruitInfosql:==" + sql);
    try {
      List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
      // 情報が存在しない
      return new DataResult<List>(list);
    } catch (Exception e) {
      logger.error(e.getMessage());
      return new DataResult<List>(ResultCode.NG.code(), "募集情報が取得失敗しました。");
    }
  }*/

  /**
   * 自分の応募情報を取消する
   *
   * @param playDate
   * @param area
   * @param golfBarName
   * @param pageMode
   * @param memberId
   * @param roundSerialNo
   * @return
   */
  
  public DataResult<CollectSearchResult> cancel(String playDate, String area, String golfBarName,
	      String pageMode, String memberId, String roundSerialNo) {
	  
	// 自分の応募情報を検索する
			List<Applyinfo> applyInfoList = applyInfoRepository.findByMemberIdAndRoundSerialNoAndApovStatus(memberId,
					Integer.parseInt(roundSerialNo), ApovStatusCode.UNApov.code());
			if (applyInfoList == null || applyInfoList.size() == 0) {
				return new DataResult<CollectSearchResult>(ResultCode.NG.code(), "この応募が取消できません。");
			}
			int regNo = applyInfoList.get(0).getRegNo();
			
			// 募集情報の削除処理
			//applyInfoRepository.deleteByRoundSerialNoAndRegNo(Integer.parseInt(roundSerialNo), regNo);//加了@Transactional还是报错No EntityManager with actual transaction available for current thread - cannot reliably process 'remove' call
			applyInfoRepository.delete(applyInfoList.get(0));
			// 募集情報を返却する
			return search(playDate, area, golfBarName, pageMode, memberId, roundSerialNo);
	  }
  
  
  
/*  public DataResult<CollectSearchResult> cancel(String playDate, String area, String golfBarName,
      String pageMode, String memberId, String roundSerialNo) {
    // 自分の応募情報を検索する
    String sql = "select RegNo from ApplyInfo where MemberID = %s  AND RoundSerialNo= %s  AND ApovStatus = '"
        + ApovStatusCode.UNApov.code() + "'";
    List<Map<String, Object>> list = null;
    sql = String.format(sql, nullToString(memberId, true), nullToString(roundSerialNo, false));
    logger.debug("cancel select  ApplyInfo sql:==" + sql);
    // 応募情報があるかを確認する
    list = jdbcTemplate.queryForList(sql);
    if (list == null || list.size() == 0) {
      return new DataResult<CollectSearchResult>(ResultCode.NG.code(), "この応募が取消できません。");
    }
    String regNo = StringUtils.toStr(list.get(0).get("RegNo"));
    sql = "delete from ApplyInfo where RoundSerialNo= %s  AND RegNo = %s ";
    sql = String.format(sql, nullToString(roundSerialNo, false), nullToString(regNo, false));
    logger.debug("delete ApplyInfo sql:==" + sql);
    jdbcTemplate.execute(sql);
    
    
    
    return search(playDate, area, golfBarName, pageMode, memberId, roundSerialNo);
  }*/

  /**
   * 自分の応募情報を更新する(delFlg)
   *
   * @param roundSerialNo
   * @return
   */
  public DataResult<String> updateDelFlg(String roundSerialNo) {
	  
	  	RecruitInfo recruitInfo = recruitInfoRepository.findByRoundSerialNo(Integer.parseInt(roundSerialNo));
	  	recruitInfo.setDelFlg(1);
	  	recruitInfoRepository.save(recruitInfo);
	  
	    return new DataResult<String>();
	  }
 /* public DataResult<String> updateDelFlg(String roundSerialNo) {
    String sql = "UPDATE RecruitInfo SET DelFlg = '1' where RoundSerialNo= %s ";
    sql = String.format(sql, nullToString(roundSerialNo, false));
    logger.debug("update RecruitInfo sql:==" + sql);
    jdbcTemplate.execute(sql);
    return new DataResult<String>();
  }*/

  /**
   * 自分の応募情報を更新する
   *
   * @param roundSerialNo
   * @return
   */
  
	public DataResult<String> updateDetail(Collect collect) {
		logger.debug("startTime before:" + collect.getStartTime());
		RecruitInfo recruitInfo = recruitInfoRepository
				.findByRoundSerialNo(Integer.parseInt(collect.getRoundSerialNo()));
		if (recruitInfo == null) {
			return new DataResult<String>(ResultCode.NG.code(), "この応募情報がありません。");
		}
		// コースIDを取得する
		String courseID = recruitInfo.getCourseID();

		String playStyle = collect.getPlayStyle();
		if (StringUtils.isNotEmpty(playStyle)) {
			collect.setPlayStyle("エンジョイ".equals(playStyle) ? "0" : "1");
		}
		String playDate = collect.getPlayDate();
		if (StringUtils.isNotEmpty(playDate)) {
			playDate = playDate.replaceAll("/", "");
			collect.setPlayDate(playDate);
		}

		if (StringUtils.isNotEmpty(collect.getStartTime()) && !"###".equals(collect.getStartTime())) {
			String startTime = collect.getStartTime().trim();
			String[] timelist = startTime.split("###");
			if (timelist.length == 2) {
				String startdate = timelist[0].replaceAll("/", "");
				String startminu = timelist[1].replaceAll(":", "");
				collect.setStartTime(startdate + startminu);
			} else {
				collect.setStartTime(null);
			}
		} else {
			collect.setStartTime(null);
		}
		logger.debug("startTime after:" + collect.getStartTime());

		String recruitNum = collect.getRecruitNum();
		if (StringUtils.isNotEmpty(recruitNum)) {
			recruitNum = recruitNum.replace("人", "");
			collect.setRecruitNum(recruitNum);
		}

		if (StringUtils.isNotEmpty(collect.getCloseDate()) && !"###".equals(collect.getCloseDate())) {
			String closeTime = collect.getCloseDate().trim();
			String[] timelist = closeTime.split("###");
			if (timelist.length == 2) {
				String closedate = timelist[0].replaceAll("/", "");
				String closeminu = timelist[1].replaceAll(":", "");
				collect.setCloseDate(closedate + closeminu);
			} else {
				collect.setCloseDate(null);
			}
		} else {
			collect.setCloseDate(null);
		}

		// 募集情報を更新する
		recruitInfo.setStartCourse(1);
		recruitInfo.setPlayDate(collect.getPlayDate());
		recruitInfo.setStartTime(collect.getStartTime());
		recruitInfo.setPlayStyle(collect.getPlayStyle());
		recruitInfo.setPlayFee(StringUtils.isEmpty(collect.getPlayFee()) ? 0 : Integer.valueOf(collect.getPlayFee()));
		recruitInfo.setLunchFlag(
				StringUtils.isEmpty(collect.getLunchFlag()) ? 0 : Integer.valueOf(collect.getLunchFlag()));
		recruitInfo.setRoundDetails(collect.getRoundDetails());
		recruitInfo.setRecruitNum(
				StringUtils.isEmpty(collect.getRecruitNum()) ? 0 : Integer.valueOf(collect.getRecruitNum()));
		recruitInfo.setRecruitRange(
				StringUtils.isEmpty(collect.getRecruitRange()) ? 1 : Integer.parseInt(collect.getRecruitRange()));
		recruitInfo.setComents(collect.getComents());
		recruitInfo.setCloseDate(collect.getCloseDate());
		recruitInfo.setPushFlag(collect.getPushFlag());
		// 更新処理を行う
		recruitInfoRepository.save(recruitInfo);

		// コース情報を更新する
		courseInfoRepository.updateCourseInfo(collect.getCourseName(), collect.getGolfCourseName(), courseID);
		return new DataResult<String>(ResultCode.OK.code(), "この応募情報が更新しました。");
	}
  
  /*public DataResult<String> updateDetail(Collect collect) {
	    logger.debug("startTime before:" + collect.getStartTime());
	    
	 // 応募情報があるかを確認する
	    //报错：Can not set int field org.leadingsoft.golf.api.entity.RecruitInfo.status to null value
	    RecruitInfo recruitInfo = recruitInfoRepository.findByRoundSerialNo(Integer.parseInt(nullToString(collect.getRoundSerialNo(), false)));
	    
	    if (recruitInfo == null) {
	      return new DataResult<String>(ResultCode.NG.code(), "この応募情報がありません。");
	    }
	    
	    
	    String courseID = recruitInfo.getCourseID();

	    String playStyle = collect.getPlayStyle();
	    if (StringUtils.isNotEmpty(playStyle)) {
	      collect.setPlayStyle("エンジョイ".equals(playStyle) ? "0" : "1");
	    }
	    String playDate = collect.getPlayDate();
	    if (StringUtils.isNotEmpty(playDate)) {
	      playDate = playDate.replaceAll("/", "");
	      collect.setPlayDate(playDate);
	    }

	    if (StringUtils.isNotEmpty(collect.getStartTime()) && !"###".equals(collect.getStartTime())) {
	      String startTime = collect.getStartTime().trim();
	      String[] timelist = startTime.split("###");
	      if (timelist.length == 2) {
	        String startdate = timelist[0].replaceAll("/", "");
	        String startminu = timelist[1].replaceAll(":", "");
	        collect.setStartTime(startdate + startminu);
	      } else {
	        collect.setStartTime(null);
	      }
	    } else {
	      collect.setStartTime(null);
	    }
	    logger.debug("startTime after:" + collect.getStartTime());

	    String recruitNum = collect.getRecruitNum();
	    if (StringUtils.isNotEmpty(recruitNum)) {
	      recruitNum = recruitNum.replace("人", "");
	      collect.setRecruitNum(recruitNum);
	    }

	    if (StringUtils.isNotEmpty(collect.getCloseDate()) && !"###".equals(collect.getCloseDate())) {
	      String closeTime = collect.getCloseDate().trim();
	      String[] timelist = closeTime.split("###");
	      if (timelist.length == 2) {
	        String closedate = timelist[0].replaceAll("/", "");
	        String closeminu = timelist[1].replaceAll(":", "");
	        collect.setCloseDate(closedate + closeminu);
	      } else {
	        collect.setCloseDate(null);
	      }
	    } else {
	      collect.setCloseDate(null);
	    }

	    recruitInfo.setStartCourse(Integer.parseInt(nullToString("1", false)));
	    recruitInfo.setPlayDate(nullToString(collect.getPlayDate(), true));
	    recruitInfo.setStartTime(nullToString(collect.getStartTime(), true));
	    recruitInfo.setPlayStyle(nullToString(collect.getPlayStyle(), true));
	    recruitInfo.setPlayFee(Integer.parseInt(nullToString(collect.getPlayFee(), false)));
	    recruitInfo.setLunchFlag(Integer.parseInt(nullToString(collect.getLunchFlag(), false)));
	    recruitInfo.setRoundDetails(nullToString(collect.getRoundDetails(), true));
	    recruitInfo.setRecruitNum(Integer.parseInt(nullToString(collect.getRecruitNum(), false)));
	    recruitInfo.setRecruitRange(Integer.parseInt(nullToString(collect.getRecruitRange(), false)));
	    recruitInfo.setComents(nullToString(collect.getComents(), true));
	    recruitInfo.setCloseDate(nullToString(collect.getCloseDate(), true));
	    recruitInfo.setPushFlag(nullToString(collect.getPushFlag(), true));
	    recruitInfoRepository.save(recruitInfo);
	    
	    CourseInfo courseInfo = courseInfoRepository.findById(nullToString(courseID, true));
	    
	    courseInfo.setName(nullToString(collect.getCourseName(), true));
	    courseInfo.setGolfCourseName(nullToString(collect.getGolfCourseName(), true));
	    courseInfoRepository.save(courseInfo);
	    
	    return new DataResult<String>(ResultCode.OK.code(), "この応募情報が更新しました。");
	  }
*/  
  
  /*public DataResult<String> updateDetail(Collect collect) {
    logger.debug("startTime before:" + collect.getStartTime());
    String sql = "SELECT CourseID FROM RecruitInfo WHERE RoundSerialNo = %s";
    List<Map<String, Object>> list = null;
    sql = String.format(sql, nullToString(collect.getRoundSerialNo(), false));
    logger.debug("updateDetail select RecruitInfo sql:==" + sql);
    // 応募情報があるかを確認する
    list = jdbcTemplate.queryForList(sql);
    if (list == null || list.size() == 0) {
      return new DataResult<String>(ResultCode.NG.code(), "この応募情報がありません。");
    }
    String courseID = StringUtils.toStr(list.get(0).get("CourseID"));

    String playStyle = collect.getPlayStyle();
    if (StringUtils.isNotEmpty(playStyle)) {
      collect.setPlayStyle("エンジョイ".equals(playStyle) ? "0" : "1");
    }
    String playDate = collect.getPlayDate();
    if (StringUtils.isNotEmpty(playDate)) {
      playDate = playDate.replaceAll("/", "");
      collect.setPlayDate(playDate);
    }

    if (StringUtils.isNotEmpty(collect.getStartTime()) && !"###".equals(collect.getStartTime())) {
      String startTime = collect.getStartTime().trim();
      String[] timelist = startTime.split("###");
      if (timelist.length == 2) {
        String startdate = timelist[0].replaceAll("/", "");
        String startminu = timelist[1].replaceAll(":", "");
        collect.setStartTime(startdate + startminu);
      } else {
        collect.setStartTime(null);
      }
    } else {
      collect.setStartTime(null);
    }
    logger.debug("startTime after:" + collect.getStartTime());

    String recruitNum = collect.getRecruitNum();
    if (StringUtils.isNotEmpty(recruitNum)) {
      recruitNum = recruitNum.replace("人", "");
      collect.setRecruitNum(recruitNum);
    }

    if (StringUtils.isNotEmpty(collect.getCloseDate()) && !"###".equals(collect.getCloseDate())) {
      String closeTime = collect.getCloseDate().trim();
      String[] timelist = closeTime.split("###");
      if (timelist.length == 2) {
        String closedate = timelist[0].replaceAll("/", "");
        String closeminu = timelist[1].replaceAll(":", "");
        collect.setCloseDate(closedate + closeminu);
      } else {
        collect.setCloseDate(null);
      }
    } else {
      collect.setCloseDate(null);
    }

    //
    sql = "UPDATE RecruitInfo SET StartCourse = %s,PlayDate = %s,StartTime = %s,PlayStyle = %s,PlayFee = %s,LunchFlag = %s,RoundDetails = %s,RecruitNum = %s,RecruitRange = %s,Coments = %s,CloseDate = %s,PushFlag = %s where RoundSerialNo= %s";
    sql = String.format(sql, nullToString("1", false), nullToString(collect.getPlayDate(), true),
        nullToString(collect.getStartTime(), true), nullToString(collect.getPlayStyle(), true),
        nullToString(collect.getPlayFee(), false), nullToString(collect.getLunchFlag(), false),
        nullToString(collect.getRoundDetails(), true), nullToString(collect.getRecruitNum(), false),
        nullToString(collect.getRecruitRange(), false), nullToString(collect.getComents(), true),
        nullToString(collect.getCloseDate(), true), nullToString(collect.getPushFlag(), true),
        nullToString(collect.getRoundSerialNo(), false));
    logger.debug("update RecruitInfo sql:==" + sql);
    jdbcTemplate.execute(sql);
    //
    sql = "UPDATE CourseInfo SET NAME = %s, GolfCourseName = %s where ID = %s";
    sql = String.format(sql, nullToString(collect.getCourseName(), true),
        nullToString(collect.getGolfCourseName(), true), nullToString(courseID, true));
    jdbcTemplate.execute(sql);
    return new DataResult<String>(ResultCode.OK.code(), "この応募情報が更新しました。");
  }*/

  /**
   * データがあるか
   *
   * @param playDate
   * @param isPre
   * @return
   */
  //TODO 无接口测试
  private String hasDatePage(String playDate, String memberId, String selectFlg) {

	  String sortType = "ASC";
	    
	    Specification<RecruitInfo> specification = new Specification<RecruitInfo>(){

			private static final long serialVersionUID = 1L;
			
			@Override
			public Predicate toPredicate(Root<RecruitInfo> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				
				String sortType = "ASC";
				List<Predicate> predicateList = new ArrayList<Predicate>();
				
				
			    if (CUR_DATE.equals(selectFlg)) {
			    	predicateList.add(criteriaBuilder.equal(root.get("playDate"),playDate));
			    }else{
				      if (PRE_DATE.equals(selectFlg)) {
				    	  predicateList.add(criteriaBuilder.lessThan(root.get("playDate"), playDate));
				    	  sortType = "DESC";
				      }
				      predicateList.add(criteriaBuilder.greaterThan(root.get("playDate"), playDate));
			    }
			    if (StringUtils.isNotEmpty(memberId)) {
			    	predicateList.add(criteriaBuilder.notEqual(root.get("memberID"), memberId));
				    }
			    predicateList.add(criteriaBuilder.or(criteriaBuilder.isNotNull(root.get("status")),criteriaBuilder.notEqual(root.get("status"), CollectStateCode.DELETE.code())));
			    
			    Predicate[] pre = new Predicate[predicateList.size()];
			    query.where(predicateList.toArray(pre));
			    if(sortType.equals("ASC")){
			    	query.orderBy(criteriaBuilder.asc(root.get("playDate").as(String.class)));
					  return query.getRestriction();
				  }
			    query.orderBy(criteriaBuilder.desc(root.get("playDate").as(String.class)));
			    return query.getRestriction();
			}
		  };
		  
		 // List<RecruitInfo> list = recruitInfoRepository.findAll(specification);
		List<RecruitInfo> list = recruitInfoRepository.findAll(specification,new Sort(Direction.DESC, "playDate"));

	    String otherPlayDate = null;
	    if (list != null && list.size() > 0) {
	      otherPlayDate = StringUtils.toStr(list.get(0).getPlayDate());
	    }
	    return otherPlayDate;
	  }

  
  /*private String hasDatePage(String playDate, String memberId, String selectFlg) {
    StringBuffer sql = new StringBuffer();
    String sortType = "ASC";
    sql.append("select PlayDate from RecruitInfo ");
    if (CUR_DATE.equals(selectFlg)) {
      sql.append(" where PlayDate =  '" + playDate + "'");
    } else {
      String compareStr = ">";
      if (PRE_DATE.equals(selectFlg)) {
        compareStr = "<";
        sortType = "DESC";
      }
      sql.append(" where PlayDate " + compareStr + " '" + playDate + "'");
    }

    if (StringUtils.isNotEmpty(memberId)) {
      sql.append("  and MemberID != '" + memberId + "' ");
    }
    sql.append(" and (RecruitInfo.Status  is null OR  RecruitInfo.Status  != "
        + CollectStateCode.DELETE.code() + ")");
    sql.append(" order by PlayDate " + sortType + " limit 1");

    List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString());
    String otherPlayDate = null;
    if (list != null && list.size() > 0) {
      otherPlayDate = StringUtils.toStr(list.get(0).get("PlayDate"));
    }
    return otherPlayDate;
  }

  private static String nullToString(String target, boolean isString) {
    if (StringUtils.isEmpty(target)) {
      return null;
    }
    return isString ? "'" + target + "'" : target;
  }*/

  
  
	  public DataResult<CollectSearchResult> deleteSelfApplyInfo(String roundSerialNo,
		      String memberId) {
		    // 自分の応募情報を検索する
		    Specification<Applyinfo> specification = new Specification<Applyinfo>(){

				private static final long serialVersionUID = 1L;
				
				@Override
				public Predicate toPredicate(Root<Applyinfo> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
					
					List<Predicate> predicateList = new ArrayList<Predicate>();

					predicateList.add(criteriaBuilder.equal(root.get("memberId"),nullToString(memberId, true)));
					predicateList.add(criteriaBuilder.equal(root.get("roundSerialNo"),nullToString(roundSerialNo, false)));
					predicateList.add(criteriaBuilder.equal(root.get("apovStatus"), ApovStatusCode.UNApov.code()));
					
					Predicate[] pre = new Predicate[predicateList.size()];
					query.where(predicateList.toArray(pre));
					return query.getRestriction();
				}
			  };
		  
			 List<Applyinfo> applyinfoList = applyInfoRepository.findAll(specification);
		  
		    if (applyinfoList == null || applyinfoList.size() == 0) {
		      return new DataResult<CollectSearchResult>(ResultCode.NG.code(), "この応募が取消できません。");
		    }
		    int regNo =applyinfoList.get(0).getRegNo();
		    //TODO 添加事物 还是报错No EntityManager with actual transaction available for current thread - cannot reliably process 'remove' call
		    applyInfoRepository.deleteByRoundSerialNoAndRegNo(Integer.parseInt(nullToString(roundSerialNo, false)), regNo);
		    
		    return new DataResult<CollectSearchResult>(ResultCode.OK.code(), "この応募が取消しました。");
		  } 
	  
	  
  /*public DataResult<CollectSearchResult> deleteSelfApplyInfo(String roundSerialNo,
      String memberId) {
    // 自分の応募情報を検索する
    String sql = "select RegNo from ApplyInfo where MemberID = %s  AND RoundSerialNo= %s  AND ApovStatus = '"
        + ApovStatusCode.UNApov.code() + "'";
    List<Map<String, Object>> list = null;
    sql = String.format(sql, nullToString(memberId, true), nullToString(roundSerialNo, false));
    logger.debug("cancel select  ApplyInfo sql:==" + sql);
    // 応募情報があるかを確認する
    list = jdbcTemplate.queryForList(sql);
    if (list == null || list.size() == 0) {
      return new DataResult<CollectSearchResult>(ResultCode.NG.code(), "この応募が取消できません。");
    }
    String regNo = StringUtils.toStr(list.get(0).get("RegNo"));
    sql = "delete from ApplyInfo where RoundSerialNo= %s  AND RegNo = %s ";
    sql = String.format(sql, nullToString(roundSerialNo, false), nullToString(regNo, false));
    logger.debug("delete ApplyInfo sql:==" + sql);
    jdbcTemplate.execute(sql);
    return new DataResult<CollectSearchResult>(ResultCode.OK.code(), "この応募が取消しました。");
  }
*/
	  
	  public DataResult<List> searchRakutenGolfCourseInformation() {
		  List<RakutenGolfCourseInfo> rakutenGolfCourseInfoList =  rakutenGolfCourseInfoRepository.findAll();
		  return new DataResult<List>(rakutenGolfCourseInfoList);
		  }
	  
  /*public DataResult<List> searchRakutenGolfCourseInformation() {
    String sql = "select GolfCourseId, GolfCourseName, GolfCourseAbbr from RakutenGolfCourseInfo";
    logger.debug("select RakutenGolfCourseInfo sql:==" + sql);
    List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
    return new DataResult<List>(list);
  }*/
  private static String nullToString(String target, boolean isString) {
	    if (StringUtils.isEmpty(target)) {
	      return null;
	    }
	    //return isString ? "'" + target + "'" : target;
	    return target;
	  }
}
