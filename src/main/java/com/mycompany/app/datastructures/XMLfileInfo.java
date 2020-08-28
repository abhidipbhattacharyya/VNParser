package com.mycompany.app.datastructures;

import java.util.*;
import java.util.stream.Collectors;
import com.mycompany.app.utils.StringUtils;
import com.mycompany.app.VNparsing.VNInfoExtractor;
import com.mycompany.app.VNparsing.WSinfoExtractor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;
@Setter
@Getter
@Accessors(fluent = true)


public class XMLfileInfo{
    String filename;
	  List<Sentence> sentences;


    public XMLfileInfo()
    {

    }

    public XMLfileInfo(String filename, List<Sentence> sentences)
    {
      this.filename = filename;
      this.sentences = sentences;
    }

    public String toString()
  	{
  		StringBuffer ss =  new StringBuffer();
      ss.append("filename:" + filename+"\n");
      ss.append("num sen: "+ sentences.size()+"\n");
      ss.append("sen:"+'\n');
      for (Sentence sen: sentences)
      {
        	ss.append(sen+"\n");
      }


  		return ss.toString();
  	}

    public void parseVN(VNInfoExtractor vnp, WSinfoExtractor wsd)
    {
        //System.out.println("here");
      /*  for (Sentence se: sentences)
        {
            //System.out.println("sentence"+ se);
            try{
              //Sentence se = sentences.get(j);
              wsd.getAnnotation(se);
            }
            catch(Exception e)
            {
              //System.out.println("****Error occured:::: "+e.getCause());
            //  badNodeWSInfo.add(sentences.get(j).id());
              continue;
            }
        }*/

        for (Sentence se: sentences)
        {
          try{
            vnp.getAnnotation2(se);
            ////se.getCONLLformattedTags();
            se.getConllStrings();
          }catch(Exception e)
          {
            //badVIds.add(se.id());
            continue;
          }
      }
    }
}
