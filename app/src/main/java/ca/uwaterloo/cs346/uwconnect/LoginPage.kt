package ca.uwaterloo.cs346.uwconnect

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class LoginPage : AppCompatActivity() {

    private fun fakeAuthenticate(email:String, password:String) : Boolean{
        // placeholder that needs to be replaced with the real thing
        val result = email == "testuser@uwaterloo.ca" && password == "password"
        return result
    }
    private fun authenticate(email:String, password:String){
        // to be implemented
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        val emailTextBox = findViewById<EditText>(R.id.EmailID)
        val passwordTextBox = findViewById<EditText>(R.id.PasswordID)
        val loginBtn = findViewById<Button>(R.id.LoginBtnID)

        loginBtn.setOnClickListener{
            val email: String = emailTextBox.text.toString()
            val password: String = passwordTextBox.text.toString()
            // detect shenanigans
            if(TextUtils.isEmpty(email)){
                Toast.makeText(this@LoginPage, "Please enter your email", Toast.LENGTH_SHORT).show()
            }
            else if(TextUtils.isEmpty(password)){
                Toast.makeText(this@LoginPage, "Please enter a password", Toast.LENGTH_SHORT).show()
            }
            // try to log in
            else{
                if(fakeAuthenticate(email, password)){
                    val homepage = Intent(this@LoginPage, MainActivity::class.java)
                    startActivity(homepage)
                    finish()
                }
                else{
                    Toast.makeText(this@LoginPage, "Your email or password is invalid, please try again", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}