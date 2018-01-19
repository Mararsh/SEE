package chong2.see.utils;

import chong2.see.data.base.Constants;
import chong2.see.data.base.Language;
import chong2.see.data.*;
import chong2.see.servlet.common.DataManager;
import chong2.see.xml.DataStructureXmlParser;
import chong2.see.data.DataReader;
import chong2.see.xml.DataXmlWriter;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>Title: 软件工程管理环境</p>
 * <p>Description: 为软件研发提供管理数据和任务的平台</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: 虫二</p>
 *
 * 与数据导入相关的静态方法
 *
 * @author 玛瑞
 * @version 0.07
 */

final public class ImportTools {

  /**
   * 导入页面框架
   * @param inRequest 当前请求
   * @param inResponse 当前响应
   * @param out 当前输出
   * @param mystrucReader 数据集结构
   * @param myset 数据集名
   * @param mybutton 当前执行按钮
   */
  public static void outImportSetupFrame(HttpServletRequest inRequest,
      HttpServletResponse inResponse,
      PrintWriter out,
      DataStructureXmlParser mystrucReader,
      String myset,
      String mybutton) {

    DataManager.initOutput(inRequest,out,mystrucReader,"import");

    if ( mystrucReader == null ) {
      PageTools.outErrorPage(inRequest,inResponse,
                             null, "invalid_data",
                             "","");
      return;
    }
    ArrayList itemnames = mystrucReader.getEditItemNames();
    if ( itemnames == null ) {
      PageTools.outErrorPage(inRequest,inResponse,
                             null, "invalid_data",
                             "","");
      return;
    }
    ArrayList notnulls = mystrucReader.getNotNullNames();
    ArrayList keys = mystrucReader.getKeys();
    ArrayList mypass = mystrucReader.getPasswordItems();

    if ( "ok".equals(mybutton)  ) {
      dealImport(inRequest,inResponse,out,mystrucReader,
                 myset, itemnames, notnulls, keys, mypass);
      return ;
    }

    out.println("\t <P class=info>" +
                LanguageTools.tr(inRequest,"import_info") +
                "</P>");
    out.println("\t <TABLE  class=data >");
    if ( mystrucReader.hasMatrixDefine()) {
      out.println("\t\t<TR class=tableline><TD>");
      OutputController.outRadioInput(inRequest,out, "importdata",
                                     "main_data",
                                     CommonTools.stringToArray("main_data,matrix_data",","),
                                     null, false, false);
      out.println("\t\t</TD></TR>");
    }
    out.println("\t\t<TR class=tableline><TD>");
    out.println(LanguageTools.tr(inRequest,"file") + " : " );
    out.println("\t\t\t<INPUT type=file name=input_importfile " +
                " size=" +  OutputController.MAX_INPUT_SIZE + ">");
    out.println("\t\t\t<BR><FONT class=info>" +
                LanguageTools.tr(inRequest,"filesize_info",
                ServletTools.getAppDefaultUploadSize(inRequest) + "") +
                "</FONT>");
    out.println("\t\t </TD></TR>");
    out.println("\t\t<TR class=tableline><TD>");
    out.println(LanguageTools.tr(inRequest,"import_override") + " : " );
    OutputController.outRadioInput(inRequest,out, "importoverride",
                                   "no",
                                   CommonTools.stringToArray("yes,no",","),
                                   null, false, false);
    out.println("\t\t</TD></TR>");
    out.println("\t </TABLE><BR>");

    out.println("\t <BR><P align=center>");
    OutputController.outSaveResetBar(inRequest,out,"seeform","ok",null);
    out.println("</FORM>");

    out.println("\t<SCRIPT LANGUAGE='javascript'>\n");
    out.println("\tfunction see_ok() {");
    out.println("\t\t if ( document.seeform.input_importfile.value.length < 1 ) {");
    out.println("\t\t\t\t alert(\"" + LanguageTools.tr(inRequest,"file") +
                LanguageTools.tr(inRequest,"should_not_null") + "\");");
    out.println("\t\t\t return -1;");
    out.println("\t\t }");
    out.println("\t\t document.seeform.mybutton.value = \"ok\";");
    out.println("\t\t document.seeform.submit();");
    out.println("\t}");
    out.println("\t</SCRIPT>");

    PageTools.outFooter(inRequest, out,0);

    return;
  }

