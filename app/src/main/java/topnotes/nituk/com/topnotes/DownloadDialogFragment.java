package topnotes.nituk.com.topnotes;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class DownloadDialogFragment extends Dialog_fragment {

    private String notificationChannelId = "DOWNLOAD_NOTIFICATION";
    private int notificationId = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //it will remove title bar from the dialog
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setWindowAnimations(R.style.dialog_animation_fade);

        View view = inflater.inflate(R.layout.download_dialog_fragment, container, false);
        Button downloadButton = view.findViewById(R.id.downloadButton);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDownloadNotification(); //show notification to user and track the download progress

                // Download the content
                Toast.makeText(getActivity(), "::Download process begins::", Toast.LENGTH_SHORT).show();
                new ContentDownloader(getActivity());
            }
        });

        return view;

    }

    public DownloadDialogFragment() {
    }

    public static DownloadDialogFragment getInstance() {
        return new DownloadDialogFragment();
    }


    public void showDownloadNotification() {

        createNotificationChannel(); //notification won't work without this in android version above 8.0+

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getActivity(), notificationChannelId);
        //this channelid should be unique and it is used to track the notification

        Intent intent = new Intent(getActivity(),SubjectListActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, 0);

        notificationBuilder.setSmallIcon(R.drawable.ic_arrow_downward_black_24dp)
                .setContentTitle("Downloading in Progress...")
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setPriority(NotificationCompat.DEFAULT_ALL) //to support Android 7.1 and lower.
                .addAction(R.drawable.light_blue_color,"pause",pendingIntent)
                .addAction(R.drawable.light_blue_color,"Stop",pendingIntent)
                .addAction(R.drawable.light_blue_color,"cancel",pendingIntent)
                .setContentText("Downloading...")
                .setProgress(10,5,false);

        //to display notification
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getActivity());

        notificationManagerCompat.notify(notificationId, notificationBuilder.build());
        // to track the current notification

    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(notificationChannelId, "TopNotes", importance);
            channel.setDescription("none");
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}