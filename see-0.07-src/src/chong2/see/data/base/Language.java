package chong2.see.data.base;

import chong2.see.utils.CommonTools;

import java.util.*;

/**
 * <p>Title: 软件工程管理环境</p>
 * <p>Description: 为软件研发提供管理数据和任务的平台</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: 虫二</p>
 *
 * <p>语言相关的方法
 *
 * @author 玛瑞
 * @version 0.07
 */

public class Language {

  /**
   * 语言翻译列表中参数的标识
   */
  public static String PARAMETER_IDENTIFY =  "%%";

  /**
   * 语言名字-语言映射表
   */
  protected static Hashtable Locales = new Hashtable() ;
  static {
    Locales.put("zh_CN",Locale.SIMPLIFIED_CHINESE);
    Locales.put("en",Locale.ENGLISH);
    Locales.put("zh_TW",Locale.TRADITIONAL_CHINESE);
  }

  /**
   * 语言名字-字符集映射表
   */
  protected static Hashtable Charsets = new Hashtable() ;
  static {
    Charsets.put("zh_CN","UTF-8");
    Charsets.put("en","UTF-8");
    Charsets.put("zh_TW","UTF-8");
  }

  /**
   * 语言名字-标准字符集映射表
   */
  protected static Hashtable StandardCharsets = new Hashtable() ;
  static {
    StandardCharsets.put("zh_CN","gb2312");
    StandardCharsets.put("en","iso-8859-1");
    StandardCharsets.put("zh_TW","big-5");
  }

  /**
   * 语言名字-资源映射表
   */
  protected static Hashtable ResList = new Hashtable();
  static {
    ResList.put("zh_CN",
                ResourceBundle.getBundle("chong2.see.Res",
                Locale.SIMPLIFIED_CHINESE));
    ResList.put("en",
                ResourceBundle.getBundle("chong2.see.Res",
                Locale.ENGLISH));
    ResList.put("zh_TW",
                ResourceBundle.getBundle("chong2.see.Res",
                Locale.TRADITIONAL_CHINESE));
  }

  /**
   * 获得编码名列表
   * @return 编码名列表
   */
  public static Hashtable getLocales() {
    return Locales;
  }

  /**
   * 获得字符集列表
   * @return 字符集列表
   */
  public static Hashtable getCharsets() {
    return Charsets;
  }

  /**
   * 获得语言名字列表
   * @return 语言名字列表
   */
  public static ArrayList getEncodingList() {
    try {
      return CommonTools.enumToArraylist(Charsets.keys());
    }
    catch (Exception ex) {
      return null;
    }
  }

  /**
   * 获得语言名字列表
   * @return 语言名字列表
   */
  public static Enumeration getEncodings() {
    return Charsets.keys();
  }

  /**
   * 获得资源映射表
   * @return 资源映射表
   */
  public static Hashtable getResList() {
    return ResList;
  }

  /**
   * 获得指定语言名字的语言值
   * @param inEnc 指定语言名字
   * @return 指定语言名字的语言值
   */
  public static Locale getLocale(String inEnc) {
    try {
      return (Locale)Locales.get(inEnc);
    }
    catch (Exception ex) {
      return null;
    }
  }

  /**
   * 获得指定语言名字的字符集
   * @param inEnc 指定语言名字
   * @return 指定语言名字的字符集
   */
  public static String getCharset(String inEnc) {
    try {
      return (String)Charsets.get(inEnc);
    }
    catch (Exception ex) {
      return null;
    }
  }

  /**
   * 获得指定语言名字的标准字符集
   * @param inEnc 指定语言名字
   * @return 指定语言名字的标准字符集
   */
  public static String getStandardCharset(String inEnc) {
    try {
      return (String)StandardCharsets.get(inEnc);
    }
    catch (Exception ex) {
      return null;
    }
  }

  /**
   * 获得标准字符集列表
   * @return 标准字符集列表
   */
  public static ArrayList getStandardCharsets() {
    try {
      ArrayList mysets = new ArrayList();
      mysets.addAll(StandardCharsets.values());
      return mysets;
    }
    catch (Exception ex) {
      return null;
    }
  }

  /**
   * 获得指定语言名字的资源值
   * @param inEnc 指定语言名字
   * @return 指定语言名字的资源集
   */
  public static ResourceBundle getRes(String inEnc) {
    try {
      return (ResourceBundle)ResList.get(inEnc);
    }
    catch (Exception ex) {
      return null;
    }
  }

  /**
   * 获得指定语言指定文字的资源值
   * @param inEnc 语言名
   * @param inKey 文字
   * @return 文字对应的资源值
   */
  public static String getText(String inEnc, String inKey) {
    try {
      if ( inKey == null ) return null;
      ResourceBundle myres = (ResourceBundle)ResList.get(inEnc);
      return  myres.getString(inKey);
    }
    catch (Exception  ex) {
      return null;
    }
  }

  /**
   * 获得指定语言指定文字的资源值，替换参数值。
   * 参数被替换的位置是PARAMETER_IDENTIFY决定的，如：%%0,%%1等
   * @param inEnc 语言
   * @param inKey 文字
   * @param inPa 参数值
   * @return 文字对应的资源值
   */
  public static String getText(String inEnc, String inKey,
                               ArrayList inPa) {
    try {
      if ( inKey == null ) return null;
      String mytt = getText(inEnc,inKey);
      if ( mytt == null ) return null;
      if ( inPa == null ) return mytt;
      for (int i = 0; i < inPa.size(); i++ ) {
        mytt = mytt.replaceAll(PARAMETER_IDENTIFY + i,
        inPa.get(i).toString());
      }
      return mytt;
    }
    catch (Exception ex) {
      return inKey;
    }
  }

  /**
   * 获得合法的语言名字
   * @param inEnc 语言名字
   * @return 合法的语言名字
   */
  public static String getVaildEncoding(String inEnc) {
    try {
      if ( ResList.get(inEnc) != null )
        return inEnc;
      return Constants.DEFAULT_LANGUAGE;
    }
    catch (Exception ex) {
      return Constants.DEFAULT_LANGUAGE;
    }
  }

  /**
   * 获得所有语言的显示名字
   * @return 所有语言的显示名字
   */
  public static Hashtable getLanaguageNames() {

    Hashtable mylangs = new Hashtable();
    String myenc,mylang;
    for ( Enumeration e = Locales.keys(); e.hasMoreElements() ; ) {
      myenc = e.nextElement().toString();
      /* 系统居然不返回“简体中文”和“繁体中文”，
      而是将“中国”和“台湾”定义为两个国家！可恶！！
      System returns "China" and "TaiWan" as two countrys!
      I hate this ugly action!!
      */
      if ( "zh_CN".equals(myenc) ||
           "zh_TW".equals(myenc) )
        mylang = myenc;
      else
        mylang = getLocale(myenc).getDisplayName();
      mylangs.put(myenc,mylang);
    }
    return mylangs;
  }

//  /**
//   * 获得编码对应的国家名的显示文本
//   * @param inEnc 编码名
//   * @return 编码对应的国家名的显示文本
//   */
//  public static String getCountryName(Locale inLocale) {
//    return inLocale.getDisplayCountry();
//  }


//  /**
//   * 获得编码对应的语言名的显示文本
//   * @param inEnc 编码名
//   * @return 编码对应的语言名的显示文本
//   */
//  public static String getLanguageName(Locale inLocale) {
//    return inLocale.getDisplayLanguage();
//  }

}