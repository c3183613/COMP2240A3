class Page
{
	// unique identifier
	private String pageID;
	// Currently in main memory, true if in main memory 
	private boolean inMain;	// not used


	// CONSUTRUCTORS
	Page(String newID)
	{
		pageID = newID.substring(7, newID.length());
		inMain = false;
	}

	Page(Page p)
	{
		this.pageID = p.getID();
		this.inMain = p.inMain;
	}

	// QUERY METHODS
	public String getID()
	{
		return pageID;
	}

	public boolean isinMain()
	{
		return inMain;
	}

	// MUTATOR METHODS

	public void setInMain(boolean i)
	{
		inMain = i;
	}

	public void outOfMain()
	{
		inMain = false;
	}

	// used for testing purposes
	public void execute(int time)
	{
		System.out.println(getID() + " has gotten service time at "+ time);
	}

	// returns true if they both have the same page id 
	public boolean equals(Page p)
	{
		if(this.pageID.equals(p.pageID))
			return true;
		return false;
	}
}