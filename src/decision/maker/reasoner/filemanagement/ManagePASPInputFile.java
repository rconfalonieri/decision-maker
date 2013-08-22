package decision.maker.reasoner.filemanagement;

import global.Statics;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import decision.maker.reasoner.ModelWrapper;
import decision.maker.reasoner.PASPModelWrapper;

public class ManagePASPInputFile extends ManageInputFile {

	public ManagePASPInputFile(String reasonerPath, String fileName) {
		super(reasonerPath, fileName);

	}

	@Override
	public Vector invokeAndCreateInput(String lparsePath, String posPsmodelsPath) {

		String result = "";
		Vector vector = null;
		ModelWrapper modelsWrapper = null;
		Process p;
		BufferedReader reader = null;
		int exit_status;
		try {

			String posPosmodelsOptionCommand = "--cardinality-optimal";

			//run = psmodelsPath+" "+reasonerPath+"/tmp";
			String run = "./posPsmodels lppod"+System.getProperty("file.separator")+fileName+" "+posPosmodelsOptionCommand;
			System.out.println("posPsmodels "+run);
			p = Runtime.getRuntime().exec(run,null,new File(reasonerPath));

			//reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			modelsWrapper = new PASPModelWrapper(p, p.getInputStream());
			modelsWrapper.run();

			exit_status = p.waitFor();
			if (exit_status != 0){
				System.out.println("psmodels returned with error exit code "+p);
				p.destroy();

			}
			p.getErrorStream().close(); //close the streams even if not accessed, it looks like it's a bug
			p.getInputStream().close(); //i got I/O exception: too many files open when dealing with programs with many models
			p.getOutputStream().close(); //http://saloon.javaranch.com/cgi-bin/ubb/ultimatebb.cgi?ubb=get_topic&f=38&t=000997
			p.destroy();

			System.out.println("PASPModelWrapper created...");

			if (modelsWrapper.getModels()==null) {
				System.out.println("No possibilistic models found");
				vector = modelsWrapper.getModels();
			}
			else { 
				if (Statics.DEBUG_POSPSMODELS_REASONER){
					Vector<ArrayList> models = modelsWrapper.getModels();
					for (int i=0; i<models.size();i++) {
						ArrayList<String> model = models.get(i);
						System.out.println("PossibilisticModel "+(i+1));
						for (int j=0; j<model.size();j++) {
							System.out.println(model.get(j));
						}
					}
				}

			}

			deleteFilesInFolders(new String[]{reasonerPath+System.getProperty("file.separator")+"lpod"+System.getProperty("file.separator"),
					reasonerPath+System.getProperty("file.separator")+"lpodmodels"+System.getProperty("file.separator"),
					reasonerPath+System.getProperty("file.separator")+"reducedlppod"+System.getProperty("file.separator"),
					reasonerPath+System.getProperty("file.separator")+"tmp"+System.getProperty("file.separator")});
			
			
			deleteFiles(new String[]{reasonerPath+System.getProperty("file.separator")+"lppod"+System.getProperty("file.separator")+fileName});

		} 
		catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		} 
		catch (Exception e) {
			System.out.println("posPsmodels returned with error exit code");
			System.exit(-1);
		}
		return modelsWrapper.getModels();
	}

	private void deleteFilesInFolders(String[] foldersTodelete) {

		for (int i=0;i<foldersTodelete.length;i++) {
			File folder = new File(foldersTodelete[i]);

			for(File file: folder.listFiles()) {
				if(file.delete()){
					System.out.println(file.getName() + " is deleted!");
				}
				else{
					System.out.println("Delete operation is failed.");
				}
			}
		}
	}

}


