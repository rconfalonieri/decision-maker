package decision.maker.gui;

import global.Statics;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.io.*;

import javax.swing.*;


import decision.maker.DecisionMaker;
import decision.maker.handler.BestDecision;
import decision.maker.handler.Knowledge;



/**
 * This class manages the layout of the Decision Interface.
 * 
 * It also implements the listeners 
 * 
 * 
 */
public class DecisionPanel implements ActionListener {

	// We use a 2x2 GridLayout :
	private JPanel pan = new JPanel(new GridLayout(2,2));
	// Selected informs about the last selected prompt.
	// The initial value of Selected means that nothing
	// have been selected yet.
	private int selected = 0; 

	// We need a fake Agent :
	//private DeliberatingAgent fakeAgent = new DeliberatingAgent();
	// We need an Offers Vector :
	private ArrayList<Knowledge> decisions = new ArrayList<Knowledge>();
	// We also need a Vocable :
	private ArrayList<String> vocable = new ArrayList<String>();

	// Used Decision instance :
	private DecisionMaker decisionMaker;

	// Panels used :
	private DecisionPrompt kPrompt; // Knowledge base
	private DecisionPrompt gPrompt; // Goals base
	private DecisionPrompt xPrompt; // Offers base
	private DecisionMenu command; // Command panel

	// The file chooser (for loading and saving Agents/Offers) :
	private JFileChooser fc = new JFileChooser();

	// BUILDER :

	public DecisionPanel() {

		// Creating the prompts :
		kPrompt = new DecisionPrompt("Knowledge",
				"Enter or load knowledge/certainty:",
				"Rules  / certainty levels :", true);
		gPrompt = new DecisionPrompt("Preferences",
				"Enter or load preferences/priority :",
				"Preferences / priorities :", true);
		xPrompt = new DecisionPrompt("Decisions",
				"Enter or load your decisions :",
				"Decisions :", false);


		// Creating the Command pane :
		decisionMaker = new DecisionMaker(pan);
		decisionMaker.setName("DecisionMaker Agent");
		command = new DecisionMenu();

		// Adding the prompts to the panel :
		pan.add(kPrompt);
		pan.add(gPrompt);
		pan.add(xPrompt);
		// Adding the Command pane to the panel :
		pan.add(command.getPan());

		// Adding the listeners for the different buttons
		// tools of the Command pane :
		command.getCompute().addActionListener(this);
		command.getDetails().addActionListener(this);
		command.getClear().addActionListener(this);
		command.getHelp().addActionListener(this);
		command.getSave().addActionListener(this);
		command.getLoad().addActionListener(this);
		command.getAbout().addActionListener(this);

		// Creating the special FocusListener (deals with the "selected"):
		DecisionFocusListener blop = new DecisionFocusListener(this);
		// Adding the same instance of the listener to the three prompts :
		kPrompt.getTextField().addFocusListener(blop);
		gPrompt.getTextField().addFocusListener(blop);
		xPrompt.getTextField().addFocusListener(blop);
		// Used Decision tools :
		//this.decisionMaker = decisionMaker;

		//added
		//command.getDetails().setEnabled(true);
		customiseFileChooser();
	}

	

