package chong2.see.utils;

import chong2.see.data.base.Constants;
import chong2.see.data.base.Language;
import chong2.see.data.*;
import chong2.see.servlet.common.DataManager;
import chong2.see.xml.DataStructureXmlParser;
import chong2.see.data.DataReader;
import chong2.see.xml.DataXmlWriter;

import java.io.File;
import java.io.PrintWriter;
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
 * �����ݹ�����صľ�̬����
 *
 * @author ����
 * @version 0.07
 */

final public class DataManagerTools {

  /**
   * ����xml��ʱ�ļ�
   * @param inRequest ��ǰ����
   * @param out ��ǰ���
   * @param names �ֶ���
   * @param data ����
   * @param myset ���ݼ���
   * @return �Ƿ�ɹ�
   */
  public static String writeXmlFile(HttpServletRequest inRequest,
                                    PrintWriter out,
                                    ArrayList names,
                                    ArrayList data,
                                    String myset) {

    try {
      String fname = getTmpFile(inRequest,myset,"xml") ;
      if ( fname == null ) return "invaild_data";
      DataXmlWriter myw = new DataXmlWriter();
      String retw = myw.writeData(names,data,
                                  fname ,
                                  ServletTools.getParameter(inRequest,
                                  "input_mycharset"));
      if ( !Constants.SUCCESSFUL.equals(retw) ) return null;
      String mylink = DataManager.TMP_FILE_PREFIX +
                      CommonTools.getFileName(fname);
      return mylink;
    }
    catch (Exception ex) {
      return null;
    }

  }

  /**
   * ����txt��ʱ�ļ�
   * @param inRequest ��ǰ����
   * @param out ��ǰ���
   * @param names �ֶ���
   * @param dis �ֶ���ʾ��
   * @param data ����
   * @param myset ���ݼ���
   * @return �Ƿ�ɹ�
   */
  public static String writeTxtFile(HttpServletRequest inRequest,
                                    PrintWriter out,
                                    ArrayList names,
                                    ArrayList dis,
                                    ArrayList data,
                                    String myset) {

    try {
      String fname = getTmpFile(inRequest,myset,"txt");
      if ( fname == null ) return "invaild_data";
      String retw =
          CommonTools.writeTxtFile(fname,
          ServletTools.getParameter(inRequest, "input_mycharset"),
          names,dis,data );
      if ( !Constants.SUCCESSFUL.equals(retw) ) return null;
      String mylink = DataManager.TMP_FILE_PREFIX +
                      CommonTools.getFileName(fname);
      return mylink;
    }
    catch (Exception ex) {
      return null;
    }

  }

  /**
   * �����ʱ�ļ�
   * @param inRequest ��ǰ����
   */
  public static void clearTmpFiles(HttpServletRequest inRequest) {

    try {
      String dname = DataManager.TMP_FILE_PREFIX ;
      ArrayList myfiles = ServletTools.getDirListFile(inRequest,
          DataManager.TMP_FILE_PREFIX);
      if ( myfiles == null) return ;
      File myf;
      long mynow = CommonTools.getCurrentTime();
      int mystay = 60000 * DataManager.TMP_FILE_STAY_MINUTES ; //���뵽����ת��
      for ( int i=0; i < myfiles.size(); i++ ) {
        myf = (File)myfiles.get(i);
        if ( ( mynow - myf.lastModified() ) > mystay )
          myf.delete();
      }
    }
    catch (Exception ex) {
      return ;
    }

  }

  /* ===================== ����xml���ݵķ��� ==================== */

  /**
   * ������ݵĽṹ����
   * @param inRequest ��ǰ����
   * @return ���ݵĽṹ����
   */
  public static DataStructureXmlParser getDataStructureReader(HttpServletRequest inRequest) {
    return getDataStructureReader(inRequest,
                                  ServletTools.getParameter(inRequest,"myset"));
  }

  /**
   * ������ݵĽṹ����
   * @param inRequest ��ǰ����
   * @param myset ���ݼ�����
   * @return ���ݵĽṹ����
   */
  public static DataStructureXmlParser getDataStructureReader(HttpServletRequest inRequest,
      String  myset) {
    try {
      String fstruc =
          getDataStructureFile(inRequest,myset);
      if ( fstruc == null ) return null;
      DataStructureXmlParser mystrucReader =
          new DataStructureXmlParser();
      String ret = mystrucReader.startParsing(fstruc);
      if ( !Constants.SUCCESSFUL.equals(ret) ) return null;
      return mystrucReader;
    }
    catch (Exception ex) {
      return null;
    }
  }

