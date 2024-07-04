// Main uses BlockingQueue to create threads and work as producer and consumer
// Tommy Yasi
// toya1800
#include "ProducerConsumer.h"

int main() {
    ProducerConsumer pc(6,6, 10000000, 10);
    pc.run();
    return 0;
}