package phuong.jsp.chatGroup.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import phuong.jsp.chatGroup.configuration.Layout;
import phuong.jsp.chatGroup.entities.*;
import phuong.jsp.chatGroup.repository.*;
import phuong.jsp.chatGroup.service.PostMessagesService;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@AllArgsConstructor
public class PostMessagesController {
    private PostMessagesRepository postMessagesRepository;
    private PostMessagesService postMessagesService;
    private PostCommentRepository postCommentRepository;
    private PostTagRepository postTagRepository;
    private PostVoteRepository postVoteRepository;
    private PostHistoryRepository postHistoryRepository;
    private UserRepository userRepository;

    @Layout(value = "default", title = "Trang chủ")
    @GetMapping("/")
    public String index(
            Model model, Authentication authentication) {
        return "index";
    }

    @RequestMapping(path = {"/getPost"}, method = RequestMethod.POST
            , produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<PostMessages> getPost(@RequestBody Map<String, Object> map, Principal principal) {
        return postMessagesRepository.findAllOrderById (PageRequest.of ((int) map.get ("page"), (int) map.get ("size")));
    }

    @RequestMapping(path = {"/countComment"}, method = RequestMethod.POST
            , produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Integer countComment(@RequestBody Map<String, Object> map) {
        return postCommentRepository.countAllByPostId ((Integer) map.get ("id"));
    }


    @RequestMapping(path = {"/createPost"}, method = RequestMethod.POST
            , produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public PostMessages createPost(@RequestBody PostMessages postMessages, Principal principal) {
        postMessages.setUsername (principal.getName ());
        postMessages.setDateTime (new Date ());
        return postMessagesService.save (postMessages);
    }

    @RequestMapping(path = {"/updatePost"}, method = RequestMethod.POST
            , produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public PostMessages updatePost(@RequestBody Map<String, Object> map, Principal principal) {
        int postId = (int) map.get ("postId");
        if ( !postMessagesRepository.isCreatePost (postId).equals (principal.getName ()) ) {
            return null;
        }
        return postMessagesService.updatePostMessages (map);
    }

    @RequestMapping(path = {"/deletePost"}, method = RequestMethod.POST
            , produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public boolean deletePost(@RequestBody Integer postId, Principal principal) {
        if ( !postMessagesRepository.isCreatePost (postId).equals (principal.getName ()) ) {
            return false;
        }
        postMessagesRepository.delete (postMessagesRepository.findById (postId).get ());
        return false;
    }

    @RequestMapping(path = {"/getHistoryPost"}, method = RequestMethod.POST
            , produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<PostHistory> getHistoryPost(@RequestBody Integer postId) {
        return postHistoryRepository.findAllByPostId (postId);
    }

    @RequestMapping(path = {"/tagIntoPost/{postId}"}, method = RequestMethod.POST
            , produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<PostTag> tagsUserIntoPost(@PathVariable("postId") int postId,
                                          @RequestBody List<String> lst) {
        lst.forEach (s -> postTagRepository.save (new PostTag (s, postId)));
        return postTagRepository.findAllByPostId (postId);
    }

    @RequestMapping(path = {"/removeTagInPost/{tagId}"}, method = RequestMethod.GET
            , produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public boolean removeTag(@PathVariable("tagId") int tagId) {
        postTagRepository.deleteById (tagId);
        return true;
    }

    @RequestMapping(path = {"/votePost/{postId}"}, method = RequestMethod.POST
            , produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<PostVote> votePost(@PathVariable("postId") int postId, @RequestBody Map<String, String> map, Principal principal) {
        String type = map.get ("type");
        if ( postVoteRepository.existsByPostIdAndUsernameAndType (postId, principal.getName (), type) ) {
            postVoteRepository.deleteByPostIdAndUsername (postId, principal.getName ());
        } else {
            postVoteRepository.insertOrUpdate (postId, principal.getName (), type);
        }

        return postVoteRepository.findAllByPostId (postId);
    }


    @RequestMapping(path = {"/getComment"}, method = RequestMethod.POST
            , produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<PostComment> createComment(@RequestBody Map<String, Integer> map) {
        int postId = map.get ("postId");
        int page = map.get ("page");
        int size = map.get ("size");
        int maxId = map.get ("maxId");
        if ( maxId != 0 )
            return postCommentRepository.findAllByPostIdAndIdBeforeOrderByIdDesc (postId, maxId, PageRequest.of (page, size));
        return postCommentRepository.findAllByPostIdOrderByIdDesc (postId, PageRequest.of (page, size));
    }

    @RequestMapping(path = {"/createComment"}, method = RequestMethod.POST
            , produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public PostComment createComment(@RequestBody Map<String, Object> map, Principal principal) {
        int postId = (int) map.get ("postId");
        String content = (String) map.get ("content");
        PostComment postComment = new PostComment (principal.getName (), postId, content, new Date ());
        postCommentRepository.save (postComment);
        return postComment;
    }

    @RequestMapping(path = {"/updateComment"}, method = RequestMethod.POST
            , produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public boolean updateComment(@RequestBody Map<String, Object> map, Principal principal) {
        String content = (String) map.get ("content");
        int commentId = (int) map.get ("commentId");
        PostComment postComment = postCommentRepository.findById (commentId).get ();
        postComment.setContent (content);
        postCommentRepository.save (postComment);
        return true;
    }

    @RequestMapping(path = {"/deleteComment"}, method = RequestMethod.POST
            , produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public boolean deleteComment(@RequestBody Integer commentId, Principal principal) {
        postCommentRepository.deleteById (commentId);
        return true;
    }

    @RequestMapping(path = {"/getUserForTags"}, method = RequestMethod.POST
            , produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<String> getUserForTags(@RequestBody String username) {
        return userRepository.listUserLikeUsernameLimit (username, PageRequest.of (0, 5));
    }

}
