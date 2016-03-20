package com.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.Statement;

public class UserModel {

	private String name;
	private String email;
	private String pass;
	private Integer id;
	private Double lat;
	private Double lon;

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLon() {
		return lon;
	}

	public void setLon(Double lon) {
		this.lon = lon;
	}

	public static UserModel addNewUser(String name, String email, String pass) {
		try {
			Connection conn = DBConnection.getActiveConnection();
			String sql = "Insert into users (`name`,`email`,`password`) VALUES  (?,?,?)";
			// System.out.println(sql);

			PreparedStatement stmt;
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, name);
			stmt.setString(2, email);
			stmt.setString(3, pass);
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				UserModel user = new UserModel();
				user.id = rs.getInt(1);
				user.email = email;
				user.pass = pass;
				user.name = name;
				user.lat = 0.0;
				user.lon = 0.0;
				return user;
			}
			return null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static UserModel login(String email, String pass) {
		try {
			Connection conn = DBConnection.getActiveConnection();
			String sql = "Select * from users where `email` = ? and `password` = ?";
			PreparedStatement stmt;
			stmt = conn.prepareStatement(sql);

			stmt.setString(1, email);
			stmt.setString(2, pass);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				UserModel user = new UserModel();
				user.id = rs.getInt(1);
				user.email = rs.getString("email");
				user.pass = rs.getString("password");
				user.name = rs.getString("name");
				user.lat = rs.getDouble("lat");
				user.lon = rs.getDouble("long");
				return user;
			}
			return null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static boolean updateUserPosition(Integer id, Double lat, Double lon) {
		try {
			Connection conn = DBConnection.getActiveConnection();
			String sql = "Update users set `lat` = ? , `long` = ? where `id` = ?";
			PreparedStatement stmt;
			stmt = conn.prepareStatement(sql);
			stmt.setDouble(1, lat);
			stmt.setDouble(2, lon);
			stmt.setInt(3, id);
			stmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static Boolean Follow(Integer userid1, Integer userid2,
			String username1, String username2) {
		try {
			Connection conn = DBConnection.getActiveConnection();
			String sql = "Insert into f (`UserID1`,`UserID2`,`username1`,`username2`) VALUES  (?,?,?,?)";
			PreparedStatement stmt;
			stmt = conn.prepareStatement(sql);

			stmt.setInt(1, userid1);
			stmt.setInt(2, userid2);
			stmt.setString(3, username1);
			stmt.setString(4, username2);
			stmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static Boolean UnFollow(Integer userid1, Integer userid2 , String username1 , String username2) {
		try {
			Connection conn = DBConnection.getActiveConnection();
			String sql = "Delete from f where `UserID1`=? and`UserID2`=? and `UserName1`=? and `UserName2`=?";
			PreparedStatement stmt;
			stmt = conn.prepareStatement(sql);

			stmt.setInt(1, userid1);
			stmt.setInt(2, userid2);
			stmt.setString(3, username1);
			stmt.setString(4, username2);
			stmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static ArrayList<String> GetFollowers(Integer userid1,
			String UserName1) {
		ArrayList<String> Following = new ArrayList<String>();
		try {

			Connection conn = DBConnection.getActiveConnection();
			String sql = "Select * from f  where `UserID1` =? and `UserName1` =? ";
			PreparedStatement stmt;
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userid1);
			stmt.setString(2, UserName1);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Following.add(rs.getString("UserName2"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Following;

	}

	public static String  GetUserLastPosition(Integer id) {
			try {
			
			Connection conn = DBConnection.getActiveConnection();
			String sqll = "Select `long` , `lat` from users where `id` = ?";

		   
			PreparedStatement stmt;
	    	stmt = conn.prepareStatement(sqll);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
		    String temp ;
			
			rs = stmt.executeQuery();
			if(rs.next()){
			        temp= rs.getString("long") + rs.getString("lat");
				   return temp;
			}
			
			return null;


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}