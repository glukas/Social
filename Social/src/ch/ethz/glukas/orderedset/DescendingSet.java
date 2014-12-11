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
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.SortedSet;

//simple wrapper that provides a reverse order view to a navigable set
//assumes that the comparator of the undelying set in non-null
public class DescendingSet<T> extends AbstractCollection<T> implements NavigableSet<T>{

	public DescendingSet(NavigableSet<T> set)
	{
		if (set == null) throw new NullPointerException();
		implementingSet = set;
	}
	

	@Override
	public Comparator<? super T> comparator() {
		if (internalComparator == null && implementingSet.comparator() != null) {
			internalComparator = new Comparator<T>() {
				@Override
				public int compare(T arg0, T arg1) {
					return implementingSet.comparator().compare(arg1, arg0);
				}
			};
		}
		return internalComparator;
	}


	@Override
	public T first() {
		return implementingSet.last();
	}


	@Override
	public T last() {
		return implementingSet.first();
	}


	@Override
	public boolean add(T e) {
		return implementingSet.add(e);
	}

	
	@Override
	public void clear() {
		implementingSet.clear();
	}

	
	@Override
	public boolean contains(Object o) {
		return implementingSet.contains(o);
	}


	@Override
	public boolean remove(Object o) {
		return implementingSet.remove(o);
	}
	

	@Override
	public int size() {
		return implementingSet.size();
	}

	
	@Override
	public T ceiling(T arg0) {
		return implementingSet.floor(arg0);
	}


	@Override
	public Iterator<T> descendingIterator() {
		return implementingSet.iterator();
	}


	@Override
	public NavigableSet<T> descendingSet() {
		return implementingSet;
	}


	@Override
	public T floor(T arg0) {
		return implementingSet.ceiling(arg0);
	}

	
	@Override
	public SortedSet<T> headSet(T to) {
		return headSet(to, false);
	}


	@Override
	public NavigableSet<T> headSet(T to, boolean inclusive) {
		return implementingSet.tailSet(to, inclusive).descendingSet();
	}


	@Override
	public T higher(T arg0) {
		return implementingSet.lower(arg0);
	}


	@Override
	public Iterator<T> iterator() {
		return new NavigableSetIterator<T>(this, first(), last());
	}


	@Override
	public T lower(T arg0) {
		return implementingSet.higher(arg0);
	}


	@Override
	public T pollFirst() {
		return implementingSet.pollLast();
	}


	@Override
	public T pollLast() {
		return implementingSet.pollFirst();
	}


	@Override
	public SortedSet<T> subSet(T from, T to) {
		return subSet(from, true, to, false);
	}


	@Override
	public NavigableSet<T> subSet(T from, boolean fromInclusive, T to, boolean toInclusive) {
		return implementingSet.subSet(to, toInclusive, from, fromInclusive).descendingSet();
	}


	@Override
	public SortedSet<T> tailSet(T from) {
		return tailSet(from, true);
	}


	@Override
	public NavigableSet<T> tailSet(T from, boolean inclusive) {
		return implementingSet.headSet(from, inclusive).descendingSet();
	}
	
	
	/////
	//instance variables
	////
	
	private NavigableSet<T> implementingSet;
	private Comparator<? super T> internalComparator;
	
}
