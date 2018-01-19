package chong2.see.utils;

import chong2.see.servlet.common.DataManager;
import chong2.see.data.DataReader;
import chong2.see.xml.DataXmlWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>Title: 软件工程管理环境</p>
 * <p>Description: 为软件研发提供管理数据和任务的平台</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: 虫二</p>
 *
 * 与文件上传相关的静态方法
 *
 * @author 玛瑞
 * @version 0.07
 */

final public class UploadTools {

  /**
   * 记录上传参数的session变量
   */
  public static String UPLOAD_NAME =  "upPara";
  public static int REQUEST_READSIZE = 1024;

  /**
   * 是否是上传页面
   * @param inRequest 当前请求
   * @return 是否是上传页面
   */
  public static boolean  isUpload(HttpServletRequest inRequest) {
    String mycontenttype = inRequest.getContentType();
    if ( mycontenttype == null ) return false;
    if ( mycontenttype.startsWith("multipart/form-data") )
      return  true;
    return false;
  }

  /**
   * 读取一个上传域的值。读取后，后一个分隔符也已读入。
   * @param inRequest 当前请求
   * @param myin 当前读取流
   * @param mybound 域的分隔符
   * @return 域值
   */
  public static byte[] readUploadValue(HttpServletRequest inRequest,
                                       ServletInputStream myin,
                                       String mybound) {
    try {
      byte[] mydata = new byte[REQUEST_READSIZE];
      ByteArrayOutputStream myout = new ByteArrayOutputStream();
      int len = myin.readLine(mydata,0,REQUEST_READSIZE);
      String myst, mycharset =
          ServletTools.getSessionCharset(inRequest);
      int bsize = mybound.getBytes().length ;
      while ( len > -1) {
        if ( ( len > bsize ) &&
             ( len < bsize + 5 ) ) {
          myst = new String(mydata,0,len,mycharset);
          if ( ( myst != null ) &&
               myst.startsWith(mybound))
            break;
        }
        myout.write(mydata, 0,len);
        len = myin.readLine(mydata,0,REQUEST_READSIZE);
      }
      return myout.toByteArray();
    }
    catch (IOException ex) {
      return null;
    }

  }

  /**
   * 获得目前上传空间的剩余（byte）
   * @param inRequest 当前请求
   * @return 目前上传空间的剩余
   */
  public static long  getUploadLeftSpace(HttpServletRequest inRequest) {
    ArrayList myfiles = ServletTools.getDirListFile(inRequest,
        DataManager.UPLOAD_FILE_PREFIX);
    if ( myfiles == null ) return -1;
    File myfile;
    long mysize= 0;
    for ( int i=0; i< myfiles.size(); i++) {
      myfile = (File)myfiles.get(i);
      if ( !myfile.exists() ) continue;
      mysize = mysize + myfile.length() ;
    }
    return ServletTools.getAppDefaultUploadTotal(inRequest) * 1000 - mysize;
  }

  /**
   * 读取一个上传页面各个域的值。
   * @param inRequest 当前请求
   * @param useLimit 是否判断文件上传的限额
   * @return 上传页面各个域的值
   */
  public static Hashtable dealUpload(HttpServletRequest inRequest,
                                     boolean   useLimit) {
    try {
      if ( !isUpload(inRequest) ) return null;
      String mycontenttype = inRequest.getContentType();
      String mybound = "--" +
                       mycontenttype.substring(mycontenttype.indexOf("=")+1);
      ServletInputStream myin = inRequest.getInputStream() ;
      byte[] mydata = new byte[REQUEST_READSIZE];

      String mycharset = ServletTools.getSessionCharset(inRequest);
      int  len = myin.readLine(mydata,0,REQUEST_READSIZE);
      String myst = new String(mydata,0,len,mycharset);
      if ( !myst.startsWith(mybound)) return null;

      len = myin.readLine(mydata,0,REQUEST_READSIZE);

      int pos;
      String myname, myfile ,myfileup;
      Hashtable myparas = new Hashtable();
      byte[] myvalue;
      String mystrvalue;
      long  mylimit = ServletTools.getAppDefaultUploadSize(inRequest) * 1000;
      long  myleft = getUploadLeftSpace(inRequest);
      long mysize;
      ArrayList myupfiles = new ArrayList();
      while ( len != -1 )
      {
        myst = new String(mydata,0,len,mycharset);
        if ( !myst.startsWith("Content-Disposition: form-data;")) {
          len = myin.readLine(mydata,0,REQUEST_READSIZE);
          continue;
        }
        pos = myst.indexOf(" name=") ;
        if ( pos < 0 ) continue;
        myname = myst.substring(pos + 7,
                                myst.indexOf("\"", pos + 7));
        pos = myst.indexOf(" filename=") ;
        if ( pos > -1 ) {
          myfile = myst.substring(pos + 11,
                                  myst.indexOf("\"", pos + 11));
          myin.readLine(mydata,0,REQUEST_READSIZE);
        } else
          myfile = null;
        len = myin.readLine(mydata,0,REQUEST_READSIZE);
        myvalue = readUploadValue(inRequest,myin,mybound);
        len = myin.readLine(mydata,0,REQUEST_READSIZE);
        if ( myvalue == null ) continue;
        if ( myfile == null) {
          mystrvalue = new String(myvalue,0, myvalue.length - 2,
                                  mycharset) ;
        } else {
          mysize = myvalue.length;
          if ( useLimit &&
               (( mysize > myleft ) || ( mysize > mylimit ) )) {
            // 超出限额。删除已上传文件，返回错误。
            for (int i=0; i< myupfiles.size(); i++) {
              CommonTools.removeFile(myupfiles.get(i).toString());
            }
            if ( mysize > myleft )
              inRequest.setAttribute("error","upload_total_over");
            else
              inRequest.setAttribute("error","upload_size_over");
            return null;
          }
          mystrvalue = getNewUploadFile(inRequest, myfile);
          if ( ( mystrvalue != null ) &&
               ( len > 2) ) {
            CommonTools.writeByteFile(mystrvalue,myvalue,0,
                                      myvalue.length - 2);
            myupfiles.add(mystrvalue);
          }
        }
        if ( (mystrvalue != null) &&
             !"".equals(mystrvalue) ) {
          if ( myparas.get(myname) != null)
            mystrvalue = myparas.get(myname).toString() +
            DataReader.LIST_SPLITTER +
            mystrvalue;
          myparas.put(myname, mystrvalue);
        }
      }
      return myparas;
    }
    catch (Exception ex) {
      inRequest.setAttribute("error",ex.toString());
      return null;
    }

  }

