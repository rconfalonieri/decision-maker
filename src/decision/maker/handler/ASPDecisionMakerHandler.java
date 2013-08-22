package decision.maker.handler;

import global.Statics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import decision.maker.reasoner.DLVReasonerHandler;
import decision.maker.reasoner.IReasonerHandler;
import decision.maker.reasoner.PosPsmodelsReasonerHandler;
import decision.maker.reasoner.PsmodelsReasonerHandler;

public class ASPDecisionMakerHandler extends DecisionMakerHandler {



	public ASPDecisionMakerHandler(ArrayList<Knowledge> knowledgeBase, ArrayList<Knowledge> preferenceBase, ArrayList<Knowledge> decisions, JPanel panel, String reasoner) {

		super(knowledgeBase,preferenceBase, decisions, panel, reasoner);

		if (Statics.DEBUG_DECISION_MAKER_REASONER)
			System.out.println("ASPDecisionMakerHandler");

		if (reasoner.equals("DLV"))
			reasonerHandler = new DLVReasonerHandler();
		if (reasoner.equals("psmodels"))
			reasonerHandler = new PsmodelsReasonerHandler();

	}

	@Override
	public BestDecision computePessimisticDecisionUnderUncertainty() {

		BestDecision bestDecision = null;
		boolean finished = false;
		double alpha = 0;

		if (Statics.DEBUG_DECISION_MAKER_REASONER) 
			System.out.println("computePessimisticDecisionUnderUncertainty()->trying with alpha="+alpha);

		Statics.add("computePessimisticDecisionUnderUncertainty()->trying with alpha="+alpha);



		knowledgeBaseCut = getStratifiedKnowledgeCut(alpha, false);


		if (knowledgeBaseCut==null)
			System.out.println("computePessimisticDecisionUnderUncertainty->knowledgeBaseCut is null...");


		preferenceBaseCut = getProritizedPreferencesCut(1-alpha, false);
		if (preferenceBaseCut==null)
			System.out.println("computePessimisticDecisionUnderUncertainty->preferenceBaseCut is null...");


		/*if (Statics.DEBUG_DECISION_MAKER_REASONER)
			System.out.println("computePessimisticDecisionUnderUncertainty()->trying with alpha="+alpha);*/
		BestDecision pessimisticLabel = computePessimisticDecision(alpha);
		if (pessimisticLabel!=null) {
			bestDecision = pessimisticLabel;

			Statics.add(bestDecision.toString());

			while (!finished && alpha < 1) {
				alpha = increaseAlpha(alpha);
				if (Statics.DEBUG_DECISION_MAKER_REASONER) 
					System.out.println("computePessimisticDecisionUnderUncertainty()->trying with alpha="+alpha);

				Statics.add("computePessimisticDecisionUnderUncertainty()->trying with alpha="+alpha);

				knowledgeBaseCut = getStratifiedKnowledgeCut(alpha, false);
				preferenceBaseCut = getProritizedPreferencesCut(1-alpha, true);
				pessimisticLabel = computePessimisticDecision(alpha);
				if (pessimisticLabel == null) {
					finished = true;
					Statics.add("No better optimal pessimistic decision found, computation finished...");
				}
				else {
					bestDecision = pessimisticLabel;
					Statics.add(bestDecision.toString());
					Statics.add("Trying to improve the pessimistic utility...");

				}
			}
		}

		if (bestDecision!=null) {
			ArrayList<String> decs = bestDecision.getDecision();

			Statics.add(bestDecision.toString());

			if (decs!=null && decs.size()>0) {
				if (Statics.DEBUG_DECISION_MAKER_REASONER) {
					System.out.print("computePessimisticDecisionUnderUncertainty()->best decision is: <{");
					for (int i=0;i<decs.size();i++) {
						if (i==decs.size()-1)
							System.out.print(decs.get(i));
						else
							System.out.print(decs.get(i)+",");
					}
					System.out.print("},");
					System.out.println(bestDecision.getUtility()+">");
				}
			}
		}

		return bestDecision;
	}



