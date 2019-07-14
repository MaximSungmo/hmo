package kr.co.sunnyvale.sunny.service.impl;

import kr.co.sunnyvale.sunny.domain.MessageInfo;
import kr.co.sunnyvale.sunny.repository.hibernate.MessageInfoRepository;
import kr.co.sunnyvale.sunny.service.MessageInfoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( value="messageInfoService" )
@Transactional
public class MessageInfoServiceImpl implements MessageInfoService{

	@Autowired
	private MessageInfoRepository channelMessageInfoRepository;
	
	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.MessageInfoService#save(kr.co.sunnyvale.sunny.domain.MessageInfo)
	 */
	@Override
	@Transactional
	public void save(MessageInfo info){
		channelMessageInfoRepository.save(info);
	}
	
	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.MessageInfoService#update(kr.co.sunnyvale.sunny.domain.MessageInfo)
	 */
	@Override
	@Transactional
	public void update(MessageInfo info){
		channelMessageInfoRepository.update(info);
	}
	
	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.MessageInfoService#select(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = true)
	public MessageInfo select(Long infoId){
		return channelMessageInfoRepository.select(infoId);
	}

	@Override
	@Transactional(readOnly = true)
	public MessageInfo getLastFromChannel(Long channelId) {
		return channelMessageInfoRepository.getLastFromChannel(channelId);
		
	}

}

