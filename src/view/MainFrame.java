package view;

import java.awt.Font;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.*;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFrame;

import javax.swing.*;

import com.formdev.flatlaf.*;

/*
 * Author: Rowel Eshan
 */
public class MainFrame {

	//frame
	public static JFrame frmRecStudio;	//The main frame that the program run off

	//panels
	public static RecordingPanel recordingPanel; //the recording screen
	public static MidiPanel midiPanel; //the midi creation screen
	public static TitlePanel titlePanel; //the title screen
	public static HelpPanel helpPanel;

	public static boolean isDark = false; //variable to track the current chosen UI theme

	/**
	 * @author Rowel Eshan
	 * Create the application.
	 */
	public MainFrame() {
		initialize(); //create and run the frame
	}


	/**
	 * @author Rowel Eshan
	 * This method initializes the frame
	 */
	private void initialize() {
		//initialize frame
		frmRecStudio = new JFrame();
		frmRecStudio.setResizable(false);
		frmRecStudio.setTitle("REC Studio");
		frmRecStudio.setBounds(100, 100, 1120, 630);
		frmRecStudio.setSize(1120,630);
		frmRecStudio.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmRecStudio.setIconImage(new ImageIcon("images/logo.png").getImage());


		try {
			if(userThemePref()) { //set look and feel
				UIManager.setLookAndFeel( new FlatDarkLaf()); //dark look and feel
			} else {
				UIManager.setLookAndFeel( new FlatLightLaf()); //light look and feel
			}
			SwingUtilities.updateComponentTreeUI(frmRecStudio); //update all components

		} catch (UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}

		//initialize the panels 
		recordingPanel = new RecordingPanel();
		midiPanel = new MidiPanel();
		titlePanel = new TitlePanel();
		helpPanel = new HelpPanel();
		
		JMenuBar menuBar = new JMenuBar(); //the navigation menu bar at the top of screen
		frmRecStudio.setJMenuBar(menuBar); 

		JMenu titleMenu = new JMenu("Home page"); //menu item to take user to title screen
		
		titleMenu.setFont(new Font("UD Digi Kyokasho NP-R", Font.PLAIN, 15));
		menuBar.add(titleMenu);
		
		titleMenu.addMouseListener(new MouseListener() {	
			@Override
			public void mousePressed(MouseEvent e) {
				//open title page and remove other panels
				frmRecStudio.getContentPane().removeAll();
				frmRecStudio.getContentPane().add(titlePanel);
				frmRecStudio.repaint();
				frmRecStudio.revalidate();

			}

			@Override
			public void mouseExited(MouseEvent e) {
				titleMenu.setSelected(false); //un-highlight item
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				titleMenu.setSelected(true); //highlight item
			}

			@Override
			public void mouseClicked(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {}
		});

		JMenu midiPanelMenu = new JMenu("MIDI Creator"); //menu item to take user to midi creation screen
		midiPanelMenu.setFont(new Font("UD Digi Kyokasho NP-R", Font.PLAIN, 15));

		midiPanelMenu.addMouseListener(new MouseListener() {

			@Override
			public void mousePressed(MouseEvent e) {
				//open midi creation page and remove other panels
				toMIDIPanel();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				midiPanelMenu.setSelected(false); //un-highlight item
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				midiPanelMenu.setSelected(true); //highlight item
			}

			@Override
			public void mouseClicked(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {}
		});

		JMenu recordingPanelMenu = new JMenu("Recorder"); //menu item to take user to recording screen
		recordingPanelMenu.setFont(new Font("UD Digi Kyokasho NP-R", Font.PLAIN, 15));
		
		menuBar.add(recordingPanelMenu);
		menuBar.add(midiPanelMenu);
		recordingPanelMenu.addMouseListener(new MouseListener() {

			@Override
			public void mousePressed(MouseEvent e) {
				//open recording page and remove other panels
				toRecordingPanel();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				recordingPanelMenu.setSelected(false); //un-highlight item
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				recordingPanelMenu.setSelected(true); //highlight item
			}

			@Override
			public void mouseClicked(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {}
		});
		
		


		JMenu themeChooser = new JMenu("Theme"); //menu for user to choose the UI theme
		themeChooser.setFont(new Font("UD Digi Kyokasho NP-R", Font.PLAIN, 15));
		
		menuBar.add(themeChooser);

		JMenuItem lightTheme = new JMenuItem("Light theme"); //light theme
		lightTheme.setFont(new Font("UD Digi Kyokasho NP-R", Font.PLAIN, 15));
		themeChooser.add(lightTheme);

		JMenuItem darkTheme = new JMenuItem("Dark theme"); //dark theme
		darkTheme.setFont(new Font("UD Digi Kyokasho NP-R", Font.PLAIN, 15));
		themeChooser.add(darkTheme);


		darkTheme.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					//set theme as dark and update all components
					UIManager.setLookAndFeel(new FlatDarkLaf());
					SwingUtilities.updateComponentTreeUI(frmRecStudio);
					SwingUtilities.updateComponentTreeUI(titlePanel);
					SwingUtilities.updateComponentTreeUI(recordingPanel);
					SwingUtilities.updateComponentTreeUI(midiPanel);
					isDark = true;
					savePref();
				} catch (UnsupportedLookAndFeelException e1) {
					e1.printStackTrace();
				}


			}

		});

