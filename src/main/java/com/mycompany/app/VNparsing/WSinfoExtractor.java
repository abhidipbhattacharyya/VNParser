package com.mycompany.app.VNparsing;


import java.util.*;

import io.github.clearwsd.DefaultSensePredictor;
import io.github.clearwsd.SensePrediction;
import io.github.clearwsd.corpus.ontonotes.OntoNotesSense;
import io.github.clearwsd.parser.Nlp4jDependencyParser;
import com.mycompany.app.datastructures.Sentence;

public class WSinfoExtractor{


  Nlp4jDependencyParser parser; // load dependency parser
  DefaultSensePredictor<OntoNotesSense> wsd; // load WSD model

  public WSinfoExtractor()
  {
    parser = new Nlp4jDependencyParser(); // load dependency parser
    wsd = DefaultSensePredictor.loadFromResource("models/nlp4j-ontonotes.bin", parser); // load WSD model
  }

  public WSinfoExtractor(Nlp4jDependencyParser parser) // to use same parser for both VNExtractor
  {
    this.parser = parser;
    wsd = DefaultSensePredictor.loadFromResource("models/nlp4j-ontonotes.bin", parser); // load WSD model
  }

  public void getAnnotation(Sentence se)throws Exception
  {
      //System.out.println("==="+se.id()+"==="+se.sentence());
      List<String> labels = new ArrayList<>();
      if(se.tokens() == null)
      {
        List<String> tokens = parser.tokenize(se.sentence()); // split sentence into tokens
        se.tokens(tokens);
      }
      for (SensePrediction<OntoNotesSense> prediction : wsd.predict(se.tokens()))
      {
        //System.out.println(prediction.originalText()+"----"+ prediction.id()+"---"+ prediction.sense().getNumber() + " --> " + prediction.sense().getName());
        labels.add(prediction.id());
        se.addWSLabels(prediction.id());
      }
      //return labels;
  }
}
