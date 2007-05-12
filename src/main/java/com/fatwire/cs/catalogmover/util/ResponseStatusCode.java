/*
 * $Logfile: /branches/CSv610/ContentServer/COM/FutureTense/Util/ResponseStatusCode.java $ $Revision: 19 $ $Date: 8/03/04 5:13p $
 *
 * Copyright (c) 2002, 2003 divine, inc., All Rights Reserved.
 * Copyright (c) 1999, 2000, 2001 Open Market, Inc., All Rights Reserved
 */

package com.fatwire.cs.catalogmover.util;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The ResponseStatusCode object is a container object for representing status
 * messages. It can be represented as a java object, or it can be represented as
 * a string, in the form of an HTML comment. It can be constructed using
 * standard Java constructors and setter methods, and it can also be set from a
 * random string of input data, which will then be parsed. <p/> The
 * ResponseStatusCode object contains four primary fields:
 * <ul>
 * <li>command string (represents the command that was executed that resulted
 * in the result represented herein</li>
 * <li>result flag (boolean representation of success/failure)</li>
 * <li>result string (text representation of success/failure) </li>
 * <li>reason string (represents the reason for which the result occurred</li>
 * </ul>
 * Additionally, the ResponseStatusCode can contain input parameters as
 * argumetns to the command specified. This enables helpful debug messages in
 * cases of failure. <p/> The ResponseStatusCode can contain multiple codes.
 * This is possible when the ResponseStatusCode object is set from a data, and
 * the data contains multiple serialized status code objects. Navigation through
 * the list of status codes in this case is done using the {@link #setNextError}
 * and {@link #rewind} methods.
 */
public class ResponseStatusCode {
    public static final String Unknown = "Unknown ";

    public static final String Command = "Command";

    public static final String Reason = "Reason";

    public static final String successStr = "success";

    public static final String failureStr = "failure";

    public static final String zerolengthstring = "";

    // the prefix other code might need to know to
    // identify a status string
    public static final String statusPrefix = "<!--FTCS";

    public static final String statusPostfix = "-->";

    // seperator string between fields
    static final String sep = "|||";

    static final String crlf = "\r\n";

    // Error format string. Created by calling
    // methods below
    static final String sName = "ftstatus";

    static final String sN = "n";

    static final String sV = "v";

    static final String _result = "result=";

    static final String _reason = "reason=";

    static final String _err = "err=";

    static final String _command = "command=";

    static final String _params = "params=";

    static final String _xresult = "xresult";

    static final String _xreason = "xreason";

    static final String _xerr = "xerr";

    static final String _xcommand = "xcommand";

    static final String _xparam = "xparam";

    private static Log log = LogFactory.getLog(ResponseStatusCode.class);

    /**
     * Returns the generic logger object
     * 
     * @return Log object
     */
    private Log getLog() {
        return log;
    }

    // the parts.
    private final StringBuffer result = new StringBuffer(
            ResponseStatusCode.Unknown);

    private String reason;

    private String command;

    private Map<String, String> hparams = null;

    private String mInput;

    private String mInput0 = null;

    private int err;

    private boolean bState;

    private String sState = null;

    /**
     * Construct an ResponseStatusCode object. The initial object will have an
     * unknown command and an unknown reason, with an error code of zero.
     */
    public ResponseStatusCode() {
        command = ResponseStatusCode.Unknown + ResponseStatusCode.Command;
        err = 0;
        reason = ResponseStatusCode.Unknown + ResponseStatusCode.Reason;
    }

    /**
     * Construct an ResponseStatusCode object without a reason code.
     * 
     * @param sCommand
     *            command string
     * @param iError
     *            error number
     */
    public ResponseStatusCode(final String sCommand, final int iError) {
        command = sCommand;
        err = iError;
        reason = ResponseStatusCode.zerolengthstring;
        bState = iError == 0;
    }

    /**
     * Construct an ResponseStatusCode includin gcommand string, error code, and
     * reason message.
     * 
     * @param sCommand
     *            command string
     * @param iError
     *            error number
     * @param sReason
     *            reason string
     */
    public ResponseStatusCode(final String sCommand, final int iError,
            final String sReason) {
        command = sCommand;
        err = iError;
        reason = sReason;
        bState = iError == 0;
    }

    /**
     * Add a parameter to the ResponseStatusCode for the purpose of providing
     * debug information
     * 
     * @param key
     *            parameter key
     * @param value
     *            parameter value
     */
    public void addParam(final String key, final String value) {
        if (hparams == null) {
            hparams = new Hashtable<String, String>(100, 100);
        }
        hparams.put(key, value);
    }

    /**
     * Compose this status code into an object that can be streamed as a string
     */
    void compose() {
        String state;
        if (StringUtils.goodString(sState)) {
            state = sState;
        } else {
            state = bState ? ResponseStatusCode.successStr
                    : ResponseStatusCode.failureStr;
        }

        // construct the shell of the string
        result.setLength(0);

        result.append(ResponseStatusCode.crlf);
        result.append(ResponseStatusCode.statusPrefix);
        result.append(ResponseStatusCode.sep);

        result.append(ResponseStatusCode._result);
        result.append(state);
        result.append(ResponseStatusCode.sep);

        result.append(ResponseStatusCode._reason);
        result.append(reason);
        result.append(ResponseStatusCode.sep);

        result.append(ResponseStatusCode._err);
        result.append(Integer.toString(err));
        result.append(ResponseStatusCode.sep);

        result.append(ResponseStatusCode._command);
        result.append(command);
        result.append(ResponseStatusCode.sep);

        // Parameters
        final StringBuilder p = new StringBuilder();
        if (hparams != null) {
            try {

                for (final Map.Entry<String, String> oEntry : hparams
                        .entrySet()) {
                    final String name = oEntry.getKey();
                    final String value = oEntry.getValue().toString();

                    if (p.length() > 0) {
                        p.append('&');
                    }
                    p.append(name).append('=').append(value);
                }
            } catch (final Exception e) {
                // this currently happens quite frequently. // TODO: fix
                getLog().trace("Failure composing status code", e);
            }
        }
        result.append(ResponseStatusCode._params).append(p.toString()).append(
                ResponseStatusCode.sep);
        result.append(ResponseStatusCode.statusPostfix); // end comment
    }

    /**
     * Get the command stored in this ResponseStatusCode. The command indicates
     * what operation was being performed at the time the code was emitted
     * 
     * @return the command string
     */
    public String getCommand() {
        return command;
    }

    /**
     * Return the error id as specified in this ResponseStatusCode. The error ID
     * should correspond to the command executed.
     * 
     * @return error id
     */
    public int getErrorID() {
        return err;
    }

    /**
     * Return a hashtable object containing all of the parameters specified in
     * this status code.
     * 
     * @return HashMap containing params.
     */
    public Map<String, String> getParamHash() {
        return new HashMap<String, String>(hparams);
    }

    /**
     * Return the parameters in a Map object.
     * 
     * @return Map containing parameters.
     */
    public Map<String, String> getParams() {
        final Map<String, String> iparams = new HashMap<String, String>();
        if (hparams != null) {
            try {
                for (final Map.Entry<String, String> oEntry : hparams
                        .entrySet()) {
                    final String name = oEntry.getKey();
                    final String value = oEntry.getValue().toString();
                    if (StringUtils.goodString(value)) {
                        iparams.put(name, value);
                    }
                }
            } catch (final Exception e) {
                getLog().error("Failure getting params in ResponseStatusCode",
                        e);
            }
        }
        return iparams;
    }

    /**
     * Return the reason code specified in this ResponseStatusCode. The reason
     * string provides a detailed explanation of the cause of the result.
     * 
     * @return reason string
     */
    public String getReason() {
        return reason;
    }

    /**
     * Return the result code, true or false
     * 
     * @return true for success, false for failure
     */
    public boolean getResult() {
        return bState;
    }

    /**
     * Return the ResponseStatusCode formatted into an HTML comment.
     * 
     * @return formatted result string
     */
    public String toString() {
        compose();
        return result.toString();
    }

    /**
     * Return the result of the status code, true or false, in the form of a
     * String.
     * 
     * @return result string
     */
    public String getStrResult() {
        return sState;
    }

    /**
     * Rewind a status object Used after setFromData() and setNextError(), this
     * resets the object to point to the first (if found) status code
     * 
     * @return if rewind happened, but does not prove data contains codes
     */
    public boolean rewind() {
        return setFromData(mInput0);
    }

    /**
     * Setter method for the command string
     * 
     * @param c
     *            command string to be set into the code
     */
    public void setCommand(final String c) {
        command = c;
    }

    /**
     * Setter method for the error number
     * 
     * @param err
     *            error number
     */
    public void setError(final int err) {
        this.err = err;
    }

    /**
     * Looks for the well defined coded message in the data. <p/> This method
     * will retain part of the input data object, and will set the status code
     * to the first error occurrence. <p/> To scroll from one error instance to
     * the next, call {@link #setNextError()}.
     * 
     * @param data
     *            response data
     * @see #setNextError
     */
    public boolean setFromData(final String data) {
        if (StringUtils.emptyString(data))
            return false;
        try {
            // if it has nothing good an exception
            // will get tossed
            final int index = data.indexOf(ResponseStatusCode.statusPrefix);

            // save the original copy for rewind
            // but add one char since setNextError()
            // always wants to advance 1 char
            if (mInput0 == null) {
                mInput0 = data;
            }
            if (index == -1) {
                return false;
            }
            mInput = data.substring(index);
            return setFromString(mInput);

        } catch (final Exception e) {
            // this behaviour currently occurs quite frequently. // todo: fix
            getLog().trace("Failure setting status code from string", e);
        }
        return false;
    }

    /**
     * Sets the status code from a single string, which is expected to contain
     * nothing other than a well-formed status code.
     * 
     * @param s
     *            status code string
     * @return true if the set was successful, false if not (due to invalid
     *         input data)
     */
    public boolean setFromString(String s) {
        if (!StringUtils.goodString(s)
                || !s.startsWith(ResponseStatusCode.statusPrefix)) {
            return false;
        }

        try {
            s = s.substring(ResponseStatusCode.statusPrefix.length()
                    + ResponseStatusCode.sep.length()); // past
            // header to
            // result,
            // past
            // first sep
            final List<String> parts = StringUtils.arguments(s,
                    ResponseStatusCode.sep);
            // if (parts.size() != 6) // 5 parts, 1 terminated comment
            if (parts.size() < 6) {
                return false;
            }

            // first is result;
            String t = parts.get(0);
            sState = t.substring(ResponseStatusCode._result.length());
            if (sState.equals(ResponseStatusCode.successStr)) {
                bState = true;
                sState = null;
            } else if (sState.equals(ResponseStatusCode.failureStr)) {
                bState = false;
                sState = null;
            }

            // reason (user message)
            t = parts.get(1);
            reason = t.substring(ResponseStatusCode._reason.length());

            // error number
            t = parts.get(2);
            t = t.substring(ResponseStatusCode._err.length());
            final Integer ii = new Integer(t);
            err = ii.intValue();

            // command
            t = parts.get(3);
            command = t.substring(ResponseStatusCode._command.length());

            // params
            t = parts.get(4);
            t = t.substring(ResponseStatusCode._params.length());

            hparams = new HashMap<String, String>(100, 100);
            StringUtils.seedTo(t, hparams, false);

            return true;
        } catch (final Exception e) {
            // this behaviour currently happens quite frequently todo: fix
            getLog().trace("Failure setting from string", e);
        }

        return false;
    }

    /**
     * Set the parameters into this status code in the form of a Hashtable
     * 
     * @param h
     *            Map of params
     */
    public void setHashParams(final Map<String, String> h) {
        hparams = h;
    }

    /**
     * Find next encoded message in the previous set data response string. <p/>
     * In other words, the object contains a string with multiple status codes
     * in it, this method advances the cursor to the next entry.
     */
    public boolean setNextError() {
        if ((mInput == null) || (mInput.length() < 1)) {
            return false;
        } else {
            // walk past first char
            return setFromData(mInput.substring(1));
        }
    }

    /**
     * Set the params for this status code using an input map
     * 
     * @param input
     *            map of params to set
     */
    public void setParams(final Map<String, String> input) {
        hparams = new HashMap<String, String>(100, 100);

        try {
            for (final Map.Entry<String, String> oEntry : input.entrySet()) {
                final String name = oEntry.getKey();
                final String value = oEntry.getValue().toString();
                if (StringUtils.goodString(value)) {
                    hparams.put(name, value);
                }
            }
        } catch (final Exception e) {
            getLog().error("Failure setting params in ResponseStatusCode", e);
        }
    }

    /**
     * Setter method for the reason string
     * 
     * @param reason
     *            reason string
     */
    public void setReason(final String reason) {
        this.reason = reason;
    }

    /**
     * Set the result as either success (true) or failure (false)
     * 
     * @param b
     *            result falg
     */
    public void setResult(final boolean b) {
        bState = b;
        sState = null;
    }

    /**
     * Set the result string
     * 
     * @param s
     *            success or failure
     * @see ftMessage#successStr
     * @see ftMessage#failureStr
     */
    public void setResult(final String s) {
        sState = s;
    }
}