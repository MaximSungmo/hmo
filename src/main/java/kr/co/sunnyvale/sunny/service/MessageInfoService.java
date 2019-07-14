package kr.co.sunnyvale.sunny.service;

import kr.co.sunnyvale.sunny.domain.MessageInfo;

import org.springframework.transaction.annotation.Transactional;

public interface MessageInfoService {

	public void save(MessageInfo info);

	public void update(MessageInfo info);

	public MessageInfo select(Long infoId);

	public MessageInfo getLastFromChannel(Long channelId);

}