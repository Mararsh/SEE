package chong2.see.utils;

import chong2.see.data.base.Constants;
import chong2.see.data.base.Language;
import chong2.see.data.DataReader;

import java.io.*;
import java.text.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * <p>Title: 软件工程管理环境</p>
 * <p>Description: 为软件研发提供管理数据和任务的平台</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: 虫二</p>
 *
 * <p>常用的静态方法。
 *
 * @author 玛瑞
 * @version 0.07
 */

final public class CommonTools {

  /**
   * 整型的空值。用户数据不要使用
   */
  public static int WRONG_INT =  -99999999 ;
  /**
   * 条件判断中的“等于”。用户数据不要使用
   */
  public static String EQUAL_PREFIX = ":c:e:q-c:e:q:"  ;
  /**
   * 条件判断中的“不等于”。用户数据不要使用
   */
  public static String NOT_EQUAL_PREFIX = ":c:n:e-c:n:e:"  ;
  /**
   * 条件判断中的“大于”。用户数据不要使用
   */
  public static String GREATTER_PREFIX = ":c:g:t-c:g:t:"  ;
  /**
   * 条件判断中的“小于”。用户数据不要使用
   */
  public static String LESS_PREFIX = ":c:l:s-c:l:s:"  ;
  /**
   * 条件判断中的“开始于”。用户数据不要使用
   */
  public static String START_PREFIX = ":c:s:t-c:s:t:"  ;
  /**
   * 条件判断中的“结束于”。用户数据不要使用
   */
  public static String END_PREFIX = ":c:e:d-c:e:d:"  ;
  /**
   * 条件判断中的“包含”。用户数据不要使用
   */
  public static String INCLUDE_PREFIX = ":c:i:d-c:i:d:"  ;
  /**
   * 条件判断中的“不包含”。用户数据不要使用
   */
  public static String NOT_INCLUDE_PREFIX = ":c:n:i-c:n:i:"  ;
  /**
   * 条件判断中的“不包含”。用户数据不要使用
   */
  public static String SAME_NAME = ":s:m:n-s:m:n:"  ;
  /**
   * txt文件中数据的分隔符
   */
  public static String TXT_FILE_SEPARATOR = "\t"  ;
  /**
   * 字节与十六进制转换过程用到的分隔符
   */
  public static String  BYTE_SPLLTER = ":" ;
  /**
   * DES加密密钥
   */
  public static  SecretKey DESKey;
  /**
   * DES加密初始化
   */
  public static Cipher DESCipher;
  static {
    try {
      DESKey = KeyGenerator.getInstance("DES").generateKey();
      DESCipher =
          Cipher.getInstance("DES/ECB/PKCS5Padding");
    }
    catch (Exception ex) {
    }
  }

  //==================== 文件处理的方法 =========================

  public static boolean isFile(String infile) {
    if (infile == null ) return false;
    File f = new File(infile);
    return  f.exists();
  }

  /**
   * 删除文件
   * @param infile 要被删除的文件名（绝对路径）
   * @return 是否成功
   */
  public static boolean removeFile(String infile) {
    if (infile == null ) return false;
    File f = new File(infile);
    if (f == null ) return false;
    return  f.delete();
  }

  /**
   * 获得文件大小
   * @param infile 文件名（绝对路径）
   * @return 文件大小
   */
  public static long getFileSize(String infile) {
    if (infile == null ) return -1;
    File f = new File(infile);
    if (f == null ) return -1;
    return  f.length();
  }

  /**
   * 获得目录下文件列表
   * @param indir 目录名（绝对路径）
   * @return 目录下文件列表
   */
  public static String[] getDirList(String indir) {
    File f = new File(indir);
    return  f.list();
  }


  /**
   * 获取文件名，除去它的路径和后缀。
   *
   * @param fname 包含路径和后缀的完整文件名
   * @param suff 文件名的后缀
   * @return 除去路径和后缀的文件名
   */
  public static String getFileName(String fname, String suff) {

    String mycont = fname;
    int sind = fname.lastIndexOf( "/" ) + 1;
    if ( sind < 1 ) sind = fname.lastIndexOf( "\\" ) + 1;
    if ( sind < 1 ) sind = 0;
    int eind = fname.lastIndexOf(suff);
    if ( eind < 0 ) {
      mycont = fname.substring(sind);
    } else {
      mycont = fname.substring(sind, eind);
    }

    return mycont;

  }

  /**
   * 获取文件名，除去它的路径。
   * @param fname 包含路径的完整文件名
   * @return 除去路径的文件名
   */
  public static String getFileName(String fname) {

    int sind = fname.lastIndexOf( "/" ) + 1;
    if ( sind < 1 ) sind = fname.lastIndexOf( "\\" ) + 1;
    if ( sind < 1 ) sind = 0;

    return fname.substring(sind);

  }

  /**
   * 获取文件后缀。
   * @param fname 文件名
   * @return 除去路径的文件名
   */
  public static String getFileSuffix(String fname) {

    String myname = getFileName(fname);
    int sind = myname.lastIndexOf( "." );
    if ( sind < 0 ) return null;
    return myname.substring(sind + 1);
  }

  /**
   * 获取除去路径和后缀的文件名。
   * @param fname 文件名
   * @return 除去路径和后缀的文件名
   */
  public static String getFilePrefix(String fname) {

    String myname = getFileName(fname);
    int sind = myname.lastIndexOf( "." );
    if ( sind < 0 ) return myname;
    return myname.substring(0,sind);
  }


