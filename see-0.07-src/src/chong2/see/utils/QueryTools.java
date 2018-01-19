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
 * <p>Title: ������̹�����</p>
 * <p>Description: Ϊ����з��ṩ�������ݺ������ƽ̨</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: ���</p>
 *
 * ���ѯ����������صľ�̬����
 *
 * @author ����
 * @version 0.07
 */

final public class QueryTools {

  /**
   * �����жϵķָ������û����ݲ�Ҫʹ��
   */
  public static String CONDITION_SPLITTER = ":c:i:s-c:i:s:"  ;
  /**
   * �����жϵġ����ҡ����û����ݲ�Ҫʹ��
   */
  public static String CONDITION_AND = ":c:a:d-c:a:d:"  ;
  /**
   * ��������жϵķָ������û����ݲ�Ҫʹ��
   */
  public static String MULTI_SPLITTER = ":m:s-m:s:";
  /**
   * ���������ӣ����������б�
   */
  public static ArrayList CONDITION_OPERATORS = new ArrayList();
  static {
    CONDITION_OPERATORS.add(CommonTools.EQUAL_PREFIX);
    CONDITION_OPERATORS.add(CommonTools.NOT_EQUAL_PREFIX);
    CONDITION_OPERATORS.add(CommonTools.GREATTER_PREFIX);
    CONDITION_OPERATORS.add(CommonTools.LESS_PREFIX);
    CONDITION_OPERATORS.add(CommonTools.START_PREFIX);
    CONDITION_OPERATORS.add(CommonTools.END_PREFIX);
    CONDITION_OPERATORS.add(CommonTools.INCLUDE_PREFIX);
    CONDITION_OPERATORS.add(CommonTools.NOT_INCLUDE_PREFIX);
  }

  /**
   * ��ѯ���ÿ�ܣ�ҳ�棩
   * @param inRequest ��ǰ����
   * @param out ��ǰ���
   * @param myset ���ݼ���
   */
  public static void outQueryFrame(HttpServletRequest inRequest,
                                       PrintWriter out,
                                       String myset) {

    DataStructureXmlParser mysetstrucReader =
        DataManagerTools.getDataStructureReader(inRequest) ;
    if ( mysetstrucReader == null )
      return ;

    String myaction = ServletTools.getParameter(inRequest,"myaction");
    if ( ( myaction != null ) &&
         !"".equals(myaction) ) {

      outAndFrame(inRequest,out,mysetstrucReader,myset, myaction);
      return;
    }
    DataStructureXmlParser mystrucReader =
        DataManagerTools.getDataStructureReader(inRequest,
        DataManager.QUERY_SETUP) ;
    if ( mystrucReader == null )
      return ;

    ArrayList itemnames = mystrucReader.getEditItemNames();
    ArrayList notnulls = mystrucReader.getNotNullNames();
    ArrayList keys = mystrucReader.getKeys();
    ArrayList mypass = mystrucReader.getPasswordItems();

    DataManager.initOutput(inRequest,out,mysetstrucReader);

    String mybutton = ServletTools.getParameter(inRequest,"mybutton");
    String mytitle = ServletTools.getParameter(inRequest,"myquerysetup");
    if ( ( mybutton != null ) &&
         (  mytitle != null) ) {
      mytitle =
          writeShowSetup(inRequest,out,mystrucReader,myset,mytitle,
          mybutton,itemnames, notnulls, keys,mypass);
    }


    String mysetwhere =
        ServletTools.getParameter(inRequest,"mysetwhere");
    showSetupList(inRequest,out,mystrucReader,
                  itemnames, notnulls, keys, mytitle,mysetwhere);

    String myauditshow =
        ServletTools.getParameter(inRequest,"myauditshow");
    if ( myauditshow != null)
    out.print("\t<INPUT type=hidden name=myauditshow " +
                "value=\"" + myauditshow + "\" " + ">");

    if ( mysetwhere == null ) {
      out.println("<P align=center>");
      OutputController.outButton(inRequest,out,"setup","or()");
      OutputController.outButton(inRequest,out,"ok");
      OutputController.outButton(inRequest,out,"cancel");
      out.println("</P>");
    } else {
      out.println("<P class=info>" +
                  LanguageTools.tr(inRequest,"query_info") +
                  "</P>");
      outCondition(inRequest,out,mysetwhere,myset);
      out.println("<P align=center>");
      OutputController.outButton(inRequest,out,"ok");
      OutputController.outButton(inRequest,out,"or");
      OutputController.outButton(inRequest,out,"clear","clearset()");
      OutputController.outButton(inRequest,out,"cancel");
      out.println("</P>");
    }

    String mywhere =
        ServletTools.getParameter(inRequest,"mywhere");
    if ( ( mywhere != null ) &&
         !"".equals(mywhere)) {
      out.println("<P class=info>" +
                  LanguageTools.tr(inRequest,"query_baseinfo") +
                  "</P>");
      outCondition(inRequest,out,mywhere,myset);
    }

    showScript(inRequest,out,myset,mysetwhere);

    PageTools.outFooter(inRequest, out,0);

  }

