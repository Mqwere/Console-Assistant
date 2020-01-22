package Support;

import java.util.ArrayList;

public class Scribe {
	private ArrayList<String> logs = new ArrayList<String>();
	private int maxLogCap;
	private int currentLogId;
	public boolean logsWereUsed = false;

	public String getStatus() {
		String status = "Scribe Status:" + "\n  maxLogCap:    " 
				+ Integer.toString(this.maxLogCap)
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
}