package tardova.online.copybook.model

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize

@Entity // это коробка
@Parcelize //это коробка внутри коробки
data class Document(
        @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var uid: Long = 0,
        @ColumnInfo(name = "theme") var theme: String = "",
        @ColumnInfo(name = "definitions") var definitions: String = "",
        @ColumnInfo(name = "content") var content: String = "",
        @ColumnInfo(name = "notes") var notes: String = "",
        @ColumnInfo(name = "homework") var homework: String =  ""
) : Parcelable

@Dao
interface DocumentDao {
    @Query("SELECT id, theme FROM document") // ищем id и тему
    suspend fun getAll(): List<IdTheme>

    @Query ("SELECT * FROM document WHERE id = :id" )
    suspend fun getById(id: Long) : Document?

    @Insert()
    suspend fun insert(document: Document) : Long

    @Query("DELETE FROM document WHERE id = :uid")
    suspend fun delete(uid: Long)

    @Update()
    suspend fun update(document: Document)

    @Query("SELECT id, theme FROM document WHERE (LOWER(theme) LIKE LOWER(:str)) or (LOWER(definitions) LIKE LOWER(:str))" )
    suspend fun findTheme(str: String) : List<IdTheme>
}

@Database(entities = arrayOf(Document::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun documentDao(): DocumentDao
}

data class IdTheme(
        @ColumnInfo(name = "id") var uid: Long = 0,
        @ColumnInfo(name = "theme") var theme: String = "",
)
