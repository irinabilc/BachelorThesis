package irinabilc.bachelorthesis.storage.textentrylist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import irinabilc.bachelorthesis.R
import irinabilc.bachelorthesis.TextEntryListActivityBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class TextEntryListActivity : AppCompatActivity() {

    private lateinit var binding: TextEntryListActivityBinding
    private val viewModel: TextEntryListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_text_entry_list)

        binding.lifecycleOwner = this

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        var adapter = TextEntryListAdapter() {}

        adapter = TextEntryListAdapter {
            startActivity(TextEntryDetailsActivity.getSTartIntent(this, it))
        }


        binding.recyclerView.adapter = adapter

        viewModel.entries.observe(this, Observer {
            it ?: return@Observer
            adapter.submitList(it)
        })
    }
}
