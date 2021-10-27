package libs;

public interface Tokenizer {
    String checkNext();
    String getNext();
    boolean checkLast(String regexp);
    boolean checkToken(String regexp);
    String getAndCheckNext(String regexp);
    boolean moreTokens();
    boolean hasTokenFromCurrent(String regexp);


}
