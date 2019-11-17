package edu.caltech.cs2.datastructures;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.IDictionary;
import edu.caltech.cs2.interfaces.IPriorityQueue;
import edu.caltech.cs2.interfaces.ISet;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class BeaverMapsGraph extends Graph<Long, Double> {
    private static JsonParser JSON_PARSER = new JsonParser();

    private IDictionary<Long, Location> ids;
    private ISet<Location> buildings;

    public BeaverMapsGraph() {
        super();
        this.buildings = new ChainingHashSet<>();
        this.ids = new ChainingHashDictionary<>(MoveToFrontDictionary::new);
    }

    /**
     * Reads in buildings, waypoinnts, and roads file into this graph.
     * Populates the ids, buildings, vertices, and edges of the graph
     * @param buildingsFileName the buildings filename
     * @param waypointsFileName the waypoints filename
     * @param roadsFileName the roads filename
     */
    public BeaverMapsGraph(String buildingsFileName, String waypointsFileName, String roadsFileName) {
        this();
        JsonElement bs = fromFile(buildingsFileName);
        for (JsonElement b : bs.getAsJsonArray()) {
            Location loc = new Location(b.getAsJsonObject());
            buildings.add(loc);
            ids.put(loc.id, loc);
            addVertex(loc);
        }
        JsonElement cs = fromFile(waypointsFileName);
        for(JsonElement b: cs.getAsJsonArray()){
            Location loc = new Location(b.getAsJsonObject());
            ids.put(loc.id, loc);
            addVertex(loc);
        }
        JsonElement ds = fromFile(roadsFileName);
        for(JsonElement b: ds.getAsJsonArray()){
            LinkedDeque<Long> nodes = new LinkedDeque<>();
            for(JsonElement c: b.getAsJsonArray()){
                nodes.add(c.getAsLong());
            }
            Location loc1 = ids.get(nodes.pop());
            while(!nodes.isEmpty()){
                Location loc2 = ids.get(nodes.pop());
                addUndirectedEdge(loc1.id, loc2.id, loc1.getDistance(loc2));
                loc1 = loc2;
            }
        }
    }

    /**
     * Returns a deque of all the locations with the name locName.
     * @param locName the name of the locations to return
     * @return a deque of all location with the name locName
     */
    public IDeque<Location> getLocationByName(String locName) {
        IDeque<Location> result = new LinkedDeque<>();
        for(Location loc : ids.values()){
            if(loc.name == locName){
                result.add(loc);
            }
        }
        return result;
    }

    /**
     * Returns the Location object corresponding to the provided id
     * @param id the id of the object to return
     * @return the location identified by id
     */
    public Location getLocationByID(long id) {
        return ids.get(id);
    }

    /**
     * Adds the provided location to this map.
     * @param n the location to add
     * @return true if n is a new location and false otherwise
     */
    public boolean addVertex(Location n) {
        ids.put(n.id, n);
        return addVertex(n.id);
    }

    /**
     * Returns the closest building to the location (lat, lon)
     * @param lat the latitude of the location to search near
     * @param lon the longitute of the location to search near
     * @return the building closest to (lat, lon)
     */
    public Location getClosestBuilding(double lat, double lon) {
        double minDistance = -1.0;
        Location min = null;
        for(Location loc : buildings){
            if(minDistance < 0 || loc.getDistance(lat, lon) < minDistance){
                minDistance = loc.getDistance(lat, lon);
                min = loc;
            }
        }
        return min;
    }

    /**
     * Returns a set of locations which are no more than threshold feet
     * away from start.
     * @param start the location to search around
     * @param threshold the number of feet in the search radius
     * @return
     */
    public ISet<Location> dfs(Location start, double threshold) {
        return dfs(start, start, threshold, new ChainingHashSet<>());
    }

    private ISet<Location> dfs(Location start, Location exploreFrom, double threshold, ISet<Location> explored){
        if(exploreFrom.getDistance(start) < threshold) {
            explored.add(exploreFrom);
            for (Long id : neighbors(exploreFrom.id)) {
                Location loc = getLocationByID(id);
                if (!explored.contains(loc)) {
                    dfs(start, loc, threshold, explored);
                }
            }
        }
        return explored;
    }

    /**
     * Returns a list of Locations corresponding to
     * buildings in the current map.
     * @return a list of all building locations
     */
    public ISet<Location> getBuildings() {
        return this.buildings;
    }

    /**
     * Returns a shortest path (i.e., a deque of vertices) between the start
     * and target locations (including the start and target locations).
     * @param start the location to start the path from
     * @param target the location to end the path at
     * @return a shortest path between start and target
     */
    public IDeque<Location> dijkstra(Location start, Location target) {
        IDictionary<Location, Double> q = new ChainingHashDictionary<>(MoveToFrontDictionary::new);
        IDictionary<Location, Location> parents = new ChainingHashDictionary<>(MoveToFrontDictionary::new);
        IDictionary<Location, Double> distances = new ChainingHashDictionary<>(MoveToFrontDictionary::new);
        q.put(start, 0.0);
        parents.put(start, null);
        distances.put(start, 0.0);
        while(!q.isEmpty() && !distances.containsKey(target)){
            Location next = minLocation(q);
            double dist = q.get(next);
            q.remove(next);
            distances.put(next, q.get(next));
            for(Long id: neighbors(next.id)){
                Location loc = getLocationByID(id);
                if(loc != null && !distances.containsKey(loc) && (!buildings.contains(loc) || loc.equals(target))){
                    if(!q.containsKey(loc) || q.get(loc) > dist + adjacent(next.id, id)){
                        q.put(loc, dist + adjacent(next.id, id));
                        parents.put(loc, next);
                    }
                }
            }
        }
        if(parents.containsKey(target)) {
            LinkedDeque<Location> path = new LinkedDeque<>();
            path.addFront(target);
            while (!path.peekFront().equals(start)) {
                path.addFront(parents.get(path.peekFront()));
            }
            //Location current = target;
            //path.addFront(current);
            /*while (!path.peekFront().equals(start)){
                current = parents.get(path.peekFront());
                if(!current.type.equals("building")){
                    path.addFront(current);
                }
            }*/
            /*for(Location loc : path){
                System.out.println(loc);
            }*/
            return path;
        } else {
            return null;
        }
    }

    private Location minLocation(IDictionary<Location, Double> dic){
        Double min = -1.0;
        Location minLoc = null;
        for(Location loc: dic.keys()){
            if(min < 0 || dic.get(loc) < min){
                min = dic.get(loc);
                minLoc = loc;
            }
        }
        return minLoc;
    }

    /**
     * Returns a JsonElement corresponding to the data in the file
     * with the filename filename
     * @param filename the name of the file to return the data from
     * @return the JSON data from filename
     */
    private static JsonElement fromFile(String filename) {
        try {
            return JSON_PARSER.parse(
                    new FileReader(
                            new File(filename)
                    )
            );
        } catch (IOException e) {
            return null;
        }
    }
}