import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class SymbolTable
{
  protected int note1;
  protected Stack<Boresight> stack1;
  protected ArrayList<Boresight> everything;
  
  public SymbolTable()
  {
    this.stack1 = new Stack<>();
    this.note1 = 0;
    this.everything = new ArrayList<>();
    Boresight globals = new Boresight("GLOBAL", nextgeneration(), null);
    this.stack1.push(globals);
    this.everything.add(globals);
  }
  
  public Boresight pushed(String type_of_scope)
  {
    Boresight scope1 = (Boresight)this.stack1.peek();
    Boresight scope2 = new Boresight(type_of_scope, nextgeneration(), scope1);
    this.stack1.push(scope2);
    this.everything.add(scope2);
    return scope2;
  }

  public Boresight curr()
  {
    if (this.stack1.size() <= 0) {return (Boresight)this.everything.get(0);}
    return (Boresight)this.stack1.peek();
  }

  public String toString()
  {
    StringBuilder string_build = new StringBuilder();
    for (Boresight scope2 : this.stack1.subList(0, this.stack1.size())) {
      string_build.append("Symbol chart " + scope2.type_of_scope + "\n" + scope2.toString() + "\n");
    }
    String temp = string_build.toString().replace("[", "").replace("]", "");
    String temp2 = temp.replaceAll(", ", "");
    return temp2;
  }
    
  public Boresight returnscope(int note1)
  {
    for (Boresight scope2 : this.stack1) {
      if (scope2.note1 == note1) {
        return scope2;
      }
    }
    return null;
  }
    private int nextgeneration() {this.note1 += 1; return this.note1;}
}