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
    	System.out.println(parser.parseHTML("The Udo.html"));
    }
}
