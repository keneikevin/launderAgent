package com.example.launder.data.other

import androidx.compose.ui.graphics.Color

object Constants {

    const val RUNNING_DATABASE_NAME = "running_db"

    const val REQUEST_CODE_LOCATION_PERMISSION = 0

    const val ACTION_START_OR_RESUME_SERVICE = "ACTION_START_OR_RESUME_SERVICE"
    const val ACTION_PAUSE_SERVICE = "ACTION_PAUSE_SERVICE"
    const val ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE"
    const val ACTION_SHOW_TRACKING_FRAGMENT = "ACTION_SHOW_TRACKING_FRAGMENT"

    const val LOCATION_UPDATE_INTERVAL = 5000L
    const val FASTEST_LOCATION_INTERVAL = 2000L
    const val SERVICE_COLLECTION = "services"
    const val USERS_COLLECTION = "users"
     val POLYLINE_COLOR = Color.Black
    const val POLYLINE_WIDTH = 8f
    const val MAP_ZOOM = 15f
    const val DEFAULT_PROFILE_PICTURE = "https://firebasestorage.googleapis.com/v0/b/kenyaeducationfund-dd541.appspot.com/o/img_avatar.png?alt=media&token=45c42fd6-53f4-4f16-a433-eb7203d34f3b"
    const val MIN_USER_NAME = 3
    const val MAX_USER_NAME = 8
    const val NOTIFICATION_CHANNEL_ID = "tracking_channel"
    const val NOTIFICATION_CHANNEL_NAME = "Tracking"
    const val NOTIFICATION_ID = 1
}