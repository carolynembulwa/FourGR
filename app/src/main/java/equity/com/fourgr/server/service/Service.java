package equity.com.fourgr.server.service;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface Service {
    @Multipart
    @POST("/")
    Call<ResponseBody> postImages(@Part MultipartBody.Part image, @Part MultipartBody.Part image2, @Part("name") RequestBody name);
}
