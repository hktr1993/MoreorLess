package hikaru.moreorless.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import hikaru.moreorless.API.TwitchAPIHelper;
import hikaru.moreorless.Database.DBHelper;
import hikaru.moreorless.Model.Stream.Channel;
import hikaru.moreorless.Model.Streamer;
import hikaru.moreorless.R;
import hikaru.moreorless.Util.ButtonShift;
import hikaru.moreorless.Util.ShiftingAnimation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.graphics.Color.rgb;

public class SecondaryActivity extends AppCompatActivity implements View.OnClickListener {

    String streamerPic1, streamerPic2, streamerName1, streamerName2, oldStreamer;
    String imgsrc = null;
    int streamerFollows1;
    int streamerFollows2;
    Integer whichOne;
    ImageView imageView, imageView2, imageView3, correct, incorrect;
    TextView follows, follows2, name, name2, scoreNumber;
    List<Streamer> streamers;
    int score = 0;
    int game_repeat;
    SharedPreferences preferences;
    Streamer streamer = new Streamer(), randomStreamerTop, randomStreamerBottom, randomStreamerNext;
    public Integer followNumberTop, followNumberBottom;
    Handler handler;
    TwitchAPIHelper api;
    ButtonShift morphingButton;
    InterstitialAd ad = new InterstitialAd(SecondaryActivity.this);
    Boolean status = true, itsMore = true, itsLess = true; //more is true, less is false

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.secondary);

        streamerPic1 = getIntent().getStringExtra("pic1");
        System.out.println(getIntent().getStringExtra("pic1"));
        streamerName1 = getIntent().getStringExtra("name1");
        System.out.println(getIntent().getStringExtra("name1"));
        streamerFollows1 = getIntent().getIntExtra("follow1", 0);
        System.out.println(getIntent().getIntExtra("follow1", 0));
        streamerPic2 = getIntent().getStringExtra("pic2");
        System.out.println(getIntent().getStringExtra("pic2"));
        streamerName2 = getIntent().getStringExtra("name2");
        System.out.println(getIntent().getStringExtra("name2"));
        streamerFollows2 = getIntent().getIntExtra("follow2", 0);
        System.out.println(getIntent().getIntExtra("follow2", 0));

        DBHelper db = new DBHelper(this);
        streamers = db.getStreamers(50);


            followNumberTop = streamerFollows1;
            followNumberBottom = streamerFollows2;


        preferences = PreferenceManager.getDefaultSharedPreferences(getApplication());
        System.out.println(preferences.getInt("game_repeat", 0));
        SharedPreferences.Editor editor = preferences.edit();
        if(preferences.contains("game_repeat")){
            int old_num = preferences.getInt("game_repeat", 0);
            int new_num = ++old_num;
            editor.putInt("game_repeat", new_num);
            editor.apply();
        } else {
            editor.putInt("game_repeat", 1);
            editor.apply();
        }

        morphingButton = (ButtonShift) findViewById(R.id.morph);
        morphingButton.setEnabled(false);

        imageView = (ImageView) findViewById(R.id.image);
        imageView.setColorFilter(rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);
        imageView2 = (ImageView) findViewById(R.id.image2);
        imageView2.setColorFilter(rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);
        //imageView3 = (ImageView) findViewById(R.id.image3);

        Glide.with(getApplication())
                .load(streamerPic1)
                .into(imageView);

        Glide.with(getApplication())
                .load(streamerPic2)
                .into(imageView2);

        /*PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences mSettings = getPreferences().getSharedPreferences("Settings", 0);

        TextView best = (TextView) findViewById(R.id.best);
        best.setText();*/

        name = (TextView) findViewById(R.id.name);
        name.setText(streamerName1);

        name2 = (TextView) findViewById(R.id.name2);
        name2.setText(streamerName2);

        follows = (TextView) findViewById(R.id.follows);
        follows.setText(NumberFormat.getNumberInstance().format(streamerFollows1));

        follows2 = (TextView) findViewById(R.id.follows2);

        api = new TwitchAPIHelper();

        correct = (ImageView) findViewById(R.id.correct);
        incorrect = (ImageView) findViewById(R.id.incorrect);

        handler = new Handler();

        scoreNumber = (TextView) findViewById(R.id.scoreNumber);
        scoreNumber.setText(R.string.scoreNumInitial);

        imageView.setOnClickListener(this);

        imageView2.setOnClickListener(this);

        if(preferences.getInt("game_repeat", 0) >= 5){
            requestNewInterstitial();
            editor.putInt("game_repeat", 1);
            editor.apply();
        }
    }

    public void setScore(String bool){
        if(bool.equals("correct")) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    score++;
                    scoreNumber.setText("Score: " + score);
                }
            }, 1000);
        } else {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplication());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if (!sharedPreferences.contains("best") || sharedPreferences.getInt("best", 0) < score) {
                editor.putInt("best", score);
                editor.apply();
            }
        }
    }

    public void followNumberReveal(){
        if (follows.getText().toString().equals("???")) {
            revealFollowersNumber(followNumberTop, follows);
        } else {
            revealFollowersNumber(followNumberBottom, follows2);
        }
    }

    public void morphToResult(final String bool, final View v) {
        final int color;
        if(bool.equals("correct")) {
            color = rgb(76, 175, 80);
        } else {
            color = rgb(221,44,0);
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ButtonShift.Params morph = ButtonShift.Params.create()
                        .duration(300)
                        .cornerRadius((int) getResources().getDimension(R.dimen.morph_size))
                        .width((int) getResources().getDimension(R.dimen.morph_size))
                        .animationListener(new ShiftingAnimation.Listener() {
                            @Override
                            public void onAnimationEnd() {
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        handleNextResult(v);
                                    }
                                }, 600);

                            }
                        })
                        .height((int) getResources().getDimension(R.dimen.morph_size))
                        .color(color)
                        .icon((bool.equals("correct") ? R.drawable.ic_check : R.drawable.ic_clear));
                morphingButton.morph(morph);
                if(bool.equals("incorrect")) {
                    if(preferences.getInt("highscore", 0) < score) {
                        int high_score = preferences.getInt("high_score", 0);

                    }
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(ad.isLoaded()){
                                ad.show();
                            } else {
                                Intent i = new Intent(SecondaryActivity.this, EndingActivty.class);
                                i.putExtra("score", score);
                                startActivity(i);
                            }
                        }
                    }, 600);

                    ad.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();
                            Intent i = new Intent(SecondaryActivity.this, EndingActivty.class);
                            i.putExtra("score", score);
                            startActivity(i);
                        }
                    });
                }
            }
        }, 700);
    }

    public void handleNextResult(final View v){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            randomStreamerNext = streamers.get(new Random().nextInt(streamers.size() - 1) + 1);
        }
        else {
            randomStreamerNext = streamers.get(new Random().nextInt(streamers.size() - 1) + 1);

        }
        System.out.println("Next streamer: " + randomStreamerNext.getFollows());
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Glide.with(getApplication())
                        .load(randomStreamerNext.getImage())
                        .into((whichOne.equals(followNumberBottom)) ? imageView : imageView2);

                if(whichOne.equals(followNumberBottom)) {
                    name.setText(randomStreamerNext.getName());

                        followNumberTop = randomStreamerNext.getFollows();
                        if(followNumberTop.equals(followNumberBottom)){
                            ButtonShift.Params morph = ButtonShift.Params.create()
                                    .duration(300)
                                    .cornerRadius(150)
                                    .width(250)
                                    .height(150)
                                    .color(rgb(225, 255, 255))
                                    .text("=");
                            morphingButton.morph(morph);
                        }

                }
                else {
                    name2.setText(randomStreamerNext.getName());

                        followNumberBottom = randomStreamerNext.getFollows();
                        if(followNumberBottom.equals(followNumberTop)){
                            ButtonShift.Params morph = ButtonShift.Params.create()
                                    .duration(300)
                                    .cornerRadius(250)
                                    .width(250)
                                    .height(250)
                                    .color(rgb(221, 255, 255))
                                    .text("=");
                            morphingButton.morph(morph);
                        }

                }

                correct.setVisibility(View.GONE);
                if (whichOne.equals(followNumberBottom)) {
                    follows.setText(getResources().getText(R.string.unknown));
                } else {
                    follows2.setText(getResources().getText(R.string.unknown));
                }
                ButtonShift.Params morph = ButtonShift.Params.create()
                        .duration(300)
                        .height(200)
                        .width(800)
                        .cornerRadius(250)
                        .color((rgb(255, 255, 255)))
                        .text((status) ? "Who has less?" : "Who has more?");
                morphingButton.morph(morph);
                status = !status;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        v.setClickable(true);
                    }
                }, 350);
            }
        }, 300);
    }

    @Override
    public void onClick(View v) {
        v.setClickable(false);
        if(status) {
            switch (v.getId()) {
                case R.id.image:
                    if (followNumberBottom < followNumberTop) {
                        System.out.println("Bottom number: " + followNumberBottom);
                        System.out.println("Top number: " + followNumberTop);
                        System.out.println("correct");

                        followNumberReveal();

                        setScore("correct");

                        morphToResult("correct", v);

                    } else if (followNumberBottom.equals(followNumberTop)) {

                        followNumberReveal();



                    } else {
                        followNumberReveal();

                        setScore("incorrect");

                        morphToResult("incorrect", v);

                        System.out.println("incorrect");

                        //finish();
                    }
                    break;
                case R.id.image2:
                    if (followNumberBottom > followNumberTop) {
                        System.out.println("Bottom number: " + followNumberBottom);
                        System.out.println("Top number: " + followNumberTop);
                        System.out.println("correct");
                        followNumberReveal();

                        setScore("correct");

                        morphToResult("correct", v);


                    } else if (followNumberBottom.equals(followNumberTop)) {

                    } else {
                        System.out.println("incorrect");
                        followNumberReveal();

                        setScore("incorrect");

                        morphToResult("incorrect", v);

                        //finish();
                    }
                    break;
            }
        } else {
            switch (v.getId()) {
                case R.id.image:
                    if (followNumberBottom > followNumberTop) {
                        System.out.println("Bottom number: " + followNumberBottom);
                        System.out.println("Top number: " + followNumberTop);
                        System.out.println("correct");

                        followNumberReveal();

                        setScore("correct");

                        morphToResult("correct", v);


                    } else if (followNumberBottom.equals(followNumberTop)) {

                    } else {
                        System.out.println("incorrect");
                        followNumberReveal();

                        setScore("incorrect");

                        morphToResult("incorrect", v);

                        //finish();
                    }
                    break;
                case R.id.image2:
                    if (followNumberBottom < followNumberTop) {
                        System.out.println("Bottom number: " + followNumberBottom);
                        System.out.println("Top number: " + followNumberTop);
                        System.out.println("correct");
                        followNumberReveal();

                        setScore("correct");

                        morphToResult("correct", v);


                    } else if (followNumberBottom.equals(followNumberTop)) {

                    } else {
                        System.out.println("incorrect");
                        followNumberReveal();

                        setScore("incorrect");

                        morphToResult("incorrect", v);

                        //finish();
                    }
                    break;
            }
        }
    }

    private void revealFollowersNumber(int endValue, final TextView view) {
        ValueAnimator animator = new ValueAnimator();
        animator.setObjectValues(0, endValue);
        animator.setDuration(700);
        animator.setInterpolator( new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                view.setText(NumberFormat.getNumberInstance().format((int) animation.getAnimatedValue()));
            }
        });
        animator.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                try {
                    whichOne = NumberFormat.getNumberInstance().parse(String.valueOf(view.getText())).intValue();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                System.out.println(whichOne);
            }
        });
        animator.start();

    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("48F6D9389E9038E2AC42D4077C20EFAD")
                .build();
        ad.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        ad.loadAd(adRequest);
    }
}
