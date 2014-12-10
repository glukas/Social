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

public class Buffer<T> {

	
	public Buffer(int capacity)
	{
		numberOfSlotsUsed = 0;
		nextInsertionIndex = 0;
		bufferSize = capacity;
		buffer = new Object[capacity];
	}
	
	public void add(T value) {
		buffer[nextInsertionIndex] = value;
		nextInsertionIndex = (nextInsertionIndex+1)%bufferSize;
		if (size() == bufferSize) {
			currentZero = (currentZero+1);
		} else {
			numberOfSlotsUsed = numberOfSlotsUsed+1;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public T get(int index)
	{
		return (T)buffer[internalIndexForIndex(index)];
	}
	
	public int internalIndexForIndex(int index)
	{
		return (currentZero+index)%bufferSize;
	}
	
	public void swap(int index1, int index2)
	{
		T temp = get(index1);
		set(index1, get(index2));
		set(index2, temp);
	}
	
	public void set(int index, T value)
	{
		buffer[internalIndexForIndex(index)] = value;
	}
	
	public int size()
	{
		return numberOfSlotsUsed;
	}
	
	public int numberOfUsedSlots()
	{
		return numberOfSlotsUsed;
	}
	
	
	private int currentZero = 0;
	private int bufferSize;
	private int nextInsertionIndex;
	private Object[] buffer;
	private int numberOfSlotsUsed;
}
