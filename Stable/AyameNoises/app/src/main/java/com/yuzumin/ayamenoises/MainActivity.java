package com.yuzumin.ayamenoises;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.yuzumin.ayamenoises.activities.AlarmActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements Count.CountListener{
    ImageView image;
    TextView textCount;
    TextView AppName;

    ImageView MenuBTN;
    ImageView AlarmBTN;

    MediaPlayer noise;
    MediaPlayer.OnCompletionListener listener;

    ObjectAnimator anim1, anim2, anim3;
    AnimatorSet set1, set2;

    SharedPreferences SavedSettings;

    boolean switch1; //IMPORTANT : TO DISABLE TOAST POPUP
    boolean switch2; //TO DISABLE CLICKER

    List<Integer> listofsounds;
    int charavalue;

    Integer DevCount=1;
    
    Count count;
    SharedPreferences sp;

    @Override
    public void OnUpdated(int count) {
        sp.edit().putInt("count", count).apply();
        textCount.setText(String.format("Count: %d", count));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        AppName=findViewById(R.id.AppName);

        MenuBTN=findViewById(R.id.menu_btn);
        MenuBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(MainActivity.this, AppMenu.class);
                startActivity(intent);
            }
        });

        AlarmBTN=findViewById(R.id.alarm_btn);
        AlarmBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(DevCount==7){
                    Intent intent;
                    intent = new Intent(MainActivity.this, AlarmActivity.class);
                    startActivity(intent);
                }
            }
        });


        SavedSettings =getSharedPreferences("save2",MODE_PRIVATE);
        switch2= SavedSettings.getBoolean("value2",true);

        listener = new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                cleanupMediaPlayer();
            }
        };

        image = findViewById(R.id.image);
        retrievedata();
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switch2){
                    count.increment();
                    startRandomSound();
                }
                else{
                    Toast.makeText(v.getContext(), "NOT ACTIVE", Toast.LENGTH_SHORT).show();
                }
            }
        });

        textCount = findViewById(R.id.count);


        anim1 = ObjectAnimator.ofFloat(image,"translationY", 0f,-50f, 50f, -50f, 50f, 0);
        anim1.setDuration(600);
        set1 = new AnimatorSet();
        ArrayList<Animator> setArray = new ArrayList<>();


        anim2 = ObjectAnimator.ofFloat(image,"translationY", 0f,-50f, 50f, -50f, 50f, -50f, 50f, -50f, 50f,0);
        anim2.setRepeatCount(ValueAnimator.INFINITE);
        setArray.add(anim2);
        anim2 = ObjectAnimator.ofFloat(image,"translationX", 0f,-70f, 70f, -70f, 70f, 0);
        anim2.setRepeatCount(ValueAnimator.INFINITE);
        setArray.add(anim2);
        set1.playTogether(setArray);
        set1.setDuration(2000);
        anim2 = null;


        set2 = new AnimatorSet();
        setArray = new ArrayList<>();
        anim3 = ObjectAnimator.ofFloat(image, "rotation", 0f, -90f, 0f, 90f, 180f, 270f, 360f);
        anim3.setRepeatCount(ValueAnimator.INFINITE);
        setArray.add(anim3);
        anim3 = ObjectAnimator.ofFloat(image, "scaleX", 1f,0.5f, 1.5f, 0.5f, 1.5f, 1f);
        anim3.setRepeatCount(ValueAnimator.INFINITE);
        setArray.add(anim3);
        anim3 = ObjectAnimator.ofFloat(image, "scaleY", 1f,.5f, 1.5f, 0.5f, 1.5f, 1f);
        anim3.setRepeatCount(ValueAnimator.INFINITE);
        setArray.add(anim3);
        set2.playTogether(setArray);
        set2.setDuration(2000);
        anim3 = null;
        count = new Count();
        count.setListener(this);

        sp = this.getSharedPreferences("count", MODE_PRIVATE);
        count.setCount(sp.getInt("count", 0));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.option,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case(R.id.Alarm):
                Intent intent;
                intent = new Intent(MainActivity.this, AlarmActivity.class);
                startActivity(intent);
                break;
            case(R.id.menu_btn):
                Intent settingmenu;
                settingmenu = new Intent(MainActivity.this, AppMenu.class);
                startActivity(settingmenu);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void stopAnimations(){
        if (anim1.isRunning())
            anim1.end();
        if (set1.isRunning())
            set1.end();
        if (set2.isRunning())
            set2.end();
    }

    private void cleanupMediaPlayer(){
        stopAnimations();
        if(noise!=null) {
            if(noise.isPlaying())
                noise.stop();
            noise.release();
            noise = null;
        }
    }

    private void startRandomSound() {

        if(listofsounds.isEmpty()){
            cleanupMediaPlayer();
            Toast.makeText(this, "NO SOUND SELECTED", Toast.LENGTH_SHORT).show();

        }else{
            int value=(int)(Math.random()*(listofsounds.size()));
            int num = listofsounds.get(value);
            cleanupMediaPlayer();


            switch (num) {
                case 3:
                    noise = MediaPlayer.create(this, R.raw.ayamenoise1);
                    noise.setOnCompletionListener(listener);
                    noise.start();
                    if (switch1) {
                        Toast.makeText(this, getString(R.string.noise1_text), Toast.LENGTH_SHORT).show();
                    }
                    set1.start();
                    break;
                case 4:
                    noise = MediaPlayer.create(this, R.raw.ayamenoise2);
                    noise.setOnCompletionListener(listener);
                    noise.start();
                    if (switch1) {
                        Toast.makeText(this, getString(R.string.noise2_text), Toast.LENGTH_SHORT).show();
                    }
                    set1.start();
                    break;
                case 5:
                    noise = MediaPlayer.create(this, R.raw.ayamenoise3);
                    noise.setOnCompletionListener(listener);
                    noise.start();
                    if (switch1) {
                        Toast.makeText(this, getString(R.string.noise3_text), Toast.LENGTH_SHORT).show();
                    }
                    set1.start();
                    break;
                case 6:
                    noise = MediaPlayer.create(this, R.raw.ayamenoise4);
                    noise.setOnCompletionListener(listener);
                    noise.start();
                    if (switch1) {
                        Toast.makeText(this, getString(R.string.noise4_text), Toast.LENGTH_SHORT).show();
                    }
                    set1.start();
                    break;
                case 7:
                    noise = MediaPlayer.create(this, R.raw.ayamenoise5);
                    noise.setOnCompletionListener(listener);
                    noise.start();
                    if (switch1) {
                        Toast.makeText(this, getString(R.string.noise5_text), Toast.LENGTH_SHORT).show();
                    }
                    set1.start();
                    break;
                case 8:
                    noise = MediaPlayer.create(this, R.raw.ayamenoise6);
                    noise.setOnCompletionListener(listener);
                    noise.start();
                    if (switch1) {
                        Toast.makeText(this, getString(R.string.noise6_text), Toast.LENGTH_SHORT).show();
                    }
                    set1.start();
                    break;
                case 9:
                    noise = MediaPlayer.create(this, R.raw.ayamenoise7);
                    noise.setOnCompletionListener(listener);
                    noise.start();
                    if (switch1) {
                        Toast.makeText(this, getString(R.string.noise7_text), Toast.LENGTH_SHORT).show();
                    }
                    set1.start();
                    break;
                case 10:
                    noise = MediaPlayer.create(this, R.raw.ayamenoise8);
                    noise.setOnCompletionListener(listener);
                    noise.start();
                    if (switch1) {
                        Toast.makeText(this, getString(R.string.noise8_text), Toast.LENGTH_SHORT).show();
                    }
                    set1.start();
                    break;
                case 11:
                    noise = MediaPlayer.create(this, R.raw.ayamenoise9);
                    noise.setOnCompletionListener(listener);
                    noise.start();
                    if (switch1) {
                        Toast.makeText(this, getString(R.string.noise9_text), Toast.LENGTH_SHORT).show();
                    }
                    set1.start();
                    break;
                case 12:
                    noise = MediaPlayer.create(this, R.raw.ayamenoise10);
                    noise.setOnCompletionListener(listener);
                    noise.start();
                    if (switch1) {
                        Toast.makeText(this, getString(R.string.noise10_text), Toast.LENGTH_SHORT).show();
                    }
                    set1.start();
                    break;
                case 13:
                    noise = MediaPlayer.create(this, R.raw.ayamenoise11);
                    noise.setOnCompletionListener(listener);
                    noise.start();
                    if (switch1) {
                        Toast.makeText(this, getString(R.string.noise11_text), Toast.LENGTH_SHORT).show();
                    }
                    set1.start();
                    break;
                case 14:
                    noise = MediaPlayer.create(this, R.raw.ayamenoise12);
                    noise.setOnCompletionListener(listener);
                    noise.start();
                    if (switch1) {
                        Toast.makeText(this, getString(R.string.noise12_text), Toast.LENGTH_SHORT).show();
                    }
                    set1.start();
                    break;
                case 15:
                    noise = MediaPlayer.create(this, R.raw.ayamenoise13);
                    noise.setOnCompletionListener(listener);
                    noise.start();
                    if (switch1) {
                        Toast.makeText(this, getString(R.string.noise13_text), Toast.LENGTH_SHORT).show();
                    }
                    set1.start();
                    break;
                case 16:
                    noise = MediaPlayer.create(this, R.raw.ayamenoise14);
                    noise.setOnCompletionListener(listener);
                    noise.start();
                    if (switch1) {
                        Toast.makeText(this, getString(R.string.noise14_text), Toast.LENGTH_SHORT).show();
                    }
                    set1.start();
                    break;
                case 17:
                    noise = MediaPlayer.create(this, R.raw.ayamenoise15);
                    noise.setOnCompletionListener(listener);
                    noise.start();
                    if (switch1) {
                        Toast.makeText(this, getString(R.string.noise15_text), Toast.LENGTH_SHORT).show();
                    }
                    set1.start();
                    break;
                case 18:
                    noise = MediaPlayer.create(this, R.raw.ayamenoise16);
                    noise.setOnCompletionListener(listener);
                    noise.start();
                    if (switch1) {
                        Toast.makeText(this, getString(R.string.noise16_text), Toast.LENGTH_SHORT).show();
                    }
                    set1.start();
                    break;
                case 19:
                    noise = MediaPlayer.create(this, R.raw.ayamenoise17);
                    noise.setOnCompletionListener(listener);
                    noise.start();
                    if (switch1) {
                        Toast.makeText(this, getString(R.string.noise17_text), Toast.LENGTH_SHORT).show();
                    }
                    set1.start();
                    break;
                case 20:
                    noise = MediaPlayer.create(this, R.raw.ayamenoise18);
                    noise.setOnCompletionListener(listener);
                    noise.start();
                    if (switch1) {
                        Toast.makeText(this, getString(R.string.noise18_text), Toast.LENGTH_SHORT).show();
                    }
                    set1.start();
                    break;
                case 21:
                    noise = MediaPlayer.create(this, R.raw.ayamenoise19);
                    noise.setOnCompletionListener(listener);
                    noise.start();
                    if (switch1) {
                        Toast.makeText(this, getString(R.string.noise19_text), Toast.LENGTH_SHORT).show();
                    }
                    set1.start();
                    break;
                case 22:
                    noise = MediaPlayer.create(this, R.raw.ayamenoise20);
                    noise.setOnCompletionListener(listener);
                    noise.start();
                    if (switch1) {
                        Toast.makeText(this, getString(R.string.noise20_text), Toast.LENGTH_SHORT).show();
                    }
                    set1.start();
                    break;
                case 23:
                    noise = MediaPlayer.create(this, R.raw.ayamenoise21);
                    noise.setOnCompletionListener(listener);
                    noise.start();
                    if (switch1) {
                        Toast.makeText(this, getString(R.string.noise21_text), Toast.LENGTH_SHORT).show();
                    }
                    set1.start();
                    break;
                case 24:
                    noise = MediaPlayer.create(this, R.raw.ayamenoise22);
                    noise.setOnCompletionListener(listener);
                    noise.start();
                    if (switch1) {
                        Toast.makeText(this, getString(R.string.noise22_text), Toast.LENGTH_SHORT).show();
                    }
                    set1.start();
                    break;
                case 25:
                    noise = MediaPlayer.create(this, R.raw.ayamenoise23);
                    noise.setOnCompletionListener(listener);
                    noise.start();
                    if (switch1) {
                        Toast.makeText(this, getString(R.string.noise23_text), Toast.LENGTH_SHORT).show();
                    }
                    set1.start();
                    break;
                case 26:
                    noise = MediaPlayer.create(this, R.raw.ayamenoise24);
                    noise.setOnCompletionListener(listener);
                    noise.start();
                    if (switch1) {
                        Toast.makeText(this, getString(R.string.noise24_text), Toast.LENGTH_SHORT).show();
                    }
                    set1.start();
                    break;
                case 27:
                    noise = MediaPlayer.create(this, R.raw.ayamenoise25);
                    noise.setOnCompletionListener(listener);
                    noise.start();
                    if (switch1) {
                        Toast.makeText(this, getString(R.string.noise25_text), Toast.LENGTH_SHORT).show();
                    }
                    set1.start();
                    break;
                case 28:
                    noise = MediaPlayer.create(this, R.raw.ayamenoise26);
                    noise.setOnCompletionListener(listener);
                    noise.start();
                    if (switch1) {
                        Toast.makeText(this, getString(R.string.noise26_text), Toast.LENGTH_SHORT).show();
                    }
                    set1.start();
                    break;
                case 29:
                    noise = MediaPlayer.create(this, R.raw.ayamenoise27);
                    noise.setOnCompletionListener(listener);
                    noise.start();
                    if (switch1) {
                        Toast.makeText(this, getString(R.string.noise27_text), Toast.LENGTH_SHORT).show();
                    }
                    set1.start();
                    break;
                case 30:
                    noise = MediaPlayer.create(this, R.raw.ayamenoise28);
                    noise.setOnCompletionListener(listener);
                    noise.start();
                    if (switch1) {
                        Toast.makeText(this, getString(R.string.noise28_text), Toast.LENGTH_SHORT).show();
                    }
                    set1.start();
                    break;
                case 31:
                    noise = MediaPlayer.create(this, R.raw.ayamenoise29);
                    noise.setOnCompletionListener(listener);
                    noise.start();
                    if (switch1) {
                        Toast.makeText(this, getString(R.string.noise29_text), Toast.LENGTH_SHORT).show();
                    }
                    set1.start();
                    break;
                case 32:
                    noise = MediaPlayer.create(this, R.raw.ayamenoise30);
                    noise.setOnCompletionListener(listener);
                    noise.start();
                    if (switch1) {
                        Toast.makeText(this, getString(R.string.noise30_text), Toast.LENGTH_SHORT).show();
                    }
                    set1.start();
                    break;
                case 33:
                    noise = MediaPlayer.create(this, R.raw.ayamenoise31);
                    noise.setOnCompletionListener(listener);
                    noise.start();
                    if (switch1) {
                        Toast.makeText(this, getString(R.string.noise31_text), Toast.LENGTH_SHORT).show();
                    }
                    set1.start();
                    break;
                case 34:
                    noise = MediaPlayer.create(this, R.raw.ayamenoise32);
                    noise.setOnCompletionListener(listener);
                    noise.start();
                    if (switch1) {
                        Toast.makeText(this, getString(R.string.noise32_text), Toast.LENGTH_SHORT).show();
                    }
                    set1.start();
                    break;
                case 35:
                    noise = MediaPlayer.create(this, R.raw.ayamenoise33);
                    noise.setOnCompletionListener(listener);
                    noise.start();
                    if (switch1) {
                        Toast.makeText(this, getString(R.string.noise33_text), Toast.LENGTH_SHORT).show();
                    }
                    set1.start();
                    break;
                case 36:
                    noise = MediaPlayer.create(this, R.raw.ayamenoise34);
                    noise.setOnCompletionListener(listener);
                    noise.start();
                    if (switch1) {
                        Toast.makeText(this, getString(R.string.noise34_text), Toast.LENGTH_SHORT).show();
                    }
                    set1.start();
                    break;
                case 37:
                    noise = MediaPlayer.create(this, R.raw.ayamenoise35);
                    noise.setOnCompletionListener(listener);
                    noise.start();
                    if (switch1) {
                        Toast.makeText(this, getString(R.string.noise35_text), Toast.LENGTH_SHORT).show();
                    }
                    set1.start();
                    break;
                case 38:
                    noise = MediaPlayer.create(this, R.raw.ayamenoise36);
                    noise.setOnCompletionListener(listener);
                    noise.start();
                    if (switch1) {
                        Toast.makeText(this, getString(R.string.noise36_text), Toast.LENGTH_SHORT).show();
                    }
                    set1.start();
                    break;
                case 39:
                    noise = MediaPlayer.create(this, R.raw.ayamenoise37);
                    noise.setOnCompletionListener(listener);
                    noise.start();
                    if (switch1) {
                        Toast.makeText(this, getString(R.string.noise37_text), Toast.LENGTH_SHORT).show();
                    }
                    set1.start();
                    break;
                case 40:
                    noise = MediaPlayer.create(this, R.raw.ayamenoise38);
                    noise.setOnCompletionListener(listener);
                    noise.start();
                    if (switch1) {
                        Toast.makeText(this, getString(R.string.noise38_text), Toast.LENGTH_SHORT).show();
                    }
                    set1.start();
                    break;
                case 41:
                    noise = MediaPlayer.create(this, R.raw.ayamenoise39);
                    noise.setOnCompletionListener(listener);
                    noise.start();
                    if (switch1) {
                        Toast.makeText(this, getString(R.string.noise39_text), Toast.LENGTH_SHORT).show();
                    }
                    set1.start();
                    break;
                case 42:
                    noise = MediaPlayer.create(this, R.raw.ayamenoise40);
                    noise.setOnCompletionListener(listener);
                    noise.start();
                    if (switch1) {
                        Toast.makeText(this, getString(R.string.noise40_text), Toast.LENGTH_SHORT).show();
                    }
                    set1.start();
                    break;
                case 43:
                    noise = MediaPlayer.create(this, R.raw.ayamenoise41);
                    noise.setOnCompletionListener(listener);
                    noise.start();
                    if (switch1) {
                        Toast.makeText(this, getString(R.string.noise41_text), Toast.LENGTH_SHORT).show();
                    }
                    set1.start();
                    break;
                case 44:
                    noise = MediaPlayer.create(this, R.raw.ayamenoise42);
                    noise.setOnCompletionListener(listener);
                    noise.start();
                    if (switch1) {
                        Toast.makeText(this, getString(R.string.noise42_text), Toast.LENGTH_SHORT).show();
                    }
                    set1.start();
                    break;
                case 45:
                    noise = MediaPlayer.create(this, R.raw.ayamenoise43);
                    noise.setOnCompletionListener(listener);
                    noise.start();
                    if (switch1) {
                        Toast.makeText(this, getString(R.string.noise43_text), Toast.LENGTH_SHORT).show();
                    }
                    set1.start();
                    break;
                case 46:
                    noise = MediaPlayer.create(this, R.raw.ayamenoise44);
                    noise.setOnCompletionListener(listener);
                    noise.start();
                    if (switch1) {
                        Toast.makeText(this, getString(R.string.noise44_text), Toast.LENGTH_SHORT).show();
                    }
                    set1.start();
                    break;
                case 47:
                    noise = MediaPlayer.create(this, R.raw.ayamenoise45);
                    noise.setOnCompletionListener(listener);
                    noise.start();
                    if (switch1) {
                        Toast.makeText(this, getString(R.string.noise45_text), Toast.LENGTH_SHORT).show();
                    }
                    set1.start();
                    break;
                default:
                    Toast.makeText(this, "NO SOUND SELECTED", Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    }


    private void retrievedata(){

        SavedSettings =getSharedPreferences("Chara",MODE_PRIVATE);
        charavalue= SavedSettings.getInt("CharaValue",0);

        switch (charavalue) {
            case 0:
                image.setImageResource(R.drawable.ayame0);
                break;
            case 1:
                image.setImageResource(R.drawable.ayame1);
                break;
            case 2:
                image.setImageResource(R.drawable.ayame2);
                break;
            case 3:
                image.setImageResource(R.drawable.ayame3);
                break;
            default:
                image.setImageResource(R.drawable.ayame4);
                break;
        }

        SavedSettings =getSharedPreferences("DevMode",MODE_PRIVATE);
        DevCount= SavedSettings.getInt("DevCount",0);
        if(DevCount==7){
            AlarmBTN.setVisibility(View.VISIBLE);
        }else {
            AlarmBTN.setVisibility(View.INVISIBLE);
        }


        listofsounds= new ArrayList<Integer>();

        SavedSettings =getSharedPreferences("save1",MODE_PRIVATE);
        switch1= SavedSettings.getBoolean("value1",true);

        SavedSettings =getSharedPreferences("save2",MODE_PRIVATE);
        switch2= SavedSettings.getBoolean("value2",true);




        SavedSettings =getSharedPreferences("save3", MODE_PRIVATE);
        if (SavedSettings.getBoolean("value3",true)) {
            listofsounds.add(3);
        }
        SavedSettings =getSharedPreferences("save4", MODE_PRIVATE);
        if (SavedSettings.getBoolean("value4",true)) {
            listofsounds.add(4);
        }
        SavedSettings =getSharedPreferences("save5", MODE_PRIVATE);
        if (SavedSettings.getBoolean("value5",true)) {
            listofsounds.add(5);
        }
        SavedSettings =getSharedPreferences("save6", MODE_PRIVATE);
        if (SavedSettings.getBoolean("value6",true)) {
            listofsounds.add(6);
        }
        SavedSettings =getSharedPreferences("save7", MODE_PRIVATE);
        if (SavedSettings.getBoolean("value7",true)) {
            listofsounds.add(7);
        }
        SavedSettings =getSharedPreferences("save8", MODE_PRIVATE);
        if (SavedSettings.getBoolean("value8",true)) {
            listofsounds.add(8);
        }
        SavedSettings =getSharedPreferences("save9", MODE_PRIVATE);
        if (SavedSettings.getBoolean("value9",true)) {
            listofsounds.add(9);
        }
        SavedSettings =getSharedPreferences("save10", MODE_PRIVATE);
        if (SavedSettings.getBoolean("value10",true)) {
            listofsounds.add(10);
        }
        SavedSettings =getSharedPreferences("save11", MODE_PRIVATE);
        if (SavedSettings.getBoolean("value11",true)) {
            listofsounds.add(11);
        }
        SavedSettings =getSharedPreferences("save12", MODE_PRIVATE);
        if (SavedSettings.getBoolean("value12",true)) {
            listofsounds.add(12);
        }
        SavedSettings =getSharedPreferences("save13", MODE_PRIVATE);
        if (SavedSettings.getBoolean("value13",true)) {
            listofsounds.add(13);
        }
        SavedSettings =getSharedPreferences("save14", MODE_PRIVATE);
        if (SavedSettings.getBoolean("value14",true)) {
            listofsounds.add(14);
        }
        SavedSettings =getSharedPreferences("save15", MODE_PRIVATE);
        if (SavedSettings.getBoolean("value15",true)) {
            listofsounds.add(15);
        }
        SavedSettings =getSharedPreferences("save16", MODE_PRIVATE);
        if (SavedSettings.getBoolean("value16",true)) {
            listofsounds.add(16);
        }
        SavedSettings =getSharedPreferences("save17", MODE_PRIVATE);
        if (SavedSettings.getBoolean("value17",true)) {
            listofsounds.add(17);
        }
        SavedSettings =getSharedPreferences("save18", MODE_PRIVATE);
        if (SavedSettings.getBoolean("value18",true)) {
            listofsounds.add(18);
        }
        SavedSettings =getSharedPreferences("save19", MODE_PRIVATE);
        if (SavedSettings.getBoolean("value19",true)) {
            listofsounds.add(19);
        }
        SavedSettings =getSharedPreferences("save20", MODE_PRIVATE);
        if (SavedSettings.getBoolean("value20",true)) {
            listofsounds.add(20);
        }
        SavedSettings =getSharedPreferences("save21", MODE_PRIVATE);
        if (SavedSettings.getBoolean("value21",true)) {
            listofsounds.add(21);
        }
        SavedSettings =getSharedPreferences("save22", MODE_PRIVATE);
        if (SavedSettings.getBoolean("value22",true)) {
            listofsounds.add(22);
        }
        SavedSettings =getSharedPreferences("save23", MODE_PRIVATE);
        if (SavedSettings.getBoolean("value23",true)) {
            listofsounds.add(23);
        }
        SavedSettings =getSharedPreferences("save24", MODE_PRIVATE);
        if (SavedSettings.getBoolean("value24",true)) {
            listofsounds.add(24);
        }
        SavedSettings =getSharedPreferences("save25", MODE_PRIVATE);
        if (SavedSettings.getBoolean("value25",true)) {
            listofsounds.add(25);
        }
        SavedSettings =getSharedPreferences("save26", MODE_PRIVATE);
        if (SavedSettings.getBoolean("value26",true)) {
            listofsounds.add(26);
        }
        SavedSettings =getSharedPreferences("save27", MODE_PRIVATE);
        if (SavedSettings.getBoolean("value27",true)) {
            listofsounds.add(27);
        }
        SavedSettings =getSharedPreferences("save28", MODE_PRIVATE);
        if (SavedSettings.getBoolean("value28",true)) {
            listofsounds.add(28);
        }
        SavedSettings =getSharedPreferences("save29", MODE_PRIVATE);
        if (SavedSettings.getBoolean("value29",true)) {
            listofsounds.add(29);
        }
        SavedSettings =getSharedPreferences("save30", MODE_PRIVATE);
        if (SavedSettings.getBoolean("value30",true)) {
            listofsounds.add(30);
        }
        SavedSettings =getSharedPreferences("save31", MODE_PRIVATE);
        if (SavedSettings.getBoolean("value31",true)) {
            listofsounds.add(31);
        }
        SavedSettings =getSharedPreferences("save32", MODE_PRIVATE);
        if (SavedSettings.getBoolean("value32",true)) {
            listofsounds.add(32);
        }
        SavedSettings =getSharedPreferences("save33", MODE_PRIVATE);
        if (SavedSettings.getBoolean("value33",true)) {
            listofsounds.add(33);
        }
        SavedSettings =getSharedPreferences("save34", MODE_PRIVATE);
        if (SavedSettings.getBoolean("value34",true)) {
            listofsounds.add(34);
        }
        SavedSettings =getSharedPreferences("save35", MODE_PRIVATE);
        if (SavedSettings.getBoolean("value35",true)) {
            listofsounds.add(35);
        }
        SavedSettings =getSharedPreferences("save36", MODE_PRIVATE);
        if (SavedSettings.getBoolean("value36",true)) {
            listofsounds.add(36);
        }
        SavedSettings =getSharedPreferences("save37", MODE_PRIVATE);
        if (SavedSettings.getBoolean("value37",true)) {
            listofsounds.add(37);
        }
        SavedSettings =getSharedPreferences("save38", MODE_PRIVATE);
        if (SavedSettings.getBoolean("value38",true)) {
            listofsounds.add(38);
        }
        SavedSettings =getSharedPreferences("save39", MODE_PRIVATE);
        if (SavedSettings.getBoolean("value39",true)) {
            listofsounds.add(39);
        }
        SavedSettings =getSharedPreferences("save40", MODE_PRIVATE);
        if (SavedSettings.getBoolean("value40",true)) {
            listofsounds.add(40);
        }
        SavedSettings =getSharedPreferences("save41", MODE_PRIVATE);
        if (SavedSettings.getBoolean("value41",true)) {
            listofsounds.add(41);
        }
        SavedSettings =getSharedPreferences("save42", MODE_PRIVATE);
        if (SavedSettings.getBoolean("value42",true)) {
            listofsounds.add(42);
        }
        SavedSettings =getSharedPreferences("save43", MODE_PRIVATE);
        if (SavedSettings.getBoolean("value43",true)) {
            listofsounds.add(43);
        }
        SavedSettings =getSharedPreferences("save44", MODE_PRIVATE);
        if (SavedSettings.getBoolean("value44",true)) {
            listofsounds.add(44);
        }
        SavedSettings =getSharedPreferences("save45", MODE_PRIVATE);
        if (SavedSettings.getBoolean("value45",true)) {
            listofsounds.add(45);
        }
        SavedSettings =getSharedPreferences("save46", MODE_PRIVATE);
        if (SavedSettings.getBoolean("value46",true)) {
            listofsounds.add(46);
        }
        SavedSettings =getSharedPreferences("save47", MODE_PRIVATE);
        if (SavedSettings.getBoolean("value47",true)) {
            listofsounds.add(47);
        }

    }

    @Override
    protected void onDestroy() {
        cleanupMediaPlayer();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        cleanupMediaPlayer();
        super.onStop();
    }

    @Override
    protected void onResume() {
        retrievedata();
        super.onResume();
    }

}