package com.mycompany.app.controller;
import com.mycompany.app.iohandler.InputReader;
import com.mycompany.app.iohandler.OutputWriter;
import com.mycompany.app.iohandler.JsonWriter;
import com.mycompany.app.iohandler.JsonReader;
import com.mycompany.app.iohandler.XMLinputreader;

import io.github.clearwsd.parser.*;
import io.github.semlink.verbnet.*;
import io.github.semlink.parser.*;
import io.github.semlink.propbank.type.PropBankArg;
import io.github.semlink.semlink.VerbNetAligner;
import io.github.clearwsd.parser.Nlp4jDependencyParser;

import java.util.*;
import com.mycompany.app.datastructures.JsonElements;
import com.mycompany.app.datastructures.Sentence;
import com.mycompany.app.datastructures.XMLfileInfo;
import com.mycompany.app.datastructures.BadDataInfo;
import com.mycompany.app.VNparsing.VNInfoExtractor;
import com.mycompany.app.VNparsing.WSinfoExtractor;


import static io.github.semlink.parser.VerbNetParser.pbRoleLabeler;
import java.util.stream.Collectors;
import java.io.File;
/**
 * Hello world!
 *
 */
public class App
{

    /** for AIDA xml **/
    public static void main( String[] args ) throws Exception
    {
      //String xmlfile = "/media/abhidip/2F1499756FA9B115/data/AIDA/M19_UNSEQUESTERED_ltf/E102/HC000Q7MH.ltf.xml";
      //XMLinputreader.readXMLfiles(xmlfile);
      String folder = "/media/abhidip/2F1499756FA9B115/data/AIDA/M19_UNSEQUESTERED_ltf/E102";
      String wrtfolder = "/media/abhidip/2F1499756FA9B115/data/AIDA/M19_UNSEQUESTERED_ltf/E102_v";
      List<XMLfileInfo> filesInfo = XMLinputreader.readFiles(folder);
      VNInfoExtractor vnp = new VNInfoExtractor();
      WSinfoExtractor wsd = new WSinfoExtractor();

      for(int j=0; j<filesInfo.size();j++)
      {
          System.out.println("processing "+ j+"/"+filesInfo.size()+" ==========="+filesInfo.get(j).filename());
          //System.out.println(filesInfo.get(j));
          filesInfo.get(j).parseVN(vnp,wsd );
          OutputWriter.writeSRLfilesXML(filesInfo.get(j), wrtfolder);
          //break;
      }
      /*for(int j=0; j<filesInfo.size();j++)
      {
        OutputWriter.writeSRLfilesXML(filesInfo.get(j), wrtfolder);
        //break;
      }*/
    }
    /** for text file processing**/
    /*
    public static void main( String[] args ) throws Exception
    {
    	String filename = "";
      List<Integer> ids = InputReader.readIdFile ("/media/abhidip/2F1499756FA9B115/data/flickr/abhidip_splits/train/fivecaption_train.ids");
    	List<Sentence> sentences = InputReader.readFileswithId( "/media/abhidip/2F1499756FA9B115/data/flickr/abhidip_splits/train/fivecaption_train.txt.image-locations.txt" , ids);

    	//List<JsonElements> nodes = JsonReader.readJsonFile("/media/abhidip/2F1499756FA9B115/data/flickr/abhidip_splits/flickrdata.json");
    	VNInfoExtractor vnp = new VNInfoExtractor();
      //vnp.getAnnotation(sentences.get(504));
        WSinfoExtractor wsd = new WSinfoExtractor();

      ArrayList<Integer> badIds= new ArrayList<Integer>();
      badIds.add(2521);
      badIds.add(6776);
      badIds.add(11381);
      badIds.add(14511);


      //List<Integer> badNodeWSInfo = new ArrayList<>();
      for(int j=0; j<sentences.size();j++)
      {
        System.out.println("=========="+ j);
          try{
            Sentence se = sentences.get(j);
            wsd.getAnnotation(se);
          }
          catch(Exception e)
          {
            //System.out.println("****Error occured:::: "+e.getCause());
          //  badNodeWSInfo.add(sentences.get(j).id());
            continue;
          }
      }

      int i=0;
      //ArrayList<Integer> badVIds= new ArrayList<Integer>();
      for (Sentence se: sentences)
      {
        //System.gc();
        //System.out.println("++++" +i + "of " + sentences.size()+"  " + se.sentence());
        //if(badIds.contains(se.id()) == false)
        //{
          //System.out.println(se.id()+"\t"+se.sentence());
        try{
          vnp.getAnnotation(se);
          ////se.getCONLLformattedTags();
          se.getConllStrings();
        }catch(Exception e)
        {
          //badVIds.add(se.id());
          continue;
        }

        //}

        i++;

      }

      OutputWriter.writeSRLfiles(sentences, "/media/abhidip/2F1499756FA9B115/data/flickr/abhidip_splits/train/VN_SRL_trainR.txt");

      //System.out.println("missing roleset_ids for sentences:===============");
      //System.out.println(badNodeWSInfo.stream().map(Object::toString).collect(Collectors.joining("\n")));
      //System.out.println("====================================");

      //System.out.println("missing SRL for sentences:===============");
      //System.out.println(badVIds.stream().map(Object::toString).collect(Collectors.joining("\n")));
      //System.out.println("====================================");
    }
    */

