package com.prashant.spring.controller;


import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.prashant.spring.dao.UserDao;
import com.prashant.spring.model.User;

@Controller
public class HomeController 
{
	
	protected String getHash() 
	{
        String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder hash = new StringBuilder();
        Random rnd = new Random();
        while (hash.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * CHARS.length());
            hash.append(CHARS.charAt(index));
        }
        String hashStr = hash.toString();
        return hashStr;

    }
	

	/**
	 * Simply selects the login view to render by returning its name a.
	 */
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	 @Autowired
	 private JavaMailSender mailSender;
	
	String NAME = "X";
	String U = "X";	
	String msg1="Same Username Exists !!!!";
	
	//---------------------------------------------------------------------------
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String Initiate(Locale locale, Model model) 
	{
		System.out.println("Login Page Requested, locale = " + locale);
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);	
		String formattedDate = dateFormat.format(date);
		model.addAttribute("serverTime", formattedDate);		
		return "login";		
	}
	//---------------------------------------------------------------------------
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String Logout(Locale locale, Model model) 
	{
		System.out.println("Logout Page Requested, locale = " + locale);
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);	
		String formattedDate = dateFormat.format(date);
		model.addAttribute("serverTime", formattedDate);		
		return "login";		
	}
	
	
	//---------------------------------------------------------------------------//-----------
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String Register(@Validated User user, Model model)
	{
		System.out.println("Register Page Requested");
		return "register";
	}
	
	
	
	//---------------------------------------------------------------------------
	@RequestMapping(value = "/Validate", method = RequestMethod.POST)
	public String Validate(@Validated User user, Model model,Locale locale) 
	{
		U = user.getUserName();
		String s1 = "password";
		String s2 = "name";
		String s3 = "verify";
		String p = "X";
		String v="0";
		try
		{
			final String selectQuery = "select * from student where username='"+U+"'";	
			Map<?, ?> map = jdbcTemplate.queryForMap(selectQuery);
			Set set  = map.entrySet();
			Iterator itr=set.iterator();  
			
			while(itr.hasNext())
			{  
				//Converting to Map.Entry so that we can get key and value separately  
				Map.Entry entry=(Map.Entry)itr.next();  
				if(entry.getKey().equals(s1))
				{ 	
					p = entry.getValue().toString();
					System.out.println(p);
				}
				if(entry.getKey().equals(s2))
				{ 	
					NAME = entry.getValue().toString();
					System.out.println(p);
				}	       
				if(entry.getKey().equals(s3))
				{ 	
					v = entry.getValue().toString();
					System.out.println(p);
				}				
			 }  			
		}
		catch(Exception e2)
		{
			System.out.println("Exception Raised while Login(user)!!!");			//not inclusion of case of NULL return by username search in the sql query
		}
		
		String dest="userHome";
		if(user.getUserName().equals("admin") && user.getPassword().equals(p))
		{
			dest = "redirect:AdminHome";						//this redirects to ModelView adminHome instead of adminHome.jsp
			System.out.println("Admin Page Requested");
		}
		else if(user.getUserName().equals(U) && user.getPassword().equals(p) && v.equals("1"))
		{
			System.out.println("User HomePage Requested");
			model.addAttribute("name", NAME);
		}
		else
		{
			dest = "errLogin";															//redirects to errLogin.jsp
			System.out.println("Err Login Page Requested, locale = " + locale);
			Date date = new Date();
			DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
			String formattedDate = dateFormat.format(date);
			
			model.addAttribute("serverTime", formattedDate);
			model.addAttribute("message", "Invalid login credentials !!! Try Again");					
		}	
		return dest;
	}
	
	
	//---------------------------------------------------------------------------//-----------
		@RequestMapping(value = "/login", method = RequestMethod.POST)
		public String RElogin(@Validated User user,Locale locale, Model model) 
		{
			System.out.println("RE_Login Page Requested, locale = " + locale);
			Date date = new Date();
			DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);	
			String formattedDate = dateFormat.format(date);
			model.addAttribute("serverTime",formattedDate );	
			int x=0;
			String a,b,c,d,e,f,g,dest="login";
			a = user.getUserName();
			b = user.getPassword();
			c = user.getName();
			d = user.getMobile();
			e = user.getBranch();
			f = user.getEmail();
			g = getHash();	
			Map<?,?> map=null;
			System.out.println(a+" "+b+" "+c+" "+d+" "+e+" "+g);
			try
			{
				try
				{
					final String q1 = "select * from student where username='"+a+"'";	
					map = jdbcTemplate.queryForMap(q1);				
					model.addAttribute("data", msg1);
				}
				catch(Exception e1)
				{
					System.out.println("Select query exception raised in RELogin : "+e1);
					final String insertQuery = "INSERT INTO student (username,password,name,mobile,branch,email,hash,verify) VALUES ('"+a+"','"+b+"','"+c+"','"+d+"','"+e+"','"+f+"','"+g+"',"+x+")";                    
					jdbcTemplate.update(insertQuery);
					String recipientAddress,subject,message;
					subject = "Verify Account !";
					message = "Hi "+c+". This is verification mail with link : http://localhost:8080/HelloWeb/verify/"+g;
					recipientAddress = f;
					// creates a simple e-mail object
			        SimpleMailMessage email = new SimpleMailMessage();
			        email.setTo(recipientAddress);
			        email.setSubject(subject);
			        email.setText(message);
			         
			        // sends the e-mail
			        mailSender.send(email);
				}			
			}
			catch(Exception e1)
			{
				System.out.println("Exception Raised while insert query in Register(RElogin)!!!"+e1);	
			}		
			if(U.equals("admin"))
				dest = "redirect:AdminHome";
			return dest;		
		}
		
		//---------------------------------------------------------------------------//-----------
		@RequestMapping(value="/verify/{hash}", method = RequestMethod.GET)
		public String Verify(@PathVariable String hash, Model model, UserDao dao)
		{
			  String s1 = "username";
			  String x="xxxxxxxxxx";
			  try
				{
					final String selectQuery = "select * from student where hash='"+hash+"'";	
					Map<?, ?> map = jdbcTemplate.queryForMap(selectQuery);
					Set set  = map.entrySet();
					Iterator itr=set.iterator();  
					
					while(itr.hasNext())
					{  
						//Converting to Map.Entry so that we can get key and value separately  
						Map.Entry entry=(Map.Entry)itr.next();  
						if(entry.getKey().equals(s1))
						{ 	
							x = entry.getValue().toString();
							System.out.println(x);
						}
						      
					}  
				}
			  
				catch(Exception e2)
				{
					System.out.println("Exception Raised while Verify!!! : "+e2);			
				}
			  System.out.println(x);
			  dao.editVerify(x, jdbcTemplate);
	//		  User user = dao.getUserById(x,jdbcTemplate); 
			return "verify";
		}
		
		
		
		//---------------------------------------------------------------------------//-----------
		
		  @RequestMapping(value="/viewProfile", method = RequestMethod.POST)
		  public String ViewProfile(Model model)
		  {				
				try
				{
					final String selectQuery = "select * from student where username='"+U+"'";	
					Map<?, ?> map = jdbcTemplate.queryForMap(selectQuery);
					Set set  = map.entrySet();
					Iterator itr=set.iterator();  
					char x='a';
					while(itr.hasNext())
					{  
						//Converting to Map.Entry so that we can get key and value separately  
						Map.Entry entry=(Map.Entry)itr.next();  
						model.addAttribute(Character.toString(x),entry.getValue());			
						x+=1;
					//	x = String.valueOf(Integer.parseInt(x)+1);						
					}  
				}
				catch(Exception e)
				{
					System.out.println("Exception Raised while ViewProfile(viewProfile)!!!");			
				}
				
				return "viewProfile";
		  }
		  
		  
		//---------------------------------------------------------------------------//-----------
			
		  @RequestMapping(value="/userAddMarks", method = RequestMethod.POST)
		  public String UserMarks(Model model)
		  {			
			  	model.addAttribute("name",NAME);		
				
				return "userAddMarks";
		  }
		
		//---------------------------------------------------------------------------//-----------
		  
		  @RequestMapping(value="/changePassword", method = RequestMethod.POST)
		  public String ChangePassword(@Validated User user,Model model,UserDao dao)
		  {
			  model.addAttribute("userName",U);			 
			  return "changePassword";
		  }
		  
		  
		  //---------------------------------------------------------------------------//-----------
		  
		  @RequestMapping(value="/userHome", method = RequestMethod.GET)
		  public String UserHome(@Validated User user,Model model,UserDao dao)
		  {
			  model.addAttribute("name",NAME);			 
			  return "userHome";
		  }
		  //---------------------------------------------------------------------------//-----------
		  
		  @RequestMapping(value="/userHome", method = RequestMethod.POST)
		  public String UserHomePost(@Validated User user,Model model,UserDao dao)
		  {
			  model.addAttribute("name",NAME);			 
			  return "userHome";
		  }
		//---------------------------------------------------------------------------//-----------
		  @RequestMapping(value="/validatePassword/{userName}", method = RequestMethod.POST)
		  public String ValidatePassword(@PathVariable String userName,@Validated User user, Model model, UserDao dao)
		  {
			  model.addAttribute("userName",userName);	
			  String P="x",eP,p1,p2, dest="redirect:/userHome";
			  eP = user.getPassword();
			  p1 = user.getP1();
			  p2 = user.getP2();
			  String s1 = "password", msg = null;
			  try
				{
					final String selectQuery = "select * from student where username='"+U+"'";	
					Map<?, ?> map = jdbcTemplate.queryForMap(selectQuery);
					Set set  = map.entrySet();
					Iterator itr=set.iterator();  
					
					while(itr.hasNext())
					{  
						//Converting to Map.Entry so that we can get key and value separately  
						Map.Entry entry=(Map.Entry)itr.next();  
						if(entry.getKey().equals(s1))
						{ 	
							P = entry.getValue().toString();
							System.out.println(P);
						}
						      
					}  
				}
				catch(Exception e2)
				{
					System.out.println("Exception Raised while ChangePassword!!!");			//not inclusion of case of NULL return by username search in the sql query
				}
			  
			  if(P.equals(eP)&&p1.equals(p2))
			  {
				  dao.editPassword(user, jdbcTemplate, p1);
			  }
			  else
			  {
				  msg = "Either Password incorrect or new password dosen't match. Try again";
				  dest = "changePassword";
			  }
			  model.addAttribute("msg",msg);
			  model.addAttribute("name",U);
			  return dest;
		  }
		  //---------------------------------------------------------------------------//-----------
		  
		  @RequestMapping(value="/changeMobile", method = RequestMethod.POST)
		  public String ChangeMobile(@Validated User user,Model model,UserDao dao)
		  {
			  model.addAttribute("userName",U);			 
			  return "changeMobile";
		  }

		  
		//---------------------------------------------------------------------------//-----------
		  @RequestMapping(value="/validateMobile/{userName}", method = RequestMethod.POST)
		  public String ValidateMobile(@PathVariable String userName,@Validated User user, Model model, UserDao dao)
		  {
			  model.addAttribute("userName",userName);	
			  String eM ,dest="redirect:/userHome";
			  eM = user.getMobile();
			  dao.editMobile(user, jdbcTemplate, eM);
			 if(userName.equals("admin"))
				 dest="redirect:AdminHome";
			  model.addAttribute("name",U);
			  return dest;
		  }
	
	
	//---------------------------------------------------------------------------
	/* It provides list of employees in model object
	 * 
	 * Iterator itr = list.iterator();
		while(itr.hasNext()){
			System.out.println("Using Iterator"+itr.next());
		}
		 */  
    @RequestMapping(value="/AdminHome")  
    public ModelAndView AdminHome(UserDao dao,Model model)
    {  
    	model.addAttribute("name",NAME);
    	System.out.println("AdminHome Name = "+NAME);
    	
        List<User> list= dao.getUsers(jdbcTemplate);                
        return new ModelAndView("adminHome","list",list);  
    }  
    
    
   //---------------------------------------------------------------------------
  	@RequestMapping(value = "/adminAddMarks", method = RequestMethod.GET)
  	public String AdminAddMarks(@Validated User user, Model model) 
  	{
  		System.out.println("Admin Add Marks Page Requested");
  		return "adminAddMarks";
  		
  	}
  	
  	
  	 //---------------------------------------------------------------------------
  	/* It displays object data into form for the given id.  
     * The @PathVariable puts URL data into variable.*/  
    @RequestMapping(value="/editUser/{userName}")  
    public ModelAndView Edit(@PathVariable String userName,UserDao dao)
    {  
    	System.out.println("Edit Username = "+userName);
        User user = dao.getUserById(userName,jdbcTemplate);  
        return new ModelAndView("editUserForm","command",user);  
    }  
    
    
    
    //---------------------------------------------------------------------------
    /* It updates model object. */  
    @RequestMapping(value="/editsave",method = RequestMethod.POST)  
    public ModelAndView editsave(@ModelAttribute("user") User user,UserDao dao)
    {  
        dao.update(user,jdbcTemplate);  
        return new ModelAndView("redirect:AdminHome");  
    }  

	
	//---------------------------------------------------------------------------
	@RequestMapping(value = "/aggregate", method = RequestMethod.POST)
	public String Aggregate(@Validated User user, Model model) 
	{
		System.out.println("Aggregate Page Requested");
		

		int a,b,c,agg;
		String grade, name, dest ="agreUser";
		a = user.getM1();
		b = user.getM2();
		c = user.getM3();
		name = user.getName();
		System.out.println("Value is "+a);
		System.out.println(b);
		System.out.println(c);
		System.out.println("login name = "+NAME);
		System.out.println("current name = "+name);
		agg = (a+b+c)/3;
		if(agg>=90)
			grade="A";
		else if(agg>=80 && agg<90)
			grade="B";
		else if(agg>=70 && agg<80)
			grade="C";
		else if(agg>=55 && agg<70)
			grade="D";
		else if(agg>=35 && agg<55)
			grade="E";
		else
			grade="F";		
		
		
		
		if(U.equals("admin"))
		{
			try
			{
				final String updateQuery = "UPDATE student set marks1="+a+",marks2="+b+",marks3="+c+",aggregate="+agg+",grade='"+grade+"' where name='"+name+"'";
				jdbcTemplate.update(updateQuery);

			}
			catch(Exception e1)
			{
				System.out.println("Exception Raised while Aggregate updation!!!");	
			}
			dest = "redirect:AdminHome";
		}		
		else
		{
			try
			{
				final String updateQuery = "UPDATE student set marks1="+a+",marks2="+b+",marks3="+c+",aggregate="+agg+",grade='"+grade+"' where username='"+U+"'";
				jdbcTemplate.update(updateQuery);

			}
			catch(Exception e1)
			{
				System.out.println("Exception Raised while Aggregate updation!!!");	
			}
			model.addAttribute("Name", NAME);
			model.addAttribute("Aggregate", agg);
			model.addAttribute("Grade", grade);
		}
		
		return dest;
		
	}
	
	/* It deletes record for the given userName in URL and redirects to /AdminHome  */  
    @RequestMapping(value="/deleteUser/{userName}",method = RequestMethod.GET)  
    public ModelAndView delete(@PathVariable String userName, UserDao dao)
    {  
        dao.delete(userName,jdbcTemplate);  
        return new ModelAndView("redirect:/AdminHome");  
    }  
}


//LOG : -

