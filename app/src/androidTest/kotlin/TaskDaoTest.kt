import android.content.Context
import android.util.Log
import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.one.toit.data.TaskDataBase
import com.one.toit.data.dao.TaskDao
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import timber.log.Timber

@RunWith(AndroidJUnit4::class)
class TaskDaoTest {

    private lateinit var database: TaskDataBase
    private lateinit var taskDao: TaskDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        Timber.plant(Timber.DebugTree())
        database = Room.inMemoryDatabaseBuilder(context, TaskDataBase::class.java)
            .allowMainThreadQueries()
            .build()
        taskDao = database.TaskDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

//    @Test
//    fun testRead() = runBlocking {
//        //taskDao.insert(article)
//        val list1 = taskDao.readTaskList()
//        Log.d("TEST", "list => $list1")
//
//        val list = taskDao.readSortedTaskSource(1, 10, 1).load(
//            PagingSource.LoadParams.Refresh(
//                key = null,
//                loadSize = 10,
//                placeholdersEnabled = false
//            )
//        )
//        // Log.d("TEST", "list => $list")
//    }
}