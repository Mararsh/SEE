package chong2.see.servlet.frame;

import chong2.see.servlet.common.DataManager;
import chong2.see.servlet.common.HttpServletType1;
import chong2.see.utils.*;
import chong2.see.data.DataReader;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>Title: 软件工程管理环境</p>
 * <p>Description: 为软件研发提供管理数据和任务的平台</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: 虫二</p>
 *
 * <p>缺省的页面框架
 *
 * @author 玛瑞
 * @version 0.07
 */


public class frame_default
    extends HttpServletType1 {

  /**
   * 页面主控方法
   * @param inRequest 当前请求
   * @param inResponse 当前响应
   * @throws IOException IO异常
   * @throws ServletException servlet异常
   */
  protected  void processRequest(HttpServletRequest inRequest,
                                 HttpServletResponse inResponse)
      throws IOException, ServletException {

    try{

      String  myframe = ServletTools.getParameter(inRequest,"myframe");
      PrintWriter out = inResponse.getWriter();

      // 输出主框架
      if ( (myframe == null) || "".equals(myframe) ) {
        outFrame(inRequest,out);
      }

      // 输出左上角页面
      if ( "head".equals(myframe) ) {
        outHead(inRequest,out);
      }

      // 输出主上页面
      if ( "mtop".equals(myframe) ) {
        outMtop(inRequest,out);
      }

      // 输出次上页面
      if ( "stop".equals(myframe) ) {
        outStop(inRequest,out);
      }

      // 输出左页面
      if ( "left".equals(myframe) ) {
        outLeft(inRequest,out);
      }

      // 输出底页面
      if ( "bottom".equals(myframe) ) {
        outBottom(inRequest,out);
      }

      // 输出主页面
      if ( "main".equals(myframe) ) {
        outMain(inRequest,inResponse,out);
      }

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * 输出主框架
   * @param inRequest 当前请求
   * @param out 当前输出
   */
  public static void  outFrame(HttpServletRequest inRequest,
                               PrintWriter out) {

    PageTools.outHeader(inRequest, out,"",-1);

    out.println("<FRAMESET border=0 frameBorder=0 frameSpacing=0 rows=100,*>");
    out.println("\t<FRAMESET border=0 cols=200,*>");
    out.println("\t\t<FRAME frameBorder=0 marginHeight=0 marginWidth=0 " +
                "name=head  src=\"" +
                inRequest.getRequestURI() +
                "?myframe=head\">");
    out.println("\t\t</FRAME>");
    out.println("\t\t<FRAMESET border=0 rows=65,*>");
    out.println("\t\t\t<FRAME frameBorder=0 marginHeight=0 marginWidth=0 " +
                "name=mtop src=\"" +
                inRequest.getRequestURI() +
                "?myframe=mtop\">");
    out.println("\t\t\t</FRAME>");
    out.println("\t\t\t<FRAME frameBorder=0 marginHeight=0 marginWidth=0 " +
                "name=stop src=\"" +
                inRequest.getRequestURI() +
                "?myframe=stop\">");
    out.println("\t\t\t</FRAME>");
    out.println("\t\t</FRAMESET>");
    out.println("\t</FRAMESET>");
    out.println("\t<FRAMESET border=0 rows=*,58>");
    out.println("\t\t<FRAMESET border=0 cols=140,*>");
    out.println("\t\t\t<FRAME frameBorder=0 marginHeight=0 " +
                "marginWidth=0 name=left src=\"" +
                inRequest.getRequestURI() +
                "?myframe=left\">");
    out.println("\t\t\t</FRAME>");
    out.println("\t\t\t<FRAME frameBorder=0 marginHeight=0 " +
                "marginWidth=0 name=main src=\"" +
                inRequest.getRequestURI() +
                "?myframe=main\">");
    out.println("\t\t\t</FRAME>");
    out.println("\t\t</FRAMESET>");
    out.println("\t\t<FRAME frameBorder=0 marginHeight=0 " +
                "marginWidth=1 name=bottom  src=\"" +
                inRequest.getRequestURI() +
                "?myframe=bottom\">");
    out.println("\t\t</FRAME>");
    out.println("\t</FRAMESET>");
    out.println("</FRAMESET>");
    out.println("</HTML>");
    return;

  }

  /**
   * 输出左上角页面
   * @param inRequest 当前请求
   * @param out 当前输出
   */
  public static void  outHead(HttpServletRequest inRequest,
                              PrintWriter out) {

    PageTools.outHeader(inRequest, out,"",1);
    out.println("<A href=image/chong2-big.jpg target=_blank>");
    out.println("<img src=image/chong2.jpg border=0 ></A></td>");
    PageTools.outFooter(inRequest, out,1);
    return;

  }

  /**
   * 输出主上页面
   * @param inRequest 当前请求
   * @param out 当前输出
   */
  public static void  outMtop(HttpServletRequest inRequest,
                              PrintWriter out) {

    ArrayList myroots = Index.getThemeChild(inRequest,"see");
    if ( myroots == null )    return;
    PageTools.outHeader(inRequest, out,"",1);

    ArrayList mytext = new ArrayList() ;
    ArrayList myurl = new ArrayList() ;
    ArrayList myimg = new ArrayList() ;
    String myname;
    for (int i=0; i< myroots.size(); i++) {
      myname = myroots.get(i).toString();
      mytext.add(myname);
      myimg.add(ServletTools.getImageName(inRequest,myname + "_top"));
      myurl.add(inRequest.getRequestURI() +
                "?myframe=left&name=" + myname);
    }
    out.println("\t<P align=right>");
    OutputController.outButtonBar(inRequest, out, mytext,
                                  myimg,myurl,"left");
    out.println("\t</P>");

    PageTools.outFooter(inRequest, out,1);
    return;

  }

  /**
   * 输出次上页面
   * @param inRequest 当前请求
   * @param out 当前输出
   */
  public static void  outStop(HttpServletRequest inRequest,
                              PrintWriter out) {

    String  myroot = ServletTools.getParameter(inRequest,"name");
    String  mymroot = ServletTools.getParameter(inRequest,"root");
    if ( myroot == null ) return;
    Hashtable mynode = Index.getThemeNode(inRequest,myroot);
    if ( mynode == null ) return;
    ArrayList myroots = new ArrayList();
    if ( mynode.get("child") != null )
      myroots =
      CommonTools.stringToArray(mynode.get("child").toString(),
      DataReader.LIST_SPLITTER);
    myroots = Index.filterThemeChild(inRequest,myroots);
    String myname = mynode.get("name").toString();
    String mylink = null;
    if ( mynode.get("link") != null ) {
      mylink = mynode.get("link").toString() ;
      if ( mylink.indexOf(".") < 0 )
        mylink = mylink +
        "&mybutton=see_menu_level_" + mymroot +
                      "_left" + "&root=" + mymroot;
    }
    if ( mylink != null )
    PageTools.refreshFrame(out,"main",
                           Index.filterThemeLink(inRequest,
                           myname,mylink));

    PageTools.outHeader(inRequest, out,"",1);
    ArrayList mytext = new ArrayList() ;
    ArrayList myurl = new ArrayList() ;
    ArrayList myimg = new ArrayList() ;
    for (int i=0; i< myroots.size(); i++) {
      myname = myroots.get(i).toString();
      mynode = Index.getThemeNode(inRequest,myname);
      if ( mynode == null ) continue;
      if ( mynode.get("link") == null ) continue;
      mylink = mynode.get("link").toString();
      mylink = Index.filterThemeLink(inRequest,
                                     mynode.get("name").toString(),mylink);
      mytext.add(myname);
      myimg.add(ServletTools.getImageName(inRequest,myname + "_stop"));
      myurl.add(mylink + "&root=" + mymroot + "");
    }
    out.println("\t<P align=left>");
    OutputController.outButtonBar(inRequest, out, mytext,myimg,myurl,"main");
    out.println("\t</P>");
    PageTools.outFooter(inRequest, out,1);
    return;

  }

  /**
   * 输出左页面
   * @param inRequest 当前请求
   * @param out 当前输出
   */
  public static void  outLeft(HttpServletRequest inRequest,
                              PrintWriter out) {

    String  myroot = ServletTools.getParameter(inRequest,"name");
    if ( myroot == null ) return;
    Hashtable mynode = Index.getThemeNode(inRequest,myroot);
    if ( mynode == null ) return;
    ArrayList myroots = new ArrayList();
    if ( mynode.get("child") != null )
      myroots =
      CommonTools.stringToArray(mynode.get("child").toString(),
      DataReader.LIST_SPLITTER);
    myroots = Index.filterThemeChild(inRequest,myroots);
    String myname = mynode.get("name").toString();
    String mylink = null;
    if ( mynode.get("link") != null )
      mylink = mynode.get("link").toString() ;
    else
      mylink = "info-page?mytitle=" + myname ;
    if ( mylink.indexOf(".") < 0 )
      mylink = mylink +
      "&mybutton=see_menu_level_" + myroot + "_mtop" ;
    PageTools.refreshFrame(out,"main",
                           Index.filterThemeLink(inRequest,
                           myname,mylink));

    PageTools.outHeader(inRequest, out, "",1);
    out.println("\t<BR>");
    ArrayList mytext = new ArrayList() ;
    ArrayList myurl = new ArrayList() ;
    ArrayList myimg = new ArrayList() ;
    for (int i=0; i< myroots.size(); i++) {
      myname = myroots.get(i).toString();
      mytext.add(myname);
      myimg.add(ServletTools.getImageName(inRequest,myname + "_left"));
      myurl.add(inRequest.getRequestURI() +
                "?myframe=stop&name="  + myname +
                "&root=" + myroot + "");
    }
    out.println("\t<P align=left>");
    OutputController.outButtonBar(inRequest, out, mytext,myimg,myurl,"stop");
    out.println("\t</P>");
    PageTools.outFooter(inRequest, out,1);
    return;

  }

  /**
   * 输出底页面
   * @param inRequest 当前请求
   * @param out 当前输出
   */
  public static void  outBottom(HttpServletRequest inRequest,
                                PrintWriter out) {

    PageTools.outHeader(inRequest, out,"",1);

    ArrayList mytext = new ArrayList() ;
    ArrayList myurl = new ArrayList() ;
    ArrayList myimg = new ArrayList() ;

    out.println("\t<HR size=1>");
    out.println("<TABLE class=bottom width=100% border=0><TR>");
    out.println("<TD align=left>");

    String  myuser = ServletTools.getSessionUser(inRequest);
    if ( myuser != null) {
      if ( !ServletTools.getAppDefaultUser(inRequest).equals(myuser)) {
        mytext.add("self");
        String myset = DataManager.USER;
        if ( AclTools.isSuperseer(inRequest) )
          myset = DataManager.SUPER_SEER;
        myurl.add(DataManager.URL +
                  "?myframe=data&myset=" + myset +
                  "&myoperation=modify" +
                  "&mybutton=modify&mykeys=id" +
                  QueryTools.CONDITION_SPLITTER +
                  "_self");
        myimg.add(ServletTools.getImageName(inRequest,"self_bottom"));
      }
    }

    mytext.add("setup");
    myurl.add(inRequest.getRequestURI() +
              "?myframe=main&function=setup");
    myimg.add(ServletTools.getImageName(inRequest,"setup_bottom"));

    mytext.add("meeting_summary");
    myurl.add("set_data(\"meeting_summary\",\"query\",\"\",\"\")");
    myimg.add(ServletTools.getImageName(inRequest,"meeting_summary_bottom"));

    mytext.add("contact_way");
    myurl.add(inRequest.getRequestURI() +
              "?myframe=main&function=contact_way");
    myimg.add(ServletTools.getImageName(inRequest,"contact_way_bottom"));

    mytext.add("problem_report");
    myurl.add("set_data(\"problem_report\",\"add\",\"\",\"\")");
    myimg.add(ServletTools.getImageName(inRequest,"problem_report_bottom"));

    mytext.add("working_log");
    myurl.add("set_data(\"working_log\",\"add\",\"\",\"\")");
    myimg.add(ServletTools.getImageName(inRequest,"working_log_bottom"));

    mytext.add("personal_message");
    myurl.add("set_data(\"" +
              DataManager.PERSONAL_INFORMATION +
              "\",\"query\",\"receive\",\"" +
              "receiver:v:h-v:h::c:i:d-c:i:d:_self\")");
    myimg.add(ServletTools.getImageName(inRequest,"personal_message_bottom"));

    OutputController.outButtonBar(inRequest, out,
                                  mytext,myimg,myurl,"main");

    int mypm = ServletTools.getNewPMSize(inRequest);
    if ( mypm > 0 )
    out.println("\t <FONT class=alert>" + mypm + "</FONT>");
    out.println("\t</TD><TD align=right>");

    mytext = new ArrayList() ;
    myurl = new ArrayList() ;
    myimg = new ArrayList() ;

    mytext.add("login");
    myurl.add(inRequest.getRequestURI() +
              "?myframe=main&function=login");
    myimg.add(ServletTools.getImageName(inRequest,"login_bottom"));

    mytext.add("mail_to");
    myurl.add("mailto:mara@chong2.com");
    myimg.add(ServletTools.getImageName(inRequest,"mail_to_bottom"));

    mytext.add("manual");
    myurl.add("manual/index.html");
    myimg.add(ServletTools.getImageName(inRequest,"manual_bottom"));

    mytext.add("close");
    myurl.add("close_window()");
    myimg.add(ServletTools.getImageName(inRequest,"close_bottom"));

    OutputController.outButtonBar(inRequest, out,
                                  mytext,myimg,myurl,"main");

    out.println("\t</TD></TR></TABLE>");

    out.println("\t<FORM name=seeform  method=post " +
              " action=\"\" onSubmit='return false;'>");

    out.println("\t<INPUT type=hidden name=myframe >");
    out.println("\t<INPUT type=hidden name=myset value= > ");
    out.println("\t<INPUT type=hidden name=myoperation value= > ");
    out.println("\t<INPUT type=hidden name=mysuboperation value= > ");
    out.println("\t<INPUT type=hidden name=mywhere value= > ");
    out.println("\t</FORM> ");

    out.println("\n\t<SCRIPT LANGUAGE='javascript'>\n");
    out.println("function see_close_window()");
    out.println("{");
    out.println("\t top.close();");
    out.println("}");
    out.println("function see_set_data(myset,myop,mysubop,mywhere)");
    out.println("{");
    out.println("\t  document.seeform.myset.value = myset;");;
    out.println("\t  document.seeform.mysuboperation.value = mysubop;");;
    out.println("\t  document.seeform.myoperation.value = myop;");;
    out.println("\t document.seeform.mywhere.value = mywhere;");;
    out.println("\t document.seeform.target = \"main\";");;
    out.println("\t document.seeform.action = \"" +
                 DataManager.URL + "\";");;
    out.println("\t document.seeform.submit();");
    out.println("\t document.seeform.myset.value = \"\";");
    out.println("\t document.seeform.mysuboperation.value = \"\";");;
    out.println("\t document.seeform.myoperation.value = \"\";");;
    out.println("\t document.seeform.mywhere.value = \"\";");;
    out.println("\t document.seeform.target = \"\";");;
    out.println("\t document.seeform.action = \"\";");;
    out.println("}");
    out.println("</SCRIPT>");

    PageTools.outFooter(inRequest, out,1);
    return;

  }

  /**
   * 输出主页面
   * @param inRequest 当前请求
   * @param inResponse 当前响应
   * @param out 当前输出
   */
  public static void  outMain(HttpServletRequest inRequest,
                              HttpServletResponse inResponse,
                              PrintWriter out) {

    try {
      String  myfunc =
          ServletTools.getParameter(inRequest,"function");

      if ( myfunc == null || "".equals(myfunc)  ) {

        String myuser = ServletTools.getSessionUser(inRequest);

        if ( ( myuser != null) &&
             !myuser.equals(ServletTools.getAppDefaultUser(inRequest))) {
          PageTools.outInfoPage(inRequest,inResponse,
                                "see_hello",
                                null,
                                myuser,
                                "null" );
          return;
        }
        else
          login(inRequest, inResponse,out);

      } else if ( "login".equals(myfunc) ) {

        login(inRequest, inResponse,out);

      } else if ( "setup".equals(myfunc) ) {

        setup(inRequest, out);

      } else if ( "contact_way".equals(myfunc) ) {

        contact_way(inRequest, out);

      }

      PageTools.outFooter(inRequest, out);
      return;
    }
    catch (ServletException ex) {
    }catch (IOException ex) {
    }
  }

  /**
   * 输出设置页面
   * @param inRequest 当前请求
   * @param out 当前输出
   * @throws IOException IO异常
   * @throws ServletException servlet异常
   */
  public static void  setup(HttpServletRequest inRequest,
                            PrintWriter out)
      throws IOException, ServletException {

    try{

      String  mystyle =
          ServletTools.setSessionInterfaceStyle(inRequest);
      String  mytheme =
          ServletTools.setSessionInterfaceTheme(inRequest);
      String  mydatastyle =
          ServletTools.setSessionDatalistStyle(inRequest,
          "input_datalist_style");
      String  myproject =
          ServletTools.setSessionProject(inRequest);
      int  mysize =
          ServletTools.setSessionPageSize(inRequest);

      String  mylang = ServletTools.getParameter(inRequest,"language");
      if ( ( mylang != null) ) {
        if ( ServletTools.setSessionEncoding(inRequest,mylang) ) {
          PageTools.refreshWindow(out);
          return;
        }
      }
      String  myop = ServletTools.getParameter(inRequest,"operation");
      if ( "ok".equals(myop) ) {
        PageTools.refreshWindow(out);
        return;
      }

      PageTools.outHeader(inRequest, out,"setup",0);
      String myenc = ServletTools.getSessionEncoding(inRequest);
      out.println("<FORM name=seeform method=post onSubmit='return false;'>");
      out.println("<INPUT type=hidden name=function value=setup>");
      out.println("<INPUT type=hidden name=function value=setup>");
      out.println("<INPUT type=hidden name=operation>");
      out.println("\t<SCRIPT LANGUAGE='javascript'>\n");
      out.println("function see_ok()");
      out.println("{");
      out.println("document.seeform.operation.value=\"ok\";");
      out.println("document.seeform.submit();");
      out.println("}");
      out.println("function see_reset()");
      out.println("{");
      out.println("document.formsetup.reset();");
      out.println("}");
      out.println("</SCRIPT>");

      out.println("<TABLE align=center class=data>");
      out.println("\t<TR class=tableline><TD align=right>" +
                  tr(inRequest,"interface_style") +
                  " : </TD>");
      out.println("<TD align=left>");
      OutputController.outInterfaceStyleInput(inRequest,out,
          "interface_style", mystyle);
      out.println("</TD>");
      out.println("\t<TR class=tableline><TD align=right>" +
                  tr(inRequest,"interface_theme") +
                  " : </TD>");
      out.println("<TD align=left>");
      OutputController.outInterfaceThemeInput(inRequest,out,
          "interface_theme", mytheme);
      out.println("</TD>");
      out.println("\t<TR class=tableline><TD align=right>" +
                  tr(inRequest,"datalist_style") +
                  " : </TD>");
      out.println("<TD align=left>");
      OutputController.outDatalistStyleInput(inRequest,out,
          "datalist_style",mydatastyle);
      out.println("</TD>");
      out.println("\t<TR class=tableline><TD align=right>" +
                  tr(inRequest,"page_size") +
                  " : </TD>");
      out.println("<TD align=left><INPUT name=input_page_size value=" +
                  mysize +  " ></TD>");
      out.println("\t<TR class=tableline><TD align=right>" +
                  tr(inRequest,"default_project") +
                  " : </TD>");
      out.println("<TD align=left>");
      OutputController.outRefInput(inRequest,out,
                                   "default_project",
                                   myproject,"project","solo",
                                   null  , -1);
      out.println("</TD>");
      out.println("</TABLE>");

      out.println("<P align=center>");
      ArrayList myt = new ArrayList() ;
      myt.add("ok");
      myt.add("reset");
      OutputController.outButtonBar(inRequest, out, myt);
      out.println("</P>");

      out.println("</FORM>");

      out.println("<P align=center>");
      OutputController.outLanguageSelection(inRequest, out);
      out.println("</P>");

      PageTools.outFooter(inRequest, out);

    } catch (Exception ex) {
      ex.printStackTrace();
    }


  }

  /**
   * 输出登录页面
   * @param inRequest 当前请求
   * @param inResponse 当前响应
   * @param out 当前输出
   * @throws IOException IO异常
   * @throws ServletException servlet异常
   */
  public static void  login(HttpServletRequest inRequest,
                            HttpServletResponse inResponse,
                            PrintWriter out)
      throws IOException, ServletException{

    try{

      String  myid = ServletTools.getParameter(inRequest,"input_id");
      String  mypass = ServletTools.getParameter(inRequest,"input_password");
      String  myrem = ServletTools.getParameter(inRequest,"input_rememberme");
      String  mylong = ServletTools.getParameter(inRequest,"input_howlong");
      String  myop = ServletTools.getParameter(inRequest,"operation");
      String  mylang = ServletTools.getParameter(inRequest,"language");
      if ( ( mylang != null) ) {
        if ( ServletTools.setSessionEncoding(inRequest,mylang) ) {
          PageTools.refreshWindow(out);
          return;
        }
      }

      Hashtable mycookie = CookieTools.getLoginCookie(inRequest);
      boolean isValid = true;

      if ( (myid == null) || "".equals(myid) ) {
        if ( mycookie.get("user") != null  ) {
          myid =  mycookie.get("user").toString() ;
          if ( mycookie.get("password") != null  )
            mypass =  mycookie.get("password").toString() ;
        } else
          myid = ServletTools.getAppDefaultUser(inRequest);
      }
      if ( mypass == null ) mypass = "";
      if ("login".equals(myop)) {
        isValid =
            ServletTools.setSessionUser(inRequest,myid,mypass);
        if (isValid ) { // 设置cookie
          if ( "yes".equals(myrem) ) {
            int  mylon;
            if ( "one_year".equals(mylong) )
              mylon = 365*24*3600;
            else  if ( "one_month".equals(mylong) )
              mylon = 31*24*3600;
            else  if ( "one_week".equals(mylong) )
              mylon = 7*24*3600;
            else  if ( "one_day".equals(mylong) )
              mylon = 24*3600;
            else
              mylon = 3600;
            CookieTools.setLoginCookie(inRequest,inResponse,
                                       myid, mypass, mylon);
          } else { // 删除登录cookie
            CookieTools.removeLoginCookie(inRequest,inResponse);
          }
          PageTools.refreshWindow(out);
          return;
        }
      }
      if ("logout".equals(myop)) {
        ServletTools.setSessionUser(inRequest,
                                    ServletTools.getAppDefaultUser(inRequest));
        CookieTools.removeLoginCookie(inRequest,inResponse);
        PageTools.refreshWindow(out);
        return;
      }

      PageTools.outHeader(inRequest, out,"login",0);
      String myenc = ServletTools.getSessionEncoding(inRequest);

      if ( isValid == false ) {
        out.print("<P class=alert align=center>");
        out.print(tr(inRequest,"invalid_login"));
        out.println("</P>");
      }
      out.println("<FORM name=formlogin method=post onSubmit='return false;'>");
      out.print("<P class=info align=center>");
      String cuid = ServletTools.getSessionUser(inRequest);
      out.print(tr(inRequest,"current_user") + " : " + cuid);
      if ( ( mycookie.get("user") != null) &&
           (  mycookie.get("long") != null) &&
           !myid.equals(ServletTools.getAppDefaultUser(inRequest)) ) {
        out.print(" &nbsp&nbsp " + tr(inRequest,"remember_me") +
                  " : " );
        int mylon =
            CommonTools.stringToInt(mycookie.get("long").toString());
        if ( mylon == 365*24*3600 )
          out.print(tr(inRequest,"one_year"));
        else if ( mylon == 365*24*3600 )
          out.print(tr(inRequest,"one_year"));
        else if ( mylon == 31*24*3600 )
          out.print(tr(inRequest,"one_month"));
        else if ( mylon == 7*24*3600 )
          out.print(tr(inRequest,"one_week"));
        else if ( mylon == 24*3600 )
          out.print(tr(inRequest,"one_day"));
        else if ( mylon == 3600 )
          out.print(tr(inRequest,"one_hour"));
        else
          out.print(mylon + tr(inRequest,"second"));
      }
      out.println("<INPUT type=hidden name=function value=login>");
      out.println("<INPUT type=hidden name=operation>");
      out.println("\t<SCRIPT LANGUAGE='javascript'>\n");
      out.println("function see_ok()");
      out.println("{");
      out.println("document.formlogin.operation.value=\"login\";");
      out.println("document.formlogin.submit();");
      out.println("}");
      out.println("function see_reset()");
      out.println("{");
      out.println("document.formlogin.reset();");
      out.println("}");
      out.println("function see_logout()");
      out.println("{");
      out.println("document.formlogin.operation.value=\"logout\";");
      out.println("document.formlogin.submit();");
      out.println("}");
      out.println("</SCRIPT>");

      out.println("<TABLE align=center class=data>");
      out.print("\t<TR class=tableline><TD align=right>" +
                tr(inRequest,"id") + " : </TD>");
      out.println("<TD><INPUT name=input_id value=\"" +
                  myid + "\"></TD></TR>");
      out.print("\t<TR class=tableline><TD align=right>" +
                tr(inRequest,"password") + " : </TD>");
      out.println("<TD><INPUT type=password name=input_password value=\"" +
                  "\"></TD></TR>");
      out.println("\t<TR class=tableline><TD align=right>" +
                tr(inRequest,"remember_me") +
                  "<INPUT type=checkbox name=input_rememberme "+
                  " value=\"yes\" checked>" + "</TD>");
      out.println("<TD>");
      OutputController.outRadioInput(inRequest,out,"howlong",
                                     "one_year" ,
                                     CommonTools.stringToArray("one_year,one_month,one_week,one_day,one_hour",","),
                                     null, false, false);
      out.println("</TD></TR>");
      out.println("</TABLE>");

      out.println("<BR>");
      ArrayList myt = new ArrayList() ;
      myt.add("ok");
      myt.add("reset");
      myt.add("logout");
      ArrayList mytab = new ArrayList() ;
      mytab.add("1");
      mytab.add("-1");
      mytab.add("-1");
      OutputController.outButtonBar(inRequest,out,myt,
                    ServletTools.getImageNames(inRequest,myt),
                    ServletTools.getClicks(myt) , null,mytab);
      out.println("<BR>" + tr(inRequest,"cookie_info") +
                  "<BR>" + tr(inRequest,"data_manager_guide") );

      out.println("</FORM>");

      OutputController.outLanguageSelection(inRequest, out);
      out.println("</P>");


    } catch (Exception ex) {
      ex.printStackTrace();
    }


  }


  /**
   * 输出联系方式的页面
   * @param inRequest 当前请求
   * @param out 当前输出
   * @throws IOException IO异常
   * @throws ServletException servlet异常
   */
  public static void  contact_way(HttpServletRequest inRequest,
                                  PrintWriter out)
      throws IOException, ServletException{

    try{

      DataReader myreader =
          DataManagerTools.getDataReader(inRequest,
          DataManager.USER, null,0,-1, null);
      if ( myreader == null  ) return;

      ArrayList mydata = myreader.getRecords();
      if ( mydata == null ) return;

      PageTools.outHeader(inRequest, out,"contact_way",0);

      String myenc = ServletTools.getSessionEncoding(inRequest);
      Hashtable myd;
      myd = AclTools.getSuperseer(inRequest);
      if ( myd != null)
        mydata.add(myd);
//      myd = new Hashtable();
//      myd.put("id",tr(inRequest,"author") +
//              ":" + tr(inRequest,"see_author"));
//      myd.put("email","mara@chong2.com");
//      mydata.add(myd);
      out.println("\t<TABLE class=data cellPadding=2 width=100%>");
      out.println("\t\t<TR class=tableheader >");
      out.print("\t\t<TH>" + tr(inRequest,"id") + "</TH>");
      out.print("<TH>" + tr(inRequest,"nickname") + "</TH>");
      out.print("<TH>" + tr(inRequest,"job_title") + "</TH>");
      out.print("<TH>" + tr(inRequest,"phone") + "</TH>");
      out.println("<TH>" + tr(inRequest,"email") + "</TH>");
      out.println("\t\t</TR>");
      String myv;
      for ( int i=0; i< mydata.size(); i++) {
        myd = (Hashtable)mydata.get(i);
        out.println("\t\t<TR  class=tabledata >");
        out.println("\t\t\t<TD align=center>" +
                    myd.get("id") + "</TD>");
        out.print("\t\t\t<TD align=center>");
        if ( myd.get("nickname") == null)
          out.print("-");
        else
          out.print(myd.get("nickname"));
        out.println("</TD>");
        out.print("\t\t\t<TD align=center>");
        if ( myd.get("job_title") == null)
          out.print("-");
        else
          out.print(myd.get("job_title"));
        out.println("</TD>");
        out.print("\t\t\t<TD align=center>");
        if ( myd.get("phone") == null)
          out.print("-");
        else
          out.print(myd.get("phone"));
        out.println("</TD>");
        out.print("\t\t\t<TD align=center>");
        if ( myd.get("email") == null)
          out.print("-");
        else
          out.print("<A href=\"mailto:" +
                    myd.get("email") +
                    "\">" + myd.get("email") + "</A>");
        out.println("</TD>");
        out.println("\t\t</TR>");
      }
      out.println("\t</TABLE>");

    } catch (Exception ex) {
      ex.printStackTrace();
    }

  }

}