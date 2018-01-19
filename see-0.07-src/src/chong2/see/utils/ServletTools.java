package chong2.see.utils;

import chong2.see.data.DataReader;
import chong2.see.data.DataReaderGetor;
import chong2.see.data.base.Constants;
import chong2.see.data.base.DataStructure;
import chong2.see.data.base.Language;
import chong2.see.servlet.common.DataManager;
import chong2.see.xml.DataStructureXmlParser;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>Title: ������̹�����</p>
 * <p>Description: Ϊ����з��ṩ�������ݺ������ƽ̨</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: ���</p>
 *
 * <p>servlet�г��õľ�̬������
 *
 * @author ����
 * @version 0.07
 */

final public class ServletTools {

  /**
   * servlet��Ӧ�õĳ���
   */
//  public static String FILE_PREFIX  = "/WEB-INF/";
//  public static String IMAGE_PATH =  "image/";
//  public static String SCRIPT_PATH =  "script/";

/**
 * ȱʡ������
 */
  public static String DEFAULT_INTERFACE_STYLE =  "style_default" ;

  /**
   * ȱʡ���淽��
   */
  public static String DEFAULT_INTERFACE_THEME =  "theme_default" ;

  /**
   * ȱʡҳ�����ݸ���
   */
  public static int DEFAULT_PAGESIZE =  8 ;

  /**
   * �û������б�����˵ķǷ��ַ�
   */
  public static String INVAILD_PARAMETER_CHARS =  "";


 /*  ===================== ͨ�÷��� ==================== */


  public static boolean  isFile(HttpServletRequest myRequest,
      String infile) {
      if ( infile == null ) return false;
      String myfile = getFileRealPath(myRequest,infile);
      if ( myfile == null ) return false;
      return CommonTools.isFile(myfile);
  }



/**
 * ���Ŀ¼�µ������ļ���
 * @param myRequest ��ǰ����
 * @param indir Ŀ¼��
 * @return Ŀ¼�µ������ļ���
 */
  public static ArrayList getDirListName(HttpServletRequest myRequest,
      String indir) {
    try {
      if ( indir == null ) return null;
      String mydir = getFileRealPath(myRequest,indir);
      File f = new File(mydir);
      String[] myf = f.list();
      ArrayList myfiles = new ArrayList();
      for (int i=0; i< myf.length; i++) {
        myfiles.add(myf[i]);
      }
      return myfiles;
    }
    catch (Exception ex) {
      return null;
    }
  }

  /**
   * ���һ��Ŀ¼�������ļ�
   * @param myRequest ��ǰ����
   * @param indir Ŀ¼�������·����
   * @return Ŀ¼�������ļ�
   */
  public static ArrayList getDirListFile(HttpServletRequest myRequest,
      String indir) {
    try {
      if ( indir == null ) return null;
      String mydir = getFileRealPath(myRequest,indir);
      if ( mydir == null ) return null;
      File f = new File(mydir);
      if ( f == null ) return null;
      File[] myf = f.listFiles();
      if ( myf == null ) return null;
      ArrayList myfiles = new ArrayList();
      for (int i=0; i< myf.length; i++) {
        myfiles.add(myf[i]);
      }
      return myfiles;
    }
    catch (Exception ex) {
      return null;
    }
  }


  /*  ===================== ��ʼ�����÷��� ==================== */

  /**
   * ��ȡ��ʼ���ļ�����Ӧ�û����±���ϵͳ�ĳ�ʼ�����ò�����
   * @param myContext ��ǰӦ�û���
   */
  public static void setInitConfig(ServletContext myContext) {

    DataReader mydataReader =
        DataReaderGetor.dataReader(myContext,"xml",
        DataManager.INIT_CONFIG);
    mydataReader.startReading();
    myContext.setAttribute(DataManager.INIT_CONFIG,
                           mydataReader.getRecords().get(0));

  }

  /**
   * ��ȡ����Ӧ�õĳ�ʼ���ò�����
   * @param myContext ��ǰӦ�û���
   * @return Ӧ�õĳ�ʼ���ò���
   */
  public static Hashtable getInitConfig(ServletContext myContext) {
    return (Hashtable)(myContext.getAttribute(DataManager.INIT_CONFIG));
  }

  /**
   * ��ȡ����Ӧ�õ�һ����ʼ���ò�����
   * @param myContext ��ǰӦ�û���
   * @param inKey ������
   * @return ����ֵ
   */
  public static String getInitConfig(ServletContext myContext,
                                     String inKey) {

    if (  ( inKey == null ) || ( "".equals(inKey) ) )
      return null;

    Hashtable myinit =
        (Hashtable)(myContext.getAttribute(DataManager.INIT_CONFIG));
    if ( myinit == null  ) return null;

    return (String)( myinit.get(inKey) );

  }

  /**
   * ��ȡ����Ӧ�õ�һ����ʼ���ò�����
   * @param myRequest ��ǰ����
   * @param inKey ������
   * @return ����ֵ
   */
  public static String getInitConfig(HttpServletRequest myRequest,
                                     String inKey) {
    return getInitConfig(getContext(myRequest),inKey);
  }

  /**
   * ��ȡ����Ӧ�õ�ȱʡ�û���
   * @param myRequest ��ǰ����
   * @return ����Ӧ�õ�ȱʡ�û���
   */
  public static String getAppDefaultUser(HttpServletRequest myRequest) {
    return LanguageTools.atr(myRequest, getInitConfig(myRequest,"default_user"));
  }

  /**
   * ��ȡ����Ӧ�õ�ȱʡ����
   * @param myRequest ��ǰ����
   * @return ����Ӧ�õ�ȱʡ����
   */
  public static String getAppDefaultEncoding(HttpServletRequest myRequest) {
    return getAppDefaultEncoding(getContext(myRequest));
  }

