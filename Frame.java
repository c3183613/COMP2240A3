class Frame
{
	private String frameID;
	private Page page;
	private boolean occupied, reserved;
	private Page reservedFor;

	Frame(String newID)
	{
		// initialize occupied
		frameID = newID;
		occupied = reserved = false;
		page = reservedFor = null;
	}

	public void setID(String newID)
	{
		frameID = newID;
	}

	public String getID()
	{
		return frameID;
	}

	public void holdPage(Page holdThis)
	{
		page = holdThis;
		holdThis.setInMain(true);
		occupied = true;
	}

	public Page getHolding()
	{
		return page;
	}

	public Page getReservedFor()
	{
		return reservedFor;
	}

	public void finishedHolding()
	{
		page = null;
		occupied = false;
	}

	public boolean isOccupied()
	{
		return occupied;
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
}