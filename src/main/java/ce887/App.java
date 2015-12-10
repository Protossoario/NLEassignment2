package ce887;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Hello world!
 *
 */
public class App 
{
	/**
	 * This Hash Map holds the tf.idf information for each term across the whole document collection.
	 * The keys are the noun-phrases found in all documents, and the value is another Hash Map.
	 * This second inner Hash Map has the names of the documents as its keys, and the tf.idf weight
	 * as its value. So to find the tf.idf for term "X" in file "Y", we would need to call:
	 * 
	 * 		termFrequencies.get(X).get(Y);
	 */
	private static HashMap<String, HashMap<String, Double>> termFrequencies = new HashMap<String, HashMap<String, Double>>();
	
	private static void processFile(String fileName) throws FileNotFoundException {
		/**
    	 * First, load the input HTML file and get its contained text using Jsoup.
    	 */
    	System.out.println("Loading HTML and parsing...");
    	HTMLParser parser = new HTMLParser();
        String text = parser.parseHTML(fileName);
        
        System.out.println("Done!");
        
        // Print out text as it's scraped from the HTML (not very human-readable)
        PrintWriter outText = new PrintWriter(fileName + "_textFromHTML.txt");
        outText.println(text);
        outText.close();
        
        /**
         * Pre-process resulting text after HTML scraping, in order to separate by sentences.
         * Then split the sentences into tokens, which can be tagged using the POS tagger provided by OpenNLP.
         */
        SentenceDetector sentDet = new SentenceDetector();
        StringTokenizer tokenizer = new StringTokenizer();
        POSTagger postagger = new POSTagger();
        
        System.out.println("Splitting HTML text into sentences.");
        // split the text into sentences
        String[] sentences = sentDet.getSentencesFromText(text);
        System.out.println("Done!");
        
        String[][] tokenizedSentences = new String[sentences.length][];
        String[][] posTags = new String[sentences.length][];
        
        PrintWriter outTokens = new PrintWriter(fileName + "_tokenizedText.txt");
        PrintWriter outTagged = new PrintWriter(fileName + "_taggedText.txt");
        
        System.out.println("Tokenizing and POS tagging each sentence...");
        
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
	    	
        	tokenizedSentences[i] = tokens;
        	posTags[i] = tags;
        }
        
        System.out.println("Done!");
        
        outTokens.close();
        outTagged.close();
        
        /**
         * Next is the noun-phrase detection part of the pipeline.
         * At the same time, we'll be counting each term's frequency within the document.
         */
        
        PhraseDetector npDet = new PhraseDetector();
        
        PrintWriter outPhrases = new PrintWriter(fileName + "_phrasesText.txt");
        
        System.out.println("Extracting noun-phrases from sentences...");
        
        int totalDocTerms = 0;
        
        for (int i = 0; i < tokenizedSentences.length; i++) {
        	String[] nounPhrases = npDet.extractPhrases(tokenizedSentences[i], posTags[i]);
        	for (String phrase : nounPhrases) {
        		outPhrases.println(phrase);
        		
        		// check if the term is in our global map of terms
        		if (termFrequencies.containsKey(phrase)) {
        			// this is a term we've previously seen (either in this or previous documents)
        			HashMap<String, Double> docFreqs = termFrequencies.get(phrase);
        			
        			// check if the term has been found in the current document
        			if (docFreqs.containsKey(fileName)) {
        				// if so, increase the count by one
        				double tf = docFreqs.get(fileName);
        				docFreqs.replace(fileName, tf + 1.0);
        			} else {
        				// otherwise start the count at 1
        				docFreqs.put(fileName, 1.0);
        			}
        		} else {
        			// this is the first time we've found this term
        			HashMap<String, Double> docFreqs = new HashMap<String, Double>();
        			
        			// since it's the first time we've ever found this term, start its count for this document at 1
        			docFreqs.put(fileName, 1.0);
        			
        			// and add the term to the global map
        			termFrequencies.put(phrase, docFreqs);
        		}
        		
        		totalDocTerms++;
        	}
        }
        
