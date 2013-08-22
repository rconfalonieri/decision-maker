package decision.maker;

import java.util.ArrayList;
import java.io.*;


import decision.maker.handler.DecisionMakerHandler;
import decision.maker.handler.Knowledge;








/**
 * The <code>Agent</code> class is the basic class for all the agents.
 * <ul>
 * <li> <code>DeliberatingAgent</code>
 * </ul>
 */
public class Agent {

	/**
	 * the <code>name</code> of the <code>Agent</code>
	 */
	protected String name;
	/**
	 * the (stratified) <code>knowledge base</code> of the <code>Agent</code>
	 */
	private ArrayList<Knowledge> knowledgeBase;
	/**
	 * the (stratified) <code>preference base</code> of the <code>Agent</code>
	 */
	private ArrayList<Knowledge> preferenceBase;
	/**
	 * the set of decisions
	 */
	private ArrayList<Knowledge> decisions;
	
	
	
	
	protected DecisionMakerHandler decisionMakerHandler;
	
	/**
	 * List of the different Decision criteria: pessimistic and optimistic
	 */
	protected String[] decisionTypeList = {"Pessimistic","Optimistic"};
	/**
	 * The field <code>decisionType</code> is used to
	 * know which kind of decision criterion the <code>findBestOffer</code>
	 * method has to use (0 is pessimistic, while 1 is optimistic)
	 */
	protected int decisionType; 
	
	/**
	 * List of the different reasoner available
	 */
	protected String[] reasonerTypeList = {"DLV","psmodels","posPsmodels"};
	protected int reasonerType; 
	
	
	/**
	 * The vocabulary is the list of the literals used by
	 * the Agent
	 */
	//private ArrayList<String> vocabulary;


	/**
	 * generic constructor
	 */
	public Agent() {
		name = "";
		setKnowledgeBase(new ArrayList<Knowledge>());
		setPreferenceBase(new ArrayList<Knowledge>());
		setDecisions(new ArrayList<Knowledge>());
		
	}

	/**
	 * Overloaded builder for the standalone Decision module.
	 * Builds a new Agent :
	 * @param name_
	 * @param knowledge
	 * @param goals
	 */
	/*
	public Agent(String name_, ArrayList<Knowledge> knowledge, ArrayList<Knowledge> goals) {

		name = name_;
		setKnowledgeBase(knowledge);
		setPreferenceBase(goals);
		vocabulary = new ArrayList<String>();
		setDecisions(new ArrayList<Knowledge>());
		
	}*/

	/*
	public Agent(String name_) {

		name = name_;
		setKnowledgeBase(new ArrayList<Knowledge>());
		setPreferenceBase(new ArrayList<Knowledge>());
		vocabulary = new ArrayList<String>();
		setDecisions(new ArrayList<Knowledge>());
		
	}*/


	

	public void printFormulaBase() {

		Knowledge formula;


		System.out.println("knowledgeBase");
		for (int i=0;i<getKnowledgeBase().size();i++) {
			formula = getKnowledgeBase().get(i);

			System.out.println(formula.toString());

		}


		System.out.println("preferenceBase");
		for (int i=0;i<getPreferenceBase().size();i++) {
			formula = getPreferenceBase().get(i);
			//formula = knowledgeBase.get(i);

			System.out.println(formula.toString());
		}

	}

	public void printDecisions() {

		System.out.println("Decisions");
		for (int i=0;i<getDecisions().size();i++)
			System.out.println(getDecisions().get(i).toString());
	}

	

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	
	// For the Round Ends :
	public void clearAgent() {

	}

	// For the Dialog Ends :
	public void resetAgent() {
		//name = "";
		getKnowledgeBase().clear();
		getPreferenceBase().clear();
		getDecisions().clear();
		//KO.clear();
		//GO.clear();
	}

	/**
	 * @return the reasonerType
	 */
	public int getReasonerType() {
		return reasonerType;
	}

	/**
	 * @param reasonerType the reasonerType to set
	 */
	public void setReasonerType(int reasonerType) {
		this.reasonerType = reasonerType;
	}

	public ArrayList<Knowledge> getKnowledgeBase() {
		return knowledgeBase;
	}

	public void setKnowledgeBase(ArrayList<Knowledge> knowledgeBase) {
		this.knowledgeBase = knowledgeBase;
	}

	public ArrayList<Knowledge> getPreferenceBase() {
		return preferenceBase;
	}

	public void setPreferenceBase(ArrayList<Knowledge> preferenceBase) {
		this.preferenceBase = preferenceBase;
	}

	public ArrayList<Knowledge> getDecisions() {
		return decisions;
	}

	public void setDecisions(ArrayList<Knowledge> decisions) {
		this.decisions = decisions;
	}
	
	
	

}
