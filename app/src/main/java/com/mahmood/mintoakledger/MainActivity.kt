package com.mahmood.mintoakledger

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mahmood.mintoakledger.databinding.ActivityMainBinding
import com.mahmood.mintoakledger.domain.model.TransactionGroup
import com.mahmood.mintoakledger.presentation.adapter.TransactionGroupAdapter
import com.mahmood.mintoakledger.presentation.viewmodel.TransactionViewModel
import com.mahmood.mintoakledger.presentation.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: TransactionViewModel
    private lateinit var adapter: TransactionGroupAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Initialize view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Set up edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        // Initialize ViewModel
        viewModel = ViewModelProvider(this, ViewModelFactory())[TransactionViewModel::class.java]
        
        // Set up RecyclerView
        setupRecyclerView()
        
        // Observe ViewModel data
        observeViewModel()
        
        // Load data
        viewModel.loadTransactionGroups()
    }
    
    private fun setupRecyclerView() {
        adapter = TransactionGroupAdapter(emptyList()) { position ->
            viewModel.toggleGroupExpansion(position)
            updateRecyclerViewItem(position)
        }
        
        binding.rvTransactionGroups.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }
    }
    
    private fun observeViewModel() {
        // Observe transaction groups
        viewModel.transactionGroups.observe(this) { groups ->
            adapter.updateData(groups)
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
     * Updates the RecyclerView when a group is expanded or collapsed.
     * This ensures the animation is applied correctly.
     */
    private fun updateRecyclerViewItem(position: Int) {
        val viewHolder = binding.rvTransactionGroups.findViewHolderForAdapterPosition(position) as? TransactionGroupAdapter.TransactionGroupViewHolder
        viewHolder?.let {
            val isExpanded = viewModel.transactionGroups.value?.get(position)?.isExpanded ?: false
            it.updateExpansionState(isExpanded, true)
        }
    }
}