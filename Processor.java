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
		for(int i=0; i<MEMORYSIZE; i++)
		{
			if(frames[i].equals(p.pages.firstElement()))
				return true;
		}
		return false;
	}

	// Returns true if there is free space within process' allocated memory
	public boolean freeSpace(Process p)
	{
		for(int i=p.memoryRange.firstElement().intValue(); i<=p.memoryRange.get(1).intValue(); i++)
		{
			if(!frames[i].isOccupied())
				return true;
		}
		return false;
	}

	// Occupies a free space within process' allocated memory slots
	// adds to lru
	// Input: process and lru vector
	public void occupy(Process p, Vector<Page> lru)
	{
		// remove any other instances from lru
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

	// Swaps the first page of process for least recently used page
	// Input: Process and lru
	public void lruSwap(Process p, Vector<Page> lru)
	{
		// replace
		for(int i=p.memoryRange.firstElement().intValue();i<=p.memoryRange.get(1).intValue();i++)
		{
			if(frames[i].equals(lru.firstElement()))
			{
				frames[i].holdPage(p.pages.firstElement());
				break;
			}
		}
		// remove lru(0)
		lru.remove(0);
		// remove any other instances of page
		for(int i=0; i<lru.size(); i++)
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

	// 
	public void run(Process p, Vector<Page> lru)
	{
		// execute page
		p.pages.firstElement().execute(getTime());
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
}