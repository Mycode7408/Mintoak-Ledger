package com.mahmood.mintoakledger.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mahmood.mintoakledger.data.model.Transaction
import com.mahmood.mintoakledger.databinding.ItemEntryBinding

/**
 * Adapter for displaying individual transaction entries in the third level of the hierarchy.
 */
class EntryAdapter(
    private val transactions: List<Transaction>
) : RecyclerView.Adapter<EntryAdapter.EntryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder {
        val binding = ItemEntryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EntryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
        holder.bind(transactions[position])
    }

    override fun getItemCount(): Int = transactions.size

    class EntryViewHolder(private val binding: ItemEntryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(transaction: Transaction) {
            binding.tvAmount.text = "Amount : ${transaction.amount}"
            binding.tvNarration.text = "Narration : ${transaction.narration}"
        }
    }
}