package ru.practicum.shareit.item;

public class CommentMapper {
    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .item(comment.getItem())
                .author(comment.getAuthor())
                .build();
    }

    public static Comment fromCommentDto(CommentDto commentDto) {
        Comment comment = new Comment();

        comment.setText(comment.getText());
        comment.setItem(commentDto.getItem());
        comment.setAuthor(commentDto.getAuthor());
        return comment;
    }
}
