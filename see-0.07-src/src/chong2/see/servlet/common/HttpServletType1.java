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
 * <p>Title: ������̹�����</p>
 * <p>Description: Ϊ����з��ṩ�������ݺ������ƽ̨</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: ���</p>
 *
 * <p>HttpServletģ��2�����������ָ��ַ��ʷ�ʽ��ͬ������
 *
 * @author ����
 * @version 0.07
 */

abstract public class HttpServletType1
    extends HttpServlet {

  // --------------------- HttpServlet���� ----------------------

  /**
   * ����"GET"���͵�http������
   *
   * @param inRequest ���ڴ����servlet��request
   * @param inResponse ���ڴ����servlet��response
   *
   * @exception IOException IO�쳣
   * @exception ServletException servlet�쳣
   */
  public void doGet(HttpServletRequest inRequest,
                    HttpServletResponse inResponse)
      throws IOException, ServletException {

    processRequest(inRequest, inResponse);

  }

  /**
   * ����"POST"���͵�http������
   *
   * @param inRequest ���ڴ����servlet��request
   * @param inResponse ���ڴ����servlet��response
   *
   * @exception IOException IO�쳣
   * @exception ServletException servlet�쳣
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
   * ����http�����󡣼̳д�ģ���servlet��ʵ������
   *
   * @param inRequest ���ڴ����servlet��request
   * @param inResponse ���ڴ����servlet��response
   *
   * @exception IOException IO�쳣
   * @exception ServletException servlet�쳣
   */
  protected void processRequest(HttpServletRequest inRequest,
                                HttpServletResponse inResponse)
      throws IOException, ServletException {

  }

  /**
   * ��ʶ�������е��ı�
   * @param inKey �ı�
   * @return �ı�
   */
  public static String tt(String inKey) {
    return CommonTools.tt(inKey);
  }

  /**
   * ���ָ�������ָ�����ֵ���Դֵ������session����ı��롣
   * @param inRequest ��ǰ����
   * @param inKey ����
   * @return ���ֶ�Ӧ����Դֵ
   */
  public static String tr(HttpServletRequest inRequest,
                          String inKey) {
    return LanguageTools.tr(inRequest,inKey);
  }

  /**
   * ���ָ�������һ��ָ�����ֵ���Դֵ������session����ı��롣�滻����ֵ��
   * �������滻��λ����PARAMETER_IDENTIFY�����ģ��磺%%0,%%1��
   * @param inRequest ��ǰ����
   * @param inKey ����
   * @param inPa ����ֵ
   * @return ���ֶ�Ӧ����Դֵ
   */
  public static String tr(HttpServletRequest inRequest,
                          String inKey,
                          String inPa) {
    return LanguageTools.tr(inRequest,inKey, inPa);
  }

  /**
   * ���ָ�������һ��ָ�����ֵ���Դֵ������session����ı��롣�滻����ֵ��
   * �������滻��λ����PARAMETER_IDENTIFY�����ģ��磺%%0,%%1��
   * @param inRequest ��ǰ����
   * @param inKey ����
   * @param inPa ����ֵ
   * @return ���ֶ�Ӧ����Դֵ
   */
  public static String tr(HttpServletRequest inRequest,
                          String inKey,
                          ArrayList inPa) {
    return LanguageTools.tr(inRequest,inKey, inPa);
  }

  /**
   * ���ָ�������һ��ָ�����ֵ���Դֵ������session����ı��롣
   * @param inRequest ��ǰ����
   * @param inKey ����
   * @return ���ֶ�Ӧ����Դֵ
   */
  public static ArrayList tr(HttpServletRequest inRequest,
                             ArrayList inKey) {
    return LanguageTools.tr(inRequest,inKey);
  }

  /**
   * ���ָ�����ֵ���Դֵ������context����ı��롣
   * @param inRequest ��ǰ����
   * @param inKey ����
   * @return ���ֶ�Ӧ����Դֵ
   */
  public static String atr(HttpServletRequest inRequest,
                           String inKey) {
    return LanguageTools.atr(inRequest,inKey);
  }

  /**
   * ���һ��ָ�����ֵ���Դֵ������context����ı��롣
   * @param inRequest ��ǰ����
   * @param inKey ����
   * @return ���ֶ�Ӧ����Դֵ
   */
  public static ArrayList atr(HttpServletRequest inRequest,
                              ArrayList inKey) {
    return LanguageTools.tr(inRequest,inKey);
  }

}