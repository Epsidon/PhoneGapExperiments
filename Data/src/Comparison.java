import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;


public class Comparison {
	

	public static void start() {
		
		// Initialize
		Constants.phonegap_webApp = new TreeMap<String, Map<String, ArrayList<Double>>>();
		Constants.phonegap_native = new TreeMap<String, Map<String, ArrayList<Double>>>();
		Constants.native_webApp = new TreeMap<String, Map<String, ArrayList<Double>>>();
		
		if (Constants.index.isEmpty()){
			System.err.println("index is empty");
			return;
		}
		for (Entry<String, Map<String, Map<String, ArrayList<String>>>> date : Constants.index.entrySet()){
			for (Entry<String, Map<String, ArrayList<String>>> time : date.getValue().entrySet()){
				Map<String, ArrayList<String>> device = time.getValue();
				
				// Ignore all elements that do not have 3 devices at the exact time.
				if ((device.size() == 3 && Constants.validCheck) || !Constants.validCheck){
					boolean n = false, w = false, p = false;
					ArrayList<String> nativeApp = null, webApp = null, phoneGap = null;
					
					for (Entry<String, ArrayList<String>> entry : device.entrySet()){
						if (entry.getKey() == "native" && !n){
							nativeApp = entry.getValue();
							n = true;
						}
						else if (entry.getKey() == "phonegap" && !p){
							phoneGap = entry.getValue();
							p = true;
						}
						else if (entry.getKey() == "webapp" && !w){
							webApp = entry.getValue();
							w = true;
						}
					}
					if(Constants.validCheck && p && w && n){
						insertEntry(
								"phonegap_webApp",
								date.getKey(),
								time.getKey(),
								Math.abs(Double.parseDouble(phoneGap.get(0))
										- Double.parseDouble(webApp.get(0))),
								Math.abs(Double.parseDouble(phoneGap.get(1))
										- Double.parseDouble(webApp.get(1))),
								Math.abs(Double.parseDouble(phoneGap.get(2))
										- Double.parseDouble(webApp.get(2))),
								DistanceAlgorithm.calculate(
												Double.parseDouble(phoneGap.get(0)),
												Double.parseDouble(phoneGap.get(1)),
												Double.parseDouble(webApp.get(0)),
												Double.parseDouble(webApp.get(1))));
						insertEntry(
								"phonegap_native",
								date.getKey(),
								time.getKey(),
								Math.abs(Double.parseDouble(phoneGap.get(0))
										- Double.parseDouble(nativeApp.get(0))),
								Math.abs(Double.parseDouble(phoneGap.get(1))
										- Double.parseDouble(nativeApp.get(1))),
								Math.abs(Double.parseDouble(phoneGap.get(2))
										- Double.parseDouble(nativeApp.get(2))),
								DistanceAlgorithm.calculate(
												Double.parseDouble(phoneGap.get(0)),
												Double.parseDouble(phoneGap.get(1)),
												Double.parseDouble(nativeApp.get(0)),
												Double.parseDouble(nativeApp.get(1))));
						insertEntry("native_webApp", date.getKey(),
								time.getKey(), Math.abs(Double
										.parseDouble(nativeApp.get(0))
										- Double.parseDouble(webApp.get(0))),
								Math.abs(Double.parseDouble(nativeApp.get(1))
										- Double.parseDouble(webApp.get(1))),
								Math.abs(Double.parseDouble(nativeApp.get(2))
										- Double.parseDouble(webApp.get(2))),
								DistanceAlgorithm.calculate(
										Double.parseDouble(nativeApp.get(0)),
										Double.parseDouble(nativeApp.get(1)),
										Double.parseDouble(webApp.get(0)),
										Double.parseDouble(webApp.get(1))));
					}
					else {
						if (p && w){
							insertEntry(
									"phonegap_webApp",
									date.getKey(),
									time.getKey(),
									Math.abs(Double.parseDouble(phoneGap.get(0))
											- Double.parseDouble(webApp.get(0))),
									Math.abs(Double.parseDouble(phoneGap.get(1))
											- Double.parseDouble(webApp.get(1))),
									Math.abs(Double.parseDouble(phoneGap.get(2))
											- Double.parseDouble(webApp.get(2))),
									DistanceAlgorithm.calculate(
													Double.parseDouble(phoneGap.get(0)),
													Double.parseDouble(phoneGap.get(1)),
													Double.parseDouble(webApp.get(0)),
													Double.parseDouble(webApp.get(1))));
						}
						if (n && p){
							insertEntry(
									"phonegap_native",
									date.getKey(),
									time.getKey(),
									Math.abs(Double.parseDouble(phoneGap.get(0))
											- Double.parseDouble(nativeApp.get(0))),
									Math.abs(Double.parseDouble(phoneGap.get(1))
											- Double.parseDouble(nativeApp.get(1))),
									Math.abs(Double.parseDouble(phoneGap.get(2))
											- Double.parseDouble(nativeApp.get(2))),
									DistanceAlgorithm.calculate(
													Double.parseDouble(phoneGap.get(0)),
													Double.parseDouble(phoneGap.get(1)),
													Double.parseDouble(nativeApp.get(0)),
													Double.parseDouble(nativeApp.get(1))));
						}
						if (n && w){
							insertEntry("native_webApp", date.getKey(),
									time.getKey(), Math.abs(Double
											.parseDouble(nativeApp.get(0))
											- Double.parseDouble(webApp.get(0))),
									Math.abs(Double.parseDouble(nativeApp.get(1))
											- Double.parseDouble(webApp.get(1))),
									Math.abs(Double.parseDouble(nativeApp.get(2))
											- Double.parseDouble(webApp.get(2))),
									DistanceAlgorithm.calculate(
											Double.parseDouble(nativeApp.get(0)),
											Double.parseDouble(nativeApp.get(1)),
											Double.parseDouble(webApp.get(0)),
											Double.parseDouble(webApp.get(1))));
						}
					}
				}
			}
		}	
	}
 //  Map<String, Map<String, ArrayList<String>>>
	public static void insertEntry (String type, String sDate, String sTime,  double lattitude, double longitude, double accuracy, ArrayList<Double> meter){
			// date exists
			if ((type == "native_webApp" && Constants.native_webApp.containsKey(sDate)) ||
				(type == "phonegap_native" && Constants.phonegap_native.containsKey(sDate)) ||
				(type == "phonegap_webApp" && Constants.phonegap_webApp.containsKey(sDate))) {
				
				Map<String, ArrayList<Double>> date = null; 
				
				if (type == "native_webApp"){
					date = Constants.native_webApp.get(sDate);
				} else if (type == "phonegap_native"){
					date = Constants.phonegap_native.get(sDate);
				}else if (type == "phonegap_webApp"){
					 date = Constants.phonegap_webApp.get(sDate);
				}	
					
				// time exists
				if (date.containsKey(sTime)){
					ArrayList<Double> time = date.get(sTime);
					time.add(lattitude);
					time.add(longitude);
					time.add(accuracy);
					if(Constants.distranceChoices != null){
						for (int i = 0; i < Constants.distranceChoices.size(); i++){
							time.add(meter.get(i));
						}
					}
					date.put(sTime, time);
					
					if (type == "native_webApp"){
						Constants.native_webApp.put(sDate, date);
					} else if (type == "phonegap_native"){
						Constants.phonegap_native.put(sDate, date);
					}else if (type == "phonegap_webApp"){
						Constants.phonegap_webApp.put(sDate, date);
					}	
					

				}
				// time does not exist
				else{
					ArrayList<Double> newTime = new ArrayList<Double>();
					newTime.add(lattitude);
					newTime.add(longitude);
					newTime.add(accuracy);
					if(Constants.distranceChoices != null){
						for (int i = 0; i < Constants.distranceChoices.size(); i++){
							newTime.add(meter.get(i));
						}
					}
					date.put(sTime, newTime);
					
					if (type == "native_webApp"){
						Constants.native_webApp.put(sDate, date);
					} else if (type == "phonegap_native"){
						Constants.phonegap_native.put(sDate, date);
					}else if (type == "phonegap_webApp"){
						Constants.phonegap_webApp.put(sDate, date);
					}	
				}
					
			}
			// Date does not exist
			else {
				Map<String, ArrayList<Double>> newDate = new TreeMap<String, ArrayList<Double>>();
				ArrayList<Double> newTime = new ArrayList<Double>();
				
				newTime.add(lattitude);
				newTime.add(longitude);
				newTime.add(accuracy);
				if(Constants.distranceChoices != null){
					for (int i = 0; i < Constants.distranceChoices.size(); i++){
						newTime.add(meter.get(i));
					}
				}
				newDate.put(sTime, newTime);
				
				if (type == "native_webApp"){
					Constants.native_webApp.put(sDate, newDate);
				} else if (type == "phonegap_native"){
					Constants.phonegap_native.put(sDate, newDate);
				}else if (type == "phonegap_webApp"){
					Constants.phonegap_webApp.put(sDate, newDate);
				}
			}
		}
}
