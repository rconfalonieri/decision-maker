package decision.maker.reasoner;

import global.Statics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;

import decision.maker.handler.WeightedRule;
import decision.maker.reasoner.filemanagement.ManageASPInputFile;
import decision.maker.reasoner.filemanagement.ManagePASPInputFile;

public class PosPsmodelsReasonerHandler implements IReasonerHandler {

	private String PATH_REASONER_RESOURCES;
	private String PATH_RESOURCES;
	private String PATH_REASONER;
	//private String PATH_LPARSE;
	private URL URL_RESOURCES;

	public PosPsmodelsReasonerHandler() {

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
				if (Statics.DEBUG_POSPSMODELS_REASONER)
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
				if (Statics.DEBUG_POSPSMODELS_REASONER)
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
				PATH_REASONER_RESOURCES = PATH_RESOURCES+"/posPsmodels/mac";
				PATH_REASONER = PATH_RESOURCES+"/posPsmodels/mac/./posPsmodels";
				//PATH_LPARSE = PATH_RESOURCES+"/posPsmodels/mac/./lparse";	
			}
			if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0) {
				PATH_REASONER_RESOURCES = PATH_RESOURCES+"/posPsmodels/linux";
				PATH_REASONER = PATH_RESOURCES+"/posPsmodels/linux/./posPsmodels";
				//PATH_LPARSE = PATH_RESOURCES+"/psmodels/linux/./lparse";
			}
			if (Statics.DEBUG_POSPSMODELS_REASONER) {
				System.out.println("PsmodelsHandler()->PATH_REASONER_RESOURCES "+PATH_REASONER_RESOURCES);
				System.out.println("PsmodelsHandler()->PATH_POSPSMODELS "+PATH_REASONER);
				//System.out.println("PsmodelsHandler()->PATH_LPARSE "+PATH_LPARSE);
			}
		}
		else 
			if (Statics.DEBUG_POSPSMODELS_REASONER) {
				System.out.println("PsmodelsHandler()->PATH_REASONER_RESOURCES not found!");
				System.out.println("PsmodelsHandler()->PATH_POSPSMODELS not found!");
				//System.out.println("PsmodelsHandler()->PATH_LPARSE not found!");
			}

	}

	@Override
	public String optimisticDecisionMakingToLP(
			ArrayList<WeightedRule> knowledgeBaseCut,
			ArrayList<WeightedRule> preferenceBaseCut,
			ArrayList<String> decisionsAvailable) {
		
		String posPsmodelsEnconding = "";

		//we add the knowledge like it is
		for (int i=0; i< knowledgeBaseCut.size();i++) {
			posPsmodelsEnconding += (int)(knowledgeBaseCut.get(i).getWeight()*100)+" "+knowledgeBaseCut.get(i).getRule()+"\n";
		}
		//for each preference in the prefereneBaseCut we add a constraint ":- not p."
		for (int i=0; i< preferenceBaseCut.size();i++) {
			posPsmodelsEnconding += (int)(preferenceBaseCut.get(i).getWeight()*100)+" "+preferenceBaseCut.get(i).getRule()+"\n";
		}

		//for each decision we add four rules "-ass(d) x ass(d)"  "d :- ass(d)" "
		for (int i=0; i< decisionsAvailable.size();i++) {
			posPsmodelsEnconding += "100 -ass("+decisionsAvailable.get(i)+") x ass("+decisionsAvailable.get(i)+").\n";
			posPsmodelsEnconding += "100 "+decisionsAvailable.get(i)+" :- ass("+decisionsAvailable.get(i)+").\n";
		}
		if (Statics.DEBUG_POSPSMODELS_REASONER) {
			System.out.println("optimisticDecisionMakingToLP() program is: "+posPsmodelsEnconding);
			Statics.add("Generated program is: "+posPsmodelsEnconding);
		}
		return posPsmodelsEnconding;
	}

	@Override
	public String pessimisticDecisionMakingToLP(
			ArrayList<WeightedRule> knowledgeBaseCut,
			ArrayList<WeightedRule> preferenceBaseCut,
			ArrayList<String> decisionsAvailable) {

		String posPsmodelsEnconding = "";

		//we add the knowledge like it is
		for (int i=0; i< knowledgeBaseCut.size();i++) {
			posPsmodelsEnconding += (int)(knowledgeBaseCut.get(i).getWeight()*100)+" "+knowledgeBaseCut.get(i).getRule()+"\n";
		}
		//for each preference in the prefereneBaseCut we add a constraint ":- not p."
		/*for (int i=0; i< preferenceBaseCut.size();i++) {
			posPsmodelsEnconding += ":- not "+preferenceBaseCut.get(i).getRule()+"\n";
		}*/

		//for each decision we add four rules "-ass(d) x ass(d)"  "d :- ass(d)" "
		for (int i=0; i< decisionsAvailable.size();i++) {
			posPsmodelsEnconding += "100 -ass("+decisionsAvailable.get(i)+") x ass("+decisionsAvailable.get(i)+").\n";
			posPsmodelsEnconding += "100 "+decisionsAvailable.get(i)+" :- ass("+decisionsAvailable.get(i)+").\n";
		}
		if (Statics.DEBUG_POSPSMODELS_REASONER) {
			System.out.println("pessimisticDecisionMakingToLP() program is: "+posPsmodelsEnconding);
			Statics.add("Generated program is: "+posPsmodelsEnconding);
		}
		return posPsmodelsEnconding;
	}

	@Override
	public Vector computeDecisionMaking(String logicProgrammingEncoding) {
		PrintWriter out = null;
		String f = PATH_REASONER_RESOURCES+System.getProperty("file.separator")+"lppod"+System.getProperty("file.separator")+"decisionmaking.lppod";
		System.out.println("computeDecisionMaking()->lppodFile: "+f);
		try {
			out = new PrintWriter(PATH_REASONER_RESOURCES+System.getProperty("file.separator")+"lppod"+System.getProperty("file.separator")+"decisionmaking.lppod");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.println(logicProgrammingEncoding);
		out.close();
		
		Vector vector;
		try {
			Statics.changePermissions(new String[]{"lparse", "psmodels", "preprocLparse", "posSmodels", "posPsmodels"},PATH_REASONER_RESOURCES);
		} catch (IOException e) {
			System.out.println("changePermissions exception");
			e.printStackTrace();
		}
		ManagePASPInputFile manage = new ManagePASPInputFile(PATH_REASONER_RESOURCES, "decisionmaking.lppod");
		vector = manage.invokeAndCreateInput("",PATH_REASONER);
		
		System.out.println("after invoking posPsmodels");
		
		return vector;
	}

	@Override
	public void testSolver() {
		// TODO Auto-generated method stub

	}

	@Override
	public void printModels(Vector models) {
		// TODO Auto-generated method stub

	}

}
