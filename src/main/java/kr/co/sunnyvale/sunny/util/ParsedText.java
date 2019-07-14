package kr.co.sunnyvale.sunny.util;

import java.util.Set;

public class ParsedText {
	
	private String taggedTextPrev;
	
	private String taggedTextNext;
	
	private int returnCount;
	
	private int syllableCount;
	
	private Set<Long> mentionReceivers;

	private Set<String> tagTitles;

	public String getTaggedTextPrev() {
		return taggedTextPrev;
	}

	public void setTaggedTextPrev(String taggedTextPrev) {
		this.taggedTextPrev = taggedTextPrev;
	}

	public String getTaggedTextNext() {
		return taggedTextNext;
	}

	public void setTaggedTextNext(String taggedTextNext) {
		this.taggedTextNext = taggedTextNext;
	}

	public int getReturnCount() {
		return returnCount;
	}

	public void setReturnCount(int returnCount) {
		this.returnCount = returnCount;
	}

	public int getSyllableCount() {
		return syllableCount;
	}

	public void setSyllableCount(int syllableCount) {
		this.syllableCount = syllableCount;
	}

	public Set<Long> getMentionReceivers() {
		return mentionReceivers;
	}

	public void setMentionReceivers(Set<Long> mentionReceivers) {
		this.mentionReceivers = mentionReceivers;
	}

	public Set<String> getTagTitles() {
		return tagTitles;
	}

	public void setTagTitles(Set<String> tagTitles) {
		this.tagTitles = tagTitles;
	}

	@Override
	public String toString() {
		return "ParsedText [taggedTextPrev=" + taggedTextPrev
				+ ", taggedTextNext=" + taggedTextNext + ", returnCount="
				+ returnCount + ", syllableCount=" + syllableCount + "]";
	}
	
	
}
