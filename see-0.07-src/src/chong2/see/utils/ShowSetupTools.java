package chong2.see.utils;

import chong2.see.servlet.common.DataManager;
import chong2.see.xml.DataStructureXmlParser;
import chong2.see.data.DataReader;
import chong2.see.xml.DataXmlWriter;

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
 * 与显示设置相关的静态方法
 *
 * @author 玛瑞
 * @version 0.07
 */

final public class ShowSetupTools {

  /**
   * 显示设置框架（页面）
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param myset 数据集名
   */
  public static void outShowSetupFrame(HttpServletRequest inRequest,
                                       PrintWriter out,
                                       String myset) {

    DataStructureXmlParser mystrucReader =
        DataManagerTools.getDataStructureReader(inRequest,DataManager.SHOW_SETUP) ;
    if ( mystrucReader == null )
      return ;
    ArrayList itemnames = mystrucReader.getEditItemNames();
    ArrayList notnulls = mystrucReader.getNotNullNames();
    ArrayList keys = mystrucReader.getKeys();
    ArrayList mypass = mystrucReader.getPasswordItems();

    String mytitle = ServletTools.getParameter(inRequest,"myshowsetup");
    String mybutton = ServletTools.getParameter(inRequest,"mybutton");
    if ( ( mybutton != null ) &&
         (  mytitle != null) ) {
      mytitle =
          writeShowSetup(inRequest,out,mystrucReader,myset,mytitle,
          mybutton,itemnames, notnulls, keys,mypass);
    }

    DataManager.initOutput(inRequest,out,mystrucReader);

    showSetupList(inRequest,out,mystrucReader,
                  itemnames, notnulls, keys, mytitle);

    mystrucReader =
        DataManagerTools.getDataStructureReader(inRequest) ;
    if ( mystrucReader == null )
      return ;
    boolean showmatrix =
        "yes".equals(ServletTools.getParameter(inRequest,"mymatrix"));
    if ( showmatrix )
      itemnames =  mystrucReader.getAllMatrixItemNames();
    else
      itemnames =  mystrucReader.getEditItemNames();

    String myitems = ServletTools.getParameter(inRequest,"myitems");
    if ( myitems == null) myitems ="";
    String mysort = ServletTools.getParameter(inRequest,"mysort");
    if ( mysort == null) mysort ="";
    String myorder = ServletTools.getParameter(inRequest,"myorder");
    if ( mysort == null) mysort ="";
    String  mystyle =
        ServletTools.setSessionDatalistStyle(inRequest,"mystyle");

    // 选择显示项
    out.println("<TABLE  border=1 class=bar width=100%>");
    out.println("<TR class=tableheader><TH colspan=2 align=center>" +
                LanguageTools.tr(inRequest,"show") + "</TH></TR>");
    out.println("<TR class=tabledata><TD align=right>" +
                LanguageTools.tr(inRequest,"item") + "</TD>");
    out.println("<TD align=left>");
    OutputController.outCheckInput(inRequest,out,"myitems",myitems,
                                   itemnames, null ,false);
    out.println("</TD></TR>");
    out.println("</TABLE><BR>");

    // 选择排序项
    out.println("<TABLE  border=1 class=bar width=100%>");
    out.println("<TR class=tableheader><TH colspan=2 align=center>" +
                LanguageTools.tr(inRequest,"sort") + "</TH></TR>");
    out.println("<TR class=tabledata><TD align=right>" +
                LanguageTools.tr(inRequest,"item") + "</TD>");
    out.println("<TD align=left>");
    OutputController.outRadioInput(inRequest,out,"mysort",mysort,
                                   itemnames,  null,true,false);
    out.println("</TD></TR>");
    out.println("<TR class=tabledata><TD align=right>" +
                LanguageTools.tr(inRequest,"order") + "</TD>");
    out.println("<TD align=left>");
    OutputController.outRadioInput(inRequest,out,"myorder",myorder,
                                   CommonTools.stringToArray("ascending,descending",","),
                                   null,false,false);
    out.println("</TD></TR>");
    out.println("</TABLE><BR>");

    // 设置颜色
    Hashtable mycmap = DataManagerTools.getColor(inRequest);
    String mycolor = null;
    if ( mycmap != null ) mycolor = mycmap.get("name").toString();
    out.println("<TABLE  border=1 class=bar width=100%>");
    out.println("<TR class=tableheader><TH colspan=2 align=center>" +
                LanguageTools.tr(inRequest,"color") + "</TH></TR>");
    out.println("<TR class=tabledata><TD colspan=2>") ;
    OutputController.outRadioInput(inRequest,out,"mycolorname",mycolor,
                                   itemnames,   null,false,false);
    out.println("</TD></TR>");
    out.println("<TR class=tableheader align=center><TD>" +
                LanguageTools.tr(inRequest,"value") + "</TD>");
    out.println("<TD>" + LanguageTools.tr(inRequest,"color") + "</TD></TR>");
    int cc = 0;
    String myn;
    if ( mycmap != null)
      for (Enumeration e=mycmap.keys();e.hasMoreElements();) {
    myn = e.nextElement().toString();
    if ( "name".equals(myn) ||
         "others".equals(myn) )
      continue;
    out.println("<TR class=tabledata><TD align=right width=30%>" +
                "<INPUT name=mycolorvalue" + cc +
                " value=\"" + myn + "\"></TD>");
    out.println("<TD align=left>" +
                "<INPUT name=mycolorvaluecolor" + cc +
                " value=" + mycmap.get(myn) +
                "></TD></TR>");
    cc++;
      }
      for (int i=cc; i< DataManager.DEFAULT_COLOR.size(); i++) {
        out.println("<TR class=tabledata><TD align=right width=30%>" +
        "<INPUT name=mycolorvalue" + i +
        " >" + "</TD>");
        out.println("<TD align=left>" +
                    "<INPUT name=mycolorvaluecolor" + i +
                    " value=" + DataManager.DEFAULT_COLOR.get(i) +
                    "></TD></TR>");
      }
      out.println("<TR class=tabledata><TD align=right width=30%>" );
      out.println(LanguageTools.tr(inRequest,"others") + "</TD>");
      if ( (mycmap != null) && (mycmap.get("others") != null) )
        myn = mycmap.get("others").toString();
      else
        myn = "EEEEEE";
      out.println("<TD align=left>" +
                  "<INPUT name=mycolorotherscolor" +
                  " value=" + myn + "></TD></TR>");
      out.println("</TABLE><BR>");

      // 选择数据风格
      out.println("<TABLE class=bar border=1 width=100%>");
      out.println("<TR class=tableheader><TH>" +
                  LanguageTools.tr(inRequest,"datalist_style") + "</TH></TR>");
      out.println("<TR class=tabledata><TD>");
      OutputController.outDatalistStyleInput(inRequest,out,"mystyle",
          mystyle);
      out.println("</TD></TR>");
      out.println("</TABLE><BR>");

      // 显示按钮条
      out.println("<BR><P align=center>");
      OutputController.outSaveResetBar(inRequest,out,"seeform","ok","back");
      out.println("</FORM>");

      // 显示常用颜色
      out.println("<TABLE>" );
      for (int i=0; i< DataManager.DEFAULT_COLOR.size(); i++) {
        out.println("<TR><TD align=right >" +
        DataManager.DEFAULT_COLOR.get(i) +
        "</TD>");
        out.println("<TD align=left bgcolor=" +
                    DataManager.DEFAULT_COLOR.get(i) +
                    "> &nbsp&nbsp&nbsp&nbsp </TD></TR>");
      }
      out.println("<TR><TD align=right >EEEEEE</TD>");
      out.println("<TD align=left bgcolor=EEEEEE>" +
                  " &nbsp&nbsp&nbsp&nbsp </TD></TR>");
      out.println("</TABLE>" );

      showScript(inRequest, out,itemnames);

      PageTools.outFooter(inRequest, out,0);
  }

