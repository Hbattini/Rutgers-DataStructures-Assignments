import java.util.ArrayList;


/**
 * This class contains methods which perform various operations on a layered linked
 * list to simulate transit
 * 
 * @author Ishaan Ivaturi
 * @author Prince Rawal
 * 
 */
public class Transit {
	/**
	 * Makes a layered linked list representing the given arrays of train stations, bus
	 * stops, and walking locations. Each layer begins with a location of 0, even though
	 * the arrays don't contain the value 0.
	 * 
	 * @param trainStations Int array listing all the train stations
	 * @param busStops Int array listing all the bus stops
	 * @param locations Int array listing all the walking locations (always increments by 1)
	 * @return The zero node in the train layer of the final layered linked list
	 */
	private static TNode h1 = null;
	private static TNode h2 = null;
	private static TNode h3 = null;
	
	private static TNode hold1 = null;
	
	private static TNode hold2 = null;
	
	private static TNode hold3 = null;
	
	//It WORKS!
	public static TNode makeList(int[] trainStations, int[] busStops, int[] locations) {
		TNode ts = new TNode(0);
		TNode bs = new TNode(0);
		TNode ls = new TNode(0);
		h1 = ts; 
		h2 = bs;
		h3 = ls;
		h1.down = h2;
		h1.down.down = h3;
		for(int t = 0; t<trainStations.length; t++)
		{
			ts.next = new TNode(trainStations[t]);
			ts = ts.next; 
		}
		
		for(int t = 0; t<busStops.length; t++)
		{
			bs.next = new TNode(busStops[t]);
			bs = bs.next; 
		}
		for(int t = 0; t<locations.length; t++)
		{
			ls.next = new TNode(locations[t]);
			ls = ls.next; 
		}
		int indt = 0;
		int indb = 0;
		int iter = 0;
		hold1 = h1;
		hold2 = h2;
		hold3 = h3;
		for(int x = 0; x<locations.length; x++)
		{
			
			if(indt<trainStations.length && trainStations[indt] == busStops[indb]) //Implicit T=B=L
			{
				iter = 0;
				hold2 = h2.next;
				while(trainStations[indt]> busStops[iter])
				{
					hold2 = hold2.next;
					iter++;
				}
					
				hold1.next.down = hold2;
				hold1 = hold1.next;
				indt++;
			}
			
			
			
			if((indb<busStops.length && indt !=0 && busStops[indb] == locations[x]) && (locations[x] == trainStations[indt-1]) )
			{
				iter = 0;
				hold3 = h3.next;
				while(busStops[indb] > locations[iter])
				{
					hold3 = hold3.next;
					iter++;
				}
				hold2.down = hold3;
				indb++;
			}
			else if(indb<busStops.length && busStops[indb] == locations[x])
			{
				iter = 0;
				hold3 = h3.next;
				while(busStops[indb] > locations[iter])
				{
					hold3 = hold3.next;
					iter++;
				}
				hold2.next.down = hold3;
				hold2 = hold2.next;
				indb++;
			}
			
			

		}
		
		return h1;
	}
	
	/**
	 * Modifies the given layered list to remove the given train station but NOT its associated
	 * bus stop or walking location. Do nothing if the train station doesn't exist
	 * 
	 * @param trainZero The zero node in the train layer of the given layered list
	 * @param station The location of the train station to remove
	 */
	public static void removeTrainStation(TNode trainZero, int station) {
		while(trainZero.next != null)
		{
			if(trainZero.next.location == station)
			{
				trainZero.next = trainZero.next.next;
			}
			else
			{
				trainZero = trainZero.next;
			}
		}
	}

	/**
	 * Modifies the given layered list to add a new bus stop at the specified location. Do nothing
	 * if there is no corresponding walking location.
	 * 
	 * @param trainZero The zero node in the train layer of the given layered list
	 * @param busStop The location of the bus stop to add
	 */
	private static TNode blayer = null;
	private static TNode wlayer = null;
	
	public static void addBusStop(TNode trainZero, int busStop) {
		blayer = trainZero.down; 
		wlayer = trainZero.down.down;
		trainZero.down = blayer; 
		// This doesnt connect to the trainZero node?
		while(blayer.next != null)
		{
			while(wlayer != null && wlayer.location<busStop )
			{
				wlayer = wlayer.next;
			}
			if(wlayer == null)
			{
				break;
			}

			
			if(busStop > blayer.location && busStop <blayer.next.location)
			{
				blayer.next = new TNode(busStop, blayer.next, wlayer);
				break;
			}
			else if(busStop == blayer.location)
			{
				break;
			}
			else
			{
				blayer = blayer.next;
			}
		}
		if(blayer.next == null && wlayer != null)
			{
				blayer.next = new TNode(busStop, null, wlayer);
			}
		
		
	}
	
	/**
	 * Determines the optimal path to get to a given destination in the walking layer, and 
	 * collects all the nodes which are visited in this path into an arraylist. 
	 * 
	 * @param trainZero The zero node in the train layer of the given layered list
	 * @param destination An int representing the destination
	 * @return
	 */
	private static TNode w = null;
	private static TNode b = null;
	private static TNode t = null;
	
