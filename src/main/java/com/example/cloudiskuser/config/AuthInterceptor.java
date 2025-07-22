package com.example.cloudiskuser.config;

import com.example.cloudiskuser.model.AuthRequest;
import com.example.cloudiskuser.model.AuthResponse;
import com.example.cloudiskuser.service.UserService;
import com.example.cloudiskuser.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private AuthProperties authProperties;
    @Autowired
    private ThirdAuthHelper helper;
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            String serviceName = request.getHeader("X-Service-Name");
            String timestamp = request.getHeader("X-Timestamp");
            String signature = request.getHeader("X-Signature");
            if(StringUtils.hasText(serviceName) && StringUtils.hasText(timestamp)  && StringUtils.hasText(signature) ){
                boolean verify = helper.verify(serviceName, timestamp, signature);
                if(!verify){
                    thirdAuthWriteAuthFail(response);
                    return false;
                }
                return true;
            }
            writeAuthFail(response);
            return false;
        }
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        try {
            Claims claims = jwtUtil.getClaimsFromToken(token);
            request.setAttribute("userId", Long.valueOf(claims.getSubject()));
            return true;
        }catch (Exception exception){
            writeAuthFail(response);
            return false;
        }
    }

    private void writeAuthFail(HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write("{\"code\":100000,\"msg\":\"未授权或token无效\",\"data\":null}");
        writer.flush();
        writer.close();
    }

    private void thirdAuthWriteAuthFail(HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write("{\"code\":100001,\"msg\":\"三方鉴权失败\",\"data\":null}");
        writer.flush();
        writer.close();
    }
} 