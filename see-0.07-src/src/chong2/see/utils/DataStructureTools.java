package chong2.see.utils;

import chong2.see.data.base.Constants;
import chong2.see.data.base.DataStructure;
import chong2.see.servlet.common.DataManager;
import chong2.see.xml.DataStructureXmlParser;
import chong2.see.xml.DataStructureXmlWriter;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>Title: 软件工程管理环境</p>
 * <p>Description: 为软件研发提供管理数据和任务的平台</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: 虫二</p>
 *
 * <p>与管理信息结构相关的静态方法
 *
 * @author 玛瑞
 * @version 0.07
 */


public class DataStructureTools{

  /**
   * 输出结构管理页面
   * @param inRequest 当前请求
   * @param inResponse 当前响应
   */
  public static void  outStructurePage(HttpServletRequest inRequest,
                                       HttpServletResponse inResponse) {

    try{

      String  myset = ServletTools.getParameter(inRequest,"myset");
      String  myaction = ServletTools.getParameter(inRequest,"myaction");
      String myid = ServletTools.getParameter(inRequest,"myid");
      String myindex = ServletTools.getParameter(inRequest,"myindex");
      boolean dealmatrix =
          "yes".equals(ServletTools.getParameter(inRequest,"mymatrix"));
      PrintWriter out = inResponse.getWriter();
      if ( myset == null ) {
        out.println("\t<FORM name=seeform method=post " +
                    " onSubmit='return false;'>");
        out.println(" <input type=hidden name=myframe value=data>");
        out.println(" <input type=hidden name=myoperation value=config>");
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
        return;
      }

      String fname =
          DataManagerTools.getDataStructureFile(inRequest);
      if ( fname == null ) {
        PageTools.outErrorPage(inRequest,inResponse,
                               LanguageTools.tr(inRequest,"file_not_found") +
                               " : " + fname);
        return;
      }

      String mytitle = LanguageTools.tr(inRequest,"data_definition") +
                       " : " + LanguageTools.tr(inRequest,myset);
      if ( "edit".equals(myaction) &&
           ( myid != null ) ) {
        mytitle = mytitle + "-" + LanguageTools.tr(inRequest,myid);
        if ( dealmatrix )
          mytitle = mytitle + " ( " +
          LanguageTools.tr(inRequest,MatrixTools.MATRIX_VALUE) + " ) ";
      }
      PageTools.outHeader(inRequest, out,mytitle,0);

      DataStructureXmlParser myreader = new DataStructureXmlParser();
      String ret = myreader.startParsing(fname);
      Hashtable mydata = myreader.getDataSet();

      boolean ismatrix = "matrix".equals(mydata.get("type"));

      String name,attr, value,myret;
      ArrayList myv;
      String sstyle,ustyle;
      Hashtable st,su,it;
      out.println("<P class=alert>" +
                  LanguageTools.tr(inRequest,"config_warning") + "</P>");

      // 保存
      if ( "save".equals(myaction) ) {
        mydata = saveSet(inRequest,out,myset,mydata,fname,ismatrix);
        out.println("\t<SCRIPT LANGUAGE='javascript'>\n");
        out.println("\t\t parent.databar.document.seeform.myframe.click();");
        out.println("\t</SCRIPT>");
        myreader.startParsing(fname);
        mydata = myreader.getDataSet();
      }

      if ( "add".equals(myaction) ||
           "edit".equals(myaction)) {
        outItem(inRequest,out,myid,myindex,myset,
                myaction, mydata,dealmatrix);
        return;
      }

      if ( "save_item".equals(myaction) ||
           "add_item".equals(myaction)) {
        myret = saveItem(inRequest,out,myset,mydata,
                         fname,myid,myindex,dealmatrix,myaction);
        if ( Constants.SUCCESSFUL.equals(myret)) {
          out.println("\t<SCRIPT LANGUAGE='javascript'>\n");
          out.println("\t\t parent.databar.document.seeform.myframe.click();");
          out.println("\t</SCRIPT>");
          myreader.startParsing(fname);
          mydata = myreader.getDataSet();
        } else {
          PageTools.outErrorWindow(out,myret,"error");
          PageTools.outBackPage(out);
          return;
        }
      }

      if ( "remove".equals(myaction) &&
           mydata.get("items") != null ) {
        removeItem(inRequest,myset,mydata,
                   fname,myid, dealmatrix);
        out.println("\t<SCRIPT LANGUAGE='javascript'>\n");
        out.println("\t\t parent.databar.document.seeform.myframe.click();");
        out.println("\t</SCRIPT>");
        myreader.startParsing(fname);
        mydata = myreader.getDataSet();
      }

      outSet(inRequest,out,myset,mydata);

      PageTools.outFooter(inRequest, out);

    } catch (Exception ex) {
      ex.printStackTrace();
    }

  }

