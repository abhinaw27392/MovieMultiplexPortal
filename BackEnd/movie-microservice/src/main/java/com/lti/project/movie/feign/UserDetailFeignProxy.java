package com.lti.project.movie.feign;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.lti.project.movie.dto.UserDetailDto;

@FeignClient(name = "user-microservice")
@RibbonClient(name = "user-microservice")
public interface UserDetailFeignProxy {
	
	@GetMapping("/api/user/{userId}")
	public ResponseEntity<UserDetailDto> getUserDetails(@PathVariable("userId") String userId);
}
