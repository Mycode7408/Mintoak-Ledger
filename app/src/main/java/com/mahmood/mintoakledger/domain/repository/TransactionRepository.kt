package com.mahmood.mintoakledger.domain.repository

import com.mahmood.mintoakledger.data.model.Transaction
import com.mahmood.mintoakledger.domain.model.TransactionGroup

/**
 * Repository interface for transactions in the domain layer.
 */
interface TransactionRepository {
    /**
     * Gets all transactions from the data source.
     * @return List of Transaction objects.
     */
    suspend fun getTransactions(): List<Transaction>
    
    /**
     * Groups transactions by Mid and Tid.
     * @return List of TransactionGroup objects.
     */
    suspend fun getGroupedTransactions(): List<TransactionGroup>
}