  /**
   * ���ҳ��ű�
   * @param inRequest ��ǰ����
   * @param out ��ǰ���
   * @param myset ��ǰ����
   * @param mysetwhere ��ǰ����
   */
  public static void  showScript(HttpServletRequest inRequest,
                                 PrintWriter out,
                                 String myset,
                                 String mysetwhere) {

    out.println("\t<SCRIPT LANGUAGE='javascript'>\n");

    out.println("\t\tfunction see_or()");
    out.println("\t\t{");
    out.println("\t\t docform = document.seeform;");
    String mymatrix = "";
    if ( "yes".equals(ServletTools.getParameter(inRequest,"mymatrix")) )
         mymatrix = "&mymatrix=yes";
    out.println("\t\t setter = window.open(\"" +
                DataManager.URL +
                "?myframe=query_setup&myset=" + myset +
                "&myaction=start" + mymatrix +
                "&myoperation=query_setup"+
//                "\", " + "\"" + myset + "_Set_or" + "\"," +
//              ���ô��ڱ�ʶ����ͬʱ�ര��ʹ����һ����ʱ����ҳ��ֻ��ʾһ��
//              �����ñ�ʶ����ÿ�δ��µ�
                "\", " + "\"\"," +
                " \"toolbar=no,menubar=no,status=yes,resizable=yes,scrollbars=yes,width=700,height=400\");");
    out.println("\t\t setter.docform = docform ;");
    out.println("\t\t}");

    out.println("\t\tfunction see_ok()");
    out.println("\t\t{");
    out.println("\t\t document.seeform.myframe.value = \"data\";");
    out.println("\t\t document.seeform.myoperation.value = "+
                "document.seeform.mytrueoperation.value;");
    if ( mysetwhere != null )
      out.println("\t\t document.seeform.mysetwhere.value = \"" +
                  mysetwhere + "\";");
    out.println("\t\t document.seeform.submit();");
    out.println("\t\t}");

    out.println("\t\tfunction see_back()");
    out.println("\t\t{");
    out.println("\t\t\t window.history.back();");
    out.println("\t\t}");

    out.println("\t\tfunction see_cancel()");
    out.println("\t\t{");
    out.println("\t\t document.seeform.myframe.value = \"data\";");
    out.println("\t\t document.seeform.myoperation.value = "+
                "document.seeform.mytrueoperation.value;");
    out.println("\t\t document.seeform.mysetwhere.value = \"\";");
    out.println("\t\t document.seeform.submit();");
//    out.println("\t\t\t window.history.back();");
    out.println("\t\t}");

    out.println("\t\tfunction see_clearset()");
    out.println("\t\t{");
    out.println("\t\t document.seeform.mysetwhere.value = \"\";");
    out.println("\t\t document.seeform.submit();");
    out.println("\t\t}");

    out.println("\t\tfunction see_add() {");
    out.println("\t\t if ( document.seeform.mytitle.value.length < 1 ) {");
    out.println("\t\t\t alert(\"" +
                LanguageTools.tr(inRequest,"title") +
                LanguageTools.tr(inRequest,"should_not_null") +
                "\");");
    out.println("\t\t\t return;");
    out.println("\t\t }");
    out.println("\t\t if ( document.seeform.mysetwhere.value.length < 1 ) {");
    out.println("\t\t\t alert(\"" +
                LanguageTools.tr(inRequest,"condition") +
                LanguageTools.tr(inRequest,"should_not_null") +
                "\");");
    out.println("\t\t\t return;");
    out.println("\t\t }");
    out.println("\t\t document.seeform.myquerysetup.value = "+
                "document.seeform.mytitle.value;");
    out.println("\t\t document.seeform.mybutton.value = \"add\";");
    out.println("\t\t document.seeform.submit();");
    out.println("\t}");

    out.println("\tfunction see_set(title,condition) {");
    out.println("\t\t document.seeform.myquerysetup.value = title;");
    out.println("\t\t document.seeform.mysetwhere.value = condition;");
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
    out.println("\t\t document.seeform.mysetwhere.value = \"\";");
    out.println("\t\t document.seeform.submit();");
    out.println("\t }");

    out.println("\t function see_modify() {");
    out.println("\t\t if ( document.seeform.mysetwhere.value.length < 1 ) {");
    out.println("\t\t\t alert(\"" +
                LanguageTools.tr(inRequest,"condition") +
                LanguageTools.tr(inRequest,"should_not_null") +
                "\");");
    out.println("\t\t\t return;");
    out.println("\t\t }");
    out.println("\t\t document.seeform.mybutton.value = \"modify\";");
    out.println("\t\t document.seeform.submit();");
    out.println("\t }");

    out.println("\t</SCRIPT>");

  }

