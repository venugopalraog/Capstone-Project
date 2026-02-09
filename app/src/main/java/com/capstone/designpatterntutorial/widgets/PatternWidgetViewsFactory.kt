package com.capstone.designpatterntutorial.widgets

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Binder
import android.os.Build
import android.text.Html
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.capstone.designpatterntutorial.R
import com.capstone.designpatterntutorial.database.DesignPatternContract.FavoritePatternEntry
import com.capstone.designpatterntutorial.views.activities.HomeActivity

class PatternWidgetViewsFactory(
    private val context: Context,
    intent: Intent
) : RemoteViewsService.RemoteViewsFactory {

    private val appWidgetId: Int = intent.getIntExtra(
        AppWidgetManager.EXTRA_APPWIDGET_ID,
        AppWidgetManager.INVALID_APPWIDGET_ID
    )
    private var cursor: Cursor? = null

    override fun onCreate() {
        // Data is fetched in onDataSetChanged(), which is called immediately after.
    }

    override fun onDataSetChanged() {
        val identityToken = Binder.clearCallingIdentity()
        cursor?.close()
        cursor = context.contentResolver.query(
            FavoritePatternEntry.CONTENT_URI,
            FavoritePatternEntry.FAVORITE_PATTERN_COLUMNS,
            null,
            null,
            FavoritePatternEntry.COLUMN_NAME
        )
        Binder.restoreCallingIdentity(identityToken)
    }

    override fun onDestroy() {
        cursor?.close()
    }

    override fun getCount(): Int {
        return cursor?.count ?: 0
    }

    override fun getViewAt(position: Int): RemoteViews {
        val row = RemoteViews(context.packageName, R.layout.list_item_quote_widget)
        cursor?.moveToPosition(position)

        val name = cursor?.getString(cursor!!.getColumnIndexOrThrow(FavoritePatternEntry.COLUMN_NAME))
        val descriptionHtml = cursor?.getString(cursor!!.getColumnIndexOrThrow(FavoritePatternEntry.COLUMN_DESCRIPTION))

        val descriptionText = if (descriptionHtml != null) {
            val unescapedHtml = descriptionHtml.replace("\\r\\n", "").replace("\\", "")
            val plainText = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(unescapedHtml, Html.FROM_HTML_MODE_LEGACY).toString()
            } else {
                @Suppress("DEPRECATION")
                Html.fromHtml(unescapedHtml).toString()
            }
            // Truncate to the first sentence for a cleaner widget UI.
            plainText.substringBefore(".") + "."
        } else {
            ""
        }

        row.setTextViewText(R.id.item_design_pattern_title, name)
        row.setTextViewText(R.id.item_design_pattern_description, descriptionText)

        val clickIntent = Intent(context, HomeActivity::class.java)
        row.setOnClickFillInIntent(R.id.widget_row, clickIntent)

        return row
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }
}