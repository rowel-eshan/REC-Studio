package view;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

import jm.music.data.Phrase;
import model.MusicalNote;

/*
 * Author: Rowel Eshan
 */
@SuppressWarnings("serial")
public class MIDIViewer extends JPanel {
	
	public static ArrayList<MusicalNote> noteArray; //array of notes created by midi generation

	/**
	 * @author Rowel Eshan
	 * This constructor creates and draws the midi viewer to the screen
	 */
	public MIDIViewer(ArrayList<MusicalNote> noteArray2) {
		MIDIViewer.noteArray = noteArray2;
		super.repaint(); //update the viewer
	}
	
	/**
	 * @author Rowel Eshan
	 * This constructor creates and draws the midi viewer to the screen using a phrase input
	 */
	public MIDIViewer(Phrase ph) {
		noteArray = new ArrayList<MusicalNote>();
		for(int i = 0; i < ph.length(); i++) {
			noteArray.add(new MusicalNote(ph.getNote(i).getFrequency(), ph.getNote(i).getRhythmValue())); // add notes to note array from phrase input
		}
		super.repaint(); //update the viewer
	}	
	
	/**
	 * @author Rowel Eshan
	 * This constructor does nothing. it only makes the panel not null.
	 */
	public MIDIViewer() {
		//Empty construtor needed because adding null panels cause errors
	}
	
	/**
	 * @author Rowel Eshan
	 * This method draws the midi viewer
	 */
	@Override
	public void paint(Graphics g) {
		
		drawStaff(g); //draw the staff lines
			
		if(MainFrame.isDark) { //check the currently selected theme and change the color accordingly
			g.setColor(Color.WHITE);
		} else {
			g.setColor(Color.BLACK);
		}

		g.drawRect(0, 0, 990, 375); //draw midi viewer border

		if(MainFrame.isDark) { //check the currently selected theme and change the color accordingly
			g.setColor(new Color(149, 127, 176));
		} else {
			g.setColor(Color.BLACK);
		}
		
		for(int i = 0; i < noteArray.size() && i < 30;i++) { //loop through the whole
			if(noteArray.get(i).getNoteNumber() > 10) {
				 //draw a note at a specific location corresponding to the pitch of the note and the time
				drawNote(i*20+30,380-noteArray.get(i).getNoteNumber()*8+getOffset(),g);
			}
		}
	}

	/**
	 * @author Rowel Eshan
	 * This method draws a note in a given location
	 */
	private void drawNote(int x, int y, Graphics g) {
		g.fillOval(x, y, 13, 8);
	}

	/**
	 * @author Rowel Eshan
	 * This method draws the bounds of the midi viewer
	 */
	@Override
	public void paintComponent(Graphics g) {
		g.fillRect(0, 0, 50, 50);
	}
	
	/**
	 * @author Rowel Eshan
	 * This method draws the staff lines of the midi viewer
	 */
	private void drawStaff(Graphics g) {
		for(int i = 382; i > 0; i-=8){ //draw a line every 8 pixels 
			g.setColor(new Color(90, 84, 97));
			g.drawLine(0, i, 990, i);
		}
	}
	
	/**
	 * @author Rowel Eshan
	 * This method returns the lowest note found.
	 */
	private int getLowestPitch() {
		int lowestNote = 999; //lowest found note. Initialized to a high number to avoid errors
		for(MusicalNote n:noteArray) {
			if(n.getNoteNumber() < lowestNote && n.getNoteNumber() > 1) {
				lowestNote = n.getNoteNumber();
			}
		}
		return lowestNote;// return the lowest found note
	}
	
	/**
	 * @author Rowel Eshan
	 * This method returns the offset needed for the notes to stay within the range of the viewer
	 */
	private int getOffset() {
		int startingNoteY = getLowestPitch() * 8; //convert the lowest note number to pixels in y direction
		return startingNoteY-50; //Subtract 50 so that it does not go below the lowest line
	}

}
