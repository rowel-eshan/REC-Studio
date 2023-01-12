package view;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class HelpPanel extends JPanel {

	/**
	 * @author rowel eshan
	 * This constructor creates the help panel
	 */
	public HelpPanel() {
		setLayout(new MigLayout("", "[978.00px,grow][]", "[36px][grow][][]"));

		JLabel lblNewLabel = new JLabel("Help"); //recording page title label
		lblNewLabel.setFont(new Font("UD Digi Kyokasho NK-R", Font.PLAIN, 30));
		add(lblNewLabel, "cell 0 0,alignx left,aligny top");

		JButton backToTitle = new JButton("Home page"); //Button to take user to redording panel
		backToTitle.setFocusPainted(false);
		backToTitle.setFont(new Font("UD Digi Kyokasho NP-R", Font.PLAIN, 18));

		backToTitle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainFrame.toTitlePanel();	//take user to title panel	
			}
		});
				
		JPanel helpLabelPanel = new JPanel();//create panel to hold help labels
		add(helpLabelPanel, "cell 0 1,grow");
		helpLabelPanel.setLayout(new MigLayout("", "[851.00]", "[][]"));
		
		//Create the help labels
		JLabel label = new JLabel("Recording:");
		label.setFont(new Font("UD Digi Kyokasho NK-R", Font.PLAIN, 17));
		helpLabelPanel.add(label, "cell 0 0");
		
		JLabel label_1 = new JLabel("	-To record yourself, head over to \"Recording\" and press \"Record\".");
		label_1.setFont(new Font("UD Digi Kyokasho NK-R", Font.PLAIN, 17));
		helpLabelPanel.add(label_1, "cell 0 1");
		
		JLabel label_2 = new JLabel("	-When you are done recording, press \"Stop recording\"");
		label_2.setFont(new Font("UD Digi Kyokasho NK-R", Font.PLAIN, 17));
		helpLabelPanel.add(label_2, "cell 0 2");
		
		JLabel label_3 = new JLabel("	-You may also load a WAV file by pressing \"load WAV file\" ");
		label_3.setFont(new Font("UD Digi Kyokasho NK-R", Font.PLAIN, 17));
		helpLabelPanel.add(label_3, "cell 0 3");
		
		JLabel label_4 = new JLabel("	-To proceed, either press \"Generate MIDI\" or save the recording by pressing \"Save audio file\" ");
		label_4.setFont(new Font("UD Digi Kyokasho NK-R", Font.PLAIN, 17));
		helpLabelPanel.add(label_4, "cell 0 4");
		
		JLabel label_5 = new JLabel("MIDI Creator: ");
		label_5.setFont(new Font("UD Digi Kyokasho NK-R", Font.PLAIN, 17));
		helpLabelPanel.add(label_5, "cell 0 6");
		
		JLabel spaceLabel = new JLabel(" ");
		spaceLabel.setFont(new Font("UD Digi Kyokasho NK-R", Font.PLAIN, 17));
		helpLabelPanel.add(spaceLabel, "cell 0 5");
		
		JLabel label_6 = new JLabel("	-To create MIDI, press \"Generate MIDI\". This will generate MIDI with either the recorded audio or the loaded WAV");
		label_6.setFont(new Font("UD Digi Kyokasho NK-R", Font.PLAIN, 17));
		helpLabelPanel.add(label_6, "cell 0 7");
		
		JLabel label_7 = new JLabel("	-To play the created MIDI, choose an instrument and press \"Play MIDI file\"");
		label_7.setFont(new Font("UD Digi Kyokasho NK-R", Font.PLAIN, 17));
		helpLabelPanel.add(label_7, "cell 0 8");
		
		JLabel label_8 = new JLabel("	-To save the created MIDI, press \"save MIDI\"");
		label_8.setFont(new Font("UD Digi Kyokasho NK-R", Font.PLAIN, 17));
		helpLabelPanel.add(label_8, "cell 0 9");
		
		JLabel label_9 = new JLabel("-To refine the generated MIDI, click the \"Enable value override\" checkbox and adjust the sliders");
		label_9.setFont(new Font("UD Digi Kyokasho NK-R", Font.PLAIN, 17));
		helpLabelPanel.add(label_9, "cell 0 10");
		
		JLabel label_10 = new JLabel("	      -The Low threshold value is the frequency at which the note detector starts detecting   ");
		label_10.setFont(new Font("UD Digi Kyokasho NK-R", Font.PLAIN, 17));
		helpLabelPanel.add(label_10, "cell 0 11");
		
		JLabel label_11 = new JLabel("	      -The High threshold value is the frequency at which the note detector stops detecting   ");
		label_11.setFont(new Font("UD Digi Kyokasho NK-R", Font.PLAIN, 17));
		helpLabelPanel.add(label_11, "cell 0 12");
		
		JLabel label_12 = new JLabel("	      -The override BPM allows you to fine tune the BPM used by the MIDI Generator. Press \"Detect DPM\" to get detected BPM");
		label_12.setFont(new Font("UD Digi Kyokasho NK-R", Font.PLAIN, 17));
		helpLabelPanel.add(label_12, "cell 0 13");
		
		JLabel label_13 = new JLabel("	      -The shift frequency is the frequency where all notes above it will be shifted down one octave");
		label_13.setFont(new Font("UD Digi Kyokasho NK-R", Font.PLAIN, 17));
		helpLabelPanel.add(label_13, "cell 0 14");


	}

}
