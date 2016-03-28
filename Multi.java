import java.util.Scanner;


public class Multi extends Thread{

	public static final String ROOT_DIR = "D:/tmp/bot/";
	
	public static String method = "";
	public static String keyword = "";
	
	public Multi(String method) {
		this.method = method;
	}
	
	@Override
	public void run() {
		super.run();
	
		if (method.equals("getinput")) {
			synchronized(this) {
				getInput();
			}
		} else if (method.equals("crawlAndIndexFiles")) {
			crawlingAndIndexing();
		}
	}
	
	public static void getInput() {
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter the keyword to Search: ");
		
		keyword = scan.next();
		scan.close();
	}
	
	public static void crawlingAndIndexing() {
		for (int i = 0; i < Crawler.filesToIndexs.size(); i++) {
			FilesToIndex f = Crawler.filesToIndexs.get(i);
			
			if (!f.isCrawled) {
				Crawler.indexFiles(f.name);
				Crawler.findExactMatch(keyword);
			}
		}
	}
	
	public static void main(String[] args) {
		Crawler.makeListOfFilesToIndex(ROOT_DIR);
		Crawler.init();
		
	
		Multi t1 = new Multi("getinput");
		Multi t2 = new Multi("getinput");
		
		t1.start();
//		t2.start();
		
		try {
			t1.join();
//			t2.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Multi t3 = new Multi("crawlAndIndexFiles");
		Multi t4 = new Multi("crawlAndIndexFiles");
		Multi t5 = new Multi("crawlAndIndexFiles");
		
		t3.start();
		t4.start();
		t5.start();
		
		try {
			t3.join();
			t4.join();
			t5.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
//		Crawler.readAllLineFromAllFilesRecursively(ROOT_DIR, "");
//
////		Crawler.findStartingMatch(keyword);
//		Crawler.findExactMatch(keyword);
		
	}
}