  /**
   * ��ȡ����Ӧ�õ�ȱʡ����
   * @param myContext ��ǰӦ�û���
   * @return ����Ӧ�õ�ȱʡ����
   */
  public static String getAppDefaultEncoding(ServletContext myContext) {
//    return getInitConfig(myContext,"default_encoding");
    // ������̬�޸�ϵͳ���뷽ʽ��û��Ҫ����Σ�գ�
    String myenc =
        myContext.getAttribute("defaultEncoding").toString();
    return Language.getVaildEncoding(myenc) ;
  }

  /**
   * ��ȡ����Ӧ�õ�ȱʡ�����ַ���
   * @param myContext ��ǰӦ�û���
   * @return ����Ӧ�õ�ȱʡ�����ַ���
   */
  public static String getAppDefaultCharset(ServletContext myContext) {
    String myenc = getAppDefaultEncoding(myContext);
    return Language.getCharset(myenc);
  }

  /**
   * ��ȡ����Ӧ�õ�ȱʡ�����ַ���
   * @param myRequest ��ǰ����
   * @return ����Ӧ�õ�ȱʡ�����ַ���
   */
  public static String getAppDefaultCharset(HttpServletRequest myRequest) {
    String myenc = getAppDefaultEncoding(myRequest);
    return Language.getCharset(myenc);
  }


  /**
   * ��ȡ����Ӧ�õ�ȱʡ������
   * @param myRequest ��ǰ����
   * @return ����Ӧ�õ�ȱʡ������
   */
  public static String getAppDefaultInterfaceStyle(HttpServletRequest myRequest) {
    return getInitConfig(getContext(myRequest),
                         "default_interface_style");
  }

  /**
   * ��ȡ����Ӧ�õ�ȱʡ���淽��
   * @param myRequest ��ǰ����
   * @return ����Ӧ�õ�ȱʡ���淽��
   */
  public static String getAppDefaultInterfaceTheme(HttpServletRequest myRequest) {
    return getInitConfig(getContext(myRequest),
                         "default_interface_theme");
  }

  /**
   * ��ȡ����Ӧ�õ������������
   * @param myRequest ��ǰ����
   * @return ����Ӧ�õ������������
   */
  public static ArrayList getAppDataAuditTypes(HttpServletRequest myRequest) {
    try {
      String myt = getInitConfig(getContext(myRequest),
                                 "data_audit_types");
      ArrayList mytt =
          CommonTools.stringToArray(myt, DataReader.LIST_SPLITTER);
      if ( mytt == null )
        mytt = new ArrayList() ;
      return mytt;
    }
    catch (Exception ex) {
      return new ArrayList();
    }
  }

  /**
   * ��ȡ����Ӧ�õ�ȱʡ�ϴ���С��KB��
   * @param myRequest ��ǰ����
   * @return ����Ӧ�õ�ȱʡ�ϴ���С
   */
  public static int getAppDefaultUploadSize(HttpServletRequest myRequest) {
    String mysize = getInitConfig(getContext(myRequest),
                                  "default_upload_size");
    if ( mysize == null ) return -1;
    return CommonTools.stringToInt(mysize);
  }

  /**
   * ��ȡ����Ӧ�õ�ȱʡ���ϴ��ռ䣨KB��
   * @param myRequest ��ǰ����
   * @return ����Ӧ�õ�ȱʡ���ϴ��ռ�
   */
  public static int getAppDefaultUploadTotal(HttpServletRequest myRequest) {
    String mysize = getInitConfig(getContext(myRequest),
                                  "default_upload_total");
    if ( mysize == null ) return -1;
    return CommonTools.stringToInt(mysize);
  }

  public static String getAppDataSourceType(HttpServletRequest myRequest) {
    return getAppDataSourceType(getContext(myRequest));
  }

  public static String getAppDataSourceType(ServletContext myContext) {
    String mytype = getInitConfig(myContext, "data_source");
    if ( mytype == null ) return "xml";
    return mytype;
  }



/*  ===================== session��صķ��� ==================== */

/**
 * ��ȡsession����Ĳ���ֵ
 * @param myRequest ��ǰ����
 * @param inKey ������
 * @return ����ֵ�������򷵻�null��
 */
  public static String getSessionAttr(HttpServletRequest myRequest,
                                      String inKey) {
    try {
      return (String)(myRequest.getSession(true).getAttribute(inKey));
    }
    catch (Exception ex) {
      return null;
    }
  }

  /**
   * ����session�Ĳ���ֵ
   * @param myRequest ��ǰ����
   * @param inKey ������
   * @param inValue ����ֵ
   */
  public static void setSessionAttr(HttpServletRequest myRequest,
                                    String inKey, Object inValue) {

    myRequest.getSession(true).setAttribute(inKey,inValue);
  }

