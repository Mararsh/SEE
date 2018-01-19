package chong2.see.data.base;

import chong2.see.utils.CommonTools;

import java.util.*;

/**
 * <p>Title: ������̹�����</p>
 * <p>Description: Ϊ����з��ṩ�������ݺ������ƽ̨</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: ���</p>
 *
 * <p>������صķ���
 *
 * @author ����
 * @version 0.07
 */

public class Language {

  /**
   * ���Է����б��в����ı�ʶ
   */
  public static String PARAMETER_IDENTIFY =  "%%";

  /**
   * ��������-����ӳ���
   */
  protected static Hashtable Locales = new Hashtable() ;
  static {
    Locales.put("zh_CN",Locale.SIMPLIFIED_CHINESE);
    Locales.put("en",Locale.ENGLISH);
    Locales.put("zh_TW",Locale.TRADITIONAL_CHINESE);
  }

  /**
   * ��������-�ַ���ӳ���
   */
  protected static Hashtable Charsets = new Hashtable() ;
  static {
    Charsets.put("zh_CN","UTF-8");
    Charsets.put("en","UTF-8");
    Charsets.put("zh_TW","UTF-8");
  }

  /**
   * ��������-��׼�ַ���ӳ���
   */
  protected static Hashtable StandardCharsets = new Hashtable() ;
  static {
    StandardCharsets.put("zh_CN","gb2312");
    StandardCharsets.put("en","iso-8859-1");
    StandardCharsets.put("zh_TW","big-5");
  }

  /**
   * ��������-��Դӳ���
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
   * ��ñ������б�
   * @return �������б�
   */
  public static Hashtable getLocales() {
    return Locales;
  }

  /**
   * ����ַ����б�
   * @return �ַ����б�
   */
  public static Hashtable getCharsets() {
    return Charsets;
  }

  /**
   * ������������б�
   * @return ���������б�
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
   * ������������б�
   * @return ���������б�
   */
  public static Enumeration getEncodings() {
    return Charsets.keys();
  }

  /**
   * �����Դӳ���
   * @return ��Դӳ���
   */
  public static Hashtable getResList() {
    return ResList;
  }

  /**
   * ���ָ���������ֵ�����ֵ
   * @param inEnc ָ����������
   * @return ָ���������ֵ�����ֵ
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
   * ���ָ���������ֵ��ַ���
   * @param inEnc ָ����������
   * @return ָ���������ֵ��ַ���
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
   * ���ָ���������ֵı�׼�ַ���
   * @param inEnc ָ����������
   * @return ָ���������ֵı�׼�ַ���
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
   * ��ñ�׼�ַ����б�
   * @return ��׼�ַ����б�
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
   * ���ָ���������ֵ���Դֵ
   * @param inEnc ָ����������
   * @return ָ���������ֵ���Դ��
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
   * ���ָ������ָ�����ֵ���Դֵ
   * @param inEnc ������
   * @param inKey ����
   * @return ���ֶ�Ӧ����Դֵ
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
   * ���ָ������ָ�����ֵ���Դֵ���滻����ֵ��
   * �������滻��λ����PARAMETER_IDENTIFY�����ģ��磺%%0,%%1��
   * @param inEnc ����
   * @param inKey ����
   * @param inPa ����ֵ
   * @return ���ֶ�Ӧ����Դֵ
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
   * ��úϷ�����������
   * @param inEnc ��������
   * @return �Ϸ�����������
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
   * ����������Ե���ʾ����
   * @return �������Ե���ʾ����
   */
  public static Hashtable getLanaguageNames() {

    Hashtable mylangs = new Hashtable();
    String myenc,mylang;
    for ( Enumeration e = Locales.keys(); e.hasMoreElements() ; ) {
      myenc = e.nextElement().toString();
      /* ϵͳ��Ȼ�����ء��������ġ��͡��������ġ���
      ���ǽ����й����͡�̨�塱����Ϊ�������ң��ɶ񣡣�
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
//   * ��ñ����Ӧ�Ĺ���������ʾ�ı�
//   * @param inEnc ������
//   * @return �����Ӧ�Ĺ���������ʾ�ı�
//   */
//  public static String getCountryName(Locale inLocale) {
//    return inLocale.getDisplayCountry();
//  }


//  /**
//   * ��ñ����Ӧ������������ʾ�ı�
//   * @param inEnc ������
//   * @return �����Ӧ������������ʾ�ı�
//   */
//  public static String getLanguageName(Locale inLocale) {
//    return inLocale.getDisplayLanguage();
//  }

}