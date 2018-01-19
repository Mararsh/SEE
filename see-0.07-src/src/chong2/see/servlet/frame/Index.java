package chong2.see.servlet.frame;

import chong2.see.servlet.common.DataManager;
import chong2.see.servlet.common.HttpServletType1;
import chong2.see.utils.*;
import chong2.see.data.*;
import chong2.see.xml.DataXmlWriter;
import chong2.see.xml.DataStructureXmlParser;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * <p>Title: ������̹�����</p>
 * <p>Description: Ϊ����з��ṩ�������ݺ������ƽ̨</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: ���</p>
 *
 * <p>Ӧ�õ���ʼҳ��
 *
 * @author ����
 * @version 0.07
 */


public class Index
    extends HttpServletType1 {

  /**
   * ҳ�����ط���
   * @param inRequest ��ǰ����
   * @param inResponse ��ǰ��Ӧ
   * @throws IOException IO�쳣
   * @throws ServletException servlet�쳣
   */
  protected  void processRequest(HttpServletRequest inRequest,
                                 HttpServletResponse inResponse)
      throws IOException, ServletException {

    try{


      // ����xml��ȡ/д���ٶȡ�
      String  mytest = ServletTools.getParameter(inRequest,"act");
      if (   "test".equals(mytest )) {
        if ( !AclTools.isSuperseer(inRequest) ) {
          PageTools.outErrorPage(inRequest,inResponse,"no_permission",
                                 DataManager.SEE_SPEED_TESTING,
                                 null,"");
          return;
        } else {
          PageTools.outInfoPage(inRequest,inResponse,
                                DataManager.SEE_SPEED_TESTING,null,
                                "", "" );
          return;
        }
      }

      // ����ֻ��ȱʡ��ܷ���
      PageTools.forwardPage(inRequest,inResponse,
                            "/frame_default");

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * ��õ�ǰ���淽���е�ָ���ڵ�
   * @param inRequest ��ǰ����
   * @param myname ָ���ڵ���
   * @return ��ǰ���淽���е�ָ���ڵ�
   */
  public static ArrayList getThemeNodes(HttpServletRequest inRequest,
                                        String myname) {
    return getThemeNodes(inRequest,
                         ServletTools.getSessionInterfaceTheme(inRequest),
                         myname);
  }

  /**
   * ���ָ�����淽���е�ָ���ڵ�
   * @param inRequest ��ǰ����
   * @param mytheme ָ�����淽����
   * @param myname ָ���ڵ���
   * @return ָ�����淽���е�ָ���ڵ�
   */
  public static ArrayList getThemeNodes(HttpServletRequest inRequest,
                                        String mytheme,
                                        String myname) {
    try {
      Hashtable mywhere =  new Hashtable();
      mywhere.put("theme",mytheme);
      mywhere.put("name",myname);
      ArrayList myw = new ArrayList();
      myw.add(mywhere);
      DataReader myreader = DataManagerTools.getDataReader(inRequest,
          DataManager.INTERFACE_THEME,null,1,-1,myw) ;
      if ( myreader == null ) return null;
      return myreader.getRecords();
    }
    catch (Exception ex) {
      return null;
    }
  }

  /**
   * ��õ�ǰ���淽����һ��ָ���ڵ������
   * @param inRequest ��ǰ����
   * @param myname ָ���ڵ���
   * @return ָ���ڵ������
   */
  public static Hashtable getThemeNode(HttpServletRequest inRequest,
                                       String myname) {
    return getThemeNode(inRequest,
                        ServletTools.getSessionInterfaceTheme(inRequest),
                        myname);
  }

  /**
   * ���ָ�����淽����һ��ָ���ڵ������
   * @param inRequest ��ǰ����
   * @param mytheme ָ�����淽����
   * @param myname ָ���ڵ���
   * @return ָ���ڵ������
   */
  public static Hashtable getThemeNode(HttpServletRequest inRequest,
                                       String mytheme,
                                       String myname) {
    ArrayList myroots = getThemeNodes(inRequest,mytheme,myname);
    if ( myroots == null )  return null;
    if ( myroots.size() < 1 )  return null;
    return (Hashtable)(myroots.get(0));
  }

  /**
   * ��õ�ǰ���淽����һ��ָ���ڵ�������ӽڵ���
   * @param inRequest ��ǰ����
   * @param myname ָ���ڵ���
   * @return ��ǰ���淽����һ��ָ���ڵ�������ӽڵ���
   */
  public static ArrayList getThemeChild(HttpServletRequest inRequest,
                                        String myname) {
    return getThemeChild(inRequest,
                         ServletTools.getSessionInterfaceTheme(inRequest),
                         myname);
  }

  /**
   * ���ָ�����淽����һ��ָ���ڵ�������ӽڵ���
   * @param inRequest ��ǰ����
   * @param mytheme ָ�����淽����
   * @param myname ָ���ڵ���
   * @return ָ�����淽����һ��ָ���ڵ�������ӽڵ���
   */
  public static ArrayList getThemeChild(HttpServletRequest inRequest,
                                        String mytheme,
                                        String myname) {
    Hashtable mynode = getThemeNode(inRequest,mytheme,myname);
    if ( mynode == null )  return null;
    if ( mynode.get("child") == null ) return null;
    return CommonTools.stringToArray(mynode.get("child").toString(),
                                     DataReader.LIST_SPLITTER);
  }

  /**
   * ���˽ڵ������Բ���ʾ�û��޲���Ȩ�޵Ĺ���
   * @param inRequest ��ǰ����
   * @param mynodes �ڵ���
   * @return ���˺�Ľڵ���
   */
  public static ArrayList filterThemeChild(HttpServletRequest inRequest,
      ArrayList mynodes) {

    ArrayList myfnodes = new ArrayList();
    String myname,mylink;
    Hashtable myv ;
    for ( int  i=0; i< mynodes.size(); i++) {
      myname = mynodes.get(i).toString();
      myv = getThemeNode(inRequest, myname);
      if ( myv == null ) continue;
      if ( myv.get("child")  != null ) {
        myfnodes.add(myname);
        continue;
      }
      if ( myv.get("link")  != null ) {
        mylink = myv.get("link").toString();
        int pos1 = mylink.indexOf(DataManager.URL);
        if ( pos1 < 0 ) {
          myfnodes.add(myname);
          continue;
        }
        pos1 = mylink.indexOf("myset=");
        int pos2 = mylink.indexOf(DataReader.CHAR_AND, pos1);
        String myset;
        if ( pos2 < 0 )
          myset = mylink.substring( (pos1 + 6) );
        else
          myset = mylink.substring( (pos1 + 6) , pos2 );
        Hashtable myops = AclTools.getUserOperations(inRequest,
            ServletTools.getSessionUser(inRequest), myset);
        if ( ( myops != null )  &&
             myops.keys().hasMoreElements() ) {
          myfnodes.add(myname);
          continue;
        }
      }
    }
    return myfnodes;
  }

  /**
   * ���������ַ��������滻�����ַ���
   * @param inRequest ��ǰ����
   * @param mynode �ڵ���
   * @param mylink ����ֵ
   * @return ���˺�Ľڵ���
   */
  public static String filterThemeLink(HttpServletRequest inRequest,
                                       String mynode,
                                       String mylink) {
    if ( ( mynode == null ) || "".equals(mynode) ) return "";
    if ( ( mylink == null ) || "".equals(mylink) ) return "";
    // xml�в���ֱ�ӱ���"&"
    String myl =  mylink.replaceAll(DataReader.CHAR_AND,"&");
    if ( "self".equals(mynode) &&
         AclTools.isSuperseer(inRequest))
      return myl.replaceAll("myset=user&","myset=superseer&");
    else
      return myl;
  }

  /**
   * �ٶȲ���
   * @param inRequest ��ǰ����
   * @param out ��ǰ���
   */
  public static void  outSpeedTesting(HttpServletRequest inRequest,
      PrintWriter out) {

    PageTools.outHeader(inRequest,out,
                        DataManager.SEE_SPEED_TESTING, 0);

    out.println("\t<FORM name=seeform  method=post " +
                " action=\"" + inRequest.getRequestURI() + "\"" +
                " onSubmit='return false;'>");
    out.println("\t<INPUT type=hidden name=myframe value=\"data\">");
    out.println("\t<INPUT type=hidden name=myset value=\"" +
                DataManager.SEE_SPEED_TESTING + "\">");
    String mybutton = ServletTools.getParameter(inRequest,"mybutton");
    if ( mybutton == null ) mybutton = "";
    out.println("\t<INPUT type=hidden name=mybutton value=" +
                mybutton + ">");

    Hashtable mykey =  new Hashtable();
    long myb = CommonTools.getCurrentTime();
    DataReader myreader =  DataReaderGetor.dataReader(inRequest,
        DataManager.SEE_SPEED_TESTING);
    String mysort = ServletTools.getParameter(inRequest,"input_sort");
    DataStructureXmlParser mystrucReader = null;
    if ( (mysort != null) && !"".equals(mysort))
      mystrucReader = DataManagerTools.getDataStructureReader(inRequest,
          DataManager.SEE_SPEED_TESTING) ;
    if ( mystrucReader != null)
      myreader.startReading(0,-1,
                            null, null, null,
                            mysort,mysort,true,
                            mystrucReader.getItemTypes(),
                            ServletTools.getAppDefaultEncoding(inRequest));
    else
      myreader.startReading();
    ArrayList mydd = null;
    mydd = myreader.getRecords();
    if ( "remove".equals(mybutton) ||
         ( mydd == null )) {
      mydd = new ArrayList();
    }
    int myadd =
        CommonTools.stringToInt(ServletTools.getParameter(inRequest,
        "input_add"));
    if ( myadd < 1 ) myadd = 100;
    if ( "add".equals(mybutton)) {
      int mycount = mydd.size() + myadd;
      String myenc = ServletTools.getAppDefaultEncoding(inRequest);
      long myvv = myb;
      for ( int i=mydd.size(); i< mycount; i++) {
        mykey =  new Hashtable();
        mykey.put("id","testing" + i);
        mykey.put("char", (i % 10) + "");
        mykey.put("int", i + "");
        mykey.put("float", (i * 0.01f) + "");
        mykey.put("string", i + "");
        mykey.put("date", CommonTools.getDateFormat(myenc).format(new Date(myb + (long)i * 3600000 * 24)) );
        mykey.put("time", CommonTools.getTimeFormat(myenc).format(new Date(myb + (long)i * 1000 )) );
        mykey.put("auto", ServletTools.getAutoValue(inRequest) );
        mydd.add(mykey);
      }
    }
    String fdata = DataManagerTools.getDataValuesFile(inRequest,
        DataManager.SEE_SPEED_TESTING);
    if ( "add".equals(mybutton) ||
         "remove".equals(mybutton) ) {
      DataXmlWriter myw = new DataXmlWriter();
      String retw = myw.writeData(mydd,fdata,
                                  ServletTools.getAppDefaultCharset(inRequest));
    }

    out.println("\t<TABLE align=center class=data >");
    out.println("\t\t<TR class=tableline>");
    out.println("\t\t\t<TD align=right>" + tr(inRequest,"add") + "</TD><TD>");
    OutputController.outRadioInput(inRequest, out,"add",myadd + "",
                               CommonTools.stringToArray("1000,500,200,100,50",","),
                               null, false , false);
    out.println("</TD></TR>");
    out.println("\t\t<TR class=tableline>");
    out.println("\t\t\t<TD align=right>" + tr(inRequest,"sort") + "</TD><TD>");
    OutputController.outRadioInput(inRequest, out,"sort",mysort,
                               CommonTools.stringToArray("char,string,int,float,date,time,auto",","),
                               null, true , false);
    out.println("</TD></TR></TABLE>");
    out.println("<P align=center>" );
    ArrayList myt = new ArrayList();
    myt.add("add");
    myt.add("remove_all");
    OutputController.outButtonBar(inRequest, out, myt);
    out.println("</P>");
    long mye = CommonTools.getCurrentTime();
    out.println("<P align=center class=outresult>");
    out.println(tr(inRequest,"total_count") +
                " : " + mydd.size() +
                " &nbsp&nbsp&nbsp&nbsp " +
                tr(inRequest,"response_speed") +
                " : " + (mye - myb) +
                " &nbsp " + tr(inRequest,"milliseconds"));
    out.println("</P>");

    out.println("<P align=center class=info>" +
                tr(inRequest,"see_speed_info") + "</P>");

    out.println("\t<SCRIPT LANGUAGE='javascript'>\n");
    out.println("\t\tfunction see_add()");
    out.println("\t\t{");
    out.println("\t\t\t document.seeform.mybutton.value = \"add\";");
    out.println("\t\t\t document.seeform.submit();");
    out.println("\t\t}");
    out.println("\t\tfunction see_remove_all()");
    out.println("\t\t{");
    out.println("\t\t\t document.seeform.mybutton.value = \"remove\";");
    out.println("\t\t\t document.seeform.submit();");
    out.println("\t\t}");
    out.println("\t</SCRIPT>\n");
    out.println("</FORM> \n");
    PageTools.outFooter(inRequest, out);
    return;
  }

}