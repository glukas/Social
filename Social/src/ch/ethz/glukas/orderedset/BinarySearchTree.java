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

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;


/**
 * 
 * An abstract Binary Search Tree
 * Provides find operations, iteration and splits
 * 
 * @author Lukas Gianinazzi
 *
 * @param <T>
 */

abstract class BinarySearchTree<T> extends AbstractCollection<T> implements Set<T>{
	
	
	///
	//CONSTRUCTION
	//clear() is guaranteed to be called by the constructors
	///
	
	protected BinarySearchTree(Comparator<? super T> comparator)
	{
		clear();
		internalComparator = comparator;
	}
	
	protected BinarySearchTree()
	{
		clear();
		internalComparator = new Comparator<T>() {
			@SuppressWarnings("unchecked")
			@Override
			public int compare(T arg0, T arg1) {
				return ((Comparable<T>)arg0).compareTo(arg1);
			}
		};
		assert checkInvariants();
	}
	
	protected TreeNode<T> newNode(T val)
	{
		return new TreeNode<T>(val);
	}
	
	
	/////
	//ABSTRACT COLLECTION
	////
	
	
	@Override
	//returns false if arg0 == null
	//to implement this method, override internalContains
	//the trivial cases arg0==null and isEmpty() are taken care of here, so these special cases need not be addressed in the implementation
	public boolean contains(Object arg0) {
		if (arg0 == null || isEmpty()) return false;
		
		@SuppressWarnings("unchecked")
		T value = (T)arg0;
		boolean contains = internalContains(value);
		assert checkInvariants();
		return contains;
	}
	
	@Override
	//throws if val == null
	public boolean add(T val)
	{
		if (val == null) throw new IllegalArgumentException();
		
		boolean modified = internalAdd(val);
		
		assert checkInvariants();
		assert contains(val);
		return modified;
	}
	
	
	@Override
	//returns null if val == null
	//to implement this method, override internalRemove
	public boolean remove(Object val)
	{
		if (val == null) return false;
		@SuppressWarnings("unchecked")
		T value = (T)val;
		boolean modified = internalRemove(value);
		
		assert checkInvariants();
		assert !contains(val);
		return modified;
	}
	

	


	@Override
	//fast readonly iterator
	public Iterator<T> iterator() {
		return new BinarySearchTreeIterator<T>(getRoot());
	}
	
	
	@Override
	public void clear() {
		metaRoot = newNode(null);
		assert checkInvariants();
	}
	
	////
	//ORDERING
	////

	
	public T first() {
		if (isEmpty()) throw new NoSuchElementException();
		return internalFirst(getRoot());
	}
	
	
	public T last() {
		if (isEmpty()) throw new NoSuchElementException();
		return internalLast(getRoot());
	}
	
	//TODO : comparator should return null if instantiated using the natural ordering?
	public Comparator<? super T> comparator() {
		return internalComparator;
	}

	
	/////
	//IMPLEMENTATION :: MODIFY
	////
	
