package com.techunseen.agentautomation.notifications

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification

/**
 * Notification access is opt-in in Android Settings.
 * This starter intentionally does not forward notification contents to a server.
 */
class AgentNotificationListener : NotificationListenerService() {
    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        // Add explicit allow-listing, local redaction and user consent before processing.
    }
}