  /**
   * ������ݶ�ȡ���
   * @param inRequest ��ǰ����
   * @param mystrucReader ���ݼ��Ľṹ����
   * @param myset ���ݼ���
   * @param myitems �������б�
   * @param mybasewhere ��ȡ���ݵĻ�������
   * @param mysetwhere ��ȡ���ݵ���������
   * @return ���ݶ�ȡ���
   */
  public static DataReader getDataReader(HttpServletRequest inRequest,
      DataStructureXmlParser mystrucReader,
      String myset,
      ArrayList myitems,
      ArrayList mybasewhere,
      ArrayList mysetwhere) {
    try {
      int  mypagesize = ServletTools.setSessionPageSize(inRequest);
      int startindex = 1 , endindex = -1;
      if ( ServletTools.getParameter(inRequest,"mystartindex") != null ) {
        startindex =
            CommonTools.stringToInt(ServletTools.getParameter(inRequest,"mystartindex"));
        if ( startindex == CommonTools.WRONG_INT )
          startindex = 1;
      }
      if ( startindex < 1 ) startindex = 1;
      endindex = startindex + mypagesize - 1;
      return getDataReader(inRequest,mystrucReader,myset,
          myitems,mybasewhere,mysetwhere,startindex,endindex);
    }
    catch (Exception ex) {
      return null;
    }
  }

  /**
   * ������ݶ�ȡ���
   * @param inRequest ��ǰ����
   * @param myset ���ݼ���
   * @param myitems �������б�
   * @param startIndex ��ȡ���ݵĿ�ʼ���
   * @param endIndex ��ȡ���ݵĽ������
   * @param mywhere ��ȡ���ݵ�����
   * @return ���ݶ�ȡ���
   */
  public static DataReader getDataReader(HttpServletRequest inRequest,
      String myset,
      ArrayList myitems,
      int  startIndex,
      int  endIndex,
      ArrayList mywhere) {
    try {
      DataStructureXmlParser mystrucReader =
          getDataStructureReader(inRequest,myset) ;
      if ( mystrucReader == null ) {
        return null;
      }
      return getDataReader(inRequest,mystrucReader,myset,
                           myitems,mywhere,null,startIndex,endIndex);
    }
    catch (Exception ex) {
      return null;
    }
  }

  /**
   * ������ݶ�ȡ���
   * @param inRequest ��ǰ����
   * @param mystrucReader ���ݼ��Ľṹ����
   * @param myset ���ݼ���
   * @param myitems �������б�
   * @param mywhere ��ȡ���ݵ�����
   * @param startIndex ��ȡ���ݵĿ�ʼ���
   * @param endIndex ��ȡ���ݵĽ������
   * @return ���ݶ�ȡ���
   */
  public static DataReader getDataReader(HttpServletRequest inRequest,
      DataStructureXmlParser mystrucReader,
      String myset,
      ArrayList myitems,
      ArrayList mywhere,
      int  startIndex,
      int  endIndex) {
    return getDataReader(inRequest,mystrucReader,myset,
                         myitems,mywhere,null,startIndex,endIndex);

  }

  /**
   * ������ݶ�ȡ���
   * @param inRequest ��ǰ����
   * @param mystrucReader ���ݼ��Ľṹ����
   * @param myset ���ݼ���
   * @param myitems �������б�
   * @param mybasewhere ��ȡ���ݵĻ�������
   * @param mysetwhere ��ȡ���ݵ���������
   * @param startIndex ��ȡ���ݵĿ�ʼ���
   * @param endIndex ��ȡ���ݵĽ������
   * @return ���ݶ�ȡ���
   */
  public static DataReader getDataReader(HttpServletRequest inRequest,
      DataStructureXmlParser mystrucReader,
      String myset,
      ArrayList myitems,
      ArrayList mybasewhere,
      ArrayList mysetwhere,
      int  startIndex,
      int  endIndex) {
    String mysort = getSort(inRequest,mystrucReader);
    boolean isac =
        "ascending".equals(getOrder(inRequest,mystrucReader));
    return  getDataReader(inRequest,mystrucReader,
                          myset,myitems,mybasewhere,
                          mysetwhere,startIndex,endIndex,
                          mysort,isac);
  }

