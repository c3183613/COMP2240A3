import java.util.Vector;

class Processor
{
	static final int MEMORYSIZE = 30;
	// Processor has 30 frames of main memory
	Frame[] frames = new Frame[MEMORYSIZE];
	// Measures time
	private int time;

	Processor()	
	{
		time = 0;
		for(int i=0; i<MEMORYSIZE; i++)
		{
			frames[i] = new Frame("frame"+i);
		}
	}

	// Increments time by 1
	public void incrTime()
	{
		time++;
	}

	// Returns time
	public int getTime()
	{
		return time;
	}

	// Returns true if main memory contains first page of process
	public boolean contains(Process p)
	{
		for(int i=p.memoryRange.firstElement().intValue(); i<=p.memoryRange.get(1).intValue(); i++)
		{
			if(frames[i].isOccupied())
			{
				if(frames[i].getHolding().equals(p.pages.firstElement()))
					return true;
			}
		}
		return false;
	}

	// Returns true if there is free space within process' allocated memory
	public boolean freeSpace(Process p)
	{
		for(int i=p.memoryRange.firstElement().intValue(); i<=p.memoryRange.get(1).intValue(); i++)
		{
			// if it's not occupied, then there is free space
			if(!frames[i].isOccupied() && !frames[i].isReserved())
				return true;
		}
		return false;
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

	// Occupies a free space within process' allocated memory slots
	// adds to lru
	// Input: process and lru vector
	public void occupy(Process p, Vector<Page> lru)
	{
		// System.out.println(p.pages.firstElement().getID() + " has been added");
		// add to end of lru
		lru.add(p.pages.firstElement());
		// take free space
		for(int i=p.memoryRange.firstElement().intValue();i<=p.memoryRange.get(1).intValue();i++)
		{
			if(!frames[i].isOccupied())
			{
				// take the space
				frames[i].holdPage(p.pages.firstElement());
				break;
			}
		}
	}

	// 
	public void occupyVariable(Process p, Vector<Page> lru)
	{
		for(int i=0; i<MEMORYSIZE; i++)
		{
			if(frames[i].getReservedFor() == p.pages.firstElement())
			{
				frames[i].holdPage(p.pages.firstElement());
				frames[i].resetReserved();
				break;
			}
		}
		lru.add(p.pages.firstElement());
		// System.out.println(lru.get(lru.size()-1).getID() + " has been added to lru and mainmemory");
	}

	public void removeFromMemory(Process p, Vector<Page> lru)
	{
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
		// check each Page in lru
		for(int i=0; i<lru.size(); i++)
		{
			// if page in lru equals p's getIDRank
			String s[] = lru.get(i).getID().split("_");
			if(p.getIDRank() == Integer.parseInt(s[0]))
			{
				lru.remove(i);
				i--;
			}
		}
	}

	// Swaps the first page of process for least recently used page
	// Input: Process and lru
	public void lruSwap(Process p, Vector<Page> lru)
	{
		// System.out.println("swapping " +lru.get(0).getID()+" with " + p.pages.firstElement().getID());
		// find and swap
		for(int i=p.memoryRange.firstElement().intValue(); i<=p.memoryRange.get(1).intValue(); i++)
		{
			if(frames[i].getHolding().equals(lru.get(0)))
			{
				frames[i].holdPage(p.pages.firstElement());
				break;
			}
		}
		// remove
		lru.remove(0);
		// remove any prior instances of recently used page
		for(int i=0; i<lru.size();i++)
		{
			if(lru.get(i).equals(p.pages.firstElement()))
			{
				lru.remove(i);
			}
		}
		// add to end of lru
		lru.add(p.pages.firstElement());
	}

	public void lruSwapVariable(Process p, Vector<Page> lru)
	{
		// System.out.println("About to swap " + p.pages.firstElement().getID() + " with " + lru.get(0).getID());
		for(int i=p.memoryRange.firstElement().intValue(); i<=p.memoryRange.get(1).intValue(); i++)
		{
			if(frames[i].getHolding().equals(lru.get(0)))
			{
				// swap
				frames[i].holdPage(p.pages.firstElement());
				// reset reserved
				frames[i].resetReserved();
				break;
			}
		}
		lru.remove(0);
		// remove any prior instances of recently used page
		for(int i=0; i<lru.size();i++)
		{
			if(lru.get(i).equals(p.pages.firstElement()))
			{
				lru.remove(i);
				break;
			}
		}
		// add to end of lru
		lru.add(p.pages.firstElement());
	}

	// run page, remove any previous instances of page in lru
	// add page to end of lru
	public void run(Process p, Vector<Page> lru)
	{
		// execute page
		// p.pages.firstElement().execute(getTime());
		
		// move page instance in lru to end of lru
		for(int i=0;i<lru.size();i++)
		{
			if(lru.get(i).equals(p.pages.firstElement()))
			{
				lru.remove(i);
				break;
			}
		}
		lru.add(p.pages.firstElement());
		// remove page from process
		p.pages.remove(0);
	}

	// Reserves the frame which p's page will be swapped into
	public void reserve(Process p, Vector<Page> lru)
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
					break;
				}
			}
		}
		else
		{

			// System.out.println(lru.get(0).getID() + " to be swapped with "+p.pages.firstElement().getID());
			// // find lru in frames and then reserve it for this process' page
			// for(int i=0; i<MEMORYSIZE; i++)
			// {
			// 	if(frames[i].isOccupied())
			// 	{
			// 		if(frames[i].getHolding().equals(lru.get(0)))
			// 		{
			// 			frames[i].reserveFor(p.pages.firstElement());
			// 			lru.add(lru.get(0));
			// 			lru.remove(0);
			// 			// just added this
			// 		}
			// 	}
			// }
		}
	}


	// returns where the index of the first taken memory is
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