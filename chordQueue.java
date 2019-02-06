package c.taylor.chordprogressionbuilder;

import android.content.Context;
import android.media.SoundPool;
import android.widget.Toast;

public class chordQueue {

    private String[][] queue;
    private int endOfQueue;

    public chordQueue(int size, int sizeOfChords){
        endOfQueue = 0;
        queue = new String[size][sizeOfChords];
            for(int r = 0; r < queue.length; r++){
                for(int c  = 0; c < queue[0].length; c++){
                    queue[r][c] = " ";
                }
            }
    }

    public int getSize(){
        return endOfQueue;
    }

    public int addChord(String[] chord){
        return addChord(chord, endOfQueue);
    }

    public int addChord(String[] chord, int pos){
        if(endOfQueue > queue.length - 1) {
            return 0;
        }
        else {
            for (int c = 0; c < queue[0].length; c++) {
                queue[pos][c] = chord[c];
            }
            endOfQueue++;
            return 1;
        }
    }

    public String[] getChord(int pos){
        String[] chord = new String[queue[0].length];
        for(int c = 0; c < chord.length; c++){
            chord[c] = queue[pos][c];
        }
        return chord;
    }

    public void removeChord(){
        removeChord(endOfQueue);
    }

    public void removeChord(int pos){
        for(int r = pos; r < queue.length - 1; r++){
            for(int c = 0; c < queue[0].length; c++)
            queue[r][c] = queue[r+1][c];
        }
        endOfQueue--;
    }

    public String toString(){
        StringBuilder allChord = new StringBuilder();
        for(int r = 0; r < queue.length-1; r++){
            StringBuilder oneChord = new StringBuilder();
            for(int c = 0; c < queue[0].length; c++){
                oneChord.append(queue[r][c]);
            }
            allChord.append(oneChord + ", ");
        }
        StringBuilder oneChord = new StringBuilder();
        for(int c = 0; c < queue[0].length; c++){
            oneChord.append(queue[queue.length-1][c]);
        }
        allChord.append(oneChord);
        return allChord.toString();
    }

}
