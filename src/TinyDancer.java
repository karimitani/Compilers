 import java.util.ArrayList;
 import java.util.LinkedHashMap;
 import java.util.Map;
 import java.util.Stack;
 
 public class TinyDancer
 {
   private int reg_num;
   private ArrayList<String> Intermediate_Representation;
   private String output = "";
   private Stack<String> out1 = new Stack<String>();
   private int temp1;
   private int count = 0;
   private int right = 0;
   private SymbolTable info1;
   private int index2 = this.reg_num + 1;
   private int index3 = 0;
   private String label3 = null;
   private int left = 0;
   protected Map<String, Map<String, String>> hashedmap = new LinkedHashMap<String, Map<String, String>>();
   protected Map<String, Integer> link1 = new LinkedHashMap<String, Integer>();
   private ArrayList<String> hold = new ArrayList<String>();
   protected Map<String, Map<String, NodeStructure>> tableMap = new LinkedHashMap<String, Map<String, NodeStructure>>();
   protected Map<String, ArrayList<String>> temporary = new LinkedHashMap<String, ArrayList<String>>();
   private Stack<CFGNode> CFG = new Stack<CFGNode>();
   private ArrayList<String> old = new ArrayList<String>();
   protected Map<String, String> irr = new LinkedHashMap<String, String>();
   private int pos = 0;
   
   public TinyDancer(ArrayList<String> admission, SymbolTable info1, Map<String, Map<String, NodeStructure>> map1, Map<String, ArrayList<String>> temporary, Stack<CFGNode> CFG, int reg_num, int temp1)
   {
     this.reg_num = reg_num;
     this.Intermediate_Representation = admission;
     this.temp1 = temp1;
     this.info1 = info1;
     this.index2 = (reg_num + 1);
     this.tableMap = map1;
     this.temporary = temporary;
     this.right = this.index2;
     this.CFG = CFG;
     int i =0;
     while(i <= (reg_num-1)) {
       this.irr.put("r" + Integer.toString(i), null);
       i++;
     }
     for (String key : this.tableMap.keySet())
     {
       Map<String, String> recent = new LinkedHashMap<String, String>();
       for (NodeStructure each : (this.tableMap.get(key)).values()) {
         if ((!this.hashedmap.containsKey(each.stuff)) && (each.stuff.contains("$P")))
         {recent.put(each.stuff, "$" + Integer.toString(Integer.parseInt(each.stuff.substring(2)) + this.index2));this.right = (Integer.parseInt(each.stuff.substring(2)) + this.index2);}
         else if (each.stuff.contains("$L"))
         {
           recent.put(each.stuff, "$" + Integer.toString(-Integer.parseInt(each.stuff.substring(2)) - this.index3));
           this.left = (-Integer.parseInt(each.stuff.substring(2)) - this.index3);
           this.count = this.count+1;
         }
         else{recent.put(each.stuff, each.stuff);}
       }
       recent.put("$R", "$" + Integer.toString(this.right + 1));
      i=0;
    while(i < (this.temp1+1))
       {
         recent.put("$T" + Integer.toString(i), "$" + Integer.toString(this.left - i));
         this.count = this.count+1;
         i=i+1;
       }
       for (String p : recent.keySet()) {
         if (p.contains("$P")) {recent.put(p, "$" + (this.right - Integer.parseInt(p.substring(2)) + 1));}
       }
       this.hashedmap.put(key, recent);
       this.link1.put(key, Integer.valueOf(this.count));
       this.left = 0;
       this.count = 0;
       this.right = this.index2;
     }
   }
   
   public String toString()
   {
     build_ir();
    int i=0;
    while(i <= (this.out1.size()-1)){
       this.output = this.output.concat((String)this.out1.get(i));
       i=i+1;
     }
     return this.output;
   }
   
   public String build_ir()
   {
     this.out1.add(";IR code\n");
     optimization();
     this.out1.add(";tiny code\n");
     first_global();
     initailMain();
    this.pos=0;
  while(this.pos <= (this.Intermediate_Representation.size()-1))
     {
       String[] arr = ((String)this.Intermediate_Representation.get(this.pos)).split(" ");
       boolean var9=(arr[0].equalsIgnoreCase("STOREF"));
       boolean var10=arr[0].equalsIgnoreCase("MULTF");
       boolean var11=arr[0].equalsIgnoreCase("ADDF");
       boolean var12=(arr[0].equalsIgnoreCase("DIVF"));
       boolean var13=(arr[0].equalsIgnoreCase("SUBF")) ;
       boolean var1=(arr[0].equalsIgnoreCase("STOREI"));
       boolean var2=(arr[0].equalsIgnoreCase("MULTI"));
       boolean var3=(arr[0].equalsIgnoreCase("ADDI"));
       boolean var4=(arr[0].equalsIgnoreCase("DIVI"));
       boolean var5=(arr[0].equalsIgnoreCase("SUBI"));
       boolean var6=(arr[0].equalsIgnoreCase("WRITEI"));
       boolean var7=arr[0].equalsIgnoreCase("WRITES");
       boolean var24=arr[0].equalsIgnoreCase("LEF");
      boolean var25=arr[0].equalsIgnoreCase("GEF");
        boolean var26=arr[0].equalsIgnoreCase("NEF");
        boolean var27=arr[0].equalsIgnoreCase("EQF");
      boolean var28=arr[0].equalsIgnoreCase("GTF");
      boolean var8=arr[0].equalsIgnoreCase("READI");
       boolean var14=(arr[0].equalsIgnoreCase("WRITEF")) ;
       boolean var15=(arr[0].equalsIgnoreCase("READF"));
       boolean var16=(arr[0].equalsIgnoreCase("LABEL"));
       boolean var17=(arr[0].equalsIgnoreCase("JUMP"));
       boolean var18=arr[0].equalsIgnoreCase("LEI");
       boolean var19=arr[0].equalsIgnoreCase("GEI");
       boolean var20=arr[0].equalsIgnoreCase("NEI");
       boolean var21=arr[0].equalsIgnoreCase("EQI");
       boolean var22=arr[0].equalsIgnoreCase("GTI");
       boolean var23=arr[0].equalsIgnoreCase("LTI");
      boolean var29=arr[0].equalsIgnoreCase("LTF");
      boolean var30=arr[0].equalsIgnoreCase("jsr");
      boolean var31=arr[0].equalsIgnoreCase("push");
      boolean var32=arr[0].equalsIgnoreCase("pop");
      boolean var33=arr[0].equalsIgnoreCase("link");
      boolean var34=arr[0].equalsIgnoreCase("RET");
       if (var1)
       {
         if ((arr[1].contains("$")) && (arr[2].contains("$")))
         {
           if ((arr[2].contains("$R")) || (arr[2].contains("$P"))) {this.out1.add("move " + generate_temporary(arr[1]) + " " + (String)((Map<String, String>)this.hashedmap.get(this.label3)).get(arr[2]) + "\n");} 
           else {this.out1.add("move " + generate_temporary(arr[1]) + " " + generate_temporary(arr[2]) + "\n");}
         }
         else if (arr[1].contains("$")){this.out1.add("move " + generate_temporary(arr[1]) + " " + arr[2] + "\n");}
         else if (arr[2].contains("$")){this.out1.add("move " + arr[1] + " " + generate_temporary(arr[2]) + "\n");}
         else
         {
           this.out1.add("move " + arr[1] + " " + reg_pos(arr[2]) + "\n");
           this.out1.add("move " + reg_pos(arr[2]) + " " + arr[2] + "\n");
         }
       }
       else if (var4)
       {
         this.hold.add(arr[3]);
         this.hold.add(arr[2]);this.hold.add(arr[1]);
         if ((arr[1].contains("$")) && (arr[2].contains("$")))
         {
           this.out1.add("move " + generate_temporary(arr[1]) + " " + generate_temporary(arr[3]) + "\n");
           this.out1.add("divi " + generate_temporary(arr[2]) + " " + generate_temporary(arr[3]) + "\n");
         }
         else if (arr[1].contains("$"))
         {
           this.out1.add("move " + generate_temporary(arr[1]) + " " + generate_temporary(arr[3]) + "\n");
           this.out1.add("divi " + arr[2] + " " + generate_temporary(arr[3]) + "\n");
         }
         else if (arr[2].contains("$"))
         {
           this.out1.add("move " + arr[1] + " " + generate_temporary(arr[3]) + "\n");
           this.out1.add("divi " + generate_temporary(arr[2]) + " " + generate_temporary(arr[3]) + "\n");
         }
         else
         {
           this.out1.add("move " + arr[1] + " " + generate_temporary(arr[3]) + "\n");
           this.out1.add("divi " + arr[2] + " " + generate_temporary(arr[3]) + "\n");
         }
         this.hold.clear();
       }
       else if (var5)
       {
         this.hold.add(arr[3]);
         this.hold.add(arr[2]);this.hold.add(arr[1]);
         if ((arr[1].contains("$")) && (arr[2].contains("$")))
         {
           this.out1.add("move " + generate_temporary(arr[1]) + " " + generate_temporary(arr[3]) + "\n");
           this.out1.add("subi " + generate_temporary(arr[2]) + " " + generate_temporary(arr[3]) + "\n");
         }
         else if (arr[1].contains("$"))
         {
           this.out1.add("move " + generate_temporary(arr[1]) + " " + generate_temporary(arr[3]) + "\n");
           this.out1.add("subi " + arr[2] + " " + generate_temporary(arr[3]) + "\n");
         }
         else if (arr[2].contains("$"))
         {
           this.out1.add("move " + arr[1] + " " + generate_temporary(arr[3]) + "\n");
           this.out1.add("subi " + generate_temporary(arr[2]) + " " + generate_temporary(arr[3]) + "\n");
         }
         else
         {
           this.out1.add("move " + arr[1] + " " + generate_temporary(arr[3]) + "\n");
           this.out1.add("subi " + arr[2] + " " + generate_temporary(arr[3]) + "\n");
         }
         this.hold.clear();
       }
       else if (var9)
       {
         if ((arr[1].contains("$")) && (arr[2].contains("$")))
         {
           if ((arr[2].contains("$R")) || (arr[2].contains("$P"))) {
             this.out1.add("move " + generate_temporary(arr[1]) + " " + (String)((Map<String, String>)this.hashedmap.get(this.label3)).get(arr[2]) + "\n");
           } else {
             this.out1.add("move " + generate_temporary(arr[1]) + " " + generate_temporary(arr[2]) + "\n");
           }
         }
         else if (arr[1].contains("$"))
         {
           this.out1.add("move " + generate_temporary(arr[1]) + " " + arr[2] + "\n");
         }
         else if (arr[2].contains("$"))
         {
           this.out1.add("move " + arr[1] + " " + generate_temporary(arr[2]) + "\n");
         }
         else
         {
           this.out1.add("move " + arr[1] + " " + reg_pos(arr[2]) + "\n");
           this.out1.add("move " + reg_pos(arr[2]) + " " + arr[2] + "\n");
         }
       }
       else if (var2)
       {
         this.hold.add(arr[3]);
         this.hold.add(arr[2]);this.hold.add(arr[1]);
         if ((arr[1].contains("$")) && (arr[2].contains("$")))
         {
           this.out1.add("move " + generate_temporary(arr[1]) + " " + generate_temporary(arr[3]) + "\n");
           this.out1.add("muli " + generate_temporary(arr[2]) + " " + generate_temporary(arr[3]) + "\n");
         }
         else if (arr[1].contains("$"))
         {
           this.out1.add("move " + generate_temporary(arr[1]) + " " + generate_temporary(arr[3]) + "\n");
           this.out1.add("muli " + arr[2] + " " + generate_temporary(arr[3]) + "\n");
         }
         else if (arr[2].contains("$"))
         {
           this.out1.add("move " + arr[1] + " " + generate_temporary(arr[3]) + "\n");
           this.out1.add("muli " + generate_temporary(arr[2]) + " " + generate_temporary(arr[3]) + "\n");
         }
         else
         {
           this.out1.add("move " + arr[1] + " " + generate_temporary(arr[3]) + "\n");
           this.out1.add("muli " + arr[2] + " " + generate_temporary(arr[3]) + "\n");
         }
         this.hold.clear();
       }
       else if (var16)
       {
         spillRegister();
         this.out1.add("label " + arr[1] + " " + "\n");
         if (!arr[1].contains("label")) {
           this.label3 = arr[1];
         }
       }
       else if (var13)
       {
         this.hold.add(arr[3]);
         this.hold.add(arr[2]);this.hold.add(arr[1]);
         if ((arr[1].contains("$")) && (arr[2].contains("$")))
         {
           this.out1.add("move " + generate_temporary(arr[1]) + " " + generate_temporary(arr[3]) + "\n");
           this.out1.add("subr " + generate_temporary(arr[2]) + " " + generate_temporary(arr[3]) + "\n");
         }
         else if (arr[1].contains("$"))
         {
           this.out1.add("move " + generate_temporary(arr[1]) + " " + generate_temporary(arr[3]) + "\n");
           this.out1.add("subr " + arr[2] + " " + generate_temporary(arr[3]) + "\n");
         }
         else if (arr[2].contains("$"))
         {
           this.out1.add("move " + arr[1] + " " + generate_temporary(arr[3]) + "\n");
           this.out1.add("subr " + generate_temporary(arr[2]) + " " + generate_temporary(arr[3]) + "\n");
         }
         else
         {
           this.out1.add("move " + arr[1] + " " + generate_temporary(arr[3]) + "\n");
           this.out1.add("subr " + arr[2] + " " + generate_temporary(arr[3]) + "\n");
         }
         this.hold.clear();
       }
       else if (var10)
       {
         this.hold.add(arr[3]);
         this.hold.add(arr[2]);this.hold.add(arr[1]);
         if ((arr[1].contains("$")) && (arr[2].contains("$")))
         {
           this.out1.add("move " + generate_temporary(arr[1]) + " " + generate_temporary(arr[3]) + "\n");
           this.out1.add("mulr " + generate_temporary(arr[2]) + " " + generate_temporary(arr[3]) + "\n");
         }
         else if (arr[1].contains("$"))
         {
           this.out1.add("move " + generate_temporary(arr[1]) + " " + generate_temporary(arr[3]) + "\n");
           this.out1.add("mulr " + arr[2] + " " + generate_temporary(arr[3]) + "\n");
         }
         else if (arr[2].contains("$"))
         {
           this.out1.add("move " + arr[1] + " " + generate_temporary(arr[3]) + "\n");
           this.out1.add("mulr " + generate_temporary(arr[2]) + " " + generate_temporary(arr[3]) + "\n");
         }
         else
         {
           this.out1.add("move " + arr[1] + " " + generate_temporary(arr[3]) + "\n");
           this.out1.add("mulr " + arr[2] + " " + generate_temporary(arr[3]) + "\n");
         }
         this.hold.clear();
       }
       else if (var11)
       {
         this.hold.add(arr[3]);
         this.hold.add(arr[2]);this.hold.add(arr[1]);
         if ((arr[1].contains("$")) && (arr[2].contains("$")))
         {
           this.out1.add("move " + generate_temporary(arr[1]) + " " + generate_temporary(arr[3]) + "\n");
           this.out1.add("addr " + generate_temporary(arr[2]) + " " + generate_temporary(arr[3]) + "\n");
         }
         else if (arr[1].contains("$"))
         {
           this.out1.add("move " + generate_temporary(arr[1]) + " " + generate_temporary(arr[3]) + "\n");
           this.out1.add("addr " + arr[2] + " " + generate_temporary(arr[3]) + "\n");
         }
         else if (arr[2].contains("$"))
         {
           this.out1.add("move " + arr[1] + " " + generate_temporary(arr[3]) + "\n");
           this.out1.add("addr " + generate_temporary(arr[2]) + " " + generate_temporary(arr[3]) + "\n");
         }
         else
         {
           this.out1.add("move " + arr[1] + " " + generate_temporary(arr[3]) + "\n");
           this.out1.add("addr " + arr[2] + " " + generate_temporary(arr[3]) + "\n");
         }
         this.hold.clear();
       }
       else if (var6)
       {
         if (arr[1].contains("$")) {
           this.out1.add("sys writei " + generate_temporary(arr[1]) + "\n");
         } else {
           this.out1.add("sys writei " + arr[1] + "\n");
         }
       }
       else if (var7)
       {
         this.out1.add("sys writes " + arr[1] + "\n");
       }
       else if (var8)
       {
         if (arr[1].contains("$")) {
           this.out1.add("sys readi " + generate_temporary(arr[1]) + "\n");
         } else {
           this.out1.add("sys readi " + arr[1] + "\n");
         }
       }
       else if (var12)
       {
         this.hold.add(arr[3]);
         this.hold.add(arr[2]);this.hold.add(arr[1]);
         if ((arr[1].contains("$")) && (arr[2].contains("$")))
         {
           this.out1.add("move " + generate_temporary(arr[1]) + " " + generate_temporary(arr[3]) + "\n");
           this.out1.add("divr " + generate_temporary(arr[2]) + " " + generate_temporary(arr[3]) + "\n");
         }
         else if (arr[1].contains("$"))
         {
           this.out1.add("move " + generate_temporary(arr[1]) + " " + generate_temporary(arr[3]) + "\n");
           this.out1.add("divr " + arr[2] + " " + generate_temporary(arr[3]) + "\n");
         }
         else if (arr[2].contains("$"))
         {
           this.out1.add("move " + arr[1] + " " + generate_temporary(arr[3]) + "\n");
           this.out1.add("divr " + generate_temporary(arr[2]) + " " + generate_temporary(arr[3]) + "\n");
         }
         else
         {
           this.out1.add("move " + arr[1] + " " + generate_temporary(arr[3]) + "\n");
           this.out1.add("divr " + arr[2] + " " + generate_temporary(arr[3]) + "\n");
         }
         this.hold.clear();
       }
       else if (var15)
       {
         if (arr[1].contains("$")) {
           this.out1.add("sys readr " + generate_temporary(arr[1]) + " " + "\n");
         } else {
           this.out1.add("sys readr " + arr[1] + " " + "\n");
         }
       }
       else if (var14)
       {
         if (arr[1].contains("$")) {
           this.out1.add("sys writer " + generate_temporary(arr[1]) + " " + "\n");
         } else {
           this.out1.add("sys writer " + arr[1] + " " + "\n");
         }
       }
       else if (var17)
       {
         spillRegister();
         this.out1.add("jmp " + arr[1] + " " + "\n");
       }
       else if (var18)
       {
         if ((arr[1].contains("$")) && (arr[2].contains("$")))
         {
           this.out1.add("cmpi " + generate_temporary(arr[1]) + " " + generate_temporary(arr[2]) + "\n");
           spillRegister();
           this.out1.add("jle " + arr[3] + "\n");
         }
         else if (arr[1].contains("$"))
         {
           this.out1.add("move " + arr[2] + " " + generate_temporary(arr[2]) + "\n");
           this.out1.add("cmpi " + generate_temporary(arr[1]) + " " + generate_temporary(arr[2]) + "\n");
           spillRegister();
           this.out1.add("jle " + arr[3] + "\n");
         }
         else if (arr[2].contains("$"))
         {
           this.out1.add("cmpi " + arr[1] + " " + generate_temporary(arr[2]) + "\n");
           spillRegister();
           this.out1.add("jle " + arr[3] + "\n");
         }
         else
         {
           this.out1.add("move " + arr[2] + " " + reg_pos(arr[2]) + "\n");
           this.out1.add("cmpi " + arr[1] + " " + reg_pos(arr[2]) + "\n");
           spillRegister();
           this.out1.add("jle " + arr[3] + "\n");
         }
       }
       else if (var27)
       {
         if ((arr[1].contains("$")) && (arr[2].contains("$")))
         {
           this.out1.add("cmpr " + generate_temporary(arr[1]) + " " + generate_temporary(arr[2]) + "\n");
           spillRegister();
           this.out1.add("jeq " + arr[3] + "\n");
         }
         else if (arr[1].contains("$"))
         {
           this.out1.add("move " + arr[2] + " " + generate_temporary(arr[2]) + "\n");
           this.out1.add("cmpr " + generate_temporary(arr[1]) + " " + generate_temporary(arr[2]) + "\n");
           spillRegister();
           this.out1.add("jeq " + arr[3] + "\n");
         }
         else if (arr[2].contains("$"))
         {
           this.out1.add("cmpr " + arr[1] + " " + generate_temporary(arr[2]) + "\n");
           spillRegister();
           this.out1.add("jeq " + arr[3] + "\n");
         }
         else
         {
           this.out1.add("move " + arr[2] + " " + reg_pos(arr[2]) + "\n");
           this.out1.add("cmpr " + arr[1] + " " + reg_pos(arr[2]) + "\n");
           spillRegister();
           this.out1.add("jeq " + arr[3] + "\n");
         }
       }
       else if (var20)
       {
         if ((arr[1].contains("$")) && (arr[2].contains("$")))
         {
           this.out1.add("cmpi " + generate_temporary(arr[1]) + " " + generate_temporary(arr[2]) + "\n");
           spillRegister();
           this.out1.add("jne " + arr[3] + "\n");
         }
         else if (arr[1].contains("$"))
         {
           this.out1.add("move " + arr[2] + " " + generate_temporary(arr[2]) + "\n");
           this.out1.add("cmpi " + generate_temporary(arr[1]) + " " + generate_temporary(arr[2]) + "\n");
           spillRegister();
           this.out1.add("jne " + arr[3] + "\n");
         }
         else if (arr[2].contains("$"))
         {
           this.out1.add("cmpi " + arr[1] + " " + generate_temporary(arr[2]) + "\n");
           spillRegister();
           this.out1.add("jne " + arr[3] + "\n");
         }
         else
         {
           this.out1.add("move " + arr[2] + " " + reg_pos(arr[2]) + "\n");
           this.out1.add("cmpi " + arr[1] + " " + reg_pos(arr[2]) + "\n");
           spillRegister();
           this.out1.add("jne " + arr[3] + "\n");
         }
       }
       else if (var3)
       {
         this.hold.add(arr[3]);
         this.hold.add(arr[2]);this.hold.add(arr[1]);
         if ((arr[1].contains("$")) && (arr[2].contains("$")))
         {
           this.out1.add("move " + generate_temporary(arr[1]) + " " + generate_temporary(arr[3]) + "\n");
           this.out1.add("addi " + generate_temporary(arr[2]) + " " + generate_temporary(arr[3]) + "\n");
         }
         else if (arr[1].contains("$"))
         {
           this.out1.add("move " + generate_temporary(arr[1]) + " " + generate_temporary(arr[3]) + "\n");
           this.out1.add("addi " + arr[2] + " " + generate_temporary(arr[3]) + "\n");
         }
         else if (arr[2].contains("$"))
         {
           this.out1.add("move " + arr[1] + " " + generate_temporary(arr[3]) + "\n");
           this.out1.add("addi " + generate_temporary(arr[2]) + " " + generate_temporary(arr[3]) + "\n");
         }
         else
         {
           this.out1.add("move " + arr[1] + " " + generate_temporary(arr[3]) + "\n");
           this.out1.add("addi " + arr[2] + " " + generate_temporary(arr[3]) + "\n");
         }
         this.hold.clear();
       }
       else if (var21)
       {
         if ((arr[1].contains("$")) && (arr[2].contains("$")))
         {
           this.out1.add("cmpi " + generate_temporary(arr[1]) + " " + generate_temporary(arr[2]) + "\n");
           spillRegister();
           this.out1.add("jeq " + arr[3] + "\n");
         }
         else if (arr[1].contains("$"))
         {
           this.out1.add("move " + arr[2] + " " + generate_temporary(arr[2]) + "\n");
           this.out1.add("cmpi " + generate_temporary(arr[1]) + " " + generate_temporary(arr[2]) + "\n");
           spillRegister();
           this.out1.add("jeq " + arr[3] + "\n");
         }
         else if (arr[2].contains("$"))
         {
           this.out1.add("cmpi " + arr[1] + " " + generate_temporary(arr[2]) + "\n");
           spillRegister();
           this.out1.add("jeq " + arr[3] + "\n");
         }
         else
         {
           this.out1.add("move " + arr[2] + " " + reg_pos(arr[2]) + "\n");
           this.out1.add("cmpi " + arr[1] + " " + reg_pos(arr[2]) + "\n");
           spillRegister();
           this.out1.add("jeq " + arr[3] + "\n");
         }
       }
       else if (var28)
       {
         if ((arr[1].contains("$")) && (arr[2].contains("$")))
         {
           this.out1.add("cmpr " + generate_temporary(arr[1]) + " " + generate_temporary(arr[2]) + "\n");
           spillRegister();
           this.out1.add("jgt " + arr[3] + "\n");
         }
         else if (arr[1].contains("$"))
         {
           this.out1.add("move " + arr[2] + " " + generate_temporary(arr[2]) + "\n");
           this.out1.add("cmpr " + generate_temporary(arr[1]) + " " + generate_temporary(arr[2]) + "\n");
           spillRegister();
           this.out1.add("jgt " + arr[3] + "\n");
         }
         else if (arr[2].contains("$"))
         {
           this.out1.add("cmpr " + arr[1] + " " + generate_temporary(arr[2]) + "\n");
           spillRegister();
           this.out1.add("jgt " + arr[3] + "\n");
         }
         else
         {
           this.out1.add("move " + arr[2] + " " + reg_pos(arr[2]) + "\n");
           this.out1.add("cmpr " + arr[1] + " " + reg_pos(arr[2]) + "\n");
           spillRegister();
           this.out1.add("jgt " + arr[3] + "\n");
         }
       }
       else if (var23)
       {
         if ((arr[1].contains("$")) && (arr[2].contains("$")))
         {
           this.out1.add("cmpi " + generate_temporary(arr[1]) + " " + generate_temporary(arr[2]) + "\n");
           spillRegister();
           this.out1.add("jlt " + arr[3] + "\n");
         }
         else if (arr[1].contains("$"))
         {
           this.out1.add("move " + arr[2] + " " + generate_temporary(arr[2]) + "\n");
           this.out1.add("cmpi " + generate_temporary(arr[1]) + " " + generate_temporary(arr[2]) + "\n");
           spillRegister();
           this.out1.add("jlt " + arr[3] + "\n");
         }
         else if (arr[2].contains("$"))
         {
           this.out1.add("cmpi " + arr[1] + " " + generate_temporary(arr[2]) + "\n");
           spillRegister();
           this.out1.add("jlt " + arr[3] + "\n");
         }
         else
         {
           this.out1.add("move " + arr[2] + " " + reg_pos(arr[2]) + "\n");
           this.out1.add("cmpi " + arr[1] + " " + reg_pos(arr[2]) + "\n");
           spillRegister();
           this.out1.add("jlt " + arr[3] + "\n");
         }
       }
       else if (var19)
       {
         if ((arr[1].contains("$")) && (arr[2].contains("$")))
         {
           this.out1.add("cmpi " + generate_temporary(arr[1]) + " " + generate_temporary(arr[2]) + "\n");
           spillRegister();
           this.out1.add("jge " + arr[3] + "\n");
         }
         else if (arr[1].contains("$"))
         {
           this.out1.add("move " + arr[2] + " " + generate_temporary(arr[2]) + "\n");
           this.out1.add("cmpi " + generate_temporary(arr[1]) + " " + generate_temporary(arr[2]) + "\n");
           spillRegister();
           this.out1.add("jge " + arr[3] + "\n");
         }
         else if (arr[2].contains("$"))
         {
           this.out1.add("cmpi " + arr[1] + " " + generate_temporary(arr[2]) + "\n");
           spillRegister();
           this.out1.add("jge " + arr[3] + "\n");
         }
         else
         {
           this.out1.add("move " + arr[2] + " " + reg_pos(arr[2]) + "\n");
           this.out1.add("cmpi " + arr[1] + " " + reg_pos(arr[2]) + "\n");
           spillRegister();
           this.out1.add("jge " + arr[3] + "\n");
         }
       }
       else if (var24)
       {
         if ((arr[1].contains("$")) && (arr[2].contains("$")))
         {
           this.out1.add("cmpr " + generate_temporary(arr[1]) + " " + generate_temporary(arr[2]) + "\n");
           spillRegister();
           this.out1.add("jle " + arr[3] + "\n");
         }
         else if (arr[1].contains("$"))
         {
           this.out1.add("move " + arr[2] + " " + generate_temporary(arr[2]) + "\n");
           this.out1.add("cmpr " + generate_temporary(arr[1]) + " " + generate_temporary(arr[2]) + "\n");
           spillRegister();
           this.out1.add("jle " + arr[3] + "\n");
         }
         else if (arr[2].contains("$"))
         {
           this.out1.add("cmpr " + arr[1] + " " + generate_temporary(arr[2]) + "\n");
           spillRegister();
           this.out1.add("jle " + arr[3] + "\n");
         }
         else
         {
           this.out1.add("move " + arr[2] + " " + reg_pos(arr[2]) + "\n");
           this.out1.add("cmpr " + arr[1] + " " + reg_pos(arr[2]) + "\n");
           spillRegister();
           this.out1.add("jle " + arr[3] + "\n");
         }
       }
       else if (var25)
       {
         if ((arr[1].contains("$")) && (arr[2].contains("$")))
         {
           this.out1.add("cmpr " + generate_temporary(arr[1]) + " " + generate_temporary(arr[2]) + "\n");
           spillRegister();
           this.out1.add("jge " + arr[3] + "\n");
         }
         else if (arr[1].contains("$"))
         {
           this.out1.add("move " + arr[2] + " " + generate_temporary(arr[2]) + "\n");
           this.out1.add("cmpr " + generate_temporary(arr[1]) + " " + generate_temporary(arr[2]) + "\n");
           spillRegister();
           this.out1.add("jge " + arr[3] + "\n");
         }
         else if (arr[2].contains("$"))
         {
           this.out1.add("cmpr " + arr[1] + " " + generate_temporary(arr[2]) + "\n");
           spillRegister();
           this.out1.add("jge " + arr[3] + "\n");
         }
         else
         {
           this.out1.add("move " + arr[2] + " " + reg_pos(arr[2]) + "\n");
           this.out1.add("cmpr " + arr[1] + " " + reg_pos(arr[2]) + "\n");
           spillRegister();
           this.out1.add("jge " + arr[3] + "\n");
         }
       }
       else if (var26)
       {
         if ((arr[1].contains("$")) && (arr[2].contains("$")))
         {
           this.out1.add("cmpr " + generate_temporary(arr[1]) + " " + generate_temporary(arr[2]) + "\n");
           spillRegister();
           this.out1.add("jne " + arr[3] + "\n");
         }
         else if (arr[1].contains("$"))
         {
           this.out1.add("move " + arr[2] + " " + generate_temporary(arr[2]) + "\n");
           this.out1.add("cmpr " + generate_temporary(arr[1]) + " " + generate_temporary(arr[2]) + "\n");
           spillRegister();
           this.out1.add("jne " + arr[3] + "\n");
         }
         else if (arr[2].contains("$"))
         {
           this.out1.add("cmpr " + arr[1] + " " + generate_temporary(arr[2]) + "\n");
           spillRegister();
           this.out1.add("jne " + arr[3] + "\n");
         }
         else
         {
           this.out1.add("move " + arr[2] + " " + reg_pos(arr[2]) + "\n");
           this.out1.add("cmpr " + arr[1] + " " + reg_pos(arr[2]) + "\n");
           spillRegister();
           this.out1.add("jne " + arr[3] + "\n");
         }
       }
       else if (var33)
       {
         this.out1.add("link " + this.link1.get(this.label3) + "\n");
       }
       else if (var22)
       {
         if ((arr[1].contains("$")) && (arr[2].contains("$")))
         {
           this.out1.add("cmpi " + generate_temporary(arr[1]) + " " + generate_temporary(arr[2]) + "\n");
           spillRegister();
           this.out1.add("jgt " + arr[3] + "\n");
         }
         else if (arr[1].contains("$"))
         {
           this.out1.add("move " + arr[2] + " " + generate_temporary(arr[2]) + "\n");
           this.out1.add("cmpi " + generate_temporary(arr[1]) + " " + generate_temporary(arr[2]) + "\n");
           spillRegister();
           this.out1.add("jgt " + arr[3] + "\n");
         }
         else if (arr[2].contains("$"))
         {
           this.out1.add("cmpi " + arr[1] + " " + generate_temporary(arr[2]) + "\n");
           spillRegister();
           this.out1.add("jgt " + arr[3] + "\n");
         }
         else
         {
           this.out1.add("move " + arr[2] + " " + reg_pos(arr[2]) + "\n");
           this.out1.add("cmpi " + arr[1] + " " + reg_pos(arr[2]) + "\n");
           spillRegister();
           this.out1.add("jgt " + arr[3] + "\n");
         }
       }
       else if (var29)
       {
         if ((arr[1].contains("$")) && (arr[2].contains("$")))
         {
           this.out1.add("cmpr " + generate_temporary(arr[1]) + " " + generate_temporary(arr[2]) + "\n");
           spillRegister();
           this.out1.add("jlt " + arr[3] + "\n");
         }
         else if (arr[1].contains("$"))
         {
           this.out1.add("move " + arr[2] + " " + generate_temporary(arr[2]) + "\n");
           this.out1.add("cmpr " + generate_temporary(arr[1]) + " " + generate_temporary(arr[2]) + "\n");
           spillRegister();
           this.out1.add("jlt " + arr[3] + "\n");
         }
         else if (arr[2].contains("$"))
         {
           this.out1.add("cmpr " + arr[1] + " " + generate_temporary(arr[2]) + "\n");
           spillRegister();
           this.out1.add("jlt " + arr[3] + "\n");
         }
         else
         {
           this.out1.add("move " + arr[2] + " " + reg_pos(arr[2]) + "\n");
           this.out1.add("cmpr " + arr[1] + " " + reg_pos(arr[2]) + "\n");
           spillRegister();
           this.out1.add("jlt " + arr[3] + "\n");
         }
       }
       else if (var30)
       {
         for (int k = 0; k < this.reg_num; k++) {
           this.out1.add("push r" + Integer.toString(k) + "\n");
         }
         this.out1.add("jsr " + arr[1] + "\n");
         for (int k = this.reg_num - 1; k >= 0; k--) {
           this.out1.add("pop r" + Integer.toString(k) + "\n");
         }
       }
       else if (var31)
       {
         if (arr.length == 1) {
           this.out1.add("push\n");
         } else if (arr[1].contains("$")) {
           this.out1.add("push " + generate_temporary(arr[1]) + "\n");
         } else {
           this.out1.add("push " + arr[1] + "\n");
         }
       }
       else if (var32)
       {
         if (arr.length == 1) {
           this.out1.add("pop\n");
         } else if (arr[1].contains("$")) {
           this.out1.add("pop " + generate_temporary(arr[1]) + "\n");
         } else {
           this.out1.add("pop " + arr[1] + "\n");
         }
       }
       else if (var34)
       {
         spillRegister();
         this.out1.add("unlnk\n");
         this.out1.add("ret\n");
       }
       reg_empty();
       this.pos = this.pos+1;
     }
     this.out1.add("end");
     return this.out1.toString();
   }
   
   private void spillRegister()
   {
    int k=0;
    while(k <= (this.irr.size()-1)) {
       if (this.irr.get("r" + Integer.toString(k)) != null) {this.out1.add("move r" + Integer.toString(k) + " " + (String)((Map<String, String>)this.hashedmap.get(this.label3)).get(this.irr.get(new StringBuilder("r").append(Integer.toString(k)).toString())) + "\n"); this.irr.put("r" + Integer.toString(k), null);}
       k=k+1;
     }
   }
   
   public void initailMain()
   {
     this.out1.add("push\n");
     int k=0;
     while(k <= (this.reg_num-1)){
       this.out1.add("push r" + Integer.toString(k) + "\n");
       k=k+1;
     }
     this.out1.add("jsr main\n");
     this.out1.add("sys halt\n");
   }
   
   public void optimization()
   {
     //for (int i = 0; i < this.Intermediate_Representation.size(); i++) {
      int i=0;
      while(i < this.Intermediate_Representation.size()){
       this.out1.add(";" + (String)this.Intermediate_Representation.get(i) + "\n");
       i=i+1;
      }
   }
   
   public void first_global()
   {
     for (Boresight scope2 : this.info1.stack1.subList(0, this.info1.stack1.size())) {
       if (scope2.type_of_scope.equalsIgnoreCase("GLOBAL")) {
         for (String key : scope2.smap.keySet()) {
           if (((BuildNode)scope2.smap.get(key)).type_of_scope == VariableType.INT) {this.out1.add("var " + key + "\n");} 
           else if (((BuildNode)scope2.smap.get(key)).type_of_scope == VariableType.FLOAT) {this.out1.add("var " + key + "\n");} 
           else if (((BuildNode)scope2.smap.get(key)).type_of_scope == VariableType.STRING) {this.out1.add("str " + key + " " + ((BuildNode)scope2.smap.get(key)).headline.stuff + "\n");} 
           else {System.out.println("error@first_global");}
    }}}}

    private String reg_pos(String in)
   {
     //for (int i = 0; i < this.irr.size(); i++) {
     int i=0;
     while(i <= (this.irr.size()-1)){if (this.irr.get("r" + Integer.toString(i)) == null) {return "r" + Integer.toString(i);}}
     return null;
   }
   
   private boolean reg_packed()
   {
    int i=0;
    while(i <= (this.irr.size()-1)){
       if (this.irr.get("r" + Integer.toString(i)) == null) {return false;}
       i=i+1;
     }
     return true;
   }
   
   private void reg_empty()
   {
    int i=0;
    while(i <= (this.irr.size()-1)){
       if ((!((CFGNode)this.CFG.get(this.pos)).live_variables.contains(this.irr.get("r" + Integer.toString(i)))) && (this.irr.get("r" + Integer.toString(i)) != null)) {this.irr.put("r" + Integer.toString(i), null);}
    i=i+1;
     }
   }
   
   
   public String generate_temporary(String temp)
   {
     if (((Map<?, ?>)this.hashedmap.get(this.label3)).containsKey(temp))
     {
       if (this.irr.containsValue(temp))
       {
          int i=0;
        while(i <= (this.irr.size()-1)){
           if ((this.irr.get("r" + Integer.toString(i)) != null) && 
             (((String)this.irr.get("r" + Integer.toString(i))).equals(temp)))
           {
             if (this.old.contains(temp)) {this.out1.add("move " + (String)((Map<?, ?>)this.hashedmap.get(this.label3)).get(temp) + " " + "r" + Integer.toString(i) + "\n");this.old.remove(temp);}
             return "r" + Integer.toString(i);
           }
           i=i+1;
         }
       }
       else
       {
         if (reg_packed())
         {
           String temporary2 = null;
           if (this.hold != null) {
            int i=0;
            while(i <= (this.reg_num-1))
             {
               temporary2 = "r" + Integer.toString(i);
               if (!this.hold.contains(this.irr.get(temporary2))) {
                 break;}
                 i=i+1;
             }
           } else {temporary2 = "r1";}
           if (((CFGNode)this.CFG.get(this.pos)).live_variables.contains(this.irr.get(temporary2)))
           {
             this.out1.add("move " + temporary2 + " " + (String)((Map<String, String>)this.hashedmap.get(this.label3)).get(this.irr.get(temporary2)) + "\n");
             this.old.add((String)this.irr.get(temporary2));
             this.irr.put(temporary2, temp);
             if (this.old.contains(temp))
             {
               this.out1.add("move " + (String)((Map<String, String>)this.hashedmap.get(this.label3)).get(temp) + " " + temporary2 + "\n");
               this.old.remove(temp);
             }
             else {this.out1.add("move " + (String)((Map<String, String>)this.hashedmap.get(this.label3)).get(temp) + " " + temporary2 + "\n");}
             return temporary2;
           }
           this.irr.put(temporary2, temp);
           if (this.old.contains(temp)) {this.out1.add("move " + (String)((Map<String, String>)this.hashedmap.get(this.label3)).get(temp) + " " + temporary2 + "\n");this.old.remove(temp);}
           return temporary2;
         }
          int i=0;
          while(i <= (this.irr.size()-1)){
           if (this.irr.get("r" + Integer.toString(i)) == null)
           {
             this.irr.put("r" + Integer.toString(i), temp);
             if (temp.contains("P")) {this.out1.add("move " + (String)((Map<String, String>)this.hashedmap.get(this.label3)).get(temp) + " " + "r" + Integer.toString(i) + "\n");}
             if (this.old.contains(temp))
             {
               this.out1.add("move " + (String)((Map<String, String>)this.hashedmap.get(this.label3)).get(temp) + " " + "r" + Integer.toString(i) + "\n");
               this.old.remove(temp);
               return "r" + Integer.toString(i);
             }
             if (temp.contains("L")) {this.out1.add("move " + (String)((Map<String, String>)this.hashedmap.get(this.label3)).get(temp) + " " + "r" + Integer.toString(i) + "\n");}
             if (temp.contains("T")) {this.out1.add("move " + (String)((Map<String, String>)this.hashedmap.get(this.label3)).get(temp) + " " + "r" + Integer.toString(i) + "\n");}             
             return "r" + Integer.toString(i);
           }
          i=i+1;
  }}}
     else
     {
       if (!((Map<?, ?>)this.hashedmap.get("GLOBAL")).containsKey(temp)) 
        {System.out.println("register not enough!! " + temp);
       return null;}
       else{
       return (String)((Map<String, String>)this.hashedmap.get("GLOBAL")).get(temp);}
     }
     System.out.println("register not enough!! " + temp);
     return null;
   }
 }