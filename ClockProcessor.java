import java.util.Vector;

class ClockProcessor
{
	public static final int MEMORYSIZE = 10;
	private boolean isFixed;
	private boolean multFullBefore[];
	private Frame frames[] = new Frame[MEMORYSIZE];
	private int time, arrow;
	private int multArrow[];

	ClockProcessor(boolean fixed)
	{
		isFixed = fixed;
		// FULLBEFORE make mutator methods for this - used for clock variable
		// if true, do nothing. if false, set true
		for(int i = 0; i<MEMORYSIZE; i++)
		{
			frames[i] = new Frame("frame"+i);
		}
		time = 0;
		if(!isFixed)
			arrow = -1;
	}


	// QUERY METHODS
	public int getTime()
	{
		return time;
	}

	// returns true if there is free space for this 
	public boolean freeSpace(Process p)
	{
		for(int i=p.memoryRange.get(0).intValue();i<= p.memoryRange.get(1).intValue(); i++)
		{
			if(!frames[i].isOccupied() && !frames[i].isReserved())
				return true;
		}
		return false;
	}

	public boolean contains(Process p)
	{
		for(int i=p.memoryRange.get(0).intValue();i<= p.memoryRange.get(1).intValue(); i++)
		{
			if(frames[i].isOccupied())
			{
				if(frames[i].getHolding().equals(p.pages.firstElement()))
					return true;
			}
		}
		return false;
	}

	// returns index where there is a first occupied frame
	public int firstOccupied()
	{
		if(occupied() > 0)
		{
			int i=0;
			while(i<MEMORYSIZE)
			{
				if(frames[i].isOccupied())
					break;
				i++;
			}
			return i;
		}
		else
			return -1;
	}

	// returns how many frames have been occupied
	public int occupied()
	{
		int occupied=0;
		for(int i=0; i<MEMORYSIZE; i++)
		{
			if(frames[i].isOccupied())
				occupied++;
		}
		return occupied;
	}

	// MUTATOR METHODS
	public void occupy(Process p)
	{
		// System.out.println("adding "+p.pages.firstElement().getID()+"@"+getTime());
		for(int i=p.memoryRange.get(0).intValue(); i <= p.memoryRange.get(1).intValue();i++)
		{
			if(!frames[i].isOccupied() && !frames[i].isReserved())
			{
				frames[i].holdPage(p.pages.get(0));
				break;
			}
		}
		// Add to lru and check if memory space for process is full
		// if memory space is full, move arrow to start of memory space for process
		if(isFixed)
		{
			int index = (p.getIDRank()-1);
			// if no more free space there, move arrow to starting value
			if(!freeSpace(p))
			{
				if(!multFullBefore[p.getIDRank()-1])
				{
					multArrow[index] = p.memoryRange.get(0).intValue();
					multFullBefore[p.getIDRank()-1] = true;
				}
				else
				{
					incrArrow(p);
				}
			}
		}
		// NOT WORKING PROPERLY (PROBABLY)
		else
		{
			// if memory is full, move arrow to 0
		}
	}

	public void clockSwap(Process p)
	{
		// System.out.println("Trying to swap in "+ p.pages.firstElement().getID() + "@"+getTime());
		// System.out.println("Already in memory:");
		// print();
		// If it is isFixed clock
		if(isFixed)
		{
			int index = multArrow[p.getIDRank()-1];
			// System.out.println("Index: " + index);
			// System.out.println("arrow pointing at" + frames[index].getHolding().getID());
			// check current frame and move to next
			while(frames[index].hasSecondChance())
			{
				frames[index].takeSecondChance();
				incrArrow(p);
			}
			// System.out.println("swapped " + frames[index].getHolding().getID());
			frames[index].holdPage(p.pages.firstElement());
		}
		else
		{

		}
	}

	public void run(Process p)
	{
		// System.out.println("running " + p.pages.firstElement().getID() + "@"+getTime());
		// give star
		for(int i=0; i<MEMORYSIZE; i++)
		{
			if(frames[i].isOccupied())
			{
				if(frames[i].getHolding().equals(p.pages.firstElement()))
				{
					frames[i].giveSecondChance();
					break;
				}
			}
		}
		p.pages.remove(0);
	}

	public void incrTime()
	{
		time++;
	}

	private void incrArrow(Process p)
	{
		if(!isFixed)
		{
			arrow++;
			if(arrow == MEMORYSIZE)
				arrow = 0;
		}
		else
		{
			int index = p.getIDRank()-1;
			multArrow[index]++;
			if(multArrow[index] > p.memoryRange.get(1).intValue())
				multArrow[index] = p.memoryRange.get(0).intValue();
		}
	}
	
	// Only used for LRUFixed
	public void setArrow(int size)
	{
		multArrow = new int[size];
		multFullBefore = new boolean[size];
		for(int i=0; i<size; i++)
		{
			multArrow[i] = -1;
			multFullBefore[i] = false;
		}
	}

	// Prints taken frames
	public void print()
	{
		// System.out.println("MEMORY at "+getTime());
		if(occupied() > 1)
		{
			String s = "{" + frames[firstOccupied()].getHolding().getID();
			for(int i=firstOccupied()+1; i<MEMORYSIZE; i++)
			{
				if(frames[i].isOccupied())
				{
					s+= ", " + frames[i].getHolding().getID();
				}
			}
			s+="}";
			System.out.println(s);
		}
		else if(occupied() == 2)
		{

		}
		else if(occupied() == 1)
			System.out.println("{"+frames[firstOccupied()].getHolding().getID()+"}");
		else
		{
			System.out.println("{}");
		}
	}
}