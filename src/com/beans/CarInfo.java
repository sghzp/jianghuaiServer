package com.beans;

public class CarInfo {
	private String time;// UTC时间，格式为hhmmss.ss
	private double longitude;// 经度数据，格式为XXX.XXXXXX，单位为度
	private double latitude;// 纬度数据，格式为XXX.XXXXXX，单位为度
	private String gPS_state;// 速度数据，格式为xx.x，单位为m/s
	private double gPS_speed;// 速度数据，格式为xx.x，单位为m/s
	private double heading;// 航向数据，格式为xxx.x，单位为度，正北偏航角
	private double airtemp;//
	private double airstress;
	private double inairtemp;// 大气压力<
	private double inairstress;
	private double risestress;
	private double oilstress;
	private double carspeed;
	private double enginespeed;
	private double enginetorque;
	private double drivertorque;
	private double footplate;
	private double engineload;
	private double fastconsumption;
	private double averageconsumption;
	private double consumptionrate;
	private double clutch;
	private double stop;

	private double engineneedtorque;
	private double enginetorquemode;
	private double frictiontorque;
	private double firestress;
	private double firetemp;
	private double oiltemp;
	private double coldtemp;
	private double voltage;
	private double enginetemp;

	private double gaugespeed;
	private double frontspeed;
	private double changegear;
	private double outair;

	private String vehicleID;// 辆号，格式为xxxxx，其中x为任意大写字母或数字
	private String deviceID;// 设备号，格式为A01~A99
	private String sum;// 校验和，格式为xxx，由所有字段的ASCII码相加（除$和#），丢弃溢出位后转换成string显示。
	private String mysum;
	private String curRoadName;
	private boolean check_result;

	private String opg;

	public double getAirstress() {
		return airstress;
	}

	public void setAirstress(double airstress) {
		this.airstress = airstress;
	}

	public String getPROJECT_TASK_ID() {
		return PROJECT_TASK_ID;
	}

	public void setPROJECT_TASK_ID(String PROJECT_TASK_ID) {
		this.PROJECT_TASK_ID = PROJECT_TASK_ID;
	}

	public String getPROJECT_NUM() {
		return PROJECT_NUM;
	}

	public void setPROJECT_NUM(String PROJECT_NUM) {
		this.PROJECT_NUM = PROJECT_NUM;
	}

	public String getSystime() {
		return systime;
	}

	public void setSystime(String systime) {
		this.systime = systime;
	}

	public void setCheck_result(boolean check_result) {
		this.check_result = check_result;
	}

	public String getINSTRUMENT_ID() {
		return INSTRUMENT_ID;
	}

	public void setINSTRUMENT_ID(String INSTRUMENT_ID) {
		this.INSTRUMENT_ID = INSTRUMENT_ID;
	}

	private String systime;
	private double inter;
	private double mile;
	private int roadType;
	private String INSTRUMENT_ID;
	private String PROJECT_NUM;
	private String PROJECT_TASK_ID;

	public String getROLE_ID() {
		return ROLE_ID;
	}

	public void setROLE_ID(String ROLE_ID) {
		this.ROLE_ID = ROLE_ID;
	}

	private String ROLE_ID;
	private int warningState;

	public int getRoadType() {
		return roadType;
	}

	public void setRoadType(int roadType) {
		this.roadType = roadType;
	}



	public int getWarningState() {
		return warningState;
	}

	public void setWarningState(int warningState) {
		this.warningState = warningState;
	}



	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getGPS_state() {
		return gPS_state;
	}

