package kr.co.sunnyvale.sunny.service;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import kr.co.sunnyvale.sunny.domain.Channel;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.dto.ChannelInsideDTO;
import kr.co.sunnyvale.sunny.domain.dto.ChannelListDTO;
import kr.co.sunnyvale.sunny.domain.dto.NotifyInfoDTO;
import kr.co.sunnyvale.sunny.domain.extend.Page;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;

public interface ChannelService {

	public Channel createChannel(Sunny sunny, User user, Set<Long> userIds);

	public void save(Channel channel);

	public void update(Channel channel);

	public Channel find(Long channelId);

	public List<Channel> getChannels(User user, Stream stream);

	public Page<Channel> getChannels(User user, Integer pageNum, int pageSize);

	public Channel getChannel(Long id);

	public List<User> getJoinUsers(Long channelId);

	public Number getChannelUnreadCount(User user);

	public Serializable getUpdateDate(Long channelId);

	public List<Channel> getChannelsAndUpdate(User user, Stream stream);

	public void inviteUsers(Sunny sunny, Long channelId, User user,
										Set<Long> userIds);

	public void minusUserCount(Long channelId);

	public void plusUserCount(Long channelId);

	public ChannelInsideDTO info(Long channelId, User user);

	public Channel getNotifyNewOne(Sunny sunny, User user);

	public NotifyInfoDTO getNotifyInfo(Sunny sunny, Long userId);

	public ChannelListDTO listInfo(Long channelId, User user);

}