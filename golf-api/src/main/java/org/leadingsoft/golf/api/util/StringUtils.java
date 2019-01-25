package org.leadingsoft.golf.api.util;

import javax.security.auth.message.callback.PrivateKeyCallback.IssuerSerialNumRequest;

public class StringUtils {
  // 半角→全角変換で変換できなかった場合に使用する文字
  private static final char TO_FULL_WIDTH_ERROR = '・';
  // 読みやすいランダムな文字列生成用の1,I,l,O,0,o,8,B,6,b,2,Z,9,qを除く文字列
  private static final String EASY_READ_PASSWORD_STRINGS = "3457acdefghijkmnprstuvwxyzACDEFGHJKLMNPQRSTUVWXY";

  public static boolean isEmpty(String str) {
    return str == null || str.trim().isEmpty();
  }

  public static boolean isNotEmpty(String str) {
    return !isEmpty(str);
  }

  /**
   * 半角を全角に変換する。
   * 
   * @param str
   * @return
   */
  public static String toFullWidth(String str) {
    return toFullWidthKANA(toFullWidthAlphanumeric(str));
  }

  /**
   * 半角英字を全角に変換する。
   * 
   * @param str
   * @return
   */
  public static String toFullWidthAlphabet(String str) {
    if (isEmpty(str)) {
      return str;
    }
    StringBuilder sb = new StringBuilder();
    for (char c : str.toCharArray()) {
      sb.append(toFullWidthAlphabet(c));
    }
    return sb.toString();
  }

  /**
   * 半角数字を全角に変換する。
   * 
   * @param str
   * @return
   */
  public static String toFullWidthNumber(String str) {
    if (isEmpty(str)) {
      return str;
    }
    StringBuilder sb = new StringBuilder();
    for (char c : str.toCharArray()) {
      sb.append(toFullWidthNumber(c));
    }
    return sb.toString();
  }

  /**
   * 半角英数字・半角記号を全角に変換する。
   * 
   * @param str
   * @return
   */
  public static String toFullWidthAlphanumeric(String str) {
    if (isEmpty(str)) {
      return str;
    }
    StringBuilder sb = new StringBuilder();
    for (char c : str.toCharArray()) {
      sb.append(toFullWidthSign(toFullWidthNumber(toFullWidthAlphabet(c))));
    }
    return sb.toString();
  }

  /**
   * 半角カナを全角カナに変換する。
   * 
   * @param str
   * @return
   */
  public static String toFullWidthKANA(String str) {
    if (isEmpty(str)) {
      return str;
    }
    StringBuilder sb = new StringBuilder();
    int i = 0;
    while (i < str.length()) {
      char c1 = str.charAt(i++);
      char c2 = ' ';
      if (i < str.length()) {
        c2 = str.charAt(i);
        if (canMerge(c1, c2)) {
          i++;
        }
      }
      sb.append(toFullWidthKANA(c1, c2));
    }
    // 全角変換後に'ﾞ'や'ﾟ'が余っている場合、エラーとみなして'・'に変換する。
    return sb.toString().replace('ﾞ', TO_FULL_WIDTH_ERROR).replace('ﾟ', TO_FULL_WIDTH_ERROR);
  }

  /*
   * c1 と c2 を1文字にまとめられるかどうか
   * 
   * @param c1
   * 
   * @param c2
   * 
   * @return まとめられる場合 true、まとめられない場合 false
   */
  private static boolean canMerge(char c1, char c2) {
    return ("ｶｷｸｹｺｻｼｽｾｿﾀﾁﾂﾃﾄﾊﾋﾌﾍﾎｳ".indexOf(c1) >= 0) && (c2 == 'ﾞ' || c2 == 'ﾟ');
  }

  private static char toFullWidthAlphabet(char c) {
    if (c >= 'a' && c <= 'z') {
      return (char) (c - 'a' + 'ａ');
    } else if (c >= 'A' && c <= 'Z') {
      return (char) (c - 'A' + 'Ａ');
    }
    return c;
  }

  private static char toFullWidthNumber(char c) {
    if (c >= '0' && c <= '9') {
      return (char) (c - '0' + '０');
    }
    return c;
  }

  private static char toFullWidthSign(char c) {
    int i = " !\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~".indexOf(c);
    if (i >= 0) {
      return "　！”＃＄％＆’（）＊＋，－．／：；＜＝＞？＠［￥］＾＿‘｛｜｝～".charAt(i);
    }
    return c;
  }

  private static char toFullWidthKANA(char c1, char c2) {
    // 濁音
    if (c2 == 'ﾞ') {
      int i = "ｶｷｸｹｺｻｼｽｾｿﾀﾁﾂﾃﾄﾊﾋﾌﾍﾎｳ".indexOf(c1);
      if (i >= 0) {
        return "ガギグゲゴザジズゼゾダヂヅデドバビブベボヴ".charAt(i);
      }
      // 半濁音
    } else if (c2 == 'ﾟ') {
      int i = "ﾊﾋﾌﾍﾎ".indexOf(c1);
      if (i >= 0) {
        return "パピプペポ".charAt(i);
      }
    }
    // 清音・半角カナ記号
    int i = "｡｢｣､･ｰｱｲｳｴｵｶｷｸｹｺｻｼｽｾｿﾀﾁﾂﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾐﾑﾒﾓﾔﾕﾖﾗﾘﾙﾚﾛﾜｦﾝｧｨｩｪｫｬｭｮｯ".indexOf(c1);
    if (i >= 0) {
      return "。「」、・ーアイウエオカキクケコサシスセソタチツテトナニヌネノハヒフヘホマミムメモヤユヨラリルレロワヲンァィゥェォャュョッ".charAt(i);
    }
    return c1;
  }

  // /**
  // * 読みやすいランダムな文字列生成用の1,I,l,O,0,o,8,B,6,b,2,Z,9,qを除く文字列ランダムなパスワードを生成する
  // * @param size パスワードの長さ
  // * @return パスワード
  // */
  // public static String getPassword(int size) {
  // String password;
  //
  // // 数字、大文字、小文字が全て含まれるパスワードを生成する。
  // do {
  // password = RandomStringUtils.random(size, EASY_READ_PASSWORD_STRINGS);
  // } while (!password.matches(".*[1-9].*") || !password.matches(".*[a-z].*")
  // || !password.matches(".*[A-Z].*"));
  //
  // return password;
  // }

  /**
   * 文字列のIDへ変更
   * 
   * @param obj
   * @return
   */
  public static String toStr(Object obj) {
    if (obj == null) {
      return null;
    } else {
      return obj.toString();
    }
  }

  public static String nullToString(String target) {
    if (isEmpty(target)) {
      return "";
    }
    return target;
  }
}
