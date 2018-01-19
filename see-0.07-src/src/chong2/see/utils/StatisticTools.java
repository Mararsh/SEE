package chong2.see.utils;

import chong2.see.data.base.Constants;
import chong2.see.servlet.common.DataManager;
import chong2.see.xml.DataStructureXmlParser;
import chong2.see.data.DataReader;
import chong2.see.data.*;
import chong2.see.xml.DataXmlWriter;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>Title: 软件工程管理环境</p>
 * <p>Description: 为软件研发提供管理数据和任务的平台</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: 虫二</p>
 *
 * 与统计相关的静态方法
 *
 * @author 玛瑞
 * @version 0.07
 */

final public class StatisticTools {

  /**
   * 统计的类型
   */
  public static ArrayList STATISTIC_TYPE = new ArrayList();
  static {
    STATISTIC_TYPE.add("count");
    STATISTIC_TYPE.add("cross_count");
//    STATISTIC_TYPE.add("accumulative_count");
    STATISTIC_TYPE.add("trend");
//    STATISTIC_TYPE.add("calculation");
  }
  /**
   * 总数的标记。用户不要用这个字段名
   */
  public static String STATISTIC_TOTAL = "SEE-count-total";
  /**
   * “其它值”的标记。用户不要用这个字段名
   */
  public static String STATISTIC_OTHER = "SEE-count-other";
  /**
   * 空值的标记。用户不要用这个字段名
   */
  public static String STATISTIC_NULL = "SEE-count-null";
  /**
   * 统计分隔符。用户不要用这个字符串
   */
  public static String STATISTIC_SPLITTER = ":a:a:d-a:a:d:";

  /**
   * 输出统计页面的框架
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param mystrucReader 数据定义
   * @param myset 数据集名
   * @return 是否成功
   */
  public static String outStatisticFrame(HttpServletRequest inRequest,
      PrintWriter out,
      DataStructureXmlParser mystrucReader,
      String myset) {

    if ( mystrucReader == null )
      return "invalid_data";

    boolean ismatrix = mystrucReader.hasMatrixDefine();

    ArrayList mytypes = new ArrayList(STATISTIC_TYPE);
    ArrayList mydt = mystrucReader.getDateTimeItems();
    ArrayList mydn = mystrucReader.getNumberItems();
    if ( ( mydt == null ) ||
         ( mydt.size() < 1 ) ||
         ( mydn == null ) ||
         ( mydn.size() < 1 ) )
      mytypes.remove("trend");
    ArrayList mymaindt = new ArrayList();
    ArrayList mymatrixdt = new ArrayList();
    ArrayList mymaindn = new ArrayList();
    ArrayList mymatrixdn = new ArrayList();
    ArrayList myitems = mystrucReader.getEditItemNames();
    Object mydd;
    for ( int i=0; i< mydt.size(); i++) {
      mydd = mydt.get(i);
      if ( myitems.contains(mydd))
        mymaindt.add(mydd);
      else
        mymatrixdt.add(mydd);
    }
    for ( int i=0; i< mydn.size(); i++) {
      mydd = mydn.get(i);
      if ( myitems.contains(mydd))
        mymaindn.add(mydd);
      else
        mymatrixdn.add(mydd);
    }
    int mytsize = mymaindt.size();
    int mynsize = mymaindn.size();
    ArrayList mymatrixvalues = mystrucReader.getMatrixValues();
    if ( ismatrix &&
         (mymatrixvalues != null) ) {
      mytsize = mytsize + mymatrixdt.size() * mymatrixvalues.size();
      mynsize = mynsize + mymatrixdn.size() * mymatrixvalues.size();
    }

    String  mytype =
        ServletTools.getParameter(inRequest,"mystattype");
    if ( !mytypes.contains(mytype) )
      mytype = "count";

    DataManager.initOutput(inRequest,out,mystrucReader);

    String mytitle = ServletTools.getParameter(inRequest,"mystatisticsetup");
    String mybutton = ServletTools.getParameter(inRequest,"mybutton");

    DataStructureXmlParser mysetupstrucReader =
        DataManagerTools.getDataStructureReader(inRequest,
        DataManager.STATISTIC_SETUP) ;
    Hashtable mysetup = null;
    if ( ( mysetupstrucReader != null ) &&
         !"ok".equals(mybutton) ) {
      if ( ( mybutton != null) &&
           ( !"set".equals(mybutton) )  &&
           (  mytitle != null) ) {
        mytitle =
            writeShowSetup(inRequest,out,mysetupstrucReader,
            myset,mytitle, mybutton,mytype);
      }
      mysetup = showSetupList(inRequest,out,mysetupstrucReader,
                              mytitle,mytype);

    }

    out.println("\t<INPUT type=hidden name=mystattype value=" +
                mytype + ">");

    if ( !"ok".equals(mybutton) )
      OutputController.outTabpage(inRequest,out,
                                  mytype, mytypes,mytypes,
                                  mytypes, null,false);

    Object myquery[] =
        QueryTools.getQuerySetup(inRequest,out,myset,null);
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

    if (  mytype.startsWith("count") )
      mysetwhere = outCountPage(inRequest,out,mystrucReader,
                                myset,mybutton,
                                mywherein,mysetwhere,ismatrix);
    else if ( mytype.startsWith("cross_count") )
      mysetwhere = outCrossCountPage(inRequest,out,mystrucReader,
                                     myset,mybutton,
                                     mywherein,mysetwhere,ismatrix);
    else if ( mytype.startsWith("trend") )
      mysetwhere = outTrendPage(inRequest,out,mystrucReader,
                                myset,mybutton,
                                mymaindt,mymaindn,mymatrixdt,mymatrixdn,
                                mywherein,mysetwhere,ismatrix);


    // 显示查询区
    out.println("\t<P align=left class=info>");
    if ( !"ok".equals(mybutton) )
      OutputController.outButton(inRequest,out,"query_setup");
    if ( ((  myw != null) &&  !"".equals(myw)) ||
         ( ( mysetwhere != null) && (mysetwhere.size()> 0 ))  )
      out.println(LanguageTools.tr(inRequest,
      "current_query_setup") +
      " : <BR>");
    if ( ( mysetwhere != null) &&
         (mysetwhere.size()> 0 ) ) {
      out.println(LanguageTools.tr(inRequest,"query_info"));
      QueryTools.outCondition(inRequest,out,mysetwhere,myset);
    }
    if ( (  myw != null) &&  !"".equals(myw) ) {
      out.println("\t<P align=left class=info>");
      out.println(LanguageTools.tr(inRequest,"query_baseinfo"));
      QueryTools.outCondition(inRequest,out,myw,myset);
    }
    out.println("\t</P>");

    out.println("\n\t<SCRIPT LANGUAGE='javascript'>\n");
    out.println("\t\tfunction see_tabselect(myselect,myop,mywhere)");
    out.println("\t\t{");
    out.println("\t\t\tdocument.seeform.mystattype.value=myselect;");
    out.println("\t\t\tdocument.seeform.mybutton.value=\"\";");
    out.println("\t\t\tdocument.seeform.submit();");
    out.println("\t\t}");

    out.println("\t</SCRIPT>\n");

    out.println("</FORM>");

    if ( ( "set".equals(mybutton)) &&
         ( mysetup != null) ) {

      outSet(inRequest,out,mystrucReader,mytype,
             mytsize,mynsize, mysetup,ismatrix);

    }

    return Constants.SUCCESSFUL;
  }

