package chong2.see.data;

import chong2.see.data.base.Constants;
import chong2.see.utils.CommonTools;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * <p>Title: ������̹�����</p>
 * <p>Description: Ϊ����з��ṩ�������ݺ������ƽ̨</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: ���</p>
 *
 * ���ݶ�ȡ�����ࡣ��xml��ȡ������ݿ��ȡ�����̳С�
 *
 * @author ����
 * @version 0.07
 */

public abstract class DataReader extends DefaultHandler{

  //------------------ ����  ------------------

  /**
   * ���ֵ�ķָ���
   */
  public static String LIST_SPLITTER  = ":-:";

  /**
   * ����ֵ�ķָ���
   */
  public static String VALUE_HASH_SPLITTER  = ":v:h-v:h:";

  /**
   * hash��-ֵ�ķָ���
   */
  public static String HASH_SPLITTER  = ":h:p-h:p:";

  /**
   * δ֪��;�ķָ���
   */
  public static String SOME_SPLITTER  = ":s:m-s:m:";

  /**
   * �û�������">"���Ž�������Ϊ����ַ�������ʾ����ʱ��ʾΪ">"��
   */
  public static String CHAR_GREATER =  ":g:t-g:t:";

  /**
   * �û�������"<"���Ž�������Ϊ����ַ�������ʾ����ʱ��ʾΪ"<"��
   */
  public static String CHAR_LESS =  ":l:s-l:s:";

  /**
   * �û�������"&"���Ž�������Ϊ����ַ�������ʾ����ʱ��ʾΪ"&"��
   */
  public static String CHAR_AND =  ":a:d-a:d:";

  /**
   * �û�������'���Ž�������Ϊ����ַ�������ʾ����ʱ��ʾΪ'��
   */
  public static String CHAR_INVERTED_COMMA =  ":i:c-i:c:";

  /**
   * �û�������"���Ž�������Ϊ����ַ�������ʾ����ʱ��ʾΪ"��
   */
  public static String CHAR_QUOTATION_MARK =  ":q:m-q:m:";

  /**
   * �û������л��н�������Ϊ����ַ�������ʾ����ʱ���С�
   */
  public static String CHAR_RETURN =  ":r:t-r:t:";

  /**
   * �û���������Ҫת����ŵ������ַ������ǿ���ʹxml����ҳ������ҡ�
   */
  public static Hashtable SPECIAL_CHARS = new Hashtable();
  static {
    SPECIAL_CHARS.put(">",CHAR_GREATER);
    SPECIAL_CHARS.put("<",CHAR_LESS);
    SPECIAL_CHARS.put("&",CHAR_AND);
    SPECIAL_CHARS.put("\"",CHAR_QUOTATION_MARK);
    SPECIAL_CHARS.put("'",CHAR_INVERTED_COMMA);
    SPECIAL_CHARS.put("\r\n",CHAR_RETURN);
  }

  /**
   * ��Ϣ�����ļ�Ŀ¼���·�����ļ�ǰ׺
   */
  public static String DATASET_VALUES_FILE_PREFIX  =
      "/WEB-INF/data/value/see-value-";

  /**
   * �����ֶεĿ�ֵ
   */
  public static String SEE_PASSWORD_NULL =  "see_password_null" ;

  /**
   * �����ֶβ��޸�
   */
  public static String SEE_PASSWORD_NOT_MODIFY =
      "see_password_not_modify" ;

  //------------------ ����  ------------------

  protected String dataSourceType = "xml";
  protected String dataSourceName = "" ;
  protected int startIndex = 1;
  protected int endIndex = -1;
  protected int count = 0;

  protected ArrayList records = new ArrayList();
  protected boolean isAscending = false;
  protected String sortName= null;
  protected String sortType = null;
  protected String sortEncoding = null;
  protected Hashtable datatypes = null;
  protected ArrayList itemnames = null;
  protected ArrayList setwhere = null;
  protected ArrayList basewhere = null;

  // --------------------- ���÷��� ----------------------

  /**
   * ��Ĺ�������
   */
  public DataReader()  {
    initReader();
  }

  /**
   * ��������Դ���͡�ȱʡΪxml
   * @param mytype ����Դ����
   */
  public void setDataSourceType(String mytype) {
    if (mytype==null) mytype = "xml";
    this.dataSourceType = mytype;
  }

