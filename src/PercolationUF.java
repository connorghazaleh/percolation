
public class PercolationUF implements IPercolate {
	
	boolean[][] myGrid;
	IUnionFind myFinder;
	int numSites = 0;
	private final int VTOP;
	private final int VBOTTOM;
	private int myOpenCount = 0;
	
	
	
	PercolationUF(int size, IUnionFind finder){
		myGrid = new boolean[size][size];
		for (int i = 0; i < myGrid.length; i++) {
			for (int j = 0; j < myGrid.length; j++) {
				myGrid[i][j] = false;
			}
		}
		myFinder = finder;
		myFinder.initialize(size*size +2);
		VTOP = (size)*(size);
		VBOTTOM = (size)*(size)+1;
	}
	
	@Override
	public void open(int row, int col) {
		// TODO Auto-generated method stub

		if ((row > myGrid.length || row < 0) || (col > myGrid.length || col < 0)) {
			throw new IndexOutOfBoundsException();
		}
		
		if (myGrid[row][col] == false) {
			myOpenCount += 1;
		}
		
		myGrid[row][col] = true;
		updateOnOpen(row,col);
	}

	@Override
	public boolean isOpen(int row, int col) {
		// TODO Auto-generated method stub
		if (!inBounds(row,col)) {
			throw new IndexOutOfBoundsException();
		}
		if (myGrid[row][col] == true) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isFull(int row, int col) {

		if (!inBounds(row,col)) {
			throw new IndexOutOfBoundsException();
		}
		if (myFinder.connected(VTOP,getIndex(row,col)) && isOpen(row,col)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean percolates() {
		// TODO Auto-generated method stub
		if (myFinder.connected(VTOP,VBOTTOM)) {
			return true;
		}
		return false;
	}

	@Override
	public int numberOfOpenSites() {
		// TODO Auto-generated method stub

		return myOpenCount;
	}
	
	protected int getIndex(int row,int col) {
		if ((row > myGrid.length || row < 0) || (col > myGrid.length || col < 0)) {
			throw new IndexOutOfBoundsException();
		}
		return (myGrid.length)*row + col;
	}
	
	

	protected void updateOnOpen(int row, int col) {
		if (!inBounds(row,col)) {
			throw new IndexOutOfBoundsException();
		}
		
		if (row == 0) {
			myFinder.union(getIndex(row,col), VTOP);
		}
		if (row == myGrid.length-1) {
			myFinder.union(getIndex(row,col), VBOTTOM);
		}
		if (!isOpen(row,col)) {
			myGrid[row][col] = true;
		}
		if (row < myGrid.length-1 && isOpen(row+1,col)) {
			myFinder.union(getIndex(row+1,col), getIndex(row,col));
		}
		if (row > 0 && isOpen(row-1,col)) {
			myFinder.union(getIndex(row-1,col), getIndex(row,col));
		}
		if (col < myGrid.length-1 && isOpen(row,col+1)) {
			myFinder.union(getIndex(row,col+1), getIndex(row,col));
		}
		if (col > 0 && isOpen(row,col-1)) {
			myFinder.union(getIndex(row,col-1), getIndex(row,col));
		}

		
	}
	protected boolean inBounds(int row, int col) {
		if (row < 0 || row >= myGrid.length) return false;
		if (col < 0 || col >= myGrid[0].length) return false;
		return true;
	}

}
