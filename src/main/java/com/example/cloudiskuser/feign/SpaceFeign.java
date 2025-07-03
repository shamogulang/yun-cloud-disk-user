package com.example.cloudiskuser.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "space-service", url = "http://localhost:8082")
public interface SpaceFeign {


    @PostMapping("/space/create")
    String createSpace(@RequestParam("userId") Long userId, @RequestParam("size") Long size);

}