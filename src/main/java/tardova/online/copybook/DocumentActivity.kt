package tardova.online.copybook

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.coroutineScope
import androidx.room.Room
import kotlinx.android.synthetic.main.document2.*
import tardova.online.copybook.model.AppDatabase
import tardova.online.copybook.model.Document
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tardova.online.copybook.model.DocumentDao
import java.lang.StringBuilder
import android.widget.Toast.makeText as makeText1

class DocumentActivity: AppCompatActivity() {

    val app = CopyBook.instance
    var ourId = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.document2)

        onNewIntent(intent) // чтобы если несколько раз нажали, не открывался

    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        document_progress.show()

        ourId = intent?.getLongExtra("uid", 0L) ?: 0L //забираем наш интент

        if (ourId != 0L) {
            lifecycle.coroutineScope.async {
                val doc = app.documentDao.getById(ourId)
                if (doc != null) {
                    mainTheme.setText(doc.theme)
                    definitions.setText(doc.definitions)
                    content.setText(doc.content)
                    notes.setText(doc.notes)
                    homework.setText(doc.homework)
                }

                document_progress.hide()
            }
        } else {
            document_progress.hide()
        }

        saveBottom.setOnClickListener {
            lifecycle.coroutineScope.launch {
                val doc: Document = Document(ourId)

                doc.theme = mainTheme.text.toString()
                doc.definitions = definitions.text.toString()
                doc.content = content.text.toString()
                doc.notes = notes.text.toString()
                doc.homework = homework.text.toString()

                if (ourId == 0L) {
                    ourId = app.documentDao.insert(doc)
                } else {
                    app.documentDao.update(doc)
                }


            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.document_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete -> {
                lifecycle.coroutineScope.launch {
                    app.documentDao.delete(ourId)
                }
                return true
            }
            R.id.update_definition -> {
                val defs = definitions.text.toString()
                lifecycle.coroutineScope.launch {
                    document_progress.show()
                    val stringBuilder = StringBuilder()
                    for (str in defs.split(System.lineSeparator())) {
                        if (str.trim().last() == '-') {
                            stringBuilder.append(app.search(str.substringBefore('-')))
                        } else {
                            stringBuilder.append(str)
                        }
                        stringBuilder.append(System.lineSeparator())
                    }
                    definitions.setText(stringBuilder.toString())
                    document_progress.hide()
                }
                return true
            }
            R.id.help -> {
                document_progress.show()

                val them = mainTheme.text.toString()
                lifecycle.coroutineScope.launch {
                    val url = app.searchUrl(them)
                    if (url != "KEK") {
                        val intent = Intent(
                            this@DocumentActivity,
                            WikiWebActivity::class.java
                        ).apply { //ТАК ОТКРЫВАЕТСЯ НОВАЯ АКТИВИТИ
                            putExtra("Url", url)
                        }
                        document_progress.hide()
                        startActivity(intent) //ДО СЮДА
                    } else {
                        document_progress.hide()
                        val text = "Упс, ничего не найдено!"
                        val duration = Toast.LENGTH_LONG
                        val toast = Toast.makeText(this@DocumentActivity, text, duration)
                        toast.show()

                    }
                }
                return true
            }


            else -> super.onOptionsItemSelected(item)
        }
    }



}