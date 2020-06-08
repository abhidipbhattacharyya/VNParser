package com.mycompany.app.iohandler;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;
import java.io.FileReader; 
import java.util.Iterator; 
import java.util.*;
import com.mycompany.app.datastructures.JsonElements;
//import org.json.simple.parser.*;


public class JsonReader{
	
	
	public static List<JsonElements>  readJsonFile( String filename)  throws Exception  
	{
	
		Object obj = new JSONParser().parse(new FileReader(filename)); 
		JSONArray nodes = (JSONArray) obj; 
		List<JsonElements> pnodes = new ArrayList<>();
		for (int i=0 ;i< nodes.size();i++)
		{
			JSONObject c = (JSONObject)nodes.get(i);
			String imagename = c.get("image").toString();
			JSONArray captions = (JSONArray) c.get("sentences");
			List<String> sentences = new ArrayList<>();
			
			for(int j=0;j<captions.size();j++)
			{
				sentences.add(captions.get(j).toString());
				//System.out.println( captions[j].getClass().getName() );
			}
			//System.out.println(c.get("sentences").getClass().getName());
			JsonElements je = new JsonElements (imagename, sentences);
			pnodes.add(je);
			//System.out.print(je);
			//break;
		}
		
		return pnodes;
	}
}