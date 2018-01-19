package chong2.see.utils;

import chong2.see.data.base.Constants;
import chong2.see.servlet.common.DataManager;

import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>Title: 软件工程管理环境</p>
 * <p>Description: 为软件研发提供管理数据和任务的平台</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: 虫二</p>
 *
 * 与输出页面相关的静态方法
 *
 * @author 玛瑞
 * @version 0.07
 */

final public class PageTools {

  /**
   * 信息输出页面的相对地址
   */
  public static String INFO_PAGE =  "info-page";
  /**
   * 显示风格目录的相对地址
   */
  public static String STYLE_PATH =  "style/";

  /**
   * 重定向页面。这种方式，不会将当前页面的参数带到新页面中。
   * @param myResponse 当前响应
   * @param inPage 要转到的页面
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
   * 重定向页面。这种方式，将会把当前页面的参数带到新页面中。
   * @param myRequest 当前请求
   * @param myResponse 当前响应
   * @param inPage 要转到的页面
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
   * 刷新当前窗口
   * @param myOut 当前输出
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
   * 刷新指定页面为新的内容
   * @param myOut 当前输出
   * @param myTarget 要刷新的frame名
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
   * 刷新指定页面为新的内容
   * @param myOut 当前输出
   * @param myTarget 要刷新的frame名
   * @param myUrl 要刷新页面的URL内容
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
   * 刷新当前页面为新的内容
   * @param myOut 当前输出
   * @param myUrl 要刷新页面的URL内容
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
   * 输出错误信息显示页面
   * @param myRequest 当前请求
   * @param myResponse 当前响应
   * @param myinfo 要显示的错误信息
   */
  public static void outErrorPage(HttpServletRequest myRequest,
                                  HttpServletResponse myResponse,
                                  String myinfo) {
    outErrorPage(myRequest,myResponse,null,myinfo,null,null);
  }

  /**
   * 输出错误信息显示页面
   * @param myRequest 当前请求
   * @param myResponse 当前响应
   * @param myinfo 要显示的错误信息
   * @param mydes 要显示的错误信息的进一步描述
   */
  public static void outErrorPage(HttpServletRequest myRequest,
                                  HttpServletResponse myResponse,
                                  String myinfo,
                                  String mydes) {
    outErrorPage(myRequest,myResponse,null,myinfo,mydes,null);
  }

  /**
   * 输出错误信息显示页面
   * @param myRequest 当前请求
   * @param myResponse 当前响应
   * @param mytitle 要显示的错误信息的标题
   * @param myinfo 要显示的错误信息
   * @param mydes 要显示的错误信息的进一步描述
   * @param mybutton 页面上的按钮名
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
   * 输出信息显示页面
   * @param myRequest 当前请求
   * @param myResponse 当前响应
   * @param myinfo 要显示的信息
   * @param mydes 要显示的信息的进一步描述
   */
  public static void outInfoPage(HttpServletRequest myRequest,
                                 HttpServletResponse myResponse,
                                 String myinfo,
                                 String mydes) {
    outInfoPage(myRequest,myResponse,null,myinfo,mydes,null);
  }

  /**
   * 输出信息显示页面
   * @param myRequest 当前请求
   * @param myResponse 当前响应
   * @param mytitle 要显示的信息的标题
   * @param myinfo 要显示的信息
   * @param mydes 要显示的信息的进一步描述
   * @param mybutton 页面上的按钮名。缺省为“返回”。
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
   * 弹出执行成功的窗口
   * @param out 当前输出
   * @param myinfo 要显示的信息
   * @param mydes 显示信息的进一步描述
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
   * 弹出执行失败的窗口
   * @param out 当前输出
   * @param myinfo 要显示的信息
   * @param mydes 显示信息的进一步描述
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
   * 使当前页面返回上一页
   * @param out 当前输出
   */
  public static void outBackPage(PrintWriter out) {
    out.println("\t<SCRIPT LANGUAGE='javascript'>\n");
    out.println("\t window.history.back();");
    out.println("\t</SCRIPT>");
  }

  /**
   * 输出网页的头信息，不显示题头
   * @param myRequest 当前请求
   * @param myOut 当前输出
   * @param inTitle 网页的标题
   */
  public static void outHeader(HttpServletRequest myRequest,
                               PrintWriter myOut,String inTitle) {
    outHeader(myRequest, myOut, inTitle, "", "","",0);
  }

  /**
   * 输出网页的头信息
   * @param myRequest 当前请求
   * @param myOut 当前输出
   * @param inTitle 网页的标题
   * @param showType 显示的类型
   */
  public static void outHeader(HttpServletRequest myRequest,
                               PrintWriter myOut,String inTitle,
                               int  showType) {
    outHeader(myRequest, myOut, inTitle, "", "","", showType);
  }

  /**
   * 输出网页的头信息
   * @param myRequest 当前请求
   * @param myOut 当前输出
   * @param inFunction 当前功能
   * @param inOperation 当前操作
   * @param inSubOperation 当前子操作
   * @param inDesciption 描述
   * @param showType 显示类型。
   * 0：显示表头，1：不显示表头，2：初始化参数，其它：不输出body页签
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
   * 输出页面的尾信息。输出版权信息。
   * @param myRequest 当前请求
   * @param myOut 当前输出。
   */
  public static void outFooter(HttpServletRequest myRequest,
                               PrintWriter myOut) {
    outFooter(myRequest, myOut, 0);
  }

  /**
   * 输出页面的尾信息
   * @param myRequest 当前请求
   * @param myOut 当前输出
   * @param showType 输出类型。0：输出版权信息，其它：不输出版权信息
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
        // 作者名还是不必输出了。:）
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