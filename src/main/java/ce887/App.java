package ce887;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws FileNotFoundException
    {
    	HTMLParser parser = new HTMLParser();
        String text = parser.parseHTML("The Udo.html");
        PrintWriter outText = new PrintWriter("textFromHTML.txt");
        outText.println(text);
    	//System.out.println(text);
        
        StringTokenizer tokenizer = new StringTokenizer();
        String tokenizedText = tokenizer.tokenize(text);
        PrintWriter outTokens = new PrintWriter("tokenizedText.txt");
        outTokens.println(tokenizedText);
        //System.out.println(tokenizedText);
        
        POSTagger postagger = new POSTagger();
        String taggedText = postagger.tag(tokenizedText);
        PrintWriter outTagged = new PrintWriter("taggedText.txt");
        outTagged.println(taggedText);
        //System.out.println(taggedText);
        
        EntityExtracter entities = new EntityExtracter();
        String[] tokens = tokenizer.tokenizeToArray(text);
        String people = entities.extractPeople(tokens);
        PrintWriter outPeople = new PrintWriter("peopleNER.txt");
        outPeople.println(people);
        System.out.println(people);
        
        outText.close();
        outTokens.close();
        outTagged.close();
        outPeople.close();
    }
}
