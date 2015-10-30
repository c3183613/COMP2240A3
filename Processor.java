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

	// If any Frame in frames is currently holding p, return true
	// else, return false	
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
		// Otherwise, return false
		return false;
	}

	public boolean freeSpace()
	{
		for(int i=0; i<MEMORYSIZE; i++)
		{
			if(frames[i].isOccupied())
			{
				return true;
			}
		}
		return false;
	}

	// If there is free space for p's page within its boundaries, return true
	// else, return false
	public boolean freeSpace(Process p)
	{
		for(int i=p.memoryRange.firstElement().intValue() ; i<=p.memoryRange.get(1).intValue(); i++)
		{
			if(!frames[i].isOccupied())
			{
				return true;
			}
		}
		return false;
	}

	// let p occupy a free space in main memory within its allocated memory range
	// public void occupy(Process p)
	// {
	// 	for(int i=p.memoryRange.firstElement().intValue(); i<p.memoryRange.get(1).intValue();i++)
	// 	{
	// 		// If frames[i] is not occupied
	// 		if(!frames[i].isOccupied())
	// 		{
	// 			// hold first page in queue
	// 			frames[i].holdPage(p.pages.firstElement());
	// 			// pop from queue
	// 			p.lastRemoved = p.pages.firstElement();
	// 			p.pages.remove(p.pages.firstElement());
	// 			break;
	// 		}
	// 	}
	// }

	// let p's first page occupy a free space in main memory within its allocated memory range
	public void occupy(Process p, Vector<Vector<Page>> lru)
	{
		// keeps me from writing it out every time
		// lru index is which first dimension of the lru index it is in
		Vector<Page> lruIndex = lru.get(p.getIDRank()-1);
		// Make sure there is only one instance of the page in the lru queue
		// if there is an instance of it in there, remove it
		for(int j=0; j<lruIndex.size();j++)
		{
			// if it is in there, remove it
			if(lruIndex.get(j).equals(p.pages.firstElement()))
			{
				// remove it.
				lruIndex.remove(j);
			}
		}
		// add it to the end
		lruIndex.add(p.pages.firstElement());
		// Finds first free Frame and occupies it
		for(int i=p.memoryRange.firstElement().intValue(); i<=p.memoryRange.get(1).intValue();i++)
		{
			// If frames[i] is not occupied
			if(!frames[i].isOccupied())
			{
				// hold first page in queue
				frames[i].holdPage(p.pages.firstElement());
				// add to end of lru
				// break to make sure it only takes 1
				break;
			}
		}
	}

	// Swap least recently used process
	public void lruSwap(Process p, Vector<Vector<Page>> lru)
	{
		System.out.println(lru.get(p.getIDRank()-1).get(0).getID()+" is getting swapped for "+p.pages.firstElement().getID());
		// swap with lru
		for(int i=p.memoryRange.firstElement().intValue(); i<= p.memoryRange.get(1).intValue();i++)
		{
			if(frames[i].getHolding().equals(lru.get(p.getIDRank()-1).firstElement()))
			{
				frames[i].holdPage(p.pages.firstElement());
				break;
			}
		}
		// remove lru 0
		lru.get(p.getIDRank()-1).remove(0);
		// remove any instances of first page in there
		for(int i=0; i<lru.get(p.getIDRank()-1).size(); i++)
		{
			if(lru.get(p.getIDRank()-1).get(i).equals(p.pages.firstElement()))
			{
				lru.get(p.getIDRank()-1).remove(i);
				i--;
			}
		}
		// add to lru with p's first page element
		lru.get(p.getIDRank()-1).add(p.pages.firstElement());
	}

	// execute the page, remove it from the front of p's vector, add it to the end of lru
	public void run(Process p, Vector<Vector<Page>> lru)
	{
		// shorthand what i am writing all the time
		Vector<Page> lruIndex = lru.get(p.getIDRank()-1);
		// find page and move it to the back
		for(int i=0;i<lru.get(p.getIDRank()-1).size(); i++)
		{
			// if found
			if(lru.get(p.getIDRank()-1).get(i).equals(p.pages.firstElement()))
			{
				lru.get(p.getIDRank()-1).remove(i);
				lru.get(p.getIDRank()-1).add(p.pages.firstElement());
			}
		}
		p.pages.firstElement().execute(getTime());
		p.lastRemoved = p.pages.firstElement();
		p.pages.remove(0);
	}

	public int getOccupied()
	{
		int j=0;
		for(int i=0; i<MEMORYSIZE; i++)
		{
			if(frames[i].isOccupied())
				j++;
		}
		return j;
	}

	public void print()
	{
		System.out.println("\nCurrently in processor at "+getTime());
		String s="{";
		for(int i=0; i<MEMORYSIZE; i++)
		{
			if(frames[i].isOccupied())
				s+=", "+frames[i].getHolding().getID();
		}
		s+="}";
		System.out.println(s);
	}
}