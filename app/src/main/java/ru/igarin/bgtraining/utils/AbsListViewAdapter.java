package ru.igarin.bgtraining.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class AbsListViewAdapter<ItemType, ItemViewHolder> extends BaseAdapter {
    protected static final int INVALID_POSITION = -1;

    protected final Context mContext;
    protected final LayoutInflater mLayoutInflater;

    protected final List<ItemType> mItemData = new ArrayList<ItemType>();

    public AbsListViewAdapter(final Context context) {
        super();

        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mItemData.size();
    }

    @Override
    public ItemType getItem(final int position) {
        return mItemData != null && mItemData.size() > position ? mItemData.get(position) : null;
    }

    public List<ItemType> getItems() {
        return mItemData;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        if (shouldCreateView(position, convertView, parent)) {
            convertView = createView(position, parent);
            convertView.setTag(createViewHolder(position, convertView, parent));
        }

        bindView(position, convertView, parent, getItemViewHolder(convertView));

        return convertView;
    }

    protected boolean shouldCreateView(final int position, final View convertView, final ViewGroup parent) {
        return convertView == null || shouldReCreateView(position, convertView, parent);
    }

    protected boolean shouldReCreateView(final int position, final View convertView, final ViewGroup parent) {
        return false;
    }

    @SuppressWarnings("unchecked")
    protected ItemViewHolder getItemViewHolder(final View view) {
        return view == null || view.getTag() == null ? null : (ItemViewHolder) view.getTag();
    }

    public void updateList(final ItemType[] itemsData) {

        mItemData.clear();

        if (!isEmpty(itemsData)) {
            for (ItemType t : itemsData) {
                mItemData.add(t);
            }
        }

        notifyDataSetChanged();
    }

    boolean isEmpty(final ItemType[] collection) {
        return collection == null || collection.length == 0;
    }

    /**
     * Create view for element at given position if it has not been created
     * before
     *
     * @param position element position
     * @param parent   parent element
     * @return view for list element
     */
    protected abstract View createView(final int position, final ViewGroup parent);

    /**
     * Creates view holder (to set as Tag)
     *
     * @param position element position
     * @param view     fresh created view
     * @param parent   parent element
     * @return holder for view
     */
    protected abstract ItemViewHolder createViewHolder(final int position, final View view, final ViewGroup parent);

    /**
     * Set values for view at given position
     *
     * @param position element position
     * @param view     item view
     * @param parent   parent element
     * @param holder   element holder
     */
    protected abstract void bindView(final int position, final View view, final ViewGroup parent,
                                     final ItemViewHolder holder);
}