  /**
   * ��ȡ����Դ����
   * @return ����Դ����
   */
  public String getDataSourceType() {
    return this.dataSourceType;
  }

  /**
   * ��ȡ���ݡ�ֻ��÷��������ġ�����󡢷�Χ�ڵ����ݡ�
   * @param startIndex ��ʼ���
   * @param endIndex �������
   * @param myitems �ֶ���
   * @param basewhere ������ѯ����
   * @param setwhere �û����õ�����
   * @param mysort �����ֶ���
   * @param mysorttype �����ֶε�����
   * @param isAscending �Ƿ�����
   * @param mytypes ��������
   * @param myenc ���ݱ���
   * @return �Ƿ�ɹ�
   */
  public String startReading(int  startIndex,
                             int  endIndex,
                             ArrayList myitems,
                             ArrayList basewhere,
                             ArrayList setwhere,
                             String mysort,
                             String mysorttype,
                             boolean  isAscending,
                             Hashtable mytypes,
                             String myenc)	{

    this.setIsAscending(isAscending);
    this.setSortName(mysort);
    this.setSortType(mysorttype);
    this.setDatatypes(mytypes);
    this.setSortEncoding(myenc);

    this.setEndIndex(endIndex);
    this.setStartIndex(startIndex);

    this.setSetWhere(setwhere);
    this.setBaseWhere(basewhere);

    this.setItems(myitems);

    return this.startReading();

  }

  /**
   * ��ȡ����
   * @return �Ƿ�ɹ�
   */
  public String startReading()	{
    return this.readData();
  }


  //------------------- �������Եķ��� -------------------


  /**
   * ��ȡ����Դ���ơ�
   * ����xml�������ļ��ľ���·�����������ݿ��������ݱ���
   * @return ����Դ����
   */
  public String getDataSourceName() {
    return dataSourceName;
  }

  /**
   * ��������Դ���ơ�
   * ����xml�������ļ��ľ���·�����������ݿ��������ݱ���
   * @param fileName ����Դ����
   * @return �Ƿ�ɹ�
   */
  public String setDataSourceName(String fileName) {
    this.dataSourceName = fileName;
    return Constants.SUCCESSFUL;
  }

  /**
   * �����������
   * @return ��������
   */
  public ArrayList getRecords() {
    return records;
  }

  /**
   * ��÷���������һ������
   * @param mywhere ����
   * @return ����������һ������
   */
  public Hashtable getRecord(Hashtable mywhere) {
    try {
      if ( records == null ) return null;
      if ( mywhere == null ) return null;
      Hashtable item;
      for (int i=0; i < records.size(); i++) {
        item = (Hashtable)records.get(i);
        if ( CommonTools.isValidHash(item, mywhere,
                                     this.datatypes,
                                     this.sortEncoding) )
          return item;
      }
      return null;
    }
    catch (Exception ex) {
      return null;
    }
  }

  /**
   * ��÷�����������������
   * @param mywhere ����
   * @return ������������������
   */
  public ArrayList getRecords(Hashtable mywhere) {
    try {
      if ( records == null ) return null;
      if ( mywhere == null ) return records;
      ArrayList myrecords = new ArrayList();
      Hashtable item;
      for (int i=0; i < records.size(); i++) {
        item = (Hashtable)records.get(i);
        if ( CommonTools.isValidHash(item, mywhere,
                                     this.datatypes,
                                     this.sortEncoding) )
          myrecords.add(item);
      }
      return myrecords;
    }
    catch (Exception ex) {
      return records;
    }
  }

  /**
   * ��úϷ�����������
   * @param itemnames ������
   * @param notnulls �ǿ����
   * @param keys �ؼ��ֱ�
   * @return �Ϸ�����������
   */
  public ArrayList getValidRecords(ArrayList itemnames,
                                   ArrayList notnulls,
                                   ArrayList keys) {
    try {
      if ( records == null ) return null;
      ArrayList myvdata = new ArrayList();
      Hashtable item;
      for (int i=0; i < records.size(); i++) {
        item = DataReaderGetor.getValidRecord(itemnames,notnulls,keys,
        (Hashtable)records.get(i));
        if ( item == null ) continue;
        myvdata.add(item);
      }
      return myvdata;
    }
    catch (Exception ex) {
      return null;
    }
  }

