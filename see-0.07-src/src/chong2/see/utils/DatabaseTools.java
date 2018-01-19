package chong2.see.utils;

import chong2.see.data.base.Constants;
import chong2.see.data.base.Language;
import chong2.see.data.DataReader;
import chong2.see.data.*;
import chong2.see.xml.DataXmlWriter;

import java.util.ArrayList;
import java.util.Hashtable;
import java.sql.*;
import java.io.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>Title: 软件工程管理环境</p>
 * <p>Description: 为软件研发提供管理数据和任务的平台</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: 虫二</p>
 *
 * 与数据库处理相关的静态方法。
 * 这一版未实现
 *
 * @author 玛瑞
 * @version 0.07
 */

final public class DatabaseTools {

/*  ===================== database methods ==================== */
//  public static DBHandler connectDB(HttpServletRequest myRequest) {
//    return connectDB(getContext(myRequest));
//  }
//
//  public static DBHandler connectDB(ServletContext myContext) {
//
//    String DBDriver = getInitConfig(myContext,"DBDriver");
//    String DBTarget = getInitConfig(myContext,"DBTarget");
//    String DBUser = getInitConfig(myContext,"DBUser");
//    String DBPassword = getInitConfig(myContext,"DBPassword");
//
//    DBHandler mydb =  new DBHandler();
//    mydb.openConnection(DBDriver,DBTarget,DBUser,DBPassword);
//
//    return mydb;
//  }

}