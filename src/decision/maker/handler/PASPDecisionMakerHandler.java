package decision.maker.handler;

import global.Statics;

import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JPanel;

import decision.maker.reasoner.DLVReasonerHandler;
import decision.maker.reasoner.PosPsmodelsReasonerHandler;

public class PASPDecisionMakerHandler extends DecisionMakerHandler {

	public PASPDecisionMakerHandler(ArrayList<Knowledge> knowledgeBase,
			ArrayList<Knowledge> preferenceBase, ArrayList<Knowledge> decisions,
			JPanel panel, String reasoner) {
		super(knowledgeBase, preferenceBase, decisions, panel, reasoner);

		if (Statics.DEBUG_POSPSMODELS_DECISION_MAKER)
			System.out.println("PASPDecisionMakerHandler");

		reasonerHandler = new PosPsmodelsReasonerHandler();
	}

	@Override
	public BestDecision computePessimisticDecisionUnderUncertainty() {

		BestDecision bestDecision = null;
		boolean finished = false;
		double alpha = 0;
		double beta = 1;

		if (Statics.DEBUG_POSPSMODELS_DECISION_MAKER) 
			System.out.println("computePessimisticDecisionUnderUncertainty()->trying with beta="+beta);

		Statics.add("computePessimisticDecisionUnderUncertainty()->trying with beta="+beta);

		knowledgeBaseCut = getStratifiedKnowledgeCut(alpha, false);

		if (knowledgeBaseCut==null)
			System.out.println("computePessimisticDecisionUnderUncertainty->knowledgeBaseCut is null...");


		preferenceBaseCut = getProritizedPreferencesCut(beta, false);
		if (preferenceBaseCut==null)
			System.out.println("computePessimisticDecisionUnderUncertainty->preferenceBaseCut is null...");


		/*if (Statics.DEBUG_POSPSMODELS_DECISION_MAKER)
			System.out.println("computePessimisticDecisionUnderUncertainty()->trying with alpha="+alpha);*/
		BestDecision pessimisticLabel = computePessimisticDecision(beta);
		if (pessimisticLabel!=null) {
			bestDecision = pessimisticLabel;

			Statics.add(bestDecision.toString());

			while (!finished && beta > 0) {
				beta = decreaseBeta(beta);
				if (Statics.DEBUG_POSPSMODELS_DECISION_MAKER) 
					System.out.println("computePessimisticDecisionUnderUncertainty()->trying with beta="+alpha);

				Statics.add("computePessimisticDecisionUnderUncertainty()->trying with alpha="+alpha);

				//not needed
				//knowledgeBaseCut = getStratifiedKnowledgeCut(alpha, false);
				preferenceBaseCut = getProritizedPreferencesCut(beta, true);
				pessimisticLabel = computePessimisticDecision(beta);
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
				if (Statics.DEBUG_POSPSMODELS_DECISION_MAKER) {
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

		if (Statics.DEBUG_POSPSMODELS_DECISION_MAKER) 
			System.out.println("computeOptimisticDecisionUnderUncertainty()->trying with alpha="+alpha);

		Statics.add("computeOptimisticDecisionUnderUncertainty()->trying with alpha="+alpha);

		knowledgeBaseCut = getStratifiedKnowledgeCut(alpha, true);

		if (knowledgeBaseCut==null)
			System.out.println("computeOptimisticDecisionUnderUncertainty->knowledgeBaseCut is null...");


		preferenceBaseCut = getProritizedPreferencesCut(alpha, true);
		if (preferenceBaseCut==null)
			System.out.println("computeOptimisticDecisionUnderUncertainty->preferenceBaseCut is null...");


		/*if (Statics.DEBUG_POSPSMODELS_DECISION_MAKER)
			System.out.println("computePessimisticDecisionUnderUncertainty()->trying with alpha="+alpha);*/
		optimisticLabel = computeOptimisticDecision(alpha);

		while (optimisticLabel==null && alpha < 1) {
			alpha = increaseAlpha(alpha);
			if (Statics.DEBUG_POSPSMODELS_DECISION_MAKER) 
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
				if (Statics.DEBUG_POSPSMODELS_DECISION_MAKER) {
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
	public BestDecision computePessimisticDecision(double beta) {

		BestDecision pessimisticLabel = null;
		ArrayList<String> possibilisticModel;
		double utility = beta;
		String decision;

		if (Statics.DEBUG_POSPSMODELS_DECISION_MAKER)
			System.out.println("computePessimisticDecision with beta: "+beta);

		String possibilisticASPEncoding = reasonerHandler.pessimisticDecisionMakingToLP(knowledgeBaseCut, preferenceBaseCut, decisionsAvailable); 
		Vector possibilisticModels = reasonerHandler.computeDecisionMaking(possibilisticASPEncoding);

		if (Statics.DEBUG_POSPSMODELS_DECISION_MAKER)
			reasonerHandler.printModels(possibilisticModels);

		if (possibilisticModels!=null && possibilisticModels.size()>0) {

			for (int i=0;i<possibilisticModels.size();i++) {

				if (checkPossibilisticSatisfability((ArrayList<String>) possibilisticModels.get(i),preferenceBaseCut)) {
					
					if (Statics.DEBUG_POSPSMODELS_DECISION_MAKER)
						System.out.println("Possibilistic Model "+(i+1)+" Preferences satisfied..");
					
					ArrayList<String> decisions = null;
					for (int j=0;j<decisionsAvailable.size();j++) {
						//for (int j=0;j<models.size();j++) {
						possibilisticModel = (ArrayList<String>) possibilisticModels.get(i);
						/*if (Statics.DEBUG_POSPSMODELS_DECISION_MAKER)
								System.out.println("before getDecisionLiteral");*/
						decision = getDecisionLiteral(possibilisticModel,decisionsAvailable.get(j));
						if (Statics.DEBUG_POSPSMODELS_DECISION_MAKER)
							System.out.println("after getDecisionLiteral "+decision);

						if (decision!=null) {
							if (Statics.DEBUG_POSPSMODELS_DECISION_MAKER)
								System.out.println("Decision is "+decision);
							if (pessimisticLabel==null) {
								if (Statics.DEBUG_POSPSMODELS_DECISION_MAKER)
									System.out.println("Pess label crated "+decision);
								decisions = new ArrayList<String>();
								pessimisticLabel = new BestDecision();
								pessimisticLabel.setUtility(1-utility);
								//System.out.println("Adding "+decision);
								decisions.add(decision);
							}
							else {
								//System.out.println("Adding "+decision);
								decisions.add(decision);
							}

						}
						if (pessimisticLabel!=null && decisions!=null) {
							if (Statics.DEBUG_POSPSMODELS_DECISION_MAKER)
								System.out.println("setting "+decisions.size());
							pessimisticLabel.setDecision(decisions);
						}
						//}
					}

				}
				else {
					if (Statics.DEBUG_POSPSMODELS_DECISION_MAKER)
						System.out.println("Possibilistic Model "+(i+1)+ " Preferences not satisfied..");
				}	
			}



		}
		return pessimisticLabel;
	}



	@Override
	public BestDecision computeOptimisticDecision(double alpha) {
		
		BestDecision optimisticLabel = null;
		ArrayList<String> model;
		double utility = (1-alpha);
		String decision;

		if (Statics.DEBUG_POSPSMODELS_DECISION_MAKER)
			System.out.println("computeOptimisticDecision with alpha: "+alpha);

		String lpEncoding = reasonerHandler.optimisticDecisionMakingToLP(knowledgeBaseCut, preferenceBaseCut, decisionsAvailable); 
		Vector possibilisticModels = reasonerHandler.computeDecisionMaking(lpEncoding);

		if (Statics.DEBUG_POSPSMODELS_DECISION_MAKER)
			reasonerHandler.printModels(possibilisticModels);

		if (possibilisticModels!=null && possibilisticModels.size()>0) {
			ArrayList<String> decisions = null;
			for (int i=0;i<decisionsAvailable.size();i++) {
				for (int j=0;j<possibilisticModels.size();j++) {
					model = (ArrayList<String>) possibilisticModels.get(j);
					/*if (Statics.DEBUG_POSPSMODELS_DECISION_MAKER)
						System.out.println("before getDecisionLiteral");*/
					decision = getDecisionLiteral(model,decisionsAvailable.get(i));
					if (Statics.DEBUG_POSPSMODELS_DECISION_MAKER)
						System.out.println("after getDecisionLiteral "+decision);
					
					if (decision!=null) {
						if (Statics.DEBUG_POSPSMODELS_DECISION_MAKER)
							System.out.println("Decision is "+decision);
						if (optimisticLabel==null) {
							if (Statics.DEBUG_POSPSMODELS_DECISION_MAKER) {
								System.out.println("Opt label crated "+decision);
								System.out.println("Utility for opt label "+utility);
							}
							decisions = new ArrayList<String>();
							optimisticLabel = new BestDecision();
							optimisticLabel.setUtility(utility);
							if (Statics.DEBUG_POSPSMODELS_DECISION_MAKER)
								System.out.println("Adding "+decision);
							decisions.add(decision);
						}
						else {
							if (Statics.DEBUG_POSPSMODELS_DECISION_MAKER)
								System.out.println("Adding "+decision);
							decisions.add(decision);
						}
						
					}
					if (optimisticLabel!=null && decisions!=null) {
						if (Statics.DEBUG_POSPSMODELS_DECISION_MAKER)
							System.out.println("setting "+decisions.size());
						optimisticLabel.setDecision(decisions);
					}
				}
			}
		}
		return optimisticLabel;
	}

	@Override
	public String getDecisionLiteral(ArrayList<String> possibilisticModel,
			String decisionLiteral) {

		String[] possibilisticLiteral;
		String literal;
		
		if (Statics.DEBUG_POSPSMODELS_DECISION_MAKER)
			System.out.println("getDecisionLiteral()");
		for (int i=0;i<possibilisticModel.size();i++) {

			possibilisticLiteral = possibilisticModel.get(i).split(",");
			literal = possibilisticLiteral[0].replaceFirst("\\(", "");

			if (Statics.DEBUG_POSPSMODELS_DECISION_MAKER)
				System.out.println("decisionLiteral "+"ass("+decisionLiteral+")"+ " literal "+literal);
			if (literal.equals("ass("+decisionLiteral+")"))
				//return model.get(i);
				return decisionLiteral;

		}
		//System.out.println("Fatal error");
		return null;

	}

	private boolean checkPossibilisticSatisfability(ArrayList<String> possibilisticModel,
			ArrayList<WeightedRule> preferenceBaseCut) {

		WeightedRule prioritisedPreference;
		int nrPrioritisedPreferences = 0;
		String[] possibilisticLiteral;
		String preferenceToSatisfy;
		int priorityToSatisfy;
		String literal;
		int priority;
		if (Statics.DEBUG_POSPSMODELS_DECISION_MAKER)
			System.out.println("checkPossibilisticSatisfability()");
		if (Statics.DEBUG_POSPSMODELS_DECISION_MAKER)
			System.out.println("preferenceBaseCut size->"+preferenceBaseCut.size());
		for (int i=0; i<preferenceBaseCut.size();i++) {

			prioritisedPreference = preferenceBaseCut.get(i);
			
			 
			preferenceToSatisfy = prioritisedPreference.getRule().replace(".", "").trim();
			priorityToSatisfy = (int) (prioritisedPreference.getWeight()*100);

			if (Statics.DEBUG_POSPSMODELS_DECISION_MAKER) {
				System.out.println("preferenceToSatisfy->"+preferenceToSatisfy);
				System.out.println("priorityToSatisfy->"+priorityToSatisfy);
			}
			for (int j=0;j<possibilisticModel.size();j++) {
				if (Statics.DEBUG_POSPSMODELS_DECISION_MAKER)
					System.out.println("possibilisticLiteral->"+possibilisticModel.get(j));
				possibilisticLiteral = possibilisticModel.get(j).split(",");
				literal = possibilisticLiteral[0].replaceFirst("\\(", "");
				priority = Integer.valueOf(possibilisticLiteral[1].replace(")", ""));
				if (Statics.DEBUG_POSPSMODELS_DECISION_MAKER) {
					System.out.println("literal->"+literal);
					System.out.println("priority->"+priority);
				}
				if (literal.equals(preferenceToSatisfy) && priority >= priorityToSatisfy) {
					if (Statics.DEBUG_POSPSMODELS_DECISION_MAKER)
						System.out.println("preference satisfied!");
					nrPrioritisedPreferences++;
				}
			}

		}
		if (Statics.DEBUG_POSPSMODELS_DECISION_MAKER)
			System.out.println("checkPossibilisticSatisfability()->"+(nrPrioritisedPreferences==preferenceBaseCut.size()));
		return (nrPrioritisedPreferences==preferenceBaseCut.size());
	}


	

}
