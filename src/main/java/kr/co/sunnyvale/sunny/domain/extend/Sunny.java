package kr.co.sunnyvale.sunny.domain.extend;

import java.io.Serializable;

import kr.co.sunnyvale.sunny.domain.Site;

import org.springframework.mobile.device.Device;

/**
 * 현재 요청에 대한 상태.
 * 현재 요청에서 사이트를 도출하기도 하고
 * 현재 요청이 잘못된 경로인지 여부도 확인한다.
 * @author Administrator
 *
 */
public class Sunny implements Serializable{

	public static final String SERVICE_ID = "hmo";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2247694122923000067L;

	private Site site;

	private String path;
	
	private String pathNext;
	
	private String serverName;
	
	private int serverPort;
	
	private String serverProtocol;
	
	private String documentDomain; 
	
	private boolean goRedirect = false;
	
	private boolean noExistsSite = false;
	
	private String ipAddress;
	
	private String requestURI;
	
	private String sessionId; 

	private boolean cordova = false; 
	
	private Device device; 
	
	/**
	 * 사이트 정보를 성공적으로 가져왔는지 여부.
	 */
	private boolean successLoadSite = false;
	
	
	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isGoRedirect() {
		return goRedirect;
	}

	public void setGoRedirect(boolean goRedirect) {
		this.goRedirect = goRedirect;
	}

	public boolean isSuccessLoadSite() {
		return successLoadSite;
	}

	public void setSuccessLoadSite(boolean successLoadSite) {
		this.successLoadSite = successLoadSite;
	}

	public String getDocumentDomain() {
		return documentDomain;
	}

	public void setDocumentDomain(String documentDomain) {
		this.documentDomain = documentDomain;
	}

	public String getPathNext() {
		return pathNext;
	}

	public void setPathNext(String pathNext) {
		this.pathNext = pathNext;
	}

	public boolean isNoExistsSite() {
		return noExistsSite;
	}

	public void setNoExistsSite(boolean noExistsSite) {
		this.noExistsSite = noExistsSite;
	}

	public String getRedirectPath(){
		if( pathNext != null ){
			return path + pathNext;
		}
		return path;
	}

	public String getRequestURI() {
		return requestURI;
	}

	public void setRequestURI(String requestURI) {
		this.requestURI = requestURI;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}


	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	
	public String getServerProtocol() {
		return serverProtocol;
	}

	public void setServerProtocol(String serverProtocol) {
		this.serverProtocol = serverProtocol;
	}
	
	public boolean isCordova() {
		return cordova;
	}

	public void setCordova(boolean cordova) {
		this.cordova = cordova;
	}

	public String getServiceId(){
		return SERVICE_ID;
	}
	
	@Override
	public String toString() {
		return "Sunny [site=" + site + ", path=" + path + ", pathNext="
				+ pathNext + ", serverName=" + serverName + ", documentDomain="
				+ documentDomain + ", goRedirect=" + goRedirect
				+ ", noExistsSite=" + noExistsSite + ", ipAddress=" + ipAddress
				+ ", requestURI=" + requestURI + ", sessionId=" + sessionId
				+ ", successLoadSite=" + successLoadSite + "]";
	}



	
}
