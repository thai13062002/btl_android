import requests
from bs4 import BeautifulSoup
import os

def download_images_from_url(url, save_directory):
    # Gửi yêu cầu HTTP để lấy nội dung của trang web
    response = requests.get(url)
    
    # Kiểm tra trạng thái yêu cầu
    if response.status_code == 200:
        # Tạo đối tượng BeautifulSoup để phân tích cú pháp HTML
        soup = BeautifulSoup(response.content, 'html.parser')
        
        # Tìm tất cả các thẻ <img> trong trang web
        img_tags = soup.find_all('img')
        print(img_tags)
        
        # Duyệt qua từng thẻ <img> và tải xuống ảnh
        for img_tag in img_tags:
            # Lấy đường dẫn ảnh từ thuộc tính 'src' của thẻ <img>
            img_url = img_tag.get('src')
            
            # Kiểm tra xem đường dẫn ảnh có đầy đủ hay không
            if img_url.startswith('http'):
                # Gửi yêu cầu HTTP để tải xuống ảnh
                response = requests.get(img_url)
                
                # Kiểm tra trạng thái yêu cầu
                if response.status_code == 200:
                    # Lấy tên tệp tin từ URL
                    file_name = img_url.split('/')[-1]
                    
                    # Tạo đường dẫn lưu trữ tệp tin
                    save_path = os.path.join(save_directory, file_name)
                    
                    # Lưu ảnh vào tệp tin
                    with open(save_path, 'wb') as file:
                        file.write(response.content)
                    
                    print(f"Đã tải xuống ảnh thành công: {file_name}")
    else:
        print("Lỗi: Không thể truy cập vào trang web.")

# Ví dụ sử dụng hàm download_images_from_url
url = "https://icdn.dantri.com.vn/thumb_w/1020/2023/06/27/thu-tuong-1687865433225.jpg"  # URL của trang web chứa các ảnh
save_directory = "D:/thai/phat_trien_ung_dung_thiet_bi_di_dong/btl_android/comic"  # Đường dẫn thư mục lưu trữ tệp tin

download_images_from_url(url, save_directory)
print('ok')
