package ca.uwaterloo.cs346.uwconnect.ui.notifications

import android.text.TextPaint
import android.text.style.URLSpan

class NoUnderlineSpan(url: String) : URLSpan(url) {
    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.isUnderlineText = false
    }
}
