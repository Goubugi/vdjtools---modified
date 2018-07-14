import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Sample {
	public static long count;
	public static double freq;
	public static long diversity;
	public static final double frequency=1;
	SampleMetadata sampleMetadata = new SampleMetadata();
	static String path;
	static ArrayList<Clonotype> ClonotypeCollection = new ArrayList<Clonotype>();
	static Scanner cScanner;
	public static String annotationHeader = null;
	
	public Sample(SampleMetadata sampleMetadata, String path) {
		this.sampleMetadata=sampleMetadata;
		this.path=path;
		collectClonotype();
	}
	
	public void collectClonotype() {
		Map<Clonotype, Clonotype> existingClonotypes = new HashMap<>();
		File dataFile = new File(path);
		try {
			 cScanner = new Scanner(dataFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(cScanner.hasNextLine()) {
			annotationHeader = cScanner.nextLine();
		}
		
		while(cScanner.hasNextLine()) {
			long cCount=cScanner.nextLong();
			double cfreq = cScanner.nextDouble();
			String cdr3nt = cScanner.next();
			String cdr3aa = cScanner.next();
			String v = cScanner.next();
			String d = cScanner.next();
			String j = cScanner.next();
			int vEnd = cScanner.nextInt();
			int dStart = cScanner.nextInt();
			int dEnd = cScanner.nextInt();
			int jStart =cScanner.nextInt();
			Clonotype clonotype = new Clonotype(cCount,cfreq,cdr3nt,cdr3aa,v,d,j,vEnd,dStart,dEnd,jStart,this);
			addClonotype(clonotype,existingClonotypes);
		}
		
		
	}
	
	public static void addClonotype(Clonotype clonotype, Map<Clonotype,Clonotype> existingClonotypes) {
		Clonotype existing= null;
		existing = existingClonotypes.get(clonotype);

		
		if (existing != null) {
			existing.append(clonotype);
		} else {
			existingClonotypes.put(clonotype, clonotype);
		}


		addClonotype(clonotype, existing);
		
	}


	public static void addClonotype(Clonotype clonotype, Clonotype existingClonotype) {
	
	    count += clonotype.count;
	    
	    freq += clonotype.freq;
	    if (existingClonotype == null) {
	        diversity++;
	            ClonotypeCollection.add(clonotype);
	    }
	
	}
}
