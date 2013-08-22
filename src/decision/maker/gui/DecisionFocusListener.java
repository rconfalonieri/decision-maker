package decision.maker.gui;


import java.awt.event.*;

/**
 * This class implements the focus listener
 * which sets the right select state of the
 * DecisionPanel.

 */
public class DecisionFocusListener implements FocusListener {
	
	private DecisionPanel panel;
	
	public DecisionFocusListener(DecisionPanel pan) {
		
		panel = pan;
		
	}
	
	public void focusGained(FocusEvent e) {
		// Selected gets a value depending on the last selected prompt : 
		if (panel.getKPrompt().getTextField().equals(e.getComponent())) {panel.setSelected(1);}
		if (panel.getGPrompt().getTextField().equals(e.getComponent())) {panel.setSelected(2);}
		if (panel.getXPrompt().getTextField().equals(e.getComponent())) {panel.setSelected(3);}
	}
	
	public void focusLost(FocusEvent e) {
		
	}
}
