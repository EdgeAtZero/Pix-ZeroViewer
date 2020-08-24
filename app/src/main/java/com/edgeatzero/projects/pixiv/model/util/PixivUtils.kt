package com.edgeatzero.projects.pixiv.model.util

import android.content.Context
import android.content.Intent
import com.edgeatzero.projects.pixiv.R
import com.edgeatzero.projects.pixiv.model.Illustration

object PixivUtils {

    fun Context.share(item: Illustration?) {
        item ?: return
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, item.generaShareMessage())
        }
        startActivity(Intent.createChooser(intent, getString(R.string.title_share)))
    }

    private fun Illustration.generaShareMessage() = "$title | $author #Pixiv ${generaShareLink()}"

    private fun Illustration.generaShareLink() = "https://www.pixiv.net/artworks/${id}"

}
