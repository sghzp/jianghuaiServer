package com.seu.others;

public class demoTest {
    private void ConvertDistanceToLogLat(double distance, String logLatPtStr, double angle) {
        String logLat = null;
        String[] temp_Arrary = logLatPtStr.split(",");
        double Ing1 = Double.parseDouble(temp_Arrary[0].replace("(", ""));
        double lat1 = Double.parseDouble(temp_Arrary[1].replace("(", ""));
        double lon = Ing1 + (distance * Math.sin(angle * Math.PI / 180)) / (111 * Math.cos(lat1 * Math.PI / 180));//将距离转换成经度的计算公式
        double lat = lat1 + (distance * Math.cos(angle * Math.PI / 180)) / 111;//将距离转换成纬度的计算公式
        String logStr = String.valueOf(lon);
        String latStr = String.valueOf(lat);
        logLat = "" + logStr + "," + latStr + "";
        System.out.println(logLat);
    }

    public static void main(String[] args) {
        demoTest demo = new demoTest();
        demo.ConvertDistanceToLogLat(0.0016,"117.19885548921999,31.692163130800001",32.0);
        demo.ConvertDistanceToLogLat(0.0016,"117.19885548921999,31.692163130800001",148.0);
        demo.ConvertDistanceToLogLat(0.0016,"117.19885548921999,31.692163130800001",212.0);
        demo.ConvertDistanceToLogLat(0.0016,"117.19885548921999,31.692163130800001",328.0);
    }

}
