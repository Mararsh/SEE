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
 * <p>Title: ������̹�����</p>
 * <p>Description: Ϊ����з��ṩ�������ݺ������ƽ̨</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: ���</p>
 *
 * ���ݶ�ȡ��ͨ�÷�����
 * ���á�����ģʽ��ʵ�֡�
 *
 * @author ����
 * @version 0.07
 */

public class DataReaderGetor {

  /**
   * ������ݶ�ȡ��
   * @param myRequest ��ǰ����
   * @param myname ������
   * @return ���ݶ�ȡ��
   */
  public static DataReader dataReader(HttpServletRequest myRequest,
                                      String myname) {
    return dataReader(ServletTools.getContext(myRequest),
                      myname);
  }

  /**
   * ������ݶ�ȡ��
   * @param myRequest ��ǰ����
   * @param mytype ����Դ����
   * @param myname ������
   * @return ���ݶ�ȡ��
   */
  public static DataReader dataReader(HttpServletRequest myRequest,
                                      String mytype,
                                      String myname) {
    return dataReader(ServletTools.getContext(myRequest),
                      mytype,myname);
  }

  /**
   * ������ݶ�ȡ��
   * @param myContext ��ǰӦ�û���
   * @param myname ������
   * @return ���ݶ�ȡ��
   */
  public static DataReader dataReader(ServletContext myContext,
                                      String myname) {
    String mytype = ServletTools.getAppDataSourceType(myContext);
    return dataReader(myContext,mytype,myname);
  }

  /**
   * ������ݶ�ȡ��
   * @param myContext ��ǰӦ�û���
   * @param mytype ����Դ����
   * @param myname ������
   * @return ���ݶ�ȡ��
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
   * ������ݶ�ȡ��
   * @param mytype ����Դ����
   * @param myname ����Դ��
   * @return ���ݶ�ȡ��
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
   * ȡ��һ���ֶε����еĲ�ֵͬ
   * @param myrecords ����
   * @param myname �ֶ���
   * @return һ���ֶε����еĲ�ֵͬ
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
   * ���һ�����ݵĹؼ���ֵ
   * @param keys �ؼ��ֶ�����
   * @param item ����
   * @return ���ݵĹؼ���ֵ
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
   * ��ԭʼ����ת��Ϊ�Ϸ����ݡ������ֶ����Ϸ����ؼ��֡��ǿ��������ֵ��
   * @param itemnames ������
   * @param notnulls �ǿ����
   * @param keys �ؼ��ֱ�
   * @param item ԭʼ����
   * @return ת����ĺϷ�����
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
   * ɾ�����ݼ�¼��Ӧ���ļ���
   * @param myrecord ���ݼ�¼
   * @param myfile �ļ����͵���
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
   * ����������
   * @param myrecords ����ǰ������
   * @param myname �����ֶ���
   * @param isAscending �Ƿ�����
   * @return ����������
   */
  public static ArrayList sortRecords(ArrayList myrecords,
                                      String myname,
                                      boolean  isAscending) {
    return CommonTools.sortData(myrecords,myname,isAscending,
                                "string",Constants.DEFAULT_LANGUAGE);
  }



}