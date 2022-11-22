package com.splendo.kaluga.example.alerts

import com.splendo.kaluga.example.alerts.compose.ComposeAlertsActivity
import com.splendo.kaluga.example.alerts.xml.XMLAlertsActivity
import com.splendo.kaluga.example.compose.ComposeOrXMLActivity

class AlertsActivity : ComposeOrXMLActivity<ComposeAlertsActivity, XMLAlertsActivity>(ComposeAlertsActivity::class.java, XMLAlertsActivity::class.java)
