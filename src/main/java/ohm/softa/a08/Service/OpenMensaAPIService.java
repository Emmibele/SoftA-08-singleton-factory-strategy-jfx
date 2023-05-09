package ohm.softa.a08.Service;

import ohm.softa.a08.api.OpenMensaAPI;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OpenMensaAPIService
{
	private final OpenMensaAPI openMensaAPI;
	private static final OpenMensaAPIService openMensaAPIService = new OpenMensaAPIService();

	private OpenMensaAPIService(){
		Retrofit retrofit = new Retrofit.Builder()
			.addConverterFactory(GsonConverterFactory.create())
			.baseUrl("https://openmensa.org/api/v2/")
			.build();

		openMensaAPI = retrofit.create(OpenMensaAPI.class);
	}

	public static OpenMensaAPIService getInstance(){
		return openMensaAPIService;
	}

	public OpenMensaAPI getAPI(){
		return openMensaAPI;
	}
}
