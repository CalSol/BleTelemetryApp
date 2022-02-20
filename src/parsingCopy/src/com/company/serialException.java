package com.company;

/** General exception indicating a Gitlet error.  For fatal errors, the
 *  result of .getMessage() is the error message to be printed.
 *  @author P. N. Hilfinger
 */
class serialException extends RuntimeException {


    /** A GitletException with no message. */
    serialException() {
        super();
    }

    /** A GitletException MSG as its message. */
    serialException(String msg) {
        super(msg);
    }

}