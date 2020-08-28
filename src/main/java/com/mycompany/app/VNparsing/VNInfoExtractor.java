package com.mycompany.app.VNparsing;

import io.github.clearwsd.parser.*;
import io.github.semlink.verbnet.*;
import io.github.semlink.parser.*;
import io.github.semlink.verbnet.semantics.SemanticPredicate;
import io.github.semlink.verbnet.semantics.SemanticArgument;
import io.github.semlink.propbank.type.PropBankArg;
import io.github.semlink.semlink.VerbNetAligner;
import com.mycompany.app.datastructures.JsonElements;
import io.github.semlink.verbnet.type.SemanticArgumentType;
import static io.github.semlink.parser.VerbNetParser.pbRoleLabeler;
import io.github.semlink.verbnet.semantics.EventArgument;
import io.github.semlink.verbnet.semantics.ThematicRoleArgument;
import io.github.semlink.verbnet.semantics.VerbSpecificArgument;
import io.github.semlink.verbnet.type.ThematicRoleType;
import java.util.*;
import io.github.semlink.app.Span;
import io.github.semlink.propbank.type.PropBankArg;
import io.github.semlink.semlink.PropBankPhrase;
import io.github.clearwsd.type.DepNode;
import io.github.clearwsd.type.DepTree;
import java.util.stream.Collectors;
import io.github.semlink.parser.Proposition;
import io.github.semlink.semlink.SemlinkRole;
import io.github.semlink.app.Chunking;
import com.mycompany.app.datastructures.Sentence;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;
@Setter
@Getter
@Accessors(fluent = true)

public class VNInfoExtractor{

	 // VerbNet index over VerbNet classes/frames
    VnIndex verbNet = new DefaultVnIndex();

    // Dependency parser used for WSD model and alignment logic
    NlpParser dependencyParser = new Nlp4jDependencyParser();
    // WSD model for predicting VerbNet classes (uses ClearWSD and the NLP4J parser)
    VerbNetSenseClassifier classifier = VerbNetSenseClassifier.fromModelPath("/media/abhidip/2F1499756FA9B115/java_workspace/semparse/nlp4j-verbnet-3.3.bin",
            verbNet, dependencyParser);
    // PropBank semantic role labeler from a TF NLP saved model
    SemanticRoleLabeler<PropBankArg> roleLabeler = pbRoleLabeler("/media/abhidip/2F1499756FA9B115/java_workspace/semparse/propbank-srl");
    // maps nominal predicates with light verbs to VerbNet classes (e.g. take a bath -> dress-41.1.1)
    LightVerbMapper verbMapper = LightVerbMapper.fromMappingsPath("/media/abhidip/2F1499756FA9B115/java_workspace/semparse/lvm.tsv", verbNet);
    // aligner that uses PropBank VerbNet mappings and heuristics to align PropBank roles with VerbNet thematic roles
    VerbNetAligner aligner = VerbNetAligner.of("/media/abhidip/2F1499756FA9B115/java_workspace/semparse/pbvn-mappings.json", "/media/abhidip/2F1499756FA9B115/java_workspace/semparse/unified-frames.bin");
    VnPredicateDetector predicateDetector = new DefaultVnPredicateDetector(classifier, verbMapper);

    // simplifying facade over the above components
    VerbNetParser parser = new VerbNetParser(predicateDetector, classifier, roleLabeler, aligner);

