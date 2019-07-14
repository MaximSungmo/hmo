package kr.co.sunnyvale.sunny.domain.extend;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class Page<E> 
{

	public static final int DEFAULT_PAGE_SIZE = 10;

	public static final int DEFAULT_SEARCH_SIZE = 6;
	
	public static final int DEFAULT_CHANNEL_SIZE = 15;
	
	private List<E> contents;
	
	private int pageNumber;
	
	private int pageSize;
	
	private long totalCountOfElements;
	
	private int startPageNum;
	
	private int lastPageNum;

	private int endPageNum;
	

	public Page(List<E> contents)
	{
		this.contents = contents;
	}
	public Page(List<E> contents, long totalCountOfElements)
	{
		this.contents = contents;
		this.pageNumber = 1;
		this.pageSize = 10;
	}
	
	public Page(List<E> contents, int pageNumber, int pageSize, long totalCountOfElements){
		this.contents = contents;
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
		this.totalCountOfElements = totalCountOfElements;
		this.calcStartPageNum();
		this.setLastPageNum(getTotalPages());
		this.calcEndPageNum();
	}
	
	public void setUniqueList(){
		LinkedHashSet<E> set = new LinkedHashSet<E>();
		set.addAll(contents);
		totalCountOfElements = set.size();
		contents = new ArrayList<E>(set);
	}
	
	public List<E> getContents() {
		return contents;
	}
	
	public int getPageNumber() {
		return pageNumber;
	}
	
	public int getPageSize() {
		return pageSize;
	}
	
	public long getTotalCountOfElements() {
		return totalCountOfElements;
	}
	
	public int getTotalPages() {
	    if (getTotalCountOfElements() == 0) {
	      return 1;
	    }
	 
	    if (getPageSize() == 0) {
	      return 1;
	    }
	 
	    int totalPages = (int) (getTotalCountOfElements() / getPageSize());
	    if (getTotalCountOfElements() % getPageSize() > 0) {
	      totalPages++;
	    }
	 
	    return totalPages;
	}
	public boolean hasNextPage() {
		return (getPageNumber() < getTotalPages());
	}

	public boolean hasPreviousPage() {
		return (getPageNumber() > 1);
	}
 
	public boolean isFirstPage() {
		return (getPageNumber() == 1);
	}
	public boolean isLastPage() {
		return (getPageNumber() == getTotalPages());
	}
	
	public int getStartPageNum(){
		return this.startPageNum;
	}
	
	public void calcStartPageNum(){
		int startPageNum = 1;
		int currentPageNum = getPageNumber();
		if ( currentPageNum > 10 ) { 
			startPageNum = (currentPageNum / 10) * 10;
		}
		if( startPageNum == 0 ) 
			startPageNum = 1;
		this.startPageNum = startPageNum;
	}

	private void calcEndPageNum() {
		this.endPageNum = this.startPageNum + 9;
		if( this.endPageNum > this.lastPageNum ){
			//this.endPageNum = this.lastPageNum - this.startPageNum;
			this.endPageNum = this.lastPageNum;
		}
		
	}
	
	public int getEndPageNum() {
		return endPageNum;
	}
	public int getLastPageNum() {
		return lastPageNum;
	}
	public void setLastPageNum(int lastPageNum) {
		this.lastPageNum = lastPageNum;
	}
	
	public String toString(){
		
		String retString = "StartNum : " + getStartPageNum() +
				"LastNum : " + getLastPageNum();

		return retString ;

	}
}