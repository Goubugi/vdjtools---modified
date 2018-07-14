public class main {
	//let this class be equal to CalcBasicStats
	static boolean unweighted;
	static String metadataLocation= "C:\\Users\\23305\\Desktop\\vdjtools-examples-master\\aging\\metadata.small.txt";
	public static int calc=1;
	static SampleCollection sampleCollection;
	
	public static void main(String args[]) {
		//Collects the needed information user inputs into the program
		userInput();
		//Collects all the data and organizes it, prepares it for calculation. 
		 sampleCollection = new SampleCollection(metadataLocation);
		calculateData();
		
		
		
		
//		Println to see if data is stored correctly		
//		System.out.println(sampleCollection.headerLine);
//		for(int i=0;i<sampleCollection.sampleList.size();i++) {
//			System.out.println(sampleCollection.sampleList.get(i).sampleMetadata);
//			System.out.println(sampleCollection.sampleList.get(i).ClonotypeCollection.get(0).cdr3nt);
//		}
		
	}
	
	public static void userInput() {
		
	}
	
	public static void calculateData() {
		System.out.println(sampleCollection.headerLine+"\t"+BasicStats.header);
		for(int i=0;i<sampleCollection.sampleList.size();i++) {
			BasicStats b = new BasicStats(sampleCollection.sampleList.get(i),!unweighted);
			System.out.println(sampleCollection.sampleList.get(i).sampleMetadata);
			System.out.println(b);
			
		}
	}
	

	
}
