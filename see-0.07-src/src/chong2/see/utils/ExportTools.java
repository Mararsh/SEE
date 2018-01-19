package chong2.see.utils;

import chong2.see.data.base.Language;
import chong2.see.servlet.common.DataManager;
import chong2.see.xml.DataStructureXmlParser;

import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>Title: 软件工程管理环境</p>
 * <p>Description: 为软件研发提供管理数据和任务的平台</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: 虫二</p>
 *
 * 与数据导出相关的静态方法
 *
 * @author 玛瑞
 * @version 0.07
 */

final public class ExportTools {

  /**
   * 导出页面框架
   * @param inRequest 当前请求
   * @param inResponse 当前响应
   * @param out 当前输出
   * @param myset 数据集名
   */
  public static void outExportSetupFrame(HttpServletRequest inRequest,
      HttpServletResponse inResponse,
      PrintWriter out,
      String myset) {

    DataStructureXmlParser mystrucReader =
        DataManagerTools.getDataStructureReader(inRequest,myset) ;
    if ( mystrucReader == null )
      return ;

    boolean ismatrix = "matrix".equals(mystrucReader.getType()) ;

    String myformat =
        ServletTools.getParameter(inRequest,"input_myexportformat");
    if ( "xml".equals(myformat) ) {
      String ret= OutputListTools.outList(inRequest,out,mystrucReader,myset,"xml");
      if ( ret == null ) {
        PageTools.outErrorPage(inRequest,inResponse,"failure",
                               null, null,"close");
        return;
      }
      PageTools.outHeader(inRequest, out,myset,"export",null,null,0);
      out.println(LanguageTools.tr(inRequest,"tmp_file_warning",
                                   CommonTools.intToString(DataManager.TMP_FILE_STAY_MINUTES)) +
                                   "<BR>");
      OutputController.outButton(inRequest,out,"open",ret);
      PageTools.outFooter(inRequest, out,0);
      return;
    }
    if ( "html".equals(myformat) ) {
      String ret = OutputListTools.outList(inRequest,out,mystrucReader,myset,"html");
      PageTools.outFooter(inRequest, out,0);
      return;
    }
    if ( "txt".equals(myformat) ) {
      String ret = OutputListTools.outList(inRequest,out,mystrucReader,myset,"txt");
      if ( ret == null ) {
        PageTools.outErrorPage(inRequest,inResponse,"failure",
                               null, null,"close");
        return;
      }
      PageTools.outHeader(inRequest, out,myset,"export",null,null,0);
      out.println(LanguageTools.tr(inRequest,"tmp_file_warning",
                                   CommonTools.intToString(DataManager.TMP_FILE_STAY_MINUTES)) +
                                   "<BR>");
      OutputController.outButton(inRequest,out,"open",ret);
      PageTools.outFooter(inRequest, out,0);
      return;
    }

    DataManager.initOutput(inRequest,out,mystrucReader,"export");

    out.println("<P class=info>" +
                LanguageTools.tr(inRequest,"export_info") +
                "</P>");
    out.println("<TABLE  border=1 class=data width=100%>");
    out.println("<TR class=tableheader><TH colspan=2 align=center>" +
                LanguageTools.tr(inRequest,"format") + "</TH></TR>");
    out.println("<TR class=tabledata>");
    out.println("<TD align=left>");
    if ( !DataManager.EXPORT_FORMAT.contains(myformat) )
      myformat = "txt";
    OutputController.outRadioInput(inRequest,out,"myexportformat",
                                   myformat,DataManager.EXPORT_FORMAT,
                                   null,  false, false);
    out.println("</TD></TR>");
    out.println("</TABLE><BR>");

    out.println("<TABLE  border=1 class=data width=100%>");
    out.println("<TR class=tableheader><TH colspan=2 align=center>" +
                LanguageTools.tr(inRequest,"data") + "</TH></TR>");
    out.println("<TR class=tabledata>");
    out.println("<TD align=left>");
    OutputController.outRadioInput(inRequest,out,"myexportdata","all",
                                   DataManager.EXPORT_DATA, null,  false, false);
    out.println("</TD></TR>");
    out.println("</TABLE><BR>");

    out.println("<TABLE  border=1 class=data width=100%>");
    out.println("<TR class=tableheader><TH colspan=2 align=center>" +
                LanguageTools.tr(inRequest,"items") + "</TH></TR>");
    out.println("<TR class=tabledata>");
    out.println("<TD align=left>");
    OutputController.outRadioInput(inRequest,out,"myexportitem","current",
                                   DataManager.EXPORT_DATA, null,  false, false);
    out.println("</TD></TR>");
    out.println("</TABLE><BR>");

    out.println("<TABLE  border=1 class=data width=100%>");
    out.println("<TR class=tableheader><TH colspan=2 align=center>" +
                LanguageTools.tr(inRequest,"charset") + "</TH></TR>");
    out.println("<TR class=tabledata>");
    out.println("<TD align=left>");
    String mycharset =
        Language.getStandardCharset(ServletTools.getAppDefaultEncoding(inRequest));
    ArrayList mysets = Language.getStandardCharsets();
//    ArrayList mysets = new ArrayList();
//    mysets.add(mycharset);
    mysets.add("UTF-8");
    OutputController.outRadioInput(inRequest,out,"mycharset",
                                   mycharset, mysets,
                                   null,  false, false);
    out.println("</TD></TR>");
    out.println("</TABLE><BR>");

    out.println("<BR><P align=center>");
    OutputController.outSaveResetBar(inRequest,out,"seeform","ok","close");
    out.println("</FORM>");

    out.println("\t<SCRIPT LANGUAGE='javascript'>\n");
    out.println("\tfunction see_ok() {");
    out.println("\t\t document.seeform.target = \"_blank\" ;");
    out.println("\t\t document.seeform.submit();");
    out.println("\t}");
    out.println("\t</SCRIPT>");

    PageTools.outFooter(inRequest, out,0);
  }


}