  /**
   * 输出数据集的整体描述
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param myset 数据集的名
   * @param mydata 数据的结构描述
   */
  public static void  outSet(HttpServletRequest inRequest,
                             PrintWriter out,
                             String myset,
                             Hashtable mydata) {


    String name,attr, value;
    ArrayList myv;
    String sstyle,ustyle;
    Hashtable st,su,it;
    out.println("<FORM method=post name=seeform onSubmit='return false;'> \n");
    out.println("\t <INPUT type=hidden name=myframe value=data>");
    out.println("\t <INPUT type=hidden name=myset value=\"" +
                myset + "\">");
    out.println("\t <INPUT type=hidden name=myoperation value=config>");
    out.println("\t <INPUT type=hidden name=myaction>");
    out.println("\t <INPUT type=hidden name=myid>");
    out.println("\t <INPUT type=hidden name=myindex> ");
    out.println("\t <INPUT type=hidden name=mymatrix value=no> ");

    boolean ismatrix = "matrix".equals(mydata.get("type"));

    out.println(LanguageTools.tr(inRequest,"items") + " : ");
    OutputController.outButton(inRequest,out,"add","add(\"no\")");
    out.println("\n\t <BR><TABLE class=data width=100% border=1>");
    out.println("\t <TR class=tableheader><TD>" +
                LanguageTools.tr(inRequest,"identify") +
                "</TD><TD>" + LanguageTools.tr(inRequest,"type") +
                "</TD><TD>" + LanguageTools.tr(inRequest,"size") +
                "</TD><TD>" + LanguageTools.tr(inRequest,"editWidth") +
                "</TD><TD>" + LanguageTools.tr(inRequest,"show") +
                "</TD></TR>");
    ArrayList itemnames = new ArrayList();
    ArrayList matrixattrs =
        CommonTools.stringToArray("notNullItems_matrix,invalidItems_matrix,matrixValues",",");
    if ( mydata.get("items") != null) {
      myv = (ArrayList)mydata.get("items");
      String mytrans;
      for ( int i = 0; i < myv.size(); i++) {
        st = (Hashtable)myv.get(i);
        if ( ismatrix &&
             "yes".equals(st.get("ismatrix")) )
          continue;
        if ( st.get("identify") == null ) continue;
        value = st.get("identify").toString();
        if ( matrixattrs.contains(value) ) continue;
        out.println("\t <TR class=tabledata>");
        itemnames.add(value);
        mytrans = LanguageTools.tr(inRequest,value);
        out.println("\t\t <TD>");
        out.println("\t\t\t ");
        OutputController.outButton(inRequest,out,"modify",
                                   "modify(\"" + value + "\",\"no\")");
        out.println("\t\t\t ");
        OutputController.outButton(inRequest,out,"insert",
                                   "insert(\"" + i + "\",\"no\")");
        out.println("\t\t\t ");
        OutputController.outButton(inRequest,out,"remove",
                                   "remove(\"" + value + "\",\"no\")");
        out.println("\t\t\t " + value);
        out.println("\t\t </TD>");
        if ( st.get("type") != null )
          value = LanguageTools.tr(inRequest,st.get("type").toString());
        else
          value = "-";
        out.println("\t\t <TD>" + value + "</TD>");
        if ( st.get("size") != null )
          value = st.get("size").toString();
        else
          value = "-";
        out.println("\t\t <TD>" + value + "</TD>");
        if ( st.get("editWidth") != null )
          value = st.get("editWidth").toString();
        else
          value = "-";
        out.println("\t\t <TD>" + value + "</TD>");
        out.println("\t\t <TD>" + mytrans + "</TD>");
        out.println("\t </TR>");
      }
    }
    out.println("\t </TABLE>");
    boolean hasitem = itemnames.size() > 0 ;
    if ( !hasitem )
      out.println("<p class=alert>" +
                  LanguageTools.tr(inRequest,"items_not_defined")+
                  "</p>");


    out.println("\t <BR><TABLE class=data width=100%>");
    for ( int i = 0; i <DataStructure.setStringAttrs.size(); i++) {
      attr = DataStructure.setStringAttrs.get(i).toString();
      if ( "matrixValues".equals(attr)) continue;
      value = null;
      if ( mydata.get(attr) != null )
        value = DataManagerTools.getValueEdit(inRequest,
            mydata.get(attr).toString(),myset);

      out.println("\t\t <TR class=tableline><TD align=right>" +
                  LanguageTools.tr(inRequest,attr) + " : </TD>" );
      out.println("\t\t\t <TD align=left>");
      if ( "keyCanModify".equals(attr) ) {
        if ( value == null)
          value = "no";
        OutputController.outRadioInput(inRequest,out,
                                       attr, value,
                                       CommonTools.stringToArray("yes,no",","),
                                       null, false , false);
      } else  if ( "defaultSort".equals(attr) ) {
        OutputController.outRadioInput(inRequest,out,attr,
                                       value,itemnames,
                                       null, true , false);
      } else  if ( "type".equals(attr) ) {
        if ( value == null)
          value = "table";
        OutputController.outRadioInput(inRequest,out,attr,value,
                                       DataStructure.DATASET_TYPES,
                                       null, false , false);
      } else  if ( "defaultOrder".equals(attr) ) {
        if ( value == null)
          value = "ascending";
        OutputController.outRadioInput(inRequest,out,"defaultOrder",
                                       value,
                                       CommonTools.stringToArray("ascending,descending",","),
                                       null, false , false);
        } else  if ( "defaultColor".equals(attr) ||
                     "description".equals(attr) ) {
          OutputController.outTextareaInput(inRequest,out,attr,
              value, -1, CommonTools.WRONG_INT, false);

        } else {
          OutputController.outTextInput(inRequest,out,attr,value);
        }
        out.println("\t\t\t </TD></TR>");
    }
    for ( int i = 0; i <DataStructure.setArrayAttrs.size(); i++) {
      attr = DataStructure.setArrayAttrs.get(i).toString();
      if ( matrixattrs.contains(attr) ) continue;
      value = null;
      myv = new ArrayList();
      if ( mydata.get(attr) != null ) {
        myv = (ArrayList)(mydata.get(attr));
        value = CommonTools.arraylistToString(myv,
            DataStructureXmlParser.LIST_SPLITTER);
      }
      if (  "operation".equals(attr) &&
            !hasitem )
        continue;

      out.println("\t\t <TR class=tableline><TD align=right>" +
                  LanguageTools.tr(inRequest,attr) + " : </TD>" );
      out.println("\t\t\t <TD align=left>");
      if ( "operation".equals(attr) ) {
        ArrayList myops = ServletTools.getAllOpearations(true);
        myops.remove("config");
        OutputController.outCheckInput(inRequest,out,attr,value,
                                       myops, null, false);
      }  else
        OutputController.outCheckInput(inRequest,out,attr,value,
                                       itemnames,null, false);
      out.println("\t\t\t </TD></TR>");
    }

    out.println("\t\t </TABLE>");

    ArrayList matrixitems = new ArrayList();
    if ( ismatrix ) {
      out.println("\t <BR><HR><BR>" +
                  LanguageTools.tr(inRequest,"matrix_items") + " : ");
      OutputController.outButton(inRequest,out,"add","add(\"yes\")");
      out.println("\n\t <BR><TABLE class=data width=100% border=1>");
      out.println("\t <TR class=tableheader><TD>" +
                  LanguageTools.tr(inRequest,"identify") +
                  "</TD><TD>" + LanguageTools.tr(inRequest,"type") +
                  "</TD><TD>" + LanguageTools.tr(inRequest,"size") +
                  "</TD><TD>" + LanguageTools.tr(inRequest,"editWidth") +
                  "</TD><TD>" + LanguageTools.tr(inRequest,"show") +
                  "</TD></TR>");
      if ( mydata.get("items") != null) {
        myv = (ArrayList)mydata.get("items");
        String mytrans;
        for ( int i = 0; i < myv.size(); i++) {
          st = (Hashtable)myv.get(i);
          if ( !"yes".equals(st.get("ismatrix")) )  continue;
          if ( st.get("identify") == null ) continue;
          value = st.get("identify").toString();
          matrixitems.add(value);
          out.println("\t <TR class=tabledata>");
          mytrans = LanguageTools.tr(inRequest,value);
          out.println("\t\t <TD>");
          out.println("\t\t\t ");
          OutputController.outButton(inRequest,out,"modify",
                                     "modify(\"" + value + "\",\"yes\")");
          out.println("\t\t\t ");
          OutputController.outButton(inRequest,out,"insert",
                                     "insert(\"" + i + "\",\"yes\")");
          out.println("\t\t\t ");
          OutputController.outButton(inRequest,out,"remove",
                                     "remove(\"" + value + "\",\"yes\")");
          out.println("\t\t\t " + value);
          out.println("\t\t </TD>");
          if ( st.get("type") != null )
            value = LanguageTools.tr(inRequest,st.get("type").toString());
          else
            value = "-";
          out.println("\t\t <TD>" + value + "</TD>");
          if ( st.get("size") != null )
            value = st.get("size").toString();
          else
            value = "-";
          out.println("\t\t <TD>" + value + "</TD>");
          if ( st.get("editWidth") != null )
            value = st.get("editWidth").toString();
          else
            value = "-";
          out.println("\t\t <TD>" + value + "</TD>");
          out.println("\t\t <TD>" + mytrans + "</TD>");
          out.println("\t </TR>");
        }
      }
      out.println("\t </TABLE><BR>");
      out.println("\t <TABLE class=data width=100%>");
      for ( int i = 0; i < matrixattrs.size(); i++) {
        attr = matrixattrs.get(i).toString();
        if ( "matrixValues".equals(attr)) continue;
        value = null;
        myv = new ArrayList();
        if ( mydata.get(attr) != null ) {
          myv = (ArrayList)(mydata.get(attr));
          value = CommonTools.arraylistToString(myv,
              DataStructureXmlParser.LIST_SPLITTER);
        }
        out.println("\t\t <TR class=tableline><TD align=right>" +
                    LanguageTools.tr(inRequest,attr) + " : </TD>" );
        out.println("\t\t <TD align=left>");
        OutputController.outCheckInput(inRequest,out,
                                       attr ,value,
                                       matrixitems,null, false);
        out.println("\t\t </TD></TR>");
      }
      value = "";
      if ( mydata.get("matrixValues") != null )
        value = DataManagerTools.getValueEdit(inRequest,
            mydata.get("matrixValues").toString(),myset);

      out.println("\t\t <TR class=tableline><TD align=right>" +
                  LanguageTools.tr(inRequest,"matrixValues") +
                  " : </TD>" );
      out.println("\t\t <TD align=left>");
      OutputController.outTextareaInput(inRequest,out,"matrixValues",
                                        value, -1, CommonTools.WRONG_INT,  false);
      out.println("\t\t <BR><FONT class=info>" +
                  LanguageTools.tr(inRequest,"matrix_info") +
                  "</FONT>");
      out.println("\t\t </TD></TR>");
      out.println("\t </TABLE><BR>");
      out.println("\t <HR>");
    }

    out.println("\t\t <P align=center>");
    OutputController.outButton(inRequest,out,"save");
    out.println("\t\t </P>");

    out.println("\t </FORM>");

    out.println("\t<SCRIPT LANGUAGE='javascript'>\n");
    out.println("\tfunction see_save()");
    out.println("\t{");
    out.println("\t\tdocument.seeform.myaction.value=\"save\";");
    out.println("\t\tdocument.seeform.mymatrix.value=\"\";");
    out.println("\t\tdocument.seeform.submit();");
    out.println("\t}");
    out.println("\tfunction see_add(ismatrix)");
    out.println("\t{");
    out.println("\t\tdocument.seeform.myaction.value=\"add\";");
    out.println("\t\tdocument.seeform.mymatrix.value= ismatrix ;");
    out.println("\t\tdocument.seeform.submit();");
    out.println("\t}");
    out.println("\tfunction see_insert(index,ismatrix)");
    out.println("\t{");
    out.println("\t\tdocument.seeform.myaction.value=\"add\";");
    out.println("\t\tdocument.seeform.mymatrix.value= ismatrix ;");
    out.println("\t\tdocument.seeform.myindex.value= index;");
    out.println("\t\tdocument.seeform.submit();");
    out.println("\t}");
    out.println("\tfunction see_modify(id,ismatrix)");
    out.println("\t{");
    out.println("\t\tdocument.seeform.myaction.value=\"edit\";");
    out.println("\t\tdocument.seeform.mymatrix.value= ismatrix ;");
    out.println("\t\tdocument.seeform.myid.value= id;");
    out.println("\t\tdocument.seeform.submit();");
    out.println("\t}");
    out.println("\tfunction see_remove(id,ismatrix)");
    out.println("\t{");
    out.println("\t\t if ( !confirm(\"" +
                LanguageTools.tr(inRequest,"sure_remove") + "\") )");
    out.println("\t\t return;");
    out.println("\t\tdocument.seeform.myaction.value=\"remove\";");
    out.println("\t\tdocument.seeform.mymatrix.value= ismatrix ;");
    out.println("\t\tdocument.seeform.myid.value= id;");
    out.println("\t\tdocument.seeform.submit();");
    out.println("\t}");
    out.println("\tfunction see_matrix_item()");
    out.println("\t{");
    out.println("\t\t window.open(\"" +
                DataManager.URL +
                "?myframe=data&myset=" + myset +
                "&myoperation=config&mybutton=matrixitem" +
                "\", " + "\"matrixitem" + myset + "\"," +
                " \"toolbar=no,menubar=no,status=yes,resizable=yes,scrollbars=yes,width=500,height=400\");");
    out.println("\t}");
    out.println("\t</SCRIPT>\n");

  }

