package chong2.see.xml;

import chong2.see.data.base.Constants;
import chong2.see.data.base.DataStructure;
import chong2.see.utils.CommonTools;
import chong2.see.utils.MatrixTools;

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
import org.xml.sax.helpers.DefaultHandler;
import chong2.see.data.*;


/**
 * <p>Title: ������̹�����</p>
 * <p>Description: Ϊ����з��ṩ�������ݺ������ƽ̨</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: ���</p>
 *
 * <p>���ݽṹ��xml����
 *
 * @author ����
 * @version 0.07
 */


public class DataStructureXmlParser extends DefaultHandler {

  //------------------ ϵͳ����  ------------------

  /**
   * ���ֵ�ķָ���
   */
  public static String LIST_SPLITTER  = ":-:";

  /**
   * hash��-ֵ�ķָ���
   */
  public static String HASH_SPLITTER  = ".h.p-h.p.";

  /**
   * ��Ϣ�ṹ�ļ�Ŀ¼����Ե�ַ
   */
  public static String DATASET_STRUCTURE_PATH_PREFIX  =
      "/WEB-INF/data/structure/";

  /**
   * ��Ϣ�ṹ�ļ���ǰ׺
   */
  public static String DATASET_STRUCTURE_FILE_PREFIX  =
      "see-structure-";

  //------------------ ����������  ------------------
  private boolean validating = false;
  private String fileName = "" ;
  private String filePath = "" ;
  private String parseInfo = "" ;
  private String parseStage = "" ;
  private int currentLevel = -1;
  private ArrayList validItems = new ArrayList();
  private ArrayList validMatrixItems = new ArrayList();

  private String[] attrName =
  {"","","","","","","","","",""};
  private Hashtable dataSet,dataItem;
  private ArrayList items = new ArrayList();
  private String currentValue = "";

  private boolean ismatrix = false;


  // --------------------- ���÷��� ----------------------

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
    try {

      if ( dataSet == null ) return;
      this.ismatrix =  "matrix".equals(this.getType());

      if ( dataSet.get("items") == null) return;
      ArrayList items = (ArrayList)dataSet.get("items");

      ArrayList itemnames =  new ArrayList();
      ArrayList initem =  null;
      if ( this.dataSet.get("invalidItems") != null )
        initem = (ArrayList)this.dataSet.get("invalidItems");

      ArrayList matrixnames =  new ArrayList();
      ArrayList inmitem =  null;
      if ( this.dataSet.get("invalidItems_matrix") != null )
        inmitem = (ArrayList)this.dataSet.get("invalidItems_matrix");

      Hashtable item;
      Object myname;
      for (int i=0; i < items.size(); i++ ) {
        item = (Hashtable)items.get(i);
        if ( ( myname = item.get("identify") )== null ) continue;
        if ( !ismatrix ||
             !"yes".equals(item.get("ismatrix") ) ) {
          if  ( ( initem == null) ||
                !initem.contains(myname) )
            itemnames.add(myname);
          continue;
        } else  {
          if  ( ( inmitem == null) ||
                !inmitem.contains(myname) )
            matrixnames.add(myname);
          continue;
        }
      }
      validItems = itemnames;
      validMatrixItems = matrixnames;

    }
    catch (Exception ex) {
      return ;
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
    currentValue = "";

    if ( currentLevel == 1 ) {

      if ( "items".equals(eName) ) {
        this.items = new ArrayList();
        this.dataSet.put(eName, this.items);
      }

    } else if ( currentLevel == 2  ) {

      if ( "items".equals(attrName[1]) &&
           "DataItem".equals(attrName[2])  ) {

        this.dataItem = new Hashtable();
        this.items.add(this.dataItem);

      }

    }

    currentLevel++;

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

    if ( currentLevel != 1 ) return;

  }

