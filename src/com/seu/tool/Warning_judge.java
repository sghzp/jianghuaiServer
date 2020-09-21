package com.seu.tool;

import com.beans.CarInfo;
import com.beans.WarningInfo;
import com.server.BusinessThreadUtil;
import com.server.ServerHandler;
import com.seu.database.DAO;
import com.seu.database.JDBCTools;
import io.netty.channel.ChannelHandlerContext;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.ValueInputStream;

import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;


public class Warning_judge {

    public final static SendMsg sm = new SendMsg();
    public static HttpClient httpClient = new HttpClient();


    public static CarInfo warningJudge(double timeDiff, ChannelHandlerContext ctx, String tablename, Object time, CarInfo carInfo, ServerHandler handler) {
        String fa = null;
        double local_speed = 0;
        if (handler.carspeed == -1 || handler.carspeed == 0 || handler.carspeed == 255) {
            if (handler.gPS_speed > 1) {
                local_speed = handler.gPS_speed * 3.6;
            }
        } else {
            local_speed = handler.carspeed;
        }
        /* ******************************************************************
         *1、point_info中的信息已经存储在了静态数据中。需要将传递过来的位置信息做搜索。
         * 通过传入车辆当前位置信息local_position_lon,local_position_lat,遍历路段点
         * 静态数据来确定属于的道路类型。
         * 输入参数：local_position_lon，local_position_lat
         * 输出参数：road_type
         *******************************************************************/
        int i = 0;//遍历循环量
        int flag = 0; //如果找到了目标值就要置位。
        int road_type = 0;//道路类型。
        String road_name = "";//道路类型。
        int road_segment_a = 0;
        int road_type_a = 0;
        int road_segment_b = 0;
        int road_type_b = 0;
        int warning_type = 0;
        int warningCount = 0;
        boolean isonroad = false;
        double road_longitude = 0;
        double road_latitude = 0;


        ArrayList<Double> polygonXA = new ArrayList<Double>();
        ArrayList<Double> polygonYA = new ArrayList<Double>();
        int length = handler.arr.size();
        map_point_judge mpj = new map_point_judge();
        //1、首先获取每个数据点的道路段ID和道路ID，如果一直一致，那么就存入list中。
        for (int j = 0; j <= (length - 2); j++) {

            road_segment_a = handler.arr.get(j).p_name;
            road_segment_b = handler.arr.get(j + 1).p_name;
            road_type_a = handler.arr.get(j).p_road;
            road_type_b = handler.arr.get(j + 1).p_road;
            road_longitude = handler.arr.get(j).lon;
            road_latitude = handler.arr.get(j).lat;
            if ((road_segment_a == road_segment_b) && (road_type_a == road_type_b)) {
                //将当前的路段位置信息存入链表
                polygonXA.add(road_longitude); //经度
                polygonYA.add(road_latitude); //纬度
            } else {
                polygonXA.add(road_longitude); //经度
                polygonYA.add(road_latitude); //纬度
                if (j == (length - 2)) {
                    polygonXA.add(handler.arr.get(j + 1).lon); //经度
                    polygonYA.add(handler.arr.get(j + 1).lat); //纬度
                }
                //根据目前的position以及road_judge
                isonroad = mpj.isPointInPolygon(handler.longitude, handler.latitude, polygonXA, polygonYA);
                if (isonroad) {
                    road_name = handler.arr.get(j).road_name;
                    road_type = handler.arr.get(j).p_road;
                    System.out.println("获取的路段是 " + road_type);
                    System.out.println("获取的路段是 " + road_name);
                    System.out.println("获取车速是 " + local_speed);
//					System.out.println("规范车速是 "+ speed_arr.get(road_name).speed_min +"-" + speed_arr.get(road_name).speed_max);

                    if (local_speed < handler.speed_arr.get(road_name).speed_min) {
                        warning_type = 2;
                    } else if (local_speed > handler.speed_arr.get(road_name).speed_max) {
                        warning_type = 1;
                    }
                    System.out.println("最大速度 " + handler.speed_arr.get(road_name).speed_max);
                    System.out.println("最小速度 " + handler.speed_arr.get(road_name).speed_min);
                    System.out.println("判断速度 " + local_speed);

                    //累加高环圈数
                    if (handler.orderid != 0 && road_type == 56) {
                        handler.ghcount++;
                        //往数据库更新数据
                        String sql = "update tb_ordermilegps set ghcount = ? where orderid = ? and equipment_num = ?";
                        DAO dao = new DAO();
                        dao.update(sql, handler.ghcount, handler.orderid, handler.vehicleID);
                    }

                    //顺序判断
                    if (!handler.roadOrderList.isEmpty()) {
                        if (handler.arr.get(j).road_name.equals(handler.roadOrderList.peek()) || handler.arr.get(j).road_name.equals(handler.roadOrderList.getLast())) {
                            if (handler.arr.get(j).road_name.equals(handler.roadOrderList.peek())) {
                                handler.roadOrderList.offer(handler.roadOrderList.peek());
                                handler.roadOrderList.poll();
                            }
                        } else {
                            //有问题，同时规范要包含这条路
                            if (handler.roadOrderList.contains(handler.arr.get(j).road_name)) {
                                warning_type = 3;
                                while (!handler.arr.get(j).road_name.equals(handler.roadOrderList.peek())) {
                                    handler.roadOrderList.offer(handler.roadOrderList.peek());
                                    handler.roadOrderList.poll();
                                }
                            }
                        }
                    }


                    flag = 1;//获取所在道路类型
                    carInfo.setWarningState(warning_type);
                    carInfo.setCurRoadName(handler.arr.get(j).road_name);
                    carInfo.setRoadType(road_type);

                    /********************每一帧判断进入某条路并且将里程累积进tb_orderMileage表中*********************/
                    int devide = 2;//每秒发送次数
                    //int weihao = Integer.parseInt(VIN.substring(VIN.length() - 2));
                    double speedtemp = local_speed;
//                    if (VIN.startsWith("ID6")) {
//                        speedtemp = speedtemp / (devide * 3600);
//                    } else if (VIN.startsWith("ID7")){
//                        speedtemp = speedtemp / (3600);
//                    }
                    if (timeDiff > 0 && timeDiff < 300) {
                        speedtemp = speedtemp * (timeDiff) / (60.0 * 60.0);
                        // TODO: 2019/11/25 orderid已经获得了，然后往数据库中累积里程就ok
                        String sql3 = "UPDATE tb_orderMileage SET road_" + road_type + " = road_" + road_type + " + " + speedtemp + " WHERE Equipment_num = '" + handler.vehicleID + "' AND orderid = " + handler.orderid + "";
                        DBO db = new DBO();//初始链接数据库类
                        ResultSet rs = null;
                        try {
                            if (handler.orderid == 0) {
                                System.out.println("订单号为空，无法累积分断里程");
                            } else {
                                db.executeUpdate(sql3);
                                System.out.println("更新分断里程成功");
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
//                System.out.println(road);
                            e.printStackTrace();
                        }//查询结果
                        finally {
                            db.close();
                            if (rs != null) {
                                try {
                                    rs.close();
                                } catch (SQLException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    /********************每一帧判断进入某条路并且将里程累积进tb_orderMileage表中结束*********************/
                    break;
                }



			/*	 //测试
				 flag = 1;
				 warning_type = 1;*/


                //判断不在该路段则要进行清空操作。
                polygonXA.clear();
                polygonYA.clear();
            }
        }

        if (flag == 0) {
            System.out.println("未获取到相应路段");
        }
        /*******************************************************************
         * 3、把告警信息写入到相应的gis_shown 数据库中
         * current_road、overspeed、if_overspeed、if_worryway、warning_time
         * 这里需要把road_type，warning_type返回。
         * 输入：warning_type,road_type,local_speed,local_time,vehicle_id
         * 输出：current_road、overspeed、if_overspeed、if_worryway、warning_time
         *******************************************************************/
        if (1 == flag) {
            Connection conn0 = null;
            PreparedStatement ps0 = null;
            ResultSet rs0 = null;
//			System.out.println("进入报警插值" +warning_type );
            //把告警信息写到GIS_SHOUW数据表中。
            if (warning_type == 1 || warning_type == 2 || warning_type == 3) {
                Connection conn = null;
                PreparedStatement ps = null;
                String uuid = null;
                try {
                    conn = JDBCTools.getConnection();
                    String sql = "insert into " + tablename + "(WARNING_ID,CURRENT_ROAD_TYPE,VIN,WARNING_TYPE,LONGITUDE,LATITUDE,GPS_SPEED,SPEED,TIME,orderid)values(?,?,?,?,?,?,?,?,?,?)";
                    System.out.println(sql);
                    uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
                    ps = conn.prepareStatement(sql);
                    ps.setString(1, uuid);
                    ps.setInt(2, road_type);
                    ps.setString(3, handler.vehicleID);
                    ps.setInt(4, warning_type);
                    ps.setDouble(5, handler.longitude);
                    ps.setDouble(6, handler.latitude);
                    ps.setDouble(7, handler.gPS_speed);
                    ps.setDouble(8, handler.carspeed);
                    time = new java.sql.Timestamp(((Date) time).getTime());
                    ps.setObject(9, time);
                    ps.setInt(10, handler.orderid);
                    ps.executeUpdate();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    JDBCTools.free(null, ps, conn);
                }
                if (road_type != handler.lastroad) {
                    handler.haswarning = new int[3];
                }
                if (warning_type == 1) {
                    if (handler.haswarning[0] == 0) {
                        fa = "$VoPlay,速度高于道路规范值\r\n";
                        WarningInfo warningInfo = new WarningInfo();
                        warningInfo.WARNING_ID = uuid;
                        warningInfo.time = time.toString();
                        warningInfo.gPS_speed = local_speed;
                        warningInfo.warning_type = warning_type;
                        warningInfo.latitude = handler.latitude;
                        warningInfo.longitude = handler.longitude;
                        warningInfo.orderid = handler.orderid;
                        warningInfo.road_name = road_name;
                        warningInfo.road_type = road_type;
                        warningInfo.VIN = handler.vehicleID;
                        warningInfo.carname = handler.carname;
                        if (local_speed < 170) {
                            sm.addQue(warningInfo);
                            ctx.channel().writeAndFlush(fa);
                        }
                        handler.haswarning[0] = 1;
                    }
                } else if (warning_type == 2) {
                    if (handler.haswarning[1] == 0) {
                        fa = "$VoPlay,速度低于道路规范值\r\n";
                        WarningInfo warningInfo = new WarningInfo();
                        warningInfo.WARNING_ID = uuid;
                        warningInfo.time = time.toString();
                        warningInfo.gPS_speed = local_speed;
                        warningInfo.warning_type = warning_type;
                        warningInfo.latitude = handler.latitude;
                        warningInfo.longitude = handler.longitude;
                        warningInfo.orderid = handler.orderid;
                        warningInfo.road_name = road_name;
                        warningInfo.road_type = road_type;
                        warningInfo.VIN = handler.vehicleID;
                        warningInfo.carname = handler.carname;
                        sm.addQue(warningInfo);
                        handler.haswarning[1] = 1;
                        ctx.channel().writeAndFlush(fa);
                    }
                } else if (warning_type == 3) {
//                    fa = "$VoPlay,规范顺序报警\r\n";
                    WarningInfo warningInfo = new WarningInfo();
                    warningInfo.WARNING_ID = uuid;
                    warningInfo.time = time.toString();
                    warningInfo.gPS_speed = local_speed;
                    warningInfo.warning_type = warning_type;
                    warningInfo.latitude = handler.latitude;
                    warningInfo.longitude = handler.longitude;
                    warningInfo.orderid = handler.orderid;
                    warningInfo.road_name = road_name;
                    warningInfo.road_type = road_type;
                    warningInfo.VIN = handler.vehicleID;
                    warningInfo.carname = handler.carname;
                    sm.addQue(warningInfo);
                    ctx.channel().writeAndFlush(fa);
                }
            }
            handler.lastroad = road_type;
        }
        //****if结束，将“超速信息”插入数据库操作完成。*********

        return carInfo;
    }
}
