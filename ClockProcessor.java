import java.util.Vector;

class ClockProcessor
{
	public static final int MEMORYSIZE = 30;
	private boolean isFixed, fullBefore;
	private boolean multFullBefore[];
	private Frame frames[] = new Frame[MEMORYSIZE];
	private int time, arrow;
	private int multArrow[];

	ClockProcessor(boolean thisvalue)
	{
		fullBefore = false;
		isFixed = thisvalue;
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

	// Used to see if there is a spot reserved for process
	public boolean spaceFor(Process p)
	{
		for(int i=p.memoryRange.firstElement().intValue(); i<=p.memoryRange.get(1).intValue(); i++)
		{
			if(frames[i].isReservedFor(p.pages.firstElement()))
				return true;
		}
		return false;
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
				frames[i].giveSecondChance();
				break;
			}
		}
		// Add to lru and check if memory space for process is full
		// if memory space is full, move arrow to start of memory space for process
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
				incrArrow(p);
		}
	}

	public void occupyVariable(Process p)
	{
		// System.out.println("adding "+p.pages.firstElement().getID()+"@"+getTime());
		for(int i=p.memoryRange.get(0).intValue(); i <= p.memoryRange.get(1).intValue();i++)
		{
			if(frames[i].isReservedFor(p.pages.firstElement()))
			{
				frames[i].holdPage(p.pages.get(0));
				frames[i].resetReserved();
				break;
			}
		}
		// Add to lru and check if memory space for process is full
		// if memory space is full, move arrow to start of memory space for process
		int index = (p.getIDRank()-1);
		// if no more free space there, move arrow to starting value
		if(!freeSpace(p))
		{
			if(!fullBefore)
			{
				arrow = p.memoryRange.get(0).intValue();
				fullBefore = true;
			}
			else
				incrArrow(p);
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
			// frames[index].giveSecondChance();
			// incrArrow(p);
		}
		else
		{
			while(frames[arrow].hasSecondChance())
			{
				frames[arrow].takeSecondChance();
				incrArrow(p);
			}
			System.out.println("swap in " +p.pages.firstElement().getID() +" for " + frames[arrow].getHolding().getID()
					+ " at " + getTime());
			frames[arrow].holdPage(p.pages.firstElement());
			frames[arrow].giveSecondChance();
			// incrArrow(p);
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

	// Reserves the frame which p's page will be swapped into
	public void reserve(Process p)
	{
		if(freeSpace(p))
		{
			// If the frame is neither occupied nor reserved, reserve it for this process' page
			for(int i=0; i<MEMORYSIZE; i++)
			{
				if(!frames[i].isOccupied() && !frames[i].isReserved())
				{
					// System.out.println( i + " is reserved for " + p.pages.firstElement().getID());
					frames[i].reserveFor(p.pages.firstElement());
					frames[i].giveSecondChance();
					break;
				}
			}
		}
	}

	public void removeFromMemory(Process p)
	{
		// check each occupied frame in main memory, if it belongs to p, remove it
		for(int i=0; i<MEMORYSIZE; i++)
		{
			if(frames[i].isOccupied())
			{
				String s[] = frames[i].getHolding().getID().split("_");
				int pageInt = Integer.parseInt(s[0]);
				if(pageInt == p.getIDRank())
					frames[i].resetOccupied();
			}
		}
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
			if(frames[firstOccupied()].hasSecondChance())
				s+="*";
			for(int i=firstOccupied()+1; i<MEMORYSIZE; i++)
			{
				if(frames[i].isOccupied())
				{
					s+= ", " + frames[i].getHolding().getID();
				}
				if(frames[i].isReserved())
					s+=",[res]";
				if(frames[i].hasSecondChance())
					s+="*";
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