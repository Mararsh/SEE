package chong2.see.utils;

import chong2.see.data.base.Constants;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.imageio.ImageIO;


/**
 * <p>Title: ������̹�����</p>
 * <p>Description: Ϊ����з��ṩ�������ݺ������ƽ̨</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: ���</p>
 *
 * �뻭ͼ��صľ�̬����
 *
 * @author ����
 * @version 0.07
 */

public class ImageTools {

  /**
   * ��ͼ�Ŀ��
   */
  public static int  PIE_WIDTH = 550;
  /**
   * ��ͼ�ĸ߶�
   */
  public static int  PIE_HEIGHT = 350;
  /**
   * ��״ͼ�Ŀ��
   */
  public static int  HISTOGRAM_WIDTH = 700;
  /**
   * ��״ͼ�ĸ߶�
   */
  public static int  HISTOGRAM_HEIGHT = 400;
  /**
   * ��ͼ��Բ��ֱ��
   */
  public static int  PIE_SIZE = 200;
  /**
   * ��ͼ��ɫ��������
   */
  public static int  PIE_COUNT = 10;
  /**
   * ��״ͼ����̶ȵĸ���
   */
  public static int  HISTOGRAM_SIZE = 10;
  /**
   * ��״ͼ��ֵ��������
   */
  public static int  HISTOGRAM_COUNT = 8;
  /**
   * ��״ͼ��ֵ��������
   */
  public static int  HISTOGRAM_CROSS_COUNT = 5;
  /**
   * ��ͼ�Ŀ��
   */
  public static int  TREND_WIDTH = 700;
  /**
   * ��ͼ�ĸ߶�
   */
  public static int  TREND_HEIGHT = 400;
  /**
   * ����ͼ����̶ȵĸ���
   */
  public static int  TREND_X = 10;
  /**
   * ����ͼ����̶ȵĸ���
   */
  public static int  TREND_Y = 10;
  /**
   * ��ɫ�б�
   */
  public static Color COLOR_LIST[] =
  { Color.BLUE, Color.RED, Color.YELLOW,
    new Color(Integer.parseInt("226633",16)),
    new Color(Integer.parseInt("DD33EE",16)),
    Color.ORANGE,
    new Color(Integer.parseInt("9999EE",16)),
    new Color(Integer.parseInt("CCEE77",16)),
    Color.PINK,Color.GREEN
  };
  /**
  * ���������
  */
  public static Font TITLE_FONT = new Font("SansSerif",Font.BOLD,16);
  /**
   * ֵ������
   */
  public static Font TEXT_FONT = new Font("SansSerif",Font.PLAIN,12);
  /**
   * С����
   */
  public static Font SMALL_TEXT_FONT =
      new Font("SansSerif",Font.PLAIN,10);
  /**
   * ͼ�ı�����ɫ
   */
  public static Color BG_COLOR = Color.WHITE;
  /**
   * �������ɫ
   */
  public static Color TITLE_COLOR =
      new Color(Integer.parseInt("223366",16));
  /**
   * ֵ����ɫ
   */
  public static Color TEXT_COLOR = Color.BLACK;

  /**
   * ��������ͼ�ļ���������ʱ�ļ���ϵͳ5���Ӻ��Զ�ɾ����
   * @param myfile �ļ���
   * @param myinfo ͼ�е���Ϣ
   * @param mydata ����ֵ
   * @param mytotal ����
   * @param mysort �Լ������������
   * @param mytype ͼ������
   * @return �Ƿ�ɹ�
   */
  public static String writeCountImage(String myfile,
                                       Hashtable myinfo,
                                       Hashtable mydata,
                                       int  mytotal,
                                       ArrayList mysort,
                                       String mytype) {

    try {
      if( !myfile.toLowerCase().endsWith(".jpeg") &&
          !myfile.toLowerCase().endsWith(".jpg"))
        return "invalid_data";
      if ( mydata == null ) return "invalid_data";
      if ( mytype == null ) return "invalid_data";
      if ( mytotal < 1 ) return "invalid_data";
      if ( mysort == null )
        mysort = CommonTools.sortIntHash(mydata);

      BufferedImage myimage = null;
      Graphics myg = null;

      if ("piechart".equals(mytype)) {
        myimage = new BufferedImage(PIE_WIDTH, PIE_HEIGHT,
                                    BufferedImage.TYPE_INT_RGB);
        myg = myimage.createGraphics();
        paintPie(myinfo,myg,mydata,mysort,mytotal);
      }  else if ("histogram".equals(mytype)) {
        myimage = new BufferedImage(HISTOGRAM_WIDTH,HISTOGRAM_HEIGHT,
                                    BufferedImage.TYPE_INT_RGB);
        myg = myimage.createGraphics();
        paintHistogram(myinfo,myg,mydata,mysort,mytotal);
      }  else
        return "invalid_data";

      if (myimage != null)
        writeImageFile(myfile, myimage);
      if (myg != null)
        myg.dispose();
      return Constants.SUCCESSFUL;
    }
    catch (Exception ex) {
      return "invalid_data";
    }

  }