  /**
   * ��/ɾ/�Ĳ�ѯ����
   * @param inRequest ��ǰ����
   * @param out ��ǰ���
   * @param mystrucReader ��ѯ���õĽṹ
   * @param myset ��ǰ����
   * @param mytitle ��ѯ���ñ���
   * @param mybutton ��ǰ����ť
   * @param itemnames ������
   * @param notnulls ���ݷǿ���
   * @param keys ���ݹؼ���
   * @param mypass ���ݿ�����
   * @return �����Ĳ�ѯ���ñ���
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
        DataManager.QUERY_SETUP, null,null, 0,-1);
    String mycond = ServletTools.getParameter(inRequest,"mysetwhere");
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
      if ( mycond != null )
        vv.put("condition",mycond);
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
          DataManagerTools.getDataValuesFile(inRequest,
          DataManager.QUERY_SETUP);
      DataXmlWriter myw = new DataXmlWriter();
      retw = myw.writeData(mydataReader.getRecords(),fdata,
                           ServletTools.getAppDefaultCharset(inRequest));
    }
    ArrayList myaudit =
        ServletTools.getAppDataAuditTypes(inRequest);
    if ( myaudit.contains(LanguageTools.atr(inRequest,mybutton))) {
      AuditTools.addAudit(inRequest,DataManager.QUERY_SETUP,mybutton,
                          vkey  , myold  , vv , retw,null ,mypass);
    }

    return mytitle;
  }

  /**
   * ��ʾ���ڳ�������Ա�͵�ǰ�û������в�ѯ�����б�
   * @param inRequest ��ǰ����
   * @param out ��ǰ���
   * @param mystrucReader ��ѯ���õĽṹ
   * @param itemnames ������
   * @param notnulls ���ݷǿ���
   * @param keys ���ݹؼ���
   * @param mytitle ��ѯ���ñ���
   * @param mysetwhere ��ǰ����
   */
  public static void  showSetupList(HttpServletRequest inRequest,
                                    PrintWriter out,
                                    DataStructureXmlParser mystrucReader,
                                    ArrayList itemnames,
                                    ArrayList notnulls,
                                    ArrayList keys,
                                    String mytitle,
                                    String mysetwhere) {

    ArrayList mywhere = new ArrayList();
    Hashtable vv = new Hashtable();
    vv.put("data",ServletTools.getParameter(inRequest,"myset"));
    String mysuper =
        LanguageTools.atr(inRequest,DataManager.SUPER_SEER);
    vv.put("user",mysuper);
    mywhere.add(vv);
    DataReader mydataReader =
        DataManagerTools.getDataReader( inRequest,mystrucReader,
        DataManager.QUERY_SETUP, null,mywhere, 0,-1);
    ArrayList mydata =
        mydataReader.getValidRecords(itemnames, notnulls, keys);
    boolean isuper = AclTools.isSuperseer(inRequest);
    String myuser = ServletTools.getSessionUser(inRequest);
    if ( !isuper ) {
      vv.put("user",myuser);
      mywhere.add(vv);
      mydataReader =
          DataManagerTools.getDataReader( inRequest,mystrucReader,
          DataManager.QUERY_SETUP, null,mywhere, 0,-1);
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
                  LanguageTools.tr(inRequest,"select_query_setup") );
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
        if ( vv.get("condition") != null )
          mystr.append(vv.get("condition"));
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

    out.print("<INPUT type=hidden name=myquerysetup");
    if ( mytitle != null )
      out.print(" value=\"" + mytitle + "\" ");
    out.println(">");

    if ( mysetwhere == null ) return;

    out.println("\t <P class=serious>" +
                LanguageTools.tr(inRequest,"set_query_setup") + "</P>");
    out.println("<TABLE class=whitebar width=100% border=0><TR>");
    out.println("<TD align=left>" + LanguageTools.tr(inRequest,"title") +
                ":<INPUT name=mytitle>");
    OutputController.outButton(inRequest,out,"add");
    out.println("</TD>");
    if ( mytitle != null ) {
      out.println("<TD align=right>" +
                  LanguageTools.tr(inRequest,"current_query_setup") +
                  ":" + mytitle );
      if ( isuper || !isupersetup) {
        OutputController.outButton(inRequest,out,"modify");
        OutputController.outButton(inRequest,out,"remove");
      }
      out.println("</TD>");
    }
    out.println("</TR></TABLE>");

  }

