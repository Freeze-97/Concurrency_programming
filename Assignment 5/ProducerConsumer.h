// WHere the producer and consumer use the queue to create threads
// Tommy Yasi
// toya1800

#ifndef BLOCKINGQUEUE_PRODUCERCONSUMER_H
#define BLOCKINGQUEUE_PRODUCERCONSUMER_H

#include "Producer.h"
#include "Consumer.h"
#include <thread>
#include <future>

class ProducerConsumer {
private:
    const int NUM_OF_PRODUCERS;
    const int NUM_OF_CONSUMERS;
    const int NUM_OF_ELEMENTS;
    const int Q_SIZE;
public:
    ProducerConsumer(int pNUM_OF_PRODUCERS, int pNUM_OF_CONSUMERS,
            int pNUM_OF_ELEMENTS, int pQ_SIZE);
    void run(); // TestProgram
};

#endif //BLOCKINGQUEUE_PRODUCERCONSUMER_H
