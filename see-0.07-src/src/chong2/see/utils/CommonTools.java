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
 * <p>Title: ������̹�����</p>
 * <p>Description: Ϊ����з��ṩ�������ݺ������ƽ̨</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: ���</p>
 *
 * <p>���õľ�̬������
 *
 * @author ����
 * @version 0.07
 */

final public class CommonTools {

  /**
   * ���͵Ŀ�ֵ���û����ݲ�Ҫʹ��
   */
  public static int WRONG_INT =  -99999999 ;
  /**
   * �����ж��еġ����ڡ����û����ݲ�Ҫʹ��
   */
  public static String EQUAL_PREFIX = ":c:e:q-c:e:q:"  ;
  /**
   * �����ж��еġ������ڡ����û����ݲ�Ҫʹ��
   */
  public static String NOT_EQUAL_PREFIX = ":c:n:e-c:n:e:"  ;
  /**
   * �����ж��еġ����ڡ����û����ݲ�Ҫʹ��
   */
  public static String GREATTER_PREFIX = ":c:g:t-c:g:t:"  ;
  /**
   * �����ж��еġ�С�ڡ����û����ݲ�Ҫʹ��
   */
  public static String LESS_PREFIX = ":c:l:s-c:l:s:"  ;
  /**
   * �����ж��еġ���ʼ�ڡ����û����ݲ�Ҫʹ��
   */
  public static String START_PREFIX = ":c:s:t-c:s:t:"  ;
  /**
   * �����ж��еġ������ڡ����û����ݲ�Ҫʹ��
   */
  public static String END_PREFIX = ":c:e:d-c:e:d:"  ;
  /**
   * �����ж��еġ����������û����ݲ�Ҫʹ��
   */
  public static String INCLUDE_PREFIX = ":c:i:d-c:i:d:"  ;
  /**
   * �����ж��еġ������������û����ݲ�Ҫʹ��
   */
  public static String NOT_INCLUDE_PREFIX = ":c:n:i-c:n:i:"  ;
  /**
   * �����ж��еġ������������û����ݲ�Ҫʹ��
   */
  public static String SAME_NAME = ":s:m:n-s:m:n:"  ;
  /**
   * txt�ļ������ݵķָ���
   */
  public static String TXT_FILE_SEPARATOR = "\t"  ;
  /**
   * �ֽ���ʮ������ת�������õ��ķָ���
   */
  public static String  BYTE_SPLLTER = ":" ;
  /**
   * DES������Կ
   */
  public static  SecretKey DESKey;
  /**
   * DES���ܳ�ʼ��
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

  //==================== �ļ�����ķ��� =========================

  public static boolean isFile(String infile) {
    if (infile == null ) return false;
    File f = new File(infile);
    return  f.exists();
  }

  /**
   * ɾ���ļ�
   * @param infile Ҫ��ɾ�����ļ���������·����
   * @return �Ƿ�ɹ�
   */
  public static boolean removeFile(String infile) {
    if (infile == null ) return false;
    File f = new File(infile);
    if (f == null ) return false;
    return  f.delete();
  }

  /**
   * ����ļ���С
   * @param infile �ļ���������·����
   * @return �ļ���С
   */
  public static long getFileSize(String infile) {
    if (infile == null ) return -1;
    File f = new File(infile);
    if (f == null ) return -1;
    return  f.length();
  }

  /**
   * ���Ŀ¼���ļ��б�
   * @param indir Ŀ¼��������·����
   * @return Ŀ¼���ļ��б�
   */
  public static String[] getDirList(String indir) {
    File f = new File(indir);
    return  f.list();
  }


