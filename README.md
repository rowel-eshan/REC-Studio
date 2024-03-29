# RECStudio

## Welcome to RECStudio, an experimental audio to MIDI platform.

## Capabilites:
- Recording audio in WAV
- WAV to MIDI conversion
- Recording Playback
- MIDI playback using different instruments
- Viewer to visualize generated MIDI
- Versatile MIDI generation parameters
- BPM autodetection and optional override
- Light and dark modes that save on close
- Seamless transition between panels
- Modern look and feel

## Homepage
<img src="https://github.com/rowel-eshan/RECStudio/blob/main/images/rec1.png" width=80% height=80%>

## Recorder and player
<img src="https://github.com/rowel-eshan/RECStudio/blob/main/images/rec2.png" width=80% height=80%>

## Midi generation
<img src="https://github.com/rowel-eshan/RECStudio/blob/main/images/rec3.png" width=80% height=80%>

## Limitations:
- Some instruments have sound profiles(timbre) that make it hard to analyze and put a definitive note to.
- Works best with Flutes and wind instruments, Piano, Guitar.
- May struggle with percussive instruments (Xylophone...)
- Will have trouble processing lower pitch instruments (Bass, Cello...)
- Will not process multiple notes playing at once. It will only detect the loudest note/frequency during FFT analysis
    - Will add multi-note detection in the future
- Users need to play as perfectly as possible for best result
- Users must play using constant BPM. Ex. Classical guitar with Rubato (variable tempo) may struggle with MIDI generation
- MIDI generation may struggle with very dynamic rhythms such as third notes

## API/Libraries used:
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
