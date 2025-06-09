package com.mahmood.mintoakledger

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mahmood.mintoakledger.databinding.ActivityLedgerBinding
import com.mahmood.mintoakledger.presentation.adapter.MidAdapter
import com.mahmood.mintoakledger.presentation.viewmodel.LedgerViewModel
import com.mahmood.mintoakledger.presentation.viewmodel.ViewModelFactory

class LedgerActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityLedgerBinding
    private lateinit var viewModel: LedgerViewModel
    private lateinit var adapter: MidAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        binding = ActivityLedgerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupEdgeToEdgeDisplay()
        setupViewModel()
        setupRecyclerView()
        observeViewModel()
        viewModel.loadLedgerData()
    }
    
    private fun setupEdgeToEdgeDisplay() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.ledgerContainer) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    
    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, ViewModelFactory())[LedgerViewModel::class.java]
    }
    
    private fun setupRecyclerView() {
        adapter = MidAdapter(emptyList()) { position ->
            viewModel.toggleMidExpansion(position)
            updateRecyclerViewItem(position)
        }
        
        binding.rvMidList.apply {
            layoutManager = LinearLayoutManager(this@LedgerActivity)
            adapter = this@LedgerActivity.adapter
        }
    }
    
    private fun observeViewModel() {

        viewModel.midItems.observe(this) { midItems ->
            adapter.updateData(midItems)
        }
        
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        
        viewModel.error.observe(this) { errorMessage ->
            if (errorMessage != null) {
                binding.tvError.text = errorMessage
                binding.tvError.visibility = View.VISIBLE
            } else {
                binding.tvError.visibility = View.GONE
            }
        }
    }
    

    private fun updateRecyclerViewItem(position: Int) {
        val viewHolder = binding.rvMidList.findViewHolderForAdapterPosition(position) as? MidAdapter.MidViewHolder
        viewHolder?.let {
            val isExpanded = viewModel.midItems.value?.get(position)?.isExpanded ?: false
            it.updateExpansionState(isExpanded, true)
        }
    }
}