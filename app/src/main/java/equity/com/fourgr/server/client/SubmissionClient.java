package equity.com.fourgr.server.client;


import equity.com.fourgr.server.model.Response;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;



public interface SubmissionClient {
    @Multipart
    @POST("API/addSubmission")
    Call<Response> createSubmission(@PartMap Map<String, RequestBody> submission, @Part MultipartBody.Part image, @Part MultipartBody.Part image2);

    @Multipart
    @POST("API/Submission/add")
    Call<ResponseBody> uploadSubmission(@PartMap Map<String, RequestBody> submission, @Part List<MultipartBody.Part> photos);
}
