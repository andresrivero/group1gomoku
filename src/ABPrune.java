import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.tree.*;

public class ABPrune {
	ABNode root;
	DefaultMutableTreeNode rut;
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
	
	class ABNode implements MutableTreeNode {
		
		
		public DefaultMutableTreeNode node;
		
		public ABNode(String type, Point score) {
			node = new DefaultMutableTreeNode();
			
			switch (type) {
			case "leaf": {
				node.setAllowsChildren(false);
				node.setUserObject(score);
				break;
			}
			case "root": {
				//this.node.setAllowsChildren(true);

				//this.node.setUserObject(null);
				break;
			}
			case "min": {
				node.setAllowsChildren(true);
				node.setUserObject(null);
				break;
			}
			case "max": {
				node.setAllowsChildren(true);
				node.setUserObject(null);
				break;
			}
			
			default: {
				break;
			}
			}
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

		@Override
		public TreeNode getChildAt(int childIndex) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getChildCount() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public TreeNode getParent() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getIndex(TreeNode node) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public boolean getAllowsChildren() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isLeaf() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Enumeration children() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void insert(MutableTreeNode child, int index) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void remove(int index) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void remove(MutableTreeNode node) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setUserObject(Object object) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void removeFromParent() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setParent(MutableTreeNode newParent) {
			// TODO Auto-generated method stub
			
		}
	}
	
	public ABPrune() {
		
		ArrayList array = new ArrayList();
		for (int x = 1; x < 10; x++) {
			array.add(new Point(x,x,x));
		}
		root = new ABNode("root", null);
		
		root.node.insert(new ABNode("min",null),0);
		root.node.insert(new ABNode("min",null),1);

		System.out.println(root.node.getChildCount());
		
		
		
		// TODO Auto-generated constructor stub
	}
	
	public static void main (String args[]) {
		ABPrune prune = new ABPrune();
		
		
		//Point bestMove = root.Prune();
		
	}
	
}
