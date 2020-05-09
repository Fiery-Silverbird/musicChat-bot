import java.io.IOException;
import java.io.*;
import java.net.*;
public class ApiAccess {
	public String[] musixmatchTopArtists(String country, String page, String page_size) throws MalformedURLException, ProtocolException, IOException {
		String root = "https://api.musixmatch.com/ws/1.1/";
		String exc = "chart.artists.get?page=" + page + "&page_size=" + page_size + "&country=" + country;
		String apiKey = "&apikey=bed48e90a27045331778f9bfa47ea5d5";
		URL topArtistsData = new URL(root + exc + apiKey);
		HttpURLConnection con = (HttpURLConnection) topArtistsData.openConnection();
		con.setRequestMethod("GET");
		con.setConnectTimeout(5000);
		con.setReadTimeout(5000);
		con.setInstanceFollowRedirects(false);
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer content = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			content.append(inputLine);
		}
		in.close();
		con.disconnect();
		String parseW = content.toString();
		String artists = "";
		int temp = 0;
		for(int i = 0; i < Integer.parseInt(page_size); i++) {
			int artist_name_S = parseW.indexOf("\"artist_name\"", temp);
			int artist_name_E = parseW.indexOf(",", artist_name_S);
			temp = artist_name_E;
			artists += (i+1) + ". " + parseW.substring(artist_name_S+15, artist_name_E-1) + ", ";
		}
		String[] ret = artists.split(", ");
		for(int i = 0; i < ret.length; i++) {
			if(ret[i].contains("\\u00e9")) {
				ret[i].trim();
				String x = ret[i].substring(0, ret[i].indexOf("\\u00e9"));
				x += "e ";
				ret[i] = x + ret[i].substring(ret[i].indexOf("\\u00e9")+7);
			}
			else if(ret[i].contains("\\u00ed")) {
				ret[i].trim();
				String x = ret[i].substring(0, ret[i].indexOf("\\u00ed"));
				x += "c";
				ret[i] = x + ret[i].substring(ret[i].indexOf("\\u00ed")+7);
			}
		}
		return ret;
	}
	public String[] musixmatchTopTracks(String country, String page, String page_size, String chart_type, String toggle) throws MalformedURLException, ProtocolException, IOException {
		String root = "https://api.musixmatch.com/ws/1.1/";
		String exc = "chart.tracks.get?chart_name=" + chart_type + "&page=" + page + "&page_size=" + page_size + "&country=" + country + "&f_has_lyrics=" + toggle;
		String apiKey = "&apikey=bed48e90a27045331778f9bfa47ea5d5";
		URL topTracksData = new URL(root + exc + apiKey);
		HttpURLConnection con = (HttpURLConnection) topTracksData.openConnection();
		con.setRequestMethod("GET");
		con.setConnectTimeout(5000);
		con.setReadTimeout(5000);
		con.setInstanceFollowRedirects(false);
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer content = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			content.append(inputLine);
		}
		in.close();
		con.disconnect();
		String parseW = content.toString();
		String tracks = "";
		int temp = 0;
		for(int i = 0; i < Integer.parseInt(page_size); i++) {
			int track_name_S = parseW.indexOf("\"track_name\"", temp);
			int track_name_E = parseW.indexOf(",", track_name_S);
			tracks += (i+1) + ". " + parseW.substring(track_name_S+14, track_name_E-1);
			int artist_name_S = parseW.indexOf("\"artist_name\"", track_name_E);
			int artist_name_E = parseW.indexOf(",", artist_name_S);
			temp = artist_name_E;
			tracks += " by " + parseW.substring(artist_name_S+15, artist_name_E-1) + ", ";
		}
		String[] ret =  tracks.split(", ");
		for(int i = 0; i < ret.length; i++) {
			if(ret[i].contains("\\u00e9")) {
				ret[i].trim();
				String x = ret[i].substring(0, ret[i].indexOf("\\u00e9"));
				x += "e ";
				ret[i] = x + ret[i].substring(ret[i].indexOf("\\u00e9")+7);
			}
			else if(ret[i].contains("\\u00ed")) {
				ret[i].trim();
				String x = ret[i].substring(0, ret[i].indexOf("\\u00ed"));
				x += "i ";
				ret[i] = x + ret[i].substring(ret[i].indexOf("\\u00ed")+7);
			}
		}
		return ret;
	}
	public String[] musixMatchLyricsMatcher(String lyrics, String artist, String page_size) throws MalformedURLException, ProtocolException, IOException {
		String root = "https://api.musixmatch.com/ws/1.1/track.search?format=jsonp&callback=callback&";
		String apiKey = "&apikey=bed48e90a27045331778f9bfa47ea5d5";
		String t = lyrics;
		lyrics.trim();
		lyrics = lyrics.replaceAll("\\s", "%20");
		artist.trim();
		artist = artist.replaceAll("\\s", "%20");
		if(artist.isEmpty()) {
			root += "q_lyrics=" + lyrics + "&s_track_rating=desc&quorum_factor=1&page_size=" + page_size + "&page=1";
		}
		else {
			root += "q_lyrics=" + lyrics + "&q_artist=" + artist + "&s_track_rating=desc&quorum_factor=1&page_size=" + page_size + "&page=1";
		}
		URL songData = new URL(root+apiKey);
		HttpURLConnection con = (HttpURLConnection) songData.openConnection();
		con.setRequestMethod("GET");
		con.setConnectTimeout(5000);
		con.setReadTimeout(5000);
		con.setInstanceFollowRedirects(false);
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer content = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			content.append(inputLine);
		}
		in.close();
		con.disconnect();
		String parseW = content.toString();
		int temp = 0;
		String[] ret = new String[Integer.parseInt(page_size)+1];
		ret[0] = "The songs closest to \"" + t + "\" are these " + page_size + " songs.";
		for(int i = 0; i < Integer.parseInt(page_size); i++) {
			int track_name_S = parseW.indexOf("\"track_name\"", temp);
			int track_name_E = parseW.indexOf(",", track_name_S);
			ret[i+1] = parseW.substring(track_name_S+14, track_name_E-1);
			int artist_name_S = parseW.indexOf("\"artist_name\"", track_name_E);
			int artist_name_E = parseW.indexOf(",", artist_name_S);
			temp = artist_name_E;
			ret[i+1] += " by " + parseW.substring(artist_name_S+15, artist_name_E-1);
		}
		for(int i = 0; i < ret.length; i++) {
			if(ret[i].contains("\\u00e9")) {
				ret[i].trim();
				String x = ret[i].substring(0, ret[i].indexOf("\\u00e9"));
				x += "e ";
				ret[i] = x + ret[i].substring(ret[i].indexOf("\\u00e9")+7);
			}
			else if(ret[i].contains("\\u00ed")) {
				ret[i].trim();
				String x = ret[i].substring(0, ret[i].indexOf("\\u00ed"));
				x += "i ";
				ret[i] = x + ret[i].substring(ret[i].indexOf("\\u00ed")+7);
			}
		}
		return ret;
	}
	public String[] mouritsLyrics(String artist, String song) throws MalformedURLException, ProtocolException, IOException {
		artist.trim();
		artist = artist.replaceAll("\\s", "%20");
		song.trim();
		song = song.replaceAll("\\s", "%20");
		String root = "https://mourits.xyz:2096/?";
		URL songLyrics = new URL(root + "a=" + artist + "&s=" + song); 
		System.out.println(root + "a=" + artist + song);
		HttpURLConnection con = (HttpURLConnection) songLyrics.openConnection();
		con.addRequestProperty("User-Agent", "Mozilla");
		con.setRequestMethod("GET");
		con.setConnectTimeout(10000);
		con.setReadTimeout(10000);
		con.setInstanceFollowRedirects(false);
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer content = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			content.append(inputLine);
		}
		in.close();
		con.disconnect();
		System.out.println(content.toString().indexOf("\"lyrics\"")+11);
		System.out.println(content.toString().indexOf("\"source\"")-2);
		String parse = content.toString().substring(content.toString().indexOf("\"lyrics\"")+11, content.toString().indexOf("{", content.toString().indexOf("\"lyrics\""))-13);
		parse = parse.replace("\\n", "; ");
		parse = parse.replace("\\r", "");
		parse = parse.replace("\\u2005", " ");
		parse = parse.replaceAll("\\n", "; ");
		parse = parse.replaceAll("\\r", "");
		String[] ret = parse.split("; ");
		return ret;
	}
}