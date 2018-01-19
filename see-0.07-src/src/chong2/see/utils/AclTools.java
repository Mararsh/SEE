package chong2.see.utils;

import chong2.see.data.base.Constants;
import chong2.see.servlet.common.DataManager;
import chong2.see.xml.DataStructureXmlParser;
import chong2.see.data.DataReader;
import chong2.see.xml.DataXmlWriter;

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
 * 与权限管理相关的静态方法
 *
 * @author 玛瑞
 * @version 0.07
 */

final public class AclTools {

  /**
   * 检查权限
   * @param myRequest 当前请求
   * @param myset 数据
   * @param mystrucReader 数据结构
   * @param myuser 用户
   * @param myop 操作
   * @return 是否合法
   */
  public static boolean checkAcl(HttpServletRequest myRequest,
                                 String myset,
                                 DataStructureXmlParser mystrucReader,
                                 String myuser,
                                 String myop) {
    try {

      // 不允许修改预定义数据的结构！（超级管理员也不行）
      if ( "config".equals(myop) &&
           DataManager.PREDEFINED_DATA.contains(myset))
        return false;

//    //不允许任何人，包括超级管理员，删除、查询别人的个人消息。
//    //但是这样会有乱发消息乱上传的危险！！
//      if (  isSuperseer(myRequest,myuser) &&
//            !DataManager.PERSONAL_INFORMATION.equals(myset) )
//      return true;

      //对超级用户完全放行
      if (  isSuperseer(myRequest,myuser)  )
        return true;

      if ( ( myop == null ) || "".equals(myop) ) return true;
      if ( "help".equals(myop) ) return true;
      if ( ( myuser == null ) || "".equals(myuser) ) return false;
      if ( ( myset == null ) || "".equals(myset) ) return false;

      ArrayList setops = mystrucReader.getOperations();
      if ( !"config".equals(myop) &&
           ( setops != null ) &&
           !setops.contains(myop) )
        return false;

      Hashtable myops = null;
      if ( "audit".equals(myop) &&
           DataManager.DATA_AUDIT.equals(myset) ) {
        String mytset = ServletTools.getParameter(myRequest,"myauditshow");
        if ( mytset == null)
          mytset = ServletTools.getParameter(myRequest,"mytrueset");
        if ( mytset != null)
          myops = getUserOperations(myRequest,myuser,mytset);
      }
      if ( myops == null)
        myops = getUserOperations(myRequest,myuser,myset);
      if ( myops == null ) return false;
      String myopwhere = "";
      String optr = LanguageTools.atr(myRequest,myop);
      if ( ( myops.get("*") == null) &&
           ( myops.get(optr) == null ) )
        return false;
      if ( myops.get(optr) != null)
        myopwhere= myops.get(optr).toString();
      else
        myopwhere= myops.get("*").toString();
      if ( "".equals(myopwhere) ) return true;
      return CommonTools.isValidCondition(DataManagerTools.getMultiWhere(myRequest),
          DataManagerTools.getMultiWhere(myRequest,myopwhere),
          mystrucReader.getItemTypes(),
          ServletTools.getAppDefaultEncoding(myRequest));
//      Hashtable myopw = DataManagerTools.getWhere(myRequest,myopwhere);
//      return CommonTools.isValidHash(DataManagerTools.getWhere(myRequest),
//                                     myopw,
//                                     mystrucReader.getItemTypes(),
//                                     ServletTools.getAppDefaultEncoding(myRequest));
    }
    catch (Exception ex) {
      return false;
    }
  }

  /**
   * 获得用户的角色
   * @param myRequest 当前请求
   * @param mytype 角色类型。区分系统角色和数据角色。
   * @return 用户的角色
   */
  public static ArrayList getUserRoles(HttpServletRequest myRequest,
                                       String mytype) {
    return getUserRoles(myRequest,
                        ServletTools.getSessionUser(myRequest),
                        mytype);
  }

