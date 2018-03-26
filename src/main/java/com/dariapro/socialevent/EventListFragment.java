package com.dariapro.socialevent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class EventListFragment extends Fragment {

    public static final int REQUEST_EVENT = 1;

    private RecyclerView mEventRecyclerView;
    private EventAdapter mEventAdapter;

    private int positionChanged = -1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_list, container,false);
        mEventRecyclerView = view.findViewById(R.id.event_recycler_view);
        mEventRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        updateUI();
        super.onResume();
    }

    private void updateUI(){
        EventLab eventLab = EventLab.get(getActivity());
        List mEvents = eventLab.getEvents();

        if(mEventAdapter == null){
            mEventAdapter = new EventAdapter(mEvents);
            mEventRecyclerView.setAdapter(mEventAdapter);
        }
        else{
            //mEventAdapter.notifyDataSetChanged();
            if(positionChanged != -1) {
                mEventAdapter.notifyItemChanged(positionChanged);
            }


        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(REQUEST_EVENT == 1){
            //
        }
    }

    private class EventHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private Event mEvent;

        public TextView mTitleTextView;
        public TextView mDateTextView;
        public CheckBox mSolvedCheckBox;

        public EventHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            mTitleTextView = itemView.findViewById(R.id.list_item_event_title);
            mDateTextView = itemView.findViewById(R.id.list_item_event_date);
            mSolvedCheckBox = itemView.findViewById(R.id.list_item_event_solved);
        }

        public void bindEvent(Event event){
            mEvent = event;
            mTitleTextView.setText(event.getTitle());
            mDateTextView.setText(event.getDate().toString());
            mSolvedCheckBox.setChecked(mEvent.isSolved());
        }

        @Override
        public void onClick(View v) {
            //Toast.makeText(getActivity(),mEvent.getTitle() + " clicked!",Toast.LENGTH_SHORT).show();

            //System.out.println(this);
            //System.out.println(getActivity());

            Intent intent = EventPagerActivity.newIntent(getActivity(), mEvent.getId());
           // startActivity(intent);
            startActivityForResult(intent,REQUEST_EVENT);
        }
    }

    private class EventAdapter extends RecyclerView.Adapter<EventHolder>{

        private List<Event> mEvents;

        public EventAdapter(List<Event> mEvents) {
            this.mEvents = mEvents;
        }

        @Override
        public EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.list_item_event, parent, false);
            return new EventHolder(view);
        }

        @Override
        public void onBindViewHolder(EventHolder holder, int position) {
            Event event = mEvents.get(position);
            holder.bindEvent(event);
            positionChanged = position;
        }

        @Override
        public int getItemCount() {
            return mEvents.size();
        }
    }
}