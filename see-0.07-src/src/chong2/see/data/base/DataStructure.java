package chong2.see.data.base;

import chong2.see.utils.CommonTools;

import java.util.ArrayList;

/**
 * <p>Title: 软件工程管理环境</p>
 * <p>Description: 为软件研发提供管理数据和任务的平台</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: 虫二</p>
 *
 * <p>数据的结构定义
 *
 * @author 玛瑞
 * @version 0.07
 */

public class DataStructure {

  /**
   * 数据集的字符串型属性
   */
  public static ArrayList setStringAttrs =
      CommonTools.stringToArray("type,keyCanModify,defaultSort,defaultOrder,defaultColor,description,matrixValues",",");

  /**
   * 数据集的列表型属性
   */
  public static ArrayList setArrayAttrs =
      CommonTools.stringToArray("keys,notNullItems,invalidItems,showItems,operation,notNullItems_matrix,invalidItems_matrix",",");

  /**
   * 数据项的字符串型属性
   */
  public static ArrayList itemStringAttrs =
      CommonTools.stringToArray("identify,displayName,description,type,size,unitage,editWidth,defaultValue,valueTranslated,valueConstraintType,valueConstraint,editControllerType,ismatrix",",");
  /**
   * 数据项的hash型属性
   */
  public static ArrayList itemHashAttrs =
      CommonTools.stringToArray("",",");

  /**
   * 数据集的类型
   */
  public static ArrayList DATASET_TYPES  =
  CommonTools.stringToArray("table,matrix",",");

  /**
   * 数据集的操作类型表
   */
  public static ArrayList DATASET_OPERATION_TYPES  =
  CommonTools.stringToArray("config,add,insert,copy,import,remove,modify,batch_modify,query,statistic,audit",",");

  /**
   * 数据集的范围（目前无用）
   */
  public static ArrayList DATASET_SCOPES  =
  CommonTools.stringToArray("system,project",",");

  /**
   * 数据项的数据类型表
   */
  public static ArrayList DATAITEM_DATA_TYPES  =
  CommonTools.stringToArray("char,string,int,float,date,time,auto",",");

  /**
   * 数据项的约束类型表
   */
  public static ArrayList DATAITEM_VALUE_CONSTRAINT_TYPES  =
  CommonTools.stringToArray("solo,multi,creattime,creatuser,currenttime,currentuser,ref_multi,ref_solo,language,ip,link,file,password,readonly,interface_style,interface_theme,datalist_style,data_audit_types",",");

  /**
  * 数据项的编辑控件类型表
  */
  public static ArrayList DATAITEM_EDIT_CONTROLLOR_TYPES  =
  CommonTools.stringToArray("text,radio,check,hidden,list,droplist,textarea",",");

  /**
  * 数据项的显示控件类型表
  */
  public static ArrayList DATAITEM_SHOW_CONTROLLOR_TYPES  =
  CommonTools.stringToArray("text,file",",");

  public DataStructure() {
  }
}