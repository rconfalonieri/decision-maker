package decision.maker.gui;

import java.awt.*;
import javax.swing.*;

/**
 * This is the Help panel for the Decision Tool.
 * 
 */
public class DecisionHelpInterface extends JPanel {

	public DecisionHelpInterface() {
		this.setPreferredSize(new Dimension(300,300));
		this.setLayout(new GridLayout(1,1));
		
		JTextArea textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		
		textArea.append("Decision Maker v0.1: \n" +
		"1. \"Load\" .agt and .dec files (some of them in the resources folder) \n"+
		"2. Choose the criterion of the decision maker \n"+
		"3. Press \"Compute\" \n" +
		"4. Press \"Details\" for computation details \n"+
		"5. \"Clear\" before loading another decision making problem");
		this.add(scrollPane);
	}
}
