import java.util.Scanner;

public class Multi extends Thread{

	public static final String ROOT_DIR = "D:/tmp/bot/";
	
	@Override
	public void run() {
		super.run();
	
		
	}
	
	
	public static void main(String[] args) {
		Crawler.init();
		
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter the keyword to Search: ");

		String keyword = scan.next();
		
		Crawler.readAllLineFromAllFilesRecursively(ROOT_DIR, "");

//		Crawler.findStartingMatch(keyword);
		Crawler.findExactMatch(keyword);
	}
}
