package com.mycompany.app.datastructures;

import java.util.*;
import java.util.stream.Collectors;
import com.mycompany.app.utils.StringUtils;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;
@Setter
@Getter
@Accessors(fluent = true)

public class Sentence
{
	String sentence;
	int id;
	int verbIndex;
	List<String> tokens = null;
	List<String> vnlabels = new ArrayList<String>();
	List<List<String>> tag_sentences = new ArrayList<List<String>>();

	List<List<String>> conll_sentences = null;


	public Sentence()
	{

	}

	public Sentence(String sen, int id){
		sentence = sen;
		this.id = id;
	}

	public void addTagTokens(List<String> BIO)
	{
		tag_sentences.add(BIO);
	}

	public void addVnLabels(String label)
	{
		vnlabels.add(label);
	}

	private List<String> bio2SE(List<String> tags)
	{
			List<String> newLabels = new ArrayList<String>();
			boolean has_opening = Boolean.parseBoolean("False");

			for(int i=0; i< tags.size();i++)
			{
				String label = tags.get(i);
				if( label.equalsIgnoreCase("O"))
				{
					newLabels.add("*");
					continue;
				}
				String new_label = "*";
			  if ( label.charAt(0) == 'B' || i == 0 || !label.substring(1).equalsIgnoreCase(tags.get(i-1).substring(1)))
				{
					new_label = "(" + label.substring(2) + new_label;
					has_opening = Boolean.parseBoolean("True");
				}
			  if( i == tags.size() - 1 || tags.get(i+1).charAt(0) == 'B' || !label.substring(1).equalsIgnoreCase(tags.get(i+1).substring(1)))
				{
					new_label = new_label + ")";
					has_opening = Boolean.parseBoolean("False");
				}
				newLabels.add(new_label);
			}

			if(has_opening)
			{
				System.out.println("Has unclosed opening: " + tags.stream().map(Object::toString).collect(Collectors.joining(" ")));
			}

			return newLabels;
	}

	public List<List<String>> getCONLLformattedTags()
	{
		if(conll_sentences == null)
		{
			List<List<String>> setags = new ArrayList<>();
			for(List<String> tags : tag_sentences)
			{
				setags.add(bio2SE(tags));
			}
			conll_sentences = setags;
		}
		return conll_sentences;
	}


	public List<String> getConllStrings()
	{
		List<String> output = new ArrayList<>();
		if(conll_sentences == null)
			getCONLLformattedTags();

			for( List<String> conll:conll_sentences )
			{
				StringBuffer ss = new StringBuffer(sentence +"\n");
				for(int i=0; i<tokens.size();i++)
				{
					String line = StringUtils.ljust(tokens.get(i), 15)  + StringUtils.rjust(conll.get(i), 15) +"\n";
					ss.append(line);
				}
				ss.append("\n");
				//System.out.print(ss);
				output.add(ss.toString());
			}
			return output;
		}
}