  /**
   * 获取文件的路径，除去它的文件名。
   * @param fname 包含路径的完整文件名
   * @return 除去文件名的路径
   */
  public static String getFilePath(String fname) {

    int sind = fname.lastIndexOf( "/" ) + 1;
    if ( sind < 1 ) sind = fname.lastIndexOf( "\\" ) + 1;
    if ( sind < 1 ) sind = 0;

    return fname.substring(0, sind);

  }

  /**
   * 将数据写入一个txt文件
   * @param fileName 文件名（绝对路径）
   * @param fileEncoding 文件的编码
   * @param names 数据名
   * @param data 数据值
   * @return 是否成功
   */
  public static String  writeTxtFile(String fileName,
                                     String fileEncoding,
                                     ArrayList names,
                                     ArrayList data) {
    return writeTxtFile(fileName,fileEncoding,names,null,data);
  }

  /**
   * 将数据写入一个txt文件
   * @param fileName 文件名（绝对路径）
   * @param fileEncoding 文件的编码
   * @param names 数据名
   * @param dis 表头行
   * @param data 数据值
   * @return 是否成功
   */
  public static String  writeTxtFile(String fileName,
                                     String fileEncoding,
                                     ArrayList names,
                                     ArrayList dis,
                                     ArrayList data) {
    if ( names == null ) return null;
    if ( names.size() < 1 ) return null;
    try {

      File f = new File(fileName);
      f.createNewFile();

      //注意这里要设定字符集！
      PrintWriter out
      = new PrintWriter(new BufferedWriter(
      new OutputStreamWriter(new FileOutputStream(f),fileEncoding)));

      String myname,myvalue;
      if ( dis != null ) {
        out.print(dis.get(0).toString() );
        for (int j = 1; j < dis.size(); j++) {
          myname = dis.get(j).toString();
          out.print(TXT_FILE_SEPARATOR + myname   );
        }
        out.println("");
      }
      Hashtable myd;
      for ( int i=0; i< data.size(); i++) {
        myd = (Hashtable)data.get(i);
        myname = names.get(0).toString();
        if ( myd.get(myname) == null )
          myvalue = "";
        else
          myvalue = myd.get(myname).toString();
        out.print(myvalue);
        for (int j = 1; j < names.size(); j++) {
          myname = names.get(j).toString();
          if ( myd.get(myname) == null )
            myvalue = "";
          else
            myvalue = myd.get(myname).toString();
          out.print(TXT_FILE_SEPARATOR + myvalue);
        }
        out.println("");
      }
      out.close();
      return Constants.SUCCESSFUL;
    } catch (IOException e) {
      return "io_exception";
    }

  }

  /**
   * 将一个字节流写入文件
   * @param fileName 文件名（绝对路径）
   * @param mydata 数据
   * @param mybegin 字节流开始写的位置
   * @param myend 字节流结束写的位置
   * @return 是否成功
   */
  public static String  writeByteFile(String fileName,
                                      byte[] mydata,
                                      int  mybegin,
                                      int  myend) {
    if ( fileName == null ) return null;
    if ( mydata == null ) return null;
    try {

      File f = new File(fileName);
      f.createNewFile();

      BufferedOutputStream out
      = new BufferedOutputStream(new FileOutputStream(f));

      out.write(mydata, mybegin , myend);
      out.close();
      return Constants.SUCCESSFUL;
    } catch (IOException e) {
      return "io_exception";
    }

  }

  //==================== see类型转换和处理的方法 =========================

  /**
   * 计算百分率
   * @param myvalue 数据值
   * @param mytotal 数据总值
   * @param mywei 小数点后位数
   * @return 计算后的百分率
   */
  public static float getPercent(String myvalue,
                                  String mytotal,
                                  int  mywei) {
    int mytt = CommonTools.stringToInt(mytotal);
    if ( mytt < 1 ) mytt = 1;

    return getPercent(myvalue, mytt,1);
  }

  /**
   * 计算百分率
   * @param myvalue 数据值
   * @param mytotal 数据总值
   * @param mywei 小数点后位数
   * @return 计算后的百分率
   */
  public static float getPercent(String myvalue,
                                  int   mytotal,
                                  int  mywei) {
    if ( mytotal < 1 ) mytotal = 1 ;
    int myrate = CommonTools.stringToInt(myvalue);
    if ( myrate < 0 ) myrate = 0;
    if ( mywei < 0 ) mywei = 0;
    float myret = myrate * 100f / mytotal;
    return getRound(myret,mywei) ;
  }

  /**
   * 截去浮点数多余位数，四舍五入。
   * @param myvalue 浮点数
   * @param mywei 小数点后保留位数
   * @return 保留小数点后位数四舍五入后的浮点值
   */
  public static float getRound(float  myvalue,
                                  int  mywei) {
    float myret = myvalue;

    if ( mywei < 0 ) mywei = 0;
    int myjin = 1;
    for (int i=0; i< mywei; i++) {
      myjin = myjin * 10;
    }
    myret = myvalue * myjin;
    myret = Math.round(myvalue * myjin) * 1f / myjin ;
    return myret;

  }

