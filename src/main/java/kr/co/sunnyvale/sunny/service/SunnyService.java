package kr.co.sunnyvale.sunny.service;

import kr.co.sunnyvale.sunny.domain.extend.Sunny;

public interface SunnyService {
	
	/**
	 * 현재 Request 에 대해 정보들을 분석한다. 
	 * 
	 * @param shouldExistsSite
	 * 반드시 Site 정보가 존재해야되는 경로인지 확인한다.
	 * 만약 사이트 정보가 존재하지 않으면 Exception 을 발생시킨다.
	 * @return
	 */
	public Sunny parseCurrent(boolean shouldExistsSite);
	
}
