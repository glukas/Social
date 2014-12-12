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

import java.util.Iterator;
import java.util.ListIterator;
import java.util.NavigableSet;

public abstract class RankedTree<T> extends BinarySearchTree<T> implements RangeSet<T> {
	//Augments the binary search tree with dynamic order statistics
	//the implementer is responsible of maintaining the tree and especially the size counts
	//all nodes are assumed to conform to RankedTreeNode
	
	
	@Override
	protected TreeNode<T> newNode(T val)
	{
		return new RankedTreeNode<T>(val);
	}
	
	@Override
	public int size()
	{
		assert subtreeSizesConsistent();
		return ((RankedTreeNode<T>) metaRoot).size()-1;
	}
	
	
	/**
	 * Returns the k'th-smallest element from the set
	 */
	public T get(int index)
	{
		if (index < 0) throw new IndexOutOfBoundsException();
		if (index >= size()) throw new IndexOutOfBoundsException();
		assert subtreeSizesConsistent();
		
		TreeNode<T> result = getByRank(getRoot(), index);
		
		assert checkInvariants();
		
		if (result == null) return null;
		return result.getValue();
	}
	
	/**
	 * Retrieves and removes the k'th smallest element from the set
	 * @param index
	 * @return  the value that has been removed
	 * @throws IndexOutOfBoundsException
	 */
	public T poll(int index)
	{
		assert subtreeSizesConsistent();
		
		T value = get(index);
		remove(value);
		assert (!contains(value));
		return value;
	}
	
	/**
	 * Removes the k'th smallest element from the set
	 * @param index
	 * @throws IndexOutOfBoundsException
	 */
	public void remove(int index)
	{
		poll(index);
		assert checkInvariants();
	}
	
	
	/**
	 * If 'value' is the k'th smallest element in the set, this method returns 'k'
	 * @param value
	 * @return the rank of 'value'
	 */
	public int indexOf(T value)
	{
		return internalIndexOf(value, getRoot());
	}
	
	/**
	 * Since values are unique, this method is equivalent to indexOf
	 * @param value
	 * @return the rank of 'value'
	 */
	public int lastIndexOf(T value)
	{
		return indexOf(value);
	}
	
	
	
	@Override
	public int sizeOfRange(T lowerbound, T upperbound, boolean fromInclusive, boolean toInclusive) {
		if (compareValues(lowerbound, upperbound) > 0) throw new IllegalArgumentException();
		assert subtreeSizesConsistent();
		assert listIteratorConsistent();
		
		T lower;
		T upper;
		if (fromInclusive) {
			lower = ceiling(lowerbound);
		} else {
			lower = higher(lowerbound);
		}
		if (toInclusive) {
			upper = floor(upperbound);
		} else {
			upper = lower(upperbound);
		}
		if (compareValues(lower, upper) > 0) return 0;
		
		int lowerIndex = indexOf(lower);
		int upperIndex = indexOf(upper);
		
		return upperIndex-lowerIndex+1;
	}


	@Override
	public void removeRange(T lowerbound, T upperbound, boolean fromInclusive, boolean toInclusive) {
		assert subtreeSizesConsistent();
		//could be done faster, but this is easy
		
		NavigableSet<T> subset = subSet(lowerbound, fromInclusive, upperbound, toInclusive);
		while(subset.pollFirst() != null) {
		}
	}

	
	public ListIterator<T> listIterator()
	{
		return listIterator(0);
	}
	
	public ListIterator<T> listIterator(int index)
	{
		assert subtreeSizesConsistent();
		if (index >= size()) return  new RangeSetIterator<T>();
		return new RangeSetIterator<T>(this, get(index), last());
	}
	
	/////
	//NAVIGABLE SET
	/////
	
	
	public T pollFirst()
	{
		if (isEmpty()) return null;
		T first = first();
		remove(first);
		return first;
	}
	
	public T poll()
	{
		return pollFirst();
	}
	
	public T pollLast()
	{
		if (isEmpty()) return null;
		T last = last();
		remove(last);
		return last;
	}
	
	
	//Non-destructive subset methods : returned sets are backed by this set so changes in one set are reflected in the other set

	@Override
	public NavigableSet<T> headSet(T toElement) {
		return headSet(toElement, false);
	}


	@Override
	public NavigableSet<T> headSet(T toElement, boolean inclusive) {
		return new SortedSubset<T>(this, null, toElement, false, inclusive);
	}
	
	
	@Override
	public NavigableSet<T> tailSet(T fromElement) {
		return tailSet(fromElement, true);
	}