  /**
   * 过滤掉重复的数据
   * @param mylist 数据集
   * @return 过滤后的数据集
   */
  public static ArrayList getUniqueList(ArrayList  mylist) {

    if (mylist == null) return null;
    ArrayList mynew = new ArrayList();
    Object myd;
    for (int i=0; i< mylist.size(); i++) {
      myd = mylist.get(i);
      if ( mynew.contains(myd)) continue;
      mynew.add(myd);
    }
    return mynew;

  }


  /**
   * 字符串转换为列表
   * @param inStr 字符串
   * @param inSplit 分割符
   * @return 转换后的列表。若异常则返回null。
   */
  public static ArrayList stringToArray(String inStr,
                                        String inSplit) {
    try {
      if ( ( inStr == null) || ( inSplit == null ) )
        return null;
      ArrayList mys = new ArrayList();
      String[] myss = inStr.split(inSplit);
      for (int i=0; i < myss.length; i++) {
        if ( "".equals(myss[i] )  ) continue;
        mys.add(myss[i]);
      }
      return mys;
    }
    catch (Exception ex) {
      return null;
    }
  }

  /**
   * 列表转换成hash数据
   * @param inNames 名表
   * @param inValues 值表
   * @return 转换后的hash数据。若异常则返回null。
   */
  public static Hashtable arraysToHash(ArrayList inNames,
                                       ArrayList inValues) {
    try {

      Hashtable myd = new Hashtable();
      for (int i=0; i < inNames.size(); i++) {
        myd.put(inNames.get(i), inValues.get(i));
      }
      return myd;
    }
    catch (Exception ex) {
      return null;
    }
  }


  /**
   * 字符串转换为名-值两列表
   * @param inStr 字符串
   * @param inSplit 分割符
   * @return 名-值两列表
   */
  public static ArrayList[] stringToArrays(String inStr,
      String inSplit) {
    try {
      ArrayList my1 = new ArrayList();
      ArrayList my2 = new ArrayList();
      String[] myss = inStr.split(inSplit);
      for (int i=0; i < myss.length; i++) {
        i++;
        if ( i >= myss.length ) break;
        if (  "".equals(myss[i-1]) || "".equals(myss[i]) )
          continue;
        my1.add(myss[i-1]);
        my2.add(myss[i]);
      }

      ArrayList[] ret = {my1,my2};
      return ret;
    }
    catch (Exception ex) {
      return null;
    }
  }


  /**
   * 字符串转换整数
   * @param inString 字符串
   * @return 转换后的整数。异常返回一个大负数。
   */
  public static int stringToInt(String inString) {

    try {
      if ( inString == null ) return WRONG_INT;
      Integer myint = new Integer( inString );

      return myint.intValue();
    }
    catch (Exception ex) {
      return WRONG_INT;
    }

  }

  /**
   * 字符串转换整数
   * @param inString 字符串
   * @return 转换后的整数。异常返回一个大负数。
   */
  public static long stringToLong(String inString) {

    try {
      Long mylong = new Long( inString );

      return mylong.longValue();
    }
    catch (NumberFormatException ex) {
      return WRONG_INT;
    }

  }

  /**
   * 整数转换为字符串
   * @param inInt 整数
   * @return 转换后的字符串
   */
  public static String intToString(int inInt) {

//    Integer myint = new Integer( inInt );
//    return myint.toString();
    return inInt + "";

  }

  /**
   * 字符串转换浮点数
   * @param inString 字符串
   * @return 转换后的浮点数。异常返回一个大负数。
   */
  public static float stringToFloat(String inString) {
    try {
      Float myf = new Float( inString );
      return myf.floatValue();
    }
    catch (NumberFormatException ex) {
      return WRONG_INT;
    }
  }

  /**
   * 浮点数转换为字符串
   * @param myfloat 浮点数
   * @return 转换后的字符串
   */
  public static String floatToString(float myfloat) {

    return myfloat + "";

  }

  /**
   * 枚举型转换为列表
   * @param inEnum 枚举数据
   * @return 转换后的列表数据。异常返回null
   */
  public static ArrayList enumToArraylist(Enumeration inEnum) {

    try {
      ArrayList mya = new ArrayList();

      while (inEnum.hasMoreElements()) {
        mya.add( inEnum.nextElement() );
      }

      return mya;
    }
    catch (Exception ex) {
      return null;
    }
  }

  /**
   * 合并列表
   * @param a1 列表数据
   * @param a2 列表数据
   * @return 合并后的列表数据
   */
  public static ArrayList mergeArray(ArrayList a1,
                                     ArrayList a2) {
    ArrayList mya = new ArrayList();
    for (int i=0; i< a1.size(); i++) {
      if ( mya.contains(a1.get(i))) continue;
      mya.add(a1.get(i));
    }
    for (int i=0; i< a2.size(); i++) {
      if ( mya.contains(a2.get(i))) continue;
      mya.add(a2.get(i));
    }
    return mya;
  }

  /**
   * 数组转换为列表
   * @param inArray 数组
   * @return 转换后的列表
   */
  public static ArrayList arrayToList(String[] inArray) {

    try {
      if (inArray == null) return null;
      ArrayList mya = new ArrayList();

      for (int i = 0; i< inArray.length; i++) {
        mya.add( inArray[i] );
      }

      return mya;
    }
    catch (Exception ex) {
      return null;
    }

  }

