package ohm.softa.a08.tests;

import ohm.softa.a08.Service.OpenMensaAPIService;
import ohm.softa.a08.api.OpenMensaAPI;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OpenMensaAPITests {

	private static final Logger logger = LogManager.getLogger(OpenMensaAPITests.class);
	private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

	private OpenMensaAPI api;

	@BeforeEach
	void setUp() {
		OpenMensaAPIService apiService = OpenMensaAPIService.getInstance();

		api = apiService.getAPI();
	}

	@Test
	void testGetMeals() throws IOException {
		/* create a call to get all meals of the current day */
		var mealsCall = api.getMeals(dateFormat.format(new Date()));
//		mealsCall = api.getMeals("08-05-2023");

		/* execute the call synchronous */
		var mealsResponse = mealsCall.execute();

		/* unwrap the response */
		var meals = mealsResponse.body();

		assertNotNull(meals);
		assertNotEquals(0, meals.size());

		/* display the results in the log of the test */
		for(var m : meals){
			logger.info(m.toString());
		}
	}
}