  /**
   * �������ı�ͼ
   * @param myinfo ͼ�е���Ϣ
   * @param myg ����
   * @param mydata ����ֵ
   * @param mysort �Լ������������
   * @param mytotal ����
   */
  public static void  paintPie(Hashtable myinfo,
                               Graphics myg,
                               Hashtable mydata,
                               ArrayList mysort,
                               int  mytotal) {

    if ( mytotal < 1 ) return;
    if ( mydata == null ) return;
    if ( mysort == null )
      mysort = CommonTools.sortIntHash(mydata);
    if ( myinfo == null ) myinfo = new Hashtable();

    myg.setColor(BG_COLOR);
    myg.fillRect(0,0,PIE_WIDTH, PIE_HEIGHT);

    int mysize = PIE_COUNT;
    if ( mysort.size() < mysize )
      mysize = mysort.size();

    //д����
    if ( myinfo != null ) {
      myg.setColor(TITLE_COLOR);
      myg.setFont(TITLE_FONT);
      if ( myinfo.get("title") != null)
        myg.drawString(myinfo.get("title").toString(),5,20);
      myg.setFont(TEXT_FONT);
      if ( mysort.size() > PIE_COUNT ) {
        if ( myinfo.get("count_show_limit") != null)
          myg.drawString(myinfo.get("count_show_limit").toString() +
                         PIE_COUNT, 5,40);
      }
    }

    // ������
    String myk,myd;
    int myrc, mystart = 0;
    int mys = PIE_WIDTH - PIE_SIZE;
    for (int i=0; i< mysize; i++) {
      myk = mysort.get(i).toString();
      if ( mydata.get(myk) == null) continue;
      myd = mydata.get(myk).toString();
      myrc = CommonTools.stringToInt(myd) * 360 / mytotal ;
      if (myrc < 4) myrc = 4;
      myg.setColor(COLOR_LIST[i % COLOR_LIST.length]);
      myg.fillArc( mys,80, PIE_SIZE,PIE_SIZE,mystart,myrc);
      mystart = mystart + myrc;
    }

    //д��ɫ����
    myg.setFont(TEXT_FONT);
    myg.setColor(TITLE_COLOR);
    int myy = 100,myw = 5,myr;
    if ( myinfo.get("item") != null)
      myg.drawString(myinfo.get("item").toString() + " : ",
                     myw,myy - 20);
    String mytext;
    myg.setColor(TEXT_COLOR);
    String mynull = StatisticTools.STATISTIC_NULL;
    if ( myinfo.get(StatisticTools.STATISTIC_NULL) != null )
      mynull = myinfo.get(StatisticTools.STATISTIC_NULL).toString();
    String myother = StatisticTools.STATISTIC_OTHER;
    if ( myinfo.get(StatisticTools.STATISTIC_OTHER) != null )
      myother = myinfo.get(StatisticTools.STATISTIC_OTHER).toString();
    for (int i=0; i< mysize; i++) {
      myk = mysort.get(i).toString();
      if ( mydata.get(myk) == null) continue;
      myw = 5;
      myg.setColor(COLOR_LIST[i % COLOR_LIST.length]);
      myg.fillRect(myw,myy - 14 ,14,14);
      myg.setColor(TEXT_COLOR);
      myw = 20 + myw;
      mytext = myk.trim();
      mytext = mytext.replaceAll(StatisticTools.STATISTIC_NULL,mynull);
      mytext = mytext.replaceAll(StatisticTools.STATISTIC_OTHER,myother);
      myg.drawString(mytext,myw,myy);
      myd = mydata.get(myk).toString();
      myw = 5 + myw + mytext.getBytes().length * 7;
      myg.drawString(" : " + myd, myw, myy);
      myr = (int)CommonTools.getPercent(myd,mytotal,0);
      myw = 5 + myw + myd.length() * 14;
      myg.drawString(" = " + myr + " %", myw, myy);
      myy = myy + 20;
    }

    return;
  }

