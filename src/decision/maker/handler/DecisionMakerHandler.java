package decision.maker.handler;

import global.Statics;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import decision.maker.reasoner.DLVReasonerHandler;
import decision.maker.reasoner.IReasonerHandler;
import decision.maker.reasoner.PosPsmodelsReasonerHandler;
import decision.maker.reasoner.PsmodelsReasonerHandler;

public abstract class DecisionMakerHandler {

	protected ArrayList<Knowledge> knowledgeBase;
	protected ArrayList<Knowledge> preferenceBase;
	protected ArrayList<Knowledge> decisions;
	protected ArrayList<Double> strata;

	protected IReasonerHandler reasonerHandler;
	protected ArrayList<WeightedRule> stratifiedKnowledgeBase;
	protected ArrayList<WeightedRule> proritizedPreferences;
	protected ArrayList<WeightedRule> knowledgeBaseCut;
	protected ArrayList<WeightedRule> preferenceBaseCut;
	protected ArrayList<String> decisionsAvailable;
	protected JPanel panel;

	private boolean guiCheckOk;

	//public abstract void testSolver();
	//public abstract void setReasonerPath();
	public abstract BestDecision computePessimisticDecisionUnderUncertainty();
	public abstract BestDecision computeOptimisticDecisionUnderUncertainty();
	public abstract BestDecision computePessimisticDecision(double alpha);
	public abstract BestDecision computeOptimisticDecision(double alpha);
	public abstract String getDecisionLiteral(ArrayList<String> models,String decisionLiteral);


	public DecisionMakerHandler(ArrayList<Knowledge> knowledgeBase, ArrayList<Knowledge> preferenceBase, ArrayList<Knowledge> decisions, JPanel panel, String reasoner)
	{
		stratifiedKnowledgeBase = new ArrayList<WeightedRule>();
		proritizedPreferences = new ArrayList<WeightedRule>();
		decisionsAvailable = new ArrayList<String>();

		//setReasonerPath(reasoner);
		this.knowledgeBase = knowledgeBase;
		this.preferenceBase = preferenceBase;
		this.decisions = decisions;
		this.panel = panel;



		setStratifiedKnowledgeBase();
		if (Statics.DEBUG_DECISION_MAKER_REASONER) {
			System.out.println("stratifiedKnowledgeBase");
			for (int i=0;i<stratifiedKnowledgeBase.size();i++)
				System.out.println(stratifiedKnowledgeBase.get(i).getWeight()+": "+ stratifiedKnowledgeBase.get(i).getRule());
		}
		setPrioritizedPreferences();
		if (Statics.DEBUG_DECISION_MAKER_REASONER) {
			System.out.println("proritizedPreferences");
			for (int i=0;i<proritizedPreferences.size();i++)
				System.out.println(proritizedPreferences.get(i).getWeight()+": "+ proritizedPreferences.get(i).getRule());
		}
		setDecisions();
		if (Statics.DEBUG_DECISION_MAKER_REASONER) {
			System.out.println("decisions");
			for (int i=0;i<decisionsAvailable.size();i++)
				System.out.println(decisionsAvailable.get(i));
		}
		setStrata();
		/*if (Statics.DEBUG_DECISION_MAKER_REASONER)
			System.out.println("LoginProgrammingReasonerHandler()");*/

		if (stratifiedKnowledgeBase.size()==0 || proritizedPreferences.size()==0 || decisionsAvailable.size()==0) {

			if (stratifiedKnowledgeBase.size()==0 )
				//if (Statics.DEBUG_DECISION_MAKER_REASONER) {
				System.out.println("Knowledge missing!");
			//Statics.add("Knowledge missing!");
			guiCheckOk = false;
			JOptionPane.showMessageDialog(panel, "Knowledge missing!");
			//System.exit(-1);
			//}
			if (proritizedPreferences.size()==0)
				//if (Statics.DEBUG_DECISION_MAKER_REASONER) {
				System.out.println("Preferences missing!");
			//Statics.add("Preferences missing!");
			guiCheckOk = false;
			JOptionPane.showMessageDialog(panel, "Preferences missing!");
			//System.exit(-1);
			//}
			if (decisionsAvailable.size()==0)
				//if (Statics.DEBUG_DECISION_MAKER_REASONER) {
				System.out.println("Decisions missing!");
			//Statics.add("Decisions missing!");
			guiCheckOk = false;
			JOptionPane.showMessageDialog(panel, "Decisions missing!");
			//System.exit(-1);
			//}
		}
		else 
			guiCheckOk = true;

	}

	public void setDecisions() {

		Knowledge formula;
		String decision;

		for (int i=0;i<decisions.size();i++) {
			formula = decisions.get(i);
			decision = formula.toASPFormat().trim();
			decision = decision.replace(".","");
			decisionsAvailable.add(decision);

		}
	} 

	public void setStrata() {

		if (Statics.DEBUG_DECISION_MAKER_REASONER) 
			System.out.println("setStrata()");
		strata = new ArrayList<Double>();

		for (int i=0;i<stratifiedKnowledgeBase.size();i++) {

			if (!strata.contains(stratifiedKnowledgeBase.get(i).getWeight())) {
				strata.add(stratifiedKnowledgeBase.get(i).getWeight());
			}
		}
		for (int i=0;i<proritizedPreferences.size();i++) {

			if (!strata.contains(proritizedPreferences.get(i).getWeight())) {
				strata.add(proritizedPreferences.get(i).getWeight());
			}
		}
		if (!strata.contains(0.0)) {
			strata.add((double) 0.0);
		}
		if (!strata.contains(1.0)) {
			strata.add((double) 1.0);
		}
		if (Statics.DEBUG_DECISION_MAKER_REASONER) {
			System.out.println("not ordered");
			for (int i=0;i<strata.size();i++)
				System.out.println(strata.get(i));
		}
		Collections.sort(strata);

		if (Statics.DEBUG_DECISION_MAKER_REASONER) {
			System.out.println("ordered");
			for (int i=0;i<strata.size();i++)
				System.out.println(strata.get(i));
		}
	}

