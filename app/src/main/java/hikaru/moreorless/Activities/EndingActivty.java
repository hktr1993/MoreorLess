package hikaru.moreorless.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.jinatonic.confetti.ConfettiManager;
import com.github.jinatonic.confetti.ConfettiSource;
import com.github.jinatonic.confetti.ConfettoGenerator;
import com.github.jinatonic.confetti.confetto.BitmapConfetto;
import com.github.jinatonic.confetti.confetto.Confetto;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.NativeExpressAdView;
import com.wang.avi.AVLoadingIndicatorView;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import hikaru.moreorless.API.TwitchAPIHelper;
import hikaru.moreorless.Database.DBHelper;
import hikaru.moreorless.Model.Stream.Channel;
import hikaru.moreorless.Model.Streamer;
import hikaru.moreorless.R;
import hikaru.moreorless.RevealLayout;
import hikaru.moreorless.Util.ButtonShift;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.graphics.Color.WHITE;

public class EndingActivty extends AppCompatActivity {

    int score, best;
    ViewGroup container;
    RelativeLayout section;
    private int size, size1, size2, size3;
    List<Streamer> streamers;
    AVLoadingIndicatorView avi;
    Intent i;
    Streamer streamer = new Streamer(), randomStreamerTop, randomStreamerBottom, randomStreamerNext;
    String followNumberTop;
    String followNumberBottom;
    RevealLayout mRevealLayout;
    View mRevealView;
    ButtonShift morphingButton;
    InterstitialAd interstitialAd;
    NativeExpressAdView adView;
    public static String PACKAGE_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ending);

        adView = (NativeExpressAdView) findViewById(R.id.adView);

        AdRequest request = new AdRequest.Builder()
                .addTestDevice("48F6D9389E9038E2AC42D4077C20EFAD")
                .build();
        adView.loadAd(request);

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");

        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }
        });

        ImageButton rate = (ImageButton) findViewById(R.id.rate);
        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    PACKAGE_NAME = getApplicationContext().getPackageName();
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=" + PACKAGE_NAME)));
                } catch (android.content.ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + PACKAGE_NAME)));
                }

            }
        });

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplication());
        System.out.println("best score" + sharedPreferences.getInt("best", 0));

        requestNewInterstitial();

        DBHelper db = new DBHelper(this);
        streamers = db.getStreamers(150);
        for (int i = 0; i < streamers.size(); i++) {
            streamers.get(i).getName();
        }

        mRevealLayout = (RevealLayout) findViewById(R.id.reveal_layout);
        mRevealView = findViewById(R.id.reveal_view);

        mRevealLayout.bringToFront();

        morphingButton = (ButtonShift) findViewById(R.id.play_again);

        avi = (AVLoadingIndicatorView) findViewById(R.id.loader);
        avi.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                i = new Intent(EndingActivty.this, SecondaryActivity.class);
                i.putExtra("pic1", randomStreamerTop.getImage());
                i.putExtra("follow1", randomStreamerTop.getFollows());
                i.putExtra("name1", randomStreamerTop.getName());
                i.putExtra("pic2", randomStreamerBottom.getImage());
                i.putExtra("follow2", randomStreamerBottom.getFollows());
                i.putExtra("name2", randomStreamerBottom.getName());
                //play.setEnabled(true);
                avi.hide();
                morphingButton.setEnabled(true);
            }
        }, 1000);

        DecimalFormat decimalFormat = new DecimalFormat("#,###,###");
        randomStreamerTop = streamers.get(new Random().nextInt(streamers.size() - 1) + 1);
        randomStreamerBottom = streamers.get(new Random().nextInt(streamers.size() - 1) + 1);

        followNumberTop = NumberFormat.getNumberInstance().format(randomStreamerTop.getFollows());
        followNumberBottom = NumberFormat.getNumberInstance().format(randomStreamerBottom.getFollows());

        morphingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ButtonShift.Params morph = ButtonShift.Params.create()
                        .duration(300)
                        .cornerRadius(200)
                        .width(200)
                        .height(200)
                        .color(WHITE)
                        .icon(R.drawable.ic_refresh);
                morphingButton.morph(morph);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        morphingButton.animate().scaleX(0f).setDuration(400).start();
                        morphingButton.animate().scaleY(0f).setDuration(400).start();
                        morphingButton.animate().rotation(2f).setDuration(400).start();
                    }
                }, 500);

                final int[] location = new int[2];
                morphingButton.getLocationOnScreen(location);
                location[0] += morphingButton.getWidth() / 2;
                location[1] += morphingButton.getHeight() / 4;

                morphingButton.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRevealView.setVisibility(View.VISIBLE);
                        mRevealLayout.setVisibility(View.VISIBLE);
                        mRevealLayout.show(location[0], location[1], 1000);

                        morphingButton.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(i);

                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            }
                        }, 1500);
                    }
                }, 860);
            }
        });

        TextView scoreView = (TextView) findViewById(R.id.score);

        score = getIntent().getIntExtra("score", 0);
        best = sharedPreferences.getInt("best", 0);
        if(best < score){
            TextView bestView = (TextView) findViewById(R.id.high);
            bestView.setText("New High Score: " + best);
            scoreView.setVisibility(View.GONE);
        } else if(best == score){
            //TextView bestView = (TextView) findViewById(R.id.high);
            //bestView.setText("High Score: " + best);
        } else {
            TextView bestView = (TextView) findViewById(R.id.high);
            bestView.setText("High Score: " + best);

        }


        TextView scoreTextView = (TextView) findViewById(R.id.score_text);
        scoreTextView.setText("Score");
        scoreView.setText(Integer.toString(score));

        container = (ViewGroup) findViewById(R.id.activity_ending_);
        section = (RelativeLayout) findViewById(R.id.elevated);
        ViewCompat.setElevation(section, 4);

        /*String urlToShare = "http://stackoverflow.com/questions/7545254";
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
// intent.putExtra(Intent.EXTRA_SUBJECT, "Foo bar"); // NB: has no effect!
        intent.putExtra(Intent.EXTRA_TEXT, urlToShare);

// See if official Facebook app is found
        boolean facebookAppFound = false;
        List<ResolveInfo> matches = getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith("com.facebook.katana")) {
                intent.setPackage(info.activityInfo.packageName);
                facebookAppFound = true;
                break;
            }
        }

// As fallback, launch sharer.php in a browser
        if (!facebookAppFound) {
            String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + urlToShare;
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
        }

        startActivity(intent);*/

        final List<Bitmap> allPossibleConfetti = new ArrayList<>();
        if(score > 5){
            allPossibleConfetti.add(BitmapFactory.decodeResource(getResources(), R.drawable.pogchamp));
            allPossibleConfetti.add(BitmapFactory.decodeResource(getResources(), R.drawable.kreygasm));
            allPossibleConfetti.add(BitmapFactory.decodeResource(getResources(), R.drawable.heyguys));
            allPossibleConfetti.add(BitmapFactory.decodeResource(getResources(), R.drawable.doritos));
        } else {
            allPossibleConfetti.add(BitmapFactory.decodeResource(getResources(), R.drawable.pjsalt));
            allPossibleConfetti.add(BitmapFactory.decodeResource(getResources(), R.drawable.failfish));
            allPossibleConfetti.add(BitmapFactory.decodeResource(getResources(), R.drawable.notlikethis));
            allPossibleConfetti.add(BitmapFactory.decodeResource(getResources(), R.drawable.wutface));
        }
