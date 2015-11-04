class Frame
{
	/*
		frameID		- unique identifier for the frame
		page 		- page that frame holds
		occupied 	- true if it is holding a page, false otherwise
		reserved 	- true if it is reserved for a page, false otherwise
		secondChance - true if Frame has second chance, false otherwise
		reseredFor 	- Page which this frame is reservedFor
	*/
	private String frameID;
	private Page page;
	private boolean occupied, reserved, secondChance;
	private Page reservedFor;

	Frame(String newID)
	{
		// initialize occupied
		frameID = newID;
		occupied = reserved = secondChance = false;
		page = reservedFor = null;
	}


	// QUERY METHODS
	public String getID()
	{
		return frameID;
	}

	// Returns the page it is holding, if it is not holding anything, return null
	public Page getHolding()
	{
		return page;
	}

	// Return which Page the Node is reserved for. If not reserved for any Page, return null
	public Page getReservedFor()
	{
		return reservedFor;
	}

	// Returns occupied
	public boolean isOccupied()
	{
		return occupied;
	}

	// returns secondChance
	public boolean hasSecondChance()
	{
		return secondChance;
	}

	// returns true if Frame is reserved, false otherwise
	public boolean isReserved()
	{
		return reserved;
	}

	// returns true if Frame is reserved for i, returns false otherwise
	public boolean isReservedFor(Page i)
	{
		if(reservedFor == i)
			return true;
		else
			return false;
	}
	// MUTATOR METHODS

	// hold Page holdThis, set occupied to true
	public void holdPage(Page holdThis)
	{
		page = holdThis;
		occupied = true;
	}

	// sets occupied to false and page to null as it is no longer holding anything
	public void finishedHolding()
	{
		page = null;
		occupied = false;
	}

	// set id
	public void setID(String newID)
	{
		frameID = newID;
	}

	// sets page to hold nothing (null) and occupied to false
	public void resetOccupied()
	{
		occupied = false;
		page = null;
	}

	// set reserved to true and set what Frame is reserve for
	public void reserveFor(Page i)
	{
		reservedFor = i;
		reserved = true;
	}

	// reset reservedFor
	public void resetReservedFor()
	{
		reservedFor = null;
	}


	// Set reserved to i
	public void setReserved(boolean i)
	{
		reserved =i;
	}

	// reset reserved and reservedFor
	public void resetReserved()
	{
		reserved = false;
		reservedFor = null;
	}

	// Set occupied to i
	public void setOccupied(boolean i)
	{
		occupied = i;
	}

	// set secondChance to true
	public void giveSecondChance()
	{
		secondChance = true;
	}

	// set secondChance to false
	public void takeSecondChance()
	{
		secondChance = false;
	}
}