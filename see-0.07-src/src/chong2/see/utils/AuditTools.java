package chong2.see.utils;

import chong2.see.data.base.Constants;
import chong2.see.servlet.common.DataManager;
import chong2.see.data.DataReader;
import chong2.see.xml.DataXmlWriter;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>Title: 软件工程管理环境</p>
 * <p>Description: 为软件研发提供管理数据和任务的平台</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: 虫二</p>
 *
 * 与审计管理相关的静态方法
 *
 * @author 玛瑞
 * @version 0.07
 */

final public class AuditTools {

  /**
   * 数据值的分隔符
   */
  public static String AUDIT_SPLITTER  = "a:m:s-a:m:s";

  /**
   * 添加审计信息
   * @param myRequest 当前请求
   * @param myset 数据集名
   * @param myoperation 操作
   * @return 是否添加成功
   */
  public static String addAudit(HttpServletRequest myRequest,
                                String myset,
                                String myoperation) {
    return addAuditL(myRequest,myset,myoperation,
                     null,null,null,Constants.SUCCESSFUL,null,null);
  }

  /**
   * 添加审计信息
   * @param myRequest 当前请求
   * @param myset 数据集名
   * @param myoperation 操作
   * @param mykeys 数据关键字
   * @param myold 数据操作前的知
   * @param mynew 数据操作后的值
   * @param myresult 操作结果
   * @param mydes 描述
   * @param mypass 口令域，不允许保存和显示
   * @return 是否添加成功
   */
  public static String addAudit(HttpServletRequest myRequest,
                                String myset,
                                String myoperation,
                                Hashtable mykeys,
                                Hashtable myold,
                                Hashtable mynew,
                                String myresult ,
                                String mydes,
                                ArrayList mypass) {
    ArrayList myk = new ArrayList();
    if ( mykeys != null )
      myk.add(mykeys);
    else
      myk = null;
    return addAuditL(myRequest,myset,myoperation,
                     myk,myold,mynew,myresult,mydes,mypass);
  }

  /**
   * 添加审计信息
   * @param myRequest 当前请求
   * @param myset 数据集名
   * @param myoperation 操作
   * @param mykeys 数据关键字
   * @param myoldv 数据操作前的知
   * @param mynewv 数据操作后的值
   * @param myresult 操作结果
   * @param mydes 描述
   * @param mypass 口令域，不允许保存和显示
   * @return 是否添加成功
   */
  public static String addAuditL(HttpServletRequest myRequest,
                                 String myset,
                                 String myoperation,
                                 ArrayList mykeys,
                                 Hashtable myoldv,
                                 Hashtable mynewv,
                                 String myresult,
                                 String mydes,
                                 ArrayList mypass ) {

    Hashtable myold = new Hashtable();
    if ( myoldv != null ) myold.putAll(myoldv);
    Hashtable mynew = new Hashtable();
    if ( mynewv != null ) mynew.putAll(mynewv);
    Hashtable mydata =  new Hashtable();
    mydata.put("id",ServletTools.getAutoValue(myRequest));
    String myenc = ServletTools.getAppDefaultEncoding(myRequest);
    if ( myset != null )
      mydata.put("data",LanguageTools.atr(myRequest,myset));
    if ( myoperation != null )
      mydata.put("operation",LanguageTools.atr(myRequest,myoperation));
    if ( (mykeys != null) && (mykeys.size() > 0) ) {
      StringBuffer myk =
          new StringBuffer(CommonTools.hashToString((Hashtable)mykeys.get(0),
          DataReader.VALUE_HASH_SPLITTER,
          mypass));
      for (int i=1; i< mykeys.size(); i++ ) {
        myk.append( AUDIT_SPLITTER );
        myk.append(CommonTools.hashToString((Hashtable)mykeys.get(i),
        DataReader.VALUE_HASH_SPLITTER, mypass));
      }
      mydata.put("keys",myk);
    }
    if ( myresult != null )
      mydata.put("result",LanguageTools.atr(myRequest,myresult));
    else
      mydata.put("result",LanguageTools.atr(myRequest,Constants.SUCCESSFUL));
    mydata.put("who",ServletTools.getSessionUser(myRequest));
    mydata.put("when",ServletTools.getCurrentTime(myRequest));
    mydata.put("where",ServletTools.getHost(myRequest));
    if ( mydes != null )
      mydata.put("description",mydes);

    //个人消息不审计内容
    if ( DataManager.PERSONAL_INFORMATION.equals(myset)  ) {

      mydata.put("new_values","");
      mydata.put("old_values","");

    } else {

      if ( ( myold != null ) &&
           ( mynew != null ) ) {
        String mykey;
        for ( Enumeration e = myold.keys(); e.hasMoreElements();) {
          mykey = e.nextElement().toString();
          if ( MatrixTools.MATRIX_VALUE.equals(mykey) ) continue;
          if ( mynew.get(mykey) == null ) continue;
          if ( myold.get(mykey).equals(mynew.get(mykey)) ) {
            myold.remove(mykey);
            mynew.remove(mykey);
          }
        }
      }
      if (  ( mynew != null) &&
            !mynew.isEmpty()  ) {
        mydata.put("new_values",
                   CommonTools.hashToString(mynew,
                   DataReader.VALUE_HASH_SPLITTER, mypass));
      }
      if (  ( myold != null) &&
            !myold.isEmpty()  ) {
        mydata.put("old_values",
                   CommonTools.hashToString(myold,
                   DataReader.VALUE_HASH_SPLITTER,mypass));
      }

      if ( "modify".equals(myoperation) ) {
        if ( mydata.get("new_values") == null ) {
          if ( mydata.get("old_values") == null )
            return Constants.SUCCESSFUL;
        } else {
          if ( mydata.get("new_values").equals(mydata.get("old_values")) )
            return Constants.SUCCESSFUL;
        }
      }

    }

    ArrayList myaudit = new ArrayList();
    DataReader myreader =
        DataManagerTools.getDataReader(myRequest,
        DataManager.DATA_AUDIT, null,0,-1, null);
    if ( myreader != null )
      myaudit = myreader.getRecords();
    if ( myaudit == null )  myaudit = new ArrayList();
    myaudit.add(0,mydata);

    String fdata =
        DataManagerTools.getDataValuesFile(myRequest,
        DataManager.DATA_AUDIT);
    DataXmlWriter myw = new DataXmlWriter();
    return myw.writeData(myaudit,fdata,
                         ServletTools.getAppDefaultCharset(myRequest));
  }

}