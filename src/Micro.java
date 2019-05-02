import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;

public class Micro 
{
  public static void main(String[] args) throws Exception
  {
	try
	{
		ANTLRFileStream stream = new ANTLRFileStream(args[0]);
		MicroLexer lex = new MicroLexer(stream);
		int res0=ASTmodifiernum(5);
    	int res1=returns1(res0);
    	debugprint();
    	debugprint2() ;
		CommonTokenStream cts = new CommonTokenStream(lex);
		MicroParser parse = new MicroParser(cts);
		ANTLRErrorStrategy es = new BailErrorStrategy();
		parse.setErrorHandler(es);
		ParseTree tree = parse.program();
		int res2=ASTmodifiernum(10);
    	int res3=returns1(res2);
		ParseTreeWalker walking = new ParseTreeWalker();
		MyListener listen = new MyListener(parse);
		walking.walk(listen, tree);
		debugprint();
		MyVisitor visited = new MyVisitor(listen.chart, listen.map);
		visited.visit(tree);
		debugprint();
		Liveness CFG = new Liveness(visited.output, visited.tableMap, listen.chart);
		System.out.print(new TinyDancer(visited.output, listen.chart, visited.tableMap, visited.temporary, CFG.CFG, 4, visited.index5));
	}
	catch (ParseCancellationException e) 
	{
		System.out.println(e);
    }
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

  public static int ASTmodifiernum(int add0)
{
  int total = 1; // Increment the value of x for 
          // next iteration 
  int variable = total*add0;
  return variable;
}

public static int returns1(int randomval)
{
  if( 1+1 == 2)//only for testing java
  {
    return 1;
  }
  else
  {
    System.out.println("This is for debugging");
  }
  return 5;
}

public static void debugprint()
{
  int variable1 = 33;
  for(int i=0;i<1;i++)
  {
    variable1 = variable1 - 32;      
  }
  if(variable1 == 0)
  {
    System.out.println("This is for debugging");
  } 
}
}
