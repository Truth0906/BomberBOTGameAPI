package ObjectStructure;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import BomberGameBOTAPI.Options;
import bin.Tool.ServerTool;

public class Center {
	
	private static String LogName = "Center";
	
	private void writeOptions(){
		
		File OptionFile = null;
		
		try {
			OptionFile = new File("Option");
			if(! OptionFile.exists()){
				ServerTool.showOnScreen(LogName, "Find Option file fail, create new one");
				OptionFile.createNewFile();
			}
		} catch (IOException e) {
			ServerTool.showOnScreen(LogName, "Error! create new Option file fails");
			return;
		}
		
		try(BufferedWriter Writer = new BufferedWriter(new FileWriter(OptionFile))) {
			
			Options temp = new Options();
			
			String OptionString = ServerTool.OptionToString(temp);
			
			OptionString = OptionString.replaceAll("\\{", "\\{" + System.lineSeparator() + "\t");
			OptionString = OptionString.replaceAll(",\"", "," + System.lineSeparator() + "\t\"");
			OptionString = OptionString.replaceAll("}", System.lineSeparator() + "}");
			
			Writer.write(OptionString);

			Writer.close();
		    
		} catch (IOException e) {
			ServerTool.showOnScreen(LogName, "Error! write Option file fails");
			return;
		}
		ServerTool.showOnScreen(LogName, "Write Option file success");
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
				
		ServerTool.StringToOption(OptionString);
		writeOptions();
		
	}
}