	@Override
	public NavigableSet<T> tailSet(T fromElement, boolean inclusive) {
		return new SortedSubset<T>(this, fromElement, null, inclusive, false);
	}


	@Override
	public NavigableSet<T> subSet(T fromElement, T toElement) {
		return subSet(fromElement, true, toElement, false);
	}


	@Override
	public NavigableSet<T> subSet(T fromElement, boolean fromInclusive, T toElement, boolean toInclusive) {
		assert subtreeSizesConsistent();
		return new SortedSubset<T>(this, fromElement, toElement, fromInclusive, toInclusive);
	}
	
	
	////
	//DESCENDING SET
	////
	
	@Override
	public Iterator<T> descendingIterator() {
		//TODO: test
		return new BinarySearchTreeIterator<T>(getRoot(), true);//use the fast BST iterator instead of the slower iterator of the descending set
	}

	@Override
	public NavigableSet<T> descendingSet() {
		return new DescendingSet<T>(this);
	}
	
	
	////
	//IMPLEMENTATION
	////
	
	
	//index is 0 based: smallest element has index '0'
	protected TreeNode<T> getByRank(TreeNode<T> root, int index)
	{
		assert subtreeSizesConsistent();
		assert index < size(root);
		
		
		int leftChildren = size(root.getLeftChild());
		
		//base case
		if (leftChildren == index) return root;
		
		//recursive step
		if (index < leftChildren) {
			return getByRank(root.getLeftChild(), index);
		} else {
			return getByRank(root.getRightChild(), index-leftChildren-1);
		}
		
	}
	
	
	//returns the index of the value relative to the current node, or -1 if the value is not present
	protected int internalIndexOf(T value, TreeNode<T> current)
	{
		assert subtreeSizesConsistent();
		//base case 1 : not found
		if (current == null) 
		{
			return -1;
		}
		
		int indexOfValue = 0;
		
		int comparison = compareValues(value, current.getValue());
		
		if (comparison < 0) {//continue searching left
			indexOfValue = internalIndexOf(value, current.getLeftChild());
		} else if (comparison > 0) {//continue searching right
			indexOfValue = internalIndexOf(value, current.getRightChild());
			if (indexOfValue != -1) {//the index is relative to the index of the child: calculate index relative to the current node
				indexOfValue += size(current.getLeftChild())+1;
			}
		} else {//base case 2: found
			assert comparison == 0;
			indexOfValue = size(current.getLeftChild());
		}
		
		assert checkInvariants();
		return indexOfValue;
	}
	

	//all nodes of this tree are ranked
	protected int size(TreeNode<T> node)
	{
		if (node == null) return 0;
		return ((RankedTreeNode<T>)node).size();
	}

	///
	//INVARIANTS
	///
	
	@Override
	protected boolean checkInvariants()
	{
		boolean result = super.checkInvariants();
		result = result && subtreeSizesConsistent();
		assert result;
		return result;
	}
	
	
	protected boolean listIteratorConsistent()
	{
		ListIterator<T> listIterator = listIterator();
		@SuppressWarnings("unchecked")
		T[] ordered = (T[]) toArray();
		assert ordered.length == size();
		
		boolean consistent = true;
		for(int i=0; i<ordered.length; i++) {
			T val2 = listIterator.next();
			consistent = consistent && compareValues(ordered[i], val2) == 0;
			assert consistent;//fail fast
		}
		consistent = consistent && !listIterator.hasNext();
		assert consistent;
		return consistent;
	}
	
	protected boolean subtreeSizesConsistent()
	{
		return subtreeSizeConsistent(metaRoot);
	}

	protected boolean subtreeSizeConsistent(TreeNode<T> node)
	{
		Out<Boolean> consistent = new Out<Boolean>();
		subtreeSize(node, consistent);
		return consistent.get();
	}
	
	
	//count size of subtree by exhaustion, place into consistent if the values agree with the cached values
	protected int subtreeSize(TreeNode<T> node, Out<Boolean> consistent)
	{
		if (node == null) {
			if (consistent.get() == null) consistent.set(true);
			return 0;
		}
		
		int total = 1 + subtreeSize(node.getLeftChild(), consistent) + subtreeSize(node.getRightChild(), consistent);
		consistent.set(consistent.get() && total == size(node));		
		assert consistent.get();
		return total;
	}
	
	
}
