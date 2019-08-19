package irinabilc.bachelorthesis

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import android.content.SharedPreferences



class MainActivity : AppCompatActivity() {

    val PREFS_FILENAME = "irinabilc.bachelorthesis"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startActivity(Intent(this, LoginActivity::class.java))

    }

}
