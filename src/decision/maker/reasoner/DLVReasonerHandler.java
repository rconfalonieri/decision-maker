package decision.maker.reasoner;

import global.Statics;
import it.unical.mat.wrapper.DLVError;
import it.unical.mat.wrapper.DLVInputProgram;
import it.unical.mat.wrapper.DLVInputProgramImpl;
import it.unical.mat.wrapper.DLVInvocation;
import it.unical.mat.wrapper.DLVInvocationException;
import it.unical.mat.wrapper.DLVWrapper;
import it.unical.mat.wrapper.Model;
import it.unical.mat.wrapper.ModelBufferedHandler;
import it.unical.mat.wrapper.Predicate;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import decision.maker.handler.WeightedRule;


public class DLVReasonerHandler implements IReasonerHandler {

	private String PATH_REASONER_RESOURCES;
	private String PATH_RESOURCES;
	private String PATH_REASONER;
	private URL URL_RESOURCES;
	private DLVInputProgram inputProgram;
	//private HashMap<String, String> replacements;

	public DLVReasonerHandler() {

		//super(knowledgeBase,preferenceBase,decisions,panel,"dlv");
		setReasonerPath();
		//replacements = new HashMap<String, String>();

	}


	public void setReasonerPath() {
		String os = System.getProperty("os.name").toLowerCase();
		String tmpPath = null;
		File absolutePath = null;

		//if windows
		if (os.indexOf("win") >= 0) {
			URL_RESOURCES = getClass().getResource("\\resources\\default.txt");

			if (URL_RESOURCES!=null) {
				if (Statics.DEBUG_DLV_REASONER)
					System.out.println(URL_RESOURCES.toString());
				String tmp = URL_RESOURCES.toString().replace("rsrc:","").replace("file:","");
				//absolutePath = new File(tmp);
				absolutePath = new File(tmp.substring(0, tmp.length()-13));
				//tmpPath = URL_RESOURCES.toString();	
				tmpPath = absolutePath.getAbsolutePath();
			}
		}
		//if mac
		if (os.indexOf("mac") >= 0) {

			URL_RESOURCES = getClass().getResource("/resources/default.txt");
			if (URL_RESOURCES!=null) {
				if (Statics.DEBUG_DLV_REASONER)
					System.out.println(URL_RESOURCES.toString());

				String tmp = URL_RESOURCES.toString().replace("rsrc:","").replace("file:","");
				//absolutePath = new File(tmp);
				absolutePath = new File(tmp.substring(0, tmp.length()-12));
				//tmpPath = URL_RESOURCES.toString();	
				tmpPath = absolutePath.getAbsolutePath();
			}
		}
		//if linux
		if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0) {
			URL_RESOURCES = getClass().getResource("/resources/default.txt");

			if (URL_RESOURCES!=null) {
				if (Statics.DEBUG_DLV_REASONER)
					System.out.println(URL_RESOURCES.toString());
				String tmp = URL_RESOURCES.toString().replace("rsrc:","").replace("file:","");
				//absolutePath = new File(tmp);
				absolutePath = new File(tmp.substring(0, tmp.length()-12));
				//tmpPath = URL_RESOURCES.toString();	
				tmpPath = absolutePath.getAbsolutePath();
			}		
		}
		if (tmpPath!=null) {
			PATH_RESOURCES = tmpPath.replace("file:","").replace("rsrc:","");
			//PATH_RESOURCES = tmpPath.replace("rsrc:","");

			//Statics.add("DLVHanlder()->PATH_RESOURCES "+PATH_RESOURCES);

			if (os.indexOf("win") >= 0) {
				PATH_REASONER = PATH_RESOURCES+"\\dlv\\windows\\dlv.exe";
				PATH_REASONER_RESOURCES = PATH_RESOURCES+"\\dlv"; 
			}
			if (os.indexOf("mac") >= 0) {
				PATH_REASONER = PATH_RESOURCES+"/dlv/mac/./dlv";
				PATH_REASONER_RESOURCES = PATH_RESOURCES+"/dlv";
			}
			if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0) {
				PATH_REASONER = PATH_RESOURCES+"/dlv/linux/./dlv";
				PATH_REASONER_RESOURCES = PATH_RESOURCES+"/dlv";
			}
			if (Statics.DEBUG_DLV_REASONER)
				System.out.println("DLVHanlder()->PATH_DLV "+PATH_REASONER);
		}
		else 
			if (Statics.DEBUG_DLV_REASONER)
				System.out.println("DLVHanlder()->PATH_DLV not found!");
	}

	public void printModels(Vector models) {

		ArrayList<String> solution;
		if (models!=null) {
			for (int i=0; i<models.size();i++) {
				System.out.println("Model "+(i+1)+":");
				solution = (ArrayList<String>) models.get(i);
				for (int j=0; j<solution.size();j++)
					System.out.println("Literal "+(j+1)+": "+solution.get(j).toString());
			}
		}

	}

	/**
	 * This method converts a decision making problem to a DLV encoding
	 * 
	 * @return
	 */

	public String pessimisticDecisionMakingToLP(ArrayList<WeightedRule> knowledgeBaseCut, ArrayList<WeightedRule> preferenceBaseCut, ArrayList<String> decisionsAvailable) {

		String dlvEnconding = "";

		//for each decision we add four rules "-ass(d) :- not ass(d)" "ass(d) :- not -ass(d)" "d :- ass(d)" and ":- d [1:]."
		for (int i=0; i< decisionsAvailable.size();i++) {
			//if (!Statics.DLV_DISJUNTIVE_IMPLEMENTATION) {

				dlvEnconding += "-ass("+decisionsAvailable.get(i).replace("-", "n")+") :- not ass("+decisionsAvailable.get(i).replace("-","n")+").\n";
				dlvEnconding += "ass("+decisionsAvailable.get(i).replace("-","n")+") :- not -ass("+decisionsAvailable.get(i).replace("-","n")+").\n";
				dlvEnconding += decisionsAvailable.get(i)+" :- ass("+decisionsAvailable.get(i).replace("-","n")+").\n";
				dlvEnconding += ":~ "+decisionsAvailable.get(i)+". [1:]\n";	
			/*}
			else {
				dlvEnconding += "-ass("+decisionsAvailable.get(i).replace("-", "n")+") v ass("+decisionsAvailable.get(i).replace("-","n")+").\n";
				dlvEnconding += decisionsAvailable.get(i)+" :- ass("+decisionsAvailable.get(i).replace("-","n")+").\n";
				
			}*/

		}
		//we add the knowledge like it is 
		for (int i=0; i< knowledgeBaseCut.size();i++) {
			dlvEnconding += knowledgeBaseCut.get(i).getRule()+"\n";
		}
		//for each preference in the prefereneBaseCut we add a constraint ":- not p."
		for (int i=0; i< preferenceBaseCut.size();i++) {
			dlvEnconding += ":- not "+preferenceBaseCut.get(i).getRule()+"\n";
		}
		if (Statics.DEBUG_DLV_REASONER) {
			System.out.println("pessimisticDecisionMakingToDLV() program is: "+dlvEnconding);
			Statics.add("Generated program is: "+dlvEnconding);
		}
		return dlvEnconding;
	}




	public String optimisticDecisionMakingToLP(ArrayList<WeightedRule> knowledgeBaseCut, ArrayList<WeightedRule> preferenceBaseCut, ArrayList<String> decisionsAvailable) {
		String dlvEnconding = "";
		String dlvrule = "";

		for (int i=0; i< decisionsAvailable.size();i++) {

			if (!Statics.DLV_DISJUNTIVE_IMPLEMENTATION) {

				dlvEnconding += "-ass("+decisionsAvailable.get(i).replace("-", "n")+") :- not ass("+decisionsAvailable.get(i).replace("-","n")+").\n";
				dlvEnconding += "ass("+decisionsAvailable.get(i).replace("-","n")+") :- not -ass("+decisionsAvailable.get(i).replace("-","n")+").\n";
				dlvEnconding += decisionsAvailable.get(i)+" :- ass("+decisionsAvailable.get(i).replace("-","n")+").\n";
				dlvEnconding += ":~ "+decisionsAvailable.get(i)+". [1:]\n";	
			}
			else {
				
				dlvEnconding += decisionsAvailable.get(i)+" :- ass("+decisionsAvailable.get(i).replace("-","n")+").\n";
				
				if (i!=decisionsAvailable.size()-1)
					dlvrule += "ass("+decisionsAvailable.get(i).replace("-", "n")+") v "; //ass("+decisionsAvailable.get(i).replace("-","n")+").\n";
				else 
					dlvEnconding += dlvrule +"ass("+decisionsAvailable.get(i).replace("-", "n")+").\n";
				
			}	

		}


		//we add the knowledge like it is taking care of handling the replacements of the form -d -> _d
		for (int i=0; i< knowledgeBaseCut.size();i++) {

			dlvEnconding += knowledgeBaseCut.get(i).getRule()+"\n";
		}

		//for each preference in the prefereneBaseCut we add a constraint ":- not p."
		for (int i=0; i< preferenceBaseCut.size();i++) {

			dlvEnconding += preferenceBaseCut.get(i).getRule()+"\n";
		}
		if (Statics.DEBUG_DLV_REASONER) {
			System.out.println("optimisticDecisionMakingToDLV() program is: "+dlvEnconding);
			Statics.add("Genereated program is: "+dlvEnconding);
		}
		return dlvEnconding;
	}



	private String replaceLiterals(String rule,
			ArrayList<String> decisionsAvailable) {
		String ruleWithReplacement = null;
		for (int i=0;i<decisionsAvailable.size();i++) {
			if (decisionsAvailable.get(i).contains("-") && rule.contains(decisionsAvailable.get(i))) {
				ruleWithReplacement = rule.replace(decisionsAvailable.get(i), decisionsAvailable.get(i).replace("-", "_"));
			}
		}
		return ruleWithReplacement;
	}

	public Vector computeDecisionMaking(String dlvEncoding) {

		//ArrayList<String> solutions = new ArrayList<String>();

		inputProgram=new DLVInputProgramImpl();
		DLVInvocation invocation=DLVWrapper.getInstance().createInvocation(PATH_REASONER); 

		if (invocation == null) {
			//Statics.add("invocation is null for path "+PATH_REASONER);
			System.out.println("invocation is null for path "+PATH_REASONER);
		}
		inputProgram.addText(dlvEncoding);
		try {
			invocation.setInputProgram(inputProgram);
		} catch (DLVInvocationException e1) {
			// TODO Auto-generated catch block
			System.out.println(getStackTrace(e1));
		}
		ModelBufferedHandler modelBufferedHandler=new ModelBufferedHandler(invocation);
		List<DLVError> dlvErrors = null;
		Vector extensions = null;

		try {
			invocation.run();
			Model model;
			extensions = new Vector();
			ArrayList<String> extension;
			// Scroll all models computed 
			while(modelBufferedHandler.hasMoreModels()){
				if (Statics.DEBUG_DLV_REASONER)
					System.out.println("modelBufferedHandler.hasMoreModels()");

				extension = new ArrayList<String>();
				model=modelBufferedHandler.nextModel();

				Enumeration<Predicate> predicates = model.getPredicates(); 
				if (predicates!=null) {
					String literal;
					while (predicates.hasMoreElements()) {
						literal = predicates.nextElement().toString();
						//the last two literals in the model are separated by a \n
						String[] literals = literal.split("\n");
						if (literals.length>1) {
							for (int i=0;i<literals.length;i++) {
								if (Statics.DEBUG_DLV_REASONER)
									System.out.println("computeDecisionMaking()->literals.length>1->Literal "+literals[i]);
								extension.add(literals[i].replace(".","").trim());
							}
						}
						else {
							if (Statics.DEBUG_DLV_REASONER)
								System.out.println("computeDecisionMaking()->Literal "+literal);
							extension.add(literal.replace(".","").trim());
						}
					}
				}
				extensions.add(extension);

			}
			dlvErrors =invocation.getErrors();
			if (dlvErrors!=null && dlvErrors.size()>0)
				Statics.add("Errors in DVL");
		}
		catch (DLVInvocationException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			Statics.add("DLVInvocationException2");
			Statics.add(getStackTrace(e));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			Statics.add("IOException");
			Statics.add(getStackTrace(e));
		}
		finally {
			return extensions;
		}


	}



	public void testSolver() {
		Statics.add("testDLVSpolver");
		Vector solutions = computeAcceptableArguments();
		ArrayList<String> solution;
		if (solutions!=null) {
			for (int i=0; i<solutions.size();i++) {
				Statics.add("Model "+(i+1)+":");
				solution = (ArrayList<String>) solutions.get(i);
				for (int j=0; j<solution.size();j++)
					Statics.add(solution.get(j).toString());
			}
		}
	}



	private Vector computeAcceptableArguments() {

		ArrayList<String> solutions = new ArrayList<String>();

		inputProgram=new DLVInputProgramImpl();
		DLVInvocation invocation=DLVWrapper.getInstance().createInvocation(PATH_REASONER); 

		if (invocation == null) {
			Statics.add("invocation is null for path "+PATH_REASONER);
		}
		// ./DLV input.dl stable.dl -filter=in
		//inputProgram.addText(argumentationInstance);
		//File stableSpecification = null;
		//File example = null;
		Statics.add("looking for example file");

		inputProgram.addFile(PATH_REASONER_RESOURCES+"/ex.dl");
		inputProgram.addFile(PATH_REASONER_RESOURCES+"/stable.dl");
		//we create a new instance of DLVInvocation using the DLVWrapper class specifying a path for DLV executable 

		try {
			invocation.setInputProgram(inputProgram);
		} catch (DLVInvocationException e1) {
			// TODO Auto-generated catch block
			Statics.add(getStackTrace(e1));
		}

		List <String> filters=new ArrayList<String>();

		filters.add("in");
		filters.add("input_error");

		try {
			invocation.setFilter(filters, true);
		} catch (DLVInvocationException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			Statics.add("DLVInvocationException1");
		}
		ModelBufferedHandler modelBufferedHandler=new ModelBufferedHandler(invocation);
		List<DLVError> dlvErrors = null;
		Vector extensions = new Vector();

		try {
			//Statics.add("before the run");
			invocation.run();
			//Statics.add("after the run");

			Model model;
			//Predicate predicate;
			//Literal literal;

			ArrayList<String> extension;
			// Scroll all models computed 
			while(modelBufferedHandler.hasMoreModels()){
				//Statics.add("hasMoreModels");
				extension = new ArrayList<String>();
				model=modelBufferedHandler.nextModel();

				Enumeration<Predicate> predicates = model.getPredicates(); 
				if (predicates!=null) {

					while (predicates.hasMoreElements()) {
						//Statics.add(predicates.nextElement().toString());
						extension.add(predicates.nextElement().toString());
					}
				}
				/*
				while(model.hasMorePredicates()){
					Statics.add("hasMorePredicates");
					predicate=model.nextPredicate();

					while(predicate.hasMoreLiterals()){
						Statics.add("hasMoreLiterals");
						literal=predicate.nextLiteral();
						extension.add(literal.toString());
					}
				}*/
				extensions.add(extension);
			}

			/*
			//getting the solutions which are in all extensions
			ArrayList<String> ext1,ext2;
			for (int i=0;i<extensions.size();i++) {
				ext1 = (ArrayList<String>) extensions.get(i);
				for (String argument1: ext1) {
					for (int j=0;j<extensions.size();j++) {
						if (i!=j) {
							ext2 = (ArrayList<String>) extensions.get(j);
							for (String argument2: ext2) {
								if (argument1.equals(argument2)) {
									if (!solutions.contains(argument1))
										solutions.add(argument1);
								}
							}

						}
					}
				}
			}*/

			dlvErrors =invocation.getErrors();
			if (dlvErrors!=null && dlvErrors.size()>0)
				Statics.add("Errors in DVL");
			//System.out.println("Errors in DVL");


		} catch (DLVInvocationException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			Statics.add("DLVInvocationException2");
			Statics.add(getStackTrace(e));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			Statics.add("IOException");
			Statics.add(getStackTrace(e));
		}
		finally {
			return extensions;
		}

	}

	

	

	private String getStackTrace(Throwable t) 
	{  
		java.io.StringWriter stringWritter = new java.io.StringWriter();  
		java.io.PrintWriter printWritter = new java.io.PrintWriter(stringWritter, true);  
		t.printStackTrace(printWritter);  
		printWritter.flush();  
		stringWritter.flush();   

		return stringWritter.toString();  
	}







}
