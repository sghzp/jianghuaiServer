package com.seu.tool;
import java.util.ArrayList;

public class map_point_judge {
		
		 //polygonXA、polygonYA是一个区域的点的集合，经度在polygonXA，纬度在polygonYA
		 public boolean isPointInPolygon ( double px , double py , ArrayList<Double> polygonXA , ArrayList<Double> polygonYA )  
		    {  
		        boolean isInside = false;  
		        double ESP = 1e-11;  //一个极限小的值，用来下面判断是否重合等
		        int count = 0;  
		        double linePoint1x;  
		        double linePoint1y;  
		        double linePoint2x = 180;  //相当于引得一条射线的X轴大小
		        double linePoint2y;  //引得一条射线的Y轴大小，这条射线我取得是向着X轴的，相当于向右侧引一条
		  
		        linePoint1x = px;  
		        linePoint1y = py;  
		        linePoint2y = py;  
		        //循环把道路信息的点取出来做判断，一次取出两个点，判断射线跟这两个点的线段是否有交点
		        //最后把交点数量相加，交点数量为奇数则判断结果为在该区域内，为偶数或者0则判断不在该区域内	        
		        for (int i = 0; i < polygonXA.size() - 1; i++)  
		        {  
		        	//循环取出道路信息
		            double cx1 = polygonXA.get(i);  
		            double cy1 = polygonYA.get(i);  
		            double cx2 = polygonXA.get(i + 1);  
		            double cy2 = polygonYA.get(i + 1);  
		            if ( isPointOnLine(px, py, cx1, cy1, cx2, cy2) )  
		            {  
		                return true;  
		            }  
		            if ( Math.abs(cy2 - cy1) < ESP )  
		            {  
		                continue;  
		            }  
		  
		            if ( isPointOnLine(cx1, cy1, linePoint1x, linePoint1y, linePoint2x, linePoint2y) )  
		            {  
		                if ( cy1 > cy2 )  
		                    count++;  
		            }  
		            else if ( isPointOnLine(cx2, cy2, linePoint1x, linePoint1y, linePoint2x, linePoint2y) )  
		            {  
		                if ( cy2 > cy1 )  
		                    count++;  
		            }  
		            else if ( isIntersect(cx1, cy1, cx2, cy2, linePoint1x, linePoint1y, linePoint2x, linePoint2y) )  
		            {  
		                count++;  
		            }  
		        }  
		        
		        double cx1 = polygonXA.get(polygonXA.size()-1);  
	            double cy1 = polygonYA.get(polygonYA.size()-1);  
	            double cx2 = polygonXA.get(0);  
	            double cy2 = polygonYA.get(0);  
	            if ( isPointOnLine(px, py, cx1, cy1, cx2, cy2) )  
	            {  
	                return true;  
	            }  
	            if ( isPointOnLine(cx1, cy1, linePoint1x, linePoint1y, linePoint2x, linePoint2y) )  
	            {  
	                if ( cy1 > cy2 )  
	                    count++;  
	            }  
	            //判断点是否在直线上
	            else if ( isPointOnLine(cx2, cy2, linePoint1x, linePoint1y, linePoint2x, linePoint2y) )  
	            {  
	                if ( cy2 > cy1 )  
	                    count++;  
	            }  
	            //判断点是否在区域内
	            else if ( isIntersect(cx1, cy1, cx2, cy2, linePoint1x, linePoint1y, linePoint2x, linePoint2y) )  
	            {  
	                count++;  
	            }  
	            //在控制台输出交点数量
	//	        System.out.println(count); 
		        //判断交点是奇数还是偶数
		        if ( count % 2 == 1 )  
		        {  
		            isInside = true;  
		        }  
		  
		        return isInside;  
		    }  
		  
		    public double Multiply ( double px0 , double py0 , double px1 , double py1 , double px2 , double py2 )  
		    {  
		        return ((px1 - px0) * (py2 - py0) - (px2 - px0) * (py1 - py0));  
		    }  
		  
		    public boolean isPointOnLine ( double px0 , double py0 , double px1 , double py1 , double px2 , double py2 )  
		    {  
		        boolean flag = false;  
		        double ESP = 1e-11;  
		        if ( (Math.abs(Multiply(px0, py0, px1, py1, px2, py2)) < ESP) && ((px0 - px1) * (px0 - px2) <= 0)  
		                && ((py0 - py1) * (py0 - py2) <= 0) )  
		        {  
		            flag = true;  
		        }  
		        return flag;  
		    }  
		  
		    public boolean isIntersect ( double px1 , double py1 , double px2 , double py2 , double px3 , double py3 , double px4 ,  
		            double py4 )  
		    {  
		        boolean flag = false;  
		        double d = (px2 - px1) * (py4 - py3) - (py2 - py1) * (px4 - px3);  
		        if ( d != 0 )  
		        {  
		            double r = ((py1 - py3) * (px4 - px3) - (px1 - px3) * (py4 - py3)) / d;  
		            double s = ((py1 - py3) * (px2 - px1) - (px1 - px3) * (py2 - py1)) / d;  
		            if ( (r >= 0) && (r <= 1) && (s >= 0) && (s <= 1) )  
		            {  
		                flag = true;  
		            }  
		        }  
		        return flag;  
		    }  
	}
