
public class PercolationDFSFast extends PercolationDFS{
		
	public PercolationDFSFast(int n) {
		super(n);
	}

	@Override
	public boolean isOpen(int row, int col) {
		
		if ((row > myGrid.length || row < 0) || (col > myGrid.length || col < 0)) {
			throw new IndexOutOfBoundsException();
		}
		
		return super.isOpen(row,col);
	}
		

}