	public void setStratifiedKnowledgeBase() {
		Knowledge formula;
		String rule;
		double weight;

		for (int i=0;i<knowledgeBase.size();i++) {
			formula = knowledgeBase.get(i);
			rule = formula.toASPFormat();
			weight = formula.getWeight();
			stratifiedKnowledgeBase.add(new WeightedRule(weight, rule));

		}
		//return stratifiedKB;
	}


	public void setPrioritizedPreferences() {

		Knowledge formula;
		String rule;
		double weight;

		for (int i=0;i<preferenceBase.size();i++) {
			formula = preferenceBase.get(i);
			rule = formula.toASPFormat();
			weight = formula.getWeight();
			proritizedPreferences.add(new WeightedRule(weight, rule));

		}

	}

	public ArrayList<WeightedRule> getStratifiedKnowledgeCut(double weight,boolean strictCut) {

		knowledgeBaseCut = new ArrayList<WeightedRule>();
		WeightedRule weightedRule;

		for (int i=0;i<stratifiedKnowledgeBase.size();i++) {
			weightedRule = stratifiedKnowledgeBase.get(i);
			if (!strictCut) {
				if (weightedRule.getWeight()>=weight)
					knowledgeBaseCut.add(stratifiedKnowledgeBase.get(i));
			}
			else {
				if (weightedRule.getWeight()>weight)
					knowledgeBaseCut.add(stratifiedKnowledgeBase.get(i));
			}
		}
		if (Statics.DEBUG_DECISION_MAKER_REASONER) {
			System.out.println("knowledgeBaseCut "+weight);
			for (int i=0;i<knowledgeBaseCut.size();i++)
				System.out.println(knowledgeBaseCut.get(i).getWeight()+":"+ knowledgeBaseCut.get(i).getRule());
		}

		return knowledgeBaseCut;
	}

	public ArrayList<WeightedRule> getProritizedPreferencesCut(double priority,boolean strictCut) {

		preferenceBaseCut = new ArrayList<WeightedRule>();
		WeightedRule weightedRule;

		for (int i=0;i<proritizedPreferences.size();i++) {
			weightedRule = proritizedPreferences.get(i);
			if (!strictCut) {
				if (weightedRule.getWeight()>=priority)
					preferenceBaseCut.add(proritizedPreferences.get(i));
			}
			else {
				if (weightedRule.getWeight()>priority)
					preferenceBaseCut.add(proritizedPreferences.get(i));
			}
		}
		if (Statics.DEBUG_DECISION_MAKER_REASONER) {
			System.out.println("preferenceBaseCut "+priority);
			for (int i=0;i<preferenceBaseCut.size();i++)
				System.out.println(preferenceBaseCut.get(i).getWeight()+":"+ preferenceBaseCut.get(i).getRule());
		}

		return preferenceBaseCut;
	}

	public double increaseAlpha(double alpha) {


		if (alpha==1) {
			System.out.println("nextStratum-> Fatal error");
			System.exit(-1);
		}
		else {
			for (int i=0; i<strata.size();i++) {
				if (strata.get(i).equals(alpha)) {
					return strata.get(i+1);
				}
			}
		}
		return -1;

	}

	public double decreaseBeta(double beta) {


		if (beta==0) {
			System.out.println("nextStratum-> Fatal error");
			System.exit(-1);
		}
		else {
			//i==0 should never happen
			for (int i=strata.size()-1; i>=0;i--) {
				if (strata.get(i).equals(beta)) {
					if (i==0) {
						System.out.println("nextStratum-> Fatal error");
						System.exit(-1);
					}
					return strata.get(i-1);
				}
			}
		}
		return -1;

	}


	public void convertInputKnowledge(ArrayList<Knowledge> knowledgeBase,ArrayList<Knowledge> preferenceBase,ArrayList<Knowledge> decisions) {
		Knowledge formula;
		String f;
		String asp;

		for (int i=0;i<knowledgeBase.size();i++) {
			formula = knowledgeBase.get(i);
			f = formula.toString();
			Statics.add(f);
		}

		Statics.add("**************");
		for (int i=0;i<knowledgeBase.size();i++) {
			formula = knowledgeBase.get(i);
			asp = formula.toASPFormat();
			Statics.add(formula.getWeight()+": "+asp);
		}

		for (int i=0;i<preferenceBase.size();i++) {
			formula = preferenceBase.get(i);
			asp = formula.toASPFormat();
			Statics.add(formula.getWeight()+": "+asp);
		}

		for (int i=0;i<decisions.size();i++) {
			formula = decisions.get(i);
			asp = formula.toASPFormat();
			Statics.add(formula.getWeight()+": "+asp);

		}

	}
	/**
	 * @return the guiCheckOk
	 */
	public boolean isGuiCheckOk() {
		return guiCheckOk;
	}
	/**
	 * @param guiCheckOk the guiCheckOk to set
	 */
	public void setGuiCheckOk(boolean guiCheckOk) {
		this.guiCheckOk = guiCheckOk;
	}


}
