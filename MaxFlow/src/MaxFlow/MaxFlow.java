package MaxFlow;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;
import javafx.application.Application;

public class MaxFlow {


	/*some variables may not be used based on needed info
	 **/
	@SuppressWarnings("unused")
	public static void main(String[] args) throws Exception {
		Scanner kb = new Scanner(System.in);
		FileWriter myFile = null;
		String fn = "";
		int size, maxC, maxFF, maxD, maxEK;
		File file = null;
		Scanner read = null;
		double time1, time2, time3;
		FileInputStream fin;
		LinkedList<String> GAL;
		Graph graph;
		System.out.println("Would you like to create a graph or test network flow?\n"
				+ "1. Create graph\n"
				+ "2. Test flow\n"
				+ "3. Quit");
		int choice = kb.nextInt();
		while(choice != 3) {
			if(choice == 1) {
				//System.out.println("What size graph would you like?");
				//size = kb.nextInt();
				//System.out.println("What is max capacity for this graph?");
				//maxC = kb.nextInt();
				maxC = 10;
				Graph imd = null;
				for(size = 100; size < 5501; size+=100) {
				imd = new Graph(size, maxC);
				myFile = new FileWriter("graphs4\\graph"+size+".dat");
				myFile.write(imd.toString());
				myFile.close();
				}
				System.out.println("Would you like to create a graph or test network flow?\n"
						+ "1. Create graph\n"
						+ "2. Test flow\n"
						+ "3. Quit");
				choice = kb.nextInt();
			} else if(choice == 2) {
				//System.out.print("Please enter filename: ");
				fn = "graphs4\\graph100.dat";//"" + kb.nextLine();
				file = new File(fn);
				int i = 100;
				while(file.exists()) {
				//	System.out.print("Please enter VALID filename: ");
				//	fn = kb.nextLine();
				//	file = new File(fn);
				//}
				fin = new FileInputStream(file);
				read = new Scanner(fin);
				GAL = new LinkedList<String>();
				while(read.hasNext()) {
					GAL.add(read.nextLine().trim());
				}
				graph = new Graph(GAL);
				fin.close();
				read.close();
				time1 = System.nanoTime();
				maxFF = graph.PRMaxFlow();//maxFF = graph.FFMaxFlow();
				time1 = (System.nanoTime() - time1);
				time2 = System.nanoTime();
				maxD = graph.DMaxFlow();
				time2 = (System.nanoTime() - time2);
				//time3 = System.nanoTime();
				//maxEK = graph.EKMaxFlow();
				//time3 = (System.nanoTime() - time3);
				myFile = new FileWriter("resultsPR\\4\\result"+i+".dat");
				if(maxFF == maxD) {
					myFile.write("Push-Relabel: "+(time1/1000000000.0)+" secs\n"+
							"Max Flow Calculated: "+ maxFF +
							"\nEdges: " + graph.countEdges());
				} else {myFile.write(maxFF+" "+maxD);}
				myFile.close();
				i=i+100;
				file = new File(fn = "graphs4\\graph"+i+".dat");
				}
				System.out.println("Would you like to create a graph or test network flow?\n"
						+ "1. Create graph\n"
						+ "2. Test flow\n"
						+ "3. Quit");
				choice = kb.nextInt();
				//}
			}
		}
		kb.close();
	}
	
	/*Graph class used to calculate max flows*/
	public static class Graph {
		
		/*Vertex used in Graph*/
		public class Vertex {
			
			int label;
			int level;
			boolean visited;
			LinkedList<Edge> adj;
			
			public Vertex() {
				this.label = -1;
				this.level = Integer.MAX_VALUE;
				this.visited = false;
				this.adj = new LinkedList<Edge>();
			}
			
			public Vertex(Vertex v) {
				this.label = v.getLabel();
				this.level = Integer.MAX_VALUE;
				this.adj = new LinkedList<Edge>();
				for(Edge e : v.adj) {
					this.adj.add(new Edge(e));
				}
				this.visited = false;
			}
			
			public int getLabel() {
				return label;
			}
			
			public int getLevel() {
				return level;
			}
			
			public boolean getVisited() {
				return visited;
			}
			
			public String toString() {
				String read = label+"/"+level+" ";
				for(Edge e : adj) {
					read += e.toString()+" ";
				}
				read.trim();
				return read;
			}
		}
		/*Edge used in Graph*/
		public class Edge {
			int c;
			int f;
			int from;
			int to;
			Edge bEdge;
			
