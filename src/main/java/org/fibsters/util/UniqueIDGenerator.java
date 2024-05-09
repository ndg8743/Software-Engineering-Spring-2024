package org.fibsters.util;

import java.util.UUID;

public class UniqueIDGenerator {

    public static String generateUniqueID() {
        return UUID.randomUUID().toString();
    }

}