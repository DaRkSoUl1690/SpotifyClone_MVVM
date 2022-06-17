package com.example.spotifyclone_mvvm.exoplayer

import android.support.v4.media.MediaMetadataCompat
import com.example.spotifyclone_mvvm.data.remote.MusicDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirebaseMusicSource @Inject constructor(
    private val musicDatabase: MusicDatabase
){

    var songs = emptyList<MediaMetadataCompat>()

    suspend fun fetchMediaData() = withContext(Dispatchers.IO) {

    }

    private val onReadyListeners = mutableListOf<(Boolean) -> Unit>()


    @OptIn(InternalCoroutinesApi::class)
    private var state: State = State.STATE_CREATED
        set(value) {
            if (value == State.STATE_INITIALIZED || value == State.STATE_ERROR) {
                synchronized(onReadyListeners)
                {
                    field = value
                    onReadyListeners.forEach { listener ->
                        listener(
                            state == State.STATE_INITIALIZED
                        )

                    }
                }
            }
            else
            {
                field = value
            }
        }

    fun whenReady(action : (Boolean) -> Unit) : Boolean{
        return if(state == State.STATE_CREATED || state == State.STATE_INITIALIZING) {
            onReadyListeners+=action
            false
        }else {
            action(state == State.STATE_INITIALIZED)
            true
        }

    }
}

enum class State {
    STATE_CREATED,
    STATE_INITIALIZING,
    STATE_INITIALIZED,
    STATE_ERROR
}