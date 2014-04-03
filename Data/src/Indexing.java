import java.util.*;


public class Indexing {

	public static void start(ArrayList<ArrayList<String>> preprocess) {
		Constants.index = new TreeMap<String, Map<String, Map<String, ArrayList<String>>>>();
		for(int i = 0; i < preprocess.size(); i++){
			indexDate(preprocess.get(i));
		}
	}
	
	// data = [date, time, filetype, latitude, longitude, accuracy]
	public static void indexDate(ArrayList<String> data){
		
		// date exists
		if (Constants.index.containsKey(data.get(0))) {
			
			Map<String, Map<String, ArrayList<String>>> date = Constants.index.get(data.get(0));
			
			// time exists
			if (date.containsKey(data.get(1))){
				
				Map<String, ArrayList<String>> time = date.get(data.get(1));
				
				ArrayList<String> coords = new ArrayList<String>();
				coords.add(data.get(3));
				coords.add(data.get(4));
				coords.add(data.get(5));
				time.put(data.get(2), coords);
				date.put(data.get(1), time);
				Constants.index.put(data.get(0), date);
			}
			// time does not exist
			else{
				Map<String, ArrayList<String>> newTime = new TreeMap<String, ArrayList<String>>();
				ArrayList<String> coords = new ArrayList<String>();
				coords.add(data.get(3));
				coords.add(data.get(4));
				coords.add(data.get(5));
				newTime.put(data.get(2), coords);
				date.put(data.get(1), newTime);
				Constants.index.put(data.get(0), date);
			}
				
		}
		// Date does not exist
		else {
			Map<String, Map<String, ArrayList<String>>> newDate = new TreeMap<String, Map<String, ArrayList<String>>>();
			Map<String, ArrayList<String>> newTime = new TreeMap<String, ArrayList<String>>();
			ArrayList<String> coords = new ArrayList<String>();
			coords.add(data.get(3));
			coords.add(data.get(4));
			coords.add(data.get(5));
			newTime.put(data.get(2), coords);
			newDate.put(data.get(1), newTime);
			Constants.index.put(data.get(0), newDate);
		}
	}
}