package com.example.demo.src.auth;
import com.example.demo.config.BaseException;
import com.example.demo.src.auth.model.SignUpReq;
import com.example.demo.src.auth.model.SignUpReqDto;
import com.example.demo.src.auth.model.SignupRes;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.config.BaseResponseStatus.IO_EXCEPTION;

@Service
@Slf4j
public class AuthService {


    private AuthDao authDao;

    public AuthService(AuthDao authDao) {
        this.authDao = authDao;
    }

    public SignupRes kakoSiginUp(SignUpReq signUpReq) throws BaseException {
        try {
            String reqURL = "https://kapi.kakao.com/v2/user/me";
            String accessToken = signUpReq.getAccessToken();
            try {
                log.debug("accessToken: "+accessToken);
                URL url = new URL(reqURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");

                //    요청에 필요한 Header에 포함될 내용
                conn.setRequestProperty("Authorization", "Bearer " + accessToken);

                int responseCode = conn.getResponseCode();
                String msg = conn.getResponseMessage();
                System.out.println("responseCode : " + responseCode);
                System.out.println(" = " + msg);

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));

                String line = "";
                String result = "";

                while ((line = br.readLine()) != null) {
                    result += line;
                }
                System.out.println("response body : " + result);

                JsonParser parser = new JsonParser();
                JsonElement element = parser.parse(result);

                JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
                JsonObject kakao_account = element.getAsJsonObject().get("kakao_account").getAsJsonObject();

                String nickname = properties.getAsJsonObject().get("nickname").getAsString();
                String email = kakao_account.getAsJsonObject().get("email").getAsString();
                SignUpReqDto signUpReqDto = new SignUpReqDto(nickname,email, signUpReq.getRefreshToken());
                if(authDao.existsEmail(email)==1){
                    Integer findEmail = authDao.findUserByEmail(email);
                    authDao.updateRefreshkey(signUpReqDto);
                    return new SignupRes(accessToken);
                }else{
                    authDao.Sinup(signUpReqDto);
                    return new SignupRes(accessToken);
                }
            } catch (IOException e) {
                throw new BaseException(IO_EXCEPTION);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public SignupRes naverSiginUp(SignUpReq signUpReq) throws BaseException {
        try {
            String apiURL = "https://openapi.naver.com/v1/nid/me";
            String accessToken = signUpReq.getAccessToken();
            try {
                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("GET");

                //    요청에 필요한 Header에 포함될 내용
                con.setRequestProperty("Authorization", "Bearer " + accessToken);

                int responseCode = con.getResponseCode();

                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String line = "";
                String result = "";

                while ((line = br.readLine()) != null) {
                    result += line;
                }
                JsonParser parser = new JsonParser();
                JsonElement element = parser.parse(result);

                JsonObject response = element.getAsJsonObject().get("response").getAsJsonObject();

                String nickname = response.getAsJsonObject().get("name").getAsString();
                String email = response.getAsJsonObject().get("email").getAsString();
                SignUpReqDto signUpReqDto = new SignUpReqDto(nickname, email, signUpReq.getRefreshToken());
                if (authDao.existsEmail(email) != null) {
                    authDao.updateRefreshkey(signUpReqDto);
                    return new SignupRes(signUpReq.getAccessToken());
                } else {
                    authDao.Sinup(signUpReqDto);
                    return new SignupRes(signUpReq.getAccessToken());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