		lightTheme.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					//set theme as light and update all components
					UIManager.setLookAndFeel(new FlatLightLaf());
					SwingUtilities.updateComponentTreeUI(frmRecStudio);
					SwingUtilities.updateComponentTreeUI(titlePanel);
					SwingUtilities.updateComponentTreeUI(recordingPanel);
					SwingUtilities.updateComponentTreeUI(midiPanel);
					isDark = false;
					savePref();
				} catch (UnsupportedLookAndFeelException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		JMenu help = new JMenu("Help"); //menu item opens the help page
		help.setFont(new Font("UD Digi Kyokasho NP-R", Font.PLAIN, 15));
		
		help.addMouseListener(new MouseListener() {

			@Override
			public void mousePressed(MouseEvent e) {
				toHelpPanel();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				help.setSelected(false); //un-highlight item
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				help.setSelected(true); //highlight item
			}

			@Override
			public void mouseClicked(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {}
		});
		menuBar.add(help);

		frmRecStudio.getContentPane().setLayout(new BoxLayout(frmRecStudio.getContentPane(), BoxLayout.X_AXIS));
		
		frmRecStudio.getContentPane().add(titlePanel); //open title page as first screen

		frmRecStudio.setVisible(true); //show the frame
	}

	/**
	 * @author Rowel Eshan
	 * This method takes the user to the midi generation panel
	 */
	public static void toMIDIPanel() {
		frmRecStudio.getContentPane().removeAll();
		frmRecStudio.getContentPane().add(midiPanel);
		MidiPanel.generateButton.getActionListeners()[0].actionPerformed(null);
		frmRecStudio.repaint();
		frmRecStudio.revalidate();
	}

	/**
	 * @author Rowel Eshan
	 * This method takes the user to the recording panel
	 */
	public static void toRecordingPanel() {
		frmRecStudio.getContentPane().removeAll();
		frmRecStudio.getContentPane().add(recordingPanel);
		frmRecStudio.repaint();
		frmRecStudio.revalidate();
	}
	
	/**
	 * @author Rowel Eshan
	 * This method takes the user to the help panel
	 */
	public static void toHelpPanel() {
		frmRecStudio.getContentPane().removeAll();
		frmRecStudio.getContentPane().add(helpPanel);
		frmRecStudio.repaint();
		frmRecStudio.revalidate();
	}
	
	/**
	 * @author Rowel Eshan
	 * This method takes the user to the title panel
	 */
	public static void toTitlePanel() {
		frmRecStudio.getContentPane().removeAll();
		frmRecStudio.getContentPane().add(titlePanel);
		frmRecStudio.repaint();
		frmRecStudio.revalidate();
	}
	
	/**
	 * @author Rowel Eshan
	 * This method gets the user's saved preference for UI theme
	 */
	private boolean userThemePref() {
		Scanner input = null;
		try {
			input = new Scanner(new File("files/pref.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(input.nextLine().contains("dark")) {
			isDark = true;
			return true;
		} else {
			isDark = false;
			return false;
		}
	}
	
	/**
	 * @author Rowel Eshan
	 * This method sets the user's preffered theme
	 */
	private void savePref() {
		try {
			BufferedWriter w = new BufferedWriter(new FileWriter("files/pref.txt"));
			if(isDark) {
				w.write("dark"); //if the selection is dark, write dark to the file
			} else {
				w.write("light"); //if the selection is light, write light to the file
			}
			w.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
