import java.util.ArrayList;
import java.util.Map;


public class Constants {
	/******************************************
	 * Settings
	 ******************************************/
	/*
	 * Input/Output
	 */
	// Input File location (default: Resources/)
	public static String resources = "Resources/";
	// Output File Location (default: Results/)
	public static String results = "Results/";
	// Output File Name
	public static String resultsfileName = "android";

	/*
	 * Output Ways
	 */
	// Only use times where data from native, console and web app is available
	public static boolean validCheck = true;	
		
	// Overall mix between all the files
	public static boolean summary = true;
	
	// print each device coord separately
	public static boolean deviceDetail = false;

	// Filtered data
	public static boolean filters = true;
		// false is use regular filter
		public static boolean filterMehod3p = true;
		// Determine which device to check when filtering; choices: (phonegap, webapp, native)
		public static String filterDevice = "phonegap";
		// Remove Outliers
		public static boolean outlierDetection = true;

		// === regular filter settings ===
		// Sharpen corners
		public static boolean cornerDetection = false;
		// Max difference between point and line in filter (meters)
		public static double diff = 5.0; 
		// how many coords to check to determine if it's a corner or outlier
		public static int coordCheck = 5;

		// === 3 point filter Setings ===
		public static double diff3 = 1.6;
		public static double PRECISION = 0.001;
		public static int outliercheck = 2;
			
	// Calculate the coordinate differences between the 3 data sets
	public static boolean coordDiff = false;
		// Output difference choices
		public static boolean haversineInM = true;
		public static boolean haversineInKM = false ;
	
	/*
	 * Splitting: Will be done for each output ways + regular output
	 */
	// Split packages into 100 by data
	public static boolean gMapLayers = false;
	
	// Split packages into 100 by Devices
	public static boolean gMapDevices = true;
    
	/******************************************
	 * Constants
	 ******************************************/
	
	/*
	 * Index TreeMap:
	 * 	- string (date)
	 * 	- TreeMap:
	 * 		- String (Time)
	 * 		- TreeMap (devices)
	 * 			- String (device Name)
	 * 			- ArrayList:
	 * 				- Latitude
	 * 				- longitude
	 * 				- Accuracy
	 */
	public static Map<String, Map<String, Map<String, ArrayList<String>>>> index;
	public static Map<String, Map<String, Map<String, ArrayList<String>>>> filteredIndex;

	
	/*
	 * Comparison TreeMap:
	 * 	- string (date)
	 * 	- TreeMap:
	 * 		- String (Time)
	 * 		- ArrayList:
	 * 			- Latitude
	 * 			- longitude
	 * 			- Accuracy
	 * 			- Meter(s)
	 */
	public static Map<String, Map<String, ArrayList<Double>>> phonegap_webApp;
	public static Map<String, Map<String, ArrayList<Double>>> phonegap_native;
	public static Map<String, Map<String, ArrayList<Double>>> native_webApp;
	
	// Choices for Distance Algorithm
	public static ArrayList<String> distranceChoices;
}
