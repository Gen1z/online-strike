package com.gamedevers.onlinestrike;

import static com.gamedevers.onlinestrike.SignInActivity.data;
import static java.lang.Math.cos;
import static java.lang.Math.floor;
import static java.lang.Math.sin;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    private Bitmap texture;
    private Bitmap enemy_texture;
    private final int resolution = 2;
    private static final SoundPool sp = new SoundPool(30, AudioManager.STREAM_MUSIC, 0);
    private static int FireSound;
    private static int OppFireSound;
    private static boolean walk = false;
    private static MediaPlayer mdShagi;
    public FileOutputStream fileLog;
    static boolean start = false;
    static int maxPlayers;
    static int healt = 100;
    static int fov;
    static float rx = 0, ry = 0;
    static boolean fire = false;
    static int width;
    static int height;
    static String encode;
    public static DatabaseReference myAdress;
    public static ArrayList<DatabaseReference> OppAdress = new ArrayList<>();
    public static ArrayList<String> codes = new ArrayList<>();
    private static int myPlayer = 0;
    private GestureDetectorCompat gd;
    static ArrayList<String> infFromOpp = new ArrayList<>();
    static ArrayList<Integer> OppX = new ArrayList<>();
    static ArrayList<Integer> OppY = new ArrayList<>();
    static ArrayList<Integer> OppAngle = new ArrayList<>();
    static ArrayList<String> OppFire = new ArrayList<>();
    static ArrayList<Integer> OppHealths = new ArrayList<>();
    public static FirebaseDatabase database = FirebaseDatabase.getInstance("https://chat-d5f5b-default-rtdb.europe-west1.firebasedatabase.app");

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        texture = BitmapFactory.decodeResource(this.getResources(), R.mipmap.texture);
        enemy_texture = BitmapFactory.decodeResource(this.getResources(), R.mipmap.enemy_texture);
        FireSound = sp.load(this,R.raw.firesound,1);
        OppFireSound = sp.load(this,R.raw.oppfiresound,1);
        maxPlayers = 500;
        try {
            fileLog = openFileOutput("log.txt", MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // Текущее время
        Date currentDate = new Date();
        String log = "//" + currentDate + " | " + "launch";
        try {
            fileLog.write(log.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        start = false;
        healt = 100;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gd = new GestureDetectorCompat(this, this);
        gd.setIsLongpressEnabled(true);
        codes.clear();
        OppX.clear();
        OppY.clear();
        OppAngle.clear();
        infFromOpp.clear();
        int i = 0;
        while(!(i==(maxPlayers+1))){
            OppX.add(0);
            OppY.add(0);
            OppAngle.add(0);
            infFromOpp.add("");
            i++;
        }
        codes.add("");
        codes.add("");
        codes.add("");
        codes.add("");
        codes.add("");
        codes.add("");
        codes.add("");
        codes.add("");
        codes.add("");
        codes.add("a");
        codes.add("b");
        codes.add("c");
        codes.add("d");
        codes.add("e");
        codes.add("f");
        codes.add("g");
        codes.add("h");
        codes.add("i");
        codes.add("j");
        codes.add("k");
        codes.add("l");
        codes.add("m");
        codes.add("n");
        codes.add("o");
        codes.add("p");
        codes.add("r");
        codes.add("s");
        codes.add("t");
        codes.add("u");
        codes.add("v");
        codes.add("w");
        codes.add("x");
        codes.add("y");
        codes.add("z");
        codes.add("0");
        codes.add("1");
        codes.add("2");
        codes.add("3");
        codes.add("4");
        codes.add("5");
        codes.add("6");
        codes.add("7");
        codes.add("8");
        codes.add("9");
        codes.add(".");
        codes.add("-");
        Display display = getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
        Window w = getWindow();
        w.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mdShagi = MediaPlayer.create(this, R.raw.shagi);
    }

    public void onClickGoToChat(View view){
        Intent i = new Intent(this, ChatActivity.class);
        startActivity(i);
    }

    public boolean checkPlayerBook(int player) throws InterruptedException {
        DatabaseReference drlast = database.getReference("P"+player);
        DatabaseReference drnew = database.getReference("P"+player);
        final String[] lastVal = new String[1];
        final String[] newVal = new String[1];
        drlast.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lastVal[0] = snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        TimeUnit.SECONDS.sleep(3);
        drnew.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                newVal[0] = snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if(Objects.equals(newVal[0], lastVal[0])){
            return false;
        }else{
            return true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myAdress.setValue("");
    }

    public void onClickStart(View view) throws InterruptedException {
        start = true;
        String a = String.valueOf(data);
        String result = a.replaceAll("[^\\p{N}]+", "");
        myPlayer = (int)(Integer.parseInt(result))/999999;
        if(myPlayer==0){
            myPlayer=1;
        }
        if(myPlayer<0){
            myPlayer=Math.abs(myPlayer);
        }
        if(myPlayer>maxPlayers){
            myPlayer=maxPlayers-1;
        }
        //
        if(checkPlayerBook(myPlayer)){
            int p = 1;
            while(!(p>=maxPlayers)){
                if(checkPlayerBook(p)){
                    p++;
                }else{
                    break;
                }
            }
            myPlayer = p;
        }
        //
        myAdress = database.getReference("P"+String.valueOf(myPlayer));
        int i = 0;
        OppAdress.clear();
        while(!(i>=maxPlayers)){
            if(!(("P" + i).equals(String.valueOf(myAdress)))){
                OppAdress.add(database.getReference("P"+ i));
                int finalI1 = i;
                OppAdress.get(OppAdress.size()-1).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        infFromOpp.set(finalI1,snapshot.getValue(String.class));
                        setContentView(new RaycastDraw(MainActivity.this));
                        encode = "";
                        saveData(String.valueOf((int) px));
                        saveData(String.valueOf((int) py));
                        saveData(String.valueOf((int) angle));
                        saveData(String.valueOf(false));
                        saveData(String.valueOf(false));
                        saveData(String.valueOf(healt));
                        myAdress.setValue(encode);
                        if(healt<=0){
                            Intent i = new Intent(MainActivity.this, MainActivity.class);
                            startActivity(i);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            i++;
        }
        setContentView(new RaycastDraw(MainActivity.this));
        // Текущее время
        Date currentDate = new Date();
        String log = "//" + currentDate + " | " + "start in" + myPlayer + "player";
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                fileLog.write(log.getBytes(StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gd.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    static ArrayList<String> map = new ArrayList<>();
    static ArrayList<Integer> lights = new ArrayList<>();
    static int texWidth = 64;
    static int texHeight = 64;
    static float rotSpeed = 0.1f;
    static double px = 11.0;
    static double py = 11.5;  //x and y start position
    static double angleX = -1.0;
    static int angle = (int) angleX;
    static double angleY = 0.0; //initial direction vector
    static double planeX = 0.0;
    static double planeY = 0.66; //the 2d raycaster version of camera plane

    public static void setUp2Dmap(){
        //
        map.add("11111111111111111111111111111111");
        map.add("10000000000000000000000000000001");
        map.add("10000000000000000000000000000001");
        map.add("10000000000000000000000000000001");
        map.add("10000000000000000000000000000001");
        map.add("10000000000000000000000000000001");
        map.add("10000000000000000000000000000001");
        map.add("10000000000000000000000000000001");
        map.add("10000000000000000000000000000001");
        map.add("10000000000000000000000000000001");
        map.add("10000000000000000000000000000001");
        map.add("10000000000000000000000000000001");
        map.add("10000000000000000000000000000001");
        map.add("10000000000000000000000000000001");
        map.add("11111111111111111111111111111111");
        //
        OppX.clear();
        OppY.clear();
        OppAngle.clear();
        OppFire.clear();
        OppHealths.clear();
        int i = 0;
        while(!(i>=maxPlayers)){
            if(i != myPlayer){
                StringBuilder value = new StringBuilder("");
                int latter = 0;
                int idx = 0;
                while(true){
                    if(Objects.equals(infFromOpp.get(i), "") || Objects.equals(infFromOpp.get(i), "0")){break;}
                    String ii = infFromOpp.get(i);
                    try{
                        if(latter >= ii.length()){break;}
                    }catch (NullPointerException ignored){

                    }
                    if(infFromOpp.get(i)==null){break;}
                    try {
                        idx = Integer.parseInt((infFromOpp.get(i).charAt(latter)) + (String.valueOf(infFromOpp.get(i).charAt(latter + 1))));
                    }catch (StringIndexOutOfBoundsException e){
                        e.printStackTrace();
                    }
                    latter+=2;
                    try {
                        if (idx < 1 || (infFromOpp.get(i).charAt(latter)) + (String.valueOf(infFromOpp.get(i).charAt(latter + 1))) == "00") {
                            break;
                        }
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }
                    if(!(idx == 0)){
                        try {
                            value= new StringBuilder(String.valueOf(value)+String.valueOf(codes.get(idx)));
                        }catch (IndexOutOfBoundsException e){
                            e.printStackTrace();
                        }
                    }
                }
                if(value.toString() == ""){
                    OppX.add(0);
                }else{
                    OppX.add(Integer.parseInt(value.toString()));
                }
                value = new StringBuilder("");
                idx = 0;
                while(true){
                    if(Objects.equals(infFromOpp.get(i), "") || Objects.equals(infFromOpp.get(i), "0")){break;}
                    String ii = infFromOpp.get(i);
                    try{
                        if(latter >= ii.length()){break;}
                    }catch (NullPointerException ignored){

                    }
                    if(infFromOpp.get(i)==null){break;}
                    try {
                        idx = Integer.parseInt((infFromOpp.get(i).charAt(latter)) + (String.valueOf(infFromOpp.get(i).charAt(latter + 1))));
                    }catch (StringIndexOutOfBoundsException e){
                        e.printStackTrace();
                    }
                    latter+=2;
                    try {
                        if (idx < 1 || (infFromOpp.get(i).charAt(latter)) + (String.valueOf(infFromOpp.get(i).charAt(latter + 1))) == "00") {
                            break;
                        }
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }
                    if(!(idx == 0)){
                        try {
                            value= new StringBuilder(String.valueOf(value)+String.valueOf(codes.get(idx)));
                        }catch (IndexOutOfBoundsException e){
                            e.printStackTrace();
                        }
                    }
                }
                if(value.toString() == ""){
                    OppY.add(0);
                }else{
                    OppY.add(Integer.parseInt(value.toString()));
                }
                value = new StringBuilder("");
                idx = 0;
                while(true){
                    if(Objects.equals(infFromOpp.get(i), "") || Objects.equals(infFromOpp.get(i), "0")){break;}
                    String ii = infFromOpp.get(i);
                    try{
                        if(latter >= ii.length()){break;}
                    }catch (NullPointerException ignored){

                    }
                    if(infFromOpp.get(i)==null){break;}
                    try {
                        idx = Integer.parseInt((infFromOpp.get(i).charAt(latter)) + (String.valueOf(infFromOpp.get(i).charAt(latter + 1))));
                    }catch (StringIndexOutOfBoundsException e){
                        e.printStackTrace();
                    }
                    latter+=2;
                    try {
                        if (idx < 1 || (infFromOpp.get(i).charAt(latter)) + (String.valueOf(infFromOpp.get(i).charAt(latter + 1))) == "00") {
                            break;
                        }
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }
                    if(!(idx == 0)){
                        try {
                            value= new StringBuilder(String.valueOf(value)+String.valueOf(codes.get(idx)));
                        }catch (IndexOutOfBoundsException e){
                            e.printStackTrace();
                        }
                    }
                }
                if(value.toString() == ""){
                    OppAngle.add(0);
                }else{
                    OppAngle.add(Integer.parseInt(value.toString()));
                }
                value = new StringBuilder("");
                idx = 0;
                while(true){
                    if(Objects.equals(infFromOpp.get(i), "") || Objects.equals(infFromOpp.get(i), "0")){break;}
                    String ii = infFromOpp.get(i);
                    try{
                        if(latter >= ii.length()){break;}
                    }catch (NullPointerException ignored){

                    }
                    try {
                        if (codes.get(idx) == "t" || codes.get(idx) == "r" || codes.get(idx) == "u") {
                            value = new StringBuilder("true");
                            break;
                        }
                    }catch (IndexOutOfBoundsException e){
                        e.printStackTrace();
                    }
                    if(infFromOpp.get(i)==null){break;}
                    try {
                        try {
                            idx = Integer.parseInt((infFromOpp.get(i).charAt(latter)) + (String.valueOf(infFromOpp.get(i).charAt(latter + 1))));
                        }catch (NumberFormatException e){
                            e.printStackTrace();
                        }
                    }catch (StringIndexOutOfBoundsException e){
                        e.printStackTrace();
                    }
                    latter+=2;
                    try {
                        try {
                            if (idx < 1 || (infFromOpp.get(i).charAt(latter)) + (String.valueOf(infFromOpp.get(i).charAt(latter + 1))) == "00") {
                                break;
                            }
                        }catch (StringIndexOutOfBoundsException e){
                            e.printStackTrace();
                        }
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }
                    if(!(idx == 0)){
                        try {
                            value= new StringBuilder(String.valueOf(value)+String.valueOf(codes.get(idx)));
                        }catch (IndexOutOfBoundsException e){
                            e.printStackTrace();
                        }
                    }
                }
                //
                OppFire.add(String.valueOf(value));
                //
                value = new StringBuilder("");
                idx = 0;
                while(true){
                    if(Objects.equals(infFromOpp.get(i), "") || Objects.equals(infFromOpp.get(i), "0")){break;}
                    String ii = infFromOpp.get(i);
                    try{
                        if(latter >= ii.length()){break;}
                    }catch (NullPointerException ignored){

                    }
                    try {
                        if (codes.get(idx) == "t" || codes.get(idx) == "r" || codes.get(idx) == "u") {
                            value = new StringBuilder("true");
                            break;
                        }
                    }catch (IndexOutOfBoundsException e){
                        e.printStackTrace();
                    }
                    if(infFromOpp.get(i)==null){break;}
                    try {
                        try {
                            idx = Integer.parseInt((infFromOpp.get(i).charAt(latter)) + (String.valueOf(infFromOpp.get(i).charAt(latter + 1))));
                        }catch (NumberFormatException e){
                            e.printStackTrace();
                        }
                    }catch (StringIndexOutOfBoundsException e){
                        e.printStackTrace();
                    }
                    latter+=2;
                    try {
                        try {
                            if (idx < 1 || (infFromOpp.get(i).charAt(latter)) + (String.valueOf(infFromOpp.get(i).charAt(latter + 1))) == "00") {
                                break;
                            }
                        }catch (StringIndexOutOfBoundsException e){
                            e.printStackTrace();
                        }
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }
                    if(!(idx == 0)){
                        try {
                            value= new StringBuilder(String.valueOf(value)+String.valueOf(codes.get(idx)));
                        }catch (IndexOutOfBoundsException e){
                            e.printStackTrace();
                        }
                    }
                }
                if(value.toString() == "true"){
                    walk();
                }
                value = new StringBuilder("");
                idx = 0;
                while(true){
                    if(Objects.equals(infFromOpp.get(i), "") || Objects.equals(infFromOpp.get(i), "0")){break;}
                    String ii = infFromOpp.get(i);
                    try{
                        if(latter >= ii.length()){break;}
                    }catch (NullPointerException ignored){

                    }
                    if(infFromOpp.get(i)==null){break;}
                    try {
                        idx = Integer.parseInt((infFromOpp.get(i).charAt(latter)) + (String.valueOf(infFromOpp.get(i).charAt(latter + 1))));
                    }catch (StringIndexOutOfBoundsException e){
                        e.printStackTrace();
                    }
                    latter+=2;
                    try {
                        if (idx < 1 || (infFromOpp.get(i).charAt(latter)) + (String.valueOf(infFromOpp.get(i).charAt(latter + 1))) == "00") {
                            break;
                        }
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }
                    if(!(idx == 0)){
                        try {
                            value= new StringBuilder(String.valueOf(value)+String.valueOf(codes.get(idx)));
                        }catch (IndexOutOfBoundsException e){
                            e.printStackTrace();
                        }
                    }
                }
                if(!(value.toString().equals(""))){
                    try {
                        OppHealths.add(Integer.valueOf(value.toString()));
                    }catch (NumberFormatException e){
                        e.printStackTrace();
                    }
                }
                StringBuilder res1 = new StringBuilder();
                try {
                    res1 = new StringBuilder(map.get((int) px));
                }catch (IndexOutOfBoundsException e){
                    e.printStackTrace();
                }
                try {
                    res1.setCharAt((int) py, '4');
                }catch (StringIndexOutOfBoundsException e){
                    e.printStackTrace();
                }
                try{
                    map.remove((int)px);
                    map.add((int) px,res1.toString());
                }catch (IndexOutOfBoundsException e){
                    e.printStackTrace();
                }
                //
                StringBuilder res = new StringBuilder();
                try {
                    res = new StringBuilder(map.get(OppX.get(OppX.size()-1) / 2));
                }catch (ArrayIndexOutOfBoundsException e){
                    e.printStackTrace();
                }
                try {
                    res.setCharAt(OppY.get(OppY.size()-1), '3');
                }catch (StringIndexOutOfBoundsException e){
                    e.printStackTrace();
                }
                map.remove(OppX.get(OppX.size()-1)/2);
                map.add(OppX.get(OppX.size()-1)/2,res.toString());
            }
            i++;
        }
        int l = 0;
        while(!(l == maxPlayers)){
            try {
                if (OppFire.get(l).equals("true")) {
                    fire(l);
                }
            }catch (IndexOutOfBoundsException e){
                e.printStackTrace();
            }
            l++;
        }
    }

    public static void walk(){
        mdShagi.start();
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        if(start){
            if(motionEvent.getX() > width/4 && motionEvent.getX()<(width/4)+100) {
                if (motionEvent.getY() > height / 4 && motionEvent.getY() < (height / 4) + 100) {
                    mdShagi.start();
                    walk = true;
                    py += 1 * Math.sin(angleX);
                    px += 1 * Math.cos(angleY);
                } else {
                    walk = false;
                }
            }
            if(motionEvent.getX() > width/4 && motionEvent.getX()<(width/4)+100) {
                if (motionEvent.getY() > (height / 4) + 120 && motionEvent.getY() < (height / 4) + 220) {
                    mdShagi.start();
                    walk = true;
                    py -= 1 * Math.sin(angleX);
                    px -= 1 * Math.cos(angleY);
                } else {
                    if(!walk){
                        walk = false;
                    }
                }
            }
            if(px<0)
                px=0;
            if(py<0)
                py=0;
            if(motionEvent.getX() > width/2+(width/4) && motionEvent.getX()<width/2+(width/4)+100){
                if(motionEvent.getY()>height/2&&motionEvent.getY()<(height/2)+100){
                    sp.play(FireSound,1,1,0,0,1);
                    fire=true;
                }else{
                    fire=false;
                }
            }else{
                fire = false;
            }
            //

            encode = "";
            saveData(String.valueOf((int) px));
            saveData(String.valueOf((int) py));
            saveData(String.valueOf((int) angle));
            saveData(String.valueOf(fire));
            saveData(String.valueOf(walk));
            saveData(String.valueOf(healt));
            myAdress.setValue(encode);
            if(fire){
                try {
                    TimeUnit.MILLISECONDS.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                encode = "";
                saveData(String.valueOf((int) px));
                saveData(String.valueOf((int) py));
                saveData(String.valueOf((int) angle));
                saveData(String.valueOf(false));
                saveData(String.valueOf(false));
                saveData(String.valueOf(healt));
                myAdress.setValue(encode);
            }
            if(walk){
                try {
                    TimeUnit.MILLISECONDS.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                encode = "";
                saveData(String.valueOf((int) px));
                saveData(String.valueOf((int) py));
                saveData(String.valueOf((int) angle));
                saveData(String.valueOf(false));
                saveData(String.valueOf(false));
                saveData(String.valueOf(healt));
                myAdress.setValue(encode);
            }
            setContentView(new RaycastDraw(this));
            // Текущее время
            Date currentDate = new Date();
            String log = "//" + currentDate + " | " + "touch on screen";
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    fileLog.write(log.getBytes(StandardCharsets.UTF_8));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static void fire(int i) {
        try {
            rx = OppX.get(i);
            ry = OppY.get(i);
            fov = OppAngle.get(i);
        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }
        sp.play(OppFireSound,1,1,0,0,1);
        for(int w = 0; w < 2; w++){
            try {
                rx = OppX.get(i);
                ry = OppY.get(i);
            }catch (IndexOutOfBoundsException e){
                e.printStackTrace();
            }
            int l = 0;
            while(!(l == 100)){
                rx += 1 * Math.sin(OppAngle.get(i));
                ry += 1 * Math.cos(OppAngle.get(i));
                try {
                    if (String.valueOf(map.get((int) rx).charAt((int) ry)).equals("4") || String.valueOf(map.get((int) rx+1).charAt((int) ry+1)).equals("4")) {
                        healt -= 10;
                        break;
                    }
                }catch (ArrayIndexOutOfBoundsException e){
                    e.printStackTrace();
                }catch (IndexOutOfBoundsException e){
                    e.printStackTrace();
                }
                if(rx == px && ry == py){healt-=10; break;}
                l++;
            }
            if (fov > 359 || fov<-359){
                fov = 0;
            }
            fov++;
        }
        if(healt<=0){
            healt=0;
        }
    }

    public static void saveData(String value){
        int latter = 0;
        int i = 0;
        while (i <= value.length()) {
            try{
                encode = encode + (String.valueOf(codes.indexOf(String.valueOf(value.charAt(latter)))));
            }catch (StringIndexOutOfBoundsException e){
                e.printStackTrace();
            }
            latter++;
            //
            i++;
        }
        encode=encode+"00";
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {

        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        if(start){
            angle+=motionEvent1.getX()/15-motionEvent.getX()/15;
            if(angle>360){angle = 0;}
            if((motionEvent1.getX()/15-motionEvent.getX()/15)>0){
                double oldDirX = angleX;
                angleX = angleX * cos(-rotSpeed) - angleY * sin(-rotSpeed);
                angleY = oldDirX * sin(-rotSpeed) + angleY * cos(-rotSpeed);
                double oldPlaneX = planeX;
                planeX = planeX * cos(-rotSpeed) - planeY * sin(-rotSpeed);
                planeY = oldPlaneX * sin(-rotSpeed) + planeY * cos(-rotSpeed);
            }else {
                double oldDirX = angleX;
                angleX = angleX * cos(rotSpeed) - angleY * sin(rotSpeed);
                angleY = oldDirX * sin(rotSpeed) + angleY * cos(rotSpeed);
                double oldPlaneX = planeX;
                planeX = planeX * cos(rotSpeed) - planeY * sin(rotSpeed);
                planeY = oldPlaneX * sin(rotSpeed) + planeY * cos(rotSpeed);
            }
            encode = "";
            saveData(String.valueOf((int) px));
            saveData(String.valueOf((int) py));
            saveData(String.valueOf(angle));
            saveData(String.valueOf(fire));
            saveData(String.valueOf(walk));
            saveData(String.valueOf(healt));
            myAdress.setValue(encode);
            setContentView(new RaycastDraw(this));
            // Текущее время
            Date currentDate = new Date();
            String log = "//" + currentDate + " | " + "touch on screen";
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    fileLog.write(log.getBytes(StandardCharsets.UTF_8));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    public class RaycastDraw extends View {


        public RaycastDraw(Context context) {
            super(context);
        }

        public int[] createBitmap(int lineHeight,int texNum){
            int[] pixels_new;
            if(texNum==1){
                texture = Bitmap.createScaledBitmap(texture,texWidth,lineHeight/resolution,false);
                pixels_new = new int[texture.getHeight()* texture.getWidth()];
                texture.getPixels(pixels_new,0,texture.getWidth(),0,0,texture.getWidth(),texture.getHeight());
            }else {
                enemy_texture = Bitmap.createScaledBitmap(enemy_texture,texWidth,(lineHeight/resolution)-5,false);
                pixels_new = new int[enemy_texture.getHeight()* enemy_texture.getWidth()];
                enemy_texture.getPixels(pixels_new,0,enemy_texture.getWidth(),0,0,enemy_texture.getWidth(),enemy_texture.getHeight());
            }
            return pixels_new;
        }

        @Override
        protected void onDraw(Canvas canvas){
            super.onDraw(canvas);
            ArrayList<Integer> list = new ArrayList<>();
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.BLACK);
            canvas.drawPaint(paint);
            lights.clear();
            map.clear();
            setUp2Dmap();
            int old_dist = 0;
            int count_x = 0;
            int y=0,stop_y=0;
            int old_mapX,old_mapY;
            int[] pixels_new = new int[0];
            for(int x = 0; x < width*1.075; x+=resolution) {
                if(x!=0){
                    old_dist=stop_y-y;
                }
                //calculate ray position and direction
                double cameraX = 2 * x / (double) width - 1; //x-coordinate in camera space
                double rayDirX = angleX + planeX * cameraX;
                double rayDirY = angleY + planeY * cameraX;

                //which box of the map we're in
                int mapX = (int) px;
                int mapY = (int) py;

                //length of ray from current position to next x or y-side
                double sideDistX;
                double sideDistY;

                //length of ray from one x or y-side to next x or y-side
                double deltaDistX = (rayDirX == 0) ? 1e30 : Math.abs(1 / rayDirX);
                double deltaDistY = (rayDirY == 0) ? 1e30 : Math.abs(1 / rayDirY);
                double perpWallDist;

                //what direction to step in x or y-direction (either +1 or -1)
                int stepX;
                int stepY;

                int hit = 0; //was there a wall hit?
                int side = 0; //was a NS or a EW wall hit?

                //calculate step and initial sideDist
                if (rayDirX < 0) {
                    stepX = -1;
                    sideDistX = (px - mapX) * deltaDistX;
                } else {
                    stepX = 1;
                    sideDistX = (mapX + 1.0 - px) * deltaDistX;
                }
                if (rayDirY < 0) {
                    stepY = -1;
                    sideDistY = (py - mapY) * deltaDistY;
                } else {
                    stepY = 1;
                    sideDistY = (mapY + 1.0 - py) * deltaDistY;
                }
                //perform DDA
                while (hit == 0) {
                    //jump to next map square, either in x-direction, or in y-direction
                    if (sideDistX < sideDistY) {
                        sideDistX += deltaDistX;
                        old_mapX=mapX;
                        mapX += stepX;
                        side = 0;
                    } else {
                        sideDistY += deltaDistY;
                        old_mapY=mapY;
                        mapY += stepY;
                        side = 1;
                    }
                    //Check if ray has hit a wall
                    if(Integer.parseInt(String.valueOf(map.get(mapX).charAt(mapY))) > 0){
                        hit = 1;
                        if(Integer.parseInt(String.valueOf(map.get(mapX).charAt(mapY))) == 1){
                            if(side==0) {
                                for (int i = 0;i<=resolution;i++){
                                    lights.add(1);
                                }
                            }
                        }else if(Integer.parseInt(String.valueOf(map.get(mapX).charAt(mapY))) == 2){
                            if(side==1) {
                                for (int i = 0;i<=resolution;i++){
                                    lights.add(2);
                                }

                            }
                        }else if(Integer.parseInt(String.valueOf(map.get(mapX).charAt(mapY))) == 3){
                            for (int i = 0;i<=resolution;i++){
                                lights.add(3);
                            }
                            if(x!=0){
                                try {
                                    if(lights.get(x-1) !=3){
                                        list.add(x);
                                    }
                                }catch (IndexOutOfBoundsException e){
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }

                //Calculate distance of perpendicular ray (Euclidean distance would give fisheye effect!)
                if (side == 0) perpWallDist = (sideDistX - deltaDistX);
                else perpWallDist = (sideDistY - deltaDistY);

                //Calculate height of line to draw on screen
                int lineHeight = (int) (height / perpWallDist);


                int pitch = 100;

                //calculate lowest and highest pixel to fill in current stripe
                int drawStart = -lineHeight / 2 + height / 2 + pitch;
                if (drawStart < 0) drawStart = 0;
                int drawEnd = lineHeight / 2 + height / 2 + pitch;
                if (drawEnd >= height) drawEnd = height - 1;

                //texturing calculations
                int texNum = Integer.parseInt(String.valueOf(map.get(mapX).charAt(mapY))); //1 subtracted from it so that texture 0 can be used!

                //calculate value of wallX
                double wallX; //where exactly the wall was hit
                if (side == 0) {
                    wallX = py + perpWallDist * rayDirY;
                }
                else{
                    wallX = px + perpWallDist * rayDirX;
                }
                wallX -= floor((wallX));
                //x coordinate on the texture
                int texX = (int) (wallX * texWidth);
                if (side == 0 && rayDirX > 0) texX = texWidth - texX - 1;
                if (side == 1 && rayDirY < 0) texX = texWidth - texX - 1;
                // How much to increase the texture coordinate per screen pixel
                double step = 1.0 * texHeight / lineHeight;
                // Starting texture coordinate
                double texPos = (drawStart - pitch - height / 2 + lineHeight / 2) * step;
                y = (lineHeight/-2)+300;
                int count_y = 0;
                if(!(x>=lights.size())){
                    if(x!=0){
                        if(lights.get(x)==3&&lights.get(x-1)!=3) {
                            count_x = 0;
                        }
                    }
                }
                stop_y  = (lineHeight/2)+300;
                if(stop_y-y!=old_dist){
                    pixels_new = new int[createBitmap(stop_y-y,texNum).length];
                    pixels_new = createBitmap(stop_y-y,texNum);
                }
                while(!(y>=stop_y)){
                    if(texture!=null&&enemy_texture!=null){
                        int pixel = 0;
                        if(!(count_x+count_y*texWidth>=pixels_new.length)){
                            pixel = pixels_new[count_x+count_y*texWidth];
                        }
                        paint.setColor(pixel);
                    }else {
                        paint.setColor(Color.DKGRAY);
                    }
                    //draw px to screen
                    canvas.drawRect(x,y,x+resolution,y+resolution,paint);
                    //
                    y+=resolution;
                    count_y+=1;
                }
                count_x+=1;
                if(texture!=null)
                    if(count_x>texture.getWidth()||count_x>enemy_texture.getWidth()){
                        count_x=0;
                    }
            }
            //
            int l = 0;
            int i = l+1;
            while(!(l == i)){
                if(!(Math.sqrt(((OppX.get(l)-px)*(OppX.get(l)-px))+((OppY.get(l)-py)*(OppY.get(l)-py)))>50)){
                    paint.setColor(Color.DKGRAY);
                    if(list.size() == 0){break;}
                    try {
                        canvas.drawRect(list.get(l)+20,  (height/2)-5, width - (width - (320 + (list.get(l)-50))), height-(height/2)-50, paint);
                    }catch (IndexOutOfBoundsException e){
                        e.printStackTrace();
                    }
                    paint.setColor(Color.RED);
                    try {
                        canvas.drawRect(list.get(l)+25, (float) height/2, (float) ((width-(width-(OppHealths.get(l)*3.90)))+(list.get(l)-125)),height-(height/2)-60,paint);
                    }catch (IndexOutOfBoundsException e){
                        e.printStackTrace();
                    }
                }
                l++;
            }
            //
            int x=0,y1=0;
            while(y1<map.size()){
                x=0;
                while(x<map.get(1).length()){
                    try {
                        if(!String.valueOf(map.get(y1).charAt(x)).equals("0")){
                            if(String.valueOf(map.get(y1).charAt(x)).equals("1")){
                                paint.setColor(Color.LTGRAY);
                            }else{
                                if(String.valueOf(map.get(y1).charAt(x)).equals("2")){
                                    paint.setColor(Color.WHITE);
                                }else{
                                    if(String.valueOf(map.get(y1).charAt(x)).equals("3")){
                                        paint.setColor(Color.RED);
                                    }else if(String.valueOf(map.get(y1).charAt(x)).equals("4")){
                                        paint.setColor(Color.BLUE);
                                    }
                                }
                            }
                            canvas.drawRect(x*10,y1*15,(x*10)-40,(y1*15)-40,paint);
                        }
                    }catch (IndexOutOfBoundsException e){
                        e.printStackTrace();
                    }
                    x++;
                }
                y1++;
            }
            //
            paint.setColor(Color.DKGRAY);
            canvas.drawRect(width/4,height/4,(width/4)+100,(height/4)+100,paint);
            canvas.drawRect(width/4,(height/4)+120,(width/4)+100,(height/4)+220,paint);
            paint.setColor(Color.WHITE);
            paint.setTextSize(15);
            canvas.drawText("UP",(width/4)+40,(height/4)+70,paint);
            canvas.drawText("DOWN",(width/4)+30,(height/4)+190,paint);
            paint.setColor(Color.DKGRAY);
            canvas.drawCircle((width/2+(width/4))+30,(height/2)+40,65,paint);
            paint.setColor(Color.WHITE);
            canvas.drawText("FIRE",(width/2+(width/4))+25,(height/2)+53,paint);
            canvas.drawCircle(width/2,height/2,5,paint);
            paint.setColor(Color.RED);
            paint.setTextSize(30);
            canvas.drawText(String.valueOf(healt),15,height-37,paint);
            paint.setColor(Color.DKGRAY);
            canvas.drawRect(80,height-25,width-(width-400),height-70,paint);
            paint.setColor(Color.RED);
            canvas.drawRect(85F, (float) (height-33), (float) (width-(width-(healt*3.90))),height-60,paint);
            // Текущее время
            Date currentDate = new Date();
            String log = "//" + currentDate + " | " + "draw to screen";
            try {
                fileLog.write(log.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}