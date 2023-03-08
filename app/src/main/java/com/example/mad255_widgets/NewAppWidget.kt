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
class NewAppWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {

        Log.i("test", "Reached")
        var count = appWidgetIds.count()
        for (i in 0 until count){
            val widgetId = appWidgetIds[i]
            val number = String.format("%03d", Random.nextInt(900) + 100)
            val remoteViews = RemoteViews(
                context.packageName,
                R.layout.new_app_widget
            )
            remoteViews.setTextViewText(R.id.numDisplay, number)
            Log.i("test", "${number}")

            val intent = Intent(context, NewAppWidget::class.java)
            intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
            val pendingIntent = PendingIntent.getBroadcast(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
            )
            remoteViews.setOnClickPendingIntent(R.id.actionButton, pendingIntent)
        }

        // There may be multiple widgets active, so update all of them
        //for (appWidgetId in appWidgetIds) {
            //updateAppWidget(context, appWidgetManager, appWidgetId)
        //}
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
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val widgetText = loadTitlePref(context, appWidgetId)
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.new_app_widget)
    views.setTextViewText(R.id.appwidget_text, widgetText)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}