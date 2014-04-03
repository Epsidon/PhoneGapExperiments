import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

public class ThreePointFilter {
	private static double diff = 8.0;
	private static double PRECISION = 0.001;
	private static int outliercheck = 2;
	
	public static void start() {

		Constants.filteredIndex = new TreeMap<String, Map<String, Map<String, ArrayList<String>>>>();
		
		ArrayList<ArrayList<String>> last = new ArrayList<ArrayList<String>>();

		for (Entry<String, Map<String, Map<String, ArrayList<String>>>> date : Constants.index.entrySet()) {
			for (Entry<String, Map<String, ArrayList<String>>> time : date.getValue().entrySet()) {
				
				if ((time.getValue().size() == 3 && Constants.validCheck) || !Constants.validCheck) {
					ArrayList<String> current = new ArrayList<String>();
					
					if(exists(date.getKey(), time.getKey(), time.getValue())) {
						current.add(date.getKey());
						current.add(time.getKey());
						// start
						if (last.isEmpty()) {
							last.add(current);
							save(date.getKey(), time.getKey(), time.getValue());
						}
						else if (straightline(last, current)){
							last.clear();
							last.add(current);
						}
						// regular node
						else if (distranceCheck(last, current)) {
							last.add(current);
						}
						// outside of distance
						else {

							if (Constants.outlierDetection && outlierCheck(last, current)){
							}
							else {
								ArrayList<ArrayList<String>> temp = new ArrayList<ArrayList<String>>();
								
								Map<String, ArrayList<String>> device = Constants.index.get(last.get(last.size()/2).get(0)).get(last.get(last.size()/2).get(1));
								save(last.get(last.size()/2).get(0), last.get(last.size()/2).get(1), device);
								
								for (int i = last.size()/2; i < last.size(); i++){
									temp.add(last.get(i));
								}
									
								last.clear();
								last = temp;
							}
						}
					}
				}
			}
		}
	}


	private static boolean straightline(ArrayList<ArrayList<String>> last, ArrayList<String> current) {
		if (last.size() < 2)
			return false;
		else
			return(DistanceAlgorithm.calculate3PointFilter(last.get(0), current, last.get(last.size()/2)) < PRECISION);
	}

	private static boolean exists(String key, String key2, Map<String, ArrayList<String>> value) {
		if(Constants.index.containsKey(key))
			if (Constants.index.get(key).containsKey(key2))
				if (Constants.index.get(key).get(key2).containsKey(Constants.filterDevice))
						return true;
				
		return false;
	}
	private static boolean distranceCheck(ArrayList<ArrayList<String>> last, ArrayList<String> current) {	
		return(DistanceAlgorithm.calculate3PointFilter(last.get(0), current, last.get(last.size()/2)) < diff);
	}
	
	private static boolean outlierCheck(ArrayList<ArrayList<String>> last, ArrayList<String> current) {
		ArrayList<ArrayList<String>> nextCoord = new ArrayList<ArrayList<String>>();
		nextCoord = findNext(current);
		if (nextCoord == null) {
			// last coordinate on the map
			Map<String, ArrayList<String>> device = Constants.index.get(current.get(0)).get(current.get(1));
			save(current.get(0), current.get(1), device);
			return true;
		} else {
			for (int i = 0; i < nextCoord.size(); i++) {
				int j;
				if (last.size()-1 < last.size()/2 + i + 1){
					j = last.size()-1;
				}
				else{
					j = last.size()/2 + i + 1;
				}
				if (DistanceAlgorithm.calculate3PointFilter(last.get(0), nextCoord.get(i), last.get(j)) < diff) {
					return true;
				}
			}
			return false;
		}
	}

	private static ArrayList<ArrayList<String>> findNext(ArrayList<String> current) {
		boolean next = false;
		ArrayList<ArrayList<String>> nextCoord = new ArrayList<ArrayList<String>>();
		
		int counter = 0;
		for (Entry<String, Map<String, Map<String, ArrayList<String>>>> date : Constants.index.entrySet()) {
			for (Entry<String, Map<String, ArrayList<String>>> time : date.getValue().entrySet()) {
				if(counter == outliercheck){
					return nextCoord;
				}
				else if (next && time.getValue().containsKey(Constants.filterDevice)){
					ArrayList<String> coord = new ArrayList<String>();
					coord.add(date.getKey());
					coord.add(time.getKey());
					nextCoord.add(coord);
					counter++;
				}
				else if(time.getKey() == current.get(1) && date.getKey() == current.get(0)){
					next = true;
				}
			}
		}
		return null;
	}

	private static void save(String cdate, String ctime, Map<String, ArrayList<String>> cdevice) {
		
		if (Constants.validCheck){
			// Date exists
			if (Constants.filteredIndex.containsKey(cdate)) {
				Map<String, Map<String, ArrayList<String>>> date = Constants.filteredIndex.get(cdate);
				date.put(ctime, cdevice);
				Constants.filteredIndex.put(cdate, date);
	
			}
			// Date does not exist
			else {
				Map<String, Map<String, ArrayList<String>>> newDate = new TreeMap<String, Map<String, ArrayList<String>>>();
				newDate.put(ctime, cdevice);
				Constants.filteredIndex.put(cdate, newDate);
			}
		}
		else {
			// Date exists
			if (Constants.filteredIndex.containsKey(cdate)) {
				Map<String, Map<String, ArrayList<String>>> date = Constants.filteredIndex.get(cdate);
				Map<String, ArrayList<String>> device = new TreeMap<String, ArrayList<String>>();
				device.put(Constants.filterDevice, cdevice.get(Constants.filterDevice));
				date.put(ctime, device);
				Constants.filteredIndex.put(cdate, date);
	
			}
			// Date does not exist
			else {
				Map<String, Map<String, ArrayList<String>>> newDate = new TreeMap<String, Map<String, ArrayList<String>>>();
				Map<String, ArrayList<String>> device = new TreeMap<String, ArrayList<String>>();
				device.put(Constants.filterDevice, cdevice.get(Constants.filterDevice));	
				newDate.put(ctime, device);
				Constants.filteredIndex.put(cdate, newDate);
			}
		}
	}
}