			public Edge(int c, int from, int to) {
				this.c = c;
				this.from = from;
				this.to = to;
				this.f = 0;
				this.bEdge = null;
			}
			
			public Edge(Edge e) {
				this.bEdge = null;
				this.c = e.getC();
				this.f = 0;
				this.to = e.getTo();
				this.from = e.getFrom();
			}
			
			/*Create bEdge for 'this' and links them as bEdge*/
			public Edge createBEdge() {
				Edge back = new Edge(0, this.to, this.from);
				this.bEdge = back;
				back.bEdge = this;
				return back;
			}
			
			public int getC() {
				return c;
			}
			
			public int getTo() {
				return to;
			}
			
			public int getFrom() {
				return from;
			}
			
			public String toString () {
				return to+":"+f+"/"+c;
			}
		}
		
		/*instance variables*/
		private Vertex[] v;
		private Vertex source, sink;
		
		/*Basic Constructor*/
		public Graph() {
			this.v = null;
			this.source = null;
			this.sink = null;
		}
		
		/*Creates graph, used to copy other graphs and add backedges
		 *in order to calculate maxflow*/
		public Graph(Graph g) {
			v = new Vertex[g.v.length];
			for (int i = 0; i < v.length; i++) {
				v[i] = new Vertex(g.v[i]);
			}
			LinkedList<Edge> temp = new LinkedList<Edge>();
			for(Vertex vert : v) {
				for(Edge e : vert.adj) {
					temp.add(e.createBEdge());
				}
			}
			for(Edge e : temp) {
				v[e.from].adj.add(e);
			}
			source = v[0];
			source.level = 0;
			sink = v[v.length-1];
		}
		
		/*Creates Graph from input file
		 *base graph does not have backedges and is maintained as a variable in main
		 *to calculate MaxFlow using different algorithms*/
		public Graph(LinkedList<String> edges) {
			v = new Vertex[edges.size()];
			for(int i = 0; i < v.length; i++) {
				v[i] = new Vertex();
			}
			int from, to, c;
			String[] temp = new String[2];
			LinkedList<Edge> nAdj = null;
			for(String e : edges) {
				nAdj = new LinkedList<Edge>();
				temp = e.split(" ", 2);
				from = Integer.parseInt(temp[0]);
				if(temp.length==1) {v[from].label = from;continue;}
				temp = temp[1].split(":", 2);
				while(true) {
					to = Integer.parseInt(temp[0]);
					temp = temp[1].split(" ", 2);
					c = Integer.parseInt(temp[0]);
					nAdj.add(new Edge(c, from, to));
					if(temp.length==1) {break;}
					temp = temp[1].split(":", 2);
				}
				v[from].label = from;
				v[from].adj = nAdj;
			}
			source = v[0];
			source.level = 0;
			sink = v[v.length-1];
		}
		
		/*Creates random graph with vert vertices and randomly created edges*/
		public Graph(int size, int maxC) {
			Random rand = new Random();
			v = new Vertex[size];
			Edge e = null;
			v[v.length-1] = new Vertex();//create sink
			v[v.length-1].label = v.length-1;
			for(int i = 0; i < v.length-1; i++) {
				v[i] = new Vertex();
				v[i].label = i;
				double chance = .7;
				for(int j = i; j < v.length+i; j++) {
					if(j==i || chance<=0) continue;
					chance = .7-((double)v[i].adj.size()*(16.0/(double)v.length));//controls chance to spawn edge
					//allows max adj list size based on v.length and is most likely to add close forward edges
					if(rand.nextDouble() < chance) {
						e = new Edge(rand.nextInt(maxC)+1, i, j%v.length);
						boolean exists = false;
						for(Edge t: v[i].adj) {
							if(t.to == e.to) exists=true;
						}
						if(!exists)v[i].adj.add(e);
					}
				}
			}
			source = v[0];
			sink = v[v.length-1];
		}
		
		/**
		 * 
		 * @param vert used to copy other graph vertex without creating new edges
		 */
		public Graph(int vert) {
			v = new Vertex[vert];
			for(int i = 0; i < v.length; i++) {
				v[i] = new Vertex();
				v[i].label = i;
			}
			source = v[0];
			sink = v[v.length-1];
		}
		
