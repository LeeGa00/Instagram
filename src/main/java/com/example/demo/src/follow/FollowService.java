package com.example.demo.src.follow;


import com.example.demo.config.BaseException;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

// Service Create, Update, Delete 의 로직 처리
@Service
public class FollowService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final FollowDao followDao;
    private final JwtService jwtService;

    public FollowService(FollowDao followDao, JwtService jwtService) {
        this.followDao = followDao;
        this.jwtService = jwtService;
    }

    public String FollowUser(int userIdx, int follower) throws BaseException {
        try {
            //비공계 유저인지 확인 필요
            if (isPublicUser(follower)) {
                followDao.newFollowActive(userIdx, follower);
            } else {
                //Inactive 상태로 row 생성
                followDao.newFollowInactive(userIdx, follower);
                //follower 에게 신청 알림 보내기
                //?
                return "팔로우 요청되었습니다.";
            }
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
        return "성공적으로 유저를 팔로우하였습니다.";
    }

    public boolean isPublicUser(int userIdx){
        if (followDao.accountIsPublic(userIdx).equals("ACTIVE"))
            return true;
        else
            return false;
    }
}
