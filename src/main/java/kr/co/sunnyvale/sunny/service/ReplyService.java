package kr.co.sunnyvale.sunny.service;

import java.io.Serializable;
import java.util.List;

import kr.co.sunnyvale.sunny.domain.Content;
import kr.co.sunnyvale.sunny.domain.Reply;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.domain.post.ReplyPost;

public interface ReplyService {

	public void save(Sunny sunny, Reply reply);

	public Reply select(Long replyId);

	public List<Reply> getReplyList(User user, Long contentId, Stream stream);

	public Serializable getCreateDate(Long replyId);

	public void delete(Reply reply);

	public List<Reply> getRecentReceiversReplys(Content content, Reply reply,
			int receiverCount);


	public Reply save(Sunny sunny, ReplyPost replyPost);

}