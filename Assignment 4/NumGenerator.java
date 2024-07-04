package concurrency.ch04_composing_objects.exercise_4_3;

import java.util.concurrent.atomic.*;

/**
 * TODO: 
 * The nextValue method returns consecutive values in the 
 * range [MIN_VALUE..MAX_VALUE]. When MAX_VALUE is reached the counter is reset to
 * MIN_VALUE and the counting restarts. But when a NumGenerator object is accessed 
 * concurrently from multiple threads it does not pass the Unit tests and it also
 * generates exceptions.
 * Apparently we have one or more race conditions. Describe the problems and provide
 * a solution that passes the Unit tests. 
 * You may only make changes to the method nextValue and you must not synchronize 
 * the whole method.
 * 
 * The resetCounter just counts the number of resets of the counter.
 * 
 */
public class NumGenerator {
	
    static final int MIN_VALUE = -256;
    static final int MAX_VALUE = 255;
    static final int INITIAL_VALUE = MIN_VALUE -1;
    

    private final AtomicInteger counter = new AtomicInteger(INITIAL_VALUE);
    private final AtomicInteger resetCounter = new AtomicInteger(0); 

    private final Object lock = new Object();

 
    /* The problem is that multiple threads could increase the counter att the same time before
     * the thread reaches the next step which is locked
     *  That's why we got a race condition where many threads increased and got the counter value before
     *  actually finishing the whole process of nextValue()
     */
    public int nextValue() {
    	int next = counter.incrementAndGet();
    	if (next > MAX_VALUE) {
    	    synchronized (lock) {
    	    /* This part should be here instead of after the if part, 
    	    * since we already know that next is more than max 
    	   	* Removed int i to make the code shorter */
    	    	next = counter.incrementAndGet();
    	        if (next > MAX_VALUE) { // Still have to check since the last check was outside the locked scope
    	        	next = MIN_VALUE; // The initial value starts with a forbidden value, use MIN_VALUE instead
    	        // This part could become much better but I couldn't find a way to simplify it
    	            counter.set(MIN_VALUE);  
    	            resetCounter.incrementAndGet();
    	        }
    	    }
    	}
    	return next; 
    }
}
