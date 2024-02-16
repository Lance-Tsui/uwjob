package ca.uwaterloo.cs346.uwconnect.ui.dashboard

class Job(
    val title: String,
    val description: String,
    val requirements: List<String>,
    var salaryRange: String? = null, // 可选属性，带有默认值null
    val isRemote: Boolean = false // 带有默认值false
) {
    // 这里可以添加一些方法，比如格式化输出职位信息等
    fun printJobDetails() {
        println("Job Title: $title")
        println("Description: $description")
        println("Requirements:")
        requirements.forEach { println("- $it") }
        salaryRange?.let { println("Salary Range: $it") }
        println("Remote: ${if (isRemote) "Yes" else "No"}")
    }
}

// 使用示例
fun main() {
    val job = Job(
        title = "Android Developer",
        description = "Develop and maintain Android applications.",
        requirements = listOf("Kotlin", "Android Studio", "Git", "MVVM"),
        salaryRange = "$80,000 - $120,000",
        isRemote = true
    )

    job.printJobDetails()
}