  /**
   * 数组转换为字符串
   * @param inArray 数组
   * @param insplit 分割符
   * @return 转换后字符串
   */
  public static String arrayToString(String[] inArray,
                                     String insplit) {

    try {
      if ( inArray == null  ) return null;
      if ( insplit == null  ) return null;
      StringBuffer mys = new StringBuffer();
      for (int i = 0; i< inArray.length-1; i++) {
        mys.append(inArray[i]);
        mys.append(insplit);
      }
      if ( inArray.length > 0 )
        mys.append(inArray[inArray.length-1]);
      return mys.toString();
    }
    catch (Exception ex) {
      return null;
    }

  }

  /**
   * 字符串转换为布尔值
   * @param inStr 字符串
   * @return 转换后的布尔值
   */
  public static boolean stringToBoolean(String inStr) {

    try {

      if ( "true".equals(inStr) ) return true;
      if ( "1".equals(inStr) ) return true;
      if ( "yes".equals(inStr) ) return true;
      return false;
    }
    catch (Exception ex) {
      return false;
    }

  }

  /**
   * 字符串转换为hash数据
   * @param inStr 字符串
   * @param inSplit 分割符
   * @return 转换后的hash数据
   */
  public static Hashtable stringToHash(String inStr,
                                       String inSplit) {
    Hashtable myd = new Hashtable();
    try {
      String[] myss = inStr.split(inSplit);
      String myk;
      for (int i=0; i < myss.length; i++) {
        i++;
        if ( i >= myss.length ) break;
        if (  "".equals(myss[i-1]) || "".equals(myss[i]) )
          continue;
        myk = myss[i-1];
        while ( myd.get(myk) != null)
          myk = myk + SAME_NAME;
        myd.put( myk,myss[i]);
      }
      return myd;
    }
    catch (Exception ex) {
      return myd;
    }
  }

  /**
   * hash数据转换为字符串
   * @param inHash hash数据
   * @param inKeys 名表
   * @param inSplit 分割符
   * @return 转换后的字符串
   */
  public static String hashToString(Hashtable inHash,
                                    ArrayList inKeys,
                                    String inSplit) {
    try {
      String myname;
      StringBuffer mystr = new StringBuffer();
      for (int j=0; j< inKeys.size(); j++) {
        myname = inKeys.get(j).toString();
        if ( inHash.get(myname) == null ) continue;
        mystr.append(myname);
        mystr.append(inSplit);
        mystr.append(inHash.get( myname ).toString());
        mystr.append(inSplit);
      }
      return mystr.toString();
    }
    catch (Exception ex) {
      return null;
    }

  }


  /**
   * hash数据转换为字符串
   * @param inHash hash数据
   * @param inSplit 分割符
   * @param mypass 口令域，不允许保存和显示
   * @return 转换后的字符串
   */
  public static String hashToString(Hashtable inHash,
                                    String inSplit,
                                    ArrayList mypass) {
    try {

      StringBuffer mystr = new StringBuffer();
      String kk,vv;
      for (Enumeration e = inHash.keys() ; e.hasMoreElements() ;) {
        kk =  e.nextElement().toString();
        vv = inHash.get(kk).toString();
        if ( ( mypass != null ) &&
             mypass.contains(kk) )
          vv = "";
        mystr.append(kk);
        mystr.append(inSplit);
        mystr.append(vv);
        mystr.append(inSplit);
      }
      return mystr.toString();
    }
    catch (Exception ex) {
      return null;
    }
  }

  /**
   * 列表转换为字符串
   * @param inList 列表
   * @param inSplit 分割符
   * @return 转换后的字符串
   */
  public static String arraylistToString(ArrayList inList,
      String inSplit) {

    try {
      if ( inList == null ) return null;
      StringBuffer str = new StringBuffer();
      int len = inList.size();
      if ( len > 0 )
        str =  new StringBuffer(inList.get(0).toString());
      for (int i = 1; i< len; i++) {
        str.append(inSplit);
        str.append(inList.get(i).toString());
      }
      return str.toString();
    }
    catch (Exception ex) {
      return null;
    }

  }

  //==================== see常用的其它方法 =========================

  /**
   * 对整型hash表进行排序
   * @param mydata 整型hash
   * @return 排序后的整型hash
   */
  public static ArrayList sortIntHash(Hashtable mydata) {
    ArrayList mymaxk = new ArrayList();
    if ( mydata == null ) return mymaxk;
    ArrayList mymax = new ArrayList();
    String mykk, mydd;
    int myv,myvv;
    boolean isadd;
    for (Enumeration e= mydata.keys(); e.hasMoreElements();) {
      mykk = e.nextElement().toString();
      myv = CommonTools.stringToInt(mydata.get(mykk).toString());
      if ( myv == CommonTools.WRONG_INT) continue;
      isadd = false;
      if ( mymax.size() == 0) {
        mymax.add(new Integer(myv));
        mymaxk.add(mykk);
        isadd = true;
      }
      for (int i=0; i< mymax.size(); i++) {
        myvv = CommonTools.stringToInt(mymax.get(i).toString());
        if (  myvv < myv ) {
          mymax.add(i,new Integer(myv));
          mymaxk.add(i,mykk);
          isadd = true;
          break;
        }
      }
      if ( !isadd ) {
        mymax.add(new Integer(myv));
        mymaxk.add(mykk);
      }
    }
    return mymaxk;
  }



