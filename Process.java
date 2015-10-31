import java.util.Vector;

class Process
{
	/*
		servTime - remaining process time required to complete
		execTime - total ticks required to complete
		arrivalTime - time that process accesses system
		waitTime - time waited in queues after arrival time
		procID - unique identifier for the process
		faultTimes - 
	*/
	private int arrivalTime, execTime, waitTime, servTime, idRank, blockedTime;
	private String procID;
	Vector<Integer> memoryRange;
	public Vector<Page> pages;
	public Page lastRemoved;
	private Vector<Integer> faultTimes;
	State state;

	// Constructors
	Process(){};

	// Used for assignment 3
	Process(String newProcName)
	{
		procID = newProcName;
		arrivalTime = 0;
		execTime = -1;
		servTime = -1;
		waitTime = 0;
		faultTimes = new Vector<Integer>();
		state = State.READY;
		String temp = "";
		memoryRange = new Vector<Integer>(2);
		pages = new Vector<Page>();
		blockedTime = 0;
		// Used for sort id
		idRank = Integer.parseInt(procID.substring(7, procID.indexOf(".")));
		System.out.println(procID+"'s idRank: "+idRank);
	}

	Process(String newProcID, int newArrivalTime, int newExecTime)
	{
		procID = newProcID;
		arrivalTime = newArrivalTime;
		execTime = newExecTime;
		servTime = newExecTime;
		faultTimes = new Vector<Integer>();
		state = State.READY;
		waitTime = 0;
		String temp = "";
		memoryRange = new Vector<Integer>(2);
		blockedTime = 0;
		// Used for sort id
		if(procID.length() > 2)
		{
			for(int i=0; i<procID.length()-1; i++)
			{
				temp += Character.toString(procID.charAt(i));
			}
		}
		else
			idRank = Integer.parseInt(Character.toString(procID.charAt(1)));
	}

	Process(Process p)
	{
		this.execTime = p.execTime;
		this.arrivalTime = p.arrivalTime;
		this.procID = p.procID;
		this.servTime = p.servTime;
		this.waitTime = p.waitTime;
		pages = new Vector<Page>();
		for(Page pp: p.pages)
		{
			this.pages.add(new Page(pp));
		}
		memoryRange = new Vector<Integer>(2);
		for(Integer i: p.memoryRange)
		{
			this.memoryRange.add(i);
		}
		faultTimes = new Vector<Integer>();
		state = State.READY;
		String temp = "";
		this.idRank = p.idRank;
	}


	// Query methods
	public int getIDRank()
	{
		return idRank;
	}

	public int getArrivalTime()
	{
		return arrivalTime;
	}

	public int getExecTime()
	{
		return execTime;
	}

	public int getWaitTime()
	{
		return waitTime;
	}

	public String getID()
	{
		return procID;
	}

	public int getServTime()
	{
		return servTime;
	}

	public int getBlockedTime()
	{
		return blockedTime;
	}

	public String getFaultTimesString()
	{
		if(faultTimes.size() == 0)
			return "{}";
		if(faultTimes.size() == 1)
			return "{"+faultTimes.firstElement()+"}";
		else
		{
			String s = "{" + faultTimes.firstElement();
			for(int i=1; i<faultTimes.size()-1; i++)
			{
				s+=", "+faultTimes.get(i);
			}
			s+=", "+faultTimes.get(faultTimes.size()-1)+"}";
			return s;
		}
	}

	public Vector<Integer> getFaultTimes()
	{
		return faultTimes;
	}

	public State getState()
	{
		return state;
	}

	//	 Mutator methods
	public void setArrivalTime(int newArrivalTime)
	{
		arrivalTime = newArrivalTime;
	}

	public void setExecTime(int newExecTime)
	{
		execTime = newExecTime;
	}

	public void setID(String newProcID)
	{
		procID = newProcID;
	}

	public void setWaitTime(int newWaitTime)
	{
		waitTime = newWaitTime;
	}

	public void setServTime(int newServTime)
	{
		servTime = newServTime;
	}

	public void decrServTime(int newServTime)
	{
		servTime--;
	}

	public void rBlockedTime()
	{
		blockedTime = 0;
	}

	public void incrBlockedTime()
	{
		blockedTime++;
	}

	public void setState(State newState)
	{
		state = newState;
	}

	// Comparison method
	// Returns true if other is equal to this Process
	// Returns false otherwise
	public boolean equals(Process other)
	{
		boolean aTime = (this.getArrivalTime() == other.getArrivalTime());
		boolean eTime = (this.getExecTime() == other.getExecTime());
		boolean wTime = (this.getWaitTime() == other.getWaitTime());
		boolean sTime = (this.getServTime() == other.getServTime());
		boolean idEqual = (this.getID().equals(other.getID()));
		if(aTime && eTime && wTime && sTime && idEqual)
			return true;
		else
			return false;
	}

	public void addFault(int time)
	{
		faultTimes.add(time);
	}

}