import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

public class Filter {

	
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
						// regular node
						else if (distranceCheck(last, current)) {
							last.add(current);
						}
						// outside of distance
						else {
							boolean fixed = false;
							
							if (Constants.outlierDetection){
								// checking outlier
								if(outlierCheck(last, current)){
									// ignores current coord
									fixed = true;
								}
							}
							
							if (Constants.cornerDetection  && !fixed){
								if(cornerCheck(last, current)){
									ArrayList<ArrayList<String>> temp = new ArrayList<ArrayList<String>>();
	
									for (int i = last.indexOf(fixCorner(last, current)); i < last.size(); i++){
										temp.add(last.get(i));
									}
									
									last.clear();
									last = temp;
									fixed = true;
								}
							}
							
							// Update Coords
							if(!fixed){
								last.clear();
								last.add(current);
								save(date.getKey(), time.getKey(), time.getValue());
							}
	
						}
					}
				}
			}
		}
	}


	private static boolean exists(String key, String key2, Map<String, ArrayList<String>> value) {
		if(Constants.index.containsKey(key))
			if (Constants.index.get(key).containsKey(key2))
				if (Constants.index.get(key).get(key2).containsKey(Constants.filterDevice))
						return true;
				
		return false;
	}
	private static boolean distranceCheck(ArrayList<ArrayList<String>> last, ArrayList<String> current) {	

		for(int i = 1; i < last.size(); i++){
			if(DistanceAlgorithm.calculateFilter(last.get(0), current, last.get(i)) > Constants.diff){
				return false;
			}
		}
		return true;
	}
	
	private static boolean cornerCheck(ArrayList<ArrayList<String>> last, ArrayList<String> current) {
		
		if (last.size() > Constants.coordCheck) {
			ArrayList<Double> memory = new ArrayList<Double>();
			for (int i = last.size() - 1; i != 0; i--) {
				memory.add(DistanceAlgorithm.calculateFilter(last.get(0), current, last.get(i)));
			}

			for (int i = 0; i < Constants.coordCheck; i++) {
				if (memory.get(i) > memory.get(i+1)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	private static ArrayList<String> fixCorner(ArrayList<ArrayList<String>> last, ArrayList<String> current) {
		ArrayList<String> corner = new ArrayList<String>();
		
		for (int i = 0; i < last.size(); i++) {
			// find the corner
			if (corner.isEmpty()){
				corner = last.get(i);
			}
			else if(DistanceAlgorithm.calculateFilter(last.get(0), current, corner) < DistanceAlgorithm.calculateFilter(last.get(0), current, last.get(i))){
				corner = last.get(i);
			}
		}
		
		Map<String, ArrayList<String>> device = Constants.index.get(corner.get(0)).get(corner.get(1));
		save(corner.get(0), corner.get(1), device);
		
		return corner;
	}
	
	private static boolean outlierCheck(ArrayList<ArrayList<String>> last, ArrayList<String> current) {

		if (DistanceAlgorithm.calculateFilter(last.get(0), current, last.get(last.size()-1)) > Constants.diff){
			ArrayList<ArrayList<String>> nextCoord = new ArrayList<ArrayList<String>>();
			nextCoord = findNext(current);
			
			if (nextCoord == null){
				// last coordinate on the map
				Map<String, ArrayList<String>> device = Constants.index.get(current.get(0)).get(current.get(1));
				save(current.get(0), current.get(1), device);
				return true;
			}
			else{
				for (int i = 0; i < Constants.coordCheck-1; i++){
					if (DistanceAlgorithm.calculateFilter(last.get(0), nextCoord.get(i), nextCoord.get(nextCoord.size()-1)) > Constants.diff){
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}

	private static ArrayList<ArrayList<String>> findNext(ArrayList<String> current) {
		boolean next = false;
		ArrayList<ArrayList<String>> nextCoord = new ArrayList<ArrayList<String>>();
		
		int counter = 0;
		for (Entry<String, Map<String, Map<String, ArrayList<String>>>> date : Constants.index.entrySet()) {
			for (Entry<String, Map<String, ArrayList<String>>> time : date.getValue().entrySet()) {
				
				if(counter == Constants.coordCheck){
					return nextCoord;
				} else if (next && time.getValue().containsKey(Constants.filterDevice)){
					ArrayList<String> coord = new ArrayList<String>();
					coord.add(date.getKey());
					coord.add(time.getKey());
					nextCoord.add(coord);
					counter++;
				} else if(time.getKey() == current.get(1) && date.getKey() == current.get(0)){
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
