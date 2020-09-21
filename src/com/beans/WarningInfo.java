package com.beans;

import com.alibaba.fastjson.JSONObject;
import com.seu.database.DAO;
import com.seu.database.JDBCTools;
import com.seu.tool.*;

import java.sql.SQLException;


public class WarningInfo implements SendMsg.ISendMsg {

    public final String RESURL = "http://172.25.202.5/GPS/gpsWarring";
    public final String RESVAL = "time=\"{0}\"&" + "warning_type={1}&gPS_speed={2}&road_type={3}&longitude={4}&latitude={5}&road_name=\"{6}\"&orderid={7}&vin=\"{8}\"&carname=\"{9}\"";

    public String WARNING_ID;
    public String time;
    public int warning_type;
    public double gPS_speed;
    public int road_type;
    public double longitude;
    public double latitude;
    public String road_name;
    public int orderid;
    public String VIN;
    public String carname;
    private String _err;
    public String getErr() {
        return _err;
    }

    @Override
    public boolean Send() {
        _err = null;
        HttpClient httpClient = new HttpClient();
        String res = xStr.format(RESVAL, time, warning_type, gPS_speed, road_type, longitude, latitude, road_name, orderid, VIN, carname);
        String result = httpClient.doPost(RESURL, res);
        if (result.equals("{1}")) {
            _err = "报警信息推送失败，稍后补发";
            return false;
        }else {
            JSONObject jsonObject = JSONObject.parseObject(result);
            int code = jsonObject.getIntValue("code");
            if(code == 0){
                changeFlg(code+1+"", WARNING_ID);
            }else {
                changeFlg(code+1+"", WARNING_ID);
            }
            return true;
        }
    }

    public static void changeFlg(String flg,String WARNING_ID){
        String sql = "update tb_warning set sendflag = ? where WARNING_ID = ? ;";
        DAO dao = new DAO();
        Object object[] = new Object[2];
        try {
            object[0] = WARNING_ID;
            object[1] = flg;
            dao.update(sql,object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
