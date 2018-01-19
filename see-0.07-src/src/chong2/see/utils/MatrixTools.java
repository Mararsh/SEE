package chong2.see.utils;

import chong2.see.data.base.Constants;
import chong2.see.data.base.DataStructure;
import chong2.see.data.base.Language;

import chong2.see.servlet.common.DataManager;
import chong2.see.xml.DataStructureXmlParser;
import chong2.see.data.DataReader;
import chong2.see.xml.DataXmlWriter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>Title: 软件工程管理环境</p>
 * <p>Description: 为软件研发提供管理数据和任务的平台</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: 虫二</p>
 *
 * 与矩阵表处理相关的静态方法
 *
 * @author 玛瑞
 * @version 0.07
 */

final public class MatrixTools {

  /**
   * 矩阵字段名的联接符，用户字段不可包含此字串。
   */
  public static String NAME_SPLITTER =  "_aaaa_";

  /**
   * 矩阵值字段名，用户字段不可取此字串。
   */
  public static String MATRIX_VALUE =  "SEE_matrix_value";

  /**
   * 矩阵文件名的后缀。
   */
  public static String MATRIX_FILE_SUFFIX  = "_matrix";

  /**
   * 保存矩阵数据记录
   * @param inRequest 当前请求
   * @param mystrucReader 数据集结构
   * @param myset 数据集名
   * @param mysave 操作类型
   * @param mydata 要保存的主数据
   * @param mykeys 数据关键字
   * @return 是否保存成功
   */
  public static String saveMatrixRecord(HttpServletRequest inRequest,
                                        DataStructureXmlParser mystrucReader,
                                        String myset,
                                        String mysave,
                                        Hashtable mydata,
                                        ArrayList mykeys) {

    try {
      if ( mystrucReader == null )
        return "invalid_data";
      if ( mysave == null )
        return "invalid_data";
      if ( (mykeys == null) ||
           (mykeys.size() < 1 ) )
        return Constants.SUCCESSFUL;

      DataReader mydataReader =
          DataManagerTools.getDataReader( inRequest,mystrucReader,
          myset + MATRIX_FILE_SUFFIX,null,null,1,-1);
      if ( mydataReader == null  ) return "invalid_data";

      ArrayList mymatrixnames = mystrucReader.getMatrixValues();
      if ( mymatrixnames == null ) return "invalid_data";

      ArrayList itemnames = mystrucReader.getVaildMatrixItemNames();
      if ( itemnames == null )  return "invalid_data";

      ArrayList myallnames = mystrucReader.getAllMatrixItemNames();
      if ( myallnames == null )  return "invalid_data";

      ArrayList notnulls = mystrucReader.getNotNullMatrixNames();
      if ( notnulls == null )
        notnulls = new ArrayList();

      ArrayList mypass = mystrucReader.getPasswordItems();
      Hashtable myctypes = mystrucReader.getConstraintTypes();

      ArrayList myvnames = new ArrayList();
      ArrayList myvpass = new ArrayList();
      Hashtable myvctypes = new Hashtable();
      String mymitem,myname, myvname,myvalue;
      for (int i=0; i< mymatrixnames.size(); i++) {
        mymitem = mymatrixnames.get(i).toString();
        for (int j=0; j< itemnames.size(); j++) {
          myname = itemnames.get(j).toString();
          myvname = mymitem + NAME_SPLITTER + myname ;
          myvnames.add(myvname);
          if ( ( mypass != null) &&
               mypass.contains(myname) )
            myvpass.add(myvname);
          if ( ( myctypes != null) &&
               ( myctypes.get(myname) != null ))
            myvctypes.put(myvname,myctypes.get(myname));
        }
      }

      Hashtable items = ServletTools.getParameters(inRequest,
          myvnames,myvpass,myvctypes,mysave);
      if ( (items == null) ||
           ( items.size() < 1) )
        return Constants.SUCCESSFUL;

      ArrayList tkeys = mystrucReader.getKeys();
      ArrayList keys = new ArrayList(tkeys);
      keys.add(MATRIX_VALUE);

      Hashtable myold = null;
      Hashtable item ;
      ArrayList myaudit =
          ServletTools.getAppDataAuditTypes(inRequest);
      String mytrans,mydeal = mysave,ret = Constants.SUCCESSFUL ;
      Hashtable mykv = null;
      Hashtable mykeyvalues,mych;
      boolean hasv = false, isw = false;
      String myk,myv;
      for (int i=0; i< mymatrixnames.size(); i++) {
        mymitem = mymatrixnames.get(i).toString();
        mytrans = LanguageTools.atr(inRequest,mymitem);
        hasv = false;
        item = new Hashtable();
        for (int j=0; j< itemnames.size(); j++) {
          myname = itemnames.get(j).toString();
          myvname = mymitem + NAME_SPLITTER + myname ;
          if ( items.get(myvname) != null) {
            item.put(myname,items.get(myvname));
            hasv = true;
          }
        }
        item.put(MATRIX_VALUE,mytrans);
        if ( !hasv)
          mych = null;
        else
          mych = new Hashtable(item);
        if ( !hasv &&
             ("add".equals(mysave) ||
             "copy".equals(mysave) ||
             "insert".equals(mysave) ||
             mysave.startsWith("batch_modify")  ) )
          continue;   // 没有有效数据的情况
        for (int kk=0; kk < mykeys.size(); kk++) {
          mykeyvalues = (Hashtable)mykeys.get(kk);
          if ( mykeyvalues == null ) continue;
          for (int j=0; j< tkeys.size(); j++) {
            myk = tkeys.get(j).toString();
            myv = null;
            if ( ( mydata != null ) &&
                 mydata.get(myk) != null ) {
              myv = mydata.get(myk).toString();
             } else if ( mykeyvalues.get(myk) != null)
                myv = mykeyvalues.get(myk).toString();
              else
                return "invalid_data";
              item.put(myk,myv);
          }
          myold = null;
          mysave = mydeal;
          mykv = new Hashtable(mykeyvalues);
          mykv.put(MATRIX_VALUE,mytrans);

          if ( "insert".equals(mysave) ) {
            myold =
                mydataReader.insertValidRecord(myallnames,notnulls,
                keys,mykv,item);
            if ( myold == null )
              mysave = "add";
            else
              isw = true;
          } else if ( "modify".equals(mysave) ||
                       mysave.startsWith("batch_modify") ) {
            Object myret =
                mydataReader.modifyValidRecord(mydataReader.getRecords(),
                myallnames,notnulls,
                keys,mykv,item, mypass,
                !"modify".equals(mysave),true);
            if ( myret == null )
              myold = null;
            else
              myold = (Hashtable)myret;
            if ( myold == null ) { //原数据不存在，或修改失败
              if ( hasv ) // 有矩阵值
                mysave = "add";
              else { // 无矩阵值
                continue;
              }
            } else { //原数据存在
              if ( hasv )  //修改成功
                isw = true;
              else  {  //删除空矩阵值
                Object myr =
                    mydataReader.removeValidRecord(myallnames,notnulls,keys,
                    mykv,null);
                if ( myr == null)
                  myold = null;
                else {
                  myold =  new Hashtable();
                  mysave = "remove";
                  isw = true;
                }
              }
            }
          }
          if ( "add".equals(mysave) ||
               "copy".equals(mysave) ) {
            if ( !hasv ) continue;
            myold = mydataReader.addValidRecord(myallnames,notnulls,
                keys,item);
            if ( myold != null)
              isw = true;
          }
          if ( myold != null )
            ret = Constants.SUCCESSFUL;
          else
            ret = "invalid_data";
          if ( !mydeal.startsWith("batch_modify") &&
               myaudit.contains(LanguageTools.atr(inRequest,mydeal)) ) {
            if ( "add".equals(mysave) ||
                 "copy".equals(mysave) ) {
              AuditTools.addAudit(inRequest,myset,mydeal,
                                  mykeyvalues, item  , null ,
                                  ret,
                                  LanguageTools.atr(inRequest,mysave) +
                                  LanguageTools.atr(inRequest,"matrix_value") ,
                                  mypass);
            } else {
              AuditTools.addAudit(inRequest,myset,mydeal,
                                  mykeyvalues, myold  , item ,
                                  ret,
                                  LanguageTools.atr(inRequest,mysave) +
                                  LanguageTools.atr(inRequest,"matrix_value") ,
                                  mypass);

            }
          }
          if ( myold == null )
            break;
        }
        if ( "batch_modify".equals(mydeal) &&
             myaudit.contains(LanguageTools.atr(inRequest,mydeal)) ) {
          AuditTools.addAuditL(inRequest,myset,mydeal,
                               mykeys, null  , mych ,
                               ret,
                               LanguageTools.tr(inRequest,"matrix_value") ,
                               mypass);
          } else if (  "batch_modify_all".equals(mydeal) &&
                       myaudit.contains(LanguageTools.atr(inRequest,"batch_modify"))) {
            AuditTools.addAuditL(inRequest,myset,mydeal,
                                 null , null  , mych ,
                                 ret,
                                 LanguageTools.tr(inRequest,"matrix_value"),
                                 mypass);
          }
          if ( !Constants.SUCCESSFUL.equals(ret) )
            return ret;
      }

      String retw = Constants.SUCCESSFUL;
      if ( isw) {
        String fdata =
            DataManagerTools.getDataValuesFile(inRequest,
            myset + MATRIX_FILE_SUFFIX);
        DataXmlWriter myw = new DataXmlWriter();
        retw = myw.writeData(mydataReader.getRecords(),fdata,
                             ServletTools.getAppDefaultCharset(inRequest));
      }

      return retw;
    }
    catch (Exception ex) {
      return null;
    }
  }


