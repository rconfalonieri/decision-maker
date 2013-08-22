package decision.maker.reasoner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class PASPModelWrapper extends ModelWrapper {

	public PASPModelWrapper(Process p, InputStream is) {
		super(p, is);

	}

	@Override
	public void setLiterals(BufferedReader bf) throws IOException {

		String line = null;
		String possibilisticModelLine;
		String[] lineSplitted;
		ArrayList<String> possibilisticModel ;
		String possibilisticLiteral;
		while ((line = bf.readLine()) != null) {
			//System.out.println("line to parse-> "+line);
			//System.out.println("line to contains Stable model-> "+line.contains("Stable model:"));

			if (line.contains("******** Possibilistic Model")) {
				possibilisticModelLine = bf.readLine();
				//System.out.println("setLiterals-> "+line);
				possibilisticModel = new ArrayList<String>();
				lineSplitted = possibilisticModelLine.split(" ");
				//System.out.println("lineSplitted-> "+lineSplitted.length);
				for (int i=0;i<lineSplitted.length;i++) {
					//if (!lineSplitted[i].contains("Stable") && !lineSplitted[i].contains("Model") && !lineSplitted[i].contains("*")) {
					//System.out.println("literal-> "+lineSplitted[i]);
					possibilisticLiteral = lineSplitted[i];
					possibilisticModel.add(possibilisticLiteral);
					//}
				}
				models.add(possibilisticModel);
			}
			if (line.contains("No possibilistic models")) {
				//System.out.println("No possibilistic models found");
			}
		}

	}

}
