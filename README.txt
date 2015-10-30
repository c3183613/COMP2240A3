Contents: COMP2240 Assignment 3 Sample Input & Output Data
Author: Marcos Cousens-Schulz (SN: 3183388)
Date: 26/10/2015
Yes, I am doing this partially in order to help verify my own results, so if you find any discrepancies, please let me know.

Directory layout:
each test has it's own folder, they are numbered 0-10.
within each folder:
    there are n (varies) files of the form "processX.txt", X in [1, n],  which are the input files for each process.
    there is also 1 output file in each folder "output.txt" which contains the results of all 4 replacement policy types for the inputs in that folder.

Assumptions:
    There is no standard output format for this assignment submission. I was planning to provide a link to a diffing tool for every to test their data against mine a lot easier, but it looks like it won't help much.
        However, if do you want to use the output format I have chosen, then you could use this online tool to quickly identify the differences between outputs:
            https://www.diffchecker.com/

    Supposing that the processor's current process A finishes a time slice on cycle X, and some blocked process B has it's IO request also finishing on cycle X, the blocked process B will go into the ready queue first.

    The number of cycles a given process wants to execute for (i.e. number of entries in the process' input file) varies from process to process even within the same test.

    In fixed local, the fixed number of frames available to each process is determined before the simulation starts, and does not change when processes exit the system.

    The number of processes in a given test can be arbitrary (as long as it's <= than the number of frames available in memory, as that would be impossible to satisfy with fixed local?)


Random test boundaries:

Number of tests: 10

In this data set, the number of processes varies in each test, and the number of cycles (i.e. page requests) each process varies for each process in each test.

Range for the number of processes: [2,12]
Range for the number of cycles: [5,30]
Range for page numbers in each request: [1,15]

Other advice:
Because fixed-local replacement splits the memory up amongst the processes, it may be useful to check your program against at least 1 small & evenly divisible number of processes, and 1 large, non-evenly divisible number of processes, i.e.:
    Therefore, I recommend checking against at least one test from the following groups:
        Tests with few processes that 30 is evenly divisible by: 4, 9.
        and
        Tests with many processes that 30 is NOT evenly divisible by: 2, 3, 8.