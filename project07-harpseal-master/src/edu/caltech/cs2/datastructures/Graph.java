package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.IGraph;
import edu.caltech.cs2.interfaces.ISet;

public class Graph<V, E> extends IGraph<V, E> {

    ChainingHashDictionary<V, ChainingHashDictionary<V, E>> vertices;

    public Graph(){
        vertices = new ChainingHashDictionary<>(MoveToFrontDictionary::new);
    }

    @Override
    public boolean addVertex(V vertex) {
        ChainingHashDictionary<V, E> edges = new ChainingHashDictionary<>(MoveToFrontDictionary::new);
        if (!vertices.containsKey(vertex) && vertices.put(vertex, edges) != null){
            return true;
        } else{
            return false;
        }
    }

    private boolean containsVertex(V vertex){
        return vertices.containsKey(vertex);
    }

    @Override
    public boolean addUndirectedEdge(V src, V dest, E e) {
        return addEdge(src, dest, e) && addEdge(dest, src, e);
    }

    @Override
    public boolean addEdge(V src, V dest, E e) {
        if(!containsVertex(src) || !containsVertex(dest)){
            throw new IllegalArgumentException();
        }
        boolean notExists = true;
        if(adjacent(src, dest) != null){
            notExists = false;
        }
        return vertices.get(src).put(dest, e) != null && notExists;
    }

    @Override
    public boolean removeEdge(V src, V dest) {
        if(!containsVertex(src) || !containsVertex(dest)){
            throw new IllegalArgumentException();
        }
        if(vertices.get(src).remove(dest) != null){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ISet<V> vertices() {
        return vertices.keySet();
    }

    @Override
    public E adjacent(V i, V j) {
        if(!containsVertex(i) || !containsVertex(j)){
            throw new IllegalArgumentException();
        }
        return vertices.get(i).get(j);
    }

    @Override
    public ISet<V> neighbors(V vertex) {
        if(!containsVertex(vertex)){
            throw new IllegalArgumentException();
        }
        return vertices.get(vertex).keySet();
    }
}