  /**
   * ��ȡ�ļ�������ȥ����·���ͺ�׺��
   *
   * @param fname ����·���ͺ�׺�������ļ���
   * @param suff �ļ����ĺ�׺
   * @return ��ȥ·���ͺ�׺���ļ���
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
   * ��ȡ�ļ�������ȥ����·����
   * @param fname ����·���������ļ���
   * @return ��ȥ·�����ļ���
   */
  public static String getFileName(String fname) {

    int sind = fname.lastIndexOf( "/" ) + 1;
    if ( sind < 1 ) sind = fname.lastIndexOf( "\\" ) + 1;
    if ( sind < 1 ) sind = 0;

    return fname.substring(sind);

  }

  /**
   * ��ȡ�ļ���׺��
   * @param fname �ļ���
   * @return ��ȥ·�����ļ���
   */
  public static String getFileSuffix(String fname) {

    String myname = getFileName(fname);
    int sind = myname.lastIndexOf( "." );
    if ( sind < 0 ) return null;
    return myname.substring(sind + 1);
  }

  /**
   * ��ȡ��ȥ·���ͺ�׺���ļ�����
   * @param fname �ļ���
   * @return ��ȥ·���ͺ�׺���ļ���
   */
  public static String getFilePrefix(String fname) {

    String myname = getFileName(fname);
    int sind = myname.lastIndexOf( "." );
    if ( sind < 0 ) return myname;
    return myname.substring(0,sind);
  }


  /**
   * ��ȡ�ļ���·������ȥ�����ļ�����
   * @param fname ����·���������ļ���
   * @return ��ȥ�ļ�����·��
   */
  public static String getFilePath(String fname) {

    int sind = fname.lastIndexOf( "/" ) + 1;
    if ( sind < 1 ) sind = fname.lastIndexOf( "\\" ) + 1;
    if ( sind < 1 ) sind = 0;

    return fname.substring(0, sind);

  }

  /**
   * ������д��һ��txt�ļ�
   * @param fileName �ļ���������·����
   * @param fileEncoding �ļ��ı���
   * @param names ������
   * @param data ����ֵ
   * @return �Ƿ�ɹ�
   */
  public static String  writeTxtFile(String fileName,
                                     String fileEncoding,
                                     ArrayList names,
                                     ArrayList data) {
    return writeTxtFile(fileName,fileEncoding,names,null,data);
  }

  /**
   * ������д��һ��txt�ļ�
   * @param fileName �ļ���������·����
   * @param fileEncoding �ļ��ı���
   * @param names ������
   * @param dis ��ͷ��
   * @param data ����ֵ
   * @return �Ƿ�ɹ�
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

      //ע������Ҫ�趨�ַ�����
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
   * ��һ���ֽ���д���ļ�
   * @param fileName �ļ���������·����
   * @param mydata ����
   * @param mybegin �ֽ�����ʼд��λ��
   * @param myend �ֽ�������д��λ��
   * @return �Ƿ�ɹ�
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

  //==================== see����ת���ʹ���ķ��� =========================

  /**
   * ����ٷ���
   * @param myvalue ����ֵ
   * @param mytotal ������ֵ
   * @param mywei С�����λ��
   * @return �����İٷ���
   */
  public static float getPercent(String myvalue,
                                  String mytotal,
                                  int  mywei) {
    int mytt = CommonTools.stringToInt(mytotal);
    if ( mytt < 1 ) mytt = 1;

    return getPercent(myvalue, mytt,1);
  }

  /**
   * ����ٷ���
   * @param myvalue ����ֵ
   * @param mytotal ������ֵ
   * @param mywei С�����λ��
   * @return �����İٷ���
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
   * ��ȥ����������λ�����������롣
   * @param myvalue ������
   * @param mywei С�������λ��
   * @return ����С�����λ�����������ĸ���ֵ
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
   * ���˵��ظ�������
   * @param mylist ���ݼ�
   * @return ���˺�����ݼ�
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
   * �ַ���ת��Ϊ�б�
   * @param inStr �ַ���
   * @param inSplit �ָ��
   * @return ת������б����쳣�򷵻�null��
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
   * �б�ת����hash����
   * @param inNames ����
   * @param inValues ֵ��
   * @return ת�����hash���ݡ����쳣�򷵻�null��
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
   * �ַ���ת��Ϊ��-ֵ���б�
   * @param inStr �ַ���
   * @param inSplit �ָ��
   * @return ��-ֵ���б�
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
   * �ַ���ת������
   * @param inString �ַ���
   * @return ת������������쳣����һ��������
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
   * �ַ���ת������
   * @param inString �ַ���
   * @return ת������������쳣����һ��������
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
   * ����ת��Ϊ�ַ���
   * @param inInt ����
   * @return ת������ַ���
   */
  public static String intToString(int inInt) {

//    Integer myint = new Integer( inInt );
//    return myint.toString();
    return inInt + "";

  }