  /**
   * 保存数据集的描述结构
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param myset 数据集名
   * @param mydata 数据集的描述结构
   * @param fname 数据集的描述结构文件名
   * @param ismatrix 是否是矩阵表
   * @return 保存后的结构定义
   */
  public static Hashtable saveSet(HttpServletRequest inRequest,
                                  PrintWriter out,
                                  String myset,
                                  Hashtable mydata,
                                  String fname,
                                  boolean  ismatrix) {

    String name,attr, value;
    ArrayList myv;
    String sstyle,ustyle;
    Hashtable st,su,it;
    Hashtable myold = new Hashtable();
    if ( mydata != null ) myold.putAll(mydata);
    ArrayList mynames = DataStructure.setStringAttrs;
    for ( int i = 0; i < mynames.size(); i++) {
      attr = mynames.get(i).toString();
      value = ServletTools.getParameter(inRequest,"input_" + attr);
      if ( ( value == null ) || "".equals(value) )
        mydata.remove(attr);
      else
        mydata.put(attr,value);
    }
    boolean hasitems = false;
    if ( mydata.get("items") != null ) {
      ArrayList myitems = (ArrayList)mydata.get("items");
      if ( myitems.size() > 0 )
        hasitems = true;
    }
    mynames = DataStructure.setArrayAttrs;
    for ( int i = 0; i < mynames.size(); i++) {
      attr = mynames.get(i).toString();
      if ( "operation".equals(attr) &&
           !hasitems )
        value = null;
      else {
        value =
            CommonTools.arrayToString(inRequest.getParameterValues("input_" + attr),
            DataStructureXmlParser.LIST_SPLITTER);
        value = ServletTools.filterParameter(value);
      }
      if ( ( value == null ) || "".equals(value) )
        mydata.remove(attr);
      else {
        mydata.put(attr,CommonTools.stringToArray(value,
            DataStructureXmlParser.LIST_SPLITTER));
      }
    }

    DataStructureXmlWriter myw = new DataStructureXmlWriter();
    String retw = myw.writeData(mydata,fname,
                                ServletTools.getAppDefaultCharset(inRequest));

    ArrayList myaudit =
        ServletTools.getAppDataAuditTypes(inRequest);
    if ( myaudit.contains(LanguageTools.atr(inRequest,"config"))) {
      AuditTools.addAudit(inRequest,myset,"config",
                          null  , myold  , mydata , retw,null , null);
    }

    return mydata;

  }

