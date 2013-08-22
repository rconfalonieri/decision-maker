package decision.maker.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import decision.maker.DecisionMaker;



/**
 * This class describes the command panel
 * of the Decision Interface.
 */

public class DecisionMenu implements ActionListener {
	
	// Components :
	private JPanel pan = new JPanel();
	private JComboBox decisionType = new JComboBox();
	private JTextArea result = new JTextArea(20,2);
	// --- Buttons :
	private JButton Compute = new JButton("Compute");
	private JButton details = new JButton("Details");
	private JButton clear = new JButton("Clear");
	private JButton help = new JButton("Help");
	// SAVE LOAD buttons :
	private JButton save = new JButton("Save");
	private JButton load = new JButton("Load"); 
	private JButton about = new JButton("About"); 
	// Type of decision (criteria) :
	private String[] decTypeList = {"Pessimistic","Optimistic"};
	// The equivalences warning label :
	private JLabel equ = new JLabel(" ");
	
	private String[] reasonerList = {"DLV","psmodels","posPsmodels"};
	private JComboBox reasonerBox = new JComboBox(reasonerList);
	private JLabel reasonerBoxLabel = new JLabel("Choose the Reasoner");
	

	public DecisionMenu() {
		
		// Setting the criteria list :
		//decTypeList = decisionMaker.getDecisionTypeList();
		//decTypeList = ;
		
		// ComboBox used to set the Decision type :
		decisionType = new JComboBox(decTypeList);
		// We will use a BoxLayout :
		pan.setLayout(new BoxLayout(pan,BoxLayout.PAGE_AXIS));
		
		// The result text area :
		JScrollPane scrollPane = new JScrollPane(result);
		result.setEditable(false);
		result.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(),
				BorderFactory.createLoweredBevelBorder()));
		
		// Setting up the decisionType chooser :
		decisionType.setSelectedIndex(0);
		decisionType.addActionListener(this);
		
		
				
		reasonerBox.setSelectedIndex(0);
		reasonerBox.addActionListener(this);
		
		// The Compute button :D :
		Compute.setMnemonic(KeyEvent.VK_C);
		Compute.setActionCommand("Compute");
		Compute.setToolTipText("This button lauches the "
				+"compution of the best Decision.");
		Compute.setPreferredSize(new Dimension(100,0));
		
		// The Details button :
		details.setMnemonic(KeyEvent.VK_D);
		details.setActionCommand("details");
		details.setToolTipText("This button allows you to "
				+"see the details of the computation.");
		details.setEnabled(false);
		details.setPreferredSize(new Dimension(100,0));
		
		// The Clear button :
		clear.setMnemonic(KeyEvent.VK_L);
		clear.setActionCommand("clear");
		clear.setToolTipText("This button clears the bases and the details.");
		clear.setPreferredSize(new Dimension(100,0));
		
		// The Help button :
		help.setMnemonic(KeyEvent.VK_H);
		help.setActionCommand("help");
		help.setToolTipText("This button displays the Help.");
		help.setPreferredSize(new Dimension(100,0));
		
		// The Save button :
		save.setMnemonic(KeyEvent.VK_S);
		save.setActionCommand("save");
		save.setToolTipText("This button allow you to save the Knowledge, the Preferences "
				+"and/or the Decisions.");
		save.setPreferredSize(new Dimension(100,0));
		
		// The Load button :
		load.setMnemonic(KeyEvent.VK_O);
		load.setActionCommand("load");
		load.setToolTipText("This button allow you to load the Knowledge, the Preferences or the Decisions.");
		load.setPreferredSize(new Dimension(100,0));
		
		about.setActionCommand("about");
		about.setToolTipText("This button allow you to know about this project");
		about.setPreferredSize(new Dimension(100,0));
		
		// ComboBox label :
		JLabel lbl1 = new JLabel("Choose your criterium :");
		
		// The result label :
		JLabel lbl0 = new JLabel("Best Decision found :");
		
		// The panel containing all this stuff :) :
		// Titled Border :
		TitledBorder promptTitle = BorderFactory.createTitledBorder("Command panel :");
		pan.setBorder(promptTitle);
		
		// Creating a secondary panel for all the components rows.
		// One panel for each row (each using a BoxLayout) :
		JPanel row0 = new JPanel();
		row0.setLayout(new BoxLayout(row0,BoxLayout.LINE_AXIS));
		JPanel row01 = new JPanel();
		row01.setLayout(new BoxLayout(row01,BoxLayout.LINE_AXIS));
		JPanel row02 = new JPanel();
		row02.setLayout(new BoxLayout(row02,BoxLayout.LINE_AXIS));
		JPanel row1 = new JPanel();
		row1.setLayout(new BoxLayout(row1,BoxLayout.LINE_AXIS));
		JPanel row2 = new JPanel();
		row2.setLayout(new BoxLayout(row2, BoxLayout.LINE_AXIS));
		JPanel row3 = new JPanel();
		row3.setLayout(new BoxLayout(row3, BoxLayout.LINE_AXIS));
		JPanel row4 = new JPanel();
		row4.setLayout(new BoxLayout(row4, BoxLayout.LINE_AXIS));
		JPanel row5 = new JPanel();
		row5.setLayout(new BoxLayout(row5, BoxLayout.LINE_AXIS));
		
		
		// Row0 : --- lbl0 -G-
		row0.add(Box.createRigidArea(new Dimension(10,0)));
		row0.add(lbl0);
		row0.add(Box.createHorizontalGlue());
		
		row01.add(Box.createRigidArea(new Dimension(10,0)));
		row01.add(reasonerBoxLabel);
		row01.add(Box.createHorizontalGlue());
		
		row02.add(Box.createRigidArea(new Dimension(10,0)));
		row02.add(reasonerBox);
		row02.add(Box.createHorizontalGlue());
		
		// Row1 : --- lbl1 -G-
		row1.add(Box.createRigidArea(new Dimension(10,0)));
		row1.add(lbl1);
		row1.add(Box.createHorizontalGlue());
		
		// Row2 : --- decisionType -G- Compute ---
		row2.add(Box.createRigidArea(new Dimension(10,0)));
		row2.add(decisionType);
		row2.add(Box.createHorizontalGlue());
		row2.add(Compute);
		row2.add(Box.createRigidArea(new Dimension(10,0)));
		
		// Row3 : -G- details ---
		row3.add(Box.createRigidArea(new Dimension(10,0)));
		row3.add(load);
		row3.add(Box.createHorizontalGlue());
		row3.add(details);
		row3.add(Box.createRigidArea(new Dimension(11,0)));
		
		// Row4 : --- save -G- clear ---
		row4.add(Box.createRigidArea(new Dimension(10,0)));
		row4.add(save);
		row4.add(Box.createHorizontalGlue());
		row4.add(help);
		row4.add(Box.createRigidArea(new Dimension(11,0)));
		
		// Row5 : --- load -G- help ---
		row5.add(Box.createRigidArea(new Dimension(10,0)));
		row5.add(clear);
		row5.add(Box.createHorizontalGlue());
		row5.add(about);
		row5.add(Box.createRigidArea(new Dimension(11,0)));
		
		
		
		// Adding the differents rows to the mains panel :
		pan.add(Box.createRigidArea(new Dimension(0,5)));
		pan.add(row0);
		pan.add(Box.createRigidArea(new Dimension(0,5)));
		pan.add(result);
		pan.add(equ);
		pan.add(Box.createRigidArea(new Dimension(0,5)));
		pan.add(row01);
		pan.add(row02);
		//pan.add(Box.createRigidArea(new Dimension(0,30)));
		pan.add(Box.createRigidArea(new Dimension(0,5)));
		pan.add(row1);
		pan.add(row2);
		pan.add(Box.createRigidArea(new Dimension(0,5)));
		pan.add(row3);
		pan.add(Box.createRigidArea(new Dimension(0,5)));
		pan.add(row4);
		pan.add(Box.createRigidArea(new Dimension(0,5)));
		pan.add(row5);
		//pan.add(Box.createRigidArea(new Dimension(0,40)));
		pan.add(Box.createRigidArea(new Dimension(0,5)));
		
		
	}
	
	public void actionPerformed(ActionEvent e) {
		
	}
	
	/**
	 * @return Returns the decTypeList.
	 */
	public String[] getDecTypeList() {
		return decTypeList;
	}
	/**
	 * @return Returns the pan.
	 */
	public JPanel getPan() {
		return pan;
	}
	/**
	 * @return Returns the clear.
	 */
	public JButton getClear() {
		return clear;
	}
	/**
	 * @return Returns the decisionType.
	 */
	public JComboBox getDecisionType() {
		return decisionType;
	}
	
	public JComboBox getReasonerType() {
		return reasonerBox;
	}
	
	/**
	 * @return Returns the details.
	 */
	public JButton getDetails() {
		return details;
	}
	/**
	 * @return Returns the Compute.
	 */
	public JButton getCompute() {
		return Compute;
	}
	/**
	 * @return Returns the help.
	 */
	public JButton getHelp() {
		return help;
	}
	/**
	 * @return Returns the result.
	 */
	public JTextArea getResult() {
		return result;
	}
	/**
	 * @param text The result to set.
	 */
	public void setResultText(String text) {
		result.setText("");
		result.append(text);
	}
	
	public void clearResult() {
		setResultText("");
	}
	
	/**
	 * @return Returns the load.
	 */
	public JButton getLoad() {
		return load;
	}
	
	/**
	 * @return Returns the about.
	 */
	public JButton getAbout() {
		return about;
	}
	
	/**
	 * @return Returns the save.
	 */
	public JButton getSave() {
		return save;
	}

	/**
	 * @return Returns the equ.
	 */
	public JLabel getEqu() {
		return equ;
	}

	
}
