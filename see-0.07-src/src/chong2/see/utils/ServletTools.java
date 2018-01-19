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
 * <p>Title: 软件工程管理环境</p>
 * <p>Description: 为软件研发提供管理数据和任务的平台</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: 虫二</p>
 *
 * <p>servlet中常用的静态方法。
 *
 * @author 玛瑞
 * @version 0.07
 */

final public class ServletTools {

  /**
   * servlet中应用的常数
   */
//  public static String FILE_PREFIX  = "/WEB-INF/";
//  public static String IMAGE_PATH =  "image/";
//  public static String SCRIPT_PATH =  "script/";

/**
 * 缺省界面风格
 */
  public static String DEFAULT_INTERFACE_STYLE =  "style_default" ;

  /**
   * 缺省界面方案
   */
  public static String DEFAULT_INTERFACE_THEME =  "theme_default" ;

  /**
   * 缺省页面数据个数
   */
  public static int DEFAULT_PAGESIZE =  8 ;

  /**
   * 用户数据中必须过滤的非法字符
   */
  public static String INVAILD_PARAMETER_CHARS =  "";


 /*  ===================== 通用方法 ==================== */


  public static boolean  isFile(HttpServletRequest myRequest,
      String infile) {
      if ( infile == null ) return false;
      String myfile = getFileRealPath(myRequest,infile);
      if ( myfile == null ) return false;
      return CommonTools.isFile(myfile);
  }



/**
 * 获得目录下的所有文件名
 * @param myRequest 当前请求
 * @param indir 目录名
 * @return 目录下的所有文件名
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
   * 获得一个目录下所有文件
   * @param myRequest 当前请求
   * @param indir 目录名（相对路径）
   * @return 目录下所有文件
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


  /*  ===================== 初始化配置方法 ==================== */

  /**
   * 读取初始化文件，在应用环境下保存系统的初始化配置参数。
   * @param myContext 当前应用环境
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
   * 获取整个应用的初始配置参数。
   * @param myContext 当前应用环境
   * @return 应用的初始配置参数
   */
  public static Hashtable getInitConfig(ServletContext myContext) {
    return (Hashtable)(myContext.getAttribute(DataManager.INIT_CONFIG));
  }

  /**
   * 获取整个应用的一个初始配置参数。
   * @param myContext 当前应用环境
   * @param inKey 参数名
   * @return 参数值
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
   * 获取整个应用的一个初始配置参数。
   * @param myRequest 当前请求
   * @param inKey 参数名
   * @return 参数值
   */
  public static String getInitConfig(HttpServletRequest myRequest,
                                     String inKey) {
    return getInitConfig(getContext(myRequest),inKey);
  }

  /**
   * 获取整个应用的缺省用户名
   * @param myRequest 当前请求
   * @return 整个应用的缺省用户名
   */
  public static String getAppDefaultUser(HttpServletRequest myRequest) {
    return LanguageTools.atr(myRequest, getInitConfig(myRequest,"default_user"));
  }

  /**
   * 获取整个应用的缺省编码
   * @param myRequest 当前请求
   * @return 整个应用的缺省编码
   */
  public static String getAppDefaultEncoding(HttpServletRequest myRequest) {
    return getAppDefaultEncoding(getContext(myRequest));
  }

  /**
   * 获取整个应用的缺省编码
   * @param myContext 当前应用环境
   * @return 整个应用的缺省编码
   */
  public static String getAppDefaultEncoding(ServletContext myContext) {
//    return getInitConfig(myContext,"default_encoding");
    // 不允许动态修改系统编码方式！没必要，且危险！
    String myenc =
        myContext.getAttribute("defaultEncoding").toString();
    return Language.getVaildEncoding(myenc) ;
  }

  /**
   * 获取整个应用的缺省编码字符集
   * @param myContext 当前应用环境
   * @return 整个应用的缺省编码字符集
   */
  public static String getAppDefaultCharset(ServletContext myContext) {
    String myenc = getAppDefaultEncoding(myContext);
    return Language.getCharset(myenc);
  }

