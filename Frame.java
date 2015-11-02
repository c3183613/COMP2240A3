class Frame
{
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

	public Page getHolding()
	{
		return page;
	}

	public Page getReservedFor()
	{
		return reservedFor;
	}

	public boolean isOccupied()
	{
		return occupied;
	}

	public boolean hasSecondChance()
	{
		return secondChance;
	}

	public boolean isReserved()
	{
		return reserved;
	}

	public boolean isReservedFor(Page i)
	{
		if(reservedFor == i)
			return true;
		else
			return false;
	}
	// MUTATOR METHODS
	public void holdPage(Page holdThis)
	{
		page = holdThis;
		holdThis.setInMain(true);
		occupied = true;
	}

	public void finishedHolding()
	{
		page = null;
		occupied = false;
	}

	public void setID(String newID)
	{
		frameID = newID;
	}

	public void resetOccupied()
	{
		occupied = false;
		page = null;
	}

	public void reserveFor(Page i)
	{
		reservedFor = i;
		reserved = true;
	}

	public void resetReservedFor()
	{
		reservedFor = null;
	}


	public void setReserved(boolean i)
	{
		reserved =i;
	}

	public void resetReserved()
	{
		reserved = false;
		reservedFor = null;
	}

	public void setOccupied(boolean i)
	{
		occupied = i;
	}

	public void giveSecondChance()
	{
		secondChance = true;
	}

	public void takeSecondChance()
	{
		secondChance = false;
	}
}