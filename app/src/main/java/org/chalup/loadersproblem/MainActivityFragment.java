package org.chalup.loadersproblem;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivityFragment extends Fragment implements LoaderCallbacks<String> {
  private static final int SLOW_LOADER_ID = 0;
  private static final int FAST_LOADER_ID = 1;

  private TextView mContent;

  public MainActivityFragment() {
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_main, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    mContent = (TextView) view.findViewById(R.id.text);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    getLoaderManager().initLoader(SLOW_LOADER_ID, null, this);
  }

  private static class SlowLoader extends AbstractLoader<String> {
    public SlowLoader(Context context) {
      super(context);
    }

    @Override
    public String loadInBackground() {
      SystemClock.sleep(1000);

      return "Slow";
    }
  }

  private static class FastLoader extends AbstractLoader<String> {
    public FastLoader(Context context) {
      super(context);
    }

    @Override
    public String loadInBackground() {
      try {
        Log.d("LoaderManager", "Start loadInBackground " + this);

        return "Fast";
      } finally {
        Log.d("LoaderManager", "Finished loadInBackground " + this);
      }
    }
  }

  @Override
  public Loader<String> onCreateLoader(int id, Bundle args) {
    switch (id) {
    case SLOW_LOADER_ID:
      return new SlowLoader(getActivity());
    case FAST_LOADER_ID:
      return new FastLoader(getActivity());
    default:
      throw new IllegalArgumentException("Unknown loader id " + id);
    }
  }

  @Override
  public void onLoadFinished(Loader<String> loader, String data) {
    mContent.setText(mContent.getText() + ", " + data);

    if (loader.getId() == SLOW_LOADER_ID) {
      getLoaderManager().restartLoader(FAST_LOADER_ID, null, this);
    }
  }

  @Override
  public void onLoaderReset(Loader<String> loader) {
  }
}
