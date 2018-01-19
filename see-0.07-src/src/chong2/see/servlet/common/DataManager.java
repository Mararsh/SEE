package chong2.see.servlet.common;

import chong2.see.servlet.frame.Index;
import chong2.see.data.base.Constants;
import chong2.see.data.base.*;
import chong2.see.data.*;
import chong2.see.utils.*;
import chong2.see.xml.DataStructureXmlParser;
import chong2.see.data.DataReader;
import chong2.see.xml.DataXmlWriter;

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
 * <p>通用数据处理程序，输出各类页面。
 *
 * @author 玛瑞
 * @version 0.07
 */

final public class DataManager
    extends HttpServletType1 {

  /**
   * 本servlet的页面（相对）地址
   */
  public static String URL = "data-manager";
  /**
   * “初始配置”的定义名
   */
  public static String INIT_CONFIG = "init_config";
  /**
   * “语言”的定义名
   */
  public static String LANGUAGE = "language";
  /**
   * “上传管理”的定义名
   */
  public static String UPLOAD_MANAGEMENT = "upload_management";
  /**
   * “用户”的定义名
   */
  public static String USER = "user";
  /**
   * “超级管理员”的定义名
   */
  public static String SUPER_SEER  = "superseer";
  /**
   * “数据角色”的定义名
   */
  public static String DATA_ROLE = "data_role";
  /**
   * “数据审计”的定义名
   */
  public static String DATA_AUDIT = "data_audit";
  /**
   * “数据用户权限”的定义名
   */
  public static String DATA_USER_ACL  = "data_user_acl";
  /**
   * “数据角色权限”的定义名
   */
  public static String DATA_ROLE_ACL  = "data_role_acl";
  /**
   * “系统角色”的定义名
   */
  public static String SYSTEM_ROLE  = "system_role";
  /**
   * “系统角色权限”的定义名
   */
  public static String SYSTEM_ROLE_ACL  = "system_role_acl";
  /**
   * “界面方案”的定义名
   */
  public static String INTERFACE_THEME = "interface_theme";
  /**
   * “显示设置”的定义名
   */
  public static String SHOW_SETUP  = "show_setup";
  /**
   * “查询设置”的定义名
   */
  public static String QUERY_SETUP  = "query_setup";
  /**
   * “统计设置”的定义名
   */
  public static String STATISTIC_SETUP  = "statistic_setup";
  /**
   * “项目”的定义名
   */
  public static String PROJECT  = "project";
  /**
   * “速度测试”的定义名
   */
  public static String SEE_SPEED_TESTING  = "see_speed_testing";
  /**
   * “数据源”的定义名
   */
  public static String DATA_SOURCE  = "data_source";
  /**
   * “个人消息”的定义名
   */
  public static String PERSONAL_INFORMATION  = "personal_message";
  /**
   * 预定义数据列表。不允许任何用户（包括超级管理员）修改它们的定义
   */
  public static ArrayList PREDEFINED_DATA = new ArrayList();
  static {
    PREDEFINED_DATA.add(INIT_CONFIG);
    PREDEFINED_DATA.add(INTERFACE_THEME);
    PREDEFINED_DATA.add(SUPER_SEER);
    PREDEFINED_DATA.add(SYSTEM_ROLE);
    PREDEFINED_DATA.add(SYSTEM_ROLE_ACL);
    PREDEFINED_DATA.add(DATA_ROLE);
    PREDEFINED_DATA.add(DATA_USER_ACL);
    PREDEFINED_DATA.add(DATA_ROLE_ACL);
    PREDEFINED_DATA.add(SHOW_SETUP);
    PREDEFINED_DATA.add(QUERY_SETUP);
    PREDEFINED_DATA.add(STATISTIC_SETUP);
    PREDEFINED_DATA.add(DATA_AUDIT);
    PREDEFINED_DATA.add(UPLOAD_MANAGEMENT);
    PREDEFINED_DATA.add(SEE_SPEED_TESTING);
    PREDEFINED_DATA.add(LANGUAGE);
    PREDEFINED_DATA.add(PERSONAL_INFORMATION);
    PREDEFINED_DATA.addAll(Language.getEncodingList());
  }
  /**
   * 系统数据列表。只允许超级管理员修改它们的内容
   */
  public static ArrayList SYSTEM_DATA = new ArrayList();
  static {
    SYSTEM_DATA.add(SUPER_SEER);
    SYSTEM_DATA.add(SYSTEM_ROLE);
    SYSTEM_DATA.add(SYSTEM_ROLE_ACL);
    SYSTEM_DATA.add(INTERFACE_THEME);
    SYSTEM_DATA.add(INIT_CONFIG);
    SYSTEM_DATA.add(SHOW_SETUP);
    SYSTEM_DATA.add(QUERY_SETUP);
    SYSTEM_DATA.add(STATISTIC_SETUP);
    SYSTEM_DATA.add(SEE_SPEED_TESTING);
  }
  /**
   * 超级管理员数据列表。只有超级管理员才能修改或者授权修改它们的内容
   */
  public static ArrayList SUPER_DATA = new ArrayList();
  static {
    SUPER_DATA.add(USER);
    SUPER_DATA.add(DATA_ROLE);
    SUPER_DATA.add(DATA_USER_ACL);
    SUPER_DATA.add(DATA_ROLE_ACL);
    SUPER_DATA.add(LANGUAGE);
    SUPER_DATA.add(UPLOAD_MANAGEMENT);
    SUPER_DATA.add(DATA_AUDIT);
    SUPER_DATA.add(PERSONAL_INFORMATION);
  }
  /**
   * 数据显示风格列表
   */
  public static ArrayList DATALIST_STYLE  =
      CommonTools.stringToArray("table_style,form_style",",");
  /**
   * 缺省的数据显示风格
   */
  public static String DEFAULT_DATA_STYLE =  "table_style" ;
  /**
   * 缺省的数据审计列表
   */
  public static ArrayList DEFAULT_DATA_AUDIT_TYPES =
      CommonTools.stringToArray("config,add,modify,remove,copy,insert,import",",");
  /**
   * 缺省的颜色列表（用于显示设置）
   */
  public static ArrayList DEFAULT_COLOR =
      CommonTools.stringToArray("FFDDDD,FFDD99,FFFF99,DDFFDD,DDFFFF,DDDDFF,FFDDFF",",");
  /**
   * 导出数据的格式选择列表
   */
  public static ArrayList EXPORT_FORMAT =
      CommonTools.stringToArray("txt,xml,html",",");
  /**
   * 导出数据的数据选择列表
   */
  public static ArrayList EXPORT_DATA =
      CommonTools.stringToArray("all,current",",");
  /**
   * 临时文件目录的相对路径
   */
  public static String TMP_FILE_PREFIX  =
      "tmp/";
  /**
   * 上传文件目录的相对路径
   */
  public static String UPLOAD_FILE_PREFIX  =
      "upload/";
  /**
   * 临时文件保留的时间（单位是分钟）
   */
  public static int TMP_FILE_STAY_MINUTES = 5;
  /**
   * 记录响应速度的session变量
   */
  public static String  SEE_START = "";

  /**
   * 页面主控方法
   * @param inRequest 当前请求
   * @param inResponse 当前响应
   * @throws IOException IO异常
   * @throws ServletException servlet异常
   */
  protected void processRequest(HttpServletRequest inRequest,
                                HttpServletResponse inResponse)
      throws IOException, ServletException {

    try{

      inRequest.setAttribute(SEE_START,
                             CommonTools.getCurrentTime() + "");

      PrintWriter out = inResponse.getWriter();

      String  myset =
          ServletTools.getParameter(inRequest,"myset");
      String  myframe =
          ServletTools.getParameter(inRequest,"myframe");

      DataManagerTools.clearTmpFiles(inRequest);

      // 未定义数据名
      if ( myset == null ) {
        outAskSet(inRequest,out);
        return;
      }

      // 数据框架
      if ( (myframe == null) || "".equals(myframe) ) {
        outTopFrame(inRequest,out,myset);
        return;
      }

      // 选择框架
      if ( "select".equals(myframe) ) {
        SelectTools.outSelectFrame(inRequest,out,myset);
        return;
      }

      // 选择框架的按钮页面
      if ( "getdata".equals(myframe) ) {
        SelectTools.outSelectBarPage(inRequest,out);
        return;
      }

      // 显示设置框架，注意没有检查权限
      if ( "show_setup".equals(myframe) ) {
        ShowSetupTools.outShowSetupFrame(inRequest,out,myset);
        return;
      }

      // 查询设置框架，注意没有检查权限
      if ( "query_setup".equals(myframe) ) {
        QueryTools.outQueryFrame(inRequest,out,myset);
        return;
      }

      // 导出设置框架
      if ( "export_setup".equals(myframe) ) {
        ExportTools.outExportSetupFrame(inRequest,inResponse,out,myset);
        return;
      }

      // 数据框架的操作按钮页面
      if ( "databar".equals(myframe) ) {
        outDataBarPage(inRequest,out,myset);
        return;
      }

      // 数据框架的数据显示页面
      if ( "data".equals(myframe) )
        outDataPage(inRequest,inResponse,out,myset);

      return;

    } catch (Exception ex) {
      ex.printStackTrace();
    }

  }

  /**
   * 未定义数据名，要求用户输入。
   * @param inRequest 当前请求
   * @param out 当前输出
   */
  public static void  outAskSet(HttpServletRequest inRequest,
                                PrintWriter out) {
    PageTools.outHeader(inRequest, out,"",0);
    out.println("\t<FORM name=seeform method=post " +
                " onSubmit='return false;'>");
    out.println("\t<INPUT name=myset>");
    out.println("\t<INPUT name=mybutton type=hidden>");
    out.println("\t</FORM>");
    OutputController.outSaveResetBar(inRequest,out,"seeform");
    out.println("\t<SCRIPT LANGUAGE='javascript'>\n");
    out.println("\t\tfunction see_save()");
    out.println("\t\t{");
    out.println("\t\t\tdocument.seeform.mybutton.value=\"save\";");
    out.println("\t\t\tdocument.seeform.submit();");
    out.println("\t\t}");
    out.println("\t</SCRIPT>");
    PageTools.outFooter(inRequest, out);
  }

  /**
   * 数据框架
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param myset 数据集名
   */
  public static void  outTopFrame(HttpServletRequest inRequest,
                                  PrintWriter out,
                                  String myset) {
    String myop = ServletTools.getParameter(inRequest,"myoperation");;
    String mysubop = ServletTools.getParameter(inRequest,"mysuboperation");;
    String mywhere = ServletTools.getParameter(inRequest,"mywhere");;
    String myroot = ServletTools.getParameter(inRequest,"root");
    if ( myop != null)
      myop = "&myoperation=" + myop;
    else
      myop = "";
    if ( mysubop != null)
      mysubop = "&mysuboperation=" + mysubop;
    else
      mysubop = "";
    if ( mywhere != null)
      mywhere = "&mywhere=" + mywhere;
    else
      mywhere = "";
    if ( myroot != null)
      myroot = "&root=" + myroot;
    else
      myroot = "";
    PageTools.outHeader(inRequest, out,"",-1);
    out.println("<FRAMESET border=0 frameBorder=0 frameSpacing=0 rows=23,*>");
    out.println("\t<FRAME frameBorder=0 marginHeight=1 marginWidth=1 " +
                "name=databar " +
                "src=\"" + inRequest.getRequestURI() +
                "?myframe=databar&myset=" +
                myset + myop + mysubop + mywhere + myroot + "\">");
    out.println("\t<FRAME frameBorder=0 marginHeight=10 marginWidth=1 " +
                "name=data  " +
                "src=\"" + inRequest.getRequestURI() +
                "?myframe=data" + "&myset=" +
                myset + myop + mysubop + mywhere + myroot + "\">");
    out.println("\t</FRAME>");
    out.println("</FRAMESET>");
    out.println("</HTML>");

  }



  /**
   * 数据操作按钮页面
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param myset 数据集名
   */
  public static void  outDataBarPage(HttpServletRequest inRequest,
                                     PrintWriter out,
                                     String myset) {

    PageTools.outHeader(inRequest, out,myset,
                        ServletTools.getParameter(inRequest,"myoperation"),
                        ServletTools.getParameter(inRequest,"mysuboperation"),
                        "",1);
    DataStructureXmlParser mystrucReader =
        DataManagerTools.getDataStructureReader(inRequest,myset) ;
    ArrayList setops = new ArrayList();
    if ( mystrucReader != null )
      setops = mystrucReader.getOperations();
    if ( setops == null )
      setops = new ArrayList();
    if ( DataManager.PREDEFINED_DATA.contains(myset) )
      setops.remove("config");
    else
      setops.add("config");

    ArrayList myids = new ArrayList();
    ArrayList myvalues = new ArrayList();
    ArrayList mywheres = new ArrayList();
    ArrayList mynames = new ArrayList();
    String myop,mywhere,optr;
    boolean issuper = AclTools.isSuperseer(inRequest);
    Hashtable myops = AclTools.getUserOperations(inRequest);
    if ( PERSONAL_INFORMATION.equals(myset)) {
      myids.add("add");
      myvalues.add("add");
      mynames.add("send");
      mywheres.add("");
      myids.add("not_read");
      myvalues.add("query");
      mynames.add("not_read");
      mywheres.add("receiver:v:h-v:h::c:i:d-c:i:d:_self:v:h-v:h:have_read:v:h-v:h:" +
                   LanguageTools.atr(inRequest,"no"));
      myids.add("receive");
      myvalues.add("query");
      mynames.add("receive");
      mywheres.add("receiver:v:h-v:h::c:i:d-c:i:d:_self");
      myids.add("sent");
      myvalues.add("query");
      mynames.add("sent");
      mywheres.add("sender:v:h-v:h:_self");
      myids.add("query");
      myvalues.add("query");
      mynames.add("query");
      if ( issuper )
        mywheres.add("");
      else
        mywheres.add("sender:v:h-v:h:_self:m:s-m:s:receiver:v:h-v:h::c:i:d-c:i:d:_self");
      myids.add("remove");
      myvalues.add("remove");
      mynames.add("remove");
      if ( issuper )
        mywheres.add("");
      else
        mywheres.add("receiver:v:h-v:h::c:i:d-c:i:d:_self");
    } else {
      for ( int i=0; i< DataStructure.DATASET_OPERATION_TYPES.size(); i++) {
        myop = DataStructure.DATASET_OPERATION_TYPES.get(i).toString();
        if ( !setops.contains(myop) ) continue;
        optr = atr(inRequest,myop);
        if ( !"help".equals(myop) &&
             ( myops.get("*") == null ) &&
             ( myops.get(optr) == null ) )
          continue;
        if ( "help".equals(myop) )
          mywhere = "";
        else if ( myops.get(optr) != null)
          mywhere= myops.get(optr).toString();
        else
          mywhere= myops.get("*").toString();
        myids.add(myop);
        myvalues.add(myop);
        mynames.add(myop);
        mywheres.add(mywhere);
      }
    }

    out.println("\t<FORM name=seeform  method=post " +
              " action=\"" + inRequest.getRequestURI() +
              " \" onSubmit='return false;'>");
    out.println("\t<INPUT type=hidden name=myframe " +
                " value=databar onClick='see_tabselect(\"config\",\"config\",\"\")'>");
    out.println("\t<INPUT type=hidden name=myset value=" +
                myset + "> ");
    out.println("\t<INPUT type=hidden name=myoperation value= > ");
    out.println("\t<INPUT type=hidden name=mysuboperation value= > ");
    out.println("\t<INPUT type=hidden name=myauditshow value= > ");
    out.println("\t<INPUT type=hidden name=mywhere value= > ");
    String  myselect =
        ServletTools.getParameter(inRequest,"mybselect");
    String  myoperation =
        ServletTools.getParameter(inRequest,"mysuboperation");
    if ( myoperation == null )
      myoperation =
          ServletTools.getParameter(inRequest,"myoperation");
    if (myselect==null) {
      if (myoperation!=null )
        myselect = myoperation;
      else
        myselect = "";
    }
    String mytselect = myselect;
    if ( "configsave".equals(myselect))
      mytselect = "config";
    out.println("\t<INPUT type=hidden name=mybselect value=\"" +
                mytselect + "\">");
    OutputController.outTabpage(inRequest,out,
                                mytselect, myids, myvalues,
                                mynames,mywheres,true);
    out.println("\t</FORM>\n");

    out.println("\n\t<SCRIPT LANGUAGE='javascript'>\n");
    out.println("\t\t function see_tabselect(myselect,myop,mywhere)");
    out.println("\t\t {");
    out.println("\t\t\t document.seeform.myset.value = \"" +
                myset + "\";");
    out.println("\t\t\t if ( false ) ");
    out.println("\t\t\t\t i=1;");
    String mynop;
    for (int i=0; i< myvalues.size(); i++) {
      mynop = myvalues.get(i).toString();
      out.println("\t\t\t else if ( myop == \"" + mynop + "\") ");
      out.println("\t\t\t\t {" );
      out.println("\t\t\t\t document.seeform.myframe.value = \"data\";");
      if ( "audit".equals(mynop) ) {
        out.println("\t\t\t\t document.seeform.myset.value = \"" +
                    DATA_AUDIT + "\";");
        out.println("\t\t\t\t document.seeform.myauditshow.value = \"" +
                    myset + "\";");
       }
       out.println("\t\t\t\t document.seeform.myoperation.value = \"" +
                   mynop + "\";");;
       out.println("\t\t\t\t }" );
    }
    out.println("\t\t\t document.seeform.mysuboperation.value = myselect;");;
    out.println("\t\t\t document.seeform.mywhere.value = mywhere;");;
    out.println("\t\t\t document.seeform.myframe.value = \"data\";");
    out.println("\t\t\t document.seeform.target = \"data\";");;
    out.println("\t\t\t document.seeform.submit();");
    out.println("\t\t\t document.seeform.mybselect.value=myselect;");
    out.println("\t\t\t document.seeform.target = \"databar\";");;
    out.println("\t\t\t document.seeform.myoperation.value = \"\";");;
    out.println("\t\t\t document.seeform.mysuboperation.value = \"\";");;
    out.println("\t\t\t document.seeform.mywhere.value = \"\";");;
    out.println("\t\t\t document.seeform.myframe.value = \"databar\";");;
    out.println("\t\t\t document.seeform.myset.value = \"" +
                myset + "\";");
    out.println("\t\t\t document.seeform.submit();");
    out.println("\t\t}");

    if ( DataManager.PERSONAL_INFORMATION.equals(myset) ) {
      out.println("\t\t\t parent.parent.bottom.location.reload(); ");
     }
    out.println("\t</SCRIPT>\n");

    PageTools.outFooter(inRequest, out ,1);

  }

  /**
   * 数据显示页面
   * @param inRequest 当前请求
   * @param inResponse 当前响应
   * @param out 当前输出
   * @param myset 数据集名
   */
  public static void outDataPage(HttpServletRequest inRequest,
                                 HttpServletResponse inResponse,
                                 PrintWriter out,
                                 String myset) {


    String  myoperation =
        ServletTools.getParameter(inRequest,"myoperation");
    String  mybutton =
        ServletTools.getParameter(inRequest,"mybutton");

    DataStructureXmlParser mystrucReader =
        DataManagerTools.getDataStructureReader(inRequest,myset) ;
    //对于没有定义的信息结构放行
//    if ( mystrucReader == null )  {
//          PageTools.outErrorPage(inRequest,inResponse,null,
//                                    "invalid_data", myset,"");
//          return;
//    }

    if ( !AclTools.checkAcl(inRequest,myset,mystrucReader,
                            ServletTools.getSessionUser(inRequest),
                            myoperation) ) {
      PageTools.outErrorPage(inRequest,inResponse,"no_permission",
                             myset, myoperation,"");
      return;
    }

    String ret = "";

    // 添加
    if ( "add".equals(myoperation)) {
      if ( "save".equals(mybutton)) {
        ret = saveRecord(inRequest,mystrucReader, myset,"add");
        if ( Constants.SUCCESSFUL.equals(ret) ) {
          PageTools.outInfoPage(inRequest,inResponse,Constants.SUCCESSFUL,
                                "add",null,"back");
          return;
        } else {
          PageTools.outErrorWindow(out,ret,"save_failure_des");
          PageTools.outBackPage(out);
          return;
        }
      } else {
          ret = OutputRecordTools.outRecord(inRequest,out,
              mystrucReader,myset,"add");
      }

      //删除
    } else if ( "remove".equals(myoperation)) {
      if ( "remove".equals(mybutton) ) {
        ret = removeRecord(inRequest,mystrucReader, myset);
        if ( Constants.SUCCESSFUL.equals(ret) ) {
          PageTools.outSuccessWindow(out,"remove","");
        } else {
          PageTools.outErrorWindow(out,ret,"remove_failure_des");
        }
      }
      ret = OutputListTools.outList(inRequest,out,mystrucReader,myset,"remove");

      //修改
    } else if ( "modify".equals(myoperation)) {
      if ( "save".equals(mybutton)) {
        ret = saveRecord(inRequest,mystrucReader, myset,"modify");
        if ( Constants.SUCCESSFUL.equals(ret) ) {
          PageTools.outInfoPage(inRequest,inResponse,Constants.SUCCESSFUL,
                                "modify",null,"back");
          return;
        } else {
          PageTools.outErrorWindow(out,ret,"save_failure_des");
          PageTools.outBackPage(out);
          return;
        }
      } else if ( "modify".equals(mybutton)) {
        ret = OutputRecordTools.outRecord(inRequest,out,mystrucReader,myset,"modify");
      } else
        ret = OutputListTools.outList(inRequest,out,mystrucReader,myset,"modify");

      //批量修改
    } else if ( "batch_modify".equals(myoperation)) {
      if ( "save".equals(mybutton)) {
        ret = saveRecord(inRequest,mystrucReader, myset,"batch_modify");
        if ( Constants.SUCCESSFUL.equals(ret) ) {
          PageTools.outInfoPage(inRequest,inResponse,Constants.SUCCESSFUL,
                                "modify",null,"back");
          return;
        } else {
          PageTools.outErrorWindow(out,ret,"save_failure_des");
          PageTools.outBackPage(out);
          return;
        }
      } else if ( "batch_modify".equals(mybutton)) {
        ret = OutputRecordTools.outRecord(inRequest,out,mystrucReader,myset,"batch_modify");
      } else {
        ret = OutputListTools.outList(inRequest,out,mystrucReader,myset,"batch_modify");
      }

      //复制
    } else if ( "copy".equals(myoperation)) {
      if ( "save".equals(mybutton)) {
        ret = saveRecord(inRequest,mystrucReader, myset,"copy");
        if ( Constants.SUCCESSFUL.equals(ret) ) {
          PageTools.outInfoPage(inRequest,inResponse,Constants.SUCCESSFUL,
                                "copy",null,"back");
          return;
        } else {
          PageTools.outErrorWindow(out,ret,"save_failure_des");
          PageTools.outBackPage(out);
          return;
        }
      } else if ( "copy".equals(mybutton)) {
        ret = OutputRecordTools.outRecord(inRequest,out,mystrucReader,myset,"copy");
      } else
        ret = OutputListTools.outList(inRequest,out,mystrucReader,myset,"copy");

      //插入
    } else if ( "insert".equals(myoperation)) {
      if ( "save".equals(mybutton)) {
        ret = saveRecord(inRequest,mystrucReader, myset,"insert");
        if ( Constants.SUCCESSFUL.equals(ret) ) {
          PageTools.outInfoPage(inRequest,inResponse,Constants.SUCCESSFUL,
                                "insert",null,"back");
          return;
        } else {
          PageTools.outErrorWindow(out,ret,"save_failure_des");
          PageTools.outBackPage(out);
          return;
        }
      } else if ( "insert".equals(mybutton)) {
        ret = OutputRecordTools.outRecord(inRequest,out,mystrucReader,myset,"add");
      } else
        ret = OutputListTools.outList(inRequest,out,mystrucReader,myset,"insert");

      //导入
    } else if ( "import".equals(myoperation)) {

      ImportTools.outImportSetupFrame(inRequest,inResponse,out,mystrucReader,
                                      myset,mybutton);
      return;

      //查询
    } else if ( "query".equals(myoperation)) {
      if ( "show".equals(mybutton))
        ret = OutputRecordTools.outRecord(inRequest,out,mystrucReader,myset,"show");
      else if ( "show_blank".equals(mybutton) )
        ret = OutputRecordTools.outRecord(inRequest,out,mystrucReader,myset,"show_blank");
      else if ( "select".equals(mybutton) )
        ret = OutputListTools.outList(inRequest,out,mystrucReader,myset,"select");
      else
        ret = OutputListTools.outList(inRequest,out,mystrucReader,myset,"show");

      //统计
    } else if ( "statistic".equals(myoperation)) {
        ret = StatisticTools.outStatisticFrame(inRequest,out,
            mystrucReader,myset);

      //选择
    } else if ( "select".equals(myoperation)) {
      ret = OutputListTools.outList(inRequest,out,mystrucReader,myset,"select");

      //审计
    } else if ( "audit".equals(myoperation)) {
      if ( "audit".equals(mybutton) ) {
        ret = OutputRecordTools.outRecord(inRequest,out,mystrucReader,myset,"audit");
      } else
        ret = OutputListTools.outList(inRequest,out,mystrucReader,myset,"audit");

      //配置
    } else if ( "config".equals(myoperation)) {
      // 数据框架的数据显示页面
      DataStructureTools.outStructurePage(inRequest,inResponse);
      return;

      //帮助
    } else {
      String  myroot =
          ServletTools.getParameter(inRequest,"root");
      String myinfo = "";
      if ( mystrucReader != null )
        myinfo = mystrucReader.getDescription();
      if ( SEE_SPEED_TESTING.equals(myset) )
        Index.outSpeedTesting(inRequest,out);
      else
        PageTools.outInfoPage(inRequest,inResponse,myset,
                              myinfo,
                              "data_manager_guide",
                              "see_menu_level_" + myroot + "_stop");
      return;
    }

    if ( !Constants.SUCCESSFUL.equals(ret) ) {
      PageTools.outErrorPage(inRequest,inResponse,null,ret,"","");
      Hashtable myw = null;
      if ( ServletTools.getParameter(inRequest,"mywhere") != null )
        myw = DataManagerTools.getWhere(inRequest);
      AuditTools.addAudit(inRequest,myset,
                          myoperation,
                          myw, null , null, ret,null,null);
      return;
    }

    PageTools.outFooter(inRequest, out);

  }


  /**
   * 数据显示页面的初始化设置
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param mystrucReader 数据集的结构信息
   */
  public static void initOutput(HttpServletRequest inRequest,
                                PrintWriter out,
                                DataStructureXmlParser mystrucReader) {
    initOutput(inRequest,out,mystrucReader,null);
  }

  /**
   * 数据显示页面的初始化设置
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param mystrucReader 数据集的结构信息
   * @param myop 当前操作
   */
  public static void initOutput(HttpServletRequest inRequest,
                                PrintWriter out,
                                DataStructureXmlParser mystrucReader,
                                String myop) {

    String  myframe = ServletTools.getParameter(inRequest,"myframe");
    if ( (myframe == null) || "".equals(myframe))
      myframe = "data";
    String  myset = ServletTools.getParameter(inRequest,"myset");
    if ( (myset == null) || "".equals(myset))  return;
    String  mytrueset = ServletTools.getParameter(inRequest,"mytrueset");
    if ( mytrueset == null ) mytrueset = "";
    String  myoperation = myop;
    if ( myoperation == null )
      myoperation = ServletTools.getParameter(inRequest,"myoperation");
    if ( myoperation == null ) myoperation = "";
    String  mytrueoperation = ServletTools.getParameter(inRequest,"mytrueoperation");
    if ( mytrueoperation == null ) mytrueoperation = "";
    String  mysuboperation = ServletTools.getParameter(inRequest,"mysuboperation");
    if ( mysuboperation == null ) mysuboperation = "";
    String  mybutton = ServletTools.getParameter(inRequest,"mybutton");
    if ( mybutton == null ) mybutton = "";
    String  myproject = ServletTools.getParameter(inRequest,"myproject");
    if ( myproject == null ) myproject = "";
    String  mykey = ServletTools.getParameter(inRequest,"mykey");
    if ( mykey == null ) mykey = "";
    String  mykeys = ServletTools.getParameter(inRequest,"mykeys");
    if ( mykeys == null ) mykeys = "";
    String  mywhere = ServletTools.getParameter(inRequest,"mywhere");
    if ( mywhere == null ) mywhere = "";
    String  mysetwhere = ServletTools.getParameter(inRequest,"mysetwhere");
    if ( mysetwhere == null ) mysetwhere = "";
    String  myitems = ServletTools.getParameter(inRequest,"myitems");
    if ( myitems == null ) myitems = "";
    String  mycolor = ServletTools.getParameter(inRequest,"mycolor");
    if ( mycolor == null ) mycolor = "";
    String  myorder = ServletTools.getParameter(inRequest,"myorder");
    if ( myorder == null ) myorder = "";
    String  mysort = ServletTools.getParameter(inRequest,"mysort");
    if ( mysort == null ) mysort = "";
    String mystartindex = ServletTools.getParameter(inRequest,"mystartindex");
    if ( mystartindex == null ) mystartindex = "";
    String myall = ServletTools.getParameter(inRequest,"myall");
    if ( myall == null ) myall = "";
    String  mystyle =
        ServletTools.setSessionDatalistStyle(inRequest,"mystyle");
    if ( mystyle == null ) mystyle = "";
    String  myref = ServletTools.getParameter(inRequest,"myref");
    if ( myref == null ) myref = "";
    String  mymatrix = ServletTools.getParameter(inRequest,"mymatrix");
    if ( mymatrix == null ) mymatrix = "";

    String myn = myset;
    if ( ServletTools.getParameter(inRequest,"myauditshow") != null )
      myn = ServletTools.getParameter(inRequest,"myauditshow");
    else if ( ServletTools.getParameter(inRequest,"mytrueset") != null )
      myn = ServletTools.getParameter(inRequest,"mytrueset");
    if ( mystrucReader.getDataSet().get("displayName") != null )
      myn = mystrucReader.getDataSet().get("displayName").toString();
    String myd = "";
    if ( mystrucReader.getDataSet().get("description") != null )
      myd = mystrucReader.getDataSet().get("description").toString();
    PageTools.outHeader(inRequest, out,myn,myoperation,mysuboperation,myd,2);

    /*
    当form定义为method=post时，以get方式传进来的数值在脚本里修改无效！
    原因：form没有定义提交地址，于是提交后系统使用的仍然是get方法的url，
    其中包含传进来的数值，覆盖了post中修改的值！
    解决：在form中明确加上提交地址
    */
    out.print("\t<FORM name=seeform  method=post " +
              " action=\"" + inRequest.getRequestURI() + "\"" );
    if ( "add".equals(myoperation) ||
         "insert".equals(myoperation) ||
         "copy".equals(myoperation) ||
         "modify".equals(myoperation) ) {
      if ( mystrucReader.hasFileType() )
        out.print(" enctype=\"multipart/form-data\" ");
    }
    if ( "import".equals(myoperation) ) {
      out.print(" enctype=\"multipart/form-data\" ");
    }
    out.println(" onSubmit='return false;'>");
    out.println("\t<INPUT type=hidden name=myframe value=" +
                myframe + ">");
    out.println("\t<INPUT type=hidden name=mytrueset value=" +
                mytrueset + "> ");
    out.println("\t<INPUT type=hidden name=myset value=" +
                myset + "> ");
    out.println("\t<INPUT type=hidden name=myoperation " +
                "value=\"" + myoperation + "\" " + ">");
    out.println("\t<INPUT type=hidden name=mytrueoperation " +
                "value=\"" + mytrueoperation + "\" " + ">");
    out.println("\t<INPUT type=hidden name=mysuboperation " +
                "value=\"" + mysuboperation + "\" " + ">");
    out.println("\t<INPUT type=hidden name=mybutton " +
                "value=\"" + mybutton + "\" " + ">");
    out.println("\t<INPUT type=hidden name=myproject " +
                "value=\"" + myproject + "\" " + ">");
    out.println("\t<INPUT type=hidden name=mysort " +
                "value=\"" + mysort + "\" " + ">");
    out.println("\t<INPUT type=hidden name=myorder " +
                "value=\"" + myorder + "\" " + ">");
    out.println("\t<INPUT type=hidden name=myitems " +
                "value=\"" + myitems + "\" " + ">");
    out.println("\t<INPUT type=hidden name=mycolor " +
                "value=\"" + mycolor + "\" " + ">");
    out.println("\t<INPUT type=hidden name=mywhere " +
                "value=\"" + mywhere + "\" " + ">");
    out.println("\t<INPUT type=hidden name=mysetwhere " +
                "value=\"" + mysetwhere + "\" " + ">");
    out.println("\t<INPUT type=hidden name=mykeys " +
                "value=\"" + mykeys + "\" " + ">");
    out.println("\t<INPUT type=hidden name=mykey " +
                "value=\"" + mykey + "\" " + ">");
    out.println("\t<INPUT type=hidden name=mystyle " +
                "value=\"" + mystyle + "\" " + ">");
    out.println("\t<INPUT type=hidden name=mystartindex " +
                "value=\"" + mystartindex + "\" " + ">");
    out.println("\t<INPUT type=hidden name=myall " +
                "value=\"" + myall + "\" " + ">");
    out.println("\t<INPUT type=hidden name=myref " +
                "value=\"" + myref + "\" " + ">");
    out.println("\t<INPUT type=hidden name=mymatrix " +
                "value=\"" + mymatrix + "\" " + ">");

    //加上以下脚本是防止浏览器多管闲事，在页面返回时把其它网页上的值带入。
    //例如，上次操作的mybutton至会遗留到此次，虽然本次实际已初始化了mybutton,却无效。
    out.println("\t<SCRIPT LANGUAGE='javascript'>\n");
    out.println("\t function see_init_parameters() {\n");
    out.println("\t\t document.seeform.myframe.value= \"" +
                myframe + "\";");
    out.println("\t\t document.seeform.mytrueset.value= \"" +
                mytrueset + "\";");
    out.println("\t\t document.seeform.myset.value= \"" +
                myset + "\";");
    out.println("\t\t document.seeform.myoperation.value= \"" +
                myoperation + "\";");
    out.println("\t\t document.seeform.mytrueoperation.value= \"" +
                mytrueoperation + "\";");
    out.println("\t\t document.seeform.mybutton.value= \"" +
                mybutton + "\";");
    out.println("\t\t document.seeform.myproject.value= \"" +
                myproject + "\";");
    out.println("\t\t document.seeform.mysort.value= \"" +
                mysort + "\";");
    out.println("\t\t document.seeform.myorder.value= \"" +
                myorder + "\";");
    out.println("\t\t document.seeform.myitems.value= \"" +
                myitems + "\";");
    out.println("\t\t document.seeform.mycolor.value= \"" +
                mycolor + "\";");
    out.println("\t\t document.seeform.mywhere.value= \"" +
                mywhere + "\";");
    out.println("\t\t document.seeform.mysetwhere.value= \"" +
                mysetwhere + "\";");
    out.println("\t\t document.seeform.mykeys.value= \"" +
                mykeys + "\";");
    out.println("\t\t document.seeform.mykey.value= \"" +
                mykey + "\";");
    out.println("\t\t document.seeform.mystyle.value= \"" +
                mystyle + "\";");
    out.println("\t\t document.seeform.mystartindex.value= \"" +
                mystartindex + "\";");
    out.println("\t\t document.seeform.myall.value= \"" +
                myall + "\";");
    out.println("\t\t document.seeform.myref.value= \"" +
                myref + "\";");
    out.println("\t\t document.seeform.mymatrix.value= \"" +
                mymatrix + "\";");
    out.println("\t }\n");
    out.println("\t</SCRIPT>\n");

  }

  /**
   * 保存数据记录
   * @param inRequest 当前请求
   * @param mystrucReader 数据集结构
   * @param myset 数据集名
   * @param mysave 操作类型
   * @return 是否保存成功
   */
  public static String saveRecord(HttpServletRequest inRequest,
                                  DataStructureXmlParser mystrucReader,
                                  String myset,
                                  String mysave) {

    try {
      if ( mystrucReader == null )
        return "invalid_data";

      ArrayList itemnames = mystrucReader.getEditItemNames();
      if ( itemnames == null ) return "invalid_data";
      ArrayList notnulls = mystrucReader.getNotNullNames();
      ArrayList keys = mystrucReader.getKeys();
      ArrayList mypass = mystrucReader.getPasswordItems();

      Hashtable mydata =
          ServletTools.getParameters(inRequest,mystrucReader,mysave);
      if ( !"batch_modify".equals(mysave) ) {
        if ( (mydata == null) ||
             (mydata.isEmpty())  )
          return "invalid_data";
      }

      DataReader mydataReader =
          DataManagerTools.getDataReader( inRequest,mystrucReader,
          myset,null,null,1,-1);
      if ( mydataReader == null  ) return "invalid_data";

      String mywherevalue = ServletTools.getParameter(inRequest,"mykeys");
      ArrayList myks = null;
      Hashtable myold = new Hashtable();
      Hashtable mykeyvalues = null;
      String mybasewhere =
          ServletTools.getParameter(inRequest,"mywhere");
      ArrayList mybasek = null;
      boolean modifysolo = true;
      if ( mybasewhere != null )
          mybasek = DataManagerTools.getMultiWhere(inRequest,mybasewhere);
      if ( "*".equals(mywherevalue)  &&
           ( mybasek != null) )   {
        myks = mybasek;
        modifysolo = false;
      } else if ( ( mywherevalue != null) &&
           !"*".equals(mywherevalue) &&
           !"".equals(mywherevalue) )
        myks = DataManagerTools.getMultiWhere(inRequest,mywherevalue);
      if ( (myks != null) &&
          myks.size() > 0 )
        mykeyvalues = (Hashtable)myks.get(0);
      if ( mykeyvalues == null ) {
        mykeyvalues = DataReaderGetor.getRecordKey(keys,mydata);
        if ( mykeyvalues == null )
          mykeyvalues = new Hashtable();
        myks =  new ArrayList();
        myks.add(mykeyvalues);
      }

      if ( "batch_modify".equals(mysave) &&
           (mydata != null) &&
           !mydata.isEmpty() ) {
        if ( "*".equals(mywherevalue) &
           ( mybasek != null)  ) {
          mydataReader.modifyAllRecord(itemnames,notnulls,keys,
              mydata, mypass);
        } else if ( mykeyvalues == null ) {
          return "invalid_data";
        } else {
          for ( int i=0; i < myks.size(); i++) {
            mykeyvalues = (Hashtable)myks.get(i);
            if ( mykeyvalues == null )  continue;
            Object mym =
                mydataReader.modifyValidRecord(mydataReader.getRecords(),
                itemnames,notnulls,keys,
                mykeyvalues,mydata, mypass, true, true);
            if ( mym == null)
              return "invalid_data";
          }
        }

      } else if ( mykeyvalues == null ) {
        return "invalid_data";

      } else if ( "add".equals(mysave) ||
             "copy".equals(mysave) ) {
          myold =
              mydataReader.addValidRecord(itemnames,notnulls,keys,mydata);
      } else if ( "insert".equals(mysave) ) {
        myold =
            mydataReader.insertValidRecord(itemnames,notnulls,
            keys,mykeyvalues,mydata);
      } else if ( "modify".equals(mysave) ) {
        myold =
            mydataReader.modifyValidRecord(itemnames,notnulls,keys,
            mykeyvalues,mydata, mypass);
      }

      String retw = Constants.SUCCESSFUL;
      if ( mystrucReader.hasMatrixDefine() ) {
        String mydeal = mysave;
        ArrayList mykeys = myks;
        if ( "*".equals(mywherevalue) ) {
          mydeal = "batch_modify_all";
          mykeys = mydataReader.getRecordKeys(keys);
        }
        retw =  MatrixTools.saveMatrixRecord(inRequest,mystrucReader,
            myset,mydeal, mydata, mykeys);
        if (!Constants.SUCCESSFUL.equals(retw))
          return  retw;
      }

      if ( myold == null )
        retw =  "invalid_data";
      else {
        String fdata =
            DataManagerTools.getDataValuesFile(inRequest);
        DataXmlWriter myw = new DataXmlWriter();
        retw = myw.writeData(mydataReader.getRecords(),fdata,
                             ServletTools.getAppDefaultCharset(inRequest));
      }
      ArrayList myaudit =
          ServletTools.getAppDataAuditTypes(inRequest);
      if ( myaudit.contains(atr(inRequest,mysave))) {
        if ( "add".equals(mysave) ||
             "copy".equals(mysave)  ||
             "insert".equals(mysave) )
          AuditTools.addAudit(inRequest,myset,mysave,
          DataReaderGetor.getRecordKey(keys,myold),
          myold  , null , retw, null ,mypass);
        else if ("modify".equals(mysave) )
          AuditTools.addAudit(inRequest,myset,
                              mysave,mykeyvalues,
                              myold, mydata, retw,null , mypass);
        else if ("batch_modify".equals(mysave) &&
           (mydata != null) &&
           !mydata.isEmpty() ) {
          if ( "*".equals(mywherevalue) )
            mysave = "batch_modify_all";
          AuditTools.addAuditL(inRequest,myset,
                               mysave,myks,
                               null, mydata, retw, null ,mypass);
        }
      }

      if (Constants.SUCCESSFUL.equals(retw)) {

        if ( INIT_CONFIG.equals(myset) )
          ServletTools.setInitConfig(ServletTools.getContext(inRequest));
        else if ( Language.getEncodingList().contains(myset) )
          LanguageTools.setUserLangauge(inRequest, myset);

      }

      return retw;
    }
    catch (Exception ex) {
      return ex.toString();
    }
  }

  /**
   * 删除数据记录
   * @param inRequest 当前请求
   * @param mystrucReader 数据集结构
   * @param myset 数据集名
   * @return 是否删除成功
   */
  public static String removeRecord(HttpServletRequest inRequest,
                                    DataStructureXmlParser mystrucReader,
                                    String myset) {

    if ( mystrucReader == null )
      return "invalid_data";

    ArrayList itemnames = mystrucReader.getEditItemNames();
    if ( itemnames == null ) return "invalid_data";
    ArrayList notnulls = mystrucReader.getNotNullNames();
    ArrayList keys = mystrucReader.getKeys();
    ArrayList mypass = mystrucReader.getPasswordItems();
    ArrayList myfile = mystrucReader.getFileItems();

    DataReader mydataReader =
        DataManagerTools.getDataReader( inRequest,mystrucReader,
        myset,null,null,1,-1);
    if ( mydataReader == null  ) return "invalid_data";

    boolean ismatrix = mystrucReader.hasMatrixDefine();

    String mywherevalue =
        ServletTools.getParameter(inRequest,"mykeys");
    if ( mywherevalue == null  ) return "invalid_data";

    ArrayList myks = null;
    String mybasewhere =
        ServletTools.getParameter(inRequest,"mywhere");
    ArrayList mybasek = null;
    boolean rmsolo = true;
    if ( mybasewhere != null )
        mybasek = DataManagerTools.getMultiWhere(inRequest,mybasewhere);
    if ( "*".equals(mywherevalue)  &&
         ( mybasek != null) )   {// 带条件的“全部删除”
      myks = mybasek;
      rmsolo = false;
    } else if ( !"".equals(mywherevalue) &&
                !"*".equals(mywherevalue))
      myks = DataManagerTools.getMultiWhere(inRequest,mywherevalue);

    ArrayList mydata = null;
    String ret = Constants.SUCCESSFUL;
    ArrayList myaudit =
        ServletTools.getAppDataAuditTypes(inRequest);
    if ( "*".equals(mywherevalue) &&
         ( mybasek == null ) ) {   //全部删除的情况
      mydata = mydataReader.getRecords();
      if ( ( myfile != null) &&
           ( myfile.size() > 0) &&
           (mydata != null) ) {
        for (int i=0; i< mydata.size(); i++) {
          DataReaderGetor.removeRecordFile((Hashtable)mydata.get(i),
          myfile);
        }
      }
      mydata = new ArrayList();
      if ( ismatrix )
        MatrixTools.removeMatrixRecord(inRequest,mystrucReader,
                                       myset,null);
      if ( myaudit.contains(atr(inRequest,"remove")) )
        AuditTools.addAudit(inRequest,myset,"remove_all",
                            null, null , null, ret,null ,mypass);
    } else if ( myks != null) {
      Hashtable mykeyvalues;
      mydata =
          mydataReader.getValidRecords(itemnames,notnulls,keys);
      for (int i=0; i< myks.size(); i++) {
        mykeyvalues = (Hashtable)myks.get(i);
        mydata = mydataReader.removeValidRecord(mydata,
            mykeyvalues, myfile, rmsolo);
        if ( ismatrix )
          MatrixTools.removeMatrixRecord(inRequest,mystrucReader,
              myset,mykeyvalues);
        if ( mydata == null )
          ret = "invalid_data";
      }
      if ( myaudit.contains(atr(inRequest,"remove")) )
        AuditTools.addAuditL(inRequest,myset,"remove",
                             myks, null , null,
                             ret, null ,mypass);
    }
    if ( mydata == null )
      return "invalid_data";

    String fdata =
        DataManagerTools.getDataValuesFile(inRequest);
    DataXmlWriter myw = new DataXmlWriter();
    String retw = myw.writeData(mydata,fdata,
                                ServletTools.getAppDefaultCharset(inRequest));

    if (Constants.SUCCESSFUL.equals(retw)) {

      if ( Language.getEncodingList().contains(myset) )
        LanguageTools.setUserLangauge(inRequest, myset);

    }

    return retw;
  }

}