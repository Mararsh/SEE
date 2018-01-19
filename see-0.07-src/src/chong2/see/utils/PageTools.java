package chong2.see.utils;

import chong2.see.data.base.Constants;
import chong2.see.servlet.common.DataManager;

import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>Title: ������̹�����</p>
 * <p>Description: Ϊ����з��ṩ�������ݺ������ƽ̨</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: ���</p>
 *
 * �����ҳ����صľ�̬����
 *
 * @author ����
 * @version 0.07
 */

final public class PageTools {

  /**
   * ��Ϣ���ҳ�����Ե�ַ
   */
  public static String INFO_PAGE =  "info-page";
  /**
   * ��ʾ���Ŀ¼����Ե�ַ
   */
  public static String STYLE_PATH =  "style/";

  /**
   * �ض���ҳ�档���ַ�ʽ�����Ὣ��ǰҳ��Ĳ���������ҳ���С�
   * @param myResponse ��ǰ��Ӧ
   * @param inPage Ҫת����ҳ��
   */
  public static void redirectPage(HttpServletResponse myResponse,
                                  String inPage) {
    try{
      myResponse.sendRedirect(myResponse.encodeRedirectURL(inPage));
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * �ض���ҳ�档���ַ�ʽ������ѵ�ǰҳ��Ĳ���������ҳ���С�
   * @param myRequest ��ǰ����
   * @param myResponse ��ǰ��Ӧ
   * @param inPage Ҫת����ҳ��
   */
  public static void forwardPage(HttpServletRequest myRequest,
                                 HttpServletResponse myResponse,
                                 String inPage) {
    try{

      ServletContext myContext =
          myRequest.getSession(true).getServletContext();

      RequestDispatcher myDispatcher =
          myContext.getRequestDispatcher(inPage);
      myDispatcher.forward(myRequest, myResponse);

    } catch (Exception ex) {
      ex.printStackTrace();
    }

  }

  /**
   * ˢ�µ�ǰ����
   * @param myOut ��ǰ���
   */
  public static void refreshWindow(PrintWriter myOut) {
    try{
      myOut.println("\t<SCRIPT LANGUAGE='javascript'>\n");
//      myOut.println("\t top.location = \"index\" ;\n");
      myOut.println("\t top.location.reload() ;\n");
      myOut.println("\t</SCRIPT>\n");
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * ˢ��ָ��ҳ��Ϊ�µ�����
   * @param myOut ��ǰ���
   * @param myTarget Ҫˢ�µ�frame��
   */
  public static void reloadFrame(PrintWriter myOut,
                                  String myTarget) {
    try{
      myOut.println("\t<SCRIPT LANGUAGE='javascript'>\n");
      myOut.println("\t parent." + myTarget + ".location.reload();\n");
      myOut.println("\t</SCRIPT>\n");
    } catch (Exception ex) {
      ex.printStackTrace();
    }

  }

  /**
   * ˢ��ָ��ҳ��Ϊ�µ�����
   * @param myOut ��ǰ���
   * @param myTarget Ҫˢ�µ�frame��
   * @param myUrl Ҫˢ��ҳ���URL����
   */
  public static void refreshFrame(PrintWriter myOut,
                                  String myTarget,
                                  String myUrl) {
    try{
      myOut.println("\t<SCRIPT LANGUAGE='javascript'>\n");
      myOut.println("\t parent." + myTarget + ".location = \"" +
                    myUrl + "\" ;\n");
      myOut.println("\t</SCRIPT>\n");
    } catch (Exception ex) {
      ex.printStackTrace();
    }

  }

  /**
   * ˢ�µ�ǰҳ��Ϊ�µ�����
   * @param myOut ��ǰ���
   * @param myUrl Ҫˢ��ҳ���URL����
   */
  public static void refreshFrame(PrintWriter myOut,
                                  String myUrl) {
    try{
      myOut.println("\t<SCRIPT LANGUAGE='javascript'>\n");
      myOut.println("\t location = \"" +
                    myUrl + "\" ;\n");
      myOut.println("\t</SCRIPT>\n");
    } catch (Exception ex) {
      ex.printStackTrace();
    }

  }

  /**
   * ���������Ϣ��ʾҳ��
   * @param myRequest ��ǰ����
   * @param myResponse ��ǰ��Ӧ
   * @param myinfo Ҫ��ʾ�Ĵ�����Ϣ
   */
  public static void outErrorPage(HttpServletRequest myRequest,
                                  HttpServletResponse myResponse,
                                  String myinfo) {
    outErrorPage(myRequest,myResponse,null,myinfo,null,null);
  }

  /**
   * ���������Ϣ��ʾҳ��
   * @param myRequest ��ǰ����
   * @param myResponse ��ǰ��Ӧ
   * @param myinfo Ҫ��ʾ�Ĵ�����Ϣ
   * @param mydes Ҫ��ʾ�Ĵ�����Ϣ�Ľ�һ������
   */
  public static void outErrorPage(HttpServletRequest myRequest,
                                  HttpServletResponse myResponse,
                                  String myinfo,
                                  String mydes) {
    outErrorPage(myRequest,myResponse,null,myinfo,mydes,null);
  }

  /**
   * ���������Ϣ��ʾҳ��
   * @param myRequest ��ǰ����
   * @param myResponse ��ǰ��Ӧ
   * @param mytitle Ҫ��ʾ�Ĵ�����Ϣ�ı���
   * @param myinfo Ҫ��ʾ�Ĵ�����Ϣ
   * @param mydes Ҫ��ʾ�Ĵ�����Ϣ�Ľ�һ������
   * @param mybutton ҳ���ϵİ�ť��
   */
  public static void outErrorPage(HttpServletRequest myRequest,
                                  HttpServletResponse myResponse,
                                  String mytitle,
                                  String myinfo,
                                  String mydes,
                                  String  mybutton) {
    try{
      if ( (mytitle == null) || "".equals(mytitle) )
        mytitle = "error";
      if ( myinfo == null )  myinfo = "";
      if ( mydes == null )  mydes = "";
      if ( mybutton == null ) mybutton = "back";
      String mypage = INFO_PAGE +
                      "?mytype=error&myinfo=" +
                      myinfo + "&mydes=" + mydes +
                      "&mytitle=" + mytitle +
                      "&mybutton=" + mybutton;
      redirectPage(myResponse,mypage);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * �����Ϣ��ʾҳ��
   * @param myRequest ��ǰ����
   * @param myResponse ��ǰ��Ӧ
   * @param myinfo Ҫ��ʾ����Ϣ
   * @param mydes Ҫ��ʾ����Ϣ�Ľ�һ������
   */
  public static void outInfoPage(HttpServletRequest myRequest,
                                 HttpServletResponse myResponse,
                                 String myinfo,
                                 String mydes) {
    outInfoPage(myRequest,myResponse,null,myinfo,mydes,null);
  }

  /**
   * �����Ϣ��ʾҳ��
   * @param myRequest ��ǰ����
   * @param myResponse ��ǰ��Ӧ
   * @param mytitle Ҫ��ʾ����Ϣ�ı���
   * @param myinfo Ҫ��ʾ����Ϣ
   * @param mydes Ҫ��ʾ����Ϣ�Ľ�һ������
   * @param mybutton ҳ���ϵİ�ť����ȱʡΪ�����ء���
   */
  public static void outInfoPage(HttpServletRequest myRequest,
                                 HttpServletResponse myResponse,
                                 String mytitle,
                                 String myinfo,
                                 String mydes,
                                 String  mybutton) {
    try{
      if ( (mytitle == null) || "".equals(mytitle) )
        mytitle = "information";
      if ( myinfo == null )  myinfo = "";
      if ( mydes == null )  mydes = "";
      if ( mybutton == null ) mybutton = "back";
      String mypage =  "/" + INFO_PAGE +
                       "?mytype=information&myinfo=" +
                       myinfo + "&mydes=" + mydes +
                       "&mytitle=" + mytitle +
                       "&mybutton=" + mybutton;
      forwardPage(myRequest,myResponse,mypage);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * ����ִ�гɹ��Ĵ���
   * @param out ��ǰ���
   * @param myinfo Ҫ��ʾ����Ϣ
   * @param mydes ��ʾ��Ϣ�Ľ�һ������
   */
  public static void outSuccessWindow(PrintWriter out,
                                      String myinfo,
                                      String mydes) {
    if ( myinfo == null )  myinfo = "";
    if ( mydes == null )  mydes = "";
    out.println("\t<SCRIPT LANGUAGE='javascript'>\n");
    out.println("\t window.open(\"" +
                INFO_PAGE +
                "?mytype=information&myinfo=" + myinfo +
                "&mydes=" + mydes +
                "&mytitle=successful" +
                "&mybutton=close" +
                "\" , \"\", " +
                " \"toolbar=no,menubar=no,resizable=yes,scrollbars=yes,width=400,height=450\");");
    out.println("\t</SCRIPT>");
  }

  /**
   * ����ִ��ʧ�ܵĴ���
   * @param out ��ǰ���
   * @param myinfo Ҫ��ʾ����Ϣ
   * @param mydes ��ʾ��Ϣ�Ľ�һ������
   */
  public static void outErrorWindow(PrintWriter out,
                                    String myinfo,
                                    String mydes) {
    if ( myinfo == null )  myinfo = "";
    if ( mydes == null )  mydes = "";
    out.println("\t<SCRIPT LANGUAGE='javascript'>\n");
    out.println("\t window.open(\"" +
                INFO_PAGE +
                "?mytype=error&myinfo=" + myinfo +
                "&mydes=" + mydes +
                "&mytitle=error" +
                "&mybutton=close" +
                "\" , \"\", " +
                " \"toolbar=no,menubar=no,resizable=yes,scrollbars=yes,width=400,height=450\");");
    out.println("\t</SCRIPT>");
  }

  /**
   * ʹ��ǰҳ�淵����һҳ
   * @param out ��ǰ���
   */
  public static void outBackPage(PrintWriter out) {
    out.println("\t<SCRIPT LANGUAGE='javascript'>\n");
    out.println("\t window.history.back();");
    out.println("\t</SCRIPT>");
  }

  /**
   * �����ҳ��ͷ��Ϣ������ʾ��ͷ
   * @param myRequest ��ǰ����
   * @param myOut ��ǰ���
   * @param inTitle ��ҳ�ı���
   */
  public static void outHeader(HttpServletRequest myRequest,
                               PrintWriter myOut,String inTitle) {
    outHeader(myRequest, myOut, inTitle, "", "","",0);
  }

  /**
   * �����ҳ��ͷ��Ϣ
   * @param myRequest ��ǰ����
   * @param myOut ��ǰ���
   * @param inTitle ��ҳ�ı���
   * @param showType ��ʾ������
   */
  public static void outHeader(HttpServletRequest myRequest,
                               PrintWriter myOut,String inTitle,
                               int  showType) {
    outHeader(myRequest, myOut, inTitle, "", "","", showType);
  }

  /**
   * �����ҳ��ͷ��Ϣ
   * @param myRequest ��ǰ����
   * @param myOut ��ǰ���
   * @param inFunction ��ǰ����
   * @param inOperation ��ǰ����
   * @param inSubOperation ��ǰ�Ӳ���
   * @param inDesciption ����
   * @param showType ��ʾ���͡�
   * 0����ʾ��ͷ��1������ʾ��ͷ��2����ʼ�������������������bodyҳǩ
   */
  public static void outHeader(HttpServletRequest myRequest,
                               PrintWriter myOut,
                               String inFunction,
                               String inOperation,
                               String inSubOperation,
                               String inDesciption,
                               int  showType) {

    try{

      String myenc = ServletTools.getSessionEncoding(myRequest);

      String mytitle = inFunction;
      if (  (inFunction == null ) ||  "".equals(inFunction) )  {
        mytitle = "information";
      }
      mytitle = LanguageTools.tr(myRequest,mytitle);

      if (  (inSubOperation != null) &&
            !"".equals(inSubOperation) )  {
        mytitle = mytitle + " - " +
                  LanguageTools.tr(myRequest,inSubOperation);
      } else if (  (inOperation != null) &&
              !"".equals(inOperation) )  {
          mytitle = mytitle + " - " +
                    LanguageTools.tr(myRequest,inOperation);
        }

      myOut.print("<!-- ");
      myOut.print(LanguageTools.tr(myRequest,"chong2see") );
      myOut.print("\t " + LanguageTools.tr(myRequest,"version") + " ");
      myOut.print( Constants.SEE_VERSION );
      myOut.println("\t " + LanguageTools.tr(myRequest,"see_author") + " -->\n");
      myOut.println("<HTML>\n");
      myOut.println("<HEAD>");
      myOut.println("<META HTTP-EQUIV=\"Pragma\" CONTENT=\"no-cache\">");
      myOut.println("<META HTTP-EQUIV=\"Cache-Control\" CONTENT=\"no-store\">");
      myOut.println("<META HTTP-EQUIV=\"content-type\" CONTENT=\"text/html;" +
                    "charset=" + ServletTools.getSessionCharset(myRequest) +
                    "\">");
      String mytt = LanguageTools.tr(myRequest,"chong2see") +
                    " - " + mytitle ;
      myOut.println("<TITLE> " + mytt + "</TITLE>");
      String fname = STYLE_PATH +
                     ServletTools.getSessionInterfaceStyle(myRequest) +
                     ".css";
      myOut.println("<LINK rel=\"stylesheet\" type=\"text/css\" " +
                    " href=\"" + fname + "\"> ");
      myOut.println("</HEAD>\n");
      myOut.println("<SCRIPT LANGUAGE='javascript'>\n");
      String mypro = ServletTools.getSessionProject(myRequest);
      if ( mypro == null ) mypro = "";
      myOut.println("\twindow.status = \"" +
                    LanguageTools.tr(myRequest,"user") + " : " +
                    ServletTools.getSessionNick(myRequest) + "      " +
                    LanguageTools.tr(myRequest,"current_project") + " : " +
                    mypro + "      " +
                    LanguageTools.tr(myRequest,"data") + " : " +
                    mytitle + "      " +
                    LanguageTools.tr(myRequest,"description") + " : " +
                    inDesciption + "\";" );
      myOut.println("\t top.document.title = \"" + mytt + "\" ;\n");
      myOut.println("</SCRIPT>\n");

      if ( showType == 0 ) {
        myOut.println("<BODY >\n");
        myOut.println("<P class=title align=center title=\"" +
                      inDesciption + "\">" +
                      mytitle + "</P>\n");
        myOut.println("<HR>\n");
      }

      if ( showType == 1 ) {
        myOut.println("<BODY >\n");
      }

      if ( showType == 2 ) {
        myOut.println("<BODY onload='see_init_parameters();'>\n");
        myOut.println("<P class=title align=center title=\"" +
                      inDesciption + "\">" +
                      mytitle + "</P>\n");
        myOut.println("<HR>\n");
      }


    } catch (Exception ex) {
      ex.printStackTrace();
    }

  }

  /**
   * ���ҳ���β��Ϣ�������Ȩ��Ϣ��
   * @param myRequest ��ǰ����
   * @param myOut ��ǰ�����
   */
  public static void outFooter(HttpServletRequest myRequest,
                               PrintWriter myOut) {
    outFooter(myRequest, myOut, 0);
  }

  /**
   * ���ҳ���β��Ϣ
   * @param myRequest ��ǰ����
   * @param myOut ��ǰ���
   * @param showType ������͡�0�������Ȩ��Ϣ���������������Ȩ��Ϣ
   */
  public static void outFooter(HttpServletRequest myRequest,
                               PrintWriter myOut,
                               int  showType) {

    try{

      ServletContext myContext = ServletTools.getContext(myRequest);
      String myenc = ServletTools.getSessionEncoding(myRequest);

      if ( showType == 0 ) {
        if( myRequest.getAttribute(DataManager.SEE_START) != null ) {
          long mys =
          CommonTools.stringToLong(myRequest.getAttribute(DataManager.SEE_START).toString());
          long mye = CommonTools.getCurrentTime();
          myOut.println("<P class=small_info align=right>" +
                        LanguageTools.tr(myRequest,"response_speed") +
                       " : " + (mye - mys) + " " +
                       LanguageTools.tr(myRequest,"milliseconds") +
                       "</P>" );
        }
        myOut.println("<HR>");
        myOut.print("<H5 class=footer align=center>" +
                    LanguageTools.tr(myRequest,"chong2see") );
        myOut.print("\t " + LanguageTools.tr(myRequest,"version") + " " );
        myOut.println(Constants.SEE_VERSION);
        myOut.println("</H5>" );
        // myOut.println("<BR>" + tr(myRequest,"see_author") + "</H5>" );
        // ���������ǲ�������ˡ�:��
        myOut.println("</BODY>\n");
      } else  {
        myOut.println("</BODY>\n");
      }

      myOut.println("</HTML>");
      myOut.close();

    } catch (Exception ex) {
      ex.printStackTrace();
    }

  }

}