  /**
   * ��ȡ�������ݵĹؼ���ֵ
   * @param keys �ؼ�����
   * @return �������ݵĹؼ���ֵ
   */
  public ArrayList getRecordKeys(ArrayList keys) {
    try {
      if ( keys == null ) return null;
      ArrayList mydata = getRecords();
      if ( mydata == null) return null;
      ArrayList mykeys =  new ArrayList();
      Hashtable myd;
      for (int i=0; i<mydata.size(); i++) {
        myd = (Hashtable)mydata.get(i);
        if (myd == null) continue;
        myd = DataReaderGetor.getRecordKey(keys,myd);
        if (myd == null) continue;
        mykeys.add(myd);
      }
      return mykeys;
    }
    catch (Exception ex) {
      return null;
    }
  }


  /**
   * ����һ���Ϸ�������
   * @param itemnames ����������
   * @param notnulls �ǿ����
   * @param keys �ؼ��ֱ�
   * @param mywhere ����λ�õ���������
   * @param item ����������
   * @return ������ĺϷ�����
   */
  public Hashtable insertValidRecord(ArrayList itemnames,
                                     ArrayList notnulls,
                                     ArrayList keys,
                                     Hashtable mywhere,
                                     Hashtable item) {
    ArrayList myrecords = getValidRecords(itemnames,notnulls,keys);
    return insertValidRecord(myrecords,itemnames,notnulls,keys,mywhere,item);
  }

  /**
   * ����һ���Ϸ�������
   * @param myrecords ��������
   * @param itemnames ����������
   * @param notnulls �ǿ����
   * @param keys �ؼ��ֱ�
   * @param mywhere ����λ�õ���������
   * @param item ����������
   * @return ������ĺϷ�����
   */
  public Hashtable insertValidRecord(ArrayList myrecords,
                                     ArrayList itemnames,
                                     ArrayList notnulls,
                                     ArrayList keys,
                                     Hashtable mywhere,
                                     Hashtable item) {
    try {
      if ( mywhere == null )
        return addValidRecord(myrecords,itemnames,notnulls,keys,item);
      if ( myrecords == null )
        return addValidRecord(myrecords,itemnames,notnulls,keys,item);
      Hashtable myitem = DataReaderGetor.getValidRecord(itemnames,notnulls,keys,item);
      if ( myitem == null ) return null;
      if ( "yes".equals(this.checkExistRecord(keys,myitem) ) )
        return null;
      Hashtable record;
      for  (int i=0; i< myrecords.size(); i++) {
        record = (Hashtable)myrecords.get(i);
        if ( !CommonTools.isValidHash(record, mywhere,
                                      this.datatypes,
                                      this.sortEncoding) )
          continue;
        myrecords.add(i,myitem);
        this.records = myrecords;
        return myitem;
      }
      return null;
    }
    catch (Exception ex) {
      return null;
    }
  }


  /**
   * ���һ���Ϸ������ݡ��������������ӡ�
   * @param itemnames ����������
   * @param notnulls �ǿ����
   * @param keys �ؼ��ֱ�
   * @param item ���������
   * @return ����ӵĺϷ�����
   */
  public Hashtable addValidRecord(ArrayList itemnames,
                                  ArrayList notnulls,
                                  ArrayList keys,
                                  Hashtable item) {
    try {
      ArrayList myrecords = getValidRecords(itemnames,notnulls,keys);
      return addValidRecord(myrecords,itemnames,notnulls,keys,item);
    }
    catch (Exception ex) {
      return null;
    }
  }

  /**
   * ���һ���Ϸ������ݡ��������������ӡ�
   * @param myrecords ��������
   * @param itemnames ����������
   * @param notnulls �ǿ����
   * @param keys �ؼ��ֱ�
   * @param item ���������
   * @return ����ӵĺϷ�����
   */
  public Hashtable addValidRecord(ArrayList myrecords,
                                  ArrayList itemnames,
                                  ArrayList notnulls,
                                  ArrayList keys,
                                  Hashtable item) {
    try {
      Hashtable myitem = DataReaderGetor.getValidRecord(itemnames,notnulls,keys,item);
      if ( myitem == null ) return null;
      if ( myrecords == null )
        myrecords = new ArrayList();
      if ( "no".equals(this.checkExistRecord(keys,myitem) ) ) {
        myrecords.add(myitem);
        this.records = myrecords;
        return myitem;
      } else
        return null;
    }
    catch (Exception ex) {
      return null;
    }
  }

