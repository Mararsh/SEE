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
 * <p>Title: 软件工程管理环境</p>
 * <p>Description: 为软件研发提供管理数据和任务的平台</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: 虫二</p>
 *
 * <p>数据集的xml解析
 *
 * @author 玛瑞
 * @version 0.07
 */


public class DataXmlReader extends DataReader {


  //------------------ 解析器属性  ------------------
  private boolean validating = false;
  private String filePath = "" ;
  private String parseInfo = "" ;
  private String parseStage = "" ;
  private int currentLevel = -1;

  private String[] attrName =
  {"","","","","","","","","",""};
  private String currentValue = "";
  private Hashtable currentRecord = new Hashtable();

  // --------------------- 公用方法 ----------------------

  /**
   * 类的构建方法
   */
  public DataXmlReader()  {
    initParser();
  }

  /**
   * 设置被解析的XML文件名（绝对路径）
   * @param fileName 被解析的XML文件名
   * @return 是否成功
   */
  public String setDataSourceName(String fileName) {

    this.dataSourceName = fileName;

    File f = new File(fileName);

    if ( (f == null) || !f.exists() )
      return "file_not_found";

    return Constants.SUCCESSFUL;

  }


  // --------------------- xml数据解析方法 ----------------------

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


    // 读完全部符合条件的记录后，再排序，再选出范围内的记录
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

    if ( ( currentLevel == 1 ) && "Record".equals(eName) )
      this.currentRecord = new Hashtable();

    currentLevel++;
    currentValue = "";

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
    String eName = sName; // 元素名
    if ("".equals(eName)) eName = qName; // not namespaceAware

    if ( currentLevel == 2 ) {
      currentValue = currentValue.trim();
      if ( !"".equals(currentValue)  )
        currentRecord.put(attrName[2],currentValue );
    }

    if ( ( currentLevel != 1 ) || !"Record".equals(eName) )
      return;

    if ( basewhere != null ) { //基础查询条件，是“或”关系
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
    if ( setwhere != null ) { //设置的查询条件，是“或”关系
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
    count++; //符合条件的个数

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
   * 这是标准接口方法。
   * 注意：这个函数不一定读取完所有字符，而可能是分次读取的。因此需要拼接。
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

    if ( currentLevel != 3 ) return;

    currentValue = currentValue + currentAttrValue;

    //天呢！这句竟然忘了注释掉！这是最频繁使用的一个方法！字串的累积消耗多少时间！
//    parseStage = parseStage +
//                 "\n" + "char : $" + currentAttrValue + "$";

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
   * 真正读取数据的方法。继承父类实现。
   * @return 是否读取成功
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
   * 获得符合条件的一个数据
   * @param mywhere 条件
   * @return 符合条件的一个数据
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
   * 获得符合条件的所有数据
   * @param mywhere 条件
   * @return 符合条件的所有数据
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
   * 获得合法的所有数据
   * @param itemnames 项名表
   * @param notnulls 非空项表
   * @param keys 关键字表
   * @return 合法的所有数据
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
   * 插入一个合法的数据
   * @param myrecords 已有数据
   * @param itemnames 数据项名表
   * @param notnulls 非空项表
   * @param keys 关键字表
   * @param mywhere 插入位置的数据条件
   * @param item 待插入数据
   * @return 被插入的合法数据
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
   * 添加一个合法的数据。即在数据最后添加。
   * @param itemnames 数据项名表
   * @param notnulls 非空项表
   * @param keys 关键字表
   * @param item 待添加数据
   * @return 被添加的合法数据
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
   * 添加一个合法的数据。即在数据最后添加。
   * @param myrecords 所有数据
   * @param itemnames 数据项名表
   * @param notnulls 非空项表
   * @param keys 关键字表
   * @param item 待添加数据
   * @return 被添加的合法数据
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
   * 修改合法的数据。
   * @param itemnames 数据项名表
   * @param notnulls 非空项表
   * @param keys 关键字表
   * @param mywhere 查找被替换数据的条件
   * @param item 待替换数据
   * @param mypass 口令类型的项
   * @return 被替换的合法数据
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
   * 修改合法的数据。完全覆盖符合条件的旧数据。
   * @param itemnames 数据项名表
   * @param notnulls 非空项表
   * @param keys 关键字表
   * @param item 待替换数据
   * @param mypass 口令类型的项
   * @return 被替换的合法数据
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
   * 修改合法的数据。完全覆盖符合条件的旧数据。
   * @param myrecords 所有数据
   * @param itemnames 数据项名表
   * @param notnulls 非空项表
   * @param keys 关键字表
   * @param item 待替换数据
   * @param mypass 口令类型的项
   * @return 被替换的合法数据
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
   * 修改合法的数据。
   * @param myrecords 所有数据
   * @param itemnames 数据项名表
   * @param notnulls 非空项表
   * @param keys 关键字表
   * @param mywhere 查找被替换数据的条件
   * @param item 待替换数据
   * @param mypass 口令类型的项
   * @param isPart 是否是修改部分数值
   * @param isSolo 是否是修改单个数据
   * @return 被替换的合法数据
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
        //修改了关键字的情况：不能覆盖已存在数据
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
            // 密码未改的情况
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
   * 修改所有的数据。
   * @param itemnames 数据项名表
   * @param notnulls 非空项表
   * @param keys 关键字表
   * @param item 待替换数据
   * @param mypass 口令类型的项
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
   * 修改所有的数据。
   * @param myrecords 所有数据
   * @param itemnames 数据项名表
   * @param notnulls 非空项表
   * @param keys 关键字表
   * @param item 待替换数据
   * @param mypass 口令类型的项
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
   * 删除符合条件的数据。
   * @param itemnames 数据项名表
   * @param notnulls 非空项表
   * @param keys 关键字表
   * @param mywhere 被删除的条件
   * @param myfile 文件类型的项
   * @return 删除后的所有数据
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
   * 删除符合条件的数据。
   * @param myrecords 所有数据
   * @param mywhere 被删除的条件
   * @param myfile 文件类型的项
   * @param isSolo 是否是删除单个数据
   * @return 删除后的所有数据
   */
  public ArrayList removeValidRecord(ArrayList myrecords,
                                     Hashtable mywhere,
                                     ArrayList myfile,
                                     boolean  isSolo) {
    try {
      if ( myrecords == null ) return null;
      Hashtable record;
      int mysize = myrecords.size();
      for  (int i= mysize ; i > 0 ; i--) { //倒着删除，这样才不会错
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
   * 检查一个数据是否已存在。根据关键字来查找。
   * @param keys 关键字表
   * @param item 数据
   * @return 检查结果。
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

  // --------------- 私用方法 ---------------
  /**
   * 初始化参数
   */
  private void initParser() {

    this.parseInfo = "";
    this.parseStage = "init";

    this.currentRecord = new Hashtable();
    this.currentLevel = 0;

  }


}