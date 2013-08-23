package decision.maker.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

public class DecisionPromptEditor extends JPanel implements ActionListener {
	
	public JTextField formField;
	public JTextField wField;
	
	
	
	public static void graphicSetup(DecisionPromptEditor toBuild) {
		
		toBuild.setLayout(new BoxLayout(toBuild,BoxLayout.PAGE_AXIS));
		
		JPanel row = new JPanel();
		row.setLayout(new BoxLayout(row,BoxLayout.LINE_AXIS));
		
		
		toBuild.add(row);
		toBuild.add(Box.createRigidArea(new Dimension(0,10)));
	}
	
	public DecisionPromptEditor(String form_, String weight_) {
		
		graphicSetup(this);
		
		JPanel row = new JPanel();
		row.setLayout(new BoxLayout(row,BoxLayout.LINE_AXIS));
		
		formField = new JTextField(10);
		formField.setText(form_);
		
		wField = new JTextField(3);
		wField.setText(weight_);
		
		formField.setMaximumSize(new Dimension(250,25));
		wField.setMaximumSize(new Dimension(25,25));
		row.add(formField);
		row.add(wField);
		
		this.add(row);
	}
	public DecisionPromptEditor(String form_) {
		
		graphicSetup(this);
		
		JPanel row = new JPanel();
		row.setLayout(new BoxLayout(row,BoxLayout.LINE_AXIS));
		
		formField = new JTextField(10);
		formField.setText(form_);
		
		
		formField.setMaximumSize(new Dimension(300,25));
		row.add(formField);
		
		this.add(row);
	}
	public void actionPerformed(ActionEvent e) {
		
		
		
	}
	
	
}

