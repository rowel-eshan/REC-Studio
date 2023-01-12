package controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.quifft.QuiFFT;
import org.quifft.output.FFTFrame;
import org.quifft.output.FFTStream;

import at.ofai.music.beatroot.BeatRoot;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.util.Write;
import model.MusicalNote;

/*
 * Author: Rowel Eshan
 */
public class MIDIGenerator {

	public static String audioFilePath = "files/recording.wav";

	public static Score mainScore = new Score(); //The main score holding the notes of the generated midi

	public static int numNotes = 0; //number of notes tracker

	public static int lowThresh = 70; //default low threshold
	public static int highThresh = 1800; //default high threshold 
	public static int shiftFreq = 1000; //default shift frequency

	private Queue<MusicalNote> notes = new LinkedList<MusicalNote>(); //a queue to hold all the notes detected by the FFT

	public static ArrayList<MusicalNote> noteArray = new ArrayList<MusicalNote>(); //an array list to hold the notes detcted for the midi viewer

	public static Phrase finalPhrase; //the final phrase after note merging


	/**
	 * @author Rowel Eshan
	 * This constructor generates midi using default values of the recordied audio
	 */
	public MIDIGenerator() {
		audioFilePath = "files/recording.wav";
		try {
			mainScore = new Score(); //reset main score
			//reset parameter values to default
			lowThresh = 70;
			highThresh = 1800;
			shiftFreq = 1000;
			finalPhrase = new Phrase(); //reset final phrase
			int BPM = (int)BeatRoot.getBPM(audioFilePath);	//set BPM using detector
			wavToMIDI(BPM); //start wav to midi conversion
		} catch (IOException | UnsupportedAudioFileException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @author Rowel Eshan
	 * This constructor generates midi using slider override values 
	 */
	public MIDIGenerator(int lowThreshhold, int highThreshold, int overrideBPM, int shiftFrequency) throws IOException, UnsupportedAudioFileException {
		//set the parameter with override values
		lowThresh = lowThreshhold;
		highThresh = highThreshold;
		shiftFreq = shiftFrequency;
		finalPhrase = new Phrase(); //reset final phrase
		mainScore = new Score(); //reset main score
		wavToMIDI(overrideBPM); //start wav to midi conversion
	}

	/**
	 * @author Rowel Eshan
	 * This method analyzes an audio file and creates a midi file
	 */
	private void wavToMIDI(int BPM) throws IOException, UnsupportedAudioFileException {

		Phrase ph = new Phrase(); //phrase to store the notes
		noteArray = new ArrayList<MusicalNote>(); 

		System.out.println("Detected BPM: " + BPM);

		FFTStream fftStream = new QuiFFT(audioFilePath).fftStream(); //Create new fft stream 

		fftStream.fftParameters.windowSize = samplesPerEighth(BPM,getSampleRate(audioFilePath)); //set the window size of the FFT
		fftStream.fftParameters.windowOverlap = 0; //disallow window overlap

		while(fftStream.hasNext()) { //loops though whole fft stream of the whole audio file
			FFTFrame nextFrame = fftStream.next(); //get the next frame
			double highestFreq = 0;   //tracker to track frequency with highest amplitude
			double highestAmp = -100; //highest amplitude tracker

			for(int f = 0; f < nextFrame.bins.length; f++) { //cycle through all the frequency bins
				if(nextFrame.bins[f].amplitude > highestAmp && nextFrame.bins[f].frequency < highThresh && nextFrame.bins[f].frequency > lowThresh && nextFrame.bins[f].amplitude > -25) {
					highestAmp = nextFrame.bins[f].amplitude; //set the highest amp
					highestFreq = nextFrame.bins[f].frequency; //set best frequency
				}
			}

			MusicalNote n = new MusicalNote(highestFreq, .5); //create new musical note using the detected frequency

			notes.add(n); //add note to note queue
			numNotes++;

		}

		//Add notes but dont add first set of rests 
		while(notes.peek() != null) { // loop untill queue is empty
			if(notes.peek().getNoteNumber() < 1 && ph.length() == 0) { //if note is a blank note, remove
				notes.remove();
			} else {
				ph.add(new Note(notes.poll().getNoteNumber(),.5));
			}
		}

		//Shift notes that are too high down one octave 
		for(int i = 0; i < ph.length(); i++) {
			if(ph.getNote(i).getFrequency() > shiftFreq) {
				ph.setNote( new Note(ph.getNote(i).getPitch()-12,ph.getNote(i).getRhythmValue()), i); 
			}
		}

		
		Note[] tempArr = ph.getNoteArray(); //temp note array to hold all the semi final notes	
		
		for(int i = 0 ;i < tempArr.length-1;i++) { //loop through array
			if(tempArr[i].getPitch() == tempArr[i+1].getPitch()) { //if two consecutive pitches are the same, merge the two
				tempArr[i] = new Note(tempArr[i].getPitch(),tempArr[i].getRhythmValue()*2);
				tempArr[i+1] = null;
				i += 1;
			}
		}


		for(Note n: tempArr) {
			if(n != null) { //add the contents of the temp array to a phrase
				finalPhrase.add(n);
			}
		}
		//populate note array list to send to midi viewer
		for(int i = 0; i < finalPhrase.length(); i++) {
			noteArray.add(new MusicalNote(finalPhrase.getNote(i).getFrequency(),.5));
		}
		
		mainScore.setTempo(BPM); //set BPM of the score
		mainScore.addPart(new Part(ph)); //add to main score
		mainScore.setTimeSignature(4, 4); //set time signature

		Write.midi(mainScore,"files/temp.mid"); //write the midi to a temp file
	}


	/**
	 * @author Rowel Eshan
	 * This method converts a midi note number to frequency in hertz
	 */
	public static Double noteToFreq(int note) {
		return 440 * (Math.pow(2, ((note-69)/12.0)));
	}

	/**
	 * @author Rowel Eshan
	 * This method converts frequency to a midi note number
	 */
	public static int freqToNote(double freq) {
		return (int)(69 + 12*(Math.log(freq/440)/ Math.log(2)));
	}

	/**
	 * @author Rowel Eshan
	 * This method gets the sample rate of the given audio file
	 */
	public static int getSampleRate(String filePath) {
		AudioInputStream audioInputStream;

		try {
			audioInputStream = AudioSystem.getAudioInputStream(new File(filePath)); //get the audio file
			return (int) audioInputStream.getFormat().getSampleRate(); //return the audio file's sample rate
		} catch (UnsupportedAudioFileException | IOException e) {
			System.out.println("File error");
			e.printStackTrace();
		}

		return -1;
	}

	/**
	 * @author Rowel Eshan
	 * This method finds out the number of samples needed per eigth note of the songs bpm
	 */
	public static int samplesPerEighth(int BPM, int sampleRate) {
		return (int)((sampleRate/((BPM)/60.0))/2.0);
	}

	/**
	 * @author Rowel Eshan
	 * This method detects a song's BPM
	 */
	public static int detectBPM() {
		return (int)BeatRoot.getBPM(audioFilePath);
	}

}
