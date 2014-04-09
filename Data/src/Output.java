import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Output {
	
	private static ArrayList<FileWriter> writer;
	private static int fileCounter = 1;
	private static int lineCounter = 0;
	public static void start() {
		String path;
		writer = new ArrayList<FileWriter>();
		
		// Results patch check, use default if none
		if (Constants.results.isEmpty()){
			path = "Results/";
		}
		else{
			path = Constants.results;
		}
		
		// Summary
		if (Constants.summary){
			generateCsvFile("summary", path, Constants.resultsfileName, Constants.index);
			
			if (Constants.gMapLayers){
				File dir = new File(path + Constants.resultsfileName + "_Layers" + "/");
				dir.mkdir();
				generateCsvFile("summary_gMapLayers", path + Constants.resultsfileName + "_Layers" + "/", Constants.resultsfileName, Constants.index);
			}
			
			if (Constants.gMapDevices){
				File dir = new File(path + Constants.resultsfileName + "_DeviceLayers" + "/");
				dir.mkdir();
				generateCsvFile("summary_gMapDevices", path + Constants.resultsfileName + "_DeviceLayers" + "/", Constants.resultsfileName, Constants.index);
			}
			if (Constants.deviceDetail){
				generateCsvFile("summary_unmerge", path,Constants.resultsfileName, Constants.index);
			}
		}
		
		// Coord differences
		if (Constants.coordDiff){
			generateCsvCoordDiff(path, Constants.resultsfileName + "_PhoneGapvsWebApp", Constants.phonegap_webApp);
			generateCsvCoordDiff(path, Constants.resultsfileName + "_PhoneGapvsNative", Constants.phonegap_native);	
			generateCsvCoordDiff(path, Constants.resultsfileName + "_NativevsWebApp", Constants.native_webApp);
		}
		
		// filter.
		if (Constants.filters){
			
			generateCsvFile("summary", path, Constants.resultsfileName + "(filtered by " + Constants.filterDevice +")", Constants.filteredIndex);
			
			if (Constants.gMapLayers){
				File dir = new File(path + Constants.resultsfileName + "_Layers (filtered by " + Constants.filterDevice +")" + "/");
				dir.mkdir();
				generateCsvFile("summary_gMapLayers", path + Constants.resultsfileName + "_Layers (filtered by " + Constants.filterDevice +")" + "/", Constants.resultsfileName, Constants.filteredIndex);
			}
			
			if (Constants.gMapDevices){
				File dir = new File(path + Constants.resultsfileName + "_DeviceLayers (filtered by " + Constants.filterDevice +")" + "/");
				dir.mkdir();
				generateCsvFile("summary_gMapDevices", path + Constants.resultsfileName + "_DeviceLayers (filtered by " + Constants.filterDevice +")" + "/", Constants.resultsfileName, Constants.filteredIndex);
			}

		}
		
	}

	private static void generateCsvFile(String type, String path, String sFileName, Map<String, Map<String, Map<String, ArrayList<String>>>> document) {
		try {
			lineCounter = 0;
			fileCounter = 0;
			// Create File
			if (type == "summary")
				writer.add(0, new FileWriter(path + sFileName + ".csv"));
			else if (type == "summary_unmerge"){
				writer.add(0, new FileWriter(path + sFileName + "(native).csv"));
				writer.add(1, new FileWriter(path + sFileName + "(phonegap).csv"));
				writer.add(2, new FileWriter(path + sFileName + "(webapp).csv"));
			}
			else if (type == "summary_gMapLayers")
				writer.add(0, new FileWriter(path + sFileName + "_" + fileCounter + ".csv"));
			else if (type == "summary_gMapDevices"){
				writer.add(0, new FileWriter(path + sFileName + "_" + fileCounter + "(native).csv"));
				fileCounter++;
				writer.add(1, new FileWriter(path + sFileName + "_" + fileCounter + "(phonegap).csv"));
				fileCounter++;
				writer.add(2, new FileWriter(path + sFileName + "_" + fileCounter + "(webapp).csv"));
				fileCounter++;
			}
			
			addHeader(0, true);
			addEnd(0);
			if (type == "summary_gMapDevices" || type == "summary_unmerge"){
				addHeader(1, true);
				addEnd(1);
				addHeader(2, true);
				addEnd(2);
			}
		
			lineCounter ++;
			if (document.isEmpty()){
				System.err.println("document is empty");
				return;
			}
			
			
			// Start Body
			for (Entry<String, Map<String, Map<String, ArrayList<String>>>> date : document.entrySet()) {
				for (Entry<String, Map<String, ArrayList<String>>> time : date.getValue().entrySet()) {
					
					// Check Lines for google map
					layersCheck(type, path, sFileName);
					
					if ((Constants.validCheck && time.getValue().size() == 3) || !Constants.validCheck) {
							for (Entry<String, ArrayList<String>> device : time.getValue().entrySet()) {
								int k = 0;
								if(type == "summary_gMapDevices" || type == "summary_unmerge"){
									if (device.getKey() == "native"){
										k = 0;
									}
									else if (device.getKey() == "phonegap"){
										k = 1;
									}
									else if (device.getKey() == "webapp"){
										k = 2;
									}
								}
								
								// Date
								writer.get(k).append(date.getKey());
								
								// Time
								writer.get(k).append(',');
								writer.get(k).append(time.getKey()); 
								
								// Device
								writer.get(k).append(',');
								writer.get(k).append(device.getKey());
								
								// Latitude, Longitude, Altitude
								for (int i = 0; i < device.getValue().size(); i++){
									writer.get(k).append(',');
									writer.get(k).append(device.getValue().get(i));
								}
								
								addEnd(k);
							}
							if (type == "summary_gMapDevices"){
								lineCounter ++;
							}
							else{
								lineCounter += 3;
							}
					}
				}
			}

			writer.get(0).close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void generateCsvCoordDiff(String path, String sFileName, Map<String, Map<String, ArrayList<Double>>> document) {
		DecimalFormat df = new DecimalFormat("#.##########");

		try {
			writer.add(0, new FileWriter(path + sFileName + ".csv"));

			addHeader(0, false);
			addEnd(0);


			if (document.isEmpty()){
				System.err.println("document is empty");
			}
			
			for (Entry<String, Map<String, ArrayList<Double>>> date : document.entrySet()) {
				for (Entry<String, ArrayList<Double>> time : date.getValue().entrySet()) {
					writer.get(0).append(date.getKey()); // date
					writer.get(0).append(',');
					writer.get(0).append(time.getKey()); // time

					for (int i = 0; i < time.getValue().size(); i++) {
						writer.get(0).append(',');
						writer.get(0).append(
								df.format(time.getValue().get(i)).toString());
					}
					addEnd(0);
				}
			}

			writer.get(0).close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void addHeader(int i, boolean all){
		try {
			writer.get(i).append("Date");
			writer.get(i).append(',');
			writer.get(i).append("Time");
			writer.get(i).append(',');
			if (all){
			writer.get(i).append("Device");
			writer.get(i).append(',');
			}
			writer.get(i).append("Latitude");
			writer.get(i).append(',');
			writer.get(i).append("Longitude");
			writer.get(i).append(',');
			writer.get(i).append("Accuracy");
			if(!all){
				if(Constants.distranceChoices != null){
					for (int j = 0; j < Constants.distranceChoices.size(); j++){
						writer.get(i).append(',');
						writer.get(i).append(Constants.distranceChoices.get(j));
					}
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void addEnd(int i){
		try {
			writer.get(i).append('\n');
			writer.get(i).flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void layersCheck(String type, String path, String sFileName){
		try {
			if (type == "summary_gMapLayers" && lineCounter > 97){
				writer.get(0).close();
				lineCounter = 0;
				
				writer.add(0, new FileWriter(path + sFileName + "_" + fileCounter + ".csv"));
				fileCounter ++;
				
				addHeader(0, true);
				addEnd(0);
				lineCounter ++;
			}
			else if (type == "summary_gMapDevices" && lineCounter > 99){
				writer.get(0).close();
				writer.get(1).close();
				writer.get(2).close();
				lineCounter = 0;
	
				writer.add(0, new FileWriter(path + sFileName + "_" + fileCounter + "(native).csv"));
				fileCounter++;
				writer.add(1, new FileWriter(path + sFileName + "_" + fileCounter + "(phonegap).csv"));
				fileCounter++;
				writer.add(2, new FileWriter(path + sFileName + "_" + fileCounter + "(webapp).csv"));
				fileCounter++;
				
				addHeader(0, true);
				addEnd(0);
				addHeader(1, true);
				addEnd(1);
				addHeader(2, true);
				addEnd(2);
				lineCounter ++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