  /**
   * 增/删/改显示设置
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param mystrucReader 显示设置的结构
   * @param myset 当前数据
   * @param mytitle 显示设置标题
   * @param mybutton 当前处理按钮
   * @param itemnames 数据项
   * @param notnulls 数据非空项
   * @param keys 数据关键字
   * @param mypass 数据口令项
   * @return 处理后的显示设置标题
   */
  public static String writeShowSetup(HttpServletRequest inRequest,
                                      PrintWriter out,
                                      DataStructureXmlParser mystrucReader,
                                      String myset,
                                      String mytitle,
                                      String mybutton,
                                      ArrayList itemnames,
                                      ArrayList notnulls,
                                      ArrayList keys,
                                      ArrayList mypass) {

    Hashtable vv = new Hashtable();
    Hashtable vkey = new Hashtable();
    String retw = "";
    DataReader mydataReader =
        DataManagerTools.getDataReader( inRequest,mystrucReader,
        DataManager.SHOW_SETUP, null, null, 0,-1);
    String myitems = ServletTools.getParameter(inRequest,"myitems");
    String mysort = ServletTools.getParameter(inRequest,"mysort");
    String myorder = ServletTools.getParameter(inRequest,"myorder");
    String mycolor = ServletTools.getParameter(inRequest,"mycolor");
    String mystyle = ServletTools.getParameter(inRequest,"mystyle");
    String myuser = ServletTools.getSessionUser(inRequest);
    vkey.put("title",mytitle);
    vkey.put("data",myset);
    vkey.put("user",myuser);
    Hashtable myold = null;
    if ( "remove".equals(mybutton) ) {
      if (mydataReader.removeValidRecord(itemnames,
          notnulls, keys, vkey,null) != null )
        myold = vkey;
      mytitle = null;
    } else {
      vv.put("title",mytitle);
      vv.put("data",myset);
      vv.put("user",myuser);
      if ( myitems != null )
        vv.put("items",myitems);
      if ( mysort != null )
        vv.put("sort",mysort);
      if ( myorder != null )
        vv.put("order",myorder);
      if ( mycolor != null )
        vv.put("color",mycolor);
      if ( mystyle != null )
        vv.put("style",mystyle);
      if ( "add".equals(mybutton) ) {
        myold = mydataReader.addValidRecord(itemnames,
            notnulls, keys, vv);
      }
      if ( "modify".equals(mybutton) ) {
        myold = mydataReader.modifyValidRecord(itemnames,
            notnulls, keys, vv, null);
      }
    }
    if ( myold == null ) {
      retw = "invalid_data";
      mytitle = null;
    } else {
      String fdata =
          DataManagerTools.getDataValuesFile(inRequest,DataManager.SHOW_SETUP);
      DataXmlWriter myw = new DataXmlWriter();
      retw = myw.writeData(mydataReader.getRecords(),fdata,
                           ServletTools.getAppDefaultCharset(inRequest));
    }
    ArrayList myaudit =
        ServletTools.getAppDataAuditTypes(inRequest);
    if ( myaudit.contains(LanguageTools.atr(inRequest,mybutton))) {
      AuditTools.addAudit(inRequest,DataManager.SHOW_SETUP,mybutton,
                          vkey  , myold  , vv , retw,null ,mypass);
    }

    return mytitle;
  }

