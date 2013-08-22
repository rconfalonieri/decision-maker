package decision.maker.reasoner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ASPModelWrapper extends ModelWrapper {

	public ASPModelWrapper(Process p, InputStream is) {
		super(p, is);

	}

	@Override
	public void setLiterals(BufferedReader bf) throws IOException {

		String line = null;
		String[] lineSplitted;
		ArrayList<String> model ;
		String literal;
		while ((line = bf.readLine()) != null) {
			//System.out.println("line to parse-> "+line);
			//System.out.println("line to contains Stable model-> "+line.contains("Stable model:"));
			if (line.contains("Stable Model:")) {
				//System.out.println("setLiterals-> "+line);
				model = new ArrayList<String>();
				lineSplitted = line.split(" ");
				System.out.println("lineSplitted-> "+lineSplitted.length);
				for (int i=0;i<lineSplitted.length;i++) {
					if (!lineSplitted[i].contains("Stable") && !lineSplitted[i].contains("Model") && !lineSplitted[i].contains("*")) {
						//System.out.println("literal-> "+lineSplitted[i]);
						literal = lineSplitted[i];
						model.add(literal);
					}
				}
				models.add(model);
			}
		}

	}


}
