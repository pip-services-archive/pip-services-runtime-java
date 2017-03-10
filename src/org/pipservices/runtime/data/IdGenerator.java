package org.pipservices.runtime.data;

import java.util.*;

public class IdGenerator {
    public static String getShort() {
        return new Long((long)Math.floor(100000000 + Math.random() * 899999999)).toString();
    }

    public static String getUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }	

    public static String uuid() {
        return getUuid();
    }	
}
