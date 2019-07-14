package kr.co.sunnyvale.sunny.service;

import kr.co.sunnyvale.sunny.domain.Approval;
import kr.co.sunnyvale.sunny.domain.Content;
import kr.co.sunnyvale.sunny.domain.Feel;
import kr.co.sunnyvale.sunny.domain.Reply;
import kr.co.sunnyvale.sunny.domain.SiteInactiveUser;
import kr.co.sunnyvale.sunny.domain.SmallGroup;
import kr.co.sunnyvale.sunny.domain.Story;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;

public interface AfterService {

	public void friendRequest(Sunny sunny,User followed, User follower);

	public void feel(Sunny sunny, User user, Content content, Feel feel);

	public void unFeel(User user, Content content, Feel alreadyFeel);

	public void reply(Sunny sunny, Reply reply);

	public void story(Sunny sunny, Story story);

	public void requestApprove(Sunny sunny, Approval approval,
			SmallGroup smallGroup);
	
	public void approveReject(Sunny sunny, Long rejectorId, Approval approval	);
//
	public void approveApprobatorComplete(Sunny sunny, Approval approval);
//
	public void approveCooperationComplete(Sunny sunny, Approval approval);

	public void requestSignup( Sunny sunny, SiteInactiveUser siteInactiveUser);

	public void notice(Sunny sunny, Long storyId, Long userId);
}