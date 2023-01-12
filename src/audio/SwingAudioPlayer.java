package audio;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;

import net.miginfocom.swing.MigLayout;

/**
 * A Swing-based audio player program.
 * NOTE: Can play only WAVE (*.wav) file.
 * @author www.codejava.net
 * Hevily modified by Rowel Eshan
 */
@SuppressWarnings("serial")
public class SwingAudioPlayer extends JPanel implements ActionListener {
	private AudioPlayer player = new AudioPlayer();
	private Thread playbackThread;
	private PlayingTimer timer;

	private boolean isPlaying = false;
	private boolean isPause = false;

	private String audioFilePath = "files/recording.wav";

	private JLabel labelFileName = new JLabel("");
	private JLabel labelTimeCounter = new JLabel("00:00:00");
	private JLabel labelDuration = new JLabel("00:00:00");

	private JButton buttonPlay = new JButton("Play");
	private JButton buttonPause = new JButton("Pause");

	private JSlider sliderTime = new JSlider();

	// Icons used for buttons
	private ImageIcon iconPlay = new ImageIcon("images/Play.png");
	private ImageIcon iconStop = new ImageIcon("images/Stop.png");
	private ImageIcon iconPause = new ImageIcon("images/Pause.png");



	public SwingAudioPlayer() {
		//super("Java Audio Player");
		setLayout(new MigLayout("", "[][679.00,grow][]", "[][]"));

		buttonPlay.setFont(new Font("UD Digi Kyokasho NP-R", Font.PLAIN, 25));//added by Rowel Eshan
		buttonPlay.setIcon(iconPlay);
		buttonPlay.setEnabled(true);

		buttonPause.setFont(new Font("UD Digi Kyokasho NP-R", Font.PLAIN, 25));//added by Rowel Eshan
		buttonPause.setIcon(iconPause);
		buttonPause.setEnabled(false);

		labelTimeCounter.setFont(new Font("UD Digi Kyokasho NP-R", Font.PLAIN, 20)); //added by Rowel Eshan
		labelDuration.setFont(new Font("UD Digi Kyokasho NP-R", Font.PLAIN, 20));//added by Rowel Eshan
		labelFileName.setFont(new Font("UD Digi Kyokasho NP-R", Font.PLAIN, 18));//added by Rowel Eshan

		sliderTime.setPreferredSize(new Dimension(400, 20));
		sliderTime.setEnabled(false);
		sliderTime.setValue(0);
		sliderTime.setPreferredSize(new Dimension(800, 8));
		
		add(labelDuration, "cell 2 0");
		
		add(sliderTime, "cell 1 0,alignx center");
		
		add(labelTimeCounter, "cell 0 0");
		
		add(buttonPlay, "flowx,cell 1 1,alignx center,aligny top");
		
		add(buttonPause, "cell 1 1,alignx center,aligny top");

		buttonPlay.addActionListener(this);
		buttonPause.addActionListener(this);

	}

	/**
	 * Handle click events on the buttons.
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		if (source instanceof JButton) {
			JButton button = (JButton) source;
			if (button == buttonPlay) {
				if (!isPlaying) {
					playBack();
				} else {
					stopPlaying();
				}
			} else if (button == buttonPause) {
				if (!isPause) {
					pausePlaying();
				} else {
					resumePlaying();
				}
			}
		}
	}


	/**
	 * Start playing back the sound.
	 */
	private void playBack() {
		timer = new PlayingTimer(labelTimeCounter, sliderTime);
		timer.start();
		isPlaying = true;
		playbackThread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {

					buttonPlay.setText("Stop");
					buttonPlay.setIcon(iconStop);
					buttonPlay.setEnabled(true);

					buttonPause.setText("Pause");
					buttonPause.setEnabled(true);

					player.load(audioFilePath);
					timer.setAudioClip(player.getAudioClip());
					sliderTime.setMaximum((int) player.getClipSecondLength());

					labelDuration.setText(player.getClipLengthString());
					player.play();

					resetControls();

				} catch (UnsupportedAudioFileException ex) {
					JOptionPane.showMessageDialog(SwingAudioPlayer.this,  
							"The audio format is unsupported!", "Error", JOptionPane.ERROR_MESSAGE);
					resetControls();
					ex.printStackTrace();
				} catch (LineUnavailableException ex) {
					JOptionPane.showMessageDialog(SwingAudioPlayer.this,  
							"Could not play the audio file because line is unavailable!", "Error", JOptionPane.ERROR_MESSAGE);
					resetControls();
					ex.printStackTrace();
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(SwingAudioPlayer.this,  
							"I/O error while playing the audio file!", "Error", JOptionPane.ERROR_MESSAGE);
					resetControls();
					ex.printStackTrace();
				}

			}
		});

		playbackThread.start();
	}

	private void stopPlaying() {
		isPause = false;
		buttonPause.setText("Pause");
		buttonPause.setEnabled(false);
		timer.reset();
		timer.interrupt();
		player.stop();
		playbackThread.interrupt();
	}

	private void pausePlaying() {
		buttonPause.setText("Resume");
		isPause = true;
		player.pause();
		timer.pauseTimer();
		playbackThread.interrupt();
	}

	private void resumePlaying() {
		buttonPause.setText("Pause");
		isPause = false;
		player.resume();
		timer.resumeTimer();
		playbackThread.interrupt();		
	}

	private void resetControls() {
		timer.reset();
		timer.interrupt();

		buttonPlay.setText("Play");
		buttonPlay.setIcon(iconPlay);

		buttonPause.setEnabled(false);

		isPlaying = false;		
	}


}