package APIObjectStructure;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.DefaultWindowManager;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import APITool.APITool;
import BomberBOTGameAPI.BomberBOTGameAPI;
import ServerObjectStructure.BitFlag;

public class StateDisplay implements Runnable{
	
	private final int informationArea = 60;
	private final int informationAreaShift = 1;
	
	//private int[][] GameMap;
	private String ID;
	
	private TextGraphics Writer;
	private Screen screen;
	
	public StateDisplay(String inputID){
		ID = inputID;
	}
	public void udateGameMap(int inputGameMap[][], int inputPlayerMark){
		
		int basicY = 12 - inputGameMap.length /2;
		int basicX = informationArea/2 - inputGameMap[0].length;
		
		String Wall = null;
		String Path = null;
		String Bomb = null;
		
		String PA = null;
		String PB = null;
		String BombRange = null;
		
		try {
			Wall = new String("▉".getBytes("UTF-8"), Charset.forName("UTF-8"));
			Path = new String("　".getBytes("UTF-8"), Charset.forName("UTF-8"));
			Bomb = new String("◎".getBytes("UTF-8"), Charset.forName("UTF-8"));
			
			PA = new String("A".getBytes("UTF-8"), Charset.forName("UTF-8"));
			PB = new String("B".getBytes("UTF-8"), Charset.forName("UTF-8"));
			
			BombRange = new String("○".getBytes("UTF-8"), Charset.forName("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Writer.setBackgroundColor(TextColor.ANSI.BLACK);
		Writer.setForegroundColor(TextColor.ANSI.WHITE);
	    for(int y = 0 ; y < inputGameMap.length ; y++){
	    	
	    	String MapLine = "";
	    	
			for(int x = 0 ; x <inputGameMap[0].length ; x++){
				
				if(APITool.CompareBitFlag(inputGameMap[y][x], BitFlag.PlayerA))			    MapLine += PA + " ";
				else if(APITool.CompareBitFlag(inputGameMap[y][x], BitFlag.PlayerB)) 		MapLine += PB + " ";
				else if(APITool.CompareBitFlag(inputGameMap[y][x], BitFlag.BlockType_Bomb)) MapLine += Bomb + " ";
				else if((inputGameMap[y][x] & BitFlag.BlockType_BombCDFilter) > 0x0) 		MapLine += BombRange + " ";
				else if(APITool.CompareBitFlag(inputGameMap[y][x], BitFlag.BlockType_Path)) MapLine += Path;
				else if(APITool.CompareBitFlag(inputGameMap[y][x], BitFlag.BlockType_Wall)) MapLine += Wall + " ";
				else MapLine += "?";
				
			}
			
			Writer.putString(basicX, basicY + y, MapLine);
		}
	    

	    
	    try {
			screen.refresh();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		try {
			// Setup terminal and screen layers
	        Terminal terminal = new DefaultTerminalFactory().createTerminal();
	        screen = new TerminalScreen(terminal);
	        screen.startScreen();
	        screen.setCursorPosition(null);
			Writer = screen.newTextGraphics();
			screen.startScreen();
			screen.clear();
			
			String line = "";
			for(int i = 0 ; i < 80 ; i++) line += " ";
				
			for(int i = 0 ; i < 24 ; i++){
				Writer.setBackgroundColor(TextColor.ANSI.WHITE);
				Writer.putString(0, i, line);
				Writer.setBackgroundColor(TextColor.ANSI.YELLOW);
				Writer.putString(informationArea, i, line);
			}
			Writer.setForegroundColor(TextColor.ANSI.BLACK);
			Writer.putString(informationArea, 0, "ID: ");
			Writer.putString(informationArea + informationAreaShift, 1, ID);
			
			Writer.putString(informationArea + informationAreaShift, 23, "API v " + BomberBOTGameAPI.APIversion);
			
			screen.refresh();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