  /**
   * �ַ���ת��������
   * @param inString �ַ���
   * @return ת����ĸ��������쳣����һ��������
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
   * ������ת��Ϊ�ַ���
   * @param myfloat ������
   * @return ת������ַ���
   */
  public static String floatToString(float myfloat) {

    return myfloat + "";

  }

  /**
   * ö����ת��Ϊ�б�
   * @param inEnum ö������
   * @return ת������б����ݡ��쳣����null
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
   * �ϲ��б�
   * @param a1 �б�����
   * @param a2 �б�����
   * @return �ϲ�����б�����
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
   * ����ת��Ϊ�б�
   * @param inArray ����
   * @return ת������б�
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
   * ����ת��Ϊ�ַ���
   * @param inArray ����
   * @param insplit �ָ��
   * @return ת�����ַ���
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
   * �ַ���ת��Ϊ����ֵ
   * @param inStr �ַ���
   * @return ת����Ĳ���ֵ
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
   * �ַ���ת��Ϊhash����
   * @param inStr �ַ���
   * @param inSplit �ָ��
   * @return ת�����hash����
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
   * hash����ת��Ϊ�ַ���
   * @param inHash hash����
   * @param inKeys ����
   * @param inSplit �ָ��
   * @return ת������ַ���
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
   * hash����ת��Ϊ�ַ���
   * @param inHash hash����
   * @param inSplit �ָ��
   * @param mypass �����򣬲����������ʾ
   * @return ת������ַ���
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
   * �б�ת��Ϊ�ַ���
   * @param inList �б�
   * @param inSplit �ָ��
   * @return ת������ַ���
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

  //==================== see���õ��������� =========================

  /**
   * ������hash���������
   * @param mydata ����hash
   * @return ����������hash
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
   * �ж�һ��hash�Ƿ������һ��hash
   * @param mhash ĸhash
   * @param shash ��hash
   * @return ĸhash�Ƿ������hash
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
   * �ж��Ƿ�Ϊ�Ϸ�����
   * @param mhash ����
   * @param shash ����
   * @param types ��������
   * @param myenc ��ǰ����
   * @return �����Ƿ��������
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
   * �ж��Ƿ�Ϊ�Ϸ�ֵ
   * @param mvalue ����ֵ
   * @param svalue ����ֵ
   * @param mytype ��������
   * @param myenc ��ǰ����
   * @return �Ƿ�Ϊ�Ϸ�ֵ
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
   * �Ƚ����ݴ�С
   * @param value1 ����1
   * @param value2 ����2
   * @param mytype ��������
   * @param myenc ��ǰ����
   * @return ����1�Ƿ��������2
   */
  public static boolean  compareData(String value1,
                                  String value2,
                                  String mytype,
                                  String myenc) {
    if (value1 == null) return false;
    if (value2 == null) return true;
    if ( myenc == null ) myenc = Constants.DEFAULT_LANGUAGE;
    boolean isGreater = false;
    if (  "auto".equals(mytype) ) { //�Զ�ȡֵ��С
      long myv1 =  CommonTools.getTime(value1.substring(0,value1.indexOf("-") ),myenc);
      long myv2 =  CommonTools.getTime(value2.substring(0,value2.indexOf("-")),myenc);
      isGreater = (  myv1 > myv2 );
    } else if ("date".equals(mytype)) {  //���ڱȴ�С
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
    } else if ("time".equals(mytype)) {  //ʱ��ȴ�С
      long myv1 =  CommonTools.getTime(value1,myenc);
      long myv2 =  CommonTools.getTime(value2,myenc);
      isGreater = (  myv1 > myv2 );
    } else if ("int".equals(mytype)) {  //���ͱȴ�С
      int myv1 = CommonTools.stringToInt(value1);
      if ( myv1 == CommonTools.WRONG_INT ) return false;
      int myv2 = CommonTools.stringToInt(value2);
      if ( myv2 == CommonTools.WRONG_INT ) return true;
      isGreater = (  myv1 > myv2 );
    } else if ("float".equals(mytype)) {  //�������ȴ�С
      float myv1 = CommonTools.stringToFloat(value1);
      if ( myv1 == CommonTools.WRONG_INT * 1f ) return false;
      float myv2 = CommonTools.stringToFloat(value2);
      if ( myv2 == CommonTools.WRONG_INT * 1f ) return true;
      isGreater = (  myv1 > myv2 );
    } else  { //���ఴ�ַ��������������йأ�
      Collator myCollator =
          Collator.getInstance(Language.getLocale(myenc));
      isGreater = (myCollator.compare(value1,value2) > 0 );
    }

    return isGreater;
  }