  /**
   * ���õ�ǰ�û���ȱʡsession����ֵ
   * @param inRequest ��ǰ����
   * @param myuser ��ǰ�û��Ĳ�����-ֵ��
   * @return �Ƿ�ɹ�
   */
  public static boolean setSessionUser(HttpServletRequest inRequest,
                                       Hashtable myuser) {
    if (  myuser == null ) return false;
    if ( myuser.get("id") == null ) return false;

    //session�û���
    String myid = myuser.get("id").toString();
    setSessionAttr(inRequest,"sessionUser",myid );

    //session�û��ǳ�
    String mynick = myuser.get("id").toString();
    if ( myuser.get("nickname") != null )
      mynick = myuser.get("nickname").toString();
    setSessionAttr(inRequest,"sessionNick",mynick);

    //session�û�������
    String mystyle;
    if ( myuser.get("interface_style") != null )
      mystyle = myuser.get("interface_style").toString();
    else
      mystyle = getAppDefaultInterfaceStyle(inRequest);
    if ( mystyle == null ) mystyle = DEFAULT_INTERFACE_STYLE;
    setSessionAttr(inRequest,"sessionInterfaceStyle",mystyle);

    //session�û����淽��
    String mytheme;
    if ( myuser.get("interface_theme") != null )
      mytheme = myuser.get("interface_theme").toString();
    else
      mytheme = getAppDefaultInterfaceTheme(inRequest);
    if ( mytheme == null ) mytheme = DEFAULT_INTERFACE_THEME;
    setSessionAttr(inRequest,"sessionInterfaceTheme",mytheme);

    //session�û�������ʾ���
    String mydatastyle = DataManager.DEFAULT_DATA_STYLE;
    if ( myuser.get("datalist_style") != null )
      mydatastyle = myuser.get("datalist_style").toString();
    setSessionAttr(inRequest,"sessionDatalistStyle",mydatastyle);

    //session�û�ȱʡ��Ŀ��
    String myproject = "";
    if ( myuser.get("default_project") != null )
      myproject = myuser.get("default_project").toString();
    setSessionAttr(inRequest,"sessionProject",myproject);

    // �������û����ñ��뷽ʽ��û��Ҫ����Σ�գ�
//    String mylang = Constants.DEFAULT_LANGUAGE;
//    if ( myuser.get("language") != null )
//      mylang = myuser.get("language").toString();
//    setSessionAttr(inRequest,"sessionEncoding",mylang);

    //session�û���������ҳ��Сֵ
    int mysize = DEFAULT_PAGESIZE;
    if ( myuser.get("page_size") != null )
      mysize =  CommonTools.stringToInt(myuser.get("page_size").toString());
    if ( mysize == CommonTools.WRONG_INT )
      mysize = DEFAULT_PAGESIZE;
    setSessionAttr(inRequest,"sessionPageSize",
                   CommonTools.intToString(mysize));

    if ( !myid.equals(getAppDefaultUser(inRequest))) {
      Hashtable myu = new Hashtable();
      myu.put("user",myid);
      ArrayList myw = new ArrayList();
      myw.add(myu);
      if ( ServletTools.getAppDataAuditTypes(inRequest).contains(LanguageTools.atr(inRequest,"login")) )
        AuditTools.addAuditL(inRequest,"chong2see","login",
                             myw,null , null,
                             Constants.SUCCESSFUL, null ,null);

    }

    return true;

  }

  /**
   * ���õ�ǰ�û���ȱʡsession����ֵ�����ȼ���Ƿ�Ϊ�Ϸ��û���
   * @param myRequest ��ǰ����
   * @param myid �û���
   * @param mypass �û�����
   * @return �Ƿ�ɹ�
   */
  public static boolean setSessionUser(HttpServletRequest myRequest,
                                       String myid,
                                       String mypass) {
    Hashtable myuser = AclTools.getUser (myRequest, myid);
    if (  myuser == null ) return false;
    if ( CommonTools.isVaildPassword(mypass,myuser.get("password")) )
      return setSessionUser(myRequest,myuser);
    else {
      if ( !myid.equals(getAppDefaultUser(myRequest))) {
        Hashtable myu = new Hashtable();
        myu.put("user",myid);
        ArrayList myw = new ArrayList();
        myw.add(myu);
        if ( ServletTools.getAppDataAuditTypes(myRequest).contains(LanguageTools.atr(myRequest,"login")) )
          AuditTools.addAuditL(myRequest,"chong2see","login",
                               myw,null , null,
                               "invalid_login", null ,null);

      }
      return false;
    }
  }

  /**
   * ���õ�ǰ�û���ȱʡsession����ֵ
   * @param myRequest ��ǰ����
   * @param myid ��ǰ�û���
   * @return �Ƿ�ɹ�
   */
  public static boolean setSessionUser(HttpServletRequest myRequest,
                                       String myid) {
    return setSessionUser(myRequest,
                          AclTools.getUser(myRequest, myid));
  }

  /**
   * ����sessionȱʡҳ��Сֵ�����Ը��ַ�ʽ������˳��Ϊ���û����롢
   * session�ѱ���ֵ��Ӧ��ȱʡֵ������ȱʡֵ
   * @param inRequest ��ǰ����
   * @return �����õ�sessionȱʡҳ��Сֵ
   */
  public static int setSessionPageSize(HttpServletRequest inRequest) {
    try {
      int  mypagesize = CommonTools.WRONG_INT;
      //�û�����ֵ
      if ( getParameter(inRequest,"input_page_size") != null )
        mypagesize =
        CommonTools.stringToInt(getParameter(inRequest,"input_page_size"));
      //��ǰsession��ȡֵ
      if ( mypagesize == CommonTools.WRONG_INT)
        mypagesize =
        CommonTools.stringToInt(getSessionPageSize(inRequest));
      //Ӧ��ȱʡֵ
      if ( mypagesize == CommonTools.WRONG_INT)
        mypagesize =
        CommonTools.stringToInt(getInitConfig(inRequest,"default_page_size"));
      //����ȱʡֵ
      if ( mypagesize == CommonTools.WRONG_INT)
        mypagesize = DEFAULT_PAGESIZE;
      setSessionAttr(inRequest,"sessionPageSize",
                     CommonTools.intToString(mypagesize));
      return mypagesize;
    }
    catch (Exception ex) {
      return DEFAULT_PAGESIZE;
    }
  }

