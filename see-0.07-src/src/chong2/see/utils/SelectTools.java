package chong2.see.utils;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>Title: ������̹�����</p>
 * <p>Description: Ϊ����з��ṩ�������ݺ������ƽ̨</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: ���</p>
 *
 * ��ѡ��������صľ�̬����
 *
 * @author ����
 * @version 0.07
 */

final public class SelectTools {

  /**
   * ѡ����
   * @param inRequest ��ǰ����
   * @param out ��ǰ���
   * @param myset ���ݼ���
   */
  public static void  outSelectFrame(HttpServletRequest inRequest,
                                     PrintWriter out,
                                     String myset) {
    PageTools.outHeader(inRequest, out,"",-1);
    if ("solo".equals(ServletTools.getParameter(inRequest,"myref") ))
      out.println("<FRAMESET border=5 frameBorder=1 frameSpacing=0 rows=*,60>");
    else
      out.println("<FRAMESET border=5 frameBorder=1 frameSpacing=0 rows=*,120>");
    out.print("\t<FRAME frameBorder=1 marginHeight=1 marginWidth=1 " +
              "name=data " +
              "src=\"" + inRequest.getRequestURI() +
              "?myframe=data&myset=" + myset +
              "&myoperation=select&mybutton=select");
    if ( ServletTools.getParameter(inRequest,"myref") != null)
      out.print("&myref=" + ServletTools.getParameter(inRequest,"myref"));
    if ( ServletTools.getParameter(inRequest,"mywhere") != null)
      out.print("&mywhere=" + ServletTools.getParameter(inRequest,"mywhere"));
    out.println("\" noresize=no>");
    out.println("\t<FRAME frameBorder=1 marginHeight=1 marginWidth=1 " +
                "name=getdata  " +
                "src=\"" + inRequest.getRequestURI() +
                "?myframe=getdata&myset=" + myset );
    if ( ServletTools.getParameter(inRequest,"myref") != null)
      out.print("&myref=" + ServletTools.getParameter(inRequest,"myref"));
    out.println("\" noresize=no>");
    out.println("\t</FRAME>");
    out.println("</FRAMESET>");
    out.println("</HTML>");
  }

  /**
   * ѡ��ťҳ��
   * @param inRequest ��ǰ����
   * @param out ��ǰ���
   */
  public static void  outSelectBarPage(HttpServletRequest inRequest,
                                       PrintWriter out) {
    PageTools.outHeader(inRequest, out,"",-1);
    out.println("\t<SCRIPT LANGUAGE='javascript'>\n");
    out.println("\tfunction see_select_all() {");
    out.println("\t\t document.seeform.myget.value = \"*\";");
    out.println("\t\t document.seeform.myshow.value = \"*\";");
    out.println("\t}");
    out.println("\tfunction see_ok() {");
    out.println("\t\t top.geter.value = document.seeform.myget.value;");
    out.println("\t\t top.shower.value = document.seeform.myshow.value;");
    out.println("\t\t top.close();");
    out.println("\t}");
    out.println("\t</SCRIPT>");
    boolean issolo =
       "solo".equals(ServletTools.getParameter(inRequest,"myref"));
    out.println("\t<FORM name=seeform method=post " +
                " onSubmit='return false;'>");
    out.println("\t<INPUT name=myget type=hidden>");
    if ( issolo )
      out.println("\t<INPUT name=myshow readonly><BR>");
    else
      out.println("\t<TEXTAREA name=myshow" +
                  " cols=" + OutputController.MAX_INPUT_COLS +
                  " rows=4" +
                  " readonly></TEXTAREA><BR>");
    OutputController.outSaveResetBar(inRequest,out,"seeform","ok","close");
    if ( !issolo )
      OutputController.outButton(inRequest,out,"select_all");
    out.println("\t</FORM>");
    PageTools.outFooter(inRequest, out,1);
  }

}