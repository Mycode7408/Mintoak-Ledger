package com.mahmood.mintoakledger.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mahmood.mintoakledger.data.repository.TransactionRepositoryImpl
import com.mahmood.mintoakledger.domain.usecase.GetGroupedTransactionsUseCase
import com.mahmood.mintoakledger.domain.usecase.GetHierarchicalTransactionsUseCase

/**
 * Factory for creating ViewModels with dependencies.
 */
class ViewModelFactory : ViewModelProvider.Factory {
    
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Create repository for both ViewModels
        val repository = TransactionRepositoryImpl()
        
        when {
            modelClass.isAssignableFrom(TransactionViewModel::class.java) -> {
                val getGroupedTransactionsUseCase = GetGroupedTransactionsUseCase(repository)
                return TransactionViewModel(getGroupedTransactionsUseCase) as T
            }
            
            modelClass.isAssignableFrom(LedgerViewModel::class.java) -> {
                val getHierarchicalTransactionsUseCase = GetHierarchicalTransactionsUseCase(repository)
                return LedgerViewModel(getHierarchicalTransactionsUseCase) as T
            }
            
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}