	public static ArrayList<TNode> bestPath(TNode trainZero, int destination) {
		ArrayList<TNode> Path = new ArrayList<>();
		t = trainZero;
		w = t.down.down;
		b = t.down;
		int g =0;
		int bloc = 0;
		while(w.location != destination)
		{
			if(t.next != null && destination>t.next.location)
			{
				Path.add(t);
				b = t.down;
				t = t.next;
				
			}
			else if(t.next == null && destination>t.location && g != 1)
			{
				Path.add(t);
				
				b = t.down;
				g = 1;
			}
			else if(t.next != null && destination<t.next.location && g != 1)
			{
				Path.add(t);
				b = t.down;
				g = 1;
			}
			else if(( t.next != null && destination == t.next.location ) || (t.next == null && destination==t.location))
			{
				Path.add(t);
				t= t.next;
				Path.add(t);
				b = t.down;
				Path.add(b);
				w = b.down;
				Path.add(w);
			}
			else if(b.next != null && destination > b.next.location )
			{
				
				Path.add(b);
				w = b.down;
				b = b.next;
			}
			else if(b.next == null && destination> b.location && bloc != 1)
			{
				Path.add(b);
				w = b.down;
				bloc = 1; 
			}
			else if((b.next != null && destination == b.next.location) || (b.next == null && destination==t.location))
			{
				Path.add(b);
				b = b.next;
				Path.add(b);
				w = b.down;
				Path.add(w);
			}
			else if(b.next != null && destination < b.next.location && bloc != 1)
			{
				Path.add(b);
				w = b.down;
				bloc = 1;
			}
			else if(destination > w.location)
			{
				Path.add(w);
				w = w.next; 
			}
		}
		if((destination == w.location) && (destination > t.location) && (destination> b.location))
		{
			Path.add(w);
		}
		return Path;
	}

	/**
	 * Returns a deep copy of the given layered list, which contains exactly the same
	 * locations and connections, but every node is a NEW node.
	 * 
	 * @param trainZero The zero node in the train layer of the given layered list
	 * @return
	 */
	private static TNode t1 = null;
	private static TNode t2 = null;
	
	private static TNode t3 = null;
	
	
	//Will most likely have to replicate item 1 and do it in list form this time as each value has to be a new node. 
	public static TNode duplicate(TNode trainZero) {
		
		t1 = trainZero;
		t2 = trainZero.down;
		t3 = trainZero.down.down;		
		int g = 0;
		int p = 0;
		while(t1.next != null)
		{
			
			g++;
			t1 = t1.next;
		}
		int trainStations[] = new int[g];
		t1 = trainZero;
		while(t1.next != null)//2nd run
		{
			trainStations[p] = t1.next.location;
			p++;
			t1 = t1.next;
		}

		g = 0;
		p = 0;
		while(t2.next != null)
		{
			
			g++;
			t2 = t2.next;
		}
		int busStops[] = new int[g];
		t2 = trainZero.down;
		
		while(t2.next != null)//2nd run
		{
			busStops[p] = t2.next.location;
			p++;
			t2 = t2.next;
		}
		g=0;
		p=0;
		while(t3.next != null)
		{
			
			g++;
			t3 = t3.next;
		}
		int locations[] = new int[g];
		t3 = trainZero.down.down;
		while(t3.next != null)
		{
			locations[p] = t3.next.location;
			p++;
			t3 = t3.next;
		}

			return makeList(trainStations,busStops, locations);
	
	}

	/**
	 * Modifies the given layered list to add a scooter layer in between the bus and
	 * walking layer.
	 * 
	 * @param trainZero The zero node in the train layer of the given layered list
	 * @param scooterStops An int array representing where the scooter stops are located
	 */
	private static TNode l1 = null;
	
	private static TNode l2 = null;
	
	private static TNode l3 = null;
	
	private static TNode hold = null;
	private static TNode bhold = null;
	private static TNode scot = null;
	
	public static void addScooter(TNode trainZero, int[] scooterStops) {
		l1 = trainZero;
		l2 = trainZero.down;
		l3 = trainZero.down.down; 
		hold = new TNode(0); 
		l2.down = hold; 
		hold.down = l3; 
		scot = hold;
		bhold = l2;
		trainZero.down = l2;
		trainZero.down.down = scot;
		trainZero.down.down.down = l3;
		l3 = l3.next;
		l2 = l2.next; 
		for(int x = 0; x<scooterStops.length; x++)
		{
			hold.next = new TNode(scooterStops[x]);
			hold = hold.next;
			if(l2 != null && l2.location == hold.location)
			{
				l2.down = hold;
				l2 = l2.next;
			}
			
			while(l3.location != hold.location)
			{
				l3 = l3.next;
			}
			if(l3.location == hold.location)
			{
				hold.down = l3;
				
			}
			
			
		}

	}
}