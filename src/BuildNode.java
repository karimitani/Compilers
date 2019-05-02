public class BuildNode
{
  protected VariableType type_of_scope;
  protected Boresight scope2;
  protected Quantifier headline;
  protected String name;
  public BuildNode(Quantifier headline, String name, VariableType type_of_scope) {this.type_of_scope = type_of_scope;this.headline = headline;this.name = name;}
  public Quantifier returnheadline() {return this.headline;}
  public String returnname() {return this.name;}
  public VariableType returntype() {return this.type_of_scope;}
  public void putscope(Boresight scope2) {this.scope2 = scope2;}
  public Boresight returnscope() {return this.scope2;}
  public String toString()
  {
    if (this.type_of_scope != null)
    {
      if (this.type_of_scope != VariableType.STRING) {
        debugprint2();
      }
      else if (this.type_of_scope == VariableType.STRING) {return "name " + returnname() + " type_of_scope " + this.type_of_scope + " value " + this.headline.stuff + "\n";}
      if(0==1){}
      else{
      return "name " + returnname() + " type_of_scope " + this.type_of_scope + "\n";}
    }
    if(0==1){}
      else{
    return returnname();}
    return returnname();
  }
  public static void debugprint2() 
{ 
  int x = 1; 
    // Exit when x becomes greater than 4 
        while (x <= 4) 
        { 
              if(x ==70)
    {
      System.out.println("Value of x:" + x); 
      }
    // Increment the value of x for 
          // next iteration 
            x++; 
        } 
  }
}
