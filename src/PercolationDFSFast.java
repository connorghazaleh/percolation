
public class PercolationDFSFast extends PercolationDFS{
		
	public PercolationDFSFast(int n) {
		//uses constructor from parent class
		super(n);
	}

	@Override
	public boolean isOpen(int row, int col) {
		//checks if coordinates are in bounds
		if (!inBounds(row,col)) {
			throw new IndexOutOfBoundsException();
		}
		//uses method isOpen from parent class
		return super.isOpen(row,col);
	}
	
	@Override
	public boolean isFull(int row, int col) {
		//checks if coordinates are in bounds
		if (!inBounds(row,col)) {
			throw new IndexOutOfBoundsException();
		}
		//uses method isFull from parent class
		return super.isFull(row,col);
	}
	
	@Override
	public void open(int row, int col) {
		//checks if coordinates are in bounds
		if (!inBounds(row,col)) {
			throw new IndexOutOfBoundsException();
		}
		
		//if site is not blocked, terminates,
		//if blocked, then site is opened, and made full if it is in the top row
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
		//checks if coordinates are in bounds
		if (!inBounds(row,col)) {
			throw new IndexOutOfBoundsException();
		}
		
		//if any site around site in question is full, set site in question to full
		//uses cases so that computer does not try to check a site that does not exist
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
