package sylaires.ctf.util;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

/*
 * Copyright 2022, Sylaires. All rights reserved.
 */

public class ScoreboardBuilder {
	
	private Scoreboard board;
	private ArrayList<String> lines = new ArrayList<String>();
	private Objective ob;
	
	public ScoreboardBuilder(String title, Player p) {
		if(p.getScoreboard().equals(Bukkit.getScoreboardManager().getMainScoreboard())) p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
		board = p.getScoreboard();
		if(board.getObjective(p.getName()) == null) {
			ob = board.registerNewObjective(p.getName(), "dummy");
		}else {
			ob = board.getObjective(p.getName());
		}
		
		ob.setDisplayName(title);
		ob.setDisplaySlot(DisplaySlot.SIDEBAR);
	}
	
	public ScoreboardBuilder addLine(String line) {
		lines.add(line);
		return this;
	}
	
	public boolean hasLine(String line) {
		if(lines.contains(line)) {
			return true;
		}
		return false;
	}
	
	public String getEntryFromScore(int score) {
	    for (String s : board.getEntries()) {
	        if(ob.getScore(s).getScore() == score) return ob.getScore(s).getEntry();
	    }
	    return null;
	}
	
	public boolean hasScoreTaken(int score) {
	    for (String s : board.getEntries()) {
	        if(ob.getScore(s).getScore() == score) return true;
	    }
	    return false;
	}
	
	public void replaceScore(int score, String name) {
	    if(hasScoreTaken(score)) {
	        if(getEntryFromScore(score).equalsIgnoreCase(name)) return;
	        if(!(getEntryFromScore(score).equalsIgnoreCase(name))) board.resetScores(getEntryFromScore(score));
	    }
	    ob.getScore(name).setScore(score);
	}
	
	public Scoreboard build() {
		int num = lines.size();
		for(String s : lines) {
			replaceScore(num, s);
			num--;
		}
		return board;
	}

}
