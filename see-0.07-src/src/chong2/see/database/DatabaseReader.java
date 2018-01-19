package chong2.see.database;

import chong2.see.data.*;
import java.sql.*;
import java.io.*;

/**
 * <p>Title: 软件工程管理环境</p>
 * <p>Description: 为软件研发提供管理数据和任务的平台</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: 虫二</p>
 *
 * <p>数据库的读取方法。
 * 这一版未实现。
 *
 * @author 玛瑞
 * @version 0.07
 */

public class DatabaseReader extends DataReader {

  private String sSeeDBDriver 	= "";
  private String sSeeConnect 	= "";
  private String sSeeUser 	= "seeManager";
  private String sSeePassword	= "Iseeyouareinthelakedrying";

  private boolean bConnecting	= false;

  Connection connMy		= null;
  ResultSet  rsMy			= null;
  Statement  stmtMy		= null;

  private int iResult		= 0;
  private String sStmt		= "";


  /**
   * 类的构建方法
   */
  public DatabaseReader()  {
//    initParser();
  }



  // ----------------------------

  public void SetSeeDBDriver(String sInput)
  {
    sSeeDBDriver = sInput;
  }

  public String GetSeeDBDriver()
  {
    return sSeeDBDriver;
  }

  // ----------------------------

  public void SetSeeConnect(String sInput)
  {
    sSeeConnect = sInput;
  }

  public String GetSeeConnect()
  {
    return sSeeConnect;
  }

  // ----------------------------

  public boolean IsConnecting()
  {
    return bConnecting;
  }

  public void SetConnecting(boolean bInput)
  {
    bConnecting = bInput;
  }

  // ----------------------------

  public String ConnectSee()
  {

    try
    {
      Class.forName(sSeeDBDriver).newInstance();
      connMy = DriverManager.getConnection(sSeeConnect,sSeeUser,sSeePassword);
      stmtMy = connMy.createStatement();
    }
    catch (Exception E)
    {
      return E.toString();

    }

    SetConnecting(true);

    return "ok";
  }


  // ----------------------------

  public String ConnectDatabase(String sD, String sC, String sU, String sP)
  {

    try
    {
      Class.forName(sD).newInstance();
      connMy = DriverManager.getConnection(sC,sU,sP);
      stmtMy = connMy.createStatement();
    }
    catch (Exception E)
    {
      return E.toString();

    }

    SetConnecting(true);

    return "ok";
  }

  public String CloseDatabase()
  {
    try
    {
      rsMy.close();
      stmtMy.close();
      connMy.close();

    }
    catch (Exception  E)
    {
      return E.toString();
    }

    SetConnecting(false);

    return "ok";
  }

  public String CreateDatabase(String sD)
  {

    if ( !IsConnecting() )
    {
      return "Database is not Connecting";
    }

    sStmt = "use " + sD;

    if ( ExecuteSQL(sStmt) == "ok" )
    {
      return "Database for SEE is already there";
    }
    else
    {

      sStmt = "create database " + sD;

      if ( ExecuteSQL(sStmt) != "ok" )
      {
        return "Cannot create database for SEE";
      }
      else
      {
        return "ok";
      }
    }

  }


  public String CreateManager(String sD, String sU, String sP)
  {
    if ( !IsConnecting() )
    {
      return "Database is not Connecting";
    }

    sStmt = "grant all privileges on " + sD + ".* to " + sU + " identified by " + sP + " with grant option";

    if ( ExecuteSQL(sStmt) != "ok" )
    {
      return "Cannot create account for SEE";
    }

    return "ok";

  }


  public ResultSet QueryData(String sStatement)
  {
    try
    {
      rsMy = stmtMy.executeQuery(sStatement);
    }
    catch (SQLException  E)
    {
      return null;
    }

    return rsMy;

  }


  public int ModifyData(String sStatement)
  {
    try
    {
      iResult = stmtMy.executeUpdate(sStatement);
    }
    catch (SQLException  E)
    {
      return -1;
    }

    return iResult;

  }

  public String ExecuteSQL(String sStatement)
  {
    try
    {
      stmtMy.execute(sStatement);
    }
    catch (Exception  E)
    {
      return E.toString();
    }

    return "ok";

  }


}