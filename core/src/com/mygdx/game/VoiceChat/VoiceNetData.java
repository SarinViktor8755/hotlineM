package com.mygdx.game.VoiceChat;

public class VoiceNetData {

	private short[] data;
	
	public VoiceNetData(){
		
	}
	
	public VoiceNetData(short[] data){	
		this.data = data;
	}
	
	public short[] getData(){
		return data;
	}
}