  /**
   * 输出计数的页面
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param mystrucReader 数据定义
   * @param myset 数据集名
   * @param mybutton 当前按钮
   * @param mywhere 基础查询条件
   * @param mysetwhere 设置查询条件
   * @param ismatrix 是否是矩阵表
   * @return 设置查询条件
   */
  public static ArrayList outCountPage(HttpServletRequest inRequest,
                                       PrintWriter out,
                                       DataStructureXmlParser mystrucReader,
                                       String myset,
                                       String mybutton,
                                       ArrayList mywhere,
                                       ArrayList mysetwhere,
                                       boolean   ismatrix) {

    ArrayList myitems = mystrucReader.getEditItemNames();
    String mygetitem = ServletTools.getParameter(inRequest,"input_item");
    String myitem = mygetitem;
    String myvalue =
        ServletTools.getParameter(inRequest,"input_mycountvalue");

    Hashtable mymaps = null;
    ArrayList mymitems = null;
    String mymatrixvalue = null;
    if ( ismatrix ) {
      ArrayList matrixitems = mystrucReader.getVaildMatrixItemNames();
      if ( matrixitems == null ) return null;
      ArrayList mymatrixvalues = mystrucReader.getMatrixValues();
      if ( mymatrixvalues == null ) return null;
      mymitems = new ArrayList();
      mymaps = new Hashtable();
      String myname,myshow;
      for ( int i =0 ; i< mymatrixvalues.size(); i++) {
        for ( int j =0 ; j< matrixitems.size(); j++) {
          myname = mymatrixvalues.get(i).toString() +
                     MatrixTools.NAME_SPLITTER +
                     matrixitems.get(j).toString();
          myshow = LanguageTools.tr(inRequest,mymatrixvalues.get(i).toString()) +
                   "-" +
                   LanguageTools.tr(inRequest,matrixitems.get(j).toString());
          mymitems.add(myname);
          mymaps.put(myname,myshow);
       }
      }
      ArrayList mytitem = CommonTools.stringToArray(myitem,
          MatrixTools.NAME_SPLITTER);
      if ( ( mytitem != null )  &&
           ( mytitem.size() > 1 ) ) {
        mymatrixvalue = mytitem.get(0).toString();
        myitem = mytitem.get(1).toString();
      }
    }

    if ( "ok".equals(mybutton) &&
         ( myitem != null ) ) {
      Hashtable mycount;
      ArrayList myv = null;
      if ( myvalue != null )
        myv = CommonTools.stringToArray(myvalue,
                                        DataReader.LIST_SPLITTER);
      mycount = getCount(inRequest,mystrucReader,myset,myitem,
                         myv,mywhere,mysetwhere, mymatrixvalue);
      if ( mycount != null ) {
        outCountResult(inRequest,out,myset,myitem,mycount,
                       mymatrixvalue);
        return mysetwhere;
      }
    }

    out.println("<TABLE  border=1 class=bar width=100%>");
    out.println("<TR class=tableheader><TH colspan=2 align=center>" +
                LanguageTools.tr(inRequest,"count") + "</TH></TR>");
    out.println("<TR class=tabledata><TD align=right>" +
                LanguageTools.tr(inRequest,"item") + "</TD>");
    out.println("<TD align=left>");
    OutputController.outRadioInput(inRequest,out,"item",mygetitem,
                                   myitems,  null,false,false);
    if ( ismatrix ) {
      out.println("<BR>");
      OutputController.outRadioInput(inRequest,out,"item",mygetitem,
                                     mymitems,  mymaps,false,false);
    }
    out.println("</TD></TR>");
    out.println("<TR class=tabledata><TD align=right>" +
                LanguageTools.tr(inRequest,"value") + "</TD>");
    out.println("<TD align=left>");
    OutputController.outTextInput(inRequest,out,"mycountvalue",myvalue);
    out.println("<BR><FONT class=info>" +
                LanguageTools.tr(inRequest,"count_info") + "</FONT>");
    out.println("</TD></TR>");
    out.println("<TR class=tabledata><TD align=right>" +
                LanguageTools.tr(inRequest,"image") + "</TD>");
    out.println("<TD align=left>");
    String myv = CommonTools.arraylistToString(
        CommonTools.arrayToList(inRequest.getParameterValues("input_image")),
        DataReader.LIST_SPLITTER);
    OutputController.outCheckInput(inRequest,out,"image",myv,
                                   CommonTools.stringToArray("piechart,histogram",","),
                                   null,false);
    out.println("</TD></TR>");
    out.println("</TABLE><BR>");

    out.println("<BR><P align=center>");
    OutputController.outSaveResetBar(inRequest,out,"seeform","ok","cancel");
    out.println("</P>");

    out.println("\n\t<SCRIPT LANGUAGE='javascript'>\n");
    out.println("\t\tfunction see_ok()");
    out.println("\t\t{");
    out.println("\t\t\t if ( see_checkCheckNull() < 0 ) return;");
    out.println("\t\t\t var oldt = document.seeform.target;");
    out.println("\t\t\tdocument.seeform.mybutton.value=\"ok\";");
    out.println("\t\t\tdocument.seeform.target=\"_blank\";");
    out.println("\t\t\tdocument.seeform.submit();");
    out.println("\t\t\tdocument.seeform.mybutton.value= \"\";");
    out.println("\t\t\tdocument.seeform.target= oldt;");
    out.println("\t\t\tdocument.seeform.submit();");
    out.println("\t\t}");


    Hashtable mycheck = new Hashtable();
    int mysize = myitems.size();
    if ( ismatrix )
      mysize = mysize + mymitems.size();
    mycheck.put("item", CommonTools.intToString(mysize));
    OutputController.outCheckSelectNull(inRequest,out,"seeform",mycheck,true);
    out.println("\t</SCRIPT>\n");

    return mysetwhere;

  }

  /**
   * 获得计数值
   * @param inRequest 当前请求
   * @param mystrucReader 数据定义
   * @param myset 数据集名
   * @param myitem 要计数的字段名
   * @param myvalues 要计数的字段值列表，null表示系统自动计数所有值。
   * @param mywhere 基础查询条件
   * @param mysetwhere 设置查询条件
   * @param mymatrixvalue 矩阵值名
   * @return 计数结果
   */
  public static Hashtable getCount(HttpServletRequest inRequest,
                                   DataStructureXmlParser mystrucReader,
                                   String myset,
                                   String myitem,
                                   ArrayList myvalues,
                                   ArrayList mywhere,
                                   ArrayList mysetwhere,
                                   String  mymatrixvalue) {
    if ( myset == null ) return null;
    if ( myitem == null ) return null;
    if ( mystrucReader == null ) return null;

    boolean  isall = (myvalues == null);

    DataReader mymatrixdataReader = null;
    if ( mymatrixvalue != null ) {
      mymatrixdataReader =
          DataManagerTools.getDataReader( inRequest,mystrucReader,
          myset + MatrixTools.MATRIX_FILE_SUFFIX,
          null,mywhere, mysetwhere,0,-1);
      if ( mymatrixdataReader == null ) return null;
    }

    DataReader mydataReader =
        DataManagerTools.getDataReader( inRequest,mystrucReader,
        myset,null,mywhere, mysetwhere,0,-1);
    if ( mydataReader == null ) return null;
    ArrayList mydata = mydataReader.getRecords();
    if ( mydata == null ) return null;

    Hashtable mycount = new Hashtable();
    mycount.put(STATISTIC_TOTAL, mydata.size() + "");
    Hashtable myd,mykeyvalue ;
    Object myv, mycc;
    int myc;
    ArrayList mykeys = mystrucReader.getKeys();
    for ( int i=0; i< mydata.size(); i++) {
      myd = (Hashtable)mydata.get(i);
      if ( mymatrixdataReader != null ) {
        mykeyvalue = DataReaderGetor.getRecordKey(mykeys,myd);
        if ( mykeyvalue == null ) continue;
        mykeyvalue.put(MatrixTools.MATRIX_VALUE,
                       LanguageTools.atr(inRequest,mymatrixvalue));
        myd = mymatrixdataReader.getRecord(mykeyvalue);
      }
      if ( myd != null)
        myv = myd.get(myitem);
      else
        myv = null;

      if ( !isall ) {
        if ( ( myv == null ) ||
             !myvalues.contains(myv))
          myv = STATISTIC_OTHER;
      }
      if (  myv == null  )
        myv = STATISTIC_NULL;
      mycc = mycount.get(myv);
      if ( mycc == null ) {
        mycount.put(myv,"1");
      } else {
        myc = CommonTools.stringToInt( mycc.toString());
        if ( myc == CommonTools.WRONG_INT )
          mycount.put(myv, "1");
        else
          mycount.put(myv,(++myc) + "");
      }
    }

    return mycount;
  }

  /**
   * 输出计数结果
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param myset 数据集名
   * @param myitem 计数的字段名
   * @param mycount 计数结果
   * @param mymatrixvalue 矩阵值名
   */
  public static void outCountResult(HttpServletRequest inRequest,
                                    PrintWriter out,
                                    String myset,
                                    String myitem,
                                    Hashtable mycount,
                                   String  mymatrixvalue) {
    if ( myitem == null ) return ;
    if ( mycount == null ) return ;
    String mytotal = mycount.get(STATISTIC_TOTAL).toString();
    mycount.remove(STATISTIC_TOTAL);
    if ( mycount.size() < 1 ) return;
    out.println("<P><TABLE  border=1 class=bar  align=center width=80%>");
    out.println("<TR class=tableheader >");
    String myname = LanguageTools.tr(inRequest,myitem);
    if (  mymatrixvalue != null )
      myname = LanguageTools.tr(inRequest,mymatrixvalue) +
      "-" + myname;
    myname = "\"" + myname + "\"";
    out.println("<TH> " + myname + "</TH>");
    out.println("<TH>" +
                LanguageTools.tr(inRequest,"count") + "</TH>");
    out.println("<TH>" +
                LanguageTools.tr(inRequest,"rate") + "</TH></TR>");
    String kk,vv,rr;
    int mytt = CommonTools.stringToInt(mytotal);
    if ( mytt < 1 ) mytt = 1;
    ArrayList mysort = CommonTools.sortIntHash(mycount);
    String mynull = LanguageTools.tr(inRequest,STATISTIC_NULL);
    String myother = LanguageTools.tr(inRequest,StatisticTools.STATISTIC_OTHER);
    for ( int i=0; i< mysort.size(); i++) {
      kk = mysort.get(i).toString();
      vv = mycount.get(kk).toString();
      rr = CommonTools.getPercent(vv,mytt,2) + " %";
      out.println("<TR class=tabledata>");
      if ( StatisticTools.STATISTIC_OTHER.equals(kk)  )
        kk = myother;
      else if ( STATISTIC_NULL.equals(kk)  )
        kk = mynull;
      out.println("<TD align=center>" + kk + "</TD>");
      out.println("<TD align=right>" + vv + "</TD>");
      out.println("<TD align=right>" + rr + "</TD></TR>");
    }
    out.println("<TR class=tabletotal>");
    out.println("<TD align=center>" +
                LanguageTools.tr(inRequest,"total_count") +
                "</TD>");
    out.println("<TD align=right>" + mytotal + "</TD>");
    out.println("<TD align=right>100 % </TD></TR>");
    out.println("</TABLE><BR>");

    String myimage[] = inRequest.getParameterValues("input_image");
    if ( myimage != null ) {
      String mytitle = LanguageTools.tr(inRequest,"count") + " - " +
                       LanguageTools.tr(inRequest,myset) + " - " +
                       myname ;
      Hashtable myinfo = new Hashtable();
      myinfo.put("item", myname);
      myinfo.put("count_show_limit",
                 LanguageTools.tr(inRequest,"count_show_limit"));
      myinfo.put(StatisticTools.STATISTIC_OTHER,myother);
      myinfo.put(STATISTIC_NULL,mynull);
      myinfo.put("xname", LanguageTools.tr(inRequest,"value"));
      myinfo.put("yname", LanguageTools.tr(inRequest,"count"));
      String myfile,myret;
      for (int i=0; i< myimage.length; i++) {
        myfile = getImageFile(inRequest,myset,"count",myimage[i]);
        if (myfile == null) continue;
        myinfo.put("title", mytitle + " - " +
                   LanguageTools.tr(inRequest,myimage[i]));
        myret = ImageTools.writeCountImage(myfile,myinfo, mycount,
            CommonTools.stringToInt(mytotal),
            mysort,myimage[i]);
        if ( Constants.SUCCESSFUL.equals(myret)) {
          String mylink = DataManager.TMP_FILE_PREFIX +
                          CommonTools.getFileName(myfile);
          out.println("<P align=center>" +
                      "<IMG border=0 src=\"" +
                      mylink + "\"></P>");
        }
      }
    }

  }

