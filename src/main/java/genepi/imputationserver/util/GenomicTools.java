package genepi.imputationserver.util;

import java.io.IOException;

import genepi.imputationserver.steps.fastqc.SnpStats;
import genepi.imputationserver.steps.fastqc.legend.LegendEntry;
import genepi.imputationserver.steps.vcf.MinimalVariantContext;
import htsjdk.variant.variantcontext.VariantContext;

public class GenomicTools {

	public static boolean isValid(String allele) {
		return allele.toUpperCase().equals("A")
				|| allele.toUpperCase().equals("C")
				|| allele.toUpperCase().equals("G")
				|| allele.toUpperCase().equals("T");
	}

	public static boolean match(VariantContext snp, LegendEntry refEntry) {

		char studyRef = snp.getReference().getBaseString().charAt(0);
		char studyAlt = snp.getAltAlleleWithHighestAlleleCount()
				.getBaseString().charAt(0);
		char legendRef = refEntry.getAlleleA();
		char legendAlt = refEntry.getAlleleB();

		if (studyRef == legendRef && studyAlt == legendAlt) {

			return true;

		}

		return false;
	}
	
	public static boolean match(MinimalVariantContext snp, LegendEntry refEntry) {

		char studyRef = snp.getReferenceAllele().charAt(0);
		char studyAlt = snp.getAlternateAllele().charAt(0);
		char legendRef = refEntry.getAlleleA();
		char legendAlt = refEntry.getAlleleB();

		if (studyRef == legendRef && studyAlt == legendAlt) {

			return true;

		}

		return false;
	}

	public static boolean alleleSwitch(VariantContext snp, LegendEntry refEntry) {

		char studyRef = snp.getReference().getBaseString().charAt(0);
		char studyAlt = snp.getAltAlleleWithHighestAlleleCount()
				.getBaseString().charAt(0);

		char legendRef = refEntry.getAlleleA();
		char legendAlt = refEntry.getAlleleB();

		// all simple cases
		if (studyRef == legendAlt && studyAlt == legendRef) {

			return true;
		}

		return false;

	}
	
	public static boolean alleleSwitch(MinimalVariantContext snp, LegendEntry refEntry) {

		char studyRef = snp.getReferenceAllele().charAt(0);
		char studyAlt = snp.getAlternateAllele().charAt(0);

		char legendRef = refEntry.getAlleleA();
		char legendAlt = refEntry.getAlleleB();

		// all simple cases
		if (studyRef == legendAlt && studyAlt == legendRef) {

			return true;
		}

		return false;

	}

	public static boolean strandFlip(char studyRef, char studyAlt,
			char legendRef, char legendAlt) {

		String studyGenotype = new StringBuilder().append(studyRef)
				.append(studyAlt).toString();

		String referenceGenotype = new StringBuilder().append(legendRef)
				.append(legendAlt).toString();

		if (studyGenotype.equals("AC")) {

			return referenceGenotype.equals("TG");

		} else if (studyGenotype.equals("CA")) {

			return referenceGenotype.equals("GT");

		} else if (studyGenotype.equals("AG")) {

			return referenceGenotype.equals("TC");

		} else if (studyGenotype.equals("GA")) {

			return referenceGenotype.equals("CT");

		} else if (studyGenotype.equals("TG")) {

			return referenceGenotype.equals("AC");

		} else if (studyGenotype.equals("GT")) {

			return referenceGenotype.equals("CA");

		} else if (studyGenotype.equals("CT")) {

			return referenceGenotype.equals("GA");

		} else if (studyGenotype.equals("TC")) {

			return referenceGenotype.equals("AG");

		}

		return false;

	}

	public static boolean complicatedGenotypes(VariantContext snp,
			LegendEntry refEntry) {

		char studyRef = snp.getReference().getBaseString().charAt(0);
		char studyAlt = snp.getAltAlleleWithHighestAlleleCount()
				.getBaseString().charAt(0);
		char legendRef = refEntry.getAlleleA();
		char legendAlt = refEntry.getAlleleB();

		String studyGenotype = new StringBuilder().append(studyRef)
				.append(studyAlt).toString();

		String referenceGenotype = new StringBuilder().append(legendRef)
				.append(legendAlt).toString();

		if ((studyGenotype.equals("AT") || studyGenotype.equals("TA"))
				&& (referenceGenotype.equals("AT") || referenceGenotype
						.equals("TA"))) {

			return true;

		} else if ((studyGenotype.equals("CG") || studyGenotype.equals("GC"))
				&& (referenceGenotype.equals("CG") || referenceGenotype
						.equals("GC"))) {

			return true;

		}
		return false;
	}
	