	@Override
	public BestDecision computeOptimisticDecisionUnderUncertainty() {

		BestDecision optimisticLabel = null;
		double alpha = 0;

		if (Statics.DEBUG_DECISION_MAKER_REASONER) 
			System.out.println("computeOptimisticDecisionUnderUncertainty()->trying with alpha="+alpha);

		Statics.add("computeOptimisticDecisionUnderUncertainty()->trying with alpha="+alpha);

		knowledgeBaseCut = getStratifiedKnowledgeCut(alpha, true);

		if (knowledgeBaseCut==null)
			System.out.println("computeOptimisticDecisionUnderUncertainty->knowledgeBaseCut is null...");


		preferenceBaseCut = getProritizedPreferencesCut(alpha, true);
		if (preferenceBaseCut==null)
			System.out.println("computeOptimisticDecisionUnderUncertainty->preferenceBaseCut is null...");


		/*if (Statics.DEBUG_DECISION_MAKER_REASONER)
			System.out.println("computePessimisticDecisionUnderUncertainty()->trying with alpha="+alpha);*/
		optimisticLabel = computeOptimisticDecision(alpha);

		while (optimisticLabel==null && alpha < 1) {
			alpha = increaseAlpha(alpha);
			if (Statics.DEBUG_DECISION_MAKER_REASONER) 
				System.out.println("computeOptimisticDecisionUnderUncertainty()->trying with alpha="+alpha);
			Statics.add("computeOptimisticDecisionUnderUncertainty()->trying with alpha="+alpha);

			knowledgeBaseCut = getStratifiedKnowledgeCut(alpha, true);
			preferenceBaseCut = getProritizedPreferencesCut(alpha, true);
			optimisticLabel = computeOptimisticDecision(alpha);
		}

		if (optimisticLabel!=null) {

			Statics.add(optimisticLabel.toString());

			ArrayList<String> decs = optimisticLabel.getDecision();

			if (decs!=null && decs.size()>0) {
				if (Statics.DEBUG_DECISION_MAKER_REASONER) {
					System.out.print("computeOptimisticDecisionUnderUncertainty()->best decision is: <{");
					for (int i=0;i<decs.size();i++) {
						if (i==decs.size()-1)
							System.out.print(decs.get(i));
						else
							System.out.print(decs.get(i)+",");
					}
					System.out.print("},");
					System.out.println(optimisticLabel.getUtility()+">");
				}
			}
		}
		return optimisticLabel;
	} 

	@Override
	public BestDecision computePessimisticDecision(double alpha) {

		BestDecision pessimisticLabel = null;
		ArrayList<String> model;
		double utility = alpha;
		String decision;

		if (Statics.DEBUG_DECISION_MAKER_REASONER)
			System.out.println("computePessimisticDecision with alpha: "+alpha);

		String logicProgrammingEncoding = reasonerHandler.pessimisticDecisionMakingToLP(knowledgeBaseCut, preferenceBaseCut, decisionsAvailable); 
		Vector models = reasonerHandler.computeDecisionMaking(logicProgrammingEncoding);

		if (Statics.DEBUG_DECISION_MAKER_REASONER)
			reasonerHandler.printModels(models);

		if (models!=null && models.size()>0) {
			ArrayList<String> decisions = null;
			for (int i=0;i<decisionsAvailable.size();i++) {
				for (int j=0;j<models.size();j++) {
					model = (ArrayList<String>) models.get(j);
					/*if (Statics.DEBUG_DECISION_MAKER_REASONER)
						System.out.println("before getDecisionLiteral");*/
					decision = getDecisionLiteral(model,decisionsAvailable.get(i));
					if (Statics.DEBUG_DECISION_MAKER_REASONER)
						System.out.println("after getDecisionLiteral "+decision);

					if (decision!=null) {
						//System.out.println("Decision is "+decision);
						if (pessimisticLabel==null) {
							//System.out.println("Pess label crated "+decision);
							decisions = new ArrayList<String>();
							pessimisticLabel = new BestDecision();
							pessimisticLabel.setUtility(utility);
							//System.out.println("Adding "+decision);
							decisions.add(decision);
						}
						else {
							//System.out.println("Adding "+decision);
							decisions.add(decision);
						}

					}
					if (pessimisticLabel!=null && decisions!=null) {
						//System.out.println("setting "+decisions.size());
						pessimisticLabel.setDecision(decisions);
					}
				}
			}
		}
		return pessimisticLabel;
	}

