package com.son.comicreader.Interface;

import com.son.comicreader.Model.Comic;

import java.util.List;

public interface IComicLoadDone {
    void onComicLoadDoneListener(List<Comic> comicList);
}
