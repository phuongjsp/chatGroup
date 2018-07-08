package phuong.jsp.chatGroup.service;

import phuong.jsp.chatGroup.entities.PostMessages;

import java.util.Map;

public interface PostMessagesService {
    PostMessages updatePostMessages(Map<String, Object> stringObjectMap);

    void deletePost(int postId);

    PostMessages save(PostMessages postMessages);
}
