import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Stream;

public class Crawler {
	
	public static Map<String, ArrayList<String>> store;

	public static ArrayList<FilesToIndex> filesToIndexs;
	
	public static void makeListOfFilesToIndex(final String ROOT_DIR){
		filesToIndexs = new ArrayList<FilesToIndex>();
		
        try (final Stream<Path> pathStream = Files.walk(Paths.get(ROOT_DIR), FileVisitOption.FOLLOW_LINKS)) {
        	pathStream.filter(
        				// should be a file not a dir
            			(p) -> !p.toFile().isDirectory() //&& p.toFile().getAbsolutePath().endsWith(extension)
            		).forEach(p -> saveFilesToIndex(p));
            
        } catch (final IOException e) {
        	e.printStackTrace();
        }
	}
	
	public static void saveFilesToIndex(final Path p) {
		FilesToIndex f = new FilesToIndex(p.toString(), false, false);
//		System.out.println("Path to files: " + p.toString());
		filesToIndexs.add(f);
	}
	
	public static void init() {
		store = new TreeMap<String, ArrayList<String>>();
	}
	
    /**
     * @CopiedFrom: https://www.javacodegeeks.com/2014/05/playing-with-java-8-lambdas-paths-and-files.html
     * Get all the non empty lines from all the files with the specific extension, recursively.
     *
     * @param path the path to start recursion
     * @param extension the file extension
     * @return list of lines
     */
    public static List<String> readAllLineFromAllFilesRecursively(final String path, final String extension) {
    	
        final List<String> lines = new ArrayList<>();
        
        try (final Stream<Path> pathStream = Files.walk(Paths.get(path), FileVisitOption.FOLLOW_LINKS)) {
            
        	pathStream.filter(
        				// should be a file not a dir
            			(p) -> !p.toFile().isDirectory() //&& p.toFile().getAbsolutePath().endsWith(extension)
            		).forEach(p -> indexFiles(p.toString()));
            
        } catch (final IOException e) {
        	e.printStackTrace();
        }
        return lines;
    }
 
    public static void indexFiles(String file) {
    	
    	try {
    		// store line's text
    		String line = "";
    		// store line number
    		int i = 0;
    		
    		BufferedReader bReader = new BufferedReader(new FileReader(file));
    		
			while ((line = bReader.readLine()) != null) {
				// Getting line by line
//				System.out.println("Line: " + line);
				i++;
				
				line = line.trim();
				if (line.length() == 0)
					continue;
				
				line = line.replaceAll("[!?,]", "");
				String[] words = line.split("\\s+");
				
//				got all the words, save in HashMap
				saveInStorage(words, file.toString());
				
			}
    		
        } catch (final IOException e) {
        	e.printStackTrace();
        }
    }
    
    public static void saveInStorage(String[] words, String filename)
    {
    	ArrayList arl = null;
    	String w = null;
    	
    	for(int i = 0; i < words.length; i++) {
    		w = words[i];
    		// if word already in hashmap
    		if (store.containsKey(w) ) {
    			// get its occurances list
    			arl = store.get(w);
    			// add new file to occurances list
    			arl.add(filename);
    		}
    		else {
    			// list of files
    			ArrayList<String> list = new ArrayList<String>();
    			list.add(filename);
    			// add list
    			store.put(w, list);
    		}
    	}
    }
    
    public static void findExactMatch(String keyword)
    {
    	if (store.containsKey(keyword)) {
    		ArrayList<String> files = store.get(keyword);
    		
    		System.out.println("\n\n" + keyword + " appears " + files.size() + " times. Here,\n");
    		for (String f : files) {
				System.out.println(f);
			}
    	}
    	else {
    		System.out.println("Keyword doesn't exist.");
    	}
    }
    
    public static int findStartingMatch(String keyword)
    {
//    	if (store.containsKey(keyword)) {
//    		ArrayList<String> files = store.get(keyword);
//    		
//    		System.out.println("\n\n" + keyword + " appears " + files.size() + " times. Here,\n");
//    		for (String f : files) {
//				System.out.println(f);
//			}
//    	}
//    	else {
//    		System.out.println("Keyword doesn't exist.");
//    	}

    	int times= 0;
    	int AS_DIFF = 0;
    	
    	Set<String> keys = store.keySet();
    	for (String key : keys) {
			if (key.startsWith(keyword)) {
				ArrayList<String> files = store.get(key);
				times = files.size();
				
				if (AS_DIFF == 0) {
		    		System.out.println("\n\n" + keyword + " match appears " + files.size() + " times. Here,\n");
		    		AS_DIFF++;
				}
				else {
					System.out.println("\n\n" + keyword + " match appears with different key " + files.size() + " times. Here,\n");
				}
	    		for (String f : files) {
					System.out.println(f);
				}
			}
		}
    	return times;
    }
    
    
    
//    public static void main(String[] args) {
//    	init();
//		readAllLineFromAllFilesRecursively(ROOT_DIR, "");
//		
//		Scanner scan = new Scanner(System.in);
//		
//		System.out.println("Enter the keyword to Search: ");
//		String keyword = scan.next();
//		
////		findExactMatch(keyword);
//		findStartingMatch(keyword);
//	}

     
}
class FilesToIndex {
	public String name;
	public boolean isCrawled;
	public boolean isIndexed;
	
	public FilesToIndex(String name, boolean isCrawled, boolean isIndexed) {
		this.name = name;
		this.isCrawled = isCrawled;
		this.isIndexed = isIndexed;    		
	}
}