package com.mahmood.mintoakledger.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mahmood.mintoakledger.databinding.ItemMidBinding
import com.mahmood.mintoakledger.domain.model.Mid
import com.mahmood.mintoakledger.presentation.util.AnimationUtils

/**
 * Adapter for displaying Mid items in the top level of the hierarchy.
 */
class MidAdapter(
    private var midItems: List<Mid>,
    private val onMidClickListener: (position: Int) -> Unit
) : RecyclerView.Adapter<MidAdapter.MidViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MidViewHolder {
        val binding = ItemMidBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MidViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MidViewHolder, position: Int) {
        holder.bind(midItems[position], position)
    }

    override fun getItemCount(): Int = midItems.size

    /**
     * Updates the adapter with new Mid items.
     * @param newMidItems The new list of Mid items.
     */
    fun updateData(newMidItems: List<Mid>) {
        this.midItems = newMidItems
        notifyDataSetChanged()
    }

    inner class MidViewHolder(private val binding: ItemMidBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(mid: Mid, position: Int) {
            // Set header information
            binding.tvMid.text = "Mid : ${mid.mid}"

            // Set up child RecyclerView
            binding.rvTidList.layoutManager = LinearLayoutManager(binding.root.context)
            binding.rvTidList.adapter = TidAdapter(mid.tidItems, mid.mid)

            // Set expansion state
            updateExpansionState(mid.isExpanded, false) // No animation for initial state

            // Set click listener for expanding/collapsing
            binding.layoutMidHeader.setOnClickListener {
                onMidClickListener(position)
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
                    binding.ivMidExpand,
                    if (isExpanded) 0f else 180f,
                    if (isExpanded) 180f else 0f
                )
                
                // Animate expansion/collapse of content
                AnimationUtils.toggleExpansion(binding.rvTidList, isExpanded)
            } else {
                // Set without animation
                binding.ivMidExpand.rotation = if (isExpanded) 180f else 0f
                binding.rvTidList.visibility = if (isExpanded) android.view.View.VISIBLE else android.view.View.GONE
            }
        }
    }
}