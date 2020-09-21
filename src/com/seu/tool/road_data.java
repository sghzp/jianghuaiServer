//定义point_info对应的静态数据类。

package com.seu.tool;
public class road_data {
	public  int id; //点ID
	public  String road_name; //点所属路段名称
	public  int p_name;//点所属道路段
	public  int p_road;//点所属道路名（道路ID）
	public  double lon;//点所属道路经度
	public  double lat;//点所属道路纬度
/*	public int speed_max;
	public int speed_min;*/
	public int reserve3;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getRoad_name() {
		return road_name;
	}
	public void setRoad_name(String road_name) {
		this.road_name = road_name;
	}
	public int getP_name() {
		return p_name;
	}
	public void setP_name(int p_name) {
		this.p_name = p_name;
	}
	public int getP_road() {
		return p_road;
	}
	public void setP_road(int p_road) {
		this.p_road = p_road;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	
/*	public int getSpeed_max() {
		return speed_max;
	}
	public void setSpeed_max(int speed_max) {
		this.speed_max = speed_max;
	}
	public int getSpeed_min() {
		return speed_min;
	}
	public void setSpeed_min(int speed_min) {
		this.speed_min = speed_min;
	}*/
	public int getReserve3() {
		return reserve3;
	}
	public void setReserve3(int reserve3) {
		this.reserve3 = reserve3;
	}
	
	
	
}
