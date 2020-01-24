package Support;

import java.util.ArrayList;

import Core.Assistant;
import Core.Command;

public class Scribe {
	private ArrayList<String> logs = new ArrayList<String>();
	private int maxLogCap;
	private int currentLogId;
	public boolean logsWereUsed = false;

	public String lastInput = "";
	public boolean suggestionUsed = true;

	public String getStatus() {
		String status = "Scribe Status:" + "\n  maxLogCap:    " + Integer.toString(this.maxLogCap)
				+ "\n  currentLogId: " + Integer.toString(this.currentLogId) + "\n  logsWereUsed: "
				+ Boolean.toString(this.logsWereUsed) + "\n  logs.size():  " + Integer.toString(this.logs.size());

		return status;
	}

	public Scribe(int capacity) {
		this.maxLogCap = capacity;
		this.currentLogId = 0;
		this.logsWereUsed = false;
	}

	private void checkSize() {
		if (this.logs.size() >= this.maxLogCap) {
			ArrayList<String> temp = new ArrayList<String>();
			for (int i = 1; i < this.logs.size(); i++) {
				temp.add(this.logs.get(i));
			}
			logs.clear();
			logs = temp;
		}
	}

	public String upGoThemLogs() {
		if (!this.logsWereUsed) {
			this.logsWereUsed = true;
			if (this.logs.size() > 0)
				return logs.get(this.currentLogId);
			else {
				return null;
			}
		} else {
			if (--this.currentLogId >= 0) {
				return logs.get(this.currentLogId);
			} else {
				this.currentLogId++;
				return null;
			}
		}
	}

	public String tabGuess(String input) {
		String eval;
		boolean skip = false;
		if (suggestionUsed) {
			eval = input.toUpperCase();
			this.lastInput = input;
		}
		else {
			eval = this.lastInput.toUpperCase();
			skip = true;
		}
		
		//communicate("Initiating tabGuess sequence for \""+eval+"\"");
		
		int size = eval.length();
		if (size > 0) {
			for(Command a: Command.values()) {
				if(!suggestionUsed) {
					if(input.equals(a.name())){
						skip = false; 
						//communicate(input+"is the same as a.name():"+a.name()); continue;
					}
					else {
						if(skip) {
							//communicate("Skipping "+a.name());
							continue;
						}
						else {
							String diff = a.name().substring(0,size-1);
							if(diff.length()==0) diff += a.name().charAt(0);
							if(eval.equals(diff)) {
								//communicate("Found a Command! " + a.name());
								suggestionUsed = false;
								return a.name().toLowerCase();
							}
							else {
								//communicate(eval+" =/= "+diff);
								continue;
							}
						}
					}
				}
				else {
					String diff = a.name().substring(0,size-1);
					if(diff.length()==0) diff += a.name().charAt(0);
					if(eval.equals(diff)) {
						//communicate("Found a Command! " + a.name());
						suggestionUsed = false;
						return a.name().toLowerCase();
					}
					else {
						//communicate(eval+" =/= "+diff);
						continue;
					}
				}
			}
			if (suggestionUsed) return input;
			else{
				this.suggestionUsed = true;
				return this.lastInput;
			}
		} else {
			return input;
		}
	}

	public String downGoThemLogs() {
		if (this.logsWereUsed) {
			if (++this.currentLogId < this.logs.size())
				return logs.get(this.currentLogId);
			else {
				this.currentLogId--;
				this.logsWereUsed = false;
			}
		}
		return null;
	}

	public void log(String input) {
		this.logsWereUsed = false;
		if (this.logs.size() > 0) {
			if (input.equals(logs.get(this.currentLogId))) {
				logs.remove(this.currentLogId);
			}
		}
		this.checkSize();
		this.logs.add(input);
		this.currentLogId = this.logs.size() - 1;
	}
	
	public static Integer tryIntParse(String input) {
		try {
			int result = Integer.parseInt(input);
			return result;
		}
		catch(Exception e) {return null;}
	}
	
	private void communicate(Object message) {
		Assistant.write("Scribe",message);
	}
}