	public static boolean complicatedGenotypes(MinimalVariantContext snp,
			LegendEntry refEntry) {

		char studyRef = snp.getReferenceAllele().charAt(0);
		char studyAlt = snp.getAlternateAllele().charAt(0);
		char legendRef = refEntry.getAlleleA();
		char legendAlt = refEntry.getAlleleB();

		String studyGenotype = new StringBuilder().append(studyRef)
				.append(studyAlt).toString();

		String referenceGenotype = new StringBuilder().append(legendRef)
				.append(legendAlt).toString();

		if ((studyGenotype.equals("AT") || studyGenotype.equals("TA"))
				&& (referenceGenotype.equals("AT") || referenceGenotype
						.equals("TA"))) {

			return true;

		} else if ((studyGenotype.equals("CG") || studyGenotype.equals("GC"))
				&& (referenceGenotype.equals("CG") || referenceGenotype
						.equals("GC"))) {

			return true;

		}
		return false;
	}

	public static boolean strandFlipAndAlleleSwitch(char studyRef,
			char studyAlt, char legendRef, char legendAlt) {

		String studyGenotype = new StringBuilder().append(studyRef)
				.append(studyAlt).toString();

		String referenceGenotype = new StringBuilder().append(legendRef)
				.append(legendAlt).toString();

		if (studyGenotype.equals("AC")) {

			return referenceGenotype.equals("GT");

		} else if (studyGenotype.equals("CA")) {

			return referenceGenotype.equals("TG");

		} else if (studyGenotype.equals("AG")) {

			return referenceGenotype.equals("CT");

		} else if (studyGenotype.equals("GA")) {

			return referenceGenotype.equals("TC");

		} else if (studyGenotype.equals("TG")) {

			return referenceGenotype.equals("CA");

		} else if (studyGenotype.equals("GT")) {

			return referenceGenotype.equals("AC");

		} else if (studyGenotype.equals("CT")) {

			return referenceGenotype.equals("AG");

		} else if (studyGenotype.equals("TC")) {

			return referenceGenotype.equals("GA");

		}

		return false;

	}

	public static ChiSquareObject chiSquare(VariantContext snp,
			LegendEntry refSnp, boolean strandSwap, int size) {

		// calculate allele frequency

		double chisq = 0;

		int refN = size;
		
		double refA = refSnp.getFrequencyA();
		double refB = refSnp.getFrequencyB();

		int majorAlleleCount;
		int minorAlleleCount;

		if (!strandSwap) {
			majorAlleleCount = snp.getHomRefCount();
			minorAlleleCount = snp.getHomVarCount();

		} else {
			majorAlleleCount = snp.getHomVarCount();
			minorAlleleCount = snp.getHomRefCount();
		}

		int countRef = snp.getHetCount() + majorAlleleCount * 2;
		int countAlt = snp.getHetCount() + minorAlleleCount * 2;

		double p = countRef / (double) (countRef + countAlt);
		double q = countAlt / (double) (countRef + countAlt);
		double studyN = (snp.getNSamples() - snp.getNoCallCount()) * 2;

		double totalQ = q * studyN + refB * refN;
		double expectedQ = totalQ / (studyN + refN) * studyN;
		double deltaQ = q * studyN - expectedQ;

		chisq += (Math.pow(deltaQ, 2) / expectedQ)
				+ (Math.pow(deltaQ, 2) / (totalQ - expectedQ));

		double totalP = p * studyN + refA * refN;
		double expectedP = totalP / (studyN + refN) * studyN;
		double deltaP = p * studyN - expectedP;

		chisq += (Math.pow(deltaP, 2) / expectedP)
				+ (Math.pow(deltaP, 2) / (totalP - expectedP));

		return new ChiSquareObject(chisq, p, q);
	}
	
	
	public static ChiSquareObject chiSquare(MinimalVariantContext snp,
			LegendEntry refSnp, boolean strandSwap, int size) {

		// calculate allele frequency

		double chisq = 0;

		int refN = size;
		
		double refA = refSnp.getFrequencyA();
		double refB = refSnp.getFrequencyB();

		int majorAlleleCount;
		int minorAlleleCount;

		if (!strandSwap) {
			majorAlleleCount = snp.getHomRefCount();
			minorAlleleCount = snp.getHomVarCount();

		} else {
			majorAlleleCount = snp.getHomVarCount();
			minorAlleleCount = snp.getHomRefCount();
		}

		int countRef = snp.getHetCount() + majorAlleleCount * 2;
		int countAlt = snp.getHetCount() + minorAlleleCount * 2;

		double p = countRef / (double) (countRef + countAlt);
		double q = countAlt / (double) (countRef + countAlt);
		double studyN = (snp.getNSamples() - snp.getNoCallCount()) * 2;

		double totalQ = q * studyN + refB * refN;
		double expectedQ = totalQ / (studyN + refN) * studyN;
		double deltaQ = q * studyN - expectedQ;

		chisq += (Math.pow(deltaQ, 2) / expectedQ)
				+ (Math.pow(deltaQ, 2) / (totalQ - expectedQ));

		double totalP = p * studyN + refA * refN;
		double expectedP = totalP / (studyN + refN) * studyN;
		double deltaP = p * studyN - expectedP;

		chisq += (Math.pow(deltaP, 2) / expectedP)
				+ (Math.pow(deltaP, 2) / (totalP - expectedP));

		return new ChiSquareObject(chisq, p, q);
	}

