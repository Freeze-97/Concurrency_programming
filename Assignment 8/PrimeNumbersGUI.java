package se.miun.toya1800.dt062g.jpaint;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Java GUI program which show all prime numbers
 * until the max number is reached. 
 * It does this via the SwingWorker class
 * 
* @author  Tommy Yasi (toya1800)
*/

public class PrimeNumbersGUI {
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					new PrimeNumbersGUI(10000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	// JFrame variables 
	private JButton startButton;
	private JButton cancelButton;
	private JTextArea textArea;
	private JLabel primesFound; // Text field on how many primes has been found
	private JProgressBar progressBar;
	private JFrame jFrame;
	
	// Other variables
	private final int LIMIT; // The max number
	private AtomicInteger primes = new AtomicInteger(0);
	private AtomicInteger numbersChecked = new AtomicInteger(0);
	private ArrayList<PrimeNumWorker> swingWorkers;

	// Constructor just used to design the frame
	public PrimeNumbersGUI(int maxNumber) {
		LIMIT = maxNumber; // Initialize the maxNumber
		createJFrame();
		createWorkers();
	}
	
	private void createJFrame() {
		// Create JFrame and set options
		jFrame = new JFrame("PrimeSeeker");
	    jFrame.setVisible(true);
		jFrame.setLayout(new BorderLayout());
		jFrame.setLocationRelativeTo(null); // Position it in the middle
		
		// Close window
		jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		// Text showing the number span
		JLabel numberSpan = new JLabel("Span of numbers 1.." + LIMIT, SwingConstants.CENTER);
		numberSpan.setFont(new Font(jFrame.getName(), Font.PLAIN, 18));
		jFrame.add(numberSpan, BorderLayout.PAGE_START);
		
		// Create textArea
		textArea = new JTextArea(8,41);
		textArea.setWrapStyleWord(true);
		textArea.setMargin(new Insets(5, 5, 5, 5));
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		
		// Add texArea with scroll option into frame
		JScrollPane scroll = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); 
		JPanel textArea_progressBar = new JPanel(new BorderLayout());
		textArea_progressBar.add(scroll, BorderLayout.CENTER);
		
		// Set values for progressBar
		progressBar = new JProgressBar(0, 100);
		progressBar.setStringPainted(true);
		textArea_progressBar.add(progressBar, BorderLayout.PAGE_END);
		jFrame.add(textArea_progressBar);
		
		// Add buttons
		JPanel lowerPanel = new JPanel(new FlowLayout());
		primesFound = new JLabel("Primes found: ");
		startButton = new JButton("Start");
		cancelButton = new JButton("Cancel");
		cancelButton.setEnabled(false);
		lowerPanel.add(startButton);
		lowerPanel.add(cancelButton);
		lowerPanel.add(primesFound);
		jFrame.add(lowerPanel, BorderLayout.PAGE_END); // Add the lower panel to the bottom
		
	    // Makes the window as big as the content
		jFrame.pack();
	}
	
	private void createWorkers() {
		int chunkSize = LIMIT / 10; 
		int startNum = 1; // Start number for a SwingWorker
		int endNum = chunkSize; // The last number for a SwingWorker
		swingWorkers = new ArrayList<PrimeNumWorker>();
		
		// Create 10 workers
		for(int i = 0; i < 10; i++) {
		
			/*
			 * Check if the endNum is higher than the LIMIT.
			 * This will most likely be the case with the
			 * last worker which won't have a span as big as
			 * the chunk size.
			 */
			if(endNum > LIMIT) {
				endNum = LIMIT;
			}
			
			// Create the worker and add it to the ArrayList
			PrimeNumWorker worker = new PrimeNumWorker(startNum, endNum); 
			swingWorkers.add(worker);
			
			// Change start and end number
			startNum += chunkSize;
			endNum += chunkSize;
		}
		
		// Add button listeners, execute workers
		startButton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e) {
				startButton.setEnabled(false);
				cancelButton.setEnabled(true);
				for(PrimeNumWorker w : swingWorkers) {
					w.execute();
				}
			}
		});
	 
		cancelButton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){  
				cancelButton.setEnabled(false);
				for(PrimeNumWorker w : swingWorkers) {
					w.cancel(true);
				}
			}  
		});  
	}
	
	private synchronized void updateStatus(List<Integer> primeNums) {
	}
	
	private class PrimeNumWorker extends SwingWorker<Void, Integer> {
		// Local variables
		private int start;
		private int end;
		
		public PrimeNumWorker(int pStart, int pEnd) {
			super();
			start = pStart;
			end = pEnd;
			
			// Add listener
			addActionListener();
		}
		
		private void addActionListener() {
			addPropertyChangeListener(
					new PropertyChangeListener() {
						public void propertyChange(PropertyChangeEvent evt) {
							if ("progress" == evt.getPropertyName()) {
								progressBar.setValue((Integer)evt.getNewValue());
							}
						}
					}); 
		}
	
		private boolean isPrime(long number) {
			if(number == 1) {
				return false;
			}
			long limit = (long) Math.sqrt(number)+1;
			for(long i=2;i<limit;++i)
				if((number%i)==0) {
					return false;
				}
			return true;
		}
	
		@Override
		protected Void doInBackground() throws Exception {
			int number = start;
			while (!isCancelled() && number <= end) {
				if(isPrime(number)) {
					publish(number); // Publish to process
					primesFound.setText("Primes found: " + primes.incrementAndGet());
				}
				// Next value
				number++;
				
				// Updating progress-bar, shown as numsChecked out of LIMIT
				setProgress(Math.round((100 * numbersChecked.incrementAndGet()) / LIMIT));
				
				// Just so it takes some time
				Thread.sleep(10);
			}
			return null;
		}	
	
		@Override
		protected void process(List<Integer> chunks) {
			super.process(chunks);
			for (int number : chunks) {
				textArea.append(number + ", ");
			}
		}
	
		@Override
		protected void done() {
			super.done();
			cancelButton.setEnabled(false);
		}
	}
}
