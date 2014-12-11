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


import java.util.ListIterator;
import java.util.NavigableSet;

class RangeSetIterator<T> implements ListIterator<T> {
	//Algorithm: repeated search by rank
	
	
	//the empty iterator
	public RangeSetIterator()
	{
	}
	
	
	//precondition: set contains first and last, first smaller than or equal to last
	public RangeSetIterator(RangeSet<T> set, T first, T last)
	{
		assert set.contains(first);
		assert set.contains(last);
		internalSet = set;
		lowerbound = first;
		upperbound = last;
		setIndexBounds();
		currentIndex = lowestIndex;
		
	}
	
	private void setIndexBounds()
	{
		lowestIndex = internalSet.indexOf(lowerbound);
		highestIndex = internalSet.indexOf(upperbound);
		assert lowestIndex <= highestIndex;
	}

	private void advance()
	{
		currentIndex++;
	}
	
	private void retreat()
	{
		currentIndex--;
	}
	
	
	///
	//LIST INTERFACE
	///
	
	@Override
	public boolean hasNext() {
		if (internalSet == null) return false;
		return currentIndex <= highestIndex;
	}

	@Override
	public boolean hasPrevious() {
		if (internalSet == null) return false;
		return currentIndex > lowestIndex;
	}

	@Override
	public T next() {
		T result = internalSet.get(currentIndex);
		advance();
		return result;
	}
	
	@Override
	public int nextIndex() {
		int result = currentIndex;
		advance();
		return result;
	}

	@Override
	public T previous() {
		retreat();
		return internalSet.get(currentIndex);
	}

	@Override
	public int previousIndex() {
		retreat();
		return currentIndex;
	}

	
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void set(T arg0) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void add(T arg0) {
		throw new UnsupportedOperationException();
	}
	
	////
	//INSTANCE VARIABLES
	////
	
	private int currentIndex;
	private int lowestIndex;
	private int highestIndex;
	private RangeSet<T> internalSet;
	private T upperbound;
	private T lowerbound;
}
