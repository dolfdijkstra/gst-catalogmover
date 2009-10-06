package com.fatwire.cs.catalogmover.diff;

public class MockComplexObject {

    private final long id;

    private final int version;

    /**
     * @param id
     * @param version
     */
    public MockComplexObject(final long id, final int version) {
        super();
        this.id = id;
        this.version = version;
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @return the version
     */
    public int getVersion() {
        return version;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + (int) (id ^ (id >>> 32));
        result = PRIME * result + version;
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final MockComplexObject other = (MockComplexObject) obj;
        if (id != other.id)
            return false;
        if (version != other.version)
            return false;
        return true;
    }

}
