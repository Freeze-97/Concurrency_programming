//
// Tommy Yasi
// toya1800
#include "Consumer.h"

Consumer::Consumer(BlockingQueue<int> *sQ, int p_nElem) {
    sharedQueue = sQ;
    nElem = p_nElem;
}

shared_ptr<deque<int>> Consumer::operator()() {
    shared_ptr<deque<int>> resultDeque (new deque<int>);
    for (int i = 0; i < nElem; i++) {
        int value = sharedQueue->pop();
        resultDeque->push_back(value);
    }
    return resultDeque;
}

