package com.example.cloudiskuser.feign;

import com.example.cloudiskuser.feign.config.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "space-service", url = "${space.create:''}",configuration = FeignConfiguration.class)
public interface SpaceFeign {


    @PostMapping("/space/create")
    String createSpace(@RequestParam("userId") Long userId, @RequestParam("size") Long size);

}