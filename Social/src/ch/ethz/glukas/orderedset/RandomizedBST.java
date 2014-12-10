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
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;

/**
 * 
 * Implements a randomized sorted set structure introduced by Conrado Martinez and Salvador Roura:
 * http://www.cis.temple.edu/~wolfgang/cis551/martinez.pdf
 * 
 * This class conforms to the java.util.NavigableSet interface. It is thus very similar in behavior to java.util.TreeSet, but it is more space efficient and provides access by rank.
 * 
 * Expected add, contains and remove performance is O(logn) for all input distributions
 * In addition to access by key, the structure provides O(logn) access and deletion by rank
 * 
 * 
 * @author Lukas Gianinazzi
 *
 */
public class RandomizedBST<T> extends RankedTree<T>{

	///
	//COLLECTION
	///
	
	protected boolean internalAdd(T value)
	{
		setRoot(internalAdd(value, getRoot(), lastOperationDidModify));
		return lastOperationDidModify.get();
	}
	
	
	protected boolean internalRemove(T value)
	{
		setRoot(internalRemove(value, getRoot(), lastOperationDidModify));
		return lastOperationDidModify.get();
	}

	////
	//NAVIGABLE SET
	///
	
	
	//NAVIGATE:
	//the algorithms are based on split and join operations
	//this maintains randomness and gives them O(log n) expected performance
	//Discussion: other methods such as threading or parent pointers might increase the performance, but increase complexity and overhead for other methods
	
	@Override
	public T floor(T e) {
		if (contains(e)) return e;
		return internalLower(e);
	}
	

	@Override
	public T lower(T e) {
		if (contains(e)) {
			int index = indexOf(e);
			if (!isEmpty() && index > 0) {
				return get(index-1);
			} else {
				return null;
			}
		} else {
			return internalLower(e);
		}
	}
	
	
	@Override
	public T ceiling(T e) {
		if (contains(e)) return e;
		return internalHigher(e);
	}

	@Override
	public T higher(T e) {
		if (contains(e)) {
			int index = indexOf(e);
			if (index < size()-1) {
				return get(index+1);
			} else {
				return null;
			}
		} else {
			return internalHigher(e);
		}
	}
	
	private T internalLower(T e)
	{
		Out<T> smallerValue = new Out<T>();
		getNeighborhood(e, smallerValue, null);
		return smallerValue.get();
	}
	
	private T internalHigher(T e)
	{
		Out<T> greaterValue = new Out<T>();
		getNeighborhood(e, null, greaterValue);
		return greaterValue.get();
	}
	
	
	///
	//DESTRUCTIVE SUBSET METHODS : elements are removed from this set and added to a new set
	///
	
	
	public RandomizedBST<T> cutHeadSet(T toElement, boolean inclusive)
	{
		//partition the tree around the value
		Out<TreeNode<T>> smaller = new Out<TreeNode<T>>();
		Out<TreeNode<T>> greater = new Out<TreeNode<T>>();
		TreeNode<T> equal = split(toElement, getRoot(), smaller, greater);
		
		//create the new set
		RandomizedBST<T> headSet = new RandomizedBST<T>();
		
		//assign the partitions of the split
		headSet.setRoot(smaller.get());
		setRoot(greater.get());
		if (equal != null) {
			if (inclusive) {
				headSet.add(equal.getValue());
			} else {
				add(equal.getValue());
			}
		}
		
		assert checkInvariants();
		return headSet;
	}
	
	
	public RandomizedBST<T> cutTailSet(T fromElement, boolean inclusive)
	{
		//partition the tree around the value
		Out<TreeNode<T>> smaller = new Out<TreeNode<T>>();
		Out<TreeNode<T>> greater = new Out<TreeNode<T>>();
		TreeNode<T> equal = split(fromElement, getRoot(), smaller, greater);
		
		//create the new set
		RandomizedBST<T> tailSet = new RandomizedBST<T>();
		
		//assign the partitions of the split
		tailSet.setRoot(greater.get());
		setRoot(smaller.get());
		if (equal != null) {
			if (inclusive) {
				tailSet.add(equal.getValue());
			} else {
				add(equal.getValue());
			}
		}
		
		assert checkInvariants();
		return tailSet;
	}
	
	////
	//IMPLEMENTATION
	////
	
	private TreeNode<T> internalAdd(T value, TreeNode<T> r, Out<Boolean> modified)
	{
		int size = size(r);

		int rand = random.nextInt(size+1);
		
		
		if (size == rand) {//base case: insert here, restructure r
			return insertAtRoot(value, r, modified);
		}
		
		int comparison = compareValues(value, r.getValue());
		
		if (comparison < 0) {
			r.setLeftChild(internalAdd(value, r.getLeftChild(),  modified));	
		} else if (comparison > 0) {
			r.setRightChild(internalAdd(value, r.getRightChild(),  modified));
		} else {//base case: already present
			modified.set(false);
		}
		
		return r;
	}
	
	

