package ca.uwaterloo.cs346.uwconnect.ui.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ReportDetail(
    companyName: String,
    positionName: String,
    progress: Int,
    rating: Float,
    comments: List<String>
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(text = companyName)
        Text(text = positionName)

        comments.forEach { comment ->
            Text(text = comment, modifier = Modifier.padding(vertical = 4.dp))
        }
    }
}