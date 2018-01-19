package chong2.see.data.base;

import chong2.see.utils.CommonTools;

import java.util.ArrayList;

/**
 * <p>Title: ������̹�����</p>
 * <p>Description: Ϊ����з��ṩ�������ݺ������ƽ̨</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: ���</p>
 *
 * <p>���ݵĽṹ����
 *
 * @author ����
 * @version 0.07
 */

public class DataStructure {

  /**
   * ���ݼ����ַ���������
   */
  public static ArrayList setStringAttrs =
      CommonTools.stringToArray("type,keyCanModify,defaultSort,defaultOrder,defaultColor,description,matrixValues",",");

  /**
   * ���ݼ����б�������
   */
  public static ArrayList setArrayAttrs =
      CommonTools.stringToArray("keys,notNullItems,invalidItems,showItems,operation,notNullItems_matrix,invalidItems_matrix",",");

  /**
   * ��������ַ���������
   */
  public static ArrayList itemStringAttrs =
      CommonTools.stringToArray("identify,displayName,description,type,size,unitage,editWidth,defaultValue,valueTranslated,valueConstraintType,valueConstraint,editControllerType,ismatrix",",");
  /**
   * �������hash������
   */
  public static ArrayList itemHashAttrs =
      CommonTools.stringToArray("",",");

  /**
   * ���ݼ�������
   */
  public static ArrayList DATASET_TYPES  =
  CommonTools.stringToArray("table,matrix",",");

  /**
   * ���ݼ��Ĳ������ͱ�
   */
  public static ArrayList DATASET_OPERATION_TYPES  =
  CommonTools.stringToArray("config,add,insert,copy,import,remove,modify,batch_modify,query,statistic,audit",",");

  /**
   * ���ݼ��ķ�Χ��Ŀǰ���ã�
   */
  public static ArrayList DATASET_SCOPES  =
  CommonTools.stringToArray("system,project",",");

  /**
   * ��������������ͱ�
   */
  public static ArrayList DATAITEM_DATA_TYPES  =
  CommonTools.stringToArray("char,string,int,float,date,time,auto",",");

  /**
   * �������Լ�����ͱ�
   */
  public static ArrayList DATAITEM_VALUE_CONSTRAINT_TYPES  =
  CommonTools.stringToArray("solo,multi,creattime,creatuser,currenttime,currentuser,ref_multi,ref_solo,language,ip,link,file,password,readonly,interface_style,interface_theme,datalist_style,data_audit_types",",");

  /**
  * ������ı༭�ؼ����ͱ�
  */
  public static ArrayList DATAITEM_EDIT_CONTROLLOR_TYPES  =
  CommonTools.stringToArray("text,radio,check,hidden,list,droplist,textarea",",");

  /**
  * ���������ʾ�ؼ����ͱ�
  */
  public static ArrayList DATAITEM_SHOW_CONTROLLOR_TYPES  =
  CommonTools.stringToArray("text,file",",");

  public DataStructure() {
  }
}