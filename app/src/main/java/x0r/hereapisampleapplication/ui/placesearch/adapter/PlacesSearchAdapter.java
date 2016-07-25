package x0r.hereapisampleapplication.ui.placesearch.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import x0r.hereapisampleapplication.R;
import x0r.hereapisampleapplication.model.Suggestion;

/**
 *
 * Adapter used to presents a list of {@link Suggestion}
 *
 */
public class PlacesSearchAdapter extends RecyclerView.Adapter<PlacesSearchAdapter.ViewHolder> implements View.OnClickListener {

    private ClickListener mClickListener;
    private List<Suggestion> mSuggestions;

    public PlacesSearchAdapter(ClickListener clickListener) {
        mSuggestions = new ArrayList<>();
        mClickListener = clickListener;
    }

    public void updateData(List<Suggestion> suggestions) {
        this.mSuggestions.clear();

        if(suggestions != null) {
            this.mSuggestions.addAll(suggestions);
        }

        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place_search_list, parent, false);
        view.setOnClickListener(this);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Suggestion item = getItem(position);

        holder.title.setText(item.getTitle());
        ((ViewGroup)holder.title.getParent()).setTag(item);
    }

    @Override
    public int getItemCount() {
        return mSuggestions.size();
    }

    private Suggestion getItem(int position) {
        return mSuggestions.get(position);
    }

    @Override
    public void onClick(View view) {
        mClickListener.onItemClick((Suggestion) view.getTag());
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ClickListener {
        void onItemClick(Suggestion suggestion);
    }
}
