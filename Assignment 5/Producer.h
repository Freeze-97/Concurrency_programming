// Producer side
// Tommy Yasi
// toya1800

#ifndef BLOCKINGQUEUE_PRODUCER_H
#define BLOCKINGQUEUE_PRODUCER_H

#include "BlockingQueue.h"
class Producer {
private:
   BlockingQueue<int> *sharedQueue;
    int first;
    int last;
public:
    Producer(BlockingQueue<int> *sQ, int pFirst, int pLast); // Constructor
    void operator ()();
};


#endif //BLOCKINGQUEUE_PRODUCER_H