  /**
   * ������ò�ѯ������ҳ��
   * @param inRequest ��ǰ����
   * @param out ��ǰ���
   * @param mystrucReader ���ݽṹ����
   * @param myset ��ǰ���ݼ���
   * @param myaction ��ǰ����
   */
  public static void outAndFrame(HttpServletRequest inRequest,
                                     PrintWriter out,
                                     DataStructureXmlParser mystrucReader,
                                     String myset,
                                     String myaction) {
    if ( "start".equals(myaction)  )  {
    PageTools.outHeader(inRequest, out,"",-1);
    String mymatrix = "";
    if ( "yes".equals(ServletTools.getParameter(inRequest,"mymatrix")) )
         mymatrix = "&mymatrix=yes";
    out.println("<FRAMESET border=5 frameBorder=5 frameSpacing=0 rows=*,200>");
    out.print("\t<FRAME frameBorder=1 marginHeight=1 marginWidth=1 noresize=no " +
              "name=setand " +
              "src=\"" + inRequest.getRequestURI() +
              "?myframe=query_setup&myset=" + myset + mymatrix +
              "&myoperation=query_setup&myaction=set");
    out.println("\">");
    out.println("\t<FRAME frameBorder=1 marginHeight=1 marginWidth=1 noresize=no  " +
                "name=getand  " +
                "src=\"" + inRequest.getRequestURI() +
                "?myframe=query_setup&myset=" + myset + mymatrix +
                "&myoperation=query_setup&myaction=get");
    out.println("\">");
    out.println("\t</FRAME>");
    out.println("</FRAMESET>");
    out.println("</HTML>");
    return;
    }

    if ( "set".equals(myaction) ) {
      OutputRecordTools.outRecord(inRequest,out,mystrucReader,
                                  myset,"query_setup");
      PageTools.outFooter(inRequest, out,0);
      return;
    }

    DataManager.initOutput(inRequest,out,mystrucReader);
    out.println("<INPUT type=hidden name=myaction value=\"" +
                myaction + "\">");

    String mysetwhere =
        ServletTools.getParameter(inRequest,"mysetwhere");
    out.println("<P align=center>");
    if ( mysetwhere != null ) {
      outCondition(inRequest,out,mysetwhere,myset);
      OutputController.outButton(inRequest,out,"ok");
      OutputController.outButton(inRequest,out,"clear","clearset()");
    }
    OutputController.outButton(inRequest,out,"cancel");
    out.println("</P>");


    out.println("\t<SCRIPT LANGUAGE='javascript'>\n");

    out.println("\t\tfunction see_ok()");
    out.println("\t\t{");
    out.println("\t\t\t if ( top.docform.mysetwhere.value.length > 0 )");
    out.println("\t\t\t\t top.docform.mysetwhere.value = " +
                "top.docform.mysetwhere.value + \"" +
                 MULTI_SPLITTER + "\"" +
                 " + document.seeform.mysetwhere.value;");
    out.println("\t\t\t else ");
    out.println("\t\t\t\t top.docform.mysetwhere.value = document.seeform.mysetwhere.value;");
    out.println("\t\t\t top.docform.submit();");
    out.println("\t\t\t top.close();");
    out.println("\t\t}");

    out.println("\t\tfunction see_cancel()");
    out.println("\t\t{");
    out.println("\t\t\t top.close();");
    out.println("\t\t}");

    out.println("\t\tfunction see_clearset()");
    out.println("\t\t{");
    out.println("\t\t document.seeform.mysetwhere.value = \"\";");
    out.println("\t\t document.seeform.submit();");
    out.println("\t\t}");

    out.println("\t</SCRIPT>");

    PageTools.outFooter(inRequest, out,0);

    return;

  }

