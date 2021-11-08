package tardova.online.copybook

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import kotlinx.android.synthetic.main.find_definition.*
import kotlinx.android.synthetic.main.list_activity.*
import kotlinx.android.synthetic.main.list_activity.recycler
import kotlinx.coroutines.launch
import tardova.online.copybook.model.AppDatabase

class FindActivity: AppCompatActivity() {

    lateinit var mainAdapter: DocumentAdapter
    val app = CopyBook.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.find_definition)


        val viewManager = LinearLayoutManager(this)
        recycler.apply {
            layoutManager = viewManager
            val docum = DocumentAdapter(listOf()) { uid -> //DocumentAdapter - написана логика работы с элементами ремайклера
                val intent = Intent(this@FindActivity, DocumentActivity::class.java).apply { //ТАК ОТКРЫВАЕТСЯ НОВАЯ АКТИВИТИ
                    putExtra("uid", uid)
                }
                startActivity(intent) //ДО СЮДА
            }

            mainAdapter = docum

            adapter = docum
        }

        imageButton.setOnClickListener {
            lifecycle.coroutineScope.launch {
                val idTheme = app.documentDao.findTheme("%${find_bar.text.toString()}%")
                mainAdapter.dataSet = idTheme
                mainAdapter.notifyDataSetChanged()
            }
        }


    }
}