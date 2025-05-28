package com.truecodes.gateway;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GatewayController {
    @GetMapping("/gateway-ping")
    public String testGateway() {
        System.out.println("Pinged the gateway");
        return "Gateway is alive";
    }
}
