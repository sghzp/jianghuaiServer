package com.beans;

import com.seu.database.DAO;
import com.seu.tool.DBO;
import com.seu.tool.HttpClient;
import com.seu.tool.SendMsg;
import com.seu.tool.xStr;

import java.sql.SQLException;

public class MileCountInfo implements SendMsg.ISendMsg {

    public final String RESURL = "http://172.25.202.5/GPS/gpsMileCount";
    public final String RESVAL = "Daozha_ID={0}&" + "orderid={1}&driveid={2}&timein=\"{3}\"&timeout=\"{4}\"&Mileage={5}";
//    {"Daozha_ID":1,"orderid":1,"driveid":1,"timein":"2019-12-12 00:00:00","timeout":"2019-12-12 00:00:00","Mileage":1.0}

    public int Daozha_ID;
    public int orderid;
    public int driveid;
    public String timein;
    public String timeout;
    public double Mileage;
    private String _err;

    public String get_err() {
        return _err;
    }

    public void set_err(String _err) {
        this._err = _err;
    }

    @Override
    public boolean Send() {
        _err = null;
        HttpClient httpClient = new HttpClient();
        String res = xStr.format(RESVAL, Daozha_ID, orderid, driveid, timein, timeout, Mileage);
        String result = httpClient.doPost(RESURL, res);
        if (result.equals("{1}")) {
            _err = "总里程推送失败，稍后补发";
            return false;
        }else {
            String sql = "update tb_mileagecount set sendflag = 1 where timein = ? and timeout = ?;";
            DAO dao = new DAO();
            Object object[] = new Object[2];
            try {
                object[0] = timein;
                object[1] = timeout;
                dao.update(sql,object);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
    }
}
