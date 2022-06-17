package com.example.spotifyclone_mvvm.data.remote

import com.example.spotifyclone_mvvm.data.entities.Song
import com.example.spotifyclone_mvvm.other.Constants.SONG_COLLECTION
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class MusicDatabase {

    private val fireStore = FirebaseFirestore.getInstance()
    private val songCollection  = fireStore.collection(SONG_COLLECTION)

    suspend fun getAllSongs() : List<Song> {
        return try{
            songCollection.get().await().toObjects(Song::class.java)
        }
        catch (e : Exception) {
            emptyList()
        }
    }

}