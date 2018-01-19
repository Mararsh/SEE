package chong2.see.xml;

import chong2.see.data.base.Constants;
import chong2.see.data.base.DataStructure;
import chong2.see.data.base.Language;
import chong2.see.utils.CommonTools;

import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * <p>Title: ������̹�����</p>
 * <p>Description: Ϊ����з��ṩ�������ݺ������ƽ̨</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: ���</p>
 *
 * <p>���ݽṹ��xmlд��
 *
 * @author ����
 * @version 0.07
 */

public class DataStructureXmlWriter {

  //------------------ ����  ------------------
  private Hashtable dataSet;
  private String fileName = "" ;
  private String fileEncoding;

  // --------------------- ���÷��� ----------------------

  /**
   * ���ļ���д�����ݽṹ
   * @param dataSet ���ݽṹ
   * @param fileName �ļ���������·����
   * @param fileEncoding �ļ�����
   * @return �Ƿ�ɹ�
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
   * ���ļ���д�����ݽṹ������ȱʡ���롣
   * @param dataSet ���ݽṹ
   * @param fileName �ļ���������·����
   * @return �Ƿ�ɹ�
   */
  public String writeData(Hashtable dataSet,
                          String fileName) {
    this.dataSet = dataSet;
    this.fileName = fileName;
    this.fileEncoding =
        Language.getCharset(Constants.DEFAULT_LANGUAGE);
    return startWriting();
  }

  //------------------- �������Եķ��� -------------------
  /**
   * ������ݽṹ
   * @return ��д�ص����ݽṹ
   */
  public Hashtable getDataSet() {
    return dataSet;
  }

  /**
   * �������ݽṹ
   * @param dataSet ���ݽṹ
   */
  public void setDataSet(Hashtable dataSet) {
    this.dataSet = dataSet;
  }

  /**
   * ����ļ���
   * @return ��д�ص�xml�ļ���
   */
  public String getFileName() {
    return fileName;
  }

  /**
   * �����ļ���
   * @param fileName �ļ���
   */
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  /**
   * ����ļ�����
   * @return д�ļ�ʱ�ı��뷽ʽ
   */
  public String getFileEncoding() {
    return fileEncoding;
  }

  /**
   * �����ļ�����
   * @param fileEncoding �ļ�����
   */
  public void setFileEncoding(String fileEncoding) {
    this.fileEncoding = fileEncoding;
  }

  // --------------- ˽�÷��� ---------------

  /**
   * д�ļ�
   * @return �Ƿ�ɹ�
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

      //ע������Ҫ�趨�ַ�����
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