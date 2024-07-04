// Tommy Yasi
// toya1800
#include "Producer.h"

Producer::Producer(BlockingQueue<int> *sQ, int pFirst, int pLast) {
    sharedQueue = sQ;
    first = pFirst;
    last = pLast;
}

void Producer::operator()() {
    for (int i = first; i < last; i++) {
        sharedQueue->push(i);
    }
}