	protected boolean internalAdd(T value) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}
	
	protected boolean internalRemove(T value)  throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}
	
	
	/////
	//IMPLEMENTATION :: TREE ROTATIONS
	/////
	
	protected void treeRotate(TreeNode<T> child, TreeNode<T> parent, int parity)
	{
		assert parent.childDirection(child) == parity;
		
		parent.setChild(parity, child.getChild(-parity));
		child.setChild(-parity, parent);
	}
	
	protected void treeRotateUp(TreeNode<T> child, TreeNode<T> parent, TreeNode<T> grandmother)
	{
		assert child != parent;
		assert parent != grandmother;
		
		
		treeRotate(child, parent, parent.childDirection(child));
		grandmother.replaceChild(parent, child);
		
		assert child.getLeftChild() != child.getRightChild();
		assert grandmother.getLeftChild() == child || grandmother.getRightChild() == child;
		assert child.getLeftChild() == parent || child.getRightChild() == parent;
		
		assert checkInvariants();
	}
	
	
	
	//////
	///IMPLEMENTATION :: FIND
	//////

	/**
	 * Returns if the value is contained in the BST
	 */
	protected boolean internalContains(T value)
	{
		return findNodeWithValueStartingFrom(metaRoot, value) != null;
	}
	
	//nullsafe: if node==null, returns null
	protected T internalLast(TreeNode<T> node)
	{
		if (node == null) return null;
		return findLast(node).getValue();
	}
	
	//nullsafe: if node==null, returns null
	protected T internalFirst(TreeNode<T> node)
	{
		if (node == null) return null;
		return findFirst(node).getValue();
	}
	
	/**
	 * Returns the node with the smallest value (the leftmost node in the subtree rooted at 'node')
	 * precondition: node != null
	 */
	protected TreeNode<T> findFirst(TreeNode<T> node)
	{
		while (node.getLeftChild() != null) {
			node = node.getLeftChild();
		}
		return node;
	}
	
	/**
	 * Returns the node with the largest value (the rightmost node in the subtree rooted at 'node')
	 * precondition: node != null
	 */
	protected TreeNode<T> findLast(TreeNode<T> node)
	{
		while (node.getRightChild() != null) {
			node = node.getRightChild();
		}
		return node;
	}
	
	protected TreeNode<T> successor(TreeNode<T> node)
	{
		if (node == null) return null;
		if (node.getRightChild() == null) return null;
		return findFirst(node.getRightChild());
	}
	
	protected TreeNode<T> predecessor(TreeNode<T> node)
	{
		if (node == null) return null;
		if (node.getLeftChild() == null) return null;
		return findLast(node.getLeftChild());
	}

	//the first element of the list is always the metaRoot
	//else if the value is present in the collection, the last element of the returned list is the node containing the value
	//else the last element of the list is the next highest node
	protected ArrayList<TreeNode<T>> find(T value)
	{
		return traceNodeWithValueStartingFrom(metaRoot, value);
	}
	
	protected Buffer<TreeNode<T>> find(T value, int capacity)
	{
		return traceNodeWithValueStartingFrom(metaRoot, value, capacity);
	}
	
	protected TreeNode<T> findNodeWithValueStartingFrom(TreeNode<T> currentNode, T valueToFind)
	{
		int comparison;
		while (currentNode != null) {
			comparison = compareValues(valueToFind, currentNode);
			if (comparison == 0) {
				break;
			} else {
				currentNode = currentNode.getChild(comparison);
			}
		}
		return currentNode;
	}
	
	
	//result contains 2 elements: the successors parent at 0 and the successor at 1
	protected Buffer<TreeNode<T>> findSuccessor(TreeNode<T> node)
	{
		assert node.getRightChild() != null;
		
		Buffer<TreeNode<T>> trace = traceNodeWithValueStartingFrom(node.getRightChild(), node.getValue(), 2);
		if (trace.numberOfUsedSlots() == 1) {//if the successor is the immediate right child, the node will need to be added to the trace
			TreeNode<T> successor = trace.get(0);
			trace.add(node);
			trace.add(successor);
		}
		return trace;
	}
	
	
	//Every call to this method invalidates all previous traces
	//TODO: this is potentially dangerous
	protected ArrayList<TreeNode<T>> traceNodeWithValueStartingFrom(TreeNode<T> startingNode, T valueToFind)
	{
		//ArrayList<TreeNode<T>> trace = new ArrayList<TreeNode<T>>();
		ArrayList<TreeNode<T>> trace = reusableTrace;
		trace.clear();
		TreeNode<T> currentNode = startingNode;
		
		int comparison;
		
		while (currentNode != null) {
			trace.add(currentNode);
			
			comparison = compareValues(valueToFind, currentNode);
			if (comparison == 0) {
				break;
			} else {
				currentNode = currentNode.getChild(comparison);
			}
		}
		
		return trace;
	}
	
	protected Buffer<TreeNode<T>> traceNodeWithValueStartingFrom(TreeNode<T> startingNode, T valueToFind, int maximumParentBufferSize)
	{

		Buffer<TreeNode<T>> trace = new Buffer<TreeNode<T>>(maximumParentBufferSize);

		TreeNode<T> currentNode = startingNode;
		int comparison = -1;
		while (currentNode != null) {
			trace.add(currentNode);
			comparison = compareValues(valueToFind, currentNode);
			if (comparison == 0) {
				break;
			} else {
				currentNode = currentNode.getChild(comparison);
			}
		}
		return trace;
	}
	
	
	////
	//IMPLEMENTATION : SPLIT
	///
	
	protected TreeNode<T> split(T value, TreeNode<T> r, Out<TreeNode<T>> less, Out<TreeNode<T>> greater)
	{
		assert isInOrder(r);

		if (r == null) {//base case 1
			less.set(null);
			greater.set(null);
			return null;
		}

		int comparison = compareValues(value, r);
		
		TreeNode<T> equal = null;
		if (comparison < 0) {
			
			equal = split(value, r.getLeftChild(), less, greater);
			r.setLeftChild(greater.get());
			greater.set(r);
			
		} else if (comparison > 0) {
			equal = split(value, r.getRightChild(), less, greater);
			r.setRightChild(less.get());
			less.set(r);
			
		} else {//base case 2
			equal = r;
			less.set(r.getLeftChild());
			greater.set(r.getRightChild());
		}
		
		assert descendantsAreSmaller(less.get(), value);
		assert descendantsAreGreater(greater.get(), value);
		assert isInOrder(less.get());
		assert isInOrder(greater.get());
		return equal;
	}
	
	/////
	//IMPLEMENTATION : HELPER METHODS
	/////
	
	protected T valueOrNull(TreeNode<T> node)
	{
		if (node == null) return null;
		return node.getValue();
	}
	
	protected TreeNode<T> getRoot()
	{
		return metaRoot.getLeftChild();
	}
	
	protected void setRoot(TreeNode<T> root)
	{
		assert isInOrder(root);
		metaRoot.setLeftChild(root);
		assert checkInvariants();
	}
	
	//wraps the comparator to make it partially null-safe: null is interpreted as +infinity
	//if both arguments are 0 the precondition is violated
	protected int compareValues(T v1 ,T v2) {
		assert v1 != null || v2 != null;
		if (v2 == null) return -1;
		if (v1 == null) return +1;
		return comparator().compare(v1, v2);
	}
	
	protected int compareValues(T v, TreeNode<T> n) {
		if (n == null) return -1;
		return compareValues(v, n.getValue());
	}
	
	protected int compareValues(TreeNode<T> n, T v) {
		if (n == null) return +1;
		return compareValues(n.getValue(), v);
	}
	
	protected int compareValues(TreeNode<T> n1, TreeNode<T> n2) {
		if (n2 == null) return -1;
		if (n1 == null) return +1;
		return compareValues(n1.getValue(), n2.getValue());
	}
	
	//swaps the elements at index1 and index2 of a list
	protected <S> void swap(List<S> list, int index1, int index2)
	{
		S temp = list.get(index1);
		list.set(index1, list.get(index2));
		list.set(index2, temp);
	}
	
	
	///
	//INVARIANTS & ASSERTIONS
	///
	
	protected boolean checkInvariants()
	{
		boolean result = isInOrder();
		assert result;
		result = result && sizeIsConsistent();
		assert result;
		return result;
	}
	
	protected boolean descendantsAreSmaller(TreeNode<T> node, T value)
	{
		if (value == null || node == null) return true;
		boolean result = compareValues(node.getValue(), value) < 0 && descendantsAreSmaller(node.getLeftChild(), value) && descendantsAreSmaller(node.getRightChild(), value);
		assert result;//fail fast
		return result;
	}
	
	protected boolean descendantsAreGreater(TreeNode<T> node, T value)
	{
		if (value == null || node == null) return true;
		boolean result = compareValues(node.getValue(), value) > 0 && descendantsAreGreater(node.getLeftChild(), value) && descendantsAreGreater(node.getRightChild(), value);
		assert result;//fail fast
		return result;
	}
	
	protected boolean isInOrder(TreeNode<T> node)
	{
		if (node == null) return true;
		
		
		boolean result = true;
		if (node.getLeftChild() != null) {
			result = result && compareValues(node.getLeftChild(), node) < 0 && isInOrder(node.getLeftChild());
			assert result;
		}
		if (node.getRightChild() != null) {
			result = result && compareValues(node.getRightChild(), node) > 0 && isInOrder(node.getRightChild());
			assert result;
		}
		return result;
	}
	
	protected boolean isInOrder()
	{
		return isInOrder(getRoot());
	}

	
	protected boolean sizeIsConsistent()
	{
		int actualSize = exhaustiveCount(getRoot());
		boolean result = size() == actualSize;
		assert result;
		return result;
	}
	
	protected int exhaustiveCount(TreeNode<T> node)
	{
		if (node == null) return 0;
		return exhaustiveCount(node.getLeftChild()) + exhaustiveCount(node.getRightChild()) + 1;
	}
	
	
	////
	//INSTANCE VARIABLES
	////
	

	
	private ArrayList<TreeNode<T>> reusableTrace = new ArrayList<TreeNode<T>>();
	private Comparator<? super T> internalComparator;
	protected TreeNode<T> metaRoot;
}
