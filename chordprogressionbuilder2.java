package c.taylor.chordprogressionbuilder;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class chordprogressionbuilder2 extends AppCompatActivity {

    private Button[] chordButtons = new Button[8];
    private Button[] progressionButtons = new Button[8];
    private String key;
    private SoundPool soundPool;
    private Button returnButton;
    private EditText BPMText;
    private Map<String, Integer> Sounds = new HashMap<>();
    private String[] notes = new String[14];
    private String[] triad = new String[3];
    private boolean chordSelected;
    private chordQueue queue = new chordQueue(8, 3);
    private String[] tempTriad;
    private Handler myHandler;
    private Runnable r;
    private int index = 0;
    private int[] soundIDsPlaying = new int[3];
    private boolean looped;
    private boolean pause;
    private int delay;
    private ImageButton loopButton;
    private Drawable loopImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chordprogressionbuilder2);
        returnButton = (Button)findViewById(R.id.ReturnButton);
        Bundle extraData = getIntent().getExtras();
        myHandler = new Handler();
        key = extraData.getString("key");
        notes = extraData.getStringArray("notes");
        r = new Runnable() {
            @Override
            public void run() {
                playChord();
            }
        };
        initializeUI();
        for (int i = 0; i < chordButtons.length; i++) {
            (chordButtons[i]).setText(getSimplifiedNote(notes[i]));
        }
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity();
            }
        });
    }

    public void changeActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void initializeUI(){
        chordButtons[0] = findViewById(R.id.ChordButton1);
        chordButtons[1] = findViewById(R.id.ChordButton2);
        chordButtons[2] = findViewById(R.id.ChordButton3);
        chordButtons[3] = findViewById(R.id.ChordButton4);
        chordButtons[4] = findViewById(R.id.ChordButton5);
        chordButtons[5] = findViewById(R.id.ChordButton6);
        chordButtons[6] = findViewById(R.id.ChordButton7);
        chordButtons[7] = findViewById(R.id.ChordButton8);
        progressionButtons[0] = findViewById(R.id.ProgressionButton1);
        progressionButtons[1] = findViewById(R.id.ProgressionButton2);
        progressionButtons[2] = findViewById(R.id.ProgressionButton3);
        progressionButtons[3] = findViewById(R.id.ProgressionButton4);
        progressionButtons[4] = findViewById(R.id.ProgressionButton5);
        progressionButtons[5] = findViewById(R.id.ProgressionButton6);
        progressionButtons[6] = findViewById(R.id.ProgressionButton7);
        progressionButtons[7] = findViewById(R.id.ProgressionButton8);
        BPMText = findViewById(R.id.BPMText);
        loopButton = findViewById(R.id.LoopButton);
        loopImage = loopButton.getDrawable();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME).setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build();
            soundPool = new SoundPool.Builder().setMaxStreams(17).setAudioAttributes(audioAttributes).build();
        } else {
            soundPool = new SoundPool(17, AudioManager.STREAM_MUSIC, 0);
        }
        createSounds();
    }

    public void createSounds(){
        Sounds.put("A4", soundPool.load(this, R.raw.piano_a4, 1));
        Sounds.put("A#4", soundPool.load(this, R.raw.piano_as4, 1));
        Sounds.put("Ab4", soundPool.load(this, R.raw.piano_ab4, 1));
        Sounds.put("B4", soundPool.load(this, R.raw.piano_b4, 1));
        Sounds.put("Bb4", soundPool.load(this, R.raw.piano_bb4, 1));
        Sounds.put("C4", soundPool.load(this, R.raw.piano_c4, 1));
        Sounds.put("C#4", soundPool.load(this, R.raw.piano_cs4, 1));
        Sounds.put("D4", soundPool.load(this, R.raw.piano_d4, 1));
        Sounds.put("Db4", soundPool.load(this, R.raw.piano_db4, 1));
        Sounds.put("D#4", soundPool.load(this, R.raw.piano_ds4, 1));
        Sounds.put("E4", soundPool.load(this, R.raw.piano_e4, 1));
        Sounds.put("Eb4", soundPool.load(this, R.raw.piano_eb4, 1));
        Sounds.put("F4", soundPool.load(this, R.raw.piano_f4, 1));
        Sounds.put("F#4", soundPool.load(this, R.raw.piano_fs4, 1));
        Sounds.put("G4", soundPool.load(this, R.raw.piano_g4, 1));
        Sounds.put("Gb4", soundPool.load(this, R.raw.piano_gb4, 1));
        Sounds.put("G#4", soundPool.load(this, R.raw.piano_gs4, 1));

        Sounds.put("A5", soundPool.load(this, R.raw.piano_a5, 1));
        Sounds.put("A#5", soundPool.load(this, R.raw.piano_as5, 1));
        Sounds.put("Ab5", soundPool.load(this, R.raw.piano_ab5, 1));
        Sounds.put("B5", soundPool.load(this, R.raw.piano_b5, 1));
        Sounds.put("Bb5", soundPool.load(this, R.raw.piano_bb5, 1));
        Sounds.put("C5", soundPool.load(this, R.raw.piano_c5, 1));
        Sounds.put("C#5", soundPool.load(this, R.raw.piano_cs5, 1));
        Sounds.put("D5", soundPool.load(this, R.raw.piano_d5, 1));
        Sounds.put("Db5", soundPool.load(this, R.raw.piano_db5, 1));
        Sounds.put("D#5", soundPool.load(this, R.raw.piano_ds5, 1));
        Sounds.put("E5", soundPool.load(this, R.raw.piano_e5, 1));
        Sounds.put("Eb5", soundPool.load(this, R.raw.piano_eb5, 1));
        Sounds.put("F5", soundPool.load(this, R.raw.piano_f5, 1));
        Sounds.put("F#5", soundPool.load(this, R.raw.piano_fs5, 1));
        Sounds.put("G5", soundPool.load(this, R.raw.piano_g5, 1));
        Sounds.put("Gb5", soundPool.load(this, R.raw.piano_gb5, 1));
        Sounds.put("G#5", soundPool.load(this, R.raw.piano_gs5, 1));
    }

    public void chordClick(View v){
        for(int i = 0; i < 8; i++){
            if(chordButtons[i].getId() == v.getId())
                addChord(chordButtons[i]);
        }
    }

    public void changeLoop(View v){
        looped = !looped;
    }

    public void pauseProgression(View v){
        pause = true;
        looped = false;
    }

    //If one of the buttons in the workspace is clicked, check to see if a chord is selected. If it is
    //then you add the selected chord to the progression button that was clicked. If a chord wasnt selected, then
    // remove that chord and reset the text to blank
    public void progressionClick(View v){
        for(int i = 0; i < 8; i++){
            if(progressionButtons[i].getId() == v.getId()){
                if(chordSelected == false) {
                    queue.removeChord(i);
                    progressionButtons[i].setText("");
                }
                else{
                    queue.addChord(triad, i);
                    progressionButtons[i].setText(getSimplifiedNote(triad[0]));
                    chordSelected = false;
                }
            }
        }
    }

    //This method creates a triad based on the chord that was selected, and sets chordSelected equal to true, allowing it
    //to be added to the progression workflow
    public void addChord(Button rootButton) {
        String rootNote = (String)rootButton.getText();
        rootNote = getUnsimplifiedNote(rootNote);
        makeTriad(rootNote);
        chordSelected = true;
    }

    public void makeTriad(String rootNote) {
        int startIndex = 0;
        for(int i = 0; i < notes.length; i++){
            if(notes[i].equals(rootNote))
                startIndex = i;
        }
        for(int a = 0; a < triad.length; a++){
            int noteIndex = startIndex + (a*2);
            if(noteIndex >= notes.length)
                noteIndex -= (notes.length - 1);
            triad[a] = notes[noteIndex];
        }
    }

    public int getBPM(){
        if(BPMText.getText().toString().equals(""))
            return 60;
        else
            return Integer.parseInt(BPMText.getText().toString());
    }

    public void playButton(View v){
        //Toast.makeText(this, getBPM() + "", Toast.LENGTH_LONG).show();
        pause = false;
        int BPM = getBPM();
        delay = (int)((60.0/BPM) *(1000));
        //1000;//(60)
        playChord();
        for (int i = 0; i < queue.getSize(); i++) {
            myHandler.postDelayed(r, delay * (i + 1));
        }
    }

    public void playChord() {
        tempTriad = queue.getChord(index);
        if(tempTriad[0] == " "){
            //There is a rest
        }
        else if(!pause){
            for (int a = 0; a < tempTriad.length; a++) {
                soundIDsPlaying[a] = soundPool.play(Sounds.get(tempTriad[a]), 1, 1, 1, 0, 1);
            }
        }
        index++;
        if(index == (queue.getSize() + 1)) {
            index = 0;
            if(looped){
                Handler repeat = new Handler();
                Runnable r2 = new Runnable() {
                    @Override
                    public void run() {
                        View view = findViewById(R.id.imageButton);
                        playButton(view);
                    }
                };
                repeat.postDelayed(r2, delay);
            }
        }
    }

    public String getSimplifiedNote(String noteWithPos) {
        return noteWithPos.substring(0, noteWithPos.length() - 1);
    }

    public String getUnsimplifiedNote(String noteWithoutPos){
        for(int i = 0; i < notes.length; i++){
            if(getSimplifiedNote(notes[i]).equals(noteWithoutPos))
                return notes[i];
        }
        return null;
    }
}
