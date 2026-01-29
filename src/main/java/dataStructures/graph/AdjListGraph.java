
package dataStructures.graph;
public class AdjListGraph {
    private static class Edge{int to,w;Edge n;Edge(int t,int w,Edge n){to=t;this.w=w;this.n=n;}}
    private Edge[] a;
    public AdjListGraph(int n){a=new Edge[n];}
    public void addOrUpdate(int f,int t,int w){
        Edge e=a[f];
        while(e!=null){ if(e.to==t){e.w+=w;return;} e=e.n; }
        a[f]=new Edge(t,w,a[f]);
    }
    public int totalOut(int v){int s=0;Edge e=a[v];while(e!=null){s+=e.w;e=e.n;}return s;}
}
