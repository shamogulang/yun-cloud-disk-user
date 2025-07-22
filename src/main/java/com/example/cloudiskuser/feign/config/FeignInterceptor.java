package com.example.cloudiskuser.feign.config;

import com.example.cloudiskuser.config.ThirdAuthHelper;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * there are 3 autowire  ways
 * 1、implement RequestInterceptor and use annotation @component,this will work for all the feign request
 * 2、use configuration
 * 3、implement RequestInterceptor，then config : @FeignClient(name = "file-service", url = "${file.url:''}",configuration = FeignInterceptor.class)
 */
@Slf4j
public class FeignInterceptor implements RequestInterceptor {
    @Autowired
    private ThirdAuthHelper thirdAuthHelper;
    @Value("${spring.application.name}")
    private String serviceName;

    @Override
    public void apply(RequestTemplate template) {
        Map<String, String> headers = new HashMap<>();
        try {
            String timestamp = String.valueOf(Instant.now().getEpochSecond());
            String signature = thirdAuthHelper.generateSignature(serviceName, timestamp);
            headers.put("X-Service-Name", serviceName);
            headers.put("X-Timestamp", timestamp);
            headers.put("X-Signature", signature);
            headers.forEach(template::header);
        }catch (Exception exception){
            log.error("setting header errors, details {0}", exception);
        }
    }
}