  /**
   * �����ѯҳ������ð�ť��
   * @param inRequest ��ǰ����
   * @param out ��ǰ���
   * @param mycheck Ҫ����check�ֶ�
   * @param myselect Ҫ����select�ֶ�
   * @param mynames �ֶ����б�
   */
  public static void outSetAndBar(HttpServletRequest inRequest,
                                       PrintWriter out,
                                       Hashtable mycheck,
                                       Hashtable myselect,
                                       ArrayList mynames) {

    ArrayList myt = new ArrayList();
    myt.add("add");
    myt.add("reset");
    ArrayList mytab = new ArrayList() ;
    mytab.add("1");
    mytab.add("-1");
    OutputController.outButtonBar(inRequest,out,myt,
                  ServletTools.getImageNames(inRequest,myt),
                  ServletTools.getClicks(myt) , null,mytab);

    out.println("\t <SCRIPT LANGUAGE='javascript'>\n");
    out.println("\t\t function see_add()");
    out.println("\t\t {");
    out.println("\t\t\t  var myv = \"\";");
    if ( mycheck!= null )
      outCheckValue(inRequest,out,mycheck,true);
    if ( myselect!= null )
      outCheckValue(inRequest,out,myselect,false);
    String myname;
    for (int i=0; i<mynames.size(); i++) {
      myname = mynames.get(i).toString();
      if ( mycheck.get(myname) != null ) continue;
      if ( myselect.get(myname) != null ) continue;
      out.println("\t\t\t  if (document.seeform" +
                ".input_" +  myname + ".value.length > 0)");
      out.println("\t\t\t  {");
      out.println("\t\t\t\t  myv = myv + \"" + myname +
                  "\" + \"" + CONDITION_SPLITTER +
                 "\"" );
      outOpeartorValue(inRequest,out,myname);
      out.println("\t\t\t\t  myv = myv + document.seeform" +
                ".input_" +  myname + ".value + \"" +
                CONDITION_SPLITTER + "\" ;");
      out.println("\t\t\t  }");
    }
    out.println("\t\t\t if ( top.getand.document.seeform.mysetwhere.value.length > 0 )");
    out.println("\t\t\t\t top.getand.document.seeform.mysetwhere.value = " +
                "top.getand.document.seeform.mysetwhere.value + myv;");
    out.println("\t\t\t else ");
    out.println("\t\t\t\t top.getand.document.seeform.mysetwhere.value = myv;");
    out.println("\t\t\t top.getand.document.seeform.submit();");
    out.println("\t\t document.seeform.reset();");
    out.println("\t\t}");

    out.println("\t\tfunction see_reset()");
    out.println("\t\t{");
    out.println("\t\t\tdocument.seeform.reset();");
    out.println("\t\t}");
    out.println("\t</SCRIPT>");

  }