  /**
   * 获取整个应用的缺省编码字符集
   * @param myRequest 当前请求
   * @return 整个应用的缺省编码字符集
   */
  public static String getAppDefaultCharset(HttpServletRequest myRequest) {
    String myenc = getAppDefaultEncoding(myRequest);
    return Language.getCharset(myenc);
  }


  /**
   * 获取整个应用的缺省界面风格
   * @param myRequest 当前请求
   * @return 整个应用的缺省界面风格
   */
  public static String getAppDefaultInterfaceStyle(HttpServletRequest myRequest) {
    return getInitConfig(getContext(myRequest),
                         "default_interface_style");
  }

  /**
   * 获取整个应用的缺省界面方案
   * @param myRequest 当前请求
   * @return 整个应用的缺省界面方案
   */
  public static String getAppDefaultInterfaceTheme(HttpServletRequest myRequest) {
    return getInitConfig(getContext(myRequest),
                         "default_interface_theme");
  }

  /**
   * 获取整个应用的数据审计类型
   * @param myRequest 当前请求
   * @return 整个应用的数据审计类型
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
   * 获取整个应用的缺省上传大小（KB）
   * @param myRequest 当前请求
   * @return 整个应用的缺省上传大小
   */
  public static int getAppDefaultUploadSize(HttpServletRequest myRequest) {
    String mysize = getInitConfig(getContext(myRequest),
                                  "default_upload_size");
    if ( mysize == null ) return -1;
    return CommonTools.stringToInt(mysize);
  }

  /**
   * 获取整个应用的缺省可上传空间（KB）
   * @param myRequest 当前请求
   * @return 整个应用的缺省可上传空间
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



/*  ===================== session相关的方法 ==================== */

/**
 * 获取session保存的参数值
 * @param myRequest 当前请求
 * @param inKey 参数名
 * @return 参数值。若无则返回null。
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
   * 设置session的参数值
   * @param myRequest 当前请求
   * @param inKey 参数名
   * @param inValue 参数值
   */
  public static void setSessionAttr(HttpServletRequest myRequest,
                                    String inKey, Object inValue) {

    myRequest.getSession(true).setAttribute(inKey,inValue);
  }

