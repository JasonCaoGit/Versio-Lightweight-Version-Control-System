package versio;

/** General exception indicating a Gitlet error.  For fatal errors, the
 *  result of .getMessage() is the error message to be printed.
 *  @author P. N. Hilfinger
 */
class VersioException extends RuntimeException {


    /** A VersioException with no message. */
    VersioException() {
        super();
    }

    /** A VersioException MSG as its message. */
    VersioException(String msg) {
        super(msg);
    }

}
