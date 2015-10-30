import java.util.Vector;

class Simulation
{
	Processor processor;
	Vector<Process> masterVector;
	int quantum;
	String printArray[];

	Simulation(Vector<Process> dummy)
	{
		processor = new Processor();
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
					processor.run(rQ.firstElement(), lru);
					roundRobin++;
					// if Process has no more pages after processing, remove it from the queue
					if(rQ.firstElement().pages.size() == 0)
					{
						printArray[rQ.firstElement().getIDRank()-1] = rQ.firstElement().getIDRank() + "\t\t"  + (processor.getTime()+1) +"\t\t\t\t" + rQ.firstElement().getFaultTimes().size() +"\t\t"
						+ rQ.firstElement().getFaultTimesString();
						rQ.remove(0);
					}
					// increment time
					processor.incrTime();
					// increment blocked queue
					incrBQ(bQ, rQ, processor, lru);
					processor.print();
				}
				// memory does not contain the page, add to blocked queue and write page fault
				else
				{
					// reset rr
					roundRobin = 0;
					// add page fault at this time
					rQ.get(0).addFault(processor.getTime());
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
				incrBQ(bQ, rQ, processor, lru);
				processor.print();
			}
		}//finished while loop
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
				// reset blocked time and add to ready queue
				bQ.get(i).rBlockedTime();
				rQ.add(bQ.get(i));
				System.out.println(bQ.firstElement().pages.firstElement().getID()+" added ");
				// if there is free space for process
				if(processor.freeSpace(bQ.get(i)))
				{
					// occupy that free space
					processor.occupy(bQ.get(i), lru);
				}
				else
				{
					// otherwise, swap with least recently used
					processor.lruSwap(bQ.get(i), lru);
				}
				// remove from blocked queue and decrement
				bQ.remove(i);
				i--;
			}
		}
	}

	public void variableLRU()
	{
		Vector<Process> rQ = cpVector(masterVector);
		Vector<Process> bQ = new Vector<Process>();
		int roundRobin = 0;
		Processor processor = new Processor();
		while(rQ.size()>0 || bQ.size()>0)
		{
			if(rQ.size()>0)
			{

			}
			else
			{
				// increment time, 
				incrBQ(bQ, rQ, )
			}
		}
	}

	// sim.fixedClock();

	// Variable allocation global replacement
	public void variableClock()
	{
		int roundRobin = 0;
		Vector<Process> rQ = cpVector(masterVector);
		Vector<Process> bQ = new Vector<Process>();
		// circular linked list
		CircLinkedList cLL = new CircLinkedList();
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
			// if there is anything in the ready queue
			if(rQ.size() > 0)
			{
				// memory contains the first element we want to run from pages
				if(cLL.contains(rQ.firstElement().pages.firstElement()))
				{
					// run
					cLL.run(rQ.firstElement());
					roundRobin++;
					// if rQ's first element no longer has any pages left, remove
					if(rQ.firstElement().pages.size() == 0)
					{
						printArray[rQ.firstElement().getIDRank()-1] = rQ.firstElement().getIDRank() + "\t\t" + (processor.getTime()+1)+"\t\t\t\t"
							+rQ.firstElement().getFaultTimes().size()+"\t\t"+rQ.firstElement().getFaultTimesString();
						rQ.remove(0);
					}
					// increment time
					cLL.incrTime();
					// System.out.println("Time incremented: "+cLL.getTime());
					// increment blocked queue time
					for(int i=0; i<bQ.size(); i++)
					{
						// increment blocked time for i
						bQ.get(i).incrBlockedTime();
						if(bQ.get(i).getBlockedTime() == 6)
						{
							// reset blocked time
							bQ.get(i).rBlockedTime();
							// add to ready queue
							rQ.add(bQ.get(i));
							// if 'processor' is not full
							if(cLL.size()<30)
							{
								cLL.add(new Node(bQ.get(i).pages.firstElement()));
							}
							// size is full
							else 
							{
								cLL.swap(bQ.get(i).pages.firstElement());
							}
							// remove from blocked queue
							bQ.remove(i);
							// set counter back to not miss any processes
							i--;
						}
					}
				}
				// next page to run is not in main memory
				else
				{
					roundRobin = 0;
					// System.out.println(rQ.firstElement().getID()+" with " 
						// + rQ.firstElement().pages.firstElement().getID() + " has been blocked at "+cLL.getTime());
					rQ.firstElement().addFault(cLL.getTime());
					bQ.add(rQ.firstElement());
					rQ.remove(0);
				}
			}
			// no processes in rQ
			else
			{
				roundRobin = 0;
				//increment time
				cLL.incrTime();
				// System.out.println("Time incremented: "+cLL.getTime());
				bQ.firstElement().incrBlockedTime();
				// first element ready to add to ready queue
				if(bQ.firstElement().getBlockedTime() == 6)
				{
					bQ.firstElement().rBlockedTime();
					rQ.add(bQ.get(0));
					// add to main memory
					if(cLL.size()<30)
					{
						cLL.add(new Node(bQ.firstElement().pages.firstElement()));
					}
					// size is full
					else 
					{
						cLL.swap(bQ.firstElement().pages.firstElement());
					}
					bQ.remove(0);
					for(int i=0;i<bQ.size();i++)
					{
						bQ.get(i).incrBlockedTime();
						// if blocked time equals 6
						if(bQ.get(i).getBlockedTime() == 6)
						{
							// reset blocked time
							bQ.get(i).rBlockedTime();
							// add to ready queue
							rQ.add(bQ.get(i));
							// add to processor
							if(cLL.size()<30)
							{
								cLL.add(new Node(bQ.get(i).pages.firstElement()));
							}
							// size is full
							else 
							{
								cLL.swap(bQ.get(i).pages.firstElement());
							}
							bQ.remove(i);
							i--;
						}
					}
				}
				else
				{
					for(int i=1;i<bQ.size();i++)
					{
						// increment blocked time
						bQ.get(i).incrBlockedTime();
						// if blocked time now equals 6
						if(bQ.get(i).getBlockedTime() == 6)
						{
							// reset blocked time
							bQ.get(i).rBlockedTime();
							// add to ready queue
							rQ.add(bQ.get(i));
							// add to processor
							if(cLL.size()<30)
							{
								cLL.add(new Node(bQ.get(i).pages.firstElement()));
							}
							// size is full
							else 
							{
								cLL.swap(bQ.get(i).pages.firstElement());
							}
							bQ.remove(i);
							i--;
						}
					}
				}
			}
			cLL.print();

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
			i+= Math.floor(30/master.size()-1);
			master.get(j).memoryRange.add(i);
			i++;
			// System.out.println(j+"'s range: "+master.get(j).memoryRange.firstElement() + " to " +master.get(j).memoryRange.get(1));
		}
	}

}