  /**
   * ������ݶ�ȡ���
   * @param inRequest ��ǰ����
   * @param mystrucReader ���ݼ��Ľṹ����
   * @param myset ���ݼ���
   * @param myitems �������б�
   * @param mybasewhere ��ȡ���ݵĻ�������
   * @param mysetwhere ��ȡ���ݵ���������
   * @param startIndex ��ȡ���ݵĿ�ʼ���
   * @param endIndex ��ȡ���ݵĽ������
   * @param mysort �����ֶ���
   * @param isAscending �Ƿ�����
   * @return ���ݶ�ȡ���
   */
  public static DataReader getDataReader(HttpServletRequest inRequest,
      DataStructureXmlParser mystrucReader,
      String myset,
      ArrayList myitems,
      ArrayList mybasewhere,
      ArrayList mysetwhere,
      int  startIndex,
      int  endIndex,
      String mysort,
      boolean isAscending) {
    try {
      DataReader mydataReader =
          DataReaderGetor.dataReader(inRequest,myset);
      Hashtable mytypes = null;
      if (mystrucReader != null)
        mytypes = mystrucReader.getItemTypes();
      String myenc = ServletTools.getAppDefaultEncoding(inRequest);
      if ( mysort != null ) {
        String mysorttype = mystrucReader.getItemAttr(mysort,"type");
        if ( mysorttype == null ) mysorttype = "string";
        mydataReader.startReading(startIndex,endIndex,
                                  myitems,mybasewhere, mysetwhere,
                                  mysort,mysorttype,
                                  isAscending,
                                  mytypes,myenc );
      }
      else
        mydataReader.startReading(startIndex,endIndex,
                                  myitems,mybasewhere, mysetwhere,
                                  null, null,true,
                                  mytypes,myenc );
      return mydataReader;
    }
    catch (Exception ex) {
      return null;
    }
  }



  /**
   * ������ݵĽṹ�����ļ���
   * @param myRequest ��ǰ����
   * @return ���ݵĽṹ�����ļ���
   */
  public static String getDataStructureFile(HttpServletRequest myRequest) {
    return getDataStructureFile(myRequest,ServletTools.getParameter(myRequest,"myset"));
  }

  /**
   * ������ݵĽṹ�����ļ���
   * @param myRequest ��ǰ����
   * @param myset ���ݼ���
   * @return ���ݵĽṹ�����ļ���
   */
  public static String getDataStructureFile(HttpServletRequest myRequest,
      String myset) {
    return getDataStructureFile( ServletTools.getContext(myRequest),
                                 myset);
  }

  /**
   * ������ݵĽṹ�����ļ���
   * @param myContext Ӧ�û���
   * @param myset ���ݼ���
   * @return ���ݵĽṹ�����ļ���
   */
  public static String getDataStructureFile(ServletContext myContext,
      String myset) {
    try {
      if ( ( myset == null ) || "".equals(myset) ) return null;
      String myname = new String(myset);
      if ( DataManager.SUPER_SEER.equals(myname) )
        myname = DataManager.USER;
      if ( Language.getEncodingList().contains(myname) )
        myname = DataManager.LANGUAGE;
      String fstruc =
          DataStructureXmlParser.DATASET_STRUCTURE_PATH_PREFIX +
//          ServletTools.getAppDefaultEncoding(myContext) + "/" +
      DataStructureXmlParser.DATASET_STRUCTURE_FILE_PREFIX +
          myname +".xml";
      return ServletTools.getFileRealPath(myContext,fstruc);
    }
    catch (Exception ex) {
      return null;
    }
  }

  /**
   * ������ݵ��ļ���
   * @param myRequest ��ǰ����
   * @return ���ݵ��ļ���
   */
  public static String getDataValuesFile(HttpServletRequest myRequest) {
    return   getDataValuesFile(myRequest,
                               ServletTools.getParameter(myRequest,"myset"));
  }

