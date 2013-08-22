package decision.maker;

import global.Statics;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import decision.maker.handler.BestDecision;
import decision.maker.handler.DecisionMakerHandler;
import decision.maker.handler.Knowledge;
import decision.maker.handler.ASPDecisionMakerHandler;
import decision.maker.handler.PASPDecisionMakerHandler;
import decision.maker.handler.WeightedRule;


import decision.maker.reasoner.DLVReasonerHandler;


public class DecisionMaker extends Agent {

	//private DecisionMakerHandler decisionMakerHandler;
	private JPanel panel;
	private String[] reasonerList = {"DLV","psmodels","posPsmodels"};
	private boolean guiCheckOkDM;


	public DecisionMaker() {
		super();
		//argumentationManager = new ArgumentationManager();
	}

	public DecisionMaker(JPanel pan) {
		super();
		
		this.panel = pan;

	}

	/*
	public DecisionMaker(String string, ArrayList<Knowledge> formulasVector,
			ArrayList<Knowledge> formulasVector2) {
		super(string,formulasVector,formulasVector2);

	}*/

	public BestDecision findBestDecision() {


		String reasoner = reasonerList[reasonerType];
		BestDecision bestDecision = null;
		guiCheckOkDM = true;

		//if we are in Windows only the DLV will work
		if (System.getProperty("os.name").toLowerCase().indexOf("win") >= 0 && !reasoner.equals("DLV")) {
			JOptionPane.showMessageDialog(panel, "On Windows, only DLV works! \n Please select the DLV reasoner");	
			guiCheckOkDM = false;
		}
		else {
			
			if (reasoner.equals("DLV") || reasoner.equals("psmodels"))
				decisionMakerHandler = new ASPDecisionMakerHandler(getKnowledgeBase(), getPreferenceBase(), getDecisions(), panel, reasoner);
			if (reasoner.equals("posPsmodels"))
				decisionMakerHandler = new PASPDecisionMakerHandler(getKnowledgeBase(), getPreferenceBase(), getDecisions(), panel, reasoner);


			if (decisionMakerHandler.isGuiCheckOk()) {

				if (decisionType==0) {
					Statics.add("********Decision Maker->computePessimisticDecisionUnderUncertainty()********");
					//Statics.add("********Decision Maker->using the "++" solver");
					bestDecision = decisionMakerHandler.computePessimisticDecisionUnderUncertainty();
					Statics.add("********Decision Maker->computePessimisticDecisionUnderUncertainty()********");
				}
				if (decisionType==1) {
					Statics.add("********Decision Maker->computeOptimisticDecisionUnderUncertainty()********");
					bestDecision = decisionMakerHandler.computeOptimisticDecisionUnderUncertainty();
					Statics.add("********Decision Maker->computeOptimisticDecisionUnderUncertainty()********");
				}
			}
			else 
				guiCheckOkDM = false;
		}
		return bestDecision;
	}





	/**
	 * clears the log
	 */
	public void clearAll() {

		Statics.clear();

		//decisionMakerHandler = new ASPDecisionMakerHandler(knowledgeBase, preferenceBase, decisions, panel, reasonerList[reasonerType]);

	}


	/**
	 * clears the log
	 */
	public void clearLog() {

		Statics.clear();
	}



	/**
	 * @return Returns the decsionType as a String
	 */
	public String getDecisionTypeS() {
		return decisionTypeList[decisionType];
	}
	/**
	 * @return Returns the decisionType.
	 */
	public int getDecisionType() {
		return decisionType;
	}
	/**
	 * @param decisionType The decisionType to set.
	 */
	public void setDecisionType(int decisionType) {
		this.decisionType = decisionType;
	}




	/**
	 * @return Returns the decisionTypeList.
	 */
	public String[] getDecisionTypeList() {
		return decisionTypeList;
	}

	/**
	 * @return the guiCheckOkDM
	 */
	public boolean isGuiCheckOkDM() {
		return guiCheckOkDM;
	}

	/**
	 * @param guiCheckOkDM the guiCheckOkDM to set
	 */
	public void setGuiCheckOkDM(boolean guiCheckOkDM) {
		this.guiCheckOkDM = guiCheckOkDM;
	}

	




}
