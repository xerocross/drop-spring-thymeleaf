package com.adamfgcross.springherokudemo.environment;

import java.util.Map;

public class EnvironmentTest {
    public static void main(String[] args) {
        Map<String, String> env = System.getenv();
        System.out.println("ENV : " + env.get("ENV"));
        System.out.println("keys:" + env.keySet());
    }
}