  /**
   * ������ݵ��ļ���
   * @param myContext Ӧ�û���
   * @param myset ���ݼ���
   * @return ���ݵ��ļ���
   */
  public static String getDataValuesFile(ServletContext myContext,
      String myset) {
    try {
      if ( ( myset == null ) || "".equals(myset) ) return null;
      String fstruc = DataReader.DATASET_VALUES_FILE_PREFIX +
                      myset +".xml";
      return ServletTools.getFileRealPath(myContext,fstruc);
    }
    catch (Exception ex) {
      return null;
    }
  }

  /**
   * ������ݵ��ļ���
   * @param myRequest ��ǰ����
   * @param myset ���ݼ���
   * @return ���ݵ��ļ���
   */
  public static String getDataValuesFile(HttpServletRequest myRequest,
      String myset) {
    return getDataValuesFile( ServletTools.getContext(myRequest),
                              myset);
  }

  /**
   * �����ʱ�ļ���������������ں�����ϵ�ǰʱ�������������10�Ρ�
   * @param myRequest ��ǰ����
   * @param myset ���ݼ���
   * @param mysuffix �ļ���չ��
   * @return ��ʱ�ļ���������·����
   */
  public static String getTmpFile(HttpServletRequest myRequest,
                                  String myset,
                                  String mysuffix) {
    try {
      if ( ( myset == null ) || "".equals(myset) ) return null;
      if ( ( mysuffix == null ) || "".equals(mysuffix) ) return null;
      String fname = DataManager.TMP_FILE_PREFIX +
                     myset + "-" +
                     CommonTools.getCurrentTime() +
                    "." + mysuffix ;
      String ff = ServletTools.getFileRealPath(myRequest,fname);
      File f = new File(ff);
      int count = 0, max = 10;
      while (f.exists()) {
        if (count > max ) return null;
        count++;
        fname = DataManager.TMP_FILE_PREFIX +
                myset + "-" +
                CommonTools.getCurrentTime() + count ;
        ff = ServletTools.getFileRealPath(myRequest,fname);
        f = new File(ff);
      }
      return ff;
    }
    catch (Exception ex) {
      return null;
    }
  }


  /**
   * ��ö�ȡ���ݵ�����
   * @param myRequest ��ǰ����
   * @return ��ȡ���ݵ�����
   */
  public static Hashtable getWhere(HttpServletRequest myRequest) {
    String mypawhere = ServletTools.getParameter(myRequest,"mywhere");
    if ( ( mypawhere == null ) ||
         "".equals(mypawhere))
      mypawhere = ServletTools.getParameter(myRequest,"mykeys");
    return getWhere(myRequest,mypawhere);
  }

  /**
   * ��ö�ȡ���ݵ�����
   * @param myRequest ��ǰ����
   * @param mywhere �����ַ���
   * @return ��ȡ���ݵ�����
   */
  public static Hashtable getWhere(HttpServletRequest myRequest,
                                   String mywhere) {
    try {
      if ( mywhere == null ) return  null;
      Hashtable myw;
      if ( mywhere.indexOf(QueryTools.CONDITION_SPLITTER) > -1 )
        myw = CommonTools.stringToHash(mywhere,
                                       QueryTools.CONDITION_SPLITTER);
      else if ( mywhere.indexOf(DataReader.VALUE_HASH_SPLITTER) > -1 )
        myw = CommonTools.stringToHash(mywhere,
                                       DataReader.VALUE_HASH_SPLITTER);
      else
        myw = CommonTools.stringToHash(mywhere,
                                       DataReader.HASH_SPLITTER);
      String mykey,myvalue;
      for (Enumeration mye = myw.keys(); mye.hasMoreElements();) {
        mykey = mye.nextElement().toString();
        myvalue = myw.get(mykey).toString();
        if ( myvalue.equals("_self") ) {
          myw.put(mykey,ServletTools.getSessionUser(myRequest));
        } else if ( myvalue.endsWith("_self") ) {
          String myprefix = myvalue.substring(0, myvalue.length() - 5);
          if ( QueryTools.CONDITION_OPERATORS.contains(myprefix) )
            myw.put(mykey, myprefix +
                    ServletTools.getSessionUser(myRequest));
        }
      }
      return myw;
    }
    catch (Exception ex) {
      return null;
    }
  }

