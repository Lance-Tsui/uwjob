package ca.uwaterloo.cs346.uwconnect.ui.notifications

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ca.uwaterloo.cs346.uwconnect.databinding.FragmentNotificationsBinding

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.net.URL


class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView = binding.rssTextView
        notificationsViewModel.text.observe(viewLifecycleOwner) { htmlContent ->
            setTextViewHTML(textView, htmlContent)
            textView.movementMethod = LinkMovementMethod.getInstance()
        }

        val spinner = binding.rssSourceSpinner
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> notificationsViewModel.setCurrentRssUrl("https://uwaterloo.ca/math/news/news.xml")
                    1 -> notificationsViewModel.setCurrentRssUrl("https://mathnews.uwaterloo.ca/?feed=rss")
                    2 -> notificationsViewModel.setCurrentRssUrl("https://uwaterloo.ca/engineering/news/news.xml")
                    3 -> notificationsViewModel.setCurrentRssUrl("https://uwaterloo.ca/science/news/news.xml")
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        return root
    }

    fun setTextViewHTML(textView: TextView, html: String) {
        val sequence = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        val spannable = SpannableString(sequence)
        val urlSpans = spannable.getSpans(0, spannable.length, URLSpan::class.java)
        urlSpans.forEach {
            val start = spannable.getSpanStart(it)
            val end = spannable.getSpanEnd(it)
            val flags = spannable.getSpanFlags(it)
            spannable.removeSpan(it)
            spannable.setSpan(NoUnderlineSpan(it.url), start, end, flags)
        }
        textView.text = spannable
        textView.movementMethod = LinkMovementMethod.getInstance() // 允许链接点击
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