	public void setGPS_state(String gPS_state) {
		this.gPS_state = gPS_state;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getgPS_speed() {
		return gPS_speed;
	}

	public void setgPS_speed(double gPS_speed) {
		this.gPS_speed = gPS_speed;
	}

	public double getHeading() {
		return heading;
	}

	public void setHeading(double heading) {
		this.heading = heading;
	}

	public double getAirtemp() {
		return airtemp;
	}

	public void setAirtemp(double airtemp) {
		this.airtemp = airtemp;
	}

	public double getInairtemp() {
		return inairtemp;
	}

	public void setInairtemp(double inairtemp) {
		this.inairtemp = inairtemp;
	}

	public double getInairstress() {
		return inairstress;
	}

	public void setInairstress(double inairstress) {
		this.inairstress = inairstress;
	}

	public double getRisestress() {
		return risestress;
	}

	public void setRisestress(double risestress) {
		this.risestress = risestress;
	}

	public double getOilstress() {
		return oilstress;
	}

	public void setOilstress(double oilstress) {
		this.oilstress = oilstress;
	}

	public double getCarspeed() {
		return carspeed;
	}

	public void setCarspeed(double carspeed) {
		this.carspeed = carspeed;
	}

	public double getEnginespeed() {
		return enginespeed;
	}

	public void setEnginespeed(double enginespeed) {
		this.enginespeed = enginespeed;
	}

	public double getEnginetorque() {
		return enginetorque;
	}

	public void setEnginetorque(double enginetorque) {
		this.enginetorque = enginetorque;
	}

	public double getDrivertorque() {
		return drivertorque;
	}

	public void setDrivertorque(double drivertorque) {
		this.drivertorque = drivertorque;
	}

	public double getFootplate() {
		return footplate;
	}

	public void setFootplate(double footplate) {
		this.footplate = footplate;
	}

	public double getEngineload() {
		return engineload;
	}

	public void setEngineload(double engineload) {
		this.engineload = engineload;
	}

	public double getFastconsumption() {
		return fastconsumption;
	}

	public void setFastconsumption(double fastconsumption) {
		this.fastconsumption = fastconsumption;
	}

	public double getAverageconsumption() {
		return averageconsumption;
	}

	public void setAverageconsumption(double averageconsumption) {
		this.averageconsumption = averageconsumption;
	}

	public double getConsumptionrate() {
		return consumptionrate;
	}

	public void setConsumptionrate(double consumptionrate) {
		this.consumptionrate = consumptionrate;
	}

	public double getClutch() {
		return clutch;
	}

	public void setClutch(double clutch) {
		this.clutch = clutch;
	}

	public double getStop() {
		return stop;
	}

	public void setStop(double stop) {
		this.stop = stop;
	}

	public double getEngineneedtorque() {
		return engineneedtorque;
	}

	public void setEngineneedtorque(double engineneedtorque) {
		this.engineneedtorque = engineneedtorque;
	}

	public double getEnginetorquemode() {
		return enginetorquemode;
	}

	public void setEnginetorquemode(double enginetorquemode) {
		this.enginetorquemode = enginetorquemode;
	}

	public double getFrictiontorque() {
		return frictiontorque;
	}

	public void setFrictiontorque(double frictiontorque) {
		this.frictiontorque = frictiontorque;
	}

	public double getFirestress() {
		return firestress;
	}

	public void setFirestress(double firestress) {
		this.firestress = firestress;
	}

	public double getFiretemp() {
		return firetemp;
	}

	public void setFiretemp(double firetemp) {
		this.firetemp = firetemp;
	}

	public double getOiltemp() {
		return oiltemp;
	}

	public void setOiltemp(double oiltemp) {
		this.oiltemp = oiltemp;
	}

	public double getColdtemp() {
		return coldtemp;
	}

	public void setColdtemp(double coldtemp) {
		this.coldtemp = coldtemp;
	}

	public double getVoltage() {
		return voltage;
	}

	public void setVoltage(double voltage) {
		this.voltage = voltage;
	}

	public double getEnginetemp() {
		return enginetemp;
	}

	public void setEnginetemp(double enginetemp) {
		this.enginetemp = enginetemp;
	}

	public double getGaugespeed() {
		return gaugespeed;
	}

	public void setGaugespeed(double gaugespeed) {
		this.gaugespeed = gaugespeed;
	}

	public double getFrontspeed() {
		return frontspeed;
	}

	public void setFrontspeed(double frontspeed) {
		this.frontspeed = frontspeed;
	}

	public double getChangegear() {
		return changegear;
	}

	public void setChangegear(double changegear) {
		this.changegear = changegear;
	}

	public double getOutair() {
		return outair;
	}

	public void setOutair(double outair) {
		this.outair = outair;
	}

	public String getVehicleID() {
		return vehicleID;
	}

	public void setVehicleID(String vehicleID) {
		this.vehicleID = vehicleID;
	}

	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	public String getSum() {
		return sum;
	}

	public void setSum(String sum) {
		this.sum = sum;
	}

	public String getCurRoadName() {
		return curRoadName;
	}

	public void setCurRoadName(String curRoadName) {
		this.curRoadName = curRoadName;
	}

	public String getMysum() {
		return mysum;
	}



	public void setMysum(String mysum) {
		this.mysum = mysum;
	}

	public boolean getCheck_result() {
		return check_result;
	}



	public double getInter() {
		return inter;
	}

	public void setInter(double inter) {
		this.inter = inter;
	}

	public double getMile() {
		return mile;
	}

	public void setMile(double mile) {
		this.mile = mile;
	}

	public String getOpg() {
		return opg;
	}

	public void setOpg(String opg) {
		this.opg = opg;
	}

	public boolean setAll(Object[] object){
		int i = 0;

		this.time = object[i ++].toString();
		if(i >= object.length){				return false;		}
		this.longitude = (double)object[i ++];
		if(i >= object.length){ 			return false; 		}
		this.latitude = (double)object[i ++];
		if(i >= object.length){ 			return false; 		}
		this.gPS_state = object[i ++].toString();
		if(i >= object.length){ 			return false; 		}
		this.gPS_speed = (double)object[i ++];
		if(i >= object.length){ 			return false; 		}
		this.heading = (double)object[i ++];
		if(i >= object.length){ 			return false; 		}


		this.airtemp = (double)object[i ++];
		if(i >= object.length){ 			return false; 		}
		this.airstress = (double)object[i ++];
		if(i >= object.length){ 			return false; 		}
		this.inairtemp = (double)object[i ++];
		if(i >= object.length){ 			return false; 		}
		this.inairstress = (double)object[i ++];
		if(i >= object.length){ 			return false; 		}
		this.risestress = (double)object[i ++];
		if(i >= object.length){ 			return false; 		}

		this.oilstress = (double)object[i ++];
		if(i >= object.length){ 			return false; 		}
		this.carspeed = (double)object[i ++];
		if(i >= object.length){ 			return false; 		}
		this.enginespeed = (double)object[i ++];
		if(i >= object.length){ 			return false; 		}
		this.enginetorque = (double)object[i ++];
		if(i >= object.length){ 			return false; 		}
		this.drivertorque = (double)object[i ++];
		if(i >= object.length){ 			return false; 		}


		this.footplate = (double)object[i ++];
		if(i >= object.length){ 			return false; 		}
		this.engineload = (double)object[i ++];
		if(i >= object.length){ 			return false; 		}
		this.fastconsumption = (double)object[i ++];
		if(i >= object.length){ 			return false; 		}
		this.averageconsumption = (double)object[i ++];
		if(i >= object.length){ 			return false; 		}
		this.consumptionrate = (double)object[i ++];
		if(i >= object.length){ 			return false; 		}


		this.clutch = (double)object[i ++];
		if(i >= object.length){ 			return false; 		}
		this.stop = (double)object[i ++];
		if(i >= object.length){ 			return false; 		}
		this.engineneedtorque = (double)object[i ++];
		if(i >= object.length){ 			return false; 		}
		this.enginetorquemode = (double)object[i ++];
		if(i >= object.length){ 			return false; 		}
		this.frictiontorque = (double)object[i ++];
		if(i >= object.length){ 			return false; 		}


		this.firestress = (double)object[i ++];
		if(i >= object.length){ 			return false; 		}
		this.firetemp = (double)object[i ++];
		if(i >= object.length){ 			return false; 		}
		this.oiltemp = (double)object[i ++];
		if(i >= object.length){ 			return false; 		}
		this.coldtemp = (double)object[i ++];
		if(i >= object.length){ 			return false; 		}
		this.voltage = (double)object[i ++];
		if(i >= object.length){ 			return false; 		}


		this.enginetemp = (double)object[i ++];
		if(i >= object.length){ 			return false; 		}
		this.gaugespeed = (double)object[i ++];
		if(i >= object.length){ 			return false; 		}
		this.frontspeed = (double)object[i ++];
		if(i >= object.length){ 			return false; 		}
		this.changegear = (double)object[i ++];
		if(i >= object.length){ 			return false; 		}
		this.outair = (double)object[i ++];
		if(i >= object.length){ 			return false; 		}


		this.vehicleID = (String)object[i ++];
		if(i >= object.length){ 			return false; 		}
		this.deviceID = (String)object[i ++];
		if(i >= object.length){ 			return false; 		}
		this.sum = (String)object[i ++];
		if(i >= object.length){ 			return false; 		}
		this.mysum = (String)object[i ++];
		if(i >= object.length){ 			return false; 		}
		this.check_result = (boolean) object[i ++];
		if(i >= object.length){ 			return false; 		}


		this.systime = object[i ++].toString();
		if(i >= object.length){ 			return false; 		}
		this.inter = (double)object[i ++];
		if(i >= object.length){ 			return false; 		}
		this.mile = (double)object[i ++];
		if(i >= object.length){ 			return false; 		}
		this.roadType = (int)object[i ++];
		if(i >= object.length){ 			return false; 		}
		this.INSTRUMENT_ID = (String)object[i ++];
		if(i >= object.length){ 			return false; 		}


		this.PROJECT_NUM = (String)object[i ++];
		if(i >= object.length){ 			return false; 		}
		this.PROJECT_TASK_ID = (String)object[i ++];
		return true;


		/*this.warningState = warningState;*/
	}

	@Override
	public String toString() {
		return "CarInfo{" +
				"time='" + time + '\'' +
				", longitude=" + longitude +
				", latitude=" + latitude +
				", gPS_speed=" + gPS_speed +
				", heading=" + heading +
				", airtemp=" + airtemp +
				", airstress=" + airstress +
				", inairtemp=" + inairtemp +
				", inairstress=" + inairstress +
				", risestress=" + risestress +
				", oilstress=" + oilstress +
				", carspeed=" + carspeed +
				", enginespeed=" + enginespeed +
				", enginetorque=" + enginetorque +
				", drivertorque=" + drivertorque +
				", footplate=" + footplate +
				", engineload=" + engineload +
				", fastconsumption=" + fastconsumption +
				", averageconsumption=" + averageconsumption +
				", consumptionrate=" + consumptionrate +
				", clutch=" + clutch +
				", stop=" + stop +
				", engineneedtorque=" + engineneedtorque +
				", enginetorquemode=" + enginetorquemode +
				", frictiontorque=" + frictiontorque +
				", firestress=" + firestress +
				", firetemp=" + firetemp +
				", oiltemp=" + oiltemp +
				", coldtemp=" + coldtemp +
				", voltage=" + voltage +
				", enginetemp=" + enginetemp +
				", gaugespeed=" + gaugespeed +
				", frontspeed=" + frontspeed +
				", changegear=" + changegear +
				", outair=" + outair +
				", vehicleID='" + vehicleID + '\'' +
				", deviceID='" + deviceID + '\'' +
				", sum='" + sum + '\'' +
				", curRoadName='" + curRoadName + '\'' +
				", mysum='" + mysum + '\'' +
				", check_result=" + check_result +
				", systime='" + systime + '\'' +
				", inter='" + inter + '\'' +
				", mile=" + mile +
				", roadType=" + roadType +
				", INSTRUMENT_ID=" + INSTRUMENT_ID +
				", PROJECT_NUM='" + PROJECT_NUM + '\'' +
				", PROJECT_TASK_ID='" + PROJECT_TASK_ID + '\'' +
				", warningState=" + warningState +
				'}';
	}


}
