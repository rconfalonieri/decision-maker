package decision.maker.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Vector;
import javax.swing.*;


/**
 * This is the Details panel for the Decision Tool.
 * 
 */
public class DecisionDetailsInterface extends JPanel {

	public DecisionDetailsInterface(Vector log) {
		
		this.setPreferredSize(new Dimension(500,300));
		this.setLayout(new GridLayout(1,1));
		
		JTextArea textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		for (int i=0;i<log.size();i++) {
			textArea.append(((String)log.get(i))+"\n");
		}
		
		this.add(scrollPane);
	}
}