	public static int getPanelSize(String panelId) {
		switch (panelId) {
		case "phase1":
			return 1092;
		case "phase3":
			return 2535;
		case "hrc":
			return 32611;
		case "hrc.r1.1.2016":
			return 32470;
		case "hapmap2":
			return 1301;
		case "caapa":
			return 883;
		case "TOPMedfreeze65k":
			return 62784;
		default:
			return 2535;
		}
	}

	public static int getPopSize(String pop) {

		switch (pop) {
		case "eur":
			return 11418;
		case "afr":
			return 17469;
		case "asn":
		case "sas":
		case "eas":
			return 14269;
		default:
			return 15000;
		}
	}

	public static boolean alleleMismatch(char studyRef, char studyAlt,
			char referenceRef, char referenceAlt) {

		return studyRef != referenceRef || studyAlt != referenceAlt;

	}
	
	public static SnpStats calculateAlleleFreq(VariantContext snp, LegendEntry refSnp, boolean strandSwap, int size)
			throws IOException, InterruptedException {

		// calculate allele frequency
		SnpStats output = new SnpStats();

		int position = snp.getStart();

		ChiSquareObject chiObj = GenomicTools.chiSquare(snp, refSnp, strandSwap, size);

		char majorAllele;
		char minorAllele;

		if (!strandSwap) {
			majorAllele = snp.getReference().getBaseString().charAt(0);
			minorAllele = snp.getAltAlleleWithHighestAlleleCount().getBaseString().charAt(0);

		} else {
			majorAllele = snp.getAltAlleleWithHighestAlleleCount().getBaseString().charAt(0);
			minorAllele = snp.getReference().getBaseString().charAt(0);
		}

		output.setType("SNP");
		output.setPosition(position);
		output.setChromosome(snp.getContig());
		output.setRefFrequencyA(refSnp.getFrequencyA());
		output.setRefFrequencyB(refSnp.getFrequencyB());
		output.setFrequencyA((float) chiObj.getP());
		output.setFrequencyB((float) chiObj.getQ());
		output.setChisq(chiObj.getChisq());
		output.setAlleleA(majorAllele);
		output.setAlleleB(minorAllele);
		output.setRefAlleleA(refSnp.getAlleleA());
		output.setRefAlleleB(refSnp.getAlleleB());
		output.setOverlapWithReference(true);

		return output;
	}
	
	public static SnpStats calculateAlleleFreq(MinimalVariantContext snp, LegendEntry refSnp, boolean strandSwap, int size)
			throws IOException, InterruptedException {

		// calculate allele frequency
		SnpStats output = new SnpStats();

		int position = snp.getStart();

		ChiSquareObject chiObj = GenomicTools.chiSquare(snp, refSnp, strandSwap, size);

		char majorAllele;
		char minorAllele;

		if (!strandSwap) {
			majorAllele = snp.getReferenceAllele().charAt(0);
			minorAllele = snp.getAlternateAllele().charAt(0);

		} else {
			majorAllele = snp.getAlternateAllele().charAt(0);
			minorAllele = snp.getReferenceAllele().charAt(0);
		}

		output.setType("SNP");
		output.setPosition(position);
		output.setChromosome(snp.getContig());
		output.setRefFrequencyA(refSnp.getFrequencyA());
		output.setRefFrequencyB(refSnp.getFrequencyB());
		output.setFrequencyA((float) chiObj.getP());
		output.setFrequencyB((float) chiObj.getQ());
		output.setChisq(chiObj.getChisq());
		output.setAlleleA(majorAllele);
		output.setAlleleB(minorAllele);
		output.setRefAlleleA(refSnp.getAlleleA());
		output.setRefAlleleB(refSnp.getAlleleB());
		output.setOverlapWithReference(true);

		return output;
	}

}
