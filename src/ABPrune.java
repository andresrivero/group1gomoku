import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;

import javax.swing.tree.*;

//pruning class
//using defaultmutabletreenode

public class ABPrune {

	public ABNode root = null;
	class ABNode extends DefaultMutableTreeNode {
		
		
		private String type;

		public ABNode(String type, Object userObject, boolean allowsChildren) {
			super(userObject, allowsChildren);
			this.type = type;

		}
		
		@Override
		public Point getUserObject() {
			
			return (Point) super.userObject;
			
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
			//Minimizer
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
			//Maximizer
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
	
	
	public ABPrune(ArrayList<Point> pointList81) {
		//long start = System.currentTimeMillis();
		for (Point tmp : pointList81) {
			System.out.print(tmp.value + " ");
		}
		System.out.println(pointList81.size());
		root = new ABNode("root", null, true);
		
		root.add(new ABNode("min1", null, true));
		root.add(new ABNode("min2", null, true));
		root.add(new ABNode("min3", null, true));
		Iterator itr = pointList81.iterator();
		
		Enumeration minChildren = root.children();
		for (int x = 0; x < root.getChildCount(); x++) {
			ABNode tmp = (ABNode)minChildren.nextElement();
			tmp.add(new ABNode("max" + x+1, null, true));
			
			tmp.add(new ABNode("max" + x+1, null, true));
			tmp.add(new ABNode("max" + x+1, null, true));
			Enumeration maxChildren = tmp.children();
			for (int y = 0; y < tmp.getChildCount(); y++){
				
				ABNode tmp2 = (ABNode)maxChildren.nextElement();
				tmp2.add(new ABNode("leaf",  (Point)itr.next(), false));

				tmp2.add(new ABNode("leaf",  (Point)itr.next(), false));

				tmp2.add(new ABNode("leaf",  (Point)itr.next(), false));
			}
			maxChildren = tmp.children();



		}
	}


}
