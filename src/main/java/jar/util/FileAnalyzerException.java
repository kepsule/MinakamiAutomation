package jar.util;

/** ファイル解析時例外 */
public class FileAnalyzerException extends AppException {

	public FileAnalyzerException(String message){
		super(ErrorCodeEnum.AnalyzationError.name(), message);
	}
}
