package hr.fer.ruazosa.networkquiz.net

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import hr.fer.ruazosa.networkquiz.JoinGameActivity
import hr.fer.ruazosa.networkquiz.R
import kotlin.random.Random

class TriviagoFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")
            val action = remoteMessage.data["action"]
            if(action.equals("join")){
                val message = remoteMessage.data["message"]
                val gameId = remoteMessage.data["game_id"]

                sendNotification(message!!, gameId!!)
            }
            else if(action.equals("begin")){
                val gameId = remoteMessage.data["game_id"]
                val intent = Intent("begin")
                intent.putExtra("game_id", gameId)
                LocalBroadcastManager.getInstance(baseContext)
                    .sendBroadcast(intent)
            }
            else if(action.equals("winner")){
                val username = remoteMessage.data["username"]
                val score = remoteMessage.data["score"]
                val intent = Intent("winner")
                intent.putExtra("username", username)
                intent.putExtra("score", score)
                LocalBroadcastManager.getInstance(baseContext)
                    .sendBroadcast(intent)
            }
            else if(action.equals("stop")) {
                val username = remoteMessage.data["username"]
                val score = remoteMessage.data["score"]
                val intent = Intent("stop")
                LocalBroadcastManager.getInstance(baseContext)
                    .sendBroadcast(intent)
            }

        }
    }

    private fun sendNotification(message: String, gameId: String){
        val intent = Intent(this, JoinGameActivity::class.java)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationID = Random.nextInt()

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotificationChannel(notificationManager)
        }

        intent.putExtra("message", message)
        intent.putExtra("game_id", gameId)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, FLAG_ONE_SHOT)
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.network_quiz_icon_round)
            .setContentTitle("New game available")
            .setContentText(message)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(notificationID, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager){
        val channelName = "channelName"
        val channel = NotificationChannel(CHANNEL_ID, channelName, IMPORTANCE_HIGH).apply {
            description = "Notify new game available"
            enableLights(true)
        }
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
        private const val CHANNEL_ID = "join_game"
    }
}