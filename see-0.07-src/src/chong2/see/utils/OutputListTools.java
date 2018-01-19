package chong2.see.utils;

import chong2.see.data.base.Constants;
import chong2.see.servlet.common.DataManager;
import chong2.see.xml.DataStructureXmlParser;
import chong2.see.data.DataReader;

import java.io.PrintWriter;
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
 * 与输出数据列表相关的静态方法
 *
 * @author 玛瑞
 * @version 0.07
 */

final public class OutputListTools {

  /**
   * 输出数据记录的列表
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param mystrucReader 数据集结构
   * @param myset 数据集名
   * @param myotype 操作类型
   * @return 是否显示成功
   */
  public static String outList(HttpServletRequest inRequest,
                               PrintWriter out,
                               DataStructureXmlParser mystrucReader,
                               String myset,
                               String myotype) {

    if ( mystrucReader == null )
      return "invalid_data";

    if ( DataManager.UPLOAD_MANAGEMENT.equals(myset) )
      UploadTools.writeUploadRecords(inRequest);

    String myauditshow =
        ServletTools.getParameter(inRequest,"myauditshow");
    boolean showmatrix =
        "yes".equals(ServletTools.getParameter(inRequest,"mymatrix"));

    String mysort = DataManagerTools.getSort(inRequest,mystrucReader);
    if ( mysort == null ) mysort ="";
    String myorder = DataManagerTools.getOrder(inRequest,mystrucReader);
    if ( myorder == null ) myorder ="";
    String  mystyle =
        ServletTools.setSessionDatalistStyle(inRequest,"mystyle");

    ArrayList shownames = null ;
    String myexportitem =
        ServletTools.getParameter(inRequest,"input_myexportitem");
    if ( "all".equals(myexportitem)  ) {
      if ( showmatrix )
        shownames =  mystrucReader.getAllMatrixItemNames();
      else
        shownames =  mystrucReader.getEditItemNames();
    } else {
      String myitems =
          ServletTools.getParameter(inRequest,"myitems");
      shownames = CommonTools.stringToArray(myitems,
          DataReader.LIST_SPLITTER ) ;
    }
    if ( ( shownames == null ) ||
         shownames.size() < 1  )  {
      if ( showmatrix )
        shownames =  mystrucReader.getAllMatrixItemNames();
      else
        shownames =  mystrucReader.getShowItemNames();
    }
    ArrayList mypass = mystrucReader.getPasswordItems();
    // 只有有import权限的用户在导出数据时才可见password字段
    if ( (mypass != null) &&
         (mypass.size() > 0) ) {
      if (!"xml".equals(myotype) &&
          !"txt".equals(myotype) &&
          !"html".equals(myotype) )
        shownames.removeAll(mypass);
      else if ( !AclTools.checkAcl(inRequest,myset,mystrucReader,
                                   ServletTools.getSessionUser(inRequest),
                                   "import"))
        shownames.removeAll(mypass);
    }

    if ( !"xml".equals(myotype) &&
         !"txt".equals(myotype) )
      DataManager.initOutput(inRequest,out,mystrucReader);

    String myvalue,myname,mydes;
    Hashtable myd,myi;
    ArrayList showdis = new ArrayList();
    ArrayList showdes = new ArrayList();
    ArrayList constype = new ArrayList();
    myd = mystrucReader.getHashItems();
    for (int i=0; i< shownames.size(); i++) {
      myname = shownames.get(i).toString();
      myi = (Hashtable)myd.get(myname);
      if ( myi.get("displayName") == null )
        myvalue = myname;
      else
        myvalue = myi.get("displayName").toString();
      myvalue =  LanguageTools.tr(inRequest,myvalue);
      if ( myi.get("unitage") != null )
        myvalue = myvalue + " (" +
        LanguageTools.tr(inRequest,myi.get("unitage").toString()) +
        ") ";
      showdis.add(i,myvalue);
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

    Object myquery[] =
        QueryTools.getQuerySetup(inRequest,out,myset,myauditshow);
    String myw = null, mysetw = null;
    ArrayList mywherein = null, mysetwhere = null;
    if ( myquery[0] != null )
      myw = (String)myquery[0];
    if ( myquery[1] != null )
      mywherein = (ArrayList)myquery[1];
    if ( myquery[2] != null )
      mysetw = (String)myquery[2];
    if ( myquery[3] != null )
      mysetwhere = (ArrayList)myquery[3];

    String myexportdata =
        ServletTools.getParameter(inRequest,"input_myexportdata");
    String myexportmatrix =
        ServletTools.getParameter(inRequest,"input_myexportmatrix");
    String myall =
        ServletTools.getParameter(inRequest,"myall");

    ArrayList notnulls;
    ArrayList keys =  mystrucReader.getKeys();
    ArrayList itemnames = shownames;
    String myget;
    if ( showmatrix ) {
      myget = myset + MatrixTools.MATRIX_FILE_SUFFIX ;
      keys.add(MatrixTools.MATRIX_VALUE);
      notnulls = mystrucReader.getNotNullMatrixNames();
    } else {
      myget = myset;
      itemnames = CommonTools.mergeArray(shownames,keys);
      notnulls = mystrucReader.getNotNullNames();
    }
    DataReader mydataReader ;
    if ( "all".equals(myexportdata) ||
         "yes".equals(myall) )
      mydataReader =
      DataManagerTools.getDataReader( inRequest,mystrucReader,
      myget,itemnames,mywherein,mysetwhere,0,-1);
    else
      mydataReader =
      DataManagerTools.getDataReader( inRequest,mystrucReader,
      myget,itemnames,mywherein,mysetwhere);
    if ( mydataReader == null  ) return "invalid_data";
    ArrayList mydata =
        mydataReader.getValidRecords(itemnames,notnulls,keys);
    if ( mydata == null  ) return "invalid_data";

    if ( "xml".equals(myotype) ) {
      return DataManagerTools.writeXmlFile(inRequest,out,shownames,mydata, myget);
    }

    if ( "txt".equals(myotype) ) {
      return DataManagerTools.writeTxtFile(inRequest,out,shownames,showdis,mydata, myget);
    }

    boolean isshowpm =
        "show".equals(myotype) &&
        DataManager.PERSONAL_INFORMATION.equals(myset) ;

    String mystype = myotype;
    // 设置按钮条
    if ( !"html".equals(myotype) ) {
      out.println("\t<TABLE width=100%><TR>");

      if ( "remove".equals(myotype) ) {
        out.println("\t\t<TD align=left>");
        OutputController.outButton(inRequest,out,"remove_all");
        if (  DataManager.PERSONAL_INFORMATION.equals(myset) &&
              !AclTools.isSuperseer(inRequest) )
          out.println("\t\t <Font class=info>" +
          LanguageTools.tr(inRequest,"pm_remove_info") +
          "</FONT>");
        out.println("\t\t</TD>");

      } else  if ( "batch_modify".equals(myotype) ) {
        out.println("\t\t<TD align=left>");
        out.println("\t\t\t<FONT class=info>" +
                    LanguageTools.tr(inRequest,"batch_modify_step1") +
                    "</FONT>");
        OutputController.outButton(inRequest,out,"batch_modify_all");
        out.println("\t\t</TD>");

      } else if ( "audit".equals(myotype) ) {
        out.println("\t\t<TD align=left>");
        String bydata = "", bytime = "", setname = "";
        if ( myauditshow == null ) {
          bydata = " checked ";
        } else {
          mystype = "show";
          bytime = " checked ";
        }
        if ( myauditshow == null )
          setname = myset;
        else
          setname = myauditshow;
        out.println("\t\t <INPUT type=radio name=myauditshow value=\"\" " +
                    bydata + " onClick='see_setaudit(0);'>" +
                    "<FONT class=setdata>" +
                    LanguageTools.tr(inRequest,"by_data") +
                    "</FONT>");
        out.println("\t\t <INPUT type=radio name=myauditshow value=" +
                    setname + " " + bytime +
                    " onClick='see_setaudit(1);'>" +
                    "<FONT class=setdata>" +
                    LanguageTools.tr(inRequest,"by_time") +
                    "</FONT>");
        out.println("\t\t</TD>");

      } else if ( mystrucReader.hasMatrixDefine() &&
           "show".equals(myotype) ) {
        out.println("\t\t<TD align=left>");
        String mymain = "", mymatrix = "";
        if ( showmatrix )
          mymatrix = " checked ";
        else
          mymain = " checked ";
        out.println("\t\t <INPUT type=radio value=\"no\" " +
                    mymain + " onClick='see_setmatrix(0);'>" +
                    "<FONT class=setdata>" +
                    LanguageTools.tr(inRequest,"main_data") +
                    "</FONT>");
        out.println("\t\t <INPUT type=radio value=\"yes\" " +
                    mymatrix + " onClick='see_setmatrix(1);'>" +
                    "<FONT class=setdata>" +
                    LanguageTools.tr(inRequest,"matrix_data") +
                    "</FONT>");
        out.println("\t\t</TD>");

      } else if (  isshowpm ) {

        out.println("\t\t<TD align=left class=info>");
        out.println("\t\t " +
        LanguageTools.tr(inRequest,"pm_query_info") );
        out.println("\t\t</TD>");

      }

      out.println("\t\t<TD align=right>");
      if ( "show".equals(myotype) &&
           ( mydata.size() > 0 ) )
          OutputController.outButton(inRequest,out,
                                     "export","export_setup()");
      OutputController.outButton(inRequest,out,"query_setup");
      OutputController.outButton(inRequest,out,"show_setup");
      out.println("\t\t</TD>");
      out.println("</TR></TABLE>");
    }

    //色彩区
    Hashtable mycmap =
        DataManagerTools.getColor(inRequest,mystrucReader );
    if ( mycmap != null) {
      out.println("\t<TABLE class=whitebar><TR><TD>" +
                  LanguageTools.tr(inRequest,mycmap.get("name").toString()) +
                  ":</TD>");
      String myn;
      for (Enumeration e=mycmap.keys();e.hasMoreElements();) {
        myn = e.nextElement().toString();
        if ( "name".equals(myn) || "others".equals(myn) ) continue;
        out.println("<TD bgcolor=#" + mycmap.get(myn) + ">" +
                    myn + "</TD>");
      }
      out.println("<TD bgcolor=#" +
                  mycmap.get("others") + ">" +
                  LanguageTools.atr(inRequest,"others") + "</TD>");
      out.println("\t</TR></TABLE>");
    }

    String mywhere;
    //数据区
    if ( LanguageTools.atr(inRequest,"form_style").equals(mystyle)  ) {
      mywhere = outListForm(inRequest,out,keys,shownames,showdis,showdes,
                            constype,mystype,mydata,myset,mycmap,
                            mysort,myorder);
    } else {
      mywhere = outListTable(inRequest,out,keys,shownames,showdis,showdes,
                             constype,mystype,mydata,myset,mycmap,
                             mysort,myorder);
    }

    boolean ismutliselect = "select".equals(myotype) &&
           !"solo".equals( ServletTools.getParameter(inRequest,"myref") ) ;
    //辅助按钮区
    if ( "remove".equals(myotype) ||
         "batch_modify".equals(myotype) ||
         ismutliselect ) {
      OutputController.outButton(inRequest,out,"page_all","autoselect(\"all\")");
      OutputController.outButton(inRequest,out,"all_not","autoselect(\"none\")");
    }

    // 分页操作区
    int  mystartindex = mydataReader.getStartIndex();
    int  myendindex = mydataReader.getEndIndex();
    int  mypagesize =
        CommonTools.stringToInt(ServletTools.getSessionPageSize(inRequest));
    int  mytotal = mydataReader.getCount();
    OutputController.outScrollerBar(inRequest,out,mytotal,mystartindex,
                                    myendindex,mypagesize,myotype);

    // 显示设置区
    if ( mystyle == null ) mystyle ="";
    out.println("\t<P align=left class=info>");
    if ( !"html".equals(myotype) ) {
      OutputController.outButton(inRequest,out,"show_setup");
    }
    out.print(LanguageTools.tr(inRequest,"current_show_setup") +
                " : ");
    if ( !"html".equals(myotype) )
      out.print( "(" + LanguageTools.tr(inRequest,"sort_info") + ")" );
    out.println("\t<TABLE class=bar cellPadding=2 width=100%><TR>");
    out.println("\t\t<TD align=left>" );
    out.println("\t\t<B>" +
                LanguageTools.tr(inRequest,"style") + ":</B>" +
                LanguageTools.tr(inRequest,mystyle) );
    out.println("\t\t</TD>" );
    if ( !"".equals(mysort) && !"".equals(myorder) ) {
      out.println("\t\t<TD align=left>" );
      out.print("\t\t<B>" + LanguageTools.tr(inRequest,"sort") + ":</B>" +
                LanguageTools.tr(inRequest,mysort) );
      out.println("-" + LanguageTools.tr(inRequest,myorder) );
      out.println("\t\t</TD>" );
    }
    out.println("\t\t<TD align=left>" );
    out.println("\t\t<B>" + LanguageTools.tr(inRequest,"item") + ":</B>" +
                LanguageTools.tr(inRequest,shownames) );
    out.println("\t\t</TD></TR>");
    out.println("\t</TABLE>");

    // 显示查询区
    out.println("\t<P align=left class=info>");
    if ( !"html".equals(myotype) ) {
      OutputController.outButton(inRequest,out,"query_setup");
    }
    if ( ((  myw != null) &&  !"".equals(myw)) ||
        ( ( mysetw != null) &&  !"".equals(mysetw))  )
         out.println(LanguageTools.tr(inRequest,
                                 "current_query_setup") +
                                 " : <BR>");
    if ( (  mysetw != null) &&  !"".equals(mysetw) ) {
      out.println(LanguageTools.tr(inRequest,"query_info"));
      QueryTools.outCondition(inRequest,out,mysetw,myset);
    }
    if ( (  myw != null) &&  !"".equals(myw) ) {
      out.println("\t<P align=left class=info>");
      out.println(LanguageTools.tr(inRequest,"query_baseinfo"));
      QueryTools.outCondition(inRequest,out,myw,myset);
    }
    out.println("\t</P>");

    //操作按钮区
    if ( "batch_modify".equals(myotype) ) {
      out.println("\t<P align=center class=info>");
      OutputController.outButton(inRequest,out,"next_step",
                                 "batch_modify_selected()");
      out.println("\t</P>");
    } else if ( "remove".equals(myotype) ) {
      out.println("\t<P align=center class=info>");
      OutputController.outButton(inRequest,out,"remove_selected",
                                 "remove_selected()");
      out.println("\t</P>");
    } else if ( ismutliselect ) {
      out.println("\t<P align=center class=info>");
      OutputController.outButton(inRequest,out,"select","mselect()");
      out.println("\t</P>");
    }

    out.println("</FORM> \n");

    //审计
    if ( ServletTools.getAppDataAuditTypes(inRequest).contains(LanguageTools.atr(inRequest,"list_data")) )
      AuditTools.addAuditL(inRequest,myset,"list_data",
                           mywherein,null , null,
                           Constants.SUCCESSFUL, null ,mypass);

    if ( "html".equals(myotype) )
      return Constants.SUCCESSFUL;


    //脚本
    out.println("\n\t<SCRIPT LANGUAGE='javascript'>\n");
    if ( "select".equals(myotype) ) {
      if ( !ismutliselect ) {

        out.println("\t\tfunction see_select(myvalue)");
        out.println("\t\t{");
        out.println("\t\t\t top.getdata.document.seeform.myget.value= myvalue ;");
        out.println("\t\t\t top.getdata.document.seeform.myshow.value= myvalue ;");
        out.println("\t\t}");

      } else {

        out.println("\t\tfunction see_mselect()");
        out.println("\t\t{");
        out.println("\t\t\tvar mywhere = \"\";");
        out.println("\t\t\tvar myshow = \"\";");
        if ( mydata.size() == 1) {
          out.println("\t\t\t if ( document.seeform.myselect.checked )");
          out.println("\t\t\t{");
          out.println("\t\t\t\t mywhere = " +
                      "document.seeform.myselect.value  ;");
          out.println("\t\t\t\t myshow = " +
                      "document.seeform.myselect.value  ;");
          out.println("\t\t\t}");
        } else {
          out.println("\t\t\tfor ( i = 0 ; i < " +
                      mydata.size() + " ; i++ ) {");
          out.println("\t\t\t\t if ( document.seeform.myselect[i].checked )");
          out.println("\t\t\t\t{");
          out.println("\t\t\t\t if ( mywhere.length > 0 ) mywhere = mywhere + \"" +
                      DataReader.LIST_SPLITTER + "\" ;");
          out.println("\t\t\t\t\t mywhere = mywhere + " +
                      "document.seeform.myselect[i].value  ;");
          out.println("\t\t\t\t if ( myshow.length > 0 ) myshow = myshow + \"\\r\\n\" ; ");
          out.println("\t\t\t\t\t myshow = myshow + " +
                      "document.seeform.myselect[i].value ;");
          out.println("\t\t\t\t}");
          out.println("\t\t\t}");
        }
        out.println("\t\t\tif ( mywhere == \"\" ) {");
        out.println("\t\t\t\t alert(\"" +
                    LanguageTools.tr(inRequest,"please_select") + "\");");
        out.println("\t\t\t\t return; ");
        out.println("\t\t\t } ");
        out.println("\t\t\t var old = top.getdata.document.seeform.myget.value;");
        out.println("\t\t\t if ( old.length > 0 ) ");
        out.println("\t\t\t\t  mywhere = mywhere + \"" +
                    DataReader.LIST_SPLITTER + "\" + old;");
        out.println("\t\t\t top.getdata.document.seeform.myget.value= mywhere;");
        out.println("\t\t\t old = top.getdata.document.seeform.myshow.value;");
        out.println("\t\t\t if ( old.length > 0 ) ");
        out.println("\t\t\t\t  myshow = myshow + \"\\r\\n\" + old;");
        out.println("\t\t\t top.getdata.document.seeform.myshow.value= myshow ;");
        out.println("\t\t}");
      }
    } else {

      out.println("\t\tfunction see_" + mystype + "(keys)");
      out.println("\t\t{");

      if ( "remove".equals(myotype) ) {
        out.println("\t\t\t if ( !confirm(\"" +
                    LanguageTools.tr(inRequest,"sure_remove") + "\")  )");
        out.println("\t\t\t\t return;");
      }
      if ( "audit".equals(myotype) ) {
        out.println("\t\t\t oldset = document.seeform.myset.value;");
        out.println("\t\t\t document.seeform.myset.value=\"data_audit\";");
        out.println("\t\t\t document.seeform.target = \"_blank\";");
        out.println("\t\t\t oldtset = document.seeform.mytrueset.value;");
        if ( myauditshow == null)
          out.println("\t\t\t\t document.seeform.mytrueset.value = " +
                      "document.seeform.myauditshow[1].value;");
        else
          out.println("\t\t\t\t document.seeform.mytrueset.value = \"\";");
        out.println("\t\t\t oldkeys = document.seeform.mykeys.value;");
        out.println("\t\t\t oldbu = document.seeform.mybutton.value;");
      }
      out.println("\t\t\t document.seeform.mybutton.value=\"" +
                  myotype +"\";");
      out.println("\t\t\t document.seeform.mykeys.value= keys;");
      out.println("\t\t\t document.seeform.submit();");
      out.println("\t\t\t document.seeform.mybutton.value=\"\";");
      if ( "audit".equals(myotype) ) {
        out.println("\t\t\t document.seeform.myset.value= oldset;");
        out.println("\t\t\t document.seeform.mytrueset.value = oldtset;");
        out.println("\t\t\t document.seeform.mykeys.value= oldkeys;");
        out.println("\t\t\t document.seeform.mybutton.value= oldbu;");
        out.println("\t\t\t document.seeform.target = \"\";");
      }
      out.println("\t\t }");
    }

    if ( "remove".equals(myotype) ||
         "batch_modify".equals(myotype) ) {

      out.println("\t\tfunction see_" + myotype + "_all()");
      out.println("\t\t{");
      out.println("\t\t\t if ( !confirm(\"" +
                  LanguageTools.tr(inRequest,"sure_" + myotype + "_all") + "\")  )");
      out.println("\t\t\t\t return;");
      out.println("\t\t\tdocument.seeform.mybutton.value=\"" +
                  myotype +"\";");
      out.println("\t\t\tdocument.seeform.mykeys.value= \"*\";");
      out.println("\t\t\tdocument.seeform.submit();");
      out.println("\t\t\t document.seeform.mybutton.value=\"\";");
      out.println("\t\t}");

      out.println("\t\tfunction see_" + myotype + "_selected()");
      out.println("\t\t{");
      out.println("\t\t\tvar mywhere = \"\";");
      if ( mydata.size() == 1) {
        out.println("\t\t\t if ( document.seeform.my" + myotype + ".checked )");
        out.println("\t\t\t\t mywhere = " +
                    "document.seeform.my" + myotype + ".value  ;");
      } else {
        out.println("\t\t\tfor ( i = 0 ; i < " +
                    mydata.size() + " ; i++ ) {");
        out.println("\t\t\t\t if ( document.seeform.my" + myotype + "[i].checked ) {");
        out.println("\t\t\t\t\t if ( mywhere.length > 0 ) mywhere = mywhere + \"" +
                    QueryTools.MULTI_SPLITTER + "\" ;");
        out.println("\t\t\t\t\t mywhere = mywhere + " +
                    "document.seeform.my" + myotype + "[i].value  ;");
        out.println("\t\t\t\t }");
        out.println("\t\t\t}");
      }
      out.println("\t\t\tif ( mywhere == \"\" ) {");
      out.println("\t\t\t\t alert(\"" +
                  LanguageTools.tr(inRequest,"please_select") + "\");");
      out.println("\t\t\t\t return; ");
      out.println("\t\t\t}");
      out.println("\t\t\t if ( !confirm(\"" +
                  LanguageTools.tr(inRequest,"sure_" + myotype + "_selected") + "\")  )");
      out.println("\t\t\t\t return;");
      out.println("\t\t\tdocument.seeform.mybutton.value=\"" +
                  myotype +"\";");
      out.println("\t\t\tdocument.seeform.mykeys.value= mywhere;");
      out.println("\t\t\tdocument.seeform.submit();");
      out.println("\t\t\t document.seeform.mybutton.value=\"\";");
      out.println("\t\t}");

    }

    out.println("\t\tfunction see_autoselect(stype)");
    out.println("\t\t{");
    if ( mydata.size() == 1) {
      out.println("\t\t\t  if ( \"all\" == stype )");
      out.println("\t\t\t\t  document.seeform.my" + myotype + ".checked = true;");
      out.println("\t\t\t  if ( \"none\" == stype )");
      out.println("\t\t\t\t  document.seeform.my" + myotype + ".checked = false;");
    } else {
      out.println("\t\t\t  if ( \"all\" == stype )");
      out.println("\t\t\t\t for ( i = 0 ; i < " +
                  mydata.size() + " ; i++ ) {");
      out.println("\t\t\t\t\t document.seeform.my" + myotype + "[i].checked = true;");
      out.println("\t\t\t\t }");
      out.println("\t\t\t  if ( \"none\" == stype )");
      out.println("\t\t\t\t for ( i = 0 ; i < " +
                  mydata.size() + " ; i++ ) {");
      out.println("\t\t\t\t\t document.seeform.my" + myotype + "[i].checked = false;");
      out.println("\t\t\t\t }");
    }
    out.println("\t\t}");

    if ( "show".equals(myotype) ) {
      out.println("\t\tfunction see_setmatrix(myv)");
      out.println("\t\t{");
      out.println("\t\t\t if ( myv == 0 ) ");
      out.println("\t\t\t {");
      out.println("\t\t\t\t document.seeform.mymatrix.value=\"\" ;");
      out.println("\t\t\t }");
      out.println("\t\t\t else {");
      out.println("\t\t\t\t document.seeform.mymatrix.value=\"yes\" ;");
      out.println("\t\t\t }");
      out.println("\t\t\t document.seeform.mysetwhere.value=\"\" ;");
      out.println("\t\t\t document.seeform.mybutton.value=\"\" ;");
      out.println("\t\t\t document.seeform.mycolor.value=\"\" ;");
      out.println("\t\t\t document.seeform.myitems.value=\"\" ;");
      out.println("\t\t\t document.seeform.submit();");
      out.println("\t\t}");
    }

    out.println("\t\tfunction see_sort(name)");
    out.println("\t\t{");
    out.println("\t\t\tdocument.seeform.mybutton.value=\"sort\";");
    out.println("\t\t\tdocument.seeform.mysort.value= name;");
    if ( "ascending".equals(ServletTools.getParameter(inRequest,"myorder")) )
      out.println("\t\t\tdocument.seeform.myorder.value= \"descending\";");
    else
      out.println("\t\t\tdocument.seeform.myorder.value= \"ascending\";");
    out.println("\t\t\tdocument.seeform.submit();");
    out.println("\t\t}");

    out.println("\t\tfunction see_show_blank(mywhere)");
    out.println("\t\t{");
    out.println("\t\t\t var oldf = document.seeform.myframe.value;");
    out.println("\t\t\tdocument.seeform.myframe.value=\"data\";");
    out.println("\t\t\t var oldop = document.seeform.myoperation.value;");
    out.println("\t\t\tdocument.seeform.myoperation.value=\"query\";");
    out.println("\t\t\t var oldbu = document.seeform.mybutton.value;");
    out.println("\t\t\tdocument.seeform.mybutton.value=\"show_blank\";");
    out.println("\t\t\t var oldk = document.seeform.mykeys.value;");
    out.println("\t\t\tdocument.seeform.mykeys.value= mywhere;");
    out.println("\t\t\tdocument.seeform.target = \"_blank\";");
    out.println("\t\t\tdocument.seeform.submit();");
    // 这里必须换回原值，不然其它按钮就会反应错误！
    out.println("\t\t\tdocument.seeform.target= \"\";");
    out.println("\t\t\tdocument.seeform.myframe.value= oldf;");
    out.println("\t\t\tdocument.seeform.myoperation.value= oldop;");
    out.println("\t\t\tdocument.seeform.mybutton.value= oldbu;");
    out.println("\t\t\tdocument.seeform.mykeys.value= oldk;");
    if ( DataManager.PERSONAL_INFORMATION.equals(myset) ) {
      out.println("\t\t\tdocument.seeform.mybutton.value= \"\";");
      out.println("\t\t\t parent.parent.bottom.location.reload(); ");
      out.println("\t\t\tdocument.seeform.submit();");
    }
    out.println("\t\t}");

    out.println("\t\tfunction see_setaudit(by)");
    out.println("\t\t{");
    out.println("\t\t\t if ( by == 1 ) {");
    out.println("\t\t\t\t document.seeform.myset.value = \"data_audit\";");
    out.println("\t\t\t } else {");
    out.println("\t\t\t\t document.seeform.myset.value = " +
                "document.seeform.myauditshow[1].value;");
    out.println("\t\t\t document.seeform.mywhere.value= \"\";");
    out.println("\t\t\t } ");
    out.println("\t\t\t document.seeform.mystartindex.value= 1;");
    out.println("\t\t\t document.seeform.submit();");
    out.println("\t\t}");

    out.println("\t\tfunction see_show_setup()");
    out.println("\t\t{");
    out.println("\t\t\tdocument.seeform.myframe.value=\"show_setup\";");
    out.println("\t\t\tdocument.seeform.myitems.value=\"" +
                CommonTools.arraylistToString(shownames,DataReader.LIST_SPLITTER) +
                "\";");
    out.println("\t\t\tdocument.seeform.myoperation.value=\"show_setup\";");
    out.println("\t\t\tdocument.seeform.mytrueoperation.value=\"" +
                ServletTools.getParameter(inRequest,"myoperation") +
                "\";");
    out.println("\t\t\tdocument.seeform.mybutton.value=\"\";");
    if (showmatrix)
      out.println("\t\t\tdocument.seeform.mymatrix.value=\"yes\";");
    out.println("\t\t\tdocument.seeform.submit();");
    out.println("\t\t}");

    out.println("\t\tfunction see_export_setup()");
    out.println("\t\t{");
    out.println("\t\t\t var oldf = document.seeform.myframe.value;");
    out.println("\t\t\t var oldt = document.seeform.target;");
    out.println("\t\t\tdocument.seeform.myframe.value=\"export_setup\";");
    out.println("\t\t\tdocument.seeform.target= \"_blank\";");
    out.println("\t\t\tdocument.seeform.submit();");
    // 这里必须换回原值，不然其它按钮就会反应错误！
    out.println("\t\t\tdocument.seeform.target= oldt;");
    out.println("\t\t\tdocument.seeform.myframe.value= oldf;");
    out.println("\t\t}");

    out.println("\t</SCRIPT>\n");


    return Constants.SUCCESSFUL;
  }

  /**
   * 输出数据记录的列表，表格格式
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param keys 数据关键字
   * @param shownames 要显示的域名
   * @param showdis 域的显示
   * @param showdes 域的描述
   * @param constype 数据约束类型
   * @param myotype 操作类型
   * @param mydata 数据值
   * @param myset 数据集名
   * @param mycmap 颜色的定义
   * @param mysort 排序字段
   * @param myorder 排序的方向
   * @return 是否显示成功
   */
  public static String outListTable(HttpServletRequest inRequest,
                                    PrintWriter out,
                                    ArrayList keys,
                                    ArrayList shownames,
                                    ArrayList showdis,
                                    ArrayList showdes,
                                    ArrayList constype,
                                    String myotype,
                                    ArrayList mydata,
                                    String myset,
                                    Hashtable mycmap,
                                    String mysort,
                                    String myorder) {

    out.println("\t<TABLE class=data cellPadding=2 width=100%>");
    out.println("\t\t<TR class=tableheader ><TH>-</TH>");
    String tt = "";
    for (int i=0; i< shownames.size(); i++) {
      tt = "";
      if ( shownames.get(i).equals(mysort) ) {
        tt = OutputController.getUpDownSign(inRequest,out,
            "ascending".equals(myorder));
        tt = "  <IMG src=\"" + tt + "\"> ";
      }
      out.println("<TH title=\"" + showdes.get(i) +
                  "\" border=1 align=center ><U>");
      if ( !"html".equals(myotype) )
        out.print("\t\t\t<A  onClick='see_sort(\"" +
                  shownames.get(i) + "\")'>" +
                  showdis.get(i) + "</A>");
      else
        out.println(showdis.get(i));
      out.println("</U>" + tt + "</TH>");
    }
    out.println("\t\t</TR>");

    String myn,mywhere ="",myname,myvalue;
    String mylink ="",mycolor;
    Hashtable myd;
    for (int i=0; i< mydata.size(); i++ ) {
      myd = (Hashtable)mydata.get(i);
      mywhere = "";
      mywhere = CommonTools.hashToString(myd, keys,
          QueryTools.CONDITION_SPLITTER);
      String myhwhere = new String(mywhere);
      if ( "select".equals(myotype) &&
           mywhere != null ) {
        if ( ( keys.size() == 1 ) &&
          ( myd.get(keys.get(0)) != null) )
            myhwhere = myd.get(keys.get(0)).toString();
        else
          myhwhere = DataManagerTools.getValueWhere(mywhere);
      }
      if ( mycmap != null ) {
        if  (  ( myd.get(mycmap.get("name")) != null) &&
               ( mycmap.get( myd.get(mycmap.get("name"))) != null ) )
          mycolor = mycmap.get( myd.get(mycmap.get("name"))).toString();
        else
          mycolor = mycmap.get("others").toString();
        out.println("\t\t<TR   bgcolor=#" +
                    mycolor + ">");
      } else
        out.println("\t\t<TR  class=tabledata >");
      out.println("\t\t\t<TD align=center>");
      if ( !"html".equals(myotype) )
        mylink = outListLink(inRequest,out,myotype,mywhere,myhwhere);
      out.println("</TD>");
      for (int j=0; j< shownames.size(); j++) {
        myname = shownames.get(j).toString();
        if ( myd.get(myname) == null )
          myvalue = "-";
        else
          myvalue = myd.get(myname).toString();
        out.println("\t\t\t<TD>" +
                    DataManagerTools.getValueShow(inRequest,myvalue,constype.get(j).toString(),myset) +
                    "</TD>");
      }
      out.println("\t\t</TR>");
    }
    out.println("\t</TABLE>");

    return Constants.SUCCESSFUL;

  }

  /**
   * 输出数据记录的列表，表格格式
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param keys 数据关键字
   * @param shownames 要显示的域名
   * @param showdis 域的显示
   * @param showdes 域的描述
   * @param constype 数据约束类型
   * @param myotype 操作类型
   * @param mydata 数据值
   * @param myset 数据集名
   * @param mycmap 颜色的定义
   * @param mysort 排序字段
   * @param myorder 排序的方向
   * @return 是否显示成功
   */
  public static String outListForm(HttpServletRequest inRequest,
                                   PrintWriter out,
                                   ArrayList keys,
                                   ArrayList shownames,
                                   ArrayList showdis,
                                   ArrayList showdes,
                                   ArrayList constype,
                                   String myotype,
                                   ArrayList mydata,
                                   String myset,
                                   Hashtable mycmap,
                                   String mysort,
                                   String myorder) {

    String myn,mykey = "",mywhere ="",myname,myvalue;
    String mylink,mycolor,tt;
    Hashtable myd;
    for (int i=0; i< mydata.size(); i++ ) {
      myd = (Hashtable)mydata.get(i);
      mykey = "";
      mywhere = "";
      mywhere = CommonTools.hashToString(myd, keys,
          QueryTools.CONDITION_SPLITTER);
      String myhwhere = new String(mywhere);
      if ( "select".equals(myotype) &&
           mywhere != null ) {
        if ( keys.size() == 1 )
          if ( myd.get(keys.get(0)) != null)
            myhwhere = myd.get(keys.get(0)).toString();
      }
      if ( mycmap != null ) {
        if  (  ( myd.get(mycmap.get("name")) != null) &&
               ( mycmap.get( myd.get(mycmap.get("name"))) != null ) )
          mycolor = mycmap.get( myd.get(mycmap.get("name"))).toString();
        else
          mycolor = mycmap.get("others").toString();
        mycolor = " bgcolor=#" + mycolor;
      } else
        mycolor = "";
      out.println("\t<TABLE class=data  align=center " +
                  " frame=box rules=none width=70%  " + mycolor +
                  ">");
      out.println("\t\t\t<TR><TD align=left colspan=2>");
      if ( !"html".equals(myotype) )
        mylink = outListLink(inRequest,out,myotype,mywhere,myhwhere);
      out.println("\t\t\t</TD></TR>");

      for (int j=0; j< shownames.size(); j++) {
        myname = shownames.get(j).toString();
        tt = "";
        if ( myname.equals(mysort) ) {
          tt = OutputController.getUpDownSign(inRequest,out,
              "ascending".equals(myorder));
          tt = "  <IMG src=\"" + tt + "\"> ";
        }
        out.println("<TD title=\"" + showdes.get(j) +
                    "\" align=right>" + tt + "<U>");
        if ( !"html".equals(myotype) )
          out.print("\t\t\t<A  onClick='see_sort(\"" +
                    myname + "\")'>" +
                    showdis.get(j) + "</A>");
        else
          out.println(showdis.get(j));
         out.println(" : </U></TD>");

        if ( myd.get(myname) == null )
          myvalue = "-";
        else
          myvalue = myd.get(myname).toString();
        out.println("\t\t\t<TD align=left>" +
                    DataManagerTools.getValueShow(inRequest,myvalue,constype.get(j).toString(),myset) +
                    "</TD>");
        out.println("\t\t</TR>");
      }
      out.println("\t</TABLE><BR>");
    }

    return Constants.SUCCESSFUL;

  }

  /**
   * 输出数据记录行的按钮和联接
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param myotype 操作类型
   * @param mywhere 域的显示
   * @param myhwhere 域的实际显示
   * @return 是否显示成功
   */
  public static String outListLink(HttpServletRequest inRequest,
                                   PrintWriter out,
                                   String myotype,
                                   String mywhere,
                                   String myhwhere) {

    try {
      String myw ;
      if ( "remove".equals(myotype) )
        out.println("\t\t\t\t<INPUT type=checkbox name=myremove value=\"" +
                    myhwhere + "\">");
      else if ( "batch_modify".equals(myotype) )
        out.println("\t\t\t\t<INPUT type=checkbox name=mybatch_modify value=\"" +
                    myhwhere + "\">");
      else if ( "select".equals(myotype) ) {
        if ( "solo".equals( ServletTools.getParameter(inRequest,"myref") ) ) {
          OutputController.outButton(inRequest,out,"select",
                                     "select(\"" + myhwhere + "\")");
        } else {
          out.println("\t\t\t\t<INPUT type=checkbox name=myselect value=\"" +
                      myhwhere + "\">");
        }
      }

      String mylink;
      if ( "show".equals(myotype) ||
           "remove".equals(myotype) ||
           "select".equals(myotype) ||
           "batch_modify".equals(myotype) )  {

        OutputController.outButton(inRequest,out,"show",
                                   "show_blank(\"" + mywhere + "\")");
        out.println("<BR>");
        if ( !"remove".equals(myotype)  )
          return "show_blank(\"" + mywhere + "\")";

      }

      OutputController.outButton(inRequest,out,myotype,
                                 myotype + "(\"" + myhwhere + "\")");
      out.println("<BR>");
      return myotype + "(\"" + myhwhere + "\")";
    }
    catch (Exception ex) {
      return null;
    }
  }



}