  /**
   * 判断一个hash是否包含另一个hash
   * @param mhash 母hash
   * @param shash 子hash
   * @return 母hash是否包含子hash
   */
  public static boolean isSubHash(Hashtable mhash,
                                  Hashtable shash) {
    try {
      if ( shash == null ) return true;
      if ( mhash == null ) return false;
      String myn;
      for (Enumeration e = shash.keys(); e.hasMoreElements();) {
        myn = e.nextElement().toString();
        if  ( mhash.get(myn) == null ) return false;
        if  ( mhash.get(myn).equals(shash.get(myn)))
          continue;
        return false;
      }
      return true;
    }
    catch (Exception ex) {
      return false;
    }
  }

  public static boolean isValidCondition(ArrayList mydata,
                                    ArrayList myconds,
                                    Hashtable mytypes,
                                    String myenc) {

    if ( myconds == null ) return true;
    if ( mydata == null ) return false;
    Hashtable myd;
    for (int i=0; i< mydata.size(); i++) {
      myd = (Hashtable)mydata.get(i);
      if ( isValidCondition(myd,myconds,mytypes,myenc) )
        return true;
    }
    return false;
  }

  public static boolean isValidCondition(Hashtable mydata,
                                    ArrayList myconds,
                                    Hashtable mytypes,
                                    String myenc) {

    if ( myconds == null ) return true;
    if ( mydata == null ) return false;
    Hashtable mycond;
    for (int i=0; i< myconds.size(); i++) {
      mycond = (Hashtable)myconds.get(i);
      if ( CommonTools.isValidHash(mydata,mycond,mytypes,myenc) )
        return true;
    }
    return false;
  }

  /**
   * 判断是否为合法数据
   * @param mhash 数据
   * @param shash 条件
   * @param types 数据类型
   * @param myenc 当前编码
   * @return 数据是否符合条件
   */
  public static boolean isValidHash(Hashtable mydata,
                                    Hashtable mycond,
                                    Hashtable mytypes,
                                    String myenc) {
    try {
      if ( mycond == null ) return true;
      if ( mydata == null ) return false;
      String myn, mvalue,svalue,mytype, mytn;
      if ( myenc == null ) myenc = Constants.DEFAULT_LANGUAGE;
      for (Enumeration e = mycond.keys(); e.hasMoreElements();) {
        myn = e.nextElement().toString();
        svalue = mycond.get(myn).toString();
        mytn = myn.replaceAll(SAME_NAME,"");
        mytype = "string";
        if ( (mytypes != null) &&
             (mytypes.get(mytn) != null))
          mytype = (String)mytypes.get(mytn);
        if  ( mydata.get(mytn) == null ) return false;
        mvalue = mydata.get(mytn).toString();
        if ( isValidValue(mvalue,svalue,mytype,myenc) )
          continue;
        else
          return false;
      }
      return true;
    }
    catch (Exception ex) {
      return false;
    }
  }

  /**
   * 判断是否为合法值
   * @param mvalue 数据值
   * @param svalue 条件值
   * @param mytype 数据类型
   * @param myenc 当前编码
   * @return 是否为合法值
   */
  public static boolean isValidValue(String mvalue,
                                     String svalue,
                                     String mytype,
                                     String myenc) {
    try {
      if ( mvalue == null ) return true;
      if ( svalue == null ) return false;

      if ( svalue.startsWith( START_PREFIX)  ) {
        if ( mvalue.startsWith(svalue.substring(START_PREFIX.length() )) )
          return true;
        else
          return false;
      }

      if ( svalue.startsWith( END_PREFIX)  ) {
        if ( mvalue.endsWith(svalue.substring(END_PREFIX.length())) )
          return true;
        else
          return false;
      }

      if ( svalue.startsWith( NOT_EQUAL_PREFIX)  ) {
        if ( !mvalue.equals(svalue.substring(NOT_EQUAL_PREFIX.length())) )
          return true;
        else
          return false;
      }

      if ( svalue.startsWith( INCLUDE_PREFIX)  ) {
        if ( mvalue.indexOf(svalue.substring(INCLUDE_PREFIX.length())) > -1 )
          return true;
        else
          return false;
      }

      if ( svalue.startsWith( NOT_INCLUDE_PREFIX)  ) {
        if ( mvalue.indexOf(svalue.substring(INCLUDE_PREFIX.length())) < 0 )
          return true;
        else
          return false;
      }

      if ( svalue.startsWith( GREATTER_PREFIX)  ) {
        return compareData(mvalue,
                           svalue.substring(GREATTER_PREFIX.length()),
                           mytype, myenc);
      }

      if ( svalue.startsWith( LESS_PREFIX)  ) {
        return compareData(svalue.substring(GREATTER_PREFIX.length()),
                           mvalue, mytype, myenc);
      }

      if ( svalue.startsWith( EQUAL_PREFIX)  )
        svalue = svalue.substring(EQUAL_PREFIX.length());

      if ( mvalue.indexOf(DataReader.VALUE_HASH_SPLITTER) > -1) {
        Hashtable mymv =  CommonTools.stringToHash(mvalue,
            DataReader.VALUE_HASH_SPLITTER);
        Hashtable mysv =  CommonTools.stringToHash(svalue,
            DataReader.VALUE_HASH_SPLITTER);
        if  ( mymv.equals(mysv))
          return true;
        else
          return false;
      }

      if ( mvalue.indexOf(DataReader.HASH_SPLITTER) > -1) {
        Hashtable mymv =  CommonTools.stringToHash(mvalue,
            DataReader.HASH_SPLITTER);
        Hashtable mysv =  CommonTools.stringToHash(svalue,
            DataReader.HASH_SPLITTER);
        if  ( mymv.equals(mysv))
          return true;
        else
          return false;
      }

      if  ( mvalue.equals(svalue))
        return true;
      else
        return false;
    }
    catch (Exception ex) {
      return false;
    }
  }

