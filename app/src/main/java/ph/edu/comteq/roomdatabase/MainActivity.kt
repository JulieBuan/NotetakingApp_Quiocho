@file:OptIn(ExperimentalLayoutApi::class)

package ph.edu.comteq.roomdatabase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ph.edu.comteq.roomdatabase.ui.theme.RoomDataBaseTheme




class MainActivity(val notesWithTags: Int) : ComponentActivity() {
    private val  viewModel:NoteViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RoomDataBaseTheme {
                var searchQuery by remember { mutableStateOf("") }
                var isSearchActive by remember {mutableStateOf(false)}
                val notes by viewModel.allNotes.collectAsState(emptyList())

                Scaffold(modifier = Modifier.fillMaxSize(),
                    topBar={
                        if(isSearchActive){
                            SearchBar(
                                modifier = Modifier.fillMaxWidth(),
                                inputField = {
                                    SearchBarDefaults.InputField(
                                        query = searchQuery,
                                        onQueryChange = {
                                            searchQuery = it
                                            viewModel.updateSearchQuery(it)
                                        },
                                        onSearch = {},
                                        expanded = true,
                                        onExpandedChange = { shouldExpand -> // This is called when the system wants to change expanded state
                                            if (!shouldExpand) {
// User wants to collapse/exit search
                                                isSearchActive = false
                                                searchQuery = ""
                                                viewModel.clearSearch()
                                            }
                                        },
                                        placeholder = {Text("Search notes...")},
                                        leadingIcon = {
                                            IconButton(onClick = {
                                                isSearchActive = false
                                                searchQuery = ""
                                                viewModel.clearSearch()
                                            }) {
                                                Icon(
                                                    Icons.Default.ArrowBack,
                                                    contentDescription = "Close search"
                                                )
                                            }
                                        },
                                        trailingIcon = {
                                            if (searchQuery.isNotEmpty()) {
                                                IconButton(onClick = {
                                                    searchQuery = ""
                                                    viewModel.clearSearch()
                                                }) {
                                                    Icon(
                                                        Icons.Default.Clear,
                                                        contentDescription = "Clear search"
                                                    )
                                                }
                                            }
                                        }
                                    )
                                },
                                expanded = true,
                                onExpandedChange = {
                                    if(!it){
                                        isSearchActive = false
                                        searchQuery = ""
                                        viewModel.clearSearch()
                                    }
                                }
                            ){
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    contentPadding = PaddingValues(16.dp)
                                ) {
                                    if (notes.isEmpty()) {
                                        item {
                                            Text(
                                                text = "No notes found",
                                                modifier = Modifier.padding(16.dp),
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                            )
                                        }
                                    }else {
                                        items(notesWithTags) { note ->
                                            NoteCard(note = note.note, tags = note.tags)
                                        }
                                    }
                                }
                            }
                        }else{
                            TopAppBar(
                                title = { Text("Notes") },
                                actions = {
                                    IconButton(onClick = {
                                        isSearchActive = true // Switch to search mode!
                                    }) {
                                        Icon(Icons.Filled.Search, contentDescription = "Search")
                                    }
                                }
                            )
                        }


                    },
                    floatingActionButton = {
                        FloatingActionButton(onClick = { /*TODO()*/ }) {
                            Icon(Icons.Filled.Add, "Add Note")
                        }
                    }
                )
                { innerPadding -> NoteListScreen(
                    viewModel = viewModel,
                    modifier = Modifier.padding(innerPadding)
                )
                }
            }
            }
        }
    }


@Composable
fun NoteListScreen(viewModel: NoteViewModel, modifier: Modifier = Modifier){
    val notesWithTags by viewModel.allNotesWithtags.collectAsState(initial = emptyList())

    LazyColumn (modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ){if (notesWithTags.isEmpty()) {
        item {
            Text(
                text = "No notes found",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}
@Composable
fun NoteCard(
    note: Note,
    tags: List<Tag> = emptyList(),  // NEW: Optional tags list
    modifier: Modifier = Modifier

) {
    Card (
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ){
        Column (
            modifier = Modifier.padding(16.dp)
        ){
            Text(
                text = DateUtils.formatDateTime(note.createdAt),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Text(
                text = note.category,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            )
            Text(
                text = note.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            //tags
            if (tags.isNotEmpty()){
                FlowRow {
                    tags.forEach {
                        Text(
                            text = it.name
                        )

                    }
                }
            }

        }
    }
}


//note_database  INSERT INTO notes(title, content, created_at) VALUES();