  /**
   * 输出交叉计数的页面
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param mystrucReader 数据定义
   * @param myset 数据集名
   * @param mybutton 当前按钮
   * @param mywhere 基础查询条件
   * @param mysetwhere 设置查询条件
   * @param ismatrix 是否是矩阵表
   * @return 设置查询条件
   */
  public static ArrayList outCrossCountPage(HttpServletRequest inRequest,
      PrintWriter out,
      DataStructureXmlParser mystrucReader,
      String myset,
      String mybutton,
      ArrayList mywhere,
      ArrayList mysetwhere,
      boolean   ismatrix) {

    ArrayList myitems = mystrucReader.getEditItemNames();

    String mygetitem = ServletTools.getParameter(inRequest,"input_item");
    String mygetitem2 = ServletTools.getParameter(inRequest,"input_cross_item");
    String myitem = mygetitem;
    String myitem2 = mygetitem2;
    String myvalue = ServletTools.getParameter(inRequest,"input_mycountvalue");
    String myvalue2 = ServletTools.getParameter(inRequest,"input_mycountvalue2");

    Hashtable mymaps = null;
    ArrayList mymitems = null;
    String mymatrixvalue = null;
    String mymatrixvalue2 = null;
    if ( ismatrix ) {
      ArrayList matrixitems = mystrucReader.getVaildMatrixItemNames();
      if ( matrixitems == null ) return null;
      ArrayList mymatrixvalues = mystrucReader.getMatrixValues();
      if ( mymatrixvalues == null ) return null;
      mymitems = new ArrayList();
      mymaps = new Hashtable();
      String myname,myshow;
      for ( int i =0 ; i< mymatrixvalues.size(); i++) {
        for ( int j =0 ; j< matrixitems.size(); j++) {
          myname = mymatrixvalues.get(i).toString() +
                     MatrixTools.NAME_SPLITTER +
                     matrixitems.get(j).toString();
          myshow = LanguageTools.tr(inRequest,mymatrixvalues.get(i).toString()) +
                   "-" +
                   LanguageTools.tr(inRequest,matrixitems.get(j).toString());
          mymitems.add(myname);
          mymaps.put(myname,myshow);
       }
      }
      ArrayList mytitem = CommonTools.stringToArray(myitem,
          MatrixTools.NAME_SPLITTER);
      if ( ( mytitem != null )  &&
           ( mytitem.size() > 1 ) ) {
        mymatrixvalue = mytitem.get(0).toString();
        myitem = mytitem.get(1).toString();
      }
      mytitem = CommonTools.stringToArray(myitem2,
          MatrixTools.NAME_SPLITTER);
      if ( ( mytitem != null )  &&
           ( mytitem.size() > 1 ) ) {
        mymatrixvalue2 = mytitem.get(0).toString();
        myitem2 = mytitem.get(1).toString();
      }
    }

    if ( "ok".equals(mybutton) &&
         ( myitem != null ) &&
         ( myitem2 != null ) ) {
      Hashtable mycount[] =
          getCrossCount(inRequest,mystrucReader,myset,
          myitem,myitem2,myvalue,myvalue2,mywhere,mysetwhere,
          mymatrixvalue,mymatrixvalue2 );
      if ( mycount != null ) {
        outCrossCountResult(inRequest,out,myset,myitem,myitem2,mycount,
                            mymatrixvalue,mymatrixvalue2);
        return mysetwhere;
      }
    }

    out.println("<TABLE  border=1 class=bar width=100%>");
    out.println("<TR class=tableheader><TH colspan=2 align=center>" +
                LanguageTools.tr(inRequest,"cross_count") + "</TH></TR>");
    out.println("<TR class=tabledata><TD align=right>" +
                LanguageTools.tr(inRequest,"item") + "</TD>");
    out.println("<TD align=left>");
    OutputController.outRadioInput(inRequest,out,"item",mygetitem,
                                   myitems,  null,false,false);
    if ( ismatrix ) {
      out.println("<BR>");
      OutputController.outRadioInput(inRequest,out,"item",mygetitem,
                                     mymitems,  mymaps,false,false);
    }
    out.println("</TD></TR>");
    out.println("<TR class=tabledata><TD align=right>" +
                LanguageTools.tr(inRequest,"value") + "</TD>");
    out.println("<TD align=left>");
    OutputController.outTextInput(inRequest,out,"mycountvalue",myvalue);
    out.println("<BR><FONT class=info>" +
                LanguageTools.tr(inRequest,"count_info") + "</FONT>");
    out.println("</TD></TR>");
    out.println("<TR class=tabledata><TD align=right>" +
                LanguageTools.tr(inRequest,"cross_item") + "</TD>");
    out.println("<TD align=left>");
    OutputController.outRadioInput(inRequest,out,"cross_item",mygetitem2,
                                   myitems,  null,false,false);
    if ( ismatrix ) {
      out.println("<BR>");
      OutputController.outRadioInput(inRequest,out,"cross_item",mygetitem2,
                                     mymitems, mymaps,false,false);
    }
    out.println("</TD></TR>");
    out.println("<TR class=tabledata><TD align=right>" +
                LanguageTools.tr(inRequest,"value") + "</TD>");
    out.println("<TD align=left>");
    OutputController.outTextInput(inRequest,out,"mycountvalue2",myvalue2);
    out.println("<BR><FONT class=info>" +
                LanguageTools.tr(inRequest,"count_info") + "</FONT>");
    out.println("</TD></TR>");
    out.println("<TR class=tabledata><TD align=right>" +
                LanguageTools.tr(inRequest,"image") + "</TD>");
    out.println("<TD align=left>");
    String myv = CommonTools.arraylistToString(
        CommonTools.arrayToList(inRequest.getParameterValues("input_image")),
        DataReader.LIST_SPLITTER);
    OutputController.outCheckInput(inRequest,out,"image",myv,
                                   CommonTools.stringToArray("piecross,histogramcross,histogramrate",","),
                                   null,false);
    out.println("<BR>" + LanguageTools.tr(inRequest,"show_value") +
                " : ");
    String mysh = ServletTools.getParameter(inRequest,"input_show_value");
    ArrayList myse =  CommonTools.stringToArray("yes,no",",");
    if ( !myse.contains(mysh)) mysh = "yes";
    OutputController.outRadioInput(inRequest,out,"show_value",mysh,
                                   myse, null,false,false);
    out.println("</TD></TR>");
    out.println("</TABLE><BR>");

    out.println("<BR><P align=center>");
    OutputController.outSaveResetBar(inRequest,out,"seeform","ok","cancel");
    out.println("</P>");

    out.println("\n\t<SCRIPT LANGUAGE='javascript'>\n");
    out.println("\t\tfunction see_ok()");
    out.println("\t\t{");
    out.println("\t\t\t if ( see_checkCheckNull() < 0 ) return;");
    out.println("\t\t\t var oldt = document.seeform.target;");
    out.println("\t\t\tdocument.seeform.mybutton.value=\"ok\";");
    out.println("\t\t\tdocument.seeform.target=\"_blank\";");
    out.println("\t\t\tdocument.seeform.submit();");
    out.println("\t\t\tdocument.seeform.mybutton.value= \"\";");
    out.println("\t\t\tdocument.seeform.target= oldt;");
    //这里其实不必提交，但是如果不提交，则mybutton的值似乎会被浏览器篡改！
    out.println("\t\t\tdocument.seeform.submit();");
    out.println("\t\t}");

    Hashtable mycheck = new Hashtable();
    int mysize = myitems.size();
    if ( ismatrix )
      mysize = mysize + mymitems.size();
    String myssize = CommonTools.intToString(mysize);
    mycheck.put("item", myssize);
    mycheck.put("cross_item", myssize);
    OutputController.outCheckSelectNull(inRequest,out,"seeform",
                                        mycheck,true);

    out.println("\t</SCRIPT>\n");

    return mysetwhere;

  }