  /**
   * 设置当前用户的缺省session参数值
   * @param inRequest 当前请求
   * @param myuser 当前用户的参数名-值对
   * @return 是否成功
   */
  public static boolean setSessionUser(HttpServletRequest inRequest,
                                       Hashtable myuser) {
    if (  myuser == null ) return false;
    if ( myuser.get("id") == null ) return false;

    //session用户名
    String myid = myuser.get("id").toString();
    setSessionAttr(inRequest,"sessionUser",myid );

    //session用户昵称
    String mynick = myuser.get("id").toString();
    if ( myuser.get("nickname") != null )
      mynick = myuser.get("nickname").toString();
    setSessionAttr(inRequest,"sessionNick",mynick);

    //session用户界面风格
    String mystyle;
    if ( myuser.get("interface_style") != null )
      mystyle = myuser.get("interface_style").toString();
    else
      mystyle = getAppDefaultInterfaceStyle(inRequest);
    if ( mystyle == null ) mystyle = DEFAULT_INTERFACE_STYLE;
    setSessionAttr(inRequest,"sessionInterfaceStyle",mystyle);

    //session用户界面方案
    String mytheme;
    if ( myuser.get("interface_theme") != null )
      mytheme = myuser.get("interface_theme").toString();
    else
      mytheme = getAppDefaultInterfaceTheme(inRequest);
    if ( mytheme == null ) mytheme = DEFAULT_INTERFACE_THEME;
    setSessionAttr(inRequest,"sessionInterfaceTheme",mytheme);

    //session用户数据显示风格
    String mydatastyle = DataManager.DEFAULT_DATA_STYLE;
    if ( myuser.get("datalist_style") != null )
      mydatastyle = myuser.get("datalist_style").toString();
    setSessionAttr(inRequest,"sessionDatalistStyle",mydatastyle);

    //session用户缺省项目名
    String myproject = "";
    if ( myuser.get("default_project") != null )
      myproject = myuser.get("default_project").toString();
    setSessionAttr(inRequest,"sessionProject",myproject);

    // 不允许用户设置编码方式！没必要，且危险！
//    String mylang = Constants.DEFAULT_LANGUAGE;
//    if ( myuser.get("language") != null )
//      mylang = myuser.get("language").toString();
//    setSessionAttr(inRequest,"sessionEncoding",mylang);

    //session用户界面数据页大小值
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
   * 设置当前用户的缺省session参数值。首先检查是否为合法用户。
   * @param myRequest 当前请求
   * @param myid 用户名
   * @param mypass 用户口令
   * @return 是否成功
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
   * 设置当前用户的缺省session参数值
   * @param myRequest 当前请求
   * @param myid 当前用户名
   * @return 是否成功
   */
  public static boolean setSessionUser(HttpServletRequest myRequest,
                                       String myid) {
    return setSessionUser(myRequest,
                          AclTools.getUser(myRequest, myid));
  }

  /**
   * 设置session缺省页大小值。尝试各种方式，优先顺序为：用户输入、
   * session已保存值、应用缺省值、程序缺省值
   * @param inRequest 当前请求
   * @return 已设置的session缺省页大小值
   */
  public static int setSessionPageSize(HttpServletRequest inRequest) {
    try {
      int  mypagesize = CommonTools.WRONG_INT;
      //用户输入值
      if ( getParameter(inRequest,"input_page_size") != null )
        mypagesize =
        CommonTools.stringToInt(getParameter(inRequest,"input_page_size"));
      //当前session的取值
      if ( mypagesize == CommonTools.WRONG_INT)
        mypagesize =
        CommonTools.stringToInt(getSessionPageSize(inRequest));
      //应用缺省值
      if ( mypagesize == CommonTools.WRONG_INT)
        mypagesize =
        CommonTools.stringToInt(getInitConfig(inRequest,"default_page_size"));
      //程序缺省值
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
   * 设置session缺省数据显示风格。尝试各种方式，优先顺序为：用户输入、
   * session已保存值、程序缺省值
   * @param inRequest 当前请求
   * @param inname 用户输入控件的名字
   * @return 已设置的session缺省数据显示风格
   */
  public static String setSessionDatalistStyle(HttpServletRequest inRequest,
      String inname) {
    try {
      //用户输入值
      String mystyle = getParameter(inRequest,inname);
      ArrayList allstyles = LanguageTools.atr(inRequest,
          DataManager.DATALIST_STYLE);
      //原来的值
      if ( ( mystyle == null ) || "".equals(mystyle) ||
           !allstyles.contains(mystyle))
        mystyle = getSessionDatalistStyle(inRequest);
      //程序缺省值
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
   * 设置session缺省界面风格。尝试各种方式，优先顺序为：用户输入、
   * session已保存值、程序缺省值
   * @param inRequest 当前请求
   * @return 已设置的session缺省界面风格
   */
  public static String setSessionInterfaceStyle(HttpServletRequest inRequest) {
    try {
      //用户输入值
      String mystyle = getParameter(inRequest,"input_interface_style");
      //原来的值
      if ( ( mystyle == null ) || "".equals(mystyle) )
        mystyle = getSessionInterfaceStyle(inRequest);
      //程序缺省值
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
   * 设置session缺省界面方案。尝试各种方式，优先顺序为：用户输入、
   * session已保存值、程序缺省值
   * @param inRequest 当前请求
   * @return 已设置的session缺省界面方案
   */
  public static String setSessionInterfaceTheme(HttpServletRequest inRequest) {
    try {
      //用户输入值
      String mytheme = getParameter(inRequest,"input_interface_theme");
      //原来的值
      if ( ( mytheme == null ) || "".equals(mytheme) )
        mytheme = getSessionInterfaceTheme(inRequest);
      //程序缺省值
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
   * 设置session缺省项目值。尝试各种方式，优先顺序为：用户输入、
   * session已保存值
   * @param inRequest 当前请求
   * @return 已设置的session缺省项目值
   */
  public static String setSessionProject(HttpServletRequest inRequest) {
    try {
      //用户输入值
      String myproject = getParameter(inRequest,"input_default_project");
      //原来的值
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
   * 获取当前session用户名
   * @param myRequest 当前请求
   * @return 当前session用户名
   */
  public static String getSessionUser(HttpServletRequest myRequest) {
    return getSessionAttr(myRequest,"sessionUser");
  }

  /**
   * 获取当前session用户昵称名
   * @param myRequest 当前请求
   * @return 当前session用户昵称名
   */
  public static String getSessionNick(HttpServletRequest myRequest) {
    return getSessionAttr(myRequest,"sessionNick");
  }

  /**
   * 获取当前session界面风格
   * @param myRequest 当前请求
   * @return 当前session界面风格
   */
  public static String getSessionInterfaceStyle(HttpServletRequest myRequest) {
    return getSessionAttr(myRequest,"sessionInterfaceStyle");
  }

  /**
   * 获取当前session界面方案
   * @param myRequest 当前请求
   * @return 当前session界面方案
   */
  public static String getSessionInterfaceTheme(HttpServletRequest myRequest) {
    return getSessionAttr(myRequest,"sessionInterfaceTheme");
  }

  /**
   * 获取当前session数据显示风格
   * @param myRequest 当前请求
   * @return 当前session数据显示风格
   */
  public static String getSessionDatalistStyle(HttpServletRequest myRequest) {
    return getSessionAttr(myRequest,"sessionDatalistStyle");
  }

  /**
   * 获取当前session编码
   * @param myRequest 当前请求
   * @return 当前session编码
   */
  public static String getSessionEncoding(HttpServletRequest myRequest) {
    String myenc = getSessionAttr(myRequest,"sessionEncoding");
    return Language.getVaildEncoding(myenc) ; //需要筛除非法取值
  }

  /**
   * 获取当前session编码的字符集
   * @param myRequest 当前请求
   * @return 当前session编码的字符集
   */
  public static String getSessionCharset(HttpServletRequest myRequest) {
    String myenc = getSessionEncoding(myRequest);
    return Language.getCharset(myenc);
  }

  /**
   * 获取当前session项目名
   * @param myRequest 当前请求
   * @return 当前session项目名
   */
  public static String getSessionProject(HttpServletRequest myRequest) {
    String myp = getSessionAttr(myRequest,"sessionProject");
    if ( myp == null )
      return ""; //避免返回空值
    else
      return getSessionAttr(myRequest,"sessionProject");
  }

  /**
   * 获取当前session页大小值
   * @param myRequest 当前请求
   * @return 当前session页大小值
   */
  public static String getSessionPageSize(HttpServletRequest myRequest) {
    return getSessionAttr(myRequest,"sessionPageSize");
  }

  /**
   * 设置session编码值
   * @param myRequest 当前请求
   * @param inEnc 编码值
   * @return 是否设置成功
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
   * 获取当前请求的应用环境值
   * @param myRequest 当前请求
   * @return 当前请求的应用环境值
   */
  public static ServletContext getContext(HttpServletRequest myRequest) {
    return myRequest.getSession(true).getServletContext();
  }

  /*  ===================== 通用方法 ==================== */

  /**
   * 获取文件的真实路径（绝对路径）
   * @param myRequest 当前请求
   * @param myfile 文件名，相对路径，即应用访问环境下的路径
   * @return 文件的真实路径（绝对路径）
   */
  public static String getFileRealPath(HttpServletRequest myRequest,
                                       String myfile) {

    return  getContext(myRequest).getRealPath(myfile);
  }

  /**
   * 获取文件的真实路径（绝对路径）
   * @param myContext 当前应用环境
   * @param myfile 文件名，相对路径，即应用访问环境下的路径
   * @return 文件的真实路径（绝对路径）
   */
  public static String getFileRealPath(ServletContext myContext,
                                       String myfile) {
    return myContext.getRealPath(myfile);
  }


  /**
   * 过滤页面参数值
   * @param myvalue 参数值
   * @return 过滤后的参数值
   */
  public static String filterParameter(String myvalue) {
    try {
      String myv = new String(myvalue);
      //空字符串也返回null
      if ( ( myv == null ) || "".equals(myv ))
        return null;
      // 非法字符直接删除
      myv = myv.replaceAll(INVAILD_PARAMETER_CHARS,"");
      // 用户数据中需要转换存放的特殊字符，它们可能使xml或网页处理混乱。
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
   * 获得页面的参数值。需要过滤非法字符、替换特殊字符。
   * @param myRequest 当前请求
   * @param myname 参数名
   * @return 参数值
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
      /* 这里注意：通过get方式提交的参数似乎无法自动接受filter中编码的设置。
      所以只能如此转换
      */
//      if ( "GET".equals(myRequest.getMethod())) {
//        myv = new String(myv.getBytes(getAppDefaultEncoding(myRequest)));
//        myv = new String(myv.getBytes("iso-8859-1"));
//        myv = new String(myv.getBytes("UTF-8"));
//      }
      /* 问题仍得不到解决。现在避免采用get方式。
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
   * 获得页面的多值参数的值。
   * @param myRequest 当前请求
   * @param myname 参数名
   * @return 参数值
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
   * 获得页面中所有参数的值。
   * @param inRequest 当前请求
   * @param mystrucReader 信息结构定义
   * @param myop 当前操作
   * @return 页面中所有参数的值
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
   * 获得页面中所有参数的值。
   * @param inRequest 当前请求
   * @param itemnames 信息结构定义
   * @param mypass 信息结构定义
   * @param mytypes 数据值约束类型的定义
   * @param myop 当前操作
   * @return 页面中所有参数的值
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
   * 获得自动取值
   * @param inRequest 当前请求
   * @return 自动取值。采用：当前时间-当前用户，以保证其唯一性。
   */
  public static String getAutoValue(HttpServletRequest inRequest) {
    return getCurrentTime(inRequest)  +
        "-" + ServletTools.getSessionUser(inRequest);
  }

  /**
   * 获得客户端网络地址
   * @param inRequest 当前请求
   * @return 客户端网络地址
   */
  public static String  getHost(HttpServletRequest inRequest) {
    return inRequest.getRemoteHost();
  }

  /**
   * 获得当前日期。按应用编码的格式取值。
   * @param inRequest 当前请求
   * @return 当前日期，按应用编码的格式取值。
   */
  public static String getCurrentDate(HttpServletRequest inRequest) {
    return CommonTools.getCurrentDate(ServletTools.getAppDefaultEncoding(inRequest));
  }

  /**
   * 获得当前时间。按应用编码的格式取值。
   * @param inRequest 当前请求
   * @return 当前时间，按应用编码的格式取值。
   */
  public static String getCurrentTime(HttpServletRequest inRequest) {
    return CommonTools.getCurrentTime(ServletTools.getAppDefaultEncoding(inRequest));
  }

  /**
   * 获得所有的数据审计类型
   * @return 所有的数据审计类型
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
   * 获得所有的操作类型
   * @param isSuperseer 是否是超级用户
   * @return 所有的数据审计类型
   */
  public static ArrayList getAllOpearations(boolean isSuperseer) {
    ArrayList myd = new ArrayList();
    myd.addAll(DataStructure.DATASET_OPERATION_TYPES);
    myd.remove("help");
    if ( !isSuperseer ) { //只有超级用户才可以决定配置和审计的权限
      myd.remove("config");
      myd.remove("audit");
    }
    myd.add("select");
    return myd;
  }

  /**
   * 获得系统中所有信息结构的名称。由系统自动根据目录下文件名判断。
   * @param myRequest 当前请求
   * @param isSuperseer 是否是超级管理员
   * @return 系统中所有信息结构的名称
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
   * 获取图片的页面路径
   * @param myRequest 当前请求
   * @param inName 图片名
   * @return 图片的页面路径
   */
  public static String getImageName(HttpServletRequest myRequest,
                                    String inName) {
    return getImageName(ServletTools.getSessionEncoding(myRequest),
                        ServletTools.getSessionInterfaceStyle(myRequest),
                        inName);
  }

  /**
   * 获取图片的页面路径
   * @param inEnc 编码
   * @param inStyle 风格
   * @param inName 图片名
   * @return 图片的页面路径
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
   * 获取一组图片的页面路径
   * @param myRequest 当前请求
   * @param inNames 图片名
   * @return 图片的页面路径
   */
  public static ArrayList getImageNames(HttpServletRequest myRequest,
                                        ArrayList inNames) {
    return getImageNames(ServletTools.getSessionEncoding(myRequest),
                         ServletTools.getSessionInterfaceStyle(myRequest),
                         inNames);
  }

  /**
   * 获取一组图片的页面路径
   * @param inEnc 编码
   * @param inStyle 风格
   * @param inNames 图片名
   * @return 图片的页面路径
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
   * 获取点击脚本函数名
   * @param inName 点击名
   * @return 脚本函数名
   */
  public static String getClick(String inName) {
    return inName + "()";
  }

  /**
   * 获取一组点击的脚本函数名
   * @param inNames 点击名
   * @return 脚本函数名
   */
  public static ArrayList getClicks(ArrayList inNames) {
    ArrayList myimg = new ArrayList();
    for (int i=0; i < inNames.size(); i++) {
      myimg.add(getClick(inNames.get(i).toString()));
    }
    return myimg;
  }


/*  ===================== 处理xml数据的方法 ==================== */



/**
 * 字符串转换为hash数据。翻译其中的名。
 * @param myRequest 当前请求
 * @param inStr 字符串
 * @param inSplit 分割符
 * @return 转换后的hash数据
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
   * hash数据转换为字符串。翻译其中的名。
   * @param myRequest 当前请求
   * @param inHash hash数据
   * @param inKeys 名表
   * @param inSplit 分割符
   * @return 转换后的字符串
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
   * hash数据转换为字符串。翻译其中的名。
   * @param myRequest 当前请求
   * @param inHash hash数据
   * @param inSplit 分割符
   * @param mypass 口令域，不允许保存和显示
   * @return 转换后的字符串
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
   * 输出当前请求的属性。用于调试程序。
   * @param inRequest 当前请求
   * @param out 当前输出
   * @param outSessionAttributes 是否输出Session属性
   * @param outContextAttributes 是否输出Context属性
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
   * 获得当前用户的新的个人消息
   * @param inRequest 当前请求
   * @return 当前用户的新的个人消息
   */
  public static ArrayList getNewPM(HttpServletRequest inRequest) {
    return getNewPM(inRequest,
                    ServletTools.getSessionUser(inRequest));
  }

  /**
   * 获得用户的新的个人消息
   * @param inRequest 当前请求
   * @param myuser 用户名
   * @return 用户的新的个人消息
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
   * 获得当前用户的新的个人消息的个数
   * @param inRequest 当前请求
   * @return 当前用户的新的个人消息的个数
   */
  public static int getNewPMSize(HttpServletRequest inRequest) {
      return getNewPMSize(inRequest,
                          ServletTools.getSessionUser(inRequest));
  }

  /**
   * 获得用户的新的个人消息的个数
   * @param inRequest 当前请求
   * @param myuser 用户名
   * @return 用户的新的个人消息的个数
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