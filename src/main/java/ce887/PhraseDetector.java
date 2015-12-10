package ce887;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.util.Span;


public class PhraseDetector {
    
    private ChunkerME chunker;
    
    public PhraseDetector () {
        //Initialize chunk parser model
        chunker = loadChunkParserModel("en-chunker.bin");
    }
    
    private ChunkerME loadChunkParserModel (String modelName) {
        
        InputStream modelIn = null;
        ChunkerModel model = null;

        try {
            // Load file of the chunk parser model
            modelIn = new FileInputStream(modelName);
            model = new ChunkerModel(modelIn);
        } catch (IOException e) {
            // Model loading failed, handle the error
            e.printStackTrace();
        } finally {
            if (modelIn != null) {
                try {
                    modelIn.close();
                } catch (IOException e) {
                }
            }
        }
        
        // Return model of the chunk parser
        return new ChunkerME(model);
    }
    
    
    /**
     * Extract the noun phrases from the tokens and return them as an array of Strings.
     * There must be an entry in posTags for each entry in tokens.
     * @param tokens Array of tokens.
     * @param posTags Array of POS tags each corresponding to an element in the tokens array.
     * @return String[] Each element represents a phrase.
     */
    public String[] extractPhrases(String[] tokens, String[] posTags) {
    	Span[] chunks = chunker.chunkAsSpans(tokens, posTags);
    	String[] chunkStrings = Span.spansToStrings(chunks, tokens);
    	
    	ArrayList<String> result = new ArrayList<String>();
    	for (int i = 0; i < chunks.length; i++) {
    		if (chunks[i].getType().equals("NP")) {
    			result.add(chunkStrings[i]);
    		}
    	}
    	
    	return result.toArray(new String[result.size()]);
    }
}