  /**
   * ����sessionȱʡ������ʾ��񡣳��Ը��ַ�ʽ������˳��Ϊ���û����롢
   * session�ѱ���ֵ������ȱʡֵ
   * @param inRequest ��ǰ����
   * @param inname �û�����ؼ�������
   * @return �����õ�sessionȱʡ������ʾ���
   */
  public static String setSessionDatalistStyle(HttpServletRequest inRequest,
      String inname) {
    try {
      //�û�����ֵ
      String mystyle = getParameter(inRequest,inname);
      ArrayList allstyles = LanguageTools.atr(inRequest,
          DataManager.DATALIST_STYLE);
      //ԭ����ֵ
      if ( ( mystyle == null ) || "".equals(mystyle) ||
           !allstyles.contains(mystyle))
        mystyle = getSessionDatalistStyle(inRequest);
      //����ȱʡֵ
      if ( ( mystyle == null ) || "".equals(mystyle) ||
           !allstyles.contains(mystyle))
        mystyle = LanguageTools.atr(inRequest,DataManager.DEFAULT_DATA_STYLE);
      setSessionAttr(inRequest,"sessionDatalistStyle",
                     mystyle);
      return mystyle;
    }
    catch (Exception ex) {
      return LanguageTools.atr(inRequest,
                               DataManager.DEFAULT_DATA_STYLE);
    }
  }

  /**
   * ����sessionȱʡ�����񡣳��Ը��ַ�ʽ������˳��Ϊ���û����롢
   * session�ѱ���ֵ������ȱʡֵ
   * @param inRequest ��ǰ����
   * @return �����õ�sessionȱʡ������
   */
  public static String setSessionInterfaceStyle(HttpServletRequest inRequest) {
    try {
      //�û�����ֵ
      String mystyle = getParameter(inRequest,"input_interface_style");
      //ԭ����ֵ
      if ( ( mystyle == null ) || "".equals(mystyle) )
        mystyle = getSessionInterfaceStyle(inRequest);
      //����ȱʡֵ
      if ( ( mystyle == null ) || "".equals(mystyle) )
        mystyle = DEFAULT_INTERFACE_STYLE;
      setSessionAttr(inRequest,"sessionInterfaceStyle",
                     mystyle);
      return mystyle;
    }
    catch (Exception ex) {
      return DEFAULT_INTERFACE_STYLE;
    }
  }

  /**
   * ����sessionȱʡ���淽�������Ը��ַ�ʽ������˳��Ϊ���û����롢
   * session�ѱ���ֵ������ȱʡֵ
   * @param inRequest ��ǰ����
   * @return �����õ�sessionȱʡ���淽��
   */
  public static String setSessionInterfaceTheme(HttpServletRequest inRequest) {
    try {
      //�û�����ֵ
      String mytheme = getParameter(inRequest,"input_interface_theme");
      //ԭ����ֵ
      if ( ( mytheme == null ) || "".equals(mytheme) )
        mytheme = getSessionInterfaceTheme(inRequest);
      //����ȱʡֵ
      if ( ( mytheme == null ) || "".equals(mytheme) )
        mytheme = DEFAULT_INTERFACE_THEME;
      setSessionAttr(inRequest,"sessionInterfaceTheme",
                     mytheme);
      return mytheme;
    }
    catch (Exception ex) {
      return DEFAULT_INTERFACE_STYLE;
    }
  }

  /**
   * ����sessionȱʡ��Ŀֵ�����Ը��ַ�ʽ������˳��Ϊ���û����롢
   * session�ѱ���ֵ
   * @param inRequest ��ǰ����
   * @return �����õ�sessionȱʡ��Ŀֵ
   */
  public static String setSessionProject(HttpServletRequest inRequest) {
    try {
      //�û�����ֵ
      String myproject = getParameter(inRequest,"input_default_project");
      //ԭ����ֵ
      if ( ( myproject == null ) || "".equals(myproject) )
        myproject = getSessionProject(inRequest);
      if (  myproject == null  ) myproject = "";
      setSessionAttr(inRequest,"sessionProject", myproject);
      return myproject;
    }
    catch (Exception ex) {
      return "";
    }
  }

  /**
   * ��ȡ��ǰsession�û���
   * @param myRequest ��ǰ����
   * @return ��ǰsession�û���
   */
  public static String getSessionUser(HttpServletRequest myRequest) {
    return getSessionAttr(myRequest,"sessionUser");
  }

  /**
   * ��ȡ��ǰsession�û��ǳ���
   * @param myRequest ��ǰ����
   * @return ��ǰsession�û��ǳ���
   */
  public static String getSessionNick(HttpServletRequest myRequest) {
    return getSessionAttr(myRequest,"sessionNick");
  }

  /**
   * ��ȡ��ǰsession������
   * @param myRequest ��ǰ����
   * @return ��ǰsession������
   */
  public static String getSessionInterfaceStyle(HttpServletRequest myRequest) {
    return getSessionAttr(myRequest,"sessionInterfaceStyle");
  }

  /**
   * ��ȡ��ǰsession���淽��
   * @param myRequest ��ǰ����
   * @return ��ǰsession���淽��
   */
  public static String getSessionInterfaceTheme(HttpServletRequest myRequest) {
    return getSessionAttr(myRequest,"sessionInterfaceTheme");
  }

  /**
   * ��ȡ��ǰsession������ʾ���
   * @param myRequest ��ǰ����
   * @return ��ǰsession������ʾ���
   */
  public static String getSessionDatalistStyle(HttpServletRequest myRequest) {
    return getSessionAttr(myRequest,"sessionDatalistStyle");
  }

  /**
   * ��ȡ��ǰsession����
   * @param myRequest ��ǰ����
   * @return ��ǰsession����
   */
  public static String getSessionEncoding(HttpServletRequest myRequest) {
    String myenc = getSessionAttr(myRequest,"sessionEncoding");
    return Language.getVaildEncoding(myenc) ; //��Ҫɸ���Ƿ�ȡֵ
  }

  /**
   * ��ȡ��ǰsession������ַ���
   * @param myRequest ��ǰ����
   * @return ��ǰsession������ַ���
   */
  public static String getSessionCharset(HttpServletRequest myRequest) {
    String myenc = getSessionEncoding(myRequest);
    return Language.getCharset(myenc);
  }