  /**
   * 获得交叉计数的结果
   * @param inRequest 当前请求
   * @param mystrucReader 数据定义
   * @param myset 数据集名
   * @param myitem 要计数的字段名
   * @param myitem2 要计数的字段名2
   * @param myvalue 要计数的字段值，null表示系统自动计数所有值
   * @param myvalue2 要计数的字段值2，null表示系统自动计数所有值
   * @param mywhere 基础查询条件
   * @param mysetwhere 设置查询条件
   * @param mymatrixvalue 矩阵值名
   * @param mymatrixvalue2 矩阵值名2
   * @return 交叉计数结果
   */
  public static Hashtable[] getCrossCount(HttpServletRequest inRequest,
      DataStructureXmlParser mystrucReader,
      String myset,
      String myitem,
      String myitem2,
      String myvalue,
      String myvalue2,
      ArrayList mywhere,
      ArrayList mysetwhere,
      String mymatrixvalue,
      String mymatrixvalue2) {
    if ( myset == null ) return null;
    if ( myitem == null ) return null;
    if ( myitem2 == null ) return null;
    if ( mystrucReader == null ) return null;

    DataReader mydataReader =
        DataManagerTools.getDataReader( inRequest,mystrucReader,
        myset,null,mywhere, mysetwhere, 0,-1);
    if ( mydataReader == null ) return null;
    ArrayList mydata = mydataReader.getRecords();
    if ( mydata == null ) return null;

    DataReader mymatrixdataReader = null;
    if ( ( mymatrixvalue != null ) ||
         ( mymatrixvalue2 != null) ) {
      mymatrixdataReader =
          DataManagerTools.getDataReader( inRequest,mystrucReader,
          myset + MatrixTools.MATRIX_FILE_SUFFIX,
          null,mywhere, mysetwhere,0,-1);
      if ( mymatrixdataReader == null ) return null;
    }

    ArrayList myvalues = CommonTools.stringToArray(myvalue,
        DataReader.LIST_SPLITTER);
    ArrayList myvalues2 = CommonTools.stringToArray(myvalue2,
        DataReader.LIST_SPLITTER);
    boolean  isall = (myvalues == null);
    boolean  isall2 = (myvalues2 == null);

    Hashtable mycount = new Hashtable();
    Hashtable myscount = new Hashtable();
    String mykey , my1, my2, my1c, my2c;
    mycount.put(STATISTIC_SPLITTER, mydata.size() + "");
    Hashtable myd,mykeyvalue = null, myw1,myw2 ;
    Object myv, mycc, myv1, myv2;
    int myc;
    ArrayList mykeys = mystrucReader.getKeys();
    for ( int i=0; i< mydata.size(); i++) {
      myd = (Hashtable)mydata.get(i);

      mykeyvalue = DataReaderGetor.getRecordKey(mykeys,myd);
      if ( mymatrixvalue != null ) {
        if ( mykeyvalue == null ) continue;
        myw1 = new Hashtable(mykeyvalue);
        myw1.put(MatrixTools.MATRIX_VALUE,
                 LanguageTools.atr(inRequest,mymatrixvalue));
        myd = mymatrixdataReader.getRecord(myw1);
      }
      if ( myd != null)
        myv1 = myd.get(myitem);
      else
        myv1 = null;

      myv1 = myd.get(myitem);
      myv2 = myd.get(myitem2);

      if ( !isall ) {
        if ( ( myv1 == null ) ||
             !myvalues.contains(myv1))
          myv1 = STATISTIC_OTHER;
      }
      if (  myv1 == null  )
        myv1 = STATISTIC_NULL;
      my1 = myv1.toString();
      my1c = my1 + STATISTIC_SPLITTER;
      mycc = mycount.get(my1c);
      if ( mycc == null ) {
        mycount.put(my1c,"1");
      } else {
        myc = CommonTools.stringToInt( mycc.toString());
        if ( myc == CommonTools.WRONG_INT )
          mycount.put(my1c, "1");
        else
          mycount.put(my1c,(++myc) + "");
      }

      if ( mymatrixvalue2 != null ) {
        myw2 = new Hashtable(mykeyvalue);
        myw2.put(MatrixTools.MATRIX_VALUE,
                 LanguageTools.atr(inRequest,mymatrixvalue2));
        myd = mymatrixdataReader.getRecord(myw2);
      }
      if ( myd != null)
        myv2 = myd.get(myitem2);
      else
        myv2 = null;

      if ( !isall2 ) {
        if ( ( myv2 == null ) ||
             !myvalues2.contains(myv2))
          myv2 = STATISTIC_OTHER;
      }
      if (  myv2 == null  )
        myv2 = STATISTIC_NULL;
      my2 = myv2.toString();
      my2c = STATISTIC_SPLITTER + my2;
      mycc = mycount.get(my2c);
      if ( mycc == null ) {
        mycount.put(my2c,"1");
      } else {
        myc = CommonTools.stringToInt( mycc.toString());
        if ( myc == CommonTools.WRONG_INT )
          mycount.put(my2c, "1");
        else
          mycount.put(my2c,(++myc) + "");
      }

      mykey = my1 + STATISTIC_SPLITTER + my2;
      mycc = myscount.get(mykey);
      if ( mycc == null ) {
        myscount.put(mykey,"1");
      } else {
        myc = CommonTools.stringToInt( mycc.toString());
        if ( myc == CommonTools.WRONG_INT )
          myscount.put(mykey, "1");
        else
          myscount.put(mykey,(++myc) + "");
      }
    }

    Hashtable myret[] = { myscount, mycount};
    return myret;
  }

  /**
   * 输出交叉计数结果
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param myset 数据集名
   * @param myitem 计数的字段名
   * @param myitem2 计数的字段名2
   * @param mycount 交叉计数结果
   * @param mymatrixvalue 矩阵值名
   * @param mymatrixvalue2 矩阵值名2
   */
  public static void outCrossCountResult(HttpServletRequest inRequest,
      PrintWriter out,
      String myset,
      String myitem,
      String myitem2,
      Hashtable mycount[],
      String mymatrixvalue,
      String mymatrixvalue2) {

    if ( myitem == null ) return ;
    if ( mycount == null ) return ;
    if ( mycount.length != 2 ) return ;

    Hashtable mydata = mycount[0];
    Hashtable mytotals = mycount[1];
    ArrayList mysort = CommonTools.sortIntHash(mydata);
    if ( mysort == null ) return;
    if ( mysort.size() < 1 ) return;

    ArrayList mykey1 =  new ArrayList();
    ArrayList mykey2 =  new ArrayList();
    String vv[];
    String kk;
    int myc;
    for ( int i=0; i< mysort.size(); i++) {
      kk = mysort.get(i).toString();
      vv = kk.split(STATISTIC_SPLITTER);
      if ( (vv == null) || vv.length < 2 ) continue;
      myc = CommonTools.stringToInt(mydata.get(kk).toString());
      if ( !"".equals(vv[0]) && !mykey1.contains(vv[0]) )
        mykey1.add(vv[0]);
      if ( !"".equals(vv[1]) && !mykey2.contains(vv[1]) )
        mykey2.add(vv[1]);
    }

    String myname1 = LanguageTools.tr(inRequest,myitem);
    String myname2 = LanguageTools.tr(inRequest,myitem2);
    if ( mymatrixvalue != null ) {
      myname1 = LanguageTools.tr(inRequest,mymatrixvalue) +
                "-" + myname1;
    }
    if ( mymatrixvalue2 != null ) {
      myname2 = LanguageTools.tr(inRequest,mymatrixvalue2) +
                "-" + myname2;
    }
    myname1 = "\"" + myname1 + "\"";
    myname2 = "\"" + myname2 + "\"";

    out.println("<P><TABLE  border=1 class=bar  align=center width=80%>");
    out.println("<TR class=tableheader >");
    out.println("<TH> " + myname2 +
                " &nbsp \\ &nbsp " + myname1 +
                "</TH>");
    for (int i=0; i< mykey1.size(); i++) {
      kk = mykey1.get(i).toString();
      out.println("<TH> " +
                  LanguageTools.tr(inRequest,
                  mykey1.get(i).toString())  +
                  "</TH>");
    }
    out.println("<TH class=tabletotal> " +
                LanguageTools.tr(inRequest,"total_count")  +
                "</TH>");
    out.println("<TH class=tabletotal> " +
                LanguageTools.tr(inRequest,"rate")  +
                "</TH></TR>");
    String myk1, myk2, mykey;
    int mytotal = 0;
    if ( mytotals.get(STATISTIC_SPLITTER) != null )
      mytotal =
      CommonTools.stringToInt(mytotals.get(STATISTIC_SPLITTER).toString());
    for (int i=0; i< mykey2.size(); i++) {
      myk2 =  mykey2.get(i).toString();
      outCrossLine(inRequest,out,mydata, mytotals,mykey1,
                   myk2, mytotal);
    }
    outCrossLine(inRequest,out,mydata,mytotals, mykey1,
                 STATISTIC_TOTAL, mytotal);
    outCrossLine(inRequest,out,mydata,mytotals, mykey1,
                 "",mytotal);
    out.println("</TABLE><BR>");

    String myimage[] = inRequest.getParameterValues("input_image");
    if ( myimage != null ) {
      String myname = myname2 + " \\ "  + myname1;
      String mynull = LanguageTools.tr(inRequest,STATISTIC_NULL);
      String myother = LanguageTools.tr(inRequest,StatisticTools.STATISTIC_OTHER);
      String mytitle = LanguageTools.tr(inRequest,"cross_count") + " - " +
                       LanguageTools.tr(inRequest,myset) +
                       " - " + myname2 + " \\ "  + myname1;
      Hashtable myinfo = new Hashtable();
      myinfo.put("item1",myname1);
      myinfo.put("item2",myname2);
      myinfo.put("item",myname);
      myinfo.put("count_show_limit",
                 LanguageTools.tr(inRequest,"count_show_limit"));
      myinfo.put(StatisticTools.STATISTIC_OTHER,myother);
      myinfo.put(STATISTIC_NULL,mynull);
      myinfo.put("xname", LanguageTools.tr(inRequest,"value"));
      myinfo.put("yname", LanguageTools.tr(inRequest,"count"));
      String myfile,myret;
      boolean myshow =
          "yes".equals(ServletTools.getParameter(inRequest,"input_show_value"));
      for (int i=0; i< myimage.length; i++) {
        myfile = getImageFile(inRequest,myset,"crosscount",myimage[i]);
        if (myfile == null) continue;
        myinfo.put("title", mytitle + " - " +
                   LanguageTools.tr(inRequest,myimage[i]));
        myret = ImageTools.writeCrosscountImage(myfile,myinfo,
            mydata,mytotal,mysort,myimage[i],myshow);
        if ( Constants.SUCCESSFUL.equals(myret)) {
          String mylink = DataManager.TMP_FILE_PREFIX +
                          CommonTools.getFileName(myfile);
          out.println("<P align=center>" +
                      "<IMG border=0 src=\"" +
                      mylink + "\"></P>");
        }
      }
    }
  }

