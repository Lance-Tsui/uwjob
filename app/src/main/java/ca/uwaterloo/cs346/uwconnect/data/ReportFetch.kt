package ca.uwaterloo.cs346.uwconnect.data

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.runBlocking

class ReportFetch {
    fun fetchRemoteFileAndParse(url: String): List<Report> {
        return runBlocking {
            val client = HttpClient(Android)
            try {
                val response: HttpResponse = client.get(url)
                val responseData: String = response.bodyAsText()
                parseReports(responseData)
            } catch (e: Exception) {
                println(e)
                emptyList<Report>()
            } finally {
                client.close()
            }
        }
    }

    fun parseReports(data: String): List<Report> {
        // 分割字符串为行
        val lines = data.split("\n")
        // 跳过标题行，并映射每行为Report对象
        return lines// 跳过标题行
            .filter { it.isNotEmpty() } // 过滤掉空行
            .map { line ->
                val parts = line.split(",") // 分割每行为r_id, s_id, p_id
                Report(parts[0].toInt(), parts[1].toInt(), parts[2].toInt()) // 创建Report对象
            }
    }

    fun fetchReport(): List<Report> {
        return fetchRemoteFileAndParse("http://frc6399.com/conn-report.php")
    }
}