package com.video.victusadownloaders.VideoSaver.searchview;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Filter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.video.victusadownloaders.VideoSaver.database.SharedPref;

public class DataHelper {
    private static List<SearchSuggWraper> sColorWrappers = new ArrayList();
    /* access modifiers changed from: private */
    public static List<SearchSugg> sSearchSugg = new ArrayList();

    public interface OnFindSuggestionsListener {
        void onResults(List<SearchSugg> list);
    }

    public static void findSuggestions(Context context, String str, int i, long j, OnFindSuggestionsListener onFindSuggestionsListener) {
        final long j2 = j;
        final Context context2 = context;
        final int i2 = i;
        final OnFindSuggestionsListener onFindSuggestionsListener2 = onFindSuggestionsListener;
        Filter r0 = new Filter() {
            /* access modifiers changed from: protected */
            public FilterResults performFiltering(CharSequence charSequence) {
                try {
                    Thread.sleep(j2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                DataHelper.initSearchSugg(context2);
                ArrayList arrayList = new ArrayList();
                if (charSequence != null && charSequence.length() != 0) {
                    for (SearchSugg searchSugg : DataHelper.sSearchSugg) {
                        if (searchSugg.getQuery().toUpperCase().contains(charSequence.toString().toUpperCase())) {
                            arrayList.add(searchSugg);
                            if (i2 != -1 && arrayList.size() == i2) {
                                break;
                            }
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                Collections.sort(arrayList, new Comparator<SearchSugg>() {
                    public int compare(SearchSugg searchSugg, SearchSugg searchSugg2) {
                        return searchSugg.getIsHistory() ? -1 : 0;
                    }
                });
                filterResults.values = arrayList;
                filterResults.count = arrayList.size();
                return filterResults;
            }

            /* access modifiers changed from: protected */
            public void publishResults(CharSequence charSequence, FilterResults filterResults) {
                OnFindSuggestionsListener onFindSuggestionsListener = onFindSuggestionsListener2;
                if (onFindSuggestionsListener != null) {
                    onFindSuggestionsListener.onResults((List) filterResults.values);
                }
            }
        };
        r0.filter(str);
    }

    /* access modifiers changed from: private */
    public static void initSearchSugg(Context context) {
        List<SearchSugg> list = sSearchSugg;
        if (list != null) {
            list.clear();
            sSearchSugg = new ArrayList();
            sSearchSugg = deserializeSuggs(SharedPref.getInstance(context).getSpString("search_suggestions"));
            if (sSearchSugg == null) {
                sSearchSugg = new ArrayList();
            }
        }
    }

    private static List<SearchSugg> deserializeSuggs(String str) {
        return (List) new Gson().fromJson(str, new TypeToken<List<SearchSugg>>() {
        }.getType());
    }

    public static void saveSearch(Context context, String str) {
        List list;
        Gson gson = new Gson();
        String spString = SharedPref.getInstance(context).getSpString("search_suggestions");
        Type type = new TypeToken<List<SearchSugg>>() {
        }.getType();
        if (spString == null || TextUtils.isEmpty(spString)) {
            list = new ArrayList();
        } else {
            list = (List) gson.fromJson(spString, type);
        }
        if (!list.contains(new SearchSugg(str)) && str != null && str.length() < 70) {
            list.add(new SearchSugg(str));
            SharedPref.getInstance(context).setSpString("search_suggestions", gson.toJson((Object) list, type));
        }
    }
}
