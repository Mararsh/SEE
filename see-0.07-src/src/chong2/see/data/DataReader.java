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
 * <p>Title: 软件工程管理环境</p>
 * <p>Description: 为软件研发提供管理数据和任务的平台</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: 虫二</p>
 *
 * 数据读取基础类。被xml读取类和数据库读取类所继承。
 *
 * @author 玛瑞
 * @version 0.07
 */

public abstract class DataReader extends DefaultHandler{

  //------------------ 常量  ------------------

  /**
   * 多个值的分隔符
   */
  public static String LIST_SPLITTER  = ":-:";

  /**
   * 数据值的分隔符
   */
  public static String VALUE_HASH_SPLITTER  = ":v:h-v:h:";

  /**
   * hash名-值的分隔符
   */
  public static String HASH_SPLITTER  = ":h:p-h:p:";

  /**
   * 未知用途的分隔符
   */
  public static String SOME_SPLITTER  = ":s:m-s:m:";

  /**
   * 用户数据中">"符号将被保存为这个字符串。显示数据时显示为">"。
   */
  public static String CHAR_GREATER =  ":g:t-g:t:";

  /**
   * 用户数据中"<"符号将被保存为这个字符串。显示数据时显示为"<"。
   */
  public static String CHAR_LESS =  ":l:s-l:s:";

  /**
   * 用户数据中"&"符号将被保存为这个字符串。显示数据时显示为"&"。
   */
  public static String CHAR_AND =  ":a:d-a:d:";

  /**
   * 用户数据中'符号将被保存为这个字符串。显示数据时显示为'。
   */
  public static String CHAR_INVERTED_COMMA =  ":i:c-i:c:";

  /**
   * 用户数据中"符号将被保存为这个字符串。显示数据时显示为"。
   */
  public static String CHAR_QUOTATION_MARK =  ":q:m-q:m:";

  /**
   * 用户数据中换行将被保存为这个字符串。显示数据时换行。
   */
  public static String CHAR_RETURN =  ":r:t-r:t:";

  /**
   * 用户数据中需要转换存放的特殊字符，它们可能使xml或网页处理混乱。
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
   * 信息数据文件目录相对路径和文件前缀
   */
  public static String DATASET_VALUES_FILE_PREFIX  =
      "/WEB-INF/data/value/see-value-";

  /**
   * 口令字段的空值
   */
  public static String SEE_PASSWORD_NULL =  "see_password_null" ;

  /**
   * 口令字段不修改
   */
  public static String SEE_PASSWORD_NOT_MODIFY =
      "see_password_not_modify" ;

  //------------------ 参数  ------------------

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

  // --------------------- 公用方法 ----------------------

  /**
   * 类的构建方法
   */
  public DataReader()  {
    initReader();
  }

  /**
   * 设置数据源类型。缺省为xml
   * @param mytype 数据源类型
   */
  public void setDataSourceType(String mytype) {
    if (mytype==null) mytype = "xml";
    this.dataSourceType = mytype;
  }

  /**
   * 读取数据源类型
   * @return 数据源类型
   */
  public String getDataSourceType() {
    return this.dataSourceType;
  }

  /**
   * 读取数据。只获得符合条件的、排序后、范围内的数据。
   * @param startIndex 开始序号
   * @param endIndex 结束序号
   * @param myitems 字段名
   * @param basewhere 基本查询条件
   * @param setwhere 用户设置的条件
   * @param mysort 排序字段名
   * @param mysorttype 排序字段的类型
   * @param isAscending 是否升序
   * @param mytypes 数据类型
   * @param myenc 数据编码
   * @return 是否成功
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
   * 读取数据
   * @return 是否成功
   */
  public String startReading()	{
    return this.readData();
  }


  //------------------- 访问属性的方法 -------------------


  /**
   * 读取数据源名称。
   * 对于xml是数据文件的绝对路径，对于数据库则是数据表名
   * @return 数据源名称
   */
  public String getDataSourceName() {
    return dataSourceName;
  }

  /**
   * 设置数据源名称。
   * 对于xml是数据文件的绝对路径，对于数据库则是数据表名
   * @param fileName 数据源名称
   * @return 是否成功
   */
  public String setDataSourceName(String fileName) {
    this.dataSourceName = fileName;
    return Constants.SUCCESSFUL;
  }

