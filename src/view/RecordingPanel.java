package view;

import java.awt.Font;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.*;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.*;

import org.apache.commons.io.FileUtils;
import audio.*;
import controller.SilenceCutter;
import net.miginfocom.swing.MigLayout;

/*
 * Author: Rowel Eshan
 */
@SuppressWarnings("serial")
public class RecordingPanel extends JPanel {

	static TargetDataLine targetLine; //The target data line for the microphone

	//Buttons
	private static JButton recordButton; //Button to start recording
	private static JButton stopButton; //Button to stop recording
	private static JButton toMIDI; //Button to take user to midi creation page
	private static JButton saveWAV; //Button to save the recording as wav in the user's computer

	//Audio player
	private static SwingAudioPlayer player;    


	/**
	 * @author Rowel Eshan
	 * This constructor creates the recording panel
	 */
	public RecordingPanel() {

		setLayout(new MigLayout("", "[1572.00]", "[63.00][157.00][433.00,grow][grow,bottom]"));

		JLabel lblNewLabel = new JLabel("Recording"); //recording page title label
		lblNewLabel.setFont(new Font("UD Digi Kyokasho NK-R", Font.PLAIN, 30));
		add(lblNewLabel, "cell 0 0");


		player = new SwingAudioPlayer(); //create and add audio player to panel
		add(player, "flowx,cell 0 2,aligny center");

		initButtons(); //initialize buttons 

	}

	/**
	 * @author Rowel Eshan
	 * This method creates all the buttons 
	 */
	private void initButtons() {
		recordButton = new JButton("Record");
		stopButton = new JButton("Stop recording");

		recordButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { //Start recording audio when button is pressed
				recordAudio(); 
				recordButton.setEnabled(false);
				stopButton.setEnabled(true);
			}
		});
		add(recordButton, "flowx,cell 0 1");

		stopButton.setEnabled(false);
		add(stopButton, "cell 0 1");

		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	//Stop recording when pressed		
				targetLine.stop(); //stop targetline from reciving data
				targetLine.close(); //close the target line
				recordButton.setEnabled(true);
				stopButton.setEnabled(false);
			}
		});

		toMIDI = new JButton("Proceed to MIDI Creator");
		add(toMIDI, "flowx,cell 0 3,aligny bottom");

		toMIDI.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new SilenceCutter("files/recording.wav"); //Cut the begining and ending silence off the recorded track
				MainFrame.toMIDIPanel(); //go to midi panel
			}
		});


		saveWAV = new JButton("Save audio file");
		add(saveWAV, "cell 0 3,aligny bottom");
		saveWAV.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {	 //save the recorded audio to user's file location choice
				JFileChooser fileChooser = new JFileChooser();
				int op = fileChooser.showSaveDialog(player);
				if(op == JFileChooser.APPROVE_OPTION){
					File file;
					System.out.println(fileChooser.getSelectedFile());
					if(fileChooser.getSelectedFile().toString().contains(".wav")) {
						file = fileChooser.getSelectedFile();
					} else {
						file = new File(fileChooser.getSelectedFile().toString() + ".wav"); //add .wav if not already added
					}

					try {
						FileUtils.copyFile(new File("files/recording.wav"), file);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});


		JButton loadWav = new JButton("Load WAV File"); //button to load a wav file from the user's computer
		add(loadWav, "flowx,cell 0 3,aligny bottom");

		loadWav.addActionListener(new ActionListener() { //write the contents of the selected wav file to the recording file
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileFilter(new FileNameExtensionFilter("WAV Files","wav"));
				if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					File file = new File(fileChooser.getSelectedFile().getPath());
					try {
						FileUtils.copyFile(file,new File("files/recording.wav"));
					} catch (IOException e1) {
						System.out.println("Load failed");
					}
				}

			}
		});
		
		//set fonts
		loadWav.setFont(new Font("UD Digi Kyokasho NP-R", Font.PLAIN, 18));
		recordButton.setFont(new Font("UD Digi Kyokasho NP-R", Font.PLAIN, 22));
		saveWAV.setFont(new Font("UD Digi Kyokasho NP-R", Font.PLAIN, 18));
		toMIDI.setFont(new Font("UD Digi Kyokasho NP-R", Font.PLAIN, 18));
		stopButton.setFont(new Font("UD Digi Kyokasho NP-R", Font.PLAIN, 22));

	}

	/**
	 * @author Rowel Eshan
	 * This method records audio and saves it to "recording.wav" in files
	 */
	private static void recordAudio() {
		AudioFormat af = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,44100,16,2,4,44100,false); //set the audio format of the recording

		DataLine.Info datainfo = new DataLine.Info(TargetDataLine.class, af); //create dataline 
				
		if(!AudioSystem.isLineSupported(datainfo)) {
			System.out.println("Not Supported");
		} else {
			System.out.println("Suppourted!");
		}

		try {
			targetLine = (TargetDataLine) AudioSystem.getLine(datainfo); //create data line for audio from user's microphone
			
			targetLine.open(); //open target data line
			targetLine.start(); //start the target data line
		

			Thread audioRecorderThread = new Thread() { // a separate thread for the audio recording to take place. This stops the recording from impeding with GUI
				@Override
				public void run() {
					AudioInputStream recStream = new AudioInputStream(targetLine); //create audio input stream using the microphone data line 
					
					File outputFile = new File("files/recording.wav"); //set the output file as "recording.wav"

					try {
						AudioSystem.write(recStream, AudioFileFormat.Type.WAVE, outputFile); //write the collected audio data to the output file 
					} catch (IOException e) {
						System.out.println("File save failed.");	
					}

					System.out.println("Recording stopped");
				}
			};

			audioRecorderThread.start(); //Start the created recording thread

		} catch (LineUnavailableException e) {
			System.out.println("Audio input not found.");
			e.printStackTrace();
		}

	}

}
