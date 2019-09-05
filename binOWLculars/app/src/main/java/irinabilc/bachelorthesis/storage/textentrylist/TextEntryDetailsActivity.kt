package irinabilc.bachelorthesis.storage.textentrylist

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import irinabilc.bachelorthesis.R
import irinabilc.bachelorthesis.TextEntryDetailsActivityBInding
import irinabilc.bachelorthesis.model.TextEntry

class TextEntryDetailsActivity : AppCompatActivity() {
    private lateinit var binding: TextEntryDetailsActivityBInding
    private lateinit var text_entry: TextEntry

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_text_entry_details)
        text_entry = intent.getParcelableExtra("text_entry")
        binding.textEntry = text_entry
    }

    companion object {
        const val KEY_TEXT_ENTRY = "text_entry"
        fun getSTartIntent(context: Context, entry: TextEntry): Intent {
            val intent = Intent(context, TextEntryDetailsActivity::class.java)
            intent.putExtra(KEY_TEXT_ENTRY, entry)
            return intent
        }
    }
}
