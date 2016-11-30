package com.firstidea.android.brokerx.model;

import java.io.Serializable;

/**
 * Created by user on 10/10/2016.
 */

public class NotificationItem implements Serializable {
    private int id;
    private String CircleText1,CircleText2;
    private String Notification1,Notification2,Notification3,Notification4;
    private String time1,timeCard2;
    private  String notification_card;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCircleText1() {
        return CircleText1;
    }

    public void setCircleText1(String circleText1) {
        CircleText1 = circleText1;
    }

    public String getCircleText2() {
        return CircleText2;
    }

    public void setCircleText2(String circleText2) {
        CircleText2 = circleText2;
    }

    public String getNotification1() {
        return Notification1;
    }

    public void setNotification1(String notification1) {
        Notification1 = notification1;
    }

    public String getNotification2() {
        return Notification2;
    }

    public void setNotification2(String notification2) {
        Notification2 = notification2;
    }

    public String getNotification3() {
        return Notification3;
    }

    public void setNotification3(String notification3) {
        Notification3 = notification3;
    }

    public String getNotification4() {
        return Notification4;
    }

    public void setNotification4(String notification4) {
        Notification4 = notification4;
    }

    public String getTime1() {
        return time1;
    }

    public void setTime1(String time1) {
        this.time1 = time1;
    }

    public String getTimeCard2() {
        return timeCard2;
    }

    public void setTimeCard2(String timeCard2) {
        this.timeCard2 = timeCard2;
    }

    public String getNotification_card() {
        return notification_card;
    }

    public void setNotification_card(String notification_card) {
        this.notification_card = notification_card;
    }
}