  /**
   * �������ֶο�ֵ��ҳ��ű�
   * @param inRequest ��ǰ����
   * @param out ��ǰ���
   * @param mycheck Ҫ�����ֶ�
   * @param isChecked �Ƿ���check�ֶ�
   */
  public static void  outCheckValue(HttpServletRequest inRequest,
                                        PrintWriter out,
                                        Hashtable mycheck,
                                        boolean   isChecked) {
      String myn;
      int mysize;
      for ( Enumeration  e=mycheck.keys(); e.hasMoreElements();) {
        myn = e.nextElement().toString();
        mysize = CommonTools.stringToInt(mycheck.get(myn).toString());
        if ( mysize > 1 ) {
          out.println("\t\t\t  for ( i = 0 ; i < " + mysize + "; i++ )");
          out.println("\t\t\t  {");
          out.print("\t\t\t\t  if (document.seeform" +
                    ".input_" +  myn + "[i].");
          if ( isChecked )
            out.println("checked ) ");
          else
            out.println("selected ) ");
          out.println("\t\t\t\t  {");
          out.println("\t\t\t\t  myv = myv + \"" + myn +
                      "\" + \"" + CONDITION_SPLITTER +
                     "\";" );
          outOpeartorValue(inRequest,out,myn);
          out.println("\t\t\t\t  myv = myv + document.seeform" +
                      ".input_" +  myn + "[i].value+ \"" +
                      CONDITION_SPLITTER +  "\";");
          out.println("\t\t\t\t  }");
          out.println("\t\t\t  }");
        } else if ( mysize == 1 ) {
          out.print("\t\t\t  if (document.seeform" +
                    ".input_" +  myn );
          if ( isChecked )
            out.println(".checked ) ");
          else
            out.println(".selected ) ");
          out.println("\t\t\t  {");
          out.println("\t\t\t\t  myv = myv + \"" + myn +
                      "\" + \"" + CONDITION_SPLITTER +
                     "\";" );
          outOpeartorValue(inRequest,out,myn);
          out.println("\t\t\t\t  myv = myv + document.seeform" +
                      ".input_" +  myn + ".value+ \"" +
                      CONDITION_SPLITTER +  "\";");
          out.println("\t\t\t  }");
        }
      }
  }

  /**
   * ������������������ҳ��ű�
   * @param inRequest ��ǰ����
   * @param out ��ǰ���
   * @param myname �ֶ���
   */
  public static void  outOpeartorValue(HttpServletRequest inRequest,
                                        PrintWriter out,
                                        String myname) {
      int mysize = CONDITION_OPERATORS.size();
      out.println("\t\t\t\t  for ( j = 0 ; j < " + mysize + "; j++ )");
      out.println("\t\t\t\t  {");
      out.println("\t\t\t\t\t  if (document.seeform" +
                ".input_" +  myname + "_op[j].checked)");
      out.println("\t\t\t\t\t  {");
      out.println("\t\t\t\t\t\t  myv = myv + document.seeform" +
                  ".input_" +  myname + "_op[j].value;");
      out.println("\t\t\t\t\t  }");
      out.println("\t\t\t\t  }");

  }

  /**
   * ��ʾ����
   * @param inRequest ��ǰ����
   * @param out ��ǰ���
   * @param mywhere ����
   * @param myset ���ݼ���
   */
  public static void outCondition(HttpServletRequest inRequest,
                                  PrintWriter out,
                                  String mywhere,
                                  String myset) {
    if ( mywhere == null ) return;
    ArrayList mycond =
        DataManagerTools.getMultiWhere(inRequest, mywhere);
    outCondition(inRequest,out,mycond,myset);
  }

