package decision.maker.handler;

public class WeightedRule {

	private double weight;
	private String rule;
	
	public WeightedRule() {
		
	}
	
	public WeightedRule(double weight, String rule) {
		
		this.weight = weight;
		this.rule = rule;
	}
	
	/**
	 * @return the weight
	 */
	public double getWeight() {
		return weight;
	}
	/**
	 * @param weight the weight to set
	 */
	public void setWeight(double weight) {
		this.weight = weight;
	}
	/**
	 * @return the rule
	 */
	public String getRule() {
		return rule;
	}
	/**
	 * @param rule the rule to set
	 */
	public void setRule(String rule) {
		this.rule = rule;
	}
	
	
}
