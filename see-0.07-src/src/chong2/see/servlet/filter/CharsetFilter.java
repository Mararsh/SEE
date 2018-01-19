package chong2.see.servlet.filter;

import chong2.see.data.base.Constants;
import chong2.see.data.base.Language;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * <p>Title: 软件工程管理环境</p>
 * <p>Description: 为软件研发提供管理数据和任务的平台</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: 虫二</p>
 *
 * <p><strong>CharacterEncodingFilter</strong> 是一个servlet的过滤器，
 * 用于统一设置所有request和response的字符编码。</p>
 *
 * <p>所有request将被设置为统一的缺省字符编码。缺省编码应是此filter的初始参数。
 * request的编码方式决定服务器解码和保存客户端传来数据的方式，而与数据显示的编码
 * 方式无必要关系。它应同时是应用中所有文件或数据库中数据的缺省编码方式。
 *
 * <p>所有response（反馈给客户端的数据）将按以下方式被编码：
 * <li>当前用户的配置，若用户已注册登录</li>
 * <li>当前request的字符编码</li>
 * <li>缺省的字符编码</li>
 *
 * <p>此filter将设置以下属性：
 * <li><strong>defaultEncoding</strong>:整个应用的缺省编码方式</li>
 * <li><strong>sessionEncoding</strong>:当前session的编码方式</li>
 * </p>
 *
 * @author 玛瑞
 * @version 0.07
 */

public class CharsetFilter implements Filter {

  /**
   * 是否忽略客户端设置的编码方式。它是此filter的初始参数
   */
  protected boolean ignoreClient = true;

  /**
   * 整个应用的缺省编码方式。
   */
  protected String defaultEncoding = Constants.DEFAULT_LANGUAGE;

  /**
   * filter配置对象。这是标准实现。
   */
  protected FilterConfig filterConfig = null;


  // ------------ 公用方法（标准，被替换） ---------

  /**
   * 初始设置。只用在servlet包容器装载此filter时，才调用它。
   * @param inConfig servlet包容器提供的配置对象
   * @throws ServletException servlet异常
   */
  public void init(FilterConfig inConfig)
      throws ServletException {

    this.filterConfig = inConfig;

    this.defaultEncoding = inConfig.getInitParameter("defaultEncoding");
    if ( this.defaultEncoding == null )
      this.defaultEncoding = getDefaultEncoding();

    ServletContext myContext = inConfig.getServletContext();
    if ( myContext.getAttribute("defaultEncoding") == null ) {
      myContext.setAttribute("defaultEncoding", this.defaultEncoding);
    }

    String value = inConfig.getInitParameter("ignoreClient");
    if (value == null)
      this.ignoreClient = true;
    else if (value.equalsIgnoreCase("true"))
      this.ignoreClient = true;
    else if (value.equalsIgnoreCase("yes"))
      this.ignoreClient = true;
    else
      this.ignoreClient = false;

  }


  /**
   * 设置所有request和response的字符编码。
   * @param inRequest 正在处理的servlet的request
   * @param inResponse 正在处理的servlet的response
   * @param inChain 正在处理的filter链
   * @exception IOException IO异常
   * @exception ServletException servlet异常
   */
  public void doFilter(ServletRequest  inRequest,
                       ServletResponse inResponse,
                       FilterChain inChain)
      throws IOException, ServletException {

    if (inRequest instanceof HttpServletRequest) {

      HttpServletRequest myreq = (HttpServletRequest)inRequest;
      HttpSession mySession = myreq.getSession(true) ;


      String myEncoding = null ;

      myEncoding =
          (String)(mySession.getAttribute("sessionEncoding"));

//      if ( (this.ignoreClient == false) && (myEncoding == null) )
//      {
//        myEncoding = inRequest.getCharacterEncoding();
//      }

      if (myEncoding == null)
      {
        myEncoding = this.defaultEncoding;
      }

      if ( mySession.getAttribute("sessionEncoding") == null ) {
        mySession.setAttribute("sessionEncoding", myEncoding);
      }

      if ( (myreq.getRequestURI().indexOf(".") < 0) ) {
//        if (  !"GET".equals(myreq.getMethod()) )
        inRequest.setCharacterEncoding(Language.getCharset(this.defaultEncoding));
        inResponse.setContentType("text/html;charset=" +
                                  Language.getCharset(myEncoding));
      }
    }

    // Pass control on to the next filter
    inChain.doFilter(inRequest, inResponse);

  }

  /**
   * 标准方法。
   */
  public void destroy() {

    this.defaultEncoding = null;
    this.filterConfig = null;

  }

  protected String getDefaultEncoding() {
    return Constants.DEFAULT_LANGUAGE;
  }

}