  /**
   * ����������״ͼ
   * @param myinfo ͼ�е���Ϣ
   * @param myg ����
   * @param mydata ����ֵ
   * @param mysort �Լ������������
   * @param mytotal ����
   */
  public static void  paintHistogram(Hashtable myinfo,
                                     Graphics myg,
                                     Hashtable mydata,
                                     ArrayList mysort,
                                     int  mytotal) {

    if ( mytotal < 1 ) return;
    if ( mydata == null ) return;
    if ( mysort == null )
      mysort = CommonTools.sortIntHash(mydata);
    if ( mysort.size() < 1 ) return;
    if ( myinfo == null ) myinfo = new Hashtable();

    myg.setColor(BG_COLOR);
    myg.fillRect(0,0,HISTOGRAM_WIDTH,HISTOGRAM_HEIGHT);

    //�Զ���������̶ȣ����ؼ��㣩
    int mymax = 0;
    mymax = CommonTools.stringToInt(mydata.get(mysort.get(0)).toString());
    if ( mymax < 1 ) return;
    int mysize = HISTOGRAM_SIZE;
    if ( mymax < mysize ) {
      mysize = mymax;
    }
    float mystepsize = (HISTOGRAM_HEIGHT - 130) * 1.0f /  mymax;
    int mykcount = mymax / mysize;
    if ( (mymax  % mysize) > 0 )
      mykcount =  mykcount + 1 ;

    //������
    int mytop = 60;
    int mybottom = mytop + (int)(mystepsize *  mymax) + 20;
    myg.setColor(TITLE_COLOR);
    myg.drawLine(35,mytop,35,mybottom);
    myg.drawLine(32,mytop + 4,35,mytop);
    myg.drawLine(35,mytop,38,mytop + 4);
    myg.drawLine(35,mybottom,
                 HISTOGRAM_WIDTH - 10,mybottom);
    myg.drawLine(HISTOGRAM_WIDTH - 14,mybottom + 3,
                 HISTOGRAM_WIDTH - 10,mybottom);
    myg.drawLine(HISTOGRAM_WIDTH - 14,mybottom - 3,
                 HISTOGRAM_WIDTH - 10,mybottom);
    myg.drawString(myinfo.get("yname").toString(),0,mytop + 5);
    myg.drawString(myinfo.get("xname").toString(),TREND_WIDTH - 30,mybottom + 15);

    //д����̶�
    myg.setFont(TEXT_FONT);
    int myc, myh;
    for (int i=0; i< mysize + 1 ; i++) {
      myc = mykcount * i;
      if ( myc > mymax) {
        myh = mybottom - (int)(mystepsize *  mymax) ;
        myg.setColor(TITLE_COLOR);
        myg.drawString(mymax + "",0,myh + 7);
        myg.setColor(Color.LIGHT_GRAY);
        myg.drawLine(35,myh,HISTOGRAM_WIDTH - 10,myh);
        break;
      } else {
        myh = mybottom - (int)(mystepsize *  myc)  ;
        myg.setColor(TITLE_COLOR);
        myg.drawString(myc + "" , 0 ,myh + 7);
        if ( myc > 0 ) {
          myg.setColor(Color.LIGHT_GRAY);
          myg.drawLine(35,myh,HISTOGRAM_WIDTH - 10,myh);
        }
      }
    }

    //����״��ɫ��
    mysize = HISTOGRAM_COUNT;
    if ( mysort.size() < mysize ) {
      mysize = mysort.size();
    }
    myg.setFont(TEXT_FONT);
    myg.setColor(TEXT_COLOR);
    int myw = 70,myr, myv,myl;
    String mytext,myk,myd;
    int myxstep = (HISTOGRAM_WIDTH - myw) / (mysize + 1);
    for (int i=0; i< mysize; i++) {
      myk = mysort.get(i).toString();
      if ( mydata.get(myk) == null) continue;
      if ( StatisticTools.STATISTIC_OTHER.equals(myk) &&
           (myinfo.get(myk) != null) )
        mytext = myinfo.get(myk).toString();
      else if ( StatisticTools.STATISTIC_NULL.equals(myk) &&
                (myinfo.get(myk) != null) )
        mytext = myinfo.get(myk).toString();
      else
        mytext = myk.trim();
      myg.setColor(TEXT_COLOR);
      myg.drawString(mytext,myw,mybottom + 16);
      myd = mydata.get(myk).toString();
      myg.drawString(myd, myw, mybottom + 30);
      myv = CommonTools.stringToInt(myd);
      myr = (int)CommonTools.getPercent(myd,mytotal,0);
      myg.drawString(myr + " %", myw, mybottom + 44);
      myl = (int)(mystepsize *  myv);
      myg.setColor(COLOR_LIST[i % COLOR_LIST.length]);
      myg.fillRect(myw, mybottom - myl ,30, myl);
      myw = 5 + myw + myxstep;
    }

    myg.setColor(TITLE_COLOR);
    if ( myinfo.get("item") != null)
      myg.drawString(myinfo.get("item").toString() + " : ",
                     5, mybottom + 30);

    //д����
    if ( myinfo != null ) {
      myg.setColor(TITLE_COLOR);
      myg.setFont(TITLE_FONT);
      if ( myinfo.get("title") != null)
        myg.drawString(myinfo.get("title").toString(),5,20);
      myg.setFont(TEXT_FONT);
      if ( mysort.size() > HISTOGRAM_COUNT ) {
        if ( myinfo.get("count_show_limit") != null)
          myg.drawString(myinfo.get("count_show_limit").toString() +
                         HISTOGRAM_COUNT,5,40);
      }
    }

    return;
  }

