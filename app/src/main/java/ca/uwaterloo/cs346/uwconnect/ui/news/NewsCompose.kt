package ca.uwaterloo.cs346.uwconnect.ui.news

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.style.URLSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import ca.uwaterloo.cs346.uwconnect.R
import androidx.fragment.app.Fragment
import androidx.compose.ui.platform.ComposeView
import ca.uwaterloo.cs346.uwconnect.ui.home.postReview
import ca.uwaterloo.cs346.uwconnect.ui.home.userProfile
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.core.text.HtmlCompat
import androidx.core.text.getSpans

class NewsCompose : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.compose_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rssSources = listOf(
            "Math News" to "https://uwaterloo.ca/math/news/news.xml",
            "MathNews UW" to "https://mathnews.uwaterloo.ca/?feed=rss",
            "Engineering News" to "https://uwaterloo.ca/engineering/news/news.xml",
            "Science News" to "https://uwaterloo.ca/science/news/news.xml"
        )

        // Find the ComposeView from the layout
        val composeView = view.findViewById<ComposeView>(R.id.navigation_news_compose)

        val viewModel = NewsViewModel()
        // Set the Composable content for the ComposeView
        composeView.setContent {
            MaterialTheme {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    RssSourceSelector(viewModel = viewModel, rssSources = rssSources)
                    NotificationsScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun RssSourceSelector(viewModel: NewsViewModel, rssSources: List<Pair<String, String>>) {
    var expanded by remember { mutableStateOf(false) }
    var selectedTitle by remember { mutableStateOf(rssSources.first().first) }

    Box(modifier = Modifier
        .fillMaxWidth()
        .wrapContentSize(Alignment.TopStart)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable { expanded = true }
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.List,
                contentDescription = "Select RSS Source",
                modifier = Modifier.size(24.dp) // Adjust the size as needed
            )
            Spacer(modifier = Modifier.width(8.dp)) // Space between the icon and the text
            Text(text = selectedTitle)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            rssSources.forEach { (title, url) ->
                DropdownMenuItem(text = { Text(title) }, onClick = {

                    viewModel.setCurrentRssUrl(url)
                    selectedTitle = title
                    expanded = false
                })
            }
        }
    }
}


@Composable
fun NotificationsScreen(viewModel: NewsViewModel) {
    // 从 ViewModel 中收集文本 LiveData
    val rssContent by viewModel.text.observeAsState(initial = "")
    DisplayRssContent(rssContent)
}

@Composable
fun DisplayRssContent(rssContent: String) {
    val context = LocalContext.current
    val annotatedString = buildAnnotatedString {
        val spanned = HtmlCompat.fromHtml(rssContent, HtmlCompat.FROM_HTML_MODE_LEGACY)

        spanned.getSpans<URLSpan>().forEach { span ->
            val start = spanned.getSpanStart(span)
            val end = spanned.getSpanEnd(span)
            val url = span.url

            // Apply a custom style to make the text look like a hyperlink
            addStyle(
                style = SpanStyle(
                    color = Color.Blue,
                    textDecoration = TextDecoration.Underline
                ),
                start = start,
                end = end
            )

            // Attach URL information to the text so it can be used in the onClick callback
            addStringAnnotation(
                tag = "URL",
                annotation = url,
                start = start,
                end = end
            )
        }

        append(spanned)
    }

    ClickableText(
        text = annotatedString,
        modifier = Modifier.padding(16.dp),
        onClick = { offset ->
            // Find URL annotations at the clicked position and open the link
            annotatedString.getStringAnnotations(tag = "URL", start = offset, end = offset)
                .firstOrNull()?.let { annotation ->
                    // You can use an Intent to open the URL in a browser
                    // or handle the URL click as you see fit
                    println("Clicked URL: ${annotation.item}")
                    val url = annotation.item
                    // Create an Intent to open the URL in a browser
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    // Start the activity using the Intent
                    context.startActivity(intent)
                }
        }
    )
}