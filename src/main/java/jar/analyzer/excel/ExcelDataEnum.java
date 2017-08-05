package jar.analyzer.excel;

import jar.util.FileAnalyzerException;

/** 読み込む設定のセルを指定する。 */
public enum ExcelDataEnum {

	/** ブラウザ */
	BROWSER_CELL(2, ColEnum.B.getCol()),

	/** 操作を開始するURL */
	URL_CELL(2, ColEnum.C.getCol()),

	/** エビデンス格納先 */
	EVIDENCE_CELL(2, ColEnum.D.getCol()),

	/** DB設定 */
	DBPATH_CELL(2, ColEnum.E.getCol()),
	DBUSER_CELL(2, ColEnum.F.getCol()),
	DBPASSWORD_CELL(2, ColEnum.G.getCol()),

	/** 処理開始する行 */
	START_ROW(5, ColEnum.B.getCol()) {
		@Override
		@Deprecated
		int getCol() throws FileAnalyzerException {
			throw new FileAnalyzerException("不正な処理呼び出し：列数取得できません");
		}
	},

	/** 操作内容 */
	OPERATION_COL(ColEnum.B.getCol(), true),

	/** 要素の種類 (name, id等) */
	TAG_COL(ColEnum.C.getCol(), true),

	/** 要素に付けられた名前 */
	NAME_COL(ColEnum.D.getCol(), true),

	/** 配列番号（0始まり） */
	NUM_COL(ColEnum.E.getCol(), true),

	/** 入力内容 */
	INPUT_COL(ColEnum.F.getCol(), true);


	/** 行 */
	private int row;

	/** 列 */
	private int col;

	/** 列数のみ取得フラグ */
	private boolean colOnly;

	/** 列 */
	private ExcelDataEnum(int col, boolean colOnly){
		this.col = col;
	}

	/** 行・列 */
	private ExcelDataEnum(int row, int col){
		this.row = row;
		this.col = col;
		colOnly = false;
	}

	/**
	 * 行数取得<p>
	 * 行数が入りえない場合、例外をスローする</p> */
	int getRow() throws FileAnalyzerException {
		if (colOnly) throw new FileAnalyzerException(
				"不正な処理呼び出し：行数取得できません");
		return row;
	}

	/** 列数取得 */
	int getCol() throws FileAnalyzerException {
		return col;
	}
}
