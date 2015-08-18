package test.service.servicetest.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.nostra13.universalimageloader.core.ImageLoader;
import test.service.servicetest.MainActivity;
import test.service.servicetest.R;

public class ListenActivityService extends Service
{
    NotificationManagerCompat notificationManager;
    int maxNotificationNumber = 10;
    int notificationNumber = 0;
    public ListenActivityService()
    {
        Log.i("Constructor", "Service Constructor");
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate()
    {
        notificationManager = NotificationManagerCompat.from(getApplicationContext());
        Log.i("onCreate", "Service onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.i("onStartCommand", "Service onStartCommand");
        new LoadImageAsyncTask().execute("http://icollect.tw/image/1/1439384274.jpg");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy()
    {
        Log.i("onDestroy", "Service onDestroy");
        super.onDestroy();
    }

    class LoadImageAsyncTask extends AsyncTask<String, Void, Bitmap>
    {
        @Override
        protected Bitmap doInBackground(String... params)
        {
            String uri = params[0];
            Bitmap image = ImageLoader.getInstance().loadImageSync(uri);
            return image;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap)
        {
            notificationNumber = notificationNumber % 10;
            NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
            //expanded title
            bigPictureStyle.setSummaryText("嘻哈雙男神朴宰範、Gray合體來台舉辦\n" +
                    "「2015 AOMG TAIPEI Fanmeeting」見面會！\n" +
                    "8月15日與台灣粉絲提前過七夕！\n");
            bigPictureStyle.bigPicture(bitmap);

            Intent intent = new Intent(ListenActivityService.this , MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(ListenActivityService.this, 0, intent, 0);

            Notification notification = new NotificationCompat.Builder(getApplicationContext())
                    .setAutoCancel(true)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("2015 AOMG TAIPEI Fanmeeting")
                    //close title
                    .setContentText("嘻哈雙男神朴宰範、Gray合體來台舉辦\n" +
                            "「2015 AOMG TAIPEI Fanmeeting」見面會！\n" +
                            "8月15日與台灣粉絲提前過七夕！\n")
                    .setStyle(bigPictureStyle)
                    .setLargeIcon(bitmap)
                    .setContentIntent(pendingIntent)
                    .setPriority(Notification.PRIORITY_MAX)
                    .build();
            notificationManager.notify(notificationNumber, notification);
            notificationNumber++;
        }
    }
}
