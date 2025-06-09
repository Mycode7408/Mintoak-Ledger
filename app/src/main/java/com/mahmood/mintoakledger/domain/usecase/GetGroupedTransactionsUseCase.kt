package com.mahmood.mintoakledger.domain.usecase

import com.mahmood.mintoakledger.domain.model.TransactionGroup
import com.mahmood.mintoakledger.domain.repository.TransactionRepository

/**
 * Use case for getting grouped transactions.
 */
class GetGroupedTransactionsUseCase(private val repository: TransactionRepository) {
    
    /**
     * Executes the use case to get grouped transactions.
     * @return List of TransactionGroup objects.
     */
    suspend operator fun invoke(): List<TransactionGroup> {
        return repository.getGroupedTransactions()
    }
}