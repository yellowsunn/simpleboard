package com.yellowsunn.userservice.infrastructure.system;

import com.yellowsunn.userservice.utils.UuidHolder;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class SystemUuidHolder implements UuidHolder {

    @Override
    public String random() {
        return UUID.randomUUID().toString();
    }
}
