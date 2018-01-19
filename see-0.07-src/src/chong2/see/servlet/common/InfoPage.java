package  chong2.see.servlet.common;

import chong2.see.utils.*;
import chong2.see.data.DataReader;
import chong2.see.xml.DataXmlWriter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * <p>Title: ������̹�����</p>
 * <p>Description: Ϊ����з��ṩ�������ݺ������ƽ̨</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: ���</p>
 *
 * <p>��ʾ�ɹ���Ϣ�������Ϣ��ҳ�档
 *
 * @author ����
 * @version 0.07
 */

final public class InfoPage
    extends HttpServletType1 {

  /**
   * ҳ�����ط���
   * @param inRequest ��ǰ����
   * @param inResponse ��ǰ��Ӧ
   * @throws IOException IO�쳣
   * @throws ServletException servlet�쳣
   */
  public void processRequest(HttpServletRequest inRequest,
                             HttpServletResponse inResponse)
      throws IOException, ServletException {

    try{

      PrintWriter out = inResponse.getWriter();
      inRequest.removeAttribute(UploadTools.UPLOAD_NAME);

      String mytype = ServletTools.getParameter(inRequest,"mytype") ;
      String myinfo = ServletTools.getParameter(inRequest,"myinfo") ;
      String mydes = ServletTools.getParameter(inRequest,"mydes") ;
      String mybutton = ServletTools.getParameter(inRequest,"mybutton") ;
      String mytitle = ServletTools.getParameter(inRequest,"mytitle") ;
      String myimg = ServletTools.getParameter(inRequest,"myimg") ;

      if ( ( mytitle == null ) || ( "".equals(mytitle) ) )
        mytitle = "information" ;
      String myttitle = mytitle;
      if ("see_hello".equals(mytitle)) {
        mytitle = LanguageTools.tr(inRequest,"hello",mydes);
        mydes = "data_manager_guide";
        int mypm = ServletTools.getNewPMSize(inRequest);
        if ( mypm > 0 )
          myinfo = LanguageTools.tr(inRequest,
                                    "pm_size_info",mypm + "");
      }

      PageTools.outHeader( inRequest, out, mytitle ,0);
      if ( "error".equals(mytype)) {
        out.println("<style type=\"text/css\">");
        out.println("BODY    {   background-color: #ffeeee;");
        out.println("font-size: 9pt");
        out.println("}</STYLE>");
      } else {
        out.println("<style type=\"text/css\">");
        out.println("BODY    {   background-color: #eeffee;");
        out.println("font-size: 9pt");
        out.println("}</STYLE>");
      }



      if ( (myinfo != null) && !("".equals(myinfo) )  ) {
        out.println("\t<P><B>" + tr(inRequest,myinfo) + "</B></P>");
      }

      if ( (mydes != null) && !("".equals(mydes) )  ) {
        out.println("\t<P>" + tr(inRequest,mydes) + "</P>");
      }

      boolean showimg = true;
      if (  AclTools.isSuperseer(inRequest) ) {

        if ( DataManager.UPLOAD_MANAGEMENT.equals(mytitle) ) {
          outUploadInformation(inRequest,out);
          showimg =false;
        }

        if ( DataManager.DATA_SOURCE.equals(mytitle) ){
          outDataSourceInformation(inRequest,out);
          showimg =false;
        }

      }

      if ( showimg ) {
        String mystyle =
            ServletTools.setSessionInterfaceStyle(inRequest);
        String myimgf = myttitle;
        if ( (myimg != null) && !("".equals(myimg) )  )
          myimgf = myimg;
        myimgf = "image/" + mystyle + "/" +
                 myimgf +".gif";
        boolean hasimg = ServletTools.isFile(inRequest,myimgf);
        if ( !hasimg &&
             (mybutton != null) &&
             ( mybutton.startsWith("see_menu_level_")) ){
          myimgf = "image/" + mystyle + "/big_" +
                   mybutton.substring("see_menu_level_".length(),
                   mybutton.length()) +
                   ".gif";
          hasimg = ServletTools.isFile(inRequest,myimgf);
        }
        if ( hasimg )
          out.println("<P align=center><IMG src=\"" +
                      myimgf + "\" align=center></P>");
      }

      out.println("<P align=center>");
      if ( "back".equals(mybutton)  )
        OutputController.outBackBar(inRequest,out);
      if ( "close".equals(mybutton)  )
        OutputController.outCloseBar(inRequest,out);
      out.println("</P>");

      PageTools.outFooter(inRequest, out);

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * �����ǰ���ϴ�����ֵ
   * @param inRequest ��ǰ����
   * @param out ��ǰ���
   */
  public static void  outUploadInformation(HttpServletRequest inRequest,
      PrintWriter out) {
    out.println("\t<TABLE class=data cellPadding=2 align=center>");
    out.println("\t\t<TR>");
    out.print("\t\t<TD align=right>" +
              tr(inRequest,"default_upload_size") +
              " : </TD>");
    out.print("\t\t<TD align=left>" +
              ServletTools.getAppDefaultUploadSize(inRequest) +
              "</TD>");
    out.println("\t\t</TR>");
    out.println("\t\t<TR>");
    out.print("\t\t<TD align=right>" +
              tr(inRequest,"default_upload_total") +
              " : </TD>");
    out.print("\t\t<TD align=left>" +
              ServletTools.getAppDefaultUploadTotal(inRequest) +
              "</TD>");
    out.println("\t\t</TR>");
    out.println("\t\t<TR>");
    out.print("\t\t<TD align=right>" +
              tr(inRequest,"current_upload_left") +
              " : </TD>");
    out.print("\t\t<TD align=left>" +
              (UploadTools.getUploadLeftSpace(inRequest)/1000)+
              "</TD>");
    out.println("\t\t</TR>");
    out.println("\t </TABLE>");
  }

  public static void  outDataSourceInformation(HttpServletRequest inRequest,
      PrintWriter out) {
    out.println("\t<TABLE class=data cellPadding=2 align=center>");
    out.println("\t\t<TR>");
    out.print("\t\t<TD align=right>" +
              tr(inRequest,"current") +
              " : </TD>");
    out.print("\t\t<TD align=left>" +
              ServletTools.getAppDataSourceType(inRequest) +
              "</TD>");
    out.println("\t\t</TR>");
    out.println("\t\t<TR>");
    out.println("\t </TABLE>");
  }

}