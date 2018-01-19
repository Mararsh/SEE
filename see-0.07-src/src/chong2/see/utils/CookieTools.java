package chong2.see.utils;

import java.util.Hashtable;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>Title: 软件工程管理环境</p>
 * <p>Description: 为软件研发提供管理数据和任务的平台</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: 虫二</p>
 *
 * 与cookie相关的静态方法
 *
 * @author 玛瑞
 * @version 0.07
 */

final public class CookieTools {

  /**
   * 删除登录cookie
   * @param inRequest 当前请求
   * @param inResponse 当前响应
   */
  public static void removeLoginCookie(HttpServletRequest inRequest,
                                       HttpServletResponse inResponse) {
    try {
      Cookie myloginuser =
          getCookie(inRequest,"loginuser");
      Cookie myloginpass =
          getCookie(inRequest,"loginpass");
      Cookie myloginlong =
          getCookie(inRequest,"loginlong");
      if ( myloginuser != null ) {
        myloginuser.setMaxAge(0);
        inResponse.addCookie(myloginuser);
      }
      if ( myloginlong != null ) {
        myloginlong.setMaxAge(0);
        inResponse.addCookie(myloginlong);
      }
      if ( myloginpass != null ) {
        myloginpass.setMaxAge(0);
        inResponse.addCookie(myloginpass);
      }
    }
    catch (Exception ex) {
    }
  }

  /**
   * 读取登录cookie值
   * @param inRequest 当前请求
   * @return 登录cookie值
   */
  public static Hashtable getLoginCookie(HttpServletRequest inRequest) {
    Hashtable mycookie = new Hashtable();
    try {
      Cookie myloginuser =
          getCookie(inRequest,"loginuser");
      Cookie myloginpass =
          getCookie(inRequest,"loginpass");
      Cookie myloginlong =
          getCookie(inRequest,"loginlong");
      if ( (myloginuser != null) &&
           (myloginuser.getValue() != null) ) {
        String myid =  CommonTools.hexToString(myloginuser.getValue());
        if ( myid != null )
          mycookie.put("user", myid);
      }
      if ( (myloginpass != null) &&
           (myloginpass.getValue() != null) ) {
        String mypass =  CommonTools.hexToString(myloginpass.getValue());
        if ( mypass != null )
          mycookie.put("password", mypass);
      }
      if ( (myloginlong != null) &&
           (myloginlong.getValue() != null) ) {
        String mylon = myloginlong.getValue();
        if ( mylon != null )
          mycookie.put("long", mylon);
      }
      return mycookie;
    }
    catch (Exception ex) {
      return new Hashtable();
    }
  }

  /**
   * 设置登录cookie
   * @param inRequest 当前请求
   * @param inResponse 当前响应
   * @param myuser 用户名
   * @param mypass 用户口令
   * @param mylong cookie有效时间
   * @return 是否设置成功
   */
  public static boolean setLoginCookie(HttpServletRequest inRequest,
                                       HttpServletResponse inResponse,
                                       String myuser,
                                       String mypass,
                                       int   mylong) {
    try {
      if ( (myuser == null) ||
           "".equals(myuser) )
        return false;
      if ( mylong < 0  )
        return false;
      Cookie mycookie = new Cookie("loginuser",
                                   CommonTools.stringToHex(myuser,true));
      mycookie.setMaxAge(mylong);
      inResponse.addCookie(mycookie);
      mycookie = new Cookie("loginlong",
                            CommonTools.intToString(mylong));
      mycookie.setMaxAge(mylong);
      inResponse.addCookie(mycookie);
      if ( "".equals(mypass) ||
           ( mypass == null) ) {
        Cookie myloginpass =
            getCookie(inRequest,"loginpass");
        if ( myloginpass != null) {
          myloginpass.setMaxAge(0);
          inResponse.addCookie(myloginpass);
        }
      } else {
        mycookie = new Cookie("loginpass",
                              CommonTools.stringToHex(mypass,true));
        mycookie.setMaxAge(mylong);
        inResponse.addCookie(mycookie);
      }
      return true;
    }
    catch (Exception ex) {
      return false;
    }
  }

  /**
   * 获取当前应用的一个cookie
   * @param myRequest 当前请求
   * @param myname cookie名
   * @return cookie值
   */
  public static Cookie getCookie(HttpServletRequest myRequest,
                                 String myname) {

    if ( myname == null ) return null;
    String mycookieid = null;
    if ( myRequest.getCookies() == null ) return null;
    Cookie[] mycookies = myRequest.getCookies();
    for ( int i = 0 ; i < mycookies.length ; i++) {
      if ( myname.equals(mycookies[i].getName()) )
        return mycookies[i];
    }
    return null;

  }

}