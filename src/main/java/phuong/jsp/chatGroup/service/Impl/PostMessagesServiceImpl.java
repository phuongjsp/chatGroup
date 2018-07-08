package phuong.jsp.chatGroup.service.Impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import phuong.jsp.chatGroup.entities.*;
import phuong.jsp.chatGroup.repository.*;
import phuong.jsp.chatGroup.service.PostMessagesService;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class PostMessagesServiceImpl implements PostMessagesService {
    private PostMessagesRepository postMessagesRepository;
    private PostHistoryRepository postHistoryRepository;
    private PostTagRepository postTagRepository;
    private PostVoteRepository postVoteRepository;
    private PostCommentRepository postCommentRepository;
    private PostImagesRepository postImagesRepository;
    private PostFilesRepository postFilesRepository;

    @Override
    public PostMessages updatePostMessages(Map<String, Object> map) {
        int postId = (int) map.get ("postId");
        PostMessages postMessages = postMessagesRepository.findById (postId).get ();
        /* Get history before update*/
        PostHistory postHistory = new PostHistory (postMessages.getTitle (),
                postMessages.getContent (), new Date (), postId);

        if ( map.get ("title") != null )
            if ( !map.get ("title").toString ().equals (postMessages.getTitle ()) )
                postMessages.setTitle (map.get ("title").toString ());
        if ( map.get ("content") != null )
            if ( !map.get ("content").toString ().equals (postMessages.getContent ()) )
                postMessages.setContent (map.get ("content").toString ());
        postHistoryRepository.save (postHistory);
        postMessagesRepository.save (postMessages);
        return postMessages;
    }

    @Override
    public void deletePost(int postId) {
        postVoteRepository.deleteByPostId (postId);
        postCommentRepository.deleteByPostId (postId);
        postHistoryRepository.deleteByPostId (postId);
        postImagesRepository.deleteByPostId (postId);

        postMessagesRepository.deleteById (postId);
    }

    @Override
    public PostMessages save(PostMessages postMessages) {
        List<PostTag> postTags = postMessages.getPostTags ();
        List<PostImages> postImages = postMessages.getPostImages ();
        List<PostFiles> postFiles = postMessages.getPostFiles ();
        postMessages.setPostTags (null);
        postMessages.setPostFiles (null);
        postMessages.setPostImages (null);
        postMessages = postMessagesRepository.save (postMessages);
        if ( postTags != null ) {
            for (PostTag pt : postTags) {
                pt.setPostId (postMessages.getId ());
                postTagRepository.save (pt);

            }

        }


        if ( postImages != null ) {
            for (PostImages pi : postImages) {
                pi.setPostId (postMessages.getId ());
                postImagesRepository.save (pi);
            }

        }

        if ( postFiles != null ) {
            for (PostFiles pf : postFiles) {
                pf.setPostId (postMessages.getId ());
                postFilesRepository.save (pf);
            }

        }

        postMessages.setPostTags (postTags);
        postMessages.setPostImages (postImages);
        postMessages.setPostFiles (postFiles);
        return postMessages;
    }


}
