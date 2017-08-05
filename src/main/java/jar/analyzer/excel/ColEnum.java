package jar.analyzer.excel;

/** Excelの列対応Enum */
public enum ColEnum {

	A(0), B(1), C(2), D(3),
	E(4), F(5), G(6), H(7),
	I(8);

	private int col;
	private ColEnum(int col){
		this.col = col;
	}

	/** 列取得 */
	int getCol() { return col; };
}
