package com.yellowsunn.userservice.infrastructure.system;

import com.yellowsunn.userservice.application.port.out.UuidHolder;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class SystemUuidHolder implements UuidHolder {

    @Override
    public String random() {
        return UUID.randomUUID().toString();
    }
}
