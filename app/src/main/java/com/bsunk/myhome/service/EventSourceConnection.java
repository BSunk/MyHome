package com.bsunk.myhome.service;

import android.content.ContentResolver;
import android.util.Log;
import com.tylerjroach.eventsource.EventSourceHandler;
import com.tylerjroach.eventsource.MessageEvent;

/**
 * Created by Bharat on 9/18/2016.
 */

public class EventSourceConnection implements EventSourceHandler {

    public EventSourceConnection() {
    }

    @Override
    public void onConnect() {
        Log.v("SSE Connected", "True");
    }

    @Override
    public void onMessage(String event, MessageEvent message) {
        Log.v("SSE Message", message.data);

    }

    @Override
    public void onComment(String comment) {
        //comments only received if exposeComments turned on
        Log.v("SSE Comment", comment);
    }

    @Override
    public void onError(Throwable t) {
        //ignore ssl NPE on eventSource.close()
    }

    @Override
    public void onClosed(boolean willReconnect) {
        Log.v("SSE Closed", "reconnect? " + willReconnect);
    }
}