  /**
   * �����������ͼ�ļ���������ʱ�ļ���ϵͳ5���Ӻ��Զ�ɾ����
   * @param myfile �ļ���
   * @param myinfo ͼ�е���Ϣ
   * @param mydata ����ֵ
   * @param mytotal ����
   * @param mysort �Լ������������
   * @param mytype ͼ������
   * @param showValue ͼ���Ƿ���ʾֵ
   * @return �Ƿ�ɹ�
   */
  public static String writeCrosscountImage(String myfile,
      Hashtable myinfo,
      Hashtable mydata,
      int  mytotal,
      ArrayList mysort,
      String mytype,
      boolean  showValue) {

    try {
      if ( mydata == null ) return "invalid_data";
      if ( mytype == null ) return "invalid_data";
      if ( mytotal < 1 ) return "invalid_data";
      if( !myfile.toLowerCase().endsWith(".jpeg") &&
          !myfile.toLowerCase().endsWith(".jpg"))
        return "invalid_data";
      if ( mysort == null )
        mysort = CommonTools.sortIntHash(mydata);

      BufferedImage myimage = null;
      Graphics myg = null;

      if ("piecross".equals(mytype)) {
        myimage = new BufferedImage(PIE_WIDTH, PIE_HEIGHT,
                                    BufferedImage.TYPE_INT_RGB);
        myg = myimage.createGraphics();
        paintCrossPie(myinfo,myg,mydata,mysort,mytotal);
      } else if ("histogramcross".equals(mytype)) {
        myimage = new BufferedImage(HISTOGRAM_WIDTH,HISTOGRAM_HEIGHT,
                                    BufferedImage.TYPE_INT_RGB);
        myg = myimage.createGraphics();
        paintCrossHistogram(myinfo,myg,mydata,mysort,
                            mytotal,showValue,true);
      } else if ("histogramrate".equals(mytype)) {
        myimage = new BufferedImage(HISTOGRAM_WIDTH,HISTOGRAM_HEIGHT,
                                    BufferedImage.TYPE_INT_RGB);
        myg = myimage.createGraphics();
        paintCrossHistogram(myinfo,myg,mydata,mysort,
                            mytotal,showValue,false);
      } else
        return "invalid_data";

      if ( (myimage == null) ||
           (myg == null) )
        return "invalid_data";

      writeImageFile(myfile, myimage);
      myg.dispose();
      return Constants.SUCCESSFUL;
    }
    catch (Exception ex) {
      return "invalid_data";
    }

  }

  /**
   * ����������ı�ͼ
   * @param myinfo ͼ�е���Ϣ
   * @param myg ����
   * @param mydata ����ֵ
   * @param mysort �Լ������������
   * @param mytotal ����
   */
  public static void  paintCrossPie(Hashtable myinfo,
                                    Graphics myg,
                                    Hashtable mydata,
                                    ArrayList mysort,
                                    int  mytotal) {
    if ( mydata == null ) return;
    if ( mysort == null )
      mysort = CommonTools.sortIntHash(mydata);
    Hashtable myuse = new Hashtable();
    ArrayList mynsort = new ArrayList();
    String kk,dd;
    String vv[];
    for (int i=0; i< mysort.size(); i++) {
      kk = mysort.get(i).toString();
      if ( mydata.get(kk) == null ) continue;;
      dd = mydata.get(kk).toString();
      vv = kk.split(StatisticTools.STATISTIC_SPLITTER);
      if ( (vv == null) || vv.length < 2) continue;
      kk = vv[1] + " \\ " + vv[0];
      myuse.put(kk, dd);
      mynsort.add(kk);
    }
    if ( myuse.isEmpty() ) return;
    paintPie(myinfo,myg,myuse,mynsort,mytotal);
  }