  public static ArrayList getMultiWhere(HttpServletRequest myRequest) {
    String mypawhere = ServletTools.getParameter(myRequest,"mywhere");
    if ( ( mypawhere == null ) ||
         "".equals(mypawhere))
      mypawhere = ServletTools.getParameter(myRequest,"mykeys");
    return getMultiWhere(myRequest,mypawhere);
  }

  /**
   * ��ö�ȡ���ݵ����������ϵ�Ķ��������
   * @param myRequest ��ǰ����
   * @param mywhere �����ַ���
   * @return ��ȡ���ݵ�����
   */
  public static ArrayList getMultiWhere(HttpServletRequest myRequest,
                                        String mywhere) {
    try {
      if ( ( mywhere == null ) ||
           "".equals(mywhere) )
        return null;
      ArrayList mymwhere = new ArrayList();
      ArrayList mywheres =
          CommonTools.stringToArray(mywhere,QueryTools.MULTI_SPLITTER);
      Hashtable mykeyvalues;
      for (int i=0; i< mywheres.size(); i++) {
        mykeyvalues = getWhere(myRequest,mywheres.get(i).toString());
        if ( mykeyvalues != null )
          mymwhere.add(mykeyvalues);
      }
      return mymwhere;
    }
    catch (Exception ex) {
      return null;
    }
  }


  /**
   * ���������е������ַ�
   * @param myRequest ��ǰ����
   * @param myvalue ����ֵ
   * @param myset ����
   * @return ���ݵ���ʾֵ
   */
  public static String getValueSpecial(HttpServletRequest myRequest,
                                       String myvalue,
                                       String myset) {
    try {
      if ( (myvalue == null) || "".equals(myvalue) )
        return myvalue;
      String myv = myvalue;
      String mykc;
      Enumeration myk = DataReader.SPECIAL_CHARS.keys();
      if ( DataManager.INTERFACE_THEME.equals(myset))
        return myv;
      // �û�������ת����ŵ������ַ����ڱ༭����ʾʱ��ԭ��
      for (; myk.hasMoreElements();) {
        mykc = myk.nextElement().toString();
        myv = myv.replaceAll(DataReader.SPECIAL_CHARS.get(mykc).toString(),
                       mykc);
      }
      // ˫���Ż�����ҳ�༭�ؼ���������ң�
      myv = myv.replaceAll("\"","&quot;");

      return myv;
    }
    catch (Exception ex) {
      return myvalue;
    }
  }

  /**
   * ������ݵı༭ֵ
   * @param myRequest ��ǰ����
   * @param myvalue ����ֵ
   * @param myset ����
   * @return ���ݵı༭ֵ
   */
  public static String getValueEdit(HttpServletRequest myRequest,
                                    String myvalue,
                                    String myset) {
    try {
      if ( (myvalue == null) || "".equals(myvalue) )
        return myvalue;
      String myv = getValueSpecial(myRequest,myvalue, myset);
      myv = myv.replaceAll(DataReader.CHAR_RETURN,"\r\n");
      return myv;
    }
    catch (Exception ex) {
      return myvalue;
    }
  }