  /**
   * 获得用户的角色
   * @param myRequest 当前请求
   * @param myuser 用户名
   * @param mytype 角色类型。区分系统角色和数据角色。
   * @return 用户的角色
   */
  public static ArrayList getUserRoles(HttpServletRequest myRequest,
                                       String myuser,
                                       String mytype) {
    ArrayList myroles = new ArrayList();
    if ( isSuperseer(myRequest,myuser) ) {
      myroles.add("*");
      return myroles;
    }
    try {
      if ( ( myuser == null ) || "".equals(myuser) )
        return myroles;
      DataReader myreader =
          DataManagerTools.getDataReader(myRequest,mytype,
          null,0,-1, null);
      ArrayList myacls = myreader.getRecords();
      Hashtable acl ;
      String myrole;
      ArrayList mymembers;
      String myguest = ServletTools.getAppDefaultUser(myRequest);
      for (int i=0; i< myacls.size(); i++) {
        acl = (Hashtable)myacls.get(i);
        if ( (acl.get("id") == null) ||
             (acl.get("member") == null ) )
          continue;
        myrole = acl.get("id").toString();
        if ( myrole == null ) continue;
        mymembers = CommonTools.stringToArray(acl.get("member").toString(),
            DataReader.LIST_SPLITTER);
        if ( myguest.equals(myuser) )
          if ( !mymembers.contains(myuser) )
            continue;
        if ( mymembers.contains("*") ||
             mymembers.contains(myuser) )
          myroles.add(myrole);
      }
      return myroles;
    }
    catch (Exception ex) {
      return myroles;
    }
  }

  /**
   * 获得用户可执行的操作
   * @param myRequest 当前请求
   * @return 用户可执行的操作
   */
  public static Hashtable getUserOperations(HttpServletRequest myRequest) {
    Hashtable myops = getUserOperations(myRequest,
                                        ServletTools.getSessionUser(myRequest),
                                        ServletTools.getParameter(myRequest,"myset"));
    return myops;
  }

  /**
   * 获得用户可执行的操作
   * @param myRequest 当前请求
   * @param myuser 用户
   * @param myset 数据
   * @return 用户可执行的操作
   */
  public static Hashtable getUserOperations(HttpServletRequest myRequest,
      String myuser,
      String myset) {
    Hashtable myops = new Hashtable();
    Hashtable myrops;
    if ( ServletTools.getAppDefaultUser(myRequest).equals(myuser))
      if ( DataManager.SUPER_DATA.contains(myset) ||
           DataManager.SYSTEM_DATA.contains(myset) )
        return myops;
      //数据角色权限
      ArrayList myroles = getUserRoles(myRequest,myuser,DataManager.DATA_ROLE);
      String mykey;
      for (int i=0; i< myroles.size(); i++) {
        myrops = getOperations(myRequest,myroles.get(i).toString(),
        myset,DataManager.DATA_ROLE);
        for (Enumeration e=myrops.keys(); e.hasMoreElements();) {
          mykey = e.nextElement().toString();
          if ( ( myops.get(mykey) == null) ||
               "".equals(myrops.get(mykey)) )
            myops.put(mykey, myrops.get(mykey));
        }
      }
      //用户权限
      myrops = getOperations(myRequest,myuser,myset,DataManager.USER);
      for (Enumeration e=myrops.keys(); e.hasMoreElements();) {
        mykey = e.nextElement().toString();
        if ( ( myops.get(mykey) == null) ||
             "".equals(myrops.get(mykey)) )
          myops.put(mykey, myrops.get(mykey));
      }
      //系统角色权限
      myroles = getUserRoles(myRequest,myuser,DataManager.SYSTEM_ROLE);
      for (int i=0; i< myroles.size(); i++) {
        myrops = getOperations(myRequest,myroles.get(i).toString(),
        myset,DataManager.SYSTEM_ROLE);
        for (Enumeration e=myrops.keys(); e.hasMoreElements();) {
          mykey = e.nextElement().toString();
          if ( ( myops.get(mykey) == null) ||
               "".equals(myrops.get(mykey)) )
            myops.put(mykey, myrops.get(mykey));
        }
      }
      return myops;
  }

