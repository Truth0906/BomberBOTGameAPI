package APIObjectStructure;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import APITool.APITool;
import BomberBOTGameAPI.APIOptions;

public class APICenter {
	
	private static String LogName = "Center";
	
	public APICenter(){
		readOptions();
	}
	private void writeOptions(){
		
		File OptionFile = null;
		
		try {
			OptionFile = new File("Option");
			if(! OptionFile.exists()){
				APITool.showOnScreen(LogName, "Find Option file fail, create new one");
				OptionFile.createNewFile();
			}
		} catch (IOException e) {
			APITool.showOnScreen(LogName, "Error! create new Option file fails");
			return;
		}
		
		try(BufferedWriter Writer = new BufferedWriter(new FileWriter(OptionFile))) {
			
			APIOptions temp = new APIOptions();
			
			String OptionString = APITool.OptionToString(temp);
			
			OptionString = OptionString.replaceAll("\\{", "\\{" + System.lineSeparator() + "\t");
			OptionString = OptionString.replaceAll(",\"", "," + System.lineSeparator() + "\t\"");
			OptionString = OptionString.replaceAll("}", System.lineSeparator() + "}");
			
			Writer.write(OptionString);

			Writer.close();
		    
		} catch (IOException e) {
			APITool.showOnScreen(LogName, "Error! write Option file fails");
			return;
		}
	}
	public void readOptions(){
		
		String OptionString = "";
		
		try(BufferedReader br = new BufferedReader(new FileReader("Option"))) {
			
		    String line = null;
		    
			while ((line = br.readLine()) != null) {
				OptionString += line + System.lineSeparator();
			}
			br.close();
			
		} catch (IOException e) {
			writeOptions();
		}
				
		APITool.StringToOption(OptionString);
		writeOptions();
		APITool.showOnScreen(LogName, "Read Option file success");
	}
}
