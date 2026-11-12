package com.techunseen.agentautomation.automation

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

/**
 * Deliberately minimal service. Add narrowly-scoped, user-approved actions.
 * Never use this service to bypass authentication, CAPTCHAs, payment controls,
 * app security, or another person's consent.
 */
class AgentAccessibilityService : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Observe only the event types declared in accessibility_service_config.xml.
        // Do not collect or transmit screen text by default.
    }
    override fun onInterrupt() = Unit

    fun findVisibleText(label: String): AccessibilityNodeInfo? {
        return rootInActiveWindow?.findAccessibilityNodeInfosByText(label)?.firstOrNull()
    }
}
