package android.example.com.booklistingapp.models;

public class Book {

    private String name;
    private String author;
    private String imageUrl;

    public Book(String name, String author, String imageId) {
        this.name = name;
        this.author = author;
        this.imageUrl = imageId;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getImageUrl() {
        return imageUrl;
    }

}
