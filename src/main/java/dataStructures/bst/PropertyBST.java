
package dataStructures.bst;
public class PropertyBST {
    private static class N{int k;String n;N l,r;N(int k,String n){this.k=k;this.n=n;}}
    private N r;
    public void insert(int k,String n){r=i(r,k,n);}
    private N i(N x,int k,String n){if(x==null)return new N(k,n);if(k<x.k)x.l=i(x.l,k,n);else x.r=i(x.r,k,n);return x;}
    public void delete(int k){r=d(r,k);}
    private N d(N x,int k){
        if(x==null)return null;
        if(k<x.k)x.l=d(x.l,k);
        else if(k>x.k)x.r=d(x.r,k);
        else{
            if(x.l==null)return x.r;
            if(x.r==null)return x.l;
            N t=m(x.r);x.k=t.k;x.n=t.n;x.r=d(x.r,t.k);
        }
        return x;
    }
    private N m(N x){while(x.l!=null)x=x.l;return x;}
    public void inorder(StringBuilder sb){o(r,sb);}
    private void o(N x,StringBuilder sb){if(x==null)return;o(x.l,sb);sb.append(x.n).append(" (").append(x.k).append(")\n");o(x.r,sb);}
}
