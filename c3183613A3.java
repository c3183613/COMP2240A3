import java.util.Scanner;
import java.io.File;
import java.util.Vector;

class c3183613A3
{
	public static void main(String args[])
	{
		c3183613A3 main = new c3183613A3();
		main.run(args);
	}

	public void run(String args[])
	{
		Process newProcess;
		Vector<Process> addVector = new Vector<Process>();
		try
		{
			// Get input
			// For however many inputs in command line
			// Create a Process for each with file contents pass
			for(int i=0; i<args.length;i++)
			{
				Scanner scan = new Scanner(new File(args[i]));
				String nextLine = scan.nextLine(); // line 1
				nextLine = scan.nextLine(); // line 2
				newProcess = new Process(args[i]);
				// Add execution trace for each Process
				while(!nextLine.equals("end"))
				{
					newProcess.pages.add(new Page(newProcess.getID()+"_"+Integer.parseInt(nextLine)));
					nextLine = scan.nextLine();
				}
				// Print out all contents of newProcess
				// System.out.println("Name: "+newProcess.getID());
				// for(Integer j: newProcess.executionTrace)
				// {
				// 	System.out.print(j);
				// }
				// System.out.println();
				addVector.add(newProcess);
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		finally
		{
			Simulation sim = new Simulation(addVector);
			sim.lruFixed();	
			sim.lruVariable();
			sim.fixedClock();
			sim.variableClock();
		}
	}
}