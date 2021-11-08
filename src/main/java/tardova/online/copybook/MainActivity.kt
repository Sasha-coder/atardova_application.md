package tardova.online.copybook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Insert
import androidx.room.Room
import kotlinx.android.synthetic.main.document2.*
import kotlinx.android.synthetic.main.list_activity.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tardova.online.copybook.model.AppDatabase
import tardova.online.copybook.model.Document

const val dbName = "copybook-db2"

class MainActivity : AppCompatActivity() {

    val app = CopyBook.instance

    lateinit var mainAdapter: DocumentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_activity)  // говорим, что отрисовать

        list_progress.show()


        val viewManager = LinearLayoutManager(this)
        recycler.apply {
            layoutManager = viewManager
            val docum = DocumentAdapter(listOf()) { uid -> //DocumentAdapter - написана логика работы с элементами ремайклера
                val intent = Intent(this@MainActivity, DocumentActivity::class.java).apply { //ТАК ОТКРЫВАЕТСЯ НОВАЯ АКТИВИТИ
                    putExtra("uid", uid)
                }
                startActivity(intent) //ДО СЮДА
            }

            mainAdapter = docum
            CopyBook.mainAdapter = mainAdapter

            adapter = docum
            lifecycle.coroutineScope.launch {
                val doc = app.documentDao.getAll()
                docum.dataSet = doc
                docum.notifyDataSetChanged()  //перерисует весь список

                list_progress.hide()
            }
        }

        addButton.setOnClickListener {
            val intent = Intent(this@MainActivity, DocumentActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_layout, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.search -> {
                val intent = Intent(this@MainActivity, FindActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}