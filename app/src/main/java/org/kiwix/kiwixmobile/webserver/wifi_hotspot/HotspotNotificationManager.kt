/*
 * Kiwix Android
 * Copyright (c) 2019 Kiwix <android.kiwix.org>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.kiwix.kiwixmobile.webserver.wifi_hotspot

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import org.kiwix.kiwixmobile.R
import org.kiwix.kiwixmobile.core.utils.HOTSPOT_SERVICE_CHANNEL_ID
import org.kiwix.kiwixmobile.main.KiwixMainActivity
import javax.inject.Inject

class HotspotNotificationManager @Inject constructor(
  private val notificationManager: NotificationManager,
  private val context: Context
) {

  private fun hotspotNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      notificationManager.createNotificationChannel(NotificationChannel(
        HOTSPOT_SERVICE_CHANNEL_ID,
        context.getString(R.string.hotspot_service_channel_name),
        NotificationManager.IMPORTANCE_DEFAULT
      ).apply {
        description = context.getString(R.string.hotspot_channel_description)
        setSound(null, null)
      })
    }
  }

  fun buildForegroundNotification(): Notification {
    val contentIntent = NavDeepLinkBuilder(context).setComponentName(
      KiwixMainActivity::class.java
    )
      .setGraph(R.navigation.kiwix_nav_graph)
      .setDestination(R.id.zimHostFragment)
      .createPendingIntent()
    hotspotNotificationChannel()
    val stopIntent = Intent(context, HotspotService::class.java).setAction(
      HotspotService.ACTION_STOP_SERVER
    )
    val stopHotspot =
      PendingIntent.getService(context, 0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT)
    return NotificationCompat.Builder(context)
      .setContentTitle(context.getString(R.string.hotspot_notification_content_title))
      .setContentText(context.getString(R.string.hotspot_running))
      .setContentIntent(contentIntent)
      .setSmallIcon(R.mipmap.ic_launcher)
      .setWhen(System.currentTimeMillis())
      .addAction(
        R.drawable.ic_close_white_24dp,
        context.getString(R.string.stop),
        stopHotspot
      )
      .setChannelId(HOTSPOT_SERVICE_CHANNEL_ID)
      .build()
  }

  fun dismissNotification() {
    notificationManager.cancel(HOTSPOT_NOTIFICATION_ID)
  }

  companion object {
    const val HOTSPOT_NOTIFICATION_ID = 666
  }
}
