package jar.enums;

public enum ColEnum {

	A(0), B(1), C(2), D(3),
	E(4), F(5), G(6), H(7),
	I(8);

	private int row;
	private ColEnum(int row){
		this.row = row;
	}

	public int getRow() { return row; };
}
