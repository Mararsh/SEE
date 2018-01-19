package chong2.see.utils;

import chong2.see.data.base.Constants;
import chong2.see.data.base.Language;
import chong2.see.data.DataReader;
import chong2.see.data.*;
import chong2.see.xml.DataXmlWriter;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>Title: 软件工程管理环境</p>
 * <p>Description: 为软件研发提供管理数据和任务的平台</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: 虫二</p>
 *
 * 与语言翻译相关的静态方法
 *
 * @author 玛瑞
 * @version 0.07
 */

final public class LanguageTools {

  /**
   * 获得指定编码的指定文字的资源值。采用session保存的编码。
   * @param myRequest 当前请求
   * @param inKey 文字
   * @return 文字对应的资源值
   */
  public static String tr(HttpServletRequest myRequest,
                          String inKey) {

    try {
      if ( inKey == null || "".equals(inKey) )
        return "";
      String myenc = ServletTools.getSessionEncoding(myRequest);
      String mytrans = Language.getText(myenc, inKey);
      if ( mytrans != null ) return mytrans;
      mytrans = utr(myRequest,inKey,myenc);
      if ( mytrans != null ) return mytrans;
//      addUserLangauge(myRequest,myenc,inKey,inKey);
      return inKey;
    }
    catch (Exception ex) {
      return inKey;
    }
  }

  /**
   * 获得指定编码的一组指定文字的资源值。采用session保存的编码。
   * @param myRequest 当前请求
   * @param inKey 文字
   * @return 文字对应的资源值
   */
  public static ArrayList tr(HttpServletRequest myRequest,
                             ArrayList inKey) {
    try {
      if ( inKey == null  )  return null;
      String myenc = ServletTools.getSessionEncoding(myRequest);
      ArrayList myv = new ArrayList();
      for (int i=0; i< inKey.size(); i++){
        myv.add(tr(myRequest, inKey.get(i).toString()));
      }
      return myv;
    }
    catch (Exception ex) {
      return inKey;
    }
  }


  /**
   * 获得指定编码的一组指定文字的资源值。采用session保存的编码。替换参数值。
   * 参数被替换的位置是PARAMETER_IDENTIFY决定的，如：%%0,%%1等
   * @param myRequest 当前请求
   * @param inKey 文字
   * @param inPa 参数值
   * @return 文字对应的资源值
   */
  public static String tr(HttpServletRequest myRequest,
                          String inKey,
                          ArrayList inPa) {
    try {
      if ( inKey == null || "".equals(inKey) )
        return "";
      String myenc = ServletTools.getSessionEncoding(myRequest);
      String mytrans = Language.getText(myenc, inKey,inPa);
      if ( mytrans != null ) return mytrans;
      mytrans = utr(myRequest,inKey,inPa,myenc);
      if ( mytrans != null ) return mytrans;
//      addUserLangauge(myRequest,myenc,inKey,inKey);
      return inKey;
    }
    catch (Exception ex) {
      return inKey;
    }
  }

  /**
   * 获得指定编码的一组指定文字的资源值。采用session保存的编码。替换参数值。
   * 参数被替换的位置是PARAMETER_IDENTIFY决定的，如：%%0,%%1等
   * @param myRequest 当前请求
   * @param inKey 文字
   * @param inPa 参数值
   * @return 文字对应的资源值
   */
  public static String tr(HttpServletRequest myRequest,
                          String inKey,
                          String inPa) {
    try {
      if ( inKey == null || "".equals(inKey) )
        return "";
      ArrayList myp = new ArrayList();
      if ( inPa != null ) myp.add(inPa);
      return tr(myRequest,inKey,myp);
    }
    catch (Exception ex) {
      return inKey;
    }
  }

  /**
   * 获得指定文字的资源值。采用context保存的编码。
   * @param myRequest 当前请求
   * @param inKey 文字
   * @return 文字对应的资源值
   */
  public static String atr(HttpServletRequest myRequest,
                           String inKey) {

    try {
      if ( inKey == null || "".equals(inKey) )
        return "";
      String myenc = ServletTools.getAppDefaultEncoding(myRequest);
      String mytrans = Language.getText(myenc, inKey);
      if ( mytrans != null ) return mytrans;
      mytrans = utr(myRequest,inKey,myenc);
      if ( mytrans != null ) return mytrans;
//      addUserLangauge(myRequest,myenc,inKey,inKey);
      return inKey;
    }
    catch (Exception ex) {
      return inKey;
    }

  }

  /**
   * 获得一组指定文字的资源值。采用context保存的编码。
   * @param myRequest 当前请求
   * @param inKey 文字
   * @return 文字对应的资源值
   */
  public static ArrayList atr(HttpServletRequest myRequest,
                              ArrayList inKey) {

    try {
      if ( inKey == null  )  return null;
      ArrayList myv = new ArrayList();
      for (int i=0; i< inKey.size(); i++){
        myv.add(atr(myRequest,inKey.get(i).toString()));
      }
      return myv;
    }
    catch (Exception ex) {
      return inKey;
    }
  }

  /**
   * 从用户定义的语言定义中获得一组指定文字的资源值。
   * @param myRequest 当前请求
   * @param inKey 文字
   * @param myenc 编码
   * @return 文字对应的资源值
   */
  public static String utr(HttpServletRequest myRequest,
                           String inKey,
                           String myenc) {
    try {
      if ( inKey == null ) return null;
      Hashtable mylangv =
          getUserLangauge(myRequest, myenc);
      if ( mylangv == null ) return null;
      if ( mylangv.get(inKey) == null ) return null;
      return mylangv.get(inKey).toString();
    }
    catch (Exception ex) {
      return null;
    }
  }

