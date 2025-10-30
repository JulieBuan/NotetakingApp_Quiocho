package ph.edu.comteq.roomdatabase

import android.util.EventLogTags
import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class NoteWithTags(
    @Embedded
    val note: Note,

    @Relation(
        parentColumn = "id", //Note's id
        entityColumn = "id", //Tag's id
        associateBy = Junction(
            value = NoteTagCrossRef::class,
            parentColumn = "note_id",
            entityColumn = "tag_id"
        )
    )

    val tags:List<Tag>
)
