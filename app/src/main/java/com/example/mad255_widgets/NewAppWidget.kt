package com.example.mad255_widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import kotlin.random.Random

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in [NewAppWidgetConfigureActivity]
 */
const val primaryWidgetId = "primaryWidget"
class NewAppWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        // When the user deletes the widget, delete the preference associated with it.
        for (appWidgetId in appWidgetIds) {
            deleteTitlePref(context, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context, intent: Intent?) {
        if (primaryWidgetId == intent?.action){
            val thisWidgetId = intent.getIntExtra("appWidgetId", 0)
            updateAppWidget(context, AppWidgetManager.getInstance(context), thisWidgetId)
        }
        super.onReceive(context, intent)
    }

    //created companion object for widget.
    companion object {
        internal fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val intent = Intent(context, NewAppWidget::class.java)
            intent.action = primaryWidgetId
            intent.putExtra("appWidgetId", appWidgetId)
            val pendingIntent = PendingIntent.getBroadcast(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
            )
            val number = String.format("%03d", Random.nextInt(900) + 100)
            val remoteViews = RemoteViews(
                context.packageName,
                R.layout.new_app_widget
            )
            remoteViews.setTextViewText(R.id.numDisplay, number)
            Log.i("test", "${number}")

            remoteViews.setOnClickPendingIntent(R.id.actionButton, pendingIntent)

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
        }
    }
}