package com.son.comicreader.Model;

import java.util.List;

public class Comic {
    public String Name;
    public String Image;
    public String Category;
    public List<Chapter> Chapter;

    public Comic() {
    }
}

// lưu cấu trúc của comic trong file json

//constructor là một phương thức đặc biệt, nó được dùng để khởi tạo và trả về đối tượng của lớp mà nó được định nghĩa