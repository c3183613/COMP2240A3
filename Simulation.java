import java.util.Vector;

class Simulation
{
	Vector<Process> masterVector;
	int quantum;
	String printArray[];

	Simulation(Vector<Process> dummy)
	{
		masterVector = dummy;
	}


	// NOT FINISHED
	public void lruFixed() 
	{
		// initialization
		Vector<Process> rQ = cpVector(masterVector);
		Vector<Process> bQ = new Vector<Process>();
		Processor processor = new Processor();
		printArray = new String[masterVector.size()];
		Vector<Vector<Page>> lru = new Vector<Vector<Page>>();
		for(Process p:rQ)
		{
			lru.add(new Vector<Page>());
		}
		getRange(rQ, processor);
		int roundRobin = 0;
		// Run until all processes are finished
		while(rQ.size() > 0 || bQ.size() > 0)
		{
			// reset roundrobin and move first element in ready queue to end of ready queue
			if(roundRobin == 3)
			{
				rQ.add(rQ.get(0));
				rQ.remove(0);
				roundRobin = 0;
			}
			// if ready queue has elements in there
			if(rQ.size() > 0)
			{
				// check if main memory contains first page of process
				if(processor.contains(rQ.firstElement()))
				{
					// if it does,
					// run it. Also moves to end of lru
					processor.run(rQ.firstElement(), lru.get(rQ.firstElement().getIDRank()-1));
					roundRobin++;
					// increment time
					processor.incrTime();
					// if Process has no more pages after processing, remove it from the queue
					if(rQ.firstElement().pages.size() == 0)
					{
						printArray[rQ.firstElement().getIDRank()-1] = rQ.firstElement().getIDRank() + "\t\t"  + (processor.getTime()) +"\t\t\t\t" + rQ.firstElement().getFaultTimes().size() +"\t\t"
						+ rQ.firstElement().getFaultTimesString();
						rQ.remove(0);
						roundRobin=0;
					}
					// increment blocked queue
					incrBQ(bQ, rQ, processor, lru);
				}
				// memory does not contain the page, add to blocked queue and write page fault
				else
				{
					// System.out.println("\n"+rQ.get(0).pages.firstElement().getID()+" has been faulted");
					// System.out.println("Before page fault");
					// processor.print();

					// reset rr
					roundRobin = 0;
					// add page fault at this time
					rQ.get(0).addFault(processor.getTime());
					// add to blocked queue
					bQ.add(rQ.get(0));
					// remove from ready queue
					rQ.remove(0);
					// System.out.println("After page fault");
					// processor.print();

				}
			}
			// rQ.size == 0
			else
			{
				roundRobin = 0;
				// increment time
				processor.incrTime();
				incrBQ(bQ, rQ, processor, lru);
			}
		}//finished while loop
		System.out.println("LRU Fixed:");
		print();
	}

	// increments time for each process in blocked queue
	// if element's blocked time == 6, add to ready queue and add into main memory
	private void incrBQ(Vector<Process> bQ, Vector<Process> rQ, Processor processor, Vector<Vector<Page>> lru)
	{
		// for each process that is blocked
		for(int i=0; i<bQ.size(); i++)
		{
			// increment the time
			bQ.get(i).incrBlockedTime();
			// if blocked time is finished
			if(bQ.get(i).getBlockedTime() == 6)
			{
				// System.out.println(bQ.get(i).pages.firstElement().getID()+" has been added at "+processor.getTime());
				// reset blocked time and add to ready queue
				bQ.get(i).rBlockedTime();
				rQ.add(bQ.get(i));
				// if there is free space for process
				if(processor.freeSpace(bQ.get(i)))
				{
					// occupy that free space
					processor.occupy(bQ.get(i), lru.get(bQ.get(i).getIDRank()-1));
				}
				else
				{
					// otherwise, swap with least recently used
					processor.lruSwap(bQ.get(i), lru.get(bQ.get(i).getIDRank()-1));
				}
				// remove from blocked queue and decrement
				bQ.remove(i);
				i--;
			}
		}
	}