	public void actionPerformed(ActionEvent e) {

		// Customising the File Chooser :
		//fc.setAccessory(new SaveTypeRadio(fc));


		if ("Compute".equals(e.getActionCommand())) {

			// Setting up the Decision instance :
			decisionMaker.clearLog();

			decisionMaker.setDecisionType(command.getDecisionType().getSelectedIndex());
			decisionMaker.setReasonerType(command.getReasonerType().getSelectedIndex());

			//in case we re run and have modified the knowledge
			decisionMaker.setKnowledgeBase(kPrompt.getFormulasVector());
			decisionMaker.setPreferenceBase(gPrompt.getFormulasVector());
			decisionMaker.setDecisions(xPrompt.getFormulasVector());


			BestDecision bestDecision = null;
			//String res = "";
			/*if (command.getReasonerType().getSelectedIndex()==0) {
				 res = decisionMaker.findBestOffer(decisionMaker,xPrompt.getFormulasVector(vocable)).toString().split(" ; ")[0];
			}
			else */
			bestDecision = decisionMaker.findBestDecision();


			//decisionMaker.getDlvHandler().testDLVSolver();

			//String res = decTool.findBestOffer(fakeAgent,decisions).toString().split(" ; ")[0];
			// Displaying the result :
			String result = "";
			if (bestDecision!=null) {
				result = bestDecision.toString();


			}
			else {
				//if the gui check passed it means that no solution was found, otherwise no computation wa triggered.
				if (decisionMaker.isGuiCheckOkDM())
					result = "No optimal solution found";

			}

			command.setResultText(result);
			command.getDetails().setEnabled(true);
			// The case when we compute only empty bases
			// (then, no details available) :
			/*
			if (result.length()!=0) {
				command.getDetails().setEnabled(true);
			}

			if (Statics.equivalences>0) {
				command.getEqu().setText("There are "+Statics.equivalences+" equivalent decisions.");
			}*/
		}
		if ("details".equals(e.getActionCommand())) {
			JOptionPane.showConfirmDialog(pan,
					new DecisionDetailsInterface(Statics.getVerbose()),
					"Details",
					JOptionPane.CLOSED_OPTION,
					JOptionPane.INFORMATION_MESSAGE,
					null);
		}
		if ("help".equals(e.getActionCommand())) {
			JOptionPane.showMessageDialog(pan,
					new DecisionHelpInterface());
		}
		if ("clear".equals(e.getActionCommand())) {

			// Clear confirmation :
			if (JOptionPane.showConfirmDialog(pan,
					"This command will clear all the bases !\n"+
							"Do you confirm your command ?", "Confirm",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {

				// Clearing :
				command.getDetails().setEnabled(false);

				vocable.clear();

				kPrompt.formulas.clear();
				kPrompt.weightsL.clear();
				gPrompt.formulas.clear();
				gPrompt.weightsL.clear();
				xPrompt.formulas.clear();
				command.clearResult();
			}
		}
		if ("save".equals(e.getActionCommand())) {

			// Setting the file chooser accessory :
			//SaveTypeRadio acc = (SaveTypeRadio) fc.getAccessory();
			//acc.loading(false);

			// Displaying the file chooser panel :
			int returnVal = fc.showSaveDialog(pan);

			// If the user clicked OK :
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				// If not OfferOnly then we save the Agent :
				//if (!(acc.getState().equals("offerOnly"))) {
					//linkIHM2Agent();
					try {
						saveAgent(fc.getSelectedFile());
					}
					catch(IOException exc) {
						JOptionPane.showMessageDialog(pan,"Cannot create "+fc.getSelectedFile()+" !");
					}
				//}
				/*
				// If not AgentOnly then we save the Offer :
				if (!(acc.getState().equals("agentOnly"))) {
					try {this.saveOffers(fc.getSelectedFile());}
					catch(IOException exc) {
						JOptionPane.showMessageDialog(pan,"Cannot create "+fc.getSelectedFile()+" !");
					}
				}*/
			}
			// Else, nothing :D.
		}
		if ("load".equals(e.getActionCommand())) {

			// Setting up the file chooser accessory :
			//SaveTypeRadio acc = (SaveTypeRadio) fc.getAccessory();
			//acc.loading(true);

			// Displaying the file chooser panel :
			int returnVal = fc.showOpenDialog(pan);

			// If the user clicked OK :
			if (returnVal == JFileChooser.APPROVE_OPTION) {

				// Getting the File name :
				String fileName = fc.getSelectedFile().toString();

				// If we are loading an Agent :
				if (fileName.substring(fileName.length()-3).equals(".dm")) {
					// First clearing the agent :
					decisionMaker.resetAgent();
					kPrompt.clear();
					gPrompt.clear();
					xPrompt.clear();
					// Then loading it :
					try {
						loadAgent(fc.getSelectedFile(),vocable);
					}
					catch(IOException exc) {
						JOptionPane.showMessageDialog(pan,"Cannot load "+fc.getSelectedFile()+" !");
					}
					// Finally updating the IHM :
					linkAgent2IHM();
				}
				/*
				// If we are loading an Offer :
				else if (fileName.substring(fileName.length()-4).equals(".ofr")) {
					// First clearing the decisions :
					decisions.clear();
					xPrompt.clear();
					// Then loading it :
					try {loadDecisions(fc.getSelectedFile(),vocable);}
					catch(IOException exc) {
						JOptionPane.showMessageDialog(pan,"Cannot load "+fc.getSelectedFile()+" !");
					}
					// Finally updating the IHM :
					xPrompt.setFormulasFromVector(decisions);
				}
				// If trying to load anything else :) :
				else {
					JOptionPane.showMessageDialog(fc,"Unknown File format !");
				}*/
			}
		}
		if ("about".equals(e.getActionCommand())) {
			JOptionPane.showMessageDialog(pan, "This prototype implements algorithms presented in the IJAR paper: \n "+
					"Using Possibilistic Logic for Modeling Qualitative Decision: Answer Set Programming Algorithms \n" +
					"Credits: Roberto Confalonieri, Henri Prade");
		}
	}

	
	
	private void linkIHM2Agent() {

		
		/*decisionMaker = new DecisionMaker("Stand alone Decision Tool agent",
				kPrompt.getFormulasVector(),
				gPrompt.getFormulasVector()
				xPrompt.getFormulasVector());*/
	}

	/**
	 * This method sets the Prompt Lists from
	 * the bases of the fakeAgent.
	 * 
	 */
	public void linkAgent2IHM() {

		//kPrompt.setFormulasFromVector(fakeAgent.getK());
		//gPrompt.setFormulasFromVector(fakeAgent.getG());
		kPrompt.setFormulasFromVector(decisionMaker.getKnowledgeBase());
		gPrompt.setFormulasFromVector(decisionMaker.getPreferenceBase());
		xPrompt.setFormulasFromVector(decisionMaker.getDecisions());
	}

	

	
	/**
	 * save the agent information in a file .dm
	 * @param outputFile
	 * @throws IOException
	 */
	private void saveAgent(File outputFile) throws IOException {

		String fileName = outputFile.toString();

		// Filter .agt :
		if (fileName.lastIndexOf('.')!=-1) {
			fileName = fileName.substring(0,fileName.lastIndexOf('.'));
		}
		fileName = fileName.concat(".dm");

		outputFile = new File(fileName);

		FileOutputStream outputStream = new FileOutputStream(outputFile);

		outputStream.write("%% --- Decision Maker Agent save file ---\n".getBytes());
		outputStream.write("%% Name :\n".getBytes());

		outputStream.write(decisionMaker.getName().getBytes());
		outputStream.write("\n\n%% Knowledge  :\n\n".getBytes());
		
		ArrayList<Knowledge> knowledge = kPrompt.getFormulasVector();
		
		for (int i=0; i<knowledge.size(); i++) {
			//System.out.println("Know "+knowledge.get(i).toFileFormat());
			outputStream.write((knowledge.get(i).toFileFormat()+"\n").getBytes());
		}

		outputStream.write("\n\n%% Preferences :\n\n".getBytes());

		ArrayList<Knowledge> preferences = gPrompt.getFormulasVector();
		
		for (int i=0; i<preferences.size(); i++) {
			//System.out.println("Pref "+preferences.get(i).toFileFormat());
			outputStream.write((preferences.get(i).toFileFormat()+"\n").getBytes());
		}
		
		outputStream.write("\n\n%% Decisions :\n\n".getBytes());

		//System.out.println("parsing decisions...");
		ArrayList<Knowledge> decisions = xPrompt.getFormulasVector();
		
		for (int i=0; i<decisions.size(); i++) {
			//System.out.println("Dec "+decisions.get(i).toFileFormat());
			outputStream.write((decisions.get(i).toFileFormat().replace(" ; 0.0", "")+"\n").getBytes());
		}

		outputStream.close();
	}

	
	/**
	 * UI method, load an agent from a .agt file
	 * @param inputFile
	 * @param vocable
	 * @throws IOException
	 */
	private void loadAgent(File inputFile, ArrayList<String> vocable) throws IOException {

		LineNumberReader inputStream = new LineNumberReader(new FileReader(inputFile));

		String line = inputStream.readLine();

		// Header comments and empty lines are ignored : 
		while(line.length()==0 || line.charAt(0)=='%') {
			line = inputStream.readLine();
		}
		// Storing the Agent name :
		decisionMaker.setName(line);

		line = inputStream.readLine();

		// Comments and empty lines are ignored : 
		while (line!=null && line.length()==0
				&& !line.equalsIgnoreCase("%% Knowledge :")) {
			line = inputStream.readLine();
		}


		// We're now dealing with the Knowledge Base block :
		while (line!=null && !line.equals("%% Preferences :")) {
			if (line.length()!=0 && line.charAt(0)!='%') {
				decisionMaker.getKnowledgeBase().add(new Knowledge(line,true));	
			}
			line = inputStream.readLine();
		}


		// We're dealing now with the Goals Base block :
		while (line!=null && !line.equals("%% Decisions :")) {
			if (line.length()!=0 && line.charAt(0)!='%') {
				decisionMaker.getPreferenceBase().add(new Knowledge(line,true));
			}
			line = inputStream.readLine();
		}


		// Comments and empty lines are ignored : 
		while(line!=null && (line.length()==0 || line.charAt(0)=='%')) {
			line = inputStream.readLine();
		}

		

		// We're now dealing with the Decisions Base block :
		while (line!=null && (line.length()!=0 && line.charAt(0)!='%')) {
			decisionMaker.getDecisions().add(new Knowledge(line,true));
			line = inputStream.readLine();
		}

		// Comments and empty lines are ignored : 
		while(line!=null && (line.length()==0 || line.charAt(0)=='%')) {
			line = inputStream.readLine();
		}

		

		inputStream.close();
	}

	private void customiseFileChooser() {
		String fileContents = null;
		File absolutePath = null;
		String defaultDirectory = null;
		String os = System.getProperty("os.name").toLowerCase();
		if (os.indexOf("win") >= 0) {
			//System.out.println(getClass().getClassLoader().getResource("resources/default.txt"));
			fileContents = getClass().getClassLoader().getResource("resources/default.txt").toString();

			if (fileContents!=null) {
				
				String tmp = fileContents.toString().replace("rsrc:","").replace("file:","");
				//absolutePath = new File(tmp);
				absolutePath = new File(tmp.substring(0, tmp.length()-12));
				//tmpPath = URL_RESOURCES.toString();	
				defaultDirectory = absolutePath.getAbsolutePath();
				//System.out.println(defaultDirectory);
			}
		}
		//if mac
		if (os.indexOf("mac") >= 0) {

			fileContents = getClass().getResource("/resources/default.txt").toString();
			if (fileContents!=null) {
				

				String tmp = fileContents.toString().replace("rsrc:","").replace("file:","");
				//absolutePath = new File(tmp);
				absolutePath = new File(tmp.substring(0, tmp.length()-12));
				//tmpPath = URL_RESOURCES.toString();	
				defaultDirectory = absolutePath.getAbsolutePath();
			}
		}
		//if linux
		if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0) {
			fileContents = getClass().getResource("/resources/default.txt").toString();

			if (fileContents!=null) {
				
				String tmp = fileContents.toString().replace("rsrc:","").replace("file:","");
				//absolutePath = new File(tmp);
				absolutePath = new File(tmp.substring(0, tmp.length()-12));
				//tmpPath = URL_RESOURCES.toString();	
				defaultDirectory = absolutePath.getAbsolutePath();
			}	
		}
		//String tmp = IOUtils.toString(new FileInputStream("path.txt"));
		File theDirectory = new File(defaultDirectory);
		fc.setCurrentDirectory(theDirectory);
		
	}


	/**
	 * @return Returns the pan.
	 */
	public JPanel getPan() {
		return pan;
	}
	/**
	 * @return Returns the gPrompt.
	 */
	public DecisionPrompt getGPrompt() {
		return gPrompt;
	}
	/**
	 * @return Returns the kPrompt.
	 */
	public DecisionPrompt getKPrompt() {
		return kPrompt;
	}
	/**
	 * @return Returns the xPrompt.
	 */
	public DecisionPrompt getXPrompt() {
		return xPrompt;
	}
	/**
	 * @return Returns the decisions.
	 */
	public ArrayList<Knowledge> getOffers() {
		return decisions;
	}
	/**
	 * @param selected The selected to set.
	 */
	public void setSelected(int selected) {
		this.selected = selected;
	}
	/**
	 * @return Returns the selected.
	 */
	public int getSelected() {
		return selected;
	}
}