  /**
   * �����������㷨�����뷨����
   * ���ڲ�ͬ���ͣ�ʱ�䡢���ڡ��Զ������͡��ַ��������в�ͬ��������
   * @param mydata ����ǰ������
   * @param myname �����ֶ���
   * @param isAscending �Ƿ�����
   * @param mytype �����ֶε�����
   * @param myenc ���ݱ���
   * @return ����������
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
          if ( isAscending ) {  // ��������
            if ( CommonTools.compareData(myi,myv,mytype,myenc) ) {
              myindexs.add(j,myindex);
              isadd = true;
              break;
            }
          } else { // ��������
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
      if ( isAscending ) {   //����ʱ����ֵ������ǰ��
        for ( int i=0; i< mynull.size(); i++) {
          myindex = mynull.get(i).toString();
          mysort.add( mydata.get(CommonTools.stringToInt(myindex)) );
        }
      }
      for ( int i=0; i< myindexs.size(); i++) {
        myindex = myindexs.get(i).toString();
        mysort.add( mydata.get(CommonTools.stringToInt(myindex)) );
      }
      if ( !isAscending ) { //����ʱ����ֵ�����ں���
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
   * ���ĳ�ֱ����ʱ���ʽ
   * @param myenc ����
   * @return �����Ӧ��ʱ���ʽ
   */
  public static DateFormat getTimeFormat(String myenc) {
    return DateFormat.getDateTimeInstance(DateFormat.LONG,
        DateFormat.LONG,Language.getLocale(myenc));
  }

  /**
   * ���ĳ�ֱ�������ڸ�ʽ
   * @param myenc ����
   * @return �����Ӧ�����ڸ�ʽ
   */
  public static DateFormat getDateFormat(String myenc) {
    return DateFormat.getDateInstance(DateFormat.LONG,
                                      Language.getLocale(myenc));
  }

  /**
   * ���ĳ�ֱ���ĵ�ǰ����
   * @param myenc ����
   * @return �����Ӧ�ĵ�ǰ����
   */
  public static String getCurrentDate(String myenc) {
    return getDateFormat(myenc).format(new Date());
  }

  /**
   * ���ĳ�ֱ���ĵ�ǰʱ��
   * @param myenc ����
   * @return �����Ӧ�ĵ�ǰʱ��
   */
  public static String getCurrentTime(String myenc) {
    return getTimeFormat(myenc).format(new Date());
  }

  /**
   * ��ȡ��ǰʱ�����ֵ
   * @return ��ǰʱ�����ֵ
   */
  public static long  getCurrentTime() {
    Date myd = new Date();
    return myd.getTime();
  }

