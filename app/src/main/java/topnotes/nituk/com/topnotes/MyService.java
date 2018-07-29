package topnotes.nituk.com.topnotes;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.SystemClock;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class MyService extends IntentService {

        private static final String name = "MyService";
        private static final int REQUEST_CODE = 1;
        private static Context newContext;
        private static int interval_time = 900000;

        public MyService() {
            super(name);
        }

        public static Intent newIntent(Context context) {

            return new Intent(context, topnotes.nituk.com.topnotes.MyService.class);
        }

        @Override
        protected void onHandleIntent(Intent intent) {
            Log.i("Handling", "--------------------------Inside handling intent" + intent.toString());
            checkAnyChangeInFBDB();
        }

        public static void registerAlarm(Context context) {

            Log.i("register Alarm", "---------------------inside register alarm");
            newContext = context;
            Intent intent = topnotes.nituk.com.topnotes.MyService.newIntent(context);

            PendingIntent senderPendingIntent = PendingIntent.getService(context, REQUEST_CODE, intent, 0);

            // We want the alarm to go off 3 seconds from now.
            long firstTime = SystemClock.elapsedRealtime();
            //firstTime += 3 * 1000;//start 3 seconds after first register.

            // Schedule the alarm!
            AlarmManager alarmManager = (AlarmManager) context
                    .getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime,
                    interval_time, senderPendingIntent);//15min interval
        }

        private void createNotification() {

            NotificationCompat.Builder builder = new NotificationCompat.Builder(newContext);
            builder.setSmallIcon(R.drawable.notes);
            builder.setContentTitle("New Notes Uploaded");
            //builder.setContentText(getResources().getStringArray(R.array.subjectList)[choosenSubject] + getResources().getStringArray(R.array.subjectList)[choosenType] +" uploaded ..You can download it..");
            //builder.setContentText(subjectNameType+" "+notesNameType+" uploaded ..You can download it..");
            Uri defaultRingtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            builder.setSound(defaultRingtone);
            Intent intent = new Intent(newContext, SubjectListActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(newContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);
            NotificationManager notificationManager = (NotificationManager) newContext.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, builder.build());

        }

        private void checkAnyChangeInFBDB() {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("courses");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Log.i("InsideOnDataChange", dataSnapshot.getValue().toString());
                    createNotification();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            /*
            databaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    //Log.i("childadded", dataSnapshot.getValue().toString());

//                    for(DataSnapshot newDataSnapshot:dataSnapshot.getChildren()){
//                        Log.i("ChildAdded",newDataSnapshot.getValue().toString());
//                        break;
//                    }
//                    createNotification();
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });*/
        }
}