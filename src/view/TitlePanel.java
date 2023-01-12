package view;

import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.FileUtils;

import javax.swing.JLabel;

import java.awt.Font;
import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;

/*
 * Author: Rowel Eshan
 */
@SuppressWarnings("serial")
public class TitlePanel extends JPanel {

	/**
	 * @author Rowel Eshan
	 * This constructor creates the home page panel
	 */
	public TitlePanel() {
		setLayout(new MigLayout("", "[450px,grow]", "[27px][][][190.00][][][][][][][][][grow]"));
		
		JLabel lblRecStudio = new JLabel("REC Studio"); //Main title label
		lblRecStudio.setFont(new Font("UD Digi Kyokasho NK-R", Font.PLAIN, 50));
		add(lblRecStudio, "cell 0 3,alignx center,aligny bottom");
		
		JLabel lblMadeByRowel = new JLabel("Made by: Rowel Eshan"); //Author label
		lblMadeByRowel.setFont(new Font("UD Digi Kyokasho NK-R", Font.PLAIN, 22));
		add(lblMadeByRowel, "cell 0 12,aligny bottom");
		
		initButtons(); //initialize buttons
	}
	
	/**
	 * @author Rowel Eshan
	 * This method creates and adds all the buttons
	 */
	private void initButtons() {
		
		JButton btnNewButton = new JButton("Record audio"); //Button to take user to redording panel
		btnNewButton.setFocusPainted(false);
		btnNewButton.setFont(new Font("UD Digi Kyokasho NP-R", Font.PLAIN, 18));
		
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainFrame.toRecordingPanel(); //take user to recording panel
			}
		});
		add(btnNewButton, "flowx,cell 0 4,alignx center");
		
		JButton btnNewButton_1 = new JButton("Create MIDI"); //button to take user to midi panel
		btnNewButton_1.setFocusPainted(false);
		add(btnNewButton_1, "cell 0 4");
		
		btnNewButton_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainFrame.toMIDIPanel(); //take user to midi panel
			}
		});
		
		
		JButton loadWav = new JButton("Load WAV File"); //button to load a wav file. then take user to midi creation 
		add(loadWav, "flowx,cell 0 4");

		loadWav.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) { //load the wav file's contents into "recording.wav"
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileFilter(new FileNameExtensionFilter("WAV Files","wav"));
				if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					File file = new File(fileChooser.getSelectedFile().getPath() );
					try {
						FileUtils.copyFile(file,new File("files/recording.wav"));
					} catch (IOException e1) {
						System.out.println("Load failed");
					}
				}
				MainFrame.toMIDIPanel(); //take user to recording panel
			}
		});
		loadWav.setFont(new Font("UD Digi Kyokasho NP-R", Font.PLAIN, 18));
		

		btnNewButton_1.setFont(new Font("UD Digi Kyokasho NP-R", Font.PLAIN, 18));
		
		JButton btnNewButton_2 = new JButton("Quit"); //button to exit the program
		btnNewButton_2.setFocusPainted(false);
		add(btnNewButton_2, "cell 0 4");
		
		btnNewButton_2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainFrame.frmRecStudio.dispose(); //close the program
			}
		});
		
		btnNewButton_2.setFont(new Font("UD Digi Kyokasho NP-R", Font.PLAIN, 18));
	}

}