  /**
   * 输出交叉计数的一行
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param mydata 交叉计数结果
   * @param mytotals 交叉计数各项合计
   * @param mykey1 字段1的值列表（横向字段）
   * @param myk2 字段2的当前值（当前纵向字段）
   * @param mytotal 总数
   */
  public static void outCrossLine(HttpServletRequest inRequest,
                                  PrintWriter out,
                                  Hashtable mydata,
                                  Hashtable mytotals,
                                  ArrayList mykey1,
                                  String myk2,
                                  int  mytotal) {
    if ( mydata == null ) return ;
    if ( mytotals == null ) return ;
    if ( mytotal < 1 ) return ;
    if ( myk2 == null ) return ;
    String mynull = LanguageTools.tr(inRequest,STATISTIC_NULL);
    String myother = LanguageTools.tr(inRequest,StatisticTools.STATISTIC_OTHER);
    out.println("<TR class=tabledata align=right >");
    String myname = LanguageTools.tr(inRequest,myk2) ;
    Hashtable mycc = mydata;
    String mycolor = "tableheader";
    if ( STATISTIC_NULL.equals(myk2))
      myname = mynull;
    else if ( STATISTIC_TOTAL.equals(myk2)) {
      myname = LanguageTools.tr(inRequest,"total_count");
      mycc = mytotals;
      mycolor = "tabletotal";
    } else if ( "".equals(myk2)) {
      myname = LanguageTools.tr(inRequest,"rate") ;
      mycc = mytotals;
      mycolor = "tabletotal";
    }
    out.println("<TH class=" + mycolor + " align=center> " +
                myname + "</TH>");
    String myk1, mykey, myvv;

    for (int j=0; j< mykey1.size(); j++) {
      myk1 =  mykey1.get(j).toString();
      if ( STATISTIC_TOTAL.equals(myk2)  )
        mykey = myk1 + STATISTIC_SPLITTER;
      else
        mykey = myk1 + STATISTIC_SPLITTER + myk2;
      myvv = "0";
      if ( mycc.get(mykey) != null )
        myvv = mycc.get(mykey).toString();
      if ( "".equals(myk2) )
        out.println("<TD class=tabletotal>" +
                    CommonTools.getPercent(myvv,mytotal,2) +
                    " % </TD>");
      else if ( STATISTIC_TOTAL.equals(myk2) )
        out.println("<TD class=tabletotal>" +
                    myvv + "</TD>");
      else
        out.println("<TD>" + myvv + "</TD>");
    }
    if ( "".equals(myk2) )
      out.println("<TD class=tabletotal>100%</TD><TD> &nbsp </TD>");
    else {
      if ( STATISTIC_TOTAL.equals(myk2)  )
        mykey = STATISTIC_SPLITTER;
      else
        mykey = STATISTIC_SPLITTER + myk2;
      String myv = "0";
      if ( mytotals.get(mykey) != null )
        myv = mytotals.get(mykey).toString();
      out.println("<TD class=tabletotal>" + myv + "</TD>");
      out.println("<TD class=tabletotal>" +
                  CommonTools.getPercent(myv,mytotal,2) +
                  " % </TD>");
    }
    out.println("</TR>");

  }

  /**
   * 输出趋势计算的页面
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param mystrucReader 数据定义
   * @param myset 数据集名
   * @param mybutton 当前按钮
   * @param mymaindt 主数据的时间日期字段
   * @param mymaindn 主数据的数值字段
   * @param mymatrixdt 矩阵数据的日期字段
   * @param mymatrixdn 矩阵数据的数值字段
   * @param mywhere 基础查询条件
   * @param mysetwhere 设置查询条件
   * @param ismatrix 是否是矩阵表
   * @return 设置查询条件
   */
  public static ArrayList  outTrendPage(HttpServletRequest inRequest,
                                        PrintWriter out,
                                        DataStructureXmlParser mystrucReader,
                                        String myset,
                                        String mybutton,
                                        ArrayList mymaindt,
                                        ArrayList mymaindn,
                                        ArrayList mymatrixdt,
                                        ArrayList mymatrixdn,
                                        ArrayList mywhere,
                                        ArrayList mysetwhere,
                                        boolean   ismatrix) {

    String mygettime = ServletTools.getParameter(inRequest,"input_time");
    String mytime = mygettime;
    String myvalues[] = inRequest.getParameterValues("input_value");

    Hashtable mymaps = null;
    ArrayList mymtitems = new ArrayList();
    ArrayList mymnitems = new ArrayList();
    String mymatrixvalue = null;
    if ( ismatrix ) {
      ArrayList matrixitems = mystrucReader.getVaildMatrixItemNames();
      if ( matrixitems == null ) return null;
      ArrayList mymatrixvalues = mystrucReader.getMatrixValues();
      if ( mymatrixvalues == null ) return null;
      mymaps = new Hashtable();
      String myname,myvalue, myshow;
      for ( int i =0 ; i< mymatrixvalues.size(); i++) {
        for ( int j =0 ; j< matrixitems.size(); j++) {
          myname = matrixitems.get(j).toString();
          myvalue = mymatrixvalues.get(i).toString() +
                     MatrixTools.NAME_SPLITTER +
                     myname;
          myshow = LanguageTools.tr(inRequest,mymatrixvalues.get(i).toString()) +
                   "-" +
                   LanguageTools.tr(inRequest,matrixitems.get(j).toString());
          if ( mymatrixdt.contains( myname) )
            mymtitems.add(myvalue);
          if ( mymatrixdn.contains( myname) )
            mymnitems.add(myvalue);
          mymaps.put(myvalue,myshow);
        }
      }
      ArrayList mytitem = CommonTools.stringToArray(mytime,
          MatrixTools.NAME_SPLITTER);
      if ( ( mytitem != null )  &&
           ( mytitem.size() > 1 ) ) {
        mymatrixvalue = mytitem.get(0).toString();
        mytime = mytitem.get(1).toString();
      }
    }

    String mystart = ServletTools.getParameter(inRequest,"input_start_time");
    String myend = ServletTools.getParameter(inRequest,"input_end_time");
    if ( "ok".equals(mybutton) &&
         ( mygettime != null ) &&
         ( myvalues != null ) &&
         ( myvalues.length > 0 ) ) {
      mysetwhere = outTrendResult(inRequest,out,myset,mygettime,
                     CommonTools.arrayToList(myvalues),
                     mystrucReader,mywhere,mysetwhere,
                     mystart,myend);
      return mysetwhere;
    }

    out.println("<TABLE  border=1 class=bar width=100%>");
    out.println("<TR class=tableheader><TH colspan=2 align=center>" +
                LanguageTools.tr(inRequest,"trend") + "</TH></TR>");
    out.println("<TR class=tabledata><TD align=right>" +
                LanguageTools.tr(inRequest,"time") + "</TD>");
    out.println("<TD align=left>");
    if (mymaindt.size() > 0 )
      OutputController.outRadioInput(inRequest,out,"time",mygettime,
                                     mymaindt,  null,false,false);
    if (mymtitems.size() > 0 ) {
      out.println("<BR>");
      OutputController.outRadioInput(inRequest,out,"time",mygettime,
                                     mymtitems, mymaps,false,false);
    }
    out.println("</TD></TR>");
    out.println("<TR class=tabledata><TD align=right>" +
                LanguageTools.tr(inRequest,"value") + "</TD>");
    out.println("<TD align=left>");
    String myv = CommonTools.arraylistToString(
        CommonTools.arrayToList(myvalues),
        DataReader.LIST_SPLITTER);
    if (mymaindn.size() > 0 )
      OutputController.outCheckInput(inRequest,out,"value",myv,
                                     mymaindn,  null,false);
    if (mymnitems.size() > 0 ) {
      out.println("<BR>");
      OutputController.outCheckInput(inRequest,out,"value",myv,
                                     mymnitems, mymaps,false);
    }
    out.println("</TD></TR>");
    out.println("<TR class=tabledata><TD align=right>" +
                LanguageTools.tr(inRequest,"start_time") + "</TD>");
    out.println("<TD align=left>");
    OutputController.outDateTimeInput(inRequest,out,"start_time",
                                      mystart,  null, true, -1,false );
    out.println("<BR>" + LanguageTools.tr(inRequest,"start_time_info"));
    out.println("</TD></TR>");
    out.println("<TR class=tabledata><TD align=right>" +
                LanguageTools.tr(inRequest,"end_time") + "</TD>");
    out.println("<TD align=left>");
    OutputController.outDateTimeInput(inRequest,out,"end_time",
                                      myend,  null, true,-1, false );
    out.println("<BR>" + LanguageTools.tr(inRequest,"end_time_info"));
    out.println("</TD></TR>");
    out.println("</TABLE><BR>");

    out.println("<BR><P align=center>");
    OutputController.outSaveResetBar(inRequest,out,"seeform","ok","cancel");
    out.println("</P>");

    out.println("\n\t<SCRIPT LANGUAGE='javascript'>\n");
    out.println("\t\tfunction see_ok()");
    out.println("\t\t{");
    out.println("\t\t\t if ( see_checkCheckNull() < 0 ) return;");
    out.println("\t\t\t var oldt = document.seeform.target;");
    out.println("\t\t\tdocument.seeform.mybutton.value=\"ok\";");
    out.println("\t\t\tdocument.seeform.target=\"_blank\";");
    out.println("\t\t\tdocument.seeform.submit();");
    out.println("\t\t\tdocument.seeform.mybutton.value= \"\";");
    out.println("\t\t\tdocument.seeform.target= oldt;");
    out.println("\t\t\tdocument.seeform.submit();");
    out.println("\t\t}");

    Hashtable mycheck = new Hashtable();
    int mysize = mymaindt.size();
    if ( ismatrix )
      mysize = mysize + mymtitems.size();
    mycheck.put("time", CommonTools.intToString(mysize));
    mysize = mymaindn.size();
    if ( ismatrix )
      mysize = mysize + mymnitems.size();
    mycheck.put("value", CommonTools.intToString(mysize));
    OutputController.outCheckSelectNull(inRequest,out,"seeform",mycheck,true);

    out.println("\t</SCRIPT>\n");

    return mysetwhere;

  }

