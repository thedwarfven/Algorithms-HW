import java.util.*;

public class NineTailVerification {
  public static void main(String[] args) {
    NineTailModel model = new NineTailModel();
    int max = 0, edges = 0;
    for (int start = 0; start < 512; start++) {
      if (NineTailModel.getIndex(NineTailModel.getNode(start)) != start)
        throw new AssertionError("Encoding " + start);
      List<Integer> path = model.getShortestPath(start);
      int independent = distance(start);
      if (path.get(0) != start || path.get(path.size()-1) != 511
          || path.size()-1 != independent)
        throw new AssertionError("Path " + start);
      for (int i = 0; i + 1 < path.size(); i++) {
        boolean legal = false;
        for (int k = 0; k < 9; k++) {
          char[] board = NineTailModel.getNode(path.get(i));
          if (board[k] == 'H' && NineTailModel.getFlippedNode(board,k) == path.get(i+1))
            legal = true;
        }
        if (!legal) throw new AssertionError("Illegal move");
      }
      for (char c : NineTailModel.getNode(start)) if (c == 'H') edges++;
      max = Math.max(max, independent);
    }
    System.out.println("PASS: 512 encoding round trips.");
    System.out.println("PASS: all 512 states reach state 511.");
    System.out.println("PASS: every path uses legal head selections.");
    System.out.println("PASS: shortest distances match independent forward BFS.");
    System.out.println("Directed edges: " + edges);
    System.out.println("Maximum shortest distance: " + max);
    List<Integer> example = model.getShortestPath(0);
    System.out.println("All-heads path: " + example);
    System.out.println("Moves: " + (example.size()-1));
  }
  static int distance(int start) {
    int[] dist = new int[512];
    Arrays.fill(dist,-1);
    ArrayDeque<Integer> q = new ArrayDeque<>();
    q.add(start); dist[start]=0;
    while (!q.isEmpty()) {
      int s=q.remove();
      if(s==511) return dist[s];
      for(int k=0;k<9;k++) {
        int mask=1 << (8-k);
        if((s & mask)!=0) continue;
        int r=k/3,c=k%3;
        if(r>0) mask |= 1 << (8-(k-3));
        if(r<2) mask |= 1 << (8-(k+3));
        if(c>0) mask |= 1 << (8-(k-1));
        if(c<2) mask |= 1 << (8-(k+1));
        int next=s^mask;
        if(dist[next]<0) {dist[next]=dist[s]+1;q.add(next);}
      }
    }
    return -1;
  }
}