  /**
   * ��ȡʱ�����ֵ
   * @param mytime ʱ���ִ�
   * @param myenc ����
   * @return ʱ�����ֵ
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
   * ��ȡʱ�����ʾ�ִ�
   * @param mytime ʱ����ֵ
   * @param myenc ����
   * @return ʱ�����ʾ�ִ�
   */
  public static String  getTimeShow(long mytime,
                              String myenc) {
    return getTimeFormat(myenc).format(new Date(mytime));
  }

  /**
   * ��ȡ���ڵ���ֵ
   * @param mydate �����ִ�
   * @param myenc ����
   * @return ���ڵ���ֵ
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
   * ��ȡ���ڵ���ʾ�ִ�
   * @param mydate ������ֵ
   * @param myenc ����
   * @return ���ڵ���ʾ�ִ�
   */
  public static String  getDateShow(long mydate,
                              String myenc) {
    return getDateFormat(myenc).format(new Date(mydate));
  }


  /**
   * �������
   * @param mytheme ����
   * @return ����
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
   * ��ȡ�����ӽڵ�����
   * @param myids ���ڵ��������
   * @param inParent ���ڵ����
   * @return �����ӽڵ�����
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
   * �ֽ�ֵת��Ϊʮ������ֵ
   * @param myc �ֽ�
   * @return ʮ������ֵ
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
   * �ֽ�ֵת��Ϊʮ������ֵ�ִ�
   * @param myc �ֽ�
   * @return ʮ������ֵ�ִ�
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
   * ʮ������ֵ�ִ�ת��Ϊ�ֽ�
   * @param myhex ʮ������ֵ�ִ�
   * @return �ֽ�
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
   * ʮ������ֵ�ִ�ת��Ϊ�ִ�
   * @param myhex ʮ������ֵ�ִ�
   * @return �ִ�
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
   * ʮ������ֵ�ִ�ת��Ϊ�ֽ���
   * @param myhex ʮ������ֵ�ִ�
   * @return �ֽ���
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
   * �ֽ���ת��Ϊʮ������ֵ�ִ�
   * @param myb �ֽ���
   * @param allowError �����м����
   * @return ʮ������ֵ�ִ�
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
   * �ִ�ת��Ϊʮ������ֵ�ִ�
   * @param mystr �ִ�
   * @param allowError �����м����
   * @return ʮ������ֵ�ִ�
   */
  public static String stringToHex(String mystr,
                                   boolean   allowError) {
    return  bytesToHex(mystr.getBytes(),allowError);
  }

  /**
   * ���ִ����ܺ�ת��Ϊʮ������ֵ�ִ�
   * @param mystr �ִ�
   * @return ���ܺ��ʮ������ֵ�ִ�
   */
  public static String encryptToHex(String mystr) {
    return encryptToHex(mystr, true);
  }

  /**
   * ���ִ����ܺ�ת��Ϊʮ������ֵ�ִ�
   * @param mystr �ִ�
   * @param allowError �����м����
   * @return ���ܺ��ʮ������ֵ�ִ�
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
 * �����ִ�
 * @param mystr �ִ�
 * @return ���ܺ���ֽ���
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
   * ����/���ܴ�����DES�㷨��
   * @param mystr ��������ַ���
   * @param isEncrypt �Ǽ��ܻ��ǽ���
   * @return �������
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
   * �Ƿ��ǺϷ��Ŀ��
   * �жϷ����ǣ�������Ŀ�����ͬ���㷨����ת�����뱣��ļ���ת������Ƚ�
   * @param inpass ����Ŀ���
   * @param savedpass ����ļ���ת�����
   * @return �Ƿ��ǺϷ��Ŀ���
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
   * ��ʶ�������е��ı�
   * @param astring �ı�
   * @return �ı�
   */
  public static String tt(String astring) {

    return astring;

  }

}