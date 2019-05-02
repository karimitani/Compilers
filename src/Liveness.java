import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
 
 public class Liveness
 {
   protected Stack<CFGNode> CFG = new Stack<CFGNode>();
   private ArrayList<String> IntermediateRepresentaton;
   private SymbolTable chart;
   private Set<String> full_set = new HashSet<String>();
   protected Map<String, Map<String, NodeStructure>> tableMap = new LinkedHashMap<String, Map<String, NodeStructure>>();
   private Map<String, Integer> labelPosition = new LinkedHashMap<String,Integer>();
   private Map<Integer, ArrayList<Integer>> loop = new LinkedHashMap<Integer, ArrayList<Integer>>();
   private Map<Integer, Set<String>> loopRecord = new LinkedHashMap<Integer, Set<String>>();
   private String out1 = "";
   
   public Liveness(ArrayList<String> admission, Map<String, Map<String, NodeStructure>> map1, SymbolTable chart)
   {
     this.chart = chart;
     this.IntermediateRepresentaton = admission;
     this.tableMap = map1;
     first_global();
     flowgraph();
     live();
   }
   
   public String toString()
   {
     for (int j = 0; j < this.CFG.size(); j++) {this.out1 = this.out1.concat(j + " " + (String)this.IntermediateRepresentaton.get(j) + " " + ((CFGNode)this.CFG.get(j)).live_variables + "\n");}
     return this.out1;
   }
   
   private void flowgraph()
   {
    int j=0;
    while(j < this.IntermediateRepresentaton.size()){
       String[] arr = ((String)this.IntermediateRepresentaton.get(j)).split(" ");
       ArrayList<String> decide = new ArrayList<String>();
       ArrayList<String> apply = new ArrayList<String>();
       boolean var1=(arr[0].equalsIgnoreCase("STOREI")) || (arr[0].equalsIgnoreCase("STOREF"));
       boolean var2=(arr[0].equalsIgnoreCase("MULTI")) || (arr[0].equalsIgnoreCase("MULTF"));
       boolean var3=(arr[0].equalsIgnoreCase("ADDI")) || (arr[0].equalsIgnoreCase("ADDF"));
       boolean var4=(arr[0].equalsIgnoreCase("DIVI")) || (arr[0].equalsIgnoreCase("DIVF"));
       boolean var5=(arr[0].equalsIgnoreCase("SUBI")) || (arr[0].equalsIgnoreCase("SUBF"));
       boolean var6=(arr[0].equalsIgnoreCase("WRITEI")) || (arr[0].equalsIgnoreCase("WRITEF"));
       boolean var7=arr[0].equalsIgnoreCase("WRITES");
       boolean var8=(arr[0].equalsIgnoreCase("READI")) || (arr[0].equalsIgnoreCase("READF"));
       boolean var9=arr[0].equalsIgnoreCase("LABEL");
       boolean var10=arr[0].equalsIgnoreCase("JUMP");
       boolean var11=(arr[0].equalsIgnoreCase("LEI")) || (arr[0].equalsIgnoreCase("LEF"));
       boolean var12=(arr[0].equalsIgnoreCase("GEI")) || (arr[0].equalsIgnoreCase("GEF"));
       boolean var13=(arr[0].equalsIgnoreCase("NEI")) || (arr[0].equalsIgnoreCase("NEF"));
       boolean var14=(arr[0].equalsIgnoreCase("EQI")) || (arr[0].equalsIgnoreCase("EQF"));
       boolean var15=(arr[0].equalsIgnoreCase("GTI")) || (arr[0].equalsIgnoreCase("GTF"));
       boolean var16=(arr[0].equalsIgnoreCase("LTI")) || (arr[0].equalsIgnoreCase("LTF"));
       boolean var17=arr[0].equalsIgnoreCase("jsr");
       boolean var18=arr[0].equalsIgnoreCase("push");
       boolean var19=arr[0].equalsIgnoreCase("pop");
       boolean var20=arr[0].equalsIgnoreCase("link");
       boolean var21=arr[0].equalsIgnoreCase("RET");
       if (var1)
       {
         if ((variable_find(arr[1])) && (variable_find(arr[2])))
         {
           apply.add(arr[2]);
           decide.add(arr[1]);
           CFGNode curr_node = new CFGNode(decide, apply, 1);
           this.CFG.add(curr_node);
         }
         else if (variable_find(arr[2]))
         {
           apply.add(arr[2]);
           CFGNode curr_node = new CFGNode(decide, apply, 1);
           this.CFG.add(curr_node);
         }
       }
       else if (var7)
       {
         CFGNode curr_node = new CFGNode(decide, apply, 1);
         this.CFG.add(curr_node);
       }
       else if (var3)
       {
         apply.add(arr[3]);
         decide.add(arr[1]);
         decide.add(arr[2]);
         CFGNode curr_node = new CFGNode(decide, apply, 1);
         this.CFG.add(curr_node);
       }
       else if (var5)
       {
         apply.add(arr[3]);
         decide.add(arr[1]);
         decide.add(arr[2]);
         CFGNode curr_node = new CFGNode(decide, apply, 1);
         this.CFG.add(curr_node);
       }
       else if (var2)
       {
         apply.add(arr[3]);
         decide.add(arr[1]);
         decide.add(arr[2]);
         CFGNode curr_node = new CFGNode(decide, apply, 1);
         this.CFG.add(curr_node);
       }
       else if (var19)
       {
         if (arr.length != 1)
         {
           apply.add(arr[1]);
           CFGNode curr_node = new CFGNode(decide, apply, 1);
           this.CFG.add(curr_node);
         }
         else
         {
           CFGNode curr_node = new CFGNode(decide, apply, 1);
           this.CFG.add(curr_node);
         }
       }
       else if (var6)
       {
         decide.add(arr[1]);
         CFGNode curr_node = new CFGNode(decide, apply, 1);
         this.CFG.add(curr_node);
       }
       else if (var9)
       {
         apply.add(arr[1]);
         CFGNode curr_node = new CFGNode(decide, apply, 3);
         this.CFG.add(curr_node);
       }
       else if (var4)
       {
         apply.add(arr[3]);
         decide.add(arr[1]);
         decide.add(arr[2]);
         CFGNode curr_node = new CFGNode(decide, apply, 1);
         this.CFG.add(curr_node);
       }
       else if (var10)
       {
         apply.add(arr[1]);
         CFGNode curr_node = new CFGNode(decide, apply, 4);
         this.CFG.add(curr_node);
       }
       else if (var12)
       {
         decide.add(arr[1]);
         decide.add(arr[2]);
         apply.add(arr[3]);
         CFGNode curr_node = new CFGNode(decide, apply, 2);
         this.CFG.add(curr_node);
       }
       else if (var13)
       {
         decide.add(arr[1]);
         decide.add(arr[2]);
         apply.add(arr[3]);
         CFGNode curr_node = new CFGNode(decide, apply, 2);
         this.CFG.add(curr_node);
       }
       else if (var8)
       {
         apply.add(arr[1]);
         CFGNode curr_node = new CFGNode(decide, apply, 1);
         this.CFG.add(curr_node);
       }
       else if (var11)
       {
         decide.add(arr[1]);
         decide.add(arr[2]);
         apply.add(arr[3]);
         CFGNode curr_node = new CFGNode(decide, apply, 2);
         this.CFG.add(curr_node);
       }
       else if (var14)
       {
         decide.add(arr[1]);
         decide.add(arr[2]);
         apply.add(arr[3]);
         CFGNode curr_node = new CFGNode(decide, apply, 2);
         this.CFG.add(curr_node);
        }
       else if (var17)
       {
         apply.add(arr[1]);
         CFGNode curr_node = new CFGNode(decide, apply, 5);
         this.CFG.add(curr_node);
       }
       else if (var18)
       {
         if (arr.length != 1)
         {
           decide.add(arr[1]);
           CFGNode curr_node = new CFGNode(decide, apply, 1);
           this.CFG.add(curr_node);
         }
         else
         {
           CFGNode curr_node = new CFGNode(decide, apply, 1);
           this.CFG.add(curr_node);
         }
       }
       else if (var15)
       {
         decide.add(arr[1]);
         decide.add(arr[2]);
         apply.add(arr[3]);
         CFGNode curr_node = new CFGNode(decide, apply, 2);
         this.CFG.add(curr_node);
       }
       else if (var16)
       {
         decide.add(arr[1]);
         decide.add(arr[2]);
         apply.add(arr[3]);
         CFGNode curr_node = new CFGNode(decide, apply, 2);
         this.CFG.add(curr_node);
       }
       else if (var20)
       {
         CFGNode curr_node = new CFGNode(decide, apply, 6);
         this.CFG.add(curr_node);
       }
       else if (var21)
       {
         decide.add("$R");
         CFGNode curr_node = new CFGNode(decide, apply, 7);
         this.CFG.add(curr_node);
       }
       j=j+1;
     }
     control_graph();
   }
   
   private void control_graph()
   {
    int i = this.CFG.size() - 1;
      while(i>=0){
       if (((CFGNode)this.CFG.elementAt(i)).type_of_scope == 3) {
         this.labelPosition.put((String)((CFGNode)this.CFG.elementAt(i)).apply.get(0), Integer.valueOf(i));
       }
       i=i-1;
     }
       i=0;
      while(i < this.CFG.size()){
       if ((((CFGNode)this.CFG.elementAt(i)).type_of_scope == 2) || (((CFGNode)this.CFG.elementAt(i)).type_of_scope == 4)) {
         if (i < ((Integer)this.labelPosition.get(((CFGNode)this.CFG.elementAt(i)).apply.get(0))).intValue())
         {
           if (this.loop.containsKey(this.labelPosition.get(((CFGNode)this.CFG.elementAt(i)).apply.get(0)))) {((ArrayList<Integer>)this.loop.get(this.labelPosition.get(((CFGNode)this.CFG.elementAt(i)).apply.get(0)))).add(Integer.valueOf(i));}
           else
           {
             ArrayList<Integer> temp = new ArrayList<Integer>();
             temp.add(Integer.valueOf(i));
             this.loop.put((Integer)this.labelPosition.get(((CFGNode)this.CFG.elementAt(i)).apply.get(0)), temp);
           }
         }
         else if (this.loop.containsKey(this.labelPosition.get(((CFGNode)this.CFG.elementAt(i)).apply.get(0)))) {((ArrayList<Integer>)this.loop.get(this.labelPosition.get(((CFGNode)this.CFG.elementAt(i)).apply.get(0)))).add(Integer.valueOf(i));}
         else
         {
           ArrayList<Integer> temp = new ArrayList<Integer>();
           temp.add(Integer.valueOf(i));
           this.loop.put((Integer)this.labelPosition.get(((CFGNode)this.CFG.elementAt(i)).apply.get(0)), temp);
         }
       }
       i=i+1;
  }}
   
   private boolean variable_find(String string1) {return (((Map<?, ?>)this.tableMap.get("GLOBAL")).containsKey(string1)) || (string1.contains("$"));}

   private void live()
   {
     int changes = 0;
     for (int i = this.CFG.size() - 1; (i >= 0) && (changes == 0); i--)
     {
       this.loopRecord.put(Integer.valueOf(i), new HashSet<String>(((CFGNode)this.CFG.get(i)).live_variables));
       if (i == this.CFG.size() - 1) {((CFGNode)this.CFG.get(i)).live_variables.addAll(this.full_set);}
       else if (this.loop.containsKey(Integer.valueOf(i)))
       {
         for (int j = 0; j < ((ArrayList<?>)this.loop.get(Integer.valueOf(i))).size(); j++)
         {
           ((CFGNode)this.CFG.get(i)).live_variables.addAll(((CFGNode)this.CFG.get(i + 1)).live_variables);
           ((CFGNode)this.CFG.get(i)).live_variables.removeAll(((CFGNode)this.CFG.get(i + 1)).apply);
           ((CFGNode)this.CFG.get(i)).live_variables.addAll(((CFGNode)this.CFG.get(i + 1)).decide);
           ((CFGNode)this.CFG.get(((Integer)((ArrayList<?>)this.loop.get(Integer.valueOf(i))).get(j)).intValue())).live_variables.addAll(((CFGNode)this.CFG.get(i)).live_variables);
           ((CFGNode)this.CFG.get(((Integer)((ArrayList<?>)this.loop.get(Integer.valueOf(i))).get(j)).intValue())).live_variables.removeAll(((CFGNode)this.CFG.get(i)).apply);
           ((CFGNode)this.CFG.get(((Integer)((ArrayList<?>)this.loop.get(Integer.valueOf(i))).get(j)).intValue())).live_variables.addAll(((CFGNode)this.CFG.get(i)).decide);
         }
       }
       else if (((CFGNode)this.CFG.get(i)).type_of_scope == 6) {((CFGNode)this.CFG.get(i)).live_variables.addAll(this.full_set);}
       else
       {
         ((CFGNode)this.CFG.get(i)).live_variables.addAll(((CFGNode)this.CFG.get(i + 1)).live_variables);
         ((CFGNode)this.CFG.get(i)).live_variables.removeAll(((CFGNode)this.CFG.get(i + 1)).apply);
         ((CFGNode)this.CFG.get(i)).live_variables.addAll(((CFGNode)this.CFG.get(i + 1)).decide);
       }
       if (i == 0) {
         for (int j = this.CFG.size() - 1; j >= 0; j--) {
           if (containsAll((Set<String>)this.loopRecord.get(Integer.valueOf(j)), ((CFGNode)this.CFG.get(j)).live_variables)){changes = 1;}
           else {changes = 0; i = this.CFG.size();break;}
}}}}
   
   private void first_global()
   {
     for (Boresight scope2 : this.chart.stack1.subList(0, this.chart.stack1.size())) {
       if (scope2.type_of_scope.equalsIgnoreCase("GLOBAL")) {
         for (String key : scope2.smap.keySet()) {
           if (((BuildNode)scope2.smap.get(key)).type_of_scope == VariableType.INT) {this.full_set.add(key);} 
           else if (((BuildNode)scope2.smap.get(key)).type_of_scope == VariableType.FLOAT) {this.full_set.add(key);} 
           else if (((BuildNode)scope2.smap.get(key)).type_of_scope != VariableType.STRING) {System.out.println("error@first_global");}
  }}}}
   
   private boolean containsAll(Set<String> in1, Set<String> in2)
   {
     if (in2.isEmpty())
     {
       if (! in1.isEmpty()) {return false;}
       else {return true;}
     }
     else{return in1.containsAll(in2);}}
   
}