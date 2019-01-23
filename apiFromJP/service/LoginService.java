package org.leadingsoft.golf.api.service;

import java.util.Calendar;
import java.util.List;

import org.leadingsoft.golf.api.code.ResultCode;
import org.leadingsoft.golf.api.code.SexCode;
import org.leadingsoft.golf.api.entity.MemberInfo;
import org.leadingsoft.golf.api.entity.SNSInfo;
import org.leadingsoft.golf.api.entity.Temporary;
import org.leadingsoft.golf.api.model.DataResult;
import org.leadingsoft.golf.api.model.Profile;
import org.leadingsoft.golf.api.repository.MemberInfoRepository;
import org.leadingsoft.golf.api.repository.SNSInfoRepository;
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
import org.springframework.stereotype.Service;

@Service
public class LoginService {
  private final static Logger logger = LoggerFactory.getLogger(LoginService.class);
  @Autowired
  private SendMailUtils sendMailUtils;
  @Autowired
  private DateLogicUtils dateLogicUtils;

	// 会員情報リポジトリ
	@Autowired
	private MemberInfoRepository memberInfoRepository;

	// SNS情報リポジトリ
	@Autowired
	private SNSInfoRepository snsInfoRepository;

	// 一時テーブルリポジトリ
	@Autowired
	private TemporaryRepository temporaryRepository;

	/**
	 * 会員情報を取得する
	 *
	 * @param nameOrEmail 氏名又はメールアロレス
	 * @return 会員情報リスト
	 */
	public List<MemberInfo> selectMemberInfoByName(String nameOrEmail) {
		// ユーザ名前とメールで会員情報を検索する
		List<MemberInfo> memberInfoList = memberInfoRepository.findByNameOrEmail(nameOrEmail, nameOrEmail);
		return memberInfoList;
	}

	/**
	 * ゲスト会員の登録処理
	 *
	 * @param id SNSID
	 * @return 会員ID
	 */
	public String selectMemberInfoByGuestId(String id) {
		// 入力SNSのIDでSNS情報を検索する
		List<SNSInfo> snsInfoList = snsInfoRepository.findBySNSID(id);
		// 会員IDを初期化する
		String memberId = "";
		// SNSIDに対するSNS情報存在する場合
		if (snsInfoList != null && snsInfoList.size() > 0) {
			memberId = snsInfoList.get(0).getMemberID();
			return memberId;
		} else {
			// データが存在しない場合
			try {
				// 会員IDを新規作成する
				memberId = IdGenerationLogic.getId();
				// SNS情報を登録する
				SNSInfo snsInfo = new SNSInfo();
				// 会員ID
				snsInfo.setMemberID(memberId);
				// SNSID
				snsInfo.setSNSID(id);
				// タイプ
				snsInfo.setSNSType(0);
				// 認証フラグ
				snsInfo.setAuthFlag(1);
				// SNS情報を登録する
				snsInfoRepository.save(snsInfo);
				// 新規登録された会員IDを返却する
				return memberId;
			} catch (Exception e) {
				// 会員ID新規作成失敗の場合
				logger.error(e.getMessage());
				return null;
			}
		}
	}

	/**
	 * 会員登録処理
	 *
	 * @param email メールアドレス
	 * @param name 名前
	 * @param password パスワード
	 * @param authCode 認証コード
	 * @param dummyMemberId ダミー会員ID
	 * 
	 * @return 登録された会員ID
	 */
	public DataResult<String> insertUserData(String email, String name, String password, String authCode,
			String dummyMemberId) {
		// IDより、一時テーブル情報を取得する
		List<Temporary> temporaryList = temporaryRepository.findByID(dummyMemberId);
		// データが存在しない場合
		if (temporaryList == null || temporaryList.size() == 0) {
			// 認証情報が存在しない
			return new DataResult<String>("E001", "認証情報がない、再度発行お願いします");
		}
		// 上記の検索結果から一番目レコードから有効期間、認証コード、メール情報を取得する
		// 有効期間を取得する
		String expire = temporaryList.get(0).getExpire();
		// 認証コード
		String dbAuthCode = temporaryList.get(0).getTemporaryValue();
		// メール情報
		String dbEmail = temporaryList.get(0).getTemporaryName();
		// 有効期間のチェック
		if (dateLogicUtils.getCurrentTimeString().compareTo(expire) > 0) {
			// 認証情報の期間切れ
			return new DataResult<String>("E002", "認証期限きれ、再度発行お願いします");
		}
		// 認証コードのチェック
		if (!authCode.equals(dbAuthCode)) {
			// 認証コード違う
			return new DataResult<String>("E003", "認証コードが違う、再度発行お願いします");
		}
		// メール情報のチェック
		// 認証コードのメール情報が一致しない
		if (!email.equals(dbEmail)) {
			// メール情報違う
			return new DataResult<String>("E004", "メール情報が違う、再度発行お願いします");
		}
		// メールアドレスの存在チェック
		boolean mailExistFlg = memberInfoRepository.existsByEmail(email);
		if (mailExistFlg) {
			return new DataResult<String>("E004", "データが存在しました。別のメールを利用お願いします");
		}
		// 会員情報データ登録
		try {
			String memberId = IdGenerationLogic.getId();
			// ユーザ登録する
			MemberInfo memerInfo = new MemberInfo();
			// 会員ID
			memerInfo.setMemberID(memberId);
			// メールアドレス
			memerInfo.setEmail(email);
			// パスワード
			memerInfo.setPassWord(PasswordUtils.getEncoderPassword(password));
			// 表示名
			memerInfo.setNickName(name);
			memberInfoRepository.save(memerInfo);
			// 登録された会員IDを返却する
			return new DataResult<String>(memberId);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new DataResult<String>("E999", "データ登録が失敗しました。");
		}
	}

