package com.example.cloudiskuser.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "disk-service", url = "${disk.init:''}")
public interface DiskFeign {

    @PostMapping("/init/user")
    String initUser(@RequestParam("userId") Long userId);

}