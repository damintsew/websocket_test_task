This is sample solution for test task (https://github.com/KenkouGmbH/maze-backend-challenge)

1) The main problem in this task is ordering of all messages. In that case I was unable use multiple separated threads.


Overall description of architecture:
1) Application has 3 separate threads   
 - Event socket listener which listens for incoming events;
 - Client socket which handles incoming user connections and notifies users
 - Thread that holds queue with incoming Events and submits event to Clients handler  
 
 As for buffer implementation I selected BlockingPriorityQueue and AtomicLong for storing previous sent sequence number. 
 So every incoming event I add to queue and check sequence number of head element. If head sequence number is next than I submit this event to ClientHandler.
 
 In this solution AtomicLong is excessive because this variable uses only one thread but further I think about implementing 
 more than one subscribers thread and query this Queue in multiple threads so we can increase throughput of solution. 
 
 