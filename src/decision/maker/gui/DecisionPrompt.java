package decision.maker.gui;





import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import decision.maker.handler.Knowledge;

public class DecisionPrompt extends JPanel implements KeyListener,MouseListener {

	// Position of the caret on the textfield :
	private int caret = 0;
	// Components of the panel :
	protected JList inputList;
	protected JList weightList;
	protected JTextField textField;
	protected JTextField weightField;
	private JPanel panel;

	// Lists we use to display in the JLists :
	public DefaultListModel formulas = new DefaultListModel();
	protected DefaultListModel weightsL = new DefaultListModel();

	// Boolean to check if the panel uses weights :
	private boolean hasWeights;

	// BUILDER :

	public DecisionPrompt(String name, String inputLabelText, String outputLabelText, boolean weights, JPanel pan) {

		// Setting up the variables :
		hasWeights = weights;
		panel = pan;

		TitledBorder promptTitle = BorderFactory.createTitledBorder(name);

		this.setBorder(promptTitle);

		// PANELS AND LAYOUTS :

		JPanel input = new JPanel();
		input.setLayout(new BoxLayout(input, BoxLayout.X_AXIS));
		JPanel output = new JPanel();
		output.setLayout(new BoxLayout(output, BoxLayout.X_AXIS));
		JPanel inputLabel = new JPanel();
		JPanel outputLabel = new JPanel();

		JScrollPane listScroller2 = new JScrollPane(weightList);

		// LISTS :

		inputList = new JList(formulas);
		inputList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		//inputList.setLayoutOrientation(JList.HORIZONTAL);
		inputList.setBorder(BorderFactory.createEtchedBorder());
		inputList.addKeyListener(this);
		inputList.addMouseListener(this);

		if (hasWeights) {
			weightList = new JList(weightsL);
			weightList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			//weightList.setLayoutOrientation(JList.HORIZONTAL);
			weightList.setBorder(BorderFactory.createEtchedBorder());
			weightList.addKeyListener(this);
			weightList.addMouseListener(this);
			// Link the selection :
			inputList.addListSelectionListener(new SelectionLinker(inputList, weightList));
			weightList.addListSelectionListener(new SelectionLinker(weightList, inputList));
		}

		// SCROLLERS :

		JScrollPane listScroller = new JScrollPane(inputList);
		listScroller.setAlignmentX(LEFT_ALIGNMENT);
		listScroller.setPreferredSize(new Dimension(224,180));

		if (hasWeights) {
			listScroller.setPreferredSize(new Dimension(202,180));
			listScroller2 = new JScrollPane(weightList);
			listScroller2.setAlignmentX(LEFT_ALIGNMENT);
			listScroller2.setPreferredSize(new Dimension(48,180));
		}

		// FIELDS :

		textField = new JTextField(20);
		textField.addKeyListener(this);


		if (hasWeights) {
			textField.setColumns(18);
			weightField = new JTextField("1.0", 4);
			weightField.setHorizontalAlignment(JTextField.TRAILING);
			weightField.addKeyListener(this);
			weightField.addFocusListener(new FL(weightField));
		}

		// LABELS :

		JLabel iL = new JLabel(inputLabelText, JLabel.LEFT);

		inputLabel.add(iL);

		JLabel oL = new JLabel(outputLabelText, JLabel.LEFT);
		outputLabel.add(oL);

		//Add Components to this panel.
		input.add(textField);
		if (hasWeights) {input.add(weightField);}

		output.add(listScroller);
		if (hasWeights) {output.add(listScroller2);}
		add(inputLabel);
		add(input);
		add(outputLabel);
		add(output);
	}

	/* Checks the typed key and perform the corresponding 
	 * action (enter a new item in a list or delete the
	 * selected components of a list).
	 */

	public void keyTyped(KeyEvent e) {

		boolean weightCheckOk = true;
		if (e.getKeyChar() == '\n' && (textField.isFocusOwner() || weightField.isFocusOwner())) {
			String newFormula = textField.getText();
			String newWeight = ""; 

			// Extracting the new weight from the field :
			if (hasWeights) {
				newWeight = weightField.getText();
				double weight = Double.valueOf(newWeight);
				if (weight <=0.0 || weight > 1.0) {
					JOptionPane.showMessageDialog(panel, "A weight cannot be <= 0 or > 1!");
					weightCheckOk = false;
				}
			}

			// If there is an error, do nothing :
			if (newFormula.length()!=0 && (!this.hasWeights || newWeight.length()!=0) && weightCheckOk) { 
				// Appending the new formuls and eventually weight to the list :
				formulas.addElement(newFormula);
				if (hasWeights) {
					weightsL.addElement(newWeight);
				}

				// Setting the fileds for the next input :
				textField.setText("");
				textField.requestFocusInWindow();
				if (hasWeights) {
					weightField.setText("1.0");
					}
			}
		}

		if (e.getKeyChar() == 127 && !textField.isFocusOwner() && (!hasWeights || !weightField.isFocusOwner())) {

			// Delete the selected items in the list :

			// We simpely get through the vector as many times as the number
			// of deleted items (!! updating the indices each time !!) and
			// delete them :) :
			int[] indices = inputList.getSelectedIndices();
			for(int i=0; i<indices.length; i++) {
				formulas.remove(indices[i]);
				if (hasWeights) {
					weightsL.remove(indices[i]);
				}
				// Updating the indices :
				for(int j=i+1; j<indices.length; j++) {
					indices[j]--;
				}
			}
		}
	}

