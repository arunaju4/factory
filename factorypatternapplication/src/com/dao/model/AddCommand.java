package com.dao.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import com.pattern.samples.Command;
import com.pattern.samples.DbConnection;

public class AddCommand implements Command{

	@Override
	public String execute(HttpServletRequest request)  {
		try {
			Connection con=DbConnection.getConnection();
			 
			PreparedStatement st=con.prepareStatement("insert into emp values (?,?)");
			st.setInt(1,Integer.parseInt(request.getParameter("id")) );
			st.setString(2, request.getParameter("user"));
			st.executeUpdate();
			st.close();
			con.close();
			
			
			
			
		}catch(Exception r) {
			r.printStackTrace();
		}
		return "success";
		
	}
	
	

}
