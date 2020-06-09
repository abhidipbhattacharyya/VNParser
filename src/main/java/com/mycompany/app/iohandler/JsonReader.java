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


	public static List<JsonElements>  readJsonFile( String filename)  //throws Exception
	{

		List<JsonElements> pnodes = new ArrayList<>();
		try{
					Object obj = new JSONParser().parse(new FileReader(filename));
					JSONArray nodes = (JSONArray) obj;

					for (int i=0 ;i< nodes.size();i++)
					{
						JSONObject c = (JSONObject)nodes.get(i);
						String imagename = c.get("image").toString();
						JSONArray captions = (JSONArray) c.get("sentences");
						List<String> sentences = new ArrayList<>();

						for(int j=0;j<captions.size();j++)
						{
							sentences.add(captions.get(j).toString());
							//System.out.println( "______"+captions.get(j).getClass().getName());
						}
						//System.out.println("========="+sentences.size());
						//System.out.println("=++++="+c.get("sentences").getClass().getName());
						JsonElements je = new JsonElements (imagename, sentences);
						pnodes.add(je);
						//System.out.println("******"+je);
						//break;
					}
				}catch(Exception e)
				{
					// Print the wrapper exception:
					System.out.println("=====Wrapper exception: " + e);

					// Print the 'actual' exception:
					System.out.println("=====Underlying exception: " + e.getCause());
				}

		return pnodes;
	}
}
