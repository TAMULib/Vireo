package org.tdl.vireo.exception;

public class SubmissionStatusException extends RuntimeException {

    private static final long serialVersionUID = 7472398998406972054L;

    public SubmissionStatusException() {
        super();
    }

    public SubmissionStatusException(String message) {
        super(message);
    }

    public SubmissionStatusException(String message, Throwable ex) {
        super(message, ex);
    }

}
