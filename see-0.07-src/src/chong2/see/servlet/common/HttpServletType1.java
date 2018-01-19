package chong2.see.servlet.common;

import chong2.see.utils.CommonTools;
import chong2.see.utils.LanguageTools;
import chong2.see.utils.PageTools;
import chong2.see.utils.UploadTools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>Title: 软件工程管理环境</p>
 * <p>Description: 为软件研发提供管理数据和任务的平台</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: 虫二</p>
 *
 * <p>HttpServlet模板2。即：不区分各种访问方式，同样处理。
 *
 * @author 玛瑞
 * @version 0.07
 */

abstract public class HttpServletType1
    extends HttpServlet {

  // --------------------- HttpServlet方法 ----------------------

  /**
   * 处理"GET"类型的http的请求
   *
   * @param inRequest 正在处理的servlet的request
   * @param inResponse 正在处理的servlet的response
   *
   * @exception IOException IO异常
   * @exception ServletException servlet异常
   */
  public void doGet(HttpServletRequest inRequest,
                    HttpServletResponse inResponse)
      throws IOException, ServletException {

    processRequest(inRequest, inResponse);

  }

  /**
   * 处理"POST"类型的http的请求
   *
   * @param inRequest 正在处理的servlet的request
   * @param inResponse 正在处理的servlet的response
   *
   * @exception IOException IO异常
   * @exception ServletException servlet异常
   */
  public void doPost(HttpServletRequest inRequest,
                     HttpServletResponse inResponse)
      throws IOException, ServletException {

    if ( !inRequest.getRequestURI().endsWith(PageTools.INFO_PAGE) &&
         UploadTools.isUpload(inRequest)) {
      Hashtable myup =  UploadTools.dealUpload(inRequest,true);
      if ( myup != null )
        inRequest.setAttribute(UploadTools.UPLOAD_NAME, myup);
      else {
        PageTools.outErrorPage(inRequest,inResponse,
                               null, inRequest.getAttribute("error").toString(),
                               "","back");
        return;
      }
    }
    processRequest(inRequest, inResponse);

  }

  // --------------------- protected Methods ----------------------

  /**
   * 处理http的请求。继承此模板的servlet来实现它。
   *
   * @param inRequest 正在处理的servlet的request
   * @param inResponse 正在处理的servlet的response
   *
   * @exception IOException IO异常
   * @exception ServletException servlet异常
   */
  protected void processRequest(HttpServletRequest inRequest,
                                HttpServletResponse inResponse)
      throws IOException, ServletException {

  }

  /**
   * 标识语言敏感的文本
   * @param inKey 文本
   * @return 文本
   */
  public static String tt(String inKey) {
    return CommonTools.tt(inKey);
  }

  /**
   * 获得指定编码的指定文字的资源值。采用session保存的编码。
   * @param inRequest 当前请求
   * @param inKey 文字
   * @return 文字对应的资源值
   */
  public static String tr(HttpServletRequest inRequest,
                          String inKey) {
    return LanguageTools.tr(inRequest,inKey);
  }

  /**
   * 获得指定编码的一组指定文字的资源值。采用session保存的编码。替换参数值。
   * 参数被替换的位置是PARAMETER_IDENTIFY决定的，如：%%0,%%1等
   * @param inRequest 当前请求
   * @param inKey 文字
   * @param inPa 参数值
   * @return 文字对应的资源值
   */
  public static String tr(HttpServletRequest inRequest,
                          String inKey,
                          String inPa) {
    return LanguageTools.tr(inRequest,inKey, inPa);
  }

  /**
   * 获得指定编码的一组指定文字的资源值。采用session保存的编码。替换参数值。
   * 参数被替换的位置是PARAMETER_IDENTIFY决定的，如：%%0,%%1等
   * @param inRequest 当前请求
   * @param inKey 文字
   * @param inPa 参数值
   * @return 文字对应的资源值
   */
  public static String tr(HttpServletRequest inRequest,
                          String inKey,
                          ArrayList inPa) {
    return LanguageTools.tr(inRequest,inKey, inPa);
  }

  /**
   * 获得指定编码的一组指定文字的资源值。采用session保存的编码。
   * @param inRequest 当前请求
   * @param inKey 文字
   * @return 文字对应的资源值
   */
  public static ArrayList tr(HttpServletRequest inRequest,
                             ArrayList inKey) {
    return LanguageTools.tr(inRequest,inKey);
  }

  /**
   * 获得指定文字的资源值。采用context保存的编码。
   * @param inRequest 当前请求
   * @param inKey 文字
   * @return 文字对应的资源值
   */
  public static String atr(HttpServletRequest inRequest,
                           String inKey) {
    return LanguageTools.atr(inRequest,inKey);
  }

  /**
   * 获得一组指定文字的资源值。采用context保存的编码。
   * @param inRequest 当前请求
   * @param inKey 文字
   * @return 文字对应的资源值
   */
  public static ArrayList atr(HttpServletRequest inRequest,
                              ArrayList inKey) {
    return LanguageTools.tr(inRequest,inKey);
  }

}