package chong2.see.utils;

import java.util.Hashtable;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>Title: ������̹�����</p>
 * <p>Description: Ϊ����з��ṩ�������ݺ������ƽ̨</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: ���</p>
 *
 * ��cookie��صľ�̬����
 *
 * @author ����
 * @version 0.07
 */

final public class CookieTools {

  /**
   * ɾ����¼cookie
   * @param inRequest ��ǰ����
   * @param inResponse ��ǰ��Ӧ
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
   * ��ȡ��¼cookieֵ
   * @param inRequest ��ǰ����
   * @return ��¼cookieֵ
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
   * ���õ�¼cookie
   * @param inRequest ��ǰ����
   * @param inResponse ��ǰ��Ӧ
   * @param myuser �û���
   * @param mypass �û�����
   * @param mylong cookie��Чʱ��
   * @return �Ƿ����óɹ�
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
   * ��ȡ��ǰӦ�õ�һ��cookie
   * @param myRequest ��ǰ����
   * @param myname cookie��
   * @return cookieֵ
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