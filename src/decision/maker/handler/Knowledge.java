package decision.maker.handler;


import java.util.*;
import java.io.*;


public class Knowledge {

	// Fields
	//private ArrayList<String> vocabulary = new ArrayList<String>(); // Vector containing only Strings

	/**
	 * Weight of a Knowledge. Should be between 0 and 1, 0 is min, 1 is max.
	 */
	private String head;
	private ArrayList<String> body;
	private double weight;
	private String rule;
	private String original;



	public Knowledge() {

	}


	public Knowledge(String line, boolean parsing) {

		if (parsing) {
			original = line;
			String[] tab = line.split(" ; ");

			//System.out.println("Knowledge "+original);
			if (tab.length == 2) {
				//System.out.println("weighted ");
				this.weight = Double.parseDouble(tab[1]);
				this.rule = tab[0].substring(0,tab[0].length()-1);
			}
			// it is a decision
			else {
				//System.out.println("decision "+original);
				this.rule = tab[0].trim();
				this.weight = -1;
			}
		}
		else {
			//System.out.println("no parsing "+line);
			this.rule = line;
		}



	}


	public Knowledge(String rule, double weight) {

		this.rule = rule;
		this.weight = weight;

	}


	/** Returns the Weigth of the Knowledge
	 * @return the weight
	 */
	public double getWeight() {
		return weight;
	}
	/** Allows User to define the weight of the Knowledge
	 * @param weight the weight to set.
	 */
	public void setWeight(int weight) {
		this.weight = weight;
	}




	/** return the Knowledge as String readable by ASP
	 * 
	 */
	public String toASPFormat()
	{
		String result = "";
		result +=rule+". ";

		return result;

	}
	
	
	public String toString()
	{
		return original;

	}


	public String toFileFormat() {
		
		String result = "";
		result += rule.replace(".", "");
		if (weight!=-1) {
			result += " ; ";
			result += weight;
		}
		//do nothing is a decision
		else
		{
			
		}
		
		return result;
		
	}



}
