package kr.co.sunnyvale.sunny.security;

import org.springframework.security.core.AuthenticationException;

public class InactiveAdminException extends AuthenticationException{
	//~ Constructors ===================================================================================================

    /**
	 * 
	 */
	private static final long serialVersionUID = -6438578597509784170L;

	/**
     * Constructs a <code>BadCredentialsException</code> with the specified
     * message.
     *
     * @param msg the detail message
     */
    public InactiveAdminException(String msg) {
        super(msg);
    }

    @Deprecated
    public InactiveAdminException(String msg, Object extraInformation) {
        super(msg, extraInformation);
    }

    /**
     * Constructs a <code>BadCredentialsException</code> with the specified
     * message and root cause.
     *
     * @param msg the detail message
     * @param t root cause
     */
    public InactiveAdminException(String msg, Throwable t) {
        super(msg, t);
    }
	
	
}