	public void lruVariable()
	{
		Vector<Process> rQ = cpVector(masterVector);
		Vector<Process> bQ = new Vector<Process>();
		Vector<Page> lru = new Vector<Page>();
		Processor processor = new Processor();
		getRangeVariable(rQ, processor);
		int roundRobin = 0;
		// while there are Pages still to be executed
		while(rQ.size()>0 || bQ.size()>0)
		{
			// if roundrobin = 3, remove from head of queue and put to end
			// reset counter
			if(roundRobin == 3)
			{
				rQ.add(rQ.get(0));
				rQ.remove(0);
				roundRobin = 0;
			}
			// if ready queue has elements in there
			if(rQ.size() > 0)
			{
				// check if main memory contains first page of process
				if(processor.contains(rQ.firstElement()))
				{
					// if it does,
					// run it. Also moves to end of lru
					processor.run(rQ.firstElement(), lru);
					roundRobin++;
					// increment time
					processor.incrTime();
					// if Process has no more pages after processing, remove it from the queue
					if(rQ.firstElement().pages.size() == 0)
					{
						// add for printing
						printArray[rQ.firstElement().getIDRank()-1] = rQ.firstElement().getIDRank() + "\t\t"  + (processor.getTime()) +"\t\t\t\t" + rQ.firstElement().getFaultTimes().size() +"\t\t"
														+ rQ.firstElement().getFaultTimesString();
						// free memory and remove from						
						processor.removeFromMemory(rQ.firstElement(), lru);
						rQ.remove(0);
						// reset roundRobin
						roundRobin = 0;
					}
					// increment blocked queue
					incrBQVariable(bQ, rQ, processor, lru);
				}
				// memory does not contain the page, add to blocked queue and write page fault
				// reserve memory space for that page to get access to
				else
				{
					// reset rr
					roundRobin = 0;
					// add page fault at this time
					rQ.get(0).addFault(processor.getTime());
					// Must reserve the slot that it is going to take
					if(processor.freeSpace(rQ.get(0)))
						processor.reserve(rQ.get(0), lru);
					// swap immediately as lru will not be used
					else
						processor.lruSwapVariable(rQ.get(0), lru);
					// add to blocked queue
					bQ.add(rQ.get(0));
					// remove from ready queue
					rQ.remove(0);
				}
			}
			// rQ.size == 0
			else
			{
				roundRobin = 0;
				// increment time
				processor.incrTime();
				incrBQVariable(bQ, rQ, processor, lru);
			}
		}
		System.out.println("\n\nLRU Variable:");
		print();
	}

	private void incrBQVariable(Vector<Process> bQ, Vector<Process> rQ, Processor processor, Vector<Page> lru)
	{
		// for each process that is blocked
		for(int i=0; i<bQ.size(); i++)
		{
			// increment the time
			bQ.get(i).incrBlockedTime();
			// if blocked time is finished
			if(bQ.get(i).getBlockedTime() == 6)
			{
				// reset blocked time and add to ready queue
				bQ.get(i).rBlockedTime();
				rQ.add(bQ.get(i));
				// if there is free space for process
				if(processor.spaceFor(bQ.get(i)))
				{
					// occupy that free space
					processor.occupyVariable(bQ.get(i), lru);
				}
				// remove from blocked queue and decrement
				bQ.remove(i);
				i--;
			}
		}
	}

	public void fixedClock()
	{
		int roundRobin = 0;
		Vector<Process> rQ = cpVector(masterVector);
		Vector<Process> bQ = new Vector<Process>();
		ClockProcessor processor = new ClockProcessor(true);
		processor.setArrow(rQ.size());
		getRangeClock(rQ, processor);
		// Runs while there are still pages that need to be executed
		while(rQ.size() > 0 || bQ.size() > 0)		
		{
			// Move to back of queue
			if(roundRobin == 3)
			{
				rQ.add(rQ.get(0));
				rQ.remove(0);
				roundRobin = 0;
			}
			// If there are things in the ready queue
			if(rQ.size() > 0)
			{
				// If page is in main memory
				if(processor.contains(rQ.firstElement()))
				{
					// execute page in memory
					processor.run(rQ.get(0));
					processor.incrTime();
					roundRobin++;
					// check if Process still has more pages to run
					if(rQ.firstElement().pages.size() == 0)
					{
						// System.out.println("\t" +rQ.get(0).getID()+" has finished");
						// remove from rQ
						printArray[rQ.firstElement().getIDRank()-1] = rQ.firstElement().getIDRank() 
							+ "\t\t"  + (processor.getTime()) +"\t\t\t\t" + rQ.firstElement().getFaultTimes().size() +"\t\t"
							+ rQ.firstElement().getFaultTimesString();
						// remove from ready list
						rQ.remove(0);
						// reset roundrobin
						roundRobin = 0;
					}
					incrBQClockFixed(bQ, rQ, processor);
				}
				else
				{
					// System.out.println("Blocking " + rQ.firstElement().pages.get(0).getID() +"@"+processor.getTime());
					// reset rr
					roundRobin = 0;
					// fault time
					rQ.firstElement().addFault(processor.getTime());
					// add to bq
					bQ.add(rQ.firstElement());
					// remove from rq
					rQ.remove(0);
				}
			}
			// nothing in rQ
			else
			{
				roundRobin = 0;
				processor.incrTime();
				// incrBQClockFixed
				incrBQClockFixed(bQ, rQ, processor);
			}
		} // end of while loop
		System.out.println("\n\nClock fixed:");
		print();
	}

