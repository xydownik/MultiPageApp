package com.example.multipagelab.services


import android.content.Context
import android.database.Cursor
import android.provider.CalendarContract
import android.util.Log
import com.example.multipagelab.models.CalendarEvent

object CalendarProviderHelper {

    fun getCalendarEvents(context: Context): List<CalendarEvent> {
        val events = mutableListOf<CalendarEvent>()

        val projection = arrayOf(
            CalendarContract.Events._ID,
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DTSTART
        )

        val cursor: Cursor? = context.contentResolver.query(
            CalendarContract.Events.CONTENT_URI,
            projection,
            null, null, "${CalendarContract.Events.DTSTART} ASC"
        )

        cursor?.use {
            while (it.moveToNext()) {
                val id = it.getLong(it.getColumnIndexOrThrow(CalendarContract.Events._ID))
                val title = it.getString(it.getColumnIndexOrThrow(CalendarContract.Events.TITLE))
                val startTime = it.getLong(it.getColumnIndexOrThrow(CalendarContract.Events.DTSTART))

                events.add(CalendarEvent(id, title, startTime))
            }
        } ?: Log.e("CalendarProvider", "Cursor is null")

        return events
    }
}