	// Others methods for the KeyListener :
	public void keyPressed(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}

	// Methods for caret listener :
	public void caretUpdate(CaretEvent e) {
		caret = e.getDot();
	}
	// The interesting method of the MouseListener :
	public void mouseClicked(MouseEvent e) {
		// Doubleclick :
		if (e.getClickCount()==2 && inputList.getSelectedIndex()>-1) {
			int i = inputList.getSelectedIndex();
			DecisionPromptEditor formEdit = (hasWeights	?
					new DecisionPromptEditor(formulas.get(i).toString(),weightsL.get(i).toString()) :
						new DecisionPromptEditor(formulas.get(i).toString()));  
			JOptionPane.showConfirmDialog(this,
					formEdit,
					"Edit",
					JOptionPane.CLOSED_OPTION,
					JOptionPane.INFORMATION_MESSAGE,
					null);
			boolean weightCheckOk = true;
			if (hasWeights) {
				if (formEdit.wField.getText()!=null) {
					double weight = Double.valueOf(formEdit.wField.getText());
					if (weight <=0.0 || weight > 1.0) {
						JOptionPane.showMessageDialog(panel, "A weight cannot be <= 0 or > 1!");
						weightCheckOk = false;
					}
					else 
						weightsL.set(i,formEdit.wField.getText());

				}

			}
			if (weightCheckOk)
				formulas.set(i,formEdit.formField.getText()); 
		}

	}
	// Others methods for the MouseListener :
	public void mouseEntered(MouseEvent e) {};
	public void mouseExited(MouseEvent e) {};
	public void mousePressed(MouseEvent e) {};
	public void mouseReleased(MouseEvent e) {};

	// FOCUS LISTENER :

	class FL implements FocusListener {

		private JTextField theLinkedField;

		public FL(JTextField theFieldToHandleWith) {
			theLinkedField = theFieldToHandleWith;
		}

		// Weight field : if ot gains the focus, the field
		// is enterely selected :
		public void focusGained(FocusEvent e) {
			theLinkedField.selectAll();
		}
		// and unselected when the focus is lost ;) :
		public void focusLost(FocusEvent e) {
			theLinkedField.select(0,0);
		}
	}

	// LIST SELECTION LISTENER (to link the items and their weights) :
	// This class manages to link the formulas list with the
	// weights list while one of them is selected :
	// Two linkers are needed to a two-ways link as one listener handles
	// only one link direction.

	class SelectionLinker implements ListSelectionListener {

		private JList list1;
		private JList list2;

		public SelectionLinker(JList list1_, JList list2_) {

			list1 = list1_;
			list2 = list2_;

		}

		public void valueChanged(ListSelectionEvent e) {
			if (list1.isFocusOwner()) {list2.setSelectedIndices(list1.getSelectedIndices());}
		}
	}

	// FORMULAS BUILDER :
	/**
	 * @return Returns the formulas vector.
	 */
	public ArrayList<Knowledge> getFormulasVector() {

		// Returned formulas Vector :
		ArrayList<Knowledge> forms = new ArrayList<Knowledge>();
		// We loop on the formulas List :
		for (int i=0; i<formulas.size(); i++) {
			// We have to make the difference between the two builders
			// (with and without the weights) :
			if (hasWeights) {
				//System.out.println("getFormulasVector "+formulas.get(i)+" "+Double.parseDouble((String) weightsL.get(i)));
				forms.add(new Knowledge( (String) formulas.get(i), Double.parseDouble((String) weightsL.get(i))) );
				//forms.add(new Knowledge((String) formulas.get(i),false));
			}
			else {
				//System.out.println("getFormulasVector "+formulas.get(i));
				forms.add(new Knowledge( ((String) formulas.get(i)).trim(),false));
			} 
		}
		return forms;
	}

	public void setFormulasFromVector(ArrayList<Knowledge> vv) {

		// We have to first clear the formulas and weights Lists :
		formulas.clear();
		weightsL.clear();

		String form;
		String wght = ""; // To abuse Java :D.
		// Loop on the Vector to fill the Lists from :
		for (int i=0; i<vv.size(); i++) {

			// We split the formula and put the right
			// part to the right List :
			form = vv.get(i).toString().split(" ; ")[0];
			formulas.addElement(form);
			if (hasWeights) {
				wght = vv.get(i).toString().split(" ; ")[1];
				weightsL.addElement(wght);
			}
		}
	}

	/**
	 * Append the txt to the textField.
	 * 
	 * @param txt
	 */

	public void appendText(String txt) {
		textField.setText(textField.getText().concat(txt));
	}

	/**
	 * inserts the txt to the textField at character #position.
	 * 
	 * @param txt
	 */

	public void insertText(int position, String txt) {
		String temp = textField.getText().substring(0,position);
		temp = temp.concat(txt);
		if (position<textField.getText().length()) {
			temp = temp.concat(textField.getText().substring(position));
		}
		textField.setText(temp);
	}
	/**
	 * @return Returns the textField.
	 */
	public JTextField getTextField() {
		return textField;
	}

	public void clear() {
		if (hasWeights) {weightsL.removeAllElements();}
		inputList.removeAll();
		formulas.removeAllElements();
		textField.removeAll();
		if (hasWeights) {
			weightList.removeAll();
			weightsL.removeAllElements();
			weightField.removeAll();
		}

	}

	/**
	 * @return Returns the caret.
	 */
	public int getCaret() {
		return caret;
	}
}
