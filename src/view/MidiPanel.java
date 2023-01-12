package view;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.Field;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import controller.MIDIGenerator;
import jm.constants.ProgramChanges;
import jm.music.data.Part;
import jm.music.data.Score;
import jm.util.Play;
import jm.util.Write;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;

/*
 * Author: Rowel Eshan
 */
@SuppressWarnings("serial")
public class MidiPanel extends JPanel {

	//Sliders
	private JSlider lowThreshold; //Slider to control the low threshold of the midi generation
	private JSlider highThreshold; //Slider to control the high threshold of the midi generation
	private JSlider overrideBPM; //Slider to override the BPM used by the midi generation
	private JSlider shiftFrequency; //Change the shift down frequency of the midi generation

	//labels
	private JLabel lowThresholdValue; //label to show the value of the low threshold slider
	private JLabel highThresholdValue; //label to show the value of the high threshold slider
	private JLabel overrideBPMValue; //label to show the value of the override BPM slider
	private JLabel shiftFreqValue; //label to show the value of the freqency shift slider

	//button
	public static JButton generateButton; //button to activate midi generation
	public static JButton quitButton;
	
	//Misc objects
	private JCheckBox overrideCheckBox; //Checkbox to toggle the usage of the overriden values 

	private JPanel sliderPanel; //panel to hold all the sliders
	public MIDIViewer mv = new MIDIViewer(); //the midi viewer panel

	static JComboBox<String> comboBox; //instrument selection box


	/**
	 * @author Rowel Eshan
	 * This constructor creates the midi panel
	 */
	public MidiPanel() {
		setLayout(new MigLayout("", "[464.00,grow]", "[][471.00,grow,fill][-37.00,grow][8.00,grow][-80.00,grow][98.00,grow][13.00,grow]"));


		sliderPanel = new JPanel();
		add(sliderPanel, "cell 0 5,grow"); // add slider panel to the main panel
		sliderPanel.setLayout(new MigLayout("", "[][][71.00][]", "[][][][]"));

		initLabels(); //initalize the labels 
		initButtons(); //initalize the buttons 
		initSlidersAndCheckBox(); //initalize the check box and sliders
		initInstSelector(); //initialize the instrument selector 
	}

