package com.mycompany.app.datastructures;
import java.util.*;
import java.util.stream.Collectors;
import com.mycompany.app.datastructures.Sentence;
import com.mycompany.app.utils.Counter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
//import org.json.simple.JSONString;
import org.json.simple.parser.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;
@Setter
@Getter
@Accessors(fluent = true)

public class JsonElements{
	String imagename;
	List<Sentence> sentences = new ArrayList<>();
	List<String> labels;
	JSONParser jsonParser =new  JSONParser();

	public JsonElements()
	{

	}

	public JsonElements(String imagename, List<String> sentences)
	{
		this.imagename = imagename;

		for(int i=0;i<sentences.size();i++)
		{
			//System.out.println("======"+sentences.get(i));
			Sentence sen  = new Sentence(sentences.get(i),i);
			this.sentences.add(sen);
		}
		//this.labels = labels;
	}


	public Sentence getCaptionAtIndex(int index)
	{
		return sentences.get(index);

	}

	public JSONObject getJsonLabels(boolean pblabel)
	{
		Counter labelCount = new Counter();

		for(Sentence se: sentences){
			List<String> labels = (pblabel == true)? se.wslabels() : se.vnlabels();
			labelCount.update(labels);
		}
		return labelCount.getJsonObject();
	}

	public JSONObject getJsonObject()
	{
		JSONObject toBeReturned = new JSONObject();
		toBeReturned.put("image", imagename);

		JSONArray jsonArray = new JSONArray();
    for(Sentence se : sentences) {
			//try{
				//JSONObject sen =  (JSONObject) jsonParser.parse(se.sentence());
				//JSONString sen = new JSONString(se.sentence());
        jsonArray.add(se.sentence());
		//	}catch(org.json.simple.parser.ParseException e)
			//{
			//	e.printStackTrace();
			//}
    }
    //jsonArray.build();

		toBeReturned.put("sentences", jsonArray);
		JSONObject labels = getJsonLabels(true);
		toBeReturned.put("Prop_labels", labels);

		labels = getJsonLabels(false);
		toBeReturned.put("VN_labels", labels);

		return toBeReturned;

	}

	@Override
    public String toString() {
		return ("image:"+ imagename) + "\n" + "sentences: [ "+sentences.stream().map(Object::toString).collect(Collectors.joining(",\n")) + " ]\n";
	}
}
