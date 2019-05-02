public class NodeStructure
{
  public String num;
  public String stuff;
  public int type_of_scope;
  public NodeStructure(String newContent, int newType) {this.stuff = newContent;this.type_of_scope = newType;}
  public String toString() {return this.stuff;}
}