	/**
	 * @author Rowel Eshan
	 * This method creates the sliders and a check box
	 */
	private void initSlidersAndCheckBox(){

		//Create, add and set slider properties
		lowThreshold = new JSlider();
		sliderPanel.add(lowThreshold, "cell 1 0");
		lowThreshold.setMaximum(1000);
		lowThreshold.setMinimum(0);
		lowThreshold.setValue(MIDIGenerator.lowThresh);

		highThreshold = new JSlider();
		sliderPanel.add(highThreshold, "cell 1 1");
		highThreshold.setMaximum(7000);
		highThreshold.setMinimum(1000);
		highThreshold.setValue(MIDIGenerator.highThresh);

		overrideBPM = new JSlider();
		sliderPanel.add(overrideBPM, "cell 1 2");
		overrideBPM.setMaximum(200);
		overrideBPM.setMinimum(50);
		overrideBPM.setValue(0);

		shiftFrequency = new JSlider();
		shiftFrequency.setValue(MIDIGenerator.shiftFreq);
		shiftFrequency.setMinimum(70);
		shiftFrequency.setMaximum(3000);
		sliderPanel.add(shiftFrequency, "cell 1 3");

		//Action listeners for sliders(setting value label)
		lowThreshold.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				lowThresholdValue.setText(Integer.toString(lowThreshold.getValue()) + " Hz" ); //set the text of the low threshold label 
			}

		});

		highThreshold.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				highThresholdValue.setText(Integer.toString(highThreshold.getValue()) + " Hz" ); //set the text of the high threshold label 
			}

		});

		shiftFrequency.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				shiftFreqValue.setText(Integer.toString(shiftFrequency.getValue()) + " Hz" ); //set the text of the shift frequency label 
			}
		});


		overrideBPM.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				overrideBPMValue.setText("BPM: " + Integer.toString(overrideBPM.getValue())); //set the text of the BPM label 
			}
		});

		//Set override sliders to be disabled
		lowThreshold.setEnabled(false);
		highThreshold.setEnabled(false);
		overrideBPM.setEnabled(false);
		shiftFrequency.setEnabled(false);

		//create and add checkbox to enable overrride
		overrideCheckBox = new JCheckBox("Enable value override");
		overrideCheckBox.setFont(new Font("UD Digi Kyokasho NP-R", Font.PLAIN, 14));
		sliderPanel.add(overrideCheckBox, "cell 3 3,aligny bottom");

		//action listerner for the checkbox
		overrideCheckBox.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {//toggle the sliders' enabled state
				if(overrideCheckBox.isSelected()) {
					//enable override sliders
					lowThreshold.setEnabled(true);
					highThreshold.setEnabled(true);
					overrideBPM.setEnabled(true);
					shiftFrequency.setEnabled(true);
				} else {
					//disable override sliders
					lowThreshold.setEnabled(false);
					highThreshold.setEnabled(false);
					overrideBPM.setEnabled(false);
					shiftFrequency.setEnabled(false);
					
					//reset slider values to default
					lowThreshold.setValue(70);
					highThreshold.setValue(1800);
					shiftFrequency.setValue(1000);
					
				}
			}

		});


	}

	/**
	 * @author Rowel Eshan
	 * This method creates all the buttons and action listeners for the buttons
	 */
	private void initButtons() {
		
		
		generateButton = new JButton("Generate MIDI"); 
		add(generateButton, "flowx,cell 0 6,aligny bottom");

		generateButton.addActionListener(new ActionListener() { // generate midi when pressed
			@Override
			public void actionPerformed(ActionEvent e) {
				remove(mv); //remove the midi viewer
				MIDIGenerator.noteArray = null; //wipe data of the note array in MIDI generator
				if(!overrideCheckBox.isSelected()) {
					new MIDIGenerator(); //generate the midi
					overrideBPM.setValue(MIDIGenerator.detectBPM()); //set the value of the slider to the detected BPM

				} else {
					try {
						new MIDIGenerator(lowThreshold.getValue(),highThreshold.getValue(),overrideBPM.getValue(),shiftFrequency.getValue()); //generate midi using overriden values
					} catch (IOException | UnsupportedAudioFileException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

				paintNotes();	//pain the notes on the midi viewer
				repaint();  //repaint gui
			}	
		});

		JButton saveMIDI = new JButton("Save MIDI"); //button to save the MIDI to the user's computer
		add(saveMIDI, "cell 0 6,aligny bottom");
		saveMIDI.addActionListener(new ActionListener() { //save the midi file 
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int op = fileChooser.showSaveDialog(null);
				if(op == JFileChooser.APPROVE_OPTION){
					System.out.println(fileChooser.getSelectedFile());	
					String file = fileChooser.getSelectedFile().getPath();
					
					if(file.contains(".mid")) {
						Write.midi(MIDIGenerator.mainScore,file); 
					} else {
						Write.midi(MIDIGenerator.mainScore,(file+".mid")); //add .mid if not added by user
					}
				}
			}
		});

		JButton returnToRec = new JButton("Return to recording"); //button to return to the recording panel
		add(returnToRec, "cell 0 6,aligny bottom");
		returnToRec.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainFrame.toRecordingPanel();	//return to the recording panel
			}
		});

		

		JButton playMIDI = new JButton("Play MIDI file"); //button to play generated midi
		add(playMIDI, "cell 0 6,aligny bottom");
		playMIDI.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				playMIDI();
			}
		});

		JButton detectBPM = new JButton("Detect BPM"); //button to set slider to the detected BPM
		sliderPanel.add(detectBPM, "cell 3 2");
		detectBPM.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				overrideBPM.setValue(MIDIGenerator.detectBPM());
			}
		});
		
		quitButton = new JButton("Quit"); //button to set slider to the detected BPM
		quitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainFrame.frmRecStudio.dispose();
			}
		});

		//set fonts
		detectBPM.setFont(new Font("UD Digi Kyokasho NP-R", Font.PLAIN, 13));
		generateButton.setFont(new Font("UD Digi Kyokasho NP-R", Font.PLAIN, 18));
		quitButton.setFont(new Font("UD Digi Kyokasho NP-R", Font.PLAIN, 18));
		returnToRec.setFont(new Font("UD Digi Kyokasho NP-R", Font.PLAIN, 18));
		saveMIDI.setFont(new Font("UD Digi Kyokasho NP-R", Font.PLAIN, 18));
		playMIDI.setFont(new Font("UD Digi Kyokasho NP-R", Font.PLAIN, 18));
	}

	/**
	 * @author Rowel Eshan
	 * This method creates all the labels
	 */
	private void initLabels() {

		JLabel title = new JLabel("MIDI Creator"); //title 
		title.setFont(new Font("UD Digi Kyokasho NK-R", Font.PLAIN, 30));
		add(title, "cell 0 0");

		JLabel sliderLabel = new JLabel("Low threshold: 	"); //low threshold label
		sliderLabel.setFont(new Font("UD Digi Kyokasho NP-R", Font.PLAIN, 14));
		sliderPanel.add(sliderLabel, "cell 0 0");

		lowThresholdValue = new JLabel("Hz "); //low threshold value label
		lowThresholdValue.setFont(new Font("UD Digi Kyokasho NP-R", Font.PLAIN, 14));
		sliderPanel.add(lowThresholdValue, "cell 2 0");

		JLabel sliderLabel_1 = new JLabel("High threshold: 	"); //high threshold label
		sliderLabel_1.setFont(new Font("UD Digi Kyokasho NP-R", Font.PLAIN, 14));
		sliderPanel.add(sliderLabel_1, "cell 0 1");

		highThresholdValue = new JLabel("Hz "); //high threshold value label
		highThresholdValue.setFont(new Font("UD Digi Kyokasho NP-R", Font.PLAIN, 14));
		sliderPanel.add(highThresholdValue, "cell 2 1");

		JLabel sliderLabel_2 = new JLabel("Override BPM: "); //override BPM label
		sliderLabel_2.setFont(new Font("UD Digi Kyokasho NP-R", Font.PLAIN, 14));
		sliderPanel.add(sliderLabel_2, "cell 0 2");

		overrideBPMValue = new JLabel("BPM "); //override BPM value label
		overrideBPMValue.setFont(new Font("UD Digi Kyokasho NP-R", Font.PLAIN, 14));
		sliderPanel.add(overrideBPMValue, "cell 2 2");

		JLabel shiftFreqLabel = new JLabel("Shift Frequency: "); //shift frequency label
		shiftFreqLabel.setFont(new Font("UD Digi Kyokasho NP-R", Font.PLAIN, 14));
		sliderPanel.add(shiftFreqLabel, "cell 0 3");

		shiftFreqValue = new JLabel("Hz "); //shift frequncy value label
		shiftFreqValue.setFont(new Font("UD Digi Kyokasho NP-R", Font.PLAIN, 14));
		sliderPanel.add(shiftFreqValue, "cell 2 3");

	}

	/**
	 * @author Rowel Eshan
	 * This method creates the instrument selection dropdown
	 */
	private void initInstSelector(){

		comboBox = new JComboBox<String>();
		comboBox.setToolTipText("");
		comboBox.setFont(new Font("UD Digi Kyokasho NP-R", Font.PLAIN, 18));
		
		//Get all the feild names from the jMusic instrument interface
		for(int i = 0; i < ProgramChanges.class.getFields().length;i++) {
			comboBox.addItem(ProgramChanges.class.getFields()[i].getName()); //get and add the variable name to the combo box
		}
		
		add(comboBox, "cell 0 6,aligny bottom");
		
		add(quitButton, "cell 0 6,aligny bottom"); //add quit button after the combo box
	}

	/**
	 * @author Rowel Eshan
	 * This method plays the generated midi file using the selected instrument
	 */
	private void playMIDI() {
		Part inst = null; //temporary part using chosen instrument
		Field f = null;
		try {
			f = ProgramChanges.class.getDeclaredField(comboBox.getSelectedItem().toString()); //Get the selected instrument Field
		} catch (NoSuchFieldException | SecurityException e1) {
			e1.printStackTrace();
		}

		try {
			inst = new Part("instrument",f.getInt(f)); //add instrument to part
		} catch (IllegalArgumentException | IllegalAccessException e1) {
			e1.printStackTrace();
		}


		inst.add(MIDIGenerator.finalPhrase); //add the final note phrase to the part
		Score tempScore = new Score(); //temporary score used to play the midi
		tempScore.add(inst); //add the part to the score
		tempScore.setTempo(overrideBPM.getValue()); //set the tempo of the score
		
		Play.midi(tempScore); //play the score
	}

	/**
	 * @author Rowel Eshan
	 * This method adds the midi viewer panel to the screen to display the generated midi
	 */
	public void paintNotes() {
		mv = new MIDIViewer(MIDIGenerator.noteArray); //update the midi viewer with newly generated midi
		add(mv, "cell 0 1,grow");
		repaint();
		revalidate();
	}

}
