package crawler;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

public class GUI implements IGUI{

	private static IGUI instance = null;
	
	private static JLabel label = new JLabel("<html>WEB CRAWLER<br>V1.0</html>", JLabel.CENTER);

	GUI() {
	}

	public static IGUI getInstance() {
		if (instance == null) {
			instance = new GUI();
			label.setBorder(BorderFactory.createLineBorder(Color.black));
			label.setVerticalAlignment(JLabel.TOP);
			label.setHorizontalAlignment(JLabel.CENTER);
			JScrollPane scroller = new JScrollPane(label, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			scroller.setPreferredSize(new Dimension(350, 200));
		    JFrame frame = new JFrame();
		    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    frame.getContentPane().add(scroller);
		    frame.setPreferredSize(new Dimension(400, 270));
		    frame.pack();
		    frame.setLocationRelativeTo(null);
	        frame.add(scroller);
		    frame.setVisible(true);
		}
		return instance;
	}
	
	public void sendMessage(String message) {
		String temp = label.getText().split("<html>")[1].split("</html>")[0];
		message = "<html>"+temp+"<br>"+message+"</html>";
		label.setText(message);
	}


}
