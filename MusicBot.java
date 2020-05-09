import java.io.IOException;
import org.jibble.pircbot.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Locale;
public class MusicBot extends PircBot {
	public MusicBot() {
		setName("MusicBot");
	}
	public void onMessage(String channel, String sender, String login, String hostname, String message) {
		ApiAccess obj = new ApiAccess();
		Map<String, String> Alpha2Codes = new HashMap<>();
		 for(String iso : Locale.getISOCountries()) {
		        Locale l = new Locale("", iso);
		        Alpha2Codes.put(l.getDisplayCountry(), iso);
		 }
		 //Questions you can ask "What are the top/hot [number of] tracks in [country]?"
		 //"What are the top/hot [number of] tracks in the [country]?"
		 //"Who are the top/hot [number of] artists in the US?"
		 //"Top/Hot [number of] tracks in/in the [country]?"
		 //"Top/Hot [number of] artists in/in the [country]?"
		 //"What song/track has the lyrics "[query any possible lyrics]"?
		 //"I want the lyrics for [song name] by [song artist]." currently does not work because the api refuses to
		 //"What are the lyrics for [song name] by [song artist]?" currently does not work because the api refuses to
		if(message.contains("tracks")  && ((message.contains("top") || message.contains("Top")) || (message.contains("hot") || message.contains("Hot")))) {
			String num = message.substring((message.contains("hot")) ? (message.indexOf("hot")+4) : message.contains("Hot") ? (message.indexOf("Hot")+4) : message.contains("top") ? (message.indexOf("top")+4) : (message.indexOf("Top")+4), message.indexOf("tracks")-1);
			String ret[] = new String[Integer.parseInt(num)];
			if(message.contains(" in ") && !message.contains("in the")) {
				int country = message.indexOf("in") + 3;
				String place = "";
				if(message.contains("?")) {
					place = message.substring(country, message.length()-1);
				}
				else {
					place = message.substring(country);
				}
				System.out.println(Alpha2Codes.get(place));
				try {
					ret = obj.musixmatchTopTracks((place.length() == 2) ? place : Alpha2Codes.get(place), "1", num, (message.contains("hot")) ? "hot" : message.contains("Hot") ? "hot" : message.contains("top") ? "top" : "top", "0");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(message.contains("in the")) {
				int country = message.indexOf("in the") + 7;
				String place = "";
				if(message.contains("?")) {
					place = message.substring(country, message.length()-1);
				}
				else {
					place = message.substring(country);
				}
				try {
					ret = obj.musixmatchTopTracks((place.length() == 2) ? place : Alpha2Codes.get(place), "1", num, (message.contains("hot")) ? "hot" : message.contains("Hot") ? "hot" : message.contains("top") ? "top" : "top", "0");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			for(int i = 0; i < ret.length; i++) {
				if(ret[i] != null) {
					sendMessage(channel, ret[i]);
				}
			}
		}
		else if(message.contains("artists") && ((message.contains("top") || message.contains("Top")) || (message.contains("hot") || message.contains("Hot")) || (message.contains("best") || message.contains("Best")))) {
			String num = message.substring((message.contains("hot")) ? (message.indexOf("hot")+4) : message.contains("Hot") ? (message.indexOf("Hot")+4) : message.contains("top") ? (message.indexOf("top")+4) : message.contains("best") ? (message.indexOf("best")+5) : message.contains("Best") ? (message.indexOf("Best")+4) : (message.indexOf("Best")+4), message.indexOf("artists")-1);
			System.out.println(num);
			String ret[] = new String[Integer.parseInt(num)]; 
			if(message.contains(" in ") && !message.contains("in the")) {
				int country = message.indexOf("in") + 3;
				String place = "";
				if(message.contains("?")) {
					place = message.substring(country, message.length()-1);
				}
				else {
					place = message.substring(country);
				}
				System.out.println(Alpha2Codes.get(place));
				try {
					ret = obj.musixmatchTopArtists((place.length() == 2) ? place : Alpha2Codes.get(place), "1", num);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(message.contains("in the")) {
				int country = message.indexOf("in the") + 7;
				String place = "";
				if(message.contains("?")) {
					place = message.substring(country, message.length()-1);
				}
				else {
					place = message.substring(country);
				}
				try {
					ret = obj.musixmatchTopArtists((place.length() == 2) ? place : Alpha2Codes.get(place), "1", num);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			for(int i = 0; i < ret.length; i++) {
				if(ret[i] != null) {
					sendMessage(channel, ret[i]);
				}
			}
		}
		else if(message.contains(" song ") || message.contains(" track ") || message.contains(" songs ") || message.contains(" tracks ") && message.contains("\"")) {
			String ret[] = new String[11];
			if(message.contains("\"")) {
				int firstQ = message.indexOf("\"");
				int secondQ = message.indexOf("\"", firstQ+1);
				try {
					ret = obj.musixMatchLyricsMatcher(message.substring(firstQ+1, secondQ), "", "10");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			for(int i = 0; i < ret.length; i++) {
				if(ret[i] != null && ret[i].contains("by")) {
					sendMessage(channel, ret[i]);
				}
			}
		}
		else if(message.contains(" by ") && message.contains("lyrics") || message.contains("Lyrics")) {
			String artist = "";
			if(message.contains("?")) {
				artist = message.substring(message.indexOf("by ")+3, message.length()-1);
			}
			else {
				artist = message.substring(message.indexOf("by ")+3);
			}
			String song = "";
			if(message.contains("of") || message.contains("to")) {
				song = message.substring(message.indexOf("lyrics")+10, message.indexOf("by"));
			}
			else if(message.contains("for")) {
				song = message.substring(message.indexOf("lyrics")+11, message.indexOf("by"));
			}
			String[] ret = new String[10];
			if(song.substring(song.length()-1).equals(" ")) {
				song = song.substring(0, song.length()-1);
			}
			System.out.println(song);
			System.out.println(artist);
			try {
				ret = obj.mouritsLyrics(artist, song);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sendMessage(channel, "The lyrics of " + song + " by " + artist + " are ");
			for(int i = 0; i < ret.length; i++) {
				if(ret[i] != null && ret[i] != "\\r") {
					sendMessage(channel, ret[i]);
				}
				else {
					sendMessage(channel, "These lyrics are currently queried wrong, try using a different query.");
					break;
				}
			}
		}
	}
	public static void main(String[] args) {
		MusicBot bot = new MusicBot();
		bot.setVerbose(true);
		try {
			bot.connect("irc.freenode.net");
		}
		catch(Exception e) {
			System.out.println("Can't connect: " + e);
			return;
		}
		bot.joinChannel("#Music&Weather");
		bot.sendRawLine("In order to get accurate results top artists and tracks, you need to specify the country of where you need, else it will default to US.");
	}
}