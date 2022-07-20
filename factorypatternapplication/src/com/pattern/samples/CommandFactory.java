package com.pattern.samples;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;



public class CommandFactory {
	static Map<String,MappingHolder> urlToMappingHolderMap=new HashMap<>();	
	static {
		Properties mappings=new Properties();
		try {
			InputStream iStream=ClassLoader.getSystemResourceAsStream("command.properties");
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
	
	public static Command createCommand(String identifier) {
		MappingHolder oneHolder=urlToMappingHolderMap.get(identifier);	
		Command commandObj=null;
		if(oneHolder==null){
			System.err.println("no mapping found for identifier " + identifier);
			return null;
		}else {			
			String className=oneHolder.getClassName();		
			//Reflection
			try {
				Class clz=Class.forName(className);
				try {
					commandObj=(Command)	clz.newInstance();
				} catch (InstantiationException | IllegalAccessException e) {				
					e.printStackTrace();
				}
			} catch (ClassNotFoundException e) {				
				e.printStackTrace();
			}
		}
		return commandObj;		
	}
}
