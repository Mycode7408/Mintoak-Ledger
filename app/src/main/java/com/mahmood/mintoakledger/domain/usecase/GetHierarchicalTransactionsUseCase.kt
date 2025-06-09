package com.mahmood.mintoakledger.domain.usecase

import com.mahmood.mintoakledger.domain.model.Mid
import com.mahmood.mintoakledger.domain.model.Tid
import com.mahmood.mintoakledger.domain.repository.TransactionRepository

/**
 * Use case for getting hierarchical transactions organized in a three-level structure:
 * Mid -> Tid -> Transaction
 */
class GetHierarchicalTransactionsUseCase(private val repository: TransactionRepository) {
    
    /**
     * Executes the use case to get hierarchical transactions.
     * @return List of Mid objects, each containing Tid objects, which contain Transaction objects.
     */
    suspend operator fun invoke(): List<Mid> {
        val transactions = repository.getTransactions()
        
        println("DEBUG: GetHierarchicalTransactionsUseCase received ${transactions.size} transactions")
        
        // First, group by Mid
        val result = transactions.groupBy { it.mid }
            .map { (midValue, midTransactions) ->
                println("DEBUG: Processing Mid=$midValue with ${midTransactions.size} transactions")
                
                // For each Mid, group by Tid
                val tidGroups = midTransactions.groupBy { it.tid }
                    .map { (tidValue, tidTransactions) ->
                        println("DEBUG: Processing Tid=$tidValue with ${tidTransactions.size} transactions")
                        
                        // Create Tid object with its transactions
                        Tid(
                            tid = tidValue.toString(),
                            transactions = tidTransactions,
                            isExpanded = false
                        )
                    }
                    .sortedBy { it.tid } // Sort Tids
                
                println("DEBUG: Created ${tidGroups.size} Tid groups for Mid=$midValue")
                
                // Create Mid object with its Tid groups
                Mid(
                    mid = midValue,
                    tidItems = tidGroups,
                    isExpanded = false
                )
            }
            .sortedBy { it.mid } // Sort Mids
        
        println("DEBUG: Returning ${result.size} Mid objects")
        return result
    }
}