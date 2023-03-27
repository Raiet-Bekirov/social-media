package socialmedia.internal;

public class PostTests {
    public static void main(String[] args) {
        Account acc1 = new Account(1, "user1");
        Account acc2 = new Account(2, "user2");
        Account acc3 = new Account(3, "user3");
        Account acc5 = new Account(5, "user5");

        Post post1 = new Post(1, acc1, "I like examples.");
        Comment com3 = new Comment(3, acc2, post1, "No more than me...");
        post1.addComment(com3);
        Comment com5 = new Comment(5, acc1, com3, "I can prove!");
        com3.addComment(com5);
        Comment com6 = new Comment(6, acc2, com5, "prove it");
        com5.addComment(com6);

        Comment com4 = new Comment(4, acc3, post1, "Can’t you do better than this?");
        post1.addComment(com4);

        Comment com7 = new Comment(7, acc5, post1, "where is the example?");
        post1.addComment(com7);
        Comment com10 = new Comment(105, acc1, com7, "This is the example!");
        com7.addComment(com10);

        System.out.println(post1.showPostDetails(new StringBuilder(), false).toString());
    }
}
