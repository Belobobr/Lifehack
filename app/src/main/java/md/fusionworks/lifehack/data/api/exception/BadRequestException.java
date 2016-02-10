package md.fusionworks.lifehack.data.api.exception;

/**
 * Created by ungvas on 1/14/16.
 */
public class BadRequestException extends Exception {

    int responseCode;

    public BadRequestException(String detailMessage, int responseCode) {
        super(detailMessage);
        this.responseCode = responseCode;
    }

    public int getResponseCode() {
        return responseCode;
    }
}
