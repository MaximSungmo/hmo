package kr.co.sunnyvale.sunny.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import kr.co.sunnyvale.sunny.domain.Answer;
import kr.co.sunnyvale.sunny.domain.Approval;
import kr.co.sunnyvale.sunny.domain.Content;
import kr.co.sunnyvale.sunny.domain.Feel;
import kr.co.sunnyvale.sunny.domain.Notification;
import kr.co.sunnyvale.sunny.domain.Reply;
import kr.co.sunnyvale.sunny.domain.SiteInactiveUser;
import kr.co.sunnyvale.sunny.domain.SmallGroup;
import kr.co.sunnyvale.sunny.domain.Story;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.dto.NotifyInfoDTO;
import kr.co.sunnyvale.sunny.domain.extend.Page;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;

public interface NotificationService {

	public abstract void feed(Sunny sunny, User feeder, User reader);

	public abstract void friendRequest(Sunny sunny, User followedUser, User followerUser);

	public abstract void feel(Sunny sunny, User activator, Content content,
			Feel feel);

	public abstract void unFeel(User user, Content content,
			Feel alreadyFeel);

	public abstract void reply(Sunny sunny, Reply reply);

	public abstract void update(Notification notification);

	public abstract void delete(long notificationId);

	public abstract Notification find(Long notificationId);

	public abstract Date getNotificationLastRead(User user);

	public abstract Date getMessageLastRead(User user);

	public abstract Number getNotificationUnreadCount(User user);

	public abstract List<Notification> getFollowNotifications(User user,
			Stream stream);

	public abstract List<Notification> getNotifications(User user, Stream stream);

	public abstract List<Notification> getNotificationsAndUpdate(User user,
			Stream stream);

	public abstract Serializable getUpdateDate(Long notiId);

	public void mentionStory(Sunny sunny, Long userId, Story story);

	public abstract void requesetApprove(Sunny sunny,  Approval approval,
			Long ... userIds);

	public abstract Long[] approveApprobatorComplete(Sunny sunny, Approval approval);
	
	public abstract Long[] approveCooperationComplete(Sunny sunny, Approval approval);

	public abstract Long[] approveReject(Sunny sunny, Long rejectorId,
			Approval approval);

	public abstract void makeRead(Sunny sunny, Long userId, Long notificationId);

	public abstract void mentionReply(Sunny sunny, Long userId, Reply reply);

	public abstract Notification geNewOne(User user);

	public abstract void requesetSignup(Sunny sunny, 
			SiteInactiveUser siteInactiveUser, Long[] userIds);

	public abstract NotifyInfoDTO getNotifyInfo(Long userId);

	public abstract void makeReadStoryNoties(Long id, User user);

	public abstract Page<Notification> getNotifications(User user, Integer page, int pageSize);

	public abstract Page<Notification> getNotificationsAndUpdate(User user,
			Integer page, int pageSize);

}