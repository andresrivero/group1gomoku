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
		
		
		private String type;

		public ABNode(String type, Object userObject, boolean allowsChildren) {
			super(userObject, allowsChildren);
			this.type = type;

		}
		
		@Override
		public Point getUserObject() {
			
			return (Point)super.userObject;
			
		}
		@Override
		public ABNode getChildAt(int childIndex) {
			return (ABNode) super.getChildAt(childIndex);
		}
//		
//		public Point Max(int alpha, int beta) {
//			this.alpha = alpha;
//			this.beta = beta;
//			Point temp = new Point(alpha,0,0);
//			this.userObject = temp;
//			for (int x = 0; x < this.getChildCount(); x++) {
//				temp = this.Min(temp.value, this.beta);
//				
//				
//			}
//			
//			return null;
//			
////		}
////		
//		public Point Min(int alpha, int beta) {
//			
//			return null;
//		}
//		
		

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
	
	public Point Prune(ABNode down, Point alpha, Point beta, boolean minOrMax) {
		
		Point abPoint = new Point(0,0,0);

		if (down.isLeaf()) {
			abPoint = down.getUserObject();
		}
		
		else if (minOrMax) {
			
			abPoint = alpha;
			int q = 0;
			int childrenleft = down.getChildCount();
			while (q < childrenleft) {
				
				
				System.out.println(q);
				Point kidPoint = Prune(down.getChildAt(q), abPoint, beta, false);
				abPoint = abPoint.value > kidPoint.value ? abPoint: kidPoint;
				
				q++;
				if (beta.value <= abPoint.value) break;
				
			}
		} else {
			int q = 0;

			abPoint = beta;
			int childrenleft = down.getChildCount();
			while (q < childrenleft) {
				
				System.out.println(q);

				Point kidPoint = Prune(down.getChildAt(q), alpha, abPoint, true);
				
				abPoint = abPoint.value < kidPoint.value ? abPoint: kidPoint;
				
				q++;
				
				if (abPoint.value <= alpha.value) break;
				
			}

		}
		return abPoint;
	}
	public ABPrune() {
		
		ArrayList<Point> array = new ArrayList<Point>();
		int[] arrayi = {-16,-19,20,-19,-3,19,-4,-19,-15};
		for (int x = 0; x < 9; x++) {
			array.add(new Point(arrayi[x],arrayi[x], arrayi[x]));
		}
		
		ABNode root = new ABNode("root", null, true);
		
		root.add(new ABNode("min", null, true));
		root.add(new ABNode("min", null, true));
		root.add(new ABNode("min", null, true));

		ABNode tmp = root;
		Enumeration children = tmp.children();
		System.out.println(root.getChildCount());
		
		ABNode tmp2 = (ABNode)children.nextElement();

		System.out.println(tmp2.hashCode());
		tmp2.add(new ABNode("leaf",  new Point(array.get(0).value, array.get(0).value, array.get(0).value), false));
		tmp2.add(new ABNode("leaf",  new Point(array.get(1).value, array.get(1).value, array.get(1).value), false));
		tmp2.add(new ABNode("leaf",  new Point(array.get(2).value, array.get(2).value, array.get(2).value), false));

		tmp2 = (ABNode) children.nextElement();

				System.out.println(tmp2.hashCode());
			tmp2.add(new ABNode("leaf",  new Point(array.get(3).value, array.get(3).value, array.get(3).value), false));
			tmp2.add(new ABNode("leaf",  new Point(array.get(4).value, array.get(4).value, array.get(4).value), false));
			tmp2.add(new ABNode("leaf",  new Point(array.get(5).value, array.get(5).value, array.get(5).value), false));

			 tmp2 = (ABNode) children.nextElement();

				System.out.println(tmp2.hashCode());
			tmp2.add(new ABNode("leaf",  new Point(array.get(6).value, array.get(6).value, array.get(6).value), false));
			tmp2.add(new ABNode("leaf",  new Point(array.get(7).value, array.get(7).value, array.get(7).value), false));
			tmp2.add(new ABNode("leaf",  new Point(array.get(8).value, array.get(8).value, array.get(8).value), false));

//		System.out.println( tmp.getChildCount());
//		
//		System.out.println(tmp.getUserObject());
//		System.out.println(tmp.getLevel());
//		tmp = (ABNode) tmp.getFirstChild();
//		System.out.println(tmp.getLevel());
//		Enumeration depth = root.breadthFirstEnumeration();
//		ABNode enumed;
//		while(depth.hasMoreElements()) {
//			enumed = (ABNode)depth.nextElement();
//			Point p = (Point) enumed.getUserObject();
//				if (p == null) {
//					System.out.println("null");
//				} else {
//					System.out.println(p.value);
//				}
//			
//		}
		System.out.println(Prune(root, new Point(Integer.MIN_VALUE, 0, 0), new Point(Integer.MAX_VALUE, 0, 0), true).value);
		
		
		
		// TODO Auto-generated constructor stub
	}
	
	public static void main (String args[]) {
		ABPrune prune = new ABPrune();
		
		
		//Point bestMove = root.Prune();
		
	}
	
}
