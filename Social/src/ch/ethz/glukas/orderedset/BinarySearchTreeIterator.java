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

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.Stack;

public class BinarySearchTreeIterator<T> implements Iterator<T> {

	public BinarySearchTreeIterator(TreeNode<T> node, boolean descending) {
		init(node, descending);
	}
	
	public BinarySearchTreeIterator(TreeNode<T> node) {
		init(node, false);
	}
	
	private void init(TreeNode<T> node, boolean descending)
	{
		if (descending) {
			parity = 1;
		} else {
			parity = -1;
		}
		push(node);
	}

	@Override
	public boolean hasNext() {
		return !deque.isEmpty();
	}

	@Override
	public T next() {
		
		TreeNode<T> current = deque.pop();
		push(current.getChild(-parity));
		return current.getValue();
	}
	
	@Override
	public void remove() {
		throw new UnsupportedOperationException();	
	}
	
	private void push(TreeNode<T> node)
	{
		while(node != null) {
			deque.push(node);
			node = node.getChild(parity);
		}
	}

	Deque<TreeNode<T>> deque = new ArrayDeque<TreeNode<T>>();
	int parity;//if parity is negative the left subtree is traversed first. otherwise the right subtree is traversed first ('descending order')
}
