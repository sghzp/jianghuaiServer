package com.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.beans.CarInfo;
import com.seu.database.CarInfoDao;
import com.seu.database.JDBCTools;
import com.seu.tool.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServerHandler extends SimpleChannelInboundHandler<String> {

    static String unKnowNum = "";
    public double totalmilegps = 0.0;


    private String tablename;// 数据库的表名;
    Timestamp pre = null;// 定义前一个点;
    Timestamp cur = null;// 定义当前的点;
    public String vehicleID = null;
    double inter = Double.MIN_VALUE;// 差值;
    public double longitude = 0;
    public double lastlongitude = 0;
    public double latitude = 0;
    public double lastlatitude = 0;
    String curRoadName = "";
    double lon = 117;
    double lat = 32;
    public double gPS_speed = 0;
    String gPS_state;
    public double carspeed = 0;
    double heading = 0;
    int mmmm = 0;
    int sum = 0;
    /*获取里程数*/
    volatile double preMile = 0;
    volatile double preMile0 = 0;
    volatile double mile = 0;
    volatile int startFlag = 0;//起始标志
    volatile String MyProjectTaskID = null;
    ConcurrentHashMap<String, String> map = null;
    public ConcurrentHashMap<String,speed_data> speed_arr = new ConcurrentHashMap<String, speed_data>();//用于存放规范数据
    public CopyOnWriteArrayList<road_data> arr = new CopyOnWriteArrayList<>();
    CopyOnWriteArrayList<road_data> arrDoors = new CopyOnWriteArrayList<>();
    CopyOnWriteArrayList<road_data> arrOutDoors = new CopyOnWriteArrayList<>();
    public Map<Integer,Double> outDoorsMaxSpeed = new HashMap();
    public LinkedList<String> roadOrderList = new LinkedList<>();
    String Road_id = null;
    int  Daozha_id = 0;
    double sumLicheng = 0.0;
    boolean doorIn = false;
    Date timein = null;
    Date timeout = null;
    public volatile int orderid = 0;
    int preOrderid = 0;
    String opg = "";
    public String carname = "";
    int driveid = 0;
    Date preDateTime = null;
    Date curDateTime = null;
    int lastCrossDoorType = 0;
    int lastCrossDoorID = 0;
    double lastCrossDoorMile = 0.0;
    String lastCrossTime = "";
    int  lastCrossDaozha_id = 0;

    public int judge_inoutdoor_second = 60;

    public int getOldRoadType() {
        return oldRoadType;
    }

    public void setOldRoadType(int oldRoadType) {
        this.oldRoadType = oldRoadType;
    }

    public int getWarnDelayFlag() {
        return warnDelayFlag;
    }

    public void setWarnDelayFlag(int warnDelayFlag) {
        this.warnDelayFlag = warnDelayFlag;
    }

    //报警延时标志
    volatile int warnDelayFlag = 5;
    //上一帧所在路段
    volatile int oldRoadType = -1;
    //第一帧解析状态（0-开始，-1-未开始，1-完成）
    volatile int firstFlag = -1;
    //MQ
    volatile int MqFlag = 0;

    ConnectionFactory connFactory = new ActiveMQConnectionFactory("failover:(tcp://127.0.0.1:61616)?maxReconnectDelay=10&maxReconnectAttempts=1&timeout=20");

    Connection conn = null;
    Session sess = null;
    Destination dest = null;
    MessageProducer prod = null;

    //报警
    public int lastroad = 0;
    public int haswarning[] = new int[3];//0，没经过；1，经过

    public int notonlinecount = 0;
    public int isGPSOnline = 1;
    public Date GPSOfflineTime = null;

    public int ghcount = 0;

    public int maxworktime = 120;
    public int maxworktimetag = 0;
    public int maxrelaxtime = 12;
    public int maxrelaxtimetag = 0;
    public Date lastRelaxTime = null;


    //	@Override
//	public void userEventTriggered(final ChannelHandlerContext ctx, Object evt) throws Exception {
//		if(evt instanceof IdleStateEvent){
//			IdleStateEvent event = (IdleStateEvent)evt;
//			if(event.state() == IdleState.WRITER_IDLE){
//				System.out.println("发送OK");
//				//清除超时会话
//				ChannelFuture writeAndFlush = ctx.writeAndFlush("OK");
////				writeAndFlush.addListener(new ChannelFutureListener() {
////
////					@Override
////					public void operationComplete(ChannelFuture future) throws Exception {
////						ctx.channel().close();
////					}
////				});
//			}
//		}else{
//			super.userEventTriggered(ctx, evt);
//		}
//	}
    @Override
    protected void messageReceived(ChannelHandlerContext ctx, String msg) throws Exception {
        if (firstFlag == -1 || firstFlag == 1) {
            BusinessThreadUtil.doBusiness(ctx, msg, this);
        }
    }

    /**
     * 新客户端接入
     * <p>
     * channelActive()方法将会在连接被建立并且准备进行通信时被调用
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelActive");
        conn = connFactory.createConnection();
        sess = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
        String fa = "$time," + this.getStringDate() + "\r\n";
        String fa1 = new String(fa.getBytes(), "utf-8");
        System.out.println(fa1);
        ctx.channel().writeAndFlush(fa1);
//		信息交互
        fa = "$VoPlay,[V15]ok"+"\r\n";
        String fa2 = new String(fa.getBytes(), "utf-8");
        System.out.println(fa2);
        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        ctx.channel().writeAndFlush(fa2);
                    }
                }, 1000
        );
    }

    public String getStringDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;

    }

    /**
     * 客户端断开
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        sess.close();
        conn.close();
        System.out.println("channelInactive");
//		ctx.channel().writeAndFlush("off\r\n");
    }

    /**
     * 异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

    /**
     * 异或运算，用于异或校验
     */
    public static String getXor(byte[] datas) {
//        System.out.println(datas.length);
        byte temp = datas[0];
        for (int i = 1; i < datas.length; i++) {
            temp ^= datas[i];
//            System.out.println(datas[i]);
        }
        String check = Integer.toHexString(temp);
        return check;
    }
}


