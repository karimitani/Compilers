import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CFGNode
{
  ArrayList<String> apply = null;
  int type_of_scope = 0;
  Set<String> live_variables = new HashSet<String>();
  ArrayList<String> decide = null;
  public CFGNode(ArrayList<String> decide, ArrayList<String> apply, int type_of_scope) {this.decide = decide; this.type_of_scope = type_of_scope;this.apply = apply;}
  public String toString() {return "apply: " + this.apply + "\ndefine: " + this.decide + "\nliveness: " + this.live_variables;}
}