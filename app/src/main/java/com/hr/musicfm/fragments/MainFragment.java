package com.hr.musicfm.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.hr.musicfm.net.*;

import com.facebook.drawee.view.SimpleDraweeView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.hr.musicfm.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public Context mContext;

    List<PlaylistItemsBean.ItemsBean> items = new ArrayList<>();

    RecyclerView recyclerView;
    PlaylistItemsAdapter adapter;

    private OnFragmentInteractionListener mListener;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_main, null, false);
        recyclerView = view.findViewById(R.id.recyclerview);
        adapter = new PlaylistItemsAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        getPlaylistItems();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void getPlaylistItems() {
        YoutubeService service = GenServiceUtil.createService(YoutubeService.class);
        Call<PlaylistItemsBean> call = service.playlistItems(20);
        call.enqueue(new Callback<PlaylistItemsBean>() {
            @Override
            public void onResponse(Call<PlaylistItemsBean> call, Response<PlaylistItemsBean> response) {
                PlaylistItemsBean bean = response.body();
                items = bean.getItems();
                adapter.updateAdapter(items);
            }

            @Override
            public void onFailure(Call<PlaylistItemsBean> call, Throwable t) {

            }
        });
    }

    class PlaylistItemsAdapter extends RecyclerView.Adapter<PlaylistItemsAdapter.StreamInfoItemHolder> {

        List<PlaylistItemsBean.ItemsBean> mList;

        public PlaylistItemsAdapter() {

        }

        public void updateAdapter(List<PlaylistItemsBean.ItemsBean> list) {
            mList = list;
            this.notifyDataSetChanged();
        }

        @Override
        public StreamInfoItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new StreamInfoItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.stream_item, parent, false));
        }


        @Override
        public void onBindViewHolder(StreamInfoItemHolder holder, int position) {

            PlaylistItemsBean.ItemsBean item = mList.get(position);
            holder.itemVideoTitleView.setText(item.getSnippet().getTitle());
            holder.itemUploaderView.setText("8M views");
//            holder.draweeView.setImageURI(Uri.parse(billboardInfo1.thumbnail));

        }

        @Override
        public int getItemCount() {
            return mList == null ? 5 : mList.size();
        }

        class StreamInfoItemHolder extends RecyclerView.ViewHolder {

            SimpleDraweeView itemThumbnailView;
            public final TextView itemVideoTitleView,
                    itemUploaderView,
                    itemDurationView,
                    itemAdditionalDetails;
            public final View itemRoot;

            public StreamInfoItemHolder(View itemView) {
                super(itemView);
                itemRoot = itemView.findViewById(R.id.itemRoot);
                itemThumbnailView = (SimpleDraweeView) itemView.findViewById(R.id.itemThumbnailView);
                itemVideoTitleView = (TextView) itemView.findViewById(R.id.itemVideoTitleView);
                itemUploaderView = (TextView) itemView.findViewById(R.id.itemUploaderView);
                itemDurationView = (TextView) itemView.findViewById(R.id.itemDurationView);
                itemAdditionalDetails = (TextView) itemView.findViewById(R.id.itemAdditionalDetails);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(getAdapterPosition() > -1){

                        }
                    }
                });
            }
        }
    }
}
