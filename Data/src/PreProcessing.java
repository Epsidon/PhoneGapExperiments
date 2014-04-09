import java.io.BufferedReader;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class PreProcessing {

	public static ArrayList<ArrayList<String>> getDocuments() {
		// result = [date, time, filetype, latitude, longitude, accuracy]
		ArrayList<ArrayList<String>> dataCollection = new ArrayList<ArrayList<String>>();
		
		File folder;
		if (Constants.resources.isEmpty()){
			folder = new File("Resources/");
		}
		else{
			folder = new File(Constants.resources);
		}
		File[] listOfFiles = folder.listFiles();

		for (File file : listOfFiles) {

		    if (file.isFile()) {
				try (BufferedReader reader = Files.newBufferedReader(file.getAbsoluteFile().toPath(), Charset.forName("UTF-8"))) {
					
					String line = null;
					if (file.getName().toLowerCase().contains("ios")) {
						if (file.getName().toLowerCase().contains("native")) {
							while ((line = reader.readLine()) != null) {
								if (line.contains("didUpdateToLocation:")) {
									addNativeiOSEntry(dataCollection, line);
								}
							}
						} else if (file.getName().toLowerCase()
								.contains("phonegap")) {
							while ((line = reader.readLine()) != null) {
								if (line.contains("[Log]")) {
									addJSiOSEntry(dataCollection, "phonegap",
											reader.readLine(),
											reader.readLine(),
											reader.readLine(),
											reader.readLine());
								}
							}
						} else if (file.getName().toLowerCase()
								.contains("web app")) {
							while ((line = reader.readLine()) != null) {
								if (line.contains("[Log]")) {
									addJSiOSEntry(dataCollection, "webapp",
											reader.readLine(),
											reader.readLine(),
											reader.readLine(),
											reader.readLine());
								}
							}
						}
					} else if (file.getName().toLowerCase().contains("android")) {
						if (file.getName().toLowerCase().contains("native")) {
							while ((line = reader.readLine()) != null) {
								if(line.contains("Time")){
									addNativeAndroidEntry(dataCollection, line);
								}
							}	
						} else if (file.getName().toLowerCase().contains("phonegap")) {
							while ((line = reader.readLine()) != null) {
								if (line.contains("Latitude")) {
									addJSAndroidEntry(dataCollection, line);
								}
							}
						} else if (file.getName().toLowerCase().contains("web app")) {
							while ((line = reader.readLine()) != null) {
								if (line.contains("Time")){
									String info = line; // time
									@SuppressWarnings("unused")
									String trash = reader.readLine();
									
									info += " " + reader.readLine(); // lat
									trash = reader.readLine();
									
									info += " " + reader.readLine(); // long
									trash = reader.readLine();
									
									info += " " + reader.readLine(); // acc
									
									addWepAppAndroidEntry(dataCollection, info);
								}
							}
						}
					} else {
						System.err.println("Invalid File Type: "
								+ file.getName());
					}
				} catch (Exception x) {
					System.err.format("Exception: %s%n", x);
				}
			}
		}

		return dataCollection;
	}
	
	private static void addWepAppAndroidEntry(ArrayList<ArrayList<String>> result, String line) {

		line = line.toLowerCase();
		line = line.replaceAll("time:", "");
		line = line.replaceAll("latitude:", "");
		line = line.replaceAll("longitude:", "");
		line = line.replaceAll("accuracy:", "");
		String[] words = line.split("\\s");
		
		
		DateFormat readFormat = new SimpleDateFormat( "EEE MMM dd yyyy HH:mm:ss");
	    DateFormat writeFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");

	    Date date = null;
	    try {
	       date = readFormat.parse(words[1] + " " +  words[2] + " " + words[3] + " " + words[4] + " " + words[5]);
	    } catch ( ParseException e ) {
	        e.printStackTrace();
	    }

	    String formattedDate = "";
	    if( date != null ) {
	    formattedDate = writeFormat.format( date );
	    }
		
	    String[] splitDate = formattedDate.toString().split("\\s");
	    
		ArrayList<String> info = new ArrayList<String>();
		// date
		info.add(splitDate[0]);
		// time
		info.add(splitDate[1]);
		// device
		info.add("webapp");
		// lat
		info.add(words[10]);
		// long
		info.add(words[13]);
		// acc
		info.add(words[16]);

		result.add(info);	
	}

	private static void addJSAndroidEntry(ArrayList<ArrayList<String>> result, String line) {
		
		line = line.toLowerCase();
		line = line.replaceAll("time :", "time: ");
		String[] words = line.split("\\s");
		
		DateFormat readFormat = new SimpleDateFormat( "EEE MMM dd yyyy HH:mm:ss");
	    DateFormat writeFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");

	    Date date = null;
	    try {
	       date = readFormat.parse(words[5] + " " +  words[6] + " " + words[7] + " " + words[8] + " " + words[9]);
	    } catch ( ParseException e ) {
	        e.printStackTrace();
	    }

	    String formattedDate = "";
	    if( date != null ) {
	    formattedDate = writeFormat.format( date );
	    }
		
	    String[] splitDate = formattedDate.toString().split("\\s");
	    
		ArrayList<String> info = new ArrayList<String>();
		// date
		info.add(splitDate[0]);
		// time
		info.add(splitDate[1]);
		// device
		info.add("phonegap");
		// lat
		info.add(words[1]);
		// long
		info.add(words[3]);
		// acc
		info.add(words[13].toString().substring(0, words[13].toString().length()-1));
		
		result.add(info);	
	}

	private static void addNativeAndroidEntry(ArrayList<ArrayList<String>> result, String line) {
		line = line.toLowerCase();
	    DateFormat readFormat = new SimpleDateFormat( "EEE MMM dd HH:mm:ss z yyyy");
	    DateFormat writeFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");

	    Date date = null;
	    try {
	       date = readFormat.parse( line.substring(6, 34) );
	    } catch ( ParseException e ) {
	        e.printStackTrace();
	    }
	    
	    String formattedDate = "";
	    if( date != null ) {
	    formattedDate = writeFormat.format( date );
	    }
	    line = line.replaceAll(line.substring(6, 34), "");
	    line = line.replaceAll("time: ", "");
	    line = line.replaceAll("latitude: ", "");
	    line = line.replaceAll("longitude: ", "");
	    line = line.replaceAll("accuracy: ", "");
		String[] words = (formattedDate + " " + line).split("\\s");
		
		ArrayList<String> info = new ArrayList<String>();
		// date
		info.add(words[0]);
		// time
		info.add(words[1]);
		// device
		info.add("native");
		// lat
		info.add(words[3]);
		// long
		info.add(words[4]);
		// acc
		info.add(words[5]);
		
		result.add(info);
	}

	private static void addNativeiOSEntry(ArrayList<ArrayList<String>> result, String line) {

		line = line.toLowerCase();
		line = line.replaceAll("<|>", "");
		line = line.replaceAll(",", " ");
		String[] words = line.split("\\s");

		ArrayList<String> info = new ArrayList<String>();
		// date
		info.add(words[0]);
		// time
		info.add(words[1].substring(0, 8));
		// device
		info.add("native");
		// lat
		info.add(words[5]);
		// long
		info.add(words[6]);
		// acc
		info.add(words[8].substring(0, words[8].length() - 1));

		result.add(info);
	}

	private static void addJSiOSEntry(ArrayList<ArrayList<String>> result, String type, String line1, String line2, String line3, String line4) {

	    DateFormat readFormat = new SimpleDateFormat( "EEE MMM dd yyyy HH:mm:ss");
	    DateFormat writeFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
	    Date date = null;
	    try {
	       date = readFormat.parse( line1.substring(12, 36) );
	    } catch ( ParseException e ) {
	        e.printStackTrace();
	    }

	    String formattedDate = "";
	    if( date != null ) {
	    formattedDate = writeFormat.format( date );
	    }
		
		line2 = line2.replaceAll("Latitude:", "");
		String[] words = (formattedDate + " " + line2 + line3 + line4).split("\\s");
		
		ArrayList<String> info = new ArrayList<String>();
		// date
		info.add(words[0]);
		// time
		info.add(words[1]);
		// device
		info.add(type);
		// lat
		info.add(words[4]);
		// long
		info.add(words[9]);
		// acc
		info.add(words[14]);

		result.add(info);
	}
}
