package com.fatwire.cs.catalogmover.catalogs;

/**
 * An object to pass around as needed
 * to be able to get multiple returns
 * from various functions where the
 * return result is data but an int
 * return is also required
 * <p/>
 * Member is public to bypass overhead
 * of a method call on a simple container.
 */
public class IntStatus {
    /**
     * The value of the object,
     * an int
     */
    public int value;

    /**
     * Constructor, defaults the
     * status to 0
     */
    public IntStatus() {
        value = 0;
    } // a decent value?

    /**
     * Constructor, set the value
     *
     * @param v
     */
    public IntStatus(final int v) {
        value = v;
    }
}
