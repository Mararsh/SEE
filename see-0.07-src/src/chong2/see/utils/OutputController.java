package chong2.see.utils;

import chong2.see.data.base.Language;
import chong2.see.servlet.common.DataManager;
import chong2.see.xml.DataStructureXmlParser;
import chong2.see.data.DataReader;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>Title: 软件工程管理环境</p>
 * <p>Description: 为软件研发提供管理数据和任务的平台</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: 虫二</p>
 *
 * 与输出页面控件相关的静态方法
 *
 * @author 玛瑞
 * @version 0.07
 */

final public class OutputController {

  /**
   * 文本段落的列数
   */
  public static int MAX_INPUT_COLS =  50 ;
  /**
   * 文本段落的行数
   */
  public static int MAX_INPUT_ROWS =  4 ;
  /**
   * 文本的长度
   */
  public static int MAX_INPUT_SIZE =  50 ;
  /**
   * 选择控件的行数
   */
  public static int SELECT_INPUT_SIZE =  4 ;
  /**
   * 界面方案列表
   */
  public static ArrayList INTERFACE_THEME  =
      CommonTools.stringToArray("theme_default",",");

  /**
   * 输出语言选择区域
   * @param myRequest 当前请求
   * @param out 当前输出
   */
  public static void outLanguageSelection(HttpServletRequest myRequest,
      PrintWriter out ) {

    ArrayList myEncodings = Language.getEncodingList() ;
    ArrayList myimage = new ArrayList() ;

    out.println("\t<SCRIPT LANGUAGE='javascript'>\n");

    for ( int i = 0 ; i < myEncodings.size(); i++ ) {

      String myenc = (String)myEncodings.get(i);

      myimage.add("image/" + myenc + ".jpg");

      out.println("\tfunction see_" + myenc + "()");
      out.println("\t{");
      out.println("\t\t document.formtable.language.value=\"" +
                  myenc +"\";");
      out.println("\t\t document.formtable.submit();");
      out.println("\t}");

    }
    out.println("\t</SCRIPT>");

    out.println("\t<FORM name=formtable method=post onSubmit='return false;'>");
    out.println("\t<INPUT type=hidden name=language value=\"\">");

    out.println("<P align=center>");
    outButtonBar(myRequest, out, myEncodings,myimage,null,"");
    out.println("<BR><FONT class=info>" +
                LanguageTools.tr(myRequest,"language_info",
                LanguageTools.tr(myRequest,ServletTools.getAppDefaultEncoding(myRequest)) ) +
                "</FONT></P>");
    out.println("\t</FORM>");

  }

  /**
   * 输出横向按钮组
   * @param myRequest 当前请求
   * @param myOut 当前输出
   * @param inText 按钮名
   */
  public static void outButtonBar(HttpServletRequest myRequest,
                                  PrintWriter myOut,
                                  ArrayList inText) {
    outButtonBar(myRequest,myOut,inText,
                 ServletTools.getImageNames(myRequest,inText),
                 ServletTools.getClicks(inText) , "");
  }

  /**
   * 输出横向按钮组
   * @param myRequest 当前请求
   * @param myOut 当前输出
   * @param inText 按钮名
   * @param inImage 图片名
   * @param inClick 脚本名
   */
  public static void outButtonBar(HttpServletRequest myRequest,
                                  PrintWriter myOut,
                                  ArrayList inText,
                                  ArrayList inImage,
                                  ArrayList inClick ) {
    outButtonBar(myRequest,myOut,inText,inImage,inClick, "");
  }

  /**
   * 输出横向按钮组
   * @param myRequest 当前请求
   * @param myOut 当前输出
   * @param inText 按钮名
   * @param inImage 图片名
   * @param inClick 脚本名
   * @param inTarget 目标页面名
   */
  public static void outButtonBar(HttpServletRequest myRequest,
                                  PrintWriter myOut,
                                  ArrayList inText,
                                  ArrayList inImage,
                                  ArrayList inClick,
                                  String inTarget) {
    ArrayList myt = new ArrayList();
    for ( int i = 0; i < inText.size(); i++ ) {
      myt.add(inTarget);
    }
    outButtonBar(myRequest,myOut,inText,inImage,inClick, myt);
  }

  /**
   * 输出横向按钮组
   * @param myRequest 当前请求
   * @param myOut 当前输出
   * @param inText 按钮名
   * @param inImage 图片名
   * @param inClick 脚本名
   * @param inTarget 目标页面名
   */
  public static void outButtonBar(HttpServletRequest myRequest,
                                  PrintWriter myOut,
                                  ArrayList inText,
                                  ArrayList inImage,
                                  ArrayList inClick,
                                  ArrayList inTarget) {

    outButtonBar(myRequest,myOut,inText,inImage,
                 inClick, inTarget, null);

  }

