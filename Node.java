class Node
{
	private Node next, prev;
	private Page data;
	private boolean empty, secondChance;

	Node()
	{
		data = null;
		next = null;
		prev = null;
		empty = true;
		secondChance= true;
	}

	Node(Node nPrev, Node nNext)
	{
		prev = nPrev;
		data = null;
		next = nNext;
		empty = true;
		secondChance= true;
	}

	Node(Node nPrev, Page nData, Node nNext)
	{
		prev = nPrev;
		data = nData;
		next = nNext;
		empty = false;
		secondChance= true;
	}

	Node(Node nPrev, Page nData)
	{
		prev = nPrev;
		data = nData;
		next = null;
		empty = false;
		secondChance= true;
	}

	Node(Page nData)
	{
		prev = null;
		data = nData;
		next = null;
		empty = false;
		secondChance= true;
	}

	// Query methods
	public Node getPrev()
	{
		return prev;
	}

	public Page getData()
	{
		return data;
	}

	public Node getNext()
	{
		return next;
	}

	public boolean equals(Node n)
	{
		if(this.isEmpty())
			return false;
		if(this.getData().equals(n.getData()))
			return true;
		else
			return false;
	}

	public boolean equals(Page page)
	{
		if(this.isEmpty())
			return false;
		if(this.getData().equals(page))
			return true;
		else
			return false;
	}

	public boolean isEmpty()
	{
		return empty;
	}

	public boolean hasSecondChance()
	{
		return secondChance;
	}
	// Mutator methods
	public void setPrev(Node nPrev)
	{
		prev = nPrev;
	}

	public void setData(Page nData)
	{
		data = nData;
		empty = false;
	}

	public void setNext(Node nNext)
	{
		next = nNext;
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