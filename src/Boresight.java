import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Boresight
{
  public String type_of_scope;
  protected Map<String, BuildNode> smap = new LinkedHashMap<String, BuildNode>();
  public final int note1;
  public Boresight scope1;
  
  public Boresight(String type_of_scope, int note1, Boresight scope1) {this.scope1 = scope1;this.type_of_scope = type_of_scope;this.note1 = note1;}
  
  public void decide(String name, ArrayList<String> params)
  {
    String args = Strings.asString(params, true, ".");
    BuildNode sign = new BuildNode(null, name + args, null);
    decide(sign);
  }
  public void decide(Quantifier headline, String name, VariableType type_of_scope) {BuildNode sign = new BuildNode(headline, name, type_of_scope); decide(sign);}
  private void decide(BuildNode sign)
  {
    sign.putscope(this);
    if (this.smap.containsKey(sign.name)) { throw new IllegalArgumentException("DECLARATION ERROR " + sign.name);}
    this.smap.put(sign.name, sign);
  }
  private BuildNode resolve(String name)
  {
    BuildNode sign = (BuildNode)this.smap.get(name);
    if (sign != null) {return sign;}
    if (this.scope1 != null) {return this.scope1.resolve(name);}
    return null;
  }
  public String toString() {return this.smap.values().toString();}
  public BuildNode resolve(String name, ArrayList<String> params) {String args = Strings.asString(params, true, "."); return resolve(name + args);}
  public Boresight scope1() {return this.scope1;}
}