  /**
   * �޸ĺϷ������ݡ�
   * @param itemnames ����������
   * @param notnulls �ǿ����
   * @param keys �ؼ��ֱ�
   * @param mywhere ���ұ��滻���ݵ�����
   * @param item ���滻����
   * @param mypass �������͵���
   * @return ���滻�ĺϷ�����
   */
  public Hashtable modifyValidRecord(ArrayList itemnames,
                                     ArrayList notnulls,
                                     ArrayList keys,
                                     Hashtable mywhere,
                                     Hashtable item,
                                     ArrayList mypass) {
    ArrayList myrecords =
        getValidRecords(itemnames,notnulls,keys);
    Object myold = modifyValidRecord(myrecords,itemnames,notnulls,keys,
                                     mywhere,item, mypass,false,true);
    if ( myold == null )
      return  null;
    else
      return (Hashtable)myold;
  }

  /**
   * �޸ĺϷ������ݡ���ȫ���Ƿ��������ľ����ݡ�
   * @param itemnames ����������
   * @param notnulls �ǿ����
   * @param keys �ؼ��ֱ�
   * @param item ���滻����
   * @param mypass �������͵���
   * @return ���滻�ĺϷ�����
   */
  public Hashtable modifyValidRecord(ArrayList itemnames,
                                     ArrayList notnulls,
                                     ArrayList keys,
                                     Hashtable item,
                                     ArrayList mypass) {

    ArrayList myrecords =
        getValidRecords(itemnames,notnulls,keys);
    return modifyValidRecord(myrecords,itemnames,notnulls,keys,
                             item, mypass);
  }


  /**
   * �޸ĺϷ������ݡ���ȫ���Ƿ��������ľ����ݡ�
   * @param myrecords ��������
   * @param itemnames ����������
   * @param notnulls �ǿ����
   * @param keys �ؼ��ֱ�
   * @param item ���滻����
   * @param mypass �������͵���
   * @return ���滻�ĺϷ�����
   */
  public Hashtable modifyValidRecord(ArrayList myrecords,
                                     ArrayList itemnames,
                                     ArrayList notnulls,
                                     ArrayList keys,
                                     Hashtable item,
                                     ArrayList mypass) {
    Hashtable mywhere = DataReaderGetor.getRecordKey(keys,item);
    if ( mywhere == null ) return null;
    Object myold = modifyValidRecord(myrecords,itemnames,notnulls,keys,
                                     mywhere,item, mypass,false, true);
    if ( myold == null )
      return  null;
    else
      return (Hashtable)myold;
  }

  /**
   * �޸ĺϷ������ݡ�
   * @param myrecords ��������
   * @param itemnames ����������
   * @param notnulls �ǿ����
   * @param keys �ؼ��ֱ�
   * @param mywhere ���ұ��滻���ݵ�����
   * @param item ���滻����
   * @param mypass �������͵���
   * @param isPart �Ƿ����޸Ĳ�����ֵ
   * @param isSolo �Ƿ����޸ĵ�������
   * @return ���滻�ĺϷ�����
   */
  public Object modifyValidRecord(ArrayList myrecords,
                                  ArrayList itemnames,
                                  ArrayList notnulls,
                                  ArrayList keys,
                                  Hashtable mywhere,
                                  Hashtable item,
                                  ArrayList mypass,
                                  boolean   isPart,
                                  boolean   isSolo )   {
    try {
      if ( myrecords == null ) return null;
//      if ( mywhere == null ) return null;
      if ( (item == null) ||
           item.isEmpty() )
        return null;
//      if ( this.getRecordKey(keys,mywhere) == null )
//        return null;
      Hashtable record, myitem,myold = null;
      ArrayList myolds = new ArrayList();
      String mykey;
      for  (int i=0; i< myrecords.size(); i++) {
        record = (Hashtable)myrecords.get(i);
        if ( !CommonTools.isValidHash(record, mywhere,
                                      this.datatypes,
                                      this.sortEncoding) )
          continue;
        if ( isPart ) {
          myitem = new Hashtable(record);
          for (int j=0; j < itemnames.size(); j++) {
            mykey = itemnames.get(j).toString();
            if ( item.get(mykey) != null)
              myitem.put(mykey,item.get(mykey));
          }
        } else {
          myitem = new Hashtable(item);
        }
        myitem = DataReaderGetor.getValidRecord(itemnames,notnulls,keys,myitem);
        if ( myitem == null )   continue;
        //�޸��˹ؼ��ֵ���������ܸ����Ѵ�������
        if ( !CommonTools.isValidHash(myitem, mywhere,
                                      this.datatypes,
                                      this.sortEncoding)  ) {
          if ( "yes".equals(this.checkExistRecord(keys,myitem) ) ) {
            continue;
          }
        }
        if ( mypass != null ) {
          String myp;
          for (int j=0; j < mypass.size(); j++) {
            myp = mypass.get(j).toString();
            if ( (myitem.get(myp) == null) ||
                 SEE_PASSWORD_NULL.equals(myitem.get(myp))  ) {
              myitem.remove(myp);
              continue;
            }
            // ����δ�ĵ����
            if ( SEE_PASSWORD_NOT_MODIFY.equals(myitem.get(myp))) {
              if ( record.get(myp) == null )
                myitem.remove(myp);
              else
                myitem.put(myp, record.get(myp));
              continue;
            }
          }
        }
        myold = record;
        myolds.add(record);
        myrecords.set(i,myitem);
        this.records = myrecords;
        if (isSolo) break;
      }
      if ( isSolo )
        return myold;
      else {
        if ( myolds.size() > 0 )
          return myolds;
        else
          return null;
      }
    }
    catch (Exception ex) {
      return null;
    }
  }

