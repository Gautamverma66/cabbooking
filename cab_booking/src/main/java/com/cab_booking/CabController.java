package com.cab_booking;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;



@RestController
public class CabController {

	
	@GetMapping("/location")
	public List showCabDeatils(HttpServletRequest req) throws ClassNotFoundException, SQLException
	{
		String location=req.getParameter("location");
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/cab_booking", "root", "Gautam@123");
		
		int lc=Integer.parseInt(location);
		
		
		List list=new ArrayList();
		
		//find near car
		String s="car";
		list.add(CabController.findMinDistanceVehicle(s, lc, con));
		
		//find near auto
		String s1="auto";
		list.add(CabController.findMinDistanceVehicle(s1, lc, con));
		
		//find near bike
		String s3="bike";
		list.add(CabController.findMinDistanceVehicle(s3, lc, con));
		
		

		return list;
		
	}
	
	public static List findMinDistanceVehicle(String vehicle_type,int lc,Connection con) throws SQLException
	{
		Statement st=con.createStatement();
		String query="Select * from cab_drivers where vehicle_category='"+vehicle_type+"' and available='yes'";
	    ResultSet rs=st.executeQuery(query);
	    HashSet<Integer> set=new HashSet<Integer>();
        while(rs.next())
		{
			set.add(rs.getInt("location_id"));

		}
		
		
		int mindistance=CabController.findMinDistance(set,lc);
		
		Statement st1=con.createStatement();
		String query1="Select * from cab_drivers where location_Id="+mindistance+"";
		ResultSet rs1=st1.executeQuery(query1);
	    ArrayList list=new ArrayList();
	 
		while(rs1.next())
		{

			list.add(rs1.getString("location_Id"));
			list.add(rs1.getString("vehicle_No"));
			list.add(rs1.getString("vehicle_category"));
			list.add(rs1.getString("available"));
			list.add(rs1.getString("driver_contact_no"));
			
			
		}
		return list;
	
	}
	
	public static int findMinDistance(HashSet hset,int lc)
	{
		Iterator i=hset.iterator();
		int min=Integer.MAX_VALUE;
		if(hset.contains(lc))
			return lc;
		else
		{
			int d=0;
		   while(i.hasNext())
		   {
			   int b=(int) i.next();
			   int c=Math.abs(lc-b);
			   if(min>c)
			   {
				  d=b;
				  min=c;
			   }
			
	       }
		   if(lc<d)
		     return lc+min;
		   else
			   return lc-min;
		}
	}
}
