package decision.maker.reasoner;

import global.Statics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JPanel;


import decision.maker.handler.BestDecision;
import decision.maker.handler.Knowledge;
import decision.maker.handler.ASPDecisionMakerHandler;
import decision.maker.handler.WeightedRule;
import decision.maker.reasoner.filemanagement.ManageASPInputFile;

public class PsmodelsReasonerHandler implements IReasonerHandler {

	private String PATH_REASONER_RESOURCES;
	private String PATH_RESOURCES;
	private String PATH_REASONER;
	private String PATH_LPARSE;
	private URL URL_RESOURCES;

	public PsmodelsReasonerHandler() {

		setReasonerPath();
	}


	public void setReasonerPath() {
		String os = System.getProperty("os.name").toLowerCase();
		String tmpPath = null;
		File absolutePath = null;

		//if windows
		//not supported

		//if mac
		if (os.indexOf("mac") >= 0) {

			URL_RESOURCES = getClass().getResource("/resources/default.txt");
			if (URL_RESOURCES!=null) {
				if (Statics.DEBUG_PSMODELS_REASONER)
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
				if (Statics.DEBUG_PSMODELS_REASONER)
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
			if (os.indexOf("mac") >= 0) {
				PATH_REASONER_RESOURCES = PATH_RESOURCES+"/psmodels/mac";
				PATH_REASONER = PATH_RESOURCES+"/psmodels/mac/./psmodels";
				PATH_LPARSE = PATH_RESOURCES+"/psmodels/mac/./lparse";	
			}
			if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0) {
				PATH_REASONER_RESOURCES = PATH_RESOURCES+"/psmodels/linux";
				PATH_REASONER = PATH_RESOURCES+"/psmodels/linux/./psmodels";
				PATH_LPARSE = PATH_RESOURCES+"/psmodels/linux/./lparse";
			}
			if (Statics.DEBUG_PSMODELS_REASONER) {
				System.out.println("PsmodelsHandler()->PATH_REASONER_RESOURCES "+PATH_REASONER_RESOURCES);
				System.out.println("PsmodelsHandler()->PATH_PSMODELS "+PATH_REASONER);
				System.out.println("PsmodelsHandler()->PATH_LPARSE "+PATH_LPARSE);
			}
		}
		else 
			if (Statics.DEBUG_PSMODELS_REASONER) {
				System.out.println("PsmodelsHandler()->PATH_REASONER_RESOURCES not found!");
				System.out.println("PsmodelsHandler()->PATH_PSMODELS not found!");
				System.out.println("PsmodelsHandler()->PATH_LPARSE not found!");
			}

	}

	@Override
	public String optimisticDecisionMakingToLP(
			ArrayList<WeightedRule> knowledgeBaseCut,
			ArrayList<WeightedRule> preferenceBaseCut,
			ArrayList<String> decisionsAvailable) {

		String psmodelsEnconding = "";
		//we add the knowledge like it is
		for (int i=0; i< knowledgeBaseCut.size();i++) {
			psmodelsEnconding += knowledgeBaseCut.get(i).getRule()+"\n";
		}
		//for each preference in the prefereneBaseCut we add a fact p."
		for (int i=0; i< preferenceBaseCut.size();i++) {
			psmodelsEnconding += preferenceBaseCut.get(i).getRule()+"\n";
		}
		//for each decision we add four rules "-ass(d) x ass(d)"  "d :- ass(d)" "
		for (int i=0; i< decisionsAvailable.size();i++) {
			psmodelsEnconding += "-ass("+decisionsAvailable.get(i)+") x ass("+decisionsAvailable.get(i)+").\n";
			psmodelsEnconding += decisionsAvailable.get(i)+" :- ass("+decisionsAvailable.get(i)+").\n";
		}
		if (Statics.DEBUG_PSMODELS_REASONER) {
			System.out.println("optimisticDecisionMakingToLP() program is: "+psmodelsEnconding);
			
		}
		if (Statics.GUI_DETAILS)
			Statics.add("Generated program is: "+psmodelsEnconding);
		return psmodelsEnconding;
	}

	@Override
	public String pessimisticDecisionMakingToLP(
			ArrayList<WeightedRule> knowledgeBaseCut,
			ArrayList<WeightedRule> preferenceBaseCut,
			ArrayList<String> decisionsAvailable) {

		String psmodelsEnconding = "";
		//we add the knowledge like it is
		for (int i=0; i< knowledgeBaseCut.size();i++) {
			psmodelsEnconding += knowledgeBaseCut.get(i).getRule()+"\n";
		}
		//for each preference in the prefereneBaseCut we add a constraint ":- not p."
		for (int i=0; i< preferenceBaseCut.size();i++) {
			psmodelsEnconding += ":- not "+preferenceBaseCut.get(i).getRule()+"\n";
		}
		//for each decision we add four rules "-ass(d) x ass(d)"  "d :- ass(d)" "
		for (int i=0; i< decisionsAvailable.size();i++) {
			psmodelsEnconding += "-ass("+decisionsAvailable.get(i)+") x ass("+decisionsAvailable.get(i)+").\n";
			psmodelsEnconding += decisionsAvailable.get(i)+" :- ass("+decisionsAvailable.get(i)+").\n";
		}
		if (Statics.DEBUG_PSMODELS_REASONER) {
			System.out.println("pessimisticDecisionMakingToLP() program is: "+psmodelsEnconding);
			
		}
		if (Statics.GUI_DETAILS)
			Statics.add("Generated program is: "+psmodelsEnconding);
		return psmodelsEnconding;
	}

	@Override
	public Vector computeDecisionMaking(String logicProgrammingEncoding) {
		
		PrintWriter out = null;
		try {
			out = new PrintWriter(PATH_REASONER_RESOURCES+"/decision-making.lp");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.println(logicProgrammingEncoding);
		out.close();
		
		Vector vector;
		try {
			Statics.changePermissions(new String[]{"lparse", "psmodels"},PATH_REASONER_RESOURCES);
		} catch (IOException e) {
			if (Statics.DEBUG_PSMODELS_REASONER)
				System.out.println("changePermissions exception");
			e.printStackTrace();
		}
		ManageASPInputFile manage = new ManageASPInputFile(PATH_REASONER_RESOURCES, "decision-making.lp");
		vector = manage.invokeAndCreateInput(PATH_LPARSE,PATH_REASONER);
		
		if (Statics.DEBUG_PSMODELS_REASONER)
			System.out.println("after invoking lparse and psmodels");
		
		return vector;
	}

	@Override
	public void testSolver() {
		// TODO Auto-generated method stub

	}

	@Override
	public void printModels(Vector models) {
		ArrayList<String> solution;
		if (models!=null) {
			System.out.println("Models returned by comuteDecisionMaking...");
			for (int i=0; i<models.size();i++) {
				System.out.println("Model "+(i+1)+":");
				solution = (ArrayList<String>) models.get(i);
				for (int j=0; j<solution.size();j++)
					System.out.println(solution.get(j).toString());
			}
		}

	}


}
