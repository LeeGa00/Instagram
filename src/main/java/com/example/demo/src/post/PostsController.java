package com.example.demo.src.post;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.post.model.ModifyUserPostReq;
import com.example.demo.src.post.model.PostUserPostReq;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/posts")
public class PostsController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());


    private final PostsDao postsDao;

    private final PostsService postsService;
    private final JwtService jwtService;

    public PostsController(PostsDao postsDao, PostsService postsService, JwtService jwtService) {
        this.postsDao = postsDao;
        this.postsService = postsService;
        this.jwtService = jwtService;
    }

    @ResponseBody
    @PostMapping("/upload")
    public BaseResponse<String> uploadPost(PostUserPostReq postUserPostReq) throws BaseException {
        int userIdxByJwt = jwtService.getUserIdx();
        postsService.uploadPost(userIdxByJwt, postUserPostReq.getContent());
        return new BaseResponse<>("성공적으로 게시물이 업로드되었습니다.");
    }

    @ResponseBody
    @PostMapping("/modify")
    public void modifyPost(ModifyUserPostReq modifyUserPostReq) throws BaseException {
        int userIdxByJwt = jwtService.getUserIdx();
        postsService.modifyPost(userIdxByJwt, modifyUserPostReq);
    }

    @ResponseBody
    @PatchMapping("/delete/{postIdx}")
    public void deletePost(@PathVariable ("postIdx") int postIdx) throws BaseException {
        int userIdxByJwt = jwtService.getUserIdx();
        postsService.deletePost(userIdxByJwt, postIdx);
    }

    @ResponseBody
    @PostMapping("/comment/{postIdx}")
    public void commentPost(@PathVariable ("postIdx") int postIdx) throws BaseException {
        int userIdxByJwt = jwtService.getUserIdx();
    }
}
