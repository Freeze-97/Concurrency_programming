package concurrency.ch02_thread_safety.exercise_2_1;

import concurrency.math.*;
import net.jcip.annotations.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.math.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * TODO: Add counters in a thread-safe way
 * Add a counter for the number of hits and the number of cache hits.  Return
 * these values from the methods getHits() and getCacheHitRatio().  The class
 * should still be thread-safe when you are done.
 */
@ThreadSafe
public class CachedFactorizer extends HttpServlet {
    @GuardedBy("this")
    private BigInteger lastNumber;
    @GuardedBy("this")
    private BigInteger[] lastFactors;
    @GuardedBy("this")
    private long hits;
    @GuardedBy("this")
    private long cacheHits;
    

    public synchronized long getHits() {
        return hits;
    }

    public synchronized double getCacheHitRatio() {
        return (double) cacheHits / (double) hits;
    }

    protected void doGet(HttpServletRequest req,
                         HttpServletResponse res)
            throws ServletException, IOException {
        BigInteger i = extractFromRequest(req);
        BigInteger[] factors = null;
        synchronized (this) {
        	++hits;
            if (i.equals(lastNumber)) {
            	++cacheHits;
            	factors = lastFactors.clone();
            	}
        }
        if (factors == null) {
            factors = Factorizer.factor(i);
            synchronized (this) {
                lastNumber = i;
                lastFactors = factors.clone();
            }
        }
        encodeIntoResponse(res, factors);
    }

    private BigInteger extractFromRequest(ServletRequest req) {
        String numberStr = req.getParameter("number");
        if (numberStr == null) return BigInteger.ZERO;
        return new BigInteger(numberStr);
    }

    private void encodeIntoResponse(ServletResponse res, BigInteger[] factors)
            throws IOException {
        res.getOutputStream().print(Arrays.toString(factors));
    }

    private static final long serialVersionUID = -3879955848640480043L;
}
