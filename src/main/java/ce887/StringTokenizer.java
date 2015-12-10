/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ce887;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

/**
 *
 * @author USER END
 */
public class StringTokenizer {
    private Tokenizer tokenizer;

    public StringTokenizer() {
            // Initialize the tokenization model
            tokenizer = loadTokenizerModel("en-token.bin");
    }

    private Tokenizer loadTokenizerModel(String modelName) {
        InputStream modelIn = null;

        TokenizerModel model;

        try {
            // Load the file of the Tokenization model
            modelIn = new FileInputStream(modelName);
            model = new TokenizerModel(modelIn);
        }
        catch (IOException e) {
            //Model loading failure, handle error
            model = null;
            e.printStackTrace();
        }
        finally {
            if (modelIn != null) {
                try {
                    modelIn.close();
                }
                catch (IOException e) {
                }
            }
        }

        // Return the model of the tokenizer
        return new TokenizerME(model);
    }
    
    public String tokenize(String text) {
        // Tokenize the text and save it into and array
        String [] tokens = tokenizer.tokenize(text);
        StringBuilder tokenizedText = new StringBuilder();
        
        // Create a single string with the tokens obtained from the text
        for (int i = 0; i < tokens.length; i++) {
            tokenizedText.append(tokens[i] + " ");
        }
        
        // Return the tokenized version of the text as a String
        return tokenizedText.toString();
    }
    
    public String[] tokenizeToArray(String text) {
        // Return the tokenized text as an array of Strings
        // Each element of the array is a token
        return tokenizer.tokenize(text);
    }
    
}
