import java.util.*;

public class MakeSymbolTable
{
    class SymbolBlock
	{
        SymbolBlock peak;
        LinkedHashMap<String, Name> stack;
        ArrayList<SymbolBlock> base;
        String firstname;
        String scope3;
		int countt = 0;
        public SymbolBlock (String firstname) {this.firstname = firstname; peak = null; base = new ArrayList<SymbolBlock>();stack = new LinkedHashMap<String, Name>();}
    }
	
	class Name
	{
		String num;
		int size;
		String name;
		String type_of_scope;

		public Name (String name, String type_of_scope) {this.name = name; this.type_of_scope = type_of_scope;this.num = null;}
		public Name (String name, String type_of_scope, String num) {this.name = name;this.type_of_scope = type_of_scope;this.num = num;}
		public boolean isString() {return type_of_scope.equals("STRING");}
		
		@Override
		public String toString() 
		{
			if (isString()) {return new String("name " + name + " type_of_scope " + type_of_scope + " num " + num);} 
			else {return new String("name " + name + " type_of_scope " + type_of_scope);}
		}
	}

    SymbolBlock global = null;
    SymbolBlock current = null;
    int blk_nbr = 1;

    public MakeSymbolTable ()
	{
        global = new SymbolBlock("GLOBAL");
        current = global;
    }
    public void openScope(String firstname)
	{
        SymbolBlock state = new SymbolBlock(firstname);
        state.scope3 = "function";
        state.peak = current;
        current = state;
        state.peak.base.add(state);
    }
    public void openScope()
	{
        openScope("BLOCK " + blk_nbr);
        current.scope3 = "block";
        blk_nbr += 1;
    }
    public void exitScope() {current = current.peak;}
    public void closeScope() {current = current.peak;}    
    public void insertString(String name, String num) {current.stack.put(name, new Name(name, "STRING", num));}
    public void enterScope() {current = current.base.get(current.countt); current.countt++;}
    public void addListOfVariables(ArrayList<String> names, String type_of_scope) {for (String name : names) {addSingleVariable(name, type_of_scope);}}
    public void addSingleVariable(String name, String type_of_scope)
	{
        if (current.stack.containsKey(name)) {System.out.println("DECLARATION ERROR " + name); System.exit(0);}
        current.stack.put(name, new Name(name,type_of_scope));
    }
}