        // finally we update all the terms we found,
        // dividing their frequency by the total # of terms
        for (String phrase : termFrequencies.keySet()) {
        	HashMap<String, Double> docFreqs = termFrequencies.get(phrase);
        	if (docFreqs.containsKey(fileName)) {
        		double tf = docFreqs.get(fileName);
				docFreqs.replace(fileName, tf / totalDocTerms);
        	}
        }
        
        System.out.println("Done!");
        
        outPhrases.close();
        
        /**
         * Now we take the tokenized sentences and run several of OpenNLP's NER models on them.
         * Each model's entities are output into a separate file, with each extracted entity occupying a line of text.
         * Since this approach takes too many lines of text, the output is not logged to console.
         */
        System.out.println("Extracting Named-Entities and calculating TF values...");
        EntityExtracter entities = new EntityExtracter();
        
        PrintWriter outPeople = new PrintWriter(fileName + "_peopleNER.txt");
        PrintWriter outLocations = new PrintWriter(fileName + "_locationsNER.txt");
        PrintWriter outOrganizations = new PrintWriter(fileName + "_organizationsNER.txt");
        PrintWriter outDates = new PrintWriter(fileName + "_datesNER.txt");
        
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
        
        System.out.println("Done!");
        
        outPeople.close();
        outLocations.close();
        outOrganizations.close();
        outDates.close();
	}
	
    public static void main( String[] args ) throws FileNotFoundException
    {
    	String[] fileNames = { "The Udo.html", "CE314_CE887_ Syllabus.html" };
    	for (String fName : fileNames) {
    		processFile(fName);
    	}
    	
    	/**
    	 * After processing all files, we need to calculate the idf for each term.
    	 * We will also sum all the tf.idf values and store them in a new 
    	 */
    	System.out.println("Calculating IDF values...");
    	
    	int N = fileNames.length; // N = total # of documents
    	
    	// here we store the total tf.idf for each term
    	HashMap<String, Double> totals = new HashMap<String, Double>();
    	
    	for (String phrase : termFrequencies.keySet()) {
    		HashMap<String, Double> docFreqs = termFrequencies.get(phrase);
    		// the number of documents where the phrase appears is equal to
    		// the size of its hashmap, since it contains an entry for every
    		// document where we found the term
    		int numOfDocsWithPhrase = docFreqs.size();
    		
    		double idf = Math.log(N / numOfDocsWithPhrase);
    		
    		double total = 0;
    		
    		for (String doc : docFreqs.keySet()) {
    			double tf = docFreqs.get(doc);
    			docFreqs.replace(doc, tf * idf);
    			
    			total += tf * idf;
    		}
    		
    		totals.put(phrase, total);
    	}
    	
    	System.out.println("Done!");
    	
    	System.out.println("Sorting terms by the sum of their tf.idf values...");
    	
    	// sort the termFrequencies map by the sum of each term's tf.idf values
    	ArrayList<String> sortedPhrases = new ArrayList<String>(termFrequencies.keySet());
    	Collections.sort(sortedPhrases, new TermFrequencyComparator(totals));
    	
    	System.out.println("Done!");
    	
    	PrintWriter outTfIdf = new PrintWriter("tfidfText.txt");
    	
    	outTfIdf.print("\nTerm:");
    	for (String fName : fileNames) {
    		outTfIdf.print("\t" + fName);
    	}
    	outTfIdf.println();
    	
    	// print all tf.idf values for each term
    	for (String phrase : sortedPhrases) {
    		HashMap<String, Double> docFreqs = termFrequencies.get(phrase);
    		
    		outTfIdf.print(phrase);
    		
    		for (String fName : fileNames) {
    			if (docFreqs.containsKey(fName)) {
    				outTfIdf.print("\t" + String.format("%.3f", docFreqs.get(fName)));
    			} else {
    				outTfIdf.print("\t0");
    			}
    		}
    		outTfIdf.println();
    	}
    	
    	outTfIdf.close();
    }
}