  /**
   * ������ݵ���ʾֵ
   * @param myRequest ��ǰ����
   * @param myvalue ����ֵ
   * @param mytype ��������
   * @param myset ����
   * @return ���ݵ���ʾֵ
   */
  public static String getValueShow(HttpServletRequest myRequest,
                                    String myvalue,
                                    String mytype,
                                    String myset) {
    try {
      if ( (myvalue == null) ||
           "".equals(myvalue) ||
           "password".equals(mytype))
        return "";
      String myv = getValueSpecial(myRequest,myvalue,myset);
      if ( "link".equals(mytype) ||
           "file".equals(mytype) )
        return getLinkShow(myRequest,myv,true, mytype);
      String myenc = ServletTools.getSessionEncoding(myRequest);
      myv = myv.replaceAll(DataReader.LIST_SPLITTER,"<BR>");
      myv = myv.replaceAll("\r\n","<BR>");
      ArrayList myav =
          CommonTools.stringToArray(myv,QueryTools.MULTI_SPLITTER);
      String mystr;
      StringBuffer myout = new StringBuffer();
      Hashtable myvv, myvh ;
      String ss = " ",mykey,mys;
      if ( myav.size() == 1 ) ss = "";
      ArrayList myaam;
      for (int i=0; i< myav.size();i++) {
        mystr = myav.get(i).toString();
        myaam = CommonTools.stringToArray(mystr,
            AuditTools.AUDIT_SPLITTER);
        for (int j=0; j< myaam.size();j++) {
          mystr = myaam.get(j).toString();
          myvh =  ServletTools.stringToHash(myRequest,mystr,
              DataReader.VALUE_HASH_SPLITTER);
          if ( j > 0 ) myout.append("<BR>");
          if ( ( myvh != null ) &&
               ( myvh.keys().hasMoreElements() ) ) {
            for (Enumeration e = myvh.keys() ; e.hasMoreElements() ;) {
              mykey =  e.nextElement().toString();
              mys = myvh.get(mykey).toString();
              myvv =  ServletTools.stringToHash(myRequest,mys,
                  DataReader.HASH_SPLITTER);
              if ( ( myvv != null ) &&
                   ( myvv.keys().hasMoreElements() ) )
                myvh.put(mykey,myvv.toString());
              else
                myvh.put(mykey,mys);
            }
            myout.append(myvh.toString());
            myout.append(ss);
          } else {
            myvv =  ServletTools.stringToHash(myRequest,mystr,
                DataReader.HASH_SPLITTER);
            if ( ( myvv != null ) &&
                 ( myvv.keys().hasMoreElements() ) ) {
              myout.append(myvv.toString());
              myout.append(ss);
            }
            else {
              myout.append(mystr);
              myout.append(ss);
            }
          }
        }
      }
      return myout.toString();
    }
    catch (Exception ex) {
      return myvalue;
    }
  }

  /**
   * ������ӵ���ʾֵ
   * @param inRequest ��ǰ����
   * @param myvalue ����ֵ
   * @param newopen �Ƿ����´����������
   * @param mytype ��������
   * @return ���ӵ���ʾֵ
   */
  public static String  getLinkShow(HttpServletRequest inRequest,
                                    String myvalue,
                                    boolean  newopen,
                                    String mytype) {
    try{
      if (  myvalue == null ) return "";
      String myn = null, myv =null;
      String[] vv =
          myvalue.toString().split(DataReader.HASH_SPLITTER);
      if ( vv.length < 2 ) return "";
      String mytarget = "";
      if ( newopen ) mytarget = " target=_blank ";
      StringBuffer myshow = new StringBuffer();
      myshow.append("<A href=\"");
      if ( "file".equals(mytype))
        myshow.append(DataManager.UPLOAD_FILE_PREFIX);
      myshow.append(vv[1]);
      myshow.append("\" ");
      myshow.append(mytarget);
      myshow.append(">");
      // ��ʾͼƬ������ͼ
      if ( "file".equals(mytype) &&
           (vv[1].endsWith(".jpeg") ||
           vv[1].endsWith(".jpg") ||
           vv[1].endsWith(".gif") ||
           vv[1].endsWith(".png") )) {
        myshow.append("<IMG src=\"");
        myshow.append(DataManager.UPLOAD_FILE_PREFIX);
        myshow.append(vv[1]);
        myshow.append("\" height=60 border=0>");
      }
      myshow.append(vv[0]);
      myshow.append("</A>");
      if ( "file".equals(mytype) ) {
        long  mysize = CommonTools.getFileSize(vv[1]) / 1000 ;
        if ( mysize < 1 ) mysize = 1;
        myshow.append(" &nbsp (");
        myshow.append(mysize);
        myshow.append("KB)");
      }
      return myshow.toString();
    } catch (Exception ex) {
      return "";
    }
  }


  /**
   * ��hash�ִ�ת����hashֵ�ִ����������ڷָ����
   * @param mywhere hash�ִ�
   * @return ת�����hashֵ�ִ�
   */
  public static String getValueWhere(String mywhere) {
    if ( mywhere == null ) return null;
    String myw = new String(mywhere);
    return myw.replaceAll(QueryTools.CONDITION_SPLITTER,
                          DataReader.VALUE_HASH_SPLITTER);
  }