  /**
   * �������������״ͼ
   * @param myinfo ͼ�е���Ϣ
   * @param myg ����
   * @param mydata ����ֵ
   * @param mysort �Լ������������
   * @param mytotal ����
   * @param showValue ͼ���Ƿ���ʾֵ
   * @param iscross �ǽ���ͼ���Ǳ���ͼ
   */
  public static void  paintCrossHistogram(Hashtable myinfo,
      Graphics myg,
      Hashtable mydata,
      ArrayList mysort,
      int  mytotal,
      boolean  showValue,
      boolean  iscross) {

    if ( mytotal < 1 ) return;
    if ( mydata == null ) return;
    if ( myinfo == null ) myinfo = new Hashtable();
    if ( mysort == null )
      mysort = CommonTools.sortIntHash(mydata);
    if ( ( mysort == null ) ||
         ( mysort.size() < 1 ) )
      return;

    myg.setColor(BG_COLOR);
    myg.fillRect(0,0,HISTOGRAM_WIDTH,HISTOGRAM_HEIGHT);

    //ѡ����
    ArrayList mykey1 =  new ArrayList();
    ArrayList mykey2 =  new ArrayList();
    String kk;
    String vv[];
    boolean isok;
    for (int i=0; i< mysort.size(); i++) {
      if ( ( mykey1.size() >= HISTOGRAM_COUNT ) &&
           ( mykey2.size() >= HISTOGRAM_CROSS_COUNT ) )
      break;
    kk = mysort.get(i).toString();
    if ( mydata.get(kk) == null ) continue;;
    vv = kk.split(StatisticTools.STATISTIC_SPLITTER);
    if ( (vv == null) || vv.length < 2) continue;
    if ( !mykey1.contains(vv[0]) &&
         ( mykey1.size() >= HISTOGRAM_COUNT ) )
      continue;;
    if ( !mykey2.contains(vv[1]) &&
         ( mykey2.size() >= HISTOGRAM_CROSS_COUNT ) )
      continue;
    if ( !mykey1.contains(vv[0]) )
      mykey1.add(vv[0]);
    if ( !mykey2.contains(vv[1]) )
      mykey2.add(vv[1]);
    }

    //�Զ���������̶ȣ����ؼ��㣩
    int mymax = 0; // ���̶�
    if ( iscross ) {
      String mm = mysort.get(0).toString();
      if ( mydata.get(mm) == null ) return;
      mymax = CommonTools.stringToInt(mydata.get(mm).toString());
    } else {
      String mykey;
      int myvalue,myt;
      for(int i = 0; i < mykey1.size(); i++ ) {
        myt = 0;
        for(int j = 0; j < mykey2.size(); j++ ) {
          mykey =  mykey1.get(i).toString() +
                    StatisticTools.STATISTIC_SPLITTER +
                    mykey2.get(j).toString() ;
          if ( mydata.get(mykey) == null ) continue;
          myvalue = CommonTools.stringToInt(mydata.get(mykey).toString());
          if ( myvalue < 1) continue;
          myt = myt + myvalue;
        }
        if ( mymax < myt ) mymax = myt;
      }
    }
    if ( mymax < 1 ) return;
    int mysize = HISTOGRAM_SIZE;
    if ( mymax < mysize ) {
      mysize = mymax;
    }
    int mykcount =  mymax / mysize;
    if ( ( mymax % mysize) > 0 )
      mykcount =  mykcount + 1 ;
    float myksize = (HISTOGRAM_HEIGHT - 150) * 1.0f /  mymax;

    //������
    int mytop = 60;
    int mybottom = mytop + (int)(myksize *  mymax) + 20;
    myg.setColor(TITLE_COLOR);
    myg.drawLine(35,mytop,35,mybottom);
    myg.drawLine(32,mytop + 4,35,mytop);
    myg.drawLine(35,mytop,38,mytop + 4);
    myg.drawLine(35,mybottom,
                 HISTOGRAM_WIDTH - 10,mybottom);
    myg.drawLine(HISTOGRAM_WIDTH - 14,mybottom + 3,
                 HISTOGRAM_WIDTH - 10,mybottom);
    myg.drawLine(HISTOGRAM_WIDTH - 14,mybottom - 3,
                 HISTOGRAM_WIDTH - 10,mybottom);
    myg.drawString(myinfo.get("yname").toString(),0,mytop + 5);
    myg.drawString(myinfo.get("xname").toString(),TREND_WIDTH - 30,mybottom + 15);

    //д����̶�
    myg.setFont(TEXT_FONT);
    int myc, myh;
    for (int i=0; i< mysize + 1 ; i++) {
      myc = mykcount * i ;
      if ( myc > mymax) {
        myh = mybottom - (int)(myksize *  mymax) ;
        myg.setColor(TITLE_COLOR);
        myg.drawString(mymax + "",0,myh + 7);
        myg.setColor(Color.LIGHT_GRAY);
        myg.drawLine(35,myh,HISTOGRAM_WIDTH - 10,myh);
        break;
      } else {
        myh = mybottom - (int)(myksize *  myc) ;
        myg.setColor(TITLE_COLOR);
        myg.drawString(myc + "" , 0 ,myh + 7);
        if ( myc > 0 ) {
          myg.setColor(Color.LIGHT_GRAY);
          myg.drawLine(35,myh,HISTOGRAM_WIDTH - 10,myh);
        }
      }
    }


    //����״��
    int mymsize = HISTOGRAM_COUNT;
    if ( mykey1.size() < mymsize )
      mymsize = mykey1.size();
    int mycsize = HISTOGRAM_CROSS_COUNT;
    if ( mykey2.size() < mycsize )
      mycsize = mykey2.size();
    myg.setFont(TEXT_FONT);
    myg.setColor(TEXT_COLOR);
    int myy = mybottom + 10;
    myg.setColor(TITLE_COLOR);
    if ( myinfo.get("item1") != null) {
      myg.drawString(myinfo.get("item1").toString() + " : ",
                     5, myy + 10);
    }
    int mywc = 70,myw, myr, myv,myl,mych= mybottom;
    String mytext1,mytext2,myk1,myk2,myd,myk;
    int myxstep = (HISTOGRAM_WIDTH - mywc) / (mymsize + 1);
    for (int i=0; i< mymsize; i++) {
      mych = mybottom;
      myk1 = mykey1.get(i).toString();
      if ( StatisticTools.STATISTIC_OTHER.equals(myk1) &&
           (myinfo.get(myk1) != null) )
        mytext1 = myinfo.get(myk1).toString();
      else if ( StatisticTools.STATISTIC_NULL.equals(myk1) &&
                (myinfo.get(myk1) != null) )
        mytext1 = myinfo.get(myk1).toString();
      else
        mytext1 = myk1.trim();
      myw = mywc;
      for (int j=0; j< mycsize; j++) {
        myk2 = mykey2.get(j).toString();
        if ( StatisticTools.STATISTIC_OTHER.equals(myk2) &&
             (myinfo.get(myk2) != null) )
          mytext2 = myinfo.get(myk2).toString();
        else if ( StatisticTools.STATISTIC_NULL.equals(myk2) &&
                  (myinfo.get(myk2) != null) )
          mytext2 = myinfo.get(myk2).toString();
        else
          mytext2 = myk2.trim();
        myk = myk1 + StatisticTools.STATISTIC_SPLITTER + myk2 ;
        if ( mydata.get(myk) == null ) continue;
        myd = mydata.get(myk).toString();
        myv = CommonTools.stringToInt(myd);
        myr = (int)CommonTools.getPercent(myd,mytotal,0);
        myl = (int)(myksize *  myv);
        myg.setColor(COLOR_LIST[j % COLOR_LIST.length]);
        if ( iscross) {
          myg.fillRect(myw, mych - myl,10, myl);
          if ( showValue ) {
            myg.setColor(TEXT_COLOR);
            myg.drawString(myd, myw - 3, mybottom - myl - 2);
            myg.drawString(myr + "%", myw - 6 , mybottom - myl - 18);
          }
          myw = 12 + myw ;
        } else {
          mych = mych - myl;
          myg.fillRect(myw, mych,30, myl);
          if ( showValue ) {
            myg.setColor(TEXT_COLOR);
            myg.drawString(myd, myw - 15, mych + 12);
            myg.drawString(myr + "%", myw + 32 , mych + 12);
          }
        }
      }
      myg.setColor(TEXT_COLOR);
      myg.drawString(mytext1, mywc + 5, myy + 10);
      mywc = 5 + mywc + myxstep;
    }

    //д��ɫ����
    int mycc = 5, mylen = 0;
    myy = mybottom + 20;
    myg.setColor(TITLE_COLOR);
    if ( myinfo.get("item2") != null) {
      myg.drawString(myinfo.get("item2").toString() + " : ",
                     mycc, myy + 20);
      mylen =
          myinfo.get("item2").toString().getBytes().length * 7 + 3;
      mycc = mycc + mylen;
    }
    myg.setColor(TEXT_COLOR);
    for (int j=0; j< mycsize; j++) {
      if ( mycc >  HISTOGRAM_WIDTH - myxstep) {
        myy = myy + 15;
        mycc = 5 + mylen;
      }
      myk2 = mykey2.get(j).toString();
      if ( StatisticTools.STATISTIC_OTHER.equals(myk2) &&
           (myinfo.get(myk2) != null) )
        mytext2 = myinfo.get(myk2).toString();
      else if ( StatisticTools.STATISTIC_NULL.equals(myk2) &&
                (myinfo.get(myk2) != null) )
        mytext2 = myinfo.get(myk2).toString();
      else
        mytext2 = myk2.trim();
      myg.setColor(COLOR_LIST[j % COLOR_LIST.length]);
      myg.fillRect(mycc,myy + 6 ,14,14);
      myg.setColor(TEXT_COLOR);
      myg.drawString(mytext2, mycc + 20, myy + 20);
      mycc = 5 + mycc + mytext2.getBytes().length * 7 + 40;
    }

    //д����
    if ( myinfo != null ) {
      myg.setColor(TITLE_COLOR);
      myg.setFont(TITLE_FONT);
      if ( myinfo.get("title") != null)
        myg.drawString(myinfo.get("title").toString(),5,20);
      myg.setFont(TEXT_FONT);
      if ( (mykey1.size() >= HISTOGRAM_COUNT) ||
           (mykey2.size() >= HISTOGRAM_CROSS_COUNT)) {
        if ( myinfo.get("count_show_limit") != null)
          myg.drawString(myinfo.get("count_show_limit").toString() +
                         HISTOGRAM_CROSS_COUNT  + " \\ " +
                         HISTOGRAM_COUNT, 5,40);
      }
    }

    return;
  }

