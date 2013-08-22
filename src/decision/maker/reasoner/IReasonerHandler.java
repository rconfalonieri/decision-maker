package decision.maker.reasoner;

import java.util.ArrayList;
import java.util.Vector;

import decision.maker.handler.WeightedRule;

public interface IReasonerHandler {
	
	//public void setReasonerPath();
	public String optimisticDecisionMakingToLP(ArrayList<WeightedRule> knowledgeBaseCut, ArrayList<WeightedRule> preferenceBaseCut, ArrayList<String> decisionsAvailable);
	public String pessimisticDecisionMakingToLP(ArrayList<WeightedRule> knowledgeBaseCut, ArrayList<WeightedRule> preferenceBaseCut, ArrayList<String> decisionsAvailable);
	public Vector computeDecisionMaking(String logicProgrammingEncoding);
	public void testSolver();
	public void printModels(Vector models);
	
	

}