  /**
   * �����޸ĺϷ������ݡ�
   * @param itemnames ����������
   * @param notnulls �ǿ����
   * @param keys �ؼ��ֱ�
   * @param mywhere ���ұ��滻���ݵ�����
   * @param item ���滻����
   * @param mypass �������͵���
   * @return ���滻�ĺϷ�����
   */
  public ArrayList batchcModifyData(ArrayList itemnames,
                                    ArrayList notnulls,
                                    ArrayList keys,
                                    Hashtable mywhere,
                                    Hashtable item,
                                    ArrayList mypass) {
    ArrayList myrecords =
        getValidRecords(itemnames,notnulls,keys);
    return batchcModifyData(myrecords,itemnames,notnulls,keys,
                            mywhere,item, mypass);
  }

  /**
   * �����޸ĺϷ������ݡ�
   * @param itemnames ����������
   * @param notnulls �ǿ����
   * @param keys �ؼ��ֱ�
   * @param item ���滻����
   * @param mypass �������͵���
   * @return ���滻�ĺϷ�����
   */
  public ArrayList batchcModifyData(ArrayList itemnames,
                                    ArrayList notnulls,
                                    ArrayList keys,
                                    Hashtable item,
                                    ArrayList mypass) {

    ArrayList myrecords =
        getValidRecords(itemnames,notnulls,keys);
    return batchcModifyData(myrecords,itemnames,notnulls,keys,
                            item, mypass);
  }

  /**
   * �����޸ĺϷ������ݡ�
   * @param myrecords ���������������
   * @param itemnames ����������
   * @param notnulls �ǿ����
   * @param keys �ؼ��ֱ�
   * @param item ���滻����
   * @param mypass �������͵���
   * @return ���滻�ĺϷ�����
   */
  public ArrayList batchcModifyData(ArrayList myrecords,
                                    ArrayList itemnames,
                                    ArrayList notnulls,
                                    ArrayList keys,
                                    Hashtable item,
                                    ArrayList mypass) {
    Hashtable mywhere = DataReaderGetor.getRecordKey(keys,item);
    return batchcModifyData(myrecords,itemnames,notnulls,keys,
                            mywhere,item, mypass);
  }

  /**
   * �����޸ĺϷ������ݡ�
   * @param myrecords ���������������
   * @param itemnames ����������
   * @param notnulls �ǿ����
   * @param keys �ؼ��ֱ�
   * @param mywhere ���ұ��滻���ݵ�����
   * @param item ���滻����
   * @param mypass �������͵���
   * @return ���滻�ĺϷ�����
   */
  public ArrayList batchcModifyData(ArrayList myrecords,
                                    ArrayList itemnames,
                                    ArrayList notnulls,
                                    ArrayList keys,
                                    Hashtable mywhere,
                                    Hashtable item,
                                    ArrayList mypass) {
    Object myold = modifyValidRecord(myrecords,itemnames,notnulls,keys,
                                     mywhere,item, mypass,true, false);
    if ( myold == null )
      return  null;
    else
      return (ArrayList)myold;
  }

