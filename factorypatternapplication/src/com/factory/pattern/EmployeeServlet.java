package com.factory.pattern;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pattern.samples.Command;
import com.pattern.samples.MappingHolder;



/**
 * Servlet implementation class BookServlet
 */
@WebServlet("/")
public class EmployeeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Map<String,MappingHolder> urlToMappingHolderMap= null;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();
	}


	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		urlToMappingHolderMap=new HashMap<>();
		Properties mappings=new Properties();
		try {
			ClassLoader classLoader=Thread.currentThread().getContextClassLoader();
			InputStream iStream=classLoader.getResourceAsStream("Command.properties");
			if(iStream!=null) {
			mappings.load(iStream);
			iStream.close();
			}else {
				System.err.println("unable to open the file");
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}
		System.out.println(mappings);		
		Iterator mappingIter=mappings.keySet().iterator();
		while(mappingIter.hasNext()) {
			String oneKey=(String)mappingIter.next();
			String mappedValue=mappings.getProperty(oneKey);
			MappingHolder oneHolder=new MappingHolder();
			String[] mappedValueArr=mappedValue.split("::");
			oneHolder.setClassName(mappedValueArr[0]);
			oneHolder.setUrl(oneKey);
			if(mappedValueArr.length>1) {
			String[] returnUrlArr=	mappedValueArr[1].split(",");
			for (String oneUrlMapping: returnUrlArr) {
				String[] parts=oneUrlMapping.split(":");
				oneHolder.getPageMappings().setProperty(parts[0], parts[1]);				
			}
			}
			urlToMappingHolderMap.put(oneKey,oneHolder);			
		}
		System.out.println(urlToMappingHolderMap);
	}


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
		process(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// doGet(request, response);
		process(request, response);
	}
	
	public void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// uri get
		String identifier = request.getRequestURI();
		System.out.println(identifier);
		String[] identifierNew = identifier.split("/");
		System.out.println(identifierNew[identifierNew.length-1]);
		MappingHolder oneHolder=urlToMappingHolderMap.get(identifierNew[identifierNew.length-1]);
		Command commandObj=null;
		String returnUrl="404.jsp";
		if(oneHolder==null){
			System.err.println("no mapping found for identifier " + identifier);
		}else {	
			String className=oneHolder.getClassName();		
			//Reflection
			try {
				Class clz=Class.forName(className);
				try {
					commandObj=(Command)clz.newInstance();
					String status = commandObj.execute(request);
					returnUrl = (String) oneHolder.getPageMappings().get(status);
				} catch (InstantiationException | IllegalAccessException e) {				
					e.printStackTrace();
				}
			} catch (ClassNotFoundException e) {				
				e.printStackTrace();
			}
		}
		RequestDispatcher dispatcher = request.getRequestDispatcher(returnUrl);
		dispatcher.forward(request, response);
		
	}

}
