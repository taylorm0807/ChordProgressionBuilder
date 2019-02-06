package c.taylor.chordprogressionbuilder;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class chordprogressionbuilder2 extends AppCompatActivity {

    private Button[] chordButtons = new Button[8];
    private String key;
    private SoundPool soundPool;
    private Button returnButton;
    private Map<String, Integer> Sounds = new HashMap<>();
    private String[] notes = new String[8];
    private String[] triad = new String[3];
    private chordQueue queue = new chordQueue(8, 3);
    private String[] tempTriad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chordprogressionbuilder2);
        returnButton = (Button)findViewById(R.id.ReturnButton);
        Bundle extraData = getIntent().getExtras();
        key = extraData.getString("key");
        notes = extraData.getStringArray("notes");
        initializeUI();
        for (int i = 0; i < chordButtons.length; i++) {
            (chordButtons[i]).setText(notes[i]);
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME).setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build();
            soundPool = new SoundPool.Builder().setMaxStreams(17).setAudioAttributes(audioAttributes).build();
        } else {
            soundPool = new SoundPool(17, AudioManager.STREAM_MUSIC, 0);
        }
        createSounds();
    }

    public void createSounds(){
        Sounds.put("A", soundPool.load(this, R.raw.piano_a, 1));
        Sounds.put("A#", soundPool.load(this, R.raw.piano_as, 1));
        Sounds.put("Ab", soundPool.load(this, R.raw.piano_ab, 1));
        Sounds.put("B", soundPool.load(this, R.raw.piano_b, 1));
        Sounds.put("Bb", soundPool.load(this, R.raw.piano_bb, 1));
        Sounds.put("C", soundPool.load(this, R.raw.piano_c, 1));
        Sounds.put("C#", soundPool.load(this, R.raw.piano_cs, 1));
        Sounds.put("D", soundPool.load(this, R.raw.piano_d, 1));
        Sounds.put("Db", soundPool.load(this, R.raw.piano_db, 1));
        Sounds.put("D#", soundPool.load(this, R.raw.piano_ds, 1));
        Sounds.put("E", soundPool.load(this, R.raw.piano_e, 1));
        Sounds.put("Eb", soundPool.load(this, R.raw.piano_eb, 1));
        Sounds.put("F", soundPool.load(this, R.raw.piano_f, 1));
        Sounds.put("F#", soundPool.load(this, R.raw.piano_fs, 1));
        Sounds.put("G", soundPool.load(this, R.raw.piano_g, 1));
        Sounds.put("Gb", soundPool.load(this, R.raw.piano_gb, 1));
        Sounds.put("G#", soundPool.load(this, R.raw.piano_gs, 1));

    }

    public void chordClick(View v){
        for(int i = 0; i < 8; i++){
            if(chordButtons[i].getId() == v.getId()){
                addChord(chordButtons[i]);
            }
        }
    }

    //This method plays the triad of the button that was touched
    public void addChord(Button rootButton) {
        String rootNote = (String)rootButton.getText();
        makeTriad(rootNote);
        int full = queue.addChord(triad);
        if(full == 0)
            Toast.makeText(this, "The queue is currently full, try removing a chord first", Toast.LENGTH_SHORT).show();
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

    public void playButton(View v){
        Toast.makeText(this, queue.toString() + "", Toast.LENGTH_LONG).show();
        for(int i = 0; i < queue.getSize(); i++) {
            tempTriad = queue.getChord(i);
            playChord();
        }
    }

    private void playChord() {
        for(int a  = 0; a < tempTriad.length; a++){
            soundPool.play(Sounds.get(tempTriad[a]), 1, 1, 1, 0, 1);
        }
    }
}