  /**
   * �޸����е����ݡ�
   * @param itemnames ����������
   * @param notnulls �ǿ����
   * @param keys �ؼ��ֱ�
   * @param item ���滻����
   * @param mypass �������͵���
   */
  public void modifyAllRecord(ArrayList itemnames,
                              ArrayList notnulls,
                              ArrayList keys,
                              Hashtable item,
                              ArrayList mypass) {

    ArrayList myrecords =
        getValidRecords(itemnames,notnulls,keys);
    modifyAllRecord(myrecords,itemnames,notnulls,keys,
                    item, mypass);
  }

  /**
   * �޸����е����ݡ�
   * @param myrecords ��������
   * @param itemnames ����������
   * @param notnulls �ǿ����
   * @param keys �ؼ��ֱ�
   * @param item ���滻����
   * @param mypass �������͵���
   */
  public void modifyAllRecord(ArrayList myrecords,
                              ArrayList itemnames,
                              ArrayList notnulls,
                              ArrayList keys,
                              Hashtable item,
                              ArrayList mypass) {

    batchcModifyData(myrecords,itemnames,notnulls,keys,
                     null ,item, mypass);
  }

  /**
   * ɾ���������������ݡ�
   * @param itemnames ����������
   * @param notnulls �ǿ����
   * @param keys �ؼ��ֱ�
   * @param mywhere ��ɾ��������
   * @param myfile �ļ����͵���
   * @return ɾ�������������
   */
  public ArrayList removeValidRecord(ArrayList itemnames,
                                     ArrayList notnulls,
                                     ArrayList keys,
                                     Hashtable mywhere,
                                     ArrayList myfile) {
    return removeValidRecord(itemnames,notnulls,keys,
                             mywhere,myfile,true);
  }

  /**
   * ɾ���������������ݡ�
   * @param itemnames ����������
   * @param notnulls �ǿ����
   * @param keys �ؼ��ֱ�
   * @param mywhere ��ɾ��������
   * @param myfile �ļ����͵���
   * @param isSolo �Ƿ���ɾ����������
   * @return ɾ�������������
   */
  public ArrayList removeValidRecord(ArrayList itemnames,
                                     ArrayList notnulls,
                                     ArrayList keys,
                                     Hashtable mywhere,
                                     ArrayList myfile,
                                     boolean  isSolo) {
    ArrayList myrecords =
        getValidRecords(itemnames,notnulls,keys);
    return removeValidRecord(myrecords,mywhere,myfile,isSolo);
  }


  /**
   * ɾ���������������ݡ�
   * @param myrecords ��������
   * @param mywhere ��ɾ��������
   * @param myfile �ļ����͵���
   * @param isSolo �Ƿ���ɾ����������
   * @return ɾ�������������
   */
  public ArrayList removeValidRecord(ArrayList myrecords,
                                     Hashtable mywhere,
                                     ArrayList myfile,
                                     boolean  isSolo) {
    try {
      if ( myrecords == null ) return null;
      Hashtable record;
      int mysize = myrecords.size();
      for  (int i= mysize ; i > 0 ; i--) { //����ɾ���������Ų����
        record = (Hashtable)myrecords.get(i-1);
        if ( ( mywhere != null ) &&
             !CommonTools.isValidHash(record, mywhere,
             this.datatypes,
             this.sortEncoding) )
          continue;
        myrecords.remove(i-1);
        if ( ( myfile != null ) &&
             ( myfile.size() > 0 ) )
          DataReaderGetor.removeRecordFile(record, myfile);
        if ( isSolo ) break;
      }
      this.records = myrecords;
      if ( myrecords == null)
        return new ArrayList();
      else
        return myrecords;
    }
    catch (Exception ex) {
      return null;
    }
  }

  /**
   * ���һ�������Ƿ��Ѵ��ڡ����ݹؼ��������ҡ�
   * @param keys �ؼ��ֱ�
   * @param item ����
   * @return �������
   */
  public String checkExistRecord(ArrayList keys,
                                 Hashtable item) {
    try {
      if ( item == null ) return "invalid_data";
      if ( records == null ) return "no";
      Hashtable mykeys = DataReaderGetor.getRecordKey(keys,item);
      Hashtable rkeys;
      for (int i=0; i < records.size(); i++ ) {
        rkeys =  DataReaderGetor.getRecordKey(keys,(Hashtable)records.get(i));
        if ( (rkeys != null ) &&  rkeys.equals(mykeys) )
          return "yes";
      }
      return "no";
    }
    catch (Exception ex) {
      return "invalid_data";
    }
  }