  /**
   * ��������ͼ�ļ���������ʱ�ļ���ϵͳ5���Ӻ��Զ�ɾ����
   * @param myfile �ļ���
   * @param myinfo ͼ�е���Ϣ
   * @param mydata ��������
   * @param mytime ʱ���ֶ���
   * @param myvalues ��ֵ�ֶ����б�
   * @param mytypes �������Ͷ���
   * @param myenc ��ǰ����
   * @return �Ƿ�ɹ�
   */
  public static String writeTrendImage(String myfile,
                                       Hashtable myinfo,
                                       ArrayList mydata,
                                       String mytime,
                                       ArrayList myvalues,
                                       Hashtable mytypes,
                                       String myenc) {

    try {
      if( !myfile.toLowerCase().endsWith(".jpeg") &&
          !myfile.toLowerCase().endsWith(".jpg"))
        return "invalid_data";
      if ( mydata == null ) return "invalid_data";

      BufferedImage myimage = null;
      Graphics myg = null;

      myimage = new BufferedImage(HISTOGRAM_WIDTH,HISTOGRAM_HEIGHT,
                                  BufferedImage.TYPE_INT_RGB);
      myg = myimage.createGraphics();
      paintTrend(myinfo,myg,mydata,mytime, myvalues,mytypes,myenc);

      if (myimage != null)
        writeImageFile(myfile, myimage);
      if (myg != null)
        myg.dispose();
      return Constants.SUCCESSFUL;
    }
    catch (Exception ex) {
      return "invalid_data";
    }

  }

