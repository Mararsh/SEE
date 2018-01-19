package chong2.see.xml;

import chong2.see.data.base.Constants;
import chong2.see.data.base.DataStructure;
import chong2.see.data.base.Language;
import chong2.see.utils.CommonTools;

import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * <p>Title: 软件工程管理环境</p>
 * <p>Description: 为软件研发提供管理数据和任务的平台</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: 虫二</p>
 *
 * <p>数据结构的xml写回
 *
 * @author 玛瑞
 * @version 0.07
 */

public class DataStructureXmlWriter {

  //------------------ 属性  ------------------
  private Hashtable dataSet;
  private String fileName = "" ;
  private String fileEncoding;

  // --------------------- 公用方法 ----------------------

  /**
   * 向文件中写入数据结构
   * @param dataSet 数据结构
   * @param fileName 文件名（绝对路径）
   * @param fileEncoding 文件编码
   * @return 是否成功
   */
  public String writeData(Hashtable dataSet,
                          String fileName,
                          String fileEncoding) {
    this.dataSet = dataSet;
    this.fileName = fileName;
    this.fileEncoding = fileEncoding;
    return startWriting();
  }

  /**
   * 向文件中写入数据结构。采用缺省编码。
   * @param dataSet 数据结构
   * @param fileName 文件名（绝对路径）
   * @return 是否成功
   */
  public String writeData(Hashtable dataSet,
                          String fileName) {
    this.dataSet = dataSet;
    this.fileName = fileName;
    this.fileEncoding =
        Language.getCharset(Constants.DEFAULT_LANGUAGE);
    return startWriting();
  }

  //------------------- 访问属性的方法 -------------------
  /**
   * 获得数据结构
   * @return 待写回的数据结构
   */
  public Hashtable getDataSet() {
    return dataSet;
  }

  /**
   * 设置数据结构
   * @param dataSet 数据结构
   */
  public void setDataSet(Hashtable dataSet) {
    this.dataSet = dataSet;
  }

  /**
   * 获得文件名
   * @return 待写回的xml文件名
   */
  public String getFileName() {
    return fileName;
  }

  /**
   * 设置文件名
   * @param fileName 文件名
   */
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  /**
   * 获得文件编码
   * @return 写文件时的编码方式
   */
  public String getFileEncoding() {
    return fileEncoding;
  }

  /**
   * 设置文件编码
   * @param fileEncoding 文件编码
   */
  public void setFileEncoding(String fileEncoding) {
    this.fileEncoding = fileEncoding;
  }

  // --------------- 私用方法 ---------------

  /**
   * 写文件
   * @return 是否成功
   */
  private String startWriting() {

    if ( ( fileName == null ) || fileName.equals("") ) {
      return "file_not_found";
    }

    if ( ( fileEncoding == null ) || fileEncoding.equals("") )
      fileEncoding = Language.getCharset(Constants.DEFAULT_LANGUAGE);

    try {

      File f = new File(fileName);
      f.createNewFile();

      //注意这里要设定字符集！
      PrintWriter out
      = new PrintWriter(new BufferedWriter(
      new OutputStreamWriter(new FileOutputStream(f),fileEncoding)));

      out.println("<?xml version=\"1.0\" encoding=\"" +
                  fileEncoding + "\"?>");

      out.println("<DataSet>");

      String attr, value;
      ArrayList myv;
      String sstyle,ustyle;
      Hashtable st,su,it;

      for ( int i = 0; i <DataStructure.setStringAttrs.size(); i++) {
        attr = DataStructure.setStringAttrs.get(i).toString();
        if (  dataSet.get(attr) == null ) continue;
        value = dataSet.get(attr).toString();
        if ( "".equals(value) )  continue;
        out.println("  <" + attr + ">" +
                    value + "</" + attr + ">");

      }
      for ( int i = 0; i <DataStructure.setArrayAttrs.size(); i++) {
        attr = DataStructure.setArrayAttrs.get(i).toString();
        if (  dataSet.get(attr) == null ) continue;
        myv = (ArrayList)dataSet.get(attr);
        if ( myv.size() < 1 )  continue;
        out.println("  <" + attr + ">" +
                    CommonTools.arraylistToString(myv,
                    DataStructureXmlParser.LIST_SPLITTER) +
                    "</" + attr + ">");
      }

      out.println("  <items>");
      if ( dataSet.get("items") != null) {
        myv = (ArrayList)dataSet.get("items");
        for ( int i = 0; i < myv.size(); i++) {
          it = (Hashtable)myv.get(i);
          if ( it.isEmpty() ) continue;
          out.println("    <DataItem>");
          for ( int j = 0; j <DataStructure.itemStringAttrs.size(); j++) {
            attr = DataStructure.itemStringAttrs.get(j).toString();
            if (  it.get(attr) == null ) continue;
            value = it.get(attr).toString();
            if ( "".equals(value) )  continue;
            out.println("      <" + attr + ">" +
                        value + "</" + attr + ">");
          }
          for ( int j = 0; j <DataStructure.itemHashAttrs.size(); j++) {
            attr = DataStructure.itemHashAttrs.get(j).toString();
            if (  it.get(attr) == null ) continue;
            st = (Hashtable)it.get(attr);
            if ( st.isEmpty() ) continue;
            out.println("      <" + attr + ">" +
                        CommonTools.hashToString(st,DataStructureXmlParser.HASH_SPLITTER, null) +
                        "</" + attr + ">");
          }

          out.println("    </DataItem>");
        }
      }
      out.println("  </items>");

      out.println("</DataSet>");

      out.close();

      return Constants.SUCCESSFUL;

    } catch (IOException e) {

      return "io_exception";

    }

  }

}