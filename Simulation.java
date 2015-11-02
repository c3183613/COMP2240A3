import java.util.Vector;

class Simulation
{
	Processor processor;
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
		processor = new Processor();
		printArray = new String[masterVector.size()];
		Vector<Vector<Page>> lru = new Vector<Vector<Page>>();
		for(Process p:rQ)
		{
			lru.add(new Vector<Page>());
		}
		getRange(rQ);
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
		processor = new Processor();
		getRangeVariable(rQ);
		int roundRobin = 0;
		// while there are Pages still to be executed
		while(rQ.size()>0 || bQ.size()>0)
		{
			// if roundrobin = 3, remove from head of queue and most to end
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
						// System.out.println(rQ.get(0).getID()+"'s pages removed from lru and main memory");
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
					// test
					// System.out.println(rQ.get(0).pages.firstElement().getID() + " fault at " + processor.getTime());
					// System.out.println("Before fault:");
					// processor.print();
					// reset rr
					roundRobin = 0;
					// add page fault at this time
					rQ.get(0).addFault(processor.getTime());
					// Must reserve the slot that it is going to take
					if(processor.freeSpace(rQ.get(0)))
						processor.reserve(rQ.get(0), lru);
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

	public void getRangeVariable(Vector<Process> vector)
	{
		for(Process p: vector)
		{
			p.memoryRange.add(0);
			p.memoryRange.add(processor.MEMORYSIZE-1);
		}
	}

	// sim.fixedClock();

	// Variable allocation global replacement
	public void variableClock()
	{
		int roundRobin = 0;
		Vector<Process> rQ = cpVector(masterVector);
		Vector<Process> bQ = new Vector<Process>();
		getRangeVariable(rQ);
		// circular linked list
		CircLinkedList cLL = new CircLinkedList(30);
		// While there are Processes that have not finished
		while(rQ.size() > 0 || bQ.size() > 0)
		{
			// each process can have 3 ticks of cpu
			if(roundRobin == 3)
			{
				rQ.add(rQ.get(0));
				rQ.remove(0);
				roundRobin = 0;
			}
			if(rQ.size()>0)
			{

			}
			else
			{
				cLL.incrTime();
				incrBQClock(bQ, rQ, cLL);
			}
			

		} // end of while loop
		// if circular linked list size < 30 and new page not already in queue
			// add it in
		// else if in list
			// give star back
		// else (only other condition is not in list)
			// swap
		System.out.println("Clock - Variable:");
		print();
	}

	public void incrBQClock(Vector<Process> bQ, Vector<Process> rQ, CircLinkedList cLL)
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
				if(cLL.freeSpace(bQ.get(i)))
				{
					// occupy
					// occupy.
				}
				else
				{

				}
				// remove from blocked queue
			}
		}
	}

	private void print()
	{
		// String temp[];
		System.out.println("PID  Turnaround Time  # Faults  Fault times");
		for(int i=0;i<masterVector.size();i++)
		{
			System.out.println(printArray[i]);
		}
	}


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
	public void getRange(Vector<Process> master)
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

}