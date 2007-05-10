package com.fatwire.cs.catalogmover.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.logging.LogFactory;

public class StringUtils {

    public static boolean goodString(final String s) {
        return (s != null) && (s.length() > 0);
    }

    public static final Collection<String> arguments(final String cmd,
            final int sep) {
        final byte data[] = { (byte) '\0' };
        data[0] = (byte) sep;
        final String str = new String(data);
        return StringUtils.arguments(cmd, str);
    }

    public static final List<String> arguments(final String cmd,
            final String sep) {
        final List<String> v = new LinkedList<String>();
        StringUtils.arguments(v, cmd, sep);
        return v;
    }

    // A simplistic tokenizer that can deal with
    // seperator strings and produces a vector
    // of substrings.
    //
    // StringTokenizer doesn't work since it requires a
    // token and this doesn't
    public static final Collection<String> arguments(
            final Collection<String> v, final String cmd, final String sep) {
        try {
            final StringTokenizer tokenizer = new StringTokenizer(cmd, sep,
                    false);
            while (tokenizer.hasMoreTokens()) {
                // tokenizer returns true always at least
                // once, so watch out for dead string
                final String s = tokenizer.nextToken().trim();
                if (!StringUtils.emptyString(s)) {
                    v.add(s);
                }
            }
        } catch (final NoSuchElementException exception) {
            LogFactory.getLog(StringUtils.class).error(
                    "NoSuchElementException creating a list from " + cmd,
                    exception);
        } catch (final StringIndexOutOfBoundsException exception) {
            LogFactory.getLog(StringUtils.class).error(
                    "StringIndexOutOfBoundsException creating a list from "
                            + cmd, exception);
        }
        return v;
    }

    public static final boolean emptyString(final String x) {
        // if the string is null, return true==empty
        if (x == null) {
            return true;
        }

        final int len = x.length();
        for (int i = 0; i < len; i++) {
            if (!Character.isWhitespace(x.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Build a map of parameters given input in the form of "a=b&c=d..."
     *
     * @param inputParam in The input string containing the parameters.
     * @param map        The output map of key/value pairs
     * @param bDecode    true to indicate that decoding is desired
     * @since 4.0
     */
    public static final void seedTo(final String inputParam,
            final Map<String, String> map, final boolean bDecode) {
        if (StringUtils.emptyString(inputParam)) {
            return;
        }

        int iequal, iamper;
        int startAt = 0;
        final int inlen = inputParam.length();
        boolean bDone = false;

        while (!bDone) {
            String n, v;
            if ((iequal = inputParam.indexOf('=', startAt)) != -1) {
                // End of current name=value is '&' or EOL
                iamper = inputParam.indexOf('&', iequal);
                n = inputParam.substring(startAt, iequal).trim();
                iequal++;
                if (iequal >= inlen) {
                    break;
                }

                if (iamper == -1) {
                    v = inputParam.substring(iequal);
                } else {
                    v = inputParam.substring(iequal, iamper);
                }

                if (iamper != -1) {
                    startAt = iamper + 1;
                } else {
                    bDone = true;
                }

                // deal with stupid value
                v = v.trim();
                if (bDecode) {
                    try {
                        n = StringUtils.decode(n);
                        v = StringUtils.decode(v);
                    } catch (final Exception exception) {
                    }
                }
                map.put(n, v);
            } else {
                break; // no more pairs
            }
        }

    }

    private static final String PLATFORM_DEFAULT_CHARSET = Charset
            .defaultCharset().name();

    private static final URLCodec APACHE_COMMONS_CODEC = new URLCodec(
            StringUtils.PLATFORM_DEFAULT_CHARSET);

    private static String decode(final String n) {
        try {
            return StringUtils.APACHE_COMMONS_CODEC.decode(n,
                    StringUtils.PLATFORM_DEFAULT_CHARSET);
        } catch (final UnsupportedEncodingException e) {
            throw new IllegalStateException(
                    "Platform default encoding is not supported??? ", e);
        } catch (final DecoderException e) {
            throw new RuntimeException(e);
        }
    }

}