package com.example.showmagnet.data.source.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith


@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class UserPreferencesManagerImplTest {
    @get:Rule
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher + Job())

    private lateinit var userPreferencesManager: UserPreferencesManager

    @Before
    fun setUp() {
        val testDataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
            scope = testScope,
            produceFile = { tmpFolder.newFile("user.preferences_pb") }
        )
        userPreferencesManager = UserPreferencesManagerImpl(
            dataStore = testDataStore
        )

    }


    @Test
    fun updateIsUserSignedInTrueSuccessTest() = runTest {
        val result = userPreferencesManager.updateIsUserSignedIn(true)
        assert(result.isSuccess)

        val userPreferencesFlow = userPreferencesManager.userPreferencesFlow.first()
        assert(userPreferencesFlow.isUserSignedIn)
    }

    @Test
    fun updateIsUserSignedInFalseSuccessTest() = runTest {
        val result = userPreferencesManager.updateIsUserSignedIn(false)
        assert(result.isSuccess)

        val userPreferencesFlow = userPreferencesManager.userPreferencesFlow.first()
        assert(!userPreferencesFlow.isUserSignedIn)
    }
}


