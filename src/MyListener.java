import java.util.LinkedHashMap;
import java.util.Map;

public class MyListener extends MicroBaseListener
{
  int counter = 1;
  MicroParser parser;
  public SymbolTable chart;
  protected Map<String, Integer> map = new LinkedHashMap<String, Integer>();
  
  public MyListener(MicroParser parser) {this.chart = new SymbolTable();this.parser = parser;}
  public void enterFunction_declaration(MicroParser.Function_declarationContext ctx)
  {
    this.chart.pushed(ctx.name().getText());
    if (ctx.any_type().getText().equalsIgnoreCase("INT")) {this.map.put(ctx.name().getText(), Integer.valueOf(1));} 
    else if (ctx.any_type().getText().equalsIgnoreCase("FLOAT")) {this.map.put(ctx.name().getText(), Integer.valueOf(2));} 
    else {this.map.put(ctx.name().getText(), Integer.valueOf(6));}
  }
  public void enterParameter_declaration(MicroParser.Parameter_declarationContext ctx)
  {
    if (ctx.variable_type().getText().equals("INT")) {this.chart.curr().decide(new Quantifier(true), ctx.name().getText(), VariableType.INT);} 
    else {this.chart.curr().decide(new Quantifier(true), ctx.name().getText(), VariableType.FLOAT);}
  }
  public void exitVariable_declaration_list(MicroParser.Variable_declaration_listContext ctx)
  {
    String temp = ctx.name_list().getText();
    String[] array = temp.split(",");
    if (ctx.variable_type().getText().equals("INT")) {
      for (String var : array) {this.chart.curr().decide(new Quantifier(), var, VariableType.INT);}
    } else {
      for (String var : array) {this.chart.curr().decide(new Quantifier(), var, VariableType.FLOAT);}
    }
  }  
  public void enterElse_portion(MicroParser.Else_portionContext ctx) {if (!ctx.getText().equals("")) {this.chart.pushed("BLOCK " + Integer.toString(this.counter));this.counter += 1;}}
  public void exitString_declaration_list(MicroParser.String_declaration_listContext ctx) {this.chart.curr().decide(new Quantifier(ctx.string().getText()), ctx.name().getText(), VariableType.STRING);}
}