  /**
   * ��ʾ����
   * @param inRequest ��ǰ����
   * @param out ��ǰ���
   * @param mycond ����
   * @param myset ���ݼ���
   */
  public static void outCondition(HttpServletRequest inRequest,
                                  PrintWriter out,
                                  ArrayList mycond,
                                  String myset) {
    if ( mycond == null ) return;
    Hashtable myc;
    String mykey, myvv,myop, myvalue,mytn;
    String myenc = ServletTools.getSessionEncoding(inRequest);
    for (int i=0; i< mycond.size(); i++) {
      myc = (Hashtable)mycond.get(i);
      if (i > 0)
        out.println("\t<P align=center>" +
                    LanguageTools.tr(inRequest,"or") + "</P>");
      out.println("\t<TABLE align=center border=1 class=bar width=90%>");
      out.println("\t\t<TR class=tableheader>");
      out.println("\t\t\t<TH align=center>" +
                  LanguageTools.tr(inRequest,"item")+ "</TH>");
      out.println("\t\t\t<TH align=center>" +
                  LanguageTools.tr(inRequest,"operator")+ "</TH>");
      out.println("\t\t\t<TH align=center>" +
                  LanguageTools.tr(inRequest,"value")+ "</TH>");
      out.println("\t\t</TR>");
      for (Enumeration e = myc.keys() ; e.hasMoreElements() ;) {
        out.println("\t\t<TR class=tableline>");
        mykey =  e.nextElement().toString();
        myvv = myc.get(mykey).toString();
        mytn = mykey.replaceAll(CommonTools.SAME_NAME,"");
        out.println("\t\t\t<TD>" +
                    LanguageTools.tr(inRequest,mytn)+ "</TD>");
        if ( (myvv.length() > 13) &&
             CONDITION_OPERATORS.contains(myop = myvv.substring(0,13)) )
          myvalue = myvv.substring(13);
        else {
          myop = CommonTools.EQUAL_PREFIX;
          myvalue = myvv;
        }
        out.println("\t\t\t<TD>" +
                    LanguageTools.tr(inRequest, myop)+
                    "</TD>");
        out.println("\t\t\t<TD>" +
                    DataManagerTools.getValueShow(inRequest,
                    myvalue, null, myset) +
                    "</TD>");
        out.println("\t\t</TR>");
      }
      out.println("\t</TABLE>");
    }
  }

  /**
   * ��õ�ǰҳ��Ĳ�ѯ����
   * @param inRequest ��ǰ����
   * @param out ��ǰ���
   * @param myset ���ݼ���
   * @param myauditshow ��Ʋ����жϷ�
   * @return ��ǰҳ��Ĳ�ѯ���ã����������ִ������������ִ���������������������
   */
  public static Object[] getQuerySetup(HttpServletRequest inRequest,
                               PrintWriter out,
                               String myset,
                               String myauditshow) {

    String myw = ServletTools.getParameter(inRequest,"mywhere");
    if ( myw == null) myw = "";
    if ( myauditshow != null) {
      myw = "data" + QueryTools.CONDITION_SPLITTER +
            CommonTools.EQUAL_PREFIX +
            LanguageTools.atr(inRequest, myauditshow) +
            QueryTools.CONDITION_SPLITTER + myw ;
    }
    if ( DataManager.DATA_AUDIT.equals(myset) &&
         !AclTools.isSuperseer(inRequest) ) {
      if ( myw == null) myw = "";
      myw = "who" + QueryTools.CONDITION_SPLITTER +
            CommonTools.NOT_EQUAL_PREFIX +
            LanguageTools.atr(inRequest,DataManager.SUPER_SEER) +
            QueryTools.CONDITION_SPLITTER + myw ;
    }
    ArrayList mywherein = null;
    if ( ( myw != null) &&  !"".equals(myw) )
      mywherein = DataManagerTools.getMultiWhere(inRequest,myw);
    String mysetw =
        ServletTools.getParameter(inRequest,"mysetwhere");
    ArrayList mysetwhere = null;
    if ( ( mysetw != null) &&  !"".equals(mysetw) )
      mysetwhere = DataManagerTools.getMultiWhere(inRequest,
          mysetw);

    out.println("\n\t<SCRIPT LANGUAGE='javascript'>\n");
    out.println("\t\tfunction see_query_setup(name)");
    out.println("\t\t{");
    out.println("\t\t\tdocument.seeform.myframe.value=\"query_setup\";");
    out.println("\t\t\tdocument.seeform.myoperation.value=\"query_setup\";");
    out.println("\t\t\tdocument.seeform.mytrueoperation.value=\"" +
                ServletTools.getParameter(inRequest,"myoperation") +
                "\";");
    out.println("\t\t\tdocument.seeform.mybutton.value=\"\";");
    if ( (myw != null) && !"".equals(myw))
      out.println("\t\t\tdocument.seeform.mywhere.value=\"" +
                  myw + "\";");
    out.println("\t\t\tdocument.seeform.submit();");
    out.println("\t\t}");
    out.println("\t</SCRIPT>\n");

    Object ret[] = {myw,mywherein,mysetw, mysetwhere};
    return ret;

  }

}