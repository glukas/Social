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
import java.util.NavigableSet;

class NavigableSetIterator<T> implements Iterator<T> {

	
	//the empty iterator
	public NavigableSetIterator()
	{
		lowerbound = null;
		upperbound = null;
		internalSet = null;
	}
	
	//both first and last are inclusive bounds. 'first' is the first, 'last' the last element returned by the iterator
	//precondition: set.contains(first), set.contains(last);
	public NavigableSetIterator(NavigableSet<T> set, T first, T last)
	{
		if (first == null) throw new NullPointerException();
		if (last == null) throw new NullPointerException();
		assert set.contains(first);
		assert set.contains(last);
		internalSet = set;
		lowerbound = first;
		upperbound = last;
		current = first;
	}
	

	private void advance()
	{
		current = internalSet.higher(current);
	}

	///
	//LIST INTERFACE
	///
	
	@Override
	public boolean hasNext() {
		if (internalSet == null) return false;
		if (current == null) return false;
		return internalSet.comparator().compare(current, upperbound) <= 0;
	}
	
	@Override
	public T next() {
		T result = current;
		advance();
		return result;
	}
	
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
	
	////
	//INSTANCE VARIABLES
	////
	
	private T current;
	private final NavigableSet<T> internalSet;
	private final T upperbound;
	private final T lowerbound;
}
