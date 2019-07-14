package kr.co.sunnyvale.sunny.util;

public class StringEscapeResult {

	public StringEscapeResult(int returnCount, String content,
			int prevCommentLength, int prevStoryLength, int prevCommentOriginalLength, int prevStoryOriginalLength) {
		super();
		this.returnCount = returnCount;
		this.content = content;
		this.prevCommentLength = prevCommentLength;
		this.prevStoryLength = prevStoryLength;
		this.prevCommentOriginalLength = prevCommentOriginalLength;
		this.prevStoryOriginalLength = prevStoryOriginalLength;
	}
	
	private int returnCount;
	private int prevCommentOriginalLength;
	private int prevStoryOriginalLength;
	private String content;
	private int prevCommentLength;
	private int prevStoryLength;
	
	
	public int getReturnCount() {
		return returnCount;
	}
	public void setReturnCount(int returnCount) {
		this.returnCount = returnCount;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getPrevCommentLength() {
		return prevCommentLength;
	}
	public void setPrevCommentLength(int prevCommentLength) {
		this.prevCommentLength = prevCommentLength;
	}
	public int getPrevStoryLength() {
		return prevStoryLength;
	}
	public void setPrevStoryLength(int prevStoryLength) {
		this.prevStoryLength = prevStoryLength;
	}
	public int getPrevCommentOriginalLength() {
		return prevCommentOriginalLength;
	}
	public void setPrevCommentOriginalLength(int prevCommentOriginalLength) {
		this.prevCommentOriginalLength = prevCommentOriginalLength;
	}
	public int getPrevStoryOriginalLength() {
		return prevStoryOriginalLength;
	}
	public void setPrevStoryOriginalLength(int prevStoryOriginalLength) {
		this.prevStoryOriginalLength = prevStoryOriginalLength;
	}
	@Override
	public String toString() {
		return "StringEscapeResult [returnCount=" + returnCount
				+ ", prevCommentOriginalLength=" + prevCommentOriginalLength
				+ ", prevStoryOriginalLength=" + prevStoryOriginalLength
				+ ", content=" + content + ", prevCommentLength="
				+ prevCommentLength + ", prevStoryLength=" + prevStoryLength
				+ "]";
	}

	
}