  /**
   * 比较数据大小
   * @param value1 数据1
   * @param value2 数据2
   * @param mytype 数据类型
   * @param myenc 当前编码
   * @return 数据1是否大于数据2
   */
  public static boolean  compareData(String value1,
                                  String value2,
                                  String mytype,
                                  String myenc) {
    if (value1 == null) return false;
    if (value2 == null) return true;
    if ( myenc == null ) myenc = Constants.DEFAULT_LANGUAGE;
    boolean isGreater = false;
    if (  "auto".equals(mytype) ) { //自动取值大小
      long myv1 =  CommonTools.getTime(value1.substring(0,value1.indexOf("-") ),myenc);
      long myv2 =  CommonTools.getTime(value2.substring(0,value2.indexOf("-")),myenc);
      isGreater = (  myv1 > myv2 );
    } else if ("date".equals(mytype)) {  //日期比大小
      DateFormat mydv;
      Date myd1,myd2;
      mydv = getDateFormat(myenc);
      myd1 = mydv.parse(value1,new ParsePosition(0));
      if ( myd1 == null ) return false;
      myd2 = mydv.parse(value2,new ParsePosition(0));
      if ( myd2 == null ) return true;
      isGreater = ( myd1.compareTo(myd2) > 0 );
//      long myv1 =  CommonTools.getDate(value1,myenc);
//      long myv2 =  CommonTools.getDate(value2,myenc);
//      isGreater = (  myv1 > myv2 );
    } else if ("time".equals(mytype)) {  //时间比大小
      long myv1 =  CommonTools.getTime(value1,myenc);
      long myv2 =  CommonTools.getTime(value2,myenc);
      isGreater = (  myv1 > myv2 );
    } else if ("int".equals(mytype)) {  //整型比大小
      int myv1 = CommonTools.stringToInt(value1);
      if ( myv1 == CommonTools.WRONG_INT ) return false;
      int myv2 = CommonTools.stringToInt(value2);
      if ( myv2 == CommonTools.WRONG_INT ) return true;
      isGreater = (  myv1 > myv2 );
    } else if ("float".equals(mytype)) {  //浮点数比大小
      float myv1 = CommonTools.stringToFloat(value1);
      if ( myv1 == CommonTools.WRONG_INT * 1f ) return false;
      float myv2 = CommonTools.stringToFloat(value2);
      if ( myv2 == CommonTools.WRONG_INT * 1f ) return true;
      isGreater = (  myv1 > myv2 );
    } else  { //其余按字符串处理（与语言有关）
      Collator myCollator =
          Collator.getInstance(Language.getLocale(myenc));
      isGreater = (myCollator.compare(value1,value2) > 0 );
    }

    return isGreater;
  }

  /**
   * 对数据排序。算法：插入法排序。
   * 对于不同类型（时间、日期、自动、整型、字符串），有不同的排序处理。
   * @param mydata 排序前的数据
   * @param myname 排序字段名
   * @param isAscending 是否升序
   * @param mytype 排序字段的类型
   * @param myenc 数据编码
   * @return 排序后的数据
   */
  public static ArrayList sortData(ArrayList mydata,
                                      String myname,
                                      boolean  isAscending,
                                      String mytype,
                                      String myenc) {
    try {
      if ( mydata == null ) return null;
      if ( myname == null ) return mydata;
      if ( myenc == null ) myenc = Constants.DEFAULT_LANGUAGE;
      ArrayList myindexs = new ArrayList();
      ArrayList mynull = new ArrayList();
      Hashtable item;
      String myv,myindex,myi;
      boolean isadd, com = false;
      for (int i=0; i < mydata.size(); i++) {
        item = (Hashtable)mydata.get(i);
        myindex = CommonTools.intToString(i);
        if ( item.get(myname) == null ) {
          mynull.add(myindex);
          continue;
        }
        myv = item.get(myname).toString();
        isadd = false;
        for (int j=0; j < myindexs.size(); j++) {
          item = (Hashtable)mydata.get(CommonTools.stringToInt(myindexs.get(j).toString()));
          myi = item.get(myname).toString();
          if ( isAscending ) {  // 升序的情况
            if ( CommonTools.compareData(myi,myv,mytype,myenc) ) {
              myindexs.add(j,myindex);
              isadd = true;
              break;
            }
          } else { // 降序的情况
            if ( CommonTools.compareData(myv,myi,mytype,myenc) ) {
              myindexs.add(j,myindex);
              isadd = true;
              break;
            }
          }
        }
        if ( !isadd )  myindexs.add(myindex);
      }

      ArrayList mysort = new ArrayList();
      if ( isAscending ) {   //升序时，空值的排在前面
        for ( int i=0; i< mynull.size(); i++) {
          myindex = mynull.get(i).toString();
          mysort.add( mydata.get(CommonTools.stringToInt(myindex)) );
        }
      }
      for ( int i=0; i< myindexs.size(); i++) {
        myindex = myindexs.get(i).toString();
        mysort.add( mydata.get(CommonTools.stringToInt(myindex)) );
      }
      if ( !isAscending ) { //降序时，空值的排在后面
        for ( int i=0; i< mynull.size(); i++) {
          myindex = mynull.get(i).toString();
          mysort.add( mydata.get(CommonTools.stringToInt(myindex)) );
        }
      }

      return mysort;
    }
    catch (Exception ex) {
      return null;
    }
  }