  /**
   * 输出数据集的一个域的描述结构
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param myid 域的标识
   * @param myindex 域所在的顺序号
   * @param myset 数据集的名
   * @param myaction 操作名
   * @param mydata 数据集的描述结构
   * @param dealmatrix 是否是矩阵表
   */
  public static void  outItem(HttpServletRequest inRequest,
                              PrintWriter out,
                              String myid,
                              String myindex,
                              String myset,
                              String myaction,
                              Hashtable mydata,
                              boolean  dealmatrix) {

    if ( mydata == null )
      return ;

    String name,attr, value;
    ArrayList myv;
    String sstyle,ustyle;
    Hashtable st,su,it;
    out.println("<form method=post name=seeform onSubmit='return false;'> \n");
    out.println(" <input type=hidden name=myframe value=data>");
    out.println(" <input type=hidden name=myset value=\"" +
                myset + "\">");
    out.println(" <input type=hidden name=myoperation value=config>");
    out.println(" <input type=hidden name=myaction>");
    out.println(" <input type=hidden name=myid ");
    if ( myid != null )
      out.print(" value=\"" + myid + "\" ");
    out.println(">");
    value = "no";
    if ( dealmatrix )
      value = "yes" ;
    out.println("\t <INPUT type=hidden name=mymatrix value=\"" +
                value + "\"> ");
    out.println(" <input type=hidden name=myindex ");
    if ( myindex != null )
      out.print(" value=\"" + myindex + "\" ");
    out.println(">");

    ArrayList myitems;
    if ( ( mydata.get("items") == null ) ||
         ( myid == null ) || "".equals(myid) )
      myaction = "add";
    it = new Hashtable();
    if ( "edit".equals(myaction) ) {
      myaction = "add";
      if ( mydata.get("items") != null ) {
        myitems = (ArrayList)mydata.get("items");
        for ( int i=0; i< myitems.size(); i++) {
          it = (Hashtable)myitems.get(i);
          if ( it.get("identify") != null ) {
            if ( it.get("identify").toString().equals(myid) ) {
              myaction = "edit";
              break;
            }
          }
        }
      }
    }

    if ( "add".equals(myaction) )
      it = new Hashtable();

    out.println("\t<TABLE class=data width=100%>");

    for ( int i = 0; i <DataStructure.itemStringAttrs.size(); i++) {
      attr = DataStructure.itemStringAttrs.get(i).toString();
      if ( "ismatrix".equals(attr) ) continue;
      out.println("\t\t <TR class=tableline><TD align=right width=200>" +
                  LanguageTools.tr(inRequest,attr) + " : </TD>");
      out.println("\t\t\t <TD align=left>");
      if ( "type".equals(attr) ) {
        if ( it.get(attr) != null)
          value = it.get(attr).toString();
        else
          value = "string";
        OutputController.outRadioInput(inRequest,out,attr,value,
                                       DataStructure.DATAITEM_DATA_TYPES,
                                       null, false , false);
      } else if ( "valueTranslated".equals(attr) ) {
        if ( it.get(attr) != null)
          value = it.get(attr).toString();
        else
          value = "no";
        OutputController.outRadioInput(inRequest,out,attr,value,
                                       CommonTools.stringToArray("yes,no",","),
                                       null, false , false);
      } else if ( "valueConstraintType".equals(attr) ) {
        OutputController.outRadioInput(inRequest,out,attr,
                                       it.get(attr),
                                       DataStructure.DATAITEM_VALUE_CONSTRAINT_TYPES,
                                       null, true , false);
      } else if ( "editControllerType".equals(attr) ) {
        if ( it.get(attr) != null)
          value = it.get(attr).toString();
        else
          value = "text";
        OutputController.outRadioInput(inRequest,out,attr,value,
                                       DataStructure.DATAITEM_EDIT_CONTROLLOR_TYPES,
                                       null, true , false);
      } else if ( "showControllerType".equals(attr) ) {
        if ( it.get(attr) != null)
          value = it.get(attr).toString();
        else
          value = "text";
        OutputController.outRadioInput(inRequest,out,attr,value,
                                       DataStructure.DATAITEM_SHOW_CONTROLLOR_TYPES,
                                       null, false , false);
      } else {
        OutputController.outTextInput(inRequest,out,attr,
                                      it.get(attr) );
        if ( "editWidth".equals(attr) )
          out.println(LanguageTools.tr(inRequest,"default") +
                      " : " + OutputController.MAX_INPUT_SIZE);
      }
      out.println("\t\t\t </TD></TR>");
    }

    for ( int i = 0; i <DataStructure.itemHashAttrs.size(); i++) {
      attr = DataStructure.itemHashAttrs.get(i).toString();
      out.println("\t\t <TR class=tableline><TD align=right>" +
                  LanguageTools.tr(inRequest,attr) + " : </TD>");
      out.println("\t\t\t <TD align=left>");
      value = null;
      if ( it.get(attr) != null)
        value = CommonTools.hashToString((Hashtable)it.get(attr),
            DataStructureXmlParser.HASH_SPLITTER ,
            null);
      OutputController.outTextInput(inRequest,out,attr,value );
      out.println("\t\t\t </TD></TR>");
    }
    out.println("\t </TABLE>");

    out.println("\t\t <P align=center>");
    OutputController.outSaveResetBar(inRequest,out,"seeform");
    out.println("\t\t </P>");

    out.println("\t </FORM>");

    out.println("\t<SCRIPT LANGUAGE='javascript'>\n");
    out.println("\tfunction see_save()");
    out.println("\t{");
    out.println("\t\t var myv = document.seeform.input_identify.value;");
    out.println("\t\t if ( myv.length < 1 ) ");
    out.println("\t\t {");
    out.println("\t\t\t alert(\"" +
                LanguageTools.tr(inRequest,"identify") +
                " : " +
                LanguageTools.tr(inRequest,"should_not_null") +
                "\");");
    out.println("\t\t\t return -1;");
    out.println("\t\t }");
    out.println("\t\t if ( (myv.indexOf(\"" + MatrixTools.NAME_SPLITTER +
                "\") > -1 ) || (myv.indexOf(\"" + MatrixTools.MATRIX_VALUE +
                "\") > -1 )  ) ");
    out.println("\t\t {");
    out.println("\t\t\t alert(\"" +
                LanguageTools.tr(inRequest,"identify") +
                " : " +
                LanguageTools.tr(inRequest,"can_not_include") +
                "     " + MatrixTools.NAME_SPLITTER + "     " +
                MatrixTools.MATRIX_VALUE + "\");");
    out.println("\t\t\t return -1;");
    out.println("\t\t }");
    out.println("\t\t var myv = document.seeform.input_identify.value;");
    out.println("\t\t var myv = myv.toLowerCase();");
    out.println("\t\t var mys = myv.substring(0,1);");
    out.println("\t\t var invalidchars = \"0123456789.-\" ; ");
    out.println("\t\t if ( (invalidchars.indexOf(mys) > -1 ) || ( myv.indexOf(\"xml\") == 0 ) ) {");
    out.println("\t\t\t alert(\"" +
                LanguageTools.tr(inRequest,"field_name_warning") +
                "\");");
    out.println("\t\t\t return -1;");
    out.println("\t\t }");
    if ( "edit".equals(myaction) )
      out.println("\t\t document.seeform.myaction.value=\"save_item\";");
    else
      out.println("\t\t document.seeform.myaction.value=\"add_item\";");
    out.println("\t\t document.seeform.submit();");
    out.println("\t}");
    out.println("\t</SCRIPT>\n");

    PageTools.outFooter(inRequest, out);
    return;

  }

