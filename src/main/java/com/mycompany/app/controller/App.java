package com.mycompany.app.controller;
import com.mycompany.app.iohandler.InputReader;
import com.mycompany.app.iohandler.OutputWriter;
import io.github.clearwsd.parser.*;
import io.github.semlink.verbnet.*;
import io.github.semlink.parser.*;
import io.github.semlink.propbank.type.PropBankArg;
import io.github.semlink.semlink.VerbNetAligner;
import io.github.clearwsd.parser.Nlp4jDependencyParser;
import com.mycompany.app.iohandler.JsonReader;
import java.util.*;
import com.mycompany.app.datastructures.JsonElements;
import com.mycompany.app.datastructures.Sentence;
import com.mycompany.app.VNparsing.VNInfoExtractor;


import static io.github.semlink.parser.VerbNetParser.pbRoleLabeler;
/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args ) throws Exception
    {
    	String filename = "";
      List<Integer> ids = InputReader.readIdFile ("/media/abhidip/2F1499756FA9B115/data/flickr/abhidip_splits/train/fivecaption_train.ids");
    	List<Sentence> sentences = InputReader.readFileswithId( "/media/abhidip/2F1499756FA9B115/data/flickr/abhidip_splits/train/fivecaption_train.txt.image-locations.txt" , ids);

    	//List<JsonElements> nodes = JsonReader.readJsonFile("/media/abhidip/2F1499756FA9B115/data/flickr/abhidip_splits/flickrdata.json");
    	VNInfoExtractor vnp = new VNInfoExtractor();
      //vnp.getAnnotation(sentences.get(504));

      ArrayList<Integer> badIds= new ArrayList<Integer>();
      badIds.add(2521);
      badIds.add(6776);
      badIds.add(11381);
      badIds.add(14511);
      int i=0;
      for (Sentence se: sentences)
      {
        //System.out.println("++++" +i + "of " + sentences.size()+"  " + se.sentence());
        if(badIds.contains(se.id()) == false)
        {
          //System.out.println(se.id()+"\t"+se.sentence());
          vnp.getAnnotation(se);
          ////se.getCONLLformattedTags();
          se.getConllStrings();
        }

        i++;

        //break;
      }

      OutputWriter.writeSRLfiles(sentences, "/media/abhidip/2F1499756FA9B115/data/flickr/abhidip_splits/train/VN_SRL_train.txt");
    	//vnp.getAnnotation(nodes.get(3));*/
    }

}
