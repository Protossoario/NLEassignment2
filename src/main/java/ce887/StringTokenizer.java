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
    
    public String tokenize(String text) {
        
        InputStream modelIn = null;
        
        TokenizerModel model;
        
        try {
            modelIn = new FileInputStream("en-token.bin");
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
        
        Tokenizer tokenizer = new TokenizerME(model);
        String [] tokens = tokenizer.tokenize(text);
        StringBuilder tokenizedText = new StringBuilder();
        
        
        for (int i = 0; i < tokens.length; i++) {
            tokenizedText.append(tokens[i] + " ");
        }
        
        return tokenizedText.toString();
    }
    
    public String[] tokenizeToArray(String text) {
    	InputStream modelIn = null;
        
        TokenizerModel model;
        
        try {
            modelIn = new FileInputStream("en-token.bin");
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
        
        Tokenizer tokenizer = new TokenizerME(model);
        
        return tokenizer.tokenize(text);
    }
    
}
