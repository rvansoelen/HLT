import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
	
	public static void main(String [ ] args) throws IOException, XMLStreamException
	{
		System.out.println("Please enter input file:");
		BufferedReader conBr = new BufferedReader(new InputStreamReader(System.in));
		String filename = conBr.readLine();
		if(filename.isEmpty()) {
			filename = "/home/vansorm1/Downloads/sense.txt";
		}	
		
		StreamTokenizer tkr = new StreamTokenizer(new BufferedReader(new InputStreamReader(new FileInputStream(filename))));
		tkr.lowerCaseMode(true);
		tkr.ordinaryChar('<');
		tkr.ordinaryChar('>');
		Map<String, List<Integer>> words = new HashMap<String, List<Integer>>();
		Map<String, Boolean> isInDoc = new HashMap<String, Boolean>();
		boolean inDoc = false;
		while(tkr.nextToken() != StreamTokenizer.TT_EOF) {
			if(tkr.ttype == StreamTokenizer.TT_WORD && inDoc) {
				String token = tkr.sval;
				List<Integer> dflt = new ArrayList<Integer>();
				dflt.add(0);
				dflt.add(0);
				List<Integer> entry = words.getOrDefault(token, dflt);
				entry.set(0, entry.get(0) + 1);  //increment collection frequency
				
				if(!isInDoc.containsKey(token)) {
					isInDoc.put(token, true);
					entry.set(1,  entry.get(1) + 1); //increment document frequency
				}
				words.put(token,  entry);
				
			} else if(tkr.ttype == '<') {
				inDoc = false;
				isInDoc.clear();
			} else if(tkr.ttype == '>') {
				inDoc = true;
			}
		}
		List<Map.Entry<String, List<Integer>>> sortedWords = new ArrayList<Map.Entry<String, List<Integer>>>(words.entrySet());
		sortedWords.sort(new Comparator<Map.Entry<String, List<Integer>>> () {
		 public int compare( Map.Entry<String, List<Integer>> o1, Map.Entry<String, List<Integer>> o2 )
            {
                return (o2.getValue().get(0)).compareTo( o1.getValue().get(0) );  //sort by collection frequency
            }
		});
		
		for(int i = 0; i < 30; i++) {
			Map.Entry<String, List<Integer>> entry =  sortedWords.get(i);
			System.out.println(entry.getKey() + ": " + entry.getValue().get(0) + " counts, in " + entry.getValue().get(1) + " documents");
		}
		System.out.println("");
		Map.Entry<String, List<Integer>> entry =  sortedWords.get(100);
		System.out.println("100th word: " + entry.getKey() + ": " + entry.getValue().get(0) + " counts, in " + entry.getValue().get(1) + " documents");
		entry =  sortedWords.get(500);
		System.out.println("500th word: " + entry.getKey() + ": " + entry.getValue().get(0) + " counts, in " + entry.getValue().get(1) + " documents");
		entry =  sortedWords.get(1000);
		System.out.println("1000th word: " + entry.getKey() + ": " + entry.getValue().get(0) + " counts, in " + entry.getValue().get(1) + " documents");
		System.out.println("");
		int count = 0;
		for(int i = sortedWords.size()-1; i >= 0; i--) {
			entry =  sortedWords.get(i);
			if(entry.getValue().get(0) == 1) {
				count++;
			} else {
				break;
			}
		}
		System.out.println("There were " + count + " hapax legomena in this file");
	}

}
