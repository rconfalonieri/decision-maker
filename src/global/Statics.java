package global;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

public class Statics {

	public static final boolean ARGUMENTATION_MANAGER_DEBUG = false;
	public static final boolean INFERENCE_DEBUG = false;
	public static final boolean DEBUG_DLV_REASONER = true;
	public static final boolean DEBUG_DECISION_MAKER_REASONER = true;
	public static final boolean DEBUG_PSMODELS_REASONER = true;
	public static final boolean DEBUG_POSPSMODELS_REASONER = true;
	public static final boolean DLV_DISJUNTIVE_IMPLEMENTATION = true;
	public static final boolean DEBUG_POSPSMODELS_DECISION_MAKER = true;
	private static Vector verbose = new Vector(); // Verbose vector.
	public static int equivalences = 0; // Number of equivalent decisions.
	public static boolean equivalence = false; // Is there an equivalence ? 
	public static boolean verboseBool = true;

	public static void add(String log) {

		verbose.add(log);
	}

	public static Vector getVerbose() {

		return verbose;
	}

	public static void clear() {

		verbose.clear();

	}

	public static void changePermissions(String [] files, String reasonerPath) throws IOException{
		Process p;
		String run;
		run ="chmod 777 "+reasonerPath;
		p = Runtime.getRuntime().exec(run);
		for (int i = 0; i < files.length; i++){
			run ="chmod 777 "+files[i];
			//p = Runtime.getRuntime().exec(run,null,new File("./reasoner"));
			p = Runtime.getRuntime().exec(run,null,new File(reasonerPath));
		}
		System.out.println("Permissions changed");
	}

	
}