  /**
   * ��ȡ��ǰsession��Ŀ��
   * @param myRequest ��ǰ����
   * @return ��ǰsession��Ŀ��
   */
  public static String getSessionProject(HttpServletRequest myRequest) {
    String myp = getSessionAttr(myRequest,"sessionProject");
    if ( myp == null )
      return ""; //���ⷵ�ؿ�ֵ
    else
      return getSessionAttr(myRequest,"sessionProject");
  }

  /**
   * ��ȡ��ǰsessionҳ��Сֵ
   * @param myRequest ��ǰ����
   * @return ��ǰsessionҳ��Сֵ
   */
  public static String getSessionPageSize(HttpServletRequest myRequest) {
    return getSessionAttr(myRequest,"sessionPageSize");
  }

  /**
   * ����session����ֵ
   * @param myRequest ��ǰ����
   * @param inEnc ����ֵ
   * @return �Ƿ����óɹ�
   */
  public static boolean setSessionEncoding(HttpServletRequest myRequest,
      String inEnc) {

    if ( getSessionEncoding(myRequest).equals(inEnc) )
      return false;
    setSessionAttr(myRequest,"sessionEncoding",
                   Language.getVaildEncoding(inEnc));
    return true;

  }

  /**
   * ��ȡ��ǰ�����Ӧ�û���ֵ
   * @param myRequest ��ǰ����
   * @return ��ǰ�����Ӧ�û���ֵ
   */
  public static ServletContext getContext(HttpServletRequest myRequest) {
    return myRequest.getSession(true).getServletContext();
  }

  /*  ===================== ͨ�÷��� ==================== */

  /**
   * ��ȡ�ļ�����ʵ·��������·����
   * @param myRequest ��ǰ����
   * @param myfile �ļ��������·������Ӧ�÷��ʻ����µ�·��
   * @return �ļ�����ʵ·��������·����
   */
  public static String getFileRealPath(HttpServletRequest myRequest,
                                       String myfile) {

    return  getContext(myRequest).getRealPath(myfile);
  }

  /**
   * ��ȡ�ļ�����ʵ·��������·����
   * @param myContext ��ǰӦ�û���
   * @param myfile �ļ��������·������Ӧ�÷��ʻ����µ�·��
   * @return �ļ�����ʵ·��������·����
   */
  public static String getFileRealPath(ServletContext myContext,
                                       String myfile) {
    return myContext.getRealPath(myfile);
  }


  /**
   * ����ҳ�����ֵ
   * @param myvalue ����ֵ
   * @return ���˺�Ĳ���ֵ
   */
  public static String filterParameter(String myvalue) {
    try {
      String myv = new String(myvalue);
      //���ַ���Ҳ����null
      if ( ( myv == null ) || "".equals(myv ))
        return null;
      // �Ƿ��ַ�ֱ��ɾ��
      myv = myv.replaceAll(INVAILD_PARAMETER_CHARS,"");
      // �û���������Ҫת����ŵ������ַ������ǿ���ʹxml����ҳ������ҡ�
      String mykc;
      for (Enumeration myk = DataReader.SPECIAL_CHARS.keys();
           myk.hasMoreElements();) {
        mykc = myk.nextElement().toString();
        myv = myv.replaceAll(mykc,
                       DataReader.SPECIAL_CHARS.get(mykc).toString());
      }
      if ( "".equals(myv) )
        return null;
      return myv;
    }
    catch (Exception ex) {
      return null;
    }
  }

  /**
   * ���ҳ��Ĳ���ֵ����Ҫ���˷Ƿ��ַ����滻�����ַ���
   * @param myRequest ��ǰ����
   * @param myname ������
   * @return ����ֵ
   */
  public static String getParameter(HttpServletRequest myRequest,
                                    String myname) {
    try {
      if ( ( myname == null ) || "".equals(myname ))
        return null;
      String myv = null ;
      if ( myRequest.getAttribute(UploadTools.UPLOAD_NAME) != null ) {
        Hashtable myup =
            (Hashtable)myRequest.getAttribute(UploadTools.UPLOAD_NAME);
        if ( myup.get(myname) != null )
          myv = myup.get(myname).toString();
      } else
        myv = myRequest.getParameter(myname);
      /* ����ע�⣺ͨ��get��ʽ�ύ�Ĳ����ƺ��޷��Զ�����filter�б�������á�
      ����ֻ�����ת��
      */
//      if ( "GET".equals(myRequest.getMethod())) {
//        myv = new String(myv.getBytes(getAppDefaultEncoding(myRequest)));
//        myv = new String(myv.getBytes("iso-8859-1"));
//        myv = new String(myv.getBytes("UTF-8"));
//      }
      /* �����Եò�����������ڱ������get��ʽ��
      */
      if ( ( myv == null ) || "".equals(myv ))
        return null;
      return filterParameter(myv);
    }
    catch (Exception ex) {
      return null;
    }
  }

  /**
   * ���ҳ��Ķ�ֵ������ֵ��
   * @param myRequest ��ǰ����
   * @param myname ������
   * @return ����ֵ
   */
  public static String getParameters(HttpServletRequest myRequest,
                                     String myname) {
    try {
      if ( ( myname == null ) || "".equals(myname ))
        return null;
      String myv = null ;
      if ( myRequest.getAttribute(UploadTools.UPLOAD_NAME) != null ) {
        Hashtable myup =
            (Hashtable)myRequest.getAttribute(UploadTools.UPLOAD_NAME);
        if ( myup.get(myname) != null )
          myv = myup.get(myname).toString();
      } else
        myv =
        CommonTools.arrayToString(myRequest.getParameterValues(myname),
        DataReader.LIST_SPLITTER);
      if ( ( myv == null ) || "".equals(myv ))
        return null;
      return myv;
    }
    catch (Exception ex) {
      return null;
    }
  }

