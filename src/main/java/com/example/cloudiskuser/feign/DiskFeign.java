package com.example.cloudiskuser.feign;

import com.example.cloudiskuser.feign.config.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "disk-service", url = "${disk.init:''}",configuration = FeignConfiguration.class)
public interface DiskFeign {

    @PostMapping("/init/user")
    String initUser(@RequestParam("userId") Long userId);

}