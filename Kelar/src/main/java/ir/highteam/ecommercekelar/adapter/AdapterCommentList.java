package ir.highteam.ecommercekelar.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ir.highteam.ecommercekelar.R;
import ir.highteam.ecommercekelar.bundle.BundleComment;

/**
 * Created by mohammad on 5/6/2016.
 */
public class AdapterCommentList extends RecyclerView.Adapter<AdapterCommentList.CommentViewHolder> {

    private List<BundleComment> commentList;
    private Context contextMain;
    Typeface tfSans;

    public AdapterCommentList(List<BundleComment> commentList, Context context) {
        this.commentList = commentList;
        this.contextMain = context;
        tfSans = Typeface.createFromAsset(contextMain.getAssets(),context.getString(R.string.FONT_IRAN_SANS));
    }

    public AdapterCommentList(Context context) {
        commentList = new ArrayList<BundleComment>();
        this.contextMain = context;

    }

    public void insertItem(BundleComment bundleComment){
        commentList.add(bundleComment);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    @Override
    public void onBindViewHolder(AdapterCommentList.CommentViewHolder commentViewHolder, final int position) {
        BundleComment commentInfo = commentList.get(position);
        commentViewHolder.commentTitle.setText(commentInfo.title);
        commentViewHolder.commentContent.setText(commentInfo.comment);
        commentViewHolder.commentUsername.setText(commentInfo.username);
        commentViewHolder.commentTitle.setTypeface(tfSans);
        commentViewHolder.commentContent.setTypeface(tfSans);
        commentViewHolder.commentUsername.setTypeface(tfSans);
    }

    @Override
    public AdapterCommentList.CommentViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_comment_item, viewGroup, false);
        CommentViewHolder comment = new CommentViewHolder(itemView);
        return comment;
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {

        protected TextView commentTitle;
        protected TextView commentContent;
        protected TextView commentUsername;

        public CommentViewHolder(View v) {
            super(v);

            contextMain = v.getContext();
            commentTitle = (TextView) v.findViewById(R.id.txtTitle);
            commentContent = (TextView) v.findViewById(R.id.txtComment);
            commentUsername = (TextView) v.findViewById(R.id.txtName);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

        }

    }
}