package ca.uwaterloo.cs346.uwconnect.data

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.runBlocking

class CompanyFetch {

    fun fetchRemoteFileAndParse(url: String): List<Company> {
        return runBlocking {
            val client = HttpClient(Android)
            try {
                val response: HttpResponse = client.get(url)
                val responseData: String = response.bodyAsText()
                parseCompanies(responseData)
            } catch (e: Exception) {
                println(e)
                emptyList<Company>()
            } finally {
                client.close()
            }
        }
    }

    fun parseCompanies(data: String): List<Company> {
        val lines = data.split("\n")
        return lines
            .filter { it.isNotEmpty() }
            .map { line ->
                val parts = line.split(",")
                Company(parts[0].toInt(), parts[1])
            }
    }

    fun fetchCompany(): List<Company> {
        return fetchRemoteFileAndParse("https://frc6399.com/conn-company.php")

    }

}