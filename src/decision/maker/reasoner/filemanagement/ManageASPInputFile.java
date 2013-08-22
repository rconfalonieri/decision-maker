package decision.maker.reasoner.filemanagement;

import global.Statics;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import decision.maker.reasoner.ASPModelWrapper;
import decision.maker.reasoner.ModelWrapper;



public class ManageASPInputFile extends ManageInputFile {

	
	public ManageASPInputFile(String reasonerPath, String fileName) {

		super(reasonerPath,fileName);

	}
	public Vector invokeAndCreateInput(String lparsePath, String psmodelsPath)  {
		
		String result = "";
		Vector vector = null;
		ModelWrapper modelsWrapper = null;
		Process p;
		BufferedReader reader = null;
		int exit_status;
		try {


			String lparseOptionCommand = "--true-negation --cardinality-optimal ";

			String run = "./lparse "+lparseOptionCommand+" "+fileName;//+ " > tmp"; //"+reasonerPath+"/tmp";
			//String[] run = {reasonerPath+"/./lparse","--true-negation","--cardinality-optimal",fileName,"> tmp"};

			System.out.println("lparse "+run);
			//p = Runtime.getRuntime().exec(run); 


			p = Runtime.getRuntime().exec(run,null,new File(reasonerPath));
			//String content = p.getInputStream().toString();
			//System.out.println(content);

			ManageOutput.inputStreamToFileApp(p.getInputStream(),reasonerPath+System.getProperty("file.separator")+"tmp");

			exit_status = p.waitFor();
			if (exit_status != 0){
				System.out.println("lparse returned with error exit code ");//+p.getErrorStream().toString());
				p.destroy();

			}
			System.out.println("after lparse");
			if (!new File(reasonerPath+System.getProperty("file.separator")+"tmp").exists()){
				//if (!new File("/tmp/out").exists()){

				System.out.println("Error!");
			}

			p.getErrorStream().close(); //close the streams even if not accessed, it looks like it's a bug
			p.getInputStream().close(); //i got I/O exception: too many files open when dealing with programs with many models
			p.getOutputStream().close(); //http://saloon.javaranch.com/cgi-bin/ubb/ultimatebb.cgi?ubb=get_topic&f=38&t=000997
			p.destroy();



			//run = psmodelsPath+" "+reasonerPath+"/tmp";
			String run2 = "./psmodels 0 tmp";
			System.out.println("psmodels "+run2);
			p = Runtime.getRuntime().exec(run2,null,new File(reasonerPath));

			//p = Runtime.getRuntime().exec(run);  

			//reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			modelsWrapper = new ASPModelWrapper(p, p.getInputStream());
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

			System.out.println("ASPModelWrapper created...");

			if (modelsWrapper.getModels()==null) {
				System.out.println("No models found");
				vector = modelsWrapper.getModels();
			}
			else { 
				if (Statics.DEBUG_PSMODELS_REASONER){
					Vector<ArrayList> models = modelsWrapper.getModels();
					for (int i=0; i<models.size();i++) {
						ArrayList<String> model = models.get(i);
						System.out.println("Model "+(i+1));
						for (int j=0; j<model.size();j++) {
							System.out.println(model.get(j));
						}
					}
				}

			}
			deleteFiles(new String[]{reasonerPath+System.getProperty("file.separator")+"tmp",reasonerPath+System.getProperty("file.separator")+fileName});
		} 
		catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		} 
		catch (Exception e) {
			System.out.println("psmodels returned with error exit code");
			System.exit(-1);
		}
		return modelsWrapper.getModels();
	}



	
}

