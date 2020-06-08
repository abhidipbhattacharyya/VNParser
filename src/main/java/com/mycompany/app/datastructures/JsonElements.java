package com.mycompany.app.datastructures;
import java.util.*;
import java.util.stream.Collectors;

public class JsonElements{
	String imagename;
	List<String> sentences;
	List<String> labels;
	
	public JsonElements()
	{
		
	}
	
	public JsonElements(String imagename, List<String> sentences)
	{
		this.imagename = imagename;
		this.sentences =  sentences;
		//this.labels = labels;
	}
	
	public String getImageName()
	{
		return imagename;
	}
	
	public List<String> getSentence()
	{
		return sentences;
	}
	
	public String getStringAtIndex(int index)
	{
		return sentences.get(index);
		
	}
	
	@Override
    public String toString() { 
		return ("image:"+ imagename) + "\n" + "sentences: [ "+sentences.stream().map(Object::toString).collect(Collectors.joining(",\n")) + " ]\n";
	}
}