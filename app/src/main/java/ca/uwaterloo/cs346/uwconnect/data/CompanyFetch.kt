package ca.uwaterloo.cs346.uwconnect.data

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.runBlocking
import org.json.JSONArray

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

    /*
    JSON String to csv format string
    fun parseCompaniesJSON(jsonString: String): List<Company> {

        val jsonArray = JSONArray(jsonString)

        val result = StringBuilder()

        for (i in 0 until jsonArray.length()) {
            val jsonObj = jsonArray.getJSONObject(i)
            val cId = jsonObj.getInt("c_id")
            val cName = jsonObj.getString("c_name")

            result.append("$cId,$cName\n")
        }

        println(result)

        return parseCompanies(result.toString())
    }
    */

    fun fetchCompany(): List<Company> {
        return fetchRemoteFileAndParse("http://frc6399.com/conn-company.php")
    }

}