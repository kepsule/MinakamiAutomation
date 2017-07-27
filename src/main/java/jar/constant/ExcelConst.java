package jar.constant;

import jar.enums.ColEnum;

/** Excelファイルで読み取りたいデータの対象行、対象列、セルを定義 */
public class ExcelConst {

	public static final String END_POINT = "END";

	public static final int OPERATION_CELL = ColEnum.B.getRow();

	public static final int TAG_CELL = ColEnum.C.getRow();

	public static final int NAME_CELL = ColEnum.D.getRow();

	public static final int NUM_CELL = ColEnum.E.getRow();

	public static final int INPUT_CELL = ColEnum.F.getRow();
}
