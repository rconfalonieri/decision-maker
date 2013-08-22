package decision.maker.reasoner.exception;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ErrorMsg {
	
	public void fatal(String msg, Process p) {
		String temp_input;
		System.out.println(msg);
		BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
		try {
			while ((temp_input = stdError.readLine()) != null) {
			    System.out.println(temp_input);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.exit(-1);
	}
}