  /**
   * 保存数据集的一个域的描述结构
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param myset 数据集名
   * @param mydata 数据集的描述结构
   * @param fname 数据集的描述结构文件名
   * @param myid 域的标识
   * @param myindex 域所在的顺序号
   * @param dealmatrix 是否是矩阵表
   * @param myaction 当前操作
   * @return 是否成功
   */
  public static String saveItem(HttpServletRequest inRequest,
                                   PrintWriter out,
                                   String myset,
                                   Hashtable mydata,
                                   String fname,
                                   String myid,
                                   String myindex,
                                   boolean  dealmatrix,
                                   String myaction) {

    String mygid =
        ServletTools.getParameter(inRequest,"input_identify" );
    if ( mygid == null )   return "invalid_data";
    String name,attr, value;
    ArrayList myv;
    String sstyle,ustyle;
    Hashtable st,su,it;
    ArrayList myitems;
    if ( mydata.get("items") != null )
      myitems = (ArrayList)mydata.get("items");
    else {
      myitems = new ArrayList();
      mydata.put("items",myitems);
    }

    boolean isnew = true;
    it = new Hashtable();
    for ( int i=0; i< myitems.size(); i++) {
      it = (Hashtable)myitems.get(i);
      if ( it.get("identify") != null ) {
        if ( it.get("identify").toString().equals(myid) ) {
          isnew = false;
          break;
        }
      }
    }

    if ( isnew ||
         ( !isnew && !mygid.equals(myid) )) {
      Hashtable myi;
      for ( int i = 0; i < myitems.size(); i++) {
        myi = (Hashtable)myitems.get(i);
        if ( mygid.equals(myi.get("identify")))
          return "already_exists";
      }
    }

    if ( isnew ) {
      it = new Hashtable();
      int ii = CommonTools.WRONG_INT;
      if ( myindex != null )
        ii = CommonTools.stringToInt(myindex);
      if ( ii == CommonTools.WRONG_INT )
        myitems.add(it);
      else
        myitems.add(ii,it);
    }

    Hashtable myold = new Hashtable();
    myold.putAll(it);

    for ( int i = 0; i <DataStructure.itemStringAttrs.size(); i++) {
      attr = DataStructure.itemStringAttrs.get(i).toString();
      value = ServletTools.getParameter(inRequest,"input_" + attr);
      if ( ( value == null ) || "".equals(value) )
        it.remove(attr);
      else
        it.put(attr,value);
    }

    for ( int i = 0; i <DataStructure.itemHashAttrs.size(); i++) {
      attr = DataStructure.itemHashAttrs.get(i).toString();
      value = ServletTools.getParameter(inRequest,"input_" + attr);
      if ( ( value == null ) || "".equals(value) )
        it.remove(attr);
      else
        it.put(attr,CommonTools.stringToHash(value,
            DataStructureXmlParser.HASH_SPLITTER));
    }

    if ( dealmatrix ) {
      it.put("ismatrix","yes");
    } else {
      it.remove("ismatrix");
    }

    DataStructureXmlWriter myw = new DataStructureXmlWriter();
    String retw = myw.writeData(mydata,fname,
                                ServletTools.getAppDefaultCharset(inRequest));

    ArrayList myaudit =
        ServletTools.getAppDataAuditTypes(inRequest);
    if ( myaudit.contains(LanguageTools.atr(inRequest,"config"))) {
      Hashtable myk = new Hashtable();
      myk.put("item",it.get("identify").toString());
      AuditTools.addAudit(inRequest,myset,"config",
                          myk  , myold  , it , retw,null , null);
    }

    return Constants.SUCCESSFUL;

  }

