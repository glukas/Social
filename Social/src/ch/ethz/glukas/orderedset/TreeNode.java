package ch.ethz.glukas.orderedset;

/*
Copyright (c) 2013, Lukas Gianinazzi (glukas@student.ethz.ch)
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met: 

1. Redistributions of source code must retain the above copyright notice, this
   list of conditions and the following disclaimer. 
2. Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution. 

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.*/

class TreeNode<T> {

	
	/////
	//CONSTRUCTION
	/////
	
	public TreeNode(T value)
	{
		this.value = value;
	}

	/////
	//GET - HAS
	/////

	public T getValue()
	{
		return value;
	}
	

	
	//if the argument is negative, the leftChild is returned, else the right child is returned
	public TreeNode<T> getChild(int parity)
	{
		if (parity < 0) {
			return getLeftChild();
		} else {
			return getRightChild();
		}
	}
	
	/**
	 * if the argument is negative, the leftChild is set, else the right child is set
	 * @param parity
	 * @param child
	 */
	public void setChild(int parity, TreeNode<T> child)
	{
		if (parity < 0) {
			setLeftChild(child);
		} else {
			setRightChild(child);
		}
	}
	
	
	public TreeNode<T> getLeftChild()
	{
		return leftChild;
	}
	
	public TreeNode<T> getRightChild()
	{
		return rightChild;
	}
	
	public boolean hasChildren()
	{	
		return !(leftChild == null && rightChild == null);
	}
	
	////
	//MODIFY
	////
	
	
	public void setRightChild(TreeNode<T> right)
	{
		rightChild = right;
	}
	
	public void setLeftChild(TreeNode<T> left)
	{
		leftChild = left;
	}
	
	public void setValue(T newValue)
	{
		value = newValue;
	}

	
	/**
	 * sets the children relationship. left and right can be null. 
	 * adjusts the left and right children's parent relationship
	 * @param left
	 * @param right
	 */
	public void addChildren(TreeNode<T> left, TreeNode<T> right)
	{
		assert left != this && right != this;;
		
		setLeftChild(left);
		setRightChild(right);
	}
	

	public void replaceChild(TreeNode<T> oldChild, TreeNode<T> newChild) {
		
		if (oldChild == leftChild) {
			setLeftChild(newChild);
		} else {
			assert oldChild == rightChild || this.value == null;
			setRightChild(newChild);
		}
	}

	//precondition: child is a child of parent
	//returns -1 if left child, 1 if right child
	public int childDirection(TreeNode<T> child)
	{
		if (leftChild == child) {
			return -1;
		} else {
			assert rightChild == child;
			return 1;
		}
	}
	
	
	/**
	 * removes children and parent relationship
	 * does not modify any nodes except this one
	 */
	public void isolate()
	{
		setLeftChild(null);
		setRightChild(null);
	}
	
	///
	//OVERRIDING OBJECT
	////
	
	@Override
	public String toString() {
		if (getValue() == null) return "(Null)";
		return getValue().toString();
	}
	
	/////
	//INSTANCE VARIABLES
	/////
	
	private TreeNode<T>leftChild;
	private TreeNode<T>rightChild;
	
	private T value;
}
