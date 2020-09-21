//定义速度规范的静态数据类。

package com.seu.tool;
public class speed_data {
	public double speed_max;
	public double speed_min;
	public String road_name;

	@Override
	public String toString() {
		return "speed_data{" +
				"speed_max=" + speed_max +
				", speed_min=" + speed_min +
				", road_name='" + road_name + '\'' +
				'}';
	}

	public String getRoad_name() {
		return road_name;
	}

	public void setRoad_name(String road_name) {
		this.road_name = road_name;
	}

	public double getSpeed_max() {
		return speed_max;
	}
	public void setSpeed_max(double speed_max) {
		this.speed_max = speed_max;
	}
	public double getSpeed_min() {
		return speed_min;
	}
	public void setSpeed_min(double speed_min) {
		this.speed_min = speed_min;
	}
}
