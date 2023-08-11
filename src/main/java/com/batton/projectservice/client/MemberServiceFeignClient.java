package com.batton.projectservice.client;

import com.batton.projectservice.dto.client.GetMemberResDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "member-service", url = "http://localhost:8081")
public interface MemberServiceFeignClient {
    @RequestMapping(method = RequestMethod.GET, value = "/members/{memberId}", consumes = "application/json")
    GetMemberResDTO getMember(@PathVariable("memberId") Long memberId);
}
