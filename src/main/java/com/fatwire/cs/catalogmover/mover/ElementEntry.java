package com.fatwire.cs.catalogmover.mover;

public class ElementEntry {

    private String elementname;

    private String description;

    private String url;

    private String resdetails1;

    private String resdetails2;

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getElementname() {
        return elementname;
    }

    public void setElementname(final String elementname) {
        this.elementname = elementname;
    }

    public String getResdetails1() {
        return resdetails1;
    }

    public void setResdetails1(final String resdetails1) {
        this.resdetails1 = resdetails1;
    }

    public String getResdetails2() {
        return resdetails2;
    }

    public void setResdetails2(final String resdetails2) {
        this.resdetails2 = resdetails2;
    }

    public String getUrl() {
        return url;
    }

    //todo: remove ,0 etc from filename
    //always use filenames with forward slashes
    public void setUrl(final String url) {
        this.url = url;
    }

    //@Override
    public String toString() {
        final StringBuilder b = new StringBuilder();
        b.append(elementname);
        b.append(", ");
        b.append(description);
        b.append(", ");
        b.append(url);
        b.append(", ");
        b.append(resdetails1);
        b.append(", ");
        b.append(resdetails2);
        return b.toString();
    }

    //@Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result
                + ((description == null) ? 0 : description.hashCode());
        result = PRIME * result
                + ((elementname == null) ? 0 : elementname.hashCode());
        result = PRIME * result
                + ((resdetails1 == null) ? 0 : resdetails1.hashCode());
        result = PRIME * result
                + ((resdetails2 == null) ? 0 : resdetails2.hashCode());
        result = PRIME * result + ((url == null) ? 0 : url.hashCode());
        return result;
    }

    //@Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ElementEntry other = (ElementEntry) obj;
        if (description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!description.equals(other.description)) {
            return false;
        }
        if (elementname == null) {
            if (other.elementname != null) {
                return false;
            }
        } else if (!elementname.equals(other.elementname)) {
            return false;
        }
        if (resdetails1 == null) {
            if (other.resdetails1 != null) {
                return false;
            }
        } else if (!resdetails1.equals(other.resdetails1)) {
            return false;
        }
        if (resdetails2 == null) {
            if (other.resdetails2 != null) {
                return false;
            }
        } else if (!resdetails2.equals(other.resdetails2)) {
            return false;
        }
        if (url == null) {
            if (other.url != null) {
                return false;
            }
        } else if (!url.equals(other.url)) {
            return false;
        }
        return true;
    }

}
