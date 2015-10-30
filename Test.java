class Test
{
	public static void main(String[] args)
	{
		Node node1 = new Node(new Page("page1"));
		Node node2 = new Node(new Page("page2"));
		Node node3 = new Node(new Page("page1"));
		Node node4 = new Node(new Page("page4"));
		Node node5 = new Node(new Page("page5"));
		Node node6 = new Node(new Page("page6"));
		Node node7 = new Node(new Page("page7"));
		Node node8 = new Node(new Page("page5"));
		Node node9 = new Node(new Page("page10"));

		// System.out.println("node 1 equals node 3" + node1.equals(node3));
		CircLinkedList cLL = new CircLinkedList(node1);
		cLL.add(node2);
		cLL.add(node4);
		cLL.add(node5);
		cLL.add(node6);
		cLL.add(node7);
		cLL.add(node8);
		cLL.add(node9);
		// System.out.println(cLL.tail.getData().getID());
		// System.out.println(cLL.tail.getNext().getData().getID());

		String hello[] = new String[30];
		// for(int i=0;i<30;i++)
		// {
		// 	hello[i] = "hello"+i;
		// }
		System.out.println(hello[3]);
		// System.out.println(s.equals(str));
		// System.out.println("Checking if page4 is in circular linked list");
		// System.out.println(cLL.contains(node4.getData()));
	}
}