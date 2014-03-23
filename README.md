akka-remote-actor-flood-test
============================

A test of sending a flood of messages from a sender to a receiver via Akka

To run, configure your IDE to run the remote.Main class as follows:  
* pass cmd line argument "receiver" to start the receiver
* pass cmd line argumets "sender <N>" to start the sender and send <N> messages to receiver
