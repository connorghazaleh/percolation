
public class PercolationDFSFast extends PercolationDFS{
		
	public PercolationDFSFast(int n) {
		super(n);
	}

	@Override
	public boolean isOpen(int row, int col) {
		
		if (!inBounds(row,col)) {
			throw new IndexOutOfBoundsException();
		}
		
		return super.isOpen(row,col);
	}
	
	@Override
	public boolean isFull(int row, int col) {
		
		if (!inBounds(row,col)) {
			throw new IndexOutOfBoundsException();
		}
		
		return super.isFull(row,col);
	}
	
	@Override
	public void open(int row, int col) {
		if (!inBounds(row,col)) {
			throw new IndexOutOfBoundsException();
		}
		if (myGrid[row][col] != BLOCKED)
			return;
		myOpenCount += 1;
		myGrid[row][col] = OPEN;
		if (row == 0) {
			myGrid[row][col]	 = FULL;
		}
		updateOnOpen(row,col);
	}
		
	@Override
	protected void updateOnOpen(int row, int col) {
		if (!inBounds(row,col)) {
			throw new IndexOutOfBoundsException();
		}
		if (isFull(row,col) ||
				(row < myGrid.length-1 && isFull(row+1,col)) ||
				(row > 0 && isFull(row-1,col)) ||
				(col < myGrid.length-1 && isFull(row,col+1)) ||
				(col > 0 && isFull(row,col-1))) {
			myGrid[row][col] = FULL;
			dfs(row - 1, col);
			dfs(row, col - 1);
			dfs(row, col + 1);
			dfs(row + 1, col);
		}
		
	}

}
