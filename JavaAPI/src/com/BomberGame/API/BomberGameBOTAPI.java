package com.BomberGame.API;

public class BomberGameBOTAPI {
	
	static{
		System.loadLibrary("BomberGameBOTAPI");
	}
	private native void echo(String inputMsg);
	
	public static void main(String[] args) {
		new BomberGameBOTAPI().echo("Hello Bomber Game!");
	}
}
