package chong2.see.xml;

import chong2.see.data.base.Constants;
import chong2.see.data.base.Language;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * <p>Title: 软件工程管理环境</p>
 * <p>Description: 为软件研发提供管理数据和任务的平台</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: 虫二</p>
 *
 * <p>数据集的xml写回
 *
 * @author 玛瑞
 * @version 0.07
 */

public class DataXmlWriter {

  //------------------ 属性  ------------------
  private ArrayList data;
  private ArrayList names;
  private String fileName = "" ;
  private String fileEncoding;

  // --------------------- 公用方法 ----------------------

  /**
   * 向文件中写入数据
   * @param names 数据项名表
   * @param data 数据
   * @param fileName 文件名（绝对路径）
   * @param fileEncoding 文件编码
   * @return 是否成功
   */
  public String writeData(ArrayList names,
                          ArrayList data,
                          String fileName,
                          String fileEncoding) {
    this.names = names;
    this.data = data;
    this.fileName = fileName;
    this.fileEncoding = fileEncoding;
    return startWriting();
  }

  /**
   * 向文件中写入数据
   * @param data 数据
   * @param fileName 文件名（绝对路径）
   * @param fileEncoding 文件编码
   * @return 是否成功
   */
  public String writeData(ArrayList data,
                          String fileName,
                          String fileEncoding) {
    this.names = null;
    this.data = data;
    this.fileName = fileName;
    this.fileEncoding = fileEncoding;
    return startWriting();
  }

  /**
   * 向文件中写入数据。采用缺省编码。
   * @param data 数据
   * @param fileName 文件名（绝对路径）
   * @return 是否成功
   */
  public String writeData(ArrayList data,
                          String fileName) {
    this.names = null;
    this.data = data;
    this.fileName = fileName;
    this.fileEncoding =
        Language.getCharset(Constants.DEFAULT_LANGUAGE);
    return startWriting();
  }

  //------------------- 访问属性的方法 -------------------
  /**
   * 获得数据
   * @return 待写回的数据
   */
  public ArrayList getDataSet() {
    return data;
  }

  /**
   * 设置数据
   * @param data 数据
   */
  public void setData(ArrayList data) {
    this.data = data;
  }

  /**
   * 获得文件名
   * @return 待写回的xml文件名
   */
  public String getFileName() {
    return fileName;
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

    if ( data == null )
      return "invalid_data";

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

      out.println("<DataValues>");

      Hashtable myd;
      String myname;
      for ( int i=0; i< data.size(); i++) {
        out.println("\t<Record>");
        myd = (Hashtable)data.get(i);
        if ( names == null )
          for (Enumeration e = myd.keys();e.hasMoreElements();) {
        myname = e.nextElement().toString();
        out.println("\t\t<" + myname + ">" + myd.get(myname) +
                    "</" + myname + ">");
          }
          else
            for (int j = 0; j < names.size(); j++) {
          myname = names.get(j).toString();
          if ( myd.get(myname) == null ) continue;
          out.println("\t\t<" + myname + ">" + myd.get(myname) +
                      "</" + myname + ">");
            }
            out.println("\t</Record>");
      }

      out.println("</DataValues>");

      out.close();

      return Constants.SUCCESSFUL;

    } catch (IOException e) {

      return "io_exception";

    }

  }

}