  /**
   * 输出趋势结果
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param myset 数据集名
   * @param mytime 时间/日期字段名
   * @param myvalues 数值字段名列表
   * @param mystrucReader 数据结构
   * @param mywhere 基础查询条件
   * @param mysetwhere 设置查询条件
   * @param mystart 开始时间
   * @param myend 结束时间
   * @return 设置查询条件
   */
  public static ArrayList outTrendResult(HttpServletRequest inRequest,
                                    PrintWriter out,
                                    String myset,
                                    String mytime,
                                    ArrayList myvalues,
                                    DataStructureXmlParser mystrucReader,
                                    ArrayList mywhere,
                                    ArrayList mysetwhere,
                                    String mystart,
                                    String myend) {

    if ( mytime == null ) return mysetwhere;
    if ( ( myvalues == null ) ||
         ( myvalues.size() < 1) )
      return mysetwhere;

    ArrayList mysetw = new ArrayList();
    String myrtime = mytime;
    if ( myrtime.indexOf(MatrixTools.NAME_SPLITTER) > 0  ) {
      ArrayList mymitem = CommonTools.stringToArray(mytime,
          MatrixTools.NAME_SPLITTER);
      if ( ( mymitem != null )  &&
           ( mymitem.size() > 1 ) ) {
        myrtime = mymitem.get(1).toString();
      }
    }
    String mymatrixtime = null, myttime = mytime;
    String mytname =  "\"" + LanguageTools.tr(inRequest,mytime)+ "\"";
    ArrayList mymitem;
    Hashtable mytypes = new Hashtable(),
    mydtypes = mystrucReader.getItemTypes();
    if ( mydtypes.get(mytime) != null)
      mytypes.put(mytime,mydtypes.get(mytime));
    if (  mytime.indexOf(MatrixTools.NAME_SPLITTER) > 0  ) {
      mymitem = CommonTools.stringToArray(mytime,
          MatrixTools.NAME_SPLITTER);
      if ( ( mymitem != null )  &&
           ( mymitem.size() > 1 ) ) {
        mymatrixtime = mymitem.get(0).toString();
        myttime = mymitem.get(1).toString();
        mytname =  "\"" + LanguageTools.tr(inRequest,mymitem.get(0).toString()) +
                   "-" +  LanguageTools.tr(inRequest,mymitem.get(1).toString()) +
                   "\"";
        if ( mydtypes.get(myttime) != null)
          mytypes.put(mytime,mydtypes.get(myttime));
      }
    }

    DataReader mydataReader =
          DataManagerTools.getDataReader( inRequest,mystrucReader,
          myset,null,mywhere,mysetwhere, 0,-1);
    if ( mydataReader == null )
      return mysetwhere;

    ArrayList mydata = mydataReader.getRecords();
    if ( (mydata == null) ||
        (mydata.size() < 1) )
      return mysetwhere;

    boolean hasmatrix = false;
    String myvv;
    for ( int i=0; i< myvalues.size(); i++) {
      myvv = myvalues.get(i).toString();
      if ( myvv.indexOf(MatrixTools.NAME_SPLITTER) > -1 ) {
        hasmatrix = true;
        break;
      }
    }
    DataReader mymatrixdataReader = null;
    if ( hasmatrix ) {
      mymatrixdataReader =
          DataManagerTools.getDataReader( inRequest,mystrucReader,
          myset + MatrixTools.MATRIX_FILE_SUFFIX,
          null,mywhere, mysetwhere,0,-1);
      if ( mymatrixdataReader == null ) return null;
    }

    String myv;
    ArrayList mymvalues = new ArrayList();
    ArrayList mytvalues = new ArrayList();
    ArrayList mynames = new ArrayList();
    for ( int i=0; i< myvalues.size(); i++) {
      myv = myvalues.get(i).toString();
      if ( hasmatrix &&
          ( myv.indexOf(MatrixTools.NAME_SPLITTER) > 0 ) ) {
        mymitem = CommonTools.stringToArray(myv,
            MatrixTools.NAME_SPLITTER);
        if ( ( mymitem != null )  &&
             ( mymitem.size() > 1 ) ) {
          mymvalues.add(mymitem.get(0).toString());
          mytvalues.add(mymitem.get(1).toString());
          mynames.add("\"" + LanguageTools.tr(inRequest,mymitem.get(0).toString()) +
                      "-" + LanguageTools.tr(inRequest,mymitem.get(1).toString()) +
                      "\"");
          if ( mydtypes.get(mymitem.get(1).toString()) != null)
            mytypes.put(myv,mydtypes.get(mymitem.get(1).toString()));
        }
      } else {
        mymvalues.add(MatrixTools.NAME_SPLITTER);
        mytvalues.add(myv);
        mynames.add("\"" + LanguageTools.tr(inRequest,myv) + "\"");
        if ( mydtypes.get(myv) != null)
          mytypes.put(myv,mydtypes.get(myv));
      }
    }

    ArrayList mykeys = mystrucReader.getKeys();
    ArrayList myout = new ArrayList();
    Hashtable myd,mykeyvalue,mynd,mymd, mykk;
    String mytv,myov,myttype = mytypes.get(mytime).toString();
    String myenc = ServletTools.getSessionEncoding(inRequest);
    for ( int i=0; i< mydata.size(); i++) {
      mynd = new Hashtable();
      myd = (Hashtable)mydata.get(i);
      mykeyvalue = DataReaderGetor.getRecordKey(mykeys,myd);
      if ( mykeyvalue == null ) continue;
      if ( hasmatrix &&
           (  mymatrixtime != null ) ) {
        mykk = new Hashtable(mykeyvalue);
        mykk.put(MatrixTools.MATRIX_VALUE,
                 LanguageTools.atr(inRequest,mymatrixtime));
        mymd = mymatrixdataReader.getRecord(mykk);
        if ( mymd.get(myttime) != null )
          mynd.put(mytime,mymd.get(myttime));
      } else {
        if ( myd.get(mytime) != null )
          mynd.put(mytime,myd.get(mytime));
      }
      if ( mynd.get(mytime) == null ) continue;
      if ( ( mystart != null ) &&
           !"".equals(mystart) &&
           !CommonTools.compareData(mynd.get(mytime).toString(),
                                   mystart,myttype,myenc) )
        continue;
      if ( ( myend != null ) &&
           !"".equals(myend) &&
           !CommonTools.compareData(myend,mynd.get(mytime).toString(),
           myttype,myenc) )
        continue;
      for ( int j = 0; j < mymvalues.size(); j++) {
        myv = mymvalues.get(j).toString();
        mytv = mytvalues.get(j).toString();
        myov = myvalues.get(j).toString();
        if ( MatrixTools.NAME_SPLITTER.equals(myv)) {
          if ( myd.get(mytv) != null )
            mynd.put(myov,myd.get(mytv));
        } else {
          mykk = new Hashtable(mykeyvalue);
          mykk.put(MatrixTools.MATRIX_VALUE,
                   LanguageTools.atr(inRequest,myv));
          mymd = mymatrixdataReader.getRecord(mykk);
          if ( mymd.get(mytv) != null )
            mynd.put(myov,mymd.get(mytv));
        }
      }
      myout.add(mynd);
    }
    if ( myout.size() < 1 )
      return mysetwhere;

    myout = CommonTools.sortData(myout,mytime,true,
                                 mytypes.get(mytime).toString(),
                                 myenc);

    String myss = "";
    if ( ( mystart != null ) &&
         !"".equals(mystart) )
      myss = mystart + "   <   " +  mytname ;
    if ( ( myend != null ) &&
         !"".equals(myend) ) {
      if ( "".equals(myss))
        myss = mytname  + "   <   " + myend;
      else
        myss = myss  + "   <   " + myend;
    }
    if ( !"".equals(myss) )
      out.println("<P class=info>" + myss + "</P>");

    out.println("<P><TABLE  border=1 class=bar  align=center width=80%>");
    out.println("<TR class=tableheader >");
    out.println("<TH> " + mytname + "</TH>");
    for (int i=0; i< mynames.size(); i++) {
      out.println("<TH> " +
      LanguageTools.tr(inRequest, mynames.get(i).toString()) +
      "</TH>");
    }
    String myk;
    for ( int i=0; i< myout.size(); i++) {
      out.println("<TR class=tabledata>");
      myd = (Hashtable)myout.get(i);
      if ( myd.get(mytime) == null)
        myv = "-";
      else
        myv = myd.get(mytime).toString();
      out.println("<TD align=center>" + myv + "</TD>");
      for (int j=0; j< myvalues.size(); j++) {
        myk = myvalues.get(j).toString();
        if ( myd.get(myk) == null)
          myv = "-";
        else
          myv = myd.get(myk).toString();
        out.println("<TD align=center>" + myv + "</TD>");
      }
      out.println("</TR>");
    }
    out.println("</TABLE><BR>");

    String mytitle = LanguageTools.tr(inRequest,"trend") + " - " +
                     LanguageTools.tr(inRequest,myset) ;
    Hashtable myinfo = new Hashtable();
    myinfo.put("xname", mytname);
    myinfo.put("yname", LanguageTools.tr(inRequest,"value") );
    myinfo.put("time", mytname);
    myinfo.put("title", mytitle);
    for (int i=0; i< mynames.size(); i++) {
      myinfo.put("name" + i,
      LanguageTools.tr(inRequest,mynames.get(i).toString()) );
    }
    String myfile,myret;
    myfile = getImageFile(inRequest,myset,"trend",mytime);
    if (myfile == null) return mysetwhere;
    myret = ImageTools.writeTrendImage(myfile,myinfo, myout,
                                       mytime, myvalues,
                                       mytypes,
                                       ServletTools.getAppDefaultEncoding(inRequest));
    if ( Constants.SUCCESSFUL.equals(myret)) {
      String mylink = DataManager.TMP_FILE_PREFIX +
                      CommonTools.getFileName(myfile);
      out.println("<P align=center>" +
                  "<IMG border=0 src=\"" +
                  mylink + "\"></P>");
    }

    return mysetwhere;

  }

