package com.lti.project.movie.feign;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.lti.project.movie.dto.MultiplexDto;

@FeignClient(name="multiplex-microservice")
@RibbonClient(name="multiplex-microservice")
public interface MultiplexFeignProxy {
	
	@GetMapping("/api/multiplex/{multiplexId}")
	public ResponseEntity<MultiplexDto> getMultiplexById(@PathVariable("multiplexId") String multiplexId);
	
}
