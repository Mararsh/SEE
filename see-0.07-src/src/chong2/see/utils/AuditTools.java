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
 * <p>Title: ������̹�����</p>
 * <p>Description: Ϊ����з��ṩ�������ݺ������ƽ̨</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: ���</p>
 *
 * ����ƹ�����صľ�̬����
 *
 * @author ����
 * @version 0.07
 */

final public class AuditTools {

  /**
   * ����ֵ�ķָ���
   */
  public static String AUDIT_SPLITTER  = "a:m:s-a:m:s";

  /**
   * ��������Ϣ
   * @param myRequest ��ǰ����
   * @param myset ���ݼ���
   * @param myoperation ����
   * @return �Ƿ���ӳɹ�
   */
  public static String addAudit(HttpServletRequest myRequest,
                                String myset,
                                String myoperation) {
    return addAuditL(myRequest,myset,myoperation,
                     null,null,null,Constants.SUCCESSFUL,null,null);
  }

  /**
   * ��������Ϣ
   * @param myRequest ��ǰ����
   * @param myset ���ݼ���
   * @param myoperation ����
   * @param mykeys ���ݹؼ���
   * @param myold ���ݲ���ǰ��֪
   * @param mynew ���ݲ������ֵ
   * @param myresult �������
   * @param mydes ����
   * @param mypass �����򣬲����������ʾ
   * @return �Ƿ���ӳɹ�
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
   * ��������Ϣ
   * @param myRequest ��ǰ����
   * @param myset ���ݼ���
   * @param myoperation ����
   * @param mykeys ���ݹؼ���
   * @param myoldv ���ݲ���ǰ��֪
   * @param mynewv ���ݲ������ֵ
   * @param myresult �������
   * @param mydes ����
   * @param mypass �����򣬲����������ʾ
   * @return �Ƿ���ӳɹ�
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

    //������Ϣ���������
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