	@Override
	public String getDecisionLiteral(ArrayList<String> model,
			String decisionLiteral) {

		if (reasonerHandler instanceof DLVReasonerHandler) {

			if (Statics.DEBUG_DECISION_MAKER_REASONER)
				System.out.println("getDecisionLiteral()");
			for (int i=0;i<model.size();i++) {
				if (Statics.DEBUG_DECISION_MAKER_REASONER)
					System.out.println("decisionLiteral "+"ass("+decisionLiteral.replace("-","n")+")"+ " literal "+model.get(i));
				if (model.get(i).equals("ass("+decisionLiteral.replace("-","n")+")"))
					//return model.get(i);
					return decisionLiteral;
			}


		}
		else {
			if (Statics.DEBUG_DECISION_MAKER_REASONER)
				System.out.println("getDecisionLiteral()");
			for (int i=0;i<model.size();i++) {
				if (Statics.DEBUG_DECISION_MAKER_REASONER)
					System.out.println("decisionLiteral "+"ass("+decisionLiteral+")"+ " literal "+model.get(i));
				if (model.get(i).equals("ass("+decisionLiteral+")"))
					//return model.get(i);
					return decisionLiteral;
			}
		}
		//System.out.println("Fatal error");
		return null;

	}

	@Override
	public BestDecision computeOptimisticDecision(double alpha) {

		BestDecision optimisticLabel = null;
		ArrayList<String> model;
		double utility = (1-alpha);
		String decision;

		if (Statics.DEBUG_DECISION_MAKER_REASONER)
			System.out.println("computeOptimisticDecision with alpha: "+alpha);

		String lpEncoding = reasonerHandler.optimisticDecisionMakingToLP(knowledgeBaseCut, preferenceBaseCut, decisionsAvailable); 
		Vector models = reasonerHandler.computeDecisionMaking(lpEncoding);

		if (Statics.DEBUG_DECISION_MAKER_REASONER)
			reasonerHandler.printModels(models);

		if (models!=null && models.size()>0) {
			ArrayList<String> decisions = null;
			for (int i=0;i<decisionsAvailable.size();i++) {
				for (int j=0;j<models.size();j++) {
					model = (ArrayList<String>) models.get(j);
					/*if (Statics.DEBUG_DECISION_MAKER_REASONER)
						System.out.println("before getDecisionLiteral");*/
					decision = getDecisionLiteral(model,decisionsAvailable.get(i));
					if (Statics.DEBUG_DECISION_MAKER_REASONER)
						System.out.println("after getDecisionLiteral "+decision);

					if (decision!=null) {
						if (Statics.DEBUG_DECISION_MAKER_REASONER)
							System.out.println("Decision is "+decision);
						if (optimisticLabel==null) {
							if (Statics.DEBUG_DECISION_MAKER_REASONER)
								System.out.println("Opt label crated "+decision);
							decisions = new ArrayList<String>();
							optimisticLabel = new BestDecision();
							optimisticLabel.setUtility(utility);
							if (Statics.DEBUG_DECISION_MAKER_REASONER) {
								System.out.println("Utility for Opt label  "+utility);
								System.out.println("Adding "+decision);
							}
							decisions.add(decision);
						}
						else {
							if (Statics.DEBUG_DECISION_MAKER_REASONER)
								System.out.println("Adding "+decision);
							decisions.add(decision);
						}

					}
					if (optimisticLabel!=null && decisions!=null) {
						if (Statics.DEBUG_DECISION_MAKER_REASONER)
							System.out.println("setting "+decisions.size());
						optimisticLabel.setDecision(decisions);
					}
				}
			}
		}
		return optimisticLabel;
	}


}
