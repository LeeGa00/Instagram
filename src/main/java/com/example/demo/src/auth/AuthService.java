package com.example.demo.src.auth;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
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

                // 요청에 필요한 Header에 포함될 내용
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
                String birthday = kakao_account.getAsJsonObject().get("birthday").getAsString();

                SignUpReqDto signUpReqDto = new SignUpReqDto(nickname, email, birthday, signUpReq.getRefreshToken());

                if(authDao.existsEmail(email)==1){
                    Integer findEmail = authDao.findUserByEmail(email);
                    authDao.updateRefreshToken(signUpReqDto);
                    return new SignupRes(accessToken);
                }else{
                    authDao.SinUp(signUpReqDto);
                    return new SignupRes(accessToken);
                }
            } catch (IOException e) {
                throw new BaseException(IO_EXCEPTION);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public SignUpReq getKakaoAccessToken(String code) throws BaseException {
        String access_Token = "";
        String refresh_Token = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=9d43614281f594c0c1fcca4d5784d58d");
            sb.append("&redirect_uri=http://localhost:9000/auth/kakao");
            sb.append("&code=" + code);
            bw.write(sb.toString());
            bw.flush();
            int responseCode = conn.getResponseCode();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";

            String result;
            for(result = ""; (line = br.readLine()) != null; result = result + line) {
            }

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);
            access_Token = element.getAsJsonObject().get("access_token").getAsString();
            refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();
            System.out.println("access_Token = " + access_Token);
            System.out.println("refresh_Token = " + refresh_Token);
            br.close();
            bw.close();
            return new SignUpReq(access_Token, refresh_Token);
        } catch (IOException var15) {
            System.out.println("var15 = " + var15);
            throw new BaseException(BaseResponseStatus.IO_EXCEPTION);
        }
    }
}
