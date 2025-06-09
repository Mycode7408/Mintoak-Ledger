package com.mahmood.mintoakledger.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mahmood.mintoakledger.databinding.ItemTransactionGroupBinding
import com.mahmood.mintoakledger.domain.model.TransactionGroup
import com.mahmood.mintoakledger.presentation.util.AnimationUtils

/**
 * Adapter for displaying transaction groups in an expandable RecyclerView.
 */
class TransactionGroupAdapter(
    private var transactionGroups: List<TransactionGroup>,
    private val onGroupClickListener: (position: Int) -> Unit
) : RecyclerView.Adapter<TransactionGroupAdapter.TransactionGroupViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionGroupViewHolder {
        val binding = ItemTransactionGroupBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TransactionGroupViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionGroupViewHolder, position: Int) {
        holder.bind(transactionGroups[position], position)
    }

    override fun getItemCount(): Int = transactionGroups.size

    /**
     * Updates the adapter with new transaction groups.
     * @param newTransactionGroups The new list of transaction groups.
     */
    fun updateData(newTransactionGroups: List<TransactionGroup>) {
        this.transactionGroups = newTransactionGroups
        notifyDataSetChanged()
    }

    inner class TransactionGroupViewHolder(private val binding: ItemTransactionGroupBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(transactionGroup: TransactionGroup, position: Int) {
            // Set header information
            binding.tvMid.text = "Mid: ${transactionGroup.mid}"
            binding.tvTid.text = "Tid: ${transactionGroup.tid}"

            // Set up child RecyclerView
            binding.rvTransactions.layoutManager = LinearLayoutManager(binding.root.context)
            binding.rvTransactions.adapter = TransactionAdapter(transactionGroup.transactions)

            // Set expansion state
            updateExpansionState(transactionGroup.isExpanded, false) // No animation for initial state

            // Set click listener for expanding/collapsing
            binding.layoutHeader.setOnClickListener {
                onGroupClickListener(position)
                // Animation will be handled in onBindViewHolder when the data changes
            }
        }
        
        /**
         * Updates the expansion state of the view with optional animation.
         * @param isExpanded Whether the view should be expanded.
         * @param animate Whether to animate the transition.
         */
        fun updateExpansionState(isExpanded: Boolean, animate: Boolean) {
            if (animate) {
                // Animate rotation of expand icon
                AnimationUtils.rotateView(
                    binding.ivExpand,
                    if (isExpanded) 0f else 180f,
                    if (isExpanded) 180f else 0f
                )
                
                // Animate expansion/collapse of content
                AnimationUtils.toggleExpansion(binding.rvTransactions, isExpanded)
            } else {
                // Set without animation
                binding.ivExpand.rotation = if (isExpanded) 180f else 0f
                binding.rvTransactions.visibility = if (isExpanded) View.VISIBLE else View.GONE
            }
        }
    }
    
    /**
     * Updates a specific item when its expansion state changes.
     * @param position The position of the item to update.
     * @param isExpanded The new expansion state.
     */
    fun updateItemExpansion(position: Int, isExpanded: Boolean) {
        if (position >= 0 && position < transactionGroups.size) {
            // The ViewHolder update is handled by MainActivity
            notifyItemChanged(position)
        }
    }
}