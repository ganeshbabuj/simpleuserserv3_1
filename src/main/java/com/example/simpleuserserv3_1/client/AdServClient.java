package com.example.simpleuserserv3_1.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(url = "${ad.service.url}", name = "adserv-client", path = "/v1/marketing")
public interface AdServClient {

    @PostMapping("/activate")
    public ActivationResponse activate(@RequestBody ActivationRequest activationRequest);

}
