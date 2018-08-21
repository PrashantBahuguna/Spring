package com.prashant.spring.model;

import org.springframework.jdbc.core.JdbcTemplate;  
//import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;  
import java.sql.ResultSet;  
import java.sql.SQLException; 

import org.springframework.jdbc.core.RowMapper;


public class User 
{
	private String userName,password,name,branch,mobile,grade,p1,p2,email,hash;
	private int m1,m2,m3,aggregate,verify;
	
	//@Autowired
//	JdbcTemplate jdbcTemplate;  

	public String getUserName() 
	{
		return userName;
	}

	public void setUserName(String userName) 
	{
		this.userName = userName;
	}
	
	public String getPassword() 
	{
		return password;
	}

	public void setPassword(String password) 
	{
		this.password = password;
	}
	
	public String getP1() 
	{
		return p1;
	}

	public void setP1(String p1) 
	{
		this.p1 = p1;
	}
	public String getP2() 
	{
		return p2;
	}

	public void setP2(String p2) 
	{
		this.p2 = p2;
	}
	
	
	public String getName() 
	{
		return name;
	}

	public void setName(String name) 
	{
		this.name = name;
	}
	
	public String getBranch() 
	{
		return branch;
	}

	public void setBranch(String branch) 
	{
		this.branch = branch;
	}
	
	public String getMobile() 
	{
		return mobile;
	}

	public void setMobile(String mobile) 
	{
		this.mobile = mobile;
	}
	public int getM1() 
	{
		return m1;
	}

	public void setM1(int m1) 
	{
		this.m1 = m1;
	}
	public int getM2() 
	{
		return m2;
	}

	public void setM2(int m2) 
	{
		this.m2 = m2;
	}
	public int getM3() 
	{
		return m3;
	}

	public void setM3(int m3) 
	{
		this.m3 = m3;
	}
	public int getAggregate() 
	{
		return aggregate;
	}

	public void setAggregate(int aggregate) 
	{
		this.aggregate = aggregate;
	}
	public String getGrade() 
	{
		return grade;
	}

	public void setGrade(String grade) 
	{
		this.grade = grade;
	}
	
	public String getEmail() 
	{
		return email;
	}

	public void setEmail(String email) 
	{
		this.email = email;
	}
	public String getHash() 
	{
		return hash;
	}

	public void setHash(String hash) 
	{
		this.hash = hash;
	}
	public int getVerify() 
	{
		return verify;
	}

	public void setVerify(int verify) 
	{
		this.verify = verify;
	}
	
	public List<User> getUsers(JdbcTemplate T) 
	{  
	    return T.query("select * from student",new RowMapper<User>()
	    {  
	        		public User mapRow(ResultSet rs, int row) throws SQLException 
	        		{  
	        			User u = new User();  
	        			u.setUserName(rs.getString("username"));  
	        			u.setPassword(rs.getString("password"));  
	        			u.setName(rs.getString("name"));  
	        			u.setMobile(rs.getString("mobile"));
	        			u.setBranch(rs.getString("branch"));
	        			u.setM1(rs.getInt("marks1"));
	        			u.setM2(rs.getInt("marks2"));
	        			u.setM3(rs.getInt("marks3"));
	        			u.setAggregate(rs.getInt("aggregate"));
	        			u.setGrade(rs.getString("grade"));
	        			String a,b,c,d,e;
	        			a = u.getUserName();
	        			b = u.getPassword();
	        			c = u.getName();
	        			d = u.getMobile();
	        			e = u.getBranch();
	        			System.out.println(a+" "+b+" "+c+" "+d+" "+e+" XXXXX");
	        			return u;  
	        		}  
	    });  
	    
	}  
	
	
	
}