  /**
   * 输出横向按钮组
   * @param myRequest 当前请求
   * @param myOut 当前输出
   * @param inText 按钮名
   * @param inImage 图片名
   * @param inClick 脚本名
   * @param inTarget 目标页面名
   * @param inOrder tab排序
   */
  public static void outButtonBar(HttpServletRequest myRequest,
                                  PrintWriter myOut,
                                  ArrayList inText,
                                  ArrayList inImage,
                                  ArrayList inClick,
                                  ArrayList inTarget,
                                  ArrayList inOrder) {
    try{
      if ( inText == null ) return;
      if ( inImage == null )
        inImage = ServletTools.getImageNames(myRequest,inText);
      if ( inClick == null )
        inClick = ServletTools.getClicks(inText);

      String myorder = null;
      for ( int i = 0; i < inText.size() ; i++ ) {
        String mytarget = null;
        if (inTarget != null)
          mytarget = (String)inTarget.get(i);
        if ( inOrder != null )
          myorder = inOrder.get(i).toString();
        outButton(myRequest, myOut, (String)inText.get(i),
                  (String)inImage.get(i), (String)inClick.get(i),
                  mytarget , myorder);
        myOut.println("\t\t\t");
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * 输出纵向按钮组
   * @param myRequest 当前请求
   * @param myOut 当前输出
   * @param inText 按钮名
   */
  public static void outButtonVBar(HttpServletRequest myRequest,
                                   PrintWriter myOut,
                                   ArrayList inText) {
    outButtonVBar(myRequest,myOut,inText,
                  ServletTools.getImageNames(myRequest,inText),
                  ServletTools.getClicks(inText) , "");
  }

  /**
   * 输出纵向按钮组
   * @param myRequest 当前请求
   * @param myOut 当前输出
   * @param inText 按钮名
   * @param inImage 图片名
   * @param inClick 脚本名
   */
  public static void outButtonVBar(HttpServletRequest myRequest,
                                   PrintWriter myOut,
                                   ArrayList inText,
                                   ArrayList inImage,
                                   ArrayList inClick ) {
    outButtonVBar(myRequest,myOut,inText,inImage,inClick, "");
  }

  /**
   * 输出纵向按钮组
   * @param myRequest 当前请求
   * @param myOut 当前输出
   * @param inText 按钮名
   * @param inImage 图片名
   * @param inClick 脚本名
   * @param inTarget 目标页面名
   */
  public static void outButtonVBar(HttpServletRequest myRequest,
                                   PrintWriter myOut,
                                   ArrayList inText,
                                   ArrayList inImage,
                                   ArrayList inClick,
                                   String inTarget) {
    ArrayList myt = new ArrayList();
    for ( int i = 0; i < inText.size(); i++ ) {
      myt.add(inTarget);
    }
    outButtonVBar(myRequest,myOut,inText,inImage,inClick, myt);
  }

  /**
   * 输出纵向按钮组
   * @param myRequest 当前请求
   * @param myOut 当前输出
   * @param inText 按钮名
   * @param inImage 图片名
   * @param inClick 脚本名
   * @param inTarget 目标页面名
   */
  public static void outButtonVBar(HttpServletRequest myRequest,
                                   PrintWriter myOut,
                                   ArrayList inText,
                                   ArrayList inImage,
                                   ArrayList inClick,
                                   ArrayList inTarget) {

    outButtonVBar(myRequest,myOut,inText,inImage,
                  inClick, inTarget, null);

  }
  /**
   * 输出纵向按钮组
   * @param myRequest 当前请求
   * @param myOut 当前输出
   * @param inText 按钮名
   * @param inImage 图片名
   * @param inClick 脚本名
   * @param inTarget 目标页面名
   * @param inOrder tab顺序
   */
  public static void outButtonVBar(HttpServletRequest myRequest,
                                   PrintWriter myOut,
                                   ArrayList inText,
                                   ArrayList inImage,
                                   ArrayList inClick,
                                   ArrayList inTarget,
                                   ArrayList inOrder) {
    try{

      if ( inText == null ) return;
      if ( inImage == null )
        inImage = ServletTools.getImageNames(myRequest,inText);
      if ( inClick == null )
        inClick = ServletTools.getClicks(inText);

      ServletContext myContext = ServletTools.getContext(myRequest);

      myOut.print("\t<TABLE >");

      String myorder = null;
      for ( int i = 0; i < inText.size() ; i++ ) {
        myOut.println("\t<TR><TD>");
        String mytarget = null;
        if (inTarget != null) mytarget = (String)inTarget.get(i);
        if ( inOrder != null)
          myorder = inOrder.get(i).toString();
        else
          myorder = null;
        outButton(myRequest, myOut,(String)inText.get(i),
                  (String)inImage.get(i), (String)inClick.get(i),
                  mytarget, myorder );
        myOut.println("\t</TD></TR>");

      }

      myOut.println("\t</TABLE>");

    } catch (Exception ex) {
      ex.printStackTrace();
    }

  }

  /**
   * 输出按钮
   * @param myRequest 当前请求
   * @param myOut 当前输出
   * @param inText 按钮名
   */
  public static void outButton(HttpServletRequest myRequest,
                               PrintWriter myOut,
                               String inText) {
    outButton(myRequest,myOut, inText,null, null,null, null);
  }

  /**
   * 输出按钮
   * @param myRequest 当前请求
   * @param myOut 当前输出
   * @param inText 按钮名
   * @param inClick 脚本名
   */
  public static void outButton(HttpServletRequest myRequest,
                               PrintWriter myOut,
                               String inText,
                               String inClick) {
    outButton(myRequest,myOut, inText,null, inClick,null, null);
  }

  /**
   * 输出按钮
   * @param myRequest 当前请求
   * @param myOut 当前输出
   * @param inText 按钮名
   * @param inImage 图片名
   * @param inClick 脚本名
   * @param inTarget 目标页面名
   * @param inOrder  该按钮的tab顺序
   */
  public static void outButton(HttpServletRequest myRequest,
                               PrintWriter myOut,
                               String inText,
                               String inImage,
                               String inClick ,
                               String inTarget,
                               String inOrder) {
    try{
      if ( inText == null ) return;
      if ( inImage == null )
        inImage = ServletTools.getImageName(ServletTools.getSessionEncoding(myRequest),
            ServletTools.getSessionInterfaceStyle(myRequest),
            inText);
      if ( inClick == null )
        inClick = ServletTools.getClick(inText);

      String mytarget = "";
      if ( inTarget != null && !("".equals(inTarget)))
        mytarget = " target=" + inTarget;

      String mytab = "";
      if ( inOrder != null )
        mytab = " tabindex=" + inOrder + " ";
      if ( inClick.indexOf('(') < 0 ) {
        myOut.print("\t\t<A href=\"" + inClick + "\" " +
                    mytarget + mytab + ">" );
        myOut.print("<IMG border=0 src=\"" + inImage + "\" " +
                    "alt=\"" +
                    LanguageTools.tr(myRequest,inText) +
                    "\"  ></A>");
      } else {
        myOut.print("\t\t<INPUT type=image onClick='see_" + inClick +
                    ";' " + mytab + mytarget  );
        myOut.print(" src=\"" + inImage + "\" " +
                    "alt=\"" +
                    LanguageTools.tr(myRequest,inText) +
                    "\"  >");
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }

  }


  /**
   * 输出页面翻页按钮-信息条
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param mytotal 总数
   * @param mystartindex 开始编号
   * @param myendindex 结尾编号
   * @param mypagesize 页大小
   * @param mytype 类型
   */
  public static void outScrollerBar(HttpServletRequest inRequest,
                                    PrintWriter out,
                                    int  mytotal,
                                    int  mystartindex,
                                    int  myendindex,
                                    int  mypagesize,
                                    String mytype) {

    if ( mytotal < 1 ) {
      mytotal = 0;
      mystartindex = 0;
      mystartindex = 0;
    }
    out.println("\t<BR><TABLE class=bar width=100%><TR>");
    out.print("\t\t<TD align=left>" +
              LanguageTools.tr(inRequest,"total_count") +
              " : " + mytotal +
              " &nbsp&nbsp&nbsp&nbsp&nbsp " +
              LanguageTools.tr(inRequest,"current_number") +
              " : " + mystartindex );
    if ( ( myendindex > mytotal ) ||
         ( myendindex < 0 ) )
      out.println(" - " + mytotal + "</TD>");
    else
      out.println(" - " + myendindex + "</TD>");

    if ( "html".equals(mytype) ||
        ( mytotal < 1  ) )  {
      out.println("\t<TR></TABLE>");
      return;
    }
    out.println("\t\t<TD align=right>");
    out.println("\t\t" +
                LanguageTools.tr(inRequest,"every_page") +
                " : <INPUT name=input_page_size size=2 " +
                "value=" + mypagesize + " >");
    outButton(inRequest,out,"ok","setPagecount(" +
              mystartindex + ")");
    out.println("\n\t\t" +
                LanguageTools.tr(inRequest,"jump_to_page") +
                " : <INPUT name=mypage size=2 " +
                "value=" +
                (mystartindex / mypagesize + 1) +
                " >");
    outButton(inRequest,out,"ok","setPage(" +
              mypagesize + "," + mytotal + ")");
    if ( mystartindex > mypagesize ) {
      out.println("");
      outButton(inRequest,out,"last_page","lastpage(" +
                (mystartindex - mypagesize) + ")");
    }
    if ( myendindex < mytotal ) {
      out.println("");
      outButton(inRequest,out,"next_page","nextpage(" +
                (mystartindex + mypagesize) + ")");
    }
    out.println("");
    outButton(inRequest,out,"all","set_all();");
    out.println("\t\t</TD>");
    out.println("\t<TR></TABLE>");
    out.println("\t<SCRIPT LANGUAGE='javascript'>\n");
    out.println("\t\tfunction see_set_all()");
    out.println("\t\t{");
    out.println("\t\t\tdocument.seeform.myall.value= \"yes\";");
    out.println("\t\t\tdocument.seeform.submit();");
    out.println("\t\t}");
    out.println("\t\tfunction see_setPagecount(startindex)");
    out.println("\t\t{");
    out.println("\t\t\tdocument.seeform.mystartindex.value= startindex;");
    out.println("\t\t\tdocument.seeform.myall.value= \"no\";");
    out.println("\t\t\tdocument.seeform.submit();");
    out.println("\t\t}");
    out.println("\t\tfunction see_nextpage(startindex)");
    out.println("\t\t{");
    out.println("\t\t\tdocument.seeform.mystartindex.value= startindex ;");
    out.println("\t\t\tdocument.seeform.myall.value= \"no\";");
    out.println("\t\t\tdocument.seeform.submit();");
    out.println("\t\t}");
    out.println("\t\tfunction see_lastpage(startindex)");
    out.println("\t\t{");
    out.println("\t\t\tif ( startindex < 1 ) ");
    out.println("\t\t\t\tdocument.seeform.mystartindex.value= 1 ;");
    out.println("\t\t\telse");
    out.println("\t\t\t\tdocument.seeform.mystartindex.value= startindex ;");
    out.println("\t\t\tdocument.seeform.myall.value= \"no\";");
    out.println("\t\t\tdocument.seeform.submit();");
    out.println("\t\t}");
    out.println("\t\tfunction see_setPage(pagecount,total)");
    out.println("\t\t{");
    out.println("\t\t\tdocument.seeform.input_page_size.value= pagecount;");
    out.println("\t\t\tvar pp = document.seeform.mypage.value;");
    out.println("\t\t\tpp = pagecount * pp - pagecount + 1;");
    out.println("\t\t\tif ( pp > total  ) {");
    out.println("\t\t\t\talert(\"" +
                LanguageTools.tr(inRequest,"over_limit") +
                "\");");
    out.println("\t\t\t\treturn;");
    out.println("\t\t\t}");
    out.println("\t\t\tdocument.seeform.mystartindex.value= pp;");
    out.println("\t\t\tdocument.seeform.myall.value= \"no\";");
    out.println("\t\t\tdocument.seeform.submit();");
    out.println("\t\t}");
    out.println("\t</SCRIPT>\n");

  }


  /**
   * 输出自动取值的表单域
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param myname 域名
   * @param myvalue 域值
   * @param readonly 是否只读
   */
  public static void outAutoInput(HttpServletRequest inRequest,
                                  PrintWriter out,
                                  String myname,
                                  Object myvalue,
                                  boolean  readonly) {
    if ( myname == null ) return;
    if ( myvalue == null )
      myvalue =  ServletTools.getAutoValue(inRequest);
    if ( readonly ) {
      out.println("\t\t\t" + myvalue);
      outHiddenInput(inRequest,out,myname,myvalue);
    } else
      outTextInput(inRequest,out,myname,myvalue);
  }

  /**
   * 输出客户端网络地址的表单域
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param myname 域名
   * @param myvalue 域值
   * @param readonly 是否只读
   */
  public static void outIPInput(HttpServletRequest inRequest,
                                PrintWriter out,
                                String myname,
                                Object myvalue,
                                boolean  readonly) {
    if ( myname == null ) return;
    if ( myvalue == null )
      myvalue =  ServletTools.getHost(inRequest);
    if ( readonly ) {
      out.println("\t\t\t" + myvalue);
      outHiddenInput(inRequest,out,myname,myvalue);
    } else
      outTextInput(inRequest,out,myname,myvalue);
  }

  /**
   * 输出联接的表单域
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param myname 域名
   * @param myvalue 域值
   * @param myesize 编辑宽度
   */
  public static void outLinkInput(HttpServletRequest inRequest,
                                  PrintWriter out,
                                  String myname,
                                  Object myvalue,
                                  int  myesize) {
    if ( myname == null ) return;
    String myn = null, myv =null;
    if (  myvalue != null ) {
      String[] vv = myvalue.toString().split(DataReader.HASH_SPLITTER);
      if ( vv.length == 2 ) {
        myn = vv[0];
        myv = vv[1];
      }
    }
    out.print(LanguageTools.tr(inRequest,"title"));
    outTextInput(inRequest,out,myname+ "2",myn,-1,myesize);
    out.print("<BR>" + LanguageTools.tr(inRequest,"value"));
    outTextInput(inRequest,out,myname ,myv,-1,myesize);
  }

  /**
   * 输出单选框的表单域
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param myname 域名
   * @param myvalue 域值
   * @param myvalues 可选的值
   * @param mymap 值-显示的对应
   * @param cannull 是否可为空
   * @param needTrans 是否翻译取值
   */
  public static void outRadioInput(HttpServletRequest inRequest,
                                   PrintWriter out,
                                   String myname,
                                   Object myvalue,
                                   ArrayList myvalues,
                                   Hashtable mymap,
                                   boolean  cannull,
                                   boolean  needTrans) {
    if ( (myname == null)) return;
    if ( (myvalues == null)) return;
    String myv, mytrans  ;
    for ( int i=0; i< myvalues.size(); i++) {
      myv = myvalues.get(i).toString();
      mytrans = LanguageTools.atr(inRequest,myv);
      out.print("\t\t\t<INPUT type=radio name=input_" +
                myname + " value=\"" );
      if ( needTrans ) {
        out.print(mytrans + "\" ");
        if ( mytrans.equals(myvalue) )
          out.print(" checked");
      } else {
        out.print(myv + "\" ");
        if ( myv.equals(myvalue) )
          out.print(" checked");
      }
      if ( ( mymap != null ) &&
           ( mymap.get(myv) != null) )
        out.println(">" +  LanguageTools.tr(inRequest,mymap.get(myv).toString()));
      else
        out.println(">" + LanguageTools.tr(inRequest,myv));
    }
    if (cannull)
      out.print("\t\t\t<INPUT type=radio name=input_" +
                myname + " value=  >" + LanguageTools.tr(inRequest,"null") );
  }

  /**
   * 输出多选框的表单域
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param myname 域名
   * @param myvalue 域值
   * @param myvalues 可选的值
   * @param mymap 值-显示的对应
   * @param needtrans 是否翻译取值
   */
  public static void outCheckInput(HttpServletRequest inRequest,
                                   PrintWriter out,
                                   String myname,
                                   Object myvalue,
                                   ArrayList myvalues,
                                   Hashtable mymap,
                                   boolean  needtrans) {
    if ( (myname == null)) return;
    if ( (myvalues == null)) return;
    String myaenc = ServletTools.getAppDefaultEncoding(inRequest);
    String myenc = ServletTools.getSessionEncoding(inRequest);
    ArrayList myvs = null;
    if ( myvalue != null )
      myvs = CommonTools.stringToArray(myvalue.toString(),
                                       DataReader.LIST_SPLITTER);
    if ( needtrans ) myvs = LanguageTools.atr(inRequest, myvs);
    String mytrans ,myv;
    int mysize =  myvalues.size();
    for ( int i=0; i< mysize; i++) {
      myv = myvalues.get(i).toString();
      mytrans = LanguageTools.atr(inRequest,myv);
      out.print("\t\t\t<INPUT type=checkbox name=input_" +
                myname + " value=\"" );
      if ( needtrans ) {
        out.print(mytrans + "\" ");
        if ( (myvs != null) && myvs.contains(mytrans) )
          out.print(" checked");
      } else {
        out.print(myv + "\" ");
        if ( (myvs != null) && myvs.contains(myv) )
          out.print(" checked");
      }
      if ( ( mymap != null ) &&
           ( mymap.get(myv) != null) )
        out.println(">" + LanguageTools.tr(inRequest,mymap.get(myv).toString()));
      else
        out.println(">" + LanguageTools.tr(inRequest,myv));
    }
    if ( mysize > 6 ) {
      outButton(inRequest,out,"all","all_" + myname + "()");
      out.println("\t\t ");
      outButton(inRequest,out,"all_not","allnot_" + myname + "()");
      out.println("\n\t<SCRIPT LANGUAGE='javascript'>\n");
      out.println("\t\tfunction see_all_" + myname + "()");
      out.println("\t\t{");
      out.println("\t\t for ( i = 0; i < " + mysize + " ; i++)");
      out.println("\t\t document.seeform.input_" + myname + "[i].checked = true;");
      out.println("\t\t}");
      out.println("\t\tfunction see_allnot_" + myname + "()");
      out.println("\t\t{");
      out.println("\t\t for ( i = 0; i < " + mysize + " ; i++)");
      out.println("\t\t document.seeform.input_" + myname + "[i].checked = false;");
      out.println("\t\t}");
      out.println("\t</SCRIPT>");

    }
  }

  /**
   * 输出列表框的表单域
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param myname 域名
   * @param myvalue 域值
   * @param myvalues 可选的值
   * @param mymap 值-显示的对应
   * @param ismulti 是否多选
   * @param isdrop 是否下拉
   * @param cannull 可否为空
   * @param needtrans 是否翻译取值
   */
  public static void outListInput(HttpServletRequest inRequest,
                                  PrintWriter out,
                                  String myname,
                                  Object myvalue,
                                  ArrayList myvalues,
                                  Hashtable mymap,
                                  boolean  ismulti,
                                  boolean  isdrop,
                                  boolean  cannull,
                                  boolean  needtrans) {
    if ( (myname == null)) return;
    if ( (myvalues == null)) return;
    String myaenc = ServletTools.getAppDefaultEncoding(inRequest);
    String myenc = ServletTools.getSessionEncoding(inRequest);
    ArrayList myvs = null;
    if ( ismulti ) {
      if ( myvalue != null ) {
        myvs = CommonTools.stringToArray(myvalue.toString(),
            DataReader.LIST_SPLITTER);
        if ( needtrans ) myvs = LanguageTools.atr(inRequest, myvs);
      }
    }
    String mytrans ,myv;
    out.print("\t\t\t<SELECT name=input_" + myname);
    if ( ismulti ) out.print(" multiple");
    if ( isdrop )
      out.print(" size=" + SELECT_INPUT_SIZE);
    else
      out.print(" size=1");
    out.println(">");
    for ( int i=0; i< myvalues.size(); i++) {
      myv = myvalues.get(i).toString();
      mytrans = LanguageTools.atr(inRequest,myv);
      out.print("\t\t\t\t<OPTION value=\"" );
      if ( needtrans ) {
        out.print(mytrans + "\" ");
        if ( ismulti ) {
          if ( (myvs != null) && myvs.contains(mytrans) )
            out.print(" selected");
        } else {
          if ( mytrans.equals(myvalue) )
            out.print(" selected");
        }
      } else {
        out.print(myv + "\" ");
        if ( ismulti ) {
          if ( (myvs != null) && myvs.contains(myv) )
            out.print(" selected");
        } else {
          if ( myv.equals(myvalue) )
            out.print(" selected");
        }
      }
      if ( ( mymap != null ) &&
           ( mymap.get(myv) != null) )
        out.println(">" + LanguageTools.tr(inRequest,mymap.get(myv).toString()));
      else
        out.println(">" + LanguageTools.tr(inRequest,myv) );
    }
    if (cannull)
      out.print("\t\t\t\t<OPTION value=  >" +
                LanguageTools.tr(inRequest,"null") );
    out.println("\t\t\t</SELECT>" );
  }

  /**
   * 输出时间的表单域
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param myname 域名
   * @param myvalue 域值
   * @param myconstype 约束类型
   * @param isnew 是否是创建
   * @param myesize 编辑宽度
   * @param isquery 是否是查询
   */
  public static void outDateInput(HttpServletRequest inRequest,
                                  PrintWriter out,
                                  String myname,
                                  Object myvalue,
                                  String myconstype,
                                  boolean  isnew,
                                  int  myesize,
                                  boolean  isquery) {
    if ( (myname == null)) return;
    if ( !isquery &&
         "currenttime".equals(myconstype) ||
         (isnew && "creattime".equals(myconstype) )  )
      myvalue = ServletTools.getCurrentDate(inRequest);
    if ( !isquery &&
         ("creattime".equals(myconstype) ||
         "currenttime".equals(myconstype))) {
      out.println("\t\t\t" + myvalue);
      outHiddenInput(inRequest,out,myname,myvalue);
    } else {
      outTextInput(inRequest,out,myname,myvalue,-1,myesize);
      outButton(inRequest,out,"format","set_" + myname +
                "(\"" + ServletTools.getCurrentDate(inRequest) +  "\")");
      out.println("\t<SCRIPT LANGUAGE='javascript'>\n");
      out.println("\t\tfunction see_set_" + myname + "(myv)");
      out.println("\t\t{");
      out.println("\t\t document.seeform.input_" + myname + ".value = myv;");
      out.println("\t\t}");
      out.println("\t</SCRIPT>");
    }
  }

  /**
   * 输出时间的表单域
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param myname 域名
   * @param myvalue 域值
   * @param myconstype 约束类型
   * @param isnew 是否是创建
   * @param myesize 编辑宽度
   * @param isquery 是否是查询
   */
  public static void outDateTimeInput(HttpServletRequest inRequest,
                                      PrintWriter out,
                                      String myname,
                                      Object myvalue,
                                      String myconstype,
                                      boolean  isnew,
                                      int  myesize,
                                      boolean  isquery) {
    if ( (myname == null)) return;
    if ( !isquery &&
         "currenttime".equals(myconstype) ||
         (isnew && "creattime".equals(myconstype) )  )
      myvalue = ServletTools.getCurrentTime(inRequest);
    if ( !isquery &&
         ("creattime".equals(myconstype) ||
         "currenttime".equals(myconstype))) {
      if ( myvalue != null )
        out.println("\t\t\t" + myvalue);
      else
        out.println("\t\t\t");
      outHiddenInput(inRequest,out,myname,myvalue);
    } else {
      outTextInput(inRequest,out,myname,myvalue,-1,myesize);
      outButton(inRequest,out,"format","set_" + myname +
                "(\"" + ServletTools.getCurrentTime(inRequest) +  "\")");
      out.println("\t<SCRIPT LANGUAGE='javascript'>\n");
      out.println("\t\tfunction see_set_" + myname + "(myv)");
      out.println("\t\t{");
      out.println("\t\t document.seeform.input_" + myname + ".value = myv;");
      out.println("\t\t}");
      out.println("\t</SCRIPT>");
    }
  }

  /**
   * 输出创建者的表单域
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param myname 域名
   * @param myvalue 域值
   * @param isnew 是否是创建
   * @param isquery 是否是查询
   */
  public static void outCreatUserInput(HttpServletRequest inRequest,
                                       PrintWriter out,
                                       String myname,
                                       Object myvalue,
                                       boolean  isnew,
                                       boolean  isquery) {
    if ( myname == null ) return;
    if ( !isquery ) {
      if ( isnew || (myvalue == null) )
        myvalue = ServletTools.getSessionUser(inRequest);
      out.println("\t\t\t" + myvalue);
      outHiddenInput(inRequest,out,myname,myvalue);
    } else
      outTextInput(inRequest,out,myname,null);
  }

  /**
   * 输出当前用户名的表单域
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param myname 域名
   * @param isquery 是否是查询
   */
  public static void outCurrentUserInput(HttpServletRequest inRequest,
      PrintWriter out,
      String myname,
      boolean  isquery) {
    if ( myname == null ) return;
    if ( !isquery ) {
      String myvalue = ServletTools.getSessionUser(inRequest);
      out.println("\t\t\t" + myvalue);
      outHiddenInput(inRequest,out,myname,myvalue);
    } else
      outTextInput(inRequest,out,myname,null);
  }

  /**
   * 输出隐藏的表单域
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param myname 域名
   * @param myvalue 值
   */
  public static void outHiddenInput(HttpServletRequest inRequest,
                                    PrintWriter out,
                                    String myname,
                                    Object myvalue) {
    if ( myname == null ) return;
    out.print("\t\t\t<INPUT type=hidden name=input_" +
              myname );
    if ( myvalue != null )
      out.print(" value=\"" + myvalue + "\" ");
    out.println(">");
  }

  /**
   * 输出只读的表单域
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param myname 域名
   * @param myvalue 域值
   * @param mymap 值-显示的对应
   * @param myset 数据集
   */
  public static void outReadonlyInput(HttpServletRequest inRequest,
                                      PrintWriter out,
                                      String myname,
                                      Object myvalue,
                                      Hashtable mymap,
                                      String myset) {
    if ( myname == null ) return;
    String myv = null;
    if ( (myvalue != null) ) myv = myvalue.toString();
    if ( ( mymap != null ) && ( myv != null) &&
         ( mymap.get(myv) != null) )
      myv = LanguageTools.tr(inRequest,mymap.get(myv).toString());
    if ( myv != null )
      out.println("\t\t\t" +
                  DataManagerTools.getValueShow(inRequest,myv,null,myset));
    outHiddenInput(inRequest,out,myname,myvalue);
  }

  /**
   * 输出文本的表单域
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param myname 域名
   * @param myvalue 域值
   */
  public static void outTextInput(HttpServletRequest inRequest,
                                  PrintWriter out,
                                  String myname,
                                  Object myvalue) {
    outTextInput(inRequest,out,myname,myvalue,-1,-1);
  }

  /**
   * 输出文本的表单域
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param myname 域名
   * @param myvalue 域值
   * @param mysize 域大小
   * @param myesize 编辑宽度
   */
  public static void outTextInput(HttpServletRequest inRequest,
                                  PrintWriter out,
                                  String myname,
                                  Object myvalue,
                                  int  mysize,
                                  int  myesize) {
    if ( myname == null ) return;
    out.print("\t\t\t<INPUT type=text name=\"input_" +
              myname + "\"" );
    if ( mysize > 0 )
      out.print(" maxlength=" + mysize);
    if ( myvalue != null )
      out.print(" value=\"" + myvalue + "\" ");
    if ( myesize > 0)
      out.println(" size=" +  myesize + ">");
    else
      out.println(" size=" +  MAX_INPUT_SIZE + ">");
  }

  /**
   * 输出文本段落的表单域
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param myname 域名
   * @param myvalue 域值
   * @param mysize 域大小
   * @param myesize 编辑宽度
   * @param isReadonly 是否只读
   */
  public static void outTextareaInput(HttpServletRequest inRequest,
                                      PrintWriter out,
                                      String myname,
                                      Object myvalue,
                                      int  mysize,
                                      int  myesize,
                                      boolean  isReadonly) {
    if ( myname == null ) return;
    int myrows = MAX_INPUT_ROWS;
    int mycols = myesize;
    if  ( myesize < 1)
      mycols = MAX_INPUT_COLS;
    if ( myesize == 0 )
      myrows = MAX_INPUT_ROWS * 5;

    out.print("\t\t\t<TEXTAREA name=input_" + myname +
              " cols=" + mycols +
              " rows=" + myrows);
    if ( mysize > 0 )
      out.print(" maxlength=" + mysize);
    if ( isReadonly )
      out.print(" readonly ");
    out.print(" >");
    String myv;
    if ( myvalue != null ) {
      myv = myvalue.toString();
      out.print(myv);
    }
    out.println("</TEXTAREA>");
  }

  /**
   * 输出文件的表单域
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param myname 域名
   * @param myvalue 域值
   * @param myset 数据集
   * @param myesize 编辑宽度
   */
  public static void outFileInput(HttpServletRequest inRequest,
                                  PrintWriter out,
                                  String myname,
                                  Object myvalue,
                                  String myset,
                                  int  myesize) {
    if ( myname == null ) return;
    String myn = null, myv =null;
    if (  myvalue != null ) {
      String[] vv = myvalue.toString().split(DataReader.HASH_SPLITTER);
      if ( vv.length == 2 ) {
        myn = vv[0];
        myv = vv[1];
      }
    }
    out.println("\t\t\t" + LanguageTools.tr(inRequest,"title") );
    outTextInput(inRequest,out,myname + "2",myn,-1 ,myesize);
    out.println("\t\t\t<BR>" + LanguageTools.tr(inRequest,"value"));
    if ( myesize < 0 )
      myesize = MAX_INPUT_SIZE;
    out.println("\t\t\t<INPUT type=file name=input_" +
                myname + " size=" +  myesize + ">");
    out.println("\t\t\t<BR><FONT class=info>" +
                LanguageTools.tr(inRequest,"filesize_info",
                ServletTools.getAppDefaultUploadSize(inRequest) + "") +
                "</FONT>");
    if ( myv != null ) {
      out.println("\t\t\t<BR><FONT class=info>" +
                  LanguageTools.tr(inRequest,"file_info") + "</FONT>");
      out.print("\t\t\t<BR>" + LanguageTools.tr(inRequest,"current_value") +
                " : " );
      outReadonlyInput(inRequest,out,myname + "3",
                       CommonTools.getFileName(myv),null, myset);
      out.println("\n\t\t\t<INPUT type=checkbox name=input_" +
                  myname + "4 value=yes>" +
                  LanguageTools.tr(inRequest,"remove"));
    } else
      outHiddenInput(inRequest,out,myname + "3",  null);

  }

  /**
   * 输出口令的表单域
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param myname 域名
   * @param myvalue 域值
   */
  public static void outPasswordInput(HttpServletRequest inRequest,
                                      PrintWriter out,
                                      String myname,
                                      Object myvalue) {
    if ( myname == null ) return;
    out.print("\t\t\t<INPUT type=password name=input_" +
              myname);
    out.println(">");
    out.println("\t\t\t" + LanguageTools.tr(inRequest,"input_again") + " : ");
    out.print("\t\t\t<INPUT type=password name=input_" +
              myname + "2");
    out.println(">");
    out.print("\t\t\t<INPUT type=checkbox name=input_" +
              myname + "3");
    out.println(" value=yes>" + LanguageTools.tr(inRequest,"set_null"));
    out.println("<BR><FONT class=info>" +
                LanguageTools.tr(inRequest,"password_info") + "</FONT>");
  }

  /**
   * 输出语言选择的表单域
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param myname 域名
   * @param myvalue 域值
   * @return 选择的个数
   */
  public static int outLanguageInput(HttpServletRequest inRequest,
                                      PrintWriter out,
                                      String myname,
                                      Object myvalue) {
    if ( myname == null ) return 0;
    /* 系统居然不返回“简体中文”和“繁体中文”，
    而是将“中国”和“台湾”定义为两个国家！可恶！！
    System returns "China" and "TaiWan" as two countrys!
    I hate this ugly action!!
    */
    ArrayList myv =  Language.getEncodingList();
    outRadioInput(inRequest,out,myname,myvalue,
                  myv, null ,   false  , false );
    return myv.size();
  }


  /**
   * 输出界面风格选择的表单域
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param myname 域名
   * @param myvalue 域值
   * @return 选择的个数
   */
  public static int outInterfaceStyleInput(HttpServletRequest inRequest,
      PrintWriter out,
      String myname,
      Object myvalue) {
    if ( myname == null ) return 0;
    // 从风格目录下读取所有风格文件，自动判断风格选择
    ArrayList mystyles =
        ServletTools.getDirListName(inRequest,PageTools.STYLE_PATH);
    String mystyle;
    for (int i = 0; i < mystyles.size(); i++) {
      mystyle = mystyles.get(i).toString();
      mystyle = mystyle.substring(0, mystyle.length() -  4);
      mystyles.set(i, mystyle );
    } // 界面风格文件均以.css为扩展名
    outRadioInput(inRequest,out,myname,myvalue,
                  mystyles,
                  null ,
                  false  , false );
    return mystyles.size();
  }

  /**
   * 输出界面方案选择的表单域
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param myname 域名
   * @param myvalue 域值
   * @return 选择的个数
  */
  public static int outInterfaceThemeInput(HttpServletRequest inRequest,
      PrintWriter out,
      String myname,
      Object myvalue) {
    if ( myname == null ) return 0;
    outRadioInput(inRequest,out,myname,myvalue,
                  INTERFACE_THEME,
                  null ,
                  false  , false );
    return INTERFACE_THEME.size();
  }

  /**
   * 输出数据审计类型选择的表单域
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param myname 域名
   * @param myvalue 域值
   * @return 选择的个数
   */
  public static int outDataAuditTypesInput(HttpServletRequest inRequest,
      PrintWriter out,
      String myname,
      Object myvalue) {
    if ( myname == null ) return 0;
    ArrayList myv = ServletTools.getAllDataAuditTypes();
    outCheckInput(inRequest,out,myname,myvalue,
                  myv,
                  null ,  true  );
    return myv.size();
  }

  /**
   * 输出操作类型选择的表单域
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param myname 域名
   * @param myvalue 域值
   * @param isSuperseer 是否是超级用户
   * @return 所有操作类型
   */
  public static ArrayList outAllOpearationsInput(HttpServletRequest inRequest,
      PrintWriter out,
      String myname,
      Object myvalue,
      boolean isSuperseer) {
    if ( myname == null ) return null;
    ArrayList myop = ServletTools.getAllOpearations(isSuperseer);
    outCheckInput(inRequest,out,myname,myvalue,
                  myop,  null ,  true  );
    return myop;
  }

  /**
   * 输出信息结构名称选择的表单域
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param myname 域名
   * @param myvalue 域值
   * @param isSuperseer 是否是超级用户
   * @return 所有信息结构名称
   */
  public static ArrayList outAllDataInput(HttpServletRequest inRequest,
      PrintWriter out,
      String myname,
      Object myvalue,
      boolean isSuperseer) {
    if ( myname == null ) return null;
    ArrayList mydata = ServletTools.getAllData(inRequest , isSuperseer);
    outCheckInput(inRequest,out,myname,myvalue,
                  mydata, null ,  true  );
    return mydata;
  }

  /**
   * 输出数据显示风格选择的表单域
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param myname 域名
   * @param myvalue 域值
   * @return 选择的个数
   */
  public static int outDatalistStyleInput(HttpServletRequest inRequest,
      PrintWriter out,
      String myname,
      Object myvalue) {
    if ( myname == null ) return 0;
    outRadioInput(inRequest,out,myname,myvalue,
                  DataManager.DATALIST_STYLE,
                  null ,  false  , true );
    return  DataManager.DATALIST_STYLE.size();
  }

  /**
   * 输出从其它数据表中选择数据的表单域
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param myname 域名
   * @param myvalue 域值
   * @param mycons 域值约束
   * @param mytype 引用类型。“solo”表示单选，“muilt”表示多选。
   * @param myset 数据集
   * @param myesize 编辑宽度
   */
  public static void outRefInput(HttpServletRequest inRequest,
                                 PrintWriter out,
                                 String myname,
                                 Object myvalue,
                                 String mycons,
                                 String mytype,
                                 String myset,
                                 int  myesize) {
    if ( myname == null ) return;
    if ( mycons == null ) return;
    ArrayList myc = CommonTools.stringToArray(mycons,
        DataStructureXmlParser.LIST_SPLITTER);
    if ( myc.size() < 1 ) {
      outTextInput(inRequest,out,myname,mycons,-1,myesize);
      return;
    }
    String mysett = myc.get(0).toString();
    String mywhere = "";
    if ( myc.size() > 1 )
      mywhere = myc.get(1).toString();
    String myv = "";
    if ( ( myvalue != null ) && !"".equals(myvalue)) {
      myv = DataManagerTools.getValueShow(inRequest,myvalue.toString(),null,myset);
      myv = myv.replaceAll("<BR>","\r\n");
    }
    outHiddenInput(inRequest,out,myname,myvalue);
    if ( myesize < 0 )
      myesize = MAX_INPUT_SIZE;
    if ( "solo".equals(mytype))
      out.println("\t\t\t<INPUT name=input_" + myname + "_show" +
                  " value=\"" + myv + "\" readonly " +
                  " size=" + myesize + ">");
    else
      out.println("\t\t\t<TEXTAREA name=input_" + myname + "_show" +
                  " cols=" + myesize +
                  " rows=" + MAX_INPUT_ROWS +
                  " size=" + myesize +
                  " readonly>" + myv + "</TEXTAREA>");
    outButton(inRequest,out,"select","select_" + myname +
              "(\"" + mysett + "\",\"" +
              mywhere + "\")");
    out.println("\n");
    outButton(inRequest,out,"set_null","set_" + myname +
              "(\"\")");
    out.println("\n");
    String myvv;
    if ( DataManager.PROJECT.equals(mysett) ) {
      myvv = ServletTools.getSessionProject(inRequest);
      if ( ( myvv != null ) &&
           ( !"".equals(myvv) ) )
        outButton(inRequest,out,"default","set_" + myname +
        "(\"" + myvv +  "\")");
    } else if ( DataManager.USER.equals(mysett) ) {
      myvv = ServletTools.getSessionUser(inRequest);
      outButton(inRequest,out,"self","set_" + myname +
                "(\"" + myvv +  "\")");
    }
    out.println("\n");
    out.println("\t<SCRIPT LANGUAGE='javascript'>\n");
    out.println("\t\tfunction see_select_" + myname + "(mydata,mywhere)");
    out.println("\t\t{");
    out.println("\t\t geter = document.seeform.input_" + myname + ";");
    out.println("\t\t shower = document.seeform.input_" + myname + "_show;");
    out.println("\t\t chooser = window.open(\"" +
                DataManager.URL +
                "?myframe=select&myset=" + mysett +
                "&myref=" + mytype +
                "&mywhere=" + mywhere +
                "\", " + "\"" + myname + "\"," +
                " \"toolbar=no,menubar=no,status=yes,resizable=yes,scrollbars=yes,width=600,height=500\");");
    out.println("\t\t chooser.geter = geter ;");
    out.println("\t\t chooser.shower = shower ;");
    out.println("\t\t}");
    out.println("\t\tfunction see_set_" + myname + "(myv)");
    out.println("\t\t{");
    out.println("\t\t document.seeform.input_" + myname + ".value = myv;");
    out.println("\t\t document.seeform.input_" + myname + "_show.value = myv;");
    out.println("\t\t}");
    out.println("\t</SCRIPT>");

  }

  /**
   * 输出返回按钮
   * @param myRequest 当前请求
   * @param out 当前输出
   */
  public static void outBackBar(HttpServletRequest myRequest,
                                PrintWriter out) {
    outButton(myRequest,out, "back");
    out.println("<SCRIPT LANGUAGE='javascript'>\n");
    out.println("function see_back()");
    out.println("{");
    out.println("\t window.history.back();");
    out.println("}");
    out.println("</SCRIPT>");
  }

  /**
   * 输出关闭按钮
   * @param myRequest 当前请求
   * @param out 当前输出
   */
  public static void outCloseBar(HttpServletRequest myRequest,
                                 PrintWriter out) {
    outButton(myRequest,out, "close","close_window()");
    out.println("<SCRIPT LANGUAGE='javascript'>\n");
    out.println("function see_close_window()");
    out.println("{");
    out.println("\t top.close();");
    out.println("}");
    out.println("</SCRIPT>");
  }

  /**
   * 输出按钮组：保存、重置、返回
   * @param myRequest 当前请求
   * @param myOut 当前输出
   * @param inForm 要提交的表单名
   */
  public static void outSaveResetBar(HttpServletRequest myRequest,
                                     PrintWriter myOut,
                                     String inForm) {
    outSaveResetBar(myRequest,myOut,inForm,"back");
  }

  /**
   * 输出按钮组：保存、重置、返回/关闭
   * @param myRequest 当前请求
   * @param myOut 当前输出
   * @param inForm 要提交的表单名
   * @param cancelType “close”表示输出关闭按钮，其它表示输出返回按钮。
   */
  public static void outSaveResetBar(HttpServletRequest myRequest,
                                     PrintWriter myOut,
                                     String inForm,
                                     String cancelType) {
    outSaveResetBar(myRequest,myOut,inForm,"save",cancelType);
  }

  /**
   * 输出按钮组：保存/确定、重置、返回/关闭
   * @param myRequest 当前请求
   * @param myOut 当前输出
   * @param inForm 要提交的表单名
   * @param saveType “ok”表示输出确定按钮，其它表示输出保存按钮。
   * @param cancelType “close”表示输出关闭按钮，其它表示输出返回按钮。
   */
  public static void outSaveResetBar(HttpServletRequest myRequest,
                                     PrintWriter myOut,
                                     String inForm,
                                     String saveType,
                                     String cancelType) {

    myOut.println("\t<SCRIPT LANGUAGE='javascript'>\n");
    myOut.println("\t\tfunction see_reset()");
    myOut.println("\t\t{");
    myOut.println("\t\t\tdocument." + inForm + ".reset();");
    myOut.println("\t\t}");
    if ( cancelType != null ) {
      if ( "close".equals(cancelType) )
        cancelType = "close_window";
      myOut.println("\t\tfunction see_" + cancelType + "()");
      myOut.println("\t\t{");
      if ( "close_window".equals(cancelType) ) {
        myOut.println("\t\t\t top.close();");
      } else {
        myOut.println("\t\t\t window.history.back();");
      }
      myOut.println("\t\t}");
    }
    myOut.println("\t</SCRIPT>");

    ArrayList myt = new ArrayList();
    myt.add(saveType);
    myt.add("reset");
    if ( cancelType != null )
      myt.add(cancelType);
    ArrayList mytab = new ArrayList() ;
    mytab.add("1");
    mytab.add("-1");
    if ( cancelType != null )
      mytab.add("-1");
    OutputController.outButtonBar(myRequest,myOut,myt,
                  ServletTools.getImageNames(myRequest,myt),
                  ServletTools.getClicks(myt) , null,mytab);

  }

  /**
   * 输出检查非空字符域的脚本
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param formname 要提交的表单名
   * @param mycheck 非空字符域名
   */
  public static void outCheckStringNull(HttpServletRequest inRequest,
                                        PrintWriter out,
                                        String formname,
                                        ArrayList mycheck) {
    try {
      out.println("\t\tfunction see_checkStringNull()");
      out.println("\t\t{");
      String myn,myenc = ServletTools.getSessionEncoding(inRequest);
      String myshow;
      int pos;
      for ( int i=0; i< mycheck.size(); i++) {
        myn = (String)mycheck.get(i);
        out.println("\t\t\t if ( document." + formname +
                    ".input_" + myn +
                    ".value.length < 1  ) {");
        if ( (pos = myn.indexOf(MatrixTools.NAME_SPLITTER) ) > -1 ) {
          myshow = LanguageTools.tr(inRequest,myn.substring(0, pos)) +
                   "-" +
                   LanguageTools.tr(inRequest,
                   myn.substring(pos + MatrixTools.NAME_SPLITTER.length()));
        } else
          myshow = LanguageTools.tr(inRequest,myn);
        out.println("\t\t\t\t alert(\"" +  myshow +
                    " : " +
                    LanguageTools.tr(inRequest,"should_not_null") +
                    "\");");
        out.println("\t\t\t\t return -1;");
        out.println("\t\t\t }");
      }
      out.println("\t\t\t return 1;");
      out.println("\t\t}");
    }
    catch (Exception ex) {
    }
  }

  /**
   * 输出检查非空口令域的脚本
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param formname 要提交的表单名
   * @param mycheck 非空口令域名
   */
  public static void outCheckPassword(HttpServletRequest inRequest,
                                      PrintWriter out,
                                      String formname,
                                      ArrayList mycheck) {
    try {
      out.println("\t\tfunction see_checkPassword()");
      out.println("\t\t{");
      out.println("\t\t\t var pass,apass;");
      String myn,myenc = ServletTools.getSessionEncoding(inRequest);
      String myshow;
      int pos;
      for ( int i=0; i< mycheck.size(); i++) {
        myn = (String)mycheck.get(i);
        out.println("\t\t\t\t pass = document." + formname +
                    ".input_" + myn + ".value ;");
        out.println("\t\t\t\t apass = document." + formname +
                    ".input_" + myn + "2.value ;");
        if ( (pos = myn.indexOf(MatrixTools.NAME_SPLITTER) ) > -1 ) {
          myshow = LanguageTools.tr(inRequest,myn.substring(0, pos)) +
                   "-" +
                   LanguageTools.tr(inRequest,
                   myn.substring(pos + MatrixTools.NAME_SPLITTER.length()));
        } else
          myshow = LanguageTools.tr(inRequest,myn);
        out.println("\t\t\t\t if ( pass != apass ) {");
        out.println("\t\t\t\t\t alert(\"" + myshow +
                    " : " + LanguageTools.tr(inRequest,"twice_input_different") +
                    "\");");
        out.println("\t\t\t\t\t return -1;");
        out.println("\t\t\t\t }");
      }
      out.println("\t\t\t return 1;");
      out.println("\t\t}");
    }
    catch (Exception ex) {
    }
  }

  /**
   * 输出检查联结域的脚本
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param formname 要提交的表单名
   * @param mycheck 联结域名
   */
  public static void outCheckLink(HttpServletRequest inRequest,
                                  PrintWriter out,
                                  String formname,
                                  ArrayList mycheck) {
    try {
      out.println("\t\tfunction see_checkLink()");
      out.println("\t\t{");
      out.println("\t\t\t var myname,myvalue;");
      String myn,myenc = ServletTools.getSessionEncoding(inRequest);
      String myshow;
      int pos;
      for ( int i=0; i< mycheck.size(); i++) {
        myn = (String)mycheck.get(i);
        out.println("\t\t\t\t myname = document." + formname +
                    ".input_" + myn + ".value ;");
        out.println("\t\t\t\t myvalue = document." + formname +
                    ".input_" + myn + "2.value ;");
        out.println("\t\t\t\t if ( ( myname.length > 0 ) &&" +
                    " ( myvalue.length < 1 ) ) {");
        if ( (pos = myn.indexOf(MatrixTools.NAME_SPLITTER) ) > -1 ) {
          myshow = LanguageTools.tr(inRequest,myn.substring(0, pos)) +
                   "-" +
                   LanguageTools.tr(inRequest,
                   myn.substring(pos + MatrixTools.NAME_SPLITTER.length()));
        } else
          myshow = LanguageTools.tr(inRequest,myn);
        out.println("\t\t\t\t\t alert(\"" + myshow +
                    " : " + LanguageTools.tr(inRequest,"link_input_error",
                    LanguageTools.tr(inRequest,"title")) +
                    "\");");
        out.println("\t\t\t\t\t return -1;");
        out.println("\t\t\t\t }");
        out.println("\t\t\t\t if ( ( myname.length < 1 ) &&" +
                    " ( myvalue.length > 0 ) ) {");
        out.println("\t\t\t\t\t alert(\"" + myshow +
                    " : " + LanguageTools.tr(inRequest,"link_input_error",
                    LanguageTools.tr(inRequest,"value")) +
                    "\");");
        out.println("\t\t\t\t\t return -1;");
        out.println("\t\t\t\t }");
      }
      out.println("\t\t\t return 1;");
      out.println("\t\t}");
    }
    catch (Exception ex) {
    }
  }

  /**
   * 输出检查文件域的脚本
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param formname 要提交的表单名
   * @param mycheck 文件域名
   */
  public static void outCheckFile(HttpServletRequest inRequest,
                                  PrintWriter out,
                                  String formname,
                                  ArrayList mycheck) {
    try {
      out.println("\t\tfunction see_checkFile()");
      out.println("\t\t{");
      out.println("\t\t\t var myname,myvalue;");
      String myn,myenc = ServletTools.getSessionEncoding(inRequest);
      String myshow;
      int pos;
      for ( int i=0; i< mycheck.size(); i++) {
        myn = (String)mycheck.get(i);
        out.println("\t\t\t\t myname = document." + formname +
                    ".input_" + myn + "2.value ;");
        out.println("\t\t\t\t myvalue = document." + formname +
                    ".input_" + myn + ".value ;");
        out.println("\t\t\t\t mycvalue = document." + formname +
                    ".input_" + myn + "3.value ;");
        out.println("\t\t\t\t if ( ( myname.length > 0 ) &&" +
                    " ( myvalue.length < 1 ) && " +
                    " ( mycvalue.length < 1 ) ) {");
        if ( (pos = myn.indexOf(MatrixTools.NAME_SPLITTER) ) > -1 ) {
          myshow = LanguageTools.tr(inRequest,myn.substring(0, pos)) +
                   "-" +
                   LanguageTools.tr(inRequest,
                   myn.substring(pos + MatrixTools.NAME_SPLITTER.length()));
        } else
          myshow = LanguageTools.tr(inRequest,myn);
        out.println("\t\t\t\t\t alert(\"" + myshow +
                    " : " + LanguageTools.tr(inRequest,"link_input_error",
                    LanguageTools.tr(inRequest,"value")) +
                    "\");");
        out.println("\t\t\t\t\t return -1;");
        out.println("\t\t\t\t }");
        out.println("\t\t\t\t if ( ( myname.length < 1 ) &&" +
                    " (( myvalue.length > 0 ) || " +
                    " ( mycvalue.length > 0 )) ) {");
        out.println("\t\t\t\t\t alert(\"" + myshow +
                    " : " + LanguageTools.tr(inRequest,"link_input_error",
                    LanguageTools.tr(inRequest,"title")) +
                    "\");");
        out.println("\t\t\t\t\t return -1;");
        out.println("\t\t\t\t }");
      }
      out.println("\t\t\t return 1;");
      out.println("\t\t}");
    }
    catch (Exception ex) {
    }
  }

  /**
   * 输出检查数字域的脚本
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param formname 要提交的表单名
   * @param mycheck 数字域名
   */
  public static void outCheckNumber(HttpServletRequest inRequest,
                                  PrintWriter out,
                                  String formname,
                                  ArrayList mycheck) {
    try {
      out.println("\t\tfunction see_checkNumber()");
      out.println("\t\t{");
      out.println("\t\t\t var myv;");
      out.println("\t\t\t var myvalid = \"1234567890-+Ee.\" ;");
      String myn,myenc = ServletTools.getSessionEncoding(inRequest);
      String myshow;
      int pos;
      for ( int i=0; i< mycheck.size(); i++) {
        myn = (String)mycheck.get(i);
        out.println("\t\t\t myv = document." + formname +
                    ".input_" + myn + ".value ;");
        if ( (pos = myn.indexOf(MatrixTools.NAME_SPLITTER) ) > -1 ) {
          myshow = LanguageTools.tr(inRequest,myn.substring(0, pos)) +
                   "-" +
                   LanguageTools.tr(inRequest,
                   myn.substring(pos + MatrixTools.NAME_SPLITTER.length()));
        } else
          myshow = LanguageTools.tr(inRequest,myn);
        out.println("\t\t\t for ( i = 0 ; i < myv.length ; i++  ) { ");
        out.println("\t\t\t\t if ( myvalid.indexOf( myv.substring(i,i+1)) < 0 ) { ");
        out.println("\t\t\t\t\t alert(\"" +
                   myshow + " : " +
                   LanguageTools.tr(inRequest,"can_only_include") +
                   "  1234567890-+Ee. \");");
        out.println("\t\t\t\t\t return -1;");
        out.println("\t\t\t\t }");
        out.println("\t\t\t }");
      }
      out.println("\t\t\t return 1;");
      out.println("\t\t}");
    }
    catch (Exception ex) {
    }
  }


  /**
   * 输出检查非空选择域的脚本
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param formname 要提交的表单名
   * @param mycheck 非空选择域名
   * @param isChecked 是“checked”还是“selected”
   */
  public static void outCheckSelectNull(HttpServletRequest inRequest,
                                        PrintWriter out,
                                        String formname,
                                        Hashtable mycheck,
                                        boolean   isChecked) {
    try {
      if ( isChecked )
        out.println("\t\tfunction see_checkCheckNull()");
      else
        out.println("\t\tfunction see_checkSelectNull()");
      out.println("\t\t{");
      out.println("\t\t\t  var haveS = false;");
      String myn,myenc = ServletTools.getSessionEncoding(inRequest);
      int mysize;
      String myshow;
      int pos;
      for ( Enumeration  e=mycheck.keys(); e.hasMoreElements();) {
        myn = e.nextElement().toString();
        mysize = CommonTools.stringToInt(mycheck.get(myn).toString());
        out.println("\t\t\t  haveS = false;");
        if ( mysize > 1 ) {
          out.println("\t\t\t  for ( i = 0 ; i < " + mysize + "; i++ )");
          out.println("\t\t\t  {");
          out.print("\t\t\t\t  if (document." + formname +
                    ".input_" +  myn + "[i].");
          if ( isChecked )
            out.println("checked ) ");
          else
            out.println("selected ) ");
          out.println("\t\t\t\t  {");
          out.println("\t\t\t\t\t  haveS = true;");
          out.println("\t\t\t\t\t  break;");
          out.println("\t\t\t\t  }");
          out.println("\t\t\t  }");
        } else if ( mysize == 1 ) {
          out.print("\t\t\t  if (document." + formname +
                    ".input_" +  myn );
          if ( isChecked )
            out.println(".checked ) ");
          else
            out.println(".selected ) ");
          out.println("\t\t\t\t  haveS = true;");
        }
        out.println("\t\t\t if ( !haveS ) {");
        if ( (pos = myn.indexOf(MatrixTools.NAME_SPLITTER) ) > -1 ) {
          myshow = LanguageTools.tr(inRequest,myn.substring(0, pos)) +
                   "-" +
                   LanguageTools.tr(inRequest,
                   myn.substring(pos + MatrixTools.NAME_SPLITTER.length()));
        } else
          myshow = LanguageTools.tr(inRequest,myn);
        out.println("\t\t\t\t alert(\"" + myshow +
                    " : " + LanguageTools.tr(inRequest,"should_not_null") + "\");");
        out.println("\t\t\t\t return -1;");
        out.println("\t\t\t }");
      }
      out.println("\t\t\t return 1;");
      out.println("\t\t}");

    }
    catch (Exception ex) {
    }
  }

  /**
   * 输出页签
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param mycid 当前标识
   * @param myids 所有标识
   * @param myvalues 所有值
   * @param mynames 所有名称
   * @param mywheres 所有条件
   * @param isMain 是否是页面主页签
   */
  public static void outTabpage(HttpServletRequest inRequest,
                                   PrintWriter out,
                                   Object mycid,
                                   ArrayList myids,
                                   ArrayList myvalues,
                                   ArrayList mynames,
                                   ArrayList mywheres,
                                   boolean  isMain) {
    if ( myvalues == null) return;
    String mypath = "image/" +
        ServletTools.getSessionInterfaceStyle(inRequest);
    out.println("\t <TABLE border=0 cellpadding=0 " +
                "cellspacing=0 class=whitebar><TR>");
    String myv, myss, myw ,myn ,myid ;
    String mybar = "", myclass = "tabbar";
    if ( isMain ) {
      mybar = "main_";
      myclass = "tabmain";
    }
    boolean hasv = ( myvalues != null ) &&
                   ( myvalues.size() >= myids.size() );
    boolean hasw = ( mywheres != null ) &&
                   ( mywheres.size() >= myids.size() );
    boolean hasn = ( mynames != null ) &&
                   ( mynames.size() >= myids.size() );
    for ( int i=0; i< myids.size(); i++) {
      myid = myids.get(i).toString();
      if ( hasv )
        myv = myvalues.get(i).toString();
      else
        myv = myid;
      if ( hasw )
        myw = mywheres.get(i).toString();
      else
        myw = "";
      if ( hasn )
        myn = mynames.get(i).toString();
      else
        myn = myv;
      String mygrey = "_grey";
      if ( myid.equals(mycid) )  mygrey = "";
      out.println("\t\t <TD><IMG src=\"" +  mypath +
                  "/" + mybar + "tab2" + mygrey + ".jpg\"></TD>");
      out.println("\t\t <TD class=" + myclass + mygrey + ">"  +
                  "<A onClick='see_tabselect(\"" + myid + "\",\"" +
                  myv +  "\",\"" + myw + "\")'><B>" +
                  LanguageTools.tr(inRequest,myn) + "</B></A></TD>");
      out.println("\t\t <TD><IMG src=\"" +  mypath +
                  "/" + mybar + "tab1" + mygrey + ".jpg\"><TD>");
    }
    out.println("\t </TR></TABLE>");
  }

  /**
   * 将数据显示为表行
   * @param out 当前输出
   * @param mydata 数据
   * @param rowsize 每行的列数
   * @param linedes 行描述
   * @param rowdes 列描述
   */
  public static void outTableLines(PrintWriter out,
                                   ArrayList mydata,
                                   int   rowsize,
                                   String linedes,
                                   String rowdes) {
    try {
      if ( mydata == null ) return;
      int cc = 0;
      out.println("\t\t <TR " + linedes + " >");
      for ( int i=0; i< mydata.size(); i++) {
        if ( cc >= rowsize ) {
          out.println("\t\t </TR><TR " + linedes + ">");
          cc = 0;
        }
        out.println("\t\t\t <TD " + rowdes + " >" +
                    mydata.get(i) + "</TD>");
        cc++;
      }
      for (int i = cc; i < rowsize; i++)
        out.println("\t\t\t <TD " + rowdes + " > &nbsp </TD>");
      out.println("\t\t </TR>");
    }
    catch (Exception ex) {
      return;
    }
  }

  /**
   * 获得标示数据排序方向的小图示文件名
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param isUp 是否是上箭头（有小到大方向）
   * @return 标示数据排序方向的小图示文件名
   */
  public static String getUpDownSign(HttpServletRequest inRequest,
                                   PrintWriter out,
                                   boolean  isUp) {
    try {
      StringBuffer mysign = new StringBuffer("image/");
      mysign.append(ServletTools.getSessionInterfaceStyle(inRequest));
      if ( isUp )
        mysign.append("/up.gif");
      else
        mysign.append("/down.gif");
      return mysign.toString();
    }
    catch (Exception ex) {
      return "";
    }
  }

}