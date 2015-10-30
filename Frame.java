class Frame
{
	private String frameID;
	private Page page;
	private boolean occupied;
	Frame(String newID)
	{
		// initialize occupied
		frameID = newID;
		occupied = false;
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

	public void finishedHolding()
	{
		page = null;
		occupied = false;
	}

	public boolean isOccupied()
	{
		return occupied;
	}

	public void setOccupied(boolean i)
	{
		occupied = i;
	}
}