package jar.util;

/** 取得データが想定結果と異なっていた場合の例外 */
public class AssertException extends AppException {

	public AssertException(String message){
		super(ErrorCodeEnum.AssertionError.name(), message);
	}
}