  /**
   * 导入数据
   * @param inRequest 当前请求
   * @param inResponse 当前响应
   * @param out 当前输出
   * @param mystrucReader 数据集结构
   * @param myset 数据集名
   * @param itemnames 数据项
   * @param notnulls 数据非空项
   * @param keys 数据关键字
   * @param mypass 数据口令项
   */
  public static void dealImport(HttpServletRequest inRequest,
                                HttpServletResponse inResponse,
                                PrintWriter out,
                                DataStructureXmlParser mystrucReader,
                                String myset,
                                ArrayList itemnames,
                                ArrayList notnulls,
                                ArrayList keys,
                                ArrayList mypass) {

    String myfile =
        ServletTools.getParameter(inRequest,"input_importfile");
    if ( myfile == null ) {
      PageTools.outErrorPage(inRequest,inResponse,
                             null, "invalid_data",
                             "","back");
      return;
    }

    String myoverride =
        ServletTools.getParameter(inRequest,"input_importoverride");
    boolean ismatrix =
        "matrix_data".equals(ServletTools.getParameter(inRequest,"input_importdata"));

    DataReader mydataReader =
        DataReaderGetor.dataReader("xml",myfile);
    mydataReader.startReading();
    String myget;
    if ( ismatrix ) {
      myget = myset + MatrixTools.MATRIX_FILE_SUFFIX ;
      itemnames =  mystrucReader.getAllMatrixItemNames();
      keys.add(MatrixTools.MATRIX_VALUE);
      notnulls = mystrucReader.getNotNullMatrixNames();
    } else {
      myget = myset;
    }

    ArrayList mydata =
        mydataReader.getValidRecords(itemnames,notnulls,keys);
    UploadTools.removeUploadFile(inRequest,myfile);
    if ( mydata == null ) {
      PageTools.outErrorPage(inRequest,inResponse,
                             null, "invalid_data",
                             "","back");
      return;
    }

    DataReader myolddataReader =
        DataManagerTools.getDataReader( inRequest,
        mystrucReader,myget, null,null,1,-1);
    if ( myolddataReader == null ) {
      PageTools.outErrorPage(inRequest,inResponse,
                             null, "invalid_data",
                             "","back");
      return;
    }
    ArrayList myolddata =
        myolddataReader.getValidRecords(itemnames,notnulls,keys);
    if ( myolddata == null )
      myolddata = new ArrayList();

    boolean  isover = "yes".equals(myoverride), isnew;
    Hashtable myrecord , myold = null , mykey, myorec;
    int addc = 0, overc = 0, myindex;
    for ( int i=0; i< mydata.size(); i++) {
      myrecord = (Hashtable)mydata.get(i);
      mykey = DataReaderGetor.getRecordKey(keys,myrecord);
      isnew = true;
      myindex = -1;
      for ( int j=0; j< myolddata.size(); j++){
        myorec = (Hashtable)myolddata.get(j);
        if ( CommonTools.isValidHash(myorec, mykey,
                                     mystrucReader.getItemTypes(),
                                     ServletTools.getAppDefaultEncoding(inRequest)) ) {
          isnew = false;
          myindex = j;
          break;
        }
      }
      if ( isnew ) {
        myolddata.add(myrecord);
        addc++;
      } else if ( isover ) {
        myolddata.set(myindex, myrecord);
        overc++;
      }
    }

    String retw;
    String fdata =
        DataManagerTools.getDataValuesFile(inRequest,myget);
    DataXmlWriter myw = new DataXmlWriter();
    retw = myw.writeData(myolddata,fdata,
                         ServletTools.getAppDefaultCharset(inRequest));
    if ( !Constants.SUCCESSFUL.equals(retw)) {
      PageTools.outErrorPage(inRequest,inResponse,
                             null, retw,
                             "","back");
      return;
    }

    ArrayList myaudit =
        ServletTools.getAppDataAuditTypes(inRequest);
    Hashtable myadd = new Hashtable();
    myadd.put("add",addc + "");
    Hashtable myover = new Hashtable();
    myover.put("override",overc + "");
    if ( myaudit.contains(LanguageTools.atr(inRequest,"import"))) {
      AuditTools.addAudit(inRequest,myset,"import",
                          null,myover ,  myadd , retw,null , mypass);
    }

    if ( Language.getEncodingList().contains(myset) )
      LanguageTools.setUserLangauge(inRequest, myset);

    out.println("<style type=\"text/css\">");
    out.println("BODY    {   background-color: #eeffee;");
    out.println("font-size: 9pt");
    out.println("}</STYLE>");
    out.println("\t<P align=center>" +
                LanguageTools.tr(inRequest, Constants.SUCCESSFUL) + "</P>");
    out.println("\t<TABLE class=data cellPadding=2 align=center>");
    out.println("\t\t<TR>");
    out.print("\t\t<TD align=right>" +
              LanguageTools.tr(inRequest,"valid") + " : </TD>");
    out.print("\t\t<TD align=left>" + mydata.size() + "</TD>");
    out.println("\t\t</TR>");
    out.println("\t\t<TR>");
    out.print("\t\t<TD align=right>" +
              LanguageTools.tr(inRequest,"add") + " : </TD>");
    out.print("\t\t<TD align=left>" + addc + "</TD>");
    out.println("\t\t</TR>");
    out.println("\t\t<TR>");
    out.print("\t\t<TD align=right>" +
              LanguageTools.tr(inRequest,"override") + " : </TD>");
    out.print("\t\t<TD align=left>" + overc + "</TD>");
    out.println("\t\t</TR>");
    out.println("\t </TABLE>");

    PageTools.outFooter(inRequest, out,0);

  }

}