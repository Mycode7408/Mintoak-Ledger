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
        
        // Initialize view binding
        binding = ActivityLedgerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Set up edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(binding.ledgerContainer) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        // Initialize ViewModel
        viewModel = ViewModelProvider(this, ViewModelFactory())[LedgerViewModel::class.java]
        
        // Set up RecyclerView
        setupRecyclerView()
        
        // Observe ViewModel data
        observeViewModel()
        
        // Load data
        viewModel.loadLedgerData()
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
        // Observe Mid items
        viewModel.midItems.observe(this) { midItems ->
            adapter.updateData(midItems)
        }
        
        // Observe loading state
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        
        // Observe error state
        viewModel.error.observe(this) { errorMessage ->
            if (errorMessage != null) {
                binding.tvError.text = errorMessage
                binding.tvError.visibility = View.VISIBLE
            } else {
                binding.tvError.visibility = View.GONE
            }
        }
    }
    
    /**
     * Updates the RecyclerView when a Mid item is expanded or collapsed.
     * This ensures the animation is applied correctly.
     */
    private fun updateRecyclerViewItem(position: Int) {
        val viewHolder = binding.rvMidList.findViewHolderForAdapterPosition(position) as? MidAdapter.MidViewHolder
        viewHolder?.let {
            val isExpanded = viewModel.midItems.value?.get(position)?.isExpanded ?: false
            it.updateExpansionState(isExpanded, true)
        }
    }
}