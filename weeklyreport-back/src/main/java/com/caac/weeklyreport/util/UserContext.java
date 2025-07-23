package com.caac.weeklyreport.util;

import com.caac.weeklyreport.biz.user.entity.UserInfo;

public class UserContext {
    private static final ThreadLocal<UserInfo> currentUser = new ThreadLocal<>();

    public static void setCurrentUser(UserInfo userInfo) {
        currentUser.set(userInfo);
    }

    public static UserInfo getCurrentUser() {
        return currentUser.get();
    }

    public static void clear() {
        currentUser.remove();
    }
}