  /**
   * 从用户定义的语言定义中获得一组指定文字的资源值。
   * @param myRequest 当前请求
   * @param inKey 文字
   * @param inPa 参数值
   * @param myenc 编码
   * @return 文字对应的资源值
   */
  public static String utr(HttpServletRequest myRequest,
                           String inKey,
                           ArrayList inPa,
                           String myenc) {
    if ( inKey == null ) return null;
    String mytrans = utr(myRequest,inKey, myenc );
    if ( mytrans == null ) return null;
    try {
      if ( inPa == null ) return mytrans;
      for (int i = 0; i < inPa.size(); i++ ) {
        mytrans = mytrans.replaceAll(Language.PARAMETER_IDENTIFY + i,
        inPa.get(i).toString());
      }
      return mytrans;
    }
    catch (Exception ex) {
      return mytrans;
    }
  }

  /**
   * 读取用户的语言翻译到内存中
   * @param myRequest 当前请求
   */
  public static void setUserLangauge(HttpServletRequest myRequest) {
    setUserLangauge(ServletTools.getContext(myRequest));
  }

  /**
   * 读取用户的语言翻译到内存中
   * @param myContext 当前应用环境
   */
  public static void setUserLangauge(ServletContext myContext) {
    try {
      ArrayList mylangs = Language.getEncodingList();
      for (int i=0; i< mylangs.size(); i++) {
        setUserLangauge(myContext, mylangs.get(i).toString());
      }
    }
    catch (Exception ex) {
    }
  }

  /**
   * 读取用户的语言翻译到内存中
   * @param myRequest 当前请求
   * @param mylang 语言名
   */
  public static void setUserLangauge(HttpServletRequest myRequest,
                                     String mylang) {
    setUserLangauge(ServletTools.getContext(myRequest), mylang);
  }

  /**
   * 读取用户的语言翻译到内存中
   * @param myContext 当前应用环境
   * @param mylang 语言名
   */
  public static void setUserLangauge(ServletContext myContext,
                                     String mylang) {
    try {
      if ( mylang == null ) return;
      String myret, myname, myvalue;
      DataReader mydataReader =
          DataReaderGetor.dataReader(myContext,mylang);
      myret = mydataReader.startReading();
      if ( !Constants.SUCCESSFUL.equals(myret) ) return;
      ArrayList mydata;
      Hashtable myrec, mylangv ;
      mydata = mydataReader.getRecords();
      if ( mydata == null ) return;
      mylangv = new Hashtable();
      for (int j = 0; j< mydata.size(); j++) {
        myrec = (Hashtable)mydata.get(j);
        if ( myrec == null ) continue;
        if ( myrec.get("name") == null ) continue;
        if ( myrec.get("value") == null ) continue;
        mylangv.put(myrec.get("name"),myrec.get("value"));
      }
//      if ( mylangv.size() == 0 ) return;
      myContext.setAttribute(mylang, mylangv);
    }
    catch (Exception ex) {
    }
  }

  /**
   * 获得指定语言的用户翻译
   * @param myRequest 当前请求
   * @param mylang 语言名
   * @return 指定语言的用户翻译
   */
  public static Hashtable getUserLangauge(HttpServletRequest myRequest,
      String mylang) {
    return getUserLangauge(ServletTools.getContext(myRequest), mylang);
  }

  /**
   * 获得指定语言的用户翻译
   * @param myContext 当前应用环境
   * @param mylang 语言名
   * @return 指定语言的用户翻译
   */
  public static Hashtable getUserLangauge(ServletContext myContext,
      String mylang) {
    if ( myContext.getAttribute(mylang) == null )
      return null;
    return (Hashtable)myContext.getAttribute(mylang);
  }


  /**
   * 将一个文字翻译数据写入用户的翻译文件中
   * @param myRequest 当前请求
   * @param mylang 语言名
   * @param myname 文字
   * @param myvalue 文字翻译
   */
  public static void addUserLangauge(HttpServletRequest myRequest,
                                     String mylang,
                                     String myname,
                                     String myvalue) {
    if ( mylang == null ) return;
    if ( myname == null ) return;
    if ( myvalue == null ) return;
    Hashtable mylangv = getUserLangauge(myRequest, mylang);
    if ( mylangv == null ) return;
    mylangv.put(myname, myvalue);
    ServletTools.getContext(myRequest).setAttribute(mylang, mylangv);
    DataReader mydataReader =
        DataReaderGetor.dataReader(myRequest,mylang);
    String myret = mydataReader.startReading();
    if ( !Constants.SUCCESSFUL.equals(myret) ) return;
    Hashtable myv = new Hashtable();
    myv.put("name",myname);
    myv.put("value",myvalue);
    myv = mydataReader.addValidRecord(CommonTools.stringToArray("name,value",","),
                                      CommonTools.stringToArray("name,value",","),
                                      CommonTools.stringToArray("name",","),
                                      myv);
    if ( myv == null ) return;
    DataXmlWriter myw = new DataXmlWriter();
    String myfile =
        DataManagerTools.getDataValuesFile(myRequest,mylang);
    if ( myfile == null ) return;
    myret = myw.writeData(mydataReader.getRecords(),myfile,
                          ServletTools.getAppDefaultCharset(myRequest));

  }

}