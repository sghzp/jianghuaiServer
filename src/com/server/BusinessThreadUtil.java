package com.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.beans.CarInfo;
import com.beans.MileCountInfo;
import com.beans.WarningInfo;
import com.seu.database.CarInfoDao;
import com.seu.database.DAO;
import com.seu.database.JDBCTools;
import com.seu.tool.*;
import com.sun.xml.internal.ws.policy.EffectiveAlternativeSelector;
import com.sun.xml.internal.ws.resources.UtilMessages;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.dao.support.DaoSupport;
import sun.java2d.loops.GraphicsPrimitive;
import sun.plugin.security.StripClassFile;

import javax.jms.Message;
import javax.sql.rowset.JdbcRowSet;
import javax.xml.soap.SAAJResult;
import java.io.File;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.concurrent.*;


public class BusinessThreadUtil {

    static final int nThreads = Runtime.getRuntime().availableProcessors();


    //static LinkedList<String> roadOrderList;

    public static final ExecutorService executor = new ThreadPoolExecutor(nThreads * 2, nThreads * 3, 1L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(Integer.MAX_VALUE));//CPU核数4-10倍

    public static void doBusiness(ChannelHandlerContext ctx, String msg, ServerHandler handler) {
        System.out.println("核心数：" + nThreads);
        //异步线程池处理
//        executor.submit(() -> {
        System.out.println("开始解析");
        if (msg.startsWith("$BDBASE")) {
            handler.unKnowNum = msg.split(",")[1];
            System.out.println(handler.unKnowNum);
        }

        if (msg.startsWith("$") && msg.substring(msg.indexOf("$"), msg.lastIndexOf("#")).length() < 500) {
            //判断是否存在E盘，即是否挂载硬盘
            File f = new File("c:/");
            if (f.exists()) {
                CarInfo carInfo = new CarInfo();
                try {
                    carInfo = BusinessThreadUtil.saveData_DB(ctx, msg, handler);
                    if (carInfo != null) {
                        if (handler.orderid != 0 && handler.orderid != handler.preOrderid) {
                            handler.dest = handler.sess.createTopic(handler.orderid + "");
//                            handler.dest = handler.sess.createTopic("zfz");
                            System.out.println("MQ TOPIC: " + handler.orderid);
                            handler.prod = handler.sess.createProducer(handler.dest);
                        }
                        Message msg0 = handler.sess.createTextMessage(JSONObject.toJSONString(carInfo));
                        handler.prod.send(msg0);
                    }
                    handler.preOrderid = handler.orderid;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
//        });
    }

    public static String getStringDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
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

    public static CarInfo saveData_DB(ChannelHandlerContext ctx, String msg, ServerHandler handler) throws Exception {
        String tablename;
        CarInfo carInfo = new CarInfo();
        System.out.println(msg);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (msg.startsWith("$") && msg.endsWith("#")) {
            String substr = msg.substring(1, msg.length() - 1);
            String[] divide_str = substr.split(",");
            // 计算校验和;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < divide_str.length - 1; i++) {
                if (i != divide_str.length - 2) {
                    sb.append(divide_str[i]);
                    sb.append(",");
                } else {
                    sb.append(divide_str[i]);
                }
            }
            String str = sb.toString();
            byte[] check = str.getBytes();
            int sum_check = 0;
            for (int k = 0; k < check.length; k++) {
                sum_check += check[k];
            }
            int check_date = sum_check & 0x000000ff;
            String mysum = check_date + "";
            boolean check_result = false;
            if (check_date == Integer.parseInt(divide_str[divide_str.length - 1])) {
                check_result = true;
            } else {
                check_result = false;
            }
            if (check_result == false) {
                return null;
            }
            // 计算校验和---------end;

            tablename = "tb_experimentdata";
            boolean flag = JDBCTools.HasTable(tablename);
            if (!flag) {
                System.out.println("先创建表，因为没有对应的表存在");
                String sql = "create table " + tablename + "(id int auto_increment primary key," + "Instrument_id int,"
                        + " time timestamp not null  default CURRENT_TIMESTAMP ," + " longitude double," + " latitude double," + " gPS_speed double,"
                        + " heading double," + " airtemp double," + " airstress double," + " inairtemp  double,"
                        + " inairstress double," + "risestress double," + " oilstress double,"
                        + " carspeed double," + " enginespeed double," + " enginetorque double,"
                        + " drivertorque double," + " footplate double," + " engineload double,"
                        + " fastconsumption double," + " averageconsumption double,"
                        + " consumptionrate double," + " clutch double," + " stop double,"
                        + " engineneedtorque double," + " enginetorquemode double," + " frictiontorque double,"
                        + "firestress double," + " firetemp double," + " oiltemp double," + " coldtemp double,"
                        + " voltage double," + " enginetemp double," + " gaugespeed double,"
                        + " frontspeed double," + " changegear double," + " outair double,"
                        + " vehicleID varchar(30)," + " deviceID varchar(30)," + " sum varchar(30),"
                        + " mysum varchar(30)," + " check_result boolean," + " systime timestamp not null  default CURRENT_TIMESTAMP ,"
                        + " inter double," + " mile double," + " road_type int," + " PROJECT_NUM varchar(30)," + " PROJECT_TASK_ID varchar(30));";
                JDBCTools.createTable(sql);
            }
            CarInfoDao carInfoDao = new CarInfoDao();
            Object[] object = new Object[divide_str.length + 9];
            for (int i = 0; i < divide_str.length; i++) {
                if (i < 36) {
                    if (divide_str[i].equals("")) {
                        object[i] = null;
                    } else {
                        if (i == 0) {
                            Date client = simpleDateFormat.parse(divide_str[i]);
                            handler.curDateTime = simpleDateFormat.parse(divide_str[i]);
                            Date now = new Date();

                            long diff = Math.abs((client.getTime() - now.getTime()) / (1000 * 20));
                            object[i] = client;
                            if (diff > 5) {
                                ctx.writeAndFlush("$time," + simpleDateFormat.format(now) + "\r\n");
                            }
                        } else if (i == 1) {
                            handler.longitude = Double.parseDouble(divide_str[i]);
                            if (handler.longitude < 180) {
                                carInfo.setLongitude(handler.longitude);
                                /*longitude = GPS_transform(longitude);*/
                                object[i] = handler.longitude;
                            } else {
                                handler.longitude = (int) (handler.longitude / 100) + (handler.longitude - (int) (handler.longitude / 100) * 100) / 60;
                                carInfo.setLongitude(handler.longitude);
                                /*longitude = GPS_transform(longitude);*/
                                object[i] = handler.longitude;
                            }

                        } else if (i == 2) {
                            handler.latitude = Double.parseDouble(divide_str[i]);
                            if (handler.latitude < 90) {
                                carInfo.setLatitude(handler.latitude);
                                /*latitude = GPS_transform(latitude);*/
                                object[i] = handler.latitude;
                            } else {
                                handler.latitude = (int) (handler.latitude / 100) + (handler.latitude - (int) (handler.latitude / 100) * 100) / 60;
                                carInfo.setLatitude(handler.latitude);
                                /*latitude = GPS_transform(latitude);*/
                                object[i] = handler.latitude;
                            }

                        } else if (i == 3) {
                            handler.gPS_state = divide_str[i];
                            object[i] = divide_str[i];
                            carInfo.setGPS_state(handler.gPS_state);
//                            System.out.println(handler.gPS_state);
                        } else if (i == 4) {
                            if (Double.parseDouble(divide_str[1]) < 180) {
                                handler.gPS_speed = Double.parseDouble(divide_str[i]);
                                object[i] = Double.parseDouble(divide_str[i]);
                                carInfo.setgPS_speed(handler.gPS_speed);
                                System.out.println("GPSsudu " + handler.gPS_speed);
                            } else {
                                handler.gPS_speed = Double.parseDouble(divide_str[i]) * 0.514;
                                object[i] = Double.parseDouble(divide_str[i]) * 0.514;
                                carInfo.setgPS_speed(handler.gPS_speed);
                                System.out.println("GPSsudu " + handler.gPS_speed);
                            }
                        } else {
                            if (i > 5) { // 代表只有OBD的30个数据才需要除以100，其他的不用除；
                                object[i] = Double.parseDouble(divide_str[i]) / 100.0;
                                if (i == 12) {
                                    handler.carspeed = (double) object[i];
                                }

                            } else
                                object[i] = Double.parseDouble(divide_str[i]);
                        }
                    }
                } else {
                    object[i] = divide_str[i];
                }
            }
            handler.vehicleID = (String) divide_str[divide_str.length - 3];
            object[divide_str.length] = mysum;
            object[divide_str.length + 1] = check_result;
            object[divide_str.length + 2] = new Date();
            object[divide_str.length + 3] = handler.inter;

            /*************************获取订单号,驾驶员号,opg,最长工作，休息时间**************************/
            getOrderID(handler);
            /*************************获取订单号end**************************/
            if (handler.orderid != handler.preOrderid) {
                /*仅在第一次获取里程数据和道路位置数据*/
                handler.arr = road_info.road_data_info;

                /*************************设置标准数组，以及构造顺序标准**************************/
                SetStandard(handler);
                /*************************设置标准数组end**************************/
                /*************************插入一条默认的里程统计放在getOrderID（）后面**************************/
                InsertOrderMileDB(handler);
                /*************************插入一条默认的里程统计end**************************/
                //插入一条新gps里程数据
                setNewMile(handler);
                //设置道闸判定规范
                setDoorStandards(handler);
                //设置休息区判定规范
                setOutDoorStandards(handler);
                //获得上一帧的总里程
                handler.mile = Mile.PreMile(handler.orderid);
                //获得高环循环次数
                getghcount(handler);
            }


            carInfo.setOpg(handler.opg);
            //重置设备不在线为0
            setzero(handler);
            //判断设备GPS是否有信号，三分钟的时间
            judgeGPSState(handler, ctx);


            if (handler.startFlag == 0) {
                //第一帧初始化前面一帧的时间
                handler.preDateTime = simpleDateFormat.parse(divide_str[0]);
                readJudgeInOutDoorSecond(handler);
                handler.startFlag = 1;
            }
            double timeDiff = (double) (handler.curDateTime.getTime() - handler.preDateTime.getTime()) / (1000.0);
            System.out.println("时间差：" + timeDiff);
            System.out.println("上一帧时间：" + handler.preDateTime);
            System.out.println("这帧时间：" + handler.curDateTime);
            double speedtemp = handler.gPS_speed / 1000;//km/s
            double mileSec = 0;//每秒里程
            if (timeDiff > 0 && timeDiff < 300) {
                mileSec = speedtemp * timeDiff;
                System.out.println("每秒里程差: " + mileSec);
                handler.mile += mileSec;

                //先获取前一帧的进出闸状态
                getLastCrossDoorState(handler);
                //判断驾驶员工作时间是否太长
                judgeWorkTime(handler, ctx);
                //判断驾驶员在高环闸内的休息时间不能太久
                judgeRelaxTime(handler, ctx);
            }
            object[divide_str.length + 4] = handler.mile;

            long t1 = System.currentTimeMillis(); // 代码执行前时间
            //这里要获得车辆信息的*************************************
            carInfo = Warning_judge.warningJudge(timeDiff, ctx, "tb_warning", object[0], carInfo, handler);

            long t2 = System.currentTimeMillis(); // 代码执行后时间
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(t2 - t1);
//            System.out.println("$$$$$$$耗时: " + c.get(Calendar.MINUTE) + "分 " + c.get(Calendar.SECOND) + "秒 " + c.get(Calendar.MILLISECOND) + " 微秒");


            object[divide_str.length + 5] = carInfo.getRoadType();
            object[divide_str.length + 6] = "-1";
            object[divide_str.length + 7] = handler.orderid + "";//这个就是订单
            object[divide_str.length + 8] = "没有任务概念了";

            System.out.println(handler.unKnowNum);
            carInfo.setAll(object);
            if (timeDiff > 0 && timeDiff < 300) {
                if (handler.orderid != 0) {
                    if (carInfo.getGPS_state().split(" ").length == 2) {
                        double curmile = Double.parseDouble(carInfo.getGPS_state().split(" ")[0]) / 1000.0;
                        getPreMile(handler);//查上一帧里程,设备号和订单号。
                        getMile(handler);
                        if (curmile < handler.preMile) {    //说明重新开机了
                            setPreMile(handler, curmile);  //更新前一帧里程，同时直接累加总里程
                            sumMilegps(handler, curmile);
                            object[divide_str.length + 3] = curmile + handler.totalmilegps;
                            handler.inter = curmile + handler.totalmilegps;
                        } else {
                            setPreMile(handler, curmile);
                            sumMilegps(handler, curmile - handler.preMile);//更新前一帧里程，作差同时累加总里程
                        }
                        object[divide_str.length + 3] = curmile - handler.preMile + handler.totalmilegps;
                        handler.inter = curmile - handler.preMile + handler.totalmilegps;
                    }
                }
                carInfoDao.InsertAll(tablename, object);
            }
            /********************累积总里程开始*********************/
            if (timeDiff > 0 && timeDiff < 300) {
                if (handler.orderid == 0) {
                    System.out.println("没有订单号无法累积里程");
                } else {
                    String sqlZonglicheng = "UPDATE tb_orderMileage " +
                            "SET totalMileage = totalMileage+" + mileSec + " WHERE Equipment_num = '" + handler.vehicleID + "' AND orderid = " + handler.orderid + "";
                    DBO dbol = new DBO();
                    try {
                        dbol.executeUpdate(sqlZonglicheng);
                        System.out.println("总里程累积成功");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        dbol.close();
                    }
                }
            }
            /********************累积总里程结束*********************/
            /********************判断点是否经过道闸*********************/
            isInDoor(handler, timeDiff, simpleDateFormat);
            isOutDoor(handler, simpleDateFormat, ctx);
            /********************判断点是否经过道闸结束*********************/
            if (handler.unKnowNum != "") {
                ctx.writeAndFlush(handler.unKnowNum);
            } else {
                //ctx.writeAndFlush("OK"); // (3)
            }
            handler.preDateTime = handler.curDateTime;//当前的时间置给上一帧
        } else if (msg.startsWith("*")) {
            System.out.println("心跳包----------------------------------" + msg);
        }
        return carInfo;
    }

    public static void judgeRelaxTime(ServerHandler handler, ChannelHandlerContext ctx) throws Exception {
        if (handler.gPS_speed < 1) {
            if (handler.lastCrossDaozha_id == 1 && handler.lastCrossDoorType == 1 && handler.maxrelaxtimetag == 0) {
                if (handler.lastRelaxTime == null) {
                    //第一次停下来
                    handler.lastRelaxTime = new Date();
                } else {
                    Date now = new Date();
                    long nowLong = now.getTime();
                    long lastLong = handler.lastRelaxTime.getTime();
                    long timeDiff = nowLong - lastLong;
                    int timeDiffInt = Integer.parseInt(String.valueOf(timeDiff));
                    if (timeDiffInt >= handler.maxrelaxtime * 60 * 1000) {
                        String fa = "$VoPlay,高环内静止时长超限的报警\r\n";
                        ctx.channel().writeAndFlush(fa);
                        Connection conn = null;
                        PreparedStatement ps = null;
                        String uuid = null;
                        try {
                            conn = JDBCTools.getConnection();
                            String sql = "insert into tb_warning (WARNING_ID,CURRENT_ROAD_TYPE,VIN,WARNING_TYPE,LONGITUDE,LATITUDE,GPS_SPEED,SPEED,TIME,orderid,PROJECT_TASK_ID)values(?,?,?,?,?,?,?,?,?,?,?)";
                            System.out.println(sql);
                            uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
                            ps = conn.prepareStatement(sql);
                            ps.setString(1, uuid);
                            ps.setInt(2, timeDiffInt / 60000);
                            ps.setString(3, handler.vehicleID);
                            ps.setInt(4, 7);//错误状态码
                            ps.setDouble(5, handler.longitude);
                            ps.setDouble(6, handler.latitude);
                            ps.setDouble(7, handler.gPS_speed);
                            ps.setDouble(8, 0);
                            ps.setObject(9, now);
                            ps.setInt(10, handler.orderid);
                            ps.setString(11, handler.lastRelaxTime.toString());
                            ps.executeUpdate();

                            WarningInfo warningInfo = new WarningInfo();
                            warningInfo.WARNING_ID = uuid;
                            warningInfo.time = xStr.simpleDateFormat.format(now);
                            warningInfo.gPS_speed = handler.gPS_speed;
                            warningInfo.warning_type = 7;
                            warningInfo.latitude = handler.latitude;
                            warningInfo.longitude = handler.longitude;
                            warningInfo.orderid = handler.orderid;
                            warningInfo.road_name = "";
                            warningInfo.road_type = 0;
                            warningInfo.VIN = handler.vehicleID;
                            warningInfo.carname = handler.carname;
                            Warning_judge.sm.addQue(warningInfo);


                        } catch (SQLException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } finally {
                            JDBCTools.free(null, ps, conn);
                        }
                        handler.maxrelaxtimetag = 1;
                    }
                }
            }
        } else {
            handler.lastRelaxTime = null;
        }
    }

    static Thread thread = null;
    public static void judgeWorkTime(ServerHandler handler, ChannelHandlerContext ctx) throws Exception {
        if (handler.gPS_speed > 1) {
            if (handler.lastCrossDoorType == 1 && handler.maxworktimetag == 0) {
                Date now = new Date();
                long nowLong = now.getTime();
                long last = xStr.simpleDateFormat.parse(handler.lastCrossTime).getTime();
                long timeDiff = nowLong - last;
                int timeDiffInt = Integer.parseInt(String.valueOf(timeDiff));//long转int
                if (timeDiffInt >= (handler.maxworktime - 15) * 60 * 1000 && timeDiffInt <= handler.maxworktime * 60 * 1000) {
                    if (thread == null) {
                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String fa = "$VoPlay,疲劳驾驶\r\n";
                                ctx.channel().writeAndFlush(fa);
                                try {
                                    Thread.sleep(10 * 1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        thread.start();
                    }
                } else if (timeDiffInt >= handler.maxworktime * 60 * 1000 && timeDiffInt <= 300 * 60 * 1000) {
                    thread.interrupt();
                    thread = null;
                    String fa = "$VoPlay,道闸内时长超限报警\r\n";
                    ctx.channel().writeAndFlush(fa);
                    Connection conn = null;
                    PreparedStatement ps = null;
                    String uuid = null;
                    try {
                        conn = JDBCTools.getConnection();
                        String sql = "insert into tb_warning (WARNING_ID,CURRENT_ROAD_TYPE,VIN,WARNING_TYPE,LONGITUDE,LATITUDE,GPS_SPEED,SPEED,TIME,orderid,PROJECT_TASK_ID)values(?,?,?,?,?,?,?,?,?,?,?)";
                        System.out.println(sql);
                        uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
                        ps = conn.prepareStatement(sql);
                        ps.setString(1, uuid);
                        ps.setInt(2, timeDiffInt / 60000);
                        ps.setString(3, handler.vehicleID);
                        ps.setInt(4, 6);
                        ps.setDouble(5, handler.longitude);
                        ps.setDouble(6, handler.latitude);
                        ps.setDouble(7, handler.gPS_speed);
                        ps.setDouble(8, handler.lastCrossDoorID);//暂存上一次进闸的ID
                        ps.setObject(9, now);
                        ps.setInt(10, handler.orderid);
                        ps.setString(11, handler.lastCrossTime);
                        ps.executeUpdate();


                        WarningInfo warningInfo = new WarningInfo();
                        warningInfo.WARNING_ID = uuid;
                        warningInfo.time = xStr.simpleDateFormat.format(now);
                        warningInfo.gPS_speed = handler.gPS_speed;
                        warningInfo.warning_type = 6;
                        warningInfo.latitude = handler.latitude;
                        warningInfo.longitude = handler.longitude;
                        warningInfo.orderid = handler.orderid;
                        warningInfo.road_name = "";
                        warningInfo.road_type = 0;
                        warningInfo.VIN = handler.vehicleID;
                        warningInfo.carname = handler.carname;
                        Warning_judge.sm.addQue(warningInfo);


                    } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } finally {
                        JDBCTools.free(null, ps, conn);
                    }
                    handler.maxworktimetag = 1;
                }
            }else {
                if(thread != null){
                    thread.interrupt();
                    thread = null;
                }
            }
        }
    }

    public static void judgeGPSState(ServerHandler handler, ChannelHandlerContext ctx) {
        if (handler.longitude == handler.lastlongitude && handler.latitude == handler.lastlatitude) {
            //不在线
            if (handler.GPSOfflineTime == null) {
                //第一次遇到没信号
                handler.GPSOfflineTime = new Date();
            } else {
                //前面已经有了没信号
                if (handler.isGPSOnline == 1 && new Date().getTime() - handler.GPSOfflineTime.getTime() > 1000 * 60 * 3) {
                    String fa = "$VoPlay,设备无GPS信号，请检查GPS天线或手动重启设备开关\r\n";
                    ctx.channel().writeAndFlush(fa);
                    String fa2 = "$ResetGPS,1\n";
                    ctx.channel().writeAndFlush(fa2);
                    Connection conn = null;
                    PreparedStatement ps = null;
                    String uuid = null;
                    try {
                        conn = JDBCTools.getConnection();
                        String sql = "insert into tb_warning (WARNING_ID,CURRENT_ROAD_TYPE,VIN,WARNING_TYPE,LONGITUDE,LATITUDE,GPS_SPEED,SPEED,TIME,orderid)values(?,?,?,?,?,?,?,?,?,?)";
                        System.out.println(sql);
                        uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
                        ps = conn.prepareStatement(sql);
                        ps.setString(1, uuid);
                        ps.setInt(2, 0);
                        ps.setString(3, handler.vehicleID);
                        ps.setInt(4, 8);
                        ps.setDouble(5, handler.longitude);
                        ps.setDouble(6, handler.latitude);
                        ps.setDouble(7, handler.gPS_speed);
                        ps.setDouble(8, handler.carspeed);
                        ps.setObject(9, xStr.simpleDateFormat.format(handler.GPSOfflineTime));
                        ps.setInt(10, handler.orderid);
                        ps.executeUpdate();

                        WarningInfo warningInfo = new WarningInfo();
                        warningInfo.WARNING_ID = uuid;
                        warningInfo.time = xStr.simpleDateFormat.format(handler.GPSOfflineTime);
                        warningInfo.gPS_speed = handler.gPS_speed;
                        warningInfo.warning_type = 8;
                        warningInfo.latitude = handler.latitude;
                        warningInfo.longitude = handler.longitude;
                        warningInfo.orderid = handler.orderid;
                        warningInfo.road_name = "";
                        warningInfo.road_type = 0;
                        warningInfo.VIN = handler.vehicleID;
                        warningInfo.carname = handler.carname;
                        Warning_judge.sm.addQue(warningInfo);

                    } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } finally {
                        JDBCTools.free(null, ps, conn);
                    }
                    handler.isGPSOnline = 0;
                }
            }
        } else {
            //在线
            handler.GPSOfflineTime = null;
            handler.isGPSOnline = 1;
        }
        handler.lastlongitude = handler.longitude;
        handler.lastlatitude = handler.latitude;
    }

    public static void readJudgeInOutDoorSecond(ServerHandler handler){
        String sql = "select judge_inoutdoor_second from tb_settings where id = 1";
        DAO dao = new DAO();
        handler.judge_inoutdoor_second = dao.getForValue(sql);
    }

    public static void isInDoor(ServerHandler handler, double timeDiff, SimpleDateFormat simpleDateFormat) throws ParseException {
        int road_segment_a = 0;
        int road_type_a = 0;
        int road_segment_b = 0;
        int road_type_b = 0;
        boolean isonroad = false;
        double road_longitude = 0;
        double road_latitude = 0;
        ArrayList<Double> polygonXA = new ArrayList<Double>();
        ArrayList<Double> polygonYA = new ArrayList<Double>();
        int length = handler.arrDoors.size();
        map_point_judge mpj = new map_point_judge();
        //1、首先获取每个数据点的道路段ID和道路ID，如果一直一致，那么就存入list中。
        for (int j = 0; j <= (length - 2); j++) {
            road_segment_a = handler.arrDoors.get(j).p_name;
            road_segment_b = handler.arrDoors.get(j + 1).p_name;
            road_type_a = handler.arrDoors.get(j).p_road;
            road_type_b = handler.arrDoors.get(j + 1).p_road;
            road_longitude = handler.arrDoors.get(j).lon;
            road_latitude = handler.arrDoors.get(j).lat;
            if ((road_segment_a == road_segment_b) && (road_type_a == road_type_b)) {
                //将当前的路段位置信息存入链表
                polygonXA.add(road_longitude); //经度
                polygonYA.add(road_latitude); //纬度
            } else {
                polygonXA.add(road_longitude); //经度
                polygonYA.add(road_latitude); //纬度
                if (j == (length - 2)) {
                    polygonXA.add(handler.arrDoors.get(j + 1).lon); //经度
                    polygonYA.add(handler.arrDoors.get(j + 1).lat); //纬度
                }
                //根据目前的position以及road_judge
                isonroad = mpj.isPointInPolygon(handler.longitude, handler.latitude, polygonXA, polygonYA);
                if (isonroad) {
                    if (handler.orderid != 0 && timeDiff >= 1) {
                        //有订单号获得上一帧的状态，可能为空（表示你第一次进来），如果不为空获得最近的一帧数据
                        getLastCrossDoorState(handler);
                        if (handler.lastCrossDoorType == 0//表示没有任何数据
                                //表示上次是出闸所以这次是进闸
                                || (handler.lastCrossDoorType == 2 && (handler.curDateTime.getTime() - simpleDateFormat.parse(handler.lastCrossTime).getTime()) > 1000 * handler.judge_inoutdoor_second)
                                //表示第二天了就一定从进闸开始，如果出现前一天最后是出闸的异常情况
                                || (handler.lastCrossDoorType == 1 && !xStr.isSameDay(handler.curDateTime.getTime(), simpleDateFormat.parse(handler.lastCrossTime).getTime()))
                        ) {
                            //第一次进闸
                            handler.timein = handler.curDateTime;
                            handler.Daozha_id = handler.arrDoors.get(j).p_road;
                            insertCrossDoorLog(handler, simpleDateFormat.format(handler.timein), 1);
                        } else if (handler.lastCrossDoorType == 1 && (handler.curDateTime.getTime() - simpleDateFormat.parse(handler.lastCrossTime).getTime()) > 1000 * handler.judge_inoutdoor_second) {
                            Connection conn = null;
                            PreparedStatement ps = null;
                            handler.timeout = handler.curDateTime;
                            handler.Daozha_id = handler.arrDoors.get(j).p_road;
                            handler.sumLicheng = handler.inter - handler.lastCrossDoorMile;
                            handler.sumLicheng = handler.sumLicheng > 0 ? handler.sumLicheng : 0;
                            try {
                                conn = JDBCTools.getConnection();
                                String slq4 = "INSERT INTO tb_MileageCount(Daozha_ID, orderid, driveid, timein, timeout, Mileage) VALUES (?,?,?,?,?,?);";
                                ps = conn.prepareStatement(slq4);
                                ps.setInt(1, handler.Daozha_id);
                                ps.setInt(2, handler.orderid);
                                ps.setInt(3, handler.driveid);
                                Object time = new java.sql.Timestamp((simpleDateFormat.parse(handler.lastCrossTime)).getTime());
                                ps.setObject(4, time);
                                Object timeout = new java.sql.Timestamp(handler.timeout.getTime());
                                ps.setObject(5, timeout);
                                ps.setDouble(6, handler.sumLicheng);
                                ps.executeUpdate();
                                System.out.println("道闸里程累积成功");
                                MileCountInfo mileCountInfo = new MileCountInfo();
                                mileCountInfo.Daozha_ID = handler.Daozha_id;
                                mileCountInfo.orderid = handler.orderid;
                                mileCountInfo.driveid = handler.driveid;
                                mileCountInfo.Mileage = handler.sumLicheng;
                                mileCountInfo.timeout = simpleDateFormat.format(handler.timeout);
                                mileCountInfo.timein = handler.lastCrossTime;
                                handler.sumLicheng = 0;
                                insertCrossDoorLog(handler, mileCountInfo.timeout, 2);
                                Warning_judge.sm.addQue(mileCountInfo);
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                JDBCTools.free(null, ps, conn);
                            }
                        }
                    }
                    break;
                }
                polygonXA.clear();
                polygonYA.clear();
            }
        }
    }

    public static void isOutDoor(ServerHandler handler, SimpleDateFormat simpleDateFormat, ChannelHandlerContext ctx) throws ParseException {
        int road_segment_a = 0;
        int road_type_a = 0;
        int road_segment_b = 0;
        int road_type_b = 0;
        boolean isonroad = false;
        double road_longitude = 0;
        double road_latitude = 0;
        ArrayList<Double> polygonXA = new ArrayList<Double>();
        ArrayList<Double> polygonYA = new ArrayList<Double>();
        int length = handler.arrOutDoors.size();
        map_point_judge mpj = new map_point_judge();
        //1、首先获取每个数据点的道路段ID和道路ID，如果一直一致，那么就存入list中。
        for (int j = 0; j <= (length - 2); j++) {
            road_segment_a = handler.arrOutDoors.get(j).p_name;
            road_segment_b = handler.arrOutDoors.get(j + 1).p_name;
            road_type_a = handler.arrOutDoors.get(j).p_road;
            road_type_b = handler.arrOutDoors.get(j + 1).p_road;
            road_longitude = handler.arrOutDoors.get(j).lon;
            road_latitude = handler.arrOutDoors.get(j).lat;
            if ((road_segment_a == road_segment_b) && (road_type_a == road_type_b)) {
                //将当前的路段位置信息存入链表
                polygonXA.add(road_longitude); //经度
                polygonYA.add(road_latitude); //纬度
            } else {
                polygonXA.add(road_longitude); //经度
                polygonYA.add(road_latitude); //纬度
                if (j == (length - 2)) {
                    polygonXA.add(handler.arrOutDoors.get(j + 1).lon); //经度
                    polygonYA.add(handler.arrOutDoors.get(j + 1).lat); //纬度
                }
                //根据目前的position以及road_judge
                isonroad = mpj.isPointInPolygon(handler.longitude, handler.latitude, polygonXA, polygonYA);
                if (isonroad) {

                    //休息区出闸补充判断
                    getLastCrossDoorState(handler);
                    /*if (handler.orderid != 0 && handler.lastCrossDoorType == 1) {
                        Connection conn = null;
                        PreparedStatement ps = null;
                        handler.timeout = handler.curDateTime;
                        handler.Daozha_id = handler.arrDoors.get(j).p_road;
                        handler.sumLicheng = handler.inter - handler.lastCrossDoorMile;
                        handler.sumLicheng = handler.sumLicheng > 0 ? handler.sumLicheng : 0;
                        try {
                            conn = JDBCTools.getConnection();
                            String slq4 = "INSERT INTO tb_MileageCount(Daozha_ID, orderid, driveid, timein, timeout, Mileage) VALUES (?,?,?,?,?,?);";
                            ps = conn.prepareStatement(slq4);
                            ps.setInt(1, handler.Daozha_id);
                            ps.setInt(2, handler.orderid);
                            ps.setInt(3, handler.driveid);
                            Object time = new java.sql.Timestamp((simpleDateFormat.parse(handler.lastCrossTime)).getTime());
                            ps.setObject(4, time);
                            Object timeout = new java.sql.Timestamp(handler.timeout.getTime());
                            ps.setObject(5, timeout);
                            ps.setDouble(6, handler.sumLicheng);
                            ps.executeUpdate();
                            System.out.println("道闸里程累积成功");
                            MileCountInfo mileCountInfo = new MileCountInfo();
                            mileCountInfo.Daozha_ID = handler.Daozha_id;
                            mileCountInfo.orderid = handler.orderid;
                            mileCountInfo.driveid = handler.driveid;
                            mileCountInfo.Mileage = handler.sumLicheng;
                            mileCountInfo.timeout = simpleDateFormat.format(handler.timeout);
                            mileCountInfo.timein = handler.lastCrossTime;
                            handler.sumLicheng = 0;
                            insertCrossDoorLog(handler, mileCountInfo.timeout, 2);
                            Warning_judge.sm.addQue(mileCountInfo);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            JDBCTools.free(null, ps, conn);
                        }
                    }*/

                    //判断速度是否超过，超过就推送超速报警
                    if (handler.gPS_speed * 3.6 > handler.outDoorsMaxSpeed.get(handler.arrOutDoors.get(j).p_name) && handler.gPS_speed * 3.6 < 170) {
                        //报警
                        String fa = "$VoPlay,休息区超速报警\r\n";
                        ctx.channel().writeAndFlush(fa);
                        Connection conn = null;
                        PreparedStatement ps = null;
                        String uuid = null;
                        try {
                            conn = JDBCTools.getConnection();
                            String sql = "insert into tb_warning (WARNING_ID,CURRENT_ROAD_TYPE,VIN,WARNING_TYPE,LONGITUDE,LATITUDE,GPS_SPEED,SPEED,TIME,orderid)values(?,?,?,?,?,?,?,?,?,?)";
                            System.out.println(sql);
                            uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
                            ps = conn.prepareStatement(sql);
                            ps.setString(1, uuid);
                            ps.setInt(2, handler.arrOutDoors.get(j).p_name);//道路类型
                            ps.setString(3, handler.vehicleID);
                            ps.setInt(4, 1);
                            ps.setDouble(5, handler.longitude);
                            ps.setDouble(6, handler.latitude);
                            ps.setDouble(7, handler.gPS_speed);
                            ps.setDouble(8, handler.carspeed);
                            ps.setObject(9, new Date());
                            ps.setInt(10, handler.orderid);
                            ps.executeUpdate();
                        } catch (SQLException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } finally {
                            JDBCTools.free(null, ps, conn);
                        }
                    }
                }
                polygonXA.clear();
                polygonYA.clear();
            }
        }
    }

    public static void setzero(ServerHandler handler) {
        if (handler.orderid == 0) return;
        String sql = "update tb_equipment set notonlinecount = 0 where orderid = ?";
        DAO dao = new DAO();
        dao.update(sql, handler.orderid);
    }

    public static void sumMilegps(ServerHandler handler, double mile) {
        if (handler.orderid == 0) return;
        String sql = "update tb_ordermilegps set totalmilegps = totalmilegps + ? where orderid = ? and equipment_num = ?";
        DAO dao = new DAO();
        Object object[] = new Object[3];
        object[0] = mile;
        object[1] = handler.orderid;
        object[2] = handler.vehicleID;
        dao.update(sql, object);
    }

    public static void setPreMile(ServerHandler handler, double mile) {
        if (handler.orderid == 0) return;
        String sql = "update tb_ordermilegps set premile = ? where orderid = ? and equipment_num = ?";
        DAO dao = new DAO();
        Object object[] = new Object[3];
        object[0] = mile;
        object[1] = handler.orderid;
        object[2] = handler.vehicleID;
        dao.update(sql, object);
    }

    public static void getPreMile(ServerHandler handler) {
        if (handler.orderid == 0) return;
        String sql = "select premile from tb_ordermilegps where orderid = ? and equipment_num = ?";
        DAO dao = new DAO();
        handler.preMile = dao.getForValue(sql, handler.orderid, handler.vehicleID);
    }

    public static void getMile(ServerHandler handler) {
        if (handler.orderid == 0) return;
        String sql = "select totalmilegps from tb_ordermilegps where orderid = ? and equipment_num = ?";
        DAO dao = new DAO();
        handler.totalmilegps = dao.getForValue(sql, handler.orderid, handler.vehicleID);
    }

    public static void getghcount(ServerHandler handler) {
        if (handler.orderid == 0) return;
        String sql = "select ghcount from tb_ordermilegps where orderid = ? and equipment_num = ?";
        DAO dao = new DAO();
        handler.ghcount = dao.getForValue(sql, handler.orderid, handler.vehicleID);
    }

    //获得当前订单下道闸的上一帧的里程以及进出道闸状态
    public static void getLastCrossDoorState(ServerHandler handler) {
        if (handler.orderid == 0) return;
        String sql = "select id,type,mile,time,daozha_id from tb_crossdoorlog where orderid = " + handler.orderid + " order by id desc limit 1";
        DBO dbo = new DBO();
        ResultSet rs = null;
        try {
            rs = dbo.query(sql);
            while (rs.next()) {
                handler.lastCrossDoorID = rs.getInt("id");
                handler.lastCrossDoorType = rs.getInt("type");
                handler.lastCrossDoorMile = rs.getDouble("mile");
                handler.lastCrossTime = rs.getString("time");
                handler.lastCrossDaozha_id = rs.getInt("daozha_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbo.close();
        }
    }

    //设置道闸判定规范
    public static void setDoorStandards(ServerHandler handler) {
        DBO db = new DBO();//初始链接数据库类
        ResultSet rs = null;
        String sql2 = "SELECT * FROM tb_judgedoors";
        try {
            //存储道闸位置
            rs = db.query(sql2);
            int k;
            while (rs.next()) {
                for (int i = 1; i < 6; i++) {
                    road_data r = new road_data();
                    r.setId(rs.getInt("Daozha_ID"));
                    r.setRoad_name(rs.getString("Road_id"));
                    if (i == 5) k = 1;
                    else k = i;
                    r.setP_name(rs.getInt("Daozha_ID"));
                    r.setP_road(rs.getInt("Daozha_ID"));
                    r.setLat(rs.getDouble("lat" + k));
                    r.setLon(rs.getDouble("lon" + k));
                    handler.arrDoors.add(r);
                }
            }
            //多存一帧，因为后面判断的时候长度会不判断最后一帧
            for (int i = 1; i < 6; i++) {
                road_data r = new road_data();
                r.setId(10000);
                r.setRoad_name("zfz");
                if (i == 5) k = 1;
                else k = i;
                r.setP_name(10000);
                r.setP_road(10000);
                r.setLat(1.0);
                r.setLon(1.0);
                handler.arrDoors.add(r);
            }//道闸结束
        } catch (SQLException e) {
            e.printStackTrace();
        }//查询结果
        finally {
            db.close();
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //设置休息区判定规范
    public static void setOutDoorStandards(ServerHandler handler) {
        DBO db = new DBO();//初始链接数据库类
        ResultSet rs = null;
        String sql2 = "SELECT * FROM tb_judgeoutdoors";
        try {
            //存储道闸位置
            rs = db.query(sql2);
            int k;
            while (rs.next()) {
                handler.outDoorsMaxSpeed.put(rs.getInt("ROADID"), rs.getDouble("maxspeed"));
                for (int i = 1; i < 6; i++) {
                    road_data r = new road_data();
                    r.setId(rs.getInt("ROADID"));
                    r.setRoad_name(rs.getString("Road_id"));
                    if (i == 5) k = 1;
                    else k = i;
                    r.setP_name(rs.getInt("ROADID"));
                    r.setP_road(rs.getInt("ROADID"));
                    r.setLat(rs.getDouble("lat" + k));
                    r.setLon(rs.getDouble("lon" + k));
                    handler.arrOutDoors.add(r);
                }
            }
            //多存一帧，因为后面判断的时候长度会不判断最后一帧
            for (int i = 1; i < 6; i++) {
                road_data r = new road_data();
                r.setId(10000);
                r.setRoad_name("zfz");
                if (i == 5) k = 1;
                else k = i;
                r.setP_name(10000);
                r.setP_road(10000);
                r.setLat(1.0);
                r.setLon(1.0);
                handler.arrOutDoors.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }//查询结果
        finally {
            db.close();
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //插入道闸进出记录，订单号不为零
    public static void insertCrossDoorLog(ServerHandler handler, String time, int type) {
        if (handler.orderid == 0) return;
        Connection conn = null;
        PreparedStatement ps = null;
        String sql = "INSERT INTO `ycdaams_test`.`tb_crossdoorlog`( `daozha_id`, `orderid`, `time`, `type`, `mile`) VALUES (?,?,?,?,?);\n";
        try {
            conn = JDBCTools.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, handler.Daozha_id);
            ps.setInt(2, handler.orderid);
            Object timestamp = new java.sql.Timestamp(xStr.simpleDateFormat.parse(time).getTime());
            ps.setObject(3, timestamp);
            ps.setInt(4, type);
            ps.setDouble(5, handler.inter);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCTools.free(null, ps, conn);
        }
    }

    //插入一条默认gps里程
    public static void setNewMile(ServerHandler handler) {
        if (handler.orderid == 0) return;
        String sql1 = "SELECT count(id) num FROM tb_ordermilegps WHERE orderid = ? and Equipment_num = ?";
        String sql2 = "INSERT INTO tb_ordermilegps(orderid,Equipment_num) VALUES (?,?);";
        DAO dao = new DAO();
        Object o[] = new Object[2];
        o[0] = handler.orderid;
        o[1] = handler.vehicleID;
        long num = dao.getForValue(sql1, o[0], o[1]);
        if (num == 0) {
            dao.update(sql2, o);
        }
    }

    //插入一条默认里程数据，订单号不为零
    public static void InsertOrderMileDB(ServerHandler handler) {
        if (handler.orderid == 0) return;
        String sql3 = "SELECT count(id) num FROM tb_orderMileage WHERE orderid = '" + handler.orderid + "' and Equipment_num = '" + handler.vehicleID + "'";
        String sql4 = "INSERT INTO tb_orderMileage(Equipment_num, orderid) VALUES (?,?);";
        //更新总里程
        DBO db = new DBO();
        DAO dao = new DAO();
        try {
            ResultSet rs = db.query(sql3);
            int count = 0;
            while (rs.next()) {
                count = rs.getInt("num");
            }//查询有没有这样的数据没有就初始化
            if (count == 0) {
                Object object[] = new Object[2];
                object[0] = handler.vehicleID;
                object[1] = handler.orderid;
                dao.update(sql4, object);
                System.out.println("插入一条新里程统计");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();

        }
    }

    //查询订单号
    public static void getOrderID(ServerHandler handler) {
        String sql = "SELECT a.carname,a.orderid,a.driveid,a.opg,a.maxworktime,a.maxrelaxtime FROM tb_equipment a where a.EQUIPMENT_NUM = '" + handler.vehicleID + "'";
        DBO dbo = new DBO();
        try {
            ResultSet rs = dbo.query(sql);
            while (rs.next()) {
                handler.orderid = rs.getInt("orderid");
                handler.driveid = rs.getInt("driveid");
                handler.opg = rs.getString("opg");
                handler.carname = rs.getString("carname");
                handler.maxworktime = rs.getInt("maxworktime");
                handler.maxrelaxtime = rs.getInt("maxrelaxtime");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbo.close();
        }
    }

    //设置规范，以及构造顺序规范的list
    public static void SetStandard(ServerHandler handler) {
        handler.roadOrderList.clear();
        handler.speed_arr.clear();
        String sql0 = "SELECT * FROM tb_roads ORDER BY road_id;";//查询道路列表
        String sql1 = "SELECT b.CONTENT FROM tb_equipment a,tb_standards b " +
                "where a.EQUIPMENT_NUM = '" + handler.vehicleID + "' and a.EQUIPMENT_stantard_ID = b.STANDARD_ID";//查询 通知sql语句，；
        String standardJSON = "";

        handler.roadOrderList = new LinkedList<>();
        String road = "";
        DBO db = new DBO();
        try {
            //查出路段列表，组成判断标准的列表
            ResultSet rs = db.query(sql0);
            while (rs.next()) {
                speed_data r = new speed_data();
                r.setSpeed_max(300);
                r.setSpeed_min(-1);
                r.setRoad_name(rs.getString("ROADNAME"));
                handler.speed_arr.put(rs.getString("ROADNAME"), r);
            }
            //获得标准规范
            rs = db.query(sql1);
            String speed_min = "";
            String speed_max = "";
            while (rs.next()) {
                standardJSON = rs.getString("CONTENT");
//							System.out.println(standardJSON);
                //解析为json对象  start
                JSONArray jsonArray = JSON.parseArray(standardJSON);
                for (Object obj : jsonArray) {
                    JSONObject jsonObject = (JSONObject) obj;
                    road = jsonObject.getString("ROAD");
                    handler.roadOrderList.offer(road);//根据content构造标准顺序队列
                    speed_min = jsonObject.getString("MIN");
                    speed_max = jsonObject.getString("MAX");
                    handler.speed_arr.get(road).setSpeed_max(Double.parseDouble(speed_max));
                    handler.speed_arr.get(road).setSpeed_min(Double.parseDouble(speed_min));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }
}
