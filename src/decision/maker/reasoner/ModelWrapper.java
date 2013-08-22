package decision.maker.reasoner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Vector;

public abstract class ModelWrapper {

	private InputStream is;
	private Process p;
	protected Vector<ArrayList> models;

	public ModelWrapper(Process p, InputStream is) {
		this.is = is;
		this.p = p;
		models = new Vector<ArrayList>();
	}

	public abstract void setLiterals(BufferedReader bf) throws IOException; 
	
	public void run() throws InterruptedException {
		try {
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			System.out.println("File loaded, start ");
			setLiterals(br);
			p.getErrorStream().close();
			p.getInputStream().close();
			p.getOutputStream().close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.out.println("parsing failed");
			System.exit(-1);
		}

	}

	/**
	 * @return the models
	 */
	public Vector<ArrayList> getModels() {
		return models;
	}

	/**
	 * @param models the models to set
	 */
	public void setModels(Vector<ArrayList> models) {
		this.models = models;
	}
	
	
}
