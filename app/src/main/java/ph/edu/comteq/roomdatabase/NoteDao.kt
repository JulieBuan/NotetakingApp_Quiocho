package ph.edu.comteq.roomdatabase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert
    suspend fun insertNote(note: Note): Long

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Upsert
    suspend fun upsertNote(note: Note)

    @Query("SELECT * FROM notes WHERE id = :id")
    fun getNoteById(id: Int): Note?

    @Query("SELECT * FROM notes ORDER BY id DESC")
    fun getAllNotes(): Flow<List<Note>>

    @Query("DELETE FROM notes")
    suspend fun deleteAllNotes()

    // Search with Category (now sorted using new updated_at field)
    @Query("SELECT * FROM notes WHERE title LIKE '%' || :searchQuery || '%' OR content LIKE '%' || :searchQuery || '%' ORDER BY updated_at DESC")
    fun searchNotes(searchQuery: String): Flow<List<Note>>

    // NEW: Search by category
    @Query("SELECT * FROM notes WHERE category = :category ORDER BY updated_at DESC")
    fun getNotesByCategory(category: String): Flow<List<Note>>

    // get all categories
    @Query("SELECT DISTINCT category FROM notes WHERE category != '' ORDER BY category ASC")
    fun getAllCategories(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTag(tag: Tag): Long

    @Query("SELECT * FROM tags ORDER BY name ASC")
    fun getAllTags(): Flow<List<Tag>>

    @Query("SELECT * FROM tags WHERE id = :id")
    suspend fun getTagById(id: Int): Tag?

    // For note tags
    // Connect a note to a tag
    @Insert(onConflict = OnConflictStrategy.IGNORE)  // Ignore if already connected
    suspend fun insertNoteTagCrossRef(crossRef: NoteTagCrossRef)

    // Disconnect a note from a tag
    @Delete
    suspend fun deleteNoteTagCrossRef(crossRef: NoteTagCrossRef)

    // get all notes with their tags
    @Transaction  // Important: Ensures all data loads together
    @Query("SELECT * FROM notes WHERE id = :noteId")
    fun getNoteWithTags(noteId: Int): NoteWithTags?

    // Get all notes WITH their tags
    @Transaction
    @Query("SELECT * FROM notes ORDER BY updated_at DESC")
    fun getAllNotesWithTags(): Flow<List<NoteWithTags>>

    @Update
    suspend fun updateTag(tag: Tag)

    @Delete
    suspend fun deleteTag(tag: Tag)
}
