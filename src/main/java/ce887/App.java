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
    	/**
    	 * First, load the input HTML file and get its contained text using Jsoup.
    	 */
    	HTMLParser parser = new HTMLParser();
        String text = parser.parseHTML("The Udo.html");
        
        // Print out text as it's scraped from the HTML (not very human-readable)
        PrintWriter outText = new PrintWriter("textFromHTML.txt");
        outText.println(text);
        outText.close();
        
        /**
         * Pre-process resulting text after HTML scraping, in order to separate by sentences.
         * Then split the sentences into tokens, which can be tagged using the POS tagger provided by OpenNLP.
         */
        SentenceDetector sentDet = new SentenceDetector();
        StringTokenizer tokenizer = new StringTokenizer();
        POSTagger postagger = new POSTagger();
        
        // split the text into sentences
        String[] sentences = sentDet.getSentencesFromText(text);
        
        String[][] tokenizedSentences = new String[sentences.length][];
        String[][] posTags = new String[sentences.length][];
        
        PrintWriter outTokens = new PrintWriter("tokenizedText.txt");
        PrintWriter outTagged = new PrintWriter("taggedText.txt");
        
        for (int i = 0; i < sentences.length; i++) {
        	// split each sentence into an array of tokens
        	String[] tokens = tokenizer.tokenizeToArray(sentences[i]);
        	// get each tokens POS tag
        	String[] tags = postagger.tagTokens(tokens);
        	
        	/**
        	 * After tokenizing the sentence, and tagging each token in the sentence,
        	 * we need to build them back into human-readable sentences.
        	 * The first file will contain simply the tokens separated by spaces.
        	 * The second file contains the tokens, each followed by an underscore '_' and its POS tag.
        	 */
        	StringBuilder tokenizedSentence = new StringBuilder();
        	StringBuilder taggedSentence = new StringBuilder();
        	for (int j = 0; j < tokens.length; j++) {
        		if (tokenizedSentence.length() > 0) {
        			tokenizedSentence.append(' ');
        			taggedSentence.append(' ');
        		}
        		tokenizedSentence.append(tokens[j]);
        		taggedSentence.append(tokens[j]);
        		taggedSentence.append('_');
        		taggedSentence.append(tags[j]);
        	}
        	outTokens.println(tokenizedSentence.toString());
        	outTagged.println(taggedSentence.toString());
        	System.out.println(tokenizedSentence.toString());
        	System.out.println(taggedSentence.toString());
        	
        	tokenizedSentences[i] = tokens;
        	posTags[i] = tags;
        }
        
        outTokens.close();
        outTagged.close();
        
        /**
         * Now we take the tokenized sentences and run several of OpenNLP's NER models on them.
         * Each model's entities are output into a separate file, with each extracted entity occupying a line of text.
         * Since this approach takes too many lines of text, the output is not logged to console.
         */
        System.out.println("NER Tagging commencing.");
        EntityExtracter entities = new EntityExtracter();
        
        PrintWriter outPeople = new PrintWriter("peopleNER.txt");
        PrintWriter outLocations = new PrintWriter("locationsNER.txt");
        PrintWriter outOrganizations = new PrintWriter("organizationsNER.txt");
        PrintWriter outDates = new PrintWriter("datesNER.txt");
        
        for (String[] tokens : tokenizedSentences) {
        	String people = entities.extractPeople(tokens);
        	String locations = entities.extractLocations(tokens);
        	String organizations = entities.extractOrganizations(tokens);
        	String dates = entities.extractDates(tokens);
        	if (people.trim().length() > 0) {
        		outPeople.println(people);
        	}
        	if (locations.trim().length() > 0) {
        		outLocations.println(locations);
        	}
        	if (organizations.trim().length() > 0) {
        		outOrganizations.println(organizations);
        	}
        	if (dates.trim().length() > 0) {
        		outDates.println(dates);
        	}
        }
        
        System.out.println("NER Tagging done!");
        
        outPeople.close();
        outLocations.close();
        outOrganizations.close();
        outDates.close();
    }
}
