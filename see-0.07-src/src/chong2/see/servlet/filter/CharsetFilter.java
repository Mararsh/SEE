package chong2.see.servlet.filter;

import chong2.see.data.base.Constants;
import chong2.see.data.base.Language;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * <p>Title: ������̹�����</p>
 * <p>Description: Ϊ����з��ṩ�������ݺ������ƽ̨</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: ���</p>
 *
 * <p><strong>CharacterEncodingFilter</strong> ��һ��servlet�Ĺ�������
 * ����ͳһ��������request��response���ַ����롣</p>
 *
 * <p>����request��������Ϊͳһ��ȱʡ�ַ����롣ȱʡ����Ӧ�Ǵ�filter�ĳ�ʼ������
 * request�ı��뷽ʽ��������������ͱ���ͻ��˴������ݵķ�ʽ������������ʾ�ı���
 * ��ʽ�ޱ�Ҫ��ϵ����Ӧͬʱ��Ӧ���������ļ������ݿ������ݵ�ȱʡ���뷽ʽ��
 *
 * <p>����response���������ͻ��˵����ݣ��������·�ʽ�����룺
 * <li>��ǰ�û������ã����û���ע���¼</li>
 * <li>��ǰrequest���ַ�����</li>
 * <li>ȱʡ���ַ�����</li>
 *
 * <p>��filter�������������ԣ�
 * <li><strong>defaultEncoding</strong>:����Ӧ�õ�ȱʡ���뷽ʽ</li>
 * <li><strong>sessionEncoding</strong>:��ǰsession�ı��뷽ʽ</li>
 * </p>
 *
 * @author ����
 * @version 0.07
 */

public class CharsetFilter implements Filter {

  /**
   * �Ƿ���Կͻ������õı��뷽ʽ�����Ǵ�filter�ĳ�ʼ����
   */
  protected boolean ignoreClient = true;

  /**
   * ����Ӧ�õ�ȱʡ���뷽ʽ��
   */
  protected String defaultEncoding = Constants.DEFAULT_LANGUAGE;

  /**
   * filter���ö������Ǳ�׼ʵ�֡�
   */
  protected FilterConfig filterConfig = null;


  // ------------ ���÷�������׼�����滻�� ---------

  /**
   * ��ʼ���á�ֻ����servlet������װ�ش�filterʱ���ŵ�������
   * @param inConfig servlet�������ṩ�����ö���
   * @throws ServletException servlet�쳣
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
   * ��������request��response���ַ����롣
   * @param inRequest ���ڴ����servlet��request
   * @param inResponse ���ڴ����servlet��response
   * @param inChain ���ڴ����filter��
   * @exception IOException IO�쳣
   * @exception ServletException servlet�쳣
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
   * ��׼������
   */
  public void destroy() {

    this.defaultEncoding = null;
    this.filterConfig = null;

  }

  protected String getDefaultEncoding() {
    return Constants.DEFAULT_LANGUAGE;
  }

}