  /**
   * 删除数据集的一个域的描述结构
   * @param inRequest 当前请求
   * @param myset 数据集的名
   * @param mydata 数据集的描述结构
   * @param fname 数据集的描述结构文件名
   * @param myid 域的标识
   * @param dealmatrix 是否是矩阵表
   * @return 删除后的结构定义
   */
  public static Hashtable removeItem(HttpServletRequest inRequest,
                                     String myset,
                                     Hashtable mydata,
                                     String fname,
                                     String myid,
                                     boolean  dealmatrix) {

    String name,attr, value;
    ArrayList myv;
    String sstyle,ustyle;
    Hashtable st,su,it;
    if ( mydata.get("items") == null ) return mydata;
    ArrayList myitems = (ArrayList)mydata.get("items");

    it = new Hashtable();
    boolean isok = false;
    for ( int i=0; i< myitems.size(); i++) {
      it = (Hashtable)myitems.get(i);
      if ( it.get("identify") == null ) continue;
      if ( it.get("identify").toString().equals(myid) ) {
        myitems.remove(i);
        isok = true;
        break;
      }
    }
    if ( !isok ) return mydata;

    mydata.put("items",myitems);

    DataStructureXmlWriter myw = new DataStructureXmlWriter();
    String retw =myw.writeData(mydata,fname,
                               ServletTools.getAppDefaultCharset(inRequest));

    ArrayList myaudit =
        ServletTools.getAppDataAuditTypes(inRequest);
    if ( myaudit.contains(LanguageTools.atr(inRequest,"config"))) {
      Hashtable myk = new Hashtable();
      myk.put("item",it.get("identify").toString());
      AuditTools.addAudit(inRequest,myset,"config",
                          myk  , it  , null, retw,null , null);
    }

    return mydata;

  }

}