  /**
   * 删除矩阵数据记录
   * @param inRequest 当前请求
   * @param mystrucReader 数据集结构
   * @param myset 数据集名
   * @param mykeyvalues 数据关键字
   * @return 是否删除成功
   */
  public static String removeMatrixRecord(HttpServletRequest inRequest,
                                    DataStructureXmlParser mystrucReader,
                                    String myset,
                                    Hashtable mykeyvalues) {

    if ( mystrucReader == null )
      return "invalid_data";

    DataReader mydataReader =
        DataManagerTools.getDataReader( inRequest,mystrucReader,
        myset + MATRIX_FILE_SUFFIX,null,null,1,-1);
    if ( mydataReader == null  ) return "invalid_data";

    ArrayList mydata = null;
    String ret = Constants.SUCCESSFUL;
    if ( ( mykeyvalues == null) ||
         mykeyvalues.isEmpty() ) {
      mydata = new ArrayList();
    } else {
      ArrayList myrecords = mydataReader.getRecords();
      mydata = mydataReader.removeValidRecord(myrecords,
          mykeyvalues, null,true);
    }
    if ( mydata == null )
      return "invalid_data";

    String fdata =
        DataManagerTools.getDataValuesFile(inRequest,
        myset + MATRIX_FILE_SUFFIX);
    DataXmlWriter myw = new DataXmlWriter();
    String retw = myw.writeData(mydata,fdata,
                                ServletTools.getAppDefaultCharset(inRequest));

    return retw;
  }