  /**
   * ������ͼ
   * @param myinfo ͼ�е���Ϣ
   * @param myg ����
   * @param mydata ��������
   * @param mytime ʱ���ֶ���
   * @param myvalues ��ֵ�ֶ����б�
   * @param mytypes �������Ͷ���
   * @param myenc ��ǰ����
   */
  public static void  paintTrend(Hashtable myinfo,
                                 Graphics myg,
                                 ArrayList mydata,
                                 String mytime,
                                 ArrayList myvalues,
                                 Hashtable mytypes,
                                 String myenc) {

    if ( mydata == null ) return;
    if ( myinfo == null ) myinfo = new Hashtable();

    myg.setColor(BG_COLOR);
    myg.fillRect(0,0,TREND_WIDTH,TREND_HEIGHT);

    //�Զ�����̶ȣ����ؼ��㣩
    float mymax = CommonTools.WRONG_INT;
    float mymin = 0f - CommonTools.WRONG_INT;
    Hashtable myd;
    float myv;
    boolean hasFloat = false;
    String myn,myt, mytt = "time";
    if ( (mytypes != null) &&
         ( mytypes.get(mytime) != null ))
      mytt = mytypes.get(mytime).toString();
    boolean istime = "time".equals(mytt);
    long mystart = 0, myend = 0,myvt;
    for (int i=0; i< mydata.size(); i++) {
      myd = (Hashtable)mydata.get(i);
      if ( myd.get(mytime) == null) continue;
      myt = myd.get(mytime).toString();
      if (istime)
        myvt = CommonTools.getTime(myt,myenc);
      else
        myvt = CommonTools.getDate(myt,myenc);
      if ( myvt > myend) myend = myvt;
      if ( (mystart == 0) ||
           (myvt < mystart))
        mystart = myvt;
      for (int j=0; j< myvalues.size(); j++) {
        myn = myvalues.get(j).toString();
        if ( (mytypes != null) &&
            "float".equals(mytypes.get(myn).toString()))
          hasFloat = true;
        if ( myd.get(myn) == null ) continue;
        myv = CommonTools.stringToFloat((String)myd.get(myn));
        if ( myv == CommonTools.WRONG_INT ) continue;
        if (myv > mymax)
          mymax = myv;
        if (myv < mymin)
          mymin = myv;
      }
    }
    //Y����
//    if ( mymin > 0 ) mymin = 0f;
    float mysize = mymax - mymin ;
    float mystepsize = 0f;
    if ( mysize > 0)
      mystepsize = (TREND_HEIGHT - 130f) * 1.0f /  mysize;
    float mykcount = mysize / (TREND_Y * 1.0f);

    //��������
    int mytop = 60;
    int mybottom = mytop + (int)(mystepsize *  mysize) + 20;
    myg.setColor(TITLE_COLOR);
    myg.drawLine(35,mytop,35,mybottom);
    myg.drawLine(32,mytop + 4,35,mytop);
    myg.drawLine(35,mytop,38,mytop + 4);
    myg.drawLine(35,mybottom,
                 TREND_WIDTH - 10,mybottom);
    myg.drawLine(TREND_WIDTH - 14,mybottom + 3,
                 TREND_WIDTH - 10,mybottom);
    myg.drawLine(TREND_WIDTH - 14,mybottom - 3,
                 TREND_WIDTH - 10,mybottom);
    myg.drawString(myinfo.get("yname").toString(),0,mytop + 5);
    myg.drawString(myinfo.get("xname").toString(),
                   TREND_WIDTH - myinfo.get("xname").toString().length() * 12 - 10,
                   mybottom + 15);
    //��ֹʱ��
    int myhh = mybottom + 15;
    String myss;
    if (istime)
      myss = CommonTools.getTimeShow(mystart,myenc) +
      " - " + CommonTools.getTimeShow(myend,myenc);
    else
      myss = CommonTools.getDateShow(mystart,myenc) +
      " - " + CommonTools.getDateShow(myend,myenc);
    myg.drawString(myss,40,mybottom + 30);

    //д��ֵ���꣨Y���꣩�̶�
    myg.setFont(TEXT_FONT);
    float myc;
    int myh;
    for (int i=0; i< TREND_Y + 1 ; i++) {
      myc = mykcount * i * 1.0f;
      if ( myc > mysize) {
        myh = mybottom - (int)(mystepsize *  mysize);
        myg.setColor(TITLE_COLOR);
        if ( hasFloat )
          myg.drawString(mymax + "",0,myh + 7);
        else
          myg.drawString((int)mymax + "",0,myh + 7);
        myg.setColor(Color.LIGHT_GRAY);
        myg.drawLine(35,myh,TREND_WIDTH - 10,myh);
        break;
      } else {
        myh = (int)(mybottom - mystepsize *  myc)  ;
        myg.setColor(TITLE_COLOR);
        if ( hasFloat )
          myg.drawString(myc + mymin + "" , 0 ,myh + 7);
        else
          myg.drawString((int)(myc + mymin) + "" , 0 ,myh + 7);
        myg.setColor(Color.LIGHT_GRAY);
        myg.drawLine(35,myh,TREND_WIDTH - 10,myh);
      }
    }

    // ����Y�̶�
    if ( mymin < 0 ) {
      int myzero = mybottom - (int)(mystepsize *  ( 0 - mymin ));
      myg.setColor(Color.RED);
      for (int i=35; i< TREND_WIDTH - 13; i = i + 8) {
        myg.drawLine(i,myzero, i + 3,myzero);
      }
      myg.drawString("0" , TREND_WIDTH - 8 ,myzero + 4);
    }

    //X����
    float mytimestep = 0;
    float mytimesize = myend - mystart;
    if ( mytimesize > 0)
      mytimestep = (TREND_WIDTH - 50) * 1.0f /  mytimesize;
    float mytcount = mytimesize / TREND_X;
    if ( (mytimesize  % TREND_X) > 0 )
      mytcount =  mytcount + 1 ;

    //дʱ�����꣨X���꣩�̶�
    myg.setFont(TEXT_FONT);
    int myw;
    for (int i=1; i< TREND_X + 1 ; i++) {
      myw = 35 + (int)(mytimestep *  mytcount * i)  ;
      myg.setColor(Color.LIGHT_GRAY);
      myg.drawLine(myw,mybottom,myw,mytop + 5);
    }

    //��������
    myg.setFont(TEXT_FONT);
    myg.setColor(TEXT_COLOR);
    int myx, myy,mylx=-1,myly=-1,len=40,hh=mybottom + 45;
    String myvn, mysn;
    float myvv;
    for (int i=0; i< myvalues.size(); i++) {
      myvn = myvalues.get(i).toString();
      mysn = myinfo.get("name" + i).toString();
      myg.setColor(TEXT_COLOR);
      myg.drawString(mysn,len, hh);
      len = len + mysn.length() * 12 + 5;
      myg.setColor(COLOR_LIST[i % COLOR_LIST.length]);
//      myg.drawLine(len,hh,len + 10,hh);
      myg.fillRect(len,hh,10,2);
      len = len + 15;
      mylx=-1;
      myly=-1;
      for (int j=0; j< mydata.size(); j++) {
        myd = (Hashtable)mydata.get(j);
        if ( myd.get(mytime) == null) continue;
        if ( myd.get(myvn) == null) continue;
        myt = myd.get(mytime).toString();
        if ( istime)
          myvt = CommonTools.getTime(myt,myenc);
        else
          myvt = CommonTools.getDate(myt,myenc);
        myx = 35 + (int)(mytimestep * (myvt - mystart));
        myvv = CommonTools.stringToFloat(myd.get(myvn).toString());
        myy = mybottom - (int)(mystepsize * (myvv - mymin) ) ;
//        myg.drawString(myvv + "" , myx ,myy);
        if ( mylx > 0 )
          myg.drawLine(mylx,myly,myx,myy);
        mylx = myx;
        myly = myy;
      }
    }

    //д����
    if ( myinfo != null ) {
      myg.setColor(TITLE_COLOR);
      myg.setFont(TITLE_FONT);
      if ( myinfo.get("title") != null)
        myg.drawString(myinfo.get("title").toString(),5,20);
    }

    return;
  }

  /**
   * �����õ�ͼ��д���ļ�
   * @param myfile �ļ���
   * @param myimage �ڴ����ѻ��õ�ͼ��
   * @return �Ƿ�ɹ�
   */
  public static String writeImageFile(String myfile,
                                      BufferedImage myimage) {

    try {

      if( !myfile.toLowerCase().endsWith(".jpeg") &&
          !myfile.toLowerCase().endsWith(".jpg"))
        return "invalid_data";

      BufferedOutputStream myout =
          new BufferedOutputStream( new FileOutputStream( new File(myfile) ));

      ImageIO.write(myimage,"jpeg",myout);

      myout.close();

      return Constants.SUCCESSFUL;
    }
    catch (IOException ex) {
      return "invalid_data";
    }
  }

}