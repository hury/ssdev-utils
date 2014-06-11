package ctd.util.exp.exception;

import ctd.util.exception.CodedBaseException;

public class ExprException extends CodedBaseException {
	private static final long serialVersionUID = -3712765640188038285L;
	
	public ExprException(String msg){
		super(msg);
	}
	
	public ExprException(Throwable e){
		super(e);
	}
}
