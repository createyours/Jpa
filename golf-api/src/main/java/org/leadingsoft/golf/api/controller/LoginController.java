package org.leadingsoft.golf.api.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;

import org.leadingsoft.golf.api.code.ResultCode;
import org.leadingsoft.golf.api.entity.MemberInfo;
import org.leadingsoft.golf.api.model.DataResult;
import org.leadingsoft.golf.api.model.Profile;
import org.leadingsoft.golf.api.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * ログイン処理
 *
 * <pre>
 *  ユーザ情報検索・登録
 * </pre>
 *
 * @author zhangyf
 */
@RestController
@RequestMapping("/api/public/login")
public class LoginController {

  private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

  /** ログインサビス */
  @Autowired
  private LoginService loginService;

  @Value("${upload.icon.dir}")
  private String iconDir;

  /**
   * 通常会員の登録処理
   *
   * @param name ユーザ名またはメールアドレス
   * @param password パスワード
   * @return ユーザ情報
   */
/*  SuppressWarnings("rawtypes")
  @GetMapping("/login")
  @ResponseBody
  public DataResult<List> doLogin(@RequestParam("name") String name,
      @RequestParam("password") String password) {
    List<Map<String, Object>> result = loginService.selectMemberInfoByName(name);
    if (result == null || result.size() == 0) {
      return new DataResult<List>(ResultCode.NG.code(), "データを存在しません。");
    }
    String passWordDB = result.get(0).get("Password").toString();
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    if (!encoder.matches(password, passWordDB)) {//方法中前一个参数为前端传来的值（例如123），后一个为数据库中需要对比的值（已加密存入数据库的密码）
      return new DataResult<List>(ResultCode.NG.code(), "パスワードが不正です。");
    }
    return new DataResult<List>(result);
  }*/
  
  @SuppressWarnings("rawtypes")
  @GetMapping("/login")
  @ResponseBody
  public DataResult<MemberInfo> doLogin(@RequestParam("name") String nameOrEmail,
      @RequestParam("password") String password) {
    List<MemberInfo> memberInfoList = loginService.selectMemberInfoByName(nameOrEmail);
    if (memberInfoList == null) {
      return new DataResult<MemberInfo>(ResultCode.NG.code(), "データを存在しません。");
    }
    String passWordDB = memberInfoList.get(0).getPassword().toString();
   if(passWordDB.equals(password)){
	   return new DataResult<MemberInfo>(ResultCode.NG.code(),"パスワードが不正です。");  
   }
   return new DataResult<MemberInfo>(memberInfoList.get(0));//返回集合
   
  }

  /**
   * ゲスト会員の登録処理
   *
   * @param id FBなどからユーザID
   * @return 会員ID
   */
  @GetMapping("/loginByGuest")
  @ResponseBody
  public DataResult<String> doLoginByGuest(@RequestParam("id") String id) {
    String memberid = loginService.selectMemberInfoByGuestId(id);
    if (StringUtils.isEmpty(memberid)) {
      return new DataResult<String>(ResultCode.NG.code(), "データを存在しません。");
    }
    return new DataResult<String>(memberid);
  }

  /**
   * ユーザ登録処理
   *
   * @param email メース
   * @param authCode 認証コード
   * @param password パスワード
   * @param name 表示名
   * @param dummyMemberId 送信の一時メンバーID
   * @return 会員ID
   */
  @GetMapping("/regist")
  @ResponseBody
  public DataResult<String> doRegist(@RequestParam("email") String email,
      @RequestParam("authCode") String authCode, @RequestParam("password") String password,
      @RequestParam("name") String name, @RequestParam("dummyMemberId") String dummyMemberId) {
    return loginService.insertUserData(email, name, password, authCode, dummyMemberId);
  }

  /**
   * ユーザ登録で認証コードを発行する
   *
   * @param email メース
   * @return 送信結果 dummyMemberId
   */
  @GetMapping("/sendMail")
  @ResponseBody
  public DataResult<String> doSendAuthCode(@RequestParam("email") String email) {
    return loginService.sendAuthCode(email);
  }

  /**
   * プロフィール情報を設定する
   *
   * @param profile プロフィール情報
   * @return 登録結果
   */
  @RequestMapping("/setProfile")
  public DataResult<List> doSetProfileInfo(@RequestBody Profile profile) {
    return loginService.setProfileInfo(profile);
  }

  /**
   * プロフィール画像を保存する
   *
   * @param profile プロフィール情報
   * @return 登録結果
   */
  @RequestMapping("/uploadIcon")
  public DataResult<String> doUploadIcon(@RequestParam("file") MultipartFile file,
      @RequestParam("userId") String userId) {
    Path fileStorageLocation = Paths.get(iconDir).toAbsolutePath().normalize();
    String fileName = "";

    try {
      Files.createDirectories(fileStorageLocation);
      // Normalize file name
      fileName = StringUtils.cleanPath(file.getOriginalFilename());
      // Check if the file's name contains invalid characters
      if (fileName.contains("..")) {
        logger.error("Sorry! Filename contains invalid path sequence " + fileName);
        return new DataResult<String>(ResultCode.NG.code(), "ファイル名が不正です");
      }
      Path targetLocation = fileStorageLocation.resolve(fileName);
      Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
      return new DataResult<String>();
    } catch (Exception ex) {
      logger.error("Could not create the directory where the uploaded files will be stored.", ex);
      return new DataResult<String>(ResultCode.NG.code(), "ファイルアップロードが失敗しました。");
    }

  }
}