	/***
	 * 認証コードをメール送信する
	 *
	 * @param email メールアドレス
	 * 
	 * @return dummyMemberId ダミー会員ID
	 */
	public DataResult<String> sendAuthCode(String email) {
		try {
			// メールアドレスの存在チェック
			boolean emailExistFlg = memberInfoRepository.existsByEmail(email);
			// メールアドレスが存在する場合
			if (emailExistFlg) {
				return new DataResult<String>(ResultCode.NG.code(), "データが存在しました。別のメールを利用お願いします");
			}
			// ダミー会員IDを作成する
			String dummyMemberId = "_dummy" + IdGenerationLogic.getId(14);
			String authoCode = String.valueOf(RandomUtils.makeAuthCode());
			Calendar cal = dateLogicUtils.getCurrent();
			String InsTstmp = dateLogicUtils.getCurrentTimeString(cal);
			cal.add(Calendar.MINUTE, 60);
			String expireTstmp = dateLogicUtils.getCurrentTimeString(cal);
			// 一時テーブル登録処理を行う
			Temporary temporary = new Temporary();
			// ID
			temporary.setID(dummyMemberId);
			// タイプ
			temporary.setType("00");
			// 一時名
			temporary.setTemporaryName(email);
			// 一時値
			temporary.setTemporaryValue(authoCode);
			// 作成日時
			temporary.setInsTstmp(InsTstmp);
			// 有効期限
			temporary.setExpire(expireTstmp);
			// 一時テーブル情報を登録する
			temporaryRepository.save(temporary);

			// メール送信処理を行う
			String sendResult = sendMailUtils.sendMail(email, null, "認証コード", "下記の認証コードを利用ください。\n\t" + authoCode);
			if (ResultCode.OK.code().equals(sendResult)) {
				return new DataResult<String>(dummyMemberId);
			}
			return new DataResult<String>(ResultCode.NG.code(), "メール送信失敗しました。");
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new DataResult<String>(ResultCode.NG.code(), "メール送信失敗しました。");
		}
	}

	/**
	 * ユーザのプロフィール情報を設定する
	 *
	 * @param profile プロフィール情報
	 * @return 設定結果
	 */
	public DataResult<List> setProfileInfo(Profile profile) {
		try {
			// 会員IDより、会員情報を取得する
			List<MemberInfo> updateMemberInfoList = memberInfoRepository.findByMemberID(profile.getMemberid());

			// 存在しない場合
			if (updateMemberInfoList == null || 0 == updateMemberInfoList.size()) {
				return new DataResult<List>(ResultCode.NG.code(), "会員が存在しないので、登録したい会員IDを確認お願いします。");
			}

			// 存在する場合、該当情報更新処理を行う
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

			// 上記のプロフィル情報を会員情報に設定する（カラムの型が一致しないのに、コピーしなくて項目ごとに設定する）
			MemberInfo updateMemberInfo = updateMemberInfoList.get(0);
			// 表示名
			updateMemberInfo.setNickName(profile.getNickname());
			// 氏名
			updateMemberInfo.setName(profile.getName());
			// カナ名
			updateMemberInfo.setKana(profile.getKana());
			// 性別
			updateMemberInfo.setSex(profile.getSex());
			// ゴルフ年数
			// ゴルフ年数がブランクの場合、DBにNULLを設定して、その以外、年数を設定する
			updateMemberInfo.setPlayYears(
					StringUtils.isEmpty(profile.getPlayYears()) ? null : Integer.valueOf(profile.getPlayYears()));
			// 平均スコア
			updateMemberInfo.setAverageScore(
					StringUtils.isEmpty(profile.getAverageScore()) ? null : Integer.valueOf(profile.getAverageScore()));
			// スタイル
			updateMemberInfo.setPlayStyle(
					StringUtils.isEmpty(profile.getPlayStyle()) ? null : Integer.valueOf(profile.getPlayStyle()));
			// 自己紹介
			updateMemberInfo.setNotes(profile.getNotes());
			// 携帯番号
			updateMemberInfo.setTelNo(profile.getTelNo());
			// 都道府県
			updateMemberInfo.setState(profile.getState());
			// 好みのゴルフ場１
			updateMemberInfo.setClub1(profile.getClub1());
			// 好みのゴルフ場２
			updateMemberInfo.setClub2(profile.getClub2());
			// 好みのゴルフ場３
			updateMemberInfo.setClub3(profile.getClub3());
			// 好みのゴルフ場４
			updateMemberInfo.setClub4(profile.getClub4());
			// 通知フラグ
			updateMemberInfo.setPushFlag(profile.getPushFlag());

			// 会員情報を更新する
			memberInfoRepository.save(updateMemberInfo);

			// 更新後の会員情報を取得する
			List<MemberInfo> list = memberInfoRepository.findByMemberID(profile.getMemberid());
			if (list == null || list.size() == 0) {
				return new DataResult<List>(ResultCode.NG.code(), "会員が存在しないので、登録したい会員IDを確認お願いします。");
			}
			return new DataResult<List>(list);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new DataResult<List>(ResultCode.NG.code(), "プロフィール情報が登録失敗しました。");
		}
	}
}
