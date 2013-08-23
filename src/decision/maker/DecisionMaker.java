package decision.maker;

import global.Statics;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

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

	
	/*public DecisionMaker(String name, ArrayList<Knowledge> knowledge,
			ArrayList<Knowledge> preference,ArrayList<Knowledge> decision) {
		super(name,knowledge,preference,decision);

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

				long startTimeNano = System.nanoTime( );
				long startCpuTimeNano = Statics.getCpuTime();
				long startSystemTimeNano = Statics.getSystemTime( );
				long startUserTimeNano   = Statics.getUserTime( );
				
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
				
				//"Wall clock time" is the real-world elapsed time experienced by a user waiting for a task to complete
				long taskTimeNano  = System.nanoTime( ) - startTimeNano;
				//"CPU time" is user time plus system time. It's the total time spent using a CPU for your application.
				long taskCpuTimeNano    = Statics.getCpuTime() - startCpuTimeNano;
				//User time" is the time spent running your application's own code.
				long taskUserTimeNano    = Statics.getUserTime( ) - startUserTimeNano;
				//"System time" is the time spent running OS code on behalf of your application (such as for I/O).
				long taskSystemTimeNano  = Statics.getSystemTime( ) - startSystemTimeNano;
				
				Statics.add("*********");
				Statics.add("Execution time:");
				
				Statics.add("You have waited for (nanosecs): "+taskTimeNano);
				Statics.add("You have waited for (millisecs): "+TimeUnit.MILLISECONDS.convert(taskTimeNano, TimeUnit.NANOSECONDS));
				
				Statics.add("Total time spent by the CPU (nanosecs): "+taskCpuTimeNano);
				Statics.add("Total time spent by the CPU (millisecs): "+TimeUnit.MILLISECONDS.convert(taskCpuTimeNano, TimeUnit.NANOSECONDS));
				
				Statics.add("User computation time (nanosecs): "+taskUserTimeNano);
				Statics.add("User computation time (millisecs): "+TimeUnit.MILLISECONDS.convert(taskUserTimeNano, TimeUnit.NANOSECONDS));
				
				Statics.add("Total System computation time (nanosecs): "+taskSystemTimeNano);
				Statics.add("Total System computation time (millisecs): "+TimeUnit.MILLISECONDS.convert(taskSystemTimeNano, TimeUnit.NANOSECONDS));
				
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
