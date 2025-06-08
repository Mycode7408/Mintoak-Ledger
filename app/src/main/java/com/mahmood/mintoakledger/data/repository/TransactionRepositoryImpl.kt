package com.mahmood.mintoakledger.data.repository

import com.mahmood.mintoakledger.data.model.Transaction
import com.mahmood.mintoakledger.data.model.TransactionResponse
import com.mahmood.mintoakledger.domain.model.TransactionGroup
import com.mahmood.mintoakledger.domain.repository.TransactionRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

/**
 * Implementation of the TransactionRepository interface.
 */
class TransactionRepositoryImpl : TransactionRepository {
    
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    
    // Sample JSON data for testing
    private val sampleJsonData = """
        { 
          "sort": [ 
            { 
              "Mid": 1, 
              "Tid": 123456797, 
              "amount": 12.32, 
              "narration": 11234684654 
            }, 
            { 
              "Mid": 2, 
              "Tid": 123456794, 
              "amount": 12.32, 
              "narration": 11234684654 
            }, 
            { 
              "Mid": 3, 
              "Tid": 123456791, 
              "amount": 12.32, 
              "narration": 11234684654 
            }, 
            { 
              "Mid": 3, 
              "Tid": 123456791, 
              "amount": 12.32, 
              "narration": 11234684654 
            }, 
            { 
              "Mid": 1, 
              "Tid": 123456797, 
              "amount": 12.32, 
              "narration": 11234684654 
            }, 
            { 
              "Mid": 2, 
              "Tid": 123456794, 
              "amount": 12.32, 
              "narration": 11234684654 
            }, 
            { 
              "Mid": 1, 
              "Tid": 123456797, 
              "amount": 12.32, 
              "narration": 11234684654 
            }, 
            { 
              "Mid": 3, 
              "Tid": 123456791, 
              "amount": 12.32, 
              "narration": 11234684654 
            }, 
            { 
              "Mid": 1, 
              "Tid": 123456795, 
              "amount": 12.32, 
              "narration": 11234684654 
            }, 
            { 
              "Mid": 1, 
              "Tid": 123456795, 
              "amount": 12.32, 
              "narration": 11234684654 
            }, 
            { 
              "Mid": 3, 
              "Tid": 123456791, 
              "amount": 12.32, 
              "narration": 11234684654 
            } 
          ] 
        }
    """.trimIndent()
    
    /**
     * Gets all transactions from the data source.
     * In a real app, this would fetch data from an API or database.
     * @return List of Transaction objects.
     */
    override suspend fun getTransactions(): List<Transaction> {
        // Parse the JSON data using Moshi
        val adapter = moshi.adapter(TransactionResponse::class.java)
        val response = adapter.fromJson(sampleJsonData)
            ?: throw IllegalStateException("Failed to parse JSON data")
        
        // Add logging to verify data parsing
        println("DEBUG: Parsed ${response.sort.size} transactions")
        response.sort.forEach { transaction ->
            println("DEBUG: Transaction: mid=${transaction.mid}, tid=${transaction.tid}, amount=${transaction.amount}, narration=${transaction.narration}")
        }
        
        return response.sort
    }
    
    /**
     * Groups transactions by Mid and Tid.
     * @return List of TransactionGroup objects.
     */
    override suspend fun getGroupedTransactions(): List<TransactionGroup> {
        val transactions = getTransactions()
        
        // Group transactions by Mid and Tid
        return transactions.groupBy { Pair(it.mid, it.tid) }
            .map { (key, transactions) ->
                TransactionGroup(
                    mid = key.first,
                    tid = key.second,
                    transactions = transactions
                )
            }
            .sortedBy { it.mid } // Sort by Mid
    }
}