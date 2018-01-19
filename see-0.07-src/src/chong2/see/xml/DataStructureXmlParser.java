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
 * <p>Title: 软件工程管理环境</p>
 * <p>Description: 为软件研发提供管理数据和任务的平台</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: 虫二</p>
 *
 * <p>数据结构的xml解析
 *
 * @author 玛瑞
 * @version 0.07
 */


public class DataStructureXmlParser extends DefaultHandler {

  //------------------ 系统常量  ------------------

  /**
   * 多个值的分隔符
   */
  public static String LIST_SPLITTER  = ":-:";

  /**
   * hash名-值的分隔符
   */
  public static String HASH_SPLITTER  = ".h.p-h.p.";

  /**
   * 信息结构文件目录的相对地址
   */
  public static String DATASET_STRUCTURE_PATH_PREFIX  =
      "/WEB-INF/data/structure/";

  /**
   * 信息结构文件的前缀
   */
  public static String DATASET_STRUCTURE_FILE_PREFIX  =
      "see-structure-";

  //------------------ 解析器属性  ------------------
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


  // --------------------- 公用方法 ----------------------

  /**
   * 设置被解析文件的真实路径。解析器将在开始任务之前自动调用此方法。
   * @param myLocator 被解析文件的真实路径
   */
  public void	setDocumentLocator(Locator myLocator)  {
    filePath = myLocator.getSystemId();
  }

  /**
   * 这是标准接口方法。
   * @throws SAXException XML解析异常
   */
  public void startDocument()	throws SAXException	{
//    parseStage = parseStage + "\n" + "start document";
  }

  /**
   * 这是标准接口方法。
   * @throws SAXException XML解析异常
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
   * 这是标准接口方法。
   * @param namespaceURI 名空间
   * @param sName 简单名
   * @param qName 资格名
   * @param attrs 属性
   * @throws SAXException XML解析异常
   */
  public void startElement(String namespaceURI,
                           String sName,
                           String qName,
                           Attributes attrs)
      throws SAXException	{

    String eName = sName; // 元素名
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
   * 这是标准接口方法。
   * @param namespaceURI 名空间
   * @param sName 简单名
   * @param qName 资格名
   * @throws SAXException XML解析异常
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
   * 这是标准接口方法。
   * @param buf 字符串
   * @param offset 偏址
   * @param len 长度
   * @throws SAXException XML解析异常
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
   * 这是标准接口方法。
   * @param target d
   * @param data d
   * @throws SAXException d
   */
  public void	processingInstruction(String target, String	data)
      throws SAXException {
  }

  /**
   * 这是标准接口方法。
   * @param e d
   * @throws SAXParseException d
   */
  // treat validation	errors as fatal
  public void	error(SAXParseException	e)
      throws SAXParseException {
    throw e;
  }

  /**
   * 这是标准接口方法。
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
   * 读取并解析XML文件
   * @param fileName 要解析的xml文件名
   * @return 是否成功
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


  //------------------- 访问属性的方法 -------------------
  /**
   * 是否验证XML的合法性
   * @return   是否验证XML的合法性
   */
  public boolean isValidating() {
    return validating;
  }

  /**
   * 设置是否验证xml合法性
   * @param validating 是否验证xml合法性
   */
  public void setValidating(boolean validating) {
    this.validating = validating;
  }

  /**
   * 获得被解析的XML文件名（绝对路径）
   * @return 被解析的XML文件名
   */
  public String getFileName() {
    return fileName;
  }

  /**
   * 设置被解析的XML文件名（绝对路径）
   * @param fileName 被解析的XML文件名
   * @return 是否成功
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
   * 获得被解析XML文件的真实路径
   * @return  被解析XML文件的真实路径
   */
  public String getFilePath() {
    return filePath;
  }

  /**
   * 设置被解析XML文件的真实路径
   * @param filePath 被解析XML文件的真实路径
   */
  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  /**
   * 获得解析的过程信息和错误报告
   * @return 解析的过程信息和错误报告
   */
  public String getParseInfo() {
    return parseInfo;
  }

  /**
   * 获得解析处于的阶段
   * @return 解析处于的阶段
   */
  public String getParseStage() {
    return parseStage;
  }

  /**
   * 设置数据结构
   * @param dataset 数据结构
   */
  public void setDataSet(Hashtable dataset) {
    this.dataSet = dataset;
  }

  /**
   * 获得数据结构
   * @return 数据结构
   */
  public Hashtable getDataSet() {
    return dataSet;
  }

  /**
   * 获得表类型
   * @return 表类型
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
   * 是否是矩阵表
   * @return 是否是矩阵表
   */
  public boolean isMatrix() {
    return this.ismatrix;
  }

  /**
   * 是否含有矩阵定义
   * @return 是否含有矩阵定义
   */
  public boolean hasMatrixDefine() {
    if ( !this.ismatrix ) return false;
    if ( this.getVaildMatrixItemNames() == null) return false;
    if ( this.getMatrixValues() == null) return false;
    return true;
  }

  /**
   * 获得hash类型的数据项
   * @return hash类型的数据项
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
   * 获得合法的数据项名表
   * @return 合法的数据项名表
   */
  public ArrayList getVaildItemNames() {
      return this.validItems;
  }

  /**
   * 获得所有合法矩阵项
   * @return 所有合法矩阵项
   */
  public ArrayList getVaildMatrixItemNames() {
    if ( this.validMatrixItems == null )
      return null;
    if ( this.validMatrixItems.size() < 1 )
      return null;
    return this.validMatrixItems;
  }

  /**
   * 获得所有矩阵项（包含关键字、“矩阵值”、矩阵项）
   * @return 所有矩阵项
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
   * 获得数据的关键字段表
   * @return 数据的关键字段表
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
   * 是否定义了数据项
   * @return 是否定义了数据项
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
   * 获得数据的操作集
   * @return 数据的操作集
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
   * 获得数据的编辑项表
   * @return 数据的编辑项表
   */
  public ArrayList getEditItemNames() {

    return this.getVaildItemNames();

  }

  /**
   * 获得数据的显示项表
   * @return 数据的显示项表
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
   * 获得数据的非空项表
   * @return 数据的非空项表
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
   * 获得数据的非空矩阵项表
   * @return 数据的非空矩阵项表
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
   * 获得所有矩阵值
   * @return 所有矩阵值
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
   * 获得所有数据项的类型
   * @return 所有数据项的类型
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
   * 判断是否有文件类型
   * @return 是否有文件类型
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
   * 获得文件类型的项列表
   * @return 文件类型的项列表
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
   * 获得口令类型的数据项表
   * @return 口令类型的数据项表
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
   * 获得日期时间类型的数据项表
   * @return 日期时间类型的数据项表
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
   * 获得数值类型的数据项表
   * @return 数值类型的数据项表
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
   * 获得数据的非空项表
   * @return 数据的非空项表
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
   * 获得数据项的一个属性
   * @param itemname 项名
   * @param attr 属性名
   * @return 属性值
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
   * 获得一个数据项的所有属性
   * @param itemname 项名
   * @return 数据项的所有属性
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
   * 获得数据的描述信息
   * @return 数据的描述信息
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

  // --------------- 私用方法 ---------------
  /**
   * 初始化参数
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