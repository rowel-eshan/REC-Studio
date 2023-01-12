package controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.quifft.QuiFFT;
import org.quifft.output.FFTFrame;
import org.quifft.output.FFTStream;

import jAudioFeatureExtractor.jAudioTools.AudioSamples;

/*
 * Author: Rowel Eshan
 */
public class SilenceCutter {

	private String filepath;
	private final int WINDOW_SIZE = 1000;

	/**
	 * @author Rowel Eshan
	 * This constructor cuts the off the begining silence of a song to increase the midi generation quality
	 * and BPM detection accuracy
	 */
	public SilenceCutter(String filepath) {
		this.filepath = filepath;
		try {
			cutStart();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @author Rowel Eshan
	 * This method cuts the starting silence off a audio file
	 */
	public void cutStart() throws Exception {
		int frameCounter = 0;

		FFTStream fftStream = new QuiFFT(filepath).fftStream();

		fftStream.fftParameters.windowSize = WINDOW_SIZE;
		boolean foundStart = false; //variable to track when the start of audio is found
		fftStream.fftParameters.windowOverlap = 0;
		
		//Finds out the number of frames the silence at the start is
		while(fftStream.hasNext()) {

			frameCounter++;

			FFTFrame nextFrame = fftStream.next();
			
			for(int f = 0; f < nextFrame.bins.length; f++) {
				if(nextFrame.bins[f].amplitude > -30) {
					foundStart = true;  //If the start of the audio has been found, stop the look
				}
			}
			
			if(foundStart) {
				break; //sto pthe loop if the start is found
			}

		}
		
		
		FFTStream fftStream2 = new QuiFFT(filepath).fftStream(); //create second fft stream to find the end of audio

		fftStream2.fftParameters.windowSize = WINDOW_SIZE;
		fftStream2.fftParameters.windowOverlap = 0;
		
		ArrayList<FFTFrame> frameList = new ArrayList<FFTFrame>(); //Array list of FFT Frames to find the end of the audio.

		while(fftStream2.hasNext()) { //populate an array list of frames of the whole file
			frameList.add(fftStream2.next());
		}
		
		boolean foundEnd = false; //used to stop the loop if the end has been found 
		int endFrames = 0; //track the number of frames of silence at the end
		
		for(int i = frameList.size()-1; i > 0; i--) { //go through the array backwards 
			endFrames++;

			for(int f = 0; f < frameList.get(i).bins.length; f++) {
				if(frameList.get(i).bins[f].amplitude > -30) {
					foundEnd = true; //if the end of the audio has been found, stop the loop
				}
			}
			if(foundEnd) {
				break;
			}
		}
		System.out.println();
		
		//Starting and ending silence in seconds 
		double silenceLengthStart = windowToSec(MIDIGenerator.getSampleRate(filepath),frameCounter);	
		double silenceLengthEnd = windowToSec(MIDIGenerator.getSampleRate(filepath),endFrames);		
		
		//Some code after this line is from online. Im unable to find the source again.
		
		AudioSamples as = new AudioSamples(new File("files/recording.wav"), "", false);

		// only get the audio excluding starting and ending silence
		double[][] samples = as.getSamplesChannelSegregated(silenceLengthStart-1, getAudioLength(filepath)-silenceLengthEnd+1);

		// discard the rest of the samples
		as.setSamples(samples);

		// write the samples to a .wav file
		as.saveAudio(new File("files/recording.wav"), true, AudioFileFormat.Type.WAVE, false);
		 
	}

	/**
	 * @author Rowel Eshan
	 * This method converts the windows of FFT frames to seconds of silence
	 */
	private double windowToSec(int sampleRate, int frameCount) {
		double singleSecondFrames = sampleRate/WINDOW_SIZE;
		return (frameCount/singleSecondFrames);
	}

	/**
	 * @author mdma
	 * Modified by Rowel Eshan
	 * Link: https://stackoverflow.com/a/3009973
	 * This method gets the length of the audio file in seconds
	 */
	private double getAudioLength(String filepath) throws IOException, UnsupportedAudioFileException{
		AudioInputStream audioInputStream;

		audioInputStream = AudioSystem.getAudioInputStream(new File(filepath));

		AudioFormat format = audioInputStream.getFormat();
		long frames = audioInputStream.getFrameLength();
		double durationInSeconds = (frames+0.0) / format.getFrameRate();  
		return durationInSeconds;
	}
}
