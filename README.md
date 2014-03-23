akka-remote-actor-flood-test
============================

A test of sending a flood of messages from a sender to a receiver via Akka to investigate weird timing behavior of received messages.

To run, configure your IDE to run the remote.Main class as follows:  
* pass cmd line argument "receiver" to start the receiver
* pass cmd line argumets "sender N" to start the sender and send N messages to receiver

The problem I'm seeing is that the time it takes to send N messages from sender to receiver does not scale linearly with N.  Both sender and receiver were executed on the same machine (new generation Macbook Pro) in separate JVM processes.

Example  
-------

Below, the receiver prints out a status message after each 20,000 messages received, where *delta* represents the amount of time elapsed since the last block of 20,000 messages was received. I would expect to see that the amount of time it takes to send N messages from sender to receiver is fairly constant (some small fluctuation would be understandable).

What's also interesting is the pattern of the timings where the later blocks of messages are received progressively faster. I've ran the code of both sender and receiver through profilers and have ruled out garbage collection as being a potential cause of this. 

**N = 100,000**
<pre>
16:07:33.504 [INFO] - delta = 2,242 ms, received = 20,000
16:07:33.976 [INFO] - delta = 475 ms, received = 40,000
16:07:34.585 [INFO] - delta = 610 ms, received = 60,000
16:07:35.065 [INFO] - delta = 479 ms, received = 80,000
16:07:35.415 [INFO] - delta = 351 ms, received = 100,000
</pre>

**N = 200,000**
<pre>
16:08:48.176 [INFO] - delta = 2,236 ms, received = 20,000
16:08:49.685 [INFO] - delta = 1,510 ms, received = 40,000
16:08:50.872 [INFO] - delta = 1,186 ms, received = 60,000
16:08:52.116 [INFO] - delta = 1,245 ms, received = 80,000
16:08:52.918 [INFO] - delta = 802 ms, received = 100,000
16:08:53.626 [INFO] - delta = 708 ms, received = 120,000
16:08:54.363 [INFO] - delta = 737 ms, received = 140,000
16:08:55.059 [INFO] - delta = 695 ms, received = 160,000
16:08:55.627 [INFO] - delta = 568 ms, received = 180,000
16:08:55.962 [INFO] - delta = 335 ms, received = 200,000
</pre>

**N = 400,000**
<pre>
16:09:44.540 [INFO] - delta = 2,333 ms, received = 20,000
16:09:47.882 [INFO] - delta = 3,341 ms, received = 40,000
16:09:50.637 [INFO] - delta = 2,755 ms, received = 60,000
16:09:53.135 [INFO] - delta = 2,499 ms, received = 80,000
16:09:56.006 [INFO] - delta = 2,870 ms, received = 100,000
16:09:59.207 [INFO] - delta = 3,202 ms, received = 120,000
16:10:02.037 [INFO] - delta = 2,830 ms, received = 140,000
16:10:04.312 [INFO] - delta = 2,274 ms, received = 160,000
16:10:06.603 [INFO] - delta = 2,291 ms, received = 180,000
16:10:08.887 [INFO] - delta = 2,285 ms, received = 200,000
16:10:10.690 [INFO] - delta = 1,803 ms, received = 220,000
16:10:12.358 [INFO] - delta = 1,667 ms, received = 240,000
16:10:13.915 [INFO] - delta = 1,558 ms, received = 260,000
16:10:15.207 [INFO] - delta = 1,292 ms, received = 280,000
16:10:16.232 [INFO] - delta = 1,024 ms, received = 300,000
16:10:17.013 [INFO] - delta = 781 ms, received = 320,000
16:10:17.700 [INFO] - delta = 687 ms, received = 340,000
16:10:18.473 [INFO] - delta = 773 ms, received = 360,000
16:10:19.125 [INFO] - delta = 652 ms, received = 380,000
16:10:19.807 [INFO] - delta = 683 ms, received = 400,000
</pre>

**N = 800,000**
<pre>
16:11:26.535 [INFO] - delta = 2,657 ms, received = 20,000
16:11:28.954 [INFO] - delta = 2,419 ms, received = 40,000
16:11:35.275 [INFO] - delta = 6,321 ms, received = 60,000
16:11:41.953 [INFO] - delta = 6,678 ms, received = 80,000
16:11:49.335 [INFO] - delta = 7,382 ms, received = 100,000
16:11:56.381 [INFO] - delta = 7,047 ms, received = 120,000
16:12:02.114 [INFO] - delta = 5,733 ms, received = 140,000
16:12:08.792 [INFO] - delta = 6,678 ms, received = 160,000
16:12:16.577 [INFO] - delta = 7,785 ms, received = 180,000
16:12:23.711 [INFO] - delta = 7,134 ms, received = 200,000
16:12:28.807 [INFO] - delta = 5,096 ms, received = 220,000
16:12:34.445 [INFO] - delta = 5,637 ms, received = 240,000
16:12:39.554 [INFO] - delta = 5,110 ms, received = 260,000
16:12:45.198 [INFO] - delta = 5,644 ms, received = 280,000
16:12:51.373 [INFO] - delta = 6,174 ms, received = 300,000
16:12:56.894 [INFO] - delta = 5,522 ms, received = 320,000
16:13:01.810 [INFO] - delta = 4,916 ms, received = 340,000
16:13:06.715 [INFO] - delta = 4,904 ms, received = 360,000
16:13:10.754 [INFO] - delta = 4,040 ms, received = 380,000
16:13:14.980 [INFO] - delta = 4,226 ms, received = 400,000
16:13:19.026 [INFO] - delta = 4,046 ms, received = 420,000
16:13:22.166 [INFO] - delta = 3,140 ms, received = 440,000
16:13:25.787 [INFO] - delta = 3,621 ms, received = 460,000
16:13:28.884 [INFO] - delta = 3,096 ms, received = 480,000
16:13:32.178 [INFO] - delta = 3,295 ms, received = 500,000
16:13:35.258 [INFO] - delta = 3,079 ms, received = 520,000
16:13:38.244 [INFO] - delta = 2,987 ms, received = 540,000
16:13:40.567 [INFO] - delta = 2,323 ms, received = 560,000
16:13:42.908 [INFO] - delta = 2,339 ms, received = 580,000
16:13:45.069 [INFO] - delta = 2,162 ms, received = 600,000
16:13:46.935 [INFO] - delta = 1,867 ms, received = 620,000
16:13:48.379 [INFO] - delta = 1,443 ms, received = 640,000
16:13:50.001 [INFO] - delta = 1,623 ms, received = 660,000
16:13:51.287 [INFO] - delta = 1,285 ms, received = 680,000
16:13:52.277 [INFO] - delta = 991 ms, received = 700,000
16:13:53.074 [INFO] - delta = 797 ms, received = 720,000
16:13:53.700 [INFO] - delta = 626 ms, received = 740,000
16:13:54.460 [INFO] - delta = 760 ms, received = 760,000
16:13:55.112 [INFO] - delta = 652 ms, received = 780,000
16:13:55.822 [INFO] - delta = 709 ms, received = 800,000
</pre>