  /**
   * ���ҳ�������в�����ֵ��
   * @param inRequest ��ǰ����
   * @param mystrucReader ��Ϣ�ṹ����
   * @param myop ��ǰ����
   * @return ҳ�������в�����ֵ
   */
  public static Hashtable getParameters(HttpServletRequest inRequest,
                                        DataStructureXmlParser mystrucReader,
                                        String myop) {
    if ( mystrucReader == null )
      return new Hashtable();
    return getParameters(inRequest,
                         mystrucReader.getEditItemNames(),
                         mystrucReader.getPasswordItems(),
                         mystrucReader.getConstraintTypes(),
                         myop);

  }

  /**
   * ���ҳ�������в�����ֵ��
   * @param inRequest ��ǰ����
   * @param itemnames ��Ϣ�ṹ����
   * @param mypass ��Ϣ�ṹ����
   * @param mytypes ����ֵԼ�����͵Ķ���
   * @param myop ��ǰ����
   * @return ҳ�������в�����ֵ
   */
  public static Hashtable getParameters(HttpServletRequest inRequest,
                                        ArrayList itemnames,
                                        ArrayList mypass,
                                        Hashtable mytypes,
                                        String myop) {

    Hashtable item = new Hashtable();
    boolean   isQuery = "query".equals(myop) ;
    try {
      if ( itemnames == null ) return item;

      String  myname,myvalue,mydtype,mytype;
      for (int i=0; i < itemnames.size(); i++ ) {
        myname = itemnames.get(i).toString();
        mytype = null;
        if ( ( mytypes != null ) &&
             ( mytypes.get(myname) != null ) )
        mytype = mytypes.get(myname).toString();
        myvalue = null;
        if (  !isQuery &&
              !"add".equals(myop) &&
              !"copy".equals(myop) &&
             mypass.contains(myname) ) {
          String mynull = getParameter(inRequest,
                                       "input_" + myname + "3");
          if ( "yes".equals(mynull))
            myvalue = DataReader.SEE_PASSWORD_NULL;
          else {
            myvalue =
                ServletTools.getParameter(inRequest,"input_" + myname);
            if ( myvalue != null)
              myvalue = CommonTools.encryptToHex(myvalue, true) ;
            else
              myvalue = DataReader.SEE_PASSWORD_NOT_MODIFY;
          }
        } else if ( "multi".equals(mytype) ||
                      "data_audit_types".equals(mytype)) {
            myvalue = getParameters(inRequest, "input_" + myname);
            myvalue = ServletTools.filterParameter(myvalue);
        } else if ( !isQuery && "link".equals(mytype) ) {
            String myv = getParameter(inRequest,"input_" + myname);
            String myn = getParameter(inRequest,
                                      "input_" + myname + "2");
            if ( (myv != null) && (myn != null) )
              myvalue = myn + DataReader.HASH_SPLITTER + myv;
        } else if ( !isQuery && "file".equals(mytype) ) {
            String myv = getParameter(inRequest,"input_" + myname);
            String myn = getParameter(inRequest,
                                      "input_" + myname + "2");
            String mycu = getParameter(inRequest,
                                       "input_" + myname + "3");
            if (myn == null) {
              if ( myv != null )
                UploadTools.removeUploadFile(inRequest,myv);
              if ( mycu != null )
                UploadTools.removeUploadFile(inRequest,mycu);
            } else {
              if ( myv != null ) {
                myvalue = myn + DataReader.HASH_SPLITTER +
                          CommonTools.getFileName(myv);
                if ( mycu != null )
                  UploadTools.removeUploadFile(inRequest,mycu);
              } else {
                if ( mycu != null ) {
                  String mynull =
                      ServletTools.getParameter(inRequest,"input_" + myname + "4");
                  if ( "yes".equals(mynull) )
                    UploadTools.removeUploadFile(inRequest,mycu);
                  else
                    myvalue = myn + DataReader.HASH_SPLITTER +
                    CommonTools.getFileName(mycu);
                }
              }
            }
          } else {
            myvalue = ServletTools.getParameter(inRequest,"input_" + myname);
          }
          if ( (myvalue == null) || "".equals(myvalue) )
            continue;
          item.put(myname,myvalue);
      }

      return item;
    }
    catch (Exception ex) {
      return item;
    }

  }

  /**
   * ����Զ�ȡֵ
   * @param inRequest ��ǰ����
   * @return �Զ�ȡֵ�����ã���ǰʱ��-��ǰ�û����Ա�֤��Ψһ�ԡ�
   */
  public static String getAutoValue(HttpServletRequest inRequest) {
    return getCurrentTime(inRequest)  +
        "-" + ServletTools.getSessionUser(inRequest);
  }

  /**
   * ��ÿͻ��������ַ
   * @param inRequest ��ǰ����
   * @return �ͻ��������ַ
   */
  public static String  getHost(HttpServletRequest inRequest) {
    return inRequest.getRemoteHost();
  }

  /**
   * ��õ�ǰ���ڡ���Ӧ�ñ���ĸ�ʽȡֵ��
   * @param inRequest ��ǰ����
   * @return ��ǰ���ڣ���Ӧ�ñ���ĸ�ʽȡֵ��
   */
  public static String getCurrentDate(HttpServletRequest inRequest) {
    return CommonTools.getCurrentDate(ServletTools.getAppDefaultEncoding(inRequest));
  }

  /**
   * ��õ�ǰʱ�䡣��Ӧ�ñ���ĸ�ʽȡֵ��
   * @param inRequest ��ǰ����
   * @return ��ǰʱ�䣬��Ӧ�ñ���ĸ�ʽȡֵ��
   */
  public static String getCurrentTime(HttpServletRequest inRequest) {
    return CommonTools.getCurrentTime(ServletTools.getAppDefaultEncoding(inRequest));
  }