		public void processPredicate(SemanticPredicate pr)
		{
			 String type = pr.type().toString();
			 List<SemanticArgument> arguments = pr.arguments();
			 System.out.println("++++++++" + pr);
			 System.out.println("********"+type);
			 System.out.println("********"+arguments);

			 for(int i=0;i<arguments.size();i++)
			 {
				 SemanticArgument sa = arguments.get(i);
				 String saType = sa.getClass().getSimpleName().toString();
				 System.out.println("######"+sa.type()+"\t"+sa.value()+"\t"+saType);

				 if(saType.equalsIgnoreCase("EventArgument"))
				 {
					 EventArgument arg = (EventArgument) sa;
					 System.out.println("\t 111######" + arg.variable().getClass().getName());
					 System.out.println("\t 111######" + arg.variable());
				 }
				 else if(saType.equalsIgnoreCase( "ThematicRoleArgument"))
				 {
					 ThematicRoleArgument arg = (ThematicRoleArgument) sa;
					 //System.out.println("\t 222######" + arg.variable().getClass().getName());
					 System.out.println("\t 222######" + arg.variable());


					 PropBankPhrase var = (PropBankPhrase)arg.variable();
					 DepTree parse = var.parse();
					 Span<PropBankArg> span = var.span();
					 PropBankArg pa = var.argument();
					 String tokens = span.get(parse).stream().map(Object::toString).collect(Collectors.joining(" ")) ;
					 System.out.println("\t 222######" +tokens);
					 System.out.println("\t 222######" +pa);
				 }
				 else if(saType.equalsIgnoreCase("VerbSpecificArgument"))
				 {
					 VerbSpecificArgument arg = (VerbSpecificArgument) sa;
					 System.out.println("\t 333######" + arg.variable().getClass().getSimpleName());
					 System.out.println("\t 333######" + arg.variable());
				 }

			 }
		}

    public void thematicRoleAnalysis2(Proposition<VnClass, SemlinkRole> prop, List<String> tokens, Sentence se)
    {

        //System.out.println("-------------------------------------------");
        Chunking<SemlinkRole> arguments = prop.arguments();
        VnClass predicate = prop.predicate();
        //System.out.println("++++"+predicate.verbNetId());
        se.addVnLabels(predicate.verbNetId().toString());

        List<Span<SemlinkRole>> spans = arguments.spans();
        List<String> BIO = new ArrayList<>();

        for (String t: tokens)
        {
          BIO.add("O");
        }

        for(Span<SemlinkRole> sp: spans)
        {

          SemlinkRole label = sp.label();
          String phrase = sp. get(tokens).stream().map(Object::toString).collect(Collectors.joining(" "));
          Optional<PropBankArg> pArg = label.pb();
          //PropBankArg pArgVal = pArg.get();
          Optional<ThematicRoleType> tr =  label.vn();
          StringBuilder pArgS = new StringBuilder();
          //String pArgS = "";
          //pArg.ifPresent( value -> pArgS.append(value.toString()));
          tr.ifPresent( value -> pArgS.append(value.toString()));

          if(tr.isPresent() && tr.get().toString().equalsIgnoreCase("Verb"))
          {
            se.addWSLabels(tokens.get(sp.startIndex()), predicate.verbNetId().toString());
          }


          //System.out.println(pArgS.toString() + '\t' + phrase);

          for(int i= sp.startIndex(); i<= sp.endIndex();i++)
          {
              String tag = pArgS.toString();
              if (i == sp.startIndex())
                tag = "B-"+ tag;
              else
                tag = "I-"+ tag;
              BIO.set(i, tag);
          }

        }
        //System.out.println(tokens.stream().map(Object::toString).collect(Collectors.joining(" ")));
        //System.out.println(BIO.stream().map(Object::toString).collect(Collectors.joining(" ")));
        se.addTagTokens(BIO);
        //System.out.println("-------------------------------------------");
        //return BIO;
    }

    public void getAnnotation2(Sentence se)
    {

      //System.out.println("===="+se.id()+" =====" + se.sentence());
      VerbNetParse parse = parser.parse(se.sentence());

      //System.out.println(parse);
      List<VerbNetProp> props = parse.props();
      for(int i=0;i<props.size();i++)
			{
				 DefaultVerbNetProp dp = ( DefaultVerbNetProp)props.get(i);
				 List<String> tokens = dp.getTokens();

         if (se.tokens() == null)
          se.tokens(tokens);
         if(dp.getProposition() == null || tokens == null)
         {
            System.out.println("ERROR in sen:"+se.id()+": "+se.sentence());
         }
         else
          thematicRoleAnalysis2(dp.getProposition(), tokens, se);
      }
    }

