# RECStudio

## Welcome to RECStudio, an experimental audio to MIDI platform.

Capabilites:
- Recording audio in WAV
- WAV to MIDI conversion
- Recording Playback
- MIDI playback using different instruments
- Viewer to visialize generated MIDI
- Versitaile MIDI generation parameters
- BPM autodetection and optional override
- Light and dark modes that save on close
- Very user friendly and modern design 
- Seamless transition between panels
- Modern look and feel
- 
![Semantic description of image](RECStudio/images/rec1.png "Image Title")


Limitations:
- Some instruments have sound profiles(timbre) that make it hard to analyze and put a definitive note to.
- Works best with Flutes and wind instruments, Piano, Guitar.
- May struggle with percussive instruments (Xylophone...)
- Will have trouble processing lower pitch instruments (Bass, chelo...)
- will not process multiple notes playing at once. It will only detect the loudest note/frequency during FFT analsys
    - Will add multi-note detection in the future
- Users need to play as perfectly as possible for best result
- Users must play using constant BPM. Ex. Classical guitar with Rubato (variable tempo) may struggle with MIDI generation
- MIDI generation may struggle with very dynamic rhythms such as third notes

API/Libraries used:
- jMusic
- QuiFFT
- TrackAnalyzer
		- Commons math 3 (library for track analyzer)
		- jtransforms (library for track analyzer)
- Flat Laf (UI Look and feel)
- jAudio
- Commons io 
- Swing libraries
		- jGoodies
		- migLayout
