package chong2.see.xml;

import chong2.see.data.DataReader;
import chong2.see.data.DataReaderGetor;
import chong2.see.data.base.Constants;
import chong2.see.utils.CommonTools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


/**
 * <p>Title: ������̹�����</p>
 * <p>Description: Ϊ����з��ṩ�������ݺ������ƽ̨</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: ���</p>
 *
 * <p>���ݼ���xml����
 *
 * @author ����
 * @version 0.07
 */


public class DataXmlReader extends DataReader {


  //------------------ ����������  ------------------
  private boolean validating = false;
  private String filePath = "" ;
  private String parseInfo = "" ;
  private String parseStage = "" ;
  private int currentLevel = -1;

  private String[] attrName =
  {"","","","","","","","","",""};
  private String currentValue = "";
  private Hashtable currentRecord = new Hashtable();

  // --------------------- ���÷��� ----------------------

  /**
   * ��Ĺ�������
   */
  public DataXmlReader()  {
    initParser();
  }

  /**
   * ���ñ�������XML�ļ���������·����
   * @param fileName ��������XML�ļ���
   * @return �Ƿ�ɹ�
   */
  public String setDataSourceName(String fileName) {

    this.dataSourceName = fileName;

    File f = new File(fileName);

    if ( (f == null) || !f.exists() )
      return "file_not_found";

    return Constants.SUCCESSFUL;

  }


  // --------------------- xml���ݽ������� ----------------------

  /**
   * ���ñ������ļ�����ʵ·�������������ڿ�ʼ����֮ǰ�Զ����ô˷�����
   * @param myLocator �������ļ�����ʵ·��
   */
  public void	setDocumentLocator(Locator myLocator)  {
    filePath = myLocator.getSystemId();
  }


  /**
   * ���Ǳ�׼�ӿڷ�����
   * @throws SAXException XML�����쳣
   */
  public void startDocument()	throws SAXException	{
//    parseStage = parseStage + "\n" + "start document";
  }

  /**
   * ���Ǳ�׼�ӿڷ�����
   * @throws SAXException XML�����쳣
   */
  public void endDocument() throws	SAXException {


    // ����ȫ�����������ļ�¼����������ѡ����Χ�ڵļ�¼
    if ( ( sortName != null ) &&
         !"".equals(sortName) ) {
      if ( ( this.sortName != null ) &&
           ( this.sortType != null ) &&
           ( this.sortEncoding != null ) )
        records = CommonTools.sortData(records,
        this.sortName,
        isAscending,
        this.sortType,
        this.sortEncoding);
      else
        records = DataReaderGetor.sortRecords(records,sortName,isAscending);
      ArrayList myre = new ArrayList();
      int end = count;
      if ( (endIndex > 0 ) && (endIndex < count) )
        end = endIndex ;
      for (int i = startIndex - 1 ; i < end ; i++) {
        myre.add(records.get(i));
      }
      records = myre;
    }
//    parseStage = parseStage + "\n" + "end document";
  }


  /**
   * ���Ǳ�׼�ӿڷ�����
   * @param namespaceURI ���ռ�
   * @param sName ����
   * @param qName �ʸ���
   * @param attrs ����
   * @throws SAXException XML�����쳣
   */
  public void startElement(String namespaceURI,
                           String sName,
                           String qName,
                           Attributes attrs)
      throws SAXException	{

    String eName = sName; // Ԫ����
    if ("".equals(eName)) eName = qName; // not namespaceAware

    attrName[currentLevel] = eName;
//    parseStage = parseStage + "\n" + "start element : " + eName;
//    parseStage = parseStage + "\n" + "current Level : " + currentLevel;

    if ( ( currentLevel == 1 ) && "Record".equals(eName) )
      this.currentRecord = new Hashtable();

    currentLevel++;
    currentValue = "";

  }


  /**
   * ���Ǳ�׼�ӿڷ�����
   * @param namespaceURI ���ռ�
   * @param sName ����
   * @param qName �ʸ���
   * @throws SAXException XML�����쳣
   */
  public void endElement(String namespaceURI,
                         String sName,
                         String qName )
      throws SAXException {

//    parseStage = parseStage + "\n" + "end	element	: " + sName;

    currentLevel--;
    String eName = sName; // Ԫ����
    if ("".equals(eName)) eName = qName; // not namespaceAware

    if ( currentLevel == 2 ) {
      currentValue = currentValue.trim();
      if ( !"".equals(currentValue)  )
        currentRecord.put(attrName[2],currentValue );
    }

    if ( ( currentLevel != 1 ) || !"Record".equals(eName) )
      return;

    if ( basewhere != null ) { //������ѯ�������ǡ��򡱹�ϵ
      boolean isvaild = false;
      for (int i=0; i< basewhere.size(); i++) {
        if ( CommonTools.isValidHash(this.currentRecord,
                                     (Hashtable)basewhere.get(i),
                                     this.datatypes,
                                     this.sortEncoding) ) {
        isvaild= true;
        break;
      }
      }
      if ( !isvaild )  return;
    }
    if ( setwhere != null ) { //���õĲ�ѯ�������ǡ��򡱹�ϵ
      boolean isvaild = false;
      for (int i=0; i< setwhere.size(); i++) {
        if ( CommonTools.isValidHash(this.currentRecord,
                                     (Hashtable)setwhere.get(i),
                                     this.datatypes,
                                     this.sortEncoding)  ) {
        isvaild= true;
        break;
      }
      }
      if ( !isvaild )  return;
    }
    count++; //���������ĸ���

    if ( (( sortName != null ) && !"".equals(sortName)) ||
         (( count >= startIndex ) &&
         ( ( endIndex < 0) || (count <= endIndex) )) ) {

      Hashtable myd = new Hashtable();
      if (  this.itemnames != null ) {
        Object myn;
        for (int i=0; i < this.itemnames.size(); i++) {
          myn = this.itemnames.get(i);
          if ( currentRecord.get(myn) != null)
            myd.put(myn, currentRecord.get(myn));
        }
      } else
        myd = currentRecord;

      this.records.add(myd);

    }

  }

