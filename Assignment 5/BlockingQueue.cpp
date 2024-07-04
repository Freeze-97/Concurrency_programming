// Implemeting a BLockingQueue to work like the one available in Java
// Tommy Yasi
// toya1800

#include "BlockingQueue.h"

template <class T>
BlockingQueue<T>::BlockingQueue(int pQ_SIZE) {
    Q_SIZE = pQ_SIZE;
}

template <class T>
void BlockingQueue<T>::push(T element) {
    unique_lock<mutex> lock(m); // Lock mutex

    // If the queue is full, wait
    while (isFull()) {
        fullCV.wait(lock);
    }

    queue.push_back(element); // Push the element to the last place, not front
    emptyCV.notify_one();  // Unblocking one thread that is waiting
}

template <class T>
T BlockingQueue<T>::pop() {
    unique_lock<mutex> lock(m); // Try to get the lock for the queue

    // If the queue is emtpy, wait
    while (isEmpty()) {
        emptyCV.wait(lock);
    }

    T outElement = queue.front();  // Get the item from the front
    queue.erase(queue.begin()); // Delete the first element which has been copied
    fullCV.notify_one(); // Notify one thread that is waiting to enqueue an element

    return outElement;
}

template <class T>
bool BlockingQueue<T>::isFull() {
    return queue.size() == Q_SIZE;
}

template <class T>
bool BlockingQueue<T>::isEmpty() {
    return queue.empty();
}