  /**
   * ����������
   * @param myname �����ֶ���
   * @param isAscending �Ƿ�����
   * @return ����������
   */
  public ArrayList sortRecords(String myname,
                               boolean  isAscending) {
    return DataReaderGetor.sortRecords(records,myname,isAscending);
  }

  /**
   * ��ÿ�ʼ���
   * @return ��ʼ���
   */
  public int getStartIndex() {
    return startIndex;
  }

  /**
   * ���ÿ�ʼ��š���1��ʼ��
   * @param startIndex ��ʼ���
   */
  public void setStartIndex(int startIndex) {
    if ( startIndex < 1 )
      this.startIndex = 1;
    else
      this.startIndex = startIndex;
  }

  /**
   * ��ý������
   * @return �������
   */
  public int getEndIndex() {
    return endIndex;
  }

  /**
   * ���ý������
   * @param endIndex �������
   */
  public void setEndIndex(int endIndex) {
    if ( endIndex < 0 )
      this.endIndex = -1;
    else
      this.endIndex = endIndex;
  }

  /**
   * ������ݸ�������Щ���ݷ��������������ܷ�Χ��
   * @return �������������ݸ���
   */
  public int getCount() {
    return count;
  }

  /**
   * ����������
   * @param isAscending �Ƿ�����
   */
  public void setIsAscending(boolean isAscending) {
    this.isAscending = isAscending;
  }

  /**
   * ���������
   * @return �Ƿ�����
   */
  public boolean getIsAscending() {
    return isAscending;
  }

  /**
   * ���������ֶ���
   * @param sortName �����ֶ���
   */
  public void setSortName(String sortName) {
    this.sortName = sortName;
  }

  /**
   * ���������ֶ�����
   * @param sortType �����ֶ�����
   */
  public void setSortType(String sortType) {
    this.sortType = sortType;
  }

  /**
   * ��������ֶ���
   * @return �����ֶ���
   */
  public String getSortName() {
    return sortName;
  }

  /**
   * ��������
   * @param records ����
   */
  public void setRecords(ArrayList records) {
    this.records = records;
  }

  /**
   * �����ֶ�����
   * @param items �ֶ�����
   */
  public void setItems(ArrayList items) {
    this.itemnames = items;
  }

  /**
   * �������ݶ�ȡ����
   * @param where ���ݶ�ȡ����
   */
  public void setSetWhere(ArrayList where) {
    this.setwhere = where;
  }

  /**
   * �������ݶ�ȡ��������
   * @param where ���ݶ�ȡ��������
   */
  public void setBaseWhere(ArrayList where) {
    this.basewhere = where;
  }

  /**
   * ��������ı���
   * @param myenc ����ı���
   */
  public void setSortEncoding(String myenc) {
    this.sortEncoding = myenc;
  }

  /**
   * �����������Ͷ���
   * @param datatypes �������Ͷ���
   */
  public void setDatatypes(Hashtable datatypes) {
    this.datatypes = datatypes;
  }

  /**
   * ����ֶ�����
   * @return �ֶ�����
   */
  public ArrayList getItems() {
    return this.itemnames;
  }

  /**
   * ������ݶ�ȡ����
   * @return ���ݶ�ȡ����
   */
  public ArrayList getSetWhere() {
    return setwhere;
  }

  /**
   * ������ݶ�ȡ��������
   * @return ���ݶ�ȡ��������
   */
  public ArrayList getBaseWhere() {
    return basewhere;
  }

  // --------------- ˽�÷��� ---------------
  /**
   * ��ʼ������
   */
  protected void initReader() {

    this.dataSourceType  =  "xml";
    this.dataSourceName = "";

    this.startIndex = 1;
    this.endIndex = -1;
    this.count = 0;

    this.records = new ArrayList();
    this.isAscending = true;
    this.sortName= null;
    this.sortType = null;
    this.sortEncoding = null;
    this.datatypes = null;
    this.itemnames = null;
    this.setwhere = null;
    this.basewhere = null;

  }

  /**
   * ������ȡ���ݵķ���������̳�ʵ�֡�
   * @return �Ƿ��ȡ�ɹ�
   */
  protected String readData()	{
    return Constants.SUCCESSFUL;
  }


}