  /**
   * ���Ǳ�׼�ӿڷ�����
   * ע�⣺���������һ����ȡ�������ַ����������Ƿִζ�ȡ�ġ������Ҫƴ�ӡ�
   * @param buf �ַ���
   * @param offset ƫַ
   * @param len ����
   * @throws SAXException XML�����쳣
   */
  public void	characters(char	buf[], int offset, int len)
      throws SAXException {

    String currentAttrValue = new String(buf, offset, len);

    if ( ( currentAttrValue == null ) ||
         "".equals(currentAttrValue) )
      return;

    if ( currentLevel != 3 ) return;

    currentValue = currentValue + currentAttrValue;

    //���أ���侹Ȼ����ע�͵���������Ƶ��ʹ�õ�һ���������ִ����ۻ����Ķ���ʱ�䣡
//    parseStage = parseStage +
//                 "\n" + "char : $" + currentAttrValue + "$";

  }

  /**
   * ���Ǳ�׼�ӿڷ�����
   * @param target d
   * @param data d
   * @throws SAXException d
   */
  public void	processingInstruction(String target, String	data)
      throws SAXException {
  }

  /**
   * ���Ǳ�׼�ӿڷ�����
   * @param e d
   * @throws SAXParseException d
   */
  // treat validation	errors as fatal
  public void	error(SAXParseException	e)
      throws SAXParseException {
    throw e;
  }

  /**
   * ���Ǳ�׼�ӿڷ�����
   * @param err d
   * @throws SAXParseException d
   */
  // dump	warnings too
  public void	warning(SAXParseException err)
      throws SAXParseException	{

//    parseInfo = parseInfo + "** Warning"
//              + ", line " + err.getLineNumber()
//              + ", uri "	+ err.getSystemId()
//              + "   " + err.getMessage()	+ "\n";
  }

  /**
   * ������ȡ���ݵķ������̳и���ʵ�֡�
   * @return �Ƿ��ȡ�ɹ�
   */
  protected String readData()	{

    if ( (this.dataSourceName == null) ||
         "".equals(this.dataSourceName) )
      return "file_not_found";

    try	{

//      parseStage =	parseStage +	"\n" + "build parser";

      // Use the default (non-validating)	parser
      SAXParserFactory factory = SAXParserFactory.newInstance();
      factory.setValidating(validating);

      // Parse the input
      SAXParser saxParser = factory.newSAXParser();
      saxParser.parse( new File(this.dataSourceName), this );

      return Constants.SUCCESSFUL;

    } catch	(SAXParseException spe)	{

      // Error generated by the parser
//      parseInfo = parseInfo	+ "* Parsing error"
//                + ", line "	+ spe.getLineNumber()
//                + ", uri " + spe.getSystemId();
//      parseInfo = parseInfo	+ "	  "	+ spe.getMessage() + "\n";

    } catch	(SAXException sxe) {

      // Error generated by this application
      // (or a parser-initialization error)
//      parseInfo = parseInfo	+ "* error , " + sxe.getMessage() +	"\n";

    } catch	(ParserConfigurationException pce) {

      // Parser with specified options can't be built
//      parseInfo = parseInfo	+ "* Parser	configuration error	, "
//                + pce.getMessage() +	"\n";

    } catch	(IOException ioe) {

      //	I/O	error
//      parseInfo = parseInfo	+ "* IO	error ,	" +	ioe.getMessage() + "\n";

    } catch	(Throwable t) {

//      parseInfo = parseInfo	+ "* Throwable , " + t.getMessage()	+ "\n";
    }

    return "can_not_perform";

  }

  /**
   * �Ƿ���֤XML�ĺϷ���
   * @return   �Ƿ���֤XML�ĺϷ���
   */
  public boolean isValidating() {
    return validating;
  }

  /**
   * �����Ƿ���֤xml�Ϸ���
   * @param validating �Ƿ���֤xml�Ϸ���
   */
  public void setValidating(boolean validating) {
    this.validating = validating;
  }


  /**
   * ��ñ�����XML�ļ�����ʵ·��
   * @return  ������XML�ļ�����ʵ·��
   */
  public String getFilePath() {
    return filePath;
  }

  /**
   * ���ñ�����XML�ļ�����ʵ·��
   * @param filePath ������XML�ļ�����ʵ·��
   */
  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  /**
   * ��ý����Ĺ�����Ϣ�ʹ��󱨸�
   * @return �����Ĺ�����Ϣ�ʹ��󱨸�
   */
  public String getParseInfo() {
    return parseInfo;
  }

  /**
   * ��ý������ڵĽ׶�
   * @return �������ڵĽ׶�
   */
  public String getParseStage() {
    return parseStage;
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

  // --------------- ˽�÷��� ---------------
  /**
   * ��ʼ������
   */
  private void initParser() {

    this.parseInfo = "";
    this.parseStage = "init";

    this.currentRecord = new Hashtable();
    this.currentLevel = 0;

  }


}