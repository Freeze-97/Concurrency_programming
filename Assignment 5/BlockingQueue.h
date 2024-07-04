// Implemeting a BLockingQueue to work like the one available in Java
// Tommy Yasi
// toya1800

#ifndef UNTITLED1_BLOCKINGQUEUE_H
#define UNTITLED1_BLOCKINGQUEUE_H

#include <iostream>
#include <mutex>
#include <queue>
#include <condition_variable>

using namespace std;

template <class T>
class BlockingQueue {
private:
    // Using a vector to act as a bounded blocking queue via cv and mutex
    int Q_SIZE;
    vector<T> queue;
    condition_variable fullCV;
    condition_variable emptyCV;
    mutex m;

public:
    // Two functions are needed, push and pop
    explicit BlockingQueue(int pQ_SIZE);
    int getLength() { return queue.size(); }
    bool isFull();
    bool isEmpty();
    void push(T element);
    T pop();
};
template class BlockingQueue<int>;

#endif //UNTITLED1_BLOCKINGQUEUE_H