  /**
   * 获得新的上传文件名。如果重名，则在后面加上当前时间和序数，可试10次。
   * @param myRequest 当前请求
   * @param myfile 文件名
   * @return 上传文件名（绝对路径）
   */
  public static String getNewUploadFile(HttpServletRequest myRequest,
                                        String myfile) {
    try {
      if ( (myfile == null) ||
           "".equals(myfile) )
        return null;
      String myname = CommonTools.getFilePrefix(myfile);
      if ( (myname == null) ||
           "".equals(myname) )
        return null;
      String mysuff = CommonTools.getFileSuffix(myfile);
      String fname = DataManager.UPLOAD_FILE_PREFIX + myname ;
      if ( (mysuff != null) ||
           !"".equals(mysuff) )
        fname = fname + "." + mysuff;
      String ff = ServletTools.getFileRealPath(myRequest,fname);
      File f = new File(ff);
      int count = 0, max = 10;
      while (f.exists()) {
        if (count > max ) return null;
        count++;
        fname = DataManager.UPLOAD_FILE_PREFIX + myname  +
                "-" + count +
                CommonTools.getCurrentTime() ;
        if ( (mysuff != null) ||
             !"".equals(mysuff) )
          fname = fname + "." + mysuff;
        ff = ServletTools.getFileRealPath(myRequest,fname);
        f = new File(ff);
      }
      return ff;
    }
    catch (Exception ex) {
      return null;
    }
  }

  /**
   * 获得上传文件的绝对路径。
   * @param myRequest 当前请求
   * @param myfile 文件名
   * @return 上传文件名（绝对路径）
   */
  public static String getUploadFile(HttpServletRequest myRequest,
                                     String myfile) {
    try {
      if ( (myfile == null) ||
           "".equals(myfile) )
        return null;
      String fname = DataManager.UPLOAD_FILE_PREFIX +
                     CommonTools.getFileName(myfile) ;
      return ServletTools.getFileRealPath(myRequest,fname);
    }
    catch (Exception ex) {
      return null;
    }
  }

  /**
   * 删除已上传文件
   * @param myRequest 当前请求
   * @param myfile 文件名
   * @return 是否删除成功
   */
  public static boolean removeUploadFile(HttpServletRequest myRequest,
      String myfile) {
    try {
      if ( (myfile == null) ||
           "".equals(myfile) )
        return false;
      String fname = DataManager.UPLOAD_FILE_PREFIX +
                     CommonTools.getFileName(myfile) ;
      String ff = ServletTools.getFileRealPath(myRequest,fname);
      return CommonTools.removeFile(ff);
    }
    catch (Exception ex) {
      return false;
    }
  }

  /**
   * 产生当前上传文件记录
   * @param inRequest  当前请求
   */
  public static void writeUploadRecords(HttpServletRequest inRequest) {
    ArrayList myfiles =
        ServletTools.getDirListFile(inRequest,
        DataManager.UPLOAD_FILE_PREFIX);
    if ( myfiles == null ) return;
    File myfile;
    ArrayList mydatas = new ArrayList();
    Hashtable mydata = new Hashtable();
    String myname,mypath;
    long mysize;
    for ( int i=0; i< myfiles.size(); i++) {
      myfile = (File)myfiles.get(i);
      if ( !myfile.exists() ) continue;
      mydata = new Hashtable();
      mydata.put("id",i + "");
      myname = myfile.getName();
      mydata.put("file", myname + DataReader.HASH_SPLITTER +
                 myname);
      mysize = myfile.length() / 1000;
      if ( mysize < 1 ) mysize = 1;
      mydata.put("size", mysize + "");
      mydatas.add(mydata);
    }
    String fdata =
        DataManagerTools.getDataValuesFile(inRequest);
    DataXmlWriter myw = new DataXmlWriter();
    myw.writeData(mydatas,fdata,
                  ServletTools.getAppDefaultCharset(inRequest));

  }

}