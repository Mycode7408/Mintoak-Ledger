package com.mahmood.mintoakledger.domain.repository

import com.mahmood.mintoakledger.data.model.Transaction
import com.mahmood.mintoakledger.domain.model.TransactionGroup

/**
 * Repository interface for transactions in the domain layer.
 */
interface TransactionRepository {

    suspend fun getTransactions(): List<Transaction>
    

    suspend fun getGroupedTransactions(): List<TransactionGroup>
}