  /**
   * 获得角色可执行的操作
   * @param myRequest 当前请求
   * @param myrole 角色
   * @param myset 数据
   * @return 角色可执行的操作
   */
  public static Hashtable getDataRoleOperations(HttpServletRequest myRequest,
      String myrole,
      String myset) {
    Hashtable myops =
        getOperations(myRequest,myrole,myset,DataManager.DATA_ROLE);
    return myops;
  }

  /**
   * 获得系统角色可执行的操作
   * @param myRequest 当前请求
   * @param myrole 角色
   * @param myset 数据
   * @return 系统角色可执行的操作
   */
  public static Hashtable getSystemRoleOperations(HttpServletRequest myRequest,
      String myrole,
      String myset) {
    Hashtable myops =
        getOperations(myRequest,myrole,myset,DataManager.SYSTEM_ROLE);
    return myops;
  }

  /**
   * 获得用户/角色可执行的操作
   * @param myRequest 当前请求
   * @param myuser 用户/角色
   * @param myset 数据
   * @param mytype 用户/角色的类型
   * @return 用户/角色可执行的操作
   */
  public static Hashtable getOperations(HttpServletRequest myRequest,
                                        String myuser,
                                        String myset,
                                        String mytype) {

    Hashtable myops = new Hashtable();
//    //不允许任何人，包括超级管理员，删除、查询别人的个人消息。
//    if ( DataManager.PERSONAL_INFORMATION.equals(myset) ) {
//      myops.put(LanguageTools.atr(myRequest,"query"),
//                "sender:v:h-v:h:_self:m:s-m:s:receiver:v:h-v:h::c:i:d-c:i:d:_self");
//      myops.put(LanguageTools.atr(myRequest,"add"),
//                "");
//      myops.put(LanguageTools.atr(myRequest,"remove"),
//                "sender:v:h-v:h:_self");
//      return myops;
//    } //但是这样会有乱发消息乱上传的危险！！

    if ( isSuperseer(myRequest,myuser) ) {
      myops.put("*","");
      return myops;
    }

    String myguest = ServletTools.getAppDefaultUser(myRequest);
    if ( !DataManager.SYSTEM_ROLE.equals(mytype) ||
         myguest.equals(myuser)) {
      if ( DataManager.SUPER_DATA.contains(myset) ||
           DataManager.SYSTEM_DATA.contains(myset) )
        return myops;
    }
//    myops.put("help","");
    try {
      if ( ( myset == null ) || "".equals(myset) )
        return myops;
      if ( ( myuser == null ) || "".equals(myuser) )
        return myops;
      DataReader myreader;
      if ( DataManager.USER.equals(mytype) )
        myreader = DataManagerTools.getDataReader(myRequest,
            DataManager.DATA_USER_ACL, null,0,-1, null);
      else if ( DataManager.DATA_ROLE.equals(mytype) )
        myreader = DataManagerTools.getDataReader(myRequest,
            DataManager.DATA_ROLE_ACL, null,0,-1, null);
      else if ( DataManager.SYSTEM_ROLE.equals(mytype) )
        myreader = DataManagerTools.getDataReader(myRequest,
            DataManager.SYSTEM_ROLE_ACL, null,0,-1, null);
      else
        return myops;
      ArrayList myacls = myreader.getRecords();
      Hashtable acl;
      ArrayList myusers,myopps,mydata;
      String mycondition,lastcondition,trop;
      String trset = LanguageTools.atr(myRequest,myset);
      for (int i=0; i< myacls.size(); i++) {
        acl = (Hashtable)myacls.get(i);
        if ( (acl.get(mytype) == null) ||
             (acl.get("data") == null) ||
             (acl.get("operation") == null ) )
          continue;
        myusers = CommonTools.stringToArray(acl.get(mytype).toString(),
            DataReader.LIST_SPLITTER);
        if ( myusers == null ) continue;
        if ( myguest.equals(myuser) )
          if (  !myusers.contains(myuser) ) continue;
        if ( !myusers.contains("*") &&
             !myusers.contains(myuser) ) continue;
        mydata =
            CommonTools.stringToArray(acl.get("data").toString(),
            DataReader.LIST_SPLITTER);
        if ( mydata == null ) continue;
        if ( !mydata.contains("*") &&
             !mydata.contains(trset) ) continue;
        myopps = CommonTools.stringToArray(acl.get("operation").toString(),
            DataReader.LIST_SPLITTER);
        if ( myopps == null ) continue;
        if ( acl.get("condition") != null )
          mycondition = acl.get("condition").toString();
        else
          mycondition = "";
        for (int j=0; j < myopps.size(); j++) {
          trop = myopps.get(j).toString();
          if ( (myops.get(trop) == null) ||
               "".equals(mycondition) )
            myops.put(trop, mycondition);
          else
            myops.put(trop, myops.get(trop).toString() +
                     QueryTools.MULTI_SPLITTER + mycondition);
        }
      }
      return myops;
    }
    catch (Exception ex) {
      return myops;
    }
  }