  /**
   * 输出矩阵数据记录
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param mystrucReader 数据集结构
   * @param myset 数据集名
   * @param mywhere 操作查询条件
   * @param myotype 操作类型
   * @param items 数据字段的定义
   * @param keyCanModify 关键字能否修改
   * @param mystringnot 非空字串类型的字段名列表
   * @param mypassword 非空口令类型的字段名列表
   * @param mylink 非空联接类型的字段名列表
   * @param myfile 非空文件类型的字段名列表
   * @param mynumber 非空数字类型的字段名列表
   * @param mychecknot 非空单选/多选类型的字段名列表
   * @param myselectnot 非空选择类型的字段名列表
   * @return 累加了矩阵字段后的各类非空类型字段列表
   */
  public static Object[] outMatrixFields(HttpServletRequest inRequest,
                                      PrintWriter out,
                                      DataStructureXmlParser mystrucReader,
                                      String myset,
                                      ArrayList mywhere,
                                      String myotype,
                                      Hashtable items,
                                      boolean  keyCanModify,
                                      ArrayList mystringnot,
                                      ArrayList mypassword,
                                      ArrayList mylink,
                                      ArrayList myfile,
                                      ArrayList mynumber,
                                      Hashtable mychecknot,
                                      Hashtable myselectnot)   {

    if ( mystrucReader == null ) return null;

    ArrayList mymatrixnames = mystrucReader.getMatrixValues();
    if ( mymatrixnames == null )  return null;

    ArrayList itemnames = mystrucReader.getVaildMatrixItemNames();
    if ( itemnames == null )  return null;

    ArrayList notnulls = mystrucReader.getNotNullMatrixNames();
    if ( notnulls == null )
      notnulls = new ArrayList();

    ArrayList keys = mystrucReader.getKeys();

    DataReader mydataReader =
        DataManagerTools.getDataReader( inRequest,mystrucReader,
        myset + MATRIX_FILE_SUFFIX,null,mywhere,1,-1);
    if ( mydataReader == null  ) return null;
    ArrayList mydatas = mydataReader.getRecords();

    out.println("\t<TABLE class=data cellPadding=2 align=center>");
    out.println("\t\t<TR class=tableheader >");
    String myname = LanguageTools.tr(inRequest,
                                     MatrixTools.MATRIX_VALUE) +
                    " \\ " +
                    LanguageTools.tr(inRequest, "matrix_item");
    out.println("<TH title=\"" + myname +
                "\" border=1 align=center >" +
                myname + "</TH>");
    Hashtable item;
    String myvalue,ret,myclass="",mydes;
    for (int i=0; i < itemnames.size(); i++ ) {
      myname =  itemnames.get(i).toString();
      myclass = "";
      if ( notnulls.contains(myname) )
        myclass = " class=notnull ";
      item = (Hashtable)items.get(myname);
      if ( item.get("displayName") == null )
        myname = myname;
      else
        myname = item.get("displayName").toString();
      myname =  LanguageTools.tr(inRequest,myname);
      if ( item.get("unitage") != null )
        myname = myname + " (" +
        LanguageTools.tr(inRequest,item.get("unitage").toString()) +
        ") ";
      if ( item.get("description") == null )
        mydes = myname;
      else
        mydes = item.get("description").toString();
      mydes = LanguageTools.tr(inRequest,mydes);
      out.println("<TH title=\"" + mydes +
                  "\" border=1 align=center " + myclass + " >" +
                  myname + "</TH>");
    }
    out.println("\t\t</TR>");

    Hashtable myvd;
    String mymitem,myrname,mytrans;
    Hashtable mydata;
    Object mykk;
    for (int i=0; i < mymatrixnames.size(); i++ ) {
      mymitem = mymatrixnames.get(i).toString();
      mytrans = LanguageTools.atr(inRequest,mymitem);
      myvd = null;
      if ( mydatas != null ) {
        for (int k=0; k < mydatas.size(); k++) {
          mydata = (Hashtable)mydatas.get(k);
          if ( !mytrans.equals(mydata.get(MatrixTools.MATRIX_VALUE)) )
            continue;
          myvd = mydata;
          break;
        }
      }
      out.println("\t\t<TR  class=tabledata >");
      out.println("\t\t\t<TD>\t\t\t" + mytrans + "</TD>");
      for (int j=0; j < itemnames.size(); j++ ) {
        myrname = itemnames.get(j).toString();
        if ( items.get(myrname) == null ) continue;
        out.println("\t\t\t<TD>\t\t\t");
        item = (Hashtable)items.get(myrname);
        myvalue = null;
        if ( ( myvd != null ) &&
             ( myvd.get(myrname) != null ) )
          myvalue = myvd.get(myrname).toString();
        myname = mymitem + MatrixTools.NAME_SPLITTER + myrname;
        ret = OutputRecordTools.outEditController(inRequest,out,
            myset,myname,item,keys,notnulls,
            myvalue,myotype,keyCanModify);

        if ( !"add".equals(myotype) &&
             !"query_setup".equals(myotype) &&
             !"modify".equals(myotype) &&
             !"copy".equals(myotype) ) continue;
        if ( ret.startsWith("ok-password"))
          mypassword.add(myname);
        else if ( ret.startsWith("ok-link"))
          mylink.add(myname);
        else if ( ret.startsWith("ok-file"))
          myfile.add(myname);
        else if ( ret.startsWith("ok-number"))
          mynumber.add(myname);
        if ( !"query_setup".equals(myotype) &&
             !notnulls.contains(myrname))
          continue;
        if ( ret.startsWith("ok-radio-"))
          mychecknot.put(myname, ret.substring(9));
        else if ( ret.startsWith("ok-check-"))
          mychecknot.put(myname, ret.substring(9));
        else if ( ret.startsWith("ok-list-"))
          myselectnot.put(myname, ret.substring(8));
        else if ( ret.startsWith("ok-droplist-"))
          myselectnot.put(myname, ret.substring(12));
        else
          mystringnot.add(myname);
        out.println("\t\t\t</TD>");
      }
      out.println("\t\t</TR>");
    }
    out.println("\t</TABLE><BR>");

    Object[] myret = {mystringnot,mypassword,mylink,myfile,mynumber,
      mychecknot,myselectnot};
    return myret;

  }

}