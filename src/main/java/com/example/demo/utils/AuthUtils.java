package com.example.demo.utils;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.auth.AuthDao;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.demo.config.BaseResponseStatus.*;

@Slf4j
@Service
public class AuthUtils {

    private final AuthDao authDao;

    @Autowired
    public AuthUtils(AuthDao authDao) {
        this.authDao = authDao;
    }

    /**
    * kauth.kakao.com/oauth/authorize?client_id=57b970a2a50db63a35052b86da6d2cd3&redirect_uri=http://localhost:9000/auth/kakao&response_type=code
    **/

    public String getAccessToken(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader("ACCESS-TOKEN");
    }
    public String getSite(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader("AUTH-SITE");
    }

    public Integer getUserIdxKakao() throws BaseException {
        String accessToken = getAccessToken();
        if (accessToken == null || accessToken.length() == 0) {
            throw new BaseException(EMPTY_ACCESS_KEY);
        }
        String reqURL = "https://kapi.kakao.com/v2/user/me";
        try {
            System.out.println("accessToken = " + accessToken);
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            //요청에 필요한 Header에 포함될 내용
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            int responseCode = conn.getResponseCode();

            System.out.println("responseCode = " + responseCode);
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));

            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            JsonObject kakao_account = element.getAsJsonObject().get("kakao_account").getAsJsonObject();

            String email = kakao_account.getAsJsonObject().get("email").getAsString();

            Integer userIdx = authDao.findUserByEmail(email);
            if(userIdx ==null){
                throw new BaseException(BaseResponseStatus.EMAIL_NOT_EXIST);
            }else{
                return userIdx;
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new BaseException(IO_EXCEPTION);
        }
    }
}