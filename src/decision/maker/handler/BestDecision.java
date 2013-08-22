package decision.maker.handler;

import java.util.ArrayList;

public class BestDecision {

	private ArrayList<String> decisions;
	private double utility;

	public BestDecision() {

	}

	public BestDecision(ArrayList<String> decisions, double utility) {

		this.decisions = decisions;
		this.utility = utility;
	}

	/**
	 * @return the decisions
	 */
	public ArrayList<String> getDecision() {
		return decisions;
	}

	/**
	 * @param decision the decision to set
	 */
	public void setDecision(ArrayList<String> decisions) {
		this.decisions = decisions;
	}

	/**
	 * @return the utility
	 */
	public double getUtility() {
		return utility;
	}

	/**
	 * @param utility the utility to set
	 */
	public void setUtility(double utility) {
		this.utility = utility;
	}

	public String toString() {
		String result = "";
		result += "Best decision is: <{";
		if (this.decisions!=null) {
			for (int i=0;i<this.decisions.size();i++) {
				if (i==this.decisions.size()-1)
					result+= this.decisions.get(i);
				else
					result += this.decisions.get(i)+",";
			}
		}
		result+= "},";
		result += ((double)Math.round(this.utility * 10) / 10)+">";
		return result;

	}


}