// Alternatively, we provide some helper methods inside `Utils` to generate square, circle,
// and triangle bitmaps.
// Utils.generateConfettiBitmaps(new int[] { Color.BLACK }, 20 /* size */);

        final int numConfetti = allPossibleConfetti.size();
        final ConfettoGenerator confettoGenerator = new ConfettoGenerator() {
            @Override
            public Confetto generateConfetto(Random random) {
                final Bitmap bitmap = allPossibleConfetti.get(random.nextInt(numConfetti));
                return new BitmapConfetto(bitmap);
            }
        };
        final Resources res = getResources();

        size = res.getDimensionPixelSize(R.dimen.default_confetti_size);
        size1 = res.getDimensionPixelSize(R.dimen.default_velocity_slow);
        size2 = res.getDimensionPixelSize(R.dimen.default_velocity_normal);
        size3 = res.getDimensionPixelSize(R.dimen.default_velocity_fast);
        final int containerMiddleX = 550;
        final int containerMiddleY = container.getHeight();
        System.out.println(containerMiddleX + containerMiddleY);
        final ConfettiSource source = new ConfettiSource(0, -200, 1500, -200);

        Handler handler2 = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                new ConfettiManager(EndingActivty.this, confettoGenerator, source, container)
                        .setEmissionDuration(ConfettiManager.INFINITE_DURATION)
                        .setEmissionRate(4)
                        .setVelocityX(0, size1)
                        .setVelocityY(size2, size1)
                        .setTouchEnabled(true)
                        .animate();
            }
        },100);

    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .build();

        interstitialAd.loadAd(adRequest);
    }
}
