class Page
{
	// unique identifier
	private String pageID;
	// Has been executed
	// Currently in main memory
	private boolean inMain;

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

	public String getID()
	{
		return pageID;
	}

	public void setInMain(boolean i)
	{
		inMain = i;
	}

	public void setInMain(boolean i, int j)
	{
		inMain = i;
	}

	public void outOfMain()
	{
		inMain = false;
	}

	public boolean isinMain()
	{
		return inMain;
	}

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