  /**
   * 显示属于超级管理员和当前用户的所有显示设置列表
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param mystrucReader 显示设置的结构
   * @param itemnames 数据项
   * @param notnulls 数据非空项
   * @param keys 数据关键字
   * @param mytitle 显示设置标题
   */
  public static void  showSetupList(HttpServletRequest inRequest,
                                    PrintWriter out,
                                    DataStructureXmlParser mystrucReader,
                                    ArrayList itemnames,
                                    ArrayList notnulls,
                                    ArrayList keys,
                                    String mytitle) {

    ArrayList mywhere = new ArrayList();
    Hashtable vv = new Hashtable();
    vv.put("data",ServletTools.getParameter(inRequest,"myset"));
    String mysuper =
        LanguageTools.atr(inRequest,DataManager.SUPER_SEER);
    vv.put("user",mysuper);
    mywhere.add(vv);
    DataReader mydataReader =
        DataManagerTools.getDataReader( inRequest,mystrucReader,
        DataManager.SHOW_SETUP,null,mywhere, 0,-1);
    ArrayList mydata =
        mydataReader.getValidRecords(itemnames, notnulls, keys);
    boolean isuper = AclTools.isSuperseer(inRequest);
    String myuser = ServletTools.getSessionUser(inRequest);
    if ( !isuper ) {
      vv.put("user",myuser);
      mywhere.add(vv);
      mydataReader =
          DataManagerTools.getDataReader( inRequest,mystrucReader,
          DataManager.SHOW_SETUP,null,mywhere, 0,-1);
      ArrayList myudata =
          mydataReader.getValidRecords(itemnames, notnulls, keys);

      if ( ( mydata != null) &&
           ( myudata != null ) ) {
        mydata.addAll(myudata);
      }
    }
    String myclick = "";
    boolean isupersetup = true;
    if ( (mydata != null ) &&
         mydata.size() > 0 ) {
      String myss ="",myt="";
      if ( "yes".equals(ServletTools.getParameter(inRequest,"mymaintenance")))
        myss = " checked ";
      else
        myt = " checked ";
      out.println("\t <P class=serious>" +
                  LanguageTools.tr(inRequest,"select_show_setup") );
      out.println("\t <INPUT type=radio name=mymaintenance value=yes " +
                  myss + ">" + LanguageTools.tr(inRequest,"maintenance") );
      out.println("\t <INPUT type=radio name=mymaintenance value=no " +
                  myt + ">" + LanguageTools.tr(inRequest,"use") + "</P>");
      out.println("\t <TABLE class=data cellPadding=2 width=100%>");
      ArrayList myshow = new ArrayList();
      StringBuffer mystr = new StringBuffer();
      for (int i=0; i< mydata.size();i++) {
        vv = (Hashtable)mydata.get(i);
        if (vv.get("title") == null) continue;
        mystr = new StringBuffer("<A onClick='see_set(\"");
        mystr.append(vv.get("title"));
        mystr.append("\",\"");
        if ( vv.get("items") != null )
          mystr.append(vv.get("items"));
        mystr.append("\",\"");
        if ( vv.get("sort") != null )
          mystr.append(vv.get("sort"));
        mystr.append("\",\"");
        if ( vv.get("order") != null )
          mystr.append(vv.get("order"));
        mystr.append("\",\"");
        if ( vv.get("color") != null )
          mystr.append(vv.get("color"));
        mystr.append("\",\"");
        if ( vv.get("style") != null )
          mystr.append(vv.get("style"));
        mystr.append("\")'><U>");
        mystr.append(vv.get("title"));
        mystr.append("</U></A>");
        myshow.add(mystr);
        if ( isuper) continue;
        if ( myuser.equals(vv.get("user")) )
          isupersetup = false;
      }
      OutputController.outTableLines(out,myshow,4,"class=tabledata","");
      out.println("</TABLE>");
      out.println("<BR><HR>");
    }

    out.println("\t <P class=serious>" +
                LanguageTools.tr(inRequest,"set_show_setup") + "</P>");
    out.println("<TABLE class=whitebar width=100% border=0><TR>");
    out.println("<TD align=left>" + LanguageTools.tr(inRequest,"title") +
                ":<INPUT name=mytitle>");
    OutputController.outButton(inRequest,out,"add");
    out.println("</TD>");
    if ( mytitle != null ) {
      out.println("<TD align=right>" +
                  LanguageTools.tr(inRequest,"current_show_setup") +
                  ":" + mytitle );
      if ( isuper || !isupersetup) {
        OutputController.outButton(inRequest,out,"modify");
        OutputController.outButton(inRequest,out,"remove");
      }
      out.println("</TD>");
    }
    out.println("</TR></TABLE>");
    out.print("<INPUT type=hidden name=myshowsetup");
    if ( mytitle != null )
      out.print(" value=\"" + mytitle + "\" ");
    out.println(">");

  }