  /**
   * 获得所有数据
   * @return 所有数据
   */
  public ArrayList getRecords() {
    return records;
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

  /**
   * 读取所有数据的关键字值
   * @param keys 关键字名
   * @return 所有数据的关键字值
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
   * 插入一个合法的数据
   * @param itemnames 数据项名表
   * @param notnulls 非空项表
   * @param keys 关键字表
   * @param mywhere 插入位置的数据条件
   * @param item 待插入数据
   * @return 被插入的合法数据
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

  /**
   * 批量修改合法的数据。
   * @param itemnames 数据项名表
   * @param notnulls 非空项表
   * @param keys 关键字表
   * @param mywhere 查找被替换数据的条件
   * @param item 待替换数据
   * @param mypass 口令类型的项
   * @return 被替换的合法数据
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
   * 批量修改合法的数据。
   * @param itemnames 数据项名表
   * @param notnulls 非空项表
   * @param keys 关键字表
   * @param item 待替换数据
   * @param mypass 口令类型的项
   * @return 被替换的合法数据
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
   * 批量修改合法的数据。
   * @param myrecords 被处理的所有数据
   * @param itemnames 数据项名表
   * @param notnulls 非空项表
   * @param keys 关键字表
   * @param item 待替换数据
   * @param mypass 口令类型的项
   * @return 被替换的合法数据
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
   * 批量修改合法的数据。
   * @param myrecords 被处理的所有数据
   * @param itemnames 数据项名表
   * @param notnulls 非空项表
   * @param keys 关键字表
   * @param mywhere 查找被替换数据的条件
   * @param item 待替换数据
   * @param mypass 口令类型的项
   * @return 被替换的合法数据
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

  /**
   * 删除符合条件的数据。
   * @param itemnames 数据项名表
   * @param notnulls 非空项表
   * @param keys 关键字表
   * @param mywhere 被删除的条件
   * @param myfile 文件类型的项
   * @param isSolo 是否是删除单个数据
   * @return 删除后的所有数据
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

  /**
   * 对数据排序
   * @param myname 排序字段名
   * @param isAscending 是否升序
   * @return 排序后的数据
   */
  public ArrayList sortRecords(String myname,
                               boolean  isAscending) {
    return DataReaderGetor.sortRecords(records,myname,isAscending);
  }

  /**
   * 获得开始序号
   * @return 开始序号
   */
  public int getStartIndex() {
    return startIndex;
  }

  /**
   * 设置开始序号。从1开始。
   * @param startIndex 开始序号
   */
  public void setStartIndex(int startIndex) {
    if ( startIndex < 1 )
      this.startIndex = 1;
    else
      this.startIndex = startIndex;
  }

  /**
   * 获得结束序号
   * @return 结束序号
   */
  public int getEndIndex() {
    return endIndex;
  }

  /**
   * 设置结束序号
   * @param endIndex 结束序号
   */
  public void setEndIndex(int endIndex) {
    if ( endIndex < 0 )
      this.endIndex = -1;
    else
      this.endIndex = endIndex;
  }

  /**
   * 获得数据个数。这些数据符合条件，而不管范围。
   * @return 符合条件的数据个数
   */
  public int getCount() {
    return count;
  }

  /**
   * 设置排序方向
   * @param isAscending 是否升序
   */
  public void setIsAscending(boolean isAscending) {
    this.isAscending = isAscending;
  }

  /**
   * 获得排序方向
   * @return 是否升序
   */
  public boolean getIsAscending() {
    return isAscending;
  }

  /**
   * 设置排序字段名
   * @param sortName 排序字段名
   */
  public void setSortName(String sortName) {
    this.sortName = sortName;
  }

  /**
   * 设置排序字段类型
   * @param sortType 排序字段类型
   */
  public void setSortType(String sortType) {
    this.sortType = sortType;
  }

  /**
   * 获得排序字段名
   * @return 排序字段名
   */
  public String getSortName() {
    return sortName;
  }

  /**
   * 设置数据
   * @param records 数据
   */
  public void setRecords(ArrayList records) {
    this.records = records;
  }

  /**
   * 设置字段名表
   * @param items 字段名表
   */
  public void setItems(ArrayList items) {
    this.itemnames = items;
  }

  /**
   * 设置数据读取条件
   * @param where 数据读取条件
   */
  public void setSetWhere(ArrayList where) {
    this.setwhere = where;
  }

  /**
   * 设置数据读取基础条件
   * @param where 数据读取基础条件
   */
  public void setBaseWhere(ArrayList where) {
    this.basewhere = where;
  }

  /**
   * 设置排序的编码
   * @param myenc 排序的编码
   */
  public void setSortEncoding(String myenc) {
    this.sortEncoding = myenc;
  }

  /**
   * 设置数据类型定义
   * @param datatypes 数据类型定义
   */
  public void setDatatypes(Hashtable datatypes) {
    this.datatypes = datatypes;
  }

  /**
   * 获得字段名表
   * @return 字段名表
   */
  public ArrayList getItems() {
    return this.itemnames;
  }

  /**
   * 获得数据读取条件
   * @return 数据读取条件
   */
  public ArrayList getSetWhere() {
    return setwhere;
  }

  /**
   * 获得数据读取基础条件
   * @return 数据读取基础条件
   */
  public ArrayList getBaseWhere() {
    return basewhere;
  }

  // --------------- 私用方法 ---------------
  /**
   * 初始化参数
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
   * 真正读取数据的方法。子类继承实现。
   * @return 是否读取成功
   */
  protected String readData()	{
    return Constants.SUCCESSFUL;
  }


}