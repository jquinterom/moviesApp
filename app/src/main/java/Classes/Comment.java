package Classes;

public class Comment {
    public int getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(int movie_id) {
        this.movie_id = movie_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    private int movie_id;
    private String comment;

    public Comment(int movie_id, String comment) {
        this.movie_id = movie_id;
        this.comment = comment;
    }

    public Comment(){}
}
