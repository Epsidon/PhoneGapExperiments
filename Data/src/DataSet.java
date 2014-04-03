import java.util.*;
import java.util.Map.Entry;


public class DataSet {
	
	public static void main(String[] args) {

		long a = 0, b = 0, c = 0, d = 0, e = 0, start, fStart, total;

		fStart = System.currentTimeMillis();
		// Preprocessing
		System.out.println("\nStart Preprocessing");
		start = System.currentTimeMillis();
		ArrayList<ArrayList<String>> preprocess = PreProcessing.getDocuments();
		a = System.currentTimeMillis() - start;
		System.out.println("End Preprocessing");
		//printPreprocess(preprocess);

		
		// Indexing
		System.out.println("\nStart Indexing");
		start = System.currentTimeMillis();
		Indexing.start(preprocess);
		b = System.currentTimeMillis() - start;
		System.out.println("End Indexing");
		//printIndex();
		
		// Comparing coordinates
		if (Constants.coordDiff){
			System.out.println("\nStart Comparing");
			start = System.currentTimeMillis();
			Comparison.start();
			d = System.currentTimeMillis() - start;
			System.out.println("End Comparing");
			//printComparison();
		}
		// Filtering
		if (Constants.filters){
			System.out.println("\nStart Filtering");
			start = System.currentTimeMillis();
			
			if (Constants.filterMehod3p){
				ThreePointFilter.start();
			}
			else{
				Filter.start();
			}
			e = System.currentTimeMillis() - start;
			System.out.println("End Filtering");
			//printFilter();
		}
		
		// Outputting the results
		System.out.println("\nStart Output Results");
		start = System.currentTimeMillis();
		Output.start();
		c = System.currentTimeMillis() - start;
		System.out.println("End Output Results");
		
		total = System.currentTimeMillis() - fStart;
		
		// Summary
		System.out.println("\nSummary: "
				+ "\n\tPreprocessing: " + (double) a/1000 + " s"
				+ "\n\tIndexing: " + (double) b/1000 + " s"
				+ "\n\tCompairing: " + (double) d/1000 + " s"
				+ "\n\tFiltering: " + (double) e/1000 + " s"
				+ "\n\tOutput: " + (double) c/1000 + " s"
				+ "\n\tMain: " + (double) (total-a-b-c-d-e)/1000 + " s"
				+ "\n\tTotal: " + (double) total/1000 + " s");

	}
	
	
	//Printing results (used for testing)
	
	@SuppressWarnings("unused")
	private static void printFilter() {
		System.out.println("Filter: ");
		for (Entry<String, Map<String, Map<String, ArrayList<String>>>> date : Constants.filteredIndex.entrySet()){
			for (Entry<String, Map<String, ArrayList<String>>> time : date.getValue().entrySet()){
				for (Entry<String, ArrayList<String>> device : time.getValue().entrySet()){
					System.out.print(date.getKey() + "\t" + time.getKey() + "\t" + device.getKey() + "\t");
					if (device.getKey() == "phonegap")
						System.out.print("\t");
					System.out.print(device.getValue().toString() + "\n");
				}
			}
		}
	}


	@SuppressWarnings("unused")
	private static void printComparison() {
		Map<String, Map<String, ArrayList<Double>>> type = new TreeMap<String, Map<String, ArrayList<Double>>>();
		
		for (int i = 0; i < 3; i++){
			switch(i){
			case 0: System.out.println("\n\nphonegap_webApp: ");
					type = Constants.phonegap_webApp;
					break;
			case 1:System.out.println("\n\nphonegap_native: ");
					type = Constants.phonegap_native;
					break;
			case 2: System.out.println("\n\nnative_webAp: ");
					type = Constants.native_webApp;
					break;
			}
			for (Entry<String, Map<String, ArrayList<Double>>> date : type.entrySet()) {
				for (Entry<String, ArrayList<Double>> time : date.getValue().entrySet()) {
				System.out.println(date.getKey() + "\t" + date.getKey()
						+ "\t" + time.getValue().get(0).toString() + "\t"
						+ time.getValue().get(1).toString() + "\t"
						+ time.getValue().get(2).toString()
						+ time.getValue().get(3).toString());
				}
			}
		}
	}

	@SuppressWarnings("unused")
	private static void printPreprocess(ArrayList<ArrayList<String>> preprocess) {
		System.out.println("Preprocess: ");
		for(int i = 0; i < preprocess.size(); i++){
			System.out.println(preprocess.get(i).toString());
		}
	}
	
	public static void printIndex(){
		System.out.println("Indexing: ");
		for (Entry<String, Map<String, Map<String, ArrayList<String>>>> date : Constants.index.entrySet()){
			for (Entry<String, Map<String, ArrayList<String>>> time : date.getValue().entrySet()){
				for (Entry<String, ArrayList<String>> device : time.getValue().entrySet()){
					System.out.print(date.getKey() + "\t" + time.getKey() + "\t" + device.getKey() + "\t");
					if (device.getKey() != "phonegap")
						System.out.print("\t");
					System.out.print(device.getValue().toString() + "\n");
				}
			}
		}
	}
}
