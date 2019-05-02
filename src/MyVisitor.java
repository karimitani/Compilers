import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;

 public class MyVisitor extends MicroBaseVisitor<NodeStructure>
 {
   private Stack<String> going = new Stack<String>();
   private Stack<Integer> stack_num = new Stack<Integer>();
   public ArrayList<String> output = new ArrayList<String>();
   private Stack<String> label_of_stack = new Stack<String>();
   private int index3 = 0;
   private int counter = 0;
   private int index1 = 0;
   private int index2 = 0;
   private Stack<String> broken = new Stack<String>();
   private Stack<String> stack2 = new Stack<String>();
   private int index4 = 0;
   public int index5 = 0;
   private Stack<ArrayList<NodeStructure>> stack_out1 = new Stack<ArrayList<NodeStructure>>();
   private Stack<ArrayList<NodeStructure>> stack_out2 = new Stack<ArrayList<NodeStructure>>();
   protected Map<String, Map<String, NodeStructure>> tableMap = new LinkedHashMap<String, Map<String, NodeStructure>>();
   protected Map<String, Integer> map = new LinkedHashMap<String, Integer>();
   protected Map<String, ArrayList<String>> temporary = new LinkedHashMap<String, ArrayList<String>>();
   private String functionRecord = "GLOBAL";
   
   public MyVisitor(SymbolTable chart, Map<String, Integer> map)
   {
     this.map = map;
     for (Boresight scope2 : chart.stack1.subList(0, chart.stack1.size()))
     {
       Map<String, NodeStructure> varMap = new LinkedHashMap<String, NodeStructure>();
       if (scope2.type_of_scope.equalsIgnoreCase("GLOBAL")) {
         for (String key : scope2.smap.keySet()) {
           if (((BuildNode)scope2.smap.get(key)).type_of_scope == VariableType.INT) {varMap.put(key, new NodeStructure(key, 1));} 
           else if (((BuildNode)scope2.smap.get(key)).type_of_scope == VariableType.FLOAT) {varMap.put(key, new NodeStructure(key, 2));} 
           else if (((BuildNode)scope2.smap.get(key)).type_of_scope == VariableType.STRING) {varMap.put(key, new NodeStructure(key, 5));} 
           else {System.out.println("error@constructor");}
         }
       } else {
         for (String key : scope2.smap.keySet()) {
           if (((BuildNode)scope2.smap.get(key)).headline.question)
           {
             if (((BuildNode)scope2.smap.get(key)).type_of_scope == VariableType.INT) {varMap.put(key, new NodeStructure(Creation(true), 1));}
             else if (((BuildNode)scope2.smap.get(key)).type_of_scope == VariableType.FLOAT) {varMap.put(key, new NodeStructure(Creation(true), 2));}
             else if (((BuildNode)scope2.smap.get(key)).type_of_scope == VariableType.STRING) {varMap.put(key, new NodeStructure(key, 5));}
            else {System.out.println("error@constructor");}
           }
           else if (((BuildNode)scope2.smap.get(key)).type_of_scope == VariableType.INT) {varMap.put(key, new NodeStructure(Creation(false), 1));}
          else if (((BuildNode)scope2.smap.get(key)).type_of_scope == VariableType.FLOAT) {varMap.put(key, new NodeStructure(Creation(false), 2));}
           else if (((BuildNode)scope2.smap.get(key)).type_of_scope == VariableType.STRING) {varMap.put(key, new NodeStructure(key, 5));}
          else {System.out.println("error@constructor");}
         }
       }
       this.tableMap.put(scope2.type_of_scope, varMap);
       this.index1 = 0;
       this.index2 = 0;
}}
   
   private String Creation(boolean question)
   {
     this.index1 += 1;
     if (question){this.index2 += 1;return "$P" + Integer.toString(this.index2);}
     return "$L" + Integer.toString(this.index1);
   }
   
   public NodeStructure which_node(String name, String scopeName)
   {
     if (((Map<?, ?>)this.tableMap.get(scopeName)).get(name) == null)
     {
       if (((Map<?, ?>)this.tableMap.get("GLOBAL")).get(name) == null)
       {System.out.println("can't find assign variable, error@which_node");return null;}
       return (NodeStructure)((Map<?, ?>)this.tableMap.get("GLOBAL")).get(name);
     }
     return (NodeStructure)((Map<?, ?>)this.tableMap.get(scopeName)).get(name);
   }

   public NodeStructure visitAssignment_frame(MicroParser.Assignment_frameContext ctx)
   {
     NodeStructure expression1 = (NodeStructure)visit(ctx.expression());
     NodeStructure curr_node = which_node(ctx.name().getText(), this.functionRecord);
     if (curr_node.type_of_scope == 1) {this.output.add("STOREI " + expression1.stuff + " " + curr_node.stuff);} 
     else {this.output.add("STOREF " + expression1.stuff + " " + curr_node.stuff);}
     return null;
   }
   
   public NodeStructure visitWhile_statement(MicroParser.While_statementContext ctx)
   {
     String label1 = createLabel();
     String label2 = createLabel();
     this.output.add("LABEL " + label1);
     this.label_of_stack.add(label2);
     NodeStructure differ = (NodeStructure)visit(ctx.condition());
     this.going.push("JUMP " + label1);
     this.broken.push("JUMP " + label2);
     this.output.add(differ.stuff + " " + label2);
     visit(ctx.statement_list());
     this.output.add("JUMP " + label1);
     this.output.add("LABEL " + label2);
     this.broken.pop();
     this.going.pop();
     return null;
   }
   
   
   public NodeStructure visitFunction_declaration(MicroParser.Function_declarationContext ctx)
   {
     ArrayList<String> newTempList = new ArrayList<String>();
     this.output.add("LABEL " + ctx.name().getText());
     this.functionRecord = ctx.name().getText();
     this.temporary.put(this.functionRecord, newTempList);
     this.output.add("LINK ");
     visitChildren(ctx);
     this.index4 = 0;
     if (ctx.any_type().getText().equals("VOID")) {
       this.output.add("RET");
     }
     return null;
   }
   
   public NodeStructure visitExpression_call(MicroParser.Expression_callContext ctx)
   {
     this.stack_num.push(Integer.valueOf(this.counter));
     this.counter = 0;
     if (ctx.expression_list() != null) {
       visit(ctx.expression_list());
     }
     this.output.add("PUSH ");
     String[] reverseList = new String[this.counter];
     int i=0;
     while(i < this.counter){
       reverseList[i] = ((String)this.stack2.pop());
       i=i+1;
     }
      i=this.counter - 1;
      while(i>=0){
       this.output.add("PUSH " + reverseList[i]);
       i=i-1;
     }
     this.output.add("JSR " + ctx.name().getText());
      i=0;
      while(i < this.counter){
       this.output.add("POP ");
      i=i+1;
     }
     this.counter = ((Integer)this.stack_num.pop()).intValue();
     NodeStructure curr_node = new NodeStructure(generate_temporary(), ((Integer)this.map.get(this.functionRecord)).intValue());
     this.output.add("POP " + curr_node.stuff);
     return curr_node;
   }

   public NodeStructure visitPrimary(MicroParser.PrimaryContext ctx)
   {
     if (ctx.expression() != null) {return (NodeStructure)visit(ctx.expression());}
     if (ctx.name() != null) {return which_node(ctx.name().getText(), this.functionRecord);}
     if (ctx.INTLITERAL() != null)
     {
       NodeStructure curr_node = new NodeStructure(generate_temporary(), 1);
       this.output.add("STOREI " + ctx.INTLITERAL().getText() + " " + curr_node.stuff);
       return curr_node;
     }
     NodeStructure curr_node = new NodeStructure(generate_temporary(), 2);
     this.output.add("STOREF " + ctx.FLOATLITERAL().getText() + " " + curr_node.stuff);
     return curr_node;
   }
   
   public NodeStructure visitExpression_list(MicroParser.Expression_listContext ctx)
   {
	 NodeStructure expression1 = (NodeStructure) visit(ctx.expression());
     this.stack2.push(expression1.stuff);
     this.counter += 1;
     if (!"".equals(ctx.expression_list_repeat().getText())) {visit((ctx.expression_list_repeat()));}
     return null;
   }
   
   public NodeStructure visitParameter_declaration(MicroParser.Parameter_declarationContext ctx)
   {
     if (ctx.variable_type().getText().equalsIgnoreCase("INT")) {new NodeStructure(ctx.name().getText(), 1);}
    else {new NodeStructure(ctx.name().getText(), 2);}
     return null;
   }
   
   public NodeStructure visitExpression(MicroParser.ExpressionContext ctx)
   {
     if (!"".equals(ctx.pre_expression().getText()))
     {
       ArrayList<NodeStructure> list1 = new ArrayList<NodeStructure>();
       this.stack_out2.push(list1);
       visit(ctx.pre_expression());
       NodeStructure solution1 = (NodeStructure)visit(ctx.factor());
       ((ArrayList<NodeStructure>)this.stack_out2.peek()).add(solution1);
       NodeStructure solution = resolve((ArrayList<NodeStructure>)this.stack_out2.pop());
       return solution;
     }
     NodeStructure solution1 = (NodeStructure)visit(ctx.factor());
     return solution1;
   }
   
   public NodeStructure visitPre_expression(MicroParser.Pre_expressionContext ctx)
   {
     if (!"".equals(ctx.pre_expression().getText())) {visit(ctx.pre_expression());}
     NodeStructure opcode1 = new NodeStructure(ctx.addition_operation().getText(), 3);
     NodeStructure solution1 = (NodeStructure)visit(ctx.factor());
     ((ArrayList<NodeStructure>)this.stack_out2.peek()).add(solution1);
     ((ArrayList<NodeStructure>)this.stack_out2.peek()).add(opcode1);
     return null;
   }
   
   public NodeStructure visitFactor(MicroParser.FactorContext ctx)
   {
     if (!"".equals(ctx.pre_factor().getText()))
     {
       ArrayList<NodeStructure> factorList = new ArrayList<NodeStructure>();
       this.stack_out1.push(factorList);
       visit(ctx.pre_factor());
       NodeStructure fixed = (NodeStructure)visit(ctx.post_expression());
       ((ArrayList<NodeStructure>)this.stack_out1.peek()).add(fixed);
       NodeStructure solution = resolve((ArrayList<NodeStructure>)this.stack_out1.pop());
       return solution;
     }
     return (NodeStructure)visit(ctx.post_expression());
   }
   
   public NodeStructure visitPre_factor(MicroParser.Pre_factorContext ctx)
   {
     if (!"".equals(ctx.pre_factor().getText())) {visit(ctx.pre_factor());}
     NodeStructure opcode1 = new NodeStructure(ctx.multiplication_operation().getText(), 3);
     NodeStructure fixed = (NodeStructure)visit(ctx.post_expression());
     ((ArrayList<NodeStructure>)this.stack_out1.peek()).add(fixed);
     ((ArrayList<NodeStructure>)this.stack_out1.peek()).add(opcode1);
     return null;
   }
  
   public NodeStructure visitExpression_list_repeat(MicroParser.Expression_list_repeatContext ctx)
   {
   NodeStructure expression1 = (NodeStructure)visit(ctx.expression());
     this.stack2.push(expression1.stuff);
     this.counter += 1;
     if (!"".equals(ctx.expression_list_repeat().getText())) {visit(ctx.expression_list_repeat());}
     return null;
   }
   
   public NodeStructure visitRe_turn(MicroParser.Re_turnContext ctx)
   {
     NodeStructure expression1 = (NodeStructure)visit(ctx.expression());
     NodeStructure temporary1 = new NodeStructure(generate_temporary(), expression1.type_of_scope);
     if (expression1.type_of_scope != 1)
     {this.output.add("STOREF " + expression1 + " " + temporary1);this.output.add("STOREF " + temporary1 + " $R");}
     else
     {this.output.add("STOREI " + expression1 + " " + temporary1);this.output.add("STOREI " + temporary1 + " $R");}
     this.output.add("RET");
     return null;
   }
   
   public NodeStructure visitRead(MicroParser.ReadContext ctx)
   {
     String[] arr1 = ctx.name_list().getText().split(",");
     //for (int i = 0; i < arr1.length; i++)
     int i=0;
     while(i < arr1.length)
     {
       NodeStructure curr_node = which_node(arr1[i], this.functionRecord);
       if (curr_node.type_of_scope == 1) {
         this.output.add("READI " + curr_node.stuff);
       } else {
         this.output.add("READF " + curr_node.stuff);
       }
      i=i+1;
     }
     return null;
   }
  
   public NodeStructure visitWrite(MicroParser.WriteContext ctx)
   {
     String[] arr1 = ctx.name_list().getText().split(",");
     int i=0;
     while(i < arr1.length)
     {
       NodeStructure curr_node = which_node(arr1[i], this.functionRecord);
       if (curr_node.type_of_scope == 1) {this.output.add("WRITEI " + curr_node.stuff);}
       else if (curr_node.type_of_scope == 5) {this.output.add("WRITES " + curr_node.stuff);} 
       else {this.output.add("WRITEF " + curr_node.stuff);}
       i=i+1;
     }
     return null;
   }
   
   public NodeStructure visitIf_statement(MicroParser.If_statementContext ctx)
   {
     if (!"".equals(ctx.else_portion().getText()))
     {
       NodeStructure differ = (NodeStructure)visit(ctx.condition());
       if (differ.stuff.equalsIgnoreCase("TRUE")) {visit(ctx.statement_list());}
       else if (differ.stuff.equalsIgnoreCase("FALSE"))
       {
         String label2 = createLabel();
         this.label_of_stack.push(label2);
         visit(ctx.else_portion());
         this.output.add("LABEL " + label2);
       }
       else
       {
         String label1 = createLabel();
         String label2 = createLabel();
         this.output.add(differ.stuff + " " + label1);
         visit(ctx.statement_list());
         this.label_of_stack.push(label2);
         this.output.add("JUMP " + label2);
         this.output.add("LABEL " + label1);
         visit(ctx.else_portion());
         this.output.add("LABEL " + label2);
       }
     }
     else
     {
       NodeStructure differ = (NodeStructure)visit(ctx.condition());
       if (differ.stuff.equalsIgnoreCase("TRUE")){visit(ctx.statement_list());}
       else if (!differ.stuff.equalsIgnoreCase("FALSE"))
       {
         String label2 = createLabel();
         this.output.add(differ.stuff + " " + label2);
         visit(ctx.statement_list());
         this.output.add("LABEL " + label2);
       }
     }
     return null;
   }
   
   public NodeStructure visitElse_portion(MicroParser.Else_portionContext ctx) {visit(ctx.statement_list()); return null;}
   
   public NodeStructure visitCondition(MicroParser.ConditionContext ctx) {NodeStructure op1 = (NodeStructure)visit(ctx.expression(0)); visit(ctx.comparison_operator()); NodeStructure op2 = (NodeStructure)visit(ctx.expression(1));return resolveComp(op1, op2, ctx.comparison_operator().getText());}
   
   public String resolveDoComp(String admission)
   {
	   /*switch(admission)
	   {
	   	case("GEI"):
	   		return admission.replace("GEI", "LTI");
		case("LEI"):
			return admission.replace("LEI", "GTI");
		case("NEI"):
			return admission.replace("NEI", "EQI");
		case("EQI"):
			return admission.replace("EQI", "NEI");
		case("GTI"):
			return admission.replace("GTI", "LEI");
		case("LTI"):
			return admission.replace("LTI", "GEI");
		case("GEF"):
			  return admission.replace("GEF", "LTF");
		case("LEF"):
			return admission.replace("LEF", "GTF");
		case("NEF"):
			return admission.replace("NEF", "EQF");
		case("EQF"):
			return admission.replace("EQF", "NEF");
		case("GTF"):
			return admission.replace("GTF", "LEF");
		case("LTF"):
			return admission.replace("LTF", "GEF");
		default:
			return null;
	   }*/
     if(admission=="GEI"){return admission.replace("GEI", "LTI");}
     else if(admission=="LEI"){return admission.replace("LEI", "GTI");}
     else if(admission=="NEI"){return admission.replace("NEI", "EQI");}
     else if(admission=="EQI"){return admission.replace("EQI", "NEI");}
     else if(admission=="GTI"){return admission.replace("GTI", "LEI");}
     else if(admission=="LTI"){return admission.replace("LTI", "GEI");}
     else if(admission=="GEF"){return admission.replace("GEF", "LTF");}
     else if(admission=="LEF"){return admission.replace("LEF", "GTF");}
     else if(admission=="NEF"){return admission.replace("NEF", "EQF");}
     else if(admission=="EQF"){return admission.replace("EQF", "NEF");}
     else if(admission=="GTF"){return admission.replace("GTF", "LEF");}
     else if(admission=="LTF"){return admission.replace("LTF", "GEF");}
     else {return null;}
   }
   
   public NodeStructure resolveComp(NodeStructure left_variable, NodeStructure right_variable, String operation)
   {
     if ((right_variable.type_of_scope == 1) && (left_variable.type_of_scope == 1))
     {
    	 switch(operation)
       /*if(operation=="<"){return new NodeStructure("GEI " + left_variable.stuff + " " + right_variable.stuff, 4);}
       else if(operation==">"){return new NodeStructure("LEI " + left_variable.stuff + " " + right_variable.stuff, 4);}
       else if(operation=="="){return new NodeStructure("NEI " + left_variable.stuff + " " + right_variable.stuff, 4);}
       else if(operation=="!="){return new NodeStructure("EQI " + left_variable.stuff + " " + right_variable.stuff, 4);}
       else if(operation=="<="){return new NodeStructure("GTI " + left_variable.stuff + " " + right_variable.stuff, 4);}
       else if(operation==">="){return new NodeStructure("LTI " + left_variable.stuff + " " + right_variable.stuff, 4);}
       else{return null;}*/
    	 {
    	 	case("<"):
    	 		return new NodeStructure("GEI " + left_variable.stuff + " " + right_variable.stuff, 4);
    	 	case(">"):
    	 		return new NodeStructure("LEI " + left_variable.stuff + " " + right_variable.stuff, 4);
    	 	case("="):
    	 		return new NodeStructure("NEI " + left_variable.stuff + " " + right_variable.stuff, 4);
    	 	case("!="):
    	 		return new NodeStructure("EQI " + left_variable.stuff + " " + right_variable.stuff, 4);
    	 	case("<="):
    	 		return new NodeStructure("GTI " + left_variable.stuff + " " + right_variable.stuff, 4);
    	 	case(">="):
    	 		return new NodeStructure("LTI " + left_variable.stuff + " " + right_variable.stuff, 4);
    	 	default:
    	 		return null;
    	 }
     }
     else
     {
    	 switch(operation)
    	 {
    	 	case("<"):
    	 		return new NodeStructure("GEF " + left_variable.stuff + " " + right_variable.stuff, 4);
	    	case(">"):
	    		return new NodeStructure("LEF " + left_variable.stuff + " " + right_variable.stuff, 4);
	    	case("="):
	    		return new NodeStructure("NEF " + left_variable.stuff + " " + right_variable.stuff, 4);
	      	case("!="):
	      		return new NodeStructure("EQF " + left_variable.stuff + " " + right_variable.stuff, 4);
	    	case("<="):
	    		return new NodeStructure("GTF " + left_variable.stuff + " " + right_variable.stuff, 4);
	      	case(">="):
	      		return new NodeStructure("LTF " + left_variable.stuff + " " + right_variable.stuff, 4);
	      	default:
	      		return null;
    	 }
       /*if(operation=="<"){return new NodeStructure("GEF " + left_variable.stuff + " " + right_variable.stuff, 4);}
       if(operation==">"){return new NodeStructure("LEF " + left_variable.stuff + " " + right_variable.stuff, 4);}
       if(operation=="="){return new NodeStructure("NEF " + left_variable.stuff + " " + right_variable.stuff, 4);}
       if(operation=="!="){return new NodeStructure("EQF " + left_variable.stuff + " " + right_variable.stuff, 4);}
       if(operation=="<="){return new NodeStructure("GTF " + left_variable.stuff + " " + right_variable.stuff, 4);}
       if(operation==">="){return new NodeStructure("LTF " + left_variable.stuff + " " + right_variable.stuff, 4);}
       else{return null;}*/
     }
   }
   
   public String createLabel()
   {this.index3 = this.index3+1;return "label" + Integer.toString(this.index3);}
   
   public String generate_temporary()
   {
     this.index4 = this.index4+1;
     if (this.index4 > this.index5) {this.index5 = this.index4;}
     ((ArrayList<String>)this.temporary.get(this.functionRecord)).add("$T" + Integer.toString(this.index4));
     return "$T" + Integer.toString(this.index4);
   }
   
   public NodeStructure resolve(ArrayList<NodeStructure> admission)
	  {
	    while (admission.size() >= 3)
	    {
	      NodeStructure left_variable = (NodeStructure)admission.get(0);
	      NodeStructure operation = (NodeStructure)admission.get(1);
	      NodeStructure right_operation = (NodeStructure)admission.get(2);
	      
	      NodeStructure curr_node = new NodeStructure(generate_temporary(), left_variable.type_of_scope);
        /*if(operation.stuff=="+"){if (left_variable.type_of_scope == 1)
            {
              String output = "ADDI " + left_variable.stuff + " " + right_operation.stuff + " " + curr_node.stuff;
              this.output.add(output);
            }
            else
            {
              String output = "ADDF " + left_variable.stuff + " " + right_operation.stuff + " " + curr_node.stuff;
              this.output.add(output);
            }}
        else if(operation.stuff=="-"){if (left_variable.type_of_scope == 1)
            {
              String output = "SUBI " + left_variable.stuff + " " + right_operation.stuff + " " + curr_node.stuff;
              this.output.add(output);
            }
            else
            {
              String output = "SUBF " + left_variable.stuff + " " + right_operation.stuff + " " + curr_node.stuff;
              this.output.add(output);
            }}
        else if(operation.stuff=="*"){if (left_variable.type_of_scope == 1)
            {
              String output = "MULTI " + left_variable.stuff + " " + right_operation.stuff + " " + curr_node.stuff;
              this.output.add(output);
            }
            else
            {
              String output = "MULTF " + left_variable.stuff + " " + right_operation.stuff + " " + curr_node.stuff;
              this.output.add(output);
            }}
        else if(operation.stuff=="/"){if (left_variable.type_of_scope == 1)
            {
              String output = "DIVI " + left_variable.stuff + " " + right_operation.stuff + " " + curr_node.stuff;
              this.output.add(output);
            }
            else
            {
              String output = "DIVF " + left_variable.stuff + " " + right_operation.stuff + " " + curr_node.stuff;
              this.output.add(output);
            }}*/
            switch(operation.stuff)
        {
          case("+"):
            if (left_variable.type_of_scope == 1)
            {
              String output = "ADDI " + left_variable.stuff + " " + right_operation.stuff + " " + curr_node.stuff;
              this.output.add(output);
            }
            else
            {
              String output = "ADDF " + left_variable.stuff + " " + right_operation.stuff + " " + curr_node.stuff;
              this.output.add(output);
            }
          break;
          case("-"):
            if (left_variable.type_of_scope == 1)
            {
              String output = "SUBI " + left_variable.stuff + " " + right_operation.stuff + " " + curr_node.stuff;
              this.output.add(output);
            }
            else
            {
              String output = "SUBF " + left_variable.stuff + " " + right_operation.stuff + " " + curr_node.stuff;
              this.output.add(output);
            }
          break;
          case("*"):
            if (left_variable.type_of_scope == 1)
            {
              String output = "MULTI " + left_variable.stuff + " " + right_operation.stuff + " " + curr_node.stuff;
              this.output.add(output);
            }
            else
            {
              String output = "MULTF " + left_variable.stuff + " " + right_operation.stuff + " " + curr_node.stuff;
              this.output.add(output);
            }
          break;
          case("/"):
            if (left_variable.type_of_scope == 1)
            {
              String output = "DIVI " + left_variable.stuff + " " + right_operation.stuff + " " + curr_node.stuff;
              this.output.add(output);
            }
            else
            {
              String output = "DIVF " + left_variable.stuff + " " + right_operation.stuff + " " + curr_node.stuff;
              this.output.add(output);
            }
          break;
        }
        int a = 0;
       while(a<=2)
	      {
	    	  admission.remove(0);
          a=a+1;
	      }
	      admission.add(0, curr_node);
	    }
	    NodeStructure returnValue = (NodeStructure)admission.get(0);
	    admission.removeAll(admission);
	    return returnValue;
	  }
   
   public String printOutput()
   {
     String out1 = "";
     int i=0;
     while (i <= (this.output.size()-1)) {
       out1 = out1 + (String)this.output.get(i);
       out1 = out1 + "\n";
       i=i+1;
     }
     return out1;
   }
 }