  /**
   * 获得图形文件的绝对路径。
   * @param myRequest 当前请求
   * @param myset 数据集名
   * @param mystype 统计类型
   * @param myitype 图片类型
   * @return 图形文件名（绝对路径）
   */
  public static String getImageFile(HttpServletRequest myRequest,
                                    String myset,
                                    String mystype,
                                    String myitype) {
    try {
      if ( myset == null ) return null;
      if ( mystype == null ) return null;
      if ( myitype == null ) return null;
      return DataManagerTools.getTmpFile(myRequest,
          myset + "-" + mystype + "-" + myitype,"jpg") ;
    }
    catch (Exception ex) {
      return null;
    }
  }

  /**
   * 显示属于超级管理员和当前用户的所有统计设置列表
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param mystrucReader 统计设置的结构
   * @param mytitle 统计设置标题
   * @param mytype 统计的类型
   * @return 当前统计设置的参数
   */
  public static Hashtable showSetupList(HttpServletRequest inRequest,
                                        PrintWriter out,
                                        DataStructureXmlParser mystrucReader,
                                        String mytitle,
                                        String mytype) {

    ArrayList itemnames = mystrucReader.getEditItemNames();
    ArrayList notnulls = mystrucReader.getNotNullNames();
    ArrayList keys = mystrucReader.getKeys();

    ArrayList mywhere = new ArrayList();
    Hashtable vv = new Hashtable();
    vv.put("data",ServletTools.getParameter(inRequest,"myset"));
    String mysuper =
        LanguageTools.atr(inRequest,DataManager.SUPER_SEER);
    vv.put("user",mysuper);
    mywhere.add(vv);
    DataReader mydataReader =
        DataManagerTools.getDataReader( inRequest,mystrucReader,
        DataManager.STATISTIC_SETUP,null,mywhere, 0,-1);
    ArrayList mydata =
        mydataReader.getValidRecords(itemnames, notnulls, keys);
    boolean isuper = AclTools.isSuperseer(inRequest);
    String myuser = ServletTools.getSessionUser(inRequest);
    if ( !isuper ) {
      vv.put("user",myuser);
      mywhere.add(vv);
      mydataReader =
          DataManagerTools.getDataReader( inRequest,mystrucReader,
          DataManager.STATISTIC_SETUP,null,mywhere, 0,-1);
      ArrayList myudata =
          mydataReader.getValidRecords(itemnames, notnulls, keys);

      if ( ( mydata != null) &&
           ( myudata != null ) ) {
        mydata.addAll(myudata);
      }
    }
    String myclick = "";
    boolean isupersetup = true;
    Hashtable mynow = null;
    if ( (mydata != null ) &&
         mydata.size() > 0 ) {
      out.println("\t <TABLE class=data cellPadding=2 width=100%>");
      ArrayList myshow = new ArrayList();
      StringBuffer mystr = new StringBuffer();
      for (int i=0; i< mydata.size();i++) {
        vv = (Hashtable)mydata.get(i);
        if (vv.get("title") == null) continue;
        if (vv.get("type") == null) continue;
        if ( vv.get("title").equals(mytitle) )
          mynow = vv;
        mystr = new StringBuffer("<A onClick='see_set(\"");
        mystr.append(vv.get("title"));
        mystr.append("\",\"");
        mystr.append(vv.get("type"));
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
                LanguageTools.tr(inRequest,"set_statistic_setup") + "</P>");
    out.println("<TABLE class=whitebar width=100% border=0><TR>");
    out.println("<TD align=left>" + LanguageTools.tr(inRequest,"title") +
                ":<INPUT name=mytitle>");
    OutputController.outButton(inRequest,out,"add");
    out.println("</TD>");
    if ( mytitle != null ) {
      out.println("<TD align=right>" +
                  LanguageTools.tr(inRequest,"current_statistic_setup") +
                  ":" + mytitle );
      if ( isuper || !isupersetup) {
        OutputController.outButton(inRequest,out,"modify");
        OutputController.outButton(inRequest,out,"remove");
      }
      out.println("</TD>");
    }
    out.println("</TR></TABLE>");
    out.print("<INPUT type=hidden name=mystatisticsetup");
    if ( mytitle != null )
      out.print(" value=\"" + mytitle + "\" ");
    out.println(">");

    out.println("\t<SCRIPT LANGUAGE='javascript'>\n");

    out.println("\tfunction see_set(mytitle,mytype) {");
    out.println("\t\t document.seeform.mystatisticsetup.value = mytitle;");
    out.println("\t\t document.seeform.mystattype.value = mytype;");
    out.println("\t\t\t if ( mytype.indexOf(\"_matrix\") > 0 )");
    out.println("\t\t\t\t document.seeform.mymatrix.value=\"yes\";");
    out.println("\t\t\t else");
    out.println("\t\t\t\t document.seeform.mymatrix.value=\"\";");
    out.println("\t\t document.seeform.mybutton.value = \"set\";");
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
    out.println("\t\t if ( see_checkCheckNull() < 0 ) return;");
    out.println("\t\t document.seeform.mybutton.value = \"modify\";");
    out.println("\t\t document.seeform.submit();");
    out.println("\t }");

    out.println("\tfunction see_add() {");
    out.println("\t\t if ( document.seeform.mytitle.value.length < 1 ) {");
    out.println("\t\t\t alert(\"" + LanguageTools.tr(inRequest,"title") +
                LanguageTools.tr(inRequest,"should_not_null")  + "\");");
    out.println("\t\t\t return;");
    out.println("\t\t }");
    out.println("\t\t if ( document.seeform.mytitle.value.indexOf(\"\\\"\") > -1 ) {");
    out.println("\t\t\t alert(\"" + LanguageTools.tr(inRequest,"title") +
                LanguageTools.tr(inRequest,"can_not_include")  +
               " : " + "\\\"\");");
    out.println("\t\t\t return;");
    out.println("\t\t }");
    out.println("\t\t if ( see_checkCheckNull() < 0 ) return;");
    out.println("\t\t document.seeform.mystatisticsetup.value = "+
                "document.seeform.mytitle.value;");
    out.println("\t\t document.seeform.mybutton.value = \"add\";");
    out.println("\t\t document.seeform.submit();");
    out.println("\t}");
    out.println("\t</SCRIPT>");

    return mynow;

  }

  /**
   * 增/删/改统计设置
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param mystrucReader 统计设置的结构
   * @param myset 当前数据
   * @param mytitle 查询设置标题
   * @param mybutton 当前处理按钮
   * @param mytype 统计类型
   * @return 处理后的统计设置标题
   */
  public static String writeShowSetup(HttpServletRequest inRequest,
                                      PrintWriter out,
                                      DataStructureXmlParser mystrucReader,
                                      String myset,
                                      String mytitle,
                                      String mybutton,
                                      String mytype) {

    if ( mystrucReader == null )
      return "invaild_data";

    ArrayList itemnames = mystrucReader.getEditItemNames();
    ArrayList notnulls = mystrucReader.getNotNullNames();
    ArrayList keys = mystrucReader.getKeys();

    Hashtable vkey = new Hashtable();
    String retw = "";
    DataReader mydataReader =
        DataManagerTools.getDataReader( inRequest,mystrucReader,
        DataManager.STATISTIC_SETUP, null, null, 0,-1);
    String myuser = ServletTools.getSessionUser(inRequest);
    String mywhere = ServletTools.getParameter(inRequest,"mysetwhere");
    String mymatrixvalue = ServletTools.getParameter(inRequest,"input_matrix_value");
    String mymatrixvalue2 = ServletTools.getParameter(inRequest,"input_cross_matrix_value");
    vkey.put("title",mytitle);
    vkey.put("data",myset);
    vkey.put("user",myuser);
    Hashtable myold = null;
    Hashtable vv = new Hashtable();
    if ( "remove".equals(mybutton) ) {
      if (mydataReader.removeValidRecord(itemnames,
          notnulls, keys, vkey,null) != null )
        myold = vkey;
      mytitle = null;
    } else {
      vv.put("title",mytitle);
      vv.put("data",myset);
      vv.put("user",myuser);
      vv.put("type",mytype);
      if ( mywhere != null )
        vv.put("condition", mywhere);
      if ( mymatrixvalue != null )
        vv.put("matrix_value", mymatrixvalue);
      if ( mymatrixvalue2 != null )
        vv.put("cross_matrix_value", mymatrixvalue2);

      if ( mytype.startsWith("cross_count")) {

        String myitem = ServletTools.getParameter(inRequest,"input_item");
        String myvalue = ServletTools.getParameter(inRequest,"input_mycountvalue");
        String mycitem = ServletTools.getParameter(inRequest,"input_cross_item");
        String mycvalue = ServletTools.getParameter(inRequest,"input_mycountvalue2");
        String myimage = CommonTools.arraylistToString(
            CommonTools.arrayToList(inRequest.getParameterValues("input_image")),
            DataReader.LIST_SPLITTER);
        String myshow = ServletTools.getParameter(inRequest,"input_show_value");
        if ( myitem != null )
          vv.put("item",myitem);
        if ( myvalue != null )
          vv.put("value",myvalue);
        if ( mycitem != null )
          vv.put("cross_item",mycitem);
        if ( mycvalue != null )
          vv.put("cross_value",mycvalue);
        if ( myimage != null )
          vv.put("image",myimage);
        if ( myshow != null )
          vv.put("show_value",myshow);

      } else if ( mytype.startsWith("trend")) {

        String mytime = ServletTools.getParameter(inRequest,"input_time");
        String myvalue = CommonTools.arraylistToString(
            CommonTools.arrayToList(inRequest.getParameterValues("input_value")),
            DataReader.LIST_SPLITTER);
        String mystart = ServletTools.getParameter(inRequest,"input_start_time");
        String myend = ServletTools.getParameter(inRequest,"input_end_time");
        if ( mytime != null )
          vv.put("time",mytime);
        if ( myvalue != null )
          vv.put("value",myvalue);
        if ( mystart != null )
          vv.put("start_time",mystart);
        if ( myend != null )
          vv.put("end_time",myend);

      } else  {

        String myitem = ServletTools.getParameter(inRequest,"input_item");
        String myvalue = ServletTools.getParameter(inRequest,"input_mycountvalue");
        String myimage = CommonTools.arraylistToString(
            CommonTools.arrayToList(inRequest.getParameterValues("input_image")),
            DataReader.LIST_SPLITTER);
        if ( myitem != null )
          vv.put("item",myitem);
        if ( myvalue != null )
          vv.put("value",myvalue);
        if ( myimage != null )
          vv.put("image",myimage);

      }
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
          DataManager.STATISTIC_SETUP);
      DataXmlWriter myw = new DataXmlWriter();
      retw = myw.writeData(mydataReader.getRecords(),fdata,
                           ServletTools.getAppDefaultCharset(inRequest));
    }
    ArrayList myaudit =
        ServletTools.getAppDataAuditTypes(inRequest);
    if ( myaudit.contains(LanguageTools.atr(inRequest,mybutton))) {
      AuditTools.addAudit(inRequest,DataManager.STATISTIC_SETUP,
                          mybutton, vkey  , myold , vv , retw,null ,null);
    }

    return mytitle;
  }

