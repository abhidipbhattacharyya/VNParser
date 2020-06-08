package com.mycompany.app.iohandler;


import java.io.File;
import java.util.Scanner;
import java.util.*;
import com.mycompany.app.datastructures.Sentence;

public class InputReader{



	public static List<Sentence> readFileswithId(String filename, List<Integer> ids) throws Exception
	{
		System.out.println( "data file read from "+filename );
		List<Sentence> lines = new ArrayList<>();
		//return lines;
		File file = new File(filename);
		Scanner sc = new Scanner(file);
		int i=0;

		while (sc.hasNextLine())
		{
			Scanner s = new Scanner(sc.nextLine().strip()).useDelimiter("\t");
			int id = s.nextInt();
			String sen = s.next();
			if(id == ids.get(i).intValue())
			{
				i++;
				//System.out.println(id+ " ----> "+sen);
				Sentence st = new Sentence(sen, id);
				lines.add(st);
				if(i>= ids.size())
					break;
			}
			s.close();
		}
		sc.close();
		//System.out.println(lines);
		return lines;
	}


	public static List<Integer> readIdFile( String filename )throws Exception
	{
		System.out.println("Id file is read from " + filename);
		List <Integer> lines = new ArrayList<>();
		File file = new File(filename);
		Scanner sc = new Scanner(file);
		while (sc.hasNextInt())
		{
			lines.add(sc.nextInt());

		}

		//System.out.println("+++++++number of lines" + lines.size());
		//System.out.println(lines);
		return lines;
	}
}
