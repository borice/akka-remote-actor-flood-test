akka-remote-actor-flood-test
============================

A test of sending a flood of messages from a sender to a receiver via Akka to investigate weird timing behavior of received messages.

To run, configure your IDE to run the remote.Main class as follows:  
* pass cmd line argument "receiver" to start the receiver
* pass cmd line argumets "sender N" to start the sender and send N messages to receiver

The problem I'm seeing is that the time it takes to send N messages from sender to receiver does not scale linearly with N.  Both sender and receiver were executed on the same machine (new generation Macbook Pro) in separate JVM processes.
For more information about this, see [this post](https://groups.google.com/forum/#!topic/akka-user/5vYs4jKMaQU).

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

**N = 1,600,000**
<pre>
17:46:29.990 [INFO] - delta = 3,839 ms, received = 20,000
17:46:44.280 [INFO] - delta = 14,290 ms, received = 40,000
17:47:01.804 [INFO] - delta = 17,524 ms, received = 60,000
17:47:19.130 [INFO] - delta = 17,325 ms, received = 80,000
17:47:35.865 [INFO] - delta = 16,736 ms, received = 100,000
17:47:48.879 [INFO] - delta = 13,014 ms, received = 120,000
17:48:06.195 [INFO] - delta = 17,316 ms, received = 140,000
17:48:23.237 [INFO] - delta = 17,041 ms, received = 160,000
17:48:38.569 [INFO] - delta = 15,333 ms, received = 180,000
17:48:54.377 [INFO] - delta = 15,806 ms, received = 200,000
17:49:08.496 [INFO] - delta = 14,121 ms, received = 220,000
17:49:24.407 [INFO] - delta = 15,911 ms, received = 240,000
17:49:39.529 [INFO] - delta = 15,121 ms, received = 260,000
17:49:55.634 [INFO] - delta = 16,106 ms, received = 280,000
17:50:08.969 [INFO] - delta = 13,335 ms, received = 300,000
17:50:23.976 [INFO] - delta = 15,007 ms, received = 320,000
17:50:37.460 [INFO] - delta = 13,483 ms, received = 340,000
17:50:52.654 [INFO] - delta = 15,195 ms, received = 360,000
17:51:06.397 [INFO] - delta = 13,743 ms, received = 380,000
17:51:19.712 [INFO] - delta = 13,315 ms, received = 400,000
17:51:32.363 [INFO] - delta = 12,651 ms, received = 420,000
17:51:45.934 [INFO] - delta = 13,570 ms, received = 440,000
17:52:00.484 [INFO] - delta = 14,550 ms, received = 460,000
17:52:13.031 [INFO] - delta = 12,548 ms, received = 480,000
17:52:22.576 [INFO] - delta = 9,545 ms, received = 500,000
17:52:34.058 [INFO] - delta = 11,481 ms, received = 520,000
17:52:45.467 [INFO] - delta = 11,409 ms, received = 540,000
17:52:57.609 [INFO] - delta = 12,143 ms, received = 560,000
17:53:09.907 [INFO] - delta = 12,297 ms, received = 580,000
17:53:20.499 [INFO] - delta = 10,593 ms, received = 600,000
17:53:31.394 [INFO] - delta = 10,894 ms, received = 620,000
17:53:41.157 [INFO] - delta = 9,764 ms, received = 640,000
17:53:52.101 [INFO] - delta = 10,944 ms, received = 660,000
17:54:02.782 [INFO] - delta = 10,681 ms, received = 680,000
17:54:12.383 [INFO] - delta = 9,601 ms, received = 700,000
17:54:20.916 [INFO] - delta = 8,533 ms, received = 720,000
17:54:30.436 [INFO] - delta = 9,520 ms, received = 740,000
17:54:39.817 [INFO] - delta = 9,381 ms, received = 760,000
17:54:50.254 [INFO] - delta = 10,437 ms, received = 780,000
17:54:59.993 [INFO] - delta = 9,739 ms, received = 800,000
17:55:09.970 [INFO] - delta = 9,975 ms, received = 820,000
17:55:17.703 [INFO] - delta = 7,734 ms, received = 840,000
17:55:26.015 [INFO] - delta = 8,313 ms, received = 860,000
17:55:35.484 [INFO] - delta = 9,469 ms, received = 880,000
17:55:43.620 [INFO] - delta = 8,136 ms, received = 900,000
17:55:50.240 [INFO] - delta = 6,619 ms, received = 920,000
17:55:57.903 [INFO] - delta = 7,664 ms, received = 940,000
17:56:05.638 [INFO] - delta = 7,735 ms, received = 960,000
17:56:12.879 [INFO] - delta = 7,241 ms, received = 980,000
17:56:19.231 [INFO] - delta = 6,352 ms, received = 1,000,000
17:56:25.643 [INFO] - delta = 6,412 ms, received = 1,020,000
17:56:31.848 [INFO] - delta = 6,205 ms, received = 1,040,000
17:56:37.866 [INFO] - delta = 6,018 ms, received = 1,060,000
17:56:43.406 [INFO] - delta = 5,539 ms, received = 1,080,000
17:56:49.706 [INFO] - delta = 6,300 ms, received = 1,100,000
17:56:55.528 [INFO] - delta = 5,823 ms, received = 1,120,000
17:57:00.471 [INFO] - delta = 4,943 ms, received = 1,140,000
17:57:05.417 [INFO] - delta = 4,945 ms, received = 1,160,000
17:57:09.322 [INFO] - delta = 3,905 ms, received = 1,180,000
17:57:13.646 [INFO] - delta = 4,325 ms, received = 1,200,000
17:57:17.178 [INFO] - delta = 3,531 ms, received = 1,220,000
17:57:21.276 [INFO] - delta = 4,099 ms, received = 1,240,000
17:57:24.656 [INFO] - delta = 3,380 ms, received = 1,260,000
17:57:28.482 [INFO] - delta = 3,825 ms, received = 1,280,000
17:57:31.473 [INFO] - delta = 2,992 ms, received = 1,300,000
17:57:34.391 [INFO] - delta = 2,917 ms, received = 1,320,000
17:57:37.106 [INFO] - delta = 2,715 ms, received = 1,340,000
17:57:39.746 [INFO] - delta = 2,641 ms, received = 1,360,000
17:57:42.281 [INFO] - delta = 2,535 ms, received = 1,380,000
17:57:44.603 [INFO] - delta = 2,321 ms, received = 1,400,000
17:57:46.377 [INFO] - delta = 1,775 ms, received = 1,420,000
17:57:48.468 [INFO] - delta = 2,091 ms, received = 1,440,000
17:57:50.062 [INFO] - delta = 1,594 ms, received = 1,460,000
17:57:51.481 [INFO] - delta = 1,418 ms, received = 1,480,000
17:57:52.489 [INFO] - delta = 1,009 ms, received = 1,500,000
17:57:53.483 [INFO] - delta = 993 ms, received = 1,520,000
17:57:54.285 [INFO] - delta = 803 ms, received = 1,540,000
17:57:55.095 [INFO] - delta = 809 ms, received = 1,560,000
17:57:55.764 [INFO] - delta = 670 ms, received = 1,580,000
17:57:56.430 [INFO] - delta = 666 ms, received = 1,600,000
</pre>