		/*Calculates max flow for graph using Ford-Fulkerson
		 *Creates a residual graph with back edges for every edge and
		 *c = 0. Finds augmented paths using DFS, augments edges and
		 *repeats until no path to the sink exists
		 *Theoretical O(E*f)*/
		public int FFMaxFlow() {
			int MaxFlow = 0;
			int BN = 0;
			Graph R = new Graph(this);//.CreateRGraph();
			//System.out.println(R);
			for(Edge[] path = R.DFS(); path[sink.label] != null; path = R.DFS()) {//find initial path
				//for(Edge e : path) {System.out.println(e);}
				BN = Integer.MAX_VALUE;
				for(Edge e = path[sink.label]; e != path[source.label]; e = path[e.from]){//find bottleneck
					BN = Math.min((e.c-e.f), BN);
				}
				for(Edge e = path[sink.label]; e != path[source.label]; e = path[e.from]) {
					e.f += BN;//augment edges and bEdges by bottleneck
					e.bEdge.c +=BN;
				}
				//System.out.println(BN+"\n");
				MaxFlow +=BN;//Add BN to MaxFlow
			}
			return MaxFlow;
		}
		
		/*Calculates max flow for graph using Dinik's
		 *Creates a level graph from the given graph that only uses
		 *edges used to first visit a vertex in BFS. Performs DFS
		 *on level graph to find flow to sink. Augments paths and
		 *recreates level graph. If no path to sink exists
		 *Optimized by pruning back edges during DFS
		 *Theoretical O(V^2E)*/
		public int DMaxFlow() {
			Graph R = new Graph(this);
			int MaxFlow = 0;
			int BN = 0;
			Graph L = R.CreateLGraph();
			Edge[] path = L.DFS();
			while(path[sink.label] != null) {
			while(path[sink.label] != null) {//find initial path
				//for(Edge e : path) {System.out.println(e);}
				BN = Integer.MAX_VALUE;
				for(Edge e = path[sink.label]; e != path[source.label]; e = path[e.from]){//find bottleneck
					BN = Math.min((e.c-e.f), BN);
				}
				for(Edge e = path[sink.label]; e != path[source.label]; e = path[e.from]) {
					e.f += BN;//augment edges and bEdges by bottleneck
					e.bEdge.c +=BN;
				}
				MaxFlow +=BN;//Add BN to MaxFlow
				path = L.DFS();
			}
			L = R.CreateLGraph();
			path = L.DFS();
			}
			return MaxFlow;
		}
		
		/*Calculates max flow for graph using Edmonds-Karp
		 *Finds augmented paths using BFS, augments edges and repeats
		 *until no path to sink exists
		 *Theoretical O(VE^2)*/
		public int EKMaxFlow() {
			int MaxFlow = 0; 
			int BN = 0;
			Graph R = new Graph(this);//.CreateRGraph();
			Edge[] path = R.BFS();//find initial path
			while(path[sink.label] != null) {
				BN = Integer.MAX_VALUE;
				for(Edge e = path[sink.label]; e != path[source.label]; e = path[e.from]){//find bottleneck
					BN = Math.min((e.c-e.f), BN);
				}
				for(Edge e = path[sink.label]; e != path[source.label]; e = path[e.from]) {
					e.f += BN;//augment edges and bEdges by bottleneck
					e.bEdge.c +=BN;
				}
				MaxFlow +=BN;//Add BN to MaxFlow
				path = R.BFS();
			} 
			return MaxFlow;
		}
		
		/* Function to find Max flow on flow network using Push-Relabel method
		 * Creates separate residual network and sets height of all vertices to 0 and
		 * source vertex to height v.length for original graph.
		 * Pushes flow through network only to lower vertices
		 * When we can't push to lower heights we relabel and push again
		 */
		public int PRMaxFlow() {
			Graph R = new Graph(this);
			int MaxFlow = 0;
			for(Vertex vert : R.v) {
				vert.level = 0;//height here
				vert.label = 0;//excess flow here
			}
			R.source.level = R.v.length;
			for(Edge e : R.source.adj) {
				e.f = e.c;//saturate edge
				R.v[e.to].label += e.c; //set excess flow at receiving vertex
				e.bEdge.f -= e.c; //maintain flow conservation on bedge
			}
			Vertex temp = R.relabel();
			while(temp!=null) {
				//System.out.println(temp);
				while(temp.label != 0) {temp.level++;R.push(temp);
				}
				//System.out.println(temp);
				//Thread.sleep(10);
				temp = R.relabel();
			}
			for(Edge edg : R.source.adj) {
				MaxFlow+=edg.f;
			}
			return MaxFlow;
		}
		
