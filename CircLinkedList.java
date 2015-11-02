class CircLinkedList
{
	Node current, head, tail;
	int time;
	public static final int MEMORYSIZE = 30;

	CircLinkedList()
	{
		current = head = tail = null;
		time = 0;
	}

	CircLinkedList(Node newData)
	{
		time = 0;
		head = tail = newData;
		current = null;
	}

	CircLinkedList(int num)
	{
		time = 0;
		current = tail = head = new Node();
		for(int i=0; i<num-1;i++)
		{
			add(new Node());
		}
	}

	public void add(Node newData)
	{
		if(head == null)
		{
			head = newData;
			tail = newData;
			head.setNext(head);
			head.setPrev(head);
		}
		else
		{
			tail.setNext(newData);
			newData.setPrev(tail);
			newData.setNext(head);
			head.setPrev(newData);
			tail = newData;
			if(size() == MEMORYSIZE)
			{
				current = head;
			}
		}
	}

	public boolean freeSpace(Process p)
	{
		for(int i=p.memoryRange.firstElement().intValue(); i<=p.memoryRange.get(1).intValue(); i++)
		{
			if(get(i).isEmpty())
				return true;
		}
		return false;
	}

	// Should only be ran after contains(Page) and returned true
	// returns Node which contains page
	public Node at(Page page)
	{
		Node temp = head;
		while(!temp.getNext().equals(head))
		{
			if(temp.equals(page))
				break;
			temp = temp.getNext();
		}
		return temp;
	}

	// Returns the node at i
	public Node get(int i)
	{
		Node temp = head;
		int j = 0;
		while(j!=i)
		{
			temp = temp.getNext();
			j++;
		}
		return temp;
	}

	// add to list
	// add at
	// remove from list

	// Returns the number of nodes there are
	public int size()
	{
		if(head==null)
			return 0;
		else
		{
			int i=1;
			Node temp = head;
			while(!temp.getNext().equals(head))
			{
				i++;
				temp = temp.getNext();
			}
			return i;
		}
	}

	// Returns the number of nodes contained in the list that do not have empty data
	public int size2()
	{
		if(head!=null)
		{
			int i=0;
			Node temp = head;
			while(!temp.getNext().equals(head))
			{
				if(temp.getData()!=null)
					i++;
				temp = temp.getNext();
			}
			return i;
		}
		else
			return 0;
	}

	// Swaps first Node with no secondChance (false)
	// each Node it ticks over that has secondChance loses second chance
	public void swap(Page swapIn)
	{
		while(current.hasSecondChance())
		{
			current.takeSecondChance();
			current=current.getNext();
		}
		// current does not have second chance
		current.setData(swapIn);
		// swapped in frame gets second chance
		current.giveSecondChance();
		// goes to next
		current=current.getNext();
	}

	public boolean contains(Page checkPage)
	{
		Node checkPointer = head;

		for(int i=0; i<size(); i++)
		{
			if(checkPage.equals(checkPointer.getData()))
					return true;
			checkPointer = checkPointer.getNext();
		}
		return false;
	}

	// If it is run, 
	public void run(Process p)
	{
		// Give the node that holds the page second chance
		at(p.pages.firstElement()).giveSecondChance();
		// if p's page size >0
		if(p.pages.size()>0)
		{
			// execute and aklsjfd
			p.pages.firstElement().execute(getTime());
			p.lastRemoved = p.pages.firstElement();
			p.pages.remove(0);
		}
		else
		{
			// execute last removed
			p.lastRemoved.execute(getTime());
		}
	}

	public void print()
	{
		if(size() > 0)
		{
			System.out.println("In CLL: ");
			if(size() == 1)
			{
				System.out.println("{"+head.getData().getID()+"}");
			}
			if(size() == 2)
			{
				System.out.println("{"+head.getData().getID()+", "+tail.getData().getID()+"}");
			}
			else
			{
				String s = "{"+head.getData().getID();
				Node tempNode = head.getNext();
				for(int i=1; i<size();i++)
				{
					s+=", " + tempNode.getData().getID();
					tempNode = tempNode.getNext();
				}
				s+="}";
				System.out.println(s);
			}
		}
		else
			System.out.println("CLL empty");

	}

	public int getTime()
	{
		return time;
	}

	public void incrTime()
	{
		time++;
	}
}