  /**
   * 显示属于超级管理员和当前用户的所有显示设置列表
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param itemnames 数据项
   */
  public static void  showScript(HttpServletRequest inRequest,
                                 PrintWriter out,
                                 ArrayList itemnames) {

    out.println("\t<SCRIPT LANGUAGE='javascript'>\n");
    out.println("\tfunction see_set(title,items,sort,order,color,style) {");
    out.println("\t\t document.seeform.myshowsetup.value = title;");
    out.println("\t\t document.seeform.myitems.value = items;");
    out.println("\t\t document.seeform.mysort.value = sort;");
    out.println("\t\t document.seeform.myorder.value = order;");
    out.println("\t\t document.seeform.mycolor.value = color;");
    out.println("\t\t document.seeform.mystyle.value = style;");
    out.println("\t\t document.seeform.mybutton.value = \"\";");
    out.println("\t\t if ( document.seeform.mymaintenance[1].checked ) { ");
    out.println("\t\t\t document.seeform.myframe.value = \"data\";");
    out.println("\t\t\t document.seeform.myoperation.value = "+
                "document.seeform.mytrueoperation.value;");
    out.println("\t\t }");
    out.println("\t\t document.seeform.submit();");
    out.println("\t}");
    out.println("\t function see_remove() {");
    out.println("\t\t if ( !confirm(\"" +
                LanguageTools.tr(inRequest,"sure_remove") + "\")  )");
    out.println("\t\t\t return;");
    out.println("\t\t document.seeform.mybutton.value = \"remove\";");
    out.println("\t\t document.seeform.submit();");
    out.println("\t }");
    out.println("\t function see_modify() {");
    out.println("\t\t if ( see_setvalue() < 0 ) return;");
    out.println("\t\t document.seeform.mybutton.value = \"modify\";");
    out.println("\t\t document.seeform.submit();");
    out.println("\t }");
    out.println("\tfunction see_add() {");
    out.println("\t\t if ( document.seeform.mytitle.value.length < 1 ) {");
    out.println("\t\t\t alert(\"" + LanguageTools.tr(inRequest,"title") +
                LanguageTools.tr(inRequest,"should_not_null")  + "\");");
    out.println("\t\t\t return;");
    out.println("\t\t }");
    out.println("\t\t if ( see_setvalue() < 0 ) return;");
    out.println("\t\t document.seeform.myshowsetup.value = "+
                "document.seeform.mytitle.value;");
    out.println("\t\t document.seeform.mybutton.value = \"add\";");
    out.println("\t\t document.seeform.submit();");
    out.println("\t}");
    out.println("\tfunction see_ok() {");
    out.println("\t\t if ( see_setvalue() < 0 ) return;");
    out.println("\t\t document.seeform.myframe.value = \"data\";");
    out.println("\t\t document.seeform.myoperation.value = "+
                "document.seeform.mytrueoperation.value;");
    out.println("\t\t document.seeform.submit();");
    out.println("\t}");
    out.println("\tfunction see_setvalue() {");
    if ( itemnames.size() == 1) {
      out.println("\t\t if ( !document.seeform.input_myitems.checked )");
      out.println("\t\t{");
      out.println("\t\t\t alert(\"" + LanguageTools.tr(inRequest,"show") +
                  LanguageTools.tr(inRequest,"should_not_null")  + "\");");
      out.println("\t\t\t return -1;");
      out.println("\t\t}");
      out.println("\t\t document.seeform.myitems.value = " +
                  "document.seeform.input_myitems.value  ;");
      out.println("\t\t if ( document.seeform.input_mysort.checked )");
      out.println("\t\t{");
      out.println("\t\t\t document.seeform.mysort.value = " +
                  "document.seeform.input_mysort.value  ;");
      out.println("\t\t}");
    } else {
      out.println("\t\t var vitems = \"\";");
      out.println("\t\t var vsort = \"\";");
      out.println("\t\tfor ( i = 0 ; i < " +
                  itemnames.size() + " ; i++ ) {");
      out.println("\t\t\t if ( document.seeform.input_myitems[i].checked )");
      out.println("\t\t\t{");
      out.println("\t\t\t\t vitems = vitems + " +
                  "document.seeform.input_myitems[i].value + \"" +
                  DataReader.LIST_SPLITTER + "\" ;");
      out.println("\t\t\t}");
      out.println("\t\t\t if ( document.seeform.input_mysort[i].checked )");
      out.println("\t\t\t{");
      out.println("\t\t\t\t vsort =  " +
                  "document.seeform.input_mysort[i].value ;");
      out.println("\t\t\t}");
      out.println("\t\t}");
      out.println("\t\t if ( vitems.length < 1 )");
      out.println("\t\t{");
      out.println("\t\t\t alert(\"" + LanguageTools.tr(inRequest,"item") +
                  LanguageTools.tr(inRequest,"should_not_null")  + "\");");
      out.println("\t\t\t return -1;");
      out.println("\t\t}");
      out.println("\t\t document.seeform.myitems.value = vitems;");
      out.println("\t\t document.seeform.mysort.value = vsort;");
    }
    out.println("\t\tfor ( i = 0 ; i < 2 ; i++ ) {");
    out.println("\t\t\t if ( document.seeform.input_myorder[i].checked )");
    out.println("\t\t\t {");
    out.println("\t\t\t\t document.seeform.myorder.value =  " +
                "document.seeform.input_myorder[i].value;");
    out.println("\t\t\t\t break;");
    out.println("\t\t\t}");
    out.println("\t\t}");
    out.println("\t\tfor ( i = 0 ; i < " +
                DataManager.DATALIST_STYLE.size() + " ; i++ ) {");
    out.println("\t\t\t if ( document.seeform.input_mystyle[i].checked )");
    out.println("\t\t\t {");
    out.println("\t\t\t\t document.seeform.mystyle.value =  " +
                "document.seeform.input_mystyle[i].value;");
    out.println("\t\t\t\t break;");
    out.println("\t\t\t}");
    out.println("\t\t}");
    out.println("\t\t var vcolor = \"\";");
    out.println("\t\t for ( i = 0 ; i < " +
                itemnames.size() + " ; i++ ) {");
    out.println("\t\t\t if ( document.seeform.input_mycolorname[i].checked )");
    out.println("\t\t\t {");
    out.println("\t\t\t\t vcolor = " +
                "document.seeform.input_mycolorname[i].value ;");
    out.println("\t\t\t\t break;");
    out.println("\t\t\t }");
    out.println("\t\t }");
    out.println("\t\t if ( vcolor.length > 0 ) {");
    out.println("\t\t\t vcolor = \"name\" + \"" +
                DataReader.VALUE_HASH_SPLITTER +
                "\" + vcolor ;");
    for (int i=0; i< DataManager.DEFAULT_COLOR.size(); i++) {
      out.println("\t\t if ( document.seeform.mycolorvalue" + i +
      ".value.length > 0 )");
      out.println("\t\t if ( document.seeform.mycolorvaluecolor" + i +
                  ".value.length > 0 )");
      out.println("\t\t\t vcolor = vcolor + \"" +
                  DataReader.VALUE_HASH_SPLITTER +
                  "\" + document.seeform.mycolorvalue" + i +
                  ".value + \"" +
                  DataReader.VALUE_HASH_SPLITTER +
                  "\" + document.seeform.mycolorvaluecolor" + i +
                  ".value ;");
    }
    out.println("\t\t if ( document.seeform.mycolorotherscolor.value.length > 0 )");
    out.println("\t\t\t vcolor = vcolor + \"" +
                DataReader.VALUE_HASH_SPLITTER +
                "others" +
                DataReader.VALUE_HASH_SPLITTER +
                "\" + document.seeform.mycolorotherscolor.value ;");
    out.println("\t\t document.seeform.mycolor.value = vcolor;");
    out.println("\t }");
    out.println("\t return 0;");
    out.println("\t}");
    out.println("\t</SCRIPT>");

  }

}