	//precondition: !contains(value)
	/*
	private void internalAdd(T value)
	{
		assert !contains(value);
		
		TreeNode<T> parent = metaRoot;
		TreeNode<T> current = getRoot();
		int rand = 0;
		int currentSize = 0;
		while (true) {
			
			currentSize = size(current);
			rand = random.nextInt(currentSize+1);
			
			if (currentSize == rand) break;
			
			
			int comparison = compareValues(value, current.getValue());
			//update size
			((RankedTreeNode<T>)current).setSize(currentSize + 1);
			//advance pointers
			parent = current;
			if (comparison < 0) {
				current = current.getLeftChild();
			} else if (comparison > 0) {
				current = current.getRightChild();
			}
			
		}
		assert parent != current;
		Out<Boolean> modified = new Out<Boolean>();
		TreeNode<T> newNode = insertAtRoot(value, current, modified);
		
		parent.replaceChild(current, newNode);
		assert contains(value);
		assert subtreeSizeConsistent(getRoot());
	}*/
	
	
	private TreeNode<T> internalRemove(T value, TreeNode<T> r, Out<Boolean> modified)
	{
		if (r == null) {//base case 1 : value is not present
			modified.set(false);
			return null; 
		}
		
		
		int comparison = compareValues(value, r.getValue());
		
		if (comparison < 0) {
			r.setLeftChild(internalRemove(value, r.getLeftChild(), modified));
		} else if (comparison > 0) {
			r.setRightChild(internalRemove(value, r.getRightChild(), modified));
		} else {//base case 2 : value is present : remove using join
			assert (value.equals(r.getValue()));
			modified.set(true);
			r = join(r.getLeftChild(), r.getRightChild());
		}
		
		return r;
	}
	
	//returns the value if it is contained in the structure, sets 'greater' to the next highest and 'smaller' to the next lowest value contained in the tree
	//smaller and greater can be null
	//Algorithm: split around the value and join the subtrees back
	public T getNeighborhood(T value, Out<T> smaller, Out<T> greater) {
		//partition tree around the value e
		TreeNode<T> equal = split(value, getRoot(), lesserTree, greaterTree);

		//extract result values if needed
		if (greaterTree.get() != null && greater != null) {
			greater.set(findFirst(greaterTree.get()).getValue());
		}
		if (lesserTree.get() != null && smaller != null) {
			smaller.set(findLast(lesserTree.get()).getValue());
		}
		
		//restore the tree
		TreeNode<T> newRoot = join(lesserTree.get(), greaterTree.get());
		setRoot(newRoot);
		if (equal != null) {
			add(value);
		}
		
		assert checkInvariants();
		return valueOrNull(equal);
	}
	


	
	//insert the value here: restructure the subtree rooted at 'r' so that value is the the root of this subtree, return the new root
	//if the value was already present, modified will be set to false, else if will be set to true
	private TreeNode<T> insertAtRoot(T value, TreeNode<T> r, Out<Boolean> modified)
	{
		TreeNode<T> equal = split(value, r, lesserTree, greaterTree);
		
		assert lesserTree.get() == null || compareValues(value, findLast(lesserTree.get()).getValue()) > 0;
		assert greaterTree.get() == null || compareValues(value, findFirst(greaterTree.get()).getValue()) < 0;
		
		
		if (equal == null) {
			modified.set(true);
			equal = newNode(value);
		} else {
			assert equal.getValue().equals(value);
			modified.set(false);
		}
		
		equal.setLeftChild(lesserTree.get());
		equal.setRightChild(greaterTree.get());
		
		assert subtreeSizeConsistent(equal);
		assert modified.get() != null;
		return equal;
	}

	
	//randomized join operation
	private TreeNode<T> join(TreeNode<T> L, TreeNode<T> R)
	{
		assert descendantsAreSmaller(L, valueOrNull(R));
		assert descendantsAreGreater(R, valueOrNull(L));
		
		int sizeL = size(L);
		int sizeR = size(R);
		int total = sizeL+sizeR;
		
		if (total == 0) return null;
		
		int r = random.nextInt(total);
		
		if (r < sizeL) {
			L.setRightChild(join(L.getRightChild(), R));
			return L;
			
		} else {
			R.setLeftChild(join(L, R.getLeftChild()));
			return R;
		}
		
	}
	
	
	///
	//INSTANCE VARIABLES
	///
	private Random random = new Random(91);
	private Out<Boolean> lastOperationDidModify = new Out<Boolean>();
	Out<TreeNode<T>> lesserTree = new Out<TreeNode<T>>();
	Out<TreeNode<T>> greaterTree = new Out<TreeNode<T>>();
	
}