  /**
   * 获得某种编码的时间格式
   * @param myenc 编码
   * @return 编码对应的时间格式
   */
  public static DateFormat getTimeFormat(String myenc) {
    return DateFormat.getDateTimeInstance(DateFormat.LONG,
        DateFormat.LONG,Language.getLocale(myenc));
  }

  /**
   * 获得某种编码的日期格式
   * @param myenc 编码
   * @return 编码对应的日期格式
   */
  public static DateFormat getDateFormat(String myenc) {
    return DateFormat.getDateInstance(DateFormat.LONG,
                                      Language.getLocale(myenc));
  }

  /**
   * 获得某种编码的当前日期
   * @param myenc 编码
   * @return 编码对应的当前日期
   */
  public static String getCurrentDate(String myenc) {
    return getDateFormat(myenc).format(new Date());
  }

  /**
   * 获得某种编码的当前时间
   * @param myenc 编码
   * @return 编码对应的当前时间
   */
  public static String getCurrentTime(String myenc) {
    return getTimeFormat(myenc).format(new Date());
  }

  /**
   * 获取当前时间的数值
   * @return 当前时间的数值
   */
  public static long  getCurrentTime() {
    Date myd = new Date();
    return myd.getTime();
  }

  /**
   * 获取时间的数值
   * @param mytime 时间字串
   * @param myenc 编码
   * @return 时间的数值
   */
  public static long  getTime(String mytime,
                              String myenc) {
    try {
      DateFormat mydv = getTimeFormat(myenc);
      Date myd = mydv.parse(mytime,new ParsePosition(0));
      if ( myd == null ) return -1;
      return myd.getTime();
    }
    catch (Exception ex) {
      return -1;
    }
  }

  /**
   * 获取时间的显示字串
   * @param mytime 时间数值
   * @param myenc 编码
   * @return 时间的显示字串
   */
  public static String  getTimeShow(long mytime,
                              String myenc) {
    return getTimeFormat(myenc).format(new Date(mytime));
  }

  /**
   * 获取日期的数值
   * @param mydate 日期字串
   * @param myenc 编码
   * @return 日期的数值
   */
  public static long  getDate(String mydate,
                              String myenc) {
    try {
      DateFormat mydv = getDateFormat(myenc);
      Date myd = mydv.parse(mydate,new ParsePosition(0));
      if ( myd == null ) return -1;
      return myd.getTime();
    }
    catch (Exception ex) {
      return -1;
    }
  }

  /**
   * 获取日期的显示字串
   * @param mydate 日期数值
   * @param myenc 编码
   * @return 日期的显示字串
   */
  public static String  getDateShow(long mydate,
                              String myenc) {
    return getDateFormat(myenc).format(new Date(mydate));
  }


  /**
   * 获得树根
   * @param mytheme 数据
   * @return 树根
   */
  public static ArrayList getTreeRoots(Hashtable mytheme) {
    try {
      if ( mytheme == null ) return null;
      ArrayList myr =  new ArrayList();
      for ( int i = 0 ; ; i++) {
        String myid = (new Integer(i)).toString();
        if ( mytheme.get(myid) == null ) break;
        myr.add(myid);
      }
      return myr;
    }
    catch (Exception ex) {
      return null;
    }
  }

  /**
   * 获取所有子节点的序号
   * @param myids 树节点所有序号
   * @param inParent 父节点序号
   * @return 所有子节点的序号
   */
  public static ArrayList getChildsInTree(ArrayList myids, String inParent) {

    if ( myids == null ) return null;

    ArrayList mychilds = new ArrayList();

    for ( int i = 0; i < myids.size(); i++ ) {

      String myid = (String)( myids.get(i) );

      if ( ( myid.length() <= inParent.length() ) ||
           ( myid.startsWith(inParent) == false ) )
        continue;

      String mys = myid.substring( inParent.length() + 1 );

      if ( mys == null ) continue;

      if ( mys.indexOf(".") < 0 )
        mychilds.add(myid);

    }

    return mychilds;
  }

  /**
   * 字节值转换为十六进制值
   * @param myc 字节
   * @return 十六进制值
   */
  public static int byteToInt(byte myc) {
    try {
      return myc & 0XFF;
    }
    catch (Exception ex) {
      return -1;
    }
  }

  /**
   * 字节值转换为十六进制值字串
   * @param myc 字节
   * @return 十六进制值字串
   */
  public static String byteToHex(byte myc) {
    try {
      int myi = byteToInt(myc);
      if ( myi < 1 ) return null;
      return  Integer.toHexString( myi );
    }
    catch (Exception ex) {
      return null;
    }
  }

  /**
   * 十六进制值字串转换为字节
   * @param myhex 十六进制值字串
   * @return 字节
   */
  public static byte  hexToByte(String myhex) {
    try {
      return  Integer.valueOf(myhex,16).byteValue();
    }
    catch (Exception ex) {
      return Byte.parseByte("",16);
    }
  }

