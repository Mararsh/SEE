package chong2.see.servlet.filter;

import chong2.see.utils.CookieTools;
import chong2.see.utils.LanguageTools;
import chong2.see.utils.ServletTools;

import java.io.IOException;
import java.util.Hashtable;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>Title: ������̹�����</p>
 * <p>Description: Ϊ����з��ṩ�������ݺ������ƽ̨</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: ���</p>
 *
 * <p><strong>ApplicationFilter</strong> ��һ��servlet�Ĺ�����������
 * ͳһ��������Ӧ�ú�����request��response�ĳ�ʼֵ��</p>
 *
 * <p>Ӧ�������г�ʼֵ����������һ��XML�ļ��С����ļ���Ӧ�Ǵ�filter�ĳ�ʼ������
 *
 * @author ����
 * @version 0.07
 */

public class ApplicationInitFilter implements Filter {


  /**
   * filter���ö������Ǳ�׼ʵ�֡�
   */
  protected FilterConfig filterConfig = null;

  // ------------ ���÷�������׼�����滻�� ---------

  /**
   * ��ʼ�����á�ֻ����servlet������װ�ش�filterʱ���ŵ�������
   * ���ȴ�filter��ʼ�����л�ȡ�����ļ�������Ȼ��������ļ��ж�ȡ���ò���������
   * ����Ӧ�õ����������С�
   *
   * @param inConfig servlet�������ṩ�����ö���
   * @throws ServletException servlet�쳣
   */
  public void init(FilterConfig inConfig)
      throws ServletException {

    this.filterConfig = inConfig;
    ServletContext mycontext = inConfig.getServletContext();
    ServletTools.setInitConfig(mycontext);
    LanguageTools.setUserLangauge(mycontext);
  }

  /**
   * ͳһ��������request��response�ĳ�ʼֵ������ȱʡ�û������������
   *
   * @param inRequest ���ڴ����servlet��request
   * @param inResponse ���ڴ����servlet��response
   * @param inChain ���ڴ����filter��
   * @throws IOException IO�쳣
   * @throws ServletException servlet�쳣
   */
  public void doFilter(ServletRequest  inRequest,
                       ServletResponse inResponse,
                       FilterChain inChain)
      throws IOException, ServletException {

    if (inRequest instanceof HttpServletRequest) {

      HttpServletRequest myRequest = (HttpServletRequest)inRequest;
      HttpServletResponse myResponse = (HttpServletResponse)inResponse;
      ServletContext myContext = this.filterConfig.getServletContext();

      try {
        if ( ServletTools.getSessionUser(myRequest) == null ) {
          String duser = null;
          String dpass = null;
          Hashtable mycookie = CookieTools.getLoginCookie(myRequest);
          if ( mycookie.get("user") != null )
            duser = mycookie.get("user").toString();
          if ( mycookie.get("password") != null )
            dpass = mycookie.get("password").toString();
          if ( duser == null) { // ɾ����¼cookie
            duser = ServletTools.getAppDefaultUser(myRequest);
            CookieTools.removeLoginCookie(myRequest,myResponse);
          }
          if ( duser != null) {
            boolean ok =
                ServletTools.setSessionUser(myRequest, duser,dpass);
            if ( !ok ) {
              duser = ServletTools.getAppDefaultUser(myRequest);
              CookieTools.removeLoginCookie(myRequest,myResponse);
              ServletTools.setSessionUser(myRequest, duser,dpass);
            }
          }
        }
      }
      catch (Exception ex) {
        ServletTools.setSessionUser(myRequest,
                                    ServletTools.getAppDefaultUser(myRequest));
      }


//            ((HttpServletResponse)inResponse).setHeader("Cache-Control", "no-store");
//            ((HttpServletResponse)inResponse).setHeader("Pragma", "no-cache");
//            ((HttpServletResponse)inResponse).setDateHeader("Expires", 0);

    }

    // Pass control on to the next filter
    inChain.doFilter(inRequest, inResponse);

  }

  /**
   * ��׼������
   */
  public void destroy() {

    this.filterConfig = null;

  }

}