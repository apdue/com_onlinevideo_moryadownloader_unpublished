package com.video.victusadownloaders.WatchVideo.download.ui.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.video.victusadownloaders.WatchVideo.download.util.StreamSizeWrapper;
import com.video.victusadownloaders.WatchVideo.download.util.Utility;

import org.schabi.newpipe.extractor.MediaFormat;
import org.schabi.newpipe.extractor.stream.AudioStream;
import org.schabi.newpipe.extractor.stream.Stream;
import org.schabi.newpipe.extractor.stream.VideoStream;

import java.util.List;

import io.awesome.gagtube.R;

import com.video.victusadownloaders.WatchVideo.util.SecondaryStreamHelper;

public class StreamItemAdapter<T extends Stream, U extends Stream> extends RecyclerView.Adapter<StreamItemAdapter<T, U>.SingleViewHolder> {

    public interface Listener {
        void onItemSelected(int position);
    }

    private final Context context;
    private final StreamSizeWrapper<T> streamsWrapper;
    private final SparseArray<SecondaryStreamHelper<U>> secondaryStreams;
    // if checkedPosition = -1, there is no default selection
    // if checkedPosition = 0, 1st item is selected by default
    private int checkedPosition = -1;
    private final Listener listener;

    public StreamItemAdapter(Context context, StreamSizeWrapper<T> streamsWrapper, SparseArray<SecondaryStreamHelper<U>> secondaryStreams, int defaultResolution, Listener listener) {
        this.context = context;
        this.streamsWrapper = streamsWrapper;
        this.secondaryStreams = secondaryStreams;
        this.checkedPosition = defaultResolution;
        this.listener = listener;
    }

    public StreamItemAdapter(Context context, StreamSizeWrapper<T> streamsWrapper, int defaultResolution, Listener listener) {
        this(context, streamsWrapper, null, defaultResolution, listener);
    }

    public List<T> getAll() {
        return streamsWrapper.getStreamsList();
    }

    public SparseArray<SecondaryStreamHelper<U>> getAllSecondary() {
        return secondaryStreams;
    }

    public T getItem(int position) {
        return streamsWrapper.getStreamsList().get(position);
    }

    @NonNull
    @Override
    public SingleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.stream_quality_item2, viewGroup, false);
        return new SingleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleViewHolder singleViewHolder, int position) {
        singleViewHolder.bind(getItem(position), position, listener);
    }

    @Override
    public int getItemCount() {
        return streamsWrapper.getStreamsList().size();
    }

    public T getSelected() {
        if (checkedPosition != -1) {
            return streamsWrapper.getStreamsList().get(checkedPosition);
        }
        return null;
    }

    public void clearSelection() {
        this.checkedPosition = -1;
        notifyDataSetChanged();
    }

    class SingleViewHolder extends RecyclerView.ViewHolder {

        final RadioButton radioButton;
        final TextView formatNameView;
        final TextView qualityView;
        final TextView sizeView;

        SingleViewHolder(@NonNull View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.radioButton);
            formatNameView = itemView.findViewById(R.id.stream_format_name);
            qualityView = itemView.findViewById(R.id.stream_quality);
            sizeView = itemView.findViewById(R.id.stream_size);
        }

        public void bind(final T stream, int position, Listener listener) {
            String qualityString;

            if (stream instanceof VideoStream) {
                VideoStream videoStream = ((VideoStream) stream);
                qualityString = videoStream.getResolution();
            } else if (stream instanceof AudioStream) {
                AudioStream audioStream = ((AudioStream) stream);
                qualityString = audioStream.getAverageBitrate() > 0 ? audioStream.getAverageBitrate() + "K" : audioStream.getFormat().getName();
            } else {
                qualityString = stream.getFormat().getSuffix();
            }
            qualityView.setText(qualityString);

            if (streamsWrapper.getSizeInBytes(position) > 0) {
                SecondaryStreamHelper secondary = secondaryStreams == null ? null : secondaryStreams.get(position);
                if (secondary != null) {
                    long size = secondary.getSizeInBytes() + streamsWrapper.getSizeInBytes(position);
                    sizeView.setText(Utility.formatBytes(size));
                } else {
                    sizeView.setText(streamsWrapper.getFormattedSize(position));
                }
                sizeView.setVisibility(View.VISIBLE);
            } else {
                sizeView.setVisibility(View.GONE);
            }

            if (stream.getFormat() == MediaFormat.WEBMA_OPUS) {
                formatNameView.setText("opus");
            } else {
                formatNameView.setText(stream.getFormat().getName());
            }

            radioButton.setChecked(checkedPosition == getBindingAdapterPosition());

            View.OnClickListener clickListener = v -> {
                checkedPosition = getBindingAdapterPosition();
                notifyDataSetChanged();
            };
            itemView.setOnClickListener(clickListener);
            radioButton.setOnClickListener(clickListener);
        }
    }
}
