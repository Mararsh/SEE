package chong2.see.xml;

import chong2.see.data.base.Constants;
import chong2.see.data.base.Language;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * <p>Title: ������̹�����</p>
 * <p>Description: Ϊ����з��ṩ�������ݺ������ƽ̨</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: ���</p>
 *
 * <p>���ݼ���xmlд��
 *
 * @author ����
 * @version 0.07
 */

public class DataXmlWriter {

  //------------------ ����  ------------------
  private ArrayList data;
  private ArrayList names;
  private String fileName = "" ;
  private String fileEncoding;

  // --------------------- ���÷��� ----------------------

  /**
   * ���ļ���д������
   * @param names ����������
   * @param data ����
   * @param fileName �ļ���������·����
   * @param fileEncoding �ļ�����
   * @return �Ƿ�ɹ�
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
   * ���ļ���д������
   * @param data ����
   * @param fileName �ļ���������·����
   * @param fileEncoding �ļ�����
   * @return �Ƿ�ɹ�
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
   * ���ļ���д�����ݡ�����ȱʡ���롣
   * @param data ����
   * @param fileName �ļ���������·����
   * @return �Ƿ�ɹ�
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

  //------------------- �������Եķ��� -------------------
  /**
   * �������
   * @return ��д�ص�����
   */
  public ArrayList getDataSet() {
    return data;
  }

  /**
   * ��������
   * @param data ����
   */
  public void setData(ArrayList data) {
    this.data = data;
  }

  /**
   * ����ļ���
   * @return ��д�ص�xml�ļ���
   */
  public String getFileName() {
    return fileName;
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

    if ( data == null )
      return "invalid_data";

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