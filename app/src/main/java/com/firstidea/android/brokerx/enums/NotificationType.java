package com.firstidea.android.brokerx.enums;

/**
 * Created by Govind on 22-Jan-17.
 */

public enum NotificationType {
    LEAD_CREATED("LC"), LEAD_REVERTED("LR"), LEAD_STATUS_CHANGED("LSC"), DEAL_DONE("DD"), MOVED_TO_PENDING_LEAD("PL"), ANALYSIS_STATUS_CHANGED("ASC"), ANALYSIS_DOCUMENT_UPLOADED("ADU");

    private final String notificationType;

    private NotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getNotificationType() {
        return this.notificationType;
    }
}