  /**
   * 十六进制值字串转换为字串
   * @param myhex 十六进制值字串
   * @return 字串
   */
  public static String hexToString(String myhex) {
    try {
      if ( myhex == null ) return null;
      byte[] mybb = hexToBytes(myhex);
      if ( mybb != null) {
        return new String(mybb);
      } else
        return null;
    }
    catch (Exception ex) {
      return null;
    }
  }

  /**
   * 十六进制值字串转换为字节组
   * @param myhex 十六进制值字串
   * @return 字节组
   */
  public static byte [] hexToBytes(String myhex) {
    try {
      if ( myhex == null ) return null;
      String[] myhexs = myhex.split(BYTE_SPLLTER);
      byte[] mybb = new byte[myhexs.length];
      for (int i=0; i< myhexs.length; i++) {
        mybb[i]= hexToByte(myhexs[i]);
      }
      return mybb;
    }
    catch (Exception ex) {
      return null;
    }
  }

  /**
   * 字节组转换为十六进制值字串
   * @param myb 字节组
   * @param allowError 允许中间出错
   * @return 十六进制值字串
   */
  public static String bytesToHex(byte[] myb,
                                  boolean   allowError) {
    try {
      if ( myb == null ) return null;
      StringBuffer  myout = new StringBuffer();
      String myget;
      for ( int i=0; i< myb.length; i++) {
        myget = byteToHex(myb[i]);
        if ( myget == null ) {
          if ( allowError ) continue;
          else return null;
        }
        myout.append( myget);
        myout.append( BYTE_SPLLTER);
      }
      return myout.toString();
    }
    catch (Exception ex) {
      return null;
    }
  }

  /**
   * 字串转换为十六进制值字串
   * @param mystr 字串
   * @param allowError 允许中间出错
   * @return 十六进制值字串
   */
  public static String stringToHex(String mystr,
                                   boolean   allowError) {
    return  bytesToHex(mystr.getBytes(),allowError);
  }

  /**
   * 将字串加密后，转换为十六进制值字串
   * @param mystr 字串
   * @return 加密后的十六进制值字串
   */
  public static String encryptToHex(String mystr) {
    return encryptToHex(mystr, true);
  }

  /**
   * 将字串加密后，转换为十六进制值字串
   * @param mystr 字串
   * @param allowError 允许中间出错
   * @return 加密后的十六进制值字串
   */
  public static String encryptToHex(String mystr,
                                    boolean  allowError) {
    try {
      if ( mystr == null ) return null;
      byte[] mycipher =  encryptString(mystr);
      if ( mycipher == null ) return null;
      return  bytesToHex(mycipher, allowError);
    }
    catch (Exception ex) {
      return null;
    }
  }

//  public static byte[] decryptString(String mystr) {
//    try {
//      return dealCipher(mystr.getBytes(), false);
//    }
//    catch (Exception ex) {
//      return null;
//    }
//  }
//
//  public static byte[] decryptFromHex(String mystr) {
//    try {
//      return dealCipher( hexToBytes(mystr), false);
//    }
//    catch (Exception ex) {
//      return null;
//    }
//  }

//  public static String decryptFromHexToString(String mystr) {
//    try {
//      byte[] myde = dealCipher( hexToBytes(mystr), false);
//      if ( myde == null ) return null;
//      return new String(myde);
//    }
//    catch (Exception ex) {
//      return null;
//    }
//  }

/**
 * 加密字串
 * @param mystr 字串
 * @return 加密后的字节组
 */
  public static byte[] encryptString(String mystr) {
    try {
      return dealCipher(mystr.getBytes(), true);
    }
    catch (Exception ex) {
      return null;
    }
  }

  /**
   * 加密/解密处理。用DES算法。
   * @param mystr 待处理的字符串
   * @param isEncrypt 是加密还是解密
   * @return 处理后结果
   */
  public static byte[] dealCipher(byte[] mystr,
                                  boolean  isEncrypt) {
    try {

      byte[] myresult;
      if ( isEncrypt ) {
        DESCipher.init(Cipher.ENCRYPT_MODE, DESKey);
        myresult = DESCipher.doFinal(mystr);
      } else {
        DESCipher.init(Cipher.DECRYPT_MODE, DESKey);
        myresult = DESCipher.doFinal(mystr);
      }
      return myresult;
    } catch (Exception ex) {
      return ex.toString().getBytes();
    }
  }

  /**
   * 是否是合法的口令。
   * 判断方法是：将输入的口令用同样算法加密转换，与保存的加密转换结果比较
   * @param inpass 输入的口令
   * @param savedpass 保存的加密转换结果
   * @return 是否是合法的口令
   */
  public static boolean isVaildPassword(String inpass,
                                        Object savedpass) {
    if ( "".equals(inpass)  ) inpass = null;
    if ( "".equals(savedpass)  ) savedpass = null;
    if ( savedpass == null) {
      if ( inpass == null)
        return true;
      else
        return false;
    }
    if ( inpass == null )
      return false;
    return savedpass.toString().equals(encryptToHex(inpass, true));
  }

  /**
   * 标识语言敏感的文本
   * @param astring 文本
   * @return 文本
   */
  public static String tt(String astring) {

    return astring;

  }

}