package com.tomgibara.crinch.record;

import java.math.BigDecimal;
import java.util.Arrays;

import com.tomgibara.crinch.coding.CodedReader;
import com.tomgibara.crinch.coding.CodedWriter;

public class ColumnStats {

	public enum Classification {
		ENUMERATED,
		TEXTUAL,
		INTEGRAL,
		FLOATING
	}

	private static int writeDecimal(CodedWriter writer, BigDecimal value) {
		int c = writer.getWriter().writeBoolean(value == null);
		if (value != null) c+= writer.writeDecimal(value);
		return c;
	}
	
	private static BigDecimal readDecimal(CodedReader reader) {
		return reader.getReader().readBoolean() ? null : reader.readDecimal();
	}
	
	private static int writeString(CodedWriter writer, String str) {
		int length = str.length();
		int c = writer.writePositiveInt(length + 1);
		for (int i = 0; i < length; i++) {
			c += writer.writePositiveInt(str.charAt(i) + 1);
		}
		return c;
	}
	
	private static String readString(CodedReader reader) {
		int length = reader.readPositiveInt() - 1;
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			sb.append((char) (reader.readPositiveInt() - 1));
		}
		return sb.toString();
	}
	
	//TODO not entirely correct: huffman implementation may vary and produce different codes
	public static int write(CodedWriter writer, ColumnStats stats) {
		int c = writer.writePositiveInt(stats.classification.ordinal() + 1);
		c += writer.getWriter().writeBoolean(stats.nullable);
		c += writeDecimal(writer, stats.maximum);
		c += writeDecimal(writer, stats.minimum);
		c += writeDecimal(writer, stats.sum);
		c += writer.writePositiveLong(stats.count + 1);
		final long[] freqs = stats.frequencies;
		int freqCount = freqs == null ? 0 : freqs.length;
		c += writer.writePositiveInt(freqCount + 1);
		for (int i = 0; i < freqCount; i++) {
			c += writer.writePositiveLong(freqs[i] + 1);
		}
		final String[] enums = stats.enumeration;
		int enumCount = enums == null ? 0 : enums.length;
		c += writer.writePositiveInt(enumCount + 1);
		for (int i = 0; i < enumCount; i++) {
			c += writeString(writer, enums[i]);
		}
		return c;
	}
	
	public static ColumnStats read(CodedReader reader) {
		ColumnStats stats = new ColumnStats();
		stats.setClassification(ColumnStats.Classification.values()[reader.readPositiveInt() - 1]);
		stats.setNullable(reader.getReader().readBoolean());
		stats.setMaximum(readDecimal(reader));
		stats.setMinimum(readDecimal(reader));
		stats.setSum(readDecimal(reader));
		stats.setCount(reader.readPositiveLong() - 1);
		
		int freqCount = reader.readPositiveInt() - 1;
		long[] freqs;
		if (freqCount == 0) {
			freqs = null;
		} else {
			freqs = new long[freqCount];
			for (int i = 0; i < freqCount; i++) {
				freqs[i] = reader.readPositiveLong() - 1;
			}
		}
		stats.setFrequencies(freqs);
		
		int enumCount = reader.readPositiveInt() - 1;
		String[] enums;
		if (enumCount == 0) {
			enums = null;
		} else {
			enums = new String[enumCount];
			for (int i = 0; i < enumCount; i++) {
				enums[i] = readString(reader);
			}
		}
		stats.setEnumeration(enums);
		
		return stats;
	}
	

	private Classification classification;
	private boolean nullable;
	private BigDecimal minimum;
	private BigDecimal maximum;
	private BigDecimal sum;
	private long count;
	private long[] frequencies;
	private String[] enumeration;

	public void setClassification(Classification classification) {
		this.classification = classification;
	}
	
	public Classification getClassification() {
		return classification;
	}
	
	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}
	
	public boolean isNullable() {
		return nullable;
	}
	
	public void setMinimum(BigDecimal minimum) {
		this.minimum = minimum;
	}
	
	public BigDecimal getMinimum() {
		return minimum;
	}
	
	public void setMaximum(BigDecimal maximum) {
		this.maximum = maximum;
	}
	
	public BigDecimal getMaximum() {
		return maximum;
	}
	
	public void setSum(BigDecimal sum) {
		this.sum = sum;
	}
	
	public BigDecimal getSum() {
		return sum;
	}
	
	public void setCount(long count) {
		this.count = count;
	}
	
	public long getCount() {
		return count;
	}
	
	public void setFrequencies(long[] frequencies) {
		if (frequencies != null && frequencies.length == 0) frequencies = null;
		this.frequencies = frequencies;
	}

	public long[] getFrequencies() {
		return frequencies;
	}
	
	public void setEnumeration(String[] enumeration) {
		if (enumeration != null && enumeration.length == 0) enumeration = null;
		this.enumeration = enumeration;
	}
	
	public String[] getEnumeration() {
		return enumeration;
	}
	
	@Override
	public String toString() {
		return "classification: " + classification + ", nullable? " + nullable + ", minimum: " + minimum + ", maximum: " + maximum + " sum: " + sum + ", count: " + count + ", frequencies: " + Arrays.toString(frequencies) + ", enumeration: " + Arrays.toString(enumeration);
	}
	
}