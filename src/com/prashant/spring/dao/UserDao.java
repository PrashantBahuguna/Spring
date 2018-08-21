package com.prashant.spring.dao;


import com.prashant.spring.model.*;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;  
//import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;  
import java.sql.ResultSet;  
import java.sql.SQLException; 

import org.springframework.jdbc.core.RowMapper;


public class UserDao 
{
	
	//@Autowired
//	JdbcTemplate jdbcTemplate;  
	
	public User getUserById(String userName,JdbcTemplate T)
	{  
	    String sql="select * from student where username=?";  
	    
	    return T.queryForObject(sql, new Object[]{userName},new BeanPropertyRowMapper<User>(User.class));  
	}  
	
	public int editVerify(String u, JdbcTemplate T)
	{
		int x=1;
		String sql = "update student set verify="+x+" where username ='"+u+"'";
		return T.update(sql);
	}
	
	public int editPassword(User p,JdbcTemplate T,String nPassword)
	{
		String sql = "update student set password='"+nPassword+"' where username='"+p.getUserName()+"'";
		return T.update(sql);
	}
	public int editMobile(User p,JdbcTemplate T,String nMobile)
	{
		String sql = "update student set mobile='"+nMobile+"' where username='"+p.getUserName()+"'";
		return T.update(sql);
	}
	
	public int update(User p,JdbcTemplate T)
	{  
	    String sql="update student set name='"+p.getName()+"', mobile='"+p.getMobile()+"', branch='"+p.getBranch()+"' where username='"+p.getUserName()+"'";  
	    return T.update(sql);  
	}  
	
	
	public int delete(String userName, JdbcTemplate T)
	{  
	    String sql="delete from student where username='"+userName+"'";  
	    return T.update(sql);  
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
	        			u.setEmail(rs.getString("email"));
	        			String a,b,c,d,e,f;
	        			a = u.getUserName();
	        			b = u.getPassword();
	        			c = u.getName();
	        			d = u.getMobile();
	        			e = u.getBranch();
	        			f = u.getEmail();
	        			System.out.println(a+" "+b+" "+c+" "+d+" "+e+" XXXXX");
	        			return u;  
	        		}  
	    });  
	    
	}  
	
	
	
}