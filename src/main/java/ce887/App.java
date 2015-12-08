package ce887;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	HTMLParser parser = new HTMLParser();
        String text = parser.parseHTML("The Udo.html");
    	//System.out.println(text);
        
        POSTagger postagger = new POSTagger();
        String taggedText = postagger.tag(text);
        System.out.println(taggedText);
    }
}
