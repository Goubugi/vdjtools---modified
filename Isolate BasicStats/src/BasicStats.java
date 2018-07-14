import java.util.HashSet;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
public class BasicStats {
		private DescriptiveStatistics cloneSize;
		private DescriptiveStatistics cloneSizeGeom;
	    private  double cdr3ntLength;
	    private  double insertSize;
	    private  double ndnSize;
	    private int ncDiversity;
	    private double ncFrequency;
	    private  long count;
	    private  long diversity;
	    private  double convergence;
	    private  boolean weighted;
	    public static final String header="count\tdiversity\tmeanFrequency\tgeomMeanFrequency\tncDiversity\tncFrequency\tmeanCdr3ntLength\t"
	    		+ "meanInsertSize\tmeanNDNSize\tconvergence";
	
	public BasicStats(Sample sample, boolean weighted) {
		 this.count = sample.count;
			        this.diversity = sample.diversity;
			        this.cloneSize = new DescriptiveStatistics();
			        this.cloneSizeGeom = new DescriptiveStatistics();

			        double cdr3ntLength = 0;
			        double	insertSize = 0;
			        double	ndnSize = 0;

			        this.weighted = weighted;

			        ClonotypeKeyGen keyGenAA = new ClonotypeKeyGen(new AminoAcid());
			        ClonotypeKeyGen   keyGenNT = new ClonotypeKeyGen(new Nucleotide() 
			        	);
			        HashSet aaSet = new HashSet<ClonotypeKey>();
			        HashSet ntSet = new HashSet<ClonotypeKey>();

			        long weight = 1;
			        double denom = 0;

			        for(int i=0;i<sample.ClonotypeCollection.size();i++) {
			            cloneSize.addValue(sample.ClonotypeCollection.get(i).getFreq());
			            cloneSizeGeom.addValue(Math.log10(sample.ClonotypeCollection.get(i).getFreq()));

			            if (weighted)
			                weight = sample.ClonotypeCollection.get(i).count;

			            cdr3ntLength += weight * sample.ClonotypeCollection.get(i).cdr3nt.length();

			            int x = sample.ClonotypeCollection.get(i).getInsertSize();
			            if (x > -1)
			                insertSize += weight * x;

			            x = sample.ClonotypeCollection.get(i).getNDNSize();
			            if (x > -1)
			                ndnSize += weight * x;

			            if (!sample.ClonotypeCollection.get(i).isCoding()) {
			                ncDiversity++;
			                ncFrequency += sample.ClonotypeCollection.get(i).getFreq();
			            }

			            aaSet.add(keyGenAA.generateKey(sample.ClonotypeCollection.get(i)));
			            ntSet.add(keyGenNT.generateKey(sample.ClonotypeCollection.get(i)));

			            denom += weight;
			        }

			        this.convergence = ntSet.size() / (double) aaSet.size();
			        this.cdr3ntLength = cdr3ntLength / denom;
			        this.ndnSize = ndnSize / denom;
			        this.insertSize = insertSize / denom;
			    }
	
/**
 * Gets mean clonotype frequency 
 * @return
 */
public double getMeanFrequency() {
   return cloneSize.getMean();
}

/**
 * Gets geometric mean of clonotype frequency 
 * @return
 */
public double getGeomMeanFrequency() {
    return Math.pow(10, cloneSizeGeom.getMean());

}

/**
 * Gets mean CDR3 nucleotide sequence length. 
 * If {@code weighted = true} the mean will be weighted by clonotype count 
 * @return
 */
public double getMeanCdr3ntLength() {
    return cdr3ntLength;
}

/**
 * Gets mean NDN region size, i.e. mean number of nucleotides between V segment end and J segment start 
 * If {@code weighted = true} the mean will be weighted by clonotype count
 * @return
 */
public double getMeanNDNSize() {
    return ndnSize;
}

/**
 * Gets mean insert size, i.e. mean number of nucleotides in V-D and D-J regions if D segment is determined or
 * V-J regions if not. If {@code weighted = true} the mean will be weighted by clonotype count
 * @return
 */
public double getMeanInsertSize() {
    return insertSize;
}

/**
 * Gets the number of reads in sample 
 * @return
 */
public long getCount() {
    return count;
}

/**
 * Gets the total number of clonotypes in sample
 * @return
 */
public long getDiversity() {
    return diversity;
}

/**
 * Gets the total number of non-coding (containing a stop or frameshift) clonotypes in sample 
 * @return
 */
public int getNcDiversity() {
    return ncDiversity;
}

/**
 * Gets the cumulative frequency of non-coding (containing a stop or frameshift) clonotypes in sample 
 * @return
 */
public double getNcFrequency() {
   return ncFrequency;
}

/**
 * Gets the convergence level of sample, i.e. number of CDR3 nucleotide sequences per CDR3 amino acid sequence 
 * @return
 */
public double getConvergence() {
   return convergence;
}
	
    @Override
    public String toString() {
        return ""+getCount()+"\t"+ getDiversity()+"\t"+
         getMeanFrequency()+"\t"+ getGeomMeanFrequency()+"\t"+
         ncDiversity+"\t"+ ncFrequency+"\t"+
         getMeanCdr3ntLength()+"\t"+ getMeanInsertSize()+"\t"+ getMeanNDNSize()+"\t"+
         convergence;
    	

    }
}
