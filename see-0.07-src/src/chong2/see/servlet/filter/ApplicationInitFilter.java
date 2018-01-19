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
 * <p>Title: 软件工程管理环境</p>
 * <p>Description: 为软件研发提供管理数据和任务的平台</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: 虫二</p>
 *
 * <p><strong>ApplicationFilter</strong> 是一个servlet的过滤器，用于
 * 统一设置整个应用和所有request和response的初始值。</p>
 *
 * <p>应用中所有初始值均被保存在一个XML文件中。此文件名应是此filter的初始参数。
 *
 * @author 玛瑞
 * @version 0.07
 */

public class ApplicationInitFilter implements Filter {


  /**
   * filter配置对象。这是标准实现。
   */
  protected FilterConfig filterConfig = null;

  // ------------ 公用方法（标准，被替换） ---------

  /**
   * 初始化设置。只用在servlet包容器装载此filter时，才调用它。
   * 首先从filter初始参数中获取配置文件的名，然后从配置文件中读取配置参数，存入
   * 整个应用的运行配置中。
   *
   * @param inConfig servlet包容器提供的配置对象
   * @throws ServletException servlet异常
   */
  public void init(FilterConfig inConfig)
      throws ServletException {

    this.filterConfig = inConfig;
    ServletContext mycontext = inConfig.getServletContext();
    ServletTools.setInitConfig(mycontext);
    LanguageTools.setUserLangauge(mycontext);
  }

  /**
   * 统一设置所有request和response的初始值。包括缺省用户、口令、界面风格
   *
   * @param inRequest 正在处理的servlet的request
   * @param inResponse 正在处理的servlet的response
   * @param inChain 正在处理的filter链
   * @throws IOException IO异常
   * @throws ServletException servlet异常
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
          if ( duser == null) { // 删除登录cookie
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
   * 标准方法。
   */
  public void destroy() {

    this.filterConfig = null;

  }

}