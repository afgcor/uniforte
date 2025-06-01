package com.example.uniforte

import android.content.Context
import android.util.AttributeSet
import android.webkit.WebView

class DraggableWebView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : WebView(context, attrs, defStyle) {

    override fun performClick(): Boolean {
        return super.performClick()
    }
}
