package tardova.online.copybook

import android.app.Application
import androidx.room.Room
import retrofit2.Response
import okhttp3.OkHttpClient
import org.json.JSONArray
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

        db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, dbName
        ).allowMainThreadQueries().build()

        documentDao = db.documentDao()

        okHttpClient = OkHttpClient.Builder().build()
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
        wikiWithMoshi = retrofitWithMoshi.create(Wiki::class.java)
    }

    suspend fun search(definition: String) : String? {
        val response: Response<WikiEntity> = wikiWithMoshi.search(definition)
        if (response.isSuccessful) {
            val pages = response.body()?.query?.pages?.entries
            val page = pages?.iterator()?.next()?.value //итератор указывает до объекта
            val title = page?.title
            val extract = page?.extract ?: return definition
            val str = extract.substringBefore('(') + extract.substringAfter(')')
            val found = str.substringBefore('.') + '.'
            return found
        } else {
            return definition;
        }

    }

    suspend fun searchUrl(theme: String) : String? {
        val response: Response<String?> = wikiWithScalars.searchUrl(theme)
        if (response.isSuccessful) {
            val jsonObject: JSONArray? = response.body()?.run { JSONArray(this) } // если 0, то 0, если нет, то jsonArray
            val jsonArray = jsonObject?.getJSONArray(3)
            val res = jsonArray?.getString(0)
            return res
        } else {
            return null
        }
    }


    companion object {
        lateinit var instance: CopyBook
            private set
        lateinit var mainAdapter : DocumentAdapter
    }


}
