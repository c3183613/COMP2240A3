import java.util.Vector;

class ClockProcessor
{
	public static final int MEMORYSIZE = 30;
	private Frame frames[];
	private int time, arrow;
	private int multArrow[];
	Vector<Page> lru;

	ClockProcessor()
	{
		frames = new Frame[30];
		time = 0;
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
			if(frames[i].equals(p.pages.firstElement()))
				return true;
		}
		return false;
	}

	// MUTATOR METHODS
	public void occupy(Process p)
	{
		for(int i=p.memoryRange.get(0).intValue(); i <= p.memoryRange.get(1).intValue();i++)
		{
			if(!frames[i].isOccupied() && !frames[i].isReserved())
			{
				frames[i].holdPage(p.pages.get(0));
				break;
			}
		}
		for(int i=0; i<lru.size();i++)
		{
			if(lru.get(i).equals(p.pages.firstElement()))
			{
				lru.remove(i);
				break;
			}
		}
		// add to lru
		lru.add(p.pages.get(0));
	}

	public void run(Process p)
	{
		// remove page from lru
		// move to end of lru
		// remove page
	}

	public void incrTime()
	{
		time++;
	}
	
	public void setArrow(int size)
	{
		multArrow = new int[size];
	}
}