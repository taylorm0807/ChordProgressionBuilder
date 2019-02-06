package c.taylor.chordprogressionbuilder;

import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Spinner KeySpinner;
    private CheckBox minorCheck;
    private Button continueButton;
    private Button confirmationButton;
    private TextView chordQualityTextView;
    private TextView triadNotesTextView;
    private Button[] chordButtons = new Button[8];
    private String key;
    private boolean loaded;
    private SoundPool soundPool;
    private Map<String, Integer> Sounds = new HashMap<>();
    private static String[] sharpNotes = new String[] {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
    private static String[] flatNotes = new String[] {"C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab", "A", "Bb", "B"};
    private String[] notes = new String[8];
    private String[] triad = new String[3];
    private boolean isMinor;
    ArrayAdapter<CharSequence> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeUI();
        adapter = ArrayAdapter.createFromResource(this, R.array.keys, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        KeySpinner.setAdapter(adapter);
        KeySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                key = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        confirmationButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(key != null) {
                    isMinor = minorCheck.isChecked();
                    setNotes();
                    for (int i = 0; i < chordButtons.length; i++) {
                        (chordButtons[i]).setText(notes[i]);
                    }
                }

            }
        });

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNextActivity();
            }
        });
    }

    public void openNextActivity(){
        Intent intent = new Intent(this, chordprogressionbuilder2.class);
        intent.putExtra("key", key);
        intent.putExtra("notes", notes);
        startActivity(intent);
    }

    //This method finds the ID of the button that was pressed and passes it to the method that plays the chord
    public void chordClick(View v){
        for(int i = 0; i < 8; i++){
            if(chordButtons[i].getId() == v.getId()){
                if(loaded && (chordButtons[i].getText()).length() > 0) {
                    playChord(chordButtons[i]);
                    openInfoBox(chordButtons[i]);
                }
            }
        }
    }

    //This method plays the triad of the button that was touched
    public void playChord(Button rootButton) {
        String rootNote = (String)rootButton.getText();
        makeTriad(rootNote);
        for(int i = 0; i < triad.length; i++) {
            AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
            float curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            float maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            float volume = curVolume/maxVolume;
            soundPool.play(Sounds.get(triad[i]), volume , volume , 1 , 0 , 1);
        }
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

    public void openInfoBox(Button chord){
        displayChordQuality(chord);
        displayNotes(chord);


    }

    public void displayNotes(Button chord) {
        makeTriad((String)chord.getText());
        triadNotesTextView.setText("");
        for(int i = 0; i < triad.length-1; i++){
            triadNotesTextView.append(triad[i] + "-");
        }
        triadNotesTextView.append(triad[2]);

    }

    public void displayChordQuality(Button chord) {
        String rootNote = (String)chord.getText();
        chordQualityTextView.setText(rootNote + " ");
        if(!isMinor){
            if(rootNote.equals(notes[1]) || rootNote.equals(notes[2]) || rootNote.equals(notes[5])){
                chordQualityTextView.append("Minor Chord");
            }
            else if(rootNote.equals(notes[6])){
                chordQualityTextView.append("Diminished Chord");
            }
            else{
                chordQualityTextView.append("Major Chord");
            }
        }
        else{
            if(rootNote.equals(notes[0]) || rootNote.equals(notes[3]) || rootNote.equals(notes[4])){
                chordQualityTextView.append("Minor Chord");
            }
            else if(rootNote.equals(notes[1])){
                chordQualityTextView.append("Diminished Chord");
            }
            else{
                chordQualityTextView.append("Major Chord");
            }
        }
    }

    //This method just connects the UI on the screen(Dropdown, checkbox, buttons) to variables in the file so their values can
    //be accessed
    public void initializeUI(){
        KeySpinner = (Spinner)findViewById(R.id.KeySpinner);
        loaded = false;
        minorCheck = (CheckBox)findViewById(R.id.MajorMinorCheckBox);
        confirmationButton = findViewById(R.id.ConfirmationButton);
        continueButton = (Button)findViewById(R.id.ContinueButton);
        chordQualityTextView = findViewById(R.id.ChordQualityTextView);
        triadNotesTextView = findViewById(R.id.ChordNoteTextView);
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
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId,
                                       int status) {
                loaded = true;
                Toast.makeText(getBaseContext(), "loaded", Toast.LENGTH_LONG).show();
            }
        });
    }

    //This method creates all of the sound notes from local files
    public void createSounds() {
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

    //This method takes the key given and updates the eight buttons to display the correct chords in the key
    public void setNotes(){
        if((key.length() == 1 && key.charAt(0) != 'F') || (key.length() > 1 && key.charAt(1) == '#')) {
            //The key is using sharp notes
            sharpKey();
        }
        else {
            //Otherwise the key is using flat notes
            flatKey();
        }
    }

    public void sharpKey(){
        int index = 0;
        //This loop finds the starting index of the sharp-notes, to base the intervals off of for the key
        for(int i = 0; i < sharpNotes.length; i++) {
            if(key.equals(sharpNotes[i])) {
                notes[0] = sharpNotes[i];
                index = i;
            }
        }
        if(!isMinor) {
            for(int a = 1; a < 8; a++) {
                if(a == 3 || a == 7)
                    index++;
                else
                    index = index + 2;
                if(index >= sharpNotes.length)
                    index = index - sharpNotes.length;
                notes[a] = sharpNotes[index];
            }
        }
        else {
            for(int a = 1; a < 8; a++) {
                if(a == 2 || a == 5)
                    index++;
                else
                    index = index + 2;
                if(index >= sharpNotes.length)
                    index = index - sharpNotes.length;
                notes[a] = sharpNotes[index];
            }
        }
    }

    //This method creates the key for a scale that uses flats as accidentals
    private void flatKey() {
        int index = 0;
        //This loop finds the starting index of the flatnotes, to base the intervals off of for the key
        for(int i = 0; i < flatNotes.length; i++) {
            if(key.equals(flatNotes[i])) {
                notes[0] = flatNotes[i];
                index = i;
            }
        }
        if(!isMinor) {
            for(int a = 1; a < 8; a++) {
                if(a == 3 || a == 7)
                    index++;
                else
                    index = index + 2;
                if(index >= flatNotes.length)
                    index = index - flatNotes.length;
                notes[a] = flatNotes[index];
            }
        }
        else {
            for(int a = 1; a < 8; a++) {
                if(a == 2 || a == 5)
                    index++;
                else
                    index = index + 2;
                if(index >= flatNotes.length)
                    index = index - flatNotes.length;
                notes[a] = flatNotes[index];
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundPool.release();
        soundPool = null;
    }

}