	// Variable allocation global replacement
	public void variableClock()
	{
		Vector<Process> rQ = cpVector(masterVector);
		Vector<Process> bQ = new Vector<Process>();
		ClockProcessor processor = new ClockProcessor(false);
		getRangeClockVariable(rQ, processor);
		int roundRobin = 0;
		// Runs while there are still pages that need to be executed
		while(rQ.size() > 0 || bQ.size() > 0)		
		{
			// Move to back of queue
			if(roundRobin == 3)
			{
				rQ.add(rQ.get(0));
				rQ.remove(0);
				roundRobin = 0;
			}
			// If there are things in the ready queue
			if(rQ.size() > 0)
			{
				// Page needed is in main memory
				if(processor.contains(rQ.firstElement()))
				{
					// run
					processor.run(rQ.firstElement());
					processor.incrTime();
					roundRobin++;
					if(rQ.get(0).pages.size() == 0)
					{
						processor.removeFromMemory(rQ.firstElement());
						// remove from rQ
						printArray[rQ.firstElement().getIDRank()-1] = rQ.firstElement().getIDRank() 
							+ "\t\t"  + (processor.getTime()) +"\t\t\t\t" + rQ.firstElement().getFaultTimes().size() +"\t\t"
							+ rQ.firstElement().getFaultTimesString();
						// remove from ready list
						rQ.remove(0);
						// reset roundrobin
						roundRobin = 0;
					}
					incrBQClockVariable(bQ, rQ, processor);
				}
				else // not in main memory
				{
					System.out.println("fault at " + processor.getTime());
					roundRobin = 0;
					if(processor.freeSpace(rQ.get(0)))
						processor.reserve(rQ.get(0));
					else
						processor.clockSwap(rQ.get(0));
					// fault time
					rQ.firstElement().addFault(processor.getTime());
					// add to bq
					bQ.add(rQ.firstElement());
					// remove from rq
					rQ.remove(0);
				}
			}
			else
			{
				roundRobin = 0;
				processor.incrTime();
				incrBQClockVariable(bQ, rQ, processor);
			}
			processor.print();
		}
		System.out.println("\n\nClock Variable:");
		print();
	}

	//  Increment BQ blocked time for Fixed Clock
	public void incrBQClockFixed(Vector<Process> bQ, Vector<Process> rQ, ClockProcessor processor)
	{
		for(int i=0; i<bQ.size(); i++)
		{
			bQ.get(i).incrBlockedTime();
			if(bQ.get(i).getBlockedTime() == 6)
			{
				// reset blocked time
				bQ.get(i).rBlockedTime();
				// add to ready queue
				rQ.add(bQ.get(i));
				// add to main memory
				if(processor.freeSpace(bQ.get(i)))
					// occupy
					processor.occupy(bQ.get(i));
				else
					processor.clockSwap(bQ.get(i));
				// remove from blocked queue
				bQ.remove(i);
				i--;
			}
		}
	}

	//  Increment BQ for VariableClock
	public void incrBQClockVariable(Vector<Process> bQ, Vector<Process> rQ, ClockProcessor processor)
	{
		for(int i=0; i<bQ.size(); i++)
		{
			bQ.get(i).incrBlockedTime();
			if(bQ.get(i).getBlockedTime() == 6)
			{
				// reset blocked time
				bQ.get(i).rBlockedTime();
				// add to ready queue
				rQ.add(bQ.get(i));
				// add to main memory
				if(processor.spaceFor(bQ.get(i)))
					// occupy
					processor.occupyVariable(bQ.get(i));
				else
					processor.clockSwap(bQ.get(i));
				// remove from blocked queue
				bQ.remove(i);
				i--;
			}
		}
	}

	private void print()
	{
		// print outputs
		System.out.println("PID  Turnaround Time  # Faults  Fault times");
		for(int i=0;i<masterVector.size();i++)
		{
			System.out.println(printArray[i]);
		}
	}


	// deep copy of vector
	private Vector<Process> cpVector(Vector<Process> master)
	{
		Vector<Process> returnVector = new Vector<Process>();
		for(Process i: master)
		{
			returnVector.add(new Process(i));
		}
		return returnVector;
	}

	

	// For each Process in master, set the range which the Process can enter in the main memory
	// Only used for fixed
	private void getRange(Vector<Process> master, Processor processor)
	{
		int i = 0;
		for(int j=0; j<master.size();j++)
		{
			master.get(j).memoryRange.add(i);
			i+= Math.floor(processor.MEMORYSIZE/master.size()-1);
			master.get(j).memoryRange.add(i);
			i++;
			// System.out.println(j+"'s range: "+master.get(j).memoryRange.firstElement() + " to " +master.get(j).memoryRange.get(1));
		}
	}

	private void getRangeClock(Vector<Process> master, ClockProcessor processor)
	{
		int i = 0;
		for(int j=0; j<master.size();j++)
		{
			master.get(j).memoryRange.add(i);
			i+= Math.floor(processor.MEMORYSIZE/master.size()-1);
			master.get(j).memoryRange.add(i);
			i++;
			// System.out.println(j+"'s range: "+master.get(j).memoryRange.firstElement() + " to " +master.get(j).memoryRange.get(1));
		}
	}

	private void getRangeVariable(Vector<Process> vector, Processor processor)
	{
		for(Process p: vector)
		{
			p.memoryRange.add(0);
			p.memoryRange.add(processor.MEMORYSIZE-1);
		}
	}

	private void getRangeClockVariable(Vector<Process> rQ, ClockProcessor processor)
	{
		for(Process p: rQ)
		{
			p.memoryRange.add(0);
			p.memoryRange.add(processor.MEMORYSIZE-1);
		}
	}
}