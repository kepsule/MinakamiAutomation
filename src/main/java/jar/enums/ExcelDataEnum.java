package jar.enums;

public enum ExcelDataEnum {

	BROWSER_CELL(2, ColEnum.B.getRow()),
	URL_CELL(2, ColEnum.C.getRow()),
	EVIDENCE_CELL(2, ColEnum.D.getRow()),
	DBPATH_CELL(2, ColEnum.E.getRow()),
	DBUSER_CELL(2, ColEnum.F.getRow()),
	DBPASSWORD_CELL(2, ColEnum.G.getRow()),
	START_CELL(5, ColEnum.B.getRow())
	;

	private int row;
	private int col;

	private ExcelDataEnum(int row, int col){
		this.row = row;
		this.col = col;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}
}
