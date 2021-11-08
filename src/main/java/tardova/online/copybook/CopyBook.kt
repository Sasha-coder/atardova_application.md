package tardova.online.copybook

import android.app.Application
import android.view.Gravity
import android.widget.Toast
import androidx.room.Room
import retrofit2.Response
import okhttp3.OkHttpClient
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import tardova.online.copybook.model.AppDatabase
import tardova.online.copybook.model.DocumentDao
import tardova.online.copybook.model.WikiEntity
import tardova.online.copybook.service.Wiki

class CopyBook : Application() {

    lateinit var db: AppDatabase
    lateinit var documentDao: DocumentDao
    lateinit var okHttpClient: OkHttpClient
    lateinit var retrofit: Retrofit
    lateinit var wikiWithMoshi: Wiki
    lateinit var wikiWithScalars: Wiki

    override fun onCreate() {
        super.onCreate()
        instance = this

        db = Room.databaseBuilder( // подключаю базу данных Room
                applicationContext,
                AppDatabase::class.java, dbName
        ).allowMainThreadQueries().build()

        documentDao = db.documentDao()

        okHttpClient = OkHttpClient.Builder().build() // сервис обрабатывающий запросы
        val retrofitWithScalars = Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://ru.wikipedia.org")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()
        wikiWithScalars = retrofitWithScalars.create(Wiki::class.java)
        val retrofitWithMoshi = Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://ru.wikipedia.org")
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
        wikiWithMoshi = retrofitWithMoshi.create(Wiki::class.java) // скарс не умеет парсить вид страниц которые возвращает Вики, а именно: [string, [] ...]
    }

    suspend fun search(definition: String) : String? {
        val response: Response<WikiEntity> = wikiWithMoshi.search(definition)
        if (response.isSuccessful) {
            val pages = response.body()?.query?.pages?.entries // set всех мапов, т е всех WikiQuery
            val page = pages?.iterator()?.next()?.value //итератор указывает до объекта
            val title = page?.title
            val extract = page?.extract ?: return definition
            val str = extract.substringBefore('(') + extract.substringAfter(')') // эта строчка обусловлена стоением
            val found = str.substringBefore('.') + '.' // я беру все до первой точки
            val found1 = definition + " — " + found.substringAfter('—')
            return found1
        } else {
            val defi = definition + " — В Вики такого нет:("
            return defi;
        }

    }

    suspend fun searchUrl(theme: String) : String? {
        val response: Response<String?> = wikiWithScalars.searchUrl(theme)
        if (response.isSuccessful) {
            println("I THOUGHT IT WAS SUCCESSFUL")
            println(response)
            println(response.body())
            val myLen = theme.length + 13
            val len = response.body()?.length
            if (myLen == len) {
                val text = "KEK"
                return text
            }

            val jsonObject: JSONArray? = response.body()?.run { JSONArray(this) } // если 0, то 0, если нет, то jsonArray
//            println("HERE")
//            println(jsonObject)

            val jsonArray = jsonObject?.getJSONArray(3)
            val res = jsonArray?.getString(0)
            return res
        } else {
            val text = "KEK"
            return text
        }
    }


    companion object {
        lateinit var instance: CopyBook
            private set
        lateinit var mainAdapter : DocumentAdapter
    }


}