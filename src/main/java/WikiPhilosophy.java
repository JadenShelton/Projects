import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

public class WikiPhilosophy {

    static List<String> visited = new ArrayList<String>();
    final static WikiFetcher wikifetcher = new WikiFetcher();

    /**
     * Tests a conjecture about Wikipedia and Philosophy.
     *
     * https://en.wikipedia.org/wiki/Wikipedia:Getting_to_Philosophy
     *
     * 1. Clicking on the first non-parenthesized, non-italicized link
     * 2. Ignoring external links, links to the current page, or red links
     * 3. Stopping when reaching "Philosophy", a page with no links or a page
     *    that does not exist, or when a loop occurs
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        String destination = "https://en.wikipedia.org/wiki/Philosophy";
        String source = "https://en.wikipedia.org/wiki/Java_(programming_language)";

        testConjecture(destination, source, 20);
    }

    /**
     * Starts from given URL and follows first link until it finds the destination or exceeds the limit.
     *
     * @param destination
     * @param source
     * @throws IOException
     */
    public static void testConjecture(String destination, String source, int limit) throws IOException {
    	int counter = 0;
    	visited.add(source);
    	while(counter < limit) {
    		if(source.equals(destination)){
    			break;
    		} else {
    			
    			source = findNextLink(source);
    			System.out.println(source.toString());
    			counter++;
    		}
    	}
    	if(source.equals(destination)) {
    		System.out.println("You found Philosophy in " + counter + " tries!");
    	} else if(counter >= limit) {
    		System.out.println("Did not find Philosophy in " + limit + " tries!");
    	}
    }
    
    public static boolean isValidLink(String link) {
    	for(int i = 0; i<visited.size(); i++) {
    		if(link.contains(visited.get(i))) {
    			return false;
    		}
    	}
    	return(link.contains("wiki") && !link.contains("Greek") && !link.contains("Latin") && !link.contains("wikitionary"));
    }
    
    public static String findNextLink(String link) throws IOException {
    	
    	Elements paragraphs = wikifetcher.fetchWikipedia(link);
    	Elements links = paragraphs.select("p > a");
    	String nextLink = "";
    	for(int i = 0; i < links.size(); i++) {
    		if(isValidLink(links.get(i).toString())) {
    			nextLink = links.get(i).toString();
    			break;
    		}
    	}
    	visited.add(nextLink.substring(9, nextLink.indexOf("\"", 10)));
    	return "https://en.wikipedia.org" + nextLink.substring(9, nextLink.indexOf("\"", 10));

    }
    	
}
