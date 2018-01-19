package chong2.see.data;

import chong2.see.data.base.Constants;
import chong2.see.database.DatabaseReader;
import chong2.see.utils.CommonTools;
import chong2.see.utils.DataManagerTools;
import chong2.see.utils.ServletTools;
import chong2.see.xml.DataXmlReader;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;


/**
 * <p>Title: 软件工程管理环境</p>
 * <p>Description: 为软件研发提供管理数据和任务的平台</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: 虫二</p>
 *
 * 数据读取的通用方法。
 * 采用“工厂模式”实现。
 *
 * @author 玛瑞
 * @version 0.07
 */

public class DataReaderGetor {

  /**
   * 获得数据读取类
   * @param myRequest 当前请求
   * @param myname 数据名
   * @return 数据读取类
   */
  public static DataReader dataReader(HttpServletRequest myRequest,
                                      String myname) {
    return dataReader(ServletTools.getContext(myRequest),
                      myname);
  }

  /**
   * 获得数据读取类
   * @param myRequest 当前请求
   * @param mytype 数据源类型
   * @param myname 数据名
   * @return 数据读取类
   */
  public static DataReader dataReader(HttpServletRequest myRequest,
                                      String mytype,
                                      String myname) {
    return dataReader(ServletTools.getContext(myRequest),
                      mytype,myname);
  }

  /**
   * 获得数据读取类
   * @param myContext 当前应用环境
   * @param myname 数据名
   * @return 数据读取类
   */
  public static DataReader dataReader(ServletContext myContext,
                                      String myname) {
    String mytype = ServletTools.getAppDataSourceType(myContext);
    return dataReader(myContext,mytype,myname);
  }

  /**
   * 获得数据读取类
   * @param myContext 当前应用环境
   * @param mytype 数据源类型
   * @param myname 数据名
   * @return 数据读取类
   */
  public static DataReader dataReader(ServletContext myContext,
                                     String mytype,
                                     String myname) {
    if ( myname == null ) return null;
    String myn = myname;
    if ( "xml".equals(mytype)) {
      myn = DataManagerTools.getDataValuesFile(myContext,myname);
    }
    return dataReader(mytype,myn);
  }

  /**
   * 获得数据读取类
   * @param mytype 数据源类型
   * @param myname 数据源名
   * @return 数据读取类
   */
   public static DataReader dataReader(String mytype,
                                       String myname) {
     DataReader myreader = new DataXmlReader();
     try {
       if ( mytype == null )
         mytype = "xml";
       if ( "database".equals(mytype))
         myreader = new DatabaseReader();
       if ( myreader == null) {
         myreader = new DataXmlReader();
         mytype = "xml";
       }
       myreader.setDataSourceType(mytype);
       if ( myname == null )
         return myreader;
       myreader.setDataSourceName(myname);
       return myreader;
     }
     catch (Exception ex) {
       return myreader;
     }
   }

  /**
   * 取得一个字段的所有的不同值
   * @param myrecords 数据
   * @param myname 字段名
   * @return 一个字段的所有的不同值
   */
  public static ArrayList getItemValues(ArrayList myrecords,
                                        String myname) {

    try {
      if ( myrecords == null ) return null;
      if ( (myname == null) || "".equals(myname) )
        return null;
      ArrayList myvalues = new ArrayList();
      Hashtable item;
      Object myv;
      for ( int i=0; i< myrecords.size(); i++) {
        item = (Hashtable)myrecords.get(i);
        myv = item.get(myname);
        if ( myv == null ) continue;
        if ( myvalues.contains(myv) ) continue;
        myvalues.add(myv);
      }
      return myvalues;
    }
    catch (Exception ex) {
      return null;
    }

  }

  /**
   * 获得一个数据的关键字值
   * @param keys 关键字段名表
   * @param item 数据
   * @return 数据的关键字值
   */
  public static Hashtable getRecordKey(ArrayList keys,
                                       Hashtable item) {
    try {
      if ( keys == null ) return item;
      if ( item == null ) return null;
      String myname;
      Hashtable myitem = new Hashtable();
      for (int i=0; i < keys.size(); i++ ) {
        myname = keys.get(i).toString();
        if ( item.get(myname) == null ) return null;
        myitem.put(myname,item.get(myname));
      }
      return myitem;
    }
    catch (Exception ex) {
      return null;
    }
  }

  /**
   * 将原始数据转换为合法数据。即：字段名合法，关键字、非空项都必须有值。
   * @param itemnames 项名表
   * @param notnulls 非空项表
   * @param keys 关键字表
   * @param item 原始数据
   * @return 转换后的合法数据
   */
  public static Hashtable getValidRecord(ArrayList itemnames,
      ArrayList notnulls,
      ArrayList keys,
      Hashtable item) {
    try {
      if ( itemnames == null ) return item;
      if ( item == null ) return null;
      String myname;
      Hashtable myitem = new Hashtable();
      for (int i=0; i < itemnames.size(); i++ ) {
        myname = itemnames.get(i).toString();
        if ( item.get(myname) == null )
          if ( ( (notnulls != null) && notnulls.contains(myname)  ) ||
               ( (keys != null) && keys.contains(myname))  )
            return null;
          else
            continue;
          myitem.put(myname,item.get(myname));
      }
      return myitem;
    }
    catch (Exception ex) {
      return null;
    }
  }

  /**
   * 删除数据记录对应的文件。
   * @param myrecord 数据记录
   * @param myfile 文件类型的项
   */
  public static void removeRecordFile(Hashtable myrecord,
                                      ArrayList myfile) {
    if ( myrecord == null ) return;
    if ( ( myfile == null ) ||
         ( myfile.size() < 1) )
      return;
    String mykey;
    String[] vv;
    for (Enumeration e=myrecord.keys(); e.hasMoreElements();) {
      mykey = e.nextElement().toString();
      if ( !myfile.contains(mykey) ) continue;
      if ( myrecord.get(mykey) == null ) continue;
      vv = myrecord.get(mykey).toString().split(DataReader.HASH_SPLITTER);
      if ( vv.length < 2 ) continue;
      CommonTools.removeFile(vv[1]);
    }
  }

  /**
   * 对数据排序
   * @param myrecords 排序前的数据
   * @param myname 排序字段名
   * @param isAscending 是否升序
   * @return 排序后的数据
   */
  public static ArrayList sortRecords(ArrayList myrecords,
                                      String myname,
                                      boolean  isAscending) {
    return CommonTools.sortData(myrecords,myname,isAscending,
                                "string",Constants.DEFAULT_LANGUAGE);
  }



}