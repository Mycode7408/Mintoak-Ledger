package com.mahmood.mintoakledger.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mahmood.mintoakledger.databinding.ItemEntryBinding
import com.mahmood.mintoakledger.databinding.ItemTidBinding
import com.mahmood.mintoakledger.domain.model.Tid
import com.mahmood.mintoakledger.presentation.util.AnimationUtils

/**
 * Adapter for displaying Tid items in the second level of the hierarchy.
 */
class TidAdapter(
    private val tidItems: List<Tid>,
    private val parentMid: Int
) : RecyclerView.Adapter<TidAdapter.TidViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TidViewHolder {
        val binding = ItemTidBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TidViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TidViewHolder, position: Int) {
        holder.bind(tidItems[position])
    }

    override fun getItemCount(): Int = tidItems.size

    inner class TidViewHolder(private val binding: ItemTidBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(tid: Tid) {
            // Set header information
            binding.tvTid.text = "Tid : ${tid.tid}"

            // Add entry views directly to the layout
            binding.layoutEntries.removeAllViews() // Clear existing views
            
            // Add each transaction as a direct child view
            for (transaction in tid.transactions) {
                val entryBinding = ItemEntryBinding.inflate(
                    LayoutInflater.from(binding.root.context),
                    binding.layoutEntries,
                    false
                )
                
                // Set the entry data
                entryBinding.tvAmount.text = "Amount : ${transaction.amount}"
                entryBinding.tvNarration.text = "Narration : ${transaction.narration}"
                
                // Add the view to the parent layout
                binding.layoutEntries.addView(entryBinding.root)
            }

            // Set expansion state
            updateExpansionState(tid.isExpanded, false) // No animation for initial state

            // Set click listener for expanding/collapsing
            binding.layoutTidHeader.setOnClickListener {
                tid.isExpanded = !tid.isExpanded
                updateExpansionState(tid.isExpanded, true)
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
                    binding.ivTidExpand,
                    if (isExpanded) 0f else 180f,
                    if (isExpanded) 180f else 0f
                )
                
                // Animate expansion/collapse of content
                AnimationUtils.toggleExpansion(binding.layoutEntries, isExpanded)
            } else {
                // Set without animation
                binding.ivTidExpand.rotation = if (isExpanded) 180f else 0f
                binding.layoutEntries.visibility = if (isExpanded) android.view.View.VISIBLE else android.view.View.GONE
            }
        }
    }
}