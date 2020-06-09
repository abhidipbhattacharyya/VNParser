package com.mycompany.app.iohandler;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import com.mycompany.app.datastructures.JsonElements;


public class JsonWriter{

    public static void writeJsonFile(String filename, List<JsonElements> nodes) throws Exception
    {
        JSONArray nodeList = new JSONArray();
        int i=0;
        for(JsonElements ne: nodes)
        {

            JSONObject jne = ne.getJsonObject();
            nodeList.add(jne);
            //if(i==3)
              //break;
            i++;
        }

        //Write JSON file
        try (FileWriter file = new FileWriter(filename)) {

            //file.write(nodeList.toJSONString(4));
            //file.write(nodeList.toString(4));
            nodeList.writeJSONString(file );
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("file has been written as "+filename);
    }
}
