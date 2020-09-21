package com.seu.others;

import com.seu.database.DAO;
import java.io.*;

public class AddRoadPoints {
    public static void main(String[] args) {
        String sql = "INSERT INTO tb_road_point (road_name,p_name,p_road,lon,lat) VALUES(?, ?, ?, ?, ?)";
        int name = 185;
        int road = 21;
        try {
            File file = new File("E:\\jianghuaiRoads\\高速测速带.txt");
            Reader r=new FileReader(file);
            BufferedReader buf=new BufferedReader(r);
            String str = null;
            StringBuffer sb = new StringBuffer();
            String [] strs;
            Object object[] = new Object[5];
            object[0] = file.getName().split("\\.")[0];
            DAO dao = new DAO();
            while((str = buf.readLine()) != null){
                if (str.equals("")){
                    name++;
                    continue;
                }
                object[1] = name;
                object[2] = road;
                strs = str.split(",");
                object[3] = strs[0];
                object[4] = strs[1];

                dao.update(sql,object);
            }
            //关闭流
            buf.close();
            r.close();
            //关闭流
            buf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}