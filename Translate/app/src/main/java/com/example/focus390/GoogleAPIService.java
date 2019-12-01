import java.lang.String;

public interface GoogleAPIService {
    @GET("https://serpapi.com/playground?engine=google&q={search_text}&tbm=isch&ijn=0")
    Single<ImageResult> getImageData(@Path("search_text") String search_text);
}