		public void thematicRoleAnalysis(Proposition<VnClass, SemlinkRole> prop, List<String> tokens, Sentence se)
		{

				//System.out.println("-------------------------------------------");
				Chunking<SemlinkRole> arguments = prop.arguments();
				VnClass predicate = prop.predicate();
				//System.out.println("++++"+predicate.verbNetId());
        se.addVnLabels(predicate.verbNetId().toString());

				List<Span<SemlinkRole>> spans = arguments.spans();
				List<String> BIO = new ArrayList<>();

				for (String t: tokens)
				{
					BIO.add("O");
				}

				for(Span<SemlinkRole> sp: spans)
				{

					SemlinkRole label = sp.label();
					String phrase = sp. get(tokens).stream().map(Object::toString).collect(Collectors.joining(" "));
					Optional<PropBankArg> pArg = label.pb();
					//PropBankArg pArgVal = pArg.get();
					Optional<ThematicRoleType> tr =  label.vn();
					StringBuilder pArgS = new StringBuilder();
					//String pArgS = "";
					pArg.ifPresent( value -> pArgS.append(value.toString()));
          //tr.ifPresent( value -> pArgS.append(value.toString()));

          //System.out.println(pArgS.toString() + '\t' + phrase);

					for(int i= sp.startIndex(); i<= sp.endIndex();i++)
					{
							String tag = pArgS.toString();
							if (i == sp.startIndex())
								tag = "B-"+ tag;
							else
								tag = "I-"+ tag;
							BIO.set(i, tag);
					}

				}
        //System.out.println(tokens.stream().map(Object::toString).collect(Collectors.joining(" ")));
        //System.out.println(BIO.stream().map(Object::toString).collect(Collectors.joining(" ")));
        se.addTagTokens(BIO);
			  //System.out.println("-------------------------------------------");
        //return BIO;
		}


    public void getAnnotation(Sentence se)
    {

      //System.out.println("===="+se.id()+" =====" + se.sentence());
      VerbNetParse parse = parser.parse(se.sentence());

      //System.out.println(parse);
      List<VerbNetProp> props = parse.props();
      for(int i=0;i<props.size();i++)
			{
				 DefaultVerbNetProp dp = ( DefaultVerbNetProp)props.get(i);
				 List<String> tokens = dp.getTokens();

         if (se.tokens() == null)
          se.tokens(tokens);
         if(dp.getProposition() == null || tokens == null)
         {
            System.out.println("ERROR in sen:"+se.id()+": "+se.sentence());
         }
         else
          thematicRoleAnalysis(dp.getProposition(), tokens, se);
      }
    }

    /*public void getAnnotation(String sentence)
    {
      VerbNetParse parse = parser.parse(sentence);
      System.out.println(sentence);
      System.out.println(parse);
      List<VerbNetProp> props = parse.props();
      //List<String> tokens =
      for(int i=0;i<props.size();i++)
      {
         DefaultVerbNetProp dp = ( DefaultVerbNetProp)props.get(i);
         List<String> tokens = dp.getTokens();
         List<SemanticPredicate> predicates = dp.predicates();
         thematicRoleAnalysis(dp.getProposition(), tokens);
         //System.out.print(dp.getProposition().toString(tokens)); need to decode this
         //System.out.println(dp.vncls() );
         //System.out.println("++++"+dp.vncls().verbNetId() );


         //processPredicate(predicates.get(0));
         //System.out.println("++++"+ predicates.get(0).type());

         //break;
      }
        //System.out.println(props);
    }
    public void getAnnotation(JsonElements je)
    {
    	List< String > sens = je.getSentence();
    	for (int i = 0; i < sens.size(); i++)
    	{
    		getAnnotation(sens.get(i));
    		break;
    	}
    }*/
}
