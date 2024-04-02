package com.jd.eventhall.MainAppBackend.Utils;

import java.util.UUID;

public class GenerateId {
    public static String generateId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
