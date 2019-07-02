package org.tdl.vireo.exception;

public class SubmissionException extends RuntimeException {

    private static final long serialVersionUID = 1015567234853452869L;

    public SubmissionException() {
        super();
    }

    public SubmissionException(String message) {
        super(message);
    }

    public SubmissionException(String message, Throwable ex) {
        super(message, ex);
    }

}
