package decision.maker.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

public class DecisionPromptEditor extends JPanel implements ActionListener,CaretListener {
	
	public JTextField formField;
	public JTextField wField;
	
	private int caret = 0;
	
//	private JButton and = new JButton("And");
//	private JButton or = new JButton("Or");
//	private JButton not = new JButton("Not");
	
	public static void graphicSetup(DecisionPromptEditor toBuild) {
		
		toBuild.setLayout(new BoxLayout(toBuild,BoxLayout.PAGE_AXIS));
		
		JPanel row = new JPanel();
		row.setLayout(new BoxLayout(row,BoxLayout.LINE_AXIS));
		
		/*
		toBuild.and.setMnemonic(KeyEvent.VK_A);
		toBuild.and.setActionCommand("and");
		toBuild.and.setToolTipText("&");
		toBuild.and.setPreferredSize(new Dimension(40,30));
		
		toBuild.or.setMnemonic(KeyEvent.VK_R);
		toBuild.or.setActionCommand("or");
		toBuild.or.setToolTipText("|");
		toBuild.or.setPreferredSize(new Dimension(40,0));
		
		toBuild.not.setMnemonic(KeyEvent.VK_N);
		toBuild.not.setActionCommand("not");
		toBuild.not.setToolTipText("!");
		toBuild.not.setPreferredSize(new Dimension(40,0));
		
		toBuild.and.addActionListener(toBuild);
		toBuild.or.addActionListener(toBuild);
		toBuild.not.addActionListener(toBuild);
		
		
		row.add(toBuild.and);
		row.add(Box.createRigidArea(new Dimension(10,0)));
		row.add(toBuild.or);
		row.add(Box.createRigidArea(new Dimension(10,0)));
		row.add(toBuild.not);
		*/
		
		toBuild.add(row);
		toBuild.add(Box.createRigidArea(new Dimension(0,10)));
	}
	
	public DecisionPromptEditor(String form_, String weight_) {
		
		graphicSetup(this);
		
		JPanel row = new JPanel();
		row.setLayout(new BoxLayout(row,BoxLayout.LINE_AXIS));
		
		formField = new JTextField(10);
		formField.setText(form_);
		formField.addCaretListener(this);
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
		formField.addCaretListener(this);
		
		formField.setMaximumSize(new Dimension(300,25));
		row.add(formField);
		
		this.add(row);
	}
	public void actionPerformed(ActionEvent e) {
		
		int pos = caret;
		if ("and".equals(e.getActionCommand())) {
			insertText(caret,"&");
			formField.requestFocusInWindow();
			formField.setCaretPosition(pos+1);
		}
		if ("or".equals(e.getActionCommand())) {
			insertText(caret,"|");
			formField.requestFocusInWindow();
			formField.setCaretPosition(pos+1);
		}
		if ("not".equals(e.getActionCommand())) {
			insertText(caret,"!");
			formField.requestFocusInWindow();
			formField.setCaretPosition(pos+1);
		}
		
	}
	public void caretUpdate(CaretEvent e) {
		caret = e.getDot();
	}
	
	public void insertText(int position, String txt) {
		String temp = formField.getText().substring(0,position);
		temp = temp.concat(txt);
		if (position<formField.getText().length()) {
			temp = temp.concat(formField.getText().substring(position));
		}
		formField.setText(temp);
	}
}

