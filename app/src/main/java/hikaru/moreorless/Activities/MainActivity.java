package hikaru.moreorless.Activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import hikaru.moreorless.Database.DBHelper;
import hikaru.moreorless.Model.Streamer;
import hikaru.moreorless.R;
import hikaru.moreorless.RevealLayout;
import hikaru.moreorless.Util.ScalableVideoView;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    List<Streamer> streamers;
    AVLoadingIndicatorView avi;
    Intent i;
    Streamer streamer = new Streamer(), randomStreamerTop, randomStreamerBottom, randomStreamerNext;
    String followNumberTop;
    String followNumberBottom;
    SurfaceHolder surfaceHolder;
    ScalableVideoView videoView;
    MediaPlayer mMediaPlayer;
    Uri videoSrc;
    RevealLayout mRevealLayout;
    View mRevealView;
    String TAG = "tag";
    Random random = new Random();
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final ImageButton play = (ImageButton) findViewById(R.id.play);
        play.setEnabled(false);

        final DBHelper db = new DBHelper(this);

        streamers = db.getStreamers(150);
        for (int i = 0; i < streamers.size(); i++) {
            streamers.get(i).getName();
            streamers.get(i).getFollows();
            streamers.get(i).getImage();
        }

        System.out.println(streamers.size());

        randomStreamerTop = streamers.get(new Random().nextInt(streamers.size()));
        randomStreamerBottom = streamers.get(new Random().nextInt(streamers.size()));

        followNumberTop = NumberFormat.getNumberInstance(Locale.US).format(randomStreamerTop.getFollows());
        followNumberBottom = NumberFormat.getNumberInstance(Locale.US).format(randomStreamerBottom.getFollows());

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                i = new Intent(MainActivity.this, SecondaryActivity.class);
                i.putExtra("pic1", randomStreamerTop.getImage());
                i.putExtra("follow1", randomStreamerTop.getFollows());
                i.putExtra("name1", randomStreamerTop.getName());
                i.putExtra("pic2", randomStreamerBottom.getImage());
                i.putExtra("follow2", randomStreamerBottom.getFollows());
                i.putExtra("name2", randomStreamerBottom.getName());
                //play.setEnabled(true);
                avi.hide();
                play.setEnabled(true);
            }
        }, 1000);

        mRevealLayout = (RevealLayout) findViewById(R.id.reveal_layout);
        mRevealView = findViewById(R.id.reveal_view);

        //initialize the VideoView
        videoView = (ScalableVideoView) findViewById(R.id.bg);

        try {
            //set the uri of the video to be played
            ArrayList<String> videos = new ArrayList<>();
            videos.add("android.resource://hikaru.moreorless/raw/reckful");
            videos.add("android.resource://hikaru.moreorless/raw/soda");
            videos.add("android.resource://hikaru.moreorless/raw/sum");
            videos.add("android.resource://hikaru.moreorless/raw/tim");

            int index = random.nextInt(videos.size());
            String video = videos.get(index);
            videoView.setVideoURI(Uri.parse(video));

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        videoView.requestFocus();
        //we also set an setOnPreparedListener in order to know when the video file is ready for playback
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer mediaPlayer) {
                //if we have a position on savedInstanceState, the video playback should start from here
                videoView.start();
                mediaPlayer.setLooping(true);
                mediaPlayer.setVolume(0f, 0f);
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.setVolume(0f, 0f);
            }
        });

        avi = (AVLoadingIndicatorView) findViewById(R.id.loader);
        avi.show();

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play.animate().scaleX(0f).setDuration(150).start();
                play.animate().scaleY(0f).setDuration(150).start();
                final int[] location = new int[2];
                play.getLocationOnScreen(location);
                location[0] += play.getWidth() / 2;
                location[1] += play.getHeight() / 4;

                Handler handler = new Handler();

                play.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRevealView.setVisibility(View.VISIBLE);
                        mRevealLayout.setVisibility(View.VISIBLE);
                        mRevealLayout.show(location[0], location[1], 1000);

                        play.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                            }
                        }, 1100);

                        play.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(i);

                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            }
                        }, 1500);
                    }
                }, 160);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        //mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        /*if (mAuthListener != null) {
            mAuth.signOut();
            mAuth.removeAuthStateListener(mAuthListener);
        }*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoSrc = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*if (mAuthListener != null) {
            mAuth.signOut();
        }*/
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
