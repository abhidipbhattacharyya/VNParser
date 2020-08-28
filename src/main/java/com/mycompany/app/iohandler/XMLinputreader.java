package com.mycompany.app.iohandler;


import java.io.File;
import java.util.Scanner;
import java.util.*;
import com.mycompany.app.datastructures.Sentence;
import com.mycompany.app.datastructures.XMLfileInfo;

//import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.util.Optional;

public class XMLinputreader
{

    public static List<XMLfileInfo> readFiles(String folderName)
    {
      File folder = new File(folderName);
      File[] listOfFiles = folder.listFiles();
      List<XMLfileInfo> filesInfo = new ArrayList<>();
      for (File file : listOfFiles)
      {
        if (file.isFile()) {
          //System.out.println(file.getAbsolutePath());
          Optional<XMLfileInfo> rrr = readXMLfiles(file.getAbsolutePath());
          	rrr.ifPresent( value -> filesInfo.add(value));
          }
          //break;
        }
        return filesInfo;
    }

    public static Optional<XMLfileInfo> readXMLfiles(String filename)
    {

      XMLfileInfo rrr = null;
      try {
            System.out.println("reading ==========="+filename);
          	File fXmlFile = new File(filename);
          	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

          dbFactory.setValidating(false);
          dbFactory.setNamespaceAware(true);
          dbFactory.setFeature("http://xml.org/sax/features/namespaces", false);
          dbFactory.setFeature("http://xml.org/sax/features/validation", false);
          dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
          dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
          	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

          	Document doc = dBuilder.parse(fXmlFile);

          	//optional, but recommended
          	//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
          	doc.getDocumentElement().normalize();

          	//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

          	NodeList nList = doc.getDocumentElement().getElementsByTagName("SEG");//.item(0).getElementsByTagName ("SEG");
            List<Sentence> lines = new ArrayList<>();

          	//System.out.println("----------------------------");

          	for (int temp = 0; temp < nList.getLength(); temp++)
            {

          		Node nNode = nList.item(temp);

          		//System.out.println("\nCurrent Element :" + nNode.getNodeName());

          		if (nNode.getNodeType() == Node.ELEMENT_NODE)
              {

          			Element eElement = (Element) nNode;
                String sen = eElement.getElementsByTagName("ORIGINAL_TEXT").item(0).getTextContent();
                Sentence st = new Sentence(sen, temp);
                lines.add(st);
          			//System.out.println("Staff id : " + eElement.getAttribute("id") + "  " +temp);
          			//System.out.println("text : " + eElement.getElementsByTagName("ORIGINAL_TEXT").item(0).getTextContent());
          			//System.out.println("Last Name : " + eElement.getElementsByTagName("lastname").item(0).getTextContent());
          			//System.out.println("Nick Name : " + eElement.getElementsByTagName("nickname").item(0).getTextContent());
          			//System.out.println("Salary : " + eElement.getElementsByTagName("salary").item(0).getTextContent());

          		}
          	}
             rrr = new XMLfileInfo(filename, lines);
             //System.out.println(lines.size());
             //System.out.println(rrr);
            //return rrr;
        } catch (Exception e)
        {
          	e.printStackTrace();

        }
        return Optional.ofNullable(rrr);
    }
}
