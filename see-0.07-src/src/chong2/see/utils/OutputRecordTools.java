package chong2.see.utils;

import chong2.see.data.base.Constants;
import chong2.see.data.base.Language;
import chong2.see.servlet.common.DataManager;
import chong2.see.xml.*;
import chong2.see.data.DataReader;

import java.io.PrintWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>Title: 软件工程管理环境</p>
 * <p>Description: 为软件研发提供管理数据和任务的平台</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: 虫二</p>
 *
 * 与输出数据记录相关的静态方法
 *
 * @author 玛瑞
 * @version 0.07
 */

final public class OutputRecordTools {

  /**
   * 输出数据记录的明细
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param mystrucReader 数据集结构
   * @param myset 数据集名
   * @param myotype 操作类型
   * @return 是否显示成功
   */
  public static String outRecord(HttpServletRequest inRequest,
                                 PrintWriter out,
                                 DataStructureXmlParser mystrucReader,
                                 String myset,
                                 String myotype) {

    if ( mystrucReader == null )  return "invalid_data";
    if ( myset == null )  return "invalid_data";
    if ( myotype == null )  return "invalid_data";

    boolean showmatrix =
        "yes".equals(ServletTools.getParameter(inRequest,"mymatrix"));

    Hashtable items = mystrucReader.getHashItems();
    if ( items == null) return "invalid_data";

    DataManager.initOutput(inRequest,out,mystrucReader);

    ArrayList itemnames;
    ArrayList notnulls ;
    ArrayList keys = mystrucReader.getKeys();
    String myget;
    if ( showmatrix ) {
      myget = myset + MatrixTools.MATRIX_FILE_SUFFIX ;
      itemnames =  mystrucReader.getAllMatrixItemNames();
      keys.add(MatrixTools.MATRIX_VALUE);
      notnulls = mystrucReader.getNotNullMatrixNames();
    } else {
      myget = myset;
      itemnames =  mystrucReader.getEditItemNames();
      notnulls = mystrucReader.getNotNullNames();
    }
    ArrayList mypass = mystrucReader.getPasswordItems();
    boolean keyCanModify = false;
    if ( mystrucReader.getDataSet().get("keyCanModify") != null )
      keyCanModify =
      "yes".equals(mystrucReader.getDataSet().get("keyCanModify").toString());

    String myname,ret;
    Hashtable item;
    String mywherevalue =
        ServletTools.getParameter(inRequest,"mykeys");
    ArrayList mydatas = new ArrayList();
    Hashtable mydata = new Hashtable();
    String mytrueset =
        ServletTools.getParameter(inRequest,"mytrueset");
    boolean isaudit =
        ( ( mytrueset != null ) &&
        "audit".equals(myotype)  ) ;
    Hashtable mykeyvalues = null;
    ArrayList mywhere = null;
    DataReader mydataReader = null;
    if ( !"add".equals(myotype) &&
         !"query_setup".equals(myotype) &&
         !"batch_modify".equals(myotype) ) {
      if (mywherevalue != null) {
        if ( isaudit  ) {
          mykeyvalues = new Hashtable();
          mykeyvalues.put("data",LanguageTools.atr(inRequest,mytrueset));
          mykeyvalues.put("keys",
                          DataManagerTools.getValueWhere(mywherevalue));
        } else
          mykeyvalues = DataManagerTools.getWhere(inRequest,mywherevalue);
        if ( mykeyvalues != null )
        {
          mywhere =  new ArrayList();
          mywhere.add(mykeyvalues);
          mydataReader =
              DataManagerTools.getDataReader( inRequest,
              mystrucReader,myget, null,mywhere,0,-1);
          if ( mydataReader != null  )
            mydatas = mydataReader.getRecords();
        }
      }
      if ( mydatas == null ) {
        if ( isaudit )
          mydatas = new ArrayList();
        else {
          if ( !"add".equals(myotype) &&
               ServletTools.getAppDataAuditTypes(inRequest).contains(LanguageTools.atr(inRequest,"show_record")) )
            AuditTools.addAudit(inRequest,myset,myotype,mykeyvalues,
            null,null,"invalid_data", null ,mypass);
          return "invalid_data";
        }
      }
    } else {
      mydatas.add(mydata);
    }

    ArrayList mynot = new ArrayList();
    mynot.addAll(keys);
    if ( notnulls != null) {
      for (int i=0; i< notnulls.size(); i++) {
        if ( mynot.contains(notnulls.get(i)) )
          continue;
        mynot.add(notnulls.get(i));
      }
    }
    if ( "audit".equals(myotype) )
      out.println("<P class=info>" +
                  LanguageTools.tr(inRequest,"audit_following"));
    if ( "query_setup".equals(myotype) )
      out.println("<P class=info>" +
                  LanguageTools.tr(inRequest,"query_setup_info"));
    if ( "batch_modify".equals(myotype) )
      out.println("\t<P align=left class=info>" +
                  LanguageTools.tr(inRequest,"batch_modify_step2") +
                  "</P>");

    String myvalue = null, mydes, mydis, mymitem,myrname;
    Hashtable myd,myi;
    ArrayList showdis = new ArrayList();
    ArrayList showdes = new ArrayList();
    ArrayList constype = new ArrayList();
    myd = mystrucReader.getHashItems();
    for (int i=0; i< itemnames.size(); i++) {
      myname = itemnames.get(i).toString();
      myi = (Hashtable)myd.get(myname);
      if ( myi.get("displayName") == null )
        myvalue = myname;
      else
        myvalue = myi.get("displayName").toString();
      showdis.add(i,LanguageTools.tr(inRequest,myvalue));
      if ( myi.get("description") == null )
        mydes = myname;
      else
        mydes = myi.get("description").toString();
      showdes.add(i,LanguageTools.tr(inRequest,mydes));
      if ( myi.get("valueConstraintType") == null )
        constype.add("null");
      else
        constype.add(myi.get("valueConstraintType"));
    }

    ArrayList mystringnot= new ArrayList();
    ArrayList mypassword= new ArrayList();
    ArrayList mylink= new ArrayList();
    ArrayList myfile= new ArrayList();
    ArrayList mynumber= new ArrayList();
    Hashtable mychecknot= new Hashtable();
    Hashtable myselectnot= new Hashtable();
    boolean lookpm =
        DataManager.PERSONAL_INFORMATION.equals(myset) &&
        "show_blank".equals(myotype);
    if ( lookpm )
      mydataReader = DataManagerTools.getDataReader( inRequest,
          mystrucReader,myset, null,null,0,-1);
    String myuser = ServletTools.getSessionUser(inRequest);
    boolean isread = false;
    for (int k=0; k < mydatas.size(); k++) {
      mypassword= new ArrayList();
      mylink= new ArrayList();
      myfile= new ArrayList();
      mynumber= new ArrayList();
      mychecknot= new Hashtable();
      myselectnot= new Hashtable();
      if ("query_setup".equals(myotype))
        out.println("\t<TABLE align=center border=1 class=bar>");
      else
        out.println("\t<TABLE align=center class=data>");
      mydata = (Hashtable)mydatas.get(k);
      for (int i=0; i < itemnames.size(); i++ ) {
        myname = itemnames.get(i).toString();
        if ( items.get(myname) == null ) continue;
        item = (Hashtable)items.get(myname);
        myvalue = null;
        if ( !"add".equals(myotype) &&
             !"query_setup".equals(myotype) &&
             !"batch_modify".equals(myotype) &&
             (mydata!= null) &&
             ( mydata.get(myname)!= null ))
          myvalue = mydata.get(myname).toString();

        ret =
            outInput(inRequest,out,myset,myname,item,keys,notnulls,
            myvalue,myotype,keyCanModify);
        if ( !"add".equals(myotype) &&
             !"query_setup".equals(myotype) &&
             !"batch_modify".equals(myotype) &&
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
        if ( "batch_modify".equals(myotype) ||
             (!"query_setup".equals(myotype) &&
             !mynot.contains(myname) ) )
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
      }
      out.println("\t</TABLE><BR>");
      if (!showmatrix &&
          mystrucReader.hasMatrixDefine() &&
          !"query_setup".equals(myotype) ) {
        Object[] myret =
            MatrixTools.outMatrixFields(inRequest,out,
            mystrucReader,myset,mywhere,
            myotype,items,keyCanModify,
            mystringnot,mypassword,mylink,myfile,mynumber,
            mychecknot,myselectnot);
        if ( myret != null ) {
          mystringnot = (ArrayList)myret[0];
          mypassword = (ArrayList)myret[1];
          mylink = (ArrayList)myret[2];
          myfile = (ArrayList)myret[3];
          mynumber = (ArrayList)myret[4];
          mychecknot = (Hashtable)myret[5];
          myselectnot = (Hashtable)myret[6];
        }
      }
      if ( lookpm &&
           myuser.equals(mydata.get("receiver")) ) {
        mydata.put("have_read",LanguageTools.atr(inRequest,"yes"));
        if ( mydataReader.modifyValidRecord(itemnames,notnulls,
            keys,mydata, mypass) != null )
          isread = true;
      }
    }
    // 标记“个人信息”记录为“已读”
    if ( lookpm &&
         isread ) {
      String fdata =
          DataManagerTools.getDataValuesFile(inRequest,myset);
      DataXmlWriter myw = new DataXmlWriter();
      myw.writeData(mydataReader.getRecords(),fdata,
                    ServletTools.getAppDefaultCharset(inRequest));
    }

    out.println("\t<P align=center>");
    if ( "add".equals(myotype) ||
         "modify".equals(myotype) ||
         "batch_modify".equals(myotype) ||
         "copy".equals(myotype) ) {
      OutputController.outSaveResetBar(inRequest,out,"seeform");
      out.println("\t<SCRIPT LANGUAGE='javascript'>\n");
      out.println("\t\tfunction see_save()");
      out.println("\t\t{");
      if ( (mystringnot != null) &&
          ( mystringnot.size() > 0) )
        out.println("\t\t\t if ( see_checkStringNull() < 0 ) return;");
      if ( (mypassword != null) &&
          ( mypassword.size() > 0) )
        out.println("\t\t\t if ( see_checkPassword() < 0 ) return;");
      if ( (mylink != null) &&
          ( mylink.size() > 0) )
        out.println("\t\t\t if ( see_checkLink() < 0 ) return;");
      if ( (myfile != null) &&
          ( myfile.size() > 0) )
        out.println("\t\t\t if ( see_checkFile() < 0 ) return;");
      if ( (mynumber != null) &&
          ( mynumber.size() > 0) )
        out.println("\t\t\t if ( see_checkNumber() < 0 ) return;");
      if ( (mychecknot != null) &&
          ( mychecknot.size() > 0) )
        out.println("\t\t\t if ( see_checkCheckNull() < 0 ) return;");
      if ( (myselectnot != null) &&
          ( myselectnot.size() > 0) )
        out.println("\t\t\t if ( see_checkSelectNull() < 0 ) return;");
      out.println("\t\t\tdocument.seeform.mybutton.value=\"save\";");
      out.println("\t\t\tdocument.seeform.submit();");
      out.println("\t\t}");
      if ( mystringnot != null )
        OutputController.outCheckStringNull(inRequest,out,
            "seeform",mystringnot);
      if ( mypassword != null )
        OutputController.outCheckPassword(inRequest,out,
            "seeform",mypassword);
      if ( mylink != null )
        OutputController.outCheckLink(inRequest,out,
                                      "seeform",mylink);
      if ( myfile != null )
        OutputController.outCheckFile(inRequest,out,
                                      "seeform",myfile);
      if ( mynumber != null )
        OutputController.outCheckNumber(inRequest,out,
                                      "seeform",mynumber);
      if ( mychecknot != null )
        OutputController.outCheckSelectNull(inRequest,out,
            "seeform",mychecknot,true);
      if ( myselectnot != null )
        OutputController.outCheckSelectNull(inRequest,out,
            "seeform",myselectnot,false);
      out.println("\t</SCRIPT>");
    } else if ( "query_setup".equals(myotype) ) {
      QueryTools.outSetAndBar(inRequest,out,mychecknot,myselectnot,itemnames);
    } else if ( "show_blank".equals(myotype) ||
                "audit".equals(myotype) ) {
      OutputController.outCloseBar(inRequest,out);
    } else
      OutputController.outBackBar(inRequest,out);

    out.println("\t</P>");

    out.println("</FORM> \n");

    if ( !"add".equals(myotype) &&
         !"batch_modify".equals(myotype) &&
         !"query_setup".equals(myotype) &&
         ServletTools.getAppDataAuditTypes(inRequest).contains(LanguageTools.atr(inRequest,"show_record")) )
      AuditTools.addAudit(inRequest,myset,
      "show_record",mykeyvalues,
      null,null,Constants.SUCCESSFUL, null ,mypass);


    return Constants.SUCCESSFUL;
  }

  /**
   * 显示数据的一个编辑域
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param myset 数据集名
   * @param myname 域名
   * @param item 域的定义
   * @param keys 数据的关键字
   * @param notnulls 数据的非空域
   * @param myvalue 域值
   * @param myotype 操作类型
   * @param keyCanModify 数据的关键字可否修改
   * @return 是否显示成功
   */
  public static String outInput(HttpServletRequest inRequest,
                                PrintWriter out,
                                String myset,
                                String myname,
                                Hashtable item,
                                ArrayList keys,
                                ArrayList notnulls,
                                String myvalue,
                                String myotype,
                                boolean  keyCanModify) {

    try {
      String mydis,mydes,myunit;

      if ( item.get("description") == null  )
        mydes = myname;
      else
        mydes = item.get("description").toString();

      if ( item.get("unitage") == null  )
        myunit = "";
      else
        myunit = " (" +
        LanguageTools.tr(inRequest,item.get("unitage").toString()) +
        ") ";

      if ( item.get("displayName") == null  )
        mydis = myname;
      else
        mydis = item.get("displayName").toString();

      if ( "query_setup".equals(myotype) ) {

        out.print("\t\t<TR title=\"" +
                  LanguageTools.tr(inRequest,mydes) +
                  "\" class=tableheader>\n");
        out.println("\t\t\t<TH align=center colspan=2>" +
                    LanguageTools.tr(inRequest,mydis) + myunit +
                    "</TH></TR>");
        out.println("\t\t <TR class=tableline>");
        out.println("\t\t\t<TD align=right>" +
                    LanguageTools.tr(inRequest,"operator") +
                    " : </TD>");
        out.println("\t\t\t<TD align=left >");
        OutputController.outRadioInput(inRequest,out,
                                       myname + "_op",
                                       CommonTools.EQUAL_PREFIX,
                                       QueryTools.CONDITION_OPERATORS,
                                       null, false, false);
        out.println("\t\t\t </TD>");
        out.println("\t\t <TR class=tableline>");
        out.println("\t\t\t<TD align=right>" +
                    LanguageTools.tr(inRequest,"value") +
                    " : </TD>");
        out.println("\t\t\t<TD align=left >");
      } else {

        out.print("\t\t<TR title=\"" +
                  LanguageTools.tr(inRequest,mydes) +
                  "\" class=tableline>\n");
        out.print("\t\t\t<TD ");
        if ( "batch_modify".equals(myotype) )
          out.print("class=dataname ");
        else if ( keys.contains(myname) )
          out.print("class=datakey ");
        else if ( (notnulls != null) &&
                  notnulls.contains(myname)  )
          out.print("class=notnull ");
        else
          out.print("class=dataname ");
        out.println(" align=right>" +
                    LanguageTools.tr(inRequest,mydis) + myunit +
                    " : </TD>");
        out.println("\t\t\t <TD align=left >");
      }

      String myret = outEditController(inRequest,out,myset,myname,
                                       item,keys,notnulls,
                                       myvalue,myotype,keyCanModify);

      out.println("\t\t</TD></TR>");

      return myret;

    }
    catch (Exception ex) {
      return ex.toString();
    }

  }

  /**
   * 显示一个编辑域控件
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param myset 数据集名
   * @param myname 域名
   * @param item 域的定义
   * @param keys 数据的关键字
   * @param notnulls 数据的非空域
   * @param myvalue 域值
   * @param myotype 操作类型
   * @param keyCanModify 数据的关键字可否修改
   * @return 是否显示成功
   */
  public static String outEditController(HttpServletRequest inRequest,
      PrintWriter out,
      String myset,
      String myname,
      Hashtable item,
      ArrayList keys,
      ArrayList notnulls,
      String myvalue,
      String myotype,
      boolean  keyCanModify) {

    try {
      String mydis,mydes,mytype,mydefault,myetype,myconstype,mycons,
      mymaptype,myinput;
      boolean  mytrans = true; // 缺省是翻译域值
      Hashtable  mymap;
      int mysize, myesize;

      if ( item.get("type") == null  )
        mytype = "string";
      else
        mytype = item.get("type").toString();

//      if ( item.get("size") == null )
//        mysize =  DataStructure.DATAITEM_DEFAULT_SIZE;
//      else {
//        mysize =  Tools.stringToInt(item.get("size").toString());
//        if ( mysize == Tools.WRONG_INT )
//          mysize = DataStructure.DATAITEM_DEFAULT_SIZE;
//      }
      mysize = CommonTools.WRONG_INT ;
      if ( item.get("size") != null ) {
        mysize =  CommonTools.stringToInt(item.get("size").toString());
      }

      myesize = CommonTools.WRONG_INT ;
      if ( item.get("editWidth") != null ) {
        myesize =  CommonTools.stringToInt(item.get("editWidth").toString());
      }

      if ( item.get("valueTranslated") != null  )
        mytrans = "yes".equals(item.get("valueTranslated").toString());

      if ( item.get("defaultValue") == null  )
        mydefault = null;
      else {
        mydefault = item.get("defaultValue").toString();
        if ( mytrans )
          mydefault = LanguageTools.atr(inRequest,mydefault);
      }

      if ( "add".equals(myotype)  )
        myvalue = mydefault;

      if ( "batch_modify".equals(myotype) )
        myvalue = null;

      if ( myvalue != null ) {

        myvalue = myvalue.replaceAll(DataReader.CHAR_AND,"&");

        if ( "char".equals(mytype) )
          myvalue = myvalue.substring(0,1);

        if ( "string".equals(mytype) &&
             ( mysize > 0 ) &&
             (myvalue.length() > mysize))
          myvalue = myvalue.substring(0,mysize);

        if ( "int".equals(mytype) )
          if  ( CommonTools.stringToInt(myvalue)== CommonTools.WRONG_INT )
            myvalue = null;

      }

      if ( item.get("valueConstraintType") == null  )
        myconstype = null;
      else
        myconstype = item.get("valueConstraintType").toString();

      if ( item.get("valueConstraint") == null  )
        mycons = null;
      else
        mycons = item.get("valueConstraint").toString();

      if ( item.get("valueMapType") == null  )
        mymaptype = null;
      else
        mymaptype = item.get("valueMapType").toString();

      if ( item.get("valueMap") == null  )
        mymap = null;
      else
        mymap =  (Hashtable)item.get("valueMap");

      if ( item.get("editControllerType") == null  )
        myetype = "text";
      else
        myetype = item.get("editControllerType").toString();

      String myret = outEditController(inRequest,out,myset,myname,
                                       keys,myvalue,myotype,
                                       mytype,myetype,
                                       myconstype,mycons,mymap,
                                       mysize, myesize,
                                       mytrans,keyCanModify);

      return myret;

    }
    catch (Exception ex) {
      return ex.toString();
    }

  }

  /**
   * 显示一个编辑域控件
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param myset 数据集名
   * @param myname 域名
   * @param keys 数据的关键字
   * @param myvalue 域值
   * @param myotype 操作类型
   * @param mytype 数据类型
   * @param myetype 数据编辑类型
   * @param myconstype 数据值约束类型
   * @param mycons 数据值约束
   * @param mymap 数据值映射
   * @param mysize 数据大小
   * @param myesize 数据编辑宽度
   * @param mytrans 是否翻译定义中的数据值
   * @param keyCanModify 数据的关键字可否修改
   * @return 是否显示成功
   */
  public static String outEditController(HttpServletRequest inRequest,
      PrintWriter out,
      String myset,
      String myname,
      ArrayList keys,
      String myvalue,
      String myotype,
      String mytype,
      String myetype,
      String myconstype,
      String mycons,
      Hashtable  mymap,
      int  mysize,
      int  myesize,
      boolean  mytrans,
      boolean  keyCanModify) {
    try {

      boolean isnew = "add".equals(myotype) ||
                      "copy".equals(myotype);
      boolean issetup = "query_setup".equals(myotype) ||
                      "batch_modify".equals(myotype);

      if ( !"add".equals(myotype) &&
           !"query_setup".equals(myotype) &&
           !"batch_modify".equals(myotype) &&
           !"modify".equals(myotype) &&
           !"copy".equals(myotype) ) {
        if ( myvalue == null ) myvalue = "";
        if ( "password".equals(myconstype) )  return "no";
        out.println("\t\t\t" +
                    DataManagerTools.getValueShow(inRequest,myvalue,myconstype,myset) );
        return "ok-show";
      }
      // 不允许批量修改口令字段和单一关键字
      if ( "batch_modify".equals(myotype)) {
        if ( "password".equals(myconstype) )  return "no";
        if ( keys.contains(myname) &&
             (keys.size() < 2) )
          return "no";
      }
      myvalue = DataManagerTools.getValueEdit(inRequest,myvalue, myset);
      if ( "hidden".equals(myetype) ) {
        OutputController.outHiddenInput(inRequest,out,myname,myvalue);
        return "ok-hidden";
      }

      ArrayList myv;
      String myinput;
      if ( "readonly".equals(myconstype) ||
           (!issetup &&
           !keyCanModify &&
           keys.contains(myname) &&
           !isnew )) {
        OutputController.outReadonlyInput(inRequest,out,myname,myvalue,mymap, myset);
        myinput = "readonly";
        return "ok-" + myinput;
      }
      if ( DataManager.SYSTEM_ROLE_ACL.equals(myset) &&
           "operation".equals(myname)) {
        ArrayList myvv =
            OutputController.outAllOpearationsInput(inRequest,out,myname,myvalue, true);
        if ( myvv != null )
          myinput = "check-" + myvv.size();
        else
          myinput = "operation";
        return "ok-" + myinput;
      }
      if ( DataManager.SYSTEM_ROLE_ACL.equals(myset) &&
           "data".equals(myname)) {
        ArrayList myvv =
            OutputController.outAllDataInput(inRequest,out,myname,myvalue, true);
        if ( myvv != null )
          myinput = "check-" + myvv.size();
        else
          myinput = "data";
        return "ok-" + myinput;
      }
      if ( ( DataManager.DATA_ROLE_ACL.equals(myset)  ||
             DataManager.DATA_USER_ACL.equals(myset) ) &&
             "operation".equals(myname)) {
        ArrayList myvv =
            OutputController.outAllOpearationsInput(inRequest,out,myname,myvalue, false);
        if ( myvv != null )
          myinput = "check-" + myvv.size();
        else
          myinput = "operation";
        return "ok-" + myinput;
      }
      if ( ( DataManager.DATA_ROLE_ACL.equals(myset)  ||
             DataManager.DATA_USER_ACL.equals(myset) ) &&
             "data".equals(myname)) {
        ArrayList myvv =
            OutputController.outAllDataInput(inRequest,out,myname,myvalue, false);
        if ( myvv != null )
          myinput = "check-" + myvv.size();
        else
          myinput = "data";
        out.println("\t\t</TD></TR>");
        return "ok-" + myinput;
      }
      if ( "auto".equals(mytype) &&
           !issetup) {
        if ( isnew ) myvalue = null;
        OutputController.outAutoInput(inRequest,out,myname,
                                      myvalue, true);
        myinput = "auto";
        return "ok-" + myinput;
      }
      if ( "password".equals(myconstype) &&
           !issetup) {
        OutputController.outPasswordInput(inRequest,out,myname,myvalue);
        myinput = "password";
        return "ok-" + myinput;
      }
      if ( "ip".equals(myconstype) &&
           !"query_setup".equals(myotype)) {
        OutputController.outIPInput(inRequest,out,myname,myvalue,
                                    true  );
        myinput = "ip";
        return "ok-" + myinput;
      }
      if ( !"query_setup".equals(myotype) &&
           "link".equals(myconstype) ) {
        OutputController.outLinkInput(inRequest,out,myname,myvalue,myesize);
        myinput = "link";
        return "ok-" + myinput;
      }
      if ( !"query_setup".equals(myotype) &&
           "file".equals(myconstype)  ) {
        OutputController.outFileInput(inRequest,out,myname,myvalue,myset,myesize);
        myinput = "file";
        return "ok-" + myinput;
      }
      if ( "interface_style".equals(myconstype) ) {
        myinput = "radio-" +
                  OutputController.outInterfaceStyleInput(inRequest,
                  out,myname,myvalue);
        return "ok-" + myinput;
      }
      if ( "interface_theme".equals(myconstype) ) {
        myinput = "radio-" +
                  OutputController.outInterfaceThemeInput(inRequest,
                  out,myname,myvalue);
        return "ok-" + myinput;
      }
      if ( "data_audit_types".equals(myconstype) ) {
        myinput = "radio-" +
                  OutputController.outDataAuditTypesInput(inRequest,
                  out,myname,myvalue);
        return "ok-" + myinput;
      }
      if ( "datalist_style".equals(myconstype) ) {
        myinput = "radio-" +
                  OutputController.outDatalistStyleInput(inRequest,
                  out,myname,myvalue);
        return "ok-" + myinput;
      }
      if ( "language".equals(myconstype) ) {
        myinput = "radio-" +
                  OutputController.outLanguageInput(inRequest,
                  out,myname,myvalue);
        return "ok-" + myinput;
      }
      if ( "radio".equals(myetype) &&
           "solo".equals(myconstype) &&
           ( mycons != null ) ) {
        myv = CommonTools.stringToArray(mycons,
                                        DataStructureXmlParser.LIST_SPLITTER);
        OutputController.outRadioInput(inRequest,out,myname,
                                       myvalue,myv,mymap,false,mytrans);
        myinput = "radio-" + myv.size();
        return "ok-" + myinput;
      }
      if ( "check".equals(myetype) &&
           "multi".equals(myconstype) &&
           ( mycons != null ) ) {
        myv = CommonTools.stringToArray(mycons,
                                        DataStructureXmlParser.LIST_SPLITTER);
        OutputController.outCheckInput(inRequest,out,myname,
                                       myvalue,myv,mymap,mytrans);
        myinput = "check-" + myv.size();
        return "ok-" + myinput;
      }
      if ( ("list".equals(myetype) ||
            ("droplist".equals(myetype)) )&&
            ( mycons != null ) &&
            ("solo".equals(myconstype) ||
            "multi".equals(myconstype))) {
        myv = CommonTools.stringToArray(mycons,
                                        DataStructureXmlParser.LIST_SPLITTER);
        OutputController.outListInput(inRequest,out,myname,
                                      myvalue,myv,mymap,
                                      "multi".equals(myconstype),
                                      "droplist".equals(myetype),
                                      false  ,mytrans );
        myinput = myetype + "-" + myv.size();
        return "ok-" + myinput;
      }
      if ( "date".equals(mytype) ) {
        OutputController.outDateInput(inRequest,out,myname,
                                      myvalue,myconstype,
                                      isnew, myesize,
                                      issetup);
        myinput = "date";
        return "ok-" + myinput;
      }
      if ( "time".equals(mytype) ) {
        OutputController.outDateTimeInput(inRequest,out,myname,
            myvalue,myconstype,
            isnew, myesize,issetup);
        myinput = "time";
        return "ok-" + myinput;
      }
      if (  "creatuser".equals(myconstype) ) {
        OutputController.outCreatUserInput(inRequest,out,myname,
            myvalue,isnew, issetup);
        myinput = "creatuser";
        return "ok-" + myinput;
      }
      if (  "currentuser".equals(myconstype) ) {
        OutputController.outCurrentUserInput(inRequest,out,
            myname,issetup);
        myinput = "currentuser";
        return "ok-" + myinput;
      }
      if ( "textarea".equals(myetype) ) {
        OutputController.outTextareaInput(inRequest,out,myname,
            myvalue,mysize, myesize, false);
        myinput = "textarea";
        return "ok-" + myinput;
      }
      if ( "ref_multi".equals(myconstype) &&
           ( mycons != null )  ) {
        OutputController.outRefInput(inRequest,out,myname,myvalue,
                                     mycons, "multi", myset,myesize);
        myinput = "ref_multi";
        return "ok-" + myinput;
      }
      if ( "ref_solo".equals(myconstype) &&
           ( mycons != null )  ) {
        OutputController.outRefInput(inRequest,out,myname,myvalue,
                                     mycons,"solo", myset,myesize);
        myinput = "ref_solo";
        return "ok-" + myinput;
      }

      OutputController.outTextInput(inRequest,out,myname,
                                    myvalue,mysize,myesize);
      if ( "int".equals(mytype) ||
           "float".equals(mytype) )
        myinput = "number";
      else
        myinput = "text";
      return "ok-" + myinput;
    }
    catch (Exception ex) {
      return ex.toString();
    }
  }

}