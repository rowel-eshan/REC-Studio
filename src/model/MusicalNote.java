package model;

import controller.MIDIGenerator;
/**
 * @Author Rowel Eshan
 * This class is a model for a musical note. 
 */
public class MusicalNote {
	
	private int noteNumber; //The midi note number
	private double frequency; //The frequency of the note
	private double length; //The musical duration of the note
	
	public int getNoteNumber() {
		return noteNumber;
	}

	public double getFrequency() {
		return frequency;
	}

	public double getLength() {
		return length;
	}

	public void setNoteNumber(int noteNumber) {
		this.noteNumber = noteNumber;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public MusicalNote(double frequency, double length) {
		this.frequency = frequency;
		this.length = length;
		noteNumber = MIDIGenerator.freqToNote(frequency);
	}
}
