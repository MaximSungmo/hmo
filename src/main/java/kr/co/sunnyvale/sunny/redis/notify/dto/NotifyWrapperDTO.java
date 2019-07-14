package kr.co.sunnyvale.sunny.redis.notify.dto;


/**
 * 레디스로 실제 Publish 하는 데이터임. Data 를 Wrapper 함.
 * @author mook
 *
 */
public class NotifyWrapperDTO {

	private NotifyWrapperDTO(){};
	
	private String serviceId;
	
	private Long senderId;
	
	private Long channelId;
	
	private Integer type;
	
	private Long[] userIds;
	
	private NotifyDataDTO data;

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public Long getSenderId() {
		return senderId;
	}

	public void setSenderId(Long senderId) {
		this.senderId = senderId;
	}

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long[] getUserIds() {
		return userIds;
	}

	public void setUserIds(Long[] userIds) {
		this.userIds = userIds;
	}

	public NotifyDataDTO getData() {
		return data;
	}

	public void setData(NotifyDataDTO data) {
		this.data = data;
	}

	public static NotifyWrapperDTO makeMessageDTo(String serviceId,
			Long[] userIds, Long channelId, Integer type, Long senderId,
			Long readerId, Long lastReadMessageInfoId) {
		
		NotifyWrapperDTO dto = new NotifyWrapperDTO();
		dto.setServiceId(serviceId);
		dto.setUserIds(userIds);
		dto.setChannelId(channelId);
		dto.setType(type);
		dto.setSenderId(senderId);
		
		NotifyDataDTO dataDto = new NotifyDataDTO();
		dataDto.setUid(senderId);
		dataDto.setCid(channelId);
		dataDto.setMid(lastReadMessageInfoId);
		dataDto.setRuid(readerId);
		dataDto.setT(type);
		
		dto.setData(dataDto);
		return dto;
	}

	public static NotifyWrapperDTO makeNoticeDTo(String serviceId, Long[] userIds, Long senderId, Long storyId) {
		
		NotifyWrapperDTO dto = new NotifyWrapperDTO();
		dto.setServiceId(serviceId);
		dto.setUserIds(userIds);
		dto.setSenderId(senderId);
		
		NotifyDataDTO dataDto = new NotifyDataDTO();
		dataDto.setUid(senderId);
		dataDto.setSid(storyId);
		
		dto.setData(dataDto);
		return dto;
	}

	public static NotifyWrapperDTO makeNotiDTo(String serviceId, Long[] userIds, Long senderId, Long storyId, Integer type) {
		
		NotifyWrapperDTO dto = new NotifyWrapperDTO();
		dto.setServiceId(serviceId);
		dto.setUserIds(userIds);
		dto.setType(type);
		dto.setSenderId(senderId);
		
		NotifyDataDTO dataDto = new NotifyDataDTO();
		dataDto.setUid(senderId);
		dataDto.setSid(storyId);
		dataDto.setT(type);
		
		dto.setData(dataDto);
		return dto;
		
	}
	
	
	
	
}