  /**
   * 输出所选择的统计设置结果
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param mystrucReader 统计设置的结构
   * @param mytype 统计类型
   * @param mytsize 时间/日期字段的总个数
   * @param mynsize 数值字段的总个数
   * @param mysetup 当前处理按钮
   * @param ismatrix 当前处理按钮
   */
  public static void outSet(HttpServletRequest inRequest,
                            PrintWriter out,
                            DataStructureXmlParser mystrucReader,
                            String mytype,
                            int   mytsize,
                            int   mynsize,
                            Hashtable mysetup,
                            boolean  ismatrix) {

    if (  mysetup == null )  return ;
    if (  mytype == null )  return ;
    ArrayList myitems = mystrucReader.getEditItemNames();

    out.println("\n\t<SCRIPT LANGUAGE='javascript'>\n");

    if (  mytype.startsWith("cross_count") ) {

      if ( mysetup.get("item") == null )  return;
      if ( mysetup.get("cross_item") == null )  return;
      if ( myitems.size() < 2 ) {
        out.println("\t\t if ( document.seeform.input_item.value == \"" +
                    mysetup.get("item") + "\" )");
        out.println("\t\t document.seeform.input_item.checked = true ;");
        out.println("\t\t if ( document.seeform.input_cross_item.value == \"" +
                    mysetup.get("cross_item") + "\" )");
        out.println("\t\t document.seeform.input_cross_item.checked = true ;");
      } else {
        out.println("\t\t for ( i=0; i<" + myitems.size() +
                    "; i++ ) {");
        out.println("\t\t\t if ( document.seeform.input_item[i].value == \"" +
                    mysetup.get("item") + "\" )");
        out.println("\t\t\t\t document.seeform.input_item[i].checked = true ;");
        out.println("\t\t\t if ( document.seeform.input_cross_item[i].value == \"" +
                    mysetup.get("cross_item") + "\" )");
        out.println("\t\t\t\t document.seeform.input_cross_item[i].checked = true ;");
        out.println("\t\t }");
      }
      if ( mysetup.get("value") != null )
        out.println("\t\t document.seeform.input_mycountvalue.value = \"" +
                    mysetup.get("value") + "\" ;");
      if ( mysetup.get("cross_value") != null )
        out.println("\t\t document.seeform.input_mycountvalue2.value = \"" +
                    mysetup.get("cross_value") + "\" ;");
      if ( mysetup.get("image") != null ) {
        ArrayList myimg = CommonTools.stringToArray(mysetup.get("image").toString(),
            DataReader.LIST_SPLITTER);
        if ( ( myimg != null ) &&
             ( myimg.size() > 0 ) ) {
          out.println("\t\t\t for ( i=0; i<3; i++ ) {");
          out.println("\t\t\t\t document.seeform.input_image[i].checked = false ;");
          for ( int i = 0; i < myimg.size(); i++) {
            out.println("\t\t\t\t if ( document.seeform.input_image[i].value == \"" +
            myimg.get(i) + "\" )");
            out.println("\t\t\t\t document.seeform.input_image[i].checked = true ;");
          }
          out.println("\t\t }");
        }
      }
      if ( "yes".equals(mysetup.get("show_value")) )
        out.println("\t\t\t\t document.seeform.input_show_value[0].checked = true ;");
      else
        out.println("\t\t\t\t document.seeform.input_show_value[1].checked = true ;");
      if ( mysetup.get("condition") != null )
        out.println("\t\t\t\t document.seeform.mysetwhere.value = \"" +
                    mysetup.get("condition") + "\" ;");
      else
        out.println("\t\t\t\t document.seeform.mysetwhere.value = \"\" ;");

    } else if (  mytype.startsWith("trend") ) {

      if ( mysetup.get("time") == null )  return;
      if ( mysetup.get("value") == null )  return;
      ArrayList myvalues = CommonTools.stringToArray(mysetup.get("value").toString(),
          DataReader.LIST_SPLITTER);
      if ( ( myvalues == null ) ||
            ( myvalues.size() < 1))
        return;
      if ( mytsize < 2 ) {
        out.println("\t\t if ( document.seeform.input_time.value == \"" +
                    mysetup.get("time") + "\" )");
        out.println("\t\t document.seeform.input_time.checked = true ;");
      } else {
        out.println("\t\t for ( i=0; i<" + mytsize +
                    "; i++ ) {");
        out.println("\t\t\t if ( document.seeform.input_time[i].value == \"" +
                    mysetup.get("time") + "\" )");
        out.println("\t\t\t\t document.seeform.input_time[i].checked = true ;");
        out.println("\t\t }");
      }
      if ( mynsize < 2 ) {
        out.println("\t\t if ( document.seeform.input_value.value == \"" +
                    myvalues.get(0) + "\" )");
        out.println("\t\t document.seeform.input_value.checked = true ;");
      } else {
        out.println("\t\t\t for ( i=0; i< " +  mynsize + "; i++ ) {");
        out.println("\t\t\t\t document.seeform.input_value[i].checked = false ;");
        for ( int i = 0; i < myvalues.size(); i++) {
          out.println("\t\t\t\t if ( document.seeform.input_value[i].value == \"" +
          myvalues.get(i) + "\" )");
          out.println("\t\t\t\t document.seeform.input_value[i].checked = true ;");
        }
        out.println("\t\t }");
      }
      if ( mysetup.get("start_time") != null )
        out.println("\t\t document.seeform.input_start_time.value = \"" +
                    mysetup.get("start_time") + "\" ;");
      if ( mysetup.get("end_time") != null )
        out.println("\t\t document.seeform.input_end_time.value = \"" +
                    mysetup.get("end_time") + "\" ;");

    } else {

      if ( mysetup.get("item") == null )  return;
      if ( myitems.size() < 2 ) {
        out.println("\t\t if ( document.seeform.input_item.value == \"" +
                    mysetup.get("item") + "\" )");
        out.println("\t\t document.seeform.input_item.checked = true ;");
      } else {
        out.println("\t\t for ( i=0; i<" + myitems.size() +
                    "; i++ ) {");
        out.println("\t\t\t if ( document.seeform.input_item[i].value == \"" +
                    mysetup.get("item") + "\" )");
        out.println("\t\t\t\t document.seeform.input_item[i].checked = true ;");
        out.println("\t\t }");
      }
      if ( mysetup.get("value") != null )
        out.println("\t\t document.seeform.input_mycountvalue.value = \"" +
                    mysetup.get("value") + "\" ;");
      if ( mysetup.get("image") != null ) {
        ArrayList myimg = CommonTools.stringToArray(mysetup.get("image").toString(),
            DataReader.LIST_SPLITTER);
        if ( ( myimg != null ) &&
             ( myimg.size() > 0 ) ) {
          out.println("\t\t\t for ( i=0; i<2; i++ ) {");
          out.println("\t\t\t\t document.seeform.input_image[i].checked = false ;");
          for ( int i = 0; i < myimg.size(); i++) {
            out.println("\t\t\t\t if ( document.seeform.input_image[i].value == \"" +
            myimg.get(i) + "\" )");
            out.println("\t\t\t\t document.seeform.input_image[i].checked = true ;");
          }
          out.println("\t\t }");
        }
      }
    }
    out.println("\t\t see_ok();");
    out.println("\t</SCRIPT>\n");

  }

}