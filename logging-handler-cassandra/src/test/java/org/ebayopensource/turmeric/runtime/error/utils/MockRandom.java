package org.ebayopensource.turmeric.runtime.error.utils;

import java.util.Random;

public class MockRandom extends Random {

    /* (non-Javadoc)
     * @see java.util.Random#nextInt()
     */
    @Override
    public int nextInt() {
        return 1; //I need it to return always a fixed value for testing purposes
    }

}