		/*Runs to find vertex with excess flow and sets its height equal
		 *to min-height neighbor +1*/
		public Vertex relabel() {
			for(Vertex vert : v) {
				if(vert == source || vert == sink) continue;//do not relabel source or sink for pushing
				if(vert.label > 0) {
					return vert;
				}
			}
			return null;
		}
		
		/*pushes for a relabeled vertex*/
		public void push(Vertex vert) {
			for(Edge e : vert.adj) {
				if(vert.label == 0)return;
				int temp = e.c-e.f;
				if(vert.level > v[e.to].level+1 && temp > 0) {
					if(vert.label < temp)temp = vert.label;
					e.f += temp;
					e.bEdge.f -= temp;
					v[e.to].label += temp;
					vert.label -= temp;
				}
			}
		}
		
		
		/*BFS used in EKMaxFlow and DMaxFlow to find SP from source 
		 *to sink
		 *Returns Edge array of Edges to augment*/
		public Edge[] BFS() {
			Queue<Vertex> q = new LinkedList<Vertex>();
			Vertex current = null;
			Edge[] path = new Edge[this.v.length];
			this.SetAllUnvisited();
			source.visited = true;
			q.offer(this.source);
			while(!q.isEmpty()) {
				current = q.poll();
				if(current == sink)
					break;//sink has been reached return now unless creating level graph
				for(Edge e: current.adj) {
					if(e.c-e.f > 0 && v[e.to].visited != true) {//Edge needs residual capacity to be traversed
						v[e.to].visited = true;
						path[v[e.to].label] = e;//Basically, this edge e is how we got to vertex e.to first
						q.offer(v[e.to]);
					}
				}
			}
			return path;
		}
		
		/*DFS used in FFMaxFlow and DMaxFlow to find a path from 
		 *source to sink
		 *Returns path as array of Edges to augment*/
		public Edge[] DFS() {
			Stack<Vertex> stack = new Stack<Vertex>();
			Edge[] path = new Edge[this.v.length];
			Vertex current = null;
			this.SetAllUnvisited();
			stack.push(source);
			while(!stack.isEmpty()) {
				current = stack.pop();
				//System.out.println(current);
				if(current == sink)//sink has been reached return now
					break;
				if(current.visited != true) {
					current.visited = true;
					for(Edge e : v[current.label].adj) {
						if(e.c-e.f > 0) {//Edge needs residual capacity to be traversed
							stack.push(this.v[e.to]);
							if(!v[e.to].visited)
								path[e.to] = e;
					}
					}
				}
			}
			//for(Edge e : path) {System.out.println(e);}
			return path;
		}

		/*Sets all vertices visited to false*/
		public void SetAllUnvisited() {
			for(Vertex v : v) {
			v.visited = false;	
			v.level = Integer.MAX_VALUE;
			}
		}
		
		/*Method to create Level graph for Dinic's Algorithm
		 *removes edges between vertices at the same "level" of the BFS*/
		public Graph CreateLGraph() {
			Graph L = new Graph(this.v.length);
			Queue<Vertex> q = new LinkedList<Vertex>();
			LinkedList<Edge> temp = new LinkedList<Edge>();
			Vertex current = null;
			L.SetAllUnvisited();
			L.source.visited = true;
			L.source.level = 0;
			q.offer(L.source);
			//System.out.println("\nnew");
			while(!q.isEmpty()) {
				current = q.poll();
				//System.out.println(current + "\n" + current.level);
				for(Edge e : v[current.label].adj) {
					//System.out.println(e +""+ L.v[e.to].level);
					if(e.c-e.f > 0 && current.level < L.v[e.to].level) {//Edge needs residual capacity to be traversed
						L.v[e.to].level = current.level+1;
						//System.out.println("adding");
						temp.add(e);
						if(L.v[e.to].visited == false) {
							L.v[e.to].visited = true;
							q.offer(L.v[e.to]);
						}
					} 
				}
			}
			for(Edge e : temp) {
				L.v[e.from].adj.add(e);
			}
			return L;
		}
		
		//counts total edges in graph for runtime purposes
		public int countEdges() {
			int total = 0;
			for(Vertex vert : this.v) {
				total+=vert.adj.size();
			}
			return total;
		}
		
		/*Generic toString method for printing*/
		public String toString() {
			String read = "";
			for(Vertex vert : v) {
				read += vert.toString() + "\n";
			}
			return read;
		}
	}
	
}