  /**
   * ���Ǳ�׼�ӿڷ�����
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

    currentAttrValue = currentAttrValue.trim();

    if ( "".equals(currentAttrValue) )  return;
    currentValue = currentValue + currentAttrValue;

    if ( currentLevel == 2 ) {

      if ( DataStructure.setStringAttrs.contains( attrName[1] ) ) {
        this.dataSet.put( attrName[1] , currentValue);
      }

      if ( DataStructure.setArrayAttrs.contains( attrName[1] ) ) {
        this.dataSet.put( attrName[1] ,
                          CommonTools.stringToArray(currentValue,
                          this.LIST_SPLITTER));
      }

      } else if ( (currentLevel == 4) &&
                  ( "items".equals(attrName[1]) ) &&
                  ( "DataItem".equals(attrName[2]) ) ) {

        if ( DataStructure.itemStringAttrs.contains(attrName[3])  ) {
          this.dataItem.put(attrName[3],currentValue);
        }

        if ( DataStructure.itemHashAttrs.contains(attrName[3])  ) {
          this.dataItem.put(attrName[3],
                            CommonTools.stringToHash(currentValue,
                            this.HASH_SPLITTER));
        }

      }

//     parseStage = parseStage + "\n" + "char : $" + currentAttrValue + "$";
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
   * ��ȡ������XML�ļ�
   * @param fileName Ҫ������xml�ļ���
   * @return �Ƿ�ɹ�
   */
  public String startParsing(String fileName)	{

    initReader();

    if ( !( (Constants.SUCCESSFUL).equals( setFileName(fileName) ) )  )
      return "file_not_found";

    try	{

//      parseStage =	parseStage +	"\n" + "build parser";

      this.dataSet = new Hashtable();

      // Use the default (non-validating)	parser
      SAXParserFactory factory = SAXParserFactory.newInstance();
      factory.setValidating(validating);

      // Parse the input
      SAXParser saxParser = factory.newSAXParser();
      saxParser.parse( new File(fileName), this );

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


  //------------------- �������Եķ��� -------------------
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
   * ��ñ�������XML�ļ���������·����
   * @return ��������XML�ļ���
   */
  public String getFileName() {
    return fileName;
  }

  /**
   * ���ñ�������XML�ļ���������·����
   * @param fileName ��������XML�ļ���
   * @return �Ƿ�ɹ�
   */
  public String setFileName(String fileName) {

    File f = new File(fileName);

    if ( (f == null) || !f.exists() ) {
      fileName = "" ;
      return "file_not_found";
    }

    this.fileName = fileName;
    return Constants.SUCCESSFUL;

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
   * �������ݽṹ
   * @param dataset ���ݽṹ
   */
  public void setDataSet(Hashtable dataset) {
    this.dataSet = dataset;
  }

  /**
   * ������ݽṹ
   * @return ���ݽṹ
   */
  public Hashtable getDataSet() {
    return dataSet;
  }

  /**
   * ��ñ�����
   * @return ������
   */
  public String getType() {
    if ( dataSet == null) return "table";
    if ( dataSet.get("type") == null) return "table";
    String myt = dataSet.get("type").toString();
    if ( !DataStructure.DATASET_TYPES.contains(myt))
      return "table";
    return myt;
  }

  /**
   * �Ƿ��Ǿ����
   * @return �Ƿ��Ǿ����
   */
  public boolean isMatrix() {
    return this.ismatrix;
  }

  /**
   * �Ƿ��о�����
   * @return �Ƿ��о�����
   */
  public boolean hasMatrixDefine() {
    if ( !this.ismatrix ) return false;
    if ( this.getVaildMatrixItemNames() == null) return false;
    if ( this.getMatrixValues() == null) return false;
    return true;
  }

  /**
   * ���hash���͵�������
   * @return hash���͵�������
   */
  public Hashtable getHashItems() {

    try {
      if ( dataSet.get("items") == null) return null;
      Hashtable hitems =  new Hashtable();
      ArrayList items = (ArrayList)dataSet.get("items");
      Hashtable item;
      for (int i=0; i < items.size(); i++ ) {
        item = (Hashtable)items.get(i);
        if ( item.get("identify") == null ) continue;
        hitems.put((String)item.get("identify"),item);
      }
      if ( this.hasMatrixDefine() ) {
        item = new Hashtable();
        item.put("identify",MatrixTools.MATRIX_VALUE);
        item.put("type","string");
        item.put("valueConstraintType","solo");
        item.put("valueConstraint",
                 CommonTools.arraylistToString(this.getMatrixValues(),
                 this.LIST_SPLITTER));
        item.put("editControllerType","radio");
        item.put("valueTranslated","yes");
        hitems.put(MatrixTools.MATRIX_VALUE, item);
      }
      return hitems;
    }
    catch (Exception ex) {
      return null;
    }
  }

  /**
   * ��úϷ�������������
   * @return �Ϸ�������������
   */
  public ArrayList getVaildItemNames() {
      return this.validItems;
  }

  /**
   * ������кϷ�������
   * @return ���кϷ�������
   */
  public ArrayList getVaildMatrixItemNames() {
    if ( this.validMatrixItems == null )
      return null;
    if ( this.validMatrixItems.size() < 1 )
      return null;
    return this.validMatrixItems;
  }

  /**
   * ������о���������ؼ��֡�������ֵ���������
   * @return ���о�����
   */
  public ArrayList getAllMatrixItemNames() {
    ArrayList mynames = this.getKeys();
    if (mynames == null ) return null;
    mynames.add( MatrixTools.MATRIX_VALUE );
    ArrayList mym = this.getVaildMatrixItemNames();
    if ( mym == null ) return mynames;
    for (int i=0; i< mym.size(); i++) {
      mynames.add(mym.get(i));
    }
    return mynames;
  }

  /**
   * ������ݵĹؼ��ֶα�
   * @return ���ݵĹؼ��ֶα�
   */
  public ArrayList getKeys() {
    ArrayList myitems = this.getVaildItemNames();
    if ( myitems == null ) return null;
    if ( this.dataSet.get("keys") == null )
      return myitems;
    ArrayList mykeys =
        (ArrayList)this.dataSet.get("keys");
    ArrayList myk = new ArrayList();
    for (int i=0; i< mykeys.size(); i++) {
      if ( myitems.contains(mykeys.get(i)) )
        myk.add(mykeys.get(i));
    }
    if ( myk.size() < 1 )
      return myitems;
    return myk;
  }

  /**
   * �Ƿ�����������
   * @return �Ƿ�����������
   */
  public boolean hasItems() {
    if ( this.dataSet.get("items") == null )
      return false;
    ArrayList myitems = (ArrayList)this.dataSet.get("items");
    if ( myitems.size() < 1 )
      return false;
    return true;
  }

  /**
   * ������ݵĲ�����
   * @return ���ݵĲ�����
   */
  public ArrayList getOperations() {
    if ( this.dataSet.get("operation") == null )
      return null;
    if ( !hasItems() )
      return null;
    ArrayList myops = (ArrayList)this.dataSet.get("operation");
    return myops;
  }

  /**
   * ������ݵı༭���
   * @return ���ݵı༭���
   */
  public ArrayList getEditItemNames() {

    return this.getVaildItemNames();

  }

  /**
   * ������ݵ���ʾ���
   * @return ���ݵ���ʾ���
   */
  public ArrayList getShowItemNames() {

    try {
      ArrayList myitems = this.getVaildItemNames();
      if ( myitems == null ) return null;
      ArrayList mykeys = myitems;
      if ( this.dataSet.get("showItems") != null )
        mykeys = (ArrayList)this.dataSet.get("showItems");
      ArrayList myk = new ArrayList();
      ArrayList mypass = this.getPasswordItems();
      for (int i=0; i< mykeys.size(); i++) {
        if ( !myitems.contains(mykeys.get(i)) ) continue;
        if ( mypass.contains (mykeys.get(i) ))  continue;
        myk.add(mykeys.get(i));
      }
      return myk;
    }
    catch (Exception ex) {
      return null;
    }
  }

  /**
   * ������ݵķǿ����
   * @return ���ݵķǿ����
   */
  public ArrayList getNotNullNames() {

    ArrayList myitems = this.getEditItemNames();
    if ( myitems == null ) return null;
    if ( this.dataSet.get("notNullItems") == null )
      return null;
    ArrayList mynots =
        (ArrayList)this.dataSet.get("notNullItems");
    ArrayList  myi = new ArrayList();
    for (int i=0; i< mynots.size(); i++) {
      if ( myitems.contains(mynots.get(i)) )
        myi.add(mynots.get(i));
    }
    return myi;
  }

  /**
   * ������ݵķǿվ������
   * @return ���ݵķǿվ������
   */
  public ArrayList getNotNullMatrixNames() {

    ArrayList myitems = this.getVaildMatrixItemNames();
    if ( myitems == null ) return null;
    if ( this.dataSet.get("notNullItems_matrix") == null )
      return null;
    ArrayList mynots =
        (ArrayList)this.dataSet.get("notNullItems_matrix");
    ArrayList  myi = new ArrayList();
    for (int i=0; i< mynots.size(); i++) {
      if ( myitems.contains(mynots.get(i)) )
        myi.add(mynots.get(i));
    }
    return myi;
  }

  /**
   * ������о���ֵ
   * @return ���о���ֵ
   */
  public ArrayList getMatrixValues() {

    try {
      if ( this.dataSet.get("matrixValues") == null )
        return null;
      ArrayList mymatrix = new ArrayList();
      mymatrix = CommonTools.stringToArray(dataSet.get("matrixValues").toString(),
          DataReader.CHAR_RETURN);
      if (  mymatrix == null   )
        return null;
      if (  mymatrix.size() < 1  )
        return null;
      return mymatrix;
    }
    catch (Exception ex) {
      return null;
    }
  }

  /**
   * ������������������
   * @return ���������������
   */
  public Hashtable getItemTypes() {

    try {
      Hashtable mytypes = new Hashtable();
      if ( dataSet.get("items") == null) return null;
      ArrayList items = (ArrayList)dataSet.get("items");
      Hashtable item;
      String mytype, myid;
      for (int i=0; i < items.size(); i++ ) {
        item = (Hashtable)items.get(i);
        if ( item.get("identify") == null ) continue;
        myid = item.get("identify").toString();
        if ( item.get("type") == null ) continue;
        mytype = item.get("type").toString();
        if ( !DataStructure.DATAITEM_DATA_TYPES.contains(mytype) )
          continue;
        mytypes.put(myid,mytype);
      }
      if ( this.hasMatrixDefine() )
        mytypes.put(MatrixTools.MATRIX_VALUE,"string");
      return mytypes;
    }
    catch (Exception ex) {
      return null;
    }
  }

  /**
   * �ж��Ƿ����ļ�����
   * @return �Ƿ����ļ�����
   */
  public boolean hasFileType() {

    try {
      if ( dataSet.get("items") == null)
        return false;
      ArrayList items = (ArrayList)dataSet.get("items");
      Hashtable item;
      for (int i=0; i < items.size(); i++ ) {
        item = (Hashtable)items.get(i);
        if ( item.get("identify") == null ) continue;
        if ( item.get("valueConstraintType") == null ) continue;
        if ( "file".equals(item.get("valueConstraintType").toString()) )
          return true;
      }
      return false;
    }
    catch (Exception ex) {
      return false;
    }
  }

  /**
   * ����ļ����͵����б�
   * @return �ļ����͵����б�
   */
  public ArrayList getFileItems() {

    ArrayList myfile = new ArrayList();
    try {
      if ( dataSet.get("items") == null) return myfile;
      ArrayList items = (ArrayList)dataSet.get("items");
      Hashtable item;
      for (int i=0; i < items.size(); i++ ) {
        item = (Hashtable)items.get(i);
        if ( item.get("identify") == null ) continue;
        if ( item.get("valueConstraintType") == null ) continue;
        if ( "file".equals(item.get("valueConstraintType")) )
          myfile.add(item.get("identify"));
      }
      return myfile;
    }
    catch (Exception ex) {
      return myfile;
    }
  }


  /**
   * ��ÿ������͵��������
   * @return �������͵��������
   */
  public ArrayList getPasswordItems() {

    ArrayList mypass = new ArrayList();
    try {
      if ( dataSet.get("items") == null) return mypass;
      ArrayList items = (ArrayList)dataSet.get("items");
      Hashtable item;
      for (int i=0; i < items.size(); i++ ) {
        item = (Hashtable)items.get(i);
        if ( item.get("identify") == null ) continue;
        if ( item.get("valueConstraintType") == null ) continue;
        if ( "password".equals(item.get("valueConstraintType")) )
          mypass.add(item.get("identify"));
      }
      return mypass;
    }
    catch (Exception ex) {
      return mypass;
    }
  }

  /**
   * �������ʱ�����͵��������
   * @return ����ʱ�����͵��������
   */
  public ArrayList getDateTimeItems() {

    ArrayList mydt = new ArrayList();
    try {
      if ( dataSet.get("items") == null) return mydt;
      ArrayList items = (ArrayList)dataSet.get("items");
      Hashtable item;
      for (int i=0; i < items.size(); i++ ) {
        item = (Hashtable)items.get(i);
        if ( item.get("identify") == null ) continue;
        if ( item.get("type") == null ) continue;
        if ( "date".equals(item.get("type")) ||
             "time".equals(item.get("type")) )
          mydt.add(item.get("identify"));
      }
      return mydt;
    }
    catch (Exception ex) {
      return mydt;
    }
  }

  /**
   * �����ֵ���͵��������
   * @return ��ֵ���͵��������
   */
  public ArrayList getNumberItems() {

    ArrayList mydt = new ArrayList();
    try {
      if ( dataSet.get("items") == null) return mydt;
      ArrayList items = (ArrayList)dataSet.get("items");
      Hashtable item;
      for (int i=0; i < items.size(); i++ ) {
        item = (Hashtable)items.get(i);
        if ( item.get("identify") == null ) continue;
        if ( item.get("type") == null ) continue;
        if ( "int".equals(item.get("type")) ||
             "float".equals(item.get("type")) )
          mydt.add(item.get("identify"));
      }
      return mydt;
    }
    catch (Exception ex) {
      return mydt;
    }
  }

  /**
   * ������ݵķǿ����
   * @return ���ݵķǿ����
   */
  public Hashtable getConstraintTypes() {

    try {
      Hashtable mytypes = new Hashtable();
      if ( dataSet.get("items") == null) return null;
      ArrayList items = (ArrayList)dataSet.get("items");
      Hashtable item;
      String mytype, myid;
      for (int i=0; i < items.size(); i++ ) {
        item = (Hashtable)items.get(i);
        if ( item.get("identify") == null ) continue;
        myid = item.get("identify").toString();
        if ( item.get("valueConstraintType") == null ) continue;
        mytype = item.get("valueConstraintType").toString();
        mytypes.put(myid,mytype);
      }
      return mytypes;
    }
    catch (Exception ex) {
      return null;
    }

  }


  /**
   * ����������һ������
   * @param itemname ����
   * @param attr ������
   * @return ����ֵ
   */
  public String getItemAttr(String itemname, String attr) {
    try {
      if ( dataSet.get("items") == null) return null;
      if ( (itemname == null) || "".equals(itemname) )
        return null;
      if ( (attr == null) || "".equals(attr) )
        return null;
      ArrayList items = (ArrayList)dataSet.get("items");
      Hashtable item;
      for (int i=0; i < items.size(); i++ ) {
        item = (Hashtable)items.get(i);
        if ( item.get("identify") == null ) continue;
        if ( item.get(attr) == null ) continue;
        if ( itemname.equals(item.get("identify").toString()) )
          return item.get(attr).toString();
      }
      return null;
    }
    catch (Exception ex) {
      return null;
    }
  }

  /**
   * ���һ�����������������
   * @param itemname ����
   * @return ���������������
   */
  public Hashtable getItem(String itemname) {
    try {
      if ( dataSet.get("items") == null) return null;
      if ( (itemname == null) || "".equals(itemname) )
        return null;
      ArrayList items = (ArrayList)dataSet.get("items");
      Hashtable item;
      for (int i=0; i < items.size(); i++ ) {
        item = (Hashtable)items.get(i);
        if ( item.get("identify") == null ) continue;
        if ( itemname.equals(item.get("identify").toString()) )
          return item;
      }
      return null;
    }
    catch (Exception ex) {
      return null;
    }
  }

  /**
   * ������ݵ�������Ϣ
   * @return ���ݵ�������Ϣ
   */
  public String getDescription() {
    try {
      if ( dataSet.get("description") == null) return "";
      return dataSet.get("description").toString();
    }
    catch (Exception ex) {
      return "";
    }
  }

  // --------------- ˽�÷��� ---------------
  /**
   * ��ʼ������
   */
  private void initReader() {

    fileName = "" ;
    filePath = "" ;
    parseInfo = "" ;
    parseStage = "" ;
    parseStage = "init";

    dataSet = new Hashtable();
    dataItem = new Hashtable();
    validItems = new ArrayList();
    validMatrixItems = new ArrayList();
    items = new ArrayList();

    currentLevel = 0;
    currentValue = "";
    ismatrix = false;
  }


}