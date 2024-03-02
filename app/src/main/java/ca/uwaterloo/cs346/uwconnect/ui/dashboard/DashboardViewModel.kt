package ca.uwaterloo.cs346.uwconnect.ui.dashboard

import android.content.Context

import androidx.lifecycle.ViewModel
import ca.uwaterloo.cs346.uwconnect.ui.dashboard.DataUtils.parseJobData

class DashboardViewModel : ViewModel() {
    fun loadJobData(context: Context): JobData? {
        val jsonString = DataUtils.loadJSONFromAsset(context)
        return jsonString?.let { parseJobData(it) }
    }

}