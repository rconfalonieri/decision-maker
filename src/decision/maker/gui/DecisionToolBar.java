package decision.maker.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * 
 */
public class DecisionToolBar extends JToolBar implements ActionListener {

	// 3 Buttons :
	/*private JButton and = new JButton("And");
	private JButton or = new JButton("Or");
	private JButton not = new JButton("Not");*/
	
	
	// We have to get a pointer to the panel because of the listener :
	private DecisionPanel panel;
	
	// BUILDER :
	
	public DecisionToolBar(DecisionPanel panelBestOffer) {
		
		panel = panelBestOffer;
		
		// Building the buttons :
		/*and.setMnemonic(KeyEvent.VK_A);
		and.setActionCommand("and");
		and.setToolTipText("&");
		and.setPreferredSize(new Dimension(40,0));
		
		or.setMnemonic(KeyEvent.VK_R);
		or.setActionCommand("or");
		or.setToolTipText("|");
		or.setPreferredSize(new Dimension(40,0));
		
		not.setMnemonic(KeyEvent.VK_N);
		not.setActionCommand("not");
		not.setToolTipText("!");
		not.setPreferredSize(new Dimension(40,0));
		
		// Adding the listener to each button :
		and.addActionListener(this);
		or.addActionListener(this);
		not.addActionListener(this);
		
		// Adding the buttons to the panel :
		add(and);
		add(Box.createRigidArea(new Dimension(10,0)));
		add(or);
		add(Box.createRigidArea(new Dimension(10,0)));
		add(not);*/
		
		/*add(Box.createRigidArea(new Dimension(10,0)));
		add(reasonerBoxLabel);
		add(reasonerBox);*/
		
		add(Box.createRigidArea(new Dimension(440,0))); // The glue does not work here :/
	}
	
	public void actionPerformed(ActionEvent e) {
		
		// Appending the right symbol to the right field :
		
		if ("and".equals(e.getActionCommand())) {
			if (panel.getSelected()==1) {
				int pos = panel.getKPrompt().getCaret();
				panel.getKPrompt().insertText(pos,"&");
				panel.getKPrompt().getTextField().requestFocusInWindow();
				panel.getKPrompt().getTextField().setCaretPosition(pos+1);
			}
			if (panel.getSelected()==2) {
				int pos = panel.getGPrompt().getCaret();
				panel.getGPrompt().insertText(pos,"&");
				panel.getGPrompt().getTextField().requestFocusInWindow();
				panel.getGPrompt().getTextField().setCaretPosition(pos+1);
			}
			if (panel.getSelected()==3) {
				int pos = panel.getXPrompt().getCaret();
				panel.getXPrompt().insertText(pos,"&");
				panel.getXPrompt().getTextField().requestFocusInWindow();
				panel.getXPrompt().getTextField().setCaretPosition(pos+1);
			}
		}
		if ("or".equals(e.getActionCommand())) {
			if (panel.getSelected()==1) {
				int pos = panel.getKPrompt().getCaret();
				panel.getKPrompt().insertText(pos,"|");
				panel.getKPrompt().getTextField().requestFocusInWindow();
				panel.getKPrompt().getTextField().setCaretPosition(pos+1);
			}
			if (panel.getSelected()==2) {
				int pos = panel.getGPrompt().getCaret();
				panel.getGPrompt().insertText(pos,"|");
				panel.getGPrompt().getTextField().requestFocusInWindow();
				panel.getGPrompt().getTextField().setCaretPosition(pos+1);
			}
			if (panel.getSelected()==3) {
				int pos = panel.getXPrompt().getCaret();
				panel.getXPrompt().insertText(pos,"|");
				panel.getXPrompt().getTextField().requestFocusInWindow();
				panel.getXPrompt().getTextField().setCaretPosition(pos+1);
			}
		}
		if ("not".equals(e.getActionCommand())) {
			if (panel.getSelected()==1) {
				int pos = panel.getKPrompt().getCaret();
				panel.getKPrompt().insertText(pos,"!");
				panel.getKPrompt().getTextField().requestFocusInWindow();
				panel.getKPrompt().getTextField().setCaretPosition(pos+1);
			}
			if (panel.getSelected()==2) {
				int pos = panel.getGPrompt().getCaret();
				panel.getGPrompt().insertText(pos,"!");
				panel.getGPrompt().getTextField().requestFocusInWindow();
				panel.getGPrompt().getTextField().setCaretPosition(pos+1);
			}
			if (panel.getSelected()==3) {
				int pos = panel.getXPrompt().getCaret();
				panel.getXPrompt().insertText(pos,"!");
				panel.getXPrompt().getTextField().requestFocusInWindow();
				panel.getXPrompt().getTextField().setCaretPosition(pos+1);
			}
		}
	}
}
