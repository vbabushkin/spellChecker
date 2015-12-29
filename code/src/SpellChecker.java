

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

//import static spellchecker.Main.writer;

/**
 *
 * @author Wild
 */
public class SpellChecker {
    static ArrayList wordList = new ArrayList();
    static java.io.File file;
    public static BufferedWriter writer;

    /**
     * @param args the command line arguments
     */
    
   static String displayWordList(){
        String res=new String();
        for(int i=0;i<wordList.size();i++){
        res=res+wordList.get(i);
        res=res+"\n";
        } 
        return res;
    }
    
    
     //reading from file
    public static void readFile(java.io.File file) throws FileNotFoundException {
        Scanner input = new Scanner(file);
        // Read text from the file
        while (input.hasNext()) {
            addWord(input.nextLine());
        }
        // Close the file
        input.close();
    }

    //writting to file
    public static void writeFile(java.io.File file) throws FileNotFoundException, IOException{
        writer = new BufferedWriter(new FileWriter(file.getAbsolutePath()));
        for(int i=0;i<wordList.size();i++){
            writer.write(String.format("%s",wordList.get(i)));
            writer.newLine();
        }
        writer.close();

    }

    //adds a word to list
    public static void addWord(String word){
        boolean flag=false;
        for(int i=0;i<wordList.size();i++){
            if(word.equals(wordList.get(i)))
                flag=true;
        }
        if(!flag)
            wordList.add(word);
        Collections.sort(wordList);
    }
//randomly selects a student
    static String selectRandom()
    {
        String res=new String();
        int n=wordList.size();
        if(n!=0){
            long seed=System.currentTimeMillis();
            Random randomGenerator = new Random(seed);
            int randomStudentIndex = randomGenerator.nextInt(n);

            res=wordList.get(randomStudentIndex).toString();
            //wordList.remove(randomStudentIndex);
            
        }
        else
        {
            res="Wordlist is empty";
        }
        return res;
    }

    
    //Plays an AudioInputStream of combined sounds
    public static void play(AudioInputStream appendedFiles){
        AudioFormat  audioFormat=appendedFiles.getFormat();
        AudioInputStream  audioStream =appendedFiles;
        SourceDataLine sourceLine;
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        try {
            sourceLine = (SourceDataLine) AudioSystem.getLine(info);
            sourceLine.open(audioFormat);
            sourceLine.start();
            int nBytesRead = 0;
            int bufferSize = audioFormat.getFrameSize() * Math.round(audioFormat.getSampleRate() / 10);
            byte[] abData = new byte[bufferSize];
            while (nBytesRead != -1) {
                try {
                    nBytesRead = audioStream.read(abData, 0, abData.length);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (nBytesRead >= 0) {
                    @SuppressWarnings("unused")
                    int nBytesWritten = sourceLine.write(abData, 0, nBytesRead);
                }
            }

            sourceLine.drain();
            sourceLine.close();

        } catch (LineUnavailableException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    
    
}

