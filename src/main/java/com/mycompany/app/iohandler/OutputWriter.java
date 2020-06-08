package com.mycompany.app.iohandler;

import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class to handle errors
import com.mycompany.app.datastructures.Sentence;
import java.util.*;
import java.util.stream.Collectors;


public class OutputWriter{

  public static void writeSRLfiles(List<Sentence> sentences, String filename)
  {
    try {
      FileWriter myWriter = new FileWriter(filename);

      for(Sentence sen: sentences)
      {
        List<String> conlls = sen.getConllStrings();
        String toBewr = conlls.stream().map(Object::toString).collect(Collectors.joining(""));
        myWriter.write(toBewr);
        //break;
      }
      System.out.println("file written as " + filename);
      myWriter.close();
      System.out.println("Successfully wrote to the file.");
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
  }
}
