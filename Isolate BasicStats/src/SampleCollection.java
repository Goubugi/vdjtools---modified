import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class SampleCollection {
	//Map<String, Sample> sampleMap = new HashMap();
	public ArrayList<Sample> sampleList = new ArrayList<>();
	public File file;
	public Scanner s;
	public String headerLine;
	
	public SampleCollection(String location) {
			file = new File(location);
		try {
			 s = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if(s.hasNextLine()) {
			headerLine = s.nextLine();
			headerLine=headerLine.substring(11, headerLine.length());
		}
		
		String currentLine;
		while(s.hasNextLine()) {
			currentLine=s.nextLine();
			SampleMetadata sampleMetadata = new SampleMetadata();
			Scanner minorLoop = new Scanner(currentLine);
			String path="";
			if(minorLoop.hasNext()) {
				String currentPath = minorLoop.next();
				 path = "C:\\Users\\23305\\Desktop\\vdjtools-examples-master"+currentPath.substring(2, currentPath.length());
			}
			while(minorLoop.hasNext()) {
				sampleMetadata.entries.add(minorLoop.next());
			}
			Sample sample = new Sample(sampleMetadata, path);
			sampleList.add(sample);
			
		}
		
	}
	
	
	
}
