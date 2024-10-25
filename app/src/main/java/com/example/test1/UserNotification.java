package com.example.test1;

import android.content.Context;

public class UserNotification extends BaseNotification {

    private String userName;

    public UserNotification(Context context, String channelId, String userName) {
        super(context, channelId);
        this.userName = userName;
    }

    @Override
    protected NotificationContent getNotificationContent() {
        String title = "Welcome!";
        String message = "Hello, You got New request for appointment";
        return new NotificationContent(title, message);
    }
}
