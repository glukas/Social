package ch.ethz.inf.vs.android.glukas.project4;

import java.util.List;

import ch.ethz.glukas.orderedset.RandomizedBST;
import ch.ethz.glukas.orderedset.RankedTree;
import android.content.Context;
import android.widget.BaseAdapter;

/**
 * Provides an adapter for sorted items
 * The adapter is backed by a data-structure that allows quick insertion & lookup by position (rank)
 * 
 * @param <T>
 */

public abstract class SortedSetAdapter<T extends Comparable<T>> extends BaseAdapter {
	
	protected final Context mContext;
	private final RankedTree<T> items = new RandomizedBST<T>();//a randomized binary search tree is a bit overkill, but it provides access by rank, and it scales to hundreds of thousands of posts (in expectation) :)
	
	public void add(T item) {
		items.add(item);
		this.notifyDataSetChanged();
	}
	
	public SortedSetAdapter(Context context, List<T> objects) {
		mContext = context;
		for (T item : objects) {
			this.items.add(item);
		}
		this.notifyDataSetChanged();
	}

	@Override
	public T getItem(int position) {
		return items.get(position);
	}

	@Override
	public int getCount() {
		return this.items.size();
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).hashCode();
	}
	
	@Override
	public boolean hasStableIds() {
		return true;
	}
	
}
