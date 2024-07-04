// Consumer side of the queue
// Tommy Yasi
// toya1800

#ifndef BLOCKINGQUEUE_CONSUMER_H
#define BLOCKINGQUEUE_CONSUMER_H

#include "BlockingQueue.h"
class Consumer {
private:
    BlockingQueue<int> *sharedQueue;
    int nElem;
public:
    Consumer(BlockingQueue<int> *sQ, int p_nElem); // Constructor
    shared_ptr<deque<int>> operator()();
};

#endif //BLOCKINGQUEUE_CONSUMER_H