  /**
   * 判断当前用户是否是超级管理员
   * @param inRequest 当前请求
   * @return 当前用户是否是超级管理员
   */
  public static boolean isSuperseer(HttpServletRequest inRequest) {
    return isSuperseer(inRequest,
                       ServletTools.getSessionUser(inRequest));
  }

  /**
   * 判断指定用户是否是超级管理员
   * @param inRequest 当前请求
   * @param myuser 用户名
   * @return 用户是否是超级管理员
   */
  public static boolean isSuperseer(HttpServletRequest inRequest,
                                    String myuser) {
    if ( ( myuser == null ) || "".equals(myuser) )
      return false;
    return myuser.equals(LanguageTools.atr(inRequest,DataManager.SUPER_SEER));
  }

  /**
   * 获得超级管理员的数据。若超级管理员不存在，则创建她。
   * 超级管理员当然是个女人啰
   * @param inRequest 当前请求
   * @return 超级管理员的数据
   */
  public static Hashtable getSuperseer(HttpServletRequest inRequest) {
    try {
      DataReader myreader =
          DataManagerTools.getDataReader(inRequest,
          DataManager.SUPER_SEER, null,0,-1, null);
      Hashtable mykey =  new Hashtable();
      mykey.put("id",
                LanguageTools.atr(inRequest,DataManager.SUPER_SEER));
      ArrayList keys = CommonTools.stringToArray("id",",");
      if ( ( myreader != null )  &&
           "yes".equals(myreader.checkExistRecord(keys,mykey)) )  {
        Hashtable mydata = myreader.getRecord(mykey);
        return mydata;
      } else   {
        mykey.put("sex",LanguageTools.atr(inRequest,"female"));
        mykey.put("birthday","2004-1-1");
        mykey.put("language","zh_CN");
        mykey.put("style","style_default");
        ArrayList myusers = myreader.getRecords();
        if ( myusers == null )  myusers = new ArrayList();
        myusers.add(0,mykey);
        String fdata =
            DataManagerTools.getDataValuesFile(inRequest,DataManager.SUPER_SEER);
        DataXmlWriter myw = new DataXmlWriter();
        String retw = myw.writeData(myusers,fdata,
                                    ServletTools.getAppDefaultCharset(inRequest));
        if ( !Constants.SUCCESSFUL.equals(retw) ) return null;
        return  mykey;
      }
    }
    catch (Exception ex) {
      return null;
    }
  }

  /**
   * 获得指定用户的数据
   * @param inRequest 当前请求
   * @param myname 用户名
   * @return 用户数据
   */
  public static Hashtable getUser(HttpServletRequest inRequest,
                                  String myname) {
    try {
      if ( myname == null ) return null;
      if ( AclTools.isSuperseer(inRequest,myname) )
        return AclTools.getSuperseer(inRequest);
      Hashtable mykey =  new Hashtable();
      DataReader myreader =
          DataManagerTools.getDataReader(inRequest,
          DataManager.USER, null,0,-1, null);
      mykey.put("id",myname);
      Hashtable mydata = myreader.getRecord(mykey);
      return mydata;
    }
    catch (Exception ex) {
      return null;
    }
  }

}