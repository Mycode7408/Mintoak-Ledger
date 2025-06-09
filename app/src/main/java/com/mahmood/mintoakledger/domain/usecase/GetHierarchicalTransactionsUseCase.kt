package com.mahmood.mintoakledger.domain.usecase

import com.mahmood.mintoakledger.domain.model.Mid
import com.mahmood.mintoakledger.domain.model.Tid
import com.mahmood.mintoakledger.domain.repository.TransactionRepository

/**
 * Use case for getting hierarchical transactions organized in a three-level structure:
 * Mid -> Tid -> Transaction
 */
class GetHierarchicalTransactionsUseCase(private val repository: TransactionRepository) {
    

    suspend operator fun invoke(): List<Mid> {
        val transactions = repository.getTransactions()
        
        println("DEBUG: GetHierarchicalTransactionsUseCase received ${transactions.size} transactions")
        
        val result = transactions.groupBy { it.mid }
            .map { (midValue, midTransactions) ->
                println("DEBUG: Processing Mid=$midValue with ${midTransactions.size} transactions")
                
                val tidGroups = midTransactions.groupBy { it.tid }
                    .map { (tidValue, tidTransactions) ->
                        println("DEBUG: Processing Tid=$tidValue with ${tidTransactions.size} transactions")
                        
                        Tid(
                            tid = tidValue.toString(),
                            transactions = tidTransactions,
                            isExpanded = false
                        )
                    }
                    .sortedBy { it.tid }
                
                println("DEBUG: Created ${tidGroups.size} Tid groups for Mid=$midValue")
                
                Mid(
                    mid = midValue,
                    tidItems = tidGroups,
                    isExpanded = false
                )
            }
            .sortedBy { it.mid }
        
        println("DEBUG: Returning ${result.size} Mid objects")
        return result
    }
}