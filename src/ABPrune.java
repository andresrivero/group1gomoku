import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.tree.*;

public class ABPrune {


	class Point  {
		int row;
		int col;
		int value;
		public Point(int value, int row, int col) {
			this.value = value;
			this.row = row;
			this.col = col;
			
		}
	}
	
	class ABNode extends DefaultMutableTreeNode {
		protected int alpha;
		protected int beta;
		protected String type;
		public ABNode(String type, int alpha, int beta, Object userObject, boolean allowsChildren) {
			super(userObject, allowsChildren);
			this.type = type;
			this.alpha = alpha;
			this.beta = beta;
			
		}
		
		public Point Max() {
			
			
			return null;
			
		}
		
		public Point Min() {
			
			return null;
		}
		
		public Point Prune() {
			
			return null;
		}

//		@Override
//		public TreeNode getChildAt(int childIndex) {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public int getChildCount() {
//			// TODO Auto-generated method stub
//			return 0;
//		}
//
//		@Override
//		public TreeNode getParent() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public int getIndex(TreeNode node) {
//			// TODO Auto-generated method stub
//			return 0;
//		}
//
//		@Override
//		public boolean getAllowsChildren() {
//			// TODO Auto-generated method stub
//			return false;
//		}
//
//		@Override
//		public boolean isLeaf() {
//			// TODO Auto-generated method stub
//			return false;
//		}
//
//		@Override
//		public Enumeration children() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public void insert(MutableTreeNode child, int index) {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void remove(int index) {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void remove(MutableTreeNode node) {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void setUserObject(Object object) {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void removeFromParent() {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void setParent(MutableTreeNode newParent) {
//			// TODO Auto-generated method stub
//			
//		}
	}
	
	public ABPrune() {
		
		ArrayList<Point> array = new ArrayList<Point>();
		for (int x = 1; x < 10; x++) {
			array.add(new Point(x,x,x));
		}
		ABNode root = new ABNode("root", Integer.MIN_VALUE, Integer.MAX_VALUE, null, true);
		

				
		root.add(new ABNode("min", 0, 0, null, true));
		root.add(new ABNode("min", 0, 0, null, true));
		root.add(new ABNode("min", 0, 0, null, true));

		ABNode tmp = root;
		Enumeration children = tmp.children();
		System.out.println(root.getChildCount());
		
		while (children.hasMoreElements()) {
			tmp = (ABNode) children.nextElement();
			
			tmp.add(new ABNode("leaf", 0, 0, new Point(array.listIterator().next().value, 0, 0), false));
			tmp.add(new ABNode("leaf", 0, 0, new Point(array.listIterator().next().value, 0, 0), false));
			tmp.add(new ABNode("leaf", 0, 0, new Point(array.listIterator().next().value, 0, 0), false));

		}
		 System.out.println( tmp.getChildCount());
		
		System.out.println(tmp.getUserObject());
		System.out.println(tmp.getLevel());
		tmp = (ABNode) tmp.getFirstChild();
		System.out.println(tmp.getLevel());

		
		
		
		// TODO Auto-generated constructor stub
	}
	
	public static void main (String args[]) {
		ABPrune prune = new ABPrune();
		
		
		//Point bestMove = root.Prune();
		
	}
	
}