    /** for json file processing**/
  /*  public static void main( String[] args ) throws Exception
    {

      String filename = "";
      //List<Integer> ids = InputReader.readIdFile ("/media/abhidip/2F1499756FA9B115/data/flickr/abhidip_splits/train/fivecaption_train.ids");
    	//List<Sentence> sentences = InputReader.readFileswithId( "/media/abhidip/2F1499756FA9B115/data/flickr/abhidip_splits/train/fivecaption_train.txt.image-locations.txt" , ids);
      List<JsonElements> nodes = JsonReader.readJsonFile("/media/abhidip/2F1499756FA9B115/data/flickr/abhidip_splits/flickrdata.json");

//--------------- VNparse------------------------------//
      /*VNInfoExtractor vnp = new VNInfoExtractor();
      List<BadDataInfo> badNodeVNSInfo = new ArrayList<>();

      for(int i=0;i<nodes.size(); i++)
      {
          System.out.println("===="+i+ ", ");
          List<Sentence> sentences =  nodes.get(i).sentences();

          BadDataInfo bdVNs = new BadDataInfo(i);
          for(int j=0; j<sentences.size();j++)
          {
              try{
                Sentence se = sentences.get(j);
                vnp.getAnnotation(se);
              }
              catch(Exception e)
              {
                System.out.println("****Error occured:::: "+e.getCause());
                //badWSIds.add(j);
                bdVNs.addSentenceId(j);
                continue;
              }
          }
          if(bdVNs.size()>0)
            badNodeVNSInfo.add(bdVNs);

          //-------------------------------------------------------//
          System.gc();
          //if(i==3)
            //break;
      }

  //----------- clear WSD parse-------------------------//

      WSinfoExtractor wsd = new WSinfoExtractor();
      List<BadDataInfo> badNodeWSInfo = new ArrayList<>();

      for(int i=0;i<nodes.size(); i++)
      {
          System.out.println("===="+i+ ", ");
          List<Sentence> sentences =  nodes.get(i).sentences();

          BadDataInfo bdWs = new BadDataInfo(i);

          for(int j=0; j<sentences.size();j++)
          {
              try{
                Sentence se = sentences.get(j);
                wsd.getAnnotation(se);
              }
              catch(Exception e)
              {
                System.out.println("****Error occured:::: "+e.getCause());
                bdWs.addSentenceId(j);
                continue;
              }
          }
          if(bdWs.size()>0)
            badNodeWSInfo.add(bdWs);

            //if(i==5)
              //break;
      }

      System.out.println(" 94745454554 I am here");
      JsonWriter.writeJsonFile("/media/abhidip/2F1499756FA9B115/data/flickr/abhidip_splits/flickrdata_VNLabel.json", nodes);

    }*/
}
