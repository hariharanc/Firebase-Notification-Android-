/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package firebase.com.firebasetest;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        // Log.d(TAG, "From eseerfere: " + remoteMessage.getFrom());
        //Log.d(TAG, "Message data payload: " + remoteMessage.getNotification().getBody());


        /***
         *@description::
         * Data messages are handled
         * here in onMessageReceived whether the app is in the foreground or background
         * Data message are handled custom key and value
         *@see data message json format
         * Header Filed::
         * Content-Type:application/json
         * Authorization:key=AIzaSyB9IEv_M6AEHiizWAmYjE2PqsPtBQQaY9s
         *
         * Raw Data::
         * {
        "data" : {
        "title":"Notification",
        "message": "welcome"
        },
        "to" : "e1Pp1hWD6ys:APA91bFdtxXfBW0jsJUDgVVeHfwuokLK6Rdm0SR9qfdG1u2ihAYds7jZ_e4vXq8V8CVBtaI-9iqk3fnv4g2p52H6Zu1W7cIGfvXVymG9JuD4GTdmRQ5UZWDrWWyqfNvpnTU12IRatQbh"
        }
         */


        Log.i(TAG, "Data message payload is::" + remoteMessage.getData().isEmpty());
        if (!remoteMessage.getData().isEmpty()) {
            String title = remoteMessage.getData().get("title");
            String message = remoteMessage.getData().get("message");
            Log.d(TAG, "Data Title is: " + title);
            Log.d(TAG, "Data Message is: " + message);
            sendNotification(message);
        }


        /***
         *@description::
         *  Notification messages are only received here in onMessageReceived when the app
         *is in the foreground. When the app is in the background an automatically generated notification is displayed.
         *When the user taps on the notification they are returned to the app. Messages containing both notification
         *and data payloads are treated as notification messages.
         *@see notification message json format
         * Header Filed::
         * Content-Type:application/json
         * Authorization:key=AIzaSyB9IEv_M6AEHiizWAmYjE2PqsPtBQQaY9s
         *
         * Raw Data::
         *  {
         *"to" : "flyB_zeDgo4:APA91bHZcxc3sUiWSM5Y4rrwD_sPbyRRLyA0mUWHZ2BzRLPdl2Bro81kKA8UVdia9Lxx7k7OmnkDtDGGSko4aoCzspSjAvlkltLYacZTMhzXVV5fuRAurRga2S2VMmHOn2CpS4JWGI_T",
         "notification" : {
        "body" : "App background",
        "title" : "Portugal vs. Denmark",
        "icon" : "myicon"
        }
        }
         */

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage.getNotification().getBody());
        }

    }


    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setContentTitle("FCM Message")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
