import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.todo.data.ToDoDao
import com.example.todo.data.ToDoDatabase
import org.junit.Before
import org.junit.runner.RunWith
import android.content.Context
import androidx.room.Room
import com.example.todo.data.ToDoItem
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.IOException
import kotlin.jvm.Throws


private lateinit var itemDao: ToDoDao
private lateinit var toDoDatabase: ToDoDatabase

@RunWith(AndroidJUnit4::class)
class ItemDaoTest {

    private var item1 = ToDoItem(1, "item1", "desc 1", 10L, 10L)
    private var item2 = ToDoItem(2, "item2", "desc 2", 100L, 100L)
    suspend fun addOneItemToDb() {
        itemDao.insertToDo(item1)
    }

    suspend fun addTwoItemsToDb() {
        itemDao.insertToDo(item1)
        itemDao.insertToDo(item2)
    }

    @Test
    @Throws(Exception::class)
    fun insertItemIntoDb() = runBlocking {
        addOneItemToDb()
        val allItems = itemDao.getToDoList().first()
        assertEquals(allItems[0], item1)
    }
    @Test
    @Throws(Exception::class)
    fun insertItemsToDb() = runBlocking {
        addTwoItemsToDb()
        val allItems = itemDao.getToDoList().first()
        assertEquals(allItems[0], item1)
        assertEquals(allItems[1], item2)
    }


    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        toDoDatabase = Room.inMemoryDatabaseBuilder(context, ToDoDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        itemDao = toDoDatabase.dao()
    }


    @After
    @Throws(IOException::class)
    fun closeDb() {
        toDoDatabase.close()
    }
}