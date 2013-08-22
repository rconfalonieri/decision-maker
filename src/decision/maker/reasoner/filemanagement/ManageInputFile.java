package decision.maker.reasoner.filemanagement;

import java.io.File;
import java.util.Vector;

public abstract class ManageInputFile {

	protected String reasonerPath;
	protected String fileName;
	
	public abstract Vector invokeAndCreateInput(String lparsePath, String reasonerPath); 
	
	public ManageInputFile(String reasonerPath, String fileName) {
		
		this.reasonerPath = reasonerPath;
		this.fileName = fileName;
	}
	
	public void deleteFiles(String[] fileToDelete) {

		for (int i=0;i<fileToDelete.length;i++) {
			File file = new File(fileToDelete[i]);

			if(file.delete()){
				System.out.println(file.getName() + " is deleted!");
			}else{
				System.out.println("Delete operation is failed.");
			}
		}
	}
}