  /**
   * ������ݵ���ʾɫ������������ҳ�渳ֵ���������������ݶ��������
   * @param myRequest ��ǰ����
   * @param mystrucReader ���ݵĽṹ����
   * @return ���ݵ���ʾɫ������
   */
  public static Hashtable getColor(HttpServletRequest myRequest,
                                   DataStructureXmlParser mystrucReader ) {
    try {
      Hashtable mycmap = getColor(myRequest);
      if ( mycmap != null ) return mycmap;
      Object mycolor =
          mystrucReader.getDataSet().get("defaultColor");
      if ( mycolor == null ) return null;
      String myc = mycolor.toString();
      if ( myc.indexOf(QueryTools.CONDITION_SPLITTER) > -1 )
        mycmap = CommonTools.stringToHash(myc,
                                       QueryTools.CONDITION_SPLITTER);
      else if ( myc.indexOf(DataReader.VALUE_HASH_SPLITTER) > -1 )
        mycmap = CommonTools.stringToHash(myc,
                                       DataReader.VALUE_HASH_SPLITTER);
      else
        mycmap = CommonTools.stringToHash(myc,
                                       DataReader.HASH_SPLITTER);
      if ( mycmap == null ) return null;
      if ( mycmap.get("name") == null ) return null;
      if ( mycmap.get("others") == null )
        mycmap.put("others","EEEEEE");
      if ( !"yes".equals(mystrucReader.getItemAttr(mycmap.get("name").toString(),
          "valueTranslated")))
        return mycmap;

      String mykey;
      Hashtable mytr = new Hashtable();
      for (Enumeration e=mycmap.keys(); e.hasMoreElements();) {
        mykey = e.nextElement().toString();
        if ( "name".equals(mykey) ||
             "others".equals(mykey) )
          mytr.put(mykey,mycmap.get(mykey));
        else
          mytr.put(LanguageTools.atr(myRequest, mykey),
                   mycmap.get(mykey));
      }
      return mytr;
    }
    catch (Exception ex) {
      return null;
    }
  }

  /**
   * ������ݵ���ʾɫ����������ҳ�渳ֵ������
   * @param myRequest ��ǰ����
   * @return ���ݵ���ʾɫ������
   */
  public static Hashtable getColor(HttpServletRequest myRequest) {
    try {
      String mycolor = ServletTools.getParameter(myRequest,"mycolor");
      if ( mycolor == null ) return null;
      Hashtable mycmap;
      if ( mycolor.indexOf(QueryTools.CONDITION_SPLITTER) > -1 )
        mycmap = CommonTools.stringToHash(mycolor,
                                       QueryTools.CONDITION_SPLITTER);
      else if ( mycolor.indexOf(DataReader.VALUE_HASH_SPLITTER) > -1 )
        mycmap = CommonTools.stringToHash(mycolor,
                                       DataReader.VALUE_HASH_SPLITTER);
      else
        mycmap = CommonTools.stringToHash(mycolor,
                                       DataReader.HASH_SPLITTER);
      if ( mycmap == null ) return null;
      if ( mycmap.get("name") == null ) return null;
      if ( mycmap.get("others") == null )
        mycmap.put("others","EEEEEE");
      return mycmap;
    }
    catch (Exception ex) {
      return null;
    }
  }

  /**
   * ������ݵ������ֶ���������ҳ�渳ֵ���������������ݶ��������
   * @param myRequest ��ǰ����
   * @param mystrucReader ���ݵĽṹ����
   * @return ���ݵ������ֶ���
   */
  public static String getSort(HttpServletRequest myRequest,
                               DataStructureXmlParser mystrucReader ) {
    try {
      String mysort =
          ServletTools.getParameter(myRequest,"mysort");
      if ( mysort != null ) return mysort;
      Object mydsort =
          mystrucReader.getDataSet().get("defaultSort");
      if ( mydsort == null ) return null;
      return mydsort.toString();
    }
    catch (Exception ex) {
      return null;
    }
  }


  /**
   * ������ݵ�����������ҳ�渳ֵ���������������ݶ��������
   * @param myRequest ��ǰ����
   * @param mystrucReader ���ݵĽṹ����
   * @return ���ݵ�������
   */
  public static String getOrder(HttpServletRequest myRequest,
                                DataStructureXmlParser mystrucReader ) {
    try {
      String myorder =
          ServletTools.getParameter(myRequest,"myorder");
      if ( myorder != null ) return myorder;
      Object mydorder =
          mystrucReader.getDataSet().get("defaultOrder");
      if ( mydorder == null ) return null;
      return mydorder.toString();
    }
    catch (Exception ex) {
      return null;
    }
  }

}