  /**
   * ������е������������
   * @return ���е������������
   */
  public static ArrayList getAllDataAuditTypes() {
    ArrayList myd = new ArrayList();
    myd.addAll(DataStructure.DATASET_OPERATION_TYPES);
    myd.remove("help");
    myd.add("list_data");
    myd.add("show_record");
    myd.add("export");
    myd.add("login");
    return myd;
  }

  /**
   * ������еĲ�������
   * @param isSuperseer �Ƿ��ǳ����û�
   * @return ���е������������
   */
  public static ArrayList getAllOpearations(boolean isSuperseer) {
    ArrayList myd = new ArrayList();
    myd.addAll(DataStructure.DATASET_OPERATION_TYPES);
    myd.remove("help");
    if ( !isSuperseer ) { //ֻ�г����û��ſ��Ծ������ú���Ƶ�Ȩ��
      myd.remove("config");
      myd.remove("audit");
    }
    myd.add("select");
    return myd;
  }

  /**
   * ���ϵͳ��������Ϣ�ṹ�����ơ���ϵͳ�Զ�����Ŀ¼���ļ����жϡ�
   * @param myRequest ��ǰ����
   * @param isSuperseer �Ƿ��ǳ�������Ա
   * @return ϵͳ��������Ϣ�ṹ������
   */
  public static ArrayList getAllData(HttpServletRequest myRequest,
                                     boolean isSuperseer) {

    ArrayList myfiles =
        ServletTools.getDirListName(myRequest,
        DataStructureXmlParser.DATASET_STRUCTURE_PATH_PREFIX);
    String mydata;
    int mypos =
        DataStructureXmlParser.DATASET_STRUCTURE_FILE_PREFIX.length();
    ArrayList myd = new ArrayList();
    for (int i = 0; i < myfiles.size(); i++) {
      mydata = myfiles.get(i).toString();
      mydata = mydata.substring(mypos, mydata.length() -  4);
      if ( DataManager.SYSTEM_DATA.contains(mydata)) continue;
      if ( !isSuperseer &&
           DataManager.SUPER_DATA.contains(mydata))
        continue;
      myd.add(mydata );
    }
    return myd;

  }

  /**
   * ��ȡͼƬ��ҳ��·��
   * @param myRequest ��ǰ����
   * @param inName ͼƬ��
   * @return ͼƬ��ҳ��·��
   */
  public static String getImageName(HttpServletRequest myRequest,
                                    String inName) {
    return getImageName(ServletTools.getSessionEncoding(myRequest),
                        ServletTools.getSessionInterfaceStyle(myRequest),
                        inName);
  }

  /**
   * ��ȡͼƬ��ҳ��·��
   * @param inEnc ����
   * @param inStyle ���
   * @param inName ͼƬ��
   * @return ͼƬ��ҳ��·��
   */
  public static String getImageName(String inEnc,
                                    String inStyle,
                                    String inName) {
    StringBuffer myname = new StringBuffer();
    myname.append("image/");
    myname.append(inStyle);
    myname.append("/");
    myname.append(inEnc);
    myname.append("/");
    myname.append(inName);
//    if ( "style_flowers".equals(inStyle) )
//      myname.append(".gif");
//    else
      myname.append(".jpg");
    return myname.toString();
  }

  /**
   * ��ȡһ��ͼƬ��ҳ��·��
   * @param myRequest ��ǰ����
   * @param inNames ͼƬ��
   * @return ͼƬ��ҳ��·��
   */
  public static ArrayList getImageNames(HttpServletRequest myRequest,
                                        ArrayList inNames) {
    return getImageNames(ServletTools.getSessionEncoding(myRequest),
                         ServletTools.getSessionInterfaceStyle(myRequest),
                         inNames);
  }

  /**
   * ��ȡһ��ͼƬ��ҳ��·��
   * @param inEnc ����
   * @param inStyle ���
   * @param inNames ͼƬ��
   * @return ͼƬ��ҳ��·��
   */
  public static ArrayList getImageNames(String inEnc,
                                        String inStyle,
                                        ArrayList inNames) {
    ArrayList myimg = new ArrayList();
    for (int i=0; i < inNames.size(); i++) {
      myimg.add(getImageName(inEnc,inStyle,inNames.get(i).toString()));
    }
    return myimg;
  }

  /**
   * ��ȡ����ű�������
   * @param inName �����
   * @return �ű�������
   */
  public static String getClick(String inName) {
    return inName + "()";
  }

  /**
   * ��ȡһ�����Ľű�������
   * @param inNames �����
   * @return �ű�������
   */
  public static ArrayList getClicks(ArrayList inNames) {
    ArrayList myimg = new ArrayList();
    for (int i=0; i < inNames.size(); i++) {
      myimg.add(getClick(inNames.get(i).toString()));
    }
    return myimg;
  }


/*  ===================== ����xml���ݵķ��� ==================== */



/**
 * �ַ���ת��Ϊhash���ݡ��������е�����
 * @param myRequest ��ǰ����
 * @param inStr �ַ���
 * @param inSplit �ָ��
 * @return ת�����hash����
 */
  public static Hashtable stringToHash(HttpServletRequest myRequest,
                                       String inStr,
                                       String inSplit) {

    try {

      Hashtable myd = new Hashtable();

      String[] myss = inStr.split(inSplit);
      for (int i=0; i < myss.length; i++) {
        i++;
        if ( i >= myss.length ) break;
        if (  "".equals(myss[i-1]) || "".equals(myss[i]) )
          continue;
        myd.put( LanguageTools.tr(myRequest,myss[i-1]),myss[i]);
      }
      return myd;
    }
    catch (Exception ex) {
      return null;
    }

  }

  /**
   * hash����ת��Ϊ�ַ������������е�����
   * @param myRequest ��ǰ����
   * @param inHash hash����
   * @param inKeys ����
   * @param inSplit �ָ��
   * @return ת������ַ���
   */
  public static String hashToString(HttpServletRequest myRequest,
                                    Hashtable inHash,
                                    ArrayList inKeys,
                                    String inSplit) {

    try {
      StringBuffer mystr = new StringBuffer();
      String myname, myvalue;
      for (int j=0; j< inKeys.size(); j++) {
        myname = inKeys.get(j).toString();
        if ( inHash.get(myname) == null ) continue;
        mystr.append(LanguageTools.tr(myRequest,myname));
        mystr.append(inSplit);
        mystr.append(LanguageTools.tr(myRequest,inHash.get(myname).toString()));
        mystr.append(inSplit);
      }
      return mystr.toString();
    }
    catch (Exception ex) {
      return null;
    }

  }

  /**
   * hash����ת��Ϊ�ַ������������е�����
   * @param myRequest ��ǰ����
   * @param inHash hash����
   * @param inSplit �ָ��
   * @param mypass �����򣬲����������ʾ
   * @return ת������ַ���
   */
  public static String hashToString(HttpServletRequest myRequest,
                                    Hashtable inHash,
                                    String inSplit,
                                    ArrayList mypass) {

    try {

      StringBuffer mystr = new StringBuffer();
      String kk,vv;
      for (Enumeration e = inHash.keys() ; e.hasMoreElements() ;) {
        kk =  e.nextElement().toString();
        vv = inHash.get(kk).toString();
        if ( ( mypass != null ) &&
             mypass.contains(kk) )
          vv = "";
        mystr.append(LanguageTools.tr(myRequest,kk));
        mystr.append(inSplit);
        mystr.append(LanguageTools.tr(myRequest,vv));
        mystr.append(inSplit);
      }
      return mystr.toString();
    }
    catch (Exception ex) {
      return null;
    }

  }

  /**
   * �����ǰ��������ԡ����ڵ��Գ���
   * @param inRequest ��ǰ����
   * @param out ��ǰ���
   * @param outSessionAttributes �Ƿ����Session����
   * @param outContextAttributes �Ƿ����Context����
   */
  public static void outRequestAttributes(HttpServletRequest inRequest,
      PrintWriter out,
      boolean outSessionAttributes,
      boolean outContextAttributes) {

    try {
      String mys;
      Enumeration myk;
      out.println("Request Attributes ========= <BR>");
      myk =  inRequest.getAttributeNames();
      for (; myk.hasMoreElements();) {
        mys = myk.nextElement().toString();
        out.println(mys + " ---- " + inRequest.getAttribute(mys) + "<BR>");
      }
      out.println("=================================<BR>");

      if (outSessionAttributes ) {
        out.println("Session Attributes ========= <BR>");
        myk =  inRequest.getSession().getAttributeNames();
        for (; myk.hasMoreElements();) {
          mys = myk.nextElement().toString();
          out.println(mys + " ---- " + inRequest.getSession().getAttribute(mys) + "<BR>");
        }
        out.println("=================================<BR>");
      }

      if ( outContextAttributes ) {
        out.println("Context Attributes ========= <BR>");
        myk =  inRequest.getSession().getServletContext().getAttributeNames();
        for (; myk.hasMoreElements();) {
          mys = myk.nextElement().toString();
          out.println(mys + " ---- " + inRequest.getSession().getServletContext().getAttribute(mys) + "<BR>");
        }
        out.println("=================================<BR>");
      }

    }
    catch (Exception ex) {
    }

  }

  /**
   * ��õ�ǰ�û����µĸ�����Ϣ
   * @param inRequest ��ǰ����
   * @return ��ǰ�û����µĸ�����Ϣ
   */
  public static ArrayList getNewPM(HttpServletRequest inRequest) {
    return getNewPM(inRequest,
                    ServletTools.getSessionUser(inRequest));
  }

  /**
   * ����û����µĸ�����Ϣ
   * @param inRequest ��ǰ����
   * @param myuser �û���
   * @return �û����µĸ�����Ϣ
   */
  public static ArrayList getNewPM(HttpServletRequest inRequest,
                               String myuser) {
    try {
      if ( myuser == null ) return null;
      Hashtable myw = new Hashtable();
      myw.put("receiver", myuser);
      myw.put("have_read",LanguageTools.atr(inRequest,"no"));
      ArrayList myconds =  new ArrayList();
      myconds.add(myw);
      DataReader mydataReader =
          DataManagerTools.getDataReader( inRequest,
          DataManager.PERSONAL_INFORMATION,null,1,-1,myconds);
      if ( mydataReader == null  ) return null;
      return mydataReader.getRecords();
    }
    catch (Exception ex) {
      return null;
    }

  }

  /**
   * ��õ�ǰ�û����µĸ�����Ϣ�ĸ���
   * @param inRequest ��ǰ����
   * @return ��ǰ�û����µĸ�����Ϣ�ĸ���
   */
  public static int getNewPMSize(HttpServletRequest inRequest) {
      return getNewPMSize(inRequest,
                          ServletTools.getSessionUser(inRequest));
  }

  /**
   * ����û����µĸ�����Ϣ�ĸ���
   * @param inRequest ��ǰ����
   * @param myuser �û���
   * @return �û����µĸ�����Ϣ�ĸ���
   */
  public static int getNewPMSize(HttpServletRequest inRequest,
                                 String myuser) {
    ArrayList mypm = getNewPM(inRequest,myuser);
    if ( mypm == null )
      return 0;
    else
      return mypm.size();
  }

}