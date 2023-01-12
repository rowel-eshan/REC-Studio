/*
 * Name: 
 * 		- Rowel Eshan
 * Date: 
 * 		- Jan 17th, 2022
 * Course Code:  
 * 		- ICS4U1-01 Mr.Fernandes
 * Title: 
 * 		- SDP3 REC Studio -Rowel Eshan
 * Description:
 * 		- This product is called REC Studio, and it is a program that records musical instrument playing and converts it into MIDI. 
 * 		- Users are able to record themselves playing, then save the file as WAV or MIDI.
 * 		- The MIDI generation's parameters can be overridden  using the override sliders for better end results 
 * Features:
 * 		- WAV to MIDI conversion
 * 		- Recording WAV
 * 		- Recording Playback
 * 		- MIDI playback using different instruments
 * 		- Light and dark modes that save on close
 * 		- Very user friendly and modern design 
 * 		- Seamless transition between panels
 * 		- High user controllability for the MIDI generation
 *  Major Skills:
 * 		- Data structures, Complex algorithms, Audio analysis, Digital signal processing, UI/UX design and development,
 * 		- File management, control structures, Using various data types, Modular programming 
 *  Areas of Concern:
 *  	- Some instruments have sound profiles(timbre) that make it hard to analyze and put a definitive note to.
 *  		- Works best with Flutes and wind instruments, Piano, Guitar
 *  		- May struggle with percussion instruments (Xylophone...)
 *  	- Will have trouble processing lower pitch instruments (Bass, chelo...)
 *  	- will not process multiple notes playing at once. It will only detect the loudest note/frequency
 *  	- Users need to play as perfectly as possible for best result
 *  	- Users must play using constant BPM. Ex. Classical guitar with Rubato (variable tempo)
 *  	- MIDI generation may struggle with very dynamic rhythms
 *  API's used:
 *  	- jMusic
 *  	- QuiFFT
 *   	- TrackAnalyzer
 *    		- Commons math 3 (library for track analyzer)
 *    		- jtransforms (library for track analyzer)
 *     	- Flat Laf (UI Look and feel)
 *     	- jAudio
 *     	- Commons io 
 *     	- Swing libraries
 *     		- jGoodies
 *     		- migLayout
 */

package application;

import view.MainFrame;

/*
 * Author: Rowel Eshan
 */
public class RECStudioRunner {

	public static void main(String[] args) {
		//run REC Studio
		new MainFrame();
	}

}
