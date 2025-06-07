package com.example.backend.utils;

public class CurrentHold {
    private final static ThreadLocal<Integer> CURRENT_LOCAL = new ThreadLocal<>();

    public static void setCurrentUserId(Integer userId) {
        CURRENT_LOCAL.set(userId);
    }
    public static Integer getCurrentUserId() {
        return CURRENT_LOCAL.get();
    }
    public static void remove(){
        CURRENT_LOCAL.remove();
    }
}
