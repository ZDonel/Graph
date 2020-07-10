package SPath;

import java.util.LinkedList;


/*
 * Class to store and find shortest path for graphs using Dijkstra's/Bellman-Ford/Floyd-Warshall alg
 * Graphs are stored as an adjacency matrix
 * Other information such as eccentricity is also trackable
 * */
public class Graph {
    int[][] adj;
    boolean[] explored;
    int[] ecc;//eccentricity(greatest distance to another vertex)
    int[] deg;//degree of vertex


    //copy a given graph
    public Graph(Graph g){
        adj = new int[g.adj.length][g.adj.length];
        explored = new boolean[g.adj.length];
        ecc = new int[g.adj.length];
        for (int i = 0; i < adj.length; i++) {
            explored[i] = g.explored[i];
            ecc[i] = g.ecc[i];
            deg[i] = g.deg[i];
            for (int j = 0; j < adj.length; j++) {
                adj[i][j] = g.adj[i][j];
            }
        }
    }

    //create graph from list of edges
    public Graph(LinkedList<String> edges, boolean directed) {
         int vCount = edges.size();
        adj = new int[vCount][vCount];
        for (int i = 0; i < adj.length; i++) {//initialize all edges to 0
            for (int j = 0; j < adj.length; j++) {
                adj[i][j] = 0;
            }
        }
        int from, to, c;
        deg = new int[vCount];
        for (int i = 0; i < adj.length; i++) {
            deg[i] = 0;
        }
        String[] temp = new String[2];
        for(String e : edges) {//parse input strings to edge capacity
            temp = e.split(" ", 2);
            from = Integer.parseInt(temp[0]);
            if(temp.length==1) {continue;}
            temp = temp[1].split(":", 2);
            while(true) {
                to = Integer.parseInt(temp[0]);
                temp = temp[1].split(" ", 2);
                c = Integer.parseInt(temp[0]);
                adj[from][to] = c;
                deg[from]++;
                if(!directed) { 
                    adj[to][from] = c;
                    deg[to]++; 
                }
                if(temp.length == 1) { break; }
                temp = temp[1].split(":", 2);
            }
        }
        explored = new boolean[vCount];
        for (int i = 0; i < explored.length; i++) {
            explored[i] = false;
        }
        ecc = new int[vCount];
        getEcc();
    }

    /* Used to find single source shortest path in graph with no neg(-) weight edges
     * does not check edge weights 
     * u parameter is label of vertex to use as source
     */
    public int[] DijPath(int src) {
        int[] sPaths = new int [adj.length];
        for (int i = 0; i < sPaths.length; i++) {
            if(adj[src][i]!=0){
                sPaths[i] = adj[src][i];
            } else {
                sPaths[i] = Integer.MAX_VALUE;
            }
        }
        sPaths[src] = 0;
        explored[src] = true;
        for (int i = 0; i < explored.length-1; i++) {  
            int u = findMin(sPaths);
            for (int v = 0; v < sPaths.length; v++) {
                if(adj[u][v] != 0 && sPaths[u]+adj[u][v] < sPaths[v]){
                    sPaths[v] = sPaths[u]+adj[u][v];
                }
            }
            explored[u] = true;
        }
        return sPaths;
    }
    
    /* Used in Dij to find minimum discovered vertex in graph
     */
    public int findMin(int[] arr){
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < arr.length; i++) {
            if(arr[i] < min && explored[i] == false)
                min = i;
        }
        return min;
    }
    
    /*Used to find single source shortest path in graph with neg(-) weight edges no neg cycles 
     * Used to find SP for each node in FW
     */
    public int[] BFPath(int src){
        int[] sPaths = new int[adj.length];
        for (int i = 0; i < sPaths.length; i++) {
            if(adj[src][i] != 0){
                sPaths[i] = adj[src][i];
                System.out.println("relaxing " + src + " " + i + " by " + adj[src][i]);
            } else {
                sPaths[i] = Integer.MAX_VALUE;
            }
        }
        sPaths[src] = 0;
        boolean changed = true;
        for (int i = 0; i < adj.length-1; i++) {
            if(changed == false)
                break;
            changed = false;
            for (int u = 0; u < adj.length; u++) {
                for (int v = 0; v < sPaths.length; v++) {
                    if(adj[u][v] != 0 && sPaths[u]+adj[u][v] > 0 && sPaths[u]+adj[u][v] < sPaths[v]){
                        sPaths[v] = sPaths[u]+adj[u][v];
                        System.out.println("relaxing " + u + " " + v + " by " + (sPaths[u]+adj[u][v]));
                        changed = true;
                    }
                }
            }
        }
        changed = false;
        for (int u = 0; u < adj.length; u++) {
            for (int v = 0; v < sPaths.length; v++) {
                if(adj[u][v] != 0 && sPaths[u]+adj[u][v] > 0 && sPaths[u]+adj[u][v] < sPaths[v]){
                    sPaths[v] = sPaths[u]+adj[u][v];
                    changed = true;
                }
            }
        }
        if(changed == false){
            return sPaths;
        } else {
            return null;
        } 
    }

    /* Used to calculate all sp for each node
     * used to get ecc for graph
     */
    public int[][] FWPath() {
        int[][] sPaths = new int[adj.length][adj.length];
        int[] nodeSPs = new int[adj.length];
        for (int i = 0; i < sPaths.length; i++) {
            System.out.println(i);
            nodeSPs = BFPath(i);
            for (int j = 0; j < nodeSPs.length; j++) {
                sPaths[i][j] = nodeSPs[j];
            }
        }
        return sPaths;
    }

    /* Calculates maximum SP length for each node and records in ecc
     * */
    public void getEcc() {
        int[][] sPaths = FWPath();
        for (int i = 0; i < ecc.length; i++) {
            int t = 0;
            for (int j = 0; j < sPaths.length; j++) {
                if (t < sPaths[i][j]) {
                    t = sPaths[i][j];
                }
            }
            ecc[i] = t;
        }
    }

    public String toString(){
        String read = "Vertex\tEcc\tEdges";
        for (int i = 0; i < adj.length; i++) {
            read += i + "\t" + ecc[i] + "\t{";
            for (int j = 0; j < adj.length; j++) {
                if (adj[i][j] != 0) {
                    read += " " + j + ":" + adj[i][j] + " ";
                }
            }
            read += "}\n";
        }
        return read;
    }

}