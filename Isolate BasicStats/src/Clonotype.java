import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Clonotype {
	public long count;
	public double freq;
	
	public String annotation;
	public Sample parent;
	public  ArrayList<Integer> segmPoints = new ArrayList<>();
	public  String v;
	public  String d;
	public  String j;
	public  String cdr3nt;
	public  String cdr3aa;
	public boolean isComplete;
	public boolean inFrame;
	public boolean noStop;
	final static String OOF_SYMBOLS_POSSIBLE = "([atgc#~_\\?])+";
	final static String OOF_CHAR = "_";
	final static String STOP_CHAR = "*";
	public Clonotype(long cCount,double cfreq,String cdr3nt,String cdr3aa,String v,String d,String j,int vEnd,int dStart,int dEnd,int jStart,Sample parent) {
		count=cCount;
		freq=cfreq;
		this.v=v;
		this.d=d;
		this.j=j;
		this.cdr3nt=cdr3nt;
		this.cdr3aa=cdr3aa;
		segmPoints.add(vEnd);
		segmPoints.add(dStart);
		segmPoints.add(dEnd);
		segmPoints.add(jStart);
		this.parent=parent;
		isComplete = cdr3aa.length() > 0;
		inFrame=inFrame(cdr3aa);
		noStop=noStop(cdr3aa);
		
	}
	
	 @Override
	    public boolean equals(Object o) {
	    
			if(main.calc ==1) {
		        if (this == o) return true;
		        if (o == null || getClass() != o.getClass()) return false;
		
		        Clonotype clonotype = (Clonotype) o;
		
		        if (!cdr3nt.equals(clonotype.cdr3nt)) return false;
		        if (!j.equals(clonotype.j)) return false;
		        if (!parent.equals(clonotype.parent)) return false;
		        if (!v.equals(clonotype.v)) return false;
		
		
		        return true;
	    	}else if(main.calc ==2) {
	   
	    		if (this == o) return true;
	    
		        if (o == null || getClass() != o.getClass()) return false;
		
		        Clonotype clonotype = (Clonotype) o;
		     
		        if (!cdr3nt.equals(clonotype.cdr3nt)) return false;
		       // if (!j.equals(clonotype.j)) return false;
		     
		        if (!parent.equals(clonotype.parent)) return false;   
		       // if (!v.equals(clonotype.v)) return false;
		  
		        return true;
	    	}else {
	    		if (this == o) return true;
		        if (o == null || getClass() != o.getClass()) return false;
		
		        Clonotype clonotype = (Clonotype) o;
		
		     //   if (!cdr3nt.equals(clonotype.cdr3nt)) return false;
		        if (!j.equals(clonotype.j)) return false;
		        if (!parent.equals(clonotype.parent)) return false;
		        if (!v.equals(clonotype.v)) return false;
		
		
		        return true;
	    	}
	    }
	 
	   @Override
	    public int hashCode() {
	    	int result = parent.hashCode();
	    	if(main.calc ==1) {
		        result = 31 * result + v.hashCode();
		        result = 31 * result + j.hashCode();
		        result = 31 * result + cdr3nt.hashCode();
	    	}else if(main.calc==2) {
		     //   result = 31 * result + v.hashCode();
		   //     result = 31 * result + j.hashCode();
		        result = 31 * result + cdr3nt.hashCode();
	    	}else {
		        result = 31 * result + v.hashCode();
		        result = 31 * result + j.hashCode();
		   //     result = 31 * result + cdr3nt.hashCode();
	    	}
	        return result;
	    }
	    public int getInsertSize() {
	        return (segmPoints.get(0) >= 0 && segmPoints.get(1) >= 0 && segmPoints.get(2) >= 0 && segmPoints.get(3) >= 0) ? getVDIns() + getDJIns() : -1;
	    }
	    public int getVDIns() {
	        return (segmPoints.get(0) >= 0 && segmPoints.get(1) >= 0) ? segmPoints.get(1)- segmPoints.get(0) - 1 : -1;
	    }
	 
	    public int getDJIns() {
	        return (segmPoints.get(3) >= 0 && segmPoints.get(2) >= 0) ? segmPoints.get(3) - segmPoints.get(2) - 1 : -1;
	    }
	    public int getNDNSize() {
	        return (segmPoints.get(0) >= 0 && segmPoints.get(3) >= 0) ? segmPoints.get(3) - segmPoints.get(0) - 1 : 0;
	    }
	    public boolean isCoding() {
	        return noStop && inFrame;
	    }
    void append(Clonotype other) {
        this.count += other.count;
        this.freq += other.freq;
    }
    static boolean noStop(String seq) {
        return !(seq.contains(STOP_CHAR));
    }
    public double getFreq() {
        return count / (double) parent.count;
    }
    static boolean inFrame(String seq) {
    	Pattern pattern = Pattern.compile(OOF_SYMBOLS_POSSIBLE);
    	Matcher matcher = pattern.matcher(seq);
    	return !matcher.find();
       // return !((boolean) (seq =~ OOF_SYMBOLS_POSSIBLE));

    }
}
