package de.bilkewall.adapters

interface DatabasePopulator {
    suspend fun clearExistingData()
    suspend fun insertInitialData()
}