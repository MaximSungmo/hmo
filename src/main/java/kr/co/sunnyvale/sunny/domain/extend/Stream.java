package kr.co.sunnyvale.sunny.domain.extend;

import java.io.Serializable;

public class Stream {

	public static boolean NEXT = true;
	public static boolean PREV = false;
	public static int DEFAULT_SIZE = 100;
	
	String baseColumn;
	Serializable baseData;
	//Boolean top;
	Integer size;
	Boolean greaterThan;
	
	
	public Stream(){		
		this(DEFAULT_SIZE);
	}

	public Stream(int size) {
		setSize(size);
	}

	public Stream( Boolean greaterThan, String baseColumn, Serializable baseData ){
		this();
		//setTop(top);
		setGreaterThan(greaterThan);
		setBaseColumn(baseColumn);
		setBaseData(baseData);
	}

	public Stream(Boolean greaterThan, String baseColumn, Serializable baseData, Integer size){
		this( size );
		setGreaterThan(greaterThan);
		//setTop(top);
		setBaseColumn(baseColumn);
		setBaseData(baseData);
	}
	
	public String getBaseColumn() {
		return baseColumn;
	}

	public void setBaseColumn(String baseColumn) {
		this.baseColumn = baseColumn;
	}

	public Serializable getBaseData() {
		return baseData;
	}

	public void setBaseData(Serializable baseData) {
		this.baseData = baseData;
	}

	public Boolean getGreaterThan() {
		return greaterThan;
	}

	public void setGreaterThan(Boolean greaterThan) {
		this.greaterThan = greaterThan;
	}

//	public Boolean getTop() {
//		return top;
//	}
//
//	public void setTop(Boolean top) {
//		this.top = top;
//	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	@Override
	public String toString() {
		return "Stream [baseColumn=" + baseColumn + ", baseData=" + baseData
				+ ", size=" + size + ", greaterThan=" + greaterThan + "]";
	}


}
