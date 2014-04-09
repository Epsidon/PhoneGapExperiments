import java.util.ArrayList;



public class DistanceAlgorithm {

	
	private static final double _eQuatorialEarthRadius = 6378.1370D;
    private static final double _d2r = (Math.PI / 180D);
	private static double PRECISION = 0.1;
	
	
	public static ArrayList<Double> calculate(double lat1, double long1, double lat2, double long2){
    	ArrayList<Double> result = new ArrayList<Double>();
    	Constants.distranceChoices = new ArrayList<String>();
		if (Constants.haversineInM){
			result.add(HaversineInM(lat1, long1, lat2, long2));
			Constants.distranceChoices.add("HarversineInM");
		}
		if (Constants.haversineInKM){
			result.add(HaversineInKM(lat1, long1, lat2, long2));
			Constants.distranceChoices.add("HarversineInKM");
		}
		return result;
    	
    }
    
    
    // Haversine Algorithm
    // source: http://stackoverflow.com/questions/365826/calculate-distance-between-2-gps-coordinates

    private static double HaversineInM(double lat1, double long1, double lat2, double long2) {
        return  (1000D * HaversineInKM(lat1, long1, lat2, long2));
    }

    private static double HaversineInKM(double lat1, double long1, double lat2, double long2) {
        double dlong = (long2 - long1) * _d2r;
        double dlat = (lat2 - lat1) * _d2r;
        double a = Math.pow(Math.sin(dlat / 2D), 2D) + Math.cos(lat1 * _d2r) * Math.cos(lat2 * _d2r)
                * Math.pow(Math.sin(dlong / 2D), 2D);
        double c = 2D * Math.atan2(Math.sqrt(a), Math.sqrt(1D - a));
        double d = _eQuatorialEarthRadius * c;
        return d;
    }
    
    // Distance between a point and a line

	public static double calculateFilter(ArrayList<String> start, ArrayList<String> end, ArrayList<String> node) {

		if (Constants.index.isEmpty()){
			System.err.println("index is empty");
		}
		
		double [] a = {Double.parseDouble(Constants.index.get(start.get(0)).get(start.get(1)).get(Constants.filterDevice).get(0)), Double.parseDouble(Constants.index.get(start.get(0)).get(start.get(1)).get(Constants.filterDevice).get(1))};
		double [] b = {Double.parseDouble(Constants.index.get(end.get(0)).get(end.get(1)).get(Constants.filterDevice).get(0)), Double.parseDouble(Constants.index.get(end.get(0)).get(end.get(1)).get(Constants.filterDevice).get(1))};
		double [] c = {Double.parseDouble(Constants.index.get(node.get(0)).get(node.get(1)).get(Constants.filterDevice).get(0)), Double.parseDouble(Constants.index.get(node.get(0)).get(node.get(1)).get(Constants.filterDevice).get(1))};

		double[] nearestNode = nearestPointGreatCircle(a, b, c);

		return HaversineInM(c[0], c[1], nearestNode[0], nearestNode[1]);
	}
	
	// source: http://stackoverflow.com/questions/1299567/how-to-calculate-distance-from-a-point-to-a-line-segment-on-a-sphere
	private static double[] nearestPointGreatCircle(double[] a, double[] b, double c[])
	{
	    double[] a_ = toCartsian(a);
	    double[] b_ = toCartsian(b);
	    double[] c_ = toCartsian(c);
	    
	    double[] G = vectorProduct(a_, b_);
	    double[] F = vectorProduct(c_, G);
	    double[] t = vectorProduct(G, F);
	    
	    return fromCartsian(multiplyByScalar(normalize(t), _eQuatorialEarthRadius));
	}

	@SuppressWarnings("unused")
	private static double[] nearestPointSegment (double[] a, double[] b, double[] c)
	{
	   double[] t= nearestPointGreatCircle(a,b,c);
	   if (onSegment(a,b,t))
	     return t;
	   return (HaversineInKM(a[0], a[1], c[0], c[1]) < HaversineInKM(b[0], b[1], c[0], c[1])) ? a : b;
	}
	
	 private static boolean onSegment (double[] a, double[] b, double[] t)
	   {
	     // should be   return distance(a,t)+distance(b,t)==distance(a,b), 
	     // but due to rounding errors, we use: 
	     return Math.abs(HaversineInKM(a[0], a[1], b[0], b[1])-HaversineInKM(a[0], a[1], t[0], t[1])-HaversineInKM(b[0], b[1], t[0], t[1])) < PRECISION;
	   }
	
	 
	// source: http://stackoverflow.com/questions/1185408/converting-from-longitude-latitude-to-cartesian-coordinates
	private static double[] toCartsian(double[] coord) {
		double[] result = new double[3];
		result[0] = _eQuatorialEarthRadius * Math.cos(Math.toRadians(coord[0])) * Math.cos(Math.toRadians(coord[1]));
		result[1] = _eQuatorialEarthRadius * Math.cos(Math.toRadians(coord[0])) * Math.sin(Math.toRadians(coord[1]));
		result[2] = _eQuatorialEarthRadius * Math.sin(Math.toRadians(coord[0]));
		return result;
	}
	
	private static double[] fromCartsian(double[] coord){
		double[] result = new double[2];
		result[0] = Math.toDegrees(Math.asin(coord[2] / _eQuatorialEarthRadius));
		result[1] = Math.toDegrees(Math.atan2(coord[1], coord[0]));
		
		return result;
	}

	
	// Basic functions
	private static double[] vectorProduct (double[] a, double[] b){
		double[] result = new double[3];
		result[0] = a[1] * b[2] - a[2] * b[1];
		result[1] = a[2] * b[0] - a[0] * b[2];
		result[2] = a[0] * b[1] - a[1] * b[0];
		
		return result;
	}
	
	private static double[] normalize(double[] t) {
		double length = Math.sqrt((t[0] * t[0]) + (t[1] * t[1]) + (t[2] * t[2]));
		double[] result = new double[3];
		result[0] = t[0]/length;
		result[1] = t[1]/length;
		result[2] = t[2]/length;
		return result;
	}

	private static double[] multiplyByScalar(double[] normalize, double k) {
		double[] result = new double[3];
		result[0] = normalize[0]*k;
		result[1] = normalize[1]*k;
		result[2] = normalize[2]*k;
		return result;
	}


	public static double calculate3PointFilter(ArrayList<String> start, ArrayList<String> end, ArrayList<String> mid) {

		double [] a = {Double.parseDouble(Constants.index.get(start.get(0)).get(start.get(1)).get(Constants.filterDevice).get(0)), Double.parseDouble(Constants.index.get(start.get(0)).get(start.get(1)).get(Constants.filterDevice).get(1))};
		double [] b = {Double.parseDouble(Constants.index.get(end.get(0)).get(end.get(1)).get(Constants.filterDevice).get(0)), Double.parseDouble(Constants.index.get(end.get(0)).get(end.get(1)).get(Constants.filterDevice).get(1))};
		double [] c = {Double.parseDouble(Constants.index.get(mid.get(0)).get(mid.get(1)).get(Constants.filterDevice).get(0)), Double.parseDouble(Constants.index.get(mid.get(0)).get(mid.get(1)).get(Constants.filterDevice).get(1))};

		/*        c
		 *    d /_ \ e
		 * 	   a  f  b
		 */
		
		double d = HaversineInM(a[0], a[1], c[0], c[1]);
		double e = HaversineInM(c[0], c[1], b[0], b[1]);
		double f = HaversineInM(a[0], a[1